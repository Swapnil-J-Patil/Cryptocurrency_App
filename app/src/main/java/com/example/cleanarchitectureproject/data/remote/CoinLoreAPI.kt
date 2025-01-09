package com.example.cleanarchitectureproject.data.remote

import com.example.cleanarchitectureproject.domain.model.Cryptocurrency
import retrofit2.http.GET

interface CoinLoreAPI{
    @GET("tickers/")
    suspend fun getCurrency(): Cryptocurrency
}