package com.example.cleanarchitectureproject.data.remote

import com.example.cleanarchitectureproject.data.remote.dto.coinlore.CryptocurrencyCLDTO
import retrofit2.http.GET

interface CoinLoreApi{
    @GET("tickers/")
    suspend fun getCurrency(): CryptocurrencyCLDTO
}