package com.swapnil.dreamtrade.data.repository

import com.swapnil.dreamtrade.domain.model.BiometricResult
import com.swapnil.dreamtrade.domain.repository.BiometricRepository
import com.swapnil.dreamtrade.presentation.auth_screen.components.BiometricPromptManager
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