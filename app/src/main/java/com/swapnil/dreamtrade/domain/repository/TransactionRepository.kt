package com.swapnil.dreamtrade.domain.repository

import com.swapnil.dreamtrade.domain.model.TransactionData
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<TransactionData>>
    suspend fun insertTransaction(transaction: TransactionData)
    suspend fun clearAllTransactions()
    suspend fun deleteTransaction(id: Int)
}