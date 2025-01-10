package com.example.cleanarchitectureproject.di

import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.data.remote.CoinLoreApi
import com.example.cleanarchitectureproject.data.remote.CoinMarketApi
import com.example.cleanarchitectureproject.data.remote.CoinPaprikaApi
import com.example.cleanarchitectureproject.data.repository.CoinMarketRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.CoinRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.CryptoRepositoryImpl
import com.example.cleanarchitectureproject.domain.repository.CoinMarketRepository
import com.example.cleanarchitectureproject.domain.repository.CoinRepository
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
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

    //For CoinPaprika Api
    @Provides
    @Singleton
    fun providePaprikaApi(): CoinPaprikaApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_COIN_PAPRIKA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinPaprikaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }

    //For CoinLore Api
    @Provides
    @Singleton
    fun provideCryptoCurrencyApi(): CoinLoreApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_COIN_LORE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinLoreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCryptoRepository(api: CoinLoreApi): CryptoRepository {
        return CryptoRepositoryImpl(api)
    }

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