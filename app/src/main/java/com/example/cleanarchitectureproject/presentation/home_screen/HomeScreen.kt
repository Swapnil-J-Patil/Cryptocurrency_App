package com.example.cleanarchitectureproject.presentation.home_screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.Navbar
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.home_screen.components.navbar.BottomNavAnimation
import com.example.cleanarchitectureproject.presentation.home_screen.components.carousel.Carousel
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.home_screen.components.currency_row.LazyRowScaleIn
import com.example.cleanarchitectureproject.presentation.home_screen.components.TypingAnimation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.statsState.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(composition)

    val screen = listOf(
        Navbar.Home,
        Navbar.Create,
        Navbar.Profile,
        Navbar.Settings
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val carouselHeight = if (screenWidth > 600.dp) 330.dp else 290.dp
    val dotsPadding = if (screenWidth > 600.dp) 8.dp else 4.dp
    val tabTitles = listOf("Top Gainers", "Top Losers")
    // Define a scale animation using animateFloatAsState
    val scale by animateFloatAsState(
        targetValue = if (!(state.error.isNotBlank() || state.isLoading)) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500, // Adjust duration for smoothness
            easing = FastOutSlowInEasing
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                    viewModel.getGainers(state.cryptocurrency!!.data.cryptoCurrencyList)
                    viewModel.getLosers(state.cryptocurrency!!.data.cryptoCurrencyList)

                    val topGainers by viewModel.topGainers.collectAsState()
                    val topLosers by viewModel.topLosers.collectAsState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 56.dp) // Reserve space for navbar
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
                                    Log.d("carousel", "clicked item: $item")
                                },
                                dotsPadding = dotsPadding,
                                currency = state.cryptocurrency!!.data.cryptoCurrencyList.subList(0, 3)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            state.cryptocurrency?.data?.let { LazyRowScaleIn(items = it.cryptoCurrencyList) }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Tabs(gainers = topGainers, losers = topLosers, onItemClick = { item, isGainer->

                            ///{coinId}/{coinSymbol}/{imageUrl}/{price}/{percentage}/{isSaved}
                            val price="$ " + if(item.quotes[0].price.toString().length>10)item.quotes[0].price.toString().substring(0, 10) else item.quotes[0].price.toString()
                            val percentage=item.quotes[0].percentChange24h.toString().substring(0, 5)
                            val isSaved=false
                            navController.navigate(Screen.CoinLivePriceScreen.route + "/${item.id}/${item.symbol}/${price}/${percentage}/${isGainer}/${isSaved}")
                        },
                            animatedVisibilityScope,
                            "home",
                            tabTitles,
                            )
                    }
                }
            }

            // Scrollable content
            // Custom Navbar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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
                    BottomNavAnimation(
                        screens = screen,
                    )
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
}
