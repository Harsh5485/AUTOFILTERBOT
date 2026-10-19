package com.harsh.streaming.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "harsh_streaming_settings")

class UserPreferencesManager(private val context: Context) {

    private val PREFERRED_PROVIDER_KEY = stringPreferencesKey("preferred_provider")
    private val APP_THEME_KEY = stringPreferencesKey("app_theme")

    val preferredProviderId: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PREFERRED_PROVIDER_KEY] ?: "vidsrc_icu"
    }

    val appTheme: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[APP_THEME_KEY] ?: "AMBER"
    }

    suspend fun setPreferredProvider(providerId: String) {
        context.dataStore.edit { prefs ->
            prefs[PREFERRED_PROVIDER_KEY] = providerId
        }
    }

    suspend fun setAppTheme(themeName: String) {
        context.dataStore.edit { prefs ->
            prefs[APP_THEME_KEY] = themeName
        }
    }
}
