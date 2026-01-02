package com.swapnil.dreamtrade.domain.repository

import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptocurrencyCMDTO

interface CoinMarketRepository {

    suspend fun getCryptoCurrency(): CryptocurrencyCMDTO

}