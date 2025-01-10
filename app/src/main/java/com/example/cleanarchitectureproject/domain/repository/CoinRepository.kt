package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.CoinDetailDto
import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.CoinDto

interface CoinRepository {

    suspend fun getCoins(): List<CoinDto>

    suspend fun getCoinById(coinId: String): CoinDetailDto
}