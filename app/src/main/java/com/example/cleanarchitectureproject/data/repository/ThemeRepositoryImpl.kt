package com.example.cleanarchitectureproject.data.repository

import com.example.cleanarchitectureproject.data.local.keystore.ThemePreferenceManager
import com.example.cleanarchitectureproject.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(private val manager: ThemePreferenceManager) : ThemeRepository {
    override fun getTheme(): Flow<Boolean> = manager.isDarkMode
    override suspend fun setTheme(isDark: Boolean) = manager.setDarkMode(isDark)
}