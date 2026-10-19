package com.harsh.streaming.ui.tv

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.harsh.streaming.model.MediaItem
import com.harsh.streaming.model.MediaType
import com.harsh.streaming.ui.viewmodel.HomeViewModel

@Composable
fun TvHomeScreen(
    viewModel: HomeViewModel,
    onMediaSelect: (Int, MediaType) -> Unit,
    onNavigateCategory: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedNavIndex by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TV Navigation Sidebar
        TvSidebar(
            selectedIndex = selectedNavIndex,
            onSelect = { idx, route ->
                selectedNavIndex = idx
                if (route != "Home") {
                    onNavigateCategory(route)
                }
            }
        )

        // Main 10-Foot Content Area
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // TV Hero Featured Banner
                state.featuredItem?.let { featured ->
                    item {
                        TvHeroBanner(
                            item = featured,
                            onPlay = { onMediaSelect(featured.id, featured.mediaType) }
                        )
                    }
                }

                // Trending Row
                if (state.trendingList.isNotEmpty()) {
                    item {
                        TvContentRow(
                            title = "🔥 Trending Today",
                            items = state.trendingList,
                            onMediaSelect = onMediaSelect
                        )
                    }
                }

                // Hindi Dubbed Row
                if (state.hindiDubbed.isNotEmpty()) {
                    item {
                        TvContentRow(
                            title = "🎬 Hindi Dubbed Cinema",
                            items = state.hindiDubbed,
                            onMediaSelect = onMediaSelect
                        )
                    }
                }

                // Popular Movies Row
                if (state.popularMovies.isNotEmpty()) {
                    item {
                        TvContentRow(
                            title = "🎥 Popular Movies",
                            items = state.popularMovies,
                            onMediaSelect = onMediaSelect
                        )
                    }
                }

                // Popular TV Shows Row
                if (state.popularTvShows.isNotEmpty()) {
                    item {
                        TvContentRow(
                            title = "📺 Popular TV Shows",
                            items = state.popularTvShows,
                            onMediaSelect = onMediaSelect
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TvSidebar(
    selectedIndex: Int,
    onSelect: (Int, String) -> Unit
) {
    val navItems = listOf(
        NavEntry("Home", Icons.Default.Home),
        NavEntry("Movies", Icons.Default.Movie),
        NavEntry("TV Shows", Icons.Default.Tv),
        NavEntry("Hindi Dubbed", Icons.Default.Translate),
        NavEntry("Search", Icons.Default.Search),
        NavEntry("Settings", Icons.Default.Settings)
    )

    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 32.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Harsh+",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp, start = 12.dp)
        )

        navItems.forEachIndexed { index, item ->
            TvNavItem(
                title = item.title,
                icon = item.icon,
                isSelected = index == selectedIndex,
                onSelect = { onSelect(index, item.title) }
            )
        }
    }
}

private data class NavEntry(val title: String, val icon: ImageVector)

@Composable
fun TvNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(if (isFocused) 1.08f else 1.0f, label = "scale")
    val backgroundColor = when {
        isFocused -> MaterialTheme.colorScheme.primary
        isSelected -> MaterialTheme.colorScheme.surfaceVariant
        else -> Color.Transparent
    }
    val contentColor = when {
        isFocused -> Color.Black
        isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.LightGray
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = contentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

@Composable
fun TvHeroBanner(
    item: MediaItem,
    onPlay: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                width = if (isFocused) 3.dp else 0.dp,
                color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .focusable()
            .clickable { onPlay() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                        Brush.horizontalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.9f), Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(28.dp)
                    .width(450.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "FEATURED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                item.overview?.let { overview ->
                    Text(
                        text = overview,
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Button(
                    onClick = onPlay,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Watch Now (Press OK)", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TvContentRow(
    title: String,
    items: List<MediaItem>,
    onMediaSelect: (Int, MediaType) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(items) { media ->
                TvPosterCard(
                    item = media,
                    onClick = { onMediaSelect(media.id, media.mediaType) }
                )
            }
        }
    }
}

@Composable
fun TvPosterCard(
    item: MediaItem,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.12f else 1.0f, label = "cardScale")

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(225.dp)
            .scale(scale)
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                width = if (isFocused) 3.dp else 0.dp,
                color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .focusable()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = item.fullPosterUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (item.voteAverage > 0) {
                Surface(
                    color = Color.Black.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "★ ${String.format("%.1f", item.voteAverage)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(8.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
