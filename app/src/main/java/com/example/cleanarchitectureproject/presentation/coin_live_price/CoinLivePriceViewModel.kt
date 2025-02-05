package com.example.cleanarchitectureproject.presentation.coin_live_price

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinLivePriceViewModel @Inject constructor(
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,
) : ViewModel() {

    private val _livePriceState = mutableStateOf(CoinLivePriceState())
    val livePriceState: State<CoinLivePriceState> = _livePriceState

    init {
        getCoinStats()
    }

    private fun getCoinStats() {
        getCurrencyStatsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _livePriceState.value = CoinLivePriceState(cryptocurrency = result.data)
                    Log.d("cryptoCurrency", "getCoin: ${result.data}")
                }

                is Resource.Error -> {
                    _livePriceState.value = CoinLivePriceState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    _livePriceState.value = CoinLivePriceState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}