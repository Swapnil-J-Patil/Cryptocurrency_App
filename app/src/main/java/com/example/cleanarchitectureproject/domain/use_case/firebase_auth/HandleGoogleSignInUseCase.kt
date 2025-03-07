package com.example.cleanarchitectureproject.domain.use_case.firebase_auth

import android.content.Intent
import com.example.cleanarchitectureproject.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HandleGoogleSignInResultUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(data: Intent?): Flow<Result<FirebaseUser>> {
        return authRepository.handleSignInResult(data)
    }
}
