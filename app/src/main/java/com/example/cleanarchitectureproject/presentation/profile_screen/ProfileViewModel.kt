package com.example.cleanarchitectureproject.presentation.profile_screen

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cleanarchitectureproject.domain.model.ProfileData
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _isSettingsTab = MutableStateFlow<ImageVector?>(null)
    val isSettings: StateFlow<ImageVector?> get() = _isSettingsTab

    fun toggleSettings(imageVector: ImageVector?)
    {
        viewModelScope.launch {
            _isSettingsTab.value=imageVector
        }
    }
}



