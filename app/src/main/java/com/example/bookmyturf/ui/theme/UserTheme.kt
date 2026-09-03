
package com.example.bookmyturf.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


// ============================================================
// BOOK MY TURF — PREMIUM USER COLORS
// ============================================================

val UserDarkGreen = Color(0xFF173D20)
val UserForestGreen = Color(0xFF2E6B35)
val UserLightGreen = Color(0xFF7DBB4A)

val UserOffWhite = Color(0xFFF8F8F5)
val UserWhite = Color(0xFFFFFFFF)

val UserCharcoal = Color(0xFF1C1C1C)
val UserGray = Color(0xFF737373)


// ============================================================
// LIGHT COLOR SCHEME
// ============================================================

private val UserLightColorScheme = lightColorScheme(

    // Main brand
    primary = UserDarkGreen,
    onPrimary = UserWhite,

    // Secondary brand
    secondary = UserForestGreen,
    onSecondary = UserWhite,

    // Accent
    tertiary = UserLightGreen,
    onTertiary = UserCharcoal,

    // Background
    background = UserOffWhite,
    onBackground = UserCharcoal,

    // Cards / surfaces
    surface = UserWhite,
    onSurface = UserCharcoal,

    surfaceVariant = Color(0xFFEFEFEA),
    onSurfaceVariant = UserGray,

    // Containers
    primaryContainer = Color(0xFFE4F0E3),
    onPrimaryContainer = UserDarkGreen,

    secondaryContainer = Color(0xFFE5EFE5),
    onSecondaryContainer = UserDarkGreen,

    tertiaryContainer = Color(0xFFEAF4DD),
    onTertiaryContainer = UserDarkGreen,

    // Error
    error = Color(0xFFBA1A1A),
    onError = UserWhite,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)


// ============================================================
// DARK COLOR SCHEME
// ============================================================

private val UserDarkColorScheme = darkColorScheme(

    primary = UserLightGreen,
    onPrimary = UserCharcoal,

    secondary = UserForestGreen,
    onSecondary = UserWhite,

    tertiary = UserLightGreen,
    onTertiary = UserCharcoal,

    background = Color(0xFF101610),
    onBackground = UserWhite,

    surface = Color(0xFF171D18),
    onSurface = UserWhite,

    surfaceVariant = Color(0xFF293129),
    onSurfaceVariant = Color(0xFFBFC8BF),

    primaryContainer = Color(0xFF28532D),
    onPrimaryContainer = Color(0xFFD7F0D4),

    secondaryContainer = Color(0xFF2E4932),
    onSecondaryContainer = Color(0xFFD8EBD7),

    tertiaryContainer = Color(0xFF3E522A),
    onTertiaryContainer = Color(0xFFE5F3D1),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),

    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)


// ============================================================
// PREMIUM TYPOGRAPHY
// ============================================================

private val UserTypography = Typography(

    // Large screen title
    displaySmall = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.5).sp
    ),

    // Main screen headings
    headlineSmall = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.3).sp
    ),

    // Section headings
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),

    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),

    // Normal content
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 23.sp
    ),

    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp
    ),

    // Labels / chips / buttons
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),

    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),

    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
)


// ============================================================
// USER THEME
// ============================================================

@Composable
fun UserTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    MaterialTheme(

        colorScheme =
            if (darkTheme) {
                UserDarkColorScheme
            } else {
                UserLightColorScheme
            },

        typography = UserTypography,

        content = content
    )
}

