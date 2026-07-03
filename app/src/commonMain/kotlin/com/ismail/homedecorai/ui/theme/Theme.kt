package com.ismail.homedecorai.ui.theme

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// Extended semantic color tokens (beyond MD3 core scheme)
// ---------------------------------------------------------------------------

@Immutable
data class HomeDecorExtraColors(
    // Core semantic
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val accent: Color,
    val accentContainer: Color,
    val diamondAccent: Color,
    val proAccent: Color,
    val premiumGold: Color,
    val card: Color,
    val elevatedCard: Color,
    val scrim: Color,
    // Extended semantic tokens for gradients, overlays, and on-gradient text
    val scrimLight: Color,
    val scrimMedium: Color,
    val scrimHeavy: Color,
    val onGradientText: Color,
    val onGradientTextSubtle: Color,
    // Quaternary / Terracotta
    val quaternary: Color,
    val onQuaternary: Color,
    val quaternaryContainer: Color,
    val onQuaternaryContainer: Color,
    // Interactive state helpers
    val surfaceHover: Color,
    val surfacePressed: Color,
    val focusRing: Color,
)

val LocalDarkTheme = staticCompositionLocalOf { false }

val LocalHomeDecorExtraColors = staticCompositionLocalOf {
    HomeDecorExtraColors(
        success = Color.Unspecified,
        successContainer = Color.Unspecified,
        warning = Color.Unspecified,
        warningContainer = Color.Unspecified,
        accent = Color.Unspecified,
        accentContainer = Color.Unspecified,
        diamondAccent = Color.Unspecified,
        proAccent = Color.Unspecified,
        premiumGold = Color.Unspecified,
        card = Color.Unspecified,
        elevatedCard = Color.Unspecified,
        scrim = Color.Unspecified,
        scrimLight = Color.Unspecified,
        scrimMedium = Color.Unspecified,
        scrimHeavy = Color.Unspecified,
        onGradientText = Color.Unspecified,
        onGradientTextSubtle = Color.Unspecified,
        quaternary = Color.Unspecified,
        onQuaternary = Color.Unspecified,
        quaternaryContainer = Color.Unspecified,
        onQuaternaryContainer = Color.Unspecified,
        surfaceHover = Color.Unspecified,
        surfacePressed = Color.Unspecified,
        focusRing = Color.Unspecified,
    )
}

val HomeDecorExtra: HomeDecorExtraColors
    @Composable
    get() = LocalHomeDecorExtraColors.current

// ---------------------------------------------------------------------------
// Light color scheme
// ---------------------------------------------------------------------------

