package com.example.cleanarchitectureproject.di

import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.data.remote.CoinPaprikaAPI
import com.example.cleanarchitectureproject.data.repository.CoinRepositoryImpl
import com.example.cleanarchitectureproject.domain.repository.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //these dependencies will leave as long as application
object AppModule {

    @Provides
    @Singleton
    fun providePaprikaApi(): CoinPaprikaAPI{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinPaprikaAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaAPI):CoinRepository{
        return CoinRepositoryImpl(api)
    }
}