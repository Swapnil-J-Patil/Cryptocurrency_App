package com.example.cleanarchitectureproject.presentation.profile_screen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.data.local.shared_prefs.PrefsManager
import com.example.cleanarchitectureproject.domain.model.PortfolioCoin
import com.example.cleanarchitectureproject.domain.model.ProfileData
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.profile_screen.components.FiltersPopup
import com.example.cleanarchitectureproject.presentation.profile_screen.components.ProfileDetailView
import com.example.cleanarchitectureproject.presentation.profile_screen.components.ProfileImageItem
import com.example.cleanarchitectureproject.presentation.shared.KeyStoreViewModel
import com.example.cleanarchitectureproject.presentation.shared.PortfolioViewModel
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.example.cleanarchitectureproject.presentation.ui.theme.lightBackground
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfileScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    keyStoreViewModel: KeyStoreViewModel = hiltViewModel(),
    portfolioViewModel: PortfolioViewModel = hiltViewModel(),
    context: Context
) {
    //viewModel.loadCrypto()
    val portfolioCoinList by portfolioViewModel.currencyList.observeAsState()
    val portfolioValue by portfolioViewModel.portfolioValue.observeAsState()
    val totalInvestment by portfolioViewModel.totalInvestment.observeAsState()
    val prefsManager = remember { PrefsManager(context) }
    val dollars = prefsManager.getDollarAmount()
    var isFilterClicked by remember {
        mutableStateOf(false)
    }
    val tokens by keyStoreViewModel.tokens.collectAsState()
    var selectedProfile by remember { mutableStateOf<ProfileData?>(null) }
    var currentProfile by remember { mutableStateOf<ProfileData>(ProfileDataList.characters.first()) }
    var currentName by remember { mutableStateOf("Swapnil Patil") }
    val tabTitles = listOf("Portfolio", "Transactions")
    var visibility by remember {
        mutableStateOf(false)
    }
    var currentFilters by remember { mutableStateOf(listOf(false, false, false, false)) }

    val comparators = mutableListOf<Comparator<PortfolioCoin>>()

    if (currentFilters[0]) comparators.add(compareByDescending<PortfolioCoin> {
        it.currentPrice ?: 0.0
    }) // Sort by currentValue
    if (currentFilters[1]) comparators.add(compareByDescending<PortfolioCoin> { it.returns }) // Sort by returns
    if (currentFilters[2]) comparators.add(compareByDescending<PortfolioCoin> { it.returnPercentage }) // Sort by percentChange
    if (currentFilters[3]) comparators.add(compareBy<PortfolioCoin> { it.symbol }) // Sort by name (A-Z)

// Combine all selected comparators correctly
    val finalComparator = comparators.reduceOrNull { acc, comparator -> acc.then(comparator) }

// Apply sorting only if filters are selected, else return original list
    val sortedCoins = portfolioCoinList?.let { list ->
        finalComparator?.let { list.sortedWith(it) } ?: list
    } ?: listOf()


    val brushColors = listOf(Color(0xFF23af92), Color(0xFF0E5C4C))
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val targetOffset = with(LocalDensity.current) {
        1000.dp.toPx()
    }
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetOffset, // Adjust based on desired movement range
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    LaunchedEffect(tokens) {
        if (!tokens.isNullOrEmpty()) {
            //Log.d("tokensKeystore", "ProfileScreen: ${tokens}")
            currentName = tokens.get(0)
            if (tokens.size >= 3) {
                currentProfile = ProfileDataList.characters.get(tokens.get(2).toInt())
            }
        }
    }
    LaunchedEffect(sortedCoins) {

        if (!sortedCoins.isNullOrEmpty()) {
            visibility = true
        }
        else
        {
            delay(1000)
            visibility = true
        }
    }
    LaunchedEffect(Unit) {
        portfolioViewModel.startFetchingCoinStats()
    }
    DisposableEffect(Unit) {
        onDispose {
            portfolioViewModel.stopFetchingCoinStats()
        }
    }
    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val brushSize = 400f
                    val brush = Brush.linearGradient(
                        colors = brushColors,
                        start = Offset(animatedOffset, animatedOffset),
                        end = Offset(animatedOffset + brushSize, animatedOffset + brushSize),
                        tileMode = TileMode.Mirror
                    )
                    onDrawBehind {
                        drawRect(brush)
                    }
                }

        ) {
            Row(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(top = 45.dp, start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    visible = selectedProfile == null,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Box(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "profile-bounds"),
                                animatedVisibilityScope = this
                            )
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable {
                                selectedProfile = currentProfile
                            }
                    ) {
                        ProfileImageItem(
                            profile = currentProfile,
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(key = "profile-image"),
                                animatedVisibilityScope = this@AnimatedVisibility
                            ),
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 80.dp, top = 42.dp), // Ensures text doesn't shift
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hello",
                    style = MaterialTheme.typography.titleMedium,
                    color = lightBackground,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins
                )
                Text(
                    text = currentName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontFamily = Poppins
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp, top = 42.dp), // Ensures text doesn't shift
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.RewardedAdScreen.route)
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.White
                    )
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(top = 115.dp),
            ) {
                AnimatedVisibility(
                    visible = visibility,
                    enter = slideInVertically(
                        initialOffsetY = { it }, // Starts from full height (bottom)
                        animationSpec = tween(
                            durationMillis = 800, // Slightly increased duration for a smoother feel
                            easing = LinearOutSlowInEasing // Smoother easing for entering animation
                        )
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 800,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it }, // Moves to full height (bottom)
                        animationSpec = tween(
                            durationMillis = 800,
                            easing = FastOutSlowInEasing // Keeps a natural exit motion
                        )
                    ) + fadeOut(
                        animationSpec = tween(
                            durationMillis = 800,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        elevation = CardDefaults.cardElevation(16.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        if(!sortedCoins.isNullOrEmpty())
                        {
                            sortedCoins.let {
                                val portfolioPercentage =
                                    ((portfolioValue?.minus(totalInvestment!!))?.div(totalInvestment!!))?.times(
                                        100
                                    )

                                if (dollars != null) {
                                    Tabs(
                                        screen = "profile",
                                        tabTitles = tabTitles,
                                        onItemClick = { item, flag ->

                                        },
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        onAuthClick = { type, method, email, password ->

                                        },
                                        portfolioCoins = it,
                                        portfolioValue = portfolioValue,
                                        portfolioPercentage = portfolioPercentage,
                                        dollars = dollars.toDouble(),
                                        totalInvestment = totalInvestment,
                                        onFilter = {
                                            isFilterClicked = true
                                        },
                                    )
                                }
                            }
                        }
                        else
                        {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.wallet),
                                    contentDescription = "wallet",
                                    modifier = Modifier
                                        .size(200.dp) // Ensure image is smaller than the border container
                                        .padding(6.dp)
                                        .background(Color.Transparent),
                                    contentScale = ContentScale.Fit
                                )
                                Text(
                                    text = "Your portfolio is empty!",
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 10.dp)
                                )
                            }
                        }

                    }
                }
            }



            AnimatedVisibility(
                visible = isFilterClicked,
                enter = scaleIn(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ),
                exit = scaleOut()
            ) {
                FiltersPopup(
                    onCancel = { isFilterClicked = false },
                    filter = "sort",
                    isTab = false,
                    currentFilters = remember { mutableStateOf(currentFilters) }, // Pass the current filters
                    onFilters = {
                        currentFilters =
                            it // Update the current filters when onFilters is triggered
                    }
                )
            }
        }

        // Profile Detail View
        ProfileDetailView(
            profile = selectedProfile,
            onSave = { profile, username ->
                currentProfile = profile
                currentName = username
                val index = profile.id - 1
                keyStoreViewModel.clearTokens()

                keyStoreViewModel.saveToken(username)
                keyStoreViewModel.saveToken("dummyemail@gmail.com")
                keyStoreViewModel.saveToken(index.toString())

                selectedProfile = null
            },
            onDisMiss = {
                selectedProfile = null
            },
            userName = currentName,
            profileList = ProfileDataList.characters.filter { it != currentProfile }
        )
    }
}



