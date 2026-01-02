package com.swapnil.dreamtrade.presentation.home_screen

import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptocurrencyCMDTO

data class CoinStatsState (
    val isLoading: Boolean =false,
    val cryptocurrency: CryptocurrencyCMDTO?= null,
    val error: String = ""
)