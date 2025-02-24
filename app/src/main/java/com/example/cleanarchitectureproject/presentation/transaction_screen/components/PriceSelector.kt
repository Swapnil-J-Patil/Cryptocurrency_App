package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.ui.theme.gold
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.grey
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import com.google.gson.Gson

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PriceSelector(
    pricePerCoin: Double,
    firstText: String,
    buttonText: String,
    primaryColor: Color,
    secondaryColor: Color,
    leadingIcon: String,
    isBuy: Boolean,
    alternateColor: Color
) {
    val prices = listOf(25, 50, 75, 100)
    var selectedPrice by remember { mutableStateOf(25) } // Holds the slider progress
    var flag by remember { mutableStateOf(false) } // Holds the slider progress
    val text1 = remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    /* val firstText="Enter Amount"
     val buttonText="BUY"*/
    Column {
        if (isBuy)
        {
            FlowRow(
                maxItemsInEachRow = 10,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp)
            ) {
                prices.forEach { price ->
                    CoinPrice(
                        price = price,
                        color = green,
                        onClick = {amount->
                            selectedPrice = amount // Extract number and update progress
                            flag=!flag
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
                CustomCircularProgressIndicator(
                    modifier = Modifier.size(300.dp),
                    initialValue = selectedPrice,
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor,
                    circleRadius = 280f,
                    onPositionChange = { position ->
                        selectedPrice = position
                    },
                    pricePerCoin = pricePerCoin,
                    flag = flag,
                )
            }

        Spacer(modifier = Modifier.height(3.dp))
        OrDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Ensures proper spacing
        ) {
            OutlinedTextField(
                value = text1.value,
                onValueChange = { text1.value = it },
                modifier = Modifier
                    .weight(1f)
                    .offset(y = -4.dp)
                    .onFocusChanged { isFocused = it.isFocused }, // Track focus state
                shape = RoundedCornerShape(8.dp),
                label = {
                    Text(
                        text = firstText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isFocused) primaryColor else Color.Gray // Change label color on focus
                    )
                },
                singleLine = true,
                leadingIcon = {
                    Text(
                        text = leadingIcon,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor, // Change outline when focused
                    unfocusedBorderColor = Color.Gray, // Default outline color
                    focusedLabelColor = alternateColor, // Change label color when focused
                    unfocusedLabelColor = Color.Gray, // Default label color
                    cursorColor = primaryColor
                )
            )

            Box(
                modifier = Modifier
                    .weight(1f) // Ensures both buttons take equal width
                    .background(
                        primaryColor,
                        RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, primaryColor, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .clickable {

                    }
                ,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
