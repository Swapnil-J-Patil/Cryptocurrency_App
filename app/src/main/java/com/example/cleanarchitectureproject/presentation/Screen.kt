package com.example.cleanarchitectureproject.presentation

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object CoinLivePriceScreen: Screen("coin_live_price_screen")
    object ZoomedChart: Screen("zoomed_chart")
    object TransactionScreen: Screen("transaction_screen")
    object SplashScreen: Screen("splash_screen")
    object AuthScreen: Screen("auth_screen")
    object OnboardingScreen: Screen("onboarding_screen")
    object SuccessScreen: Screen("success_screen")

    object HomeScreenTab: Screen("home_screen_tab")
    object HomeScreen: Screen("home_screen")

    object MarketScreen: Screen("coin_market_screen")
    object MarketScreenTab: Screen("coin_market_screen_tab")

    object SavedCoinsScreen: Screen("saved_coins_screen")
    object SavedCoinsScreenTab: Screen("saved_coins_screen_tab")
}