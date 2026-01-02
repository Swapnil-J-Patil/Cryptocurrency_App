package com.swapnil.dreamtrade.domain.use_case.rewarded_ad

import android.app.Activity
import com.swapnil.dreamtrade.domain.repository.RewardedAdRepository
import com.google.android.gms.ads.rewarded.RewardItem
import javax.inject.Inject

class RewardedAdUseCase @Inject constructor(
    private val repository: RewardedAdRepository
) {
    fun loadRewardedAd(activity: Activity, onLoaded: (Boolean) -> Unit) {
        repository.loadRewardedAd(activity, onLoaded)
    }

    fun showRewardedAd(activity: Activity, onRewardEarned: (RewardItem) -> Unit) {
        repository.showRewardedAd(activity, onRewardEarned)
    }
}