package com.swapnil.dreamtrade.data.repository

import android.content.Context
import com.swapnil.dreamtrade.domain.repository.LuckyWheelRepository
import javax.inject.Inject

class LuckyWheelRepositoryImpl @Inject constructor(
    private val context: Context
) : LuckyWheelRepository {
    private val prefs = context.getSharedPreferences("cooldown_prefs", Context.MODE_PRIVATE)

    override suspend fun saveLastSpinTime(timeMillis: Long) {
        prefs.edit().putLong("last_spin", timeMillis).apply()
    }

    override suspend fun getLastSpinTime(): Long {
        return prefs.getLong("last_spin", 0L)
    }
}
