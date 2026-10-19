package com.harsh.streaming.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.streaming.database.WatchHistoryEntity
import com.harsh.streaming.database.WatchLaterEntity
import com.harsh.streaming.model.MediaItem
import com.harsh.streaming.model.MediaType
import com.harsh.streaming.preferences.UserPreferencesManager
import com.harsh.streaming.repository.CatalogRepository
import com.harsh.streaming.repository.UserDataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CategoryUiState(
    val title: String = "",
    val isLoading: Boolean = true,
    val items: List<MediaItem> = emptyList(),
    val errorMessage: String? = null
)

class CategoryViewModel(
    private val categoryType: String,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState(title = categoryType))
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategoryContent()
    }

    fun loadCategoryContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = when (categoryType.lowercase()) {
                "movies" -> catalogRepository.getPopularMovies()
                "tv shows", "tvshows" -> catalogRepository.getPopularTvShows()
                "hindi dubbed", "hindidubbed" -> catalogRepository.getHindiDubbedContent()
                else -> catalogRepository.getTrending()
            }

            result.fold(
                onSuccess = { list ->
                    _uiState.update { it.copy(isLoading = false, items = list) }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = err.message ?: "Failed to load category") }
                }
            )
        }
    }
}

data class LibraryUiState(
    val watchLaterList: List<WatchLaterEntity> = emptyList(),
    val historyList: List<WatchHistoryEntity> = emptyList()
)

class LibraryViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    val uiState: StateFlow<LibraryUiState> = combine(
        userDataRepository.watchLaterList,
        userDataRepository.allHistory
    ) { watchLater, history ->
        LibraryUiState(watchLaterList = watchLater, historyList = history)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LibraryUiState())

    fun clearHistory() {
        viewModelScope.launch {
            userDataRepository.clearHistory()
        }
    }
}

data class SettingsUiState(
    val preferredProviderId: String = "vidsrc_icu",
    val theme: String = "AMBER"
)

class SettingsViewModel(
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        preferencesManager.preferredProviderId,
        preferencesManager.appTheme
    ) { provider, theme ->
        SettingsUiState(preferredProviderId = provider, theme = theme)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setPreferredProvider(providerId: String) {
        viewModelScope.launch {
            preferencesManager.setPreferredProvider(providerId)
        }
    }

    fun setAppTheme(theme: String) {
        viewModelScope.launch {
            preferencesManager.setAppTheme(theme)
        }
    }
}
