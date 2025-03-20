package com.example.cleanarchitectureproject.presentation.profile_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cleanarchitectureproject.domain.model.ProfileData
import com.example.cleanarchitectureproject.presentation.common_components.Divider
import com.example.cleanarchitectureproject.presentation.profile_screen.ProfileDataList

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
@Composable
fun SharedTransitionScope.ProfileDetailView(
    profile: ProfileData?,
    onDismiss: () -> Unit,
    userName: String,
    profileList: List<ProfileData>
) {

    var name by remember {
        mutableStateOf(userName)
    }
    var selectedImage by remember {
        mutableStateOf(profile)
    }
    LaunchedEffect(profile) {
        selectedImage=profile
    }
    AnimatedContent(
        targetState = profile,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ProfileDetailView"
    ) { profile ->
        if (profile!= null && selectedImage != null) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(16.dp)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "profile-bounds"),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileImageItem(
                        profile = selectedImage!!, // Use the same painter
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "profile-image"),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                            .clip(CircleShape),
                        size = 200.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .clip(RoundedCornerShape(8.dp)), // Apply rounded corners
                        shape = RoundedCornerShape(8.dp),
                        placeholder = { Text("Enter Username") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Person Icon"
                            )
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(text = "Change Avatar", padding = 10.dp)

                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        maxItemsInEachRow = 5,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        profileList.forEach{profile->
                            ProfileImageItem(
                                profile = profile,
                                modifier = Modifier.clickable {
                                    selectedImage=profile
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = onDismiss) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
