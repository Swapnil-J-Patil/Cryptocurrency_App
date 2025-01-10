package com.example.cleanarchitectureproject.presentation.coin_detail.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.remote.dto.coinlore.CurrencyCL
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun PriceLineChart(currency: CurrencyCL, modifier: Modifier) {
    // Data preparation
    val priceChanges = listOf(
        currency.percentChange1h.toDouble(),
        currency.percentChange24h.toDouble(),
        currency.percentChange7d.toDouble()
    )


    LineChart(
        modifier = modifier,
        data = remember {
            listOf(
                Line(
                    label = "Price Change (%)",
                    values = priceChanges,
                    color = SolidColor(Color(0xFF23af92)),
                    firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic), // Line stroke animation
                    gradientAnimationDelay = 1000, // Delay for gradient fill
                    drawStyle = DrawStyle.Stroke(width = 2.dp)
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = { it * 500L })
    )
}




