package com.example.cleanarchitectureproject.domain.use_case.keystore

import com.example.cleanarchitectureproject.domain.repository.UserDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTokensUseCase @Inject constructor(
    private val repository: UserDetailsRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return repository.getTokens()
    }
}