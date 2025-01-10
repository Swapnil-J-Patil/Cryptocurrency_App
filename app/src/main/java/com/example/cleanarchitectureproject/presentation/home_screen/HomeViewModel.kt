package com.example.cleanarchitectureproject.presentation.home_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.use_case.get_coin.GetCoinUseCase
import com.example.cleanarchitectureproject.domain.use_case.get_cryptocurrency.GetCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.example.cleanarchitectureproject.presentation.coin_detail.CoinDetailState
import com.example.cleanarchitectureproject.presentation.coin_detail.CryptoCurrencyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,
) : ViewModel() {

    private val _statsState = mutableStateOf(CoinStatsState())
    val statsState: State<CoinStatsState> = _statsState

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

}