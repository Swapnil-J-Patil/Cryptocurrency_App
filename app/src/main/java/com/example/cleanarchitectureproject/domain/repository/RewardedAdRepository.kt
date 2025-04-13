package com.example.cleanarchitectureproject.domain.repository

import com.google.android.gms.ads.rewarded.RewardItem
import android.app.Activity

interface RewardedAdRepository {
    fun loadRewardedAd(activity: Activity, onLoaded: (Boolean) -> Unit)
    fun showRewardedAd(activity: Activity, onRewardEarned: (RewardItem) -> Unit)
}
