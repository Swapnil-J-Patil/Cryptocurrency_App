package com.example.cleanarchitectureproject.presentation.auth_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.graphics.Color
import com.example.cleanarchitectureproject.domain.model.SweetToastProperty
import com.example.cleanarchitectureproject.presentation.ui.theme.redBright

class Error : SweetToastProperty {
    override fun getResourceId() = Icons.Default.Error
    override fun getBackgroundColor() = Color.White
    override fun getBorderColor() = Color.Transparent
    override fun getTextColor() = Color.Black
    override fun getProgressBarColor() = redBright
}