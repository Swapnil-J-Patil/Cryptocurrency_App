package com.example.cleanarchitectureproject.domain.use_case.transactions

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import com.example.cleanarchitectureproject.domain.repository.TransactionRepository
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
