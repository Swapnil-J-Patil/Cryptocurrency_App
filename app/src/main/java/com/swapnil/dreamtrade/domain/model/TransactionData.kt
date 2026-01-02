package com.swapnil.dreamtrade.domain.model

data class TransactionData(
    val id:Int=0,
    val coinName: String,
    val date: String,
    val quantity: Double,
    val usd: Double?,
    val transaction: String,
    val image: String,
)