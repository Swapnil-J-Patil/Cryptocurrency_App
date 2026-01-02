package com.swapnil.dreamtrade.presentation.profile_screen.components.animated_fab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.swapnil.dreamtrade.presentation.ui.theme.green


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AnimatedFab(
    onFilterClicked: (Int) -> Unit = {},
) {

    var expanded: Boolean by remember {
        mutableStateOf(false)
    }

    //val offset = remember { mutableStateOf(Offset(0f, 0f)) }

    val alpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = ""
    )

    ShaderContainer(
        modifier = Modifier
            .width(120.dp)
            .fillMaxHeight()
            /*.offset {
                IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt())
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // consume the gesture
                    offset.value += dragAmount
                }
            }*/
    ) {

        ButtonComponent(
            Modifier.padding(
                paddingValues = PaddingValues(
                    bottom = 80.dp
                ) * FastOutSlowInEasing
                    .transform((alpha))
            ),
            onClick = {
                onFilterClicked(1)
                expanded = !expanded
            }
        ) {
            Icon(
                imageVector = Icons.Default.CurrencyBitcoin,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.alpha(alpha)
            )
        }

        ButtonComponent(
            Modifier.padding(
                paddingValues = PaddingValues(
                    bottom = 160.dp
                ) * FastOutSlowInEasing.transform(alpha)
            ),
            onClick = {
                onFilterClicked(2)
                expanded = !expanded
            }
        ) {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.alpha(alpha)
            )
        }

        ButtonComponent(
            Modifier.padding(
                paddingValues = PaddingValues(
                    bottom = 240.dp
                ) * FastOutSlowInEasing.transform(alpha)
            ),
            onClick = {
                onFilterClicked(3)
                expanded = !expanded
            }
        ) {
            Icon(
                imageVector = Icons.Default.SettingsBackupRestore,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.alpha(alpha)
            )
        }

        ButtonComponent(
            Modifier.align(Alignment.BottomEnd),
            onClick = {
                expanded = !expanded
            },
        ) {
            val rotation by animateFloatAsState(
                targetValue = if (expanded) 45f else 0f,
                label = "",
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.rotate(rotation),
                tint = Color.White
            )
        }
    }
}

@Composable
fun BoxScope.ButtonComponent(
    modifier: Modifier = Modifier,
    background: Color = green,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    BlurContainer(
        modifier = modifier
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = onClick,
            )
            .align(Alignment.BottomEnd),
        component = {
            Box(
                Modifier
                    .size(40.dp)
                    .background(color = background, CircleShape)
            )
        }
    ) {
        Box(
            Modifier.size(80.dp),
            content = content,
            contentAlignment = Alignment.Center,
        )
    }
}

private operator fun PaddingValues.times(factor: Float): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(LayoutDirection.Ltr) * factor,
        end = this.calculateEndPadding(LayoutDirection.Ltr) * factor,
        top = this.calculateTopPadding() * factor,
        bottom = this.calculateBottomPadding() * factor
    )
}