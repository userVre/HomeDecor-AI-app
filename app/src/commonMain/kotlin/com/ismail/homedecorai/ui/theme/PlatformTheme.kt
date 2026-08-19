package com.ismail.homedecorai.ui.theme

import androidx.compose.runtime.Composable

@Composable
expect fun HomeDecorTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit,
)

@Composable
expect fun isReducedMotionEnabled(): Boolean
