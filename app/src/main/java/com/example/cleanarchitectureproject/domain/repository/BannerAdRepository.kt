package com.example.cleanarchitectureproject.domain.repository

import android.content.Context
import com.google.android.gms.ads.AdView

interface BannerAdRepository {
    fun createAdView(context: Context): AdView
}
