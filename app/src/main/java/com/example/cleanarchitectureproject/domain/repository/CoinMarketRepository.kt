package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO

interface CoinMarketRepository {

    suspend fun getCryptoCurrency(): CryptocurrencyCMDTO

}