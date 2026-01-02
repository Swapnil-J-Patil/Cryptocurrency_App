package com.swapnil.dreamtrade.domain.model

import com.swapnil.dreamtrade.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.swapnil.dreamtrade.data.remote.dto.coinmarket.QuoteCM

data class CryptoCoin(
    val id: Int,
    val name: String,
    val symbol: String?,
    val slug: String? = "",
    val tags: List<String>?= emptyList(),
    val cmcRank: Int? = 1,
    val marketPairCount: Int? = 1,
    val circulatingSupply: Double?,
    val selfReportedCirculatingSupply: Double?,
    val totalSupply: Double?,
    val maxSupply: Double?,
    val isActive: Int?=1,
    val lastUpdated: String? = "",
    val dateAdded: String? = "",
    val quotes: List<QuoteCM>?,
    val isAudited: Boolean?=true,
    val badges: List<Int>?= emptyList()
)

fun CryptoCurrencyCM.toCryptoCoin(): CryptoCoin {
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
