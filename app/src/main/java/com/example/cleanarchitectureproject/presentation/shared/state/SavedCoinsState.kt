package com.example.cleanarchitectureproject.presentation.shared.state

import com.example.cleanarchitectureproject.domain.model.CryptoCoin

data class SavedCoinsState (
    val isLoading: Boolean =false,
    val cryptocurrency: List<CryptoCoin>?= null,
    val error: String = ""
)