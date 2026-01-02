package com.swapnil.dreamtrade.data.remote

import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import retrofit2.http.GET

interface CoinMarketApi{
    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=1000")
    suspend fun getCryptoCurrency(): CryptocurrencyCMDTO
}