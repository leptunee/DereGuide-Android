package com.dereguide.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// DereGuide brand colors inspired by Cinderella Girls
private val Pink200 = Color(0xFFFFB3E6)
private val Pink500 = Color(0xFFE91E63)
private val Pink700 = Color(0xFFC2185B)
private val Teal200 = Color(0xFF80CBC4)
private val Teal700 = Color(0xFF00796B)

// Attribute colors
val CuteColor = Color(0xFFFF69B4)    // Hot Pink
val CoolColor = Color(0xFF4169E1)     // Royal Blue  
val PassionColor = Color(0xFFFF4500)  // Orange Red

private val DarkColorScheme = darkColorScheme(
    primary = Pink200,
    secondary = Teal200,
    tertiary = Pink700,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Pink500,
    secondary = Teal700,
    tertiary = Pink700,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun DereGuideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
