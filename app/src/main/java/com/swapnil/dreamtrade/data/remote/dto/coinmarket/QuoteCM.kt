package com.swapnil.dreamtrade.data.remote.dto.coinmarket

data class QuoteCM(
    val name: String,
    val price: Double,
    val volume24h: Double,
    val marketCap: Double,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val lastUpdated: String,
    val percentChange30d: Double,
    val percentChange60d: Double,
    val percentChange90d: Double,
    val fullyDilluttedMarketCap: Double,
    val marketCapByTotalSupply: Double,
    val dominance: Double,
    val turnover: Double,
    val ytdPriceChangePercentage: Double,
    val percentChange1y: Double
)
