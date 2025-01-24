package com.example.cleanarchitectureproject.presentation.coin_live_price

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.coin_live_price.components.SupplyInfoCard
import com.example.cleanarchitectureproject.presentation.common_components.PriceLineChart
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.darkRed
import kotlin.math.absoluteValue

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
    coinData: CryptoCoin
) {
    val coinImage = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coinId}.png"
    val prefix = if (isGainer) "+ " else ""
    val color = if (isGainer) darkGreen else darkRed
    val graph = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coinId}.png"
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val tabTitles = listOf("15 Min", "1 Hour", "4 Hours", "1 Day", "1 Week", "1 Month")

    val adaptiveHeight = if (screenWidth > 600.dp) 150.dp else 120.dp
    val adaptiveWeightLogo = if (screenWidth > 600.dp) 0.1f else 0.2f
    val adaptiveWeightDetails = if (screenWidth > 600.dp) 0.7f else 0.53f
    val adaptiveWeightGraph = if (screenWidth > 600.dp) 0.2f else 0.27f
    val lazyColumnPadding=if (screenWidth > 600.dp) 45.dp else 35.dp

    val circularPercentage = if (coinData.totalSupply != null) {
        (coinData.circulatingSupply / coinData.totalSupply) * 100
    } else {
        0f
    }
    val iconSize = if (screenWidth > 600.dp) 50.dp else 40.dp

    var cardHeight by remember { mutableStateOf(adaptiveHeight) }

    val maxCardHeight = adaptiveHeight
    val minCardHeight = 0.dp
    var imageScale by remember { mutableFloatStateOf(1f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Calculate the change in image size based on scroll delta
                val delta = available.y
                val newImageSize = cardHeight + delta.dp
                val previousImageSize = cardHeight

                // Constrain the image size within the allowed bounds
                cardHeight = newImageSize.coerceIn(minCardHeight, maxCardHeight)
                val consumed = cardHeight - previousImageSize

                // Calculate the scale for the image
                imageScale = cardHeight / maxCardHeight

                // Return the consumed scroll amount
                return Offset(0f, consumed.value)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 45.dp, start = 8.dp, end = 8.dp),
    ) {
        // Top Row: Coin Symbol and Save Icon

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = if (isSaved) "Bookmarked" else "Bookmark",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(iconSize)
                    .padding(end = 8.dp, bottom = 5.dp)
            )

        }
        // Row: Coin Image, Price, and Percentage

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(adaptiveHeight)
                .graphicsLayer {
                    scaleX = imageScale
                    scaleY = imageScale
                    // Center the image vertically as it scales
                    translationY = -(maxCardHeight.toPx() - cardHeight.toPx()) / 2f
                }
                .sharedElement(
                    state = rememberSharedContentState(key = "coinCard/${coinId}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .padding(start =  8.dp, end=8.dp,top=55.dp),
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
                    modifier = Modifier.weight(adaptiveWeightDetails),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = coinPrice,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$prefix$coinPercentage%",
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        color = color,
                    )
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



        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = lazyColumnPadding)
                .offset {
                    IntOffset(0, cardHeight.roundToPx())
                }
        ) {
            item {
                Tabs(
                    screen = "livePrice",
                    animatedVisibilityScope = animatedVisibilityScope,
                    tabTitles = tabTitles,
                    onItemClick = { item, flag ->

                    },
                    symbol = coinSymbol
                )
                Spacer(modifier = Modifier.height(12.dp))

            }
            item {
                SupplyInfoCard(
                    circulatingSupply = coinData.circulatingSupply,
                    maxSupply = coinData.maxSupply,
                    totalSupply = coinData.totalSupply,
                    symbol = coinSymbol,
                    circulatingPercentage = circularPercentage.toFloat()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    PriceLineChart(
                        currencyCM = coinData.quotes[0],
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp),
                        isMoreData = true,
                        labelName = coinSymbol
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }


}

