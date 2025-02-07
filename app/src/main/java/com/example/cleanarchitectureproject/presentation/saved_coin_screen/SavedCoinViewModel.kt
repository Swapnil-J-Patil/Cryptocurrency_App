package com.example.cleanarchitectureproject.presentation.saved_coin_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.data.local.CryptoCoinEntity
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.DeleteCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.GetAllCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.InsertCryptoUseCase
import com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins.IsCoinSavedUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SavedCoinViewModel @Inject constructor(
    private val getAllCryptoUseCase: GetAllCryptoUseCase,
    private val insertCryptoUseCase: InsertCryptoUseCase,
    private val deleteCryptoUseCase: DeleteCryptoUseCase,
    private val isCoinSavedUseCase: IsCoinSavedUseCase,

    ) : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoCoin>>(emptyList())
    val cryptoList = _cryptoList.asStateFlow()

    init {
        loadCrypto()
    }

    private fun loadCrypto() {
        viewModelScope.launch {
            getAllCryptoUseCase().collect {
                _cryptoList.value = it
            }
        }
    }

    fun addCrypto(crypto: CryptoCoin) {
        viewModelScope.launch {
            insertCryptoUseCase(crypto)
        }
    }
    fun isCoinSaved(coinId: String): StateFlow<Boolean> = isCoinSavedUseCase(coinId)
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun removeCrypto(coin: CryptoCoin) {
        viewModelScope.launch {
            deleteCryptoUseCase(coin)
        }
    }
}