package com.swapnil.dreamtrade.presentation.market_screen

import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptocurrencyCMDTO

data class CoinListState (
    val isLoading: Boolean =false,
    val cryptocurrency: CryptocurrencyCMDTO?= null,
    val error: String = ""
)