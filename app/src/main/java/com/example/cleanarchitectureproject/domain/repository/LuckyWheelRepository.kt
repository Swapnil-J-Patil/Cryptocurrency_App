package com.example.cleanarchitectureproject.domain.repository

interface LuckyWheelRepository {
    suspend fun saveLastSpinTime(timeMillis: Long)
    suspend fun getLastSpinTime(): Long
}
