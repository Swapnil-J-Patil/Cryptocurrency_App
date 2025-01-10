package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.CoinDetailDto
import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.CoinDto

interface CoinMarketRepository {

    suspend fun getCryptoCurrency(): CryptocurrencyCMDTO

}