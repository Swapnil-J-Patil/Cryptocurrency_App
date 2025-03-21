package com.example.cleanarchitectureproject.presentation.profile_screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cleanarchitectureproject.domain.model.ProfileData
import com.example.cleanarchitectureproject.presentation.profile_screen.components.ProfileDetailView
import com.example.cleanarchitectureproject.presentation.profile_screen.components.ProfileImageItem
import com.example.cleanarchitectureproject.presentation.shared.KeyStoreViewModel
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.example.cleanarchitectureproject.presentation.ui.theme.green

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

    LaunchedEffect(tokens) {
        if (!tokens.isNullOrEmpty())
        {
            Log.d("tokensKeystore", "ProfileScreen: ${tokens}")
            currentName=tokens.get(0)
            if (tokens.size >= 3) {
                currentProfile = ProfileDataList.characters.get(tokens.get(2).toInt())
            }
        }
    }
    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize()

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
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins
                )
                Text(
                    text = currentName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = Poppins
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(top = 145.dp, start = 15.dp, end = 15.dp),
            ) {

            }
        }

        // Profile Detail View
        ProfileDetailView(
            profile = selectedProfile,
            onSave = { profile,username->
                currentProfile=profile
                currentName=username
                val index= profile.id -1
                keyStoreViewModel.clearTokens()

                keyStoreViewModel.saveToken(username)
                keyStoreViewModel.saveToken("dummyemail@gmail.com")
                keyStoreViewModel.saveToken(index.toString())

                selectedProfile = null
                     },
            onDisMiss = {
                selectedProfile=null
            },
            userName = currentName,
            profileList = ProfileDataList.characters.filter { it != currentProfile }
        )
    }
}



