package com.ismail.homedecorai.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// HomeDecor AI Dimensions
// ---------------------------------------------------------------------------
// Consistent spacing, sizing, and layout tokens for MD3 Expressive.
// ---------------------------------------------------------------------------

object HomeDecorSpacing {
    val Xxs = 2.dp
    val Xs = 4.dp
    val Sm = 8.dp
    val Md = 12.dp
    val Base = 16.dp
    val Lg = 24.dp
    val Xl = 32.dp
    val Xxl = 40.dp

    // Layout
    val ScreenHorizontal = 16.dp
    val CardPadding = 16.dp
    val CardInternal = 16.dp
    val SectionGap = 24.dp
    val ListItemGap = 16.dp
    val ChipGap = 8.dp
    val IconGap = 8.dp
    val ButtonHeight = 56.dp
    val TouchTarget = 48.dp
    val CtaBarHeight = 80.dp
    val NavBarReservation = 90.dp
    val BottomContentPadding = 90.dp
    val WizardBottomContentPadding = 104.dp

    // Expressive additions
    val HeroVerticalPadding = 32.dp
    val ScreenVerticalPadding = 20.dp
    val CardElevationGap = 4.dp
}

fun navBarBottomPadding(additionalContentPadding: Dp = 0.dp): Dp {
    return HomeDecorSpacing.NavBarReservation + additionalContentPadding
}
