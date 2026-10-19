package com.harsh.streaming.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AmberPrimary = Color(0xFFF59E0B)
val AmberSecondary = Color(0xFFD97706)
val DarkBackground = Color(0xFF0C0C0D)
val DarkSurface = Color(0xFF18181B)
val DarkSurfaceVariant = Color(0xFF27272A)
val TextPrimary = Color(0xFFFAFAFA)
val TextSecondary = Color(0xFFA1A1AA)

private val AppColorScheme: ColorScheme = darkColorScheme(
    primary = AmberPrimary,
    secondary = AmberSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun HarshStreamingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}
