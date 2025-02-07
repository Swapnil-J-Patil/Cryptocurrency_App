package com.example.cleanarchitectureproject.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {
    @Query("SELECT * FROM saved_coins")
    fun getAllCrypto(): Flow<List<CryptoCoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrypto(crypto: CryptoCoinEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_coins WHERE id = :coinId)")
    fun isCoinSaved(coinId: String): Flow<Boolean>

    @Delete
    suspend fun deleteCoin(coin: CryptoCoinEntity)

    @Query("DELETE FROM saved_coins")
    suspend fun clearCrypto()
}
