package com.swapnil.dreamtrade.domain.use_case.saved_crypto_coins

import com.swapnil.dreamtrade.common.Resource
import com.swapnil.dreamtrade.domain.model.CryptoCoin
import com.swapnil.dreamtrade.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class DeleteCryptoUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    suspend operator fun invoke(crypto: CryptoCoin): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading<Unit>()) // Emit loading state
            repository.deleteCoin(crypto)
            emit(Resource.Success<Unit>(Unit)) // Emit success
        } catch (e: Exception) {
            emit(Resource.Error<Unit>("Failed to delete crypto: ${e.message}")) // Emit error state
        }
    }
}
