package com.example.cleanarchitectureproject.presentation.transaction_screen.helper

object DecimalChecker {
    fun hasMoreThanSixDecimals(value: Double): Boolean {
        val decimalPart = value.toString().split(".").getOrNull(1) ?: return false
        return decimalPart.length > 6
    }
}