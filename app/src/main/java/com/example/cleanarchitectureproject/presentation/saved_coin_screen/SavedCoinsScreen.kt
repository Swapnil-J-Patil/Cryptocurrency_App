package com.example.cleanarchitectureproject.presentation.saved_coin_screen

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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
import com.example.cleanarchitectureproject.presentation.saved_coin_screen.components.SquareCoinCardItem
import com.example.cleanarchitectureproject.presentation.shared.SavedCoinViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SavedCoinsScreen(
    navController: NavController,
    savedCoinViewModel: SavedCoinViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = savedCoinViewModel.coinListState.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever // Infinite repeat mode
    )
    val coroutineScope = rememberCoroutineScope()
    var isSaved by remember { mutableStateOf(false) }
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

    val searchQuery by savedCoinViewModel.searchQuery.collectAsState()
    val filteredCoins by savedCoinViewModel.filteredCoins.collectAsState() // Observe filtered coins

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
                state.cryptocurrency?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { savedCoinViewModel.updateSearchQuery(it) },
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
                                columns = GridCells.Fixed(2),
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top=5.dp, start = 8.dp,end=8.dp)
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
                                    val listType = "savedCoins"
                                    val price = coin.quotes[0].price
                                    val dfSmall = DecimalFormat("0.#####") // Up to 6 decimal places
                                    val formattedPrice = when {
                                        coin.quotes[0].price < 0.0001 -> "0.00.."  // Extremely small values
                                        coin.quotes[0].price < 100 -> dfSmall.format(coin.quotes[0].price) // Up to 6 decimal places
                                        else -> price.toInt().toString().take(3) + ".." // Large numbers (first 3 digits + "..")
                                    }
                                    "$ $formattedPrice"

                                    val firstQuote = coin.quotes?.firstOrNull() // Handle missing quotes
                                    val formattedPercentage = if (firstQuote!!.percentChange1h > 0) {
                                        if (coin.percentage.length > 5) coin.percentage.substring(0, 5)  else coin.percentage
                                    } else {
                                        if (coin.percentage.length > 5) coin.percentage.substring(0, 6) else coin.percentage
                                    }
                                    SquareCoinCardItem(
                                        currencyName = if(coin.name.length > 10) coin.name.substring(0,10) +".." else coin.name,
                                        symbol = coin.symbol,
                                        percentage = if (coin.isGainer) "+" + formattedPercentage + "%" else formattedPercentage + "%",
                                        isGainer = coin.isGainer,
                                        price = formattedPrice,
                                        color = coin.color,
                                        logo = coin.logo,
                                        quotes = coin.quotes[0],
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .padding(vertical = 8.dp, horizontal = 8.dp)
                                            .graphicsLayer(
                                                scaleX = scale.value,
                                                scaleY = scale.value
                                            )
                                            .clickable {
                                                //onItemClick(coin, true)

                                                coroutineScope.launch {
                                                    isSaved = savedCoinViewModel.isCoinSaved(coin.id.toString())

                                                    val coinData = coin.toCryptoCoin()
                                                    val gson = Gson() // Or use kotlinx.serialization
                                                    val coinDataJson = gson.toJson(coinData)
                                                    navController.navigate(Screen.CoinLivePriceScreen.route + "/${coin.id}/${coin.symbol}/${price}/${coin.percentage}/${coin.isGainer}/${isSaved}/${coinDataJson}/${listType}") {
                                                        launchSingleTop = true
                                                    }
                                                }
                                            },
                                        isTab = false,
                                        coinId = coin.id.toString(),
                                        listType = listType,
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if(state.cryptocurrency?.isEmpty() == true)
        {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(start=140.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wallet),
                    contentDescription = "saved_coins",
                    modifier = Modifier
                        .size(200.dp) // Ensure image is smaller than the border container
                        .padding(6.dp)
                        .background(Color.Transparent),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "No saved coins!",
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }
        // Error message or loading animation
        if (state.error.isNotBlank()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.offline),
                    contentDescription = "offline",
                    modifier = Modifier
                        .size(200.dp) // Ensure image is smaller than the border container
                        .padding(6.dp)
                        .background(Color.Transparent),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
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