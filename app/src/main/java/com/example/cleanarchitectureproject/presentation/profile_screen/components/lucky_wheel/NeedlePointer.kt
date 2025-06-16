package com.example.cleanarchitectureproject.presentation.profile_screen.components.lucky_wheel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NeedlePointer(
    modifier: Modifier = Modifier,
    width: Dp =40.dp,
    height: Dp=60.dp,
    offset: Dp =-30.dp) {
    Canvas(
        modifier = modifier
            .size(width, height) // Adjust size as needed
            .offset(y=offset)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val centerX = canvasWidth / 2
        val radius = canvasWidth / 2

        // 🔵 Draw circular top
        drawCircle(
            color = Color.DarkGray, // Orange
            radius = radius,
            center = Offset(centerX, radius)
        )

        // 🔻 Draw triangle pointer
        val trianglePath = Path().apply {
            moveTo(centerX, canvasHeight) // Triangle tip
            lineTo(centerX - radius, radius) // Left base
            lineTo(centerX + radius, radius) // Right base
            close()
        }

        drawPath(
            path = trianglePath,
            color = Color.DarkGray, // Orange
        )
    }
}


