package com.example.cleanarchitectureproject.presentation.common_components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.presentation.coin_live_price.components.WebViewItem
import com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser.TopGainersScreen
import com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser.TopLosersScreen
import com.example.cleanarchitectureproject.presentation.transaction_screen.components.TransactionCard
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.red
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
    symbol: String? = null,
    listType: String?=null,
    coin: CryptoCoin?=null,
    transaction: String?=null
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabTitles.size })
    val pagerStateForTransaction=rememberPagerState(initialPage = if(transaction=="buy") 0 else 1, pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()
    val screenWidth = LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.widthPixels / density }
    val screenHeight = LocalDensity.current.run { androidx.compose.ui.platform.LocalContext.current.resources.displayMetrics.heightPixels / density }

    val width = screenWidth * 0.3
    val height=if(screenWidth> 600) screenHeight*0.9 else screenHeight*0.5
    val isDarkTheme = isSystemInDarkTheme()
    val theme=if(isDarkTheme)"Dark" else "Light"

    val urls = remember {
        listOf(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=15&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=$theme&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT",
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=1H&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=$theme&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT",
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=4H&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=$theme&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT",
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=$theme&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT",
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=W&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=$theme&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT",
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=M&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=$theme&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT",

            )
    }

    // var time by remember { mutableStateOf("15") }
    Column(modifier = Modifier.fillMaxSize()) {
        when (screen) {
            "home" -> {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            modifier = Modifier.weight(1f),
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
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
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
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
                                gainersGraph = gainersGraph,
                                listType = listType!!
                            )
                        }
                        1 -> losers?.let {
                            TopLosersScreen(
                                it,
                                onItemClick = { item, isGainer -> onItemClick(item, isGainer) },
                                animatedVisibilityScope,
                                losersPercentage = losersPercentage,
                                losersPrice = losersPrice,
                                losersLogo = losersLogo,
                                losersGraph = losersGraph,
                                listType = listType!!
                            )
                        }
                    }
                }
            }
            "transaction" -> {
                TabRow(
                    selectedTabIndex = pagerStateForTransaction.currentPage,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .background(MaterialTheme.colorScheme.tertiary)
                    ,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[pagerStateForTransaction.currentPage])
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            modifier = Modifier.weight(1f)
                                .background(MaterialTheme.colorScheme.tertiary)
                            ,
                            selected = pagerStateForTransaction.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerStateForTransaction.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (pagerStateForTransaction.currentPage == index)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerStateForTransaction,
                    modifier = Modifier
                        .fillMaxSize()
                ) { page ->
                    when (page) {
                        0 ->  TransactionCard(
                            price = coin?.quotes?.get(0)?.price.toString(),
                            firstText = "Buy For",
                            secondText = "Quantity",
                            buttonText = "BUY",
                            color = green,
                            firstPrefix = "$",
                            secondPrefix = coin?.symbol.toString()
                        )
                        1 ->TransactionCard(
                            price = coin?.quotes?.get(0)?.price.toString(),
                            firstText = "Quantity",
                            secondText = "Sell For",
                            buttonText = "SELL",
                            color = red,
                            firstPrefix = coin?.symbol.toString(),
                            secondPrefix = "$"
                        )
                    }
                }
            }
            else -> {
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.width(screenWidth.dp),
                    edgePadding = 0.dp,
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
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            modifier = Modifier.width(width.dp).background(MaterialTheme.colorScheme.background),
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
                        .fillMaxSize()
                        .padding(top=24.dp,bottom=10.dp)
                        .background(MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.Top

                ) { page ->
                    WebViewItem(url = urls[page], Modifier.height(height.dp).background(MaterialTheme.colorScheme.background))
                }
            }
        }
    }
}



