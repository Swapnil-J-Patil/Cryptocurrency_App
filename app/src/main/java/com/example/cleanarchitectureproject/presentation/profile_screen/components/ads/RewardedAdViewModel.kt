package com.example.cleanarchitectureproject.presentation.profile_screen.components.ads

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.cleanarchitectureproject.domain.use_case.rewarded_ad.RewardedAdUseCase
import com.google.android.gms.ads.rewarded.RewardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RewardedAdViewModel @Inject constructor(
    private val rewardedAdUseCase: RewardedAdUseCase
) : ViewModel() {

    private val _isAdLoaded = MutableStateFlow(false)
    val isAdLoaded: StateFlow<Boolean> = _isAdLoaded

    private val _rewardEarned = MutableStateFlow<RewardItem?>(null)
    val rewardEarned: StateFlow<RewardItem?> = _rewardEarned

    fun loadRewardedAd(activity: Activity) {
        rewardedAdUseCase.loadRewardedAd(activity) { loaded ->
            _isAdLoaded.value = loaded
        }
    }

    fun showRewardedAd(activity: Activity) {
        rewardedAdUseCase.showRewardedAd(activity) { reward ->
            _rewardEarned.value = reward
        }
    }
}