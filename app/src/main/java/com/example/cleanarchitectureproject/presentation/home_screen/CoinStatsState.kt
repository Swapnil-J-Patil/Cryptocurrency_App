package com.example.cleanarchitectureproject.presentation.home_screen

import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO

data class CoinStatsState (
    val isLoading: Boolean =false,
    val cryptocurrency: CryptocurrencyCMDTO?= null,
    val error: String = ""
)