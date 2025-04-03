package com.example.cleanarchitectureproject.domain.use_case.transactions

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.model.TransactionData
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
import com.example.cleanarchitectureproject.domain.repository.TransactionRepository
import javax.inject.Inject

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: TransactionData): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading<Unit>()) // Emit loading state
            repository.insertTransaction(transaction)
            emit(Resource.Success<Unit>(Unit)) // Emit success state
        } catch (e: Exception) {
            emit(Resource.Error<Unit>("Failed to insert transaction: ${e.message}")) // Emit error state
        }
    }
}

