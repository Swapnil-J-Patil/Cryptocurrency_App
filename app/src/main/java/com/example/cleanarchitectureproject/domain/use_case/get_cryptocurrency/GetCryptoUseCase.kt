package com.example.cleanarchitectureproject.domain.use_case.get_cryptocurrency

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.data.remote.dto.coinlore.CryptocurrencyCLDTO
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCryptoUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator fun invoke(): Flow<Resource<CryptocurrencyCLDTO>> = flow {
        try {
            emit(Resource.Loading<CryptocurrencyCLDTO>())
            val cryptoCurrency = repository.getCurrency()
            emit(Resource.Success<CryptocurrencyCLDTO>(cryptoCurrency))
        } catch(e: HttpException) {
            emit(Resource.Error<CryptocurrencyCLDTO>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<CryptocurrencyCLDTO>("Couldn't reach server. Check your internet connection."))
        }
    }
}