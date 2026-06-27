package com.ismail.homedecorai.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.ismail.homedecorai.ui.discover.NetworkImage
import com.ismail.homedecorai.ui.theme.*

private val CardShape = HomeDecorShape.CardLarge
private val CtaShape = HomeDecorShape.Button

@Composable
fun SharedToolsScreen(
    state: ToolsScreenState,
    onToolClick: (ToolItem) -> Unit,
) {
    val screenWidth = getScreenWidthDp()
    val isDesktop = screenWidth >= 1024
    val isTablet = screenWidth in 640..1023
    val columns = when {
        isDesktop -> 4
        else -> 2
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
            isDesktop = isDesktop,
            isTablet = isTablet,
        )

        val chunkedTools = state.tools.chunked(columns)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = if (isDesktop) HomeDecorSpacing.Lg else HomeDecorSpacing.ScreenHorizontal,
                end = if (isDesktop) HomeDecorSpacing.Lg else HomeDecorSpacing.ScreenHorizontal,
                top = HomeDecorSpacing.Base,
                bottom = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Lg,
            ),
            verticalArrangement = Arrangement.spacedBy(if (isDesktop) HomeDecorSpacing.Lg else HomeDecorSpacing.Md),
        ) {
            itemsIndexed(
                items = chunkedTools,
                key = { _, row -> row.joinToString(",") { it.id } },
            ) { rowIndex, rowTools ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(if (isDesktop) HomeDecorSpacing.Lg else HomeDecorSpacing.Md),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    rowTools.forEach { tool ->
                        val toolIndex = state.tools.indexOf(tool)
                        ToolCard(
                            tool = tool,
                            toolIndex = toolIndex,
                            onClick = { onToolClick(tool) },
                            isDesktop = isDesktop,
                            isTablet = isTablet,
                            modifier = if (columns > 1) Modifier.weight(1f) else Modifier.fillMaxWidth(),
                        )
                    }
                    if (columns > 1 && rowTools.size < columns) {
                        repeat(columns - rowTools.size) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

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

@Composable
fun ToolsHeader(
    isDesktop: Boolean = false,
    isTablet: Boolean = false,
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
                    horizontal = if (isDesktop) HomeDecorSpacing.Lg else HomeDecorSpacing.Base,
                    vertical = if (isDesktop) HomeDecorSpacing.Lg else HomeDecorSpacing.Md,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                Strings.navTools,
                style = when {
                    isDesktop -> MaterialTheme.typography.displaySmall
                    isTablet -> MaterialTheme.typography.headlineLarge
                    else -> MaterialTheme.typography.headlineMedium
                },
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun ToolCard(
    tool: ToolItem,
    toolIndex: Int,
    onClick: () -> Unit,
    isDesktop: Boolean = false,
    isTablet: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val title = Strings.toolTitle(tool.id)
    val description = Strings.toolDescription(tool.id)
    val toolCardDescription = Strings.a11yToolCard(title, description)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val pressScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessHigh,
        ),
        label = "toolCardScale",
    )
    val hoverElevation by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isHovered && !isPressed) 8f else 0f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow,
        ),
        label = "hoverElevation",
    )

    val textGradient = Brush.verticalGradient(
        0.0f to Color.Transparent,
        0.70f to Color.Transparent,
        1.0f to Color.Black.copy(alpha = 0.06f),
    )

    val cardHeight = when {
        isDesktop -> 440.dp
        isTablet -> 280.dp
        else -> 220.dp
    }
    val titleStyle = when {
        isDesktop -> MaterialTheme.typography.headlineLarge
        isTablet -> MaterialTheme.typography.headlineSmall
        else -> MaterialTheme.typography.titleMedium
    }
    val descStyle = when {
        isDesktop -> MaterialTheme.typography.titleMedium
        isTablet -> MaterialTheme.typography.bodyLarge
        else -> MaterialTheme.typography.bodySmall
    }
    val ctaStyle = when {
        isDesktop -> MaterialTheme.typography.titleLarge
        isTablet -> MaterialTheme.typography.titleMedium
        else -> MaterialTheme.typography.labelLarge
    }
    val ctaHPadding = when {
        isDesktop -> 48.dp
        isTablet -> 20.dp
        else -> 14.dp
    }
    val ctaVPadding = when {
        isDesktop -> 22.dp
        isTablet -> 12.dp
        else -> 10.dp
    }
    val ctaMinWidth = when {
        isDesktop -> 260.dp
        isTablet -> 140.dp
        else -> 110.dp
    }
    val ctaIconSize = when {
        isDesktop -> 22.dp
        isTablet -> 16.dp
        else -> 14.dp
    }
    val contentHPadding = when {
        isDesktop -> 32.dp
        isTablet -> 20.dp
        else -> 14.dp
    }
    val contentVPadding = when {
        isDesktop -> 28.dp
        isTablet -> 20.dp
        else -> 14.dp
    }

    Surface(
        onClick = onClick,
        shape = CardShape,
        color = Color.Transparent,
        shadowElevation = hoverElevation.dp,
        interactionSource = interactionSource,
        modifier = modifier
            .height(cardHeight)
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .clip(CardShape)
            .testTag(Strings.formatTestTag(Strings.TestTags.toolCard, tool.id))
            .semantics {
                contentDescription = toolCardDescription
                role = Role.Button
            },
    ) {
        Box {
            if (tool.imageUrl.isNotEmpty()) {
                NetworkImage(
                    url = tool.imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CardShape),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(listOf(tool.gradientStart, tool.gradientEnd))),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(textGradient),
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = contentHPadding, vertical = contentVPadding),
            ) {
                Text(
                    title,
                    color = Color.White,
                    style = titleStyle,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() },
                )
                Spacer(Modifier.height(if (isDesktop) 8.dp else 4.dp))
                Text(
                    description,
                    color = Color.White.copy(alpha = 0.92f),
                    style = descStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(if (isDesktop) 24.dp else if (isTablet) 14.dp else 10.dp))
                Surface(
                    shape = CtaShape,
                    color = Color.White.copy(alpha = 0.92f),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .widthIn(min = ctaMinWidth)
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.5f),
                            shape = CtaShape,
                        ),
                ) {
                    Row(
                        Modifier.padding(horizontal = ctaHPadding, vertical = ctaVPadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            Strings.tryThis,
                            color = HomeDecorColors.Accent,
                            style = ctaStyle,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(ctaIconSize),
                            tint = HomeDecorColors.Accent,
                        )
                    }
                }
            }
        }
    }
}
