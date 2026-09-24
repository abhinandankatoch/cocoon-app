package com.abhinandan.cocoon.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6B8E75),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCE8DD),
    onPrimaryContainer = Color(0xFF23392A),
    secondary = Color(0xFFC98D6B),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFAF7F2),
    onBackground = Color(0xFF3D3833),
    surface = Color(0xFFF3EEE6),
    onSurface = Color(0xFF3D3833),
    surfaceVariant = Color(0xFFE9E2D6),
    onSurfaceVariant = Color(0xFF8A8378),
    outline = Color(0xFFDAD2C4),
    error = Color(0xFFB3564A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9CB8A3),
    onPrimary = Color(0xFF17281C),
    primaryContainer = Color(0xFF2E4534),
    onPrimaryContainer = Color(0xFFDCE8DD),
    secondary = Color(0xFFE0AE8C),
    onSecondary = Color(0xFF3A2214),
    background = Color(0xFF1C1917),
    onBackground = Color(0xFFEDE8E0),
    surface = Color(0xFF262220),
    onSurface = Color(0xFFEDE8E0),
    surfaceVariant = Color(0xFF3A342F),
    onSurfaceVariant = Color(0xFFA79E92),
    outline = Color(0xFF4A433C),
    error = Color(0xFFD08578)
)

@Composable
fun CocoonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = CocoonTypography,
        content = content
    )
}