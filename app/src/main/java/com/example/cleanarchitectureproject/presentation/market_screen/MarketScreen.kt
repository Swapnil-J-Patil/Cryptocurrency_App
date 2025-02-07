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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.cleanarchitectureproject.domain.model.toCryptoCoin
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.common_components.CoinCardItem
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.darkRed
import com.google.gson.Gson

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MarketScreen(
    navController: NavController,
    viewModel: MarketViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.coinListState.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever // Infinite repeat mode
    )
    val scaleBox by animateFloatAsState(
        targetValue = if (!(state.error.isNotBlank() || state.isLoading)) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500, // Adjust duration for smoothness
            easing = FastOutSlowInEasing
        )
    )

    val screenWidth =
        LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.widthPixels / density }

    val listState = rememberLazyGridState()
    val halfScreenWidth = if (screenWidth > 600) screenWidth / 3 else screenWidth

    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredCoins by viewModel.filteredCoins.collectAsState() // Observe filtered coins

    // Precompute visible indices
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 45.dp,
                bottom = 56.dp
            ) // Reserve space for navbar
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
                        scaleX = scaleBox,
                        scaleY = scaleBox
                    )
            ) {
                state.cryptocurrency?.data?.let {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .clip(RoundedCornerShape(8.dp)), // Apply rounded corners
                            shape = RoundedCornerShape(8.dp),
                            placeholder = { Text("Search by Name, Symbol or ID") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search Icon"
                                )
                            },
                            singleLine = true
                        )
                        key(searchQuery) {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(halfScreenWidth.dp),
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top=5.dp)

                            ) {
                                itemsIndexed(
                                    filteredCoins,
                                    key = { _, coin -> coin.id }) { index, coin ->

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
                                    val listType = "marketScreen_new"
                                    val price =
                                        "$ " + if (coin.quotes[0].price.toString().length > 10) coin.quotes[0].price.toString()
                                            .substring(
                                                0,
                                                10
                                            ) else coin.quotes[0].price.toString()
                                    CoinCardItem(
                                        currencyName = coin.name,
                                        symbol = coin.symbol,
                                        percentage = if (coin.isGainer) "+" + coin.percentage + "%" else coin.percentage + "%",
                                        price = coin.price,
                                        image = coin.graph,
                                        color = coin.color,
                                        logo = coin.logo,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 15.dp)
                                            .graphicsLayer(
                                                scaleX = scale.value,
                                                scaleY = scale.value
                                            )
                                            .sharedElement(
                                                state = rememberSharedContentState(key = "coinCard/${listType}_${coin.id}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                            .clickable {
                                                //onItemClick(coin, true)

                                                val isSaved = false
                                                val coinData = coin.toCryptoCoin()
                                                val gson = Gson() // Or use kotlinx.serialization
                                                val coinDataJson = gson.toJson(coinData)
                                                navController.navigate(Screen.CoinLivePriceScreen.route + "/${coin.id}/${coin.symbol}/${price}/${coin.percentage}/${coin.isGainer}/${isSaved}/${coinDataJson}/${listType}") {
                                                    launchSingleTop = true
                                                }
                                            },
                                    )
                                }
                            }
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