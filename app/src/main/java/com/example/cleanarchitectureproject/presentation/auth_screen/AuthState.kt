package com.example.cleanarchitectureproject.presentation.auth_screen

sealed class AuthState {
    object SignedOut : AuthState()
    object Loading : AuthState()
    data class SignedIn(val user: com.google.firebase.auth.FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}