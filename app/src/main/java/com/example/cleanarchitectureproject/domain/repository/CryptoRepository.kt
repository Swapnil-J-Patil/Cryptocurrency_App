package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.domain.model.Cryptocurrency

interface CryptoRepository {
    suspend fun getCurrency(): Cryptocurrency

}