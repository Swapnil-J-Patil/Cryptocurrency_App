package com.swapnil.dreamtrade.data.local.transactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(crypto: TransactionsEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransaction(transactionId: Int)

    @Query("DELETE FROM transactions")
    suspend fun clearAllTransactions()
}
