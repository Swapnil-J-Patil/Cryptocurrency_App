package com.example.cleanarchitectureproject.presentation.profile_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.profile_screen.components.pie_chart.PieChart

@Composable
fun PortfolioCard(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        PieChart(
            data = mapOf(
                Pair("Sample-1", 150),
                Pair("Sample-2", 120),
                Pair("Sample-3", 110),
                Pair("Sample-4", 170),
                Pair("Sample-5", 120),
            ),
            ringBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
            ringBgColor = MaterialTheme.colorScheme.tertiary
        )
    }
}