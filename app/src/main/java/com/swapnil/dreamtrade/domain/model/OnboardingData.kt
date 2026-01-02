package com.swapnil.dreamtrade.domain.model

import androidx.compose.ui.graphics.Color

data class OnboardingData(
    val animation: Int,  // Change from image to animation
    val title: String,
    val desc: String,
    val backgroundColor: Color,
    val mainColor: Color = Color.Blue,
)