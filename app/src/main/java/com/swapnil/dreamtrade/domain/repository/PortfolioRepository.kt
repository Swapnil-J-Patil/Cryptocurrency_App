package com.swapnil.dreamtrade.domain.repository

import com.swapnil.dreamtrade.domain.model.PortfolioCoin
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    fun getAllCrypto(): Flow<List<PortfolioCoin>>
    suspend fun insertCrypto(crypto: PortfolioCoin)
    suspend fun clearCrypto()
    suspend fun isCoinSaved(coinId: String): Boolean
    suspend fun deleteCoin(coin: PortfolioCoin)
    fun getCoin(coinId: String): Flow<PortfolioCoin>
}