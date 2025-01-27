package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.remote.CoinMarketApi
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import com.example.cleanarchitectureproject.domain.repository.CoinMarketRepository
import javax.inject.Inject

class CoinMarketRepositoryImpl @Inject constructor(
    private val api: CoinMarketApi
) : CoinMarketRepository {

    override suspend fun getCryptoCurrency(): CryptocurrencyCMDTO {
        return api.getCryptoCurrency()
    }
}