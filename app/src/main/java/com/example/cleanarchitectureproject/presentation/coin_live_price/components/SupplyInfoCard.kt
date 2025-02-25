package com.example.cleanarchitectureproject.presentation.coin_live_price.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.ui.theme.green

@Composable
fun SupplyInfoCard(
    circulatingSupply: Double,
    maxSupply: Double?,
    totalSupply: Double,
    symbol: String,
    circulatingPercentage: Float,
    color: Color
) {
   /* val supply= "%,.0f".format(circulatingSupply)
    val supplyText= if(supply.length>17)*/
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Circulating Supply",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "${"%,.0f".format(circulatingSupply)}  $symbol",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(0.8f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${circulatingPercentage.toInt()}%",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.End,
                color = color,
                modifier = Modifier.weight(0.2f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedProgressBar(progress = circulatingPercentage / 100,color=color)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Max Supply",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = if(maxSupply!=null)"%,.0f".format(maxSupply) else "--- ---",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Column {
                Text(
                    text = "Total Supply",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "%,.0f".format(totalSupply),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
