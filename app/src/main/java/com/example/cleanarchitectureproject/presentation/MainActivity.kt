package com.example.cleanarchitectureproject.presentation

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cleanarchitectureproject.di.AppContainer
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.presentation.coin_live_price.CoinLivePriceScreen
import com.example.cleanarchitectureproject.presentation.common_components.ZoomedChart
import com.example.cleanarchitectureproject.presentation.home_screen.HomeScreen
import com.example.cleanarchitectureproject.presentation.home_screen.HomeScreenTab
import com.example.cleanarchitectureproject.presentation.auth_screen.AuthScreen
import com.example.cleanarchitectureproject.presentation.auth_screen.BiometricViewModel
import com.example.cleanarchitectureproject.presentation.main_screen.MainScreen
import com.example.cleanarchitectureproject.presentation.market_screen.MarketScreen
import com.example.cleanarchitectureproject.presentation.market_screen.MarketScreenTab
import com.example.cleanarchitectureproject.presentation.onboarding_screen.BubblePagerContent
import com.example.cleanarchitectureproject.presentation.shared.AppThemeViewModel
import com.example.cleanarchitectureproject.presentation.profile_screen.ProfileScreen
import com.example.cleanarchitectureproject.presentation.profile_screen.ProfileScreenTab
import com.example.cleanarchitectureproject.presentation.profile_screen.components.ads.RewardedAdScreen
import com.example.cleanarchitectureproject.presentation.profile_screen.components.about_us.AboutUs
import com.example.cleanarchitectureproject.presentation.profile_screen.components.help.HelpScreen
import com.example.cleanarchitectureproject.presentation.profile_screen.components.lucky_wheel.LuckyWheelScreen
import com.example.cleanarchitectureproject.presentation.saved_coin_screen.SavedCoinsScreen
import com.example.cleanarchitectureproject.presentation.saved_coin_screen.SavedCoinsScreenTab
import com.example.cleanarchitectureproject.presentation.splash_screen.SplashScreen
import com.example.cleanarchitectureproject.presentation.success_screen.SuccessScreen
import com.example.cleanarchitectureproject.presentation.transaction_screen.TransactionScreen
import com.example.cleanarchitectureproject.presentation.ui.theme.CleanArchitectureProjectTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appContainer: AppContainer
    private lateinit var biometricViewModel: BiometricViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSharedTransitionApi::class, ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)

        appContainer = AppContainer(this)
        biometricViewModel = appContainer.biometricViewModel

        val intent = Intent(this, MainActivity::class.java)

        setContent {
            val appThemeViewModel: AppThemeViewModel = hiltViewModel()
            val isDark = appThemeViewModel.isDark.collectAsState()

            CleanArchitectureProjectTheme(
                darkTheme = isDark.value
            ) {
                val navController = rememberNavController()

                SharedTransitionLayout {

                    NavHost(
                        navController = navController,
                        startDestination = Screen.SplashScreen.route
                    ) {
                        //Entrypoint
                        composable(
                            route = Screen.SplashScreen.route
                        ) {
                            SplashScreen(navController,this@MainActivity)
                        }
                        composable(
                            route = Screen.OnboardingScreen.route
                        ) {
                            BubblePagerContent(navController,this@MainActivity)
                        }
                        composable(
                            route = Screen.AuthScreen.route
                        ) {
                            AuthScreen(navController, context = this@MainActivity, animatedVisibilityScope = this, biometricViewModel = biometricViewModel)
                        }
                        composable(
                            route = Screen.SuccessScreen.route
                        ) {
                            SuccessScreen(navController = navController)
                        }
                        composable(
                            route = Screen.RewardedAdScreen.route
                        ) {
                            RewardedAdScreen(this@MainActivity)
                        }
                        composable(
                            route= Screen.HelpScreen.route
                        ) {
                            HelpScreen()
                        }
                        composable(
                            route = Screen.LuckyWheelScreen.route,
                            /*enterTransition = {
                                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500))
                            },
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500))
                            },
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500))
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { it },
                                    animationSpec = tween(500)
                                )
                            }*/
                        ) {
                            LuckyWheelScreen()
                        }
                        composable(
                            route = Screen.MainScreen.route,
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None },
                            popEnterTransition = { EnterTransition.None },
                            popExitTransition = { ExitTransition.None }
                        ) {
                            MainScreen(navController, animatedVisibilityScope = this,
                                isDarkTheme = isDark.value, onToggle = {
                                    appThemeViewModel.toggleTheme()
                                },
                                onLogout = {
                                    /*navController.navigate(Screen.AuthScreen.route) {
                                        popUpTo(Screen.MainScreen.route) { inclusive = true }
                                    }*/
                                    //val context = LocalContext.current
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    finish()
                                    startActivity(intent)

                                })

                        }

                        //Home Screen
                        composable(
                            route = Screen.HomeScreenTab.route
                        ) {
                            HomeScreenTab(navController, animatedVisibilityScope = this)
                        }
                        composable(
                            route = Screen.HomeScreen.route
                        ) {
                            HomeScreen(navController, animatedVisibilityScope = this)
                        }

                        //About Us Screen
                        composable(
                            route = Screen.AboutUsScreen.route,
                           /* enterTransition = {
                                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500))
                            },
                            exitTransition = {
                                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500))
                            },
                            popEnterTransition = {
                                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500))
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = { it },
                                    animationSpec = tween(500)
                                )
                            }*/
                        ) {
                            AboutUs()
                        }

                        //Market Screen
                        composable(
                            route = Screen.MarketScreen.route
                        ){
                            MarketScreen(
                                navController = navController,
                                animatedVisibilityScope = this
                            )
                        }
                        composable(
                            route = Screen.MarketScreenTab.route
                        ){
                            MarketScreenTab(
                                navController = navController,
                                animatedVisibilityScope = this
                            )
                        }

                        //Saved Coins Screen
                        composable(
                            route = Screen.SavedCoinsScreen.route
                        ){
                            SavedCoinsScreen(
                                navController = navController,
                                animatedVisibilityScope = this
                            )
                        }
                        composable(
                            route = Screen.SavedCoinsScreenTab.route
                        ){
                            SavedCoinsScreenTab(
                                navController = navController,
                                animatedVisibilityScope = this
                            )
                        }

                        //Profile screen
                        composable(
                            route = Screen.ProfileScreen.route
                        ) {
                            ProfileScreen(navController, animatedVisibilityScope = this, context = this@MainActivity, isDarkTheme = isDark.value, onToggle = {
                                appThemeViewModel.toggleTheme()
                            },
                                onLogout = {

                                })
                        }
                        composable(
                            route = Screen.ProfileScreenTab.route
                        ) {
                            ProfileScreenTab(navController, animatedVisibilityScope = this)
                        }
                        //Transaction Screen
                        composable(
                            route = Screen.TransactionScreen.route+ "/{transaction}/{coinData}",
                            arguments = listOf(
                                navArgument("coinData") {
                                    type = NavType.StringType
                                },
                                navArgument("transaction") {
                                    type = NavType.StringType
                                }
                            )
                        ){
                            val gson = Gson() // Or use kotlinx.serialization
                            val coinDataJson = it.arguments?.getString("coinData") ?: ""
                            val transaction = it.arguments?.getString("transaction") ?: ""
                            val coinData = gson.fromJson(
                                coinDataJson,
                                CryptoCoin::class.java
                            )
                            TransactionScreen( animatedVisibilityScope = this, transaction = transaction, coin = coinData, context = this@MainActivity, navController = navController,)
                        }
                        //ZoomedChart Screen
                        composable(
                            route = Screen.ZoomedChart.route+ "/{coinSymbol}/{coinData}/{isHome}/{listType}/{isGainer}",
                            arguments = listOf(
                                navArgument("coinSymbol") {
                                    type = NavType.StringType
                                },
                                navArgument("coinData") {
                                    type = NavType.StringType
                                },
                                navArgument("isHome") {
                                    type = NavType.BoolType
                                },
                                navArgument("listType") {
                                    type = NavType.StringType
                                },
                                navArgument("isGainer") {
                                    type = NavType.BoolType
                                },
                            )
                        ){
                            val gson = Gson() // Or use kotlinx.serialization
                            val coinSymbol = it.arguments?.getString("coinSymbol") ?: ""
                            val listType = it.arguments?.getString("listType") ?: ""
                            val isHome = it.arguments?.getBoolean("isHome") ?: false
                            val isGainer = it.arguments?.getBoolean("isGainer") ?: false
                            val coinDataJson = it.arguments?.getString("coinData") ?: ""

                            val coinData = gson.fromJson(
                                coinDataJson,
                                CryptoCoin::class.java
                            )
                            ZoomedChart(currency = coinData, id = coinSymbol, isHomeScreen = isHome, animatedVisibilityScope = this,listType, isGainer = isGainer)
                        }

                        //Live Price Screen
                        composable(
                            route = Screen.CoinLivePriceScreen.route + "/{coinId}/{coinSymbol}/{price}/{coinPercentage}/{isGainer}/{isSaved}/{coinData}/{listType}",
                            arguments = listOf(
                                navArgument("coinId") {
                                    type = NavType.StringType
                                },
                                navArgument("coinSymbol") {
                                    type = NavType.StringType
                                },
                                navArgument("price") {
                                    type = NavType.StringType
                                },
                                navArgument("coinPercentage") {
                                    type = NavType.StringType
                                },
                                navArgument("isGainer") {
                                    type = NavType.BoolType
                                },
                                navArgument("isSaved") {
                                    type = NavType.BoolType
                                },
                                navArgument("coinData")
                                { type = NavType.StringType },
                                navArgument("listType")
                                { type = NavType.StringType }

                            )
                        ) {
                            val gson = Gson() // Or use kotlinx.serialization
                            val coinId = it.arguments?.getString("coinId") ?: ""
                            val coinSymbol = it.arguments?.getString("coinSymbol") ?: ""
                            val coinPrice = it.arguments?.getString("price") ?: ""
                            val coinPercentage = it.arguments?.getString("coinPercentage") ?: ""
                            val isGainer = it.arguments?.getBoolean("isGainer") ?: false
                            val isSaved = it.arguments?.getBoolean("isSaved") ?: false
                            val listType = it.arguments?.getString("listType") ?: ""
                            val coinDataJson = it.arguments?.getString("coinData") ?: ""
                            val coinData = gson.fromJson(
                                coinDataJson,
                                CryptoCoin::class.java
                            ) // Deserialize
                            CoinLivePriceScreen(
                                coinId = coinId,
                                coinSymbol = coinSymbol,
                                coinPrice = coinPrice,
                                coinPercentage = coinPercentage,
                                isGainer = isGainer,
                                isSaved = isSaved,
                                animatedVisibilityScope = this,
                                coinData = coinData,
                                navController = navController,
                                listType = listType
                            )
                        }

                    }
                }
            }
        }
    }
}


