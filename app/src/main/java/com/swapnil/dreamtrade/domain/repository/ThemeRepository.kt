package com.swapnil.dreamtrade.domain.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getTheme(): Flow<Boolean>
    suspend fun setTheme(isDark: Boolean)
}
