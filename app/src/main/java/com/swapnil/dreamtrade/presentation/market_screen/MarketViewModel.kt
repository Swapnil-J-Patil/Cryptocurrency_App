package com.swapnil.dreamtrade.presentation.market_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.dreamtrade.common.Resource
import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.swapnil.dreamtrade.domain.model.CryptocurrencyCoin
import com.swapnil.dreamtrade.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.swapnil.dreamtrade.presentation.ui.theme.green
import com.swapnil.dreamtrade.presentation.ui.theme.lightRed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,
) : ViewModel() {

    private val _coinListState = mutableStateOf(CoinListState())
    val coinListState: State<CoinListState> = _coinListState

    private val _currencyList = MutableStateFlow<List<CryptocurrencyCoin>>(emptyList())
    private val currencyList: StateFlow<List<CryptocurrencyCoin>> = _currencyList

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var fetchJob: Job? = null

    fun startFetchingCoinStats() {
        fetchJob?.cancel()  // Ensure previous job is canceled before starting a new one
        fetchJob = viewModelScope.launch {
            while (isActive) { // Ensures cancellation if ViewModel is cleared
                getLivePrices()
                delay(5000)
            }
        }
    }

    fun stopFetchingCoinStats() {
        fetchJob?.cancel()
    }

    var filteredCoins = searchQuery
        .combine(currencyList) { query, allCoins ->
            if (query.isBlank()) allCoins
            else allCoins.filter { coin ->
                coin.name.contains(query, ignoreCase = true) ||
                        coin.symbol.contains(query, ignoreCase = true) ||
                        coin.id.toString().contains(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            getCoinStats()
            filteredCoins.collectLatest { coins ->
                //Log.d("FilteredCoinsUpdate", "Filtered Size: ${coins.size}, Coins: $coins")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
       // Log.d("FilteredCoins", "Size: ${filteredCoins.value.size}, List: $filteredCoins")

    }

    private fun getCoinStats() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _coinListState.value = CoinListState(cryptocurrency = result.data)
                   // _coins.value= result.data?.data!!.cryptoCurrencyList
                   // Log.d("cryptoCurrency", "getCoin: ${result.data}")
                    result.data?.data?.let { processCoins(it.cryptoCurrencyList) }
                }

                is Resource.Error -> {
                    _coinListState.value = CoinListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _coinListState.value = CoinListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getLivePrices() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val allCoins = result.data?.data?.cryptoCurrencyList ?: emptyList()

                    // Get the list of IDs from currencyList
                    /*val selectedCoinIds = filteredCoins.value.map { it.id }

                    // Extract only the prices of the selected coins
                    val coins = selectedCoinIds.mapNotNull { id ->
                        allCoins.find { it.id == id }
                    }*/

                    processCoins(allCoins)
                }

                is Resource.Error -> { /* Handle Error */ }

                is Resource.Loading -> { /* Handle Loading */ }
            }
        }.launchIn(viewModelScope)
    }

    private fun processCoins(coins: List<CryptoCurrencyCM>) {
        viewModelScope.launch {
            val cryptocurrencyCoins = coins.map { coin ->
                val dfSmall = DecimalFormat("0.#####") // Up to 6 decimal places
                val dfPercentage = DecimalFormat("#,##0.00") // Ensures percentages are formatted properly
                val firstQuote = coin.quotes.firstOrNull() // Handle missing quotes

                val percentage = firstQuote?.percentChange1h.toString()


                val price = firstQuote!!.price
                val formattedPrice = "$ " + when {
                    price < 0.0001 -> "0.00.."  // Extremely small values
                    price < 100 -> dfSmall.format(price) // Up to 6 decimal places
                    else -> price.toInt().toString().take(3) + ".." // Large numbers (first 3 digits + "..")
                }
                val color = if (coin.quotes[0].percentChange1h > 0.0) green else lightRed

                CryptocurrencyCoin(
                    id = coin.id,
                    name = coin.name,
                    symbol = coin.symbol,
                    slug = coin.slug,
                    tags = coin.tags,
                    cmcRank = coin.cmcRank,
                    marketPairCount = coin.marketPairCount,
                    circulatingSupply = coin.circulatingSupply,
                    selfReportedCirculatingSupply = coin.selfReportedCirculatingSupply,
                    totalSupply = coin.totalSupply,
                    maxSupply = coin.maxSupply,
                    isActive = coin.isActive,
                    lastUpdated = coin.lastUpdated,
                    dateAdded = coin.dateAdded,
                    quotes = coin.quotes,
                    isAudited = coin.isAudited,
                    auditInfoList = coin.auditInfoList ?: emptyList(),
                    badges = coin.badges,
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


}