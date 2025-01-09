package com.example.cleanarchitectureproject.presentation.coin_detail

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.coin_detail.components.CoinTag
import com.example.cleanarchitectureproject.presentation.coin_detail.components.PriceLineChart
import com.example.cleanarchitectureproject.presentation.coin_detail.components.TeamListItem
import com.example.cleanarchitectureproject.presentation.ui.theme.green


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
@Composable
fun SharedTransitionScope.CoinDetailScreen(
    coinId: String,
    viewModel: CoinDetailViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.state.value
    val cryptoState=viewModel.cryptoState.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader)) // Ensure the animation is in res/raw folder
    val progress by animateLottieCompositionAsState(composition)
    val alpha = remember { Animatable(0f) } // Start with full transparency
    val offsetX = remember { Animatable(-150f) } // Start slightly off-screen

    LaunchedEffect(Unit) {
        // Smooth alpha fade-in animation
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800, // Adjust the duration for smoothness
                easing = FastOutSlowInEasing // Provides a smooth transition
            )
        )
        // Smooth slide-in animation for horizontal offset
        offsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 800, // Match duration with alpha for consistency
                easing = FastOutSlowInEasing // Smooth deceleration easing
            )
        )
    }

    // Load image with Coil

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 20.dp)
    ) {
        state.coin?.let { coin ->
            if (!state.error.isNotBlank() && !state.isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .sharedElement(
                                    state = rememberSharedContentState(key = "coinCard/$coinId"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${coin.rank}. ${coin.name} (${coin.symbol})",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.weight(8f)
                            )
                            Text(
                                text = if (coin.isActive) "active" else "inactive",
                                color = if (coin.isActive) green else Color.Red,
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .align(CenterVertically)
                                    .weight(2f)
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = coin.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .graphicsLayer(
                                    alpha = alpha.value,
                                    translationX = offsetX.value // Offset to create a slide-in effect
                                )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        cryptoState.cryptocurrency?.let { cryptocurrency ->
                            state.coin?.let { coin ->
                                val matchingCurrency = cryptocurrency.data.find { it.symbol.equals(coin.symbol, ignoreCase = true) }
                                matchingCurrency?.let { currency ->
                                    PriceLineChart(currency = currency,Modifier
                                        .graphicsLayer(
                                            alpha = alpha.value,
                                            translationX = offsetX.value // Offset to create a slide-in effect
                                        )
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .padding(horizontal = 10.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Tags",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .graphicsLayer(
                                    alpha = alpha.value,
                                    translationX = offsetX.value // Offset to create a slide-in effect
                                )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        FlowRow(
                            maxItemsInEachRow = 10,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer(
                                    alpha = alpha.value,
                                    translationX = offsetX.value // Offset to create a slide-in effect
                                )
                        ) {
                            coin.tags.forEach { tag ->
                                CoinTag(tag = tag)
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "Team members",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .graphicsLayer(
                                    alpha = alpha.value,
                                    translationX = offsetX.value // Offset to create a slide-in effect
                                )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    items(coin.team) { teamMember ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 5.dp,
                                    vertical = 5.dp
                                ) // Add vertical padding for spacing between cards
                                .graphicsLayer(
                                    alpha = alpha.value,
                                    translationX = offsetX.value // Offset to create a slide-in effect
                                ),
                            colors = CardDefaults.cardColors(containerColor = Color.White), // Set the background color
                            elevation = CardDefaults.cardElevation(4.dp), // Add elevation for shadow
                            shape = RoundedCornerShape(8.dp) // Rounded corners
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) { // Padding inside the card
                                TeamListItem(
                                    teamMember = teamMember,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp)) // Optional spacing between items
                    }

                }
            }
        }
        if (state.error.isNotBlank() || cryptoState.error.isNotBlank()) {
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
        if (state.isLoading || cryptoState.isLoading) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp) // Set the size of the animation
            )
        }
    }
}
