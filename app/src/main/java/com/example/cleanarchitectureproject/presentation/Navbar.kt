package com.example.cleanarchitectureproject.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.CandlestickChart
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cleanarchitectureproject.presentation.ui.theme.green

sealed class Navbar(
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    object Home: Navbar("Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Market: Navbar("Market", Icons.Filled.InsertChart, Icons.Outlined.InsertChart)
    object Saved: Navbar("Saved", Icons.Filled.Bookmark, Icons.Outlined.Bookmark)
    object Profile: Navbar("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}