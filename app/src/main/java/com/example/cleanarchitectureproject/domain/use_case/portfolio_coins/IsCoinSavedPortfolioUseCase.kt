package com.example.cleanarchitectureproject.domain.use_case.portfolio_coins

import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
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
