package com.swapnil.dreamtrade.di

import androidx.appcompat.app.AppCompatActivity


class BiometricDependency(private val activity: AppCompatActivity) {
    fun getActivity(): AppCompatActivity = activity
}
