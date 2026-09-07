package com.example.bookmyturf.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BookMyTurfColorScheme = lightColorScheme(
    primary = AdminDarkGreen,
    onPrimary = AdminWhite,

    primaryContainer = AdminForestGreen,
    onPrimaryContainer = AdminWhite,

    secondary = AdminForestGreen,
    onSecondary = AdminWhite,

    tertiary = AdminLightGreen,
    onTertiary = AdminDarkCharcoal,

    background = AdminOffWhite,
    onBackground = AdminDarkCharcoal,

    surface = AdminWhite,
    onSurface = AdminDarkCharcoal,

    surfaceVariant = AdminOffWhite,
    onSurfaceVariant = AdminGray,

    outline = AdminGray
)

@Composable
fun BookMyTurfTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BookMyTurfColorScheme,
        typography = Typography,
        content = content
    )
}