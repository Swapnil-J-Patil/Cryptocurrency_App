package com.example.cleanarchitectureproject.presentation.coin_live_price

import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO

data class CoinLivePriceState (
    val isLoading: Boolean =false,
    val cryptocurrency: CryptocurrencyCMDTO?= null,
    val error: String = ""
)