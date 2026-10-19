package com.harsh.streaming.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.harsh.streaming.model.MediaType
import com.harsh.streaming.model.StreamProvider
import com.harsh.streaming.ui.viewmodel.DetailsViewModel

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    onPlayMedia: (Int, MediaType, Int, Int, String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val detail = state.mediaDetail

    if (state.isLoading || detail == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            // Backdrop Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = detail.fullBackdropUrl,
                    contentDescription = detail.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                            )
                        )
                )
            }
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = detail.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", detail.voteAverage),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    detail.releaseDate?.let { date ->
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = date.take(4), color = Color.Gray, fontSize = 14.sp)
                    }
                    if (detail.mediaType == MediaType.MOVIE && detail.runtime != null) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "${detail.runtime} mins", color = Color.Gray, fontSize = 14.sp)
                    }
                }

                detail.overview?.let { overview ->
                    Text(
                        text = overview,
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Play Button & Watch Later Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val providerId = state.selectedProvider?.id ?: "vidsrc_icu"
                            onPlayMedia(detail.id, detail.mediaType, 1, 1, providerId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Play", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { viewModel.toggleWatchLater() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = if (state.isWatchLater) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (state.isWatchLater) MaterialTheme.colorScheme.primary else Color.White
                        )
                    }
                }

                // Provider Selector Row
                Text("Select Streaming Server:", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.availableProviders) { provider ->
                        val isSelected = provider.id == state.selectedProvider?.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectProvider(provider) },
                            label = { Text(provider.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }
            }
        }

        // TV Show Season & Episode Picker
        if (detail.mediaType == MediaType.TV && detail.seasons.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Seasons", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                    LazyRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(detail.seasons.filter { it.seasonNumber > 0 }) { season ->
                            val isSelected = season.seasonNumber == state.selectedSeason
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectSeason(season.seasonNumber) },
                                label = { Text(season.name) }
                            )
                        }
                    }

                    Text("Episodes", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(top = 12.dp))
                }
            }

            items(state.episodes) { ep ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable {
                            val providerId = state.selectedProvider?.id ?: "vidsrc_icu"
                            onPlayMedia(detail.id, detail.mediaType, ep.seasonNumber, ep.episodeNumber, providerId)
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "E${ep.episodeNumber}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = ep.name, fontWeight = FontWeight.SemiBold, color = Color.White, fontSize = 14.sp)
                            ep.overview?.let {
                                Text(text = it, color = Color.Gray, fontSize = 12.sp, maxLines = 2)
                            }
                        }
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
