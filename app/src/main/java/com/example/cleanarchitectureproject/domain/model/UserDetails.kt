package com.example.cleanarchitectureproject.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val tokens: List<String> = emptyList()
)