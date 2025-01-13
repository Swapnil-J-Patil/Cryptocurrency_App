package com.example.cleanarchitectureproject.presentation.common_components

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
import com.example.cleanarchitectureproject.data.remote.dto.coinlore.CurrencyCL
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

@Composable
fun PriceLineChart(
    currency: CurrencyCL?=null, modifier: Modifier,currencyCM: QuoteCM?=null, isMoreData: Boolean, labelName: String ?=null) {
    // Data preparation
    val priceChanges = if(!isMoreData)currency?.percentChange1h?.let {
        listOf(
        it.toDouble(),
        currency.percentChange24h.toDouble(),
        currency.percentChange7d.toDouble()
    )
    } else currencyCM?.let {
        listOf( currencyCM.percentChange1h,
            it.percentChange24h,
        currencyCM.percentChange24h,
        currencyCM.percentChange7d,
        currencyCM.percentChange30d,
        currencyCM.percentChange90d,
        currencyCM.percentChange1y,
        )
    }


    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            ), // Add vertical padding for spacing between cards
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary), // Set the background color
        elevation = CardDefaults.cardElevation(4.dp), // Add elevation for shadow
        shape = RoundedCornerShape(8.dp) // Rounded corners
    ) {
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

            labelHelperProperties = LabelHelperProperties(enabled = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
            ),
            indicatorProperties = HorizontalIndicatorProperties(enabled = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
            ),
            animationMode = AnimationMode.Together(delayBuilder = { it * 500L })
        )
    }
}




