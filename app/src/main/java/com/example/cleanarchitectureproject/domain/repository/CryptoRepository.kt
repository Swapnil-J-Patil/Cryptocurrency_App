package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.data.remote.dto.coinlore.CryptocurrencyCLDTO


interface CryptoRepository {
    suspend fun getCurrency(): CryptocurrencyCLDTO

}