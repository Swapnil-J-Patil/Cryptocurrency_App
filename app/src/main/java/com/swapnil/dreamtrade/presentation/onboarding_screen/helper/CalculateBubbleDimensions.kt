package com.swapnil.dreamtrade.presentation.onboarding_screen.helper

import androidx.compose.animation.core.Easing
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

fun calculateBubbleDimensions(
    swipeProgress: Float,
    easing: Easing,
    minRadius: Dp,
    maxRadius: Dp
): Pair<Dp, Dp> {
    // swipe value ranges between 0 to 1.0 for half of the swipe
    // and 1.0 to 0 for the other half of the swipe
    val swipeValue = lerp(0f, 2f, swipeProgress.absoluteValue)
    val radius = androidx.compose.ui.unit.lerp(
        minRadius,
        maxRadius,
        easing.transform(swipeValue)
    )
    var centerX = androidx.compose.ui.unit.lerp(
        0.dp,
        maxRadius,
        easing.transform(swipeValue)
    )
    if (swipeProgress < 0) {
        centerX = -centerX
    }
    return Pair(radius, centerX)
}