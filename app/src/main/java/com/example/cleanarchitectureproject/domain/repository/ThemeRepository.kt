package com.example.cleanarchitectureproject.domain.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getTheme(): Flow<Boolean>
    suspend fun setTheme(isDark: Boolean)
}
