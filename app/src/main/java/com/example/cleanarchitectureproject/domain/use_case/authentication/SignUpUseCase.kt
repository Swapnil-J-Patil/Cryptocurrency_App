package com.example.cleanarchitectureproject.domain.use_case.authentication

import com.example.cleanarchitectureproject.domain.repository.AuthRepository
import javax.inject.Inject


class SignUpUseCase @Inject constructor
    (private val repository: AuthRepository)
{
    suspend operator fun invoke(email: String, password: String) = repository.signUp(email, password)
}