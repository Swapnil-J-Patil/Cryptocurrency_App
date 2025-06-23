package com.example.cleanarchitectureproject.data.repository

import android.app.Activity
import android.content.Context
import com.example.cleanarchitectureproject.domain.repository.RewardedAdRepository
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.OnUserEarnedRewardListener
import javax.inject.Inject

class RewardedAdRepositoryImpl @Inject constructor(
    private val context: Context
) : RewardedAdRepository {

    private var rewardedAd: RewardedAd? = null
    private val adUnitId = "Your_TEST_ADMOB_ID"  // Replace with your real AdUnit ID

    override fun loadRewardedAd(activity: Activity, onLoaded: (Boolean) -> Unit) {
        RewardedAd.load(
            activity,
            adUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    onLoaded(true)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedAd = null
                    onLoaded(false)
                }
            }
        )
    }


    override fun showRewardedAd(activity: Activity, onRewardEarned: (RewardItem) -> Unit) {
        rewardedAd?.let { ad ->
            ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
                onRewardEarned(rewardItem)
            })
        }
    }
}