internal val LightColorScheme = lightColorScheme(
    primary = HomeDecorColors.Primary,
    onPrimary = HomeDecorColors.OnPrimary,
    primaryContainer = HomeDecorColors.PrimaryContainer,
    onPrimaryContainer = HomeDecorColors.OnPrimaryContainer,
    secondary = HomeDecorColors.Secondary,
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
    background = HomeDecorColors.Canvas,
    onBackground = HomeDecorColors.OnBackground,
    surface = HomeDecorColors.Paper,
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

// ---------------------------------------------------------------------------
// Dark color scheme
// ---------------------------------------------------------------------------

internal val DarkColorScheme = darkColorScheme(
    primary = HomeDecorColors.DarkPrimary,
    onPrimary = HomeDecorColors.DarkOnPrimary,
    primaryContainer = HomeDecorColors.DarkPrimaryContainer,
    onPrimaryContainer = HomeDecorColors.DarkOnPrimaryContainer,
    secondary = HomeDecorColors.DarkSecondary,
    onSecondary = HomeDecorColors.DarkOnSecondary,
    secondaryContainer = HomeDecorColors.DarkSecondaryContainer,
    onSecondaryContainer = HomeDecorColors.DarkOnSecondaryContainer,
    tertiary = HomeDecorColors.DarkTertiary,
    onTertiary = HomeDecorColors.DarkOnTertiary,
    tertiaryContainer = HomeDecorColors.DarkTertiaryContainer,
    onTertiaryContainer = HomeDecorColors.DarkOnTertiaryContainer,
    error = HomeDecorColors.DarkError,
    onError = HomeDecorColors.DarkOnError,
    errorContainer = HomeDecorColors.DarkErrorContainer,
    onErrorContainer = HomeDecorColors.DarkOnErrorContainer,
    background = HomeDecorColors.Dark,
    onBackground = HomeDecorColors.DarkOnBackground,
    surface = HomeDecorColors.DarkSurface,
    onSurface = HomeDecorColors.DarkOnSurface,
    onSurfaceVariant = HomeDecorColors.DarkOnSurfaceVariant,
    outline = HomeDecorColors.DarkOutline,
    outlineVariant = HomeDecorColors.DarkOutlineVariant,
    surfaceDim = HomeDecorColors.DarkSurfaceDim,
    surfaceBright = HomeDecorColors.DarkSurfaceBright,
    surfaceContainerLowest = HomeDecorColors.DarkSurfaceContainerLowest,
    surfaceContainerLow = HomeDecorColors.DarkSurfaceContainerLow,
    surfaceContainer = HomeDecorColors.DarkSurfaceContainer,
    surfaceContainerHigh = HomeDecorColors.DarkSurfaceContainerHigh,
    surfaceContainerHighest = HomeDecorColors.DarkSurfaceContainerHighest,
)

// ---------------------------------------------------------------------------
// Extra semantic tokens — light vs dark
// ---------------------------------------------------------------------------

internal val LightExtra = HomeDecorExtraColors(
    success = HomeDecorColors.Success,
    successContainer = HomeDecorColors.SuccessContainer,
    warning = HomeDecorColors.Warning,
    warningContainer = HomeDecorColors.WarningContainer,
    accent = HomeDecorColors.Accent,
    accentContainer = HomeDecorColors.AccentContainer,
    diamondAccent = HomeDecorColors.DiamondAccent,
    proAccent = HomeDecorColors.ProAccent,
    premiumGold = HomeDecorColors.PremiumGold,
    card = HomeDecorColors.Paper,
    elevatedCard = HomeDecorColors.SurfaceContainerLow,
    scrim = HomeDecorColors.Scrim,
    scrimLight = HomeDecorColors.ScrimLight,
    scrimMedium = HomeDecorColors.ScrimMedium,
    scrimHeavy = HomeDecorColors.ScrimHeavy,
    onGradientText = HomeDecorColors.OnGradientText,
    onGradientTextSubtle = HomeDecorColors.OnGradientTextSubtle,
    quaternary = HomeDecorColors.Quaternary,
    onQuaternary = HomeDecorColors.OnQuaternary,
    quaternaryContainer = HomeDecorColors.QuaternaryContainer,
    onQuaternaryContainer = HomeDecorColors.OnQuaternaryContainer,
    surfaceHover = Color(0x0D000000),   // 5% black for hover on light
    surfacePressed = Color(0x14000000),  // 8% black for pressed on light
    focusRing = HomeDecorColors.Primary,
)

internal val DarkExtra = HomeDecorExtraColors(
    success = HomeDecorColors.DarkSuccess,
    successContainer = HomeDecorColors.DarkSuccessContainer,
    warning = HomeDecorColors.DarkWarning,
    warningContainer = HomeDecorColors.DarkWarningContainer,
    accent = HomeDecorColors.DarkAccent,
    accentContainer = HomeDecorColors.DarkAccentContainer,
    diamondAccent = HomeDecorColors.DarkDiamondAccent,
    proAccent = HomeDecorColors.DarkProAccent,
    premiumGold = HomeDecorColors.DarkPremiumGold,
    card = HomeDecorColors.DarkSurface,
    elevatedCard = HomeDecorColors.DarkOverlay,
    scrim = HomeDecorColors.DarkScrim,
    scrimLight = HomeDecorColors.DarkScrimLight,
    scrimMedium = HomeDecorColors.DarkScrimMedium,
    scrimHeavy = HomeDecorColors.DarkScrimHeavy,
    onGradientText = HomeDecorColors.DarkOnGradientText,
    onGradientTextSubtle = HomeDecorColors.DarkOnGradientTextSubtle,
    quaternary = HomeDecorColors.DarkQuaternary,
    onQuaternary = HomeDecorColors.DarkOnQuaternary,
    quaternaryContainer = HomeDecorColors.DarkQuaternaryContainer,
    onQuaternaryContainer = HomeDecorColors.DarkOnQuaternaryContainer,
    surfaceHover = Color(0x14FFFFFF),   // 8% white for hover on dark
    surfacePressed = Color(0x1FFFFFFF), // 12% white for pressed on dark
    focusRing = HomeDecorColors.DarkPrimary,
)

// ---------------------------------------------------------------------------
// Internal helper used by platform actuals
// ---------------------------------------------------------------------------

@Composable
internal fun HomeDecorThemeInner(
    colorScheme: androidx.compose.material3.ColorScheme,
    content: @Composable () -> Unit,
) {
    val isDark = colorScheme == DarkColorScheme
    val extraColors = if (isDark) DarkExtra else LightExtra

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HomeDecorTypography,
        shapes = HomeDecorShapes,
    ) {
        CompositionLocalProvider(
            LocalDarkTheme provides isDark,
            LocalHomeDecorExtraColors provides extraColors,
        ) {
            content()
        }
    }
}

// ---------------------------------------------------------------------------
// Utility modifiers & helpers
// ---------------------------------------------------------------------------

fun Modifier.minimumTouchTarget(): Modifier = sizeIn(minWidth = 48.dp, minHeight = 48.dp)

fun Modifier.disabledSemantics(enabled: Boolean): Modifier =
    if (enabled) this else semantics { disabled() }

@Composable
fun studioStateContainer(selected: Boolean) =
    if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

@Composable
fun studioStateElevation(selected: Boolean) =
    if (selected) 2.dp else 0.dp

@Composable
fun studioStateBorder(selected: Boolean) =
    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant

@Composable
fun studioStateIconContainer(selected: Boolean) =
    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow

@Composable
fun studioStateIconContent(selected: Boolean) =
    if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

@Composable
fun studioPrimaryButtonColors() = ButtonDefaults.buttonColors(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
)

@Composable
fun studioSecondaryButtonColors() = ButtonDefaults.buttonColors(
    containerColor = MaterialTheme.colorScheme.secondaryContainer,
    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
)

@Composable
fun studioDestructiveButtonColors() = ButtonDefaults.buttonColors(
    containerColor = MaterialTheme.colorScheme.error,
    contentColor = MaterialTheme.colorScheme.onError,
    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
)

@Composable
fun studioProButtonColors() = ButtonDefaults.buttonColors(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
)

@Composable
fun studioTextButtonColors() = ButtonDefaults.textButtonColors(
    contentColor = MaterialTheme.colorScheme.primary,
    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
)

@Composable
fun studioIconButtonColors() = ButtonDefaults.textButtonColors(
    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
)
