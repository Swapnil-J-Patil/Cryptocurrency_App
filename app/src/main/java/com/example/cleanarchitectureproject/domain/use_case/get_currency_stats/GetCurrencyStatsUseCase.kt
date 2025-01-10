package com.example.cleanarchitectureproject.domain.use_case.get_currency_stats

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import com.example.cleanarchitectureproject.domain.repository.CoinMarketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCurrencyStatsUseCase @Inject constructor(
    private val repository: CoinMarketRepository
) {
    operator fun invoke(): Flow<Resource<CryptocurrencyCMDTO>> = flow {
        try {
            emit(Resource.Loading<CryptocurrencyCMDTO>())
            val cryptoCurrency = repository.getCryptoCurrency()
            emit(Resource.Success<CryptocurrencyCMDTO>(cryptoCurrency))
        } catch(e: HttpException) {
            emit(Resource.Error<CryptocurrencyCMDTO>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<CryptocurrencyCMDTO>("Couldn't reach server. Check your internet connection."))
        }
    }
}