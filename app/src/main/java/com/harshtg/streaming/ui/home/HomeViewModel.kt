package com.harshtg.streaming.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshtg.streaming.data.model.StremioMetaItem
import com.harshtg.streaming.data.remote.StremioApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CatalogSection(
    val title: String,
    val items: List<StremioMetaItem>
)

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val spotlightItem: StremioMetaItem?,
        val sections: List<CatalogSection>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: StremioApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val fallbackSpotlight = StremioMetaItem(
                id = "tt1375666",
                type = "movie",
                name = "Inception",
                poster = "https://image.tmdb.org/t/p/w500/oYuLE131121v12v2a.jpg",
                background = "https://image.tmdb.org/t/p/original/8ZTVqvKDQ8A.jpg",
                logo = null,
                description = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                releaseInfo = "2010",
                imdbRating = "8.8",
                genres = listOf("Action", "Sci-Fi", "Thriller"),
                videos = null
            )

            val fallbackSections = listOf(
                CatalogSection("Latest & Popular", listOf(
                    fallbackSpotlight,
                    StremioMetaItem("tt0816692", "movie", "Interstellar", "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI61CU6M84v3.jpg", null, null, "Space exploration", "2014", "8.7", listOf("Sci-Fi"), null),
                    StremioMetaItem("tt0468569", "movie", "The Dark Knight", "https://image.tmdb.org/t/p/w500/qJ2tW6WM232BD1222.jpg", null, null, "Batman vs Joker", "2008", "9.0", listOf("Action"), null)
                )),
                CatalogSection("Bollywood Hits", listOf(
                    StremioMetaItem("tt9389998", "movie", "Pathaan", "https://image.tmdb.org/t/p/w500/aK7M7556.jpg", null, null, "Spy thriller", "2023", "6.0", listOf("Action"), null),
                    StremioMetaItem("tt12844910", "movie", "Jawan", "https://image.tmdb.org/t/p/w500/jawan.jpg", null, null, "Action drama", "2023", "7.1", listOf("Action"), null)
                )),
                CatalogSection("Anime & K-Drama", listOf(
                    StremioMetaItem("tt9838080", "series", "Demon Slayer", "https://image.tmdb.org/t/p/w500/demon.jpg", null, null, "Anime", "2019", "8.7", listOf("Anime"), null),
                    StremioMetaItem("tt13138834", "series", "Squid Game", "https://image.tmdb.org/t/p/w500/squid.jpg", null, null, "K-Drama", "2021", "8.0", listOf("Drama"), null)
                ))
            )

            try {
                val movieResponse = apiService.getCatalog("movie", "top")
                if (movieResponse.isSuccessful && movieResponse.body()?.metas?.isNotEmpty() == true) {
                    val fetchedMetas = movieResponse.body()!!.metas
                    _uiState.value = HomeUiState.Success(
                        spotlightItem = fetchedMetas.firstOrNull(),
                        sections = listOf(CatalogSection("Server Catalog", fetchedMetas))
                    )
                } else {
                    _uiState.value = HomeUiState.Success(
                        spotlightItem = fallbackSpotlight,
                        sections = fallbackSections
                    )
                }
            } catch (e: Exception) {
                // Return fallback data on connection error so user can browse UI smoothly
                _uiState.value = HomeUiState.Success(
                    spotlightItem = fallbackSpotlight,
                    sections = fallbackSections
                )
            }
        }
    }
}
