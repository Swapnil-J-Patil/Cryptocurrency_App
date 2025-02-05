package com.example.cleanarchitectureproject.presentation.main_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.cleanarchitectureproject.presentation.Navbar
import com.example.cleanarchitectureproject.presentation.home_screen.components.navbar.BottomNavAnimation
import com.example.cleanarchitectureproject.presentation.home_screen.HomeScreen
import com.example.cleanarchitectureproject.presentation.home_screen.HomeScreenTab
import com.example.cleanarchitectureproject.presentation.home_screen.HomeViewModel
import com.example.cleanarchitectureproject.presentation.market_screen.MarketScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.statsState.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    var isPlaying by remember { mutableStateOf(true) } // Control animation state

    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever, isPlaying = isPlaying // Infinite repeat mode
    )

    val screen = listOf(
        Navbar.Home,
        Navbar.Create,
        Navbar.Profile,
        Navbar.Settings
    )

    val configuration = LocalConfiguration.current
    val isTab = configuration.screenWidthDp.dp > 600.dp

    val scale by animateFloatAsState(
        targetValue = if (!(state.error.isNotBlank() || state.isLoading)) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500, // Adjust duration for smoothness
            easing = FastOutSlowInEasing
        )
    )
    var selectedTab by remember { mutableStateOf(0) }
    var bottomBarVisibility by remember { mutableStateOf(true) }
    val isMarketScreen by viewModel.isMarketScreen.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isTab) {
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
                        if(isMarketScreen)
                        {
                            bottomBarVisibility = true
                            MarketScreen(
                                navController = navController,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                        else{
                            bottomBarVisibility = true
                            HomeScreenTab(navController = navController, animatedVisibilityScope = animatedVisibilityScope)
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        ) {
                            AnimatedVisibility(
                                visible = !(state.error.isNotBlank() || state.isLoading) || bottomBarVisibility,
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
                                    isTab = isTab,
                                    onClick = { tab ->
                                        selectedTab = tab
                                        if(selectedTab==1)
                                        {
                                            viewModel.toggleTab()
                                        }
                                    }
                                )
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
        else {

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
                        if(isMarketScreen)
                        {
                            bottomBarVisibility = true
                            MarketScreen(
                                navController = navController,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                        else
                        {
                            HomeScreen(navController = navController, animatedVisibilityScope = animatedVisibilityScope)
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        ) {
                            AnimatedVisibility(
                                visible = !(state.error.isNotBlank() || state.isLoading) || bottomBarVisibility,
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
                                    isTab = isTab,
                                    onClick = { tab ->
                                        selectedTab = tab
                                        if(selectedTab==1)
                                        {
                                            viewModel.toggleTab()
                                        }
                                    }
                                )
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
                    bottomBarVisibility = false
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
}
