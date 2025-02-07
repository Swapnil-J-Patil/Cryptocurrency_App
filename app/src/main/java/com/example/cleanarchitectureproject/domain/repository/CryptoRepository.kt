package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.data.local.CryptoCoinEntity
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun getAllCrypto(): Flow<List<CryptoCoin>>
    suspend fun insertCrypto(crypto: CryptoCoin)
    suspend fun clearCrypto()
    fun isCoinSaved(coinId: String): Flow<Boolean>
    suspend fun deleteCoin(coin: CryptoCoin)

}
