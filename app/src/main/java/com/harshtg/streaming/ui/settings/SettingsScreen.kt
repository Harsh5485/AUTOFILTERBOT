package com.harshtg.streaming.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harshtg.streaming.data.repository.UserPreferencesRepository
import com.harshtg.streaming.ui.components.dpadFocusable
import com.harshtg.streaming.ui.theme.AppThemeMode
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    userPreferencesRepository: UserPreferencesRepository,
    onLogout: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val currentTheme by userPreferencesRepository.themeMode.collectAsState(initial = AppThemeMode.NIGHT)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Settings & Theme Engine",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ColorLens, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "App Theme Mode", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppThemeMode.values().forEach { theme ->
                        FilterChip(
                            selected = currentTheme == theme,
                            onClick = {
                                coroutineScope.launch {
                                    userPreferencesRepository.saveThemeMode(theme)
                                }
                            },
                            label = { Text(theme.name) },
                            modifier = Modifier.dpadFocusable(cornerRadius = 8.dp)
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Tv, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "TV Optimization Features", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• D-Pad Zero Touch Navigation Enabled (1.08x Scale + Glow Ring)\n• Sony Bravia Auto Frame Rate (AFR) & HDR Passthrough Active",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    userPreferencesRepository.clearAuth()
                    onLogout()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .dpadFocusable(cornerRadius = 12.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "DISCONNECT SERVER / CHANGE HOST")
        }
    }
}
