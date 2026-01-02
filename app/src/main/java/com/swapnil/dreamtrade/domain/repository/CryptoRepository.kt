package com.swapnil.dreamtrade.domain.repository

import com.swapnil.dreamtrade.domain.model.CryptoCoin
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun getAllCrypto(): Flow<List<CryptoCoin>>
    suspend fun insertCrypto(crypto: CryptoCoin)
    suspend fun clearCrypto()
    suspend fun isCoinSaved(coinId: String): Boolean
    suspend fun deleteCoin(coin: CryptoCoin)
}
