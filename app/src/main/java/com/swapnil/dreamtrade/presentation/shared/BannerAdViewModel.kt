package com.swapnil.dreamtrade.presentation.shared

import android.content.Context
import androidx.lifecycle.ViewModel
import com.swapnil.dreamtrade.domain.use_case.banner_ad.LoadBannerAdUseCase
import com.google.android.gms.ads.AdView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BannerAdViewModel @Inject constructor(
    private val loadBannerAdUseCase: LoadBannerAdUseCase
) : ViewModel() {

    private val _adView = MutableStateFlow<AdView?>(null)
    val adView: StateFlow<AdView?> = _adView

    fun loadAd(context: Context) {
        _adView.value = loadBannerAdUseCase.execute(context)
    }
}
