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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.model.toCryptoCoin
import com.example.cleanarchitectureproject.presentation.Navbar
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.home_screen.components.navbar.BottomNavAnimation
import com.example.cleanarchitectureproject.presentation.home_screen.components.carousel.Carousel
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.home_screen.components.currency_row.LazyRowScaleIn
import com.example.cleanarchitectureproject.presentation.home_screen.components.TypingAnimation
import com.example.cleanarchitectureproject.presentation.market_screen.MarketScreen
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.darkRed
import com.google.gson.Gson

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
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
    val carouselHeight = if (isTab) 350.dp else 320.dp
    val dotsPadding = if (isTab) 8.dp else 4.dp
    val tabTitles = listOf("Top Gainers", "Top Losers")
    // Define a scale animation using animateFloatAsState
    val scale by animateFloatAsState(
        targetValue = if (!(state.error.isNotBlank() || state.isLoading)) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500, // Adjust duration for smoothness
            easing = FastOutSlowInEasing
        )
    )
    var selectedTab by remember { mutableStateOf(0) }
    var bottomBarVisibility by remember { mutableStateOf(true) }
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
                        when (selectedTab) {
                            0 -> {
                                bottomBarVisibility = true
                                // Apply scaling animation along with visibility change

                              /*  viewModel.getGainers(state.cryptocurrency!!.data.cryptoCurrencyList)
                                viewModel.getLosers(state.cryptocurrency!!.data.cryptoCurrencyList)*/

                              /*  val topGainers by viewModel.topGainers.collectAsState()
                                val topLosers by viewModel.topLosers.collectAsState()*/
                                val list = state.cryptocurrency!!.data.cryptoCurrencyList
                                    .subList(0, 3)

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
                                                val gson = Gson() // Or use kotlinx.serialization
                                                val coinDataJson = gson.toJson(coinData)
                                                val flag = true
                                                val listType="carousel"
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

                                                    ///{coinId}/{coinSymbol}/{imageUrl}/{price}/{percentage}/{isSaved}
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
                                                    val listType="lazyRow"
                                                    navController.navigate(Screen.CoinLivePriceScreen.route + "/${item.id}/${item.symbol}/${price}/${percentage}/${isGainer}/${isSaved}/${coinDataJson}/${listType}")

                                                },
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                   /* val gainerPercentageList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            if (gainer.quotes[0].percentChange1h.toString().length > 5) {
                                                "+" + gainer.quotes[0].percentChange1h.toString()
                                                    .substring(0, 5) + " %"
                                            } else {
                                                "+" + gainer.quotes[0].percentChange1h.toString() + " %"
                                            }
                                        }
                                    }
                                    val gainerPriceList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            if (gainer.quotes[0].price < 1000) {
                                                "$ " + gainer.quotes[0].price.toString()
                                                    .substring(0, 5)
                                            } else {
                                                "$ " + gainer.quotes[0].price.toString()
                                                    .substring(0, 3) + ".."
                                            }
                                        }
                                    }

                                    val gainerLogoList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            "https://s2.coinmarketcap.com/static/img/coins/64x64/${gainer.id}.png"

                                        }
                                    }
                                    val gainerGraphList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${gainer.id}.png"
                                        }
                                    }
                                    val loserPercentageList = remember(topLosers) {
                                        topLosers.map { loser ->
                                            if (loser.quotes[0].percentChange1h.toString().length > 5) {
                                                loser.quotes[0].percentChange1h.toString()
                                                    .substring(0, 5) + " %"
                                            } else {
                                                loser.quotes[0].percentChange1h.toString() + " %"
                                            }
                                        }
                                    }
                                    val loserPriceList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            "$ " + gainer.quotes[0].price.toString().substring(0, 5)
                                        }
                                    }
                                    val loserLogoList = remember(topLosers) {
                                        topLosers.map { loser ->
                                            "https://s2.coinmarketcap.com/static/img/coins/64x64/${loser.id}.png"

                                        }
                                    }
                                    val loserGraphList = remember(topLosers) {
                                        topLosers.map { loser ->
                                            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${loser.id}.png"
                                        }
                                    }*/
                                    Tabs(
                                        gainers = topGainers,
                                        losers = topLosers,
                                        gainersPercentage = gainerPercentageList,
                                        losersPercentage = loserPercentageList,
                                        onItemClick = { item, isGainer ->

                                            ///{coinId}/{coinSymbol}/{imageUrl}/{price}/{percentage}/{isSaved}
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
                                            val listType="gainersAndLosers"

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

                            1 -> {
                                bottomBarVisibility = true

                                MarketScreen(
                                    navController = navController,
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                            }
                        }


                        // Scrollable content
                        // Custom Navbar

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
                                    }
                                )
                            }
                        }

                        // Error message or loading animation

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

        } else {

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
                        when (selectedTab) {
                            0 -> {
                                bottomBarVisibility = true

                                // Apply scaling animation along with visibility change

                            /*    viewModel.getGainers(state.cryptocurrency!!.data.cryptoCurrencyList)
                                viewModel.getLosers(state.cryptocurrency!!.data.cryptoCurrencyList)
*/
                              /*  val topGainers by viewModel.topGainers.collectAsState()
                                val topLosers by viewModel.topLosers.collectAsState()*/
                                val list = state.cryptocurrency!!.data.cryptoCurrencyList
                                    .subList(0, 3)

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .padding(bottom = 56.dp) // Reserve space for navbar
                                ) {
                                    TypingAnimation(
                                        text = "Let's Dive Into the Market!",
                                        modifier = Modifier
                                            .padding(
                                                top = 45.dp,
                                                start = 15.dp,
                                                end = 15.dp
                                            )
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
                                                val gson =
                                                    Gson() // Or use kotlinx.serialization
                                                val coinDataJson = gson.toJson(coinData)
                                                val flag = true
                                                val listType="carousel"
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

                                                    ///{coinId}/{coinSymbol}/{imageUrl}/{price}/{percentage}/{isSaved}
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
                                                    val listType="lazyRow"
                                                    navController.navigate(Screen.CoinLivePriceScreen.route + "/${item.id}/${item.symbol}/${price}/${percentage}/${isGainer}/${isSaved}/${coinDataJson}/${listType}")

                                                },
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                  /*  val gainerPercentageList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            if (gainer.quotes[0].percentChange1h.toString().length > 5) {
                                                "+" + gainer.quotes[0].percentChange1h.toString()
                                                    .substring(0, 5) + " %"
                                            } else {
                                                "+" + gainer.quotes[0].percentChange1h.toString() + " %"
                                            }
                                        }
                                    }
                                    val gainerPriceList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            if (gainer.quotes[0].price < 1000) {
                                                "$ " + gainer.quotes[0].price.toString()
                                                    .substring(0, 5)
                                            } else {
                                                "$ " + gainer.quotes[0].price.toString()
                                                    .substring(0, 3) + ".."
                                            }
                                        }
                                    }

                                    val gainerLogoList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            "https://s2.coinmarketcap.com/static/img/coins/64x64/${gainer.id}.png"

                                        }
                                    }
                                    val gainerGraphList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${gainer.id}.png"
                                        }
                                    }
                                    val loserPercentageList = remember(topLosers) {
                                        topLosers.map { loser ->
                                            if (loser.quotes[0].percentChange1h.toString().length > 5) {
                                                loser.quotes[0].percentChange1h.toString()
                                                    .substring(0, 5) + " %"
                                            } else {
                                                loser.quotes[0].percentChange1h.toString() + " %"
                                            }
                                        }
                                    }
                                    val loserPriceList = remember(topGainers) {
                                        topGainers.map { gainer ->
                                            "$ " + gainer.quotes[0].price.toString()
                                                .substring(0, 5)
                                        }
                                    }
                                    val loserLogoList = remember(topLosers) {
                                        topLosers.map { loser ->
                                            "https://s2.coinmarketcap.com/static/img/coins/64x64/${loser.id}.png"

                                        }
                                    }
                                    val loserGraphList = remember(topLosers) {
                                        topLosers.map { loser ->
                                            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${loser.id}.png"
                                        }
                                    }*/
                                    Tabs(
                                        gainers = topGainers,
                                        losers = topLosers,
                                        gainersPercentage = gainerPercentageList,
                                        losersPercentage = loserPercentageList,
                                        onItemClick = { item, isGainer ->

                                            ///{coinId}/{coinSymbol}/{imageUrl}/{price}/{percentage}/{isSaved}
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
                                            val gson =
                                                Gson() // Or use kotlinx.serialization
                                            val coinDataJson = gson.toJson(coinData)
                                            val listType="gainersAndLosers"
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

                            1 -> {
                                bottomBarVisibility = true

                                MarketScreen(
                                    navController = navController,
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                            }
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
                                    }
                                )
                            }
                        }

                        // Scrollable content

                }
                    // Custom Navbar


                    // Error message or loading animation

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
