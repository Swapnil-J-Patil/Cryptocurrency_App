package com.swapnil.dreamtrade.presentation.main_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {

    private val _currentTab = MutableStateFlow("home")
    val currentTab: StateFlow<String> = _currentTab

    fun toggleTab(tab: String) {
        _currentTab.value = tab
    }


}
