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
    operator fun invoke(): Flow<Resource<Flow<List<CryptoCoin>>>> = flow {
        try {
            emit(Resource.Loading<Flow<List<CryptoCoin>>>())  // Emit loading state
            val cryptoList = repository.getAllCrypto() // Fetch from DB
            emit(Resource.Success<Flow<List<CryptoCoin>>>(cryptoList)) // Emit success state
        } catch (e: Exception) {
            emit(Resource.Error<Flow<List<CryptoCoin>>>("Failed to load crypto: ${e.message}"))
        }
    }
}

