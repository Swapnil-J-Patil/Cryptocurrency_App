package com.example.cleanarchitectureproject.data.remote.dto.coinlore

import com.google.gson.annotations.SerializedName

data class CurrencyCL(
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price_usd")
    val priceUsd: Float,
    @SerializedName("percent_change_24h")
    val percentChange24h: Float,
    @SerializedName("percent_change_1h")
    val percentChange1h: Float,
    @SerializedName("percent_change_7d")
    val percentChange7d: Float,
    @SerializedName("market_cap_usd")
    val marketCapUsd: Float,
    @SerializedName("volume24")
    val volume24: Float
)
