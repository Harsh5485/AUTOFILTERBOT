package com.harshtg.streaming.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.harshtg.streaming.data.model.StremioMetaItem
import com.harshtg.streaming.ui.components.dpadFocusable

@Composable
fun DetailScreen(
    type: String,
    id: String,
    onPlayClick: (String) -> Unit
) {
    // Sample item data for detail view
    val item = StremioMetaItem(
        id = id,
        type = type,
        name = if (type == "movie") "Inception (4K Remux)" else "Demon Slayer: Entertainment District",
        poster = "https://image.tmdb.org/t/p/w500/oYuLE131121v12v2a.jpg",
        background = "https://image.tmdb.org/t/p/original/8ZTVqvKDQ8A.jpg",
        logo = null,
        description = "A skilled thief who steals valuable secrets from deep within the subconscious during the dream state, when the mind is at its most vulnerable.",
        releaseInfo = "2010",
        imdbRating = "8.8",
        genres = listOf("Action", "Sci-Fi", "Adventure"),
        videos = null
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = item.background ?: item.poster,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = item.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.imdbRating ?: "8.5", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = item.releaseInfo ?: "2023", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = item.description ?: "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onPlayClick("http://sample.vod/stream.mkv") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .dpadFocusable(cornerRadius = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "PLAY STREAM (TELEGRAM FAST VOD)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
