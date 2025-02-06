package com.example.cleanarchitectureproject.presentation.home_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.cleanarchitectureproject.domain.model.toCryptoCoin
import com.example.cleanarchitectureproject.presentation.Navbar
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.home_screen.components.TypingAnimation
import com.example.cleanarchitectureproject.presentation.home_screen.components.carousel.Carousel
import com.example.cleanarchitectureproject.presentation.home_screen.components.currency_row.LazyRowScaleIn
import com.google.gson.Gson

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreenTab(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.statsState.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    var isPlaying by remember { mutableStateOf(true) } // Control animation state

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying // Infinite repeat mode
    )

    val scale by animateFloatAsState(
        targetValue = if (!(state.error.isNotBlank() || state.isLoading)) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500, // Adjust duration for smoothness
            easing = FastOutSlowInEasing
        )
    )
    val configuration = LocalConfiguration.current
    val isTab = configuration.screenWidthDp.dp > 600.dp
    val carouselHeight = if (isTab) 350.dp else 320.dp
    val dotsPadding = if (isTab) 8.dp else 4.dp
    val tabTitles = listOf("Top Gainers", "Top Losers")

    val gainerPercentageList by viewModel.gainerPercentageList.collectAsState()
    val gainerPriceList by viewModel.gainerPriceList.collectAsState()
    val gainerLogoList by viewModel.gainerLogoList.collectAsState()
    val gainerGraphList by viewModel.gainerGraphList.collectAsState()

    val loserPercentageList by viewModel.loserPercentageList.collectAsState()
    val loserPriceList by viewModel.loserPriceList.collectAsState()
    val loserLogoList by viewModel.loserLogoList.collectAsState()
    val loserGraphList by viewModel.loserGraphList.collectAsState()
    val topGainers by viewModel.topGainers.collectAsState()
    val topLosers by viewModel.topLosers.collectAsState()

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
            ) {
                state.cryptocurrency?.data?.let {
                    val list = state.cryptocurrency!!.data.cryptoCurrencyList
                        .subList(0, 3)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 140.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            TypingAnimation(
                                text = "Let's Dive Into the Market!",
                                modifier = Modifier
                                    .padding(top = 45.dp, start = 15.dp, end = 15.dp)
                            )

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Top,
                            ) {
                                Carousel(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(carouselHeight),
                                    onClick = { item ->

                                        val coinData = item.toCryptoCoin()
                                        val gson = Gson()
                                        val coinDataJson = gson.toJson(coinData)
                                        val flag = true
                                        val listType = "carousel"
                                        navController.navigate(Screen.ZoomedChart.route + "/${item.id}/${coinDataJson}/${flag}/${listType}")
                                    },
                                    dotsPadding = dotsPadding,
                                    currency = list,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    listType = "carousel"
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(top = 10.dp)
                            ) {
                                state.cryptocurrency?.data?.let {
                                    isPlaying = false

                                    LazyRowScaleIn(

                                        items = it.cryptoCurrencyList,
                                        onCardClicked = { item ->

                                            val price =
                                                "$ " + if (item.quotes[0].price.toString().length > 10) item.quotes[0].price.toString()
                                                    .substring(
                                                        0,
                                                        10
                                                    ) else item.quotes[0].price.toString()
                                            val percentage =
                                                item.quotes[0].percentChange1h.toString()

                                            val isSaved = false
                                            val coinData = item.toCryptoCoin()
                                            val isGainer =
                                                if (item.quotes[0].percentChange1h > 0.0) true else false

                                            val gson =
                                                Gson() // Or use kotlinx.serialization
                                            val coinDataJson = gson.toJson(coinData)
                                            val listType = "lazyRow"
                                            navController.navigate(Screen.CoinLivePriceScreen.route + "/${item.id}/${item.symbol}/${price}/${percentage}/${isGainer}/${isSaved}/${coinDataJson}/${listType}")

                                        },
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Tabs(
                                gainers = topGainers,
                                losers = topLosers,
                                gainersPercentage = gainerPercentageList,
                                losersPercentage = loserPercentageList,
                                onItemClick = { item, isGainer ->

                                    val price =
                                        "$ " + if (item.quotes[0].price.toString().length > 10) item.quotes[0].price.toString()
                                            .substring(
                                                0,
                                                10
                                            ) else item.quotes[0].price.toString()
                                    val percentage =
                                        item.quotes[0].percentChange1h.toString()

                                    val isSaved = false
                                    val coinData = item.toCryptoCoin()
                                    val gson = Gson() // Or use kotlinx.serialization
                                    val coinDataJson = gson.toJson(coinData)
                                    val listType = "gainersAndLosers"

                                    navController.navigate(Screen.CoinLivePriceScreen.route + "/${item.id}/${item.symbol}/${price}/${percentage}/${isGainer}/${isSaved}/${coinDataJson}/${listType}")
                                },
                                animatedVisibilityScope,
                                "home",
                                tabTitles,
                                gainersPrice = gainerPriceList,
                                losersPrice = loserPriceList,
                                gainersLogo = gainerLogoList,
                                losersLogo = loserLogoList,
                                gainersGraph = gainerGraphList,
                                losersGraph = loserGraphList,
                                listType = "gainersAndLosers"
                            )
                        }
                    }

                }
            }
        }
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