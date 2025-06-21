package com.example.cleanarchitectureproject.presentation.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.DeleteCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.GetAllCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.InsertCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.IsCoinSavedUseCase
import com.example.cleanarchitectureproject.presentation.shared.state.SavedCoinsState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM
import com.example.cleanarchitectureproject.domain.model.CryptocurrencyCoin
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.example.cleanarchitectureproject.presentation.home_screen.CoinStatsState
import com.example.cleanarchitectureproject.presentation.shared.state.PortfolioCoinState
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedCoinViewModel @Inject constructor(
    private val getAllCryptoUseCase: GetAllCryptoUseCase,
    private val insertCryptoUseCase: InsertCryptoUseCase,
    private val deleteCryptoUseCase: DeleteCryptoUseCase,
    private val isCoinSavedUseCase: IsCoinSavedUseCase,
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,

    ) : ViewModel() {

    private val _coinListState = mutableStateOf(SavedCoinsState())
    val coinListState: State<SavedCoinsState> = _coinListState

    private val _currencyList = MutableStateFlow<List<CryptocurrencyCoin>>(emptyList())
    private val currencyList: StateFlow<List<CryptocurrencyCoin>> = _currencyList

    private val _coinPrices = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val coinPrices: StateFlow<Map<Int, Double>> = _coinPrices


    private val _statsState = mutableStateOf(CoinStatsState())
    val statsState: State<CoinStatsState> = _statsState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var fetchJob: Job? = null

    val filteredCoins = searchQuery
        .combine(currencyList) { query, allCoins ->
            if (query.isBlank()) allCoins
            else allCoins.filter { coin ->
                coin.name.contains(query, ignoreCase = true) ||
                        coin.symbol.contains(query, ignoreCase = true) ||
                        coin.id.toString().contains(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        //Log.d("FilteredCoins", "Size: ${filteredCoins.value.size}, List: $filteredCoins")

    }
    init {
        loadCrypto()
    }

    fun startFetchingCoinStats(id:Int) {
        fetchJob?.cancel()  // Ensure previous job is canceled before starting a new one
        fetchJob = viewModelScope.launch {
            while (isActive) { // Ensures cancellation if ViewModel is cleared
                getCoinStats(id)
                delay(2000)
            }
        }
    }

    fun stopFetchingCoinStats() {
        fetchJob?.cancel()
    }

    fun getCoinStats(id: Int) {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val allCoins = result.data?.data?.cryptoCurrencyList ?: emptyList()
                    val filteredPrice = allCoins.find { it.id == id }?.quotes?.firstOrNull()?.price
                    filteredPrice?.let { price ->
                        _coinPrices.update { currentMap ->
                            currentMap + (id to price)
                        }
                    }
                }

                is Resource.Error -> {
                    _statsState.value = CoinStatsState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _statsState.value = CoinStatsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun loadCrypto() {
        viewModelScope.launch {
            getAllCryptoUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _coinListState.value = SavedCoinsState(isLoading = true)
                        Log.d("SavedCoinViewModel", "Loading data...")
                    }

                    is Resource.Success -> {
                        _coinListState.value = SavedCoinsState(cryptocurrency = result.data)
                        result.data?.let { processCoins(it) }
                        //Log.d("SavedCoinViewModel", "Successfully loaded: ${result.data}")
                    }

                    is Resource.Error -> {
                        _coinListState.value = SavedCoinsState(error = result.message ?: "Unknown error")
                        Log.e("SavedCoinViewModel", "Error loading data: ${result.message}")
                    }
                }
            }
        }
    }


    fun addCrypto(crypto: CryptoCoin) {
        viewModelScope.launch {
            insertCryptoUseCase(crypto).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("SavedCoinViewModel", "Inserting crypto...")
                    is Resource.Success -> Log.d("SavedCoinViewModel", "Successfully inserted: ${crypto.name}")
                    is Resource.Error -> Log.e("SavedCoinViewModel", "Error inserting crypto: ${result.message}")
                }
            }
        }
    }

    suspend fun isCoinSaved(coinId: String): Boolean {
        return isCoinSavedUseCase(coinId)
    }

    fun removeCrypto(coin: CryptoCoin) {
        viewModelScope.launch {
            deleteCryptoUseCase(coin).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("SavedCoinViewModel", "Deleting crypto...")
                    is Resource.Success -> Log.d("SavedCoinViewModel", "Successfully deleted: ${coin.name}")
                    is Resource.Error -> Log.e("SavedCoinViewModel", "Error deleting crypto: ${result.message}")
                }
            }
        }
    }

    private fun processCoins(coins: List<CryptoCoin>) {
        viewModelScope.launch {
            val cryptocurrencyCoins = coins.map { coin ->
                val firstQuote = coin.quotes?.firstOrNull() // Handle missing quotes

                val percentage = firstQuote!!.percentChange1h.toString()


                val price = firstQuote.price
                val formattedPrice = "$ " + if (price < 1000) price.toString().take(5) else price.toString().take(3) + ".."
                val color = if (coin.quotes[0].percentChange1h > 0.0) green else lightRed

                CryptocurrencyCoin(
                    id = coin.id,
                    name = coin.name,
                    symbol = coin.symbol.toString(),
                    slug = coin.slug.toString(),
                    tags = coin.tags?: emptyList(),
                    cmcRank = coin.cmcRank?:0,
                    marketPairCount = coin.marketPairCount?:0,
                    circulatingSupply = coin.circulatingSupply?:0.0,
                    selfReportedCirculatingSupply = coin.selfReportedCirculatingSupply?:0.0,
                    totalSupply = coin.totalSupply?:0.0,
                    maxSupply = coin.maxSupply,
                    isActive = coin.isActive?:0,
                    lastUpdated = coin.lastUpdated.toString(),
                    dateAdded = coin.dateAdded.toString(),
                    quotes = coin.quotes,
                    isAudited = coin.isAudited?:false,
                    auditInfoList = emptyList(),
                    badges = coin.badges?: emptyList(),
                    percentage = percentage,
                    price = formattedPrice,
                    logo = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png",
                    graph = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coin.id}.png",
                    color = color,
                    isGainer = firstQuote.percentChange1h > 0
                )
            }

            _currencyList.value = cryptocurrencyCoins
        }
    }

    fun getDynamicThreshold(marketCap: Double): Pair<Double, Double> {
        return when {
            marketCap > 1_000_000_000 -> 1_000_000.0 to 0.02 // Large cap: $1M+ volume, 2% turnover
            marketCap > 100_000_000 -> 100_000.0 to 0.015 // Mid cap: $100K+ volume, 1.5% turnover
            else -> 10_000.0 to 0.01 // Small cap: $10K+ volume, 1% turnover
        }
    }

    fun checkLiquidity(crypto: CryptoCoin): Boolean {
       // Log.d("checkLiquidity", "checkLiquidity is called")
        val quote = crypto.quotes?.firstOrNull() ?: return false // Get the first quote, return false if null
        val marketCap = quote.marketCap // Market cap of the coin

        if (marketCap <= 0) return false // Ensure valid market cap

        val (volumeThreshold, turnoverThreshold) = getDynamicThreshold(marketCap) // Get thresholds dynamically

        return isLiquidityLow(quote, volumeThreshold, turnoverThreshold) // Check if liquidity is low
    }

    fun isLiquidityLow(quote: QuoteCM, volumeThreshold: Double, turnoverThreshold: Double): Boolean {
        return quote.volume24h < volumeThreshold || quote.turnover < turnoverThreshold
    }
    fun formatPrice(value: Double): String {
        val priceStr = value.toBigDecimal().toPlainString() // Avoid scientific notation

        return when {
            priceStr.length >= 10 -> priceStr.substring(0, 10)  // Truncate if too long
            else -> priceStr.padEnd(10, '0')  // Pad with zeros if too short
        }
    }
    fun formatPriceForSavedScreen(value: Double): String {
        val priceStr = value.toBigDecimal().toPlainString() // Avoid scientific notation

        return when {
            priceStr.length >= 10 -> priceStr.substring(0, 5)  // Truncate if too long
            else -> priceStr.padEnd(10, '0')  // Pad with zeros if too short
        }
    }
}