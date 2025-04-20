package com.example.cleanarchitectureproject.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.cleanarchitectureproject.common.Constants
import com.example.cleanarchitectureproject.data.local.CryptoDatabase
import com.example.cleanarchitectureproject.data.remote.CoinMarketApi
import com.example.cleanarchitectureproject.data.repository.AuthRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.BannerAdRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.CoinMarketRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.CryptoRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.PortfolioRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.RewardedAdRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.TransactionRepositoryImpl
import com.example.cleanarchitectureproject.data.repository.UserDetailsRepositoryImpl
import com.example.cleanarchitectureproject.domain.repository.AuthRepository
import com.example.cleanarchitectureproject.domain.repository.BannerAdRepository
import com.example.cleanarchitectureproject.domain.repository.CoinMarketRepository
import com.example.cleanarchitectureproject.domain.repository.CryptoRepository
import com.example.cleanarchitectureproject.domain.repository.PortfolioRepository
import com.example.cleanarchitectureproject.domain.repository.RewardedAdRepository
import com.example.cleanarchitectureproject.domain.repository.TransactionRepository
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
    fun providePortfolioRepository(db: CryptoDatabase): PortfolioRepository {
        return PortfolioRepositoryImpl(db.portfolioDao)
    }

    @Provides
    @Singleton
    fun provideTransactionsRepository(db: CryptoDatabase): TransactionRepository {
        return TransactionRepositoryImpl(db.transactionsDao)
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

    //Admob
    @Provides
    @Singleton
    fun provideRewardedAdRepository(
        @ApplicationContext context: Context
    ): RewardedAdRepository {
        return RewardedAdRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideBannerAdRepository(
        @ApplicationContext context: Context
    ): BannerAdRepository {
        return BannerAdRepositoryImpl()
    }
}