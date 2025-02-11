package com.example.cleanarchitectureproject.presentation.saved_coin_screen.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM
import com.example.cleanarchitectureproject.presentation.common_components.FlipIcon
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.red

@Composable
fun SquareCoinCardItem(
    currencyName: String,
    symbol: String,
    percentage: String,
    price: String,
    modifier: Modifier = Modifier,
    color: Color,
    logo: String,
    quotes: QuoteCM,
    isGainer:Boolean,
) {
    val degree= if(isGainer) 270f else 90f
    val rotationMax= 360f
    val rotationMin= 0f

    val isSelected= remember {
        mutableStateOf(false)
    }
    val animatedAlpha by animateFloatAsState(targetValue = if (isSelected.value) 1f else .5f)
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected.value) 26.dp else 20.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
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

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(logo)
                    .crossfade(true)
                    .build(),
                contentDescription = "Currency Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxHeight()
                    .weight(0.2f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )


            Spacer(modifier = Modifier.width(12.dp))

            // Currency Name and Symbol (30%)
            Column(
                modifier = Modifier.weight(0.6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = currencyName,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = price,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FlipIcon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .alpha(animatedAlpha)
                        .rotate(degree)
                        .size(animatedIconSize),
                    isActive = isSelected.value,
                    activeIcon = Icons.Filled.PlayArrow,
                    inactiveIcon = Icons.Filled.PlayArrow,
                    contentDescription = "Bottom Navigation Icon",
                    color = color,
                    rotationMax = rotationMax,
                    rotationMin = rotationMin
                )
                Text(
                    text = "$percentage",
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    color = color,
                )
            }
        }
        LineChartItem(
            currencyCM = quotes,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(10.dp),
            color1 = if(isGainer) green else red,
            color2 = if(isGainer) lightGreen else lightRed
        )
    }
}