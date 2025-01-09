package com.example.cleanarchitectureproject.presentation.coin_detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cleanarchitectureproject.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.use_case.get_coin.GetCoinUseCase
import com.example.cleanarchitectureproject.domain.use_case.get_cryptocurrency.GetCryptoUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    private val getCryptoUseCase: GetCryptoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state

    private val _cryptoState = mutableStateOf(CryptoCurrencyState())
    val cryptoState: State<CryptoCurrencyState> = _cryptoState

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            getCoin(coinId)
        }
        getCurrency()
    }

    private fun getCoin(coinId: String) {
        getCoinUseCase(coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = CoinDetailState(coin = result.data)
                    Log.d("cryptoCurrency", "getCoin: ${result.data}")
                }
                is Resource.Error -> {
                    _state.value = CoinDetailState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = CoinDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getCurrency() {
        getCryptoUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _cryptoState.value = CryptoCurrencyState(cryptocurrency = result.data)
                    Log.d("cryptoCurrency", "getCoin: ${result.data}")
                }
                is Resource.Error -> {
                    _cryptoState.value = CryptoCurrencyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _cryptoState.value = CryptoCurrencyState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}