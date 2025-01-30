package com.example.cleanarchitectureproject.presentation.home_screen.components.carousel

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.presentation.common_components.PriceLineChart
import kotlin.math.absoluteValue

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ZoomedChart(
    currency: CryptoCoin,
    symbol:String,
    isHomeScreen: Boolean,
    animatedVisibilityScope:AnimatedVisibilityScope
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val boxHeight = if (screenWidth > 600.dp) 350.dp else 320.dp
    val boxPadding= if(isHomeScreen) 15.dp else 8.dp
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        ,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight)
                .padding(boxPadding),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable {
                        // onClick(page)
                    }
                    .sharedElement(
                        state = rememberSharedContentState(key = "coinChart/${symbol}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary), // Set the background color
                elevation = CardDefaults.cardElevation(4.dp), // Add elevation for shadow
                shape = RoundedCornerShape(8.dp) // Rounded corners
            ) {
                PriceLineChart(
                    currencyCM = currency.quotes[0],
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 10.dp, vertical = 16.dp)
                        .clickable {
                            //onClick(page)
                        },
                    labelName = currency.symbol
                )
            }
        }
    }
}