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
import com.example.cleanarchitectureproject.presentation.coin_detail.CoinDetailScreen
import com.example.cleanarchitectureproject.presentation.coin_list.CoinListScreen
import com.example.cleanarchitectureproject.presentation.home_screen.HomeScreen
import com.example.cleanarchitectureproject.presentation.ui.theme.CleanArchitectureProjectTheme
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
                            HomeScreen()
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
                    }
                }
            }
        }
    }
}


