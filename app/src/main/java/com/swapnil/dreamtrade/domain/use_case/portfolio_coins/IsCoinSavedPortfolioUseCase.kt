package com.swapnil.dreamtrade.domain.use_case.portfolio_coins

import com.swapnil.dreamtrade.domain.repository.PortfolioRepository
import javax.inject.Inject

class IsCoinSavedPortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    suspend operator fun invoke(coinId: String): Boolean {
        return try {
            repository.isCoinSaved(coinId) // Returns true/false
        } catch (e: Exception) {
            false // Return false if any error occurs
        }
    }
}
