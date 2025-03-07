package com.example.cleanarchitectureproject.domain.use_case.firebase_auth

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.example.cleanarchitectureproject.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleOneTapUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>) {
        authRepository.signInWithGoogle(context, launcher)
    }
}
