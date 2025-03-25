package com.example.cleanarchitectureproject.domain.model

import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM

data class PortfolioCoin(
    val id: Int,
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
    val badges: List<Int>?,
    val quantity: Double?,
    val purchasedAt: Double?
)