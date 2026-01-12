package com.swapnil.dreamtrade.presentation.splash_screen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.swapnil.dreamtrade.R
import com.swapnil.dreamtrade.presentation.Screen
import com.swapnil.dreamtrade.data.local.shared_prefs.PrefsManager
import com.swapnil.dreamtrade.presentation.ui.theme.Poppins
import com.swapnil.dreamtrade.presentation.ui.theme.lightBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController,context: Context) {
    val scale = remember { Animatable(0f) }
    val circleScale = remember { Animatable(1f) }
    var flag by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isTab = configuration.screenWidthDp.dp > 600.dp

    LaunchedEffect(key1 = true) {
        val prefsManager = PrefsManager(context)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { it } // Linear interpolation
            )
        )
        flag = true
        circleScale.animateTo(
            targetValue = if(isTab) 11f else 6f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = { it } // Linear interpolation
            )
        )

        delay(500L) // Wait for 1 second before showing text
        showText = true

        delay(1500L) // Wait for another 2 seconds before navigation
        if(prefsManager.isOnboardingCompleted() && !prefsManager.isFirebaseAuthCompleted() && !prefsManager.isBiometricAuthCompleted())
        {
            navController.navigate(Screen.AuthScreen.route ) {
                popUpTo(Screen.SplashScreen.route ) { inclusive = true }
            }
        }
        else if(!prefsManager.isOnboardingCompleted()){
            navController.navigate(Screen.OnboardingScreen.route ) {
                popUpTo(Screen.SplashScreen.route ) { inclusive = true }
            }
        }
        else
        {
            navController.navigate(Screen.MainScreen.route ) {
                popUpTo(Screen.SplashScreen.route ) { inclusive = true }
            }
        }

    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, lightBackground, Color.LightGray),
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(circleScale.value)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = if (flag) listOf(Color(0xFF23af92), Color(0xFF121212)) else listOf(
                            Color.Transparent,
                            Color.Transparent
                        ),
                    )
                )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Splash Image",
                modifier = Modifier
                    .size(220.dp)
                    .scale(scale.value)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
            )

            // Animated Visibility for "DreamTrade" after 1 second
            AnimatedVisibility(visible = showText) {
                Text(
                    text = "DreamTrade",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    fontFamily = Poppins,
                    )
            }
        }
    }
}
