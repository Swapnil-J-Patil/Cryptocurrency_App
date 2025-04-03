package com.example.cleanarchitectureproject.domain.repository

import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.model.TransactionData
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<TransactionData>>
    suspend fun insertTransaction(transaction: TransactionData)
    suspend fun clearAllTransactions()
    suspend fun deleteTransaction(id: Int)
}