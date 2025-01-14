package com.example.cleanarchitectureproject.presentation.home_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.use_case.get_coin.GetCoinUseCase
import com.example.cleanarchitectureproject.domain.use_case.get_cryptocurrency.GetCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.example.cleanarchitectureproject.presentation.coin_detail.CoinDetailState
import com.example.cleanarchitectureproject.presentation.coin_detail.CryptoCurrencyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,
) : ViewModel() {

    private val _statsState = mutableStateOf(CoinStatsState())
    val statsState: State<CoinStatsState> = _statsState

    private val _topGainers = MutableStateFlow<List<CryptoCurrencyCM>>(emptyList())
    val topGainers: StateFlow<List<CryptoCurrencyCM>> = _topGainers

    private val _topLosers = MutableStateFlow<List<CryptoCurrencyCM>>(emptyList())
    val topLosers: StateFlow<List<CryptoCurrencyCM>> = _topLosers

    init {
        getCoinStats()
    }

    private fun getCoinStats() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _statsState.value = CoinStatsState(cryptocurrency = result.data)
                    Log.d("cryptoCurrency", "getCoin: ${result.data}")
                }

                is Resource.Error -> {
                    _statsState.value = CoinStatsState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _statsState.value = CoinStatsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getGainers(cryptoCurrencyList: List<CryptoCurrencyCM>) {
        viewModelScope.launch {
            val sortedCryptocurrencies = cryptoCurrencyList?.sortedWith { o1, o2 ->
                // Sort by `percentChange24h` in descending order
                o2.quotes[0].percentChange24h.compareTo(o1.quotes[0].percentChange24h)
            } ?: emptyList()

            // Update state with the sorted list
            _topGainers.value = sortedCryptocurrencies
            Log.d("cryptoCurrency", "Sorted Gainers: $sortedCryptocurrencies")
        }
    }

    fun getLosers(cryptoCurrencyList: List<CryptoCurrencyCM>) {
        viewModelScope.launch {
            val sortedCryptocurrencies = cryptoCurrencyList.sortedWith { o1, o2 ->
                // Sort by `percentChange24h` in ascending order
                o1.quotes[0].percentChange24h.compareTo(o2.quotes[0].percentChange24h)
            }

            // Update state with the sorted list
            _topLosers.value = sortedCryptocurrencies
            Log.d("cryptoCurrency", "Sorted Losers: $sortedCryptocurrencies")
        }
    }

}