package com.example.cleanarchitectureproject.presentation.market_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    private val _coinList = mutableStateOf(CoinListState())
    val coinList: State<CoinListState> = _coinList

    private val _coins = MutableStateFlow<List<CryptoCurrencyCM>>(emptyList())
    val coins: StateFlow<List<CryptoCurrencyCM>> = _coins

    private val _coinPercentageList = MutableStateFlow<List<String>>(emptyList())
    val coinPercentageList: StateFlow<List<String>> = _coinPercentageList

    private val _coinPriceList = MutableStateFlow<List<String>>(emptyList())
    val coinPriceList: StateFlow<List<String>> = _coinPriceList

    private val _coinLogoList = MutableStateFlow<List<String>>(emptyList())
    val coinLogoList: StateFlow<List<String>> = _coinLogoList

    private val _coinGraphList = MutableStateFlow<List<String>>(emptyList())
    val coinGraphList: StateFlow<List<String>> = _coinGraphList

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredCoins = _searchQuery
        .combine(_coins) { query, allCoins ->
            if (query.isBlank()) {
                allCoins
            } else {
                allCoins.filter { coin ->
                    coin.name.startsWith(query, ignoreCase = true) ||
                            coin.symbol.startsWith(query, ignoreCase = true) ||
                            coin.id.toString().startsWith(query, ignoreCase = true)
                }

            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

    }
    init {
        getCoinStats()
    }

    private fun getCoinStats() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _coinList.value = CoinListState(cryptocurrency = result.data)
                    _coins.value= result.data?.data!!.cryptoCurrencyList
                    Log.d("cryptoCurrency", "getCoin: ${result.data}")
                    processCoins(result.data.data.cryptoCurrencyList)
                }

                is Resource.Error -> {
                    _coinList.value = CoinListState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _coinList.value = CoinListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun processCoins(coins: List<CryptoCurrencyCM>) {
        viewModelScope.launch {
            _coinPercentageList.value = coins.map { gainer ->
                val percentage = gainer.quotes[0].percentChange1h.toString()
                if(gainer.quotes[0].percentChange1h > 0)
                {
                    "+" + if (percentage.length > 5) percentage.substring(0, 5) + " %"  else percentage + " %"
                }
                else{
                    if (percentage.length > 5) percentage.substring(0, 6)  + " %" else percentage + " %"
                }
            }

            _coinPriceList.value = coins.map { gainer ->
                val price = gainer.quotes[0].price
                "$ " + if (price < 1000) price.toString().substring(0, 5) else price.toString().substring(0, 3) + ".."
            }

            _coinLogoList.value = coins.map { gainer ->
                "https://s2.coinmarketcap.com/static/img/coins/64x64/${gainer.id}.png"
            }

            _coinGraphList.value = coins.map { gainer ->
                "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${gainer.id}.png"
            }
        }
    }

}