package com.example.cleanarchitectureproject.presentation.shared.state

import com.example.cleanarchitectureproject.domain.model.TransactionData

data class TransactionState (
    val isLoading: Boolean =false,
    val transaction: List<TransactionData>?= null,
    val error: String = ""
)