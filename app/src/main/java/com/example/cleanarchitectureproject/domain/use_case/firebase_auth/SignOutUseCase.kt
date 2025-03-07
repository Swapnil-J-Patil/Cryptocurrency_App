package com.example.cleanarchitectureproject.domain.use_case.firebase_auth

import com.example.cleanarchitectureproject.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor
    (private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.signOut()
}