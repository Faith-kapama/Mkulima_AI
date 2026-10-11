package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MkulimaColorScheme =
  lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = BrandSurfaceGreen,
    onPrimaryContainer = ForestGreenDark,
    secondary = ForestGreenLight,
    onSecondary = Color.White,
    secondaryContainer = BrandSurfaceGreenLight,
    onSecondaryContainer = ForestGreenDark,
    tertiary = ForestGreenDark,
    background = BackgroundLight,
    onBackground = TextPrimaryDark,
    surface = SurfaceLight,
    onSurface = TextPrimaryDark,
    surfaceVariant = BrandSurfaceGreenLight,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = MkulimaColorScheme,
    typography = Typography,
    content = content,
  )
}

