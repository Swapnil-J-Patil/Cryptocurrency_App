package com.example.cleanarchitectureproject.presentation.auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.domain.model.BiometricResult
import com.example.cleanarchitectureproject.domain.use_case.biometric_auth.FingerprintAuthUseCase
import com.example.cleanarchitectureproject.domain.use_case.biometric_auth.DeviceCredentialAuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BiometricViewModel(
    private val authenticateBiometricUseCase: FingerprintAuthUseCase,
    private val authenticateWithDeviceCredentialUseCase: DeviceCredentialAuthUseCase

) : ViewModel() {

    private val _biometricState = MutableStateFlow<BiometricResult?>(null)
    val biometricState: StateFlow<BiometricResult?> = _biometricState

    fun fingerprintAuth(title: String, description: String) {
        viewModelScope.launch {
            authenticateBiometricUseCase(title, description).collect { result ->
                _biometricState.value = result
            }
        }
    }
    fun deviceCredentialAuth(title: String, description: String) {
        viewModelScope.launch {
            authenticateWithDeviceCredentialUseCase(title, description).collect { result ->
                _biometricState.value = result
            }
        }
    }
}