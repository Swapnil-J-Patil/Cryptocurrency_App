package com.swapnil.dreamtrade.domain.use_case.transactions

import com.swapnil.dreamtrade.common.Resource
import com.swapnil.dreamtrade.domain.model.TransactionData
import com.swapnil.dreamtrade.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<Resource<List<TransactionData>>> = flow {
        try {
            emit(Resource.Loading())  // Emit loading state
            repository.getAllTransactions()
                .collect { transactions ->
                emit(Resource.Success(transactions)) // Emit success state with actual data
            }
        } catch (e: Exception) {
            emit(Resource.Error("Failed to load crypto: ${e.message}"))
        }
    }
}

