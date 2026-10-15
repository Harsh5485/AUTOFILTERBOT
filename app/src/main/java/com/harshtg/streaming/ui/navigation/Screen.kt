package com.harshtg.streaming.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Login : Screen("login", "Login", Icons.Default.Dns)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Catalogs : Screen("catalogs", "Catalogs", Icons.Default.Movie)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Dashboard : Screen("dashboard", "Server", Icons.Default.Dns)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object Detail : Screen("detail/{type}/{id}", "Detail", Icons.Default.Movie) {
        fun createRoute(type: String, id: String) = "detail/$type/$id"
    }
}
