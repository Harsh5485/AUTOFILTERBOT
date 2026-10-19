package com.harsh.streaming.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.streaming.model.MediaItem
import com.harsh.streaming.model.MediaType
import com.harsh.streaming.ui.components.MediaPosterCard
import com.harsh.streaming.ui.components.SectionHeader
import com.harsh.streaming.ui.viewmodel.LibraryViewModel
import com.harsh.streaming.ui.viewmodel.SettingsViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onMediaClick: (Int, MediaType) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Text(
                text = "My Library",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            SectionHeader(title = "Watch Later List")
            if (state.watchLaterList.isEmpty()) {
                Text(
                    text = "No saved movies or TV shows.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.watchLaterList) { item ->
                        val media = MediaItem(
                            id = item.mediaId,
                            title = item.title,
                            overview = null,
                            posterPath = item.posterPath,
                            backdropPath = null,
                            mediaType = if (item.mediaType == "TV") MediaType.TV else MediaType.MOVIE,
                            voteAverage = item.voteAverage,
                            releaseDate = null
                        )
                        MediaPosterCard(item = media, onClick = { onMediaClick(media.id, media.mediaType) })
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Watch History", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                if (state.historyList.isNotEmpty()) {
                    TextButton(onClick = { viewModel.clearHistory() }) {
                        Text("Clear History", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            if (state.historyList.isEmpty()) {
                Text(
                    text = "No watch history available.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.historyList) { item ->
                        val media = MediaItem(
                            id = item.mediaId,
                            title = item.title,
                            overview = null,
                            posterPath = item.posterPath,
                            backdropPath = null,
                            mediaType = if (item.mediaType == "TV") MediaType.TV else MediaType.MOVIE,
                            voteAverage = 0.0,
                            releaseDate = null
                        )
                        MediaPosterCard(item = media, onClick = { onMediaClick(media.id, media.mediaType) })
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))

        Text("Default Stream Server", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "vidsrc_icu" to "Server 1 (Primary 4K Fast)",
            "vidsrc_me" to "Server 2 (Pro Multi-Sub)",
            "vidsrc_cc" to "Server 3 (Ultra HD)",
            "vidsrc_xyz" to "Server 4 (Backup 1)",
            "vidlink" to "Server 5 (Backup 2)"
        ).forEach { (id, name) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = state.preferredProviderId == id,
                    onClick = { viewModel.setPreferredProvider(id) },
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                )
                Text(text = name, color = Color.White, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.DarkGray)

        Text("Harsh Streaming v1.0.0", color = Color.Gray, fontSize = 12.sp)
        Text("Privacy-first streaming engine powered by high-speed edge providers.", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
    }
}
