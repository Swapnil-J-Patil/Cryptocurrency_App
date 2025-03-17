package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.data.local.shared_prefs.PrefsManager
import com.example.cleanarchitectureproject.domain.model.CryptoCoin
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.grey
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import com.example.cleanarchitectureproject.presentation.ui.theme.red
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DraggableCards(
    coin: CryptoCoin,
    imageUrl: String,
    context: Context,
    transaction: String
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() } // Convert screen height to pixels
    val maxDrag = screenHeightPx * 0.73f // Allow dragging until 10% from bottom
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isTab = screenWidth> 600.dp
    val prefsManager = remember { PrefsManager(context) } // Initialize SharedPreferences
    val dollars=prefsManager.getDollarAmount()
    val cardHeight= if(isTab) 800.dp else screenHeight
    val topCardOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val instructionBuy="Tap the amount in USD to enter exact amount, use slider to adjust the amount or enter custom amount manually."
    val instructionSell="Use slider to adjust the quantity of coin or enter custom quantity manually."

    LaunchedEffect(Unit) {
        if(transaction.lowercase()=="sell")
        {
            topCardOffset.snapTo(maxDrag)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        contentAlignment = Alignment.TopCenter
    ) {
        // Bottom Card (Revealed as top card is dragged)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiaryContainer),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                ) {
                    Text(
                        text = "Sell ${coin.symbol}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .weight(0.5f),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    CoinDisplay(amount = 50.12345, imageUrl = imageUrl, isSell = true)
                }
                Text(
                    text = instructionSell,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                coin.quotes?.get(0)?.let {

                    PriceSelector(
                        pricePerCoin = it.price,
                        firstText = "Quantity",
                        buttonText = "SELL",
                        primaryColor = red,
                        secondaryColor = MaterialTheme.colorScheme.primaryContainer,
                        leadingIcon = "#",
                        isBuy = false,
                        alternateColor = lightRed,
                        availableCoins = 50.12345
                    )
                }
            }
        }

        // Top Card (Draggable)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    top = 30.dp,
                    start = 6.dp,
                    end = 6.dp
                ) // Top padding to create stacked effect
                .offset { IntOffset(0, topCardOffset.value.roundToInt()) }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val newOffset = (topCardOffset.value + delta).coerceIn(0f, maxDrag)
                        coroutineScope.launch {
                            topCardOffset.snapTo(newOffset)
                        }
                    },
                    onDragStopped = {
                        coroutineScope.launch {
                            val target = if (topCardOffset.value > maxDrag / 2) maxDrag else 0f
                            topCardOffset.animateTo(target, tween(300))
                        }
                    }
                ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiaryContainer),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    ) {
                    Text(
                        text = "Buy ${coin.symbol}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .weight(0.5f),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    if (dollars != null) {
                        CoinDisplay(amount = dollars.toDouble(), isSell = false)
                    }
                }
                Text(
                    text = instructionBuy,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                coin.quotes?.get(0)?.let {

                    PriceSelector(
                        pricePerCoin = it.price,
                        firstText = "Amount",
                        buttonText = "BUY",
                        primaryColor = green,
                        secondaryColor = MaterialTheme.colorScheme.primaryContainer,
                        leadingIcon = "$",
                        isBuy = true,
                        alternateColor = green
                    )
                }
            }
        }
    }
}
