package com.swapnil.dreamtrade.presentation.shared.state

import com.swapnil.dreamtrade.domain.model.CryptoCoin

data class SavedCoinsState (
    val isLoading: Boolean =false,
    val cryptocurrency: List<CryptoCoin>?= null,
    val error: String = ""
)