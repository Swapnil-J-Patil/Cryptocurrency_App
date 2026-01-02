package com.swapnil.dreamtrade.data.local.transactions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")  // Ensure the table name is correctly set
data class TransactionsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Auto-generate ID
    val coinName: String,
    val date: String,
    val quantity: Double,
    val usd: Double,
    val transaction: String,
    val image: String,
)