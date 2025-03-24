package com.example.cleanarchitectureproject.presentation.profile_screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.domain.model.ProfileData
import com.example.cleanarchitectureproject.presentation.common_components.Tabs
import com.example.cleanarchitectureproject.presentation.profile_screen.components.ProfileDetailView
import com.example.cleanarchitectureproject.presentation.profile_screen.components.ProfileImageItem
import com.example.cleanarchitectureproject.presentation.shared.KeyStoreViewModel
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import com.example.cleanarchitectureproject.presentation.ui.theme.lightBackground

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfileScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    keyStoreViewModel: KeyStoreViewModel = hiltViewModel()

) {
    val tokens by keyStoreViewModel.tokens.collectAsState()
    var selectedProfile by remember { mutableStateOf<ProfileData?>(null) }
    var currentProfile by remember { mutableStateOf<ProfileData>(ProfileDataList.characters.first()) }
    var currentName by remember { mutableStateOf("Swapnil Patil") }
    val tabTitles = listOf("Portfolio", "Transactions")
    var visibility by remember {
        mutableStateOf(false)
    }
    val brushColors = listOf(Color(0xFF23af92), Color(0xFF0E5C4C))
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
    LaunchedEffect(tokens) {
        if (!tokens.isNullOrEmpty()) {
            Log.d("tokensKeystore", "ProfileScreen: ${tokens}")
            currentName = tokens.get(0)
            if (tokens.size >= 3) {
                currentProfile = ProfileDataList.characters.get(tokens.get(2).toInt())
            }
        }
    }
    LaunchedEffect(Unit) {
        visibility=!visibility
    }
    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize()
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
                    .background(Color.Transparent)
                    .padding(top = 45.dp, start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    visible = selectedProfile == null,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Box(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "profile-bounds"),
                                animatedVisibilityScope = this
                            )
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable {
                                selectedProfile = currentProfile
                            }
                    ) {
                        ProfileImageItem(
                            profile = currentProfile,
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(key = "profile-image"),
                                animatedVisibilityScope = this@AnimatedVisibility
                            ),

                            )
                    }
                }
                // Spacer(modifier = Modifier.width(15.dp))

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 80.dp, top = 42.dp), // Ensures text doesn't shift
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hello",
                    style = MaterialTheme.typography.titleMedium,
                    color = lightBackground,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins
                )
                Text(
                    text = currentName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontFamily = Poppins
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(top = 115.dp),
            ) {
                AnimatedVisibility(
                    visible = visibility,
                    enter = slideInVertically(
                        initialOffsetY = { it }, // Starts from full height (bottom)
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
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        elevation = CardDefaults.cardElevation(16.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Tabs(
                            screen = "profile",
                            tabTitles = tabTitles,
                            onItemClick = { item, flag ->

                            },
                            animatedVisibilityScope = animatedVisibilityScope,
                            onAuthClick = { type, method, email, password ->

                            }
                        )
                    }
                }
            }
        }

        // Profile Detail View
        ProfileDetailView(
            profile = selectedProfile,
            onSave = { profile, username ->
                currentProfile = profile
                currentName = username
                val index = profile.id - 1
                keyStoreViewModel.clearTokens()

                keyStoreViewModel.saveToken(username)
                keyStoreViewModel.saveToken("dummyemail@gmail.com")
                keyStoreViewModel.saveToken(index.toString())

                selectedProfile = null
            },
            onDisMiss = {
                selectedProfile = null
            },
            userName = currentName,
            profileList = ProfileDataList.characters.filter { it != currentProfile }
        )
    }
}



