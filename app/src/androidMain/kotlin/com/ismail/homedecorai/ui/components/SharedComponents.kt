package com.ismail.homedecorai.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.GeneratedResult
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.model.PendingPurchaseSync
import com.ismail.homedecorai.Project
import com.ismail.homedecorai.R
import com.ismail.homedecorai.model.isGeneratedResult
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.boardToolTitleRes
import com.ismail.homedecorai.ui.utility.choiceIcon
import com.ismail.homedecorai.ui.utility.choiceImageRes
import com.ismail.homedecorai.ui.utility.localizedOption
import com.ismail.homedecorai.ui.utility.replacementIcon
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 72.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f),
    )
}

@Composable
fun ToolToggle(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String = label,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        tonalElevation = studioStateElevation(selected),
        modifier = modifier
            .height(HomeDecorSpacing.TouchTarget)
            .border(1.dp, studioStateBorder(selected), CircleShape)
            .semantics {
                this.contentDescription = contentDescription
                this.selected = selected
                role = Role.Button
            },
    ) {
        Row(Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
            Text(label, color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun MaskActionButton(
    label: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = label,
    showLabel: Boolean = true,
) {
    val contentColor = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.52f)
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = HomeDecorShape.Badge,
        color = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.72f),
        tonalElevation = if (enabled) 1.dp else 0.dp,
        modifier = modifier
            .height(HomeDecorSpacing.TouchTarget)
            .border(1.dp, if (enabled) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), HomeDecorShape.Badge)
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Button
            }
            .disabledSemantics(enabled),
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(17.dp), tint = contentColor)
            if (showLabel) {
                Spacer(Modifier.width(6.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun styleSwatchColor(label: String): Color = when {
    label.contains("Moderne", ignoreCase = true) -> Color(0xFF1B5860)
    label.contains("Noël", ignoreCase = true) -> Color(0xFFC62828)
    label.contains("Luxe", ignoreCase = true) -> Color(0xFF7A5B10)
    label.contains("Japandi", ignoreCase = true) -> Color(0xFF8D6E63)
    label.contains("Zen", ignoreCase = true) -> Color(0xFF4A6B3E)
    label.contains("Jardin anglais", ignoreCase = true) -> Color(0xFF66BB6A)
    label.contains("Cyberpunk", ignoreCase = true) -> Color(0xFF7C4DFF)
    label.contains("Tropicale", ignoreCase = true) -> Color(0xFFFF7043)
    label.contains("Peach", ignoreCase = true) -> Color(0xFFFFCBA4)
    label.contains("Minimaliste", ignoreCase = true) -> Color(0xFF9E9E9E)
    label.contains("Marocain", ignoreCase = true) -> Color(0xFFD84315)
    label.contains("Scandinave", ignoreCase = true) -> Color(0xFFBCAAA4)
    label.contains("Bohème", ignoreCase = true) -> Color(0xFFA1887F)
    label.contains("Midcentury", ignoreCase = true) -> Color(0xFFE65100)
    label.contains("Art Deco", ignoreCase = true) -> Color(0xFF1A237E)
    label.contains("Côtier", ignoreCase = true) -> Color(0xFF4FC3F7)
    label.contains("Rustique", ignoreCase = true) -> Color(0xFF795548)
    label.contains("Vintage", ignoreCase = true) -> Color(0xFF8D6E63)
    label.contains("Méditerranéen", ignoreCase = true) -> Color(0xFF1565C0)
    label.contains("Glam", ignoreCase = true) -> Color(0xFFAD1457)
    label.contains("Campagne française", ignoreCase = true) -> Color(0xFFA5D6A7)
    label.contains("Marbre", ignoreCase = true) -> Color(0xFFF5F0EB)
    label.contains("Chêne", ignoreCase = true) || label.contains("Oak", ignoreCase = true) -> Color(0xFFC49A6C)
    label.contains("Noyer", ignoreCase = true) || label.contains("Walnut", ignoreCase = true) -> Color(0xFF5C4033)
    label.contains("Béton", ignoreCase = true) || label.contains("Concrete", ignoreCase = true) -> Color(0xFF9E9E9E)
    label.contains("Terrazzo", ignoreCase = true) -> Color(0xFFE8E0D0)
    label.contains("Tuile", ignoreCase = true) -> Color(0xFFB0BEC5)
    label.contains("Chevron", ignoreCase = true) -> Color(0xFF6D4C41)
    label.contains("Paysage", ignoreCase = true) -> Color(0xFF4CAF50)
    label.contains("Piscine", ignoreCase = true) -> Color(0xFF29B6F6)
    label.contains("Limewash", ignoreCase = true) -> Color(0xFFF5F0E8)
    label.contains("White Tile", ignoreCase = true) -> Color(0xFFF0F0F0)
    label.contains("Black Tile", ignoreCase = true) -> Color(0xFF2A2A2A)
    label.contains("Beige", ignoreCase = true) -> Color(0xFFD4C0A8)
    label.contains("Dark", ignoreCase = true) -> Color(0xFF3A3A3A)
    label.contains("Suggestion IA", ignoreCase = true) -> Color(0xFF7C4DFF)
    label.contains("Appartement", ignoreCase = true) -> Color(0xFF5C6BC0)
    label.contains("Maison", ignoreCase = true) -> Color(0xFF26A69A)
    label.contains("Immeuble", ignoreCase = true) -> Color(0xFF42A5F5)
    label.contains("Résidentiel", ignoreCase = true) -> Color(0xFF66BB6A)
    label.contains("Vente", ignoreCase = true) -> Color(0xFFEF5350)
    label.contains("Villa", ignoreCase = true) -> Color(0xFF26C6DA)
    label.contains("Patio", ignoreCase = true) -> Color(0xFF8D6E63)
    label.contains("Cour", ignoreCase = true) -> Color(0xFF66BB6A)
    label.contains("Terrasse", ignoreCase = true) -> Color(0xFFA1887F)
    else -> Color(0xFFB5DDE1)
}

@Composable
fun StyleChoiceCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavored: Boolean = false,
    onFavorite: (() -> Unit)? = null,
    favoriteLabel: String = "Favorite $label",
) {
    val displayLabel = localizedOption(label)
    val cardShape = HomeDecorShape.Large

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val targetScale = when {
        isPressed -> 0.97f
        isHovered -> 1.01f
        selected -> 0.98f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "styleCardScale",
    )

    val targetElevation = when {
        isHovered -> 4.dp
        selected -> 2.dp
        else -> 0.dp
    }
    val elevation by animateFloatAsState(
        targetValue = targetElevation.value,
        animationSpec = tween(durationMillis = 150, easing = LinearEasing),
        label = "styleCardElevation",
    )

    val borderColor = when {
        selected -> MaterialTheme.colorScheme.primary
        isHovered -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.40f)
    }
    val borderWidth = if (selected) 2.dp else 1.dp

    Surface(
        onClick = onClick,
        shape = cardShape,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.30f) else Color.White,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .border(borderWidth, borderColor, cardShape)
            .semantics {
                this.selected = selected
                contentDescription = displayLabel
                role = Role.RadioButton
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (label == "Suggestion IA") {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(24.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            } else {
                val isPeach = label.contains("Peach", ignoreCase = true)
                if (isPeach) {
                    // Peach: 24dp circle with border
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFCBA4))
                            .border(1.dp, Color(0xFFE0A878), CircleShape),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(styleSwatchColor(label))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f), RoundedCornerShape(6.dp)),
                    )
                }
            }

            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 17.sp),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1C1C),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (onFavorite != null) {
                Surface(
                    onClick = onFavorite,
                    shape = CircleShape,
                    color = Color.Transparent,
                    modifier = Modifier.size(24.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isFavored) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = favoriteLabel,
                            modifier = Modifier.size(16.dp),
                            tint = if (isFavored) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            if (selected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// MaterialThumbnailCard  –  Material swatch thumbnail with image + label
// ---------------------------------------------------------------------------
// Used for floor materials (Marble, Oak, Walnut, etc.) and wall materials
// (Limewash, White Tile, Black Tile, etc.). Shows the material image with
// a bottom scrim and label, consistent with StyleChoiceCard.
// ---------------------------------------------------------------------------

@Composable
fun MaterialThumbnailCard(
    label: String,
    imageRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavored: Boolean = false,
    onFavorite: (() -> Unit)? = null,
    favoriteLabel: String = "Favorite $label",
) {
    val displayLabel = localizedOption(label)
    val cardShape = HomeDecorShape.Large

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val targetScale = when {
        isPressed -> 0.97f
        isHovered -> 1.01f
        selected -> 0.98f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "materialCardScale",
    )

    val targetElevation = when {
        isHovered -> 4.dp
        selected -> 2.dp
        else -> 0.dp
    }
    val elevation by animateFloatAsState(
        targetValue = targetElevation.value,
        animationSpec = tween(durationMillis = 150, easing = LinearEasing),
        label = "materialCardElevation",
    )

    val borderColor = when {
        selected -> MaterialTheme.colorScheme.primary
        isHovered -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.40f)
    }
    val borderWidth = if (selected) 2.dp else 1.dp

    val swatch = materialSwatchSpec(label)

    Surface(
        onClick = onClick,
        shape = cardShape,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.30f) else Color.White,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .border(borderWidth, borderColor, cardShape)
            .semantics {
                this.selected = selected
                contentDescription = displayLabel
                role = Role.RadioButton
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(swatch.base)
                    .border(1.dp, swatch.accent.copy(alpha = 0.35f), RoundedCornerShape(6.dp)),
            )

            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 17.sp),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1C1C),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (onFavorite != null) {
                Surface(
                    onClick = onFavorite,
                    shape = CircleShape,
                    color = Color.Transparent,
                    modifier = Modifier.size(24.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isFavored) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = favoriteLabel,
                            modifier = Modifier.size(16.dp),
                            tint = if (isFavored) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            if (selected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveChoiceChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    val scale by animateFloatAsState(
        targetValue = if (selected) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "chipScale",
    )
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Badge,
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier
            .height(78.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .semantics {
                this.selected = selected
                contentDescription = displayLabel
            }
            .border(1.dp, studioStateBorder(selected), HomeDecorShape.Badge),
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(studioStateIconContainer(selected)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (selected) Icons.Rounded.Check else choiceIcon(label),
                    null,
                    Modifier.size(if (selected) 17.dp else 19.dp),
                    tint = studioStateIconContent(selected),
                )
            }
            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                style = if (displayLabel.length > 12) {
                    MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp, lineHeight = 18.sp)
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun IntensityChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    val containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val borderWidth = if (selected) 2.dp else 1.dp
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Badge,
        color = containerColor,
        modifier = modifier
            .height(78.dp)
            .semantics {
                this.selected = selected
                contentDescription = displayLabel
            }
            .border(borderWidth, borderColor, HomeDecorShape.Badge),
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (selected) Icons.Rounded.Check else choiceIcon(label),
                    null,
                    Modifier.size(if (selected) 17.dp else 19.dp),
                    tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                style = if (displayLabel.length > 12) {
                    MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp, lineHeight = 18.sp)
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ReplaceSuggestionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    val shape = HomeDecorShape.Card
    val containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
    Surface(
        onClick = onClick,
        shape = shape,
        color = containerColor,
        tonalElevation = if (selected) 4.dp else 0.dp,
        modifier = modifier
            .height(64.dp)
            .semantics {
                this.selected = selected
                contentDescription = displayLabel
            }
            .border(if (selected) 2.dp else 1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant, shape),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                if (selected) Icons.Rounded.Check else replacementIcon(label),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (selected) Color.White else MaterialTheme.colorScheme.primary,
            )
            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                color = contentColor,
                style = if (displayLabel.length > 12) {
                    MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp, lineHeight = 18.sp)
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ModeCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.ExtraExtraLarge,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier
            .height(188.dp)
            .shadow(1.dp, HomeDecorShape.ExtraExtraLarge)
            .semantics {
                this.selected = selected
                contentDescription = "$title. $description"
            }
            .border(
            if (selected) 2.dp else 1.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.40f),
            HomeDecorShape.ExtraExtraLarge,
        ),
    ) {
        Box {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow) {
                    Icon(
                        if (title.contains("Renovation")) Icons.Rounded.AutoAwesome else Icons.Rounded.Brush,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp).size(22.dp),
                        tint = if (selected) Color.White else MaterialTheme.colorScheme.primary,
                    )
                }
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 2)
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (selected) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp).size(14.dp),
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
fun DailyRewardCard(
    state: HomeDecorUiState,
    onClaim: () -> Boolean,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
) {
    val isDark = isSystemInDarkTheme()
    val useDark = dark || isDark
    val reward = state.workspace.dailyReward
    val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
    val claimedToday = reward.lastClaimEpochDay == today
    val activeStreak = when (reward.lastClaimEpochDay) {
        today, today - 1 -> reward.currentStreak.coerceAtLeast(if (claimedToday) 1 else 0)
        else -> 0
    }
    val displayDay = if (claimedToday) activeStreak.coerceAtLeast(1) else (activeStreak + 1).coerceAtLeast(1)

    val containerColor = if (useDark) {
        MaterialTheme.colorScheme.surfaceContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val titleColor = MaterialTheme.colorScheme.onSurface
    val subtitleColor = MaterialTheme.colorScheme.onSurfaceVariant
    val iconBg = if (useDark) {
        HomeDecorExtra.diamondAccent.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
    }
    val iconTint = if (useDark) {
        HomeDecorExtra.diamondAccent
    } else {
        MaterialTheme.colorScheme.primary
    }
    val dotActiveColor = if (useDark) {
        HomeDecorExtra.diamondAccent
    } else {
        MaterialTheme.colorScheme.primary
    }
    val dotInactiveColor = if (useDark) {
        MaterialTheme.colorScheme.surfaceContainerHighest
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    // Sparkle animation for unclaimed state
    val infiniteTransition = rememberInfiniteTransition(label = "sparkle")
    val sparkleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "sparkleAlpha",
    )
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "iconScale",
    )

    val cardShape = HomeDecorShape.CardLarge
    ElevatedCard(
        shape = cardShape,
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            ) {
                // Diamond icon with sparkle effect
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        shape = HomeDecorShape.Medium,
                        color = iconBg,
                    ) {
                        Box(Modifier.padding(10.dp)) {
                            Icon(
                                Icons.Rounded.Diamond,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(22.dp)
                                    .graphicsLayer {
                                        if (!claimedToday) {
                                            scaleX = iconScale
                                            scaleY = iconScale
                                        }
                                    },
                                tint = iconTint,
                            )
                        }
                    }
                    // Sparkle icons when unclaimed
                    if (!claimedToday) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier
                                .size(12.dp)
                                .offset(x = 18.dp, y = (-18).dp)
                                .graphicsLayer { alpha = sparkleAlpha },
                            tint = dotActiveColor.copy(alpha = 0.7f),
                        )
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier
                                .size(8.dp)
                                .offset(x = (-14).dp, y = 16.dp)
                                .graphicsLayer { alpha = sparkleAlpha * 0.7f },
                            tint = dotActiveColor.copy(alpha = 0.5f),
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        if (claimedToday) stringResource(R.string.daily_reward_claimed_title) else stringResource(R.string.daily_reward_title),
                        color = titleColor,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        if (claimedToday) stringResource(R.string.daily_reward_claimed_subtitle) else stringResource(R.string.daily_reward_subtitle_new),
                        color = subtitleColor,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.height(HomeDecorSpacing.Md))

            Row(
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(7) { index ->
                    val isFilled = index < displayDay.coerceAtMost(7)
                    val dotScale by animateFloatAsState(
                        targetValue = if (isFilled) 1f else 0.8f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                        label = "dotScale",
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .graphicsLayer {
                                scaleX = dotScale
                                scaleY = dotScale
                            }
                            .clip(CircleShape)
                            .background(if (isFilled) dotActiveColor else dotInactiveColor)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                DailyRewardQuietPill(
                    label = if (activeStreak > 0) {
                        stringResource(R.string.daily_reward_streak_format, activeStreak)
                    } else {
                        stringResource(R.string.daily_reward_soft_start)
                    },
                    bgColor = if (useDark) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainerLow,
                    textColor = subtitleColor,
                )
            }

            Spacer(modifier = Modifier.height(HomeDecorSpacing.Md))

            if (claimedToday) {
                Surface(
                    shape = HomeDecorShape.Button,
                    color = if (useDark) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.TouchTarget),
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = dotActiveColor,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            stringResource(R.string.daily_reward_come_back),
                            color = subtitleColor,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            } else {
                Button(
                    onClick = { onClaim() },
                    shape = HomeDecorShape.Button,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (useDark) HomeDecorExtra.diamondAccent else MaterialTheme.colorScheme.primary,
                        contentColor = if (useDark) HomeDecorColors.OnPrimaryContainer else MaterialTheme.colorScheme.onPrimary,
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.TouchTarget),
                ) {
                    Icon(
                        Icons.Rounded.Diamond,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        stringResource(R.string.daily_reward_claim),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
fun DailyRewardQuietPill(
    label: String,
    dark: Boolean = isSystemInDarkTheme(),
    bgColor: Color = if (dark) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainerLow,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Surface(
        shape = HomeDecorShape.Badge,
        color = bgColor,
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun CreditPill(
    state: HomeDecorUiState,
    compact: Boolean,
    onClick: (() -> Unit)? = null,
) {
    val balanceLabel = if (state.isPro) stringResource(R.string.pro_upper) else stringResource(R.string.diamonds_count, state.diamonds)
    val pillDescription = if (onClick != null) {
        stringResource(R.string.a11y_open_diamond_store_balance, balanceLabel)
    } else {
        balanceLabel
    }
    Surface(
        onClick = { onClick?.invoke() },
        shape = CircleShape,
        color = if (state.isPro) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .minimumTouchTarget()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
            .semantics { contentDescription = pillDescription },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (compact) 8.dp else 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.Rounded.Diamond, contentDescription = null, Modifier.size(17.dp), tint = if (state.isPro) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary)
            Text(if (state.isPro) stringResource(R.string.pro_upper) else "${state.diamonds}", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ReferenceImagePicker(
    selectedUri: Uri?,
    selectedExample: String?,
    selectedImageRes: Int,
    onImport: () -> Unit,
    onExample: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.padding(8.dp).size(18.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.reference_picker_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.reference_picker_body), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Surface(
            shape = HomeDecorShape.ExtraExtraLarge,
            color = studioStateContainer(selectedUri != null || selectedExample != null),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().border(1.dp, studioStateBorder(selectedUri != null || selectedExample != null), HomeDecorShape.ExtraExtraLarge),
        ) {
            Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(Modifier.size(78.dp).clip(RoundedCornerShape(18.dp))) {
                    UriOrResourceImage(
                        uri = selectedUri,
                        imageRes = selectedImageRes,
                        contentDescription = stringResource(R.string.style_reference),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (selectedUri != null || selectedExample != null) stringResource(R.string.reference_added) else stringResource(R.string.no_reference), fontWeight = FontWeight.SemiBold)
                    Text(
                        if (selectedUri != null || selectedExample != null) stringResource(R.string.reference_picker_selected_body) else stringResource(R.string.reference_picker_empty_body),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Icon(Icons.Rounded.Check, null, tint = if (selectedUri != null || selectedExample != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onImport, shape = CircleShape, modifier = Modifier.weight(1f).height(HomeDecorSpacing.TouchTarget)) {
                Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.import_action))
            }
            OutlinedButton(onClick = onExample, shape = CircleShape, modifier = Modifier.weight(1f).height(HomeDecorSpacing.TouchTarget)) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.example))
            }
        }
    }
}

@Composable
fun ProjectHeaderPreview(
    project: Project,
    results: List<GeneratedResult>,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            WorkspaceImage(
                imageUrl = project.coverImageUrl,
                imageUri = project.coverImageUri ?: project.originalPhotoUris.firstOrNull(),
                contentDescription = project.name,
                modifier = Modifier.size(64.dp).clip(HomeDecorShape.ImageLarge),
            )
            Column(Modifier.weight(1f)) {
                Text(
                    project.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    project.roomType.ifBlank { "No room type" },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (results.isNotEmpty()) {
                    Text(
                        "${results.size} result${if (results.size > 1) "s" else ""}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectMetricChip(
    icon: ImageVector,
    text: String,
) {
    Surface(
        shape = HomeDecorShape.Medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurface)
            Text(text, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ProjectResultThumb(
    title: String,
    imageUrl: String?,
    imageUri: String?,
    imageRes: Int = 0,
) {
    Surface(
        shape = HomeDecorShape.Medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.size(100.dp),
    ) {
        Box {
            WorkspaceImage(
                imageUrl = imageUrl,
                imageUri = imageUri,
                imageRes = imageRes,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
            )
            Surface(
                shape = RoundedCornerShape(bottomStart = 14.dp),
                color = HomeDecorExtra.scrim.copy(alpha = 0.54f),
                modifier = Modifier.align(Alignment.BottomStart),
            ) {
                Text(
                    title,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun PurchaseSyncNotice(
    message: String,
    pending: Boolean,
    busy: Boolean,
    onRetry: () -> Unit,
) {
    Surface(
        shape = HomeDecorShape.Badge,
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.Rounded.Error, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error)
            Text(
                message,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
            if (pending) {
                OutlinedButton(
                    onClick = onRetry,
                    enabled = !busy,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(HomeDecorSpacing.TouchTarget),
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.retry), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
