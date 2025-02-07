package com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins

import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsCoinSavedUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator suspend fun invoke(coinId: String):Boolean = repository.isCoinSaved(coinId)
}
