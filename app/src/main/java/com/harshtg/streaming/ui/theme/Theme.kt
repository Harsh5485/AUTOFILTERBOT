package com.harshtg.streaming.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class AppThemeMode {
    NIGHT,
    INK,
    PAPER,
    TV_OLED
}

private val NightColorScheme = darkColorScheme(
    background = NightBackground,
    surface = NightSurface,
    surfaceVariant = NightSurfaceVariant,
    primary = NightPrimary,
    secondary = NightSecondary,
    onBackground = NightTextPrimary,
    onSurface = NightTextPrimary,
    onSurfaceVariant = NightTextSecondary
)

private val InkColorScheme = darkColorScheme(
    background = InkBackground,
    surface = InkSurface,
    surfaceVariant = InkSurfaceVariant,
    primary = InkPrimary,
    secondary = InkSecondary,
    onBackground = InkTextPrimary,
    onSurface = InkTextPrimary,
    onSurfaceVariant = InkTextSecondary
)

private val PaperColorScheme = lightColorScheme(
    background = PaperBackground,
    surface = PaperSurface,
    surfaceVariant = PaperSurfaceVariant,
    primary = PaperPrimary,
    secondary = PaperSecondary,
    onBackground = PaperTextPrimary,
    onSurface = PaperTextPrimary,
    onSurfaceVariant = PaperTextSecondary
)

private val OLEDColorScheme = darkColorScheme(
    background = OLEDBackground,
    surface = OLEDSurface,
    surfaceVariant = OLEDSurfaceVariant,
    primary = OLEDPrimary,
    secondary = OLEDSecondary,
    onBackground = OLEDTextPrimary,
    onSurface = OLEDTextPrimary,
    onSurfaceVariant = OLEDTextSecondary
)

@Composable
fun HarshTGTheme(
    themeMode: AppThemeMode = AppThemeMode.NIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.NIGHT -> NightColorScheme
        AppThemeMode.INK -> InkColorScheme
        AppThemeMode.PAPER -> PaperColorScheme
        AppThemeMode.TV_OLED -> OLEDColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
