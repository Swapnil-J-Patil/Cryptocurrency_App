package com.example.cleanarchitectureproject.presentation.coin_list.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cleanarchitectureproject.domain.model.Coin
import com.example.cleanarchitectureproject.presentation.ui.theme.green


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CoinListItem(
    coin: Coin,
    onItemClick: (Coin) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(coin) }
            .padding(horizontal = 5.dp, vertical = 3.dp)
            .sharedElement(
                state = rememberSharedContentState(key = "coinCard/${coin.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${coin.rank}. ${coin.name} (${coin.symbol})",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = if (coin.isActive) "active" else "inactive",
                color = if (coin.isActive) green else Color.Red,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}