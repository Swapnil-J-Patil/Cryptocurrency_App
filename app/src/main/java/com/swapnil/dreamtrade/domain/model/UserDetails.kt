package com.swapnil.dreamtrade.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val tokens: List<String> = emptyList()
)