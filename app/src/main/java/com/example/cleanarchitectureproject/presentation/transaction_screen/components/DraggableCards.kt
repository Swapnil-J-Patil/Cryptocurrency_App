package com.example.cleanarchitectureproject.presentation.transaction_screen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.grey
import com.example.cleanarchitectureproject.presentation.ui.theme.lightGreen
import com.example.cleanarchitectureproject.presentation.ui.theme.white
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DraggableCards() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx =
        with(density) { configuration.screenHeightDp.dp.toPx() } // Convert screen height to pixels
    val maxDrag = screenHeightPx * 0.7f // Allow dragging until 10% from bottom

    val topCardOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        // Bottom Card (Revealed as top card is dragged)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
            border = BorderStroke(2.dp,MaterialTheme.colorScheme.tertiaryContainer),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CustomCircularProgressIndicator(
                    modifier = Modifier
                        .size(250.dp),
                    initialValue = 50,
                    primaryColor = green,
                    secondaryColor = grey,
                    circleRadius = 230f,
                    onPositionChange = { position ->
                        //do something with this position value
                    }
                )

            }
        }

        // Top Card (Draggable)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    top = 30.dp,
                    start = 6.dp,
                    end = 6.dp
                ) // Top padding to create stacked effect
                .offset { IntOffset(0, topCardOffset.value.roundToInt()) }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val newOffset = (topCardOffset.value + delta).coerceIn(0f, maxDrag)
                        coroutineScope.launch {
                            topCardOffset.snapTo(newOffset)
                        }
                    },
                    onDragStopped = {
                        coroutineScope.launch {
                            val target = if (topCardOffset.value > maxDrag / 2) maxDrag else 0f
                            topCardOffset.animateTo(target, tween(300))
                        }
                    }
                ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
            border = BorderStroke(2.dp,MaterialTheme.colorScheme.tertiaryContainer),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                )
                {

                }
            }
            Box(contentAlignment = Alignment.Center) {
                CustomCircularProgressIndicator(
                    modifier = Modifier
                        .size(250.dp)
                        .background(MaterialTheme.colorScheme.tertiary),
                    initialValue = 50,
                    primaryColor = green,
                    secondaryColor = grey,
                    circleRadius = 230f,
                    onPositionChange = { position ->
                        //do something with this position value
                    }
                )
            }
        }
    }
}
