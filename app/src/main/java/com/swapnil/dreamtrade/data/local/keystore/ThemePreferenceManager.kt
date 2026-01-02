package com.swapnil.dreamtrade.data.local.keystore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

class ThemePreferenceManager @Inject constructor(private val context: Context) {
    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[DARK_MODE_KEY] ?: true
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }
}
