package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.ui.theme.gold
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import com.google.gson.Gson

@Composable
fun TransactionCard(
    price: String,
    firstText: String,
    secondText: String,
    buttonText: String,
    color: Color,
    firstPrefix: String,
    secondPrefix: String
) {
    val text1 = remember { mutableStateOf("") }
    val text2 = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$ " + price.substring(0, 10),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = text1.value,
            onValueChange = { text1.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(8.dp)), // Apply rounded corners
            shape = RoundedCornerShape(8.dp),
            label = { Text(firstText) },
            trailingIcon = {
                Text(
                    text = firstPrefix,
                    color = gold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end=10.dp)
                )
            }, // Set leading text instead of icon

            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = text2.value,
            onValueChange = { text2.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(8.dp)), // Apply rounded corners
            shape = RoundedCornerShape(8.dp),
            label = { Text(secondText) },
            trailingIcon = {
                Text(
                    text = secondPrefix,
                    color = gold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end=10.dp)
                )
            }, // Set leading text instead of icon
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp)
                .background(color = color, RoundedCornerShape(8.dp))
                .border(1.dp, color, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}