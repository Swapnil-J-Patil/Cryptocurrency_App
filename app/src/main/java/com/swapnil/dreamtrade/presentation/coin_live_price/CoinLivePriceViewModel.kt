package com.swapnil.dreamtrade.presentation.coin_live_price

import androidx.lifecycle.ViewModel
import com.swapnil.dreamtrade.domain.use_case.get_currency_stats.GetCurrencyStatsUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoinLivePriceViewModel @Inject constructor(
    private val getCurrencyStatsUseCase: GetCurrencyStatsUseCase,
) : ViewModel() {

}