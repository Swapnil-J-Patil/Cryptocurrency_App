package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.grey

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PriceSelector(
    remainingSupply: Double?,
    pricePerCoin: Double
) {
    val prices = listOf(25, 50, 75, 100)
    var selectedPrice by remember { mutableStateOf(40) } // Holds the slider progress

    Column {
        FlowRow(
            maxItemsInEachRow = 10,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            prices.forEach { price ->
                CoinPrice(
                    price = price,
                    color = green,
                    onClick = {amount->
                        selectedPrice = amount // Extract number and update progress
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (remainingSupply != null) {
                CustomCircularProgressIndicator(
                    modifier = Modifier.size(300.dp),
                    initialValue = selectedPrice,
                    primaryColor = green,
                    secondaryColor = grey,
                    circleRadius = 280f,
                    onPositionChange = { position ->
                        selectedPrice = position
                    },
                    remainingSupply = remainingSupply,
                    pricePerCoin = pricePerCoin
                )
            }
        }
    }
}
