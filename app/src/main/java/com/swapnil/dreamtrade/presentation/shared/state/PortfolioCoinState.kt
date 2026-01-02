package com.swapnil.dreamtrade.presentation.shared.state

import com.swapnil.dreamtrade.domain.model.PortfolioCoin

data class PortfolioCoinState (
    val isLoading: Boolean =false,
    val cryptocurrency: List<PortfolioCoin>?= null,
    val error: String = ""
)