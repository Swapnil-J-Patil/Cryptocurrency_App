package com.swapnil.dreamtrade.presentation.onboarding_screen.helper

import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerDefaults
import com.google.accompanist.pager.PagerState
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun bubblePagerFlingBehavior(pagerState: PagerState) =
    PagerDefaults.flingBehavior(
        state = pagerState,
        snapAnimationSpec = spring(dampingRatio = 1.9f, stiffness = 600f),
    )

@OptIn(ExperimentalPagerApi::class)
fun PagerState.getBubbleColor(bubbleColors: List<Color>): Color {
    val index = if (currentPageOffset < 0) {
        currentPage - 1
    } else nextSwipeablePageIndex
    return bubbleColors[index]
}

@OptIn(ExperimentalPagerApi::class)
fun PagerState.getNextBubbleColor(bubbleColors: List<Color>): Color {
    return bubbleColors[nextSwipeablePageIndex]
}

@OptIn(ExperimentalPagerApi::class)
fun PagerState.shouldHideBubble(isDragged: Boolean): Boolean = derivedStateOf {
    var b = false
    if (isDragged) {
        b = true
    }
    if (currentPageOffset.absoluteValue > 0.1) {
        b = true
    }
    b
}.value

@OptIn(ExperimentalPagerApi::class)
val PagerState.nextSwipeablePageIndex: Int
    get() = if ((currentPage + 1) == pageCount) currentPage - 1 else currentPage + 1

fun lerp(start: Float, end: Float, value: Float): Float {
    return start + (end - start) * value
}