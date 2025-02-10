package com.example.cleanarchitectureproject.presentation.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.DeleteCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.GetAllCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.InsertCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.IsCoinSavedUseCase
import com.example.cleanarchitectureproject.presentation.saved_coin_screen.SavedCoinsState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SavedCoinViewModel @Inject constructor(
    private val getAllCryptoUseCase: GetAllCryptoUseCase,
    private val insertCryptoUseCase: InsertCryptoUseCase,
    private val deleteCryptoUseCase: DeleteCryptoUseCase,
    private val isCoinSavedUseCase: IsCoinSavedUseCase,

    ) : ViewModel() {

    private val _coinListState = MutableStateFlow(SavedCoinsState())
    val coinListState = _coinListState.asStateFlow()

    init {
        loadCrypto()
    }

    private fun loadCrypto() {
        viewModelScope.launch {
            getAllCryptoUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _coinListState.value = SavedCoinsState(isLoading = true)
                        Log.d("SavedCoinViewModel", "Loading data...")
                    }

                    is Resource.Success -> {
                        _coinListState.value = SavedCoinsState(cryptocurrency = result.data)
                        Log.d("SavedCoinViewModel", "Successfully loaded: ${result.data.toString()}")
                    }

                    is Resource.Error -> {
                        _coinListState.value = SavedCoinsState(error = result.message ?: "Unknown error")
                        Log.e("SavedCoinViewModel", "Error loading data: ${result.message}")
                    }
                }
            }
        }
    }

    fun addCrypto(crypto: CryptoCoin) {
        viewModelScope.launch {
            insertCryptoUseCase(crypto).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("SavedCoinViewModel", "Inserting crypto...")
                    is Resource.Success -> Log.d("SavedCoinViewModel", "Successfully inserted: ${crypto.name}")
                    is Resource.Error -> Log.e("SavedCoinViewModel", "Error inserting crypto: ${result.message}")
                }
            }
        }
    }


    suspend fun isCoinSaved(coinId: String): Boolean {
        return isCoinSavedUseCase(coinId)
    }


    fun removeCrypto(coin: CryptoCoin) {
        viewModelScope.launch {
            deleteCryptoUseCase(coin).collect { result ->
                when (result) {
                    is Resource.Loading -> Log.d("SavedCoinViewModel", "Deleting crypto...")
                    is Resource.Success -> Log.d("SavedCoinViewModel", "Successfully deleted: ${coin.name}")
                    is Resource.Error -> Log.e("SavedCoinViewModel", "Error deleting crypto: ${result.message}")
                }
            }
        }
    }

}