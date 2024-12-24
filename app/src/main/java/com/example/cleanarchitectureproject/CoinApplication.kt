package com.example.cleanarchitectureproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoinApplication: Application() {
    //created this class so that dagger hilt can use application context if required
}