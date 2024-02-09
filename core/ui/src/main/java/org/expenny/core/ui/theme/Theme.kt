package org.expenny.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = light_primary,
    onPrimary = light_onPrimary,
    primaryContainer = light_primaryContainer,
    onPrimaryContainer = light_onPrimaryContainer,
    secondary = light_secondary,
    onSecondary = light_onSecondary,
    secondaryContainer = light_secondaryContainer,
    onSecondaryContainer = light_onSecondaryContainer,
    tertiary = light_tertiary,
    onTertiary = light_onTertiary,
    tertiaryContainer = light_tertiaryContainer,
    onTertiaryContainer = light_onTertiaryContainer,
    error = light_error,
    errorContainer = light_errorContainer,
    onError = light_onError,
    onErrorContainer = light_onErrorContainer,
    background = light_background,
    onBackground = light_onBackground,
    surface = light_surface,
    onSurface = light_onSurface,
    surfaceVariant = light_surfaceVariant,
    onSurfaceVariant = light_onSurfaceVariant,
    outline = light_outline,
    outlineVariant = light_outlineVariant,
    inverseOnSurface = light_inverseOnSurface,
    inverseSurface = light_inverseSurface,
    inversePrimary = light_inversePrimary,
    surfaceTint = light_surfaceTint,
    scrim = light_scrim,
    surfaceBright = light_surfaceBright,
    surfaceContainer = light_surfaceContainer,
    surfaceContainerHigh = light_surfaceContainerHigh,
    surfaceContainerHighest = light_surfaceContainerHighest,
    surfaceContainerLow = light_surfaceContainerLow,
    surfaceContainerLowest = light_surfaceContainerLowest,
    surfaceDim = light_surfaceDim,
)

private val DarkColors = darkColorScheme(
    primary = dark_primary,
    onPrimary = dark_onPrimary,
    primaryContainer = dark_primaryContainer,
    onPrimaryContainer = dark_onPrimaryContainer,
    secondary = dark_secondary,
    onSecondary = dark_onSecondary,
    secondaryContainer = dark_secondaryContainer,
    onSecondaryContainer = dark_onSecondaryContainer,
    tertiary = dark_tertiary,
    onTertiary = dark_onTertiary,
    tertiaryContainer = dark_tertiaryContainer,
    onTertiaryContainer = dark_onTertiaryContainer,
    error = dark_error,
    errorContainer = dark_errorContainer,
    onError = dark_onError,
    onErrorContainer = dark_onErrorContainer,
    background = dark_background,
    onBackground = dark_onBackground,
    surface = dark_surface,
    onSurface = dark_onSurface,
    surfaceVariant = dark_surfaceVariant,
    onSurfaceVariant = dark_onSurfaceVariant,
    outline = dark_outline,
    outlineVariant = dark_outlineVariant,
    inverseOnSurface = dark_inverseOnSurface,
    inverseSurface = dark_inverseSurface,
    inversePrimary = dark_inversePrimary,
    surfaceTint = dark_surfaceTint,
    scrim = dark_scrim,
    surfaceBright = dark_surfaceBright,
    surfaceContainer = dark_surfaceContainer,
    surfaceContainerHigh = dark_surfaceContainerHigh,
    surfaceContainerHighest = dark_surfaceContainerHighest,
    surfaceContainerLow = dark_surfaceContainerLow,
    surfaceContainerLowest = dark_surfaceContainerLowest,
    surfaceDim = dark_surfaceDim,
)

val ColorScheme.surfaceInput: Color
    @Composable get() {
        return MaterialTheme.colorScheme.surfaceContainer
    }

val ColorScheme.positive: Color
    @Composable get() {
        return if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF00C853)
    }

val ColorScheme.negative: Color
    @Composable get() {
        return MaterialTheme.colorScheme.error
    }

@Composable
fun ExpennyTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!isDarkTheme) LightColors else DarkColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
