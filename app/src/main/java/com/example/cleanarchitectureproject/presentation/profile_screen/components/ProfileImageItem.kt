package com.example.cleanarchitectureproject.presentation.profile_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cleanarchitectureproject.domain.model.ProfileData

@Composable
fun ProfileImageItem(
    profile: ProfileData,
    modifier: Modifier = Modifier,
    size: Dp =50.dp,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            ,
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = profile.imageUrl,
            contentDescription = profile.imageUrl,
            contentScale = ContentScale.Crop, // Prevents distortion
            modifier = modifier
                .fillMaxSize() // Set proper size
                .clip(CircleShape)
                .background(Color.Gray)
        )
    }
}
