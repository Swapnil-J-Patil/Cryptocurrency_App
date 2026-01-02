package com.swapnil.dreamtrade.data.local.shared_prefs

import android.content.Context

class PrefsManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    //Onboarding
    fun setOnboardingCompleted(value: Boolean) {
        sharedPreferences.edit().putBoolean("onboarding_completed", value).apply()
    }

    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean("onboarding_completed", false)
    }

    //Firebase Authentication
    fun setFirebaseAuthCompleted(value: Boolean) {
        sharedPreferences.edit().putBoolean("firebase_auth_completed", value).apply()
    }

    fun isFirebaseAuthCompleted(): Boolean {
        return sharedPreferences.getBoolean("firebase_auth_completed", false)
    }

    //Biometric Authentication
    fun setBiometricAuthCompleted(value: Boolean) {
        sharedPreferences.edit().putBoolean("biometric_auth_completed", value).apply()
    }

    fun isBiometricAuthCompleted(): Boolean {
        return sharedPreferences.getBoolean("biometric_auth_completed", false)
    }

    //Dollars
    fun setDollarAmount(value: String) {
        sharedPreferences.edit().putString("dollars", value).apply()
    }

    fun getDollarAmount(): String? {
        return sharedPreferences.getString("dollars", null)
    }
}
