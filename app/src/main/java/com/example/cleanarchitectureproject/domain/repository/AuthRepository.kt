package com.example.cleanarchitectureproject.domain.repository

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Flow<Result<AuthResult>>
    suspend fun signIn(email: String, password: String): Flow<Result<AuthResult>>
    suspend fun signOut()
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    fun signInWithGoogle(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>)
    fun handleSignInResult(data: Intent?): Flow<Result<FirebaseUser>>
}
