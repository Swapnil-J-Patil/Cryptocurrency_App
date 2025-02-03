package com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser

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
import com.example.cleanarchitectureproject.presentation.common_components.CoinCardItem
import com.example.cleanarchitectureproject.presentation.ui.theme.darkRed

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TopLosersScreen(
    losers: List<CryptoCurrencyCM>,
    onItemClick:(CryptoCurrencyCM,Boolean)->Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    losersPercentage: List<String>? = emptyList(),
    losersPrice: List<String>? = emptyList(),
    losersLogo: List<String>? = emptyList(),
    losersGraph: List<String>? = emptyList(),
    listType:String
) {
    val screenWidth = LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.widthPixels / density }
    val screenHeight = LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.heightPixels / density }

    val listState = rememberLazyGridState()
    val halfScreenWidth = if(screenWidth > 600) screenWidth / 3 else screenWidth
    val adaptiveHeight = screenHeight *0.5

    LazyVerticalGrid(
        columns = GridCells.Adaptive(halfScreenWidth.dp),
        state = listState, // Pass the state here
        modifier = Modifier
            .fillMaxWidth()
            .height(adaptiveHeight.dp)
            .padding(top = 8.dp)
    ) {
        itemsIndexed(losers) { index, loser ->
           // val color = if (loser.quotes[0].percentChange24h > 0.0) darkGreen else darkRed

            // Animate visibility
            val isVisible = remember {
                derivedStateOf {
                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == index }
                }
            }
            val scale = remember { Animatable(0f) }
            //val hasAnimated = remember { mutableStateOf(false) }

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

            CoinCardItem(
                currencyName = loser.name,
                symbol = loser.symbol,
                percentage = losersPercentage?.get(index) ?: "",
                price = losersPrice?.get(index) ?: "",
                image = losersGraph?.get(index) ?: "",
                color = darkRed,
                logo = losersLogo?.get(index) ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                    .padding(vertical = 8.dp, horizontal = 15.dp)
                    .clickable {
                        onItemClick(loser,false)
                    }
                    .sharedElement(
                        state = rememberSharedContentState(key = "coinCard/${listType}_${loser.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
            )
        }
    }
}

