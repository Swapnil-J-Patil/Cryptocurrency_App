package com.example.cleanarchitectureproject.presentation.coin_detail

import com.example.cleanarchitectureproject.data.remote.dto.coinlore.CryptocurrencyCLDTO

data class CryptoCurrencyState(
    val isLoading: Boolean =false,
    val cryptocurrency: CryptocurrencyCLDTO?= null,
    val error: String = ""
)
