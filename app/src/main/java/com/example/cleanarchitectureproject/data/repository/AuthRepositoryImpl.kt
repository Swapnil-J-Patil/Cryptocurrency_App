package com.example.cleanarchitectureproject.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.example.cleanarchitectureproject.BuildConfig
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    override suspend fun signUp(email: String, password: String): Flow<Result<AuthResult>> = flow {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun signIn(email: String, password: String): Flow<Result<AuthResult>> = flow {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override fun signInWithGoogle(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>) {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                launcher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
            }
            .addOnFailureListener { e ->
                Log.e("GoogleSignIn", "Sign-in failed", e)
            }
    }

    override fun handleSignInResult(data: Intent?): Flow<Result<FirebaseUser>> = flow {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val googleIdToken = credential?.googleIdToken
            if (googleIdToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                emit(Result.success(authResult.user!!))
            } else {
                emit(Result.failure(Exception("No Google ID Token received")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

