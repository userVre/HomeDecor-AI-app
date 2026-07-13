package com.ismail.homedecorai.ui.tools

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.model.ToolsScreenState
import com.ismail.homedecorai.ui.discover.NetworkImage
import com.ismail.homedecorai.ui.theme.*

// ---------------------------------------------------------------------------
// SharedToolsScreen  –  MD3 Expressive  |  Production Product Hub
// ---------------------------------------------------------------------------
// Full-bleed image cards matching native app density.
// Responsive grid: GridCells.Adaptive(280.dp) auto-wraps across viewports.
// Each card: full-bleed image, gradient overlay, title, description, pill CTA.
// All 8 tools route to /create/{toolId} via onToolClick callback.
// ---------------------------------------------------------------------------

@Composable
fun SharedToolsScreen(
    state: ToolsScreenState,
    onToolClick: (ToolItem) -> Unit,
) {
    if (state.isLoading) {
        ToolsLoadingContent()
        return
    }

    if (state.error != null) {
        ToolsErrorContent(message = state.error)
        return
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.toolsScreen),
    ) {
        ToolsHeader()

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.ScreenHorizontal,
                end = HomeDecorSpacing.ScreenHorizontal,
                top = HomeDecorSpacing.Sm,
                bottom = HomeDecorSpacing.NavBarReservation,
            ),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
        ) {
            itemsIndexed(
                items = state.tools,
                key = { _, tool -> tool.id },
            ) { _, tool ->
                ToolCard(
                    tool = tool,
                    onClick = { onToolClick(tool) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Header  –  Title + subtitle
// ---------------------------------------------------------------------------

@Composable
private fun ToolsHeader() {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier.testTag(Strings.TestTags.toolsHeader),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = HomeDecorSpacing.ScreenHorizontal,
                    vertical = HomeDecorSpacing.Md,
                ),
        ) {
            Text(
                Strings.navTools,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() },
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xxs))
            Text(
                Strings.toolsSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Tool Card  –  Full-bleed image with gradient overlay (native parity)
// ---------------------------------------------------------------------------

@Composable
fun ToolCard(
    tool: ToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = Strings.toolTitle(tool.id)
    val description = Strings.toolDescription(tool.id)
    val toolCardDescription = Strings.a11yToolCard(title, description)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    var isFocused by remember { mutableStateOf(false) }

    // Press scale — bouncy micro-interaction
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh,
        ),
        label = "toolCardScale",
    )

    // Hover elevation — subtle lift on desktop
    val hoverElevation by animateDpAsState(
        targetValue = when {
            isPressed -> HomeDecorElevation.Level2
            isHovered -> HomeDecorElevation.Level1
            else      -> HomeDecorElevation.Level0
        },
        label = "hoverElevation",
    )

    // Image zoom on hover
    val imageScale by animateFloatAsState(
        targetValue = if (isHovered) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "imageScale",
    )

    // Gradient overlay — text readability on image (matches native app)
    // Cards with light photo backgrounds (paint, floor) need a stronger scrim.
    val needsStrongerScrim = tool.id == "paint" || tool.id == "floor"
    val gradientStops = if (needsStrongerScrim) {
        arrayOf(
            0.0f to Color.Transparent,
            0.55f to Color.Transparent,
            0.70f to Color.Black.copy(alpha = 0.25f),
            0.82f to Color.Black.copy(alpha = 0.50f),
            0.92f to Color.Black.copy(alpha = 0.72f),
            1.0f to Color.Black.copy(alpha = 0.85f),
        )
    } else {
        arrayOf(
            0.0f to Color.Transparent,
            0.55f to Color.Transparent,
            0.70f to Color.Black.copy(alpha = 0.15f),
            0.82f to Color.Black.copy(alpha = 0.35f),
            0.92f to Color.Black.copy(alpha = 0.52f),
            1.0f to Color.Black.copy(alpha = 0.65f),
        )
    }
    val gradientOverlay = Brush.verticalGradient(*gradientStops)

    Surface(
        onClick = onClick,
        shape = HomeDecorShape.CardLarge,
        color = Color.Transparent,
        shadowElevation = hoverElevation,
        interactionSource = interactionSource,
        modifier = modifier
            .clip(HomeDecorShape.CardLarge)
            .onFocusChanged { isFocused = it.isFocused }
            .testTag(Strings.formatTestTag(Strings.TestTags.toolCard, tool.id))
            .semantics {
                contentDescription = toolCardDescription
                role = Role.Button
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f),
        ) {
            // ── Full-bleed image ─────────────────────────────────────────
            if (tool.imageUrl.isNotEmpty()) {
                NetworkImage(
                    url = tool.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = imageScale
                            scaleY = imageScale
                        },
                )
            } else {
                // Gradient fallback when no image available
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(tool.gradientStart, tool.gradientEnd)
                            ),
                        ),
                )
            }

            // ── Gradient overlay for text readability ────────────────────
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientOverlay),
            )

            // ── Content overlaid at bottom ───────────────────────────────
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
            ) {
                Text(
                    title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() },
                )
                Spacer(Modifier.height(4.dp))
                // Subtitle with drop shadow for readability on any card background
                Box {
                    Text(
                        description,
                        color = Color.Black.copy(alpha = 0.55f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.7f),
                                offset = Offset(0f, 1.5f),
                                blurRadius = 4f,
                            ),
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        description,
                        color = Color(0xD9FFFFFF),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                val costNote = Strings.toolCostNote(tool.id)
                if (costNote.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        costNote,
                        color = Color(0xB3FFFFFF),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.height(12.dp))
                // Slim ElevatedButton CTA
                ElevatedButton(
                    onClick = {},
                    shape = HomeDecorShape.Badge,
                    colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xD9FFFFFF),
                        contentColor = HomeDecorColors.Primary,
                    ),
                    elevation = androidx.compose.material3.ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 0.dp,
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp),
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = HomeDecorColors.Primary,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        Strings.toolCta(tool.id),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            // ── Focus ring (keyboard navigation) ─────────────────────────
            if (isFocused) {
                val focusRingColor = MaterialTheme.colorScheme.primary.copy(
                    alpha = HomeDecorStateLayers.FocusRing
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            drawRect(
                                color = focusRingColor,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = 3.dp.toPx(),
                                ),
                            )
                        },
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Loading & Error States
// ---------------------------------------------------------------------------

@Composable
private fun ToolsLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            androidx.compose.material3.CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(
                text = Strings.loadingContent,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ToolsErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = Strings.errorGeneric,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
