package com.example.cleanarchitectureproject.presentation.common_components

import android.annotation.SuppressLint
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.white
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties.Rotation.Mode
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

@Composable
fun PriceLineChart(
    modifier: Modifier = Modifier,
    currencyCM: QuoteCM? = null,
    labelName: String? = null
) {
    val labels = listOf("1Y", "90D", "30D", "7D", "24H", "1H")

    // Track visibility state
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

    LaunchedEffect(isVisible) {
        if (isVisible) {
            // Triggers recomposition when the chart becomes visible
        }
    }

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
                        label = labelName ?: "",
                        values = priceChanges,
                        color = SolidColor(Color(0xFF23af92)),
                        firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                        gradientAnimationDelay = 1000,
                        drawStyle = DrawStyle.Stroke(width = 2.dp)
                    ),
                )
            },
            labelHelperProperties = LabelHelperProperties(
                enabled = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            ),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
            ),
            gridProperties = GridProperties(
                enabled = true,
                yAxisProperties = GridProperties.AxisProperties(lineCount = labels.size)
            ),
            animationMode = AnimationMode.Together(delayBuilder = { it * 500L }),
            labelProperties = LabelProperties(
                enabled = true,
                rotation = LabelProperties.Rotation(Mode.IfNecessary, 0.0f),
                labels = labels,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.secondary
                ),
            ),
        )
    }
}





