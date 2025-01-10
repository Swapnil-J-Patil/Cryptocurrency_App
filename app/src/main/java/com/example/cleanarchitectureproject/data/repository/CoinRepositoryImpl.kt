package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.remote.CoinPaprikaApi
import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.CoinDetailDto
import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.CoinDto
import com.example.cleanarchitectureproject.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
) : CoinRepository {

    override suspend fun getCoins(): List<CoinDto> {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}