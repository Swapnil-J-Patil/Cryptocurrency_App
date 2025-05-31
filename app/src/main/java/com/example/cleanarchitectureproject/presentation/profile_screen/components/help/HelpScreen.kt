package com.example.cleanarchitectureproject.presentation.profile_screen.components.help

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue //
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

@Composable
fun HelpScreen(modifier: Modifier = Modifier) {
    NewAnimatedBorderCard(
        onClick = { /* Handle click */ }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Shining Card", style = MaterialTheme.typography.titleMedium)
        }
    }

}



@Composable
fun AnimatedBorderCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    borderWidth: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "color_rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_angle"
    )

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .drawBehind {
                val strokeWidthPx = borderWidth.toPx()
                val inset = strokeWidthPx / 2f

                // Rotate the gradient manually around the center
                rotate(rotationAngle, pivot = center) {
                    drawRoundRect(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color.Magenta,
                                Color.Cyan,
                                Color.Magenta
                            ),
                            center = center
                        ),
                        topLeft = Offset(inset, inset),
                        size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                        style = Stroke(width = strokeWidthPx)
                    )
                }
            }
            .padding(borderWidth)
    ) {
        Surface(
            shape = shape,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            shadowElevation = 2.dp,
        ) {
            content()
        }
    }
}

@Composable
fun NewAnimatedBorderCard(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val infiniteColorTransition = rememberInfiniteTransition(label = "Color animation")
    val degrees by infiniteColorTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Color rotation"
    )
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.clickable(onClick = onClick),
            shape = CircleShape
        ) {
            Surface(
                modifier = Modifier
                    .padding(2.dp) // Border width
                    .drawWithContent {
                        rotate(degrees) {
                            drawCircle(
                                brush = Brush.linearGradient(
                                    listOf(
                                        Color.Magenta,
                                        Color.Transparent
                                    )
                                ),
                                radius = size.width,
                                blendMode = BlendMode.SrcIn
                            )
                        }

                        drawContent()
                    },

                shape = CircleShape
            ) {
                content()
            }
        }
    }
}

