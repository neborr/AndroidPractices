package com.example.androidpractices.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.androidpractices.navigation.Screen
import com.example.androidpractices.navigation.bottomNavItems

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
x
            val showBottomBar = bottomNavItems.any { it.route == currentRoute }

            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                screen.icon?.let {
                                    Icon(imageVector = it, contentDescription = screen.title)
                                }
                            },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Launches.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Launches.route) {
                LaunchesScreen(
                    onLaunchClick = { launchId ->
                        navController.navigate(Screen.LaunchDetail.createRoute(launchId))
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = Screen.LaunchDetail.route,
                arguments = listOf(navArgument("launchId") { type = NavType.StringType })
            ) { backStackEntry ->
                val launchId = backStackEntry.arguments?.getString("launchId") ?: ""
                LaunchDetailScreen(
                    launchId = launchId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}