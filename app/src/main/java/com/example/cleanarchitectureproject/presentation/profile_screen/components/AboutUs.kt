package com.example.cleanarchitectureproject.presentation.profile_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import kotlinx.coroutines.delay

@Composable
fun AboutUsScreen(
) {
    var visibility by remember { mutableStateOf(false) }
    var color = MaterialTheme.colorScheme.secondary
   /* LaunchedEffect(isDarkTheme) {
        if (isDarkTheme) Color.White else Color.Black
    }*/
    LaunchedEffect(Unit) {
        delay(1000)
        visibility=true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(top = 115.dp),
    ) {
        AnimatedVisibility(
            visible = visibility,
            enter = slideInVertically(
                initialOffsetY = { it }, // Starts from full height (bottom)
                animationSpec = tween(
                    durationMillis = 800, // Slightly increased duration for a smoother feel
                    easing = LinearOutSlowInEasing // Smoother easing for entering animation
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Moves to full height (bottom)
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing // Keeps a natural exit motion
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                elevation = CardDefaults.cardElevation(16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "About Us",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = color,
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(Modifier.height(30.dp))
                        Text(
                            text = "DreamTrade is a virtual crypto trading simulator designed for aspiring traders, students, and enthusiasts who want to explore the world of cryptocurrency — without any financial risk.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = color,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "\uD83D\uDCB9 What We Offer:",
                            color = color,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        BulletPoint("Trade real cryptocurrencies using virtual (paper) money", color)
                        BulletPoint("Experience live market prices and realistic trading dynamics", color)
                        BulletPoint("Practice buying, selling, and managing your portfolio with ease", color)
                        BulletPoint("Learn and improve your trading skills in a risk-free environment", color)

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "\uD83C\uDFAF Why DreamTrade?",
                            color = color,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "We believe in learning by doing. DreamTrade empowers you to experiment, fail, and grow — without losing a single rupee. Whether you're just starting out or sharpening your skills, DreamTrade is your safe space to master crypto trading.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = color,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "\uD83D\uDE80 Our Mission",
                            color = color,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "To make crypto trading education accessible, interactive, and engaging for everyone.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = color,
                        )
                    }
                }
            }
        }



}

@Composable
fun BulletPoint(text: String, color: Color) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            "• ", style = MaterialTheme.typography.bodyLarge, color = color,
        )

        Text(
            text, style = MaterialTheme.typography.bodyLarge, color = color,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun About() {
    AboutUsScreen()
}
