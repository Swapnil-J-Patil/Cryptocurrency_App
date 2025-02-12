package com.example.cleanarchitectureproject.presentation.market_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM
import com.example.cleanarchitectureproject.domain.model.CryptocurrencyCoin
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    init {
        viewModelScope.launch {
            getCoinStats()
            filteredCoins.collectLatest { coins ->
                Log.d("FilteredCoinsUpdate", "Filtered Size: ${coins.size}, Coins: $coins")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        Log.d("FilteredCoins", "Size: ${filteredCoins.value.size}, List: $filteredCoins")

    }

    private fun getCoinStats() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _coinListState.value = CoinListState(cryptocurrency = result.data)
                   // _coins.value= result.data?.data!!.cryptoCurrencyList
                    Log.d("cryptoCurrency", "getCoin: ${result.data}")
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

    private fun processCoins(coins: List<CryptoCurrencyCM>) {
        viewModelScope.launch {
            val cryptocurrencyCoins = coins.map { coin ->
                val firstQuote = coin.quotes.firstOrNull() // Handle missing quotes

                val percentage = firstQuote?.percentChange1h.toString()


                val price = firstQuote!!.price
                val formattedPrice = "$ " + if (price < 1000) price.toString().take(5) else price.toString().take(3) + ".."
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