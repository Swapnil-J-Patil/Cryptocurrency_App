package com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

@Composable
fun GainersLosersTabs(
    gainers: List<CryptoCurrencyCM>,
    losers: List<CryptoCurrencyCM>
) {
    val tabTitles = listOf("Top Gainers", "Top Losers")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope() // Use Compose's coroutine scope

    Column(modifier = Modifier.fillMaxSize()) {
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
                0 -> TopGainersScreen(gainers) // Content for "Top Gainers"
                1 -> TopLosersScreen(losers)   // Content for "Top Losers"
            }
        }
    }
}


