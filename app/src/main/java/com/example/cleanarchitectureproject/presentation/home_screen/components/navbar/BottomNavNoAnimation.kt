package com.example.cleanarchitectureproject.presentation.home_screen.components.navbar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.cleanarchitectureproject.presentation.Navbar

@Composable
fun BottomNavAnimation(
    screens: List<Navbar>,
    isTab:Boolean,
    onClick:(Int)->Unit
) {
    var selectedScreen by remember { mutableStateOf(0) }
    if (!isTab)
    {
    Box(
        Modifier
            .shadow(5.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .height(64.dp)
            .fillMaxWidth()
           // .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (screen in screens) {
                val isSelected = screen == screens[selectedScreen]
                val animatedWeight by animateFloatAsState(
                    targetValue = if (isSelected) 1.5f else 1f
                )
                Box(
                    modifier = Modifier.weight(animatedWeight),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    BottomNavItem(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            selectedScreen = screens.indexOf(screen)
                            onClick(selectedScreen)
                        },
                        screen = screen,
                        isSelected = isSelected,
                        isTab = isTab
                    )
                }
            }
        }
    }
    }
    else
    {
        Box(
            Modifier
                .shadow(5.dp)
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxHeight() // Ensure the Box takes full height
                .width(140.dp) // Adjust width for vertical layout
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiary),
                 // Fill available space
                verticalArrangement = Arrangement.Top, // Align items at the top
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                for (screen in screens) {
                    val isSelected = screen == screens[selectedScreen]
                    val animatedWeight by animateFloatAsState(
                        targetValue = if (isSelected) 1.5f else 1f
                    )

                    Box(
                        modifier = Modifier
                            .weight(animatedWeight, fill = false) // Allow flexible spacing
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val interactionSource = remember { MutableInteractionSource() }
                        BottomNavItem(
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selectedScreen = screens.indexOf(screen)
                                onClick(selectedScreen)
                            },
                            screen = screen,
                            isSelected = isSelected,
                            isTab = isTab
                        )
                    }
                }
            }
        }

    }
}