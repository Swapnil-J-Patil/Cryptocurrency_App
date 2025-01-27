package com.example.cleanarchitectureproject.data.remote

import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import retrofit2.http.GET

interface CoinMarketApi{
    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    suspend fun getCryptoCurrency(): CryptocurrencyCMDTO
}