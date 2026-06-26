package com.ismail.homedecorai.ui.theme

import androidx.compose.runtime.Composable
import com.ismail.homedecorai.isReducedMotionEnabled

@Composable
actual fun HomeDecorTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    HomeDecorThemeInner(colorScheme, content)
}

@Composable
actual fun isReducedMotionEnabled(): Boolean = isReducedMotionEnabled()
