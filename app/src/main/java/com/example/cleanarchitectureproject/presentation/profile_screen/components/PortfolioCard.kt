package com.example.cleanarchitectureproject.presentation.profile_screen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.model.CryptocurrencyCoin
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.presentation.common_components.CoinCardItem
import com.example.cleanarchitectureproject.presentation.profile_screen.components.pie_chart.PieChart
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import com.example.cleanarchitectureproject.presentation.ui.theme.red
import java.text.DecimalFormat

@Composable
fun PortfolioCard(
    modifier: Modifier = Modifier,
    portfolioCoins: List<PortfolioCoin>,
    portfolioValue: Double,
    portfolioPercentage: Double,
) {
    val listState = rememberLazyListState()

    val df = DecimalFormat("#,##0.00") // Ensures two decimal places
    val formattedPrice = "$ ${df.format(portfolioValue)}"
    val formattedPercentage =
        if (portfolioPercentage >= 0.0) "+" + df.format(portfolioPercentage) + " %" else df.format(
            portfolioPercentage
        ) + " %"
    val portfolioColor = if (portfolioPercentage >= 0.0) green else lightRed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            state = listState,
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))

                if (portfolioCoins.isNotEmpty()) {
                    // Sort coins by quantity in descending order, safely handling null values
                    val sortedCoins = portfolioCoins.sortedByDescending { it.quantity ?: 0.0 }

                    val pieChartData: Map<String, Int> = when {
                        sortedCoins.size <= 4 -> {
                            // If there are 4 or fewer coins, use them all directly
                            sortedCoins.associate { it.name to (it.quantity?.toInt() ?: 0) }
                        }
                        else -> {
                            // Take the first 4 coins
                            val topCoins = sortedCoins.take(4)
                                .associate { it.name to (it.quantity?.toInt() ?: 0) }

                            // Sum the quantities of the remaining coins
                            val othersQuantity = sortedCoins.drop(4).sumOf { it.quantity ?: 0.0 }.toInt()

                            // Include "Others" only if there's a remaining quantity
                            if (othersQuantity > 0) {
                                topCoins + ("Others" to othersQuantity)
                            } else {
                                topCoins
                            }
                        }
                    }

                    // Pass to PieChart
                    PieChart(
                        data = pieChartData,
                        ringBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
                        ringBgColor = MaterialTheme.colorScheme.tertiary,
                        portfolioValue = formattedPrice,
                        portfolioPercentage = formattedPercentage,
                        portfolioColor = portfolioColor
                    )
                }

                /*PieChart(
                    data = mapOf(
                        Pair("Sample-1", 150),
                        Pair("Sample-2", 120),
                        Pair("Sample-3", 110),
                        Pair("Sample-4", 170),
                        Pair("Sample-5", 120),
                    ),
                    ringBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ringBgColor = MaterialTheme.colorScheme.tertiary,
                    portfolioValue = formattedPrice,
                    portfolioPercentage = formatedPercentage,
                    portfolioColor = portfolioColor
                )*/
                Spacer(modifier = Modifier.height(60.dp))
            }

            itemsIndexed(
                portfolioCoins,
                key = { _, coin -> coin.id }) { index, portfolioCoin ->  // Use key
                val isVisible = remember {
                    derivedStateOf {
                        val visibleItems = listState.layoutInfo.visibleItemsInfo
                        visibleItems.any { it.index == index }
                    }
                }
                val scale = remember { Animatable(0f) }

                LaunchedEffect(isVisible.value) {
                    if (isVisible.value) {
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    } else {
                        scale.snapTo(0f) // Reset scale when not visible
                    }
                }

                /*While navigating
                        percentage = portfolioCoin.quotes.get(0).percentChange1h.toString(),
                price = portfolioCoin.quotes.get(0).price.toString(),*/
                if (portfolioCoins.isNotEmpty() && index < portfolioCoins.size) {
                    val formattedPP = "$ ${df.format(portfolioCoin.purchasedAt)}"
                    val formattedCP = "$ ${df.format(portfolioCoin.currentPrice)}"
                    val formattedQuantity = "${df.format(portfolioCoin.quantity)}"
                    PortfolioCoinCardItem(
                        symbol = portfolioCoin.symbol!!,
                        purchasedPrice = formattedPP,
                        currentPrice = formattedCP,
                        graph = portfolioCoin.graph.toString(),
                        color = portfolioCoin.color!!,
                        logo = portfolioCoin.logo.toString(),
                        quantity = formattedQuantity,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 15.dp)
                            .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                        /*.clickable {
                            onItemClick(portfolioCoin, true)
                        }
                        .sharedElement(
                            state = rememberSharedContentState(key = "coinCard/${listType}_${portfolioCoin.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),*/
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }

        }
    }
}