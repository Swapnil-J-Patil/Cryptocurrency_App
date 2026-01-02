package com.swapnil.dreamtrade.domain.repository

interface LuckyWheelRepository {
    suspend fun saveLastSpinTime(timeMillis: Long)
    suspend fun getLastSpinTime(): Long
}
