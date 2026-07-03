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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Diamond
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.ismail.homedecorai.openUrl
import com.ismail.homedecorai.ui.discover.NetworkImage
import com.ismail.homedecorai.ui.theme.*

// ---------------------------------------------------------------------------
// SharedToolsScreen  –  MD3 Expressive  |  Production Product Hub
// ---------------------------------------------------------------------------
// Full-bleed image cards matching native app density.
// Responsive grid: 4 cols desktop, 3 medium, 2 tablet, 1 mobile.
// Each card: full-bleed image, gradient overlay, title, description, pill CTA.
// All 8 tools route to /create/{toolId} via onToolClick callback.
// ---------------------------------------------------------------------------

@Composable
fun SharedToolsScreen(
    state: ToolsScreenState,
    onToolClick: (ToolItem) -> Unit,
) {
    val screenWidth = getScreenWidthDp()
    val columns = when {
        screenWidth >= 1440 -> 4
        screenWidth >= 1024 -> 3
        screenWidth >= 600  -> 2
        else                -> 1
    }

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
        ToolsHeader(
            diamonds = state.diamonds,
            isPro = state.isPro,
            onCredits = { openUrl("https://homedecor-ai.com/diamonds") },
        )

        val horizontalPadding = when {
            columns >= 4 -> HomeDecorSpacing.Xxl
            columns >= 3 -> HomeDecorSpacing.Xl
            else         -> HomeDecorSpacing.ScreenHorizontal
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                top = HomeDecorSpacing.Sm,
                bottom = if (columns >= 4) HomeDecorSpacing.Xxl else HomeDecorSpacing.NavBarReservation,
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
// Header  –  Title + subtitle row with credits badge
// ---------------------------------------------------------------------------

@Composable
private fun ToolsHeader(
    diamonds: Int,
    isPro: Boolean,
    onCredits: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier.testTag(Strings.TestTags.toolsHeader),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = HomeDecorSpacing.ScreenHorizontal,
                    vertical = HomeDecorSpacing.Md,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    Strings.navTools,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
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

            // Credits badge — matches native app header
            val creditsDescription = Strings.a11yOpenDiamondStore
            Surface(
                onClick = onCredits,
                shape = HomeDecorShape.Pill,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .semantics {
                        contentDescription = creditsDescription
                        role = Role.Button
                    },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                    modifier = Modifier
                        .height(HomeDecorSpacing.TouchTarget)
                        .padding(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(
                        Icons.Rounded.Diamond,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = HomeDecorExtra.diamondAccent,
                    )
                    Text(
                        if (isPro) "PRO" else "$diamonds",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
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
    val gradientOverlay = Brush.verticalGradient(
        0.0f to Color.Transparent,
        0.35f to Color.Transparent,
        0.55f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.06f),
        0.72f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.14f),
        0.88f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.28f),
        1.0f to MaterialTheme.colorScheme.scrim.copy(alpha = 0.42f),
    )

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
                Text(
                    description,
                    color = Color.White.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(12.dp))
                // Pill CTA — matches native app style
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = tool.accentColor.copy(alpha = 0.65f),
                    modifier = Modifier.widthIn(min = 120.dp),
                ) {
                    Row(
                        Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            Strings.tryThis,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
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
