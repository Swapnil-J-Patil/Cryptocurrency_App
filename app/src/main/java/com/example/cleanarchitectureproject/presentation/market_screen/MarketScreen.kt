package com.example.cleanarchitectureproject.presentation.market_screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser.GainerAndLoserCardItem
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MarketScreen(
    coins: List<CryptoCurrencyCM>,
    onItemClick: (CryptoCurrencyCM, Boolean) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    coinsPercentage: List<String>? = emptyList(),
    coinsPrice: List<String>? = emptyList(),
    coinsLogo: List<String>? = emptyList(),
    coinsGraph: List<String>? = emptyList(), 
) {
    val screenWidth = LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.widthPixels / density }
    val screenHeight = LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.heightPixels / density }

    val listState = rememberLazyGridState()
    val halfScreenWidth = if (screenWidth > 600) screenWidth / 3 else screenWidth
    val adaptiveHeight = screenHeight *0.5
    // Precompute visible indices

    LazyVerticalGrid(
        columns = GridCells.Adaptive(halfScreenWidth.dp),
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(adaptiveHeight.dp)
            .padding(top = 8.dp)
    ) {
        itemsIndexed(coins) { index, gainer ->
            // val color = if (gainer.quotes[0].percentChange24h > 0.0) darkGreen else darkRed

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

            GainerAndLoserCardItem(
                currencyName = gainer.name,
                symbol = gainer.symbol,
                percentage = coinsPercentage?.get(index) ?: "",
                price = coinsPrice?.get(index) ?: "",
                image = coinsGraph?.get(index) ?: "",
                color = darkGreen,
                logo = coinsLogo?.get(index) ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 15.dp)
                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                    .clickable {
                        onItemClick(gainer, true)
                    }
                    .sharedElement(
                        state = rememberSharedContentState(key = "coinCard/${gainer.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
            )
        }
    }
}