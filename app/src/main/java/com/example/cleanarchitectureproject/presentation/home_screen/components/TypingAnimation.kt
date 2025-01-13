package com.example.cleanarchitectureproject.presentation.home_screen.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TypingAnimation(text: String, modifier: Modifier = Modifier) {
    var visibleText by remember { mutableStateOf("") }
    val fullText = text

    // Launch the typing animation
    LaunchedEffect(fullText) {
        while (true) {
            for (i in fullText.indices) {
                visibleText = fullText.take(i + 1)
                delay(100) // Controls the typing speed
            }
            // Loop the animation after finishing the full text
            delay(1000) // Wait before starting again
        }
    }

    Text(
        text = visibleText,
        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
        modifier = modifier
    )
}