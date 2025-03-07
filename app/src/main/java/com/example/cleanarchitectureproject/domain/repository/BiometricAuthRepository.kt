package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.domain.model.BiometricResult
import kotlinx.coroutines.flow.Flow

interface BiometricRepository {
    fun authenticateWithBiometrics(title: String, description: String): Flow<BiometricResult>
    fun authenticateWithDeviceCredential(title: String, description: String): Flow<BiometricResult>
}