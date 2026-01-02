package com.swapnil.dreamtrade.domain.use_case.saved_crypto_coins

import com.swapnil.dreamtrade.domain.repository.CryptoRepository
import javax.inject.Inject

class IsCoinSavedUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    suspend operator fun invoke(coinId: String): Boolean {
        return try {
            repository.isCoinSaved(coinId) // Returns true/false
        } catch (e: Exception) {
            false // Return false if any error occurs
        }
    }
}
