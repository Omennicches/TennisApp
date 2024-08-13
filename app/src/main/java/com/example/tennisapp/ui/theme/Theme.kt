package com.example.tennisapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Immutable

// Deine benutzerdefinierten Farben
@Immutable
data class AppColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceTint: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color
)

// Konvertiere AppColorScheme zu ColorScheme
fun AppColorScheme.toColorScheme(): ColorScheme {
    return ColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim
    )
}

// Light and Dark Color Schemes
fun lightColorScheme(): AppColorScheme {
    return AppColorScheme(
        primary = ColorLightTokens.Primary,
        onPrimary = ColorLightTokens.OnPrimary,
        primaryContainer = ColorLightTokens.PrimaryContainer,
        onPrimaryContainer = ColorLightTokens.OnPrimaryContainer,
        inversePrimary = ColorLightTokens.InversePrimary,
        secondary = ColorLightTokens.Secondary,
        onSecondary = ColorLightTokens.OnSecondary,
        secondaryContainer = ColorLightTokens.SecondaryContainer,
        onSecondaryContainer = ColorLightTokens.OnSecondaryContainer,
        tertiary = ColorLightTokens.Tertiary,
        onTertiary = ColorLightTokens.OnTertiary,
        tertiaryContainer = ColorLightTokens.TertiaryContainer,
        onTertiaryContainer = ColorLightTokens.OnTertiaryContainer,
        background = ColorLightTokens.Background,
        onBackground = ColorLightTokens.OnBackground,
        surface = ColorLightTokens.Surface,
        onSurface = ColorLightTokens.OnSurface,
        surfaceVariant = ColorLightTokens.SurfaceVariant,
        onSurfaceVariant = ColorLightTokens.OnSurfaceVariant,
        surfaceTint = ColorLightTokens.Primary,
        inverseSurface = ColorLightTokens.InverseSurface,
        inverseOnSurface = ColorLightTokens.InverseOnSurface,
        error = ColorLightTokens.Error,
        onError = ColorLightTokens.OnError,
        errorContainer = ColorLightTokens.ErrorContainer,
        onErrorContainer = ColorLightTokens.OnErrorContainer,
        outline = ColorLightTokens.Outline,
        outlineVariant = ColorLightTokens.OutlineVariant,
        scrim = ColorLightTokens.Scrim
    )
}

fun darkColorScheme(): AppColorScheme {
    return AppColorScheme(
        primary = ColorDarkTokens.Primary,
        onPrimary = ColorDarkTokens.OnPrimary,
        primaryContainer = ColorDarkTokens.PrimaryContainer,
        onPrimaryContainer = ColorDarkTokens.OnPrimaryContainer,
        inversePrimary = ColorDarkTokens.InversePrimary,
        secondary = ColorDarkTokens.Secondary,
        onSecondary = ColorDarkTokens.OnSecondary,
        secondaryContainer = ColorDarkTokens.SecondaryContainer,
        onSecondaryContainer = ColorDarkTokens.OnSecondaryContainer,
        tertiary = ColorDarkTokens.Tertiary,
        onTertiary = ColorDarkTokens.OnTertiary,
        tertiaryContainer = ColorDarkTokens.TertiaryContainer,
        onTertiaryContainer = ColorDarkTokens.OnTertiaryContainer,
        background = ColorDarkTokens.Background,
        onBackground = ColorDarkTokens.OnBackground,
        surface = ColorDarkTokens.Surface,
        onSurface = ColorDarkTokens.OnSurface,
        surfaceVariant = ColorDarkTokens.SurfaceVariant,
        onSurfaceVariant = ColorDarkTokens.OnSurfaceVariant,
        surfaceTint = ColorDarkTokens.Primary,
        inverseSurface = ColorDarkTokens.InverseSurface,
        inverseOnSurface = ColorDarkTokens.InverseOnSurface,
        error = ColorDarkTokens.Error,
        onError = ColorDarkTokens.OnError,
        errorContainer = ColorDarkTokens.ErrorContainer,
        onErrorContainer = ColorDarkTokens.OnErrorContainer,
        outline = ColorDarkTokens.Outline,
        outlineVariant = ColorDarkTokens.OutlineVariant,
        scrim = ColorDarkTokens.Scrim
    )
}

// Die Haupttheme-Funktion
@Composable
fun TennisAppTheme(
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val colorScheme = if (isDarkTheme) {
        darkColorScheme().toColorScheme()
    } else {
        lightColorScheme().toColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Hier eine Typografie definieren
        content = content
    )
}
