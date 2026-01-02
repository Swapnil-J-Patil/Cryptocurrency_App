package com.swapnil.dreamtrade.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

interface SweetToastProperty {
    fun getResourceId(): ImageVector
    fun getBackgroundColor(): Color
    fun getBorderColor(): Color
    fun getTextColor(): Color
    fun getProgressBarColor(): Color
}