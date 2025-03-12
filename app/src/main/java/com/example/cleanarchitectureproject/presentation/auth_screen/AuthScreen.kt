package com.example.cleanarchitectureproject.presentation.auth_screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanarchitectureproject.R
import com.example.cleanarchitectureproject.domain.model.BiometricResult
import com.example.cleanarchitectureproject.domain.model.SweetToastProperty
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.common_components.CustomSweetToast
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.example.cleanarchitectureproject.presentation.ui.theme.lightBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AuthScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: AuthViewModel = hiltViewModel(),
    biometricViewModel: BiometricViewModel
) {

    val tabTitles = listOf("Sign In", "Sign Up")
    val brushColors = listOf(Color(0xFF23af92), Color(0xFF0E5C4C))
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("Success!") }
    val coroutineScope = rememberCoroutineScope()
    var toastType by remember { mutableStateOf<SweetToastProperty>(Error()) }

    val authState by viewModel.authState.collectAsState()
    val biometricState by biometricViewModel.biometricState.collectAsState()

    LaunchedEffect(biometricState) {
        biometricState?.let { result ->
            when (result) {
                is BiometricResult.AuthenticationSuccess -> {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.AuthScreen.route) { inclusive = true }
                    }
                }

                is BiometricResult.AuthenticationError -> {
                    Log.d("biometricAuth", "AuthScreen: $result")
                    showToast = false // Dismiss current toast
                    coroutineScope.launch { // Ensure state updates properly
                        delay(100) // Small delay to allow recomposition
                        toastMessage = "Authentication failed"
                        toastType = Error()
                        showToast = true
                    }
                }

                is BiometricResult.AuthenticationFailed -> {
                    Log.d("biometricAuth", "AuthScreen: $result")
                    showToast = false // Dismiss current toast
                    coroutineScope.launch { // Ensure state updates properly
                        delay(100) // Small delay to allow recomposition
                        toastMessage = "Authentication failed"
                        toastType = Error()
                        showToast = true
                    }
                }

                is BiometricResult.HardwareUnavailable -> {
                    showToast = false // Dismiss current toast
                    coroutineScope.launch { // Ensure state updates properly
                        delay(100) // Small delay to allow recomposition
                        toastMessage = "Scanner not found!"
                        toastType = Error()
                        showToast = true
                    }
                }

                is BiometricResult.FeatureUnavailable -> {
                    showToast = false // Dismiss current toast
                    coroutineScope.launch { // Ensure state updates properly
                        delay(100) // Small delay to allow recomposition
                        toastMessage = "Feature unavailable for this device."
                        toastType = Error()
                        showToast = true
                    }
                }

                is BiometricResult.AuthenticationNotSet -> {
                    showToast = false // Dismiss current toast
                    coroutineScope.launch { // Ensure state updates properly
                        delay(100) // Small delay to allow recomposition
                        toastMessage = "No Biometrics Enrolled"
                        toastType = Error()
                        showToast = true
                    }
                }
            }
        }

    }

    LaunchedEffect(authState) {
        authState?.let { result ->
            when (result) {
                is AuthState.SignedIn -> {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.AuthScreen.route) { inclusive = true }
                    }
                }

                is AuthState.Error -> {
                    showToast = false // Dismiss current toast
                    coroutineScope.launch { // Ensure state updates properly
                        delay(100) // Small delay to allow recomposition
                        toastMessage = "Authentication failed"
                        toastType = Error()
                        showToast = true
                    }
                }

                is AuthState.SignedOut -> "Signed Out successfully!"
                is AuthState.Loading -> "Signing in..."
            }
        }
    }

    val context = LocalContext.current

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.handleSignInResult(result.data)
        }
    }

    // Animation state
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val targetOffset = with(LocalDensity.current) {
        1000.dp.toPx()
    }
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetOffset, // Adjust based on desired movement range
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    val scrollState = rememberScrollState()
    var visibility by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        visibility = true
    }

    Box() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState) // Make the Column scrollable
                .drawWithCache {
                    val brushSize = 400f
                    val brush = Brush.linearGradient(
                        colors = brushColors,
                        start = Offset(animatedOffset, animatedOffset),
                        end = Offset(animatedOffset + brushSize, animatedOffset + brushSize),
                        tileMode = TileMode.Mirror
                    )
                    onDrawBehind {
                        drawRect(brush)
                    }
                }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(top = 50.dp, start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Currency Logo (10%)
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                )

                Spacer(modifier = Modifier.width(15.dp))
                // Price and Percentage (20%)
                Text(
                    text = "DreamTrade",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontFamily = Poppins,
                    style = MaterialTheme.typography.displaySmall,
                )
            }

            Text(
                text = "Unleash your inner trader today.",
                color = lightBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Start,
                fontFamily = Poppins,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraLight
            )
            AnimatedVisibility(
                visible = visibility,
                enter = slideInVertically(
                    initialOffsetY = { it / 3 }, // Starts from full height (bottom)
                    animationSpec = tween(
                        durationMillis = 800, // Slightly increased duration for a smoother feel
                        easing = LinearOutSlowInEasing // Smoother easing for entering animation
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = LinearOutSlowInEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }, // Moves to full height (bottom)
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing // Keeps a natural exit motion
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 26.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    elevation = CardDefaults.cardElevation(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Tabs(
                        screen = "login",
                        tabTitles = tabTitles,
                        onItemClick = { item, flag ->

                        },
                        animatedVisibilityScope = animatedVisibilityScope,
                        onAuthClick = { type, method, email, password ->
                            when (type) {
                                "signIn" -> {
                                    when (method) {
                                        "email" -> {
                                            viewModel.signIn(email, password)
                                        }

                                        "gmail" -> {
                                            viewModel.signInWithGoogle(context, signInLauncher)
                                        }

                                        "fingerprint" -> {
                                            biometricViewModel.fingerprintAuth(
                                                "Authentication",
                                                "Use your fingerprint to authenticate"
                                            )
                                        }

                                        "pin" -> {
                                            biometricViewModel.deviceCredentialAuth(
                                                "Authentication",
                                                "Use device pin or pattern to authenticate"
                                            )
                                        }
                                    }
                                }

                                "signUp" -> {
                                    if (method == "email") {
                                        viewModel.signUp(email, password)
                                    }
                                }
                            }
                        }
                    )
                }
            }

        }
        CustomSweetToast(
            message = toastMessage,
            type = toastType,
            onDismiss = { showToast = !showToast },
            visibility = showToast
        )
    }

}