package com.example.cleanarchitectureproject.presentation.saved_coin_screen.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties

@Composable
fun LineChartItem(
    modifier: Modifier = Modifier,
    currencyCM: QuoteCM? = null,
    color1: Color,
    color2:Color
) {

    var isVisible by remember { mutableStateOf(false) }

    val priceChanges = currencyCM?.let {
        listOf(
            currencyCM.percentChange1y,
            currencyCM.percentChange90d,
            currencyCM.percentChange30d,
            currencyCM.percentChange7d,
            it.percentChange24h,
            currencyCM.percentChange1h,
        )
    } ?: emptyList()


    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                isVisible = coordinates.isAttached
            }
            .drawWithContent {
                if (!isVisible) return@drawWithContent
                drawContent()
            }
    ) {
        LineChart(
            modifier = Modifier.fillMaxSize(),
            data = remember(isVisible) { // Recomposition happens when `isVisible` changes
                listOf(
                    Line(
                        label = "",
                        values = priceChanges,
                        color = SolidColor(color1),
                        firstGradientFillColor = color2.copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                        gradientAnimationDelay = 1000,
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
                    ),
                )
            },
            dividerProperties = DividerProperties(
                xAxisProperties = LineProperties(enabled = false),
                yAxisProperties = LineProperties(enabled = false),
                ),
            labelHelperProperties = LabelHelperProperties(
                enabled = false,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            ),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = false,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
            ),
            gridProperties = GridProperties(
                enabled = false,
                yAxisProperties = GridProperties.AxisProperties()
            ),

            animationMode = AnimationMode.Together(delayBuilder = { it * 500L }),
        )
    }
}