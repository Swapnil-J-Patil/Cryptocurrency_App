package com.example.cleanarchitectureproject.data.repository

import android.content.Context
import com.example.cleanarchitectureproject.domain.repository.BannerAdRepository
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import javax.inject.Inject

class BannerAdRepositoryImpl @Inject constructor() : BannerAdRepository {

    override fun createAdView(context: Context): AdView {
        val adView = AdView(context)
        adView.setAdUnitId("Your_TEST_ADMOB_ID")  // <-- Test Ad Unit ID
        adView.setAdSize(AdSize.BANNER)  // <-- setAdSize instead of direct assignment

        adView.loadAd(AdRequest.Builder().build())
        return adView
    }
}
