package com.example.cleanarchitectureproject.presentation.coin_detail

import com.example.cleanarchitectureproject.domain.model.Cryptocurrency

data class CryptoCurrencyState(
    val isLoading: Boolean =false,
    val cryptocurrency: Cryptocurrency?= null,
    val error: String = ""
)
