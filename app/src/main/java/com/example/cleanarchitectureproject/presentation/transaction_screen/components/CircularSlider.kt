package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun CircularSlider(
    modifier: Modifier = Modifier,
    initialValue:Int,
    primaryColor: Color,
    secondaryColor:Color,
    textColor: Color,
    minValue:Int = 0,
    maxValue:Int = 100,
    circleRadius:Float,
    onPositionChange:(Int)->Unit,
    pricePerCoin: Double,
    flag: Boolean,
    isBuy: Boolean,
    availableCoins: Double?=0.0
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var positionValue by remember {
        mutableStateOf(initialValue)
    }

    var changeAngle by remember {
        mutableStateOf(0f)
    }

    var dragStartedAngle by remember {
        mutableStateOf(0f)
    }

    var oldPositionValue by remember {
        mutableStateOf(initialValue)
    }


    var formattedAmount by remember { mutableStateOf("") }
    var formattedUsdValue by remember { mutableStateOf("") }

    LaunchedEffect(flag) {

        val usdValue = initialValue.toDouble()  // Since initialValue represents USD directly
        val amountOfCoin = if (pricePerCoin > 0) usdValue / pricePerCoin else 0.0  // Avoid division by zero
        formattedUsdValue = "%,.2f".format(usdValue)  // Format as currency
        formattedAmount = "%,.6f".format(amountOfCoin)  // Format coin amount with precision

        positionValue = initialValue
        oldPositionValue = initialValue
    }
    LaunchedEffect(initialValue) {

        if (isBuy) {
            val usdValue = initialValue.toDouble()
            val amountOfCoin = if (pricePerCoin > 0) usdValue / pricePerCoin else 0.0
            formattedUsdValue = "%,.2f".format(usdValue)
            formattedAmount = "%,.6f".format(amountOfCoin)
        } else {
            val amountOfCoin =  (initialValue / 100.0) * availableCoins!!
            val usdValue = amountOfCoin * pricePerCoin
            formattedUsdValue = "%,.2f".format(usdValue)
            formattedAmount = "%,.6f".format(amountOfCoin)
        }
    }

    Box(
        modifier = modifier
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartedAngle = -atan2(
                                x = circleCenter.y - offset.y,
                                y = circleCenter.x - offset.x
                            ) * (180f / PI).toFloat()
                            dragStartedAngle = (dragStartedAngle + 180f).mod(360f)
                        },
                        onDrag = { change, _ ->
                            var touchAngle = -atan2(
                                x = circleCenter.y - change.position.y,
                                y = circleCenter.x - change.position.x
                            ) * (180f / PI).toFloat()
                            touchAngle = (touchAngle + 180f).mod(360f)

                            val currentAngle = oldPositionValue * 360f / (maxValue - minValue)
                            changeAngle = touchAngle - currentAngle

                            val lowerThreshold = currentAngle - (360f / (maxValue - minValue) * 5)
                            val higherThreshold = currentAngle + (360f / (maxValue - minValue) * 5)

                            if (dragStartedAngle in lowerThreshold..higherThreshold) {
                                positionValue =
                                    (oldPositionValue + (changeAngle / (360f / (maxValue - minValue))).roundToInt()).coerceIn(
                                        minValue, maxValue
                                    )

                                if (isBuy) {
                                    // Slider moves USD -> Calculate coin amount
                                    val usdValue = positionValue.toDouble()
                                    val amountOfCoin = if (pricePerCoin > 0) usdValue / pricePerCoin else 0.0
                                    formattedUsdValue = "%,.2f".format(usdValue)
                                    formattedAmount = "%,.6f".format(amountOfCoin)
                                }
                                else {
                                    // Slider moves amount of coin -> Calculate USD
                                    val amountOfCoin =  (positionValue / 100.0) * availableCoins!!
                                    val usdValue = amountOfCoin * pricePerCoin
                                    formattedUsdValue = "%,.2f".format(usdValue)
                                    formattedAmount = "%,.6f".format(amountOfCoin)
                                }
                            }
                        },
                        onDragEnd = {
                            oldPositionValue = positionValue
                            onPositionChange(positionValue)

                            if (isBuy) {
                                val usdValue = positionValue.toDouble()
                                val amountOfCoin = if (pricePerCoin > 0) usdValue / pricePerCoin else 0.0
                                formattedUsdValue = "%,.2f".format(usdValue)
                                formattedAmount = "%,.6f".format(amountOfCoin)
                            }
                            else {
                                val amountOfCoin =  (positionValue / 100.0) * availableCoins!!
                                val usdValue = amountOfCoin * pricePerCoin
                                formattedUsdValue = "%,.2f".format(usdValue)
                                formattedAmount = "%,.6f".format(amountOfCoin)
                            }
                        }
                    )
                }
        ){
            val width = size.width
            val height = size.height
            val circleThickness = width / 25f
            circleCenter = Offset(x = width/2f, y = height/2f)


            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        primaryColor.copy(0.45f),
                        secondaryColor.copy(0.15f)
                    )
                ),
                radius = circleRadius,
                center = circleCenter
            )


            drawCircle(
                style = Stroke(
                    width = circleThickness
                ),
                color = secondaryColor,
                radius = circleRadius,
                center = circleCenter
            )

            drawArc(
                color = primaryColor,
                startAngle = 90f,
                sweepAngle = (360f/maxValue) * positionValue.toFloat(),
                style = Stroke(
                    width = circleThickness,
                    cap = StrokeCap.Round
                ),
                useCenter = false,
                size = Size(
                    width = circleRadius * 2f,
                    height = circleRadius * 2f
                ),
                topLeft = Offset(
                    (width - circleRadius * 2f)/2f,
                    (height - circleRadius * 2f)/2f
                )

            )

            val outerRadius = circleRadius + circleThickness/2f
            val gap = 15f
            for (i in 0 .. (maxValue-minValue)){
                val color = if(i < positionValue-minValue) primaryColor else primaryColor.copy(alpha = 0.3f)
                val angleInDegrees = i*360f/(maxValue-minValue).toFloat()
                val angleInRad = angleInDegrees * PI / 180f + PI/2f

                val yGapAdjustment = cos(angleInDegrees * PI / 180f)*gap
                val xGapAdjustment = -sin(angleInDegrees * PI / 180f)*gap

                val start = Offset(
                    x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                    y = (outerRadius * sin(angleInRad) + circleCenter.y + yGapAdjustment).toFloat()
                )

                val end = Offset(
                    x = (outerRadius * cos(angleInRad) + circleCenter.x + xGapAdjustment).toFloat(),
                    y = (outerRadius * sin(angleInRad) + circleThickness + circleCenter.y + yGapAdjustment).toFloat()
                )

                rotate(
                    angleInDegrees,
                    pivot = start
                ){
                    drawLine(
                        color = color,
                        start = start,
                        end = end,
                        strokeWidth = 1.dp.toPx()
                    )
                }

            }

            drawContext.canvas.nativeCanvas.apply {
                drawIntoCanvas {
                    val textSpacing = 30.dp.toPx()  // Space between lines
                    val padding=10.dp.toPx()
                    // 1. Display percentage
                    drawText(
                        formattedAmount,
                        circleCenter.x,
                        circleCenter.y - textSpacing,  // Move slightly upward for balance
                        Paint().apply {
                            textSize = 22.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            color = textColor.toArgb()
                            isFakeBoldText = true
                        }
                    )

                    // 2. "amount of coin" below percentage
                    drawText(
                        "Amount of coin",
                        circleCenter.x,
                        circleCenter.y-padding,  // Center position
                        Paint().apply {
                            textSize = 12.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            color = textColor.toArgb()
                        }
                    )

                    // 3. "$145.00" below "amount of coin"
                    drawText(
                        "$$formattedUsdValue",
                        circleCenter.x,
                        circleCenter.y + textSpacing,  // Move down
                        Paint().apply {
                            textSize = 22.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            color = textColor.toArgb()
                        }
                    )

                    // 4. "Amount in USD" below the amount
                    drawText(
                        "Amount in USD",
                        circleCenter.x,
                        circleCenter.y + (textSpacing * 2)-padding,  // Move further down
                        Paint().apply {
                            textSize = 12.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            color = textColor.toArgb()
                        }
                    )
                }
            }
        }
    }
}

