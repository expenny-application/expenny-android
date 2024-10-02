package org.expenny.core.ui.foundation

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.expenny.core.ui.extensions.isApplicationInDarkMode

internal val lightColors = lightColorScheme(
    primary = Color(0xFF0856CE),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD4E4F6),
    onPrimaryContainer = Color(0xFF0D1D2A),
    secondary = Color(0xFF334155),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = Color(0xFF1E293B),
    tertiary = Color(0xFF735471),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFED7F9),
    onTertiaryContainer = Color(0xFF2B122B),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFBFBFF),
    onBackground = Color(0xFF1A1B21),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1B21),
    surfaceVariant = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFF334155),
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFFD1D5DB),
    inverseOnSurface = Color(0xFFFBFBFF),
    inverseSurface = Color(0xFF121318),
    inversePrimary = Color(0xFFB1D0FF),
    surfaceTint = Color(0xFF0856CE),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFFAF8FF),
    surfaceContainer = Color(0xFFF4F6F8),
    surfaceContainerHigh = Color(0xFFE2E8F0),
    surfaceContainerHighest = Color(0xFFCBD5E1),
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFD9D9E3),
)

internal val darkColors = darkColorScheme(
    primary = Color(0xFFB1D0FF),
    onPrimary = Color(0xFF16274B),
    primaryContainer = Color(0xFF13438C),
    onPrimaryContainer = Color(0xFFDAE2FF),
    secondary = Color(0xFFC1C4D4),
    onSecondary = Color(0xFF1A1B21),
    secondaryContainer = Color(0xFF33343A),
    onSecondaryContainer = Color(0xFFC1C4D4),
    tertiary = Color(0xFFE1BBDD),
    onTertiary = Color(0xFF412741),
    tertiaryContainer = Color(0xFF5A3D59),
    onTertiaryContainer = Color(0xFFFED7F9),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF121318),
    onBackground = Color(0xFFE4E2E6),
    surface = Color(0xFF121318),
    onSurface = Color(0xFFFBFBFF),
    surfaceVariant = Color(0xFF45464F),
    onSurfaceVariant = Color(0xFFC5C6D0),
    outline = Color(0xFF6B7280),
    outlineVariant = Color(0xFF45464F),
    inverseOnSurface = Color(0xFF1A1B21),
    inverseSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF0856CE),
    surfaceTint = Color(0xFFB1D0FF),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF23252E),
    surfaceContainer = Color(0xFF1E2026),
    surfaceContainerHigh = Color(0xFF202229),
    surfaceContainerHighest = Color(0xFF22242C),
    surfaceContainerLow = Color(0xFF1B1C22),
    surfaceContainerLowest = Color(0xFF0D0E11),
    surfaceDim = Color(0xFF121318),
)

val ColorScheme.transparent: Color
    @Composable get() = Color.Transparent

val ColorScheme.primaryContrast: Color
    @Composable get() {
        return if (isApplicationInDarkMode) Color(0xFFCBD5E1) else Color(0xFF1E293B)
    }

val ColorScheme.primaryFixed: Color
    @Composable get() = Color(0xFF0856CE)

val ColorScheme.redFixed: Color
    @Composable get() = Color(0xFFE53935)

val ColorScheme.deepPurpleFixed: Color
    @Composable get() = Color(0xFF723CEB)

val ColorScheme.yellowFixed: Color
    @Composable get() = Color(0xFFFFD600)

val ColorScheme.skyBlueFixed: Color
    @Composable get() = Color(0xFF42A5F5)

val ColorScheme.purpleFixed: Color
    @Composable get() = Color(0xFFE040FB)

val ColorScheme.lightGreenFixed: Color
    @Composable get() = Color(0xFFAEEA00)

val ColorScheme.pinkFixed: Color
    @Composable get() = Color(0xFFEC407A)

val ColorScheme.blueFixed: Color
    @Composable get() = Color(0xFF2962FF)

val ColorScheme.tealFixed: Color
    @Composable get() = Color(0xFF00BFA5)

val ColorScheme.orangeFixed: Color
    @Composable get() = Color(0xFFEF6C00)

val ColorScheme.limeFixed: Color
    @Composable get() = Color(0xFFC0CA33)

val ColorScheme.greenFixed: Color
    @Composable get() = Color(0xFF41C300)

val ColorScheme.grayFixed: Color
    @Composable get() = Color(0xFF616161)

val ColorScheme.brownFixed: Color
    @Composable get() = Color(0xFF8D6E63)

val ColorScheme.blackFixed: Color
    @Composable get() = Color(0xFF212121)

object ExpennyColor {
    val Red: Color = Color(0xFFE53935)
    val DeepPurple: Color = Color(0xFF723CEB)
    val Yellow: Color = Color(0xFFFFD600)
    val SkyBlue: Color = Color(0xFF42A5F5)
    val Purple: Color = Color(0xFFE040FB)
    val LightGreen: Color = Color(0xFFAEEA00)
    val Pink: Color = Color(0xFFEC407A)
    val Blue: Color = Color(0xFF2962FF)
    val Teal: Color = Color(0xFF00BFA5)
    val Orange: Color = Color(0xFFEF6C00)
    val Lime: Color = Color(0xFFC0CA33)
    val Green: Color = Color(0xFF41C300)
    val Gray: Color = Color(0xFF616161)
    val Brown: Color = Color(0xFF8D6E63)
    val Black: Color = Color(0xFF212121)
}
