package com.example.androidpractices.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Launches : Screen("launches", "Запуски", Icons.Default.Home)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
    object Settings : Screen("settings", "Настройки", Icons.Default.Settings)

    object LaunchDetail : Screen("launch_detail/{launchId}", "Детали") {
        fun createRoute(launchId: String) = "launch_detail/$launchId"
    }
}

val bottomNavItems = listOf(
    Screen.Launches,
    Screen.Profile,
    Screen.Settings
)