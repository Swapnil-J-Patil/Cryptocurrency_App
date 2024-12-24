package com.example.cleanarchitectureproject.presentation.coin_detail

import com.example.cleanarchitectureproject.domain.model.Coin
import com.example.cleanarchitectureproject.domain.model.CoinDetail

data class CoinDetailState(
    val isLoading: Boolean =false,
    val coin: CoinDetail ?= null,
    val error: String = ""
)
