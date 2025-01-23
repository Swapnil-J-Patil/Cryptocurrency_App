package com.example.cleanarchitectureproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.CryptoCurrencyCM
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.presentation.coin_detail.CoinDetailScreen
import com.example.cleanarchitectureproject.presentation.coin_list.CoinListScreen
import com.example.cleanarchitectureproject.presentation.coin_live_price.CoinLivePriceScreen
import com.example.cleanarchitectureproject.presentation.home_screen.HomeScreen
import com.example.cleanarchitectureproject.presentation.ui.theme.CleanArchitectureProjectTheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanArchitectureProjectTheme {
                val navController = rememberNavController()
                SharedTransitionLayout {

                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        composable(
                            route = Screen.HomeScreen.route
                        ) {
                            HomeScreen(navController, animatedVisibilityScope = this)
                        }
                        composable(
                            route = Screen.CoinListScreen.route
                        ) {
                            CoinListScreen(navController, animatedVisibilityScope = this)
                        }
                        composable(
                            route = Screen.CoinDetailScreen.route + "/{coinId}",
                            arguments = listOf(
                                navArgument("coinId") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val coinId = it.arguments?.getString("coinId") ?: ""
                            CoinDetailScreen(coinId = coinId, animatedVisibilityScope = this)
                        }

                        composable(
                            route = Screen.CoinLivePriceScreen.route + "/{coinId}/{coinSymbol}/{price}/{coinPercentage}/{isGainer}/{isSaved}/{coinData}",
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
                                coinData = coinData
                            )
                        }
                    }
                }
            }
        }
    }
}


