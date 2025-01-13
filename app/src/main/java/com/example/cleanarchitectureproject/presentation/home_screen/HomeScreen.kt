package com.example.cleanarchitectureproject.presentation.home_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.Navbar
import com.example.cleanarchitectureproject.presentation.coin_detail.CoinDetailViewModel
import com.example.cleanarchitectureproject.presentation.home_screen.components.BottomNavAnimation
import com.example.cleanarchitectureproject.presentation.home_screen.components.Carousel
import com.example.cleanarchitectureproject.presentation.home_screen.components.LazyRowScaleIn
import com.example.cleanarchitectureproject.presentation.home_screen.components.TypingAnimation
import kotlin.math.log

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state = viewModel.statsState.value
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(composition)

    val imageUrls = listOf(
        "https://t4.ftcdn.net/jpg/07/49/00/79/360_F_749007978_BJP4clVoWmmPWkXqg8n8LZ0E604vjKgz.jpg",
        "https://img.freepik.com/premium-photo/ethereum-symbol-futuristic-style-metallic-blue-tones-cryptocurrency-technology-banner_1057260-11197.jpg",
        "https://png.pngtree.com/thumb_back/fh260/background/20231003/pngtree-captivating-3d-illustration-of-tether-cryptocurrency-image_13564713.png"
    )

    val screen = listOf(
        Navbar.Home,
        Navbar.Create,
        Navbar.Profile,
        Navbar.Settings
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val carouselHeight = if (screenWidth > 600.dp) 330.dp else 290.dp
    val dotsPadding = if (screenWidth > 600.dp) 15.dp else 4.dp
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (!state.error.isNotBlank() && !state.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 56.dp) // Reserve space for navbar
                ) {
                    TypingAnimation(
                        text = "Let's Dive Into the Market!",
                        modifier = Modifier
                            .padding(top = 45.dp, start = 15.dp, end = 15.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Carousel(
                            imageUrls = imageUrls,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(carouselHeight),
                            onClick = { item ->
                                Log.d("carousel", "clicked item: $item")
                            },
                            dotsPadding = dotsPadding,
                            currency = state.cryptocurrency!!.data.cryptoCurrencyList.subList(0,3)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        state.cryptocurrency?.data?.let { LazyRowScaleIn(items = it.cryptoCurrencyList) }
                    }
                }

                // Custom Navbar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    BottomNavAnimation(
                        screens = screen,
                    )
                }
            }
            // Scrollable content


            // Error message or loading animation
            if (state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }

            if (state.isLoading) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(250.dp)
                )
            }
        }
    }
}