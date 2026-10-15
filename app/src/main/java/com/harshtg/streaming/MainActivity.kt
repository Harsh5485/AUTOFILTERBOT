package com.harshtg.streaming

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.harshtg.streaming.data.repository.UserPreferencesRepository
import com.harshtg.streaming.player.HarshPlayerActivity
import com.harshtg.streaming.ui.auth.LoginScreen
import com.harshtg.streaming.ui.auth.LoginViewModel
import com.harshtg.streaming.ui.components.dpadFocusable
import com.harshtg.streaming.ui.dashboard.ServerDashboardScreen
import com.harshtg.streaming.ui.detail.DetailScreen
import com.harshtg.streaming.ui.home.HomeScreen
import com.harshtg.streaming.ui.home.HomeViewModel
import com.harshtg.streaming.ui.navigation.Screen
import com.harshtg.streaming.ui.settings.SettingsScreen
import com.harshtg.streaming.ui.theme.AppThemeMode
import com.harshtg.streaming.ui.theme.HarshTGTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeMode by userPreferencesRepository.themeMode.collectAsState(initial = AppThemeMode.NIGHT)
            val credentials by userPreferencesRepository.serverCredentials.collectAsState(initial = null)

            HarshTGTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val startDestination = if (credentials?.isLoggedIn == true) Screen.Home.route else Screen.Login.route

                    Scaffold(
                        bottomBar = {
                            if (currentRoute != Screen.Login.route && currentRoute?.startsWith("detail") != true) {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ) {
                                    NavigationBarItem(
                                        selected = currentRoute == Screen.Home.route,
                                        onClick = { navController.navigate(Screen.Home.route) },
                                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                        label = { Text("Home") },
                                        modifier = Modifier.dpadFocusable(cornerRadius = 8.dp)
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == Screen.Dashboard.route,
                                        onClick = { navController.navigate(Screen.Dashboard.route) },
                                        icon = { Icon(Icons.Default.Dns, contentDescription = "Server") },
                                        label = { Text("Server") },
                                        modifier = Modifier.dpadFocusable(cornerRadius = 8.dp)
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == Screen.Settings.route,
                                        onClick = { navController.navigate(Screen.Settings.route) },
                                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                        label = { Text("Settings") },
                                        modifier = Modifier.dpadFocusable(cornerRadius = 8.dp)
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Login.route) {
                                val loginViewModel: LoginViewModel = hiltViewModel()
                                LoginScreen(
                                    viewModel = loginViewModel,
                                    onLoginSuccess = {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable(Screen.Home.route) {
                                val homeViewModel: HomeViewModel = hiltViewModel()
                                HomeScreen(
                                    viewModel = homeViewModel,
                                    onItemClick = { item ->
                                        navController.navigate(Screen.Detail.createRoute(item.type, item.id))
                                    },
                                    onPlayClick = { item ->
                                        launchPlayer("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", item.name)
                                    }
                                )
                            }

                            composable(Screen.Dashboard.route) {
                                ServerDashboardScreen()
                            }

                            composable(Screen.Settings.route) {
                                SettingsScreen(
                                    userPreferencesRepository = userPreferencesRepository,
                                    onLogout = {
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable(Screen.Detail.route) { backStackEntry ->
                                val type = backStackEntry.arguments?.getString("type") ?: "movie"
                                val id = backStackEntry.arguments?.getString("id") ?: ""
                                DetailScreen(
                                    type = type,
                                    id = id,
                                    onPlayClick = { streamUrl ->
                                        launchPlayer(streamUrl, "Harsh TG VOD Stream")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun launchPlayer(streamUrl: String, title: String) {
        val intent = Intent(this, HarshPlayerActivity::class.java).apply {
            putExtra("STREAM_URL", streamUrl)
            putExtra("STREAM_TITLE", title)
        }
        startActivity(intent)
    }
}
