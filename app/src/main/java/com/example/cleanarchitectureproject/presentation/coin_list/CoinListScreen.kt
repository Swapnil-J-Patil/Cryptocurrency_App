package com.example.cleanarchitectureproject.presentation.coin_list

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.coin_list.components.CoinListItem


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CoinListScreen(
    navController: NavController,
    viewModel: CoinListViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.state.value

    // Load Lottie animation composition
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader)) // Ensure the animation is in res/raw folder
    val progress by animateLottieCompositionAsState(composition)

    // Load image with Coil
    val painter = rememberImagePainter("https://www.shutterstock.com/image-illustration/top-7-cryptocurrency-tokens-by-600nw-2152214777.jpg")

    Box(modifier = Modifier.fillMaxSize()
        .background(Color.White)
        //.padding(top=10.dp)
    ) {
        if(!state.error.isNotBlank() && !state.isLoading)
        {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(220.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalArrangement = Arrangement.SpaceEvenly

            ){
                item(
                    span = { GridItemSpan(this.maxLineSpan) }
                ) {
                    // Display the image at the top of the list
                    Image(
                        painter = painter,
                        contentDescription = "Cryptocurrency Image",
                        modifier = Modifier.fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 10.dp)
                        ,
                        contentScale = ContentScale.Crop // Ensure the image is cropped to fit the bounds
                    )
                }
                items(state.coins) { coin ->

                    CoinListItem(
                        coin = coin,
                        onItemClick = {
                            navController.navigate(Screen.CoinDetailScreen.route + "/${coin.id}")
                        },
                        animatedVisibilityScope = animatedVisibilityScope

                    )
                }
            }

        }

        // Show error message if present
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

        // Show Lottie animation if loading
        if (state.isLoading) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(250.dp) // Set the size of the animation
            )
        }
    }
}

