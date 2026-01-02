package com.swapnil.dreamtrade.domain.use_case.firebase_auth

import com.swapnil.dreamtrade.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        return authRepository.sendPasswordResetEmail(email)
    }
}
