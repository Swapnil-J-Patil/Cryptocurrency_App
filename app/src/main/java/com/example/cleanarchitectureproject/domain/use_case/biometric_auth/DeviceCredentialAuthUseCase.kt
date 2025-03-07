package com.example.cleanarchitectureproject.domain.use_case.biometric_auth

import com.example.cleanarchitectureproject.domain.model.BiometricResult
import com.example.cleanarchitectureproject.domain.repository.BiometricRepository
import kotlinx.coroutines.flow.Flow

class DeviceCredentialAuthUseCase(
    private val biometricRepository: BiometricRepository
) {
    operator fun invoke(title: String, description: String): Flow<BiometricResult> {
        return biometricRepository.deviceCredentialAuth(title, description)
    }
}