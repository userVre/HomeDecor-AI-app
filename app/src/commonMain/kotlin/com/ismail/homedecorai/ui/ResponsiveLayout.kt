package com.ismail.homedecorai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.getScreenWidthDp

private val DesktopBreakpoint = 1024
private val CompactBreakpoint = 600
private val DesktopMaxWidth = 1200.dp

@Composable
fun rememberIsDesktop(): Boolean {
    val screenWidthDp = getScreenWidthDp()
    return remember(screenWidthDp) { screenWidthDp >= DesktopBreakpoint }
}

@Composable
fun rememberIsCompact(): Boolean {
    val screenWidthDp = getScreenWidthDp()
    return remember(screenWidthDp) { screenWidthDp < CompactBreakpoint }
}

@Composable
fun ResponsiveLayout(
    content: @Composable () -> Unit,
) {
    val isDesktop = rememberIsDesktop()

    if (isDesktop) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier.widthIn(max = DesktopMaxWidth),
            ) {
                content()
            }
        }
    } else {
        content()
    }
}
