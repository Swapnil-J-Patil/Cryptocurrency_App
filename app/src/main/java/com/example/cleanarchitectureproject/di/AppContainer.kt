package com.example.cleanarchitectureproject.di

import androidx.appcompat.app.AppCompatActivity
import com.example.cleanarchitectureproject.presentation.auth_screen.util.BiometricPromptManager

class AppContainer(activity: AppCompatActivity) {
    private val biometricDependency = BiometricDependency(activity)

    val biometricPromptManager by lazy {
        BiometricPromptManager(biometricDependency)
    }
}
