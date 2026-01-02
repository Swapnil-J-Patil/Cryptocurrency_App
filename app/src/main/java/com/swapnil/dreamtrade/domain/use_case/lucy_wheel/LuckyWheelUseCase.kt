package com.swapnil.dreamtrade.domain.use_case.lucy_wheel

import com.swapnil.dreamtrade.domain.repository.LuckyWheelRepository
import javax.inject.Inject

class LuckyWheelUseCase  @Inject constructor(private val repository: LuckyWheelRepository) {
    private val cooldownMillis = 3 * 60 * 60 * 1000L

    suspend fun canSpin(): Pair<Boolean, Long> {
        val lastSpin = repository.getLastSpinTime()
        val elapsed = System.currentTimeMillis() - lastSpin
        val remaining = cooldownMillis - elapsed
        return Pair(remaining <= 0, maxOf(0, remaining))
    }

    suspend fun recordSpinTime() {
        repository.saveLastSpinTime(System.currentTimeMillis())
    }
}
