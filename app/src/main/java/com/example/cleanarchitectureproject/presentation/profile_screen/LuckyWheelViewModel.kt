package com.example.cleanarchitectureproject.presentation.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.domain.use_case.lucy_wheel.LuckyWheelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LuckyWheelViewModel @Inject constructor(
    private val useCase: LuckyWheelUseCase
) : ViewModel() {

    private val _canSpin = MutableStateFlow(true)
    val canSpin: StateFlow<Boolean> = _canSpin

    private val _countdown = MutableStateFlow(0L)
    val countdown: StateFlow<Long> = _countdown

    private var timerJob: Job? = null

    init {
        checkCooldown()
    }

    private fun checkCooldown() {
        viewModelScope.launch {
            val (canSpinNow, remaining) = useCase.canSpin()
            _canSpin.value = canSpinNow
            _countdown.value = remaining
            if (!canSpinNow) startCountdown(remaining)
        }
    }

    private fun startCountdown(timeMillis: Long) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = timeMillis
            while (remaining > 0) {
                delay(1000L)
                remaining -= 1000L
                _countdown.value = remaining
            }
            _canSpin.value = true
        }
    }

    fun recordSpin() {
        viewModelScope.launch {
            useCase.recordSpinTime()
            _canSpin.value = false
            _countdown.value = 3 * 60 * 60 * 1000L
            startCountdown(_countdown.value)
        }
    }
}
