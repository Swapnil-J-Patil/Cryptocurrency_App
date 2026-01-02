package com.swapnil.dreamtrade.domain.repository

import com.swapnil.dreamtrade.domain.model.BiometricResult
import kotlinx.coroutines.flow.Flow

interface BiometricRepository {
    fun fingerprintAuth(title: String, description: String): Flow<BiometricResult>
    fun deviceCredentialAuth(title: String, description: String): Flow<BiometricResult>
}