package com.example.cleanarchitectureproject.domain.model

import com.example.cleanarchitectureproject.data.remote.dto.coinpaprika.TeamMember

data class CoinDetail(
    val coinId:String,
    val name:String,
    val description: String,
    val symbol: String,
    val rank: Int,
    val isActive: Boolean,
    val tags: List<String>,
    val team: List<TeamMember>
)
