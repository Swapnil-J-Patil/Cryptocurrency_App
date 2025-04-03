package com.example.cleanarchitectureproject.data.local.transactions

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM

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