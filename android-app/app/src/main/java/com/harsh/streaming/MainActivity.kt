package com.harsh.streaming

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.room.Room
import com.harsh.streaming.database.AppDatabase
import com.harsh.streaming.model.MediaType
import com.harsh.streaming.network.TmdbApiService
import com.harsh.streaming.preferences.UserPreferencesManager
import com.harsh.streaming.repository.CatalogRepository
import com.harsh.streaming.repository.UserDataRepository
import com.harsh.streaming.ui.player.WebEmbedPlayerView
import com.harsh.streaming.ui.screens.*
import com.harsh.streaming.ui.theme.HarshStreamingTheme
import com.harsh.streaming.ui.tv.TvHomeScreen
import com.harsh.streaming.ui.viewmodel.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var catalogRepository: CatalogRepository
    private lateinit var userDataRepository: UserDataRepository
    private lateinit var preferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Data Layer & Repositories
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(TmdbApiService::class.java)
        catalogRepository = CatalogRepository(apiService)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "harsh_streaming.db"
        ).build()

        userDataRepository = UserDataRepository(db.watchHistoryDao(), db.watchLaterDao())
        preferencesManager = UserPreferencesManager(applicationContext)

        val isTv = isTvDevice()

        setContent {
            HarshStreamingTheme {
                val navController = rememberNavController()

                if (isTv) {
                    // Android TV Flow
                    NavHost(navController = navController, startDestination = "tv_home") {
                        composable("tv_home") {
                            val vm: HomeViewModel = viewModel(factory = createViewModelFactory {
                                HomeViewModel(catalogRepository, userDataRepository)
                            })
                            TvHomeScreen(
                                viewModel = vm,
                                onMediaSelect = { id, type ->
                                    navController.navigate("details/$id/${type.name}")
                                },
                                onNavigateCategory = { route ->
                                    when (route) {
                                        "Search" -> navController.navigate("search")
                                        "Settings" -> navController.navigate("settings")
                                        else -> navController.navigate("category/$route")
                                    }
                                }
                            )
                        }

                        composable(
                            route = "details/{id}/{type}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.IntType },
                                navArgument("type") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: 0
                            val typeStr = backStackEntry.arguments?.getString("type") ?: "MOVIE"
                            val type = if (typeStr == "TV") MediaType.TV else MediaType.MOVIE

                            val vm: DetailsViewModel = viewModel(
                                viewModelStoreOwner = backStackEntry,
                                key = "details_$id",
                                factory = createViewModelFactory {
                                    DetailsViewModel(id, type, catalogRepository, userDataRepository, preferencesManager)
                                }
                            )

                            DetailsScreen(
                                viewModel = vm,
                                onPlayMedia = { mediaId, mediaType, season, episode, providerId ->
                                    navController.navigate("player/$mediaId/${mediaType.name}/$season/$episode/$providerId")
                                }
                            )
                        }

                        composable("category/{type}") { backStackEntry ->
                            val categoryType = backStackEntry.arguments?.getString("type") ?: "Movies"
                            val vm: CategoryViewModel = viewModel(
                                viewModelStoreOwner = backStackEntry,
                                key = "cat_$categoryType",
                                factory = createViewModelFactory {
                                    CategoryViewModel(categoryType, catalogRepository)
                                }
                            )
                            CategoryScreen(
                                viewModel = vm,
                                onMediaClick = { id, type ->
                                    navController.navigate("details/$id/${type.name}")
                                }
                            )
                        }

                        composable("search") {
                            val vm: SearchViewModel = viewModel(factory = createViewModelFactory {
                                SearchViewModel(catalogRepository)
                            })
                            SearchScreen(
                                viewModel = vm,
                                onMediaClick = { id, type ->
                                    navController.navigate("details/$id/${type.name}")
                                }
                            )
                        }

                        composable("settings") {
                            val vm: SettingsViewModel = viewModel(factory = createViewModelFactory {
                                SettingsViewModel(preferencesManager)
                            })
                            SettingsScreen(viewModel = vm)
                        }

                        composable(
                            route = "player/{id}/{type}/{season}/{episode}/{providerId}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.IntType },
                                navArgument("type") { type = NavType.StringType },
                                navArgument("season") { type = NavType.IntType },
                                navArgument("episode") { type = NavType.IntType },
                                navArgument("providerId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: 0
                            val typeStr = backStackEntry.arguments?.getString("type") ?: "MOVIE"
                            val mediaType = if (typeStr == "TV") MediaType.TV else MediaType.MOVIE
                            val season = backStackEntry.arguments?.getInt("season") ?: 1
                            val episode = backStackEntry.arguments?.getInt("episode") ?: 1
                            val providerId = backStackEntry.arguments?.getString("providerId") ?: "vidsrc_icu"

                            val provider = catalogRepository.getAvailableProviders().find { it.id == providerId }
                                ?: catalogRepository.getAvailableProviders().first()

                            val embedUrl = provider.getEmbedUrl(mediaType, id, season, episode)

                            WebEmbedPlayerView(
                                embedUrl = embedUrl,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                } else {
                    // Android Phone / Tablet App Navigation
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            if (currentRoute in listOf("home", "search", "library", "settings")) {
                                BottomNavigationBar(
                                    currentRoute = currentRoute,
                                    onNavigate = { route ->
                                        navController.navigate(route) {
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
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") {
                                val vm: HomeViewModel = viewModel(factory = createViewModelFactory {
                                    HomeViewModel(catalogRepository, userDataRepository)
                                })
                                HomeScreen(
                                    viewModel = vm,
                                    onMediaClick = { id, type -> navController.navigate("details/$id/${type.name}") },
                                    onSeeAllClick = { cat -> navController.navigate("category/$cat") }
                                )
                            }

                            composable("search") {
                                val vm: SearchViewModel = viewModel(factory = createViewModelFactory {
                                    SearchViewModel(catalogRepository)
                                })
                                SearchScreen(
                                    viewModel = vm,
                                    onMediaClick = { id, type -> navController.navigate("details/$id/${type.name}") }
                                )
                            }

                            composable("library") {
                                val vm: LibraryViewModel = viewModel(factory = createViewModelFactory {
                                    LibraryViewModel(userDataRepository)
                                })
                                LibraryScreen(
                                    viewModel = vm,
                                    onMediaClick = { id, type -> navController.navigate("details/$id/${type.name}") }
                                )
                            }

                            composable("settings") {
                                val vm: SettingsViewModel = viewModel(factory = createViewModelFactory {
                                    SettingsViewModel(preferencesManager)
                                })
                                SettingsScreen(viewModel = vm)
                            }

                            composable(
                                route = "details/{id}/{type}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.IntType },
                                    navArgument("type") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getInt("id") ?: 0
                                val typeStr = backStackEntry.arguments?.getString("type") ?: "MOVIE"
                                val type = if (typeStr == "TV") MediaType.TV else MediaType.MOVIE

                                val vm: DetailsViewModel = viewModel(
                                    viewModelStoreOwner = backStackEntry,
                                    key = "details_$id",
                                    factory = createViewModelFactory {
                                        DetailsViewModel(id, type, catalogRepository, userDataRepository, preferencesManager)
                                    }
                                )

                                DetailsScreen(
                                    viewModel = vm,
                                    onPlayMedia = { mediaId, mediaType, season, episode, providerId ->
                                        navController.navigate("player/$mediaId/${mediaType.name}/$season/$episode/$providerId")
                                    }
                                )
                            }

                            composable("category/{type}") { backStackEntry ->
                                val categoryType = backStackEntry.arguments?.getString("type") ?: "Movies"
                                val vm: CategoryViewModel = viewModel(
                                    viewModelStoreOwner = backStackEntry,
                                    key = "cat_$categoryType",
                                    factory = createViewModelFactory {
                                        CategoryViewModel(categoryType, catalogRepository)
                                    }
                                )
                                CategoryScreen(
                                    viewModel = vm,
                                    onMediaClick = { id, type -> navController.navigate("details/$id/${type.name}") }
                                )
                            }

                            composable(
                                route = "player/{id}/{type}/{season}/{episode}/{providerId}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.IntType },
                                    navArgument("type") { type = NavType.StringType },
                                    navArgument("season") { type = NavType.IntType },
                                    navArgument("episode") { type = NavType.IntType },
                                    navArgument("providerId") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getInt("id") ?: 0
                                val typeStr = backStackEntry.arguments?.getString("type") ?: "MOVIE"
                                val mediaType = if (typeStr == "TV") MediaType.TV else MediaType.MOVIE
                                val season = backStackEntry.arguments?.getInt("season") ?: 1
                                val episode = backStackEntry.arguments?.getInt("episode") ?: 1
                                val providerId = backStackEntry.arguments?.getString("providerId") ?: "vidsrc_icu"

                                val provider = catalogRepository.getAvailableProviders().find { it.id == providerId }
                                    ?: catalogRepository.getAvailableProviders().first()

                                val embedUrl = provider.getEmbedUrl(mediaType, id, season, episode)

                                WebEmbedPlayerView(
                                    embedUrl = embedUrl,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isTvDevice(): Boolean {
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        return uiModeManager?.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T : ViewModel> createViewModelFactory(crossinline creator: () -> T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                return creator() as VM
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home),
        BottomNavItem("search", "Search", Icons.Default.Search),
        BottomNavItem("library", "Library", Icons.Default.VideoLibrary),
        BottomNavItem("settings", "Settings", Icons.Default.Settings)
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

private data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)
