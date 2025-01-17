package com.example.cleanarchitectureproject.data.remote.dto.coinmarket

import androidx.compose.runtime.Immutable

@Immutable
data class CryptoCurrencyCM(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val tags: List<String>,
    val cmcRank: Int,
    val marketPairCount: Int,
    val circulatingSupply: Double,
    val selfReportedCirculatingSupply: Double,
    val totalSupply: Double,
    val maxSupply: Double?,
    val isActive: Int,
    val lastUpdated: String,
    val dateAdded: String,
    val quotes: List<QuoteCM>,
    val isAudited: Boolean,
    val auditInfoList: List<Any>, // Replace with a proper type if audit info structure is known
    val badges: List<Int>
)