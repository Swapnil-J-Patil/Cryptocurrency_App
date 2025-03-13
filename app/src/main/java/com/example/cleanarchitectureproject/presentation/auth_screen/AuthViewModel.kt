package com.example.cleanarchitectureproject.presentation.auth_screen

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.domain.use_case.firebase_auth.ForgotPasswordUseCase
import com.example.cleanarchitectureproject.domain.use_case.firebase_auth.HandleGoogleSignInResultUseCase
import com.example.cleanarchitectureproject.domain.use_case.firebase_auth.SignInUseCase
import com.example.cleanarchitectureproject.domain.use_case.firebase_auth.SignInWithGoogleOneTapUseCase
import com.example.cleanarchitectureproject.domain.use_case.firebase_auth.SignOutUseCase
import com.example.cleanarchitectureproject.domain.use_case.firebase_auth.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val signInWithGoogleOneTapUseCase: SignInWithGoogleOneTapUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val handleGoogleSignInResultUseCase: HandleGoogleSignInResultUseCase,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val authState: StateFlow<AuthState> = _authState

    private val _forgotPasswordState = MutableStateFlow<Result<Unit>?>(null)
    val forgotPasswordState: StateFlow<Result<Unit>?> = _forgotPasswordState

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            signUpUseCase(email, password).collect { result ->
                result.onSuccess { authResult ->
                    authResult.user?.let { user ->
                        _authState.value = AuthState.SignedIn(user)
                    } ?: run {
                        _authState.value = AuthState.Error("Unknown error occurred")
                    }
                }.onFailure { exception ->
                    _authState.value = AuthState.Error(exception.localizedMessage ?: "Sign-up failed")
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            signInUseCase(email, password).collect { result ->
                result.onSuccess { authResult ->
                    authResult.user?.let { user ->
                        _authState.value = AuthState.SignedIn(user)
                    } ?: run {
                        _authState.value = AuthState.Error("Unknown error occurred")
                    }
                }.onFailure { exception ->
                    _authState.value = AuthState.Error(exception.localizedMessage ?: "Sign-in failed")
                }
            }
        }
    }

    fun signInWithGoogle(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>) {
        signInWithGoogleOneTapUseCase(context, launcher)
    }

    fun handleSignInResult(data: Intent?) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            handleGoogleSignInResultUseCase(data).collect { result ->
                result.onSuccess { user ->
                    _authState.value = AuthState.SignedIn(user)
                }.onFailure { exception ->
                    _authState.value = AuthState.Error(exception.localizedMessage ?: "Google Sign-in failed")
                }
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _forgotPasswordState.value = forgotPasswordUseCase(email)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
            _authState.value = AuthState.SignedOut
        }
    }
    fun getUserName(): String? {
        val currentUser = (_authState.value as? AuthState.SignedIn)?.user
        return currentUser?.displayName
    }
    fun getUserEmail(): String? {
        val currentUser = (_authState.value as? AuthState.SignedIn)?.user
        return currentUser?.email
    }

}


