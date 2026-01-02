package com.swapnil.dreamtrade.presentation.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.dreamtrade.domain.use_case.app_theme.GetThemeUseCase
import com.swapnil.dreamtrade.domain.use_case.app_theme.SetThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppThemeViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {
    private val _isDark = MutableStateFlow(false)
    val isDark: StateFlow<Boolean> get() = _isDark

    init {
        viewModelScope.launch {
            getThemeUseCase().collect {
                _isDark.value = it
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            setThemeUseCase(!isDark.value)
        }
    }
}