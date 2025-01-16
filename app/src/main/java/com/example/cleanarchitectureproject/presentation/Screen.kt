package com.example.cleanarchitectureproject.presentation

sealed class Screen(val route: String) {
    object CoinListScreen: Screen("coin_list_screen")
    object CoinDetailScreen: Screen("coin_detail_screen")
    object HomeScreen: Screen("home_screen")
    object CoinLivePriceScreen: Screen("coin_live_price_screen")
}