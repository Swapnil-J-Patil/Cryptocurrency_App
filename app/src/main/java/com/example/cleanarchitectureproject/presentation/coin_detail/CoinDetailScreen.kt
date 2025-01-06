package com.example.cleanarchitectureproject.presentation.coin_detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.cleanarchitectureproject.presentation.coin_detail.components.TeamListItem


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
@Composable
fun SharedTransitionScope.CoinDetailScreen(
    coinId: String,
    viewModel: CoinDetailViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.state.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader)) // Ensure the animation is in res/raw folder
    val progress by animateLottieCompositionAsState(composition)
    val alpha = remember { Animatable(0f) } // Start with full transparency
    val offsetX = remember { Animatable(-50f) } // Start slightly off-screen

    LaunchedEffect(Unit) {
        // Animate the alpha to 1f (fully visible)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        // Animate the offset to 0 (position in place)
        offsetX.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }
    // Load image with Coil

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
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
                                color = if (coin.isActive) Color.Green else Color.Red,
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
                        Text(
                            text = "Tags",
                            style = MaterialTheme.typography.bodyMedium,
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
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .graphicsLayer(
                                    alpha = alpha.value,
                                    translationX = offsetX.value // Offset to create a slide-in effect
                                )
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    items(coin.team) { teamMember ->
                        TeamListItem(
                            teamMember = teamMember,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .graphicsLayer(
                                    alpha = alpha.value,
                                    translationX = offsetX.value // Offset to create a slide-in effect
                                )
                        )
                        Divider()
                        Spacer(modifier = Modifier.height(3.dp))
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
                    .size(250.dp) // Set the size of the animation
            )
        }
    }
}
