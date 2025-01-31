package com.example.cleanarchitectureproject.presentation.market_screen

import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO

data class CoinListState (
    val isLoading: Boolean =false,
    val cryptocurrency: CryptocurrencyCMDTO?= null,
    val error: String = ""
)