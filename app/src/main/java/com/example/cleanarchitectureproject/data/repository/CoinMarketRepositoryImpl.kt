package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.remote.CoinMarketApi
import com.example.cleanarchitectureproject.data.remote.CoinPaprikaApi
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.CoinDetailDto
import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.CoinDto
import com.example.cleanarchitectureproject.domain.repository.CoinMarketRepository
import com.example.cleanarchitectureproject.domain.repository.CoinRepository
import javax.inject.Inject

class CoinMarketRepositoryImpl @Inject constructor(
    private val api: CoinMarketApi
) : CoinMarketRepository {

    override suspend fun getCryptoCurrency(): CryptocurrencyCMDTO {
        return api.getCryptoCurrency()
    }
}