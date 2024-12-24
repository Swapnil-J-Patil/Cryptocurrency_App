package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.remote.CoinPaprikaAPI
import com.example.cleanarchitectureproject.data.remote.dto.CoinDetailDto
import com.example.cleanarchitectureproject.data.remote.dto.CoinDto
import com.example.cleanarchitectureproject.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaAPI
) : CoinRepository{
    //ctrl +i
    override suspend fun getCoins(): List<CoinDto> {
       return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}