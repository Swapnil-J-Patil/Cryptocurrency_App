package com.swapnil.dreamtrade.domain.use_case.keystore

import com.swapnil.dreamtrade.domain.repository.UserDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTokensUseCase @Inject constructor(
    private val repository: UserDetailsRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return repository.getTokens()
    }
}