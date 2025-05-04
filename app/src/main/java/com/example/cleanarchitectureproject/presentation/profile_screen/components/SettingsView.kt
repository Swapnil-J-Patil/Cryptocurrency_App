package com.example.cleanarchitectureproject.presentation.profile_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cleanarchitectureproject.domain.model.ProfileData
import com.example.cleanarchitectureproject.presentation.profile_screen.ProfileDataList
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins
import com.example.cleanarchitectureproject.presentation.ui.theme.green
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
@Composable
fun SharedTransitionScope.SettingsView(
    settings: Boolean?,
    onDisMiss:()-> Unit,
    isDarkTheme: Boolean,
    onThemeChange:()-> Unit
) {

   /* var selectedImage by remember {
        mutableStateOf(settings)
    }
    LaunchedEffect(settings) {
        selectedImage = settings
    }*/
    var isChecked by remember { mutableStateOf(isDarkTheme) }
    var currentMode by remember { mutableStateOf("Light Mode") }
    LaunchedEffect(isChecked) {
        if(!isChecked)
        {
            //delay(1000)
            currentMode="Light Mode"
        }
        else
        {
            //delay(1000)
            currentMode="Dark Mode"
        }
    }
    AnimatedContent(
        targetState = settings,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "SettingsView"
    ) { profile ->
        if (profile != null) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "settings-bounds"),
                            animatedVisibilityScope = this@AnimatedContent
                        ),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiaryContainer),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(bottom = 40.dp),
                        ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 5.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { onDisMiss() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp), // Ensures text doesn't shift
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.padding(top=25.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(35.dp)
                                        .sharedElement(
                                            state = rememberSharedContentState(key = "settings-image"),
                                            animatedVisibilityScope = this@AnimatedContent
                                        ),
                                    tint = if(isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "Settings",
                                    modifier = Modifier.padding(start = 5.dp),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = if(isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Poppins
                                )
                            }


                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp, start = 16.dp, end = 16.dp), // Ensures text doesn't shift
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Spacer(Modifier.height(15.dp))
                            Text(
                                text = currentMode,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins
                            )
                            Spacer(Modifier.height(5.dp))
                            DarkModeSwitch(
                                checked = isChecked,
                                modifier = Modifier,
                                switchWidth = 68.dp,
                                switchHeight = 30.dp,
                                handleSize = 20.dp,
                                handlePadding = 4.dp,
                                onCheckedChanged = {flag->
                                    isChecked=flag
                                    onThemeChange()
                                }
                            )

                            Spacer(Modifier.height(10.dp))

                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                thickness = 2.dp
                            )

                            ItemSettings(text = "Earn More Coins", onClick = {})

                            ItemSettings(text = "About Us", onClick = {})

                            ItemSettings(text = "Help", onClick = {})

                            ItemSettings(text = "Logout", onClick = {})


                        }
                    }
                }
            }
        }
    }
}


