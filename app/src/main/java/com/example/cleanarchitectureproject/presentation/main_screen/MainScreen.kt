package com.example.cleanarchitectureproject.presentation.main_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanarchitectureproject.presentation.Navbar
import com.example.cleanarchitectureproject.presentation.main_screen.components.navbar.BottomNavAnimation
import com.example.cleanarchitectureproject.presentation.home_screen.HomeScreen
import com.example.cleanarchitectureproject.presentation.home_screen.HomeScreenTab
import com.example.cleanarchitectureproject.presentation.market_screen.MarketScreen
import com.example.cleanarchitectureproject.presentation.market_screen.MarketScreenTab
import com.example.cleanarchitectureproject.presentation.profile_screen.ProfileScreen
import com.example.cleanarchitectureproject.presentation.profile_screen.ProfileScreenTab
import com.example.cleanarchitectureproject.presentation.saved_coin_screen.SavedCoinsScreen
import com.example.cleanarchitectureproject.presentation.saved_coin_screen.SavedCoinsScreenTab

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainScreen(
    navController: NavController,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val screen = listOf(
        Navbar.Home,
        Navbar.Market,
        Navbar.Saved,
        Navbar.Profile
    )

    val configuration = LocalConfiguration.current
    val isTab = configuration.screenWidthDp.dp > 600.dp

    var selectedTab by rememberSaveable(stateSaver = Saver(
        save = { it },
        restore = { it }
    )) { mutableStateOf(0) }
    var bottomBarVisibility by remember { mutableStateOf(true) }
    val isMarketScreen by mainScreenViewModel.currentTab.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isTab) {
            when (isMarketScreen) {
                "market" -> {
                    bottomBarVisibility = true
                    MarketScreenTab(
                        navController = navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

                "saved" -> {
                    SavedCoinsScreenTab(
                        navController = navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

                "profile" -> {
                    ProfileScreenTab(
                        navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

                else -> {
                    bottomBarVisibility = true
                    HomeScreenTab(
                        navController = navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                AnimatedVisibility(
                    visible = bottomBarVisibility,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 600,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 600,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    BottomNavAnimation(
                        screens = screen,
                        isTab = isTab,
                        selectedTab = selectedTab,  // Pass selectedTab
                        onClick = { tab ->
                            selectedTab = tab
                            when (tab) {
                                0 -> mainScreenViewModel.toggleTab("home")
                                1 -> mainScreenViewModel.toggleTab("market")
                                2 -> mainScreenViewModel.toggleTab("saved")
                                3 -> mainScreenViewModel.toggleTab("profile")
                            }
                        }
                    )
                }
            }

        } else {

            when (isMarketScreen) {
                "market" -> {
                    bottomBarVisibility = true
                    MarketScreen(
                        navController = navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

                "saved" -> {
                    SavedCoinsScreen(
                        navController = navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }

                "profile" -> {
                    ProfileScreen(
                        navController,
                        animatedVisibilityScope = animatedVisibilityScope,
                        context = LocalContext.current
                    )
                }
                else -> {
                    HomeScreen(
                        navController = navController,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = bottomBarVisibility,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 600,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 600,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    BottomNavAnimation(
                        screens = screen,
                        isTab = isTab,
                        selectedTab = selectedTab,  // Pass selectedTab
                        onClick = { tab ->
                            selectedTab = tab
                            when (tab) {
                                0 -> mainScreenViewModel.toggleTab("home")
                                1 -> mainScreenViewModel.toggleTab("market")
                                2 -> mainScreenViewModel.toggleTab("saved")
                                3 -> mainScreenViewModel.toggleTab("profile")
                            }
                        }
                    )
                }
            }
        }
    }
}
