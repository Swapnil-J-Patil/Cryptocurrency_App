package com.example.cleanarchitectureproject.presentation.coin_live_price

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.darkRed

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CoinLivePriceScreen(
    coinId: String,
    coinSymbol: String,
    coinPrice: String,
    coinPercentage: String,
    isSaved: Boolean,
    isGainer: Boolean,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val coinImage = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coinId}.png"
    val prefix = if (isGainer) "+ " else ""
    val color = if (isGainer) darkGreen else darkRed
    val graph = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coinId}.png"
    val screenWidth = LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.widthPixels / density }
    val tabTitles = listOf("15 Min", "1 Hour", "4 Hour", "1 Day", "1 Week", "1 Month")

    val adaptiveHeight = if (screenWidth > 600) screenWidth*0.2 else screenWidth*0.25
    val adaptiveWeight= if (screenWidth > 600) 0.1f else 0.2f
    val iconSize=if (screenWidth > 600) 50.dp else 40.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 45.dp, start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Row: Coin Symbol and Save Icon
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .sharedElement(
                    state = rememberSharedContentState(key = "coinCard/${coinId}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
        ) {

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
                    .height(adaptiveHeight.dp)
                    .padding(vertical = 8.dp, horizontal = 8.dp),
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
                            .weight(adaptiveWeight)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    // Price and Percentage (20%)
                    Column(
                        modifier = Modifier.weight(0.5f),
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
                    Box(
                        modifier = Modifier
                            .weight(0.3f)
                            .padding(horizontal = 10.dp)
                            .aspectRatio(2.5f),
                        contentAlignment = Alignment.Center // Center the icon inside the box
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = graph,
                                error = painterResource(id = R.drawable.placeholder) // Replace with your drawable resource
                            ),
                            contentDescription = "Graph Image",
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Tabs(
                screen = "livePrice",
                animatedVisibilityScope = animatedVisibilityScope,
                tabTitles = tabTitles,
                onItemClick = {item, flag->

                }
            )
        }


    }
}

