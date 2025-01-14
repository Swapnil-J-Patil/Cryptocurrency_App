package com.example.cleanarchitectureproject.presentation.home_screen.components.carousel

import android.annotation.SuppressLint
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.presentation.common_components.PriceLineChart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("UnusedBoxWithConstraintsScope", "UnrememberedMutableInteractionSource")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    modifier: Modifier = Modifier,
    onClick:(Int)->Unit,
    dotsPadding: Dp,
    currency:List<CryptoCurrencyCM>
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        val itemSpacing = 16.dp
        val pagerState = rememberPagerState(pageCount = { currency.size})
        val scope = rememberCoroutineScope()

        // Auto-scroll functionality
        LaunchedEffect(Unit) {
            while (true) {
                delay(4000) // Change image every 3 seconds
                val nextPage = (pagerState.currentPage + 1) % currency.size
                scope.launch {
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(
                            durationMillis = 800, // Smooth scroll duration
                            easing = LinearOutSlowInEasing // Smooth easing
                        )
                    )
                }
            }
        }

        // HorizontalPager
        HorizontalPager(
            modifier = Modifier.fillMaxSize(), // Use a simple modifier
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(0)
            ),
            contentPadding = PaddingValues(horizontal = 5.dp),
            pageSpacing = itemSpacing
        ) { page ->

            PriceLineChart(currencyCM = currency.get(page).quotes[0],
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp, vertical = 16.dp)
                    .clickable {
                        onClick(page)
                    }
                    //.background(MaterialTheme.colorScheme.surfaceContainer)
                   // .clip(RoundedCornerShape(16.dp)) // Rounded corners
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                ).absoluteValue

                        // Smoother scaling and alpha effects
                        val easingFraction = CubicBezierEasing(0.25f, 0.8f, 0.25f, 1f).transform(
                            1f - pageOffset.coerceIn(0f, 1f)
                        )

                        alpha = lerp(start = 0.8f, stop = 1f, fraction = easingFraction)
                        scaleY = lerp(start = 0.9f, stop = 1f, fraction = easingFraction)
                        scaleX = scaleY
                    },
                isMoreData = true,
                labelName = currency.get(page).name
            )
        }

        // Dot Indicators
        DotIndicators(
            pageCount = currency.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(dotsPadding)
        )
    }
}



