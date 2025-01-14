package com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.presentation.ui.theme.darkGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.darkRed

@Composable
fun TopLosersScreen(losers: List<CryptoCurrencyCM>) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState, // Pass the state here
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min=1000.dp,max = 2000.dp)
            .padding(top = 8.dp)
    ) {
        itemsIndexed(losers) { index, loser ->
            val color = if (loser.quotes[0].percentChange24h > 0.0) darkGreen else darkRed

            // Animate visibility
            val isVisible = remember {
                derivedStateOf {
                    val visibleItems = listState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == index }
                }
            }
            val scale = remember { Animatable(0f) }
            val hasAnimated = remember { mutableStateOf(false) }

            LaunchedEffect(isVisible.value) {
                if (isVisible.value && !hasAnimated.value) {
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    )
                    hasAnimated.value = true
                }
            }

            GainerAndLoserCardItem(
                currencyName = loser.name,
                symbol = loser.symbol,
                percentage = loser.quotes[0].percentChange24h.toString().substring(0, 5) + " %",
                price = "$ " + loser.quotes[0].price.toString().substring(0, 5),
                image = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${loser.id}.png",
                color = color,
                logo = "https://s2.coinmarketcap.com/static/img/coins/64x64/${loser.id}.png",
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                    .padding(vertical = 8.dp, horizontal = 15.dp)
            )
        }
    }
}

