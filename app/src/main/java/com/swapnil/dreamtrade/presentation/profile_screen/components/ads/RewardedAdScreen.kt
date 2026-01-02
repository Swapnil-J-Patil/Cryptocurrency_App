package com.swapnil.dreamtrade.presentation.profile_screen.components.ads

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.swapnil.dreamtrade.presentation.shared.BannerAdViewModel

@Composable
fun RewardedAdScreen(
    context: Activity,
    viewModel: RewardedAdViewModel = hiltViewModel(),
    bannerAdViewModel: BannerAdViewModel = hiltViewModel()

) {

    //RewardedAd
    /*val isAdLoaded by viewModel.isAdLoaded.collectAsState()
    val rewardEarned by viewModel.rewardEarned.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = { viewModel.loadRewardedAd(context) }) {
            Text("Load Ad")
        }

        Button(
            onClick = {

                viewModel.showRewardedAd(context)
            },
            enabled = isAdLoaded
        ) {
            Text("Show Ad")
        }

        rewardEarned?.let { reward ->
            Text("You earned: ${reward.amount} ${reward.type}")
        }
    }*/

    //BannerAd
    val context = LocalContext.current
    val adView by bannerAdViewModel.adView.collectAsState()

    LaunchedEffect(Unit) {
        bannerAdViewModel.loadAd(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        adView?.let { ad ->
            AndroidView(factory = { ad })
        }
    }
}

