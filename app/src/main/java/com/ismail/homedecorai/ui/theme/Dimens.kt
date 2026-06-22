package com.ismail.homedecorai.ui.theme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object HomeDecorSpacing {
    val Xxs = 2.dp
    val Xs = 4.dp
    val Sm = 8.dp
    val Md = 12.dp
    val Base = 16.dp
    val Lg = 20.dp
    val Xl = 24.dp
    val Xxl = 28.dp
    val Xxl2 = 32.dp
    val Xxl3 = 40.dp
    val Xxl4 = 48.dp
    val Xxl5 = 56.dp
    val Xxl6 = 64.dp
    val Xxl7 = 80.dp

    val ScreenHorizontal = 16.dp
    val CardPadding = 16.dp
    val SectionGap = 24.dp
    val ListItemGap = 16.dp
    val ChipGap = 8.dp
    val IconGap = 8.dp
    val ButtonHeight = 56.dp
    val TouchTarget = 48.dp
    val CtaBarHeight = 80.dp
    val NavBarReservation = 80.dp
    val BottomContentPadding = 80.dp
    val WizardBottomContentPadding = 104.dp
}

@Composable
fun navBarBottomPadding(additionalContentPadding: Dp = 0.dp): Dp {
    val navBarBottom = WindowInsets.navigationBars
        .asPaddingValues(LocalDensity.current)
        .calculateBottomPadding()
    return HomeDecorSpacing.Base + navBarBottom + additionalContentPadding
}
