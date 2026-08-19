package com.ismail.homedecorai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Redo
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.theme.HomeDecorExtra
import com.ismail.homedecorai.ui.theme.HomeDecorIconSize
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing

// ---------------------------------------------------------------------------
// Mask data model
// ---------------------------------------------------------------------------

/**
 * Re-exports from MaskEditor.kt — kept here so this file's composables
 * can reference MaskStroke, MaskTool, MaskEditorState without importing.
 */

// ---------------------------------------------------------------------------
// MaskToolButton — brush / eraser toggle
// ---------------------------------------------------------------------------

@Composable
fun MaskToolButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    val bgColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceContainerLow

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "maskToolButtonScale",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = when {
            isSelected -> MaterialTheme.colorScheme.primary
            isHovered -> MaterialTheme.colorScheme.surfaceContainerHigh
            else -> MaterialTheme.colorScheme.surfaceContainerLow
        },
        interactionSource = interactionSource,
        modifier = modifier
            .scale(scale)
            .testTag(testTag)
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = label
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = contentColor,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// UndoRedoButton
// ---------------------------------------------------------------------------

@Composable
fun UndoRedoButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .testTag(testTag)
            .semantics {
                contentDescription = label
            },
        colors = IconButtonDefaults.iconButtonColors(
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        ),
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(HomeDecorIconSize.Medium),
            tint = if (enabled)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        )
    }
}

// ---------------------------------------------------------------------------
// ClearMaskButton
// ---------------------------------------------------------------------------

@Composable
fun ClearMaskButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "clearMaskButtonScale",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isHovered)
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f)
        else
            MaterialTheme.colorScheme.errorContainer,
        interactionSource = interactionSource,
        modifier = modifier
            .scale(scale)
            .testTag(Strings.TestTags.wizardMaskClear)
            .semantics {
                role = Role.Button
                contentDescription = Strings.wizardMaskClear
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                Icons.Rounded.Delete,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Small),
                tint = MaterialTheme.colorScheme.onErrorContainer,
            )
            Text(
                Strings.wizardMaskClear,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// BrushSizeControl
// ---------------------------------------------------------------------------

/**
 * Horizontal slider for selecting brush size.
 *
 * Shows the current brush size as a circular preview on the left,
 * a labeled slider in the middle, and the numeric value on the right.
 */
@Composable
fun BrushSizeControl(
    brushSize: Float,
    onSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedSize by animateFloatAsState(
        targetValue = brushSize,
        animationSpec = spring(),
        label = "brushSize",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Brush size preview circle
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                modifier = Modifier.size((animatedSize / MaskEditorState.MAX_BRUSH_SIZE * 28f).dp.coerceAtLeast(6.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), CircleShape),
                )
            }
        }

        // Slider
        Slider(
            value = brushSize,
            onValueChange = onSizeChange,
            valueRange = MaskEditorState.MIN_BRUSH_SIZE..MaskEditorState.MAX_BRUSH_SIZE,
            modifier = Modifier
                .weight(1f)
                .testTag(Strings.TestTags.wizardMaskBrushSize),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
            ),
        )

        // Numeric label
        Text(
            "${brushSize.toInt()}px",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.End,
        )
    }
}

// ---------------------------------------------------------------------------
// MaskOpacityControl
// ---------------------------------------------------------------------------

/**
 * Horizontal slider for controlling mask opacity.
 */
@Composable
fun MaskOpacityControl(
    opacity: Float,
    onOpacityChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Opacity preview dot
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF444444).copy(alpha = opacity * 0.6f),
                modifier = Modifier.size(20.dp),
                content = {},
            )
        }

        // Slider
        Slider(
            value = opacity,
            onValueChange = onOpacityChange,
            valueRange = 0.1f..1f,
            modifier = Modifier
                .weight(1f)
                .testTag(Strings.TestTags.wizardMaskOpacity),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
            ),
        )

        // Numeric label
        Text(
            "${(opacity * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.End,
        )
    }
}

// ---------------------------------------------------------------------------
// MaskCoverageIndicator
// ---------------------------------------------------------------------------

/**
 * Shows current mask coverage with a label and percentage.
 *
 * - Before any mask: "Paint over the object you want to replace."
 * - After mask: "Mask the object completely — 12%"
 */
@Composable
fun MaskCoverageIndicator(
    hasMask: Boolean,
    coveragePercent: String,
    coverageRatio: Float,
    modifier: Modifier = Modifier,
) {
    val indicatorColor = when {
        !hasMask -> MaterialTheme.colorScheme.onSurface
        coverageRatio >= 0.05f -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.tertiary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Coverage dot
        Surface(
            shape = CircleShape,
            color = indicatorColor.copy(alpha = 0.2f),
            modifier = Modifier.size(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(indicatorColor, CircleShape),
            )
        }

        // Label
        Text(
            text = if (hasMask) {
                "${Strings.wizardMaskCoverageLabel} \u2014 $coveragePercent"
            } else {
                Strings.wizardMaskCoverageEmpty
            },
            style = MaterialTheme.typography.bodySmall,
            color = indicatorColor,
        )
    }
}

// ---------------------------------------------------------------------------
// MaskInstructionOverlay — first-use overlay
// ---------------------------------------------------------------------------

/**
 * Semi-transparent overlay shown on first use with a brush icon and
 * instruction text. Disappears after the user starts drawing.
 */
@Composable
fun MaskInstructionOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300, easing = LinearEasing)),
        exit = fadeOut(tween(200, easing = LinearEasing)),
        modifier = modifier,
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.55f),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss() }
                },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Brush icon
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        modifier = Modifier.size(56.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Create,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }

                    // Instruction text
                    Text(
                        Strings.wizardMaskInstruction,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )

                    // Hint
                    Text(
                        Strings.wizardMaskInstructionHint,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(4.dp))

                    // Dismiss label
                    Text(
                        Strings.wizardMaskInstructionDismiss,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.5f),
                    )
                }
            }
        }
    }
}

