package com.swapnil.dreamtrade.domain.use_case.biometric_auth

import com.swapnil.dreamtrade.domain.model.BiometricResult
import com.swapnil.dreamtrade.domain.repository.BiometricRepository
import kotlinx.coroutines.flow.Flow

class FingerprintAuthUseCase(
    private val biometricRepository: BiometricRepository
) {
    operator fun invoke(title: String, description: String): Flow<BiometricResult> {
        return biometricRepository.fingerprintAuth(title, description)
    }
}