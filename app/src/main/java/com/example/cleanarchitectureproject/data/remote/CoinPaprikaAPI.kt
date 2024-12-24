package com.example.cleanarchitectureproject.data.remote

import com.example.cleanarchitectureproject.data.remote.dto.CoinDetailDto
import com.example.cleanarchitectureproject.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinPaprikaAPI {

    @GET("/v1/coins")
    suspend fun getCoins():List<CoinDto>

    @GET("/v1/coins/{coinId}")
    suspend fun getCoinById(
        @Path("coinId") coinId: String
    ): CoinDetailDto
}