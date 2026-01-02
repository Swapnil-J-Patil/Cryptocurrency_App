package com.swapnil.dreamtrade.presentation.common_components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@Composable
fun SquishyToggleSwitch(
    color: Color,
    containerHeight: Int = 32,
    containerWidth: Int = 60,
    circleSize: Int = 24,
    padding: Int = 4,
    isTurnedOn: Boolean =false,
    shadowOffset: Int = 5,
    onTurnedOn:()->Unit,
    onTurnedOff:()->Unit
) {
    var isToggled by remember { mutableStateOf(isTurnedOn) }
    val scope = rememberCoroutineScope()

    val transition = updateTransition(targetState = isToggled, label = "Switch Transition")
    /*val trackColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 1200) },
        label = "Track Color"
    ) { checked -> if (checked) color else Color.LightGray }*/

    // Animatable properties
    val thumbPosition = remember { Animatable(0f) }
    val squishX = remember { Animatable(1f) }
    val squishY = remember { Animatable(1f) }


    val trackColor by animateColorAsState(
        targetValue = androidx.compose.ui.graphics.lerp(Color.Gray, color, thumbPosition.value),
        animationSpec = tween(durationMillis = 100),
        label = "Track Color"
    )
    // Define easing curves for smooth, jelly-like movement
    val stretchEasing = CubicBezierEasing(0.75f, 0f, 1f, 1f)
    val compressEasing = CubicBezierEasing(0f, 0f, 0.2f, 1f) // Softer squash

    fun animateToggle(targetState: Boolean) {
        val targetValue = if (targetState) 1f else 0f
        scope.launch {
            // Step 1: Slight Stretch before movement
            val stretchXJob = launch {
                squishX.animateTo(1.20f, animationSpec = tween(300, easing = stretchEasing))
            }
            val compressYJob = launch {
                squishY.animateTo(0.9f, animationSpec = tween(300, easing = stretchEasing))
            }
            // joinAll(stretchXJob, compressYJob)

            // Step 2: Move while keeping squish
            val moveJob = launch {
                thumbPosition.animateTo(
                    targetValue,
                    animationSpec = tween(350, easing = compressEasing)
                )
            }

            joinAll(stretchXJob, compressYJob,moveJob)

            // Step 3: Gentle Squash after reaching the other side
            val squashXJob = launch {
                squishX.animateTo(0.95f, animationSpec = tween(300, easing = compressEasing))
            }
            val expandYJob = launch {
                squishY.animateTo(1.05f, animationSpec = tween(300, easing = compressEasing))
            }
            joinAll(squashXJob, expandYJob)

            // Step 4: Restore to normal
            launch { squishX.animateTo(1f, animationSpec = tween(350)) }
            launch { squishY.animateTo(1f, animationSpec = tween(350)) }
        }
    }
    LaunchedEffect(Unit) {
        animateToggle(isTurnedOn)
    }
    val maxTranslation =
        with(LocalDensity.current) { (containerWidth - circleSize - (padding * 2)).dp.toPx() }

    Box(
        modifier = Modifier
            .size(containerWidth.dp, containerHeight.dp)
            .clip(CircleShape)
            .background(trackColor)

            .padding(padding.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Canvas(
            modifier = Modifier
                .size(circleSize.dp)
                .graphicsLayer(
                    scaleX = squishX.value,
                    scaleY = squishY.value,
                    shape = CircleShape,
                    translationX = thumbPosition.value * maxTranslation, // FIXED TRANSLATION
                    shadowElevation = with(LocalDensity.current) { 10.dp.toPx() } // Outer shadow
                )
                .clip(CircleShape)
                .clickable(
                    indication = null, // 🔹 Removes the ripple effect
                    interactionSource = remember { MutableInteractionSource() } // 🔹 Prevents highlight on touch
                ) {
                    isToggled = !isToggled
                    animateToggle(isToggled)
                    if (isToggled) {
                        onTurnedOn()
                    } else {
                        onTurnedOff()
                    }
                }

        ) {
            val shadowColor = Color.Black.copy(alpha = 0.4f) // 10% opacity

            drawCircle(
                color = Color.White,
                style = Fill
            )

            // Inset Shadow Effect
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    shader = RadialGradientShader(
                        center = Offset(
                            size.width / 2,
                            size.height / 2 - shadowOffset.dp.toPx()
                        ), // Moves it UP
                        radius = size.width / 1.2f, // Slightly smaller than full size
                        colors = listOf(
                            Color.White, // Fades inward
                            Color.White,
                            // Color.White,
                            shadowColor, // Dark edges (simulating depth)
                        ),
                        colorStops = listOf(0f,0.6f,1f), // Gradual transition
                        tileMode = TileMode.Clamp
                    )
                }
                canvas.drawCircle(
                    Offset(size.width / 2, size.height / 2),
                    size.minDimension / 2,
                    paint
                )
            }
        }
    }
}


