package com.swapnil.dreamtrade.presentation.profile_screen.components.about_us

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swapnil.dreamtrade.domain.model.AboutUsData
import com.swapnil.dreamtrade.presentation.ui.theme.Poppins
import com.swapnil.dreamtrade.presentation.ui.theme.grey
import com.swapnil.dreamtrade.presentation.ui.theme.lightGrey
import com.swapnil.dreamtrade.presentation.ui.theme.white
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("UnrememberedMutableState")
@Composable
fun AboutUsCard(
    modifier: Modifier = Modifier,
    data: AboutUsData,
    priority: Float,
    index: Int,
    onMoveToBack: () -> Unit
    ) {
    val xOffset = remember { Animatable(0f) }

    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(priority) }
    var isRightFlick by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    val getInitialOffset = when (index) {
        0 -> 0f
        1 -> -25f
        2 -> -50f
        3 -> -75f
        else -> 0f
    }
    val yOffset = derivedStateOf { getInitialOffset + (-abs(xOffset.value) * 0.2f) }

    Card(
        modifier = modifier
            .offset(
                x = xOffset.value.dp,
                y = yOffset.value.dp
            )
            .graphicsLayer(
                rotationZ = rotation.value,
                scaleX = scale.value,
                scaleY = scale.value
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isRightFlick = offset.x > size.width / 2
                    },
                    onDrag = { _, dragAmount ->
                        scope.launch {
                            xOffset.snapTo(xOffset.value + dragAmount.x)
                            rotation.snapTo(xOffset.value / 5) // rotate based on horizontal movement
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            coroutineScope {
                                // Step 1: Animate card off-screen (left/right)
                                val direction = if (isRightFlick) 300f else -300f
                                val offsetJob = launch {
                                    xOffset.animateTo(
                                        targetValue = direction,
                                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                                    )


                                }
                                val rotationJob = launch {
                                    rotation.animateTo(
                                        targetValue = if (isRightFlick) 360f else -360f,
                                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                                    )
                                }
                                val scaleJob = launch {
                                    scale.animateTo(
                                        targetValue = 0.8f,
                                        animationSpec = tween(300)
                                    )
                                }
                                joinAll(offsetJob, rotationJob, scaleJob)
                            }

                            onMoveToBack()

                            // Step 3: Reset animations
                            xOffset.snapTo(0f)
                            rotation.snapTo(0f)
                            scale.snapTo(priority)
                        }
                    }
                )
            },
        colors = CardDefaults.cardColors(containerColor = grey),
        border = BorderStroke(2.dp, lightGrey),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        AsyncImage(
            model = data.imageUrl,
            contentDescription = "Card Image",
            contentScale = ContentScale.FillBounds,
            modifier= Modifier.fillMaxWidth()
                .height(220.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = data.title,
            modifier = Modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            color = white
        )
        Spacer(Modifier.height(15.dp))
        Divider(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp),
            color = lightGrey,
            thickness = 2.dp
        )
        Spacer(Modifier.height(15.dp))
        Column() {
            data.text.forEach { text ->
                Text(
                    text = "• $text",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 16.dp, end = 10.dp, bottom = 12.dp)
                )
            }
        }
    }
}