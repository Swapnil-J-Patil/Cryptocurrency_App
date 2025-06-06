package com.example.cleanarchitectureproject.presentation.profile_screen.components.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanarchitectureproject.data.local.shared_prefs.PrefsManager
import com.example.cleanarchitectureproject.presentation.Screen
import com.example.cleanarchitectureproject.presentation.auth_screen.AuthViewModel
import com.example.cleanarchitectureproject.presentation.profile_screen.components.SettingsIconItem
import com.example.cleanarchitectureproject.presentation.shared.KeyStoreViewModel
import com.example.cleanarchitectureproject.presentation.ui.theme.Poppins

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
@Composable
fun SharedTransitionScope.SettingsView(
    settings: ImageVector?,
    onDisMiss: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    keyStoreViewModel: KeyStoreViewModel = hiltViewModel(),
    onLogout:()->Unit
    ) {
    val context= LocalContext.current
    val prefsManager = PrefsManager(context)

     var selectedImage by remember {
         mutableStateOf(settings)
     }
     LaunchedEffect(settings) {
         selectedImage = settings
     }
    var isChecked by remember { mutableStateOf(isDarkTheme) }
    var currentMode by remember { mutableStateOf("Light Mode") }
    val color =
        if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    LaunchedEffect(isChecked) {
        if (!isChecked) {
            //delay(1000)
            currentMode = "Light Mode"
        } else {
            //delay(1000)
            currentMode = "Dark Mode"
        }
    }
    AnimatedContent(
        targetState = settings,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "SettingsView"
    ) { image ->
        if (image != null && selectedImage != null) {

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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, end = 5.dp),
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

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .offset(y=-15.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        SettingsIconItem(
                            icon = selectedImage!!,
                            modifier = Modifier
                                .sharedElement(
                                    state = rememberSharedContentState(key = "settings-image"),
                                    animatedVisibilityScope = this@AnimatedContent
                                ),
                            size = 35.dp,
                            tint = color
                        )
                        Text(
                            text = "Settings",
                            modifier = Modifier.padding(start = 5.dp),
                            style = MaterialTheme.typography.headlineSmall,
                            color = color,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        )
                    }


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            ), // Ensures text doesn't shift
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(Modifier.height(10.dp))
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
                            onCheckedChanged = { flag ->
                                isChecked = flag
                                onThemeChange()
                            }
                        )

                        Spacer(Modifier.height(5.dp))

                        /* Divider(
                             modifier = Modifier.fillMaxWidth(),
                             color = MaterialTheme.colorScheme.tertiaryContainer,
                             thickness = 2.dp
                         )*/
                        Spacer(Modifier.height(30.dp))

                        ItemSettings(
                            text = "Earn More Coins", onClick = {
                                navController.navigate(Screen.LuckyWheelScreen.route)
                            },
                            tint = color,
                            icon = Icons.Default.MonetizationOn
                        )

                        ItemSettings(
                            text = "About Us", onClick = {
                                navController.navigate(Screen.AboutUsScreen.route)
                            },
                            tint = color,
                            icon = Icons.Default.Person
                        )

                        ItemSettings(
                            text = "Help", onClick = {
                                navController.navigate(Screen.HelpScreen.route)
                            },
                            tint = color,
                            icon = Icons.Default.Help
                        )

                        ItemSettings(
                            text = "Logout", onClick = {

                                prefsManager.setBiometricAuthCompleted(false)
                                keyStoreViewModel.clearTokens()

                                if(prefsManager.isFirebaseAuthCompleted())
                                {
                                    authViewModel.signOut()
                                    prefsManager.setFirebaseAuthCompleted(false)
                                }
                               onLogout()


                            },
                            tint = color,
                            icon = Icons.Default.Logout
                        )
                        Spacer(Modifier.height(30.dp))

                    }
                }
            }
        }
    }
}


