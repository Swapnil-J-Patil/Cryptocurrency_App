package com.example.cleanarchitectureproject.data.local.portfolio

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio")
    fun getAllCrypto(): Flow<List<PortfolioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrypto(crypto: PortfolioEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM portfolio WHERE id = :coinId)")
    suspend fun isCoinSaved(coinId: String): Boolean

    @Query("SELECT * FROM portfolio WHERE id = :coinId")
    fun getCoin(coinId: String): Flow<PortfolioEntity?>

    @Delete
    suspend fun deleteCoin(coin: PortfolioEntity)

    @Query("DELETE FROM portfolio")
    suspend fun clearCrypto()
}