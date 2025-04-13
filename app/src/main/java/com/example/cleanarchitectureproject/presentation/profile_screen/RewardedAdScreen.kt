package com.example.cleanarchitectureproject.presentation.profile_screen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cleanarchitectureproject.presentation.shared.RewardedAdViewModel

@Composable
fun RewardedAdScreen(
    context: Activity,
    viewModel: RewardedAdViewModel = hiltViewModel()
) {
    val isAdLoaded by viewModel.isAdLoaded.collectAsState()
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
    }
}
