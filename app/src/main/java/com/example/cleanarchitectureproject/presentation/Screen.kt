package com.example.cleanarchitectureproject.presentation

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object CoinLivePriceScreen: Screen("coin_live_price_screen")
}