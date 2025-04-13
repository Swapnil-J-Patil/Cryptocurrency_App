package com.example.cleanarchitectureproject

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DreamTradeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize AdMob with your app ID
        MobileAds.initialize(this) { }
    }
}