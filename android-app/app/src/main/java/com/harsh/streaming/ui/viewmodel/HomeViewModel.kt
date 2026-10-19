package com.harsh.streaming.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.streaming.database.WatchHistoryEntity
import com.harsh.streaming.model.MediaItem
import com.harsh.streaming.repository.CatalogRepository
import com.harsh.streaming.repository.UserDataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val featuredItem: MediaItem? = null,
    val trendingList: List<MediaItem> = emptyList(),
    val popularMovies: List<MediaItem> = emptyList(),
    val popularTvShows: List<MediaItem> = emptyList(),
    val hindiDubbed: List<MediaItem> = emptyList(),
    val continueWatching: List<WatchHistoryEntity> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(
    private val catalogRepository: CatalogRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
        observeContinueWatching()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val trendingResult = catalogRepository.getTrending()
            val moviesResult = catalogRepository.getPopularMovies()
            val tvResult = catalogRepository.getPopularTvShows()
            val hindiResult = catalogRepository.getHindiDubbedContent()

            val trending = trendingResult.getOrDefault(emptyList())
            val movies = moviesResult.getOrDefault(emptyList())
            val tvShows = tvResult.getOrDefault(emptyList())
            val hindi = hindiResult.getOrDefault(emptyList())

            val error = if (trendingResult.isFailure && moviesResult.isFailure) {
                "Unable to load content. Please check network connection."
            } else null

            _uiState.update {
                it.copy(
                    isLoading = false,
                    featuredItem = trending.firstOrNull(),
                    trendingList = trending,
                    popularMovies = movies,
                    popularTvShows = tvShows,
                    hindiDubbed = hindi,
                    errorMessage = error
                )
            }
        }
    }

    private fun observeContinueWatching() {
        viewModelScope.launch {
            userDataRepository.continueWatching.collect { list ->
                _uiState.update { it.copy(continueWatching = list) }
            }
        }
    }
}
