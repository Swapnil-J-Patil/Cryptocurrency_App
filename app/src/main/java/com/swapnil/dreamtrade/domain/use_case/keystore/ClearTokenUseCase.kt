package com.swapnil.dreamtrade.domain.use_case.keystore

import com.swapnil.dreamtrade.domain.repository.UserDetailsRepository
import javax.inject.Inject

class ClearTokensUseCase @Inject constructor(
    private val repository: UserDetailsRepository
) {
    suspend operator fun invoke() {
        repository.clearTokens()
    }
}