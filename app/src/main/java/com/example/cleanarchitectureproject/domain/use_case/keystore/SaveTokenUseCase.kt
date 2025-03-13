package com.example.cleanarchitectureproject.domain.use_case.keystore

import com.example.cleanarchitectureproject.domain.repository.UserDetailsRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val repository: UserDetailsRepository
) {
    suspend operator fun invoke(token: String) {
        repository.saveToken(token)
    }
}