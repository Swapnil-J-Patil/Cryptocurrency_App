package com.swapnil.dreamtrade.presentation.onboarding_screen.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swapnil.dreamtrade.presentation.onboarding_screen.helper.bubblePagerFlingBehavior
import com.swapnil.dreamtrade.presentation.onboarding_screen.helper.calculateBubbleDimensions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BubblePager(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    bubbleMinRadius: Dp = 40.dp,
    bubbleMaxRadius: Dp = 12000.dp,
    bubbleBottomPadding: Dp = 140.dp,
    bubbleColors: List<Color>,
    content: @Composable PagerScope.(Int) -> Unit,
) {
    Box(modifier = modifier) {
        HorizontalPager(
            count = pageCount,
            state = pagerState,
            flingBehavior = bubblePagerFlingBehavior(pagerState),
            modifier = Modifier.drawBehind {
                drawRect(color = bubbleColors[pagerState.currentPage], size = size)
                val (radius, centerX) = calculateBubbleDimensions(
                    swipeProgress = pagerState.currentPageOffset,
                    easing = CubicBezierEasing(1f, 0f, .92f, .62f),
                    minRadius = bubbleMinRadius,
                    maxRadius = bubbleMaxRadius
                )
                drawBubble(
                    radius = radius,
                    centerX = centerX,
                    bottomPadding = bubbleBottomPadding,
                    color = Color.White
                )
            }
        ) { page ->
            content(page)
        }
    }
}
