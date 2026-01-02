package com.swapnil.dreamtrade.presentation.coin_live_price

import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptocurrencyCMDTO

data class CoinLivePriceState (
    val isLoading: Boolean =false,
    val cryptocurrency: CryptocurrencyCMDTO?= null,
    val error: String = ""
)