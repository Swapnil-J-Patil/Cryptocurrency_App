package com.example.cleanarchitectureproject.presentation.transaction_screen

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebSettings
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.home_screen.components.currency_row.CurrencyCardItem
import com.example.cleanarchitectureproject.presentation.transaction_screen.components.CurrencyCard
import com.example.cleanarchitectureproject.presentation.transaction_screen.components.DraggableCards
import com.example.cleanarchitectureproject.presentation.transaction_screen.components.TransactionCard
import com.example.cleanarchitectureproject.presentation.ui.theme.darkBackground
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.red

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TransactionScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    transaction: String,
    coin: CryptoCoin
) {

    val tabTitles = listOf<String>("Buy ${coin.symbol}", "Sell ${coin.symbol}")


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(top = 45.dp),
            ) {
            CurrencyCard(
                currency = coin.name,
                image = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 8.dp),
                animatedVisibilityScope = animatedVisibilityScope,
                currencyId = coin.id.toString(),
                listType = transaction,
                symbol = coin.symbol.toString(),
                graph = "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coin.id}.png"

            )

            DraggableCards(coin = coin, imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png")
            Spacer(modifier = Modifier.height(8.dp))
        }


    }
}

