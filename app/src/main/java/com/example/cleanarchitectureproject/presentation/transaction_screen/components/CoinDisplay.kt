package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.R

@Composable
fun CoinDisplay(
    amount: Double?,
    modifier: Modifier = Modifier,
    imageUrl: String?=null,
    isSell: Boolean,

    ) {
    Box(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .border(2.dp, Color(0xFFDAA520), RoundedCornerShape(16.dp)), // Border for better appearance
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            if(!isSell)
            {
                Image(
                    painter = painterResource(id = R.drawable.dollar), // Replace with your coin icon
                    contentDescription = "Coin",
                    modifier = Modifier.size(20.dp) // Icon size
                )
            }
            else
            {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl), // Replace with your coin icon
                    contentDescription = "CoinImage",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(18.dp) // Icon size
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(6.dp)) // Space between icon and text

            Text(
                text = "%,.2f".format(amount),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}
