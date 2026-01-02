package com.swapnil.dreamtrade.data.repository

import com.swapnil.dreamtrade.data.local.keystore.ThemePreferenceManager
import com.swapnil.dreamtrade.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(private val manager: ThemePreferenceManager) : ThemeRepository {
    override fun getTheme(): Flow<Boolean> = manager.isDarkMode
    override suspend fun setTheme(isDark: Boolean) = manager.setDarkMode(isDark)
}