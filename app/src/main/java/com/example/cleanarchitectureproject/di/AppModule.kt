package com.example.cleanarchitectureproject.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.data.local.portfolio.PortfolioDatabase
import com.example.cleanarchitectureproject.data.local.saved_coins.CryptoDatabase
import com.example.cleanarchitectureproject.data.remote.CoinMarketApi
import com.example.cleanarchitectureproject.data.repository.AuthRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.CoinMarketRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.CryptoRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.PortfolioRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.UserDetailsRepositoryImpl
import com.example.cleanarchitectureproject.domain.repository.AuthRepository
import com.example.cleanarchitectureproject.domain.repository.CoinMarketRepository
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
import com.example.cleanarchitectureproject.domain.repository.UserDetailsRepository
import com.example.cleanarchitectureproject.domain.use_case.keystore.ClearTokensUseCase
import com.example.cleanarchitectureproject.domain.use_case.keystore.GetTokensUseCase
import com.example.cleanarchitectureproject.domain.use_case.keystore.SaveTokenUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //CoinMarket Api
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

    //Room Database
    @Provides
    @Singleton
    fun provideDatabase(app: Application): CryptoDatabase {
        return Room.databaseBuilder(
            app,
            CryptoDatabase::class.java,
            "crypto_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCryptoRepository(db: CryptoDatabase): CryptoRepository {
        return CryptoRepositoryImpl(db.cryptoDao)
    }

    @Provides
    @Singleton
    fun providePortfolioDatabase(app: Application): PortfolioDatabase {
        return Room.databaseBuilder(
            app,
            PortfolioDatabase::class.java,
            "portfolio_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePortfolioRepository(db: PortfolioDatabase): PortfolioRepository {
        return PortfolioRepositoryImpl(db.portfolioDao)
    }

    //Keystore
    @Provides
    @Singleton
    fun provideUserDetailsRepository(@ApplicationContext context: Context): UserDetailsRepository {
        return UserDetailsRepositoryImpl(context)
    }

    @Provides
    fun provideSaveTokenUseCase(repository: UserDetailsRepository) = SaveTokenUseCase(repository)

    @Provides
    fun provideGetTokensUseCase(repository: UserDetailsRepository) = GetTokensUseCase(repository)

    @Provides
    fun provideClearTokensUseCase(repository: UserDetailsRepository) = ClearTokensUseCase(repository)

    //Firebase
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, @ApplicationContext context: Context): AuthRepository {
        return AuthRepositoryImpl(auth, context)
    }
}