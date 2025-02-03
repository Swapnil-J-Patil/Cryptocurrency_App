package com.example.cleanarchitectureproject.presentation.home_screen.components.currency_row

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.darkRed

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.LazyRowScaleIn(
    items: List<CryptoCurrencyCM>,
    onCardClicked:(CryptoCurrencyCM)->Unit,
    animatedVisibilityScope: AnimatedVisibilityScope) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        itemsIndexed(items, key = { _, items -> items.symbol }) { index, item ->
            // Check if the item is visible
            val isVisible = remember {
                derivedStateOf {
                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == index }
                }
            }

            val scale = remember { Animatable(0f) }
            val prefix = if (item.quotes[0].percentChange1h > 0.0) "+" else ""
            val color = if (item.quotes[0].percentChange1h > 0.0) darkGreen else darkRed

            // Trigger animation when the item becomes visible
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

            CurrencyCardItem(
                currency = item.name,
                percentage = "$prefix ${item.quotes[0].percentChange1h} %",
                image = "https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png",
                modifier = Modifier
                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                    .padding(5.dp),
                color = color,
                onCardClicked = { onCardClicked(item)},
                animatedVisibilityScope = animatedVisibilityScope,
                currencyId = item.id.toString(),
                listType = "lazyRow"
            )
        }
    }
}


