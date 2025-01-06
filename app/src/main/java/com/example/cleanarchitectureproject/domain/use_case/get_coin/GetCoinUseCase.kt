package com.example.cleanarchitectureproject.domain.use_case.get_coin

import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.data.remote.dto.toCoin
import com.example.cleanarchitectureproject.data.remote.dto.toCoinDetail
import com.example.cleanarchitectureproject.domain.model.Coin
import com.example.cleanarchitectureproject.domain.model.CoinDetail
import com.example.cleanarchitectureproject.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading<CoinDetail>())
            val coin = repository.getCoinById(coinId).toCoinDetail()
            emit(Resource.Success<CoinDetail>(coin))
        } catch(e: HttpException) {
            emit(Resource.Error<CoinDetail>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<CoinDetail>("Couldn't reach server. Check your internet connection."))
        }
    }
}