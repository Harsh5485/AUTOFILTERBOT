package com.harsh.streaming.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.streaming.model.*
import com.harsh.streaming.preferences.UserPreferencesManager
import com.harsh.streaming.repository.CatalogRepository
import com.harsh.streaming.repository.UserDataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DetailsUiState(
    val isLoading: Boolean = true,
    val mediaDetail: MediaDetail? = null,
    val selectedSeason: Int = 1,
    val episodes: List<EpisodeInfo> = emptyList(),
    val isWatchLater: Boolean = false,
    val availableProviders: List<StreamProvider> = emptyList(),
    val selectedProvider: StreamProvider? = null,
    val savedPositionMs: Long = 0,
    val errorMessage: String? = null
)

class DetailsViewModel(
    private val mediaId: Int,
    private val mediaType: MediaType,
    private val catalogRepository: CatalogRepository,
    private val userDataRepository: UserDataRepository,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        loadDetails()
        observeWatchLater()
        loadSavedPosition()
        loadProviders()
    }

    fun loadDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = if (mediaType == MediaType.MOVIE) {
                catalogRepository.getMovieDetails(mediaId)
            } else {
                catalogRepository.getTvDetails(mediaId)
            }

            result.fold(
                onSuccess = { detail ->
                    _uiState.update { it.copy(isLoading = false, mediaDetail = detail) }
                    if (mediaType == MediaType.TV && detail.seasons.isNotEmpty()) {
                        val firstSeason = detail.seasons.firstOrNull { it.seasonNumber > 0 }?.seasonNumber ?: 1
                        selectSeason(firstSeason)
                    }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = err.message ?: "Failed to load details") }
                }
            )
        }
    }

    fun selectSeason(seasonNumber: Int) {
        _uiState.update { it.copy(selectedSeason = seasonNumber) }
        viewModelScope.launch {
            catalogRepository.getSeasonEpisodes(mediaId, seasonNumber).fold(
                onSuccess = { epList ->
                    _uiState.update { it.copy(episodes = epList) }
                },
                onFailure = {
                    _uiState.update { it.copy(episodes = emptyList()) }
                }
            )
        }
    }

    fun selectProvider(provider: StreamProvider) {
        _uiState.update { it.copy(selectedProvider = provider) }
    }

    fun toggleWatchLater() {
        val detail = uiState.value.mediaDetail ?: return
        viewModelScope.launch {
            userDataRepository.toggleWatchLater(
                mediaId = detail.id,
                title = detail.title,
                posterPath = detail.posterPath,
                mediaType = mediaType.name,
                voteAverage = detail.voteAverage,
                currentlySaved = uiState.value.isWatchLater
            )
        }
    }

    private fun observeWatchLater() {
        viewModelScope.launch {
            userDataRepository.isWatchLater(mediaId).collect { isSaved ->
                _uiState.update { it.copy(isWatchLater = isSaved) }
            }
        }
    }

    private fun loadSavedPosition() {
        viewModelScope.launch {
            val item = userDataRepository.getHistoryItem(mediaId)
            if (item != null) {
                _uiState.update { it.copy(savedPositionMs = item.positionMs) }
            }
        }
    }

    private fun loadProviders() {
        val providers = catalogRepository.getAvailableProviders()
        viewModelScope.launch {
            preferencesManager.preferredProviderId.collect { preferredId ->
                val selected = providers.find { it.id == preferredId } ?: providers.first()
                _uiState.update {
                    it.copy(
                        availableProviders = providers,
                        selectedProvider = selected
                    )
                }
            }
        }
    }
}
