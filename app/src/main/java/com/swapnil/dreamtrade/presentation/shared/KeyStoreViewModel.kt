package com.swapnil.dreamtrade.presentation.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.dreamtrade.domain.use_case.keystore.ClearTokensUseCase
import com.swapnil.dreamtrade.domain.use_case.keystore.GetTokensUseCase
import com.swapnil.dreamtrade.domain.use_case.keystore.SaveTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class KeyStoreViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getTokensUseCase: GetTokensUseCase,
    private val clearTokensUseCase: ClearTokensUseCase
) : ViewModel() {

    val tokens: StateFlow<List<String>> = getTokensUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveToken(token: String) {
        viewModelScope.launch {
            saveTokenUseCase(token)
        }
    }

    fun clearTokens() {
        viewModelScope.launch {
            clearTokensUseCase()
        }
    }
}