package com.example.cleanarchitectureproject.presentation.coin_list

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cleanarchitectureproject.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CoinListState())
    val state: State<CoinListState> = _state

    private var hasFetchedCoins = false // Add a flag

    init {
        getCoins()
    }

    private fun getCoins() {
        if (hasFetchedCoins) return // Prevent multiple calls
        hasFetchedCoins = true

        getCoinsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = CoinListState(coins = result.data ?: emptyList())
                    Log.d("coinsFetched", "In viewmodel: ${state.value}")
                }
                is Resource.Error -> {
                    _state.value = CoinListState(error = result.message ?: "An unexpected error occurred!")
                    Log.d("coinsFetched", "In viewmodel: ${state.value}")
                }
                is Resource.Loading -> {
                    _state.value = CoinListState(isLoading = true)
                    Log.d("coinsFetched", "In viewmodel: ${state.value}")
                }
            }
        }.launchIn(viewModelScope)
    }
}
