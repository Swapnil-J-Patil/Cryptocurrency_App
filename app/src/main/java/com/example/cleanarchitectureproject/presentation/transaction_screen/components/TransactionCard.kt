package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.common_components.OrDivider
import com.example.cleanarchitectureproject.presentation.ui.theme.gold
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import com.google.gson.Gson

@Composable
fun TransactionCard(
    firstText: String,
    secondText: String,
    buttonText: String,
    color: Color,
) {
    val text1 = remember { mutableStateOf("") }
    val text2 = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedTextField(
            value = text1.value,
            onValueChange = { text1.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(8.dp)), // Apply rounded corners
            shape = RoundedCornerShape(8.dp),
            label = { Text(firstText) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription ="login icon",
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
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(8.dp)), // Apply rounded corners
            shape = RoundedCornerShape(8.dp),
            label = { Text(secondText) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription ="login icon",
                    modifier = Modifier.padding(end=10.dp)
                )
            }, // Set leading text instead of icon
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .background(color = color, RoundedCornerShape(8.dp))
                .border(1.dp, color, RoundedCornerShape(8.dp)),
            onClick = {},
            contentPadding = PaddingValues(vertical = 10.dp),
            ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OrDivider()
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            val fingerprint = "https://img.freepik.com/premium-vector/blue-light-fingerprint-icon-circle-hud-digital-screen-dark-background-illustration-cyber-security-technology-concept_115968-43.jpg"
            val google="https://cdn1.iconfinder.com/data/icons/google-s-logo/150/Google_Icons-09-512.png"
            val faceId="https://cdn-icons-png.flaticon.com/512/6356/6356241.png"
            Box(
                modifier = Modifier
                    .size(67.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF23af92)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    rememberAsyncImagePainter(model = google),
                    contentDescription = "google",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary),
                )
            }
            Box(
                modifier = Modifier
                    .size(67.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF23af92)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    rememberAsyncImagePainter(model = fingerprint),
                    contentDescription = "fingerprint",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary),
                )
            }
            Box(
                modifier = Modifier
                    .size(67.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF23af92)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    rememberAsyncImagePainter(model = faceId),
                    contentDescription = "faceId",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary),
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

    }
}