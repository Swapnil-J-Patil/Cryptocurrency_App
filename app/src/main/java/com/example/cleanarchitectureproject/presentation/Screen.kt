package com.example.cleanarchitectureproject.presentation

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object HomeScreenTab: Screen("home_screen_tab")
    object HomeScreen: Screen("home_screen")
    object CoinLivePriceScreen: Screen("coin_live_price_screen")
    object ZoomedChart: Screen("zoomed_chart")
    object MarketScreen: Screen("coin_market_screen")

}