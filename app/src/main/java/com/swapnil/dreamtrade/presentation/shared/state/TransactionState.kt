package com.swapnil.dreamtrade.presentation.shared.state

import com.swapnil.dreamtrade.domain.model.TransactionData

data class TransactionState (
    val isLoading: Boolean =false,
    val transaction: List<TransactionData>?= null,
    val error: String = ""
)