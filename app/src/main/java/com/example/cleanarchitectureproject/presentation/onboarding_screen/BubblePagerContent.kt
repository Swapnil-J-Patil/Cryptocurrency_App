package com.example.cleanarchitectureproject.presentation.onboarding_screen

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.onboarding_screen.components.BubblePager
import com.example.cleanarchitectureproject.presentation.onboarding_screen.components.PagerIndicator
import com.example.cleanarchitectureproject.data.local.shared_prefs.PrefsManager
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BubblePagerContent(
    navController: NavController,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val prefsManager = remember { PrefsManager(context) } // Initialize SharedPreferences

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        BubblePager(
            pagerState = pagerState,
            pageCount = onboardingItems.size,
            modifier = Modifier.fillMaxSize(),
            bubbleColors = onboardingItems.map { it.backgroundColor },
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(
                        onboardingItems[page].animation
                    )
                )

                // Use animateLottieCompositionAsState inside the Composable scope
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    speed = if (page != 2) 1.8f else 1.0f
                )

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 140.dp)
                        .height(280.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Transparent)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp), // Move the card up to overlap the background
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(topStart = 120.dp) // Curved top edges
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PagerIndicator(items = onboardingItems, currentPage = pagerState.currentPage)

                    Text(
                        text = onboardingItems[pagerState.currentPage].title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        color = onboardingItems[pagerState.currentPage].mainColor,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Text(
                        text = onboardingItems[pagerState.currentPage].desc,
                        modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
                        color = Color.Gray,
                        fontFamily = Poppins,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraLight
                    )

                    // Buttons (Skip / Get Started)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 24.dp)
                    ) {
                        if (pagerState.currentPage != onboardingItems.lastIndex) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .padding(top = 4.dp, start = 10.dp, end = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                TextButton(onClick = {
                                    prefsManager.setOnboardingCompleted(true)
                                    prefsManager.setDollarAmount("1000")
                                    navController.navigate(Screen.AuthScreen.route ) {
                                        popUpTo(Screen.OnboardingScreen.route ) { inclusive = true }
                                    }
                                }) {
                                    Text(
                                        text = "Skip Now",
                                        color = Color.Gray,
                                        fontFamily = Poppins,
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                OutlinedButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            val nextPage = pagerState.currentPage + 1
                                            val pageWidth =
                                                1000f // Adjust based on your layout if needed

                                            pagerState.animateScrollBy(
                                                value = pageWidth, // Scroll a full page width
                                                animationSpec = tween(
                                                    durationMillis = 1000, // Adjust for slower scrolling
                                                    easing = LinearEasing // Ensures uniform motion (no stops)
                                                )
                                            )
                                            // Ensure it lands exactly on the next page
                                            pagerState.scrollToPage(nextPage)
                                        }
                                    },
                                    border = BorderStroke(
                                        14.dp,
                                        onboardingItems[pagerState.currentPage].mainColor
                                    ),
                                    shape = RoundedCornerShape(50), // = 50% percent
                                    //or shape = CircleShape
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = onboardingItems[pagerState.currentPage].mainColor),
                                    modifier = Modifier.size(65.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_right_arrow),
                                        contentDescription = "",
                                        tint = onboardingItems[pagerState.currentPage].mainColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                        } else {
                            Button(
                                onClick = {
                                    prefsManager.setOnboardingCompleted(true)
                                    prefsManager.setDollarAmount("1000")
                                    navController.navigate(Screen.AuthScreen.route ) {
                                        popUpTo(Screen.OnboardingScreen.route ) { inclusive = true }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.8f),
                                colors = ButtonDefaults.buttonColors(containerColor = onboardingItems[pagerState.currentPage].mainColor),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "Get Started",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}