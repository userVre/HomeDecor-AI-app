package com.ismail.homedecorai.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// Extended semantic color tokens (beyond MD3 core scheme)
// ---------------------------------------------------------------------------

@Immutable
data class HomeDecorExtraColors(
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
)

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
    )
}

val HomeDecorExtra: HomeDecorExtraColors
    @Composable
    get() = LocalHomeDecorExtraColors.current

// ---------------------------------------------------------------------------
// Light color scheme  –  warm cream surfaces, teal primary, gold tertiary
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private val LightColorScheme = lightColorScheme(
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

// ---------------------------------------------------------------------------
// Dark color scheme  –  warm charcoal / deep green-gray, never pure black
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private val DarkColorScheme = darkColorScheme(
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
// Extra semantic tokens – light vs dark
// ---------------------------------------------------------------------------

private val LightExtra = HomeDecorExtraColors(
    success = HomeDecorColors.Success,
    successContainer = HomeDecorColors.SuccessContainer,
    warning = HomeDecorColors.Warning,
    warningContainer = HomeDecorColors.WarningContainer,
    accent = HomeDecorColors.Accent,
    accentContainer = HomeDecorColors.AccentContainer,
    diamondAccent = HomeDecorColors.DiamondAccent,
    proAccent = HomeDecorColors.ProAccent,  // teal
    premiumGold = HomeDecorColors.PremiumGold,
    card = HomeDecorColors.Paper,
    elevatedCard = HomeDecorColors.SurfaceContainerLow,
)

private val DarkExtra = HomeDecorExtraColors(
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
)

// ---------------------------------------------------------------------------
// Theme composable
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeDecorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extraColors = if (darkTheme) DarkExtra else LightExtra

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = HomeDecorTypography,
        shapes = HomeDecorShapes,
    ) {
        CompositionLocalProvider(LocalHomeDecorExtraColors provides extraColors) {
            content()
        }
    }
}

// ---------------------------------------------------------------------------
// Utility modifiers & helpers (kept for backward compatibility)
// ---------------------------------------------------------------------------

fun Modifier.minimumTouchTarget(): Modifier = sizeIn(minWidth = 48.dp, minHeight = 48.dp)

fun Modifier.disabledSemantics(enabled: Boolean): Modifier =
    if (enabled) this else semantics { disabled() }

@Composable
fun studioStateContainer(selected: Boolean): Color =
    if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow

@Composable
fun studioStateBorder(selected: Boolean): Color =
    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant

fun studioStateElevation(selected: Boolean): Dp = if (selected) 2.dp else 0.dp

@Composable
fun studioStateIconContainer(selected: Boolean): Color =
    if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow

@Composable
fun studioStateIconContent(selected: Boolean): Color =
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
