package com.swapnil.dreamtrade.domain.use_case.firebase_auth

import com.swapnil.dreamtrade.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor
    (private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.signIn(email, password)
}