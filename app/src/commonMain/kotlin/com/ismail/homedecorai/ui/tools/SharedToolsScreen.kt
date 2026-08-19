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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.getScreenWidthDp
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.model.ToolsScreenState
import com.ismail.homedecorai.ui.MainLayout
import com.ismail.homedecorai.ui.components.ImageCard
import com.ismail.homedecorai.ui.components.ScrimIntensity
import com.ismail.homedecorai.ui.theme.AppTokens
import com.ismail.homedecorai.ui.theme.*

// ---------------------------------------------------------------------------
// Tool Categories  –  User-goal-oriented grouping
// ---------------------------------------------------------------------------

data class ToolCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val tools: List<String>,
)

val toolCategories = listOf(
    ToolCategory(
        id = "redesign",
        title = "Redesign my space",
        subtitle = "Transform entire room or exterior",
        icon = Icons.Rounded.AutoAwesome,
        tools = listOf("interior", "facade", "garden", "reference"),
    ),
    ToolCategory(
        id = "change",
        title = "Change one thing",
        subtitle = "Update specific element",
        icon = Icons.Rounded.SwapHoriz,
        tools = listOf("replace", "paint", "floor", "remove"),
    ),
    ToolCategory(
        id = "layout",
        title = "Improve my layout",
        subtitle = "Optimize placement and flow",
        icon = Icons.Rounded.Home,
        tools = listOf("layout"),
    ),
)

// ---------------------------------------------------------------------------
// Tool metadata  –  Extended info for each card
// ---------------------------------------------------------------------------

data class ToolMeta(
    val photoNeeded: String,
    val estimatedTime: String,
    val credits: String,
)

fun toolMeta(toolId: String): ToolMeta = when (toolId) {
    "interior" -> ToolMeta("Room photo", "~15–30 sec", "1 credit")
    "facade" -> ToolMeta("Exterior photo", "~15–30 sec", "1 credit")
    "garden" -> ToolMeta("Outdoor photo", "~15–30 sec", "1 credit")
    "paint" -> ToolMeta("Wall photo", "~10–20 sec", "1 credit")
    "floor" -> ToolMeta("Floor photo", "~10–20 sec", "1 credit")
    "layout" -> ToolMeta("Room photo", "~15–30 sec", "1 credit")
    "replace" -> ToolMeta("Furniture photo", "~20–40 sec", "1 credit")
    "remove" -> ToolMeta("Room photo", "~10–20 sec", "1 credit")
    "reference" -> ToolMeta("Room + reference", "~20–40 sec", "1 credit")
    else -> ToolMeta("Photo", "~15–30 sec", "1 credit")
}

// ---------------------------------------------------------------------------
// SharedToolsScreen  –  Filterable tool catalog
// ---------------------------------------------------------------------------
// Structure:
//   1. Title + subtitle header
//   2. Filter chips (All, Redesign, Change, Layout)
//   3. Filtered flat grid of tool cards + Pro card
// ---------------------------------------------------------------------------

private enum class ToolsFilter(val label: String) {
    All("All"),
    Redesign("Redesign"),
    Change("Change"),
    Layout("Layout"),
}

