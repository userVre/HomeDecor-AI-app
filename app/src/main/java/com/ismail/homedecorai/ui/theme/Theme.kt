package com.ismail.homedecorai.ui.theme

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object HomeDecorColors {
    val Ink = Color(0xFF19140D)
    val InkSoft = Color(0xFF5F5648)
    val Canvas = Color(0xFFF6F1E9)
    val Paper = Color(0xFFFFFCF7)
    val Mist = Color(0xFFEDE5D8)
    val Line = Color(0xFFD9CBB8)
    val Secondary = Color(0xFF6A604D)
    val Accent = Color(0xFF8A5A18)
    val AccentContainer = Color(0xFFFFE7BA)
    val ProContainer = Color(0xFFFFF0D1)
    val PremiumGold = Color(0xFFD2AA5A)
    val GoldDeep = Color(0xFFB88A3A)
    val Dark = Color(0xFF070706)
    val DarkSurface = Color(0xFF11100D)
    val DarkOverlay = Color(0xFF4A3522)
    val DisabledDarkButton = Color(0xFF2B261D)
    val DisabledDarkText = Color(0xFFC8BDAA)
    val Error = Color(0xFFB3261E)
    val ErrorContainer = Color(0xFFFFEDEA)
    val Success = Color(0xFF9BC489)
}

val StudioInk = HomeDecorColors.Ink
val StudioAccent = HomeDecorColors.Accent
val StudioBlue = StudioAccent
val StudioGreen = StudioAccent
val StudioMoss = HomeDecorColors.Secondary
val StudioRose = HomeDecorColors.Error
val StudioCanvas = HomeDecorColors.Canvas
val StudioPaper = HomeDecorColors.Paper
val StudioMist = HomeDecorColors.Mist
val StudioLine = HomeDecorColors.Line
val StudioBlack = HomeDecorColors.DarkSurface
val StudioGold = HomeDecorColors.GoldDeep
val StudioSky = StudioAccent
val StudioViolet = StudioAccent
val StudioPrimaryContainer = HomeDecorColors.AccentContainer
val StudioProContainer = HomeDecorColors.ProContainer
val StudioErrorContainer = HomeDecorColors.ErrorContainer

val PaywallBg = HomeDecorColors.Dark
val PaywallAccent = HomeDecorColors.GoldDeep
val PaywallPremiumGold = HomeDecorColors.PremiumGold
val PaywallCard = Color(0x14FFFAEE)
val PaywallCardAlt = Color(0x0CFFFAEE)
val PaywallBorder = Color(0x29EFDDB8)
val PaywallTextSecondary = Color(0xDDF6EFE0)
val PaywallTextMuted = Color(0xB8F6EFE0)
val PaywallDisabledButton = HomeDecorColors.DisabledDarkButton
val PaywallDisabledText = HomeDecorColors.DisabledDarkText
val PaywallSuccess = HomeDecorColors.Success

fun Modifier.minimumTouchTarget(): Modifier = sizeIn(minWidth = 48.dp, minHeight = 48.dp)

fun Modifier.disabledSemantics(enabled: Boolean): Modifier =
    if (enabled) this else semantics { disabled() }

fun studioStateContainer(selected: Boolean): Color = if (selected) StudioPrimaryContainer else StudioPaper
fun studioStateBorder(selected: Boolean): Color = if (selected) StudioBlue else StudioLine
fun studioStateElevation(selected: Boolean): Dp = if (selected) 4.dp else 1.dp
fun studioStateIconContainer(selected: Boolean): Color = if (selected) StudioBlue else StudioMist

@Composable
fun studioStateIconContent(selected: Boolean): Color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

@Composable
fun studioPrimaryButtonColors() = ButtonDefaults.buttonColors(
    containerColor = StudioBlue,
    contentColor = Color.White,
)

@Composable
fun studioProButtonColors() = ButtonDefaults.buttonColors(
    containerColor = StudioGold,
    contentColor = Color.White,
)
