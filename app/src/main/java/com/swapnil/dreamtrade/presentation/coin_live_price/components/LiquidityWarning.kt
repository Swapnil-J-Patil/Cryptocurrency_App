package com.swapnil.dreamtrade.presentation.coin_live_price.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LiquidityWarning(
    flag: Boolean,
    )
{
    if (flag) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, top = 10.dp, end = 2.dp, bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.2f))
        ) {
            Text(
                text = "⚠️ Low Liquidity: Buying or selling may be difficult!",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}
