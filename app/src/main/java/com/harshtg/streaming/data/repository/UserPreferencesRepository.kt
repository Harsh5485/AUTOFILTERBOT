package com.harshtg.streaming.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.harshtg.streaming.ui.theme.AppThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

data class ServerAuthCredentials(
    val hostUrl: String = "",
    val username: String = "",
    val password: String = ""
) {
    val isLoggedIn: Boolean
        get() = hostUrl.isNotBlank()
}

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val HOST_URL = stringPreferencesKey("host_url")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val serverCredentials: Flow<ServerAuthCredentials> = context.dataStore.data.map { prefs ->
        ServerAuthCredentials(
            hostUrl = prefs[Keys.HOST_URL] ?: "",
            username = prefs[Keys.USERNAME] ?: "",
            password = prefs[Keys.PASSWORD] ?: ""
        )
    }

    val themeMode: Flow<AppThemeMode> = context.dataStore.data.map { prefs ->
        val savedTheme = prefs[Keys.THEME_MODE] ?: AppThemeMode.NIGHT.name
        try {
            AppThemeMode.valueOf(savedTheme)
        } catch (e: Exception) {
            AppThemeMode.NIGHT
        }
    }

    suspend fun saveServerCredentials(hostUrl: String, username: String, password: String) {
        val formattedHost = if (!hostUrl.startsWith("http://") && !hostUrl.startsWith("https://")) {
            "http://$hostUrl"
        } else {
            hostUrl
        }.trimEnd('/')

        context.dataStore.edit { prefs ->
            prefs[Keys.HOST_URL] = formattedHost
            prefs[Keys.USERNAME] = username.trim()
            prefs[Keys.PASSWORD] = password.trim()
        }
    }

    suspend fun saveThemeMode(themeMode: AppThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun clearAuth() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.HOST_URL)
            prefs.remove(Keys.USERNAME)
            prefs.remove(Keys.PASSWORD)
        }
    }
}
