package com.swapnil.dreamtrade.data.repository

import com.swapnil.dreamtrade.data.remote.CoinMarketApi
import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import com.swapnil.dreamtrade.domain.repository.CoinMarketRepository
import javax.inject.Inject

class CoinMarketRepositoryImpl @Inject constructor(
    private val api: CoinMarketApi
) : CoinMarketRepository {

    override suspend fun getCryptoCurrency(): CryptocurrencyCMDTO {
        return api.getCryptoCurrency()
    }
}