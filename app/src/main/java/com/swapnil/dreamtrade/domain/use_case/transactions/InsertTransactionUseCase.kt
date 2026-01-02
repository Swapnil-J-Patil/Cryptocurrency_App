package com.swapnil.dreamtrade.domain.use_case.transactions

import com.swapnil.dreamtrade.common.Resource
import com.swapnil.dreamtrade.domain.model.TransactionData
import com.swapnil.dreamtrade.domain.repository.TransactionRepository
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

