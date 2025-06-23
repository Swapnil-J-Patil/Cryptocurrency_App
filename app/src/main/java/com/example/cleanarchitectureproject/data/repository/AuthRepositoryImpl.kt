package com.example.cleanarchitectureproject.data.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.Keep
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
import com.example.cleanarchitectureproject.BuildConfig

@Keep
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    override suspend fun signUp(email: String, password: String): Flow<Result<AuthResult>> = flow {
        Log.d("AuthRepository", "Attempting sign-up with email: $email")
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("AuthRepository", "Sign-up successful: ${result.user?.uid}")
            emit(Result.success(result))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Sign-up failed", e)
            emit(Result.failure(e))
        }
    }

    override suspend fun signIn(email: String, password: String): Flow<Result<AuthResult>> = flow {
        Log.d("AuthRepository", "Attempting sign-in with email: $email")
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.d("AuthRepository", "Sign-in successful: ${result.user?.uid}")
            emit(Result.success(result))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Sign-in failed", e)
            emit(Result.failure(e))
        }
    }

    override suspend fun signOut() {
        Log.d("AuthRepository", "Signing out...")
        auth.signOut()
        Log.d("AuthRepository", "Signed out successfully")
    }

    override fun signInWithGoogle(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>) {
        Log.d("GoogleSignIn", "Starting One Tap sign-in flow...")
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
                Log.d("GoogleSignIn", "One Tap prompt launched")
                launcher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
            }
            .addOnFailureListener { e ->
                Log.e("GoogleSignIn", "One Tap sign-in failed", e)
            }
    }

    override fun handleSignInResult(data: Intent?): Flow<Result<FirebaseUser>> = flow {
        Log.d("GoogleSignIn", "Handling sign-in result...")
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val googleIdToken = credential?.googleIdToken

            Log.d("GoogleSignIn", "Received ID token: ${googleIdToken != null}")

            if (googleIdToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                Log.d("GoogleSignIn", "Signing in to Firebase with Google credential...")
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                Log.d("GoogleSignIn", "Firebase sign-in successful: ${authResult.user?.uid}")
                emit(Result.success(authResult.user!!))
            } else {
                Log.e("GoogleSignIn", "No Google ID Token received")
                emit(Result.failure(Exception("No Google ID Token received")))
            }
        } catch (e: FirebaseAuthException) {
            Log.e("GoogleSignIn", "Firebase Auth error: ${e.errorCode}", e)
            emit(Result.failure(e))
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Sign-in result handling failed", e)
            emit(Result.failure(e))
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        Log.d("AuthRepository", "Sending password reset email to: $email")
        return try {
            auth.sendPasswordResetEmail(email).await()
            Log.d("AuthRepository", "Password reset email sent")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to send password reset email", e)
            Result.failure(e)
        }
    }
}


