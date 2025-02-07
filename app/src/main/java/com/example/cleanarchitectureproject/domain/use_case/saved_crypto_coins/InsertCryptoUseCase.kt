package com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins

import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import javax.inject.Inject

class InsertCryptoUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    suspend operator fun invoke(crypto: CryptoCoin) {
        repository.insertCrypto(crypto)
    }
}
