package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.remote.CoinLoreApi
import com.example.cleanarchitectureproject.data.remote.dto.coinlore.CryptocurrencyCLDTO
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val api: CoinLoreApi
) : CryptoRepository {

    override suspend fun getCurrency(): CryptocurrencyCLDTO {
        return api.getCurrency()
    }
}