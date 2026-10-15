package com.harshtg.streaming.player

import android.media.audiofx.LoudnessEnhancer
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.harshtg.streaming.ui.components.dpadFocusable
import com.harshtg.streaming.ui.theme.HarshTGTheme

@OptIn(UnstableApi::class)
class HarshPlayerActivity : ComponentActivity() {

    private var player: ExoPlayer? = null
    private var loudnessEnhancer: LoudnessEnhancer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val streamUrl = intent.getStringExtra("STREAM_URL") ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val title = intent.getStringExtra("STREAM_TITLE") ?: "Harsh TG Live VOD Stream"

        player = ExoPlayer.Builder(this)
            .setSeekForwardIncrementMs(10000)
            .setSeekBackIncrementMs(10000)
            .build().apply {
                val mediaItem = MediaItem.fromUri(streamUrl)
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }

        // Initialize Audio Boost (300% Volume Enhancer)
        player?.audioSessionId?.let { sessionId ->
            if (sessionId != 0) {
                try {
                    loudnessEnhancer = LoudnessEnhancer(sessionId).apply {
                        setTargetGain(1500) // 15dB Boost (Up to 300% Volume Gain)
                        enabled = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        setContent {
            HarshTGTheme {
                PlayerOverlayUI(
                    player = player,
                    title = title,
                    onBack = { finish() }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loudnessEnhancer?.release()
        player?.release()
        player = null
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlayUI(
    player: ExoPlayer?,
    title: String,
    onBack: () -> Unit
) {
    var showControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var volumeBoostEnabled by remember { mutableStateOf(true) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { showControls = !showControls },
                    onDoubleTap = { offset ->
                        if (offset.x > size.width / 2) {
                            player?.seekForward()
                        } else {
                            player?.seekBack()
                        }
                    }
                )
            }
    ) {
        // ExoPlayer Native View
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Custom OSD Control Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(24.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.dpadFocusable(cornerRadius = 20.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        // Volume Boost Pill Button
                        FilterChip(
                            selected = volumeBoostEnabled,
                            onClick = { volumeBoostEnabled = !volumeBoostEnabled },
                            label = { Text("300% Boost") },
                            leadingIcon = { Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.dpadFocusable(cornerRadius = 8.dp)
                        )
                    }
                }

                // Center Play/Pause & Seek
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { player?.seekBack() },
                        modifier = Modifier.dpadFocusable(cornerRadius = 24.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Replay10, contentDescription = "-10s", tint = Color.White, modifier = Modifier.size(36.dp))
                    }

                    FloatingActionButton(
                        onClick = {
                            if (isPlaying) player?.pause() else player?.play()
                            isPlaying = !isPlaying
                        },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.dpadFocusable(cornerRadius = 28.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        onClick = { player?.seekForward() },
                        modifier = Modifier.dpadFocusable(cornerRadius = 24.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Forward10, contentDescription = "+10s", tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                }

                // Bottom Quick Control Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            playbackSpeed = if (playbackSpeed == 1.0f) 1.5f else if (playbackSpeed == 1.5f) 2.0f else 1.0f
                            player?.setPlaybackSpeed(playbackSpeed)
                        },
                        modifier = Modifier.dpadFocusable(cornerRadius = 10.dp)
                    ) {
                        Text(text = "Speed: ${playbackSpeed}x")
                    }

                    Button(
                        onClick = { /* Audio Track Switcher Dialog */ },
                        modifier = Modifier.dpadFocusable(cornerRadius = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Audiotrack, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Audio (5.1/Dolby)")
                    }

                    Button(
                        onClick = { /* Subtitle ASS/SSA Dialog */ },
                        modifier = Modifier.dpadFocusable(cornerRadius = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Subtitles, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Subtitles (ASS/SRT)")
                    }

                    Button(
                        onClick = { /* Stream Quality Switcher */ },
                        modifier = Modifier.dpadFocusable(cornerRadius = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.HighQuality, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "4K / 1080p Stream")
                    }
                }
            }
        }
    }
}
