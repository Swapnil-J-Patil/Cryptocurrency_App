package com.example.cleanarchitectureproject.presentation.shared

import android.content.Context

class PrefsManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun setOnboardingCompleted(value: Boolean) {
        sharedPreferences.edit().putBoolean("onboarding_completed", value).apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean("onboarding_completed", false)
    }
}
