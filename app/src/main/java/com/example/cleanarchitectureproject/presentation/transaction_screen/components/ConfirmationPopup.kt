package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cleanarchitectureproject.R

@Composable
fun ConfirmationPopup(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onConfirm: (String,String) -> Unit,
    color: Color,
    usd: String,
    quantity: String,
    isBuy: Boolean,
    imageUrl: String,
    symbol: String,
    isTab: Boolean,
    pricePerCoin: Double,
    availableCoins: Double
) {
    var isChecked by remember { mutableStateOf(false) }
    var liveUsd by remember { mutableStateOf(usd) }
    var liveQuantity by remember { mutableStateOf(quantity) }

    LaunchedEffect(pricePerCoin) {
       // Log.d("livePrice", "Current Price of coin: $pricePerCoin usd:$liveUsd quantity:$liveQuantity")
        val availableDollars = liveUsd.replace(",", "").toDoubleOrNull() ?: 0.0
        //val availableQuantity= liveQuantity.replace(",", "").toDoubleOrNull() ?: 0.0
        if (isBuy) {
            val usdValue = availableDollars
            val amountOfCoin = if (pricePerCoin > 0.0) usdValue / pricePerCoin else 0.0
            val amountOfDollars= amountOfCoin * pricePerCoin
            liveUsd = "%,.2f".format(amountOfDollars)
            liveQuantity = "%,.2f".format(amountOfCoin)
        } else {
            val amountOfCoin = (availableDollars / 100.0) * availableCoins
            val usdValue = amountOfCoin * pricePerCoin
            liveUsd = "%,.2f".format(usdValue)
            liveQuantity = "%,.2f".format(amountOfCoin)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiaryContainer),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp, top = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onCancel() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Confirm Order",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        if (isBuy) {
                            Image(
                                painter = painterResource(id = R.drawable.dollar), // Replace with your coin icon
                                contentDescription = "Coin",
                                modifier = Modifier.size(20.dp) // Icon size
                            )
                        } else {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .size(Size.ORIGINAL)
                                    .build(), // Replace with your coin icon
                                contentDescription = "CoinImage",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(18.dp) // Icon size
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Spend",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowCircleRight,
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        contentDescription = "Right arrow",
                        modifier = Modifier
                            .size(30.dp)
                            .offset(y = 20.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)

                    ) {
                        Text(
                            text = "Receive",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (!isBuy) {
                            Image(
                                painter = painterResource(id = R.drawable.dollar), // Replace with your coin icon
                                contentDescription = "Coin",
                                modifier = Modifier.size(20.dp) // Icon size
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl), // Replace with your coin icon
                                contentDescription = "CoinImage",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(18.dp) // Icon size
                                    .clip(CircleShape)
                            )
                        }
                    }

                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        if (!isBuy && liveQuantity.length > 8 && !isTab) {
                            Text(
                                text = liveQuantity,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                            Text(
                                text = symbol,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        } else if (isBuy && liveUsd.length > 8 && !isTab) {
                            Text(
                                text = liveUsd,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                            Text(
                                text = "USD",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        } else {
                            Text(
                                text = if (isBuy) liveUsd + " USD" else liveQuantity + " $symbol",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        }

                    }
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        if (isBuy && liveQuantity.length > 8 && !isTab) {
                            Text(
                                text = liveQuantity,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                            Text(
                                text = symbol,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        } else if (!isBuy && liveUsd.length > 8 && !isTab) {
                            Text(
                                text = liveUsd,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                            Text(
                                text = "USD",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        } else {
                            Text(
                                text = if (isBuy) liveQuantity + " $symbol" else liveUsd + " USD",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = color,
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "I understand that this is a simulation, and no real money or cryptocurrency is involved.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isChecked) color else Color.Gray,
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (isChecked) color else Color.Gray,
                            RoundedCornerShape(8.dp)
                        ),
                    onClick = {
                        if(isChecked)
                        {
                            onConfirm(liveUsd,liveQuantity)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isChecked) color else Color.Gray
                    ),
                    contentPadding = PaddingValues(vertical = 10.dp),
                ) {
                    Text(
                        text = "Confirm",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
