package com.example.cleanarchitectureproject.domain.use_case.portfolio_coins

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
import javax.inject.Inject

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertCryptoPortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(crypto: PortfolioCoin): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading<Unit>()) // Emit loading state
            repository.insertCrypto(crypto)
            emit(Resource.Success<Unit>(Unit)) // Emit success state
        } catch (e: Exception) {
            emit(Resource.Error<Unit>("Failed to insert crypto: ${e.message}")) // Emit error state
        }
    }
}

