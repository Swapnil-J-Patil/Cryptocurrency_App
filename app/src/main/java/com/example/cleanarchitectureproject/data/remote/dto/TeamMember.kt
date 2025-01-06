package com.example.cleanarchitectureproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TeamMember(
    val id: String,
    val name: String,
    val position: String
)