package com.example.cleanarchitectureproject.presentation.coin_detail

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    savedStateHandle: SavedStateHandle  //bundle: contains information about state
) : ViewModel() {
    //Used to maintain the state

    private val _state =
        mutableStateOf(CoinDetailState())  //private : To avoid modifying the list during compositions
    val state: State<CoinDetailState> = _state

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let {coinId->
            getCoin(coinId)
        }
    }
    private fun getCoin(coinId: String) {
        getCoinUseCase(coinId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                  _state.value= CoinDetailState(coin = result.data)
                }

                is Resource.Error -> {
                    _state.value =
                        CoinDetailState(error = result.message ?: "An Unexpected error occurred!")
                }

                is Resource.Loading -> {
                    _state.value = CoinDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}