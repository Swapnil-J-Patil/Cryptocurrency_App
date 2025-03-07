package com.example.cleanarchitectureproject.presentation.auth_screen

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.domain.use_case.authentication.HandleGoogleSignInResultUseCase
import com.example.cleanarchitectureproject.domain.use_case.authentication.SignInUseCase
import com.example.cleanarchitectureproject.domain.use_case.authentication.SignInWithGoogleOneTapUseCase
import com.example.cleanarchitectureproject.domain.use_case.authentication.SignOutUseCase
import com.example.cleanarchitectureproject.domain.use_case.authentication.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val signInWithGoogleOneTapUseCase: SignInWithGoogleOneTapUseCase,
    private val handleGoogleSignInResultUseCase: HandleGoogleSignInResultUseCase,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val authState: StateFlow<AuthState> = _authState

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

    fun signInWithGoogleOneTap(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>) {
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

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
            _authState.value = AuthState.SignedOut
        }
    }
}


