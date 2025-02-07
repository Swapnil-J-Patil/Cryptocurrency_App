package com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins

import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCryptoUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator fun invoke(): Flow<List<CryptoCoin>> = repository.getAllCrypto()
}
