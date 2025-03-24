package com.example.cleanarchitectureproject.presentation.profile_screen.components.pie_chart

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.cleanarchitectureproject.presentation.common_components.FlipIcon
import com.example.cleanarchitectureproject.presentation.ui.theme.aquaMint
import com.example.cleanarchitectureproject.presentation.ui.theme.blueBright
import com.example.cleanarchitectureproject.presentation.ui.theme.darkCyan
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.greenBright
import com.example.cleanarchitectureproject.presentation.ui.theme.mutedLime
import com.example.cleanarchitectureproject.presentation.ui.theme.orangeBright
import com.example.cleanarchitectureproject.presentation.ui.theme.redBright
import com.example.cleanarchitectureproject.presentation.ui.theme.emeraldGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.lightRed
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    data: Map<String, Int>,
    radiusOuter: Dp = 120.dp,
    chartBarWidth: Dp = 25.dp,
    animDuration: Int = 1000,
    arcSpacing: Float = 18f, // Space between arcs
    ringBgColor: Color = Color.Transparent,
    ringBorderColor: Color = Color.Transparent,
    minValue:Int = 0,
    maxValue:Int = 120,

) {
    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    val isSelected = remember {
        mutableStateOf(false)
    }
    val animatedAlpha by animateFloatAsState(targetValue = if (isSelected.value) 1f else .5f)
    val animatedIconSize by animateDpAsState(
        targetValue = if (isSelected.value) 26.dp else 20.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
    val isGainer=true
    data.values.forEachIndexed { index, values ->
        floatValue.add(index, (360 * values.toFloat() / totalSum.toFloat()) - arcSpacing)
    }
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    val imageRequest = ImageRequest.Builder(context)
        .data("https://s2.coinmarketcap.com/static/img/coins/64x64/1.png") // Replace with your actual image URL
        .size(50, 50) // Set the size as needed
        .build()

// Load the image asynchronously
    var imageBitmap by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    val degree = if (isGainer) 270f else 90f
    val color = if (isGainer) green else lightRed
    val rotationMax = 360f
    val rotationMin = 0f
    val arcColors = listOf(
        listOf(Color(0xFFff7e5f), Color(0xFFfeb47b)),
        listOf(Color(0xFF00c6ff), Color(0xFF0072ff)),
        listOf(Color(0xFFf7971e), Color(0xFFffd200)),
        listOf( Color(0xFF0E5C4C),Color(0xFF23af92)),
        listOf(Color(0xFFee9ca7), Color(0xFFffdde1))
    )

    var animationPlayed by remember { mutableStateOf(false) }
    var lastValue = 0f

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )
    LaunchedEffect(Unit) {
        val result = imageLoader.execute(imageRequest)
        if (result is SuccessResult) {
            imageBitmap = result.drawable.toBitmap().asImageBitmap()
        }
        animationPlayed = true

        delay(2000L) // Delay for 1 second (1000 milliseconds)
        isSelected.value = true
    }



    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier
                    .offset { IntOffset.Zero }
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {

                // **Draw the background ring (Donut Shape)**
                drawArc(
                    color = ringBorderColor, // Gray border color
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(chartBarWidth.toPx() * 2, cap = StrokeCap.Butt) // Slightly larger width
                )

                // **Draw the background ring (Donut Shape)**
                drawArc(
                    color = ringBgColor, // Dark background ring color
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke((chartBarWidth * 2 - 10.dp ).toPx() , cap = StrokeCap.Butt) // No rounded edges
                )

                val outerRadius = (radiusOuter + chartBarWidth / 2f).toPx() + 40 // Convert to Px
                val gap = 10f
                val width = size.width
                val height = size.height
                val circleThickness = width / 25f
                circleCenter = Offset(x = width / 2f, y = height / 2f)

                for (i in 0 until (maxValue - minValue)) {
                    val color = ringBorderColor
                    val angleInDegrees = i * 360f / (maxValue - minValue).toFloat()
                    val angleInRad = angleInDegrees * PI / 180f + PI / 2f

                    val yGapAdjustment = cos(angleInDegrees * PI / 180f) * gap
                    val xGapAdjustment = -sin(angleInDegrees * PI / 180f) * gap

                    val start = Offset(
                        x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                        y = (outerRadius * sin(angleInRad) + circleCenter.y + yGapAdjustment).toFloat()
                    )

                    val end = Offset(
                        x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                        y = (outerRadius * sin(angleInRad) + circleCenter.y + circleThickness + yGapAdjustment).toFloat()
                    )

                    rotate(
                        angleInDegrees,
                        pivot = start
                    ) {
                        drawLine(
                            color = color,
                            start = start,
                            end = end,
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }

                // **Draw the arcs on top**
                floatValue.forEachIndexed { index, value ->
                    val startAngleRad = Math.toRadians(lastValue.toDouble())
                    val endAngleRad = Math.toRadians((lastValue + value).toDouble())

                    val startX = (size.width / 2) + (radiusOuter.toPx() * cos(startAngleRad)).toFloat()
                    val startY = (size.height / 2) + (radiusOuter.toPx() * sin(startAngleRad)).toFloat()

                    val endX = (size.width / 2) + (radiusOuter.toPx() * cos(endAngleRad)).toFloat()
                    val endY = (size.height / 2) + (radiusOuter.toPx() * sin(endAngleRad)).toFloat()

                    val brush = Brush.linearGradient(
                        colors = arcColors[index],
                        start = Offset(startX, startY),
                        end = Offset(endX, endY)
                    )

                    drawArc(
                        brush = brush,
                        startAngle = lastValue,
                        sweepAngle = maxOf(0f, value), // Ensure it's not negative
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Round) // Rounded edges
                    )

                    // **Calculate Midpoint Angle**
                    val midAngle = lastValue + (value / 2)
                    val midAngleRad = Math.toRadians(midAngle.toDouble())

                    // **Find the Line Start & End Coordinates**
                    val lineStart = Offset(
                        x = (size.width / 2) + ((radiusOuter.toPx() + chartBarWidth.toPx() / 2) * cos(midAngleRad)).toFloat(),
                        y = (size.height / 2) + ((radiusOuter.toPx() + chartBarWidth.toPx() / 2) * sin(midAngleRad)).toFloat()
                    )

                    val lineEnd = Offset(
                        x = (size.width / 2) + ((radiusOuter.toPx() + chartBarWidth.toPx() + 15.dp.toPx()) * cos(midAngleRad)).toFloat(),
                        y = (size.height / 2) + ((radiusOuter.toPx() + chartBarWidth.toPx() + 15.dp.toPx()) * sin(midAngleRad)).toFloat()
                    )

                    // **Draw the Highlighted Line**
                    drawLine(
                        color = arcColors[index].first(), // Start color of the gradient
                        start = lineStart,
                        end = lineEnd,
                        strokeWidth = 4.dp.toPx(), // Slightly thicker for visibility
                        cap = StrokeCap.Round
                    )
                    drawCircle(
                        color = arcColors[index].first(), // Same color as the highlighted line
                        radius = 12.dp.toPx(), // Adjust size as needed
                        center = lineEnd // Place it at the end of the line
                    )


                    drawIntoCanvas { canvas ->
                        imageBitmap?.let { bitmap ->
                            val paint = Paint()
                            val imageSize = 28.dp.toPx() // Adjust the size of the image

                            // Calculate the top-left position so the image is centered at lineEnd
                            val imageOffset = Offset(
                                x = lineEnd.x - imageSize / 2 + 18,
                                y = lineEnd.y - imageSize / 2 + 18
                            )

                            // Draw the image at the correct position
                            canvas.drawImage(
                                image = bitmap,
                                topLeftOffset = imageOffset,
                                paint = paint
                            )
                        }
                    }
                    lastValue += value + arcSpacing // Add spacing between arcs
                }

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Portfolio Value",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "$14,053.00",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top=5.dp)
                ) {
                    Text(
                        text = "5.1%",
                        color = green,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    FlipIcon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .alpha(animatedAlpha)
                            .rotate(degree)
                            .size(animatedIconSize),
                        isActive = isSelected.value,
                        activeIcon = Icons.Filled.PlayArrow,
                        inactiveIcon = Icons.Filled.PlayArrow,
                        contentDescription = "Percentage gain or lose icon",
                        color = color,
                        rotationMax = rotationMax,
                        rotationMin = rotationMin
                    )
                }
            }
        }
    }
}







