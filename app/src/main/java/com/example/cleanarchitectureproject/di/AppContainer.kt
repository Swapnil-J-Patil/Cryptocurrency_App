package com.example.cleanarchitectureproject.di

import androidx.appcompat.app.AppCompatActivity
import com.example.cleanarchitectureproject.data.repository.BiometricRepositoryImpl
import com.example.cleanarchitectureproject.domain.use_case.biometric_auth.FingerprintAuthUseCase
import com.example.cleanarchitectureproject.domain.use_case.biometric_auth.DeviceCredentialAuthUseCase
import com.example.cleanarchitectureproject.presentation.auth_screen.BiometricViewModel
import com.example.cleanarchitectureproject.presentation.auth_screen.components.BiometricPromptManager

class AppContainer(activity: AppCompatActivity) {
    private val biometricDependency = BiometricDependency(activity)
    private val biometricPromptManager = BiometricPromptManager(biometricDependency)
    private val biometricRepository = BiometricRepositoryImpl(biometricPromptManager)
    private val authenticateBiometricUseCase = FingerprintAuthUseCase(biometricRepository)
    private val authenticateWithDeviceCredentialUseCase = DeviceCredentialAuthUseCase(biometricRepository)
    val biometricViewModel = BiometricViewModel(authenticateBiometricUseCase,authenticateWithDeviceCredentialUseCase)
}