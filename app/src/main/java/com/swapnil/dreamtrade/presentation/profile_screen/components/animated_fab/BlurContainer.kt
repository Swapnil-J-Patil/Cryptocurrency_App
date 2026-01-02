package com.swapnil.dreamtrade.presentation.profile_screen.components.animated_fab

import android.annotation.SuppressLint
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun BlurContainer(
    modifier: Modifier = Modifier,
    blur: Float = 40f,
    component: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .customBlur(blur)
            ,
            content = component,
        )
        Box(
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@SuppressLint("SuspiciousModifierThen")
fun Modifier.customBlur(blur: Float) = this.then(
    graphicsLayer {
        if (blur > 0f)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                renderEffect = RenderEffect
                    .createBlurEffect(
                        blur,
                        blur,
                        Shader.TileMode.DECAL,
                    )
                    .asComposeRenderEffect()
            }
    }
)