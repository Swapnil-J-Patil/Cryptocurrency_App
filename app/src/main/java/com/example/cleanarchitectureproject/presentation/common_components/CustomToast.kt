package com.example.cleanarchitectureproject.presentation.common_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.domain.model.SweetToastProperty

@Composable
fun CustomSweetToast(
    message: String,
    type: SweetToastProperty,
    durationMillis: Long = 3000, // Auto-dismiss time
    onDismiss: () -> Unit,
    visibility: Boolean
) {
    val progress = remember { Animatable(1f) }

    // Reset progress and start animation when visibility becomes true
    LaunchedEffect(visibility) {
        if (visibility) {
            progress.snapTo(1f) // Reset progress instantly
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis.toInt(), easing = LinearEasing)
            )
            onDismiss() // Dismiss toast after progress completion
        }
    }

    // Slide-Up Animation
    val animatedOffset by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(800, easing = LinearOutSlowInEasing),
        label = "toast-slide-up"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .background(Color.Transparent)
            .offset(y = animatedOffset.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visibility,
            enter = scaleIn(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ),
            exit = scaleOut()
        ) {
            Column(
                modifier = Modifier
                    .shadow(6.dp, shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp))
                    .border(2.dp, type.getBorderColor(), RoundedCornerShape(8.dp))
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Icon(
                            imageVector = type.getResourceId(),
                            contentDescription = "Toast Icon",
                            tint = type.getProgressBarColor(),
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = message,
                            modifier = Modifier.padding(start = 10.dp, end = 15.dp, top = 5.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Toast Icon",
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier
                            .size(25.dp)
                            .align(AbsoluteAlignment.CenterRight)
                            .clickable {
                                onDismiss()
                            }
                    )
                }

                // Smooth Progress Bar
                LinearProgressIndicator(
                    progress = progress.value, // Smooth animated progress
                    color = type.getProgressBarColor(),
                    trackColor = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            }
        }
    }
}