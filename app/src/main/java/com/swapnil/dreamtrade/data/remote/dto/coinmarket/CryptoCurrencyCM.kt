package com.swapnil.dreamtrade.data.remote.dto.coinmarket

import androidx.compose.runtime.Immutable

@Immutable
data class CryptoCurrencyCM(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String = "",
    val tags: List<String>?= emptyList(),
    val cmcRank: Int=1,
    val marketPairCount: Int=1,
    val circulatingSupply: Double,
    val selfReportedCirculatingSupply: Double,
    val totalSupply: Double,
    val maxSupply: Double?,
    val isActive: Int=1,
    val lastUpdated: String = "",
    val dateAdded: String = "",
    val quotes: List<QuoteCM>,
    val isAudited: Boolean=true,
    val auditInfoList: List<Any>?= emptyList(), // Replace with a proper type if audit info structure is known
    val badges: List<Int>?= emptyList()
)