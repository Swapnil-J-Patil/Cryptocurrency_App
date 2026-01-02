package com.swapnil.dreamtrade.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserDetailsRepository {
    suspend fun saveToken(token: String)
    fun getTokens(): Flow<List<String>>
    suspend fun clearTokens()
}