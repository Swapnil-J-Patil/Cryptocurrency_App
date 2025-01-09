package com.example.cleanarchitectureproject.di

import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.data.remote.CoinLoreAPI
import com.example.cleanarchitectureproject.data.remote.CoinPaprikaApi
import com.example.cleanarchitectureproject.data.repository.CoinRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.CryptoRepositoryImpl
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

    @Provides
    @Singleton
    fun providePaprikaApi(): CoinPaprikaApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinPaprikaApi::class.java)
    }
    @Provides
    @Singleton
    fun provideCryptoCurrencyApi(): CoinLoreAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_NEW)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinLoreAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCryptoRepository(api: CoinLoreAPI): CryptoRepository {
        return CryptoRepositoryImpl(api)
    }
}