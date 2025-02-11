package com.example.cleanarchitectureproject.domain.use_case.saved_crypto_coins

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllCryptoUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator fun invoke(): Flow<Resource<List<CryptoCoin>>> = flow {
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

