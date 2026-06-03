package com.ismail.homedecorai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private val LightColorScheme = expressiveLightColorScheme().copy(
    primary = HomeDecorColors.Primary,
    onPrimary = HomeDecorColors.OnPrimary,
    primaryContainer = HomeDecorColors.PrimaryContainer,
    onPrimaryContainer = HomeDecorColors.OnPrimaryContainer,
    secondary = HomeDecorColors.SecondaryColor,
    onSecondary = HomeDecorColors.OnSecondary,
    secondaryContainer = HomeDecorColors.SecondaryContainer,
    onSecondaryContainer = HomeDecorColors.OnSecondaryContainer,
    tertiary = HomeDecorColors.Tertiary,
    onTertiary = HomeDecorColors.OnTertiary,
    tertiaryContainer = HomeDecorColors.TertiaryContainer,
    onTertiaryContainer = HomeDecorColors.OnTertiaryContainer,
    error = HomeDecorColors.ErrorColor,
    onError = HomeDecorColors.OnError,
    errorContainer = HomeDecorColors.ErrorContainerColor,
    onErrorContainer = HomeDecorColors.OnErrorContainer,
    background = HomeDecorColors.Background,
    onBackground = HomeDecorColors.OnBackground,
    surface = HomeDecorColors.Canvas,
    onSurface = HomeDecorColors.OnSurface,
    onSurfaceVariant = HomeDecorColors.OnSurfaceVariant,
    outline = HomeDecorColors.Outline,
    outlineVariant = HomeDecorColors.OutlineVariant,
    surfaceDim = HomeDecorColors.SurfaceDim,
    surfaceBright = HomeDecorColors.SurfaceBright,
    surfaceContainerLowest = HomeDecorColors.SurfaceContainerLowest,
    surfaceContainerLow = HomeDecorColors.SurfaceContainerLow,
    surfaceContainer = HomeDecorColors.SurfaceContainer,
    surfaceContainerHigh = HomeDecorColors.SurfaceContainerHigh,
    surfaceContainerHighest = HomeDecorColors.SurfaceContainerHighest,
)

private val DarkColorScheme = darkColorScheme(
    primary = HomeDecorColors.PremiumGold,
    onPrimary = Color.Black,
    primaryContainer = HomeDecorColors.DarkOverlay,
    onPrimaryContainer = HomeDecorColors.PremiumGold,
    secondary = HomeDecorColors.DisabledDarkText,
    onSecondary = Color.Black,
    tertiary = HomeDecorColors.GoldDeep,
    onTertiary = Color.Black,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    background = HomeDecorColors.Dark,
    onBackground = Color(0xFFE8E0D5),
    surface = HomeDecorColors.DarkSurface,
    onSurface = Color(0xFFE8E0D5),
    onSurfaceVariant = Color(0xFFCCC3B5),
    outline = Color(0xFF968E80),
    outlineVariant = Color(0xFF4C4639),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeDecorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = HomeDecorTypography,
        shapes = HomeDecorShapes,
        content = content,
    )
}

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
