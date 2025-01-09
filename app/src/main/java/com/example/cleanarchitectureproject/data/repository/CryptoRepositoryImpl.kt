package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.remote.CoinLoreAPI
import com.example.cleanarchitectureproject.domain.model.Cryptocurrency
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val api: CoinLoreAPI
) : CryptoRepository {

    override suspend fun getCurrency(): Cryptocurrency {
        return api.getCurrency()
    }
}