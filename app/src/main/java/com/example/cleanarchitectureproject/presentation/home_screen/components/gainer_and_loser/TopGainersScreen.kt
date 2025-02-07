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
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TopGainersScreen(
    gainers: List<CryptoCurrencyCM>,
    onItemClick: (CryptoCurrencyCM, Boolean) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    gainersPercentage: List<String>? = emptyList(),
    gainersPrice: List<String>? = emptyList(),
    gainersLogo: List<String>? = emptyList(),
    gainersGraph: List<String>? = emptyList(),
    listType: String
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
        itemsIndexed(gainers, key = { _, gainer -> gainer.id }) { index, gainer ->  // Use key
            val isVisible = remember {
                derivedStateOf {
                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == index }
                }
            }
            val scale = remember { Animatable(0f) }

            LaunchedEffect(isVisible.value) {
                if (isVisible.value) {
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                } else {
                    scale.snapTo(0f) // Reset scale when not visible
                }
            }

            CoinCardItem(
                currencyName = if(gainer.name.length > 10) gainer.name.substring(0,8) +".." else gainer.name,
                symbol = gainer.symbol,
                percentage = gainersPercentage?.getOrNull(index) ?: "",
                price = gainersPrice?.getOrNull(index) ?: "",
                image = gainersGraph?.getOrNull(index) ?: "",
                color = darkGreen,
                logo = gainersLogo?.getOrNull(index) ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 15.dp)
                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                    .clickable {
                        onItemClick(gainer, true)
                    }
                    .sharedElement(
                        state = rememberSharedContentState(key = "coinCard/${listType}_${gainer.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
            )
        }
    }

}

