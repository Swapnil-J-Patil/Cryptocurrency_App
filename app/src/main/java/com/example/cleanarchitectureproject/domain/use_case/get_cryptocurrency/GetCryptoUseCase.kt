package com.example.cleanarchitectureproject.domain.use_case.get_cryptocurrency

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.data.remote.dto.toCoin
import com.example.cleanarchitectureproject.domain.model.Coin
import com.example.cleanarchitectureproject.domain.model.Cryptocurrency
import com.example.cleanarchitectureproject.domain.repository.CoinRepository
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCryptoUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator fun invoke(): Flow<Resource<Cryptocurrency>> = flow {
        try {
            emit(Resource.Loading<Cryptocurrency>())
            val cryptoCurrency = repository.getCurrency()
            emit(Resource.Success<Cryptocurrency>(cryptoCurrency))
        } catch(e: HttpException) {
            emit(Resource.Error<Cryptocurrency>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<Cryptocurrency>("Couldn't reach server. Check your internet connection."))
        }
    }
}