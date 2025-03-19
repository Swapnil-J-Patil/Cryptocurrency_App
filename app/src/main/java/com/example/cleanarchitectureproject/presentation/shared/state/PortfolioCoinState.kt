package com.example.cleanarchitectureproject.presentation.shared.state

import com.example.cleanarchitectureproject.domain.model.PortfolioCoin

data class PortfolioCoinState (
    val isLoading: Boolean =false,
    val cryptocurrency: List<PortfolioCoin>?= null,
    val error: String = ""
)