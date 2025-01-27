package com.example.cleanarchitectureproject.di

import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.data.remote.CoinMarketApi
import com.example.cleanarchitectureproject.data.repository.CoinMarketRepositoryImpl
import com.example.cleanarchitectureproject.domain.repository.CoinMarketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //For CoinMarket Api
    @Provides
    @Singleton
    fun provideCoinMarketApi(): CoinMarketApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_COIN_MARKET)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinMarketApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinMarketRepository(api: CoinMarketApi): CoinMarketRepository {
        return CoinMarketRepositoryImpl(api)
    }
}