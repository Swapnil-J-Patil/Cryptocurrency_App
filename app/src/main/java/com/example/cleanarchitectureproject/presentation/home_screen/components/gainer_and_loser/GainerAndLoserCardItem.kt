package com.example.cleanarchitectureproject.presentation.home_screen.components.gainer_and_loser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.ui.theme.green

@Composable
fun GainerAndLoserCardItem(
    currencyName: String,
    symbol: String,
    percentage: String,
    price: String,
    image: String,
    modifier: Modifier = Modifier,
    color: Color,
    logo: String
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
            // Currency Logo (10%)
            Box(
                modifier = Modifier
                    .weight(0.1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(green.copy(alpha = 0.2f)), // Background color for the icon
                contentAlignment = Alignment.Center // Center the icon inside the box
            ) {
                Image(
                    painter = rememberAsyncImagePainter(logo),
                    contentDescription = "Currency Logo",
                    contentScale = ContentScale.FillBounds,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            // Currency Name and Symbol (30%)
            Column(
                modifier = Modifier.weight(0.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = currencyName,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = symbol,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Price and Percentage (20%)
            Column(
                modifier = Modifier.weight(0.2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = price,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = percentage,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = color,
                )
            }

            // Graph Image (40%)
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 10.dp)
                    .aspectRatio(2.5f),
                contentAlignment = Alignment.Center // Center the icon inside the box
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = image,
                        error = painterResource(id = R.drawable.placeholder) // Replace with your drawable resource
                        ),
                    contentDescription = "Graph Image",
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
    }
}
