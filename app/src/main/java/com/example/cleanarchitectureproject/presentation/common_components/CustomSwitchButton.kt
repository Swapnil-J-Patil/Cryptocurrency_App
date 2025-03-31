package com.example.cleanarchitectureproject.presentation.common_components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.ui.theme.Blue
import com.example.cleanarchitectureproject.presentation.ui.theme.DayYellow
import com.example.cleanarchitectureproject.presentation.ui.theme.NavyBlue
import com.example.cleanarchitectureproject.presentation.ui.theme.NightGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomSwitchButton() {

    var isSwitchOn by remember { mutableStateOf(false) }

    val bgColor: Color by animateColorAsState(
        if (isSwitchOn) Blue else NavyBlue,
        animationSpec = tween(300, easing = LinearEasing),
        label = "Animated Switch Color"
    )

    val thumbColor: Color by animateColorAsState(
        if (isSwitchOn) DayYellow else NightGray,
        animationSpec = tween(300, easing = LinearEasing),
        label = "Animated Switch Color"
    )

    val offset by animateDpAsState(
        targetValue = if (isSwitchOn) {
            45.dp
        } else {
            8.dp
        }, label = "offset", animationSpec = tween(
            300, easing = LinearEasing
        )
    )

    val rotation by animateFloatAsState(
        targetValue = if (isSwitchOn) 180f else 0f,
        animationSpec = tween(300, easing = LinearEasing),
        label = ""
    )

    Box(
        modifier = Modifier
            .padding(20.dp)
            .width(104.dp)
            .height(64.dp)
            .clip(shape = CircleShape)
            .background(bgColor)
            .clickable {
                isSwitchOn = !isSwitchOn
            },
        contentAlignment = Alignment.CenterStart,
    ) {

        Box(
            modifier = Modifier
                .height(50.dp)
                .offset(x = offset)
                .clip(shape = CircleShape)
                .background(thumbColor),
        ) {

            Crossfade(
                isSwitchOn, animationSpec = tween(300), label = ""
            ) { targetState ->

                Icon(
                    imageVector = (if (targetState) Icons.Default.WbSunny else Icons.Default.WaterDrop),
                    contentDescription = "Day Mode",
                    modifier = Modifier
                        .padding(10.dp)
                        .rotate(rotation),
                    tint = Color.White
                )
            }
        }

    }

}
@Composable
fun SquishySwitch(color: Color, modifier: Modifier = Modifier) {
    var isChecked by remember { mutableStateOf(false) }
    var toggledOnce by remember { mutableStateOf(false) }

    val thumbOffset = remember { Animatable(0f) }
    val scaleX = remember { Animatable(1f) }
    val scaleY = remember { Animatable(1f) }
    val borderRadius = remember { Animatable(11f) }

    // Custom Easing (CSS-like Bezier Curves)
    val growEasing = CubicBezierEasing(0.75f, 0f, 1f, 1f)
    val bounceEasing = CubicBezierEasing(0f, 0f, 0.3f, 1.5f)

    // Function to apply Easing manually
    fun Easing.transform(from: Float, to: Float, value: Float): Float {
        val normalized = ((value - from) * (1f / (to - from))).coerceIn(0f, 1f)
        return transform(normalized)
    }

    // Handle animation when toggled
    LaunchedEffect(isChecked) {
        if (!toggledOnce) toggledOnce = true

        if (isChecked) {
            launch {
                borderRadius.animateTo(
                    targetValue = 11f * (1 / (34f / 22f)),
                    animationSpec = tween(200, easing = { growEasing.transform(0f, 1f, it) })
                )
                thumbOffset.animateTo(
                    targetValue = 8f,
                    animationSpec = tween(200, easing = { growEasing.transform(0f, 1f, it) })
                )
                scaleX.animateTo(
                    targetValue = 34f / 22f,
                    animationSpec = tween(200, easing = { growEasing.transform(0f, 1f, it) })
                )
                scaleY.animateTo(
                    targetValue = 16f / 22f,
                    animationSpec = tween(200, easing = { growEasing.transform(0f, 1f, it) })
                )
            }
            delay(200)

            launch {
                borderRadius.animateTo(
                    targetValue = 11f,
                    animationSpec = tween(200, easing = { bounceEasing.transform(0f, 1f, it) })
                )
                thumbOffset.animateTo(
                    targetValue = 52f - 26f,
                    animationSpec = tween(200, easing = { bounceEasing.transform(0f, 1f, it) })
                )
                scaleX.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(200, easing = { bounceEasing.transform(0f, 1f, it) })
                )
                scaleY.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(200, easing = { bounceEasing.transform(0f, 1f, it) })
                )
            }
        } else {
            launch {
                borderRadius.animateTo(
                    targetValue = 11f * (1 / (34f / 22f)),
                    animationSpec = tween(200, easing = { growEasing.transform(0f, 1f, it) })
                )
                thumbOffset.animateTo(
                    targetValue = 2f,
                    animationSpec = tween(200, easing = { growEasing.transform(0f, 1f, it) })
                )
                scaleX.animateTo(
                    targetValue = 34f / 22f,
                    animationSpec = tween(200, easing = { growEasing.transform(0f, 1f, it) })
                )
                scaleY.animateTo(
                    targetValue = 16f / 22f,
                    animationSpec = tween(200, easing = { growEasing.transform(0f, 1f, it) })
                )
            }
            delay(200)

            launch {
                borderRadius.animateTo(
                    targetValue = 11f,
                    animationSpec = tween(200, easing = { bounceEasing.transform(0f, 1f, it) })
                )
                thumbOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(200, easing = { bounceEasing.transform(0f, 1f, it) })
                )
                scaleX.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(200, easing = { bounceEasing.transform(0f, 1f, it) })
                )
                scaleY.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(200, easing = { bounceEasing.transform(0f, 1f, it) })
                )
            }
        }
    }

    Box(
        modifier = modifier
            .size(52.dp, 30.dp)
            .clip(RoundedCornerShape(50))
            .background(if (isChecked) color else Color.Gray)
            .clickable { isChecked = !isChecked }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset.value.dp)
                .size(22.dp)
                .clip(RoundedCornerShape(borderRadius.value.dp))
                .background(Color.White)
                .graphicsLayer(
                    scaleX = scaleX.value,
                    scaleY = scaleY.value,
                )
        )
    }
}









