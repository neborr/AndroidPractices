package com.example.androidpractices.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.androidpractices.AndroidPracticesApplication
import com.example.androidpractices.navigation.Screen
import com.example.androidpractices.navigation.bottomNavItems
import com.example.androidpractices.ui.detail.LaunchDetailViewModel
import com.example.androidpractices.ui.favorites.FavoritesScreen
import com.example.androidpractices.ui.favorites.FavoritesViewModel
import com.example.androidpractices.ui.launches.LaunchesViewModel
import com.example.androidpractices.ui.settings.SettingsScreen
import com.example.androidpractices.ui.settings.SettingsViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val appContainer = remember {
        (context.applicationContext as AndroidPracticesApplication).container
    }

    val hasActiveFilters by appContainer.filterCache.hasActiveFilters.collectAsState()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = bottomNavItems.any { it.route == currentRoute }

            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (screen == Screen.Settings && hasActiveFilters) {
                                            Badge { Text("·") }
                                        }
                                    }
                                ) {
                                    screen.icon?.let {
                                        Icon(imageVector = it, contentDescription = screen.title)
                                    }
                                }
                            },
                            label = { Text(screen.title) },
                            selected = isSelected,
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
                val launchesViewModel: LaunchesViewModel = viewModel(
                    factory = LaunchesViewModel.Factory(
                        appContainer.getLaunchesUseCase,
                        appContainer.launchRepository,
                        appContainer.settingsDataStore,
                        appContainer.filterCache
                    )
                )
                LaunchesScreen(
                    viewModel = launchesViewModel,
                    onLaunchClick = { launchId ->
                        navController.navigate(Screen.LaunchDetail.createRoute(launchId))
                    }
                )
            }
            composable(Screen.Favorites.route) {
                val favoritesViewModel: FavoritesViewModel = viewModel(
                    factory = FavoritesViewModel.Factory(appContainer.launchRepository)
                )
                FavoritesScreen(
                    viewModel = favoritesViewModel,
                    onLaunchClick = { launchId ->
                        navController.navigate(Screen.LaunchDetail.createRoute(launchId))
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Settings.route) {
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = SettingsViewModel.Factory(
                        appContainer.settingsDataStore,
                        appContainer.filterCache
                    )
                )
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onDone = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = Screen.LaunchDetail.route,
                arguments = listOf(navArgument("launchId") { type = NavType.StringType })
            ) { backStackEntry ->
                val launchId = backStackEntry.arguments?.getString("launchId") ?: ""
                val launchDetailViewModel: LaunchDetailViewModel = viewModel(
                    factory = LaunchDetailViewModel.Factory(
                        appContainer.getLaunchByIdUseCase,
                        appContainer.launchRepository
                    )
                )
                LaunchDetailScreen(
                    launchId = launchId,
                    viewModel = launchDetailViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
