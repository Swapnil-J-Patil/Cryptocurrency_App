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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
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
import com.example.cleanarchitectureproject.presentation.ui.theme.green


@Composable
fun HelpScreen() {
    val helpItems = listOf(
        HelpItem(
            question = "🔐 How do I log in?",
            answers = listOf(
                "Email & password",
                "Google Sign-In",
                "Fingerprint or Pattern Lock (for quick access)"
            )
        ),
        HelpItem(
            question = "💵 What is Paper Money?",
            answers = listOf(
                "DreamTrade gives you virtual USD to simulate real trades.",
                "This isn’t real money, so you can learn and experiment with no risk."
            )
        ),
        HelpItem(
            question = "📈 How does trading work?",
            answers = listOf(
                "1. Go to the Market screen",
                "2. Search and select a coin",
                "3. Tap Buy or Sell",
                "4. Use the slider or enter amount manually",
                "5. Confirm the transaction"
            )
        ),
        HelpItem(
            question = "💼 What’s in my Portfolio?",
            answers = listOf(
                "All your purchased coins",
                "A donut chart showing top holdings",
                "Real-time value updates based on market price"
            )
        ),
        HelpItem(
            question = "🎡 What is Lucky Wheel?",
            answers = listOf(
                "Spin the Lucky Wheel every 3 hours to earn bonus paper money 💰",
                "Use it to boost your virtual wallet and trade more!"
            )
        ),
        HelpItem(
            question = "🌙 How to switch to Dark Mode?",
            answers = listOf(
                "Go to Settings and toggle between Light and Dark Mode."
            )
        ),
        HelpItem(
            question = "🧑‍💼 Who built DreamTrade?",
            answers = listOf(
                "DreamTrade is a Proof of Concept, built by a solo developer — Swapnil Patil.",
                "One vision. One codebase. Many late nights.",
                "Just how Harvey Specter would approve. 😉"
            )
        ),
        HelpItem(
            question = "📩 Need help?",
            answers = listOf(
                "Got feedback or questions?",
                "Reach out to: swapnil.androiddev@gmail.com"
            )
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support") },
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Welcome to DreamTrade — your safe space to learn, explore, and master crypto trading with zero financial risk.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(helpItems) { item ->
                ExpandableHelpItem(item)
            }
        }
    }
}

/*@Composable
fun HelpScreen(modifier: Modifier = Modifier) {
    NewAnimatedBorderCard(
        onClick = { *//* Handle click *//* }
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

}*/



/*
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
*/

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
                                        green,
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

