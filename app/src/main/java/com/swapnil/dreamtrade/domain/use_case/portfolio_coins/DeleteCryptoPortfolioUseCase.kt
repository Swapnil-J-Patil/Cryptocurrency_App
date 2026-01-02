package com.swapnil.dreamtrade.domain.use_case.portfolio_coins

import com.swapnil.dreamtrade.common.Resource
import com.swapnil.dreamtrade.domain.model.PortfolioCoin
import com.swapnil.dreamtrade.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class DeleteCryptoPortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(crypto: PortfolioCoin): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading<Unit>()) // Emit loading state
            repository.deleteCoin(crypto)
            emit(Resource.Success<Unit>(Unit)) // Emit success
        } catch (e: Exception) {
            emit(Resource.Error<Unit>("Failed to delete crypto: ${e.message}")) // Emit error state
        }
    }
}
