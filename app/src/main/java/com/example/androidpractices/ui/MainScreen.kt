package com.example.androidpractices.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.androidpractices.data.remote.RetrofitClient
import com.example.androidpractices.data.repository.LaunchRepositoryImpl
import com.example.androidpractices.domain.usecase.GetLaunchByIdUseCase
import com.example.androidpractices.domain.usecase.GetLaunchesUseCase
import com.example.androidpractices.navigation.Screen
import com.example.androidpractices.navigation.bottomNavItems
import com.example.androidpractices.ui.detail.LaunchDetailViewModel
import com.example.androidpractices.ui.launches.LaunchesViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val repository = remember { LaunchRepositoryImpl(RetrofitClient.api) }
    val getLaunchesUseCase = remember { GetLaunchesUseCase(repository) }
    val getLaunchByIdUseCase = remember { GetLaunchByIdUseCase(repository) }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

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
                val launchesViewModel: LaunchesViewModel = viewModel(
                    factory = LaunchesViewModel.Factory(getLaunchesUseCase)
                )
                LaunchesScreen(
                    viewModel = launchesViewModel,
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
                val launchDetailViewModel: LaunchDetailViewModel = viewModel(
                    factory = LaunchDetailViewModel.Factory(getLaunchByIdUseCase)
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
