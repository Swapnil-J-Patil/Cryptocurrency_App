package com.example.cleanarchitectureproject.presentation.main_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitectureproject.common.Resource
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase
import com.example.cleanarchitectureproject.presentation.home_screen.CoinStatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {

    private val _isMarketScreen = MutableStateFlow("home")
    val isMarketScreen: StateFlow<String> = _isMarketScreen

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab

    fun toggleTab(tab: String) {
        _isMarketScreen.value = tab
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }
}
