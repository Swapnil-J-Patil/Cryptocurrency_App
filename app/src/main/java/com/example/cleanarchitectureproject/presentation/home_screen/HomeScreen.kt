package com.example.cleanarchitectureproject.presentation.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.Navbar
import com.example.cleanarchitectureproject.presentation.home_screen.components.BottomNavAnimation
import com.example.cleanarchitectureproject.presentation.home_screen.components.Carousel

@Composable
fun HomeScreen() {
    val imageUrls = listOf(
        "https://t4.ftcdn.net/jpg/07/49/00/79/360_F_749007978_BJP4clVoWmmPWkXqg8n8LZ0E604vjKgz.jpg",
        "https://img.freepik.com/premium-photo/ethereum-symbol-futuristic-style-metallic-blue-tones-cryptocurrency-technology-banner_1057260-11197.jpg",
        "https://png.pngtree.com/thumb_back/fh260/background/20231003/pngtree-captivating-3d-illustration-of-tether-cryptocurrency-image_13564713.png"
    )
    val screen = listOf(
        Navbar.Home,
        Navbar.Create,
        Navbar.Profile,
        Navbar.Settings
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ){
            Carousel(
                imageUrls = imageUrls,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            BottomNavAnimation(screens = screen)
        }
    }

}