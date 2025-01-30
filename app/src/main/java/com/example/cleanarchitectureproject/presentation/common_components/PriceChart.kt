package com.example.cleanarchitectureproject.presentation.common_components

import android.annotation.SuppressLint
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
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
    modifier: Modifier,
    currencyCM: QuoteCM? = null,
    labelName: String? = null
) {
    // Data preparation
    val priceChanges = currencyCM?.let {
        listOf(
            currencyCM.percentChange1y,
            currencyCM.percentChange90d,
            currencyCM.percentChange30d,
            currencyCM.percentChange7d,
            it.percentChange24h,
            currencyCM.percentChange1h,
        )
    }

    val labels:List<String> = listOf("1Y", "90D", "30D", "7D", "24H", "1H")

        LineChart(
            modifier = modifier,
            data = remember {
                listOf(
                    Line(
                        label = labelName!!,
                        values = priceChanges ?: emptyList(),
                        color = SolidColor(Color(0xFF23af92)),
                        firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(
                            2000,
                            easing = EaseInOutCubic
                        ), // Line stroke animation
                        gradientAnimationDelay = 1000, // Delay for gradient fill
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
            gridProperties = GridProperties(enabled = true, yAxisProperties = GridProperties.AxisProperties(lineCount = labels.size)),
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




