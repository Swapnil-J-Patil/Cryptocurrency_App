package com.example.cleanarchitectureproject.presentation.home_screen.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("UnusedBoxWithConstraintsScope", "UnrememberedMutableInteractionSource")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    imageUrls: List<String>,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        val itemSpacing = 16.dp
        val pagerState = rememberPagerState(pageCount = { imageUrls.size })
        val scope = rememberCoroutineScope()

        // Auto-scroll functionality
        LaunchedEffect(Unit) {
            while (true) {
                delay(3000) // Change image every 3 seconds
                val nextPage = (pagerState.currentPage + 1) % imageUrls.size
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
            modifier = modifier,
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(0)
            ),
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = itemSpacing
        ) { page ->
            Image(
                painter = rememberAsyncImagePainter(imageUrls[page]),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(16.dp)) // Rounded corners
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
                    }
            )
        }

        // Dot Indicators
        DotIndicators(
            pageCount = imageUrls.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        )
    }
}



