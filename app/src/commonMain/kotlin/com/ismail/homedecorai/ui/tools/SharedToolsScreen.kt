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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Diamond
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
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
    onCredits: () -> Unit,
    onToolClick: (ToolItem) -> Unit,
) {
    val screenWidth = getScreenWidthDp()
    val columns = when {
        screenWidth >= 1000 -> 3
        screenWidth >= 640 -> 2
        else -> 1
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
            .background(MaterialTheme.colorScheme.background),
    ) {
        ToolsHeader(state = state, onCredits = onCredits)

        val chunkedTools = state.tools.chunked(columns)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.ScreenHorizontal,
                end = HomeDecorSpacing.ScreenHorizontal,
                top = HomeDecorSpacing.Base,
                bottom = navBarBottomPadding(additionalContentPadding = 140.dp),
            ),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
        ) {
            itemsIndexed(
                items = chunkedTools,
                key = { _, row -> row.joinToString(",") { it.id } },
            ) { rowIndex, rowTools ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    rowTools.forEach { tool ->
                        val toolIndex = state.tools.indexOf(tool)
                        ToolCard(
                            tool = tool,
                            toolIndex = toolIndex,
                            onClick = { onToolClick(tool) },
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
    state: ToolsScreenState,
    onCredits: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                Strings.navTools,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            Surface(
                onClick = onCredits,
                shape = CtaShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                modifier = Modifier.semantics {
                    contentDescription = Strings.a11yOpenDiamondStore
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
                        if (state.isPro) Strings.proUpper else "${state.diamonds}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun ToolCard(
    tool: ToolItem,
    toolIndex: Int,
    onClick: () -> Unit,
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
        0.45f to Color.Transparent,
        0.7f to Color.Black.copy(alpha = 0.25f),
        1.0f to Color.Black.copy(alpha = 0.7f),
    )

    Surface(
        onClick = onClick,
        shape = CardShape,
        color = Color.Transparent,
        shadowElevation = hoverElevation.dp,
        interactionSource = interactionSource,
        modifier = modifier
            .height(220.dp)
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .clip(CardShape)
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
                    .padding(horizontal = 16.dp, vertical = 14.dp),
            ) {
                Text(
                    title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    description,
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(10.dp))
                Surface(
                    shape = CtaShape,
                    color = Color.White.copy(alpha = 0.18f),
                    modifier = Modifier
                        .widthIn(min = 110.dp)
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.3f),
                            shape = CtaShape,
                        ),
                ) {
                    Row(
                        Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            Strings.tryThis,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}
