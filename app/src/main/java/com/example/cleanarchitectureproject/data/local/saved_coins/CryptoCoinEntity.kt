package com.example.cleanarchitectureproject.data.local.saved_coins

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM

@Entity(tableName = "saved_coins")  // Ensure the table name is correctly set
data class CryptoCoinEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val symbol: String?,
    val slug: String?,
    val tags: List<String>?,
    val cmcRank: Int?,
    val marketPairCount: Int?,
    val circulatingSupply: Double?,
    val selfReportedCirculatingSupply: Double?,
    val totalSupply: Double?,
    val maxSupply: Double?,
    val isActive: Int?,
    val lastUpdated: String?,
    val dateAdded: String?,
    val quotes: List<QuoteCM>?,
    val isAudited: Boolean?,
    val badges: List<Int>?
)
