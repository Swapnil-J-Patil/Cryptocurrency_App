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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
}