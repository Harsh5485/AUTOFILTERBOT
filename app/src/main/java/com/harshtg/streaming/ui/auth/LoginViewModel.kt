package com.harshtg.streaming.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshtg.streaming.data.repository.ServerAuthCredentials
import com.harshtg.streaming.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val credentials = preferencesRepository.serverCredentials

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(hostUrl: String, username: String, password: String) {
        if (hostUrl.isBlank()) {
            _uiState.value = LoginUiState.Error("Host Name / Server URL is required!")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                preferencesRepository.saveServerCredentials(
                    hostUrl = hostUrl,
                    username = username,
                    password = password
                )
                _uiState.value = LoginUiState.Success
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Failed to save credentials: ${e.localizedMessage}")
            }
        }
    }
}
