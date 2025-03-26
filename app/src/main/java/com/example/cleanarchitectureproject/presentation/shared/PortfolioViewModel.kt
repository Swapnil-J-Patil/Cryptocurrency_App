package com.example.cleanarchitectureproject.presentation.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoDataCM
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM
import com.example.cleanarchitectureproject.domain.model.CryptocurrencyCoin
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.example.cleanarchitectureproject.domain.use_case.portfolio_coins.DeleteCryptoPortfolioUseCase
import com.example.cleanarchitectureproject.domain.use_case.portfolio_coins.GetAllCryptoPortfolioUseCase
import com.example.cleanarchitectureproject.domain.use_case.portfolio_coins.GetCoinPortfolioUseCase
import com.example.cleanarchitectureproject.domain.use_case.portfolio_coins.InsertCryptoPortfolioUseCase
import com.example.cleanarchitectureproject.domain.use_case.portfolio_coins.IsCoinSavedPortfolioUseCase
import com.example.cleanarchitectureproject.presentation.home_screen.CoinStatsState
import com.example.cleanarchitectureproject.presentation.shared.state.PortfolioCoinState
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getAllCryptoUseCase: GetAllCryptoPortfolioUseCase,
    private val getCoinUseCase: GetCoinPortfolioUseCase,
    private val insertCryptoUseCase: InsertCryptoPortfolioUseCase,
    private val deleteCryptoUseCase: DeleteCryptoPortfolioUseCase,
    private val isCoinSavedUseCase: IsCoinSavedPortfolioUseCase,
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,

    ) : ViewModel() {

    private val _coinListState = MutableLiveData(PortfolioCoinState())
    val coinListState: LiveData<PortfolioCoinState> = _coinListState

    private val _statsState = mutableStateOf(CoinStatsState())
    val statsState: State<CoinStatsState> = _statsState

    private val _currencyList = MutableLiveData<List<PortfolioCoin>>(emptyList())
    val currencyList: LiveData<List<PortfolioCoin>> = _currencyList

    private val _filteredCurrencyList = MutableLiveData<List<Double>>(emptyList())
    val filteredCurrencyList: LiveData<List<Double>> = _filteredCurrencyList

    val portfolioValue= MutableLiveData(0.0)
    val portfolioPercentage= MutableLiveData(0.0)


    init {
        loadCrypto()
    }
    private fun loadCrypto() {
        viewModelScope.launch {
            getAllCryptoUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _coinListState.value = PortfolioCoinState(isLoading = true)
                        Log.d("portfolioViewmodel", "Loading data...")
                    }

                    is Resource.Success -> {
                        portfolioValue.value=0.0
                        portfolioValue.value=0.0
                        _coinListState.value = PortfolioCoinState(cryptocurrency = result.data)
                        getCoinStats()
                        result.data?.let {
                            processCoins(it) }
                        Log.d("portfolioViewmodel", "Successfully loaded: ${result.data}")
                    }

                    is Resource.Error -> {
                        _coinListState.value = PortfolioCoinState(error = result.message ?: "Unknown error")
                        Log.e("portfolioViewmodel", "Error loading data: ${result.message}")
                    }
                }
            }
        }
    }

    private fun getCoinStats() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val allCoins = result.data?.data?.cryptoCurrencyList ?: emptyList()

                    // Get the list of IDs from currencyList
                    val selectedCoinIds = _coinListState.value?.cryptocurrency?.map { it.id } ?: emptyList()

                    // Extract only the prices of the selected coins
                    val filteredPrices = selectedCoinIds.mapNotNull { id ->
                        allCoins.find { it.id == id }?.quotes?.firstOrNull()?.price
                    }

                    // Update state with ordered filtered prices
                    _filteredCurrencyList.value = filteredPrices
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

    fun getSavedCoinQuantity(coinId: String, onResult: (Double?) -> Unit) {
        viewModelScope.launch {
            val isSaved = isCoinSaved(coinId)
            if (isSaved) {
                getCoinUseCase(coinId).collect { result ->
                    when (result) {
                        is Resource.Success -> onResult(result.data?.quantity)
                        else -> onResult(null)
                    }
                }
            } else {
                onResult(null)
            }
        }
    }


    fun addCrypto(crypto: PortfolioCoin) {
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

    fun removeCrypto(coin: PortfolioCoin) {
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

    private fun processCoins(coins: List<PortfolioCoin>) {
        viewModelScope.launch {
            filteredCurrencyList.observeForever { filteredPrices ->
                val cryptocurrencyCoins = coins.mapIndexed { index,coin ->
                    Log.d("portfolioViewmodel", "coins : ${coins.size} and ${coins}")

                    val firstQuote = coin.quotes?.firstOrNull() // Handle missing quotes


                    val percentage = firstQuote!!.percentChange1h.toString()
                    val livePrice = filteredPrices.getOrNull(index) ?: firstQuote?.price ?: 0.0

                    val currentPrice = livePrice * (coin.quantity ?: 0.0) //need to change this later to get live price
                    val purchasedPrice = (coin.purchasedAt?.toDouble() ?: 0.0) * (coin.quantity ?: 0.0)

                    val percentageChange = (currentPrice - purchasedPrice) / purchasedPrice * 100


                    portfolioValue.value = portfolioValue.value?.plus(currentPrice)
                    portfolioPercentage.value = portfolioPercentage.value?.plus(percentageChange)

                    val color = if (currentPrice - purchasedPrice > 0) green else lightRed

                    PortfolioCoin(
                        id = coin.id,
                        name = coin.name,
                        symbol = coin.symbol.toString(),
                        slug = coin.slug.toString(),
                        tags = coin.tags ?: emptyList(),
                        cmcRank = coin.cmcRank ?: 0,
                        marketPairCount = coin.marketPairCount ?: 0,
                        circulatingSupply = coin.circulatingSupply ?: 0.0,
                        selfReportedCirculatingSupply = coin.selfReportedCirculatingSupply ?: 0.0,
                        totalSupply = coin.totalSupply ?: 0.0,
                        maxSupply = coin.maxSupply,
                        isActive = coin.isActive ?: 0,
                        lastUpdated = coin.lastUpdated.toString(),
                        dateAdded = coin.dateAdded.toString(),
                        quotes = coin.quotes,
                        isAudited = coin.isAudited ?: false,
                        badges = coin.badges ?: emptyList(),
                        purchasedAt = purchasedPrice,
                        currentPrice = currentPrice,
                        logo = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png",
                        graph = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coin.id}.png",
                        color = color,
                        isGainer = percentageChange > 0,
                        quantity = coin.quantity
                    )
                }

                _currencyList.value = cryptocurrencyCoins
            }
        }
    }

}