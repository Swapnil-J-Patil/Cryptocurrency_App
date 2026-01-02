package com.swapnil.dreamtrade.presentation.profile_screen

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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



