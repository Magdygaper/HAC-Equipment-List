package com.example.hacequipment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = HacAccentAmber,
    onPrimary = HacBg,
    primaryContainer = HacAmberDark,
    onPrimaryContainer = HacText,
    secondary = HacBlue,
    onSecondary = HacBg,
    secondaryContainer = HacSurfaceElevated,
    onSecondaryContainer = HacText,
    tertiary = HacGreen,
    onTertiary = HacBg,
    background = HacBg,
    onBackground = HacText,
    surface = HacSurface,
    onSurface = HacText,
    surfaceVariant = HacSurfaceElevated,
    onSurfaceVariant = HacTextMuted,
    outline = HacBorder,
    outlineVariant = HacBorder,
    error = HacRed,
    onError = HacText
)

@Composable
fun HacEquipmentTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
