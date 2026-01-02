package com.swapnil.dreamtrade.domain.use_case.portfolio_coins

import com.swapnil.dreamtrade.common.Resource
import com.swapnil.dreamtrade.domain.model.PortfolioCoin
import com.swapnil.dreamtrade.domain.repository.PortfolioRepository
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