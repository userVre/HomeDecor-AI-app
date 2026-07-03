package com.ismail.homedecorai.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.theme.HomeDecorElevation
import com.ismail.homedecorai.ui.theme.HomeDecorExtra
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing
import com.ismail.homedecorai.ui.theme.HomeDecorStateLayers

// ---------------------------------------------------------------------------
// MD3 Expressive Buttons  –  Web-first with hover/pressed/focus states
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed && enabled -> 0.97f
            else -> 1f
        },
        animationSpec = tween(100),
        label = "btnScale",
    )

    val elevation by animateDpAsState(
        targetValue = when {
            isPressed && enabled -> HomeDecorElevation.Level2
            isHovered && enabled -> HomeDecorElevation.Level1
            else -> HomeDecorElevation.Level0
        },
        animationSpec = tween(150),
        label = "btnElevation",
    )

    Button(
        onClick = { if (!loading) onClick() },
        modifier = modifier
            .heightIn(min = HomeDecorSpacing.ButtonHeightMedium)
            .scale(scale)
            .semantics { role = Role.Button },
        enabled = enabled && !loading,
        shape = HomeDecorShape.Button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                alpha = HomeDecorStateLayers.DisabledContainer
            ),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                alpha = HomeDecorStateLayers.DisabledContent
            ),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = HomeDecorElevation.Level2,
            disabledElevation = HomeDecorElevation.Level0,
        ),
        contentPadding = PaddingValues(
            horizontal = HomeDecorSpacing.Xl,
            vertical = HomeDecorSpacing.Sm,
        ),
        interactionSource = interactionSource,
        content = {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(18.dp)
                        .semantics { contentDescription = Strings.a11yLoading },
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            content()
        },
    )
}

@Composable
fun HomeDecorTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed && enabled -> 0.97f
            else -> 1f
        },
        animationSpec = tween(100),
        label = "btnScale",
    )

    Button(
        onClick = { if (!loading) onClick() },
        modifier = modifier
            .heightIn(min = HomeDecorSpacing.ButtonHeightMedium)
            .scale(scale)
            .semantics { role = Role.Button },
        enabled = enabled && !loading,
        shape = HomeDecorShape.Button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(
                alpha = HomeDecorStateLayers.DisabledContainer
            ),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                alpha = HomeDecorStateLayers.DisabledContent
            ),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = HomeDecorElevation.Level0,
            pressedElevation = HomeDecorElevation.Level1,
            disabledElevation = HomeDecorElevation.Level0,
        ),
        contentPadding = PaddingValues(
            horizontal = HomeDecorSpacing.Xl,
            vertical = HomeDecorSpacing.Sm,
        ),
        interactionSource = interactionSource,
        content = {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(18.dp)
                        .semantics { contentDescription = Strings.a11yLoading },
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            content()
        },
    )
}

@Composable
fun HomeDecorOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed && enabled -> 0.97f
            else -> 1f
        },
        animationSpec = tween(100),
        label = "btnScale",
    )

    OutlinedButton(
        onClick = { if (!loading) onClick() },
        modifier = modifier
            .heightIn(min = HomeDecorSpacing.ButtonHeightMedium)
            .scale(scale)
            .semantics { role = Role.Button },
        enabled = enabled && !loading,
        shape = HomeDecorShape.Button,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                alpha = HomeDecorStateLayers.DisabledContent
            ),
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = HomeDecorStateLayers.DisabledOutline)
                isHovered -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                else -> MaterialTheme.colorScheme.outline
            },
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = HomeDecorElevation.Level0,
            pressedElevation = HomeDecorElevation.Level1,
            disabledElevation = HomeDecorElevation.Level0,
        ),
        contentPadding = PaddingValues(
            horizontal = HomeDecorSpacing.Xl,
            vertical = HomeDecorSpacing.Sm,
        ),
        interactionSource = interactionSource,
        content = {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(18.dp)
                        .semantics { contentDescription = Strings.a11yLoading },
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            content()
        },
    )
}

@Composable
fun HomeDecorTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        enabled = enabled,
        shape = HomeDecorShape.Button,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                alpha = HomeDecorStateLayers.DisabledContent
            ),
        ),
        contentPadding = PaddingValues(
            horizontal = HomeDecorSpacing.Sm,
            vertical = HomeDecorSpacing.Xs,
        ),
        interactionSource = remember { MutableInteractionSource() },
        content = content,
    )
}

// ---------------------------------------------------------------------------
// MD3 Expressive Cards  –  with proper elevation hierarchy
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: Dp = HomeDecorElevation.Level0,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedElevation by animateDpAsState(
        targetValue = when {
            isPressed -> HomeDecorElevation.Level3
            isHovered && onClick != null -> HomeDecorElevation.Level2
            else -> elevation
        },
        animationSpec = tween(150),
        label = "cardElevation",
    )

    val containerColor = MaterialTheme.colorScheme.surface

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .clip(HomeDecorShape.Card)
                .hoverable(interactionSource = interactionSource),
            shape = HomeDecorShape.Card,
            color = containerColor,
            shadowElevation = animatedElevation,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            ),
            interactionSource = interactionSource,
            content = content,
        )
    } else {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(HomeDecorShape.Card),
            shape = HomeDecorShape.Card,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shadowElevation = animatedElevation,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
            ),
            content = content,
        )
    }
}

// ---------------------------------------------------------------------------
// MD3 Expressive Filter Chip  –  with animated states
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.onSurface.copy(
                alpha = HomeDecorStateLayers.DisabledContainer
            )
            selected -> MaterialTheme.colorScheme.primaryContainer
            isHovered -> MaterialTheme.colorScheme.surfaceContainerHigh
            isPressed -> MaterialTheme.colorScheme.surfaceContainerHighest
            else -> MaterialTheme.colorScheme.surfaceContainerLow
        },
        animationSpec = tween(150),
        label = "chipBg",
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.onSurface.copy(
                alpha = HomeDecorStateLayers.DisabledContent
            )
            selected -> MaterialTheme.colorScheme.onPrimaryContainer
            else -> MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(150),
        label = "chipContent",
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outlineVariant
        },
        animationSpec = tween(150),
        label = "chipBorder",
    )

    Surface(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = HomeDecorSpacing.ButtonHeightSmall)
            .hoverable(interactionSource = interactionSource)
            .semantics { role = Role.Checkbox },
        enabled = enabled,
        shape = if (selected) HomeDecorShape.ChipSelected else HomeDecorShape.Chip,
        color = backgroundColor,
        contentColor = contentColor,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = borderColor,
        ),
        interactionSource = interactionSource,
    ) {
        Box(modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Sm)) {
            label()
        }
    }
}

// ---------------------------------------------------------------------------
// MD3 Expressive Dialog Surface
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorDialogSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.clip(HomeDecorShape.Dialog),
        shape = HomeDecorShape.Dialog,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HomeDecorElevation.DialogElevation,
        content = content,
    )
}

// ---------------------------------------------------------------------------
// MD3 Expressive Bottom Sheet Surface
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorSheetSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.clip(HomeDecorShape.BottomSheet),
        shape = HomeDecorShape.BottomSheet,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HomeDecorElevation.SheetElevation,
        content = content,
    )
}

// ---------------------------------------------------------------------------
// MD3 Expressive Step Indicator  –  with connected lines and pulse
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorStepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (step in 0 until totalSteps) {
            val isActive = step == currentStep
            val isCompleted = step < currentStep

            val dotColor by animateColorAsState(
                targetValue = when {
                    isActive -> MaterialTheme.colorScheme.primary
                    isCompleted -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outlineVariant
                },
                animationSpec = tween(200),
                label = "stepDotColor",
            )

            val dotSize by animateDpAsState(
                targetValue = when {
                    isActive -> 14.dp
                    isCompleted -> 12.dp
                    else -> 10.dp
                },
                animationSpec = tween(200),
                label = "stepDotSize",
            )

            Box(
                modifier = Modifier
                    .sizeIn(minWidth = dotSize, minHeight = dotSize)
                    .clip(RoundedCornerShape(50))
                    .background(dotColor),
                contentAlignment = Alignment.Center,
            ) {
                if (isCompleted) {
                    Text(
                        text = "✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            if (step < totalSteps - 1) {
                val lineColor by animateColorAsState(
                    targetValue = if (isCompleted) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant,
                    animationSpec = tween(200),
                    label = "stepLineColor",
                )

                Box(
                    modifier = Modifier
                        .sizeIn(minWidth = 32.dp, minHeight = 3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(lineColor.copy(alpha = if (isCompleted) 0.4f else 0.3f)),
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// MD3 Expressive Scrim Overlay
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorScrim(
    modifier: Modifier = Modifier,
    alpha: Float = 0.4f,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.scrim.copy(alpha = alpha),
        onClick = onClick,
    ) {}
}

// ---------------------------------------------------------------------------
// MD3 Expressive Section Header  –  consistent across screens
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(horizontal = HomeDecorSpacing.ScreenHorizontal),
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.semantics { heading() },
            )
            action?.invoke()
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// MD3 Expressive Divider  –  subtle, purposeful
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color),
    )
}

// ---------------------------------------------------------------------------
// MD3 Expressive Badge  –  for counts, status
// ---------------------------------------------------------------------------

@Composable
fun HomeDecorBadge(
    count: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = MaterialTheme.colorScheme.onError,
) {
    Surface(
        modifier = modifier,
        shape = HomeDecorShape.Badge,
        color = color,
    ) {
        Text(
            text = if (count > 99) "99+" else count.toString(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        )
    }
}
