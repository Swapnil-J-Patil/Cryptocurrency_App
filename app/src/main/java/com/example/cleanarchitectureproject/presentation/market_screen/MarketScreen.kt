package com.example.cleanarchitectureproject.presentation.market_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.common_components.CoinCardItem
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MarketScreen(
    navController: NavController,
    viewModel: MarketViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.coinList.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever // Infinite repeat mode
    )
    val scale by animateFloatAsState(
        targetValue = if (!(state.error.isNotBlank() || state.isLoading)) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500, // Adjust duration for smoothness
            easing = FastOutSlowInEasing
        )
    )

    val screenWidth =
        LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.widthPixels / density }
    val screenHeight =
        LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.heightPixels / density }

    val listState = rememberLazyGridState()
    val halfScreenWidth = if (screenWidth > 600) screenWidth / 3 else screenWidth
    val adaptiveHeight = screenHeight * 0.5
    val coinsList by viewModel.coins.collectAsState()

    // Precompute visible indices
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = !(state.error.isNotBlank() || state.isLoading),
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            // Apply scaling animation along with visibility change
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            ) {
                state.cryptocurrency?.data?.let {

                    val coinPercentageList = remember(coinsList) {
                        coinsList.map { gainer ->
                            if (gainer.quotes[0].percentChange1h.toString().length > 5) {
                                "+" + gainer.quotes[0].percentChange1h.toString()
                                    .substring(0, 5) + " %"
                            } else {
                                "+" + gainer.quotes[0].percentChange1h.toString() + " %"
                            }
                        }
                    }
                    val coinPriceList = remember(coinsList) {
                        coinsList.map { coin ->
                            if (coin.quotes[0].price < 1000) {
                                "$ " + coin.quotes[0].price.toString().substring(0, 5)
                            } else {
                                "$ " + coin.quotes[0].price.toString()
                                    .substring(0, 3) + ".."
                            }
                        }
                    }

                    val coinLogoList = remember(coinsList) {
                        coinsList.map { coin ->
                            "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png"

                        }
                    }
                    val coinGraphList = remember(coinsList) {
                        coinsList.map { coin ->
                            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coin.id}.png"
                        }
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(halfScreenWidth.dp),
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(adaptiveHeight.dp)
                            .padding(top = 8.dp)
                    ) {
                        itemsIndexed(state.cryptocurrency.data.cryptoCurrencyList) { index, coin ->
                            // val color = if (gainer.quotes[0].percentChange24h > 0.0) darkGreen else darkRed

                            // Check visibility
                            val isVisible = remember {
                                derivedStateOf {
                                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                                    visibleItems.any { it.index == index }
                                }
                            }
                            val scale = remember { Animatable(0f) }
                            // val hasAnimated = remember { mutableStateOf(false) }

                            LaunchedEffect(isVisible.value) {
                                if (isVisible.value) {
                                    scale.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(
                                            durationMillis = 300, // Adjust as needed for smoothness
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                } else {
                                    scale.snapTo(0f) // Reset scale when not visible
                                }
                            }

                            CoinCardItem(
                                currencyName = coin.name,
                                symbol = coin.symbol,
                                percentage = coinPercentageList[index],
                                price = coinPriceList[index],
                                image = coinGraphList[index],
                                color = darkGreen,
                                logo = coinLogoList[index],
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 15.dp)
                                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                                    .clickable {
                                        //onItemClick(coin, true)
                                    }
                                    .sharedElement(
                                        state = rememberSharedContentState(key = "coinCard/${coin.id}"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    ),
                            )
                        }
                    }
                }
            }
        }
        // Error message or loading animation
        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }

        if (state.isLoading) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp)
            )
        }
    }

}