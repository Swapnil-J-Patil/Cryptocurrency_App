package com.swapnil.dreamtrade.domain.use_case.transactions

import com.swapnil.dreamtrade.common.Resource
import com.swapnil.dreamtrade.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class DeleteAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading<Unit>()) // Emit loading state
            repository.clearAllTransactions()
            emit(Resource.Success<Unit>(Unit)) // Emit success
        } catch (e: Exception) {
            emit(Resource.Error<Unit>("Failed to delete transaction: ${e.message}")) // Emit error state
        }
    }
}
