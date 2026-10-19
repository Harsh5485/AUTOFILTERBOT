package com.harsh.streaming.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.harsh.streaming.model.MediaItem
import com.harsh.streaming.model.MediaType
import com.harsh.streaming.ui.components.MediaPosterCard
import com.harsh.streaming.ui.components.SectionHeader
import com.harsh.streaming.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMediaClick: (Int, MediaType) -> Unit,
    onSeeAllClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Featured Cinematic Banner
        state.featuredItem?.let { featured ->
            item {
                FeaturedBanner(
                    item = featured,
                    onPlayClick = { onMediaClick(featured.id, featured.mediaType) }
                )
            }
        }

        // Continue Watching Section
        if (state.continueWatching.isNotEmpty()) {
            item {
                SectionHeader(title = "Continue Watching")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.continueWatching) { history ->
                        Card(
                            modifier = Modifier
                                .width(160.dp)
                                .height(110.dp)
                                .clickable {
                                    val type = if (history.mediaType == "TV") MediaType.TV else MediaType.MOVIE
                                    onMediaClick(history.mediaId, type)
                                },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = if (!history.posterPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500${history.posterPath}" else "",
                                    contentDescription = history.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                            )
                                        )
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = history.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    val progress = if (history.durationMs > 0) history.positionMs.toFloat() / history.durationMs.toFloat() else 0f
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Trending Row
        item {
            SectionHeader(title = "🔥 Trending Now", onSeeAllClick = { onSeeAllClick("Trending") })
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.trendingList) { media ->
                    MediaPosterCard(
                        item = media,
                        onClick = { onMediaClick(media.id, media.mediaType) }
                    )
                }
            }
        }

        // Hindi Dubbed Row
        item {
            SectionHeader(title = "🎬 Hindi Dubbed Specials", onSeeAllClick = { onSeeAllClick("Hindi Dubbed") })
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.hindiDubbed) { media ->
                    MediaPosterCard(
                        item = media,
                        onClick = { onMediaClick(media.id, media.mediaType) }
                    )
                }
            }
        }

        // Popular Movies Row
        item {
            SectionHeader(title = "🎥 Blockbuster Movies", onSeeAllClick = { onSeeAllClick("Movies") })
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.popularMovies) { media ->
                    MediaPosterCard(
                        item = media,
                        onClick = { onMediaClick(media.id, media.mediaType) }
                    )
                }
            }
        }

        // Popular TV Shows Row
        item {
            SectionHeader(title = "📺 Popular TV Shows", onSeeAllClick = { onSeeAllClick("TV Shows") })
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.popularTvShows) { media ->
                    MediaPosterCard(
                        item = media,
                        onClick = { onMediaClick(media.id, media.mediaType) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FeaturedBanner(
    item: MediaItem,
    onPlayClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
    ) {
        AsyncImage(
            model = item.fullBackdropUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = item.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            item.overview?.let { overview ->
                Text(
                    text = overview,
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
                )
            }
            Button(
                onClick = onPlayClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Watch Now", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
