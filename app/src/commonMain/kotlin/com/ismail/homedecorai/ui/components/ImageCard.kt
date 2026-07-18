package com.ismail.homedecorai.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.ui.theme.HomeDecorColors
import com.ismail.homedecorai.ui.theme.HomeDecorIconSize
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing
import com.ismail.homedecorai.ui.theme.HomeDecorExtra
import com.ismail.homedecorai.ui.discover.NetworkImage

// ---------------------------------------------------------------------------
// ImageCard  –  Shared image card with loading, error, retry, favorite,
//               selection, scrim, hover/press animations
// ---------------------------------------------------------------------------
// v2 — rebuilt per M3 spec:
//   - 16dp card radius (HomeDecorShape.Large)
//   - 16dp internal spacing
//   - 24–32dp desktop content padding
//   - Two columns tablet, one column mobile (caller controls grid)
//   - 150ms scale/elevation animation on hover and selection
//   - Bottom scrim for text readability
//   - Loading skeleton, error state with retry, favorite button
//   - Material 3 selection state (border + checkmark)
//   - Stable aspect ratio (default 4:3)
//   - Content description for accessibility
// ---------------------------------------------------------------------------

/** Scrim intensity presets for text readability on images. */
enum class ScrimIntensity {
    /** No scrim — for cards with text outside the image area. */
    None,
    /** Light scrim — dark images with white text. */
    Light,
    /** Standard scrim — default for most image cards. */
    Standard,
    /** Strong scrim — light/bright images (paint, floor) with white text. */
    Strong,
}

/** State of the image loading pipeline. */
enum class ImageState {
    /** No URL provided — show empty placeholder. */
    Empty,
    /** Image is loading — show skeleton. */
    Loading,
    /** Image failed to load — show error fallback. */
    Error,
    /** Image loaded successfully. */
    Loaded,
}

/**
 * A shared image card composable that handles all image display states.
 *
 * @param imageUrl URL of the image to display. Empty string shows empty placeholder.
 * @param contentDescription Accessibility description for the image.
 * @param modifier Modifier applied to the outer container.
 * @param aspectRatio Width-to-height ratio (default 4:3).
 * @param shape Corner shape (default HomeDecorShape.Large = 16dp).
 * @param imageState Current loading state. If null, inferred from imageUrl.
 * @param isSelected Whether the card is in selected state (border + check).
 * @param isFavored Whether the card is in favorite state (filled heart).
 * @param scrimIntensity Text readability scrim intensity behind content.
 * @param badge Optional badge text shown in top-right corner.
 * @param showMenu Whether to show the overflow action menu.
 * @param menuItems Menu items for the action dropdown.
 * @param onMenuAction Callback when a menu item is selected.
 * @param onRetry Callback when retry is tapped in error state.
 * @param onFavorite Callback when favorite button is tapped.
 * @param favoriteLabel Accessible label for the favorite button.
 * @param content Optional composable content overlaid on the image (at bottom).
 */
@Composable
fun ImageCard(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 4f / 3f,
    shape: androidx.compose.ui.graphics.Shape = HomeDecorShape.Large,
    imageState: ImageState? = null,
    isSelected: Boolean = false,
    isFavored: Boolean = false,
    scrimIntensity: ScrimIntensity = ScrimIntensity.Standard,
    badge: String? = null,
    showMenu: Boolean = false,
    menuItems: List<ImageCardMenuItem> = emptyList(),
    onMenuAction: ((ImageCardMenuItem) -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    onFavorite: (() -> Unit)? = null,
    favoriteLabel: String = "Favorite",
    content: @Composable (() -> Unit)? = null,
) {
    val resolvedState = imageState ?: when {
        imageUrl.isEmpty() -> ImageState.Empty
        else -> ImageState.Loaded
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    // 150ms scale animation on hover/press
    val targetScale = when {
        isPressed -> 0.97f
        isHovered -> 1.01f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "imageCardScale",
    )

    // 150ms elevation animation on hover/selection
    val targetElevation = when {
        isSelected -> 6.dp
        isHovered -> 3.dp
        else -> 0.dp
    }
    val elevation by animateFloatAsState(
        targetValue = targetElevation.value,
        animationSpec = tween(durationMillis = 150, easing = LinearEasing),
        label = "imageCardElevation",
    )

    // Border styling
    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isHovered -> MaterialTheme.colorScheme.outline
        else -> Color.Transparent
    }
    val borderWidth = if (isSelected) 2.dp else 0.dp

    Surface(
        onClick = { },
        shape = shape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = elevation.dp,
        interactionSource = interactionSource,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .border(borderWidth, borderColor, shape)
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
                role = Role.Button
                selected = isSelected
            },
    ) {
        Box {
            // ── Image / Placeholder / Error / Skeleton ──────────────────
            when (resolvedState) {
                ImageState.Loaded -> {
                    NetworkImage(
                        url = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(aspectRatio),
                    )
                }
                ImageState.Loading -> {
                    LoadingSkeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(aspectRatio),
                    )
                }
                ImageState.Error -> {
                    ErrorFallback(
                        label = contentDescription,
                        onRetry = onRetry,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(aspectRatio),
                    )
                }
                ImageState.Empty -> {
                    EmptyPlaceholder(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(aspectRatio),
                    )
                }
            }

            // ── Gradient scrim for text readability ─────────────────────
            if (scrimIntensity != ScrimIntensity.None && content != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(scrimBrush(scrimIntensity)),
                )
            }

            // ── Content slot (overlaid at bottom) ───────────────────────
            if (content != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    content()
                }
            }

            // ── Selected state: checkmark badge ─────────────────────────
            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Sm)
                        .size(24.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.White,
                        )
                    }
                }
            }

            // ── Favorite button (top-left) — 44dp touch target, 28dp visual ──
            if (onFavorite != null) {
                IconButton(
                    onClick = onFavorite,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(HomeDecorSpacing.Xs)
                        .size(HomeDecorSpacing.MinTouchTarget),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isFavored) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                contentDescription = favoriteLabel,
                                modifier = Modifier.size(HomeDecorIconSize.Small),
                                tint = if (isFavored) Color(0xFFE53935) else Color.White.copy(alpha = 0.9f),
                            )
                        }
                    }
                }
            }

            // ── Optional badge ──────────────────────────────────────────
            if (badge != null && !isSelected) {
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Sm),
                ) {
                    Text(
                        badge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(
                            horizontal = HomeDecorSpacing.Sm,
                            vertical = HomeDecorSpacing.Xs,
                        ),
                    )
                }
            }

            // ── Optional action menu ────────────────────────────────────
            if (showMenu && menuItems.isNotEmpty()) {
                var menuExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.align(Alignment.TopStart)) {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier
                            .padding(HomeDecorSpacing.Xs)
                            .size(HomeDecorSpacing.MinTouchTarget),
                    ) {
                        Icon(
                            Icons.Rounded.MoreVert,
                            contentDescription = "Actions",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(HomeDecorIconSize.Small),
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                    ) {
                        menuItems.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.label) },
                                onClick = {
                                    menuExpanded = false
                                    onMenuAction?.invoke(item)
                                },
                                leadingIcon = {
                                    Icon(
                                        item.icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(HomeDecorIconSize.Small),
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// ImageCardMenuItem  –  Data class for action menu items
// ---------------------------------------------------------------------------

data class ImageCardMenuItem(
    val id: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

// ---------------------------------------------------------------------------
// Loading skeleton  –  Animated shimmer placeholder
// ---------------------------------------------------------------------------

@Composable
private fun LoadingSkeleton(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeletonShimmer")
    val shimmerX by infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerX",
    )

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .drawWithContent {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent,
                        ),
                        start = Offset(shimmerX - 120f, 0f),
                        end = Offset(shimmerX + 120f, size.height),
                    ),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.Rounded.Image,
            contentDescription = null,
            modifier = Modifier.size(HomeDecorIconSize.Xl),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
        )
    }
}

// ---------------------------------------------------------------------------
// Error fallback  –  Broken image icon + label + retry button
// ---------------------------------------------------------------------------

@Composable
private fun ErrorFallback(
    label: String?,
    onRetry: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(HomeDecorSpacing.Base),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Rounded.BrokenImage,
            contentDescription = null,
            modifier = Modifier.size(HomeDecorIconSize.Xl),
            tint = MaterialTheme.colorScheme.error,
        )
        Spacer(Modifier.height(HomeDecorSpacing.Sm))
        Text(
            label ?: "Image unavailable",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (onRetry != null) {
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            Button(
                onClick = onRetry,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(32.dp),
            ) {
                Icon(
                    Icons.Rounded.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(HomeDecorIconSize.Small),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Retry",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Empty placeholder  –  Gradient background + image icon (no gray)
// ---------------------------------------------------------------------------

@Composable
private fun EmptyPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(
                    listOf(
                        HomeDecorColors.PrimaryContainer,
                        HomeDecorColors.TertiaryContainer,
                    )
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Rounded.Image,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Xxl),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            Text(
                "No image",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Scrim brush factory  –  Gradient overlays for text readability
// ---------------------------------------------------------------------------

@Composable
private fun scrimBrush(intensity: ScrimIntensity): Brush {
    val stops = when (intensity) {
        ScrimIntensity.None -> return Brush.verticalGradient(
            listOf(Color.Transparent, Color.Transparent),
        )
        ScrimIntensity.Light -> arrayOf(
            0.0f to Color.Transparent,
            0.55f to Color.Transparent,
            0.75f to Color.Black.copy(alpha = 0.15f),
            0.88f to Color.Black.copy(alpha = 0.35f),
            1.0f to Color.Black.copy(alpha = 0.50f),
        )
        ScrimIntensity.Standard -> arrayOf(
            0.0f to Color.Transparent,
            0.50f to Color.Transparent,
            0.68f to Color.Black.copy(alpha = 0.15f),
            0.82f to Color.Black.copy(alpha = 0.35f),
            0.92f to Color.Black.copy(alpha = 0.52f),
            1.0f to Color.Black.copy(alpha = 0.65f),
        )
        ScrimIntensity.Strong -> arrayOf(
            0.0f to Color.Transparent,
            0.30f to Color.Transparent,
            0.50f to Color.Black.copy(alpha = 0.18f),
            0.65f to Color.Black.copy(alpha = 0.38f),
            0.78f to Color.Black.copy(alpha = 0.60f),
            0.88f to Color.Black.copy(alpha = 0.80f),
            1.0f to Color.Black.copy(alpha = 0.92f),
        )
    }
    return Brush.verticalGradient(*stops)
}
