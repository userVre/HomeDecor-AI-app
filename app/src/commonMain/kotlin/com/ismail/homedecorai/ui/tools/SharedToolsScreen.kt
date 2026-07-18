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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.CircularProgressIndicator
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
import com.ismail.homedecorai.getScreenWidthDp
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.model.ToolsScreenState
import com.ismail.homedecorai.ui.components.ImageCard
import com.ismail.homedecorai.ui.components.ScrimIntensity
import com.ismail.homedecorai.ui.theme.*

// ---------------------------------------------------------------------------
// SharedToolsScreen  –  MD3 Expressive  |  Production Product Hub
// ---------------------------------------------------------------------------
// Responsive grid: 4 columns desktop, 2 tablet, 1 mobile.
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

    if (state.tools.isEmpty()) {
        ToolsEmptyContent()
        return
    }

    val screenWidthDp = getScreenWidthDp()
    val columns = when {
        screenWidthDp >= 1200 -> GridCells.Fixed(4)
        screenWidthDp >= 600 -> GridCells.Fixed(2)
        else -> GridCells.Fixed(1)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.toolsScreen),
    ) {
        ToolsHeader()

        LazyVerticalGrid(
            columns = columns,
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

    // Scrim intensity: paint/floor cards use stronger scrim for light backgrounds
    val scrimIntensity = if (tool.id == "paint" || tool.id == "floor")
        ScrimIntensity.Strong else ScrimIntensity.Standard

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
            // Full-bleed image via shared ImageCard
            ImageCard(
                imageUrl = tool.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = imageScale
                        scaleY = imageScale
                    },
                aspectRatio = 4f / 3f,
                scrimIntensity = scrimIntensity,
            )

            // Content overlaid at bottom
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
                // Pill CTA — styled text inside the Surface (no separate clickable)
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = Color(0xD9FFFFFF),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.Palette,
                            contentDescription = null,
                            modifier = Modifier.size(HomeDecorIconSize.Small),
                            tint = HomeDecorColors.Primary,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            Strings.toolCta(tool.id),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = HomeDecorColors.Primary,
                        )
                    }
                }
            }

            // Focus ring (keyboard navigation)
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
// Loading, Error & Empty States
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
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
                    .semantics { contentDescription = Strings.a11yLoading },
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
            Icon(
                imageVector = Icons.Rounded.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Xxl),
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
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

@Composable
private fun ToolsEmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Xxl),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(
                text = "No tools available",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(
                text = "Check back later for new tools",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