@Composable
fun SharedToolsScreen(
    state: ToolsScreenState,
    onToolClick: (ToolItem) -> Unit,
    onOpenPaywall: () -> Unit = {},
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

    var activeFilter by rememberSaveable { mutableStateOf(ToolsFilter.All) }

    val filteredTools = remember(state.tools, activeFilter) {
        when (activeFilter) {
            ToolsFilter.All -> state.tools
            ToolsFilter.Redesign -> state.tools.filter { it.id in listOf("interior", "facade", "garden", "reference") }
            ToolsFilter.Change -> state.tools.filter { it.id in listOf("replace", "paint", "floor", "remove") }
            ToolsFilter.Layout -> state.tools.filter { it.id == "layout" }
        }
    }

    val screenWidthDp = getScreenWidthDp()
    val isDesktop = screenWidthDp >= 1024

    MainLayout {
        Column(
            Modifier
                .fillMaxSize()
                .background(PremiumPalette.Surface)
                .testTag(Strings.TestTags.toolsScreen),
        ) {
            if (isDesktop) {
                DesktopToolsLayout(
                    filteredTools = filteredTools,
                    activeFilter = activeFilter,
                    onFilterChange = { activeFilter = it },
                    onToolClick = onToolClick,
                    onOpenPaywall = onOpenPaywall,
                )
            } else {
                MobileToolsLayout(
                    filteredTools = filteredTools,
                    activeFilter = activeFilter,
                    onFilterChange = { activeFilter = it },
                    onToolClick = onToolClick,
                    onOpenPaywall = onOpenPaywall,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Desktop Layout  –  Title + filters + grid
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DesktopToolsLayout(
    filteredTools: List<ToolItem>,
    activeFilter: ToolsFilter,
    onFilterChange: (ToolsFilter) -> Unit,
    onToolClick: (ToolItem) -> Unit,
    onOpenPaywall: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = AppTokens.SectionGap),
    ) {
        ToolsHeader()
        ToolsFilterRow(activeFilter = activeFilter, onFilterChange = onFilterChange)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = AppTokens.ItemGap,
                end = AppTokens.ItemGap,
                bottom = HomeDecorSpacing.NavBarReservation,
            ),
            horizontalArrangement = Arrangement.spacedBy(AppTokens.ItemGap),
            verticalArrangement = Arrangement.spacedBy(AppTokens.ItemGap),
        ) {
            itemsIndexed(
                items = filteredTools + listOf(
                    ToolItem(
                        id = "pro",
                        title = "Upgrade to Pro",
                        description = "Unlock unlimited generations, priority queue, and premium styles",
                        imageUrl = "",
                        gradientStart = Color(0xFF6C63FF),
                        gradientEnd = Color(0xFF3F51B5),
                        accentColor = Color(0xFFE8EAF6),
                    )
                ),
                key = { _, item -> item.id },
            ) { _, tool ->
                if (tool.id == "pro") {
                    ProUpgradeCard(onClick = onOpenPaywall)
                } else {
                    ToolInfoCard(
                        tool = tool,
                        onClick = { onToolClick(tool) },
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Mobile Layout  –  Title + filters + grid
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MobileToolsLayout(
    filteredTools: List<ToolItem>,
    activeFilter: ToolsFilter,
    onFilterChange: (ToolsFilter) -> Unit,
    onToolClick: (ToolItem) -> Unit,
    onOpenPaywall: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = HomeDecorSpacing.NavBarReservation,
            top = AppTokens.SectionGap,
        ),
        verticalArrangement = Arrangement.spacedBy(AppTokens.ItemGap),
    ) {
        item {
            ToolsHeader()
        }
        item {
            ToolsFilterRow(activeFilter = activeFilter, onFilterChange = onFilterChange)
        }
        item {
            ToolsFlatGrid(
                tools = filteredTools,
                onToolClick = onToolClick,
                onOpenPaywall = onOpenPaywall,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Header  –  "Tools" title + subtitle
// ---------------------------------------------------------------------------

@Composable
private fun ToolsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTokens.ItemGap, vertical = HomeDecorSpacing.Base),
    ) {
        Text(
            "Tools",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(HomeDecorSpacing.Xxs))
        Text(
            "All AI tools for your home",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------
// Filter Row  –  All, Redesign, Change, Layout
// ---------------------------------------------------------------------------

@Composable
private fun ToolsFilterRow(
    activeFilter: ToolsFilter,
    onFilterChange: (ToolsFilter) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = AppTokens.ItemGap),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(ToolsFilter.entries.size) { index ->
            val filter = ToolsFilter.entries[index]
            val isActive = activeFilter == filter
            FilterChip(
                selected = isActive,
                onClick = { onFilterChange(filter) },
                label = {
                    Text(
                        filter.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                    )
                },
                shape = HomeDecorShape.Chip,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Flat Grid  –  All tools in one responsive grid + Pro card
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ToolsFlatGrid(
    tools: List<ToolItem>,
    onToolClick: (ToolItem) -> Unit,
    onOpenPaywall: () -> Unit,
) {
    val allItems = tools + listOf(
        ToolItem(
            id = "pro",
            title = "Upgrade to Pro",
            description = "Unlock unlimited generations, priority queue, and premium styles",
            imageUrl = "",
            gradientStart = Color(0xFF6C63FF),
            gradientEnd = Color(0xFF3F51B5),
            accentColor = Color(0xFFE8EAF6),
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 280.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(((allItems.size / 2.coerceAtLeast(1) + 1) * 320).dp),
        contentPadding = PaddingValues(horizontal = AppTokens.ItemGap),
        horizontalArrangement = Arrangement.spacedBy(AppTokens.ItemGap),
        verticalArrangement = Arrangement.spacedBy(AppTokens.ItemGap),
        userScrollEnabled = false,
    ) {
        itemsIndexed(
            items = allItems,
            key = { _, item -> item.id },
        ) { _, tool ->
            if (tool.id == "pro") {
                ProUpgradeCard(onClick = onOpenPaywall)
            } else {
                ToolInfoCard(
                    tool = tool,
                    onClick = { onToolClick(tool) },
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Pro Upgrade Card
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProUpgradeCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val hoverElevation by animateDpAsState(
        targetValue = when {
            isPressed -> HomeDecorElevation.Level2
            isHovered -> HomeDecorElevation.Level1
            else -> HomeDecorElevation.Level0
        },
        label = "proHoverElevation",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(AppTokens.CardRadius),
        color = PremiumPalette.Primary,
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, PremiumPalette.OnPrimary.copy(alpha = 0.4f)),
        interactionSource = interactionSource,
        modifier = modifier
            .clip(RoundedCornerShape(AppTokens.CardRadius))
            .testTag("pro_upgrade_card"),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White,
                    )
                }
            }

            Text(
                "Upgrade to Pro",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                "Unlock unlimited generations, priority queue, premium styles, and no watermark",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(4.dp))

            Surface(
                shape = HomeDecorShape.Pill,
                color = PremiumPalette.OnPrimary,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(HomeDecorIconSize.Small),
                        tint = PremiumPalette.Primary,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "View Plans",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PremiumPalette.Primary,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Hero Section  –  "Transform your space with AI"
// ---------------------------------------------------------------------------

@Composable
private fun ToolsHeroSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTokens.SectionGap),
    ) {
        Text(
            Strings.heroTitle,
            style = MaterialTheme.typography.headlineLarge,
            color = PremiumPalette.Primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(HomeDecorSpacing.Sm))
        Text(
            Strings.heroSubtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------
// Benefits Row  –  Three quick value props
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BenefitsRow() {
    val benefits = listOf(
        Strings.benefitUpload,
        Strings.benefitExplore,
        Strings.benefitSave,
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = HomeDecorSpacing.Base),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        benefits.forEach { benefit ->
            Surface(
                shape = HomeDecorShape.Chip,
                color = PremiumPalette.PrimaryContainer,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = PremiumPalette.Primary,
                    )
                    Text(
                        benefit,
                        style = MaterialTheme.typography.labelSmall,
                        color = PremiumPalette.Primary,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Trust Signals  –  Privacy, no-commitment, free trial
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TrustSignalsRow() {
    val signals = listOf(
        Strings.trustPrivacy,
        Strings.trustNoCommitment,
        Strings.trustFreeTrial,
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = HomeDecorSpacing.Xs),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        signals.forEach { signal ->
            Surface(
                shape = HomeDecorShape.Chip,
                color = PremiumPalette.OutlineVariant.copy(alpha = 0.5f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = PremiumPalette.Primary,
                    )
                    Text(
                        signal,
                        style = MaterialTheme.typography.labelSmall,
                        color = PremiumPalette.Primary,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Category Section  –  Grouped tools under a heading
// ---------------------------------------------------------------------------

@Composable
fun CategorySection(
    category: ToolCategory,
    tools: List<ToolItem>,
    onToolClick: (ToolItem) -> Unit,
) {
    val categoryTools = category.tools.mapNotNull { id -> tools.find { it.id == id } }
    if (categoryTools.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = HomeDecorSpacing.Sm),
    ) {
        // Category header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Surface(
                shape = HomeDecorShape.Small,
                color = PremiumPalette.PrimaryContainer,
            ) {
                Icon(
                    category.icon,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp).size(18.dp),
                    tint = PremiumPalette.Primary,
                )
            }
            Column {
                Text(
                    category.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    fontWeight = FontWeight.Bold,
                    color = PremiumPalette.Primary,
                    modifier = Modifier.semantics { heading() },
                )
                Text(
                    category.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(Modifier.height(HomeDecorSpacing.Md))

        // Tool cards in a grid
        if (categoryTools.size == 1) {
            // Single tool gets full width
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HomeDecorSpacing.Sm),
            ) {
                ToolInfoCard(
                    tool = categoryTools.first(),
                    onClick = { onToolClick(categoryTools.first()) },
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 280.dp),
                modifier = Modifier.height(
                    ((categoryTools.size / 2.coerceAtLeast(1) + 1) * 300).dp
                ),
                contentPadding = PaddingValues(
                    horizontal = HomeDecorSpacing.Sm,
                ),
                horizontalArrangement = Arrangement.spacedBy(AppTokens.ItemGap),
                verticalArrangement = Arrangement.spacedBy(AppTokens.ItemGap),
                userScrollEnabled = false,
            ) {
                itemsIndexed(
                    items = categoryTools,
                    key = { _, tool -> tool.id },
                ) { _, tool ->
                    ToolInfoCard(
                        tool = tool,
                        onClick = { onToolClick(tool) },
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Tool Info Card  –  Rich card with metadata
// ---------------------------------------------------------------------------
// Layout: 16:9 image top, bottom content with 16dp padding.
// Props: title, subtitle, whatPhotoNeeded, time, creditCost, ctaLabel.
// Chips: M3 AssistChip for photo type, time, credits.
// CTA: non-interactive badge (outer card handles click).
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ToolInfoCard(
    tool: ToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = Strings.toolTitle(tool.id)
    val description = Strings.toolDescription(tool.id)
    val meta = toolMeta(tool.id)
    val ctaLabel = Strings.toolCta(tool.id)
    val toolCardDescription = Strings.a11yToolCard(title, description)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    var isFocused by remember { mutableStateOf(false) }

    val hoverElevation by animateDpAsState(
        targetValue = when {
            isPressed -> HomeDecorElevation.Level2
            isHovered -> HomeDecorElevation.Level1
            else -> HomeDecorElevation.Level0
        },
        label = "hoverElevation",
    )

    val imageScale by animateFloatAsState(
        targetValue = if (isHovered) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "imageScale",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(AppTokens.CardRadius),
        color = PremiumPalette.Surface,
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, PremiumPalette.OutlineVariant.copy(alpha = 0.4f)),
        interactionSource = interactionSource,
        modifier = modifier
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(AppTokens.CardRadius),
                ambientColor = PremiumPalette.OutlineVariant.copy(alpha = 0.15f),
                spotColor = PremiumPalette.OutlineVariant.copy(alpha = 0.10f),
            )
            .clip(RoundedCornerShape(AppTokens.CardRadius))
            .onFocusChanged { isFocused = it.isFocused }
            .testTag(Strings.formatTestTag(Strings.TestTags.toolCard, tool.id))
            .semantics {
                contentDescription = toolCardDescription
                role = Role.Button
            },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 16:9 image top
            ImageCard(
                imageUrl = tool.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = imageScale
                        scaleY = imageScale
                    },
                aspectRatio = 16f / 9f,
                scrimIntensity = ScrimIntensity.None,
            )

            // Bottom content with 16dp padding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTokens.ContentPadding),
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PremiumPalette.Primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics { heading() },
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    description,
                    style = HomeDecorType.CardSubtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.heightIn(min = 36.dp),
                )
                Spacer(Modifier.height(10.dp))

                // Metadata chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AssistChip(
                        onClick = {},
                        modifier = Modifier.height(32.dp),
                        label = {
                            Text(
                                meta.photoNeeded,
                                style = MaterialTheme.typography.labelSmall,
                                color = PremiumPalette.Primary,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(HomeDecorIconSize.Small),
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = PremiumPalette.PrimaryContainer.copy(alpha = 0.5f),
                            labelColor = PremiumPalette.Primary,
                        ),
                        border = BorderStroke(1.dp, PremiumPalette.OutlineVariant.copy(alpha = 0.5f)),
                    )
                    AssistChip(
                        onClick = {},
                        modifier = Modifier.height(32.dp),
                        label = {
                            Text(
                                meta.estimatedTime,
                                style = MaterialTheme.typography.labelSmall,
                                color = PremiumPalette.Primary,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(HomeDecorIconSize.Small),
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = PremiumPalette.PrimaryContainer.copy(alpha = 0.5f),
                            labelColor = PremiumPalette.Primary,
                        ),
                        border = BorderStroke(1.dp, PremiumPalette.OutlineVariant.copy(alpha = 0.5f)),
                    )
                    AssistChip(
                        onClick = {},
                        modifier = Modifier.height(32.dp),
                        label = {
                            Text(
                                meta.credits,
                                style = MaterialTheme.typography.labelSmall,
                                color = PremiumPalette.Primary,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(HomeDecorIconSize.Small),
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = PremiumPalette.PrimaryContainer.copy(alpha = 0.5f),
                            labelColor = PremiumPalette.Primary,
                        ),
                        border = BorderStroke(1.dp, PremiumPalette.OutlineVariant.copy(alpha = 0.5f)),
                    )
                }

                Spacer(Modifier.height(12.dp))

                // CTA badge — visual only, outer card handles click
                Surface(
                    shape = HomeDecorShape.Pill,
                    color = PremiumPalette.PrimaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            Icons.Rounded.Palette,
                            contentDescription = null,
                            modifier = Modifier.size(HomeDecorIconSize.Small),
                            tint = PremiumPalette.Primary,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            ctaLabel,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = PremiumPalette.Primary,
                        )
                    }
                }
            }

            // Focus ring
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
// Legacy ToolCard  –  Kept for backward compatibility
// ---------------------------------------------------------------------------

@Composable
fun ToolCard(
    tool: ToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ToolInfoCard(tool = tool, onClick = onClick, modifier = modifier)
}

// ---------------------------------------------------------------------------
// Loading, Error & Empty States
// ---------------------------------------------------------------------------

@Composable
private fun ToolsLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumPalette.Surface),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = PremiumPalette.Primary,
                modifier = Modifier.size(48.dp)
                    .semantics { contentDescription = Strings.a11yLoading },
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(
                text = Strings.loadingContent,
                style = MaterialTheme.typography.bodyMedium,
                color = PremiumPalette.Primary,
            )
        }
    }
}

@Composable
private fun ToolsErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumPalette.Surface),
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
                color = PremiumPalette.Primary,
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
            .background(PremiumPalette.Surface),
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
                color = PremiumPalette.Primary,
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
