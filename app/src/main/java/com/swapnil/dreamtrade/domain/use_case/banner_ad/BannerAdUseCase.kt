package com.swapnil.dreamtrade.domain.use_case.banner_ad

import android.content.Context
import com.swapnil.dreamtrade.domain.repository.BannerAdRepository
import com.google.android.gms.ads.AdView
import javax.inject.Inject

class LoadBannerAdUseCase @Inject constructor(
    private val repository: BannerAdRepository
) {
    fun execute(context: Context): AdView {
        return repository.createAdView(context)
    }
}
