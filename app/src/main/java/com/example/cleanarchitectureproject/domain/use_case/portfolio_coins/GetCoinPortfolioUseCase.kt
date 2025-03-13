package com.example.cleanarchitectureproject.domain.use_case.portfolio_coins

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCoinPortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<PortfolioCoin?>> = flow {
        try {
            emit(Resource.Loading())  // Emit loading state
            repository.getCoin(coinId).collect { coin ->
                emit(Resource.Success(coin)) // Emit success state with actual data
            }
        } catch (e: Exception) {
            emit(Resource.Error("Failed to load crypto: ${e.message}"))
        }
    }
}