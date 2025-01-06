package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.data.remote.dto.CoinDetailDto
import com.example.cleanarchitectureproject.data.remote.dto.CoinDto
import com.example.cleanarchitectureproject.domain.model.Coin

interface CoinRepository {

    suspend fun getCoins(): List<CoinDto>

    suspend fun getCoinById(coinId: String): CoinDetailDto
}