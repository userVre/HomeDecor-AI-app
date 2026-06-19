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
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    primary = Color(0xFF9CD0D3),
    onPrimary = Color(0xFF0A3335),
    primaryContainer = Color(0xFF2A4E50),
    onPrimaryContainer = Color(0xFFD8ECEF),
    secondary = Color(0xFFB4C8B8),
    onSecondary = Color(0xFF1E3322),
    secondaryContainer = Color(0xFF364E3E),
    onSecondaryContainer = Color(0xFFE0EDE2),
    tertiary = Color(0xFFCCB8DC),
    onTertiary = Color(0xFF342844),
    tertiaryContainer = Color(0xFF4C3E60),
    onTertiaryContainer = Color(0xFFF0E4F8),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF2B8B5),
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
    dynamicColor: Boolean = true,
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
