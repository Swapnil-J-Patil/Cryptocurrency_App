package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.domain.model.BiometricResult
import com.example.cleanarchitectureproject.domain.repository.BiometricRepository
import com.example.cleanarchitectureproject.presentation.auth_screen.components.BiometricPromptManager
import kotlinx.coroutines.flow.Flow

class BiometricRepositoryImpl(
    private val biometricPromptManager: BiometricPromptManager
) : BiometricRepository {

    override fun fingerprintAuth(title: String, description: String): Flow<BiometricResult> {
        biometricPromptManager.showFingerprintPrompt(title, description)
        return biometricPromptManager.promptResults
    }

    override fun deviceCredentialAuth(title: String, description: String): Flow<BiometricResult> {
        biometricPromptManager.showDeviceCredentialPrompt(title, description)
        return biometricPromptManager.promptResults
    }
}