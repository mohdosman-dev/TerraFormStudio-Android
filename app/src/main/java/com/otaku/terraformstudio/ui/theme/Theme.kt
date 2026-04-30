package com.otaku.terraformstudio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    error = Error,
    surfaceVariant = SurfaceWarm,
    onSurfaceVariant = TextSecondary,
    outline = Border
)

// Dark mode can be refined later if needed, for now using a darkened version of the palette
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimaryLight,
    background = Color(0xFF1F1A17),
    onBackground = Color(0xFFF5F1EB),
    surface = Color(0xFF1F1A17),
    onSurface = Color(0xFFF5F1EB)
)

@Composable
fun TerraFormStudioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
