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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cleanarchitectureproject.domain.model.ProfileData

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfileDetailView(
    profile: ProfileData?,
    onDismiss: () -> Unit
) {
    AnimatedContent(
        targetState = profile,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ProfileDetailView"
    ) { profile ->
        if (profile != null) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profile.imageUrl)
                    .crossfade(true)
                    .size(Size.ORIGINAL) // High-quality image
                    .build()
            )

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
                    Image(
                        painter = painter, // Use the same painter
                        contentDescription = profile.imageUrl,
                        contentScale = ContentScale.Crop, // High quality
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "profile-image"),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                            .clip(CircleShape)
                            .size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = profile.imageUrl,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = profile.id.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        }
    }
}
