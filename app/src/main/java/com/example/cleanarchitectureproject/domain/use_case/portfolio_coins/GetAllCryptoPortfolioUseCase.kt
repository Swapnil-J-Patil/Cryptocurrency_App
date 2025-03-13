package com.example.cleanarchitectureproject.domain.use_case.portfolio_coins

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllCryptoPortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    operator fun invoke(): Flow<Resource<List<PortfolioCoin>>> = flow {
        try {
            emit(Resource.Loading())  // Emit loading state
            repository.getAllCrypto().collect { cryptoList ->
                emit(Resource.Success(cryptoList)) // Emit success state with actual data
            }
        } catch (e: Exception) {
            emit(Resource.Error("Failed to load crypto: ${e.message}"))
        }
    }
}

