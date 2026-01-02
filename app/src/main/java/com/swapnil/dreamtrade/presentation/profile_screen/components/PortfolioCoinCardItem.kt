package com.swapnil.dreamtrade.presentation.profile_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.swapnil.dreamtrade.R

@Composable
fun PortfolioCoinCardItem(
    symbol: String,
    graph: String,
    modifier: Modifier = Modifier,
    color: Color,
    logo: String,
    currentPrice: String="",
    purchasedPrice: String = "",
    quantity: String = ""
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary), // Set the background color
        elevation = CardDefaults.cardElevation(4.dp), // Add elevation for shadow
        shape = RoundedCornerShape(8.dp) // Rounded corners
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Column(
                modifier = Modifier.weight(0.4f)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start

            ) {
                Text(
                    text = symbol,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(logo)
                            .crossfade(true)
                            .size(Size.ORIGINAL)
                            .build(), // Replace with your coin icon
                        contentDescription = "CoinImage",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(18.dp) // Icon size
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(6.dp)) // Space between icon and text

                    Text(
                        text = quantity,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

            }

           // Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(horizontal = 10.dp)
                    .aspectRatio(2.5f),
                contentAlignment = Alignment.Center // Center the icon inside the box
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(graph)
                        .crossfade(true) // Enable crossfade animation
                        .placeholder(R.drawable.placeholder) // Placeholder drawable
                        .error(R.drawable.placeholder) // Error drawable
                        .build(),
                    contentDescription = "Graph Image",
                    contentScale = ContentScale.Fit,
                )
            }
            Column(
                modifier = Modifier.weight(0.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentPrice,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    color = color,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = purchasedPrice,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}