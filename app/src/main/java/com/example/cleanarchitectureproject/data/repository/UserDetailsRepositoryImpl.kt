package com.example.cleanarchitectureproject.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.cleanarchitectureproject.data.local.keystore.UserDetailsSerializer
import com.example.cleanarchitectureproject.domain.model.UserDetails
import com.example.cleanarchitectureproject.domain.repository.UserDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDetailsRepositoryImpl(private val context: Context) : UserDetailsRepository {

    private val Context.dataStore: DataStore<UserDetails> by dataStore(
        fileName = "user-details",
        serializer = UserDetailsSerializer
    )

    override suspend fun saveToken(token: String) {
        context.dataStore.updateData { preferences ->
            preferences.copy(tokens = preferences.tokens + token)
        }
    }

    override fun getTokens(): Flow<List<String>> {
        return context.dataStore.data.map { it.tokens }
    }

    override suspend fun clearTokens() {
        context.dataStore.updateData {
            it.copy(tokens = emptyList())
        }
    }
}