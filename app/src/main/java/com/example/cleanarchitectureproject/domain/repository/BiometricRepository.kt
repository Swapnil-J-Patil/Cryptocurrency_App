package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.domain.model.BiometricResult
import kotlinx.coroutines.flow.Flow

interface BiometricRepository {
    fun fingerprintAuth(title: String, description: String): Flow<BiometricResult>
    fun deviceCredentialAuth(title: String, description: String): Flow<BiometricResult>
}