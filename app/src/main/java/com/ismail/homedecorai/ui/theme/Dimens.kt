package com.ismail.homedecorai.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object HomeDecorSpacing {
    val Xxs = 2.dp
    val Xs = 4.dp
    val Sm = 8.dp
    val Md = 12.dp
    val Base = 16.dp
    val Lg = 24.dp
    val Xl = 32.dp
    val Xxl = 40.dp

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
    val NavBarReservation = 80.dp
    val BottomContentPadding = 80.dp
    val WizardBottomContentPadding = 104.dp
}

fun navBarBottomPadding(additionalContentPadding: Dp = 0.dp): Dp {
    return HomeDecorSpacing.NavBarReservation + additionalContentPadding
}
