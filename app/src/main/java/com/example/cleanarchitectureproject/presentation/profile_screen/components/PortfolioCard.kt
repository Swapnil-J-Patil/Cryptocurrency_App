package com.example.cleanarchitectureproject.presentation.profile_screen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.model.CryptocurrencyCoin
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.presentation.common_components.CoinCardItem
import com.example.cleanarchitectureproject.presentation.profile_screen.components.pie_chart.PieChart
import com.example.cleanarchitectureproject.presentation.transaction_screen.components.CoinDisplay
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
    totalInvestment: Double,
    dollars: Double
) {
    val listState = rememberLazyListState()

    val df = DecimalFormat("#,##0.00") // Ensures two decimal places
    val formattedPrice = "$ ${df.format(portfolioValue)}"
    val formattedInvestment="$ ${df.format(totalInvestment)}"
    val formattedPercentage =
        if (portfolioPercentage >= 0.0) "+" + df.format(portfolioPercentage) + " %" else df.format(
            portfolioPercentage
        ) + " %"
    val portfolioColor = if (portfolioPercentage >= 0.0) green else lightRed

    val sortedCoins = remember(portfolioCoins) {
        portfolioCoins.sortedByDescending { it.quantity ?: 0.0 }
    }

    val (pieChartData, imageUrls) = remember(sortedCoins) {
        when {
            sortedCoins.size <= 4 -> {
                sortedCoins.associate { it.name to (it.quantity?.toInt() ?: 0) } to
                        sortedCoins.map { it.logo.toString() }
            }

            else -> {
                val topCoins = sortedCoins.take(4)
                    .associate { it.name to (it.quantity?.toInt() ?: 0) }
                val topImageUrls = sortedCoins.take(4).map { it.logo }
                val othersQuantity = sortedCoins.drop(4).sumOf { it.quantity ?: 0.0 }.toInt()

                val dummyUrl =
                    "https://cdn-icons-png.freepik.com/256/17470/17470916.png?semt=ais_hybrid"
                if (othersQuantity > 0) {
                    topCoins + ("Others" to othersQuantity) to (topImageUrls + dummyUrl)
                } else {
                    topCoins to topImageUrls
                }
            }
        }
    }
    val pieChartState = remember { mutableStateOf(pieChartData) }

    LaunchedEffect(pieChartData) {
        pieChartState.value = pieChartData
    }
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                )
                {
                    CoinDisplay(amount = dollars.toDouble(), isSell = false)
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                // Pass to PieChart
                PieChart(
                    data = pieChartState.value,
                    ringBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ringBgColor = MaterialTheme.colorScheme.tertiary,
                    portfolioValue = formattedPrice,
                    portfolioPercentage = formattedPercentage,
                    portfolioColor = portfolioColor,
                    totalInvestment = formattedInvestment,
                    imageUrls = imageUrls // Pass the image URLs
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
            /*item {
                Spacer(modifier = Modifier.height(40.dp))
            }*/
            itemsIndexed(
                portfolioCoins,
                key = { _, coin -> coin.id }) { index, portfolioCoin ->  // Use key
                val isVisible = remember {
                    derivedStateOf {
                        val visibleItems = listState.layoutInfo.visibleItemsInfo
                        visibleItems.any { it.index -1 == index }
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
                Spacer(modifier = Modifier.height(12.dp))
            }

        }
    }
}