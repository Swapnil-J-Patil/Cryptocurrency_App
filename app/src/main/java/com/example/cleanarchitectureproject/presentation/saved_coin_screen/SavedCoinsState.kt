package com.example.cleanarchitectureproject.presentation.saved_coin_screen

import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptocurrencyCMDTO
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import kotlinx.coroutines.flow.Flow

data class SavedCoinsState (
    val isLoading: Boolean =false,
    val cryptocurrency: List<CryptoCoin>?= null,
    val error: String = ""
)