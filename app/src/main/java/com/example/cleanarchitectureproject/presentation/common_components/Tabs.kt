package com.example.cleanarchitectureproject.presentation.common_components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser.TopGainersScreen
import com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser.TopLosersScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.Tabs(
    gainers: List<CryptoCurrencyCM>? = emptyList(),
    losers: List<CryptoCurrencyCM>? = emptyList(),
    gainersPercentage: List<String>? = emptyList(),
    losersPercentage: List<String>? = emptyList(),
    onItemClick: (CryptoCurrencyCM, Boolean) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    screen: String,
    tabTitles: List<String>,
    gainersPrice: List<String>? = emptyList(),
    losersPrice: List<String>? = emptyList(),
    gainersLogo: List<String>? = emptyList(),
    losersLogo: List<String>? = emptyList(),
    gainersGraph: List<String>? = emptyList(),
    losersGraph: List<String>? = emptyList(),
) {
    // val tabTitles = listOf("Top Gainers", "Top Losers")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope() // Use Compose's coroutine scope
    val screenWidth =
        LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.widthPixels / density }
    val width = screenWidth * 0.3
    Column(modifier = Modifier.fillMaxSize()) {

        if (screen.equals("home")) {
            // Tabs
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(currentTabPosition = tabPositions[pagerState.currentPage])
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier.weight(1f), // Ensure equal width for tabs
                        selected = pagerState.currentPage == index,
                        onClick = {
                            // Use the coroutine scope from Compose
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index) // Smooth animation between tabs
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (pagerState.currentPage == index)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                ),
                                modifier = Modifier.fillMaxWidth(), // Align text properly in full width
                                textAlign = TextAlign.Center // Center the text within the tab
                            )
                        }
                    )
                }
            }
            // Pager for smooth animations
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize() // Let the pager take up the remaining space
            ) { page ->

                when (page) {
                    0 -> gainers?.let {
                        TopGainersScreen(
                            it,
                            onItemClick = { item, isGainer -> onItemClick(item, isGainer) },
                            animatedVisibilityScope = animatedVisibilityScope,
                            gainersPercentage = gainersPercentage,
                            gainersPrice = gainersPrice,
                            gainersLogo = gainersLogo,
                            gainersGraph=gainersGraph
                        )
                    } // Content for "Top Gainers"
                    1 -> losers?.let {
                        TopLosersScreen(
                            it,
                            onItemClick = { item, isGainer -> onItemClick(item, isGainer) },
                            animatedVisibilityScope,
                            losersPercentage = losersPercentage,
                            losersPrice = losersPrice,
                            losersLogo = losersLogo,
                            losersGraph = losersGraph
                        )
                    }   // Content for "Top Losers"
                }
            }
        } else {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.width(screenWidth.dp),
                edgePadding = 0.dp, // Remove padding to ensure tabs start at the edge
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index) // Smooth animation between tabs
                            }
                        },
                        modifier = Modifier.width(width.dp), // Ensures tabs take equal width
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (pagerState.currentPage == index)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize() // Let the pager take up the remaining space
            ) { page ->

            }
        }


    }
}


