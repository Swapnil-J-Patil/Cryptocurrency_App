package com.example.cleanarchitectureproject.presentation.coin_live_price

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.coin_live_price.components.LiquidityWarning
import com.example.cleanarchitectureproject.presentation.coin_live_price.components.SupplyInfoCard
import com.example.cleanarchitectureproject.presentation.common_components.PriceLineChart
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.common_components.FlipIcon
import com.example.cleanarchitectureproject.presentation.shared.SavedCoinViewModel
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import com.example.cleanarchitectureproject.presentation.ui.theme.red
import com.google.gson.Gson
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CoinLivePriceScreen(
    coinId: String,
    coinSymbol: String,
    coinPrice: String,
    coinPercentage: String,
    isSaved: Boolean,
    isGainer: Boolean,
    animatedVisibilityScope: AnimatedVisibilityScope,
    coinData: CryptoCoin,
    navController: NavController,
    listType: String,
    viewModel: SavedCoinViewModel = hiltViewModel(),
    ) {
    val coinImage = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coinId}.png"
    val prefix = if (isGainer) "+ " else ""
    val color = if (isGainer) green else lightRed
    val graph = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coinId}.png"
    val configuration = LocalConfiguration.current
    val isSavedState = remember { mutableStateOf(isSaved) }
    val isLowLiquidity = viewModel.checkLiquidity(crypto = coinData)
    val screenWidth = configuration.screenWidthDp.dp
    val tabTitles = listOf("15 Min", "1 Hour", "4 Hours", "1 Day", "1 Week", "1 Month")
    val coinValue by viewModel.coinLivePrice.observeAsState()
    var livePrice by remember {
        mutableStateOf(coinPrice)
    }
    val adaptiveHeight = if (screenWidth > 600.dp) 100.dp else 75.dp
    val adaptiveWeightLogo = if (screenWidth > 600.dp) 0.08f else 0.2f
    val adaptiveWeightDetails = if (screenWidth > 600.dp) 0.7f else 0.53f
    val adaptiveWeightGraph = if (screenWidth > 600.dp) 0.2f else 0.27f

    val circularPercentage = if (coinData.totalSupply != null) {
        (coinData.circulatingSupply?.div(coinData.totalSupply))?.times(100)
    } else {
        0f
    }
    val iconSize = if (screenWidth > 600.dp) 50.dp else 40.dp
    val degree = if (isGainer) 270f else 90f
    val rotationMax = 360f
    val rotationMin = 0f

    val isSelected = remember {
        mutableStateOf(false)
    }
    val animatedAlpha by animateFloatAsState(targetValue = if (isSelected.value) 1f else .5f)
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected.value) 26.dp else 20.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )

    LaunchedEffect(Unit) {
        viewModel.startFetchingCoinStats(coinId.toInt())
        delay(1000L) // Delay for 1 second (1000 milliseconds)
        isSelected.value = true
    }
    LaunchedEffect(coinValue) {
        val formattedPrice= coinValue?.let { viewModel.formatPrice(it) }
        if (formattedPrice != null) {
            //Log.d("FormattedPrice", "Price: $formattedPrice")
            livePrice=formattedPrice
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopFetchingCoinStats()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 45.dp, start = 8.dp, end = 8.dp),
    ) {
        // Top Row: Coin Symbol and Save Icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        ) {
            Text(
                text = coinSymbol,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )

            Icon(
                imageVector = if (isSavedState.value) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = if (isSavedState.value) "Bookmarked" else "Bookmark",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(iconSize)
                    .clickable {
                        if (!isSavedState.value) {
                            viewModel.addCrypto(coinData) // Save the coin
                        } else {
                            viewModel.removeCrypto(coinData) // Remove the coin
                        }
                        isSavedState.value = !isSavedState.value // Toggle state
                    }
                    .padding(end = 8.dp, bottom = 5.dp)
            )

        }
        // Row: Coin Image, Price, and Percentage

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(adaptiveHeight)
                        .sharedElement(
                            state = rememberSharedContentState(key = "coinCard/${listType}_${coinId}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .padding(start = 8.dp, end = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary), // Set the background color
                    elevation = CardDefaults.cardElevation(4.dp), // Add elevation for shadow
                    shape = RoundedCornerShape(8.dp) // Rounded corners
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Currency Logo (10%)

                        Image(
                            painter = rememberAsyncImagePainter(coinImage),
                            contentDescription = "Currency Logo",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .clip(CircleShape)
                                .fillMaxHeight()
                                .weight(adaptiveWeightLogo)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        // Price and Percentage (20%)
                        Column(
                            modifier = Modifier
                                .weight(adaptiveWeightDetails)
                                .padding(top = 2.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = livePrice,
                                maxLines = 1,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(x = -6.dp)
                            ) {
                                FlipIcon(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .alpha(animatedAlpha)
                                        .rotate(degree)
                                        .size(animatedIconSize),
                                    isActive = isSelected.value,
                                    activeIcon = Icons.Filled.PlayArrow,
                                    inactiveIcon = Icons.Filled.PlayArrow,
                                    contentDescription = "Percentage gain or lose icon",
                                    color = color,
                                    rotationMax = rotationMax,
                                    rotationMin = rotationMin
                                )
                                Text(
                                    text = "$prefix$coinPercentage %",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontStyle = FontStyle.Italic,
                                    color = color,
                                )
                            }
                        }

                        // Graph Image (40%)

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = graph,
                                error = painterResource(id = R.drawable.placeholder) // Replace with your drawable resource
                            ),
                            contentDescription = "Graph Image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .weight(adaptiveWeightGraph)
                                .padding(end = 10.dp),
                        )
                    }

                }
            }
            item {
                Tabs(
                    screen = "livePrice",
                    animatedVisibilityScope = animatedVisibilityScope,
                    tabTitles = tabTitles,
                    onItemClick = { item, flag ->

                    },
                    onFilter = {

                    },
                    symbol = coinSymbol,
                    onAuthClick = { type, method, email, password ->

                    }
                )
                Spacer(modifier = Modifier.height(12.dp))

            }
            item {
                coinData.circulatingSupply?.let {
                    coinData.totalSupply?.let { it1 ->
                        circularPercentage?.let { it2 ->
                            SupplyInfoCard(
                                circulatingSupply = it,
                                maxSupply = coinData.maxSupply,
                                totalSupply = it1,
                                symbol = coinSymbol,
                                circulatingPercentage = it2.toFloat(),
                                color = color
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                val listTypeNew = "lineChart"
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp))
                        .sharedElement(
                            state = rememberSharedContentState(key = "coinChart/${listTypeNew}_${coinId}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .clickable {
                            val gson = Gson() // Or use kotlinx.serialization
                            val coinDataJson = gson.toJson(coinData)
                            val flag = false
                            navController.navigate(Screen.ZoomedChart.route + "/${coinId}/${coinDataJson}/${flag}/${listTypeNew}/${isGainer}")
                        }
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    PriceLineChart(
                        currencyCM = coinData.quotes?.get(0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp),
                        labelName = coinSymbol,
                        color1 = if (isGainer) green else red,
                        color2 = if (isGainer) lightGreen else lightRed
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                LiquidityWarning(isLowLiquidity)
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Ensures proper spacing
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f) // Ensures both buttons take equal width
                            .border(2.dp, green, RoundedCornerShape(8.dp))
                            .sharedElement(
                                state = rememberSharedContentState(key = "coinCardTransaction/${"buy"}_${coinId}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            val gson = Gson() // Or use kotlinx.serialization
                            val coinDataJson = gson.toJson(coinData)
                            navController.navigate(Screen.TransactionScreen.route + "/${"buy"}/${coinDataJson}") {
                                launchSingleTop = true
                            }
                        },
                        contentPadding = PaddingValues(vertical = 16.dp),
                    ) {
                        Text(
                            text = "Buy",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f) // Ensures both buttons take equal width
                            .border(2.dp, lightRed, RoundedCornerShape(8.dp))
                            .sharedElement(
                                state = rememberSharedContentState(key = "coinCardTransaction/${"sell"}_${coinId}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            val gson = Gson() // Or use kotlinx.serialization
                            val coinDataJson = gson.toJson(coinData)
                            navController.navigate(Screen.TransactionScreen.route + "/${"sell"}/${coinDataJson}") {
                                launchSingleTop = true
                            }
                        },
                        contentPadding = PaddingValues(vertical = 16.dp),
                    ) {
                        Text(
                            text = "Sell",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }

}

