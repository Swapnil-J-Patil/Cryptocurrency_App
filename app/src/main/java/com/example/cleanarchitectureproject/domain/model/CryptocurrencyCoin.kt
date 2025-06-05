package com.example.cleanarchitectureproject.domain.model

import androidx.compose.ui.graphics.Color
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM

data class CryptocurrencyCoin(
    val name: String,
    val percentage: String,
    val price: String,
    val logo: String,
    val graph: String,
    val symbol: String,
    val color: Color,
    val isGainer: Boolean,
    val id: Int,
    val slug: String = "",
    val tags: List<String>?= emptyList(),
    val cmcRank: Int = 1,
    val marketPairCount: Int = 1,
    val circulatingSupply: Double,
    val selfReportedCirculatingSupply: Double,
    val totalSupply: Double,
    val maxSupply: Double?,
    val isActive: Int,
    val lastUpdated: String = "",
    val dateAdded: String = "",
    val quotes: List<QuoteCM>,
    val isAudited: Boolean=true,
    val auditInfoList: List<Any>?= emptyList(),
    val badges: List<Int>?= emptyList()
)
fun CryptocurrencyCoin.toCryptoCoin(): CryptoCoin {
    return CryptoCoin(
        id = id,
        name = name,
        symbol = symbol,
        slug = slug,
        tags = tags,
        cmcRank = cmcRank,
        marketPairCount = marketPairCount,
        circulatingSupply = circulatingSupply,
        selfReportedCirculatingSupply = selfReportedCirculatingSupply,
        totalSupply = totalSupply,
        maxSupply = maxSupply,
        isActive = isActive,
        lastUpdated = lastUpdated,
        dateAdded = dateAdded,
        quotes = quotes, // Assuming a `toQuote()` function exists
        isAudited = isAudited,
        badges = badges
    )
}