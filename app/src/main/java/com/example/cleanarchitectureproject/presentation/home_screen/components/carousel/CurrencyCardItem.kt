package com.example.cleanarchitectureproject.presentation.home_screen.components.carousel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.presentation.ui.theme.green

@Composable
fun CurrencyCardItem(
    currency: String,
    percentage: String,
    image: String,
    modifier: Modifier = Modifier,
    color: Color
    ) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary), // Set the background color
        elevation = CardDefaults.cardElevation(4.dp), // Add elevation for shadow
        shape = RoundedCornerShape(8.dp) // Rounded corners
    ){
        Row(
            modifier = modifier.background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            ) {
            // Profile Icon
            Box(
                modifier = Modifier
                    .size(40.dp) // Adjust size as needed
                    .clip(CircleShape)
                    .background(green.copy(alpha = 0.2f)), // Background color for the icon
                contentAlignment = Alignment.Center // Center the icon inside the box
            ) {
                Image(
                    painter = rememberAsyncImagePainter(image),
                    contentDescription = "Profile Icon",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

            // Text Column
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = percentage,
                    style = MaterialTheme.typography.titleSmall,
                    color = color
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currency,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

}