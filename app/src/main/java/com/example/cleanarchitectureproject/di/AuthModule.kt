package com.example.cleanarchitectureproject.di

import android.content.Context
import com.example.cleanarchitectureproject.data.repository.AuthRepositoryImpl
import com.example.cleanarchitectureproject.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, @ApplicationContext context: Context): AuthRepository {
        return AuthRepositoryImpl(auth, context)
    }
}
