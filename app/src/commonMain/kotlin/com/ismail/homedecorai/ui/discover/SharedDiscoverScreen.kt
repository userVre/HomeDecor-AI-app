package com.ismail.homedecorai.ui.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.SingleBed
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.getScreenWidthDp
import com.ismail.homedecorai.model.DiscoverSectionItem
import com.ismail.homedecorai.model.DiscoverScreenState
import com.ismail.homedecorai.model.GalleryCardItem
import com.ismail.homedecorai.ui.auth.SharedAuthScreen
import com.ismail.homedecorai.ui.components.ImageCard
import com.ismail.homedecorai.ui.components.ScrimIntensity
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*

// ---------------------------------------------------------------------------
// Filter definitions
// ---------------------------------------------------------------------------

private enum class FilterCategory(val label: String) {
    Room(Strings.filterRoom),
    Style(Strings.filterStyle),
    Color(Strings.filterColor),
    Mood(Strings.filterMood),
}

// ─── Main Screen ──────────────────────────────────────────────────────────

@Composable
fun SharedDiscoverScreen(
    state: DiscoverScreenState,
    onToggleFavorite: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onAddToMoodboard: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onUseStyle: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onSignUp: (String, String) -> Unit = { _, _ -> },
    onGoogleSignIn: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    isAuthLoading: Boolean = false,
    authError: String? = null,
    onDismissAuthError: () -> Unit = {},
) {
    if (state.isLoading) {
        DiscoverLoadingContent()
        return
    }

    if (state.error != null) {
        DiscoverErrorContent(message = state.error)
        return
    }

    if (state.noResultsMessage != null) {
        DiscoverNoResultsContent(message = state.noResultsMessage)
        return
    }

    var selectedCluster by rememberSaveable { mutableStateOf(state.selectedCluster) }
    var detailSection by rememberSaveable { mutableStateOf<String?>(null) }
    var previewItem by rememberSaveable { mutableStateOf<String?>(null) }
    var showAuthOverlay by rememberSaveable { mutableStateOf(false) }

    // Filter state
    var activeFilterCategory by rememberSaveable { mutableStateOf<FilterCategory?>(null) }
    var selectedFilterValues by rememberSaveable { mutableStateOf(setOf<String>()) }

    val clusters = listOf("interior", "architecture", "landscape")
    val sections = state.sections.filter { it.cluster == selectedCluster }
    val isDesktop = rememberIsDesktop()
    val scrollState = rememberLazyListState()

    // Auto-close auth overlay when user signs in
    LaunchedEffect(state.isSignedIn) {
        if (showAuthOverlay && state.isSignedIn) {
            showAuthOverlay = false
        }
    }

    val activeDetailSection = remember(detailSection, state.sections) {
        detailSection?.let { id -> state.sections.find { it.id == id } }
    }
    val activePreviewPair = remember(previewItem, state.sections) {
        previewItem?.let { itemId ->
            for (section in state.sections) {
                val item = section.items.find { it.id == itemId }
                if (item != null) return@let section to item
            }
            null
        }
    }

    fun toggleFavorite(section: DiscoverSectionItem, item: GalleryCardItem) {
        if (!state.isSignedIn) {
            showAuthOverlay = true
            return
        }
        onToggleFavorite(section, item)
    }

    fun addToMoodboard(section: DiscoverSectionItem, item: GalleryCardItem) {
        if (!state.isSignedIn) {
            showAuthOverlay = true
            return
        }
        onAddToMoodboard(section, item)
    }

    fun useStyle(section: DiscoverSectionItem, item: GalleryCardItem) {
        onUseStyle(section, item)
        previewItem = null
        detailSection = null
    }

    // Build filter option sets from all items in current cluster
    val filterOptions = remember(sections) {
        FilterCategory.entries.associateWith { category ->
            sections.flatMap { section ->
                section.items.map { item ->
                    when (category) {
                        FilterCategory.Room -> item.room
                        FilterCategory.Style -> item.styleType
                        FilterCategory.Color -> item.color
                        FilterCategory.Mood -> item.mood
                    }
                }.filter { it.isNotBlank() }
            }.toSet().sorted().toList()
        }
    }

    // Apply filters to sections
    val filteredSections = remember(sections, activeFilterCategory, selectedFilterValues) {
        if (activeFilterCategory == null || selectedFilterValues.isEmpty()) {
            sections
        } else {
            sections.map { section ->
                val filteredItems = section.items.filter { item ->
                    val fieldValue = when (activeFilterCategory) {
                        FilterCategory.Room -> item.room
                        FilterCategory.Style -> item.styleType
                        FilterCategory.Color -> item.color
                        FilterCategory.Mood -> item.mood
                        null -> ""
                    }
                    fieldValue in selectedFilterValues
                }
                section.copy(items = filteredItems)
            }.filter { it.items.isNotEmpty() }
        }
    }

    if (activeDetailSection != null) {
        DiscoverDetailScreen(
            section = activeDetailSection,
            onBack = { detailSection = null },
            onPreview = { previewItem = it.id },
            favoriteSources = state.favoriteSourceIds,
            onFavorite = { toggleFavorite(activeDetailSection, it) },
            onMoodboard = { addToMoodboard(activeDetailSection, it) },
            onUseStyle = { useStyle(activeDetailSection, it) },
            isSignedIn = state.isSignedIn,
            onRequireAuth = { showAuthOverlay = true },
        )
        return
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.discoverScreen),
    ) {
        ScreenHeaderPills(title = Strings.discoverStylesTitle, trailing = null)
        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(
                start = if (isDesktop) HomeDecorSpacing.ScreenHorizontal else 0.dp,
                end = if (isDesktop) HomeDecorSpacing.ScreenHorizontal else 0.dp,
                top = HomeDecorSpacing.Xxs,
                bottom = HomeDecorSpacing.Xxl,
            ),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.SectionGap),
        ) {
            // Cluster tabs
            item {
                DiscoverClusterTabs(
                    clusters = clusters,
                    selected = selectedCluster,
                    onSelect = {
                        selectedCluster = it
                        activeFilterCategory = null
                        selectedFilterValues = emptySet()
                    },
                )
            }
            // Filter chips row
            item {
                DiscoverFilterRow(
                    activeCategory = activeFilterCategory,
                    selectedValues = selectedFilterValues,
                    options = filterOptions,
                    onCategorySelect = { category ->
                        if (activeFilterCategory == category) {
                            activeFilterCategory = null
                            selectedFilterValues = emptySet()
                        } else {
                            activeFilterCategory = category
                            selectedFilterValues = emptySet()
                        }
                    },
                    onValueToggle = { value ->
                        selectedFilterValues = if (value in selectedFilterValues) {
                            selectedFilterValues - value
                        } else {
                            selectedFilterValues + value
                        }
                    },
                    onClear = {
                        activeFilterCategory = null
                        selectedFilterValues = emptySet()
                    },
                )
            }
            // Sections
            if (filteredSections.isEmpty() && activeFilterCategory != null) {
                item {
                    DiscoverFilteredEmptyContent()
                }
            } else {
                items(filteredSections, key = { it.id }) { section ->
                    DiscoverSectionRow(
                        section = section,
                        onSeeAll = {
                            previewItem = null
                            detailSection = section.id
                        },
                        onPreview = { previewItem = it.id },
                        favoriteSources = state.favoriteSourceIds,
                        onFavorite = { toggleFavorite(section, it) },
                        onMoodboard = { addToMoodboard(section, it) },
                        onUseStyle = { useStyle(section, it) },
                        isSignedIn = state.isSignedIn,
                        onRequireAuth = { showAuthOverlay = true },
                    )
                }
            }
        }
    }

    activePreviewPair?.let { (targetSection, targetItem) ->
        DiscoverPreviewDialog(
            section = targetSection,
            item = targetItem,
            onDismiss = { previewItem = null },
            isFavorite = targetItem.id in state.favoriteSourceIds,
            onFavorite = { toggleFavorite(targetSection, targetItem) },
            onMoodboard = { addToMoodboard(targetSection, targetItem) },
            onUseStyle = { useStyle(targetSection, targetItem) },
        )
    }

    if (showAuthOverlay) {
        SharedAuthScreen(
            onSignIn = { _, _ ->
                showAuthOverlay = false
            },
            onSignUp = { email, password ->
                onSignUp(email, password)
                showAuthOverlay = false
            },
            onGoogleSignIn = {
                onGoogleSignIn()
                showAuthOverlay = false
            },
            onForgotPassword = onForgotPassword,
            onClose = { showAuthOverlay = false },
            isLoading = isAuthLoading,
            errorMessage = authError,
            onDismissError = onDismissAuthError,
        )
    }
}

// ─── Header ────────────────────────────────────────────────────────────────

@Composable
fun ScreenHeaderPills(title: String, trailing: (@Composable () -> Unit)?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(
                start = HomeDecorSpacing.ScreenHorizontal,
                end = HomeDecorSpacing.ScreenHorizontal,
                top = HomeDecorSpacing.Base,
                bottom = HomeDecorSpacing.Sm,
            ))
            .semantics {
                contentDescription = title
                heading()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xxs))
            Text(
                "Explore AI-generated design inspiration",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Box(contentAlignment = Alignment.CenterEnd) {
            trailing?.invoke()
        }
    }
}

// ─── MD3 Expressive Segmented Tabs ────────────────────────────────────────

@Composable
fun DiscoverClusterTabs(clusters: List<String>, selected: String, onSelect: (String) -> Unit) {
    val selectedIndex = clusters.indexOf(selected).coerceAtLeast(0)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Xs),
    ) {
        val tabWidth = maxWidth / clusters.size
        val animatedOffset by animateDpAsState(
            targetValue = tabWidth * selectedIndex,
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
            label = "tabIndicator",
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(HomeDecorShape.Chip)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(3.dp),
        ) {
            Box(
                modifier = Modifier
                    .offset(x = animatedOffset)
                    .width(tabWidth)
                    .height(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                clusters.forEach { cluster ->
                    val clusterLabel = Strings.discoverCluster(cluster)
                    val isSelected = selected == cluster
                    Text(
                        text = clusterLabel,
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                            .clickable { onSelect(cluster) }
                            .testTag(Strings.formatTestTag(Strings.TestTags.discoverClusterTab, cluster))
                            .semantics {
                                this.selected = isSelected
                                contentDescription = Strings.a11yDiscoverCluster(clusterLabel)
                                role = Role.Tab
                            },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

// ─── Filter Chips Row ─────────────────────────────────────────────────────

@Composable
private fun DiscoverFilterRow(
    activeCategory: FilterCategory?,
    selectedValues: Set<String>,
    options: Map<FilterCategory, List<String>>,
    onCategorySelect: (FilterCategory) -> Unit,
    onValueToggle: (String) -> Unit,
    onClear: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(Strings.TestTags.discoverFilterRow),
    ) {
        // Category selector row
        LazyRow(
            contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            items(FilterCategory.entries.toList()) { category ->
                val isActive = activeCategory == category
                FilterChip(
                    selected = isActive,
                    onClick = { onCategorySelect(category) },
                    label = {
                        Text(
                            category.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = category.icon(),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                    },
                    trailingIcon = if (isActive) {
                        {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    } else null,
                    shape = HomeDecorShape.Chip,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = MaterialTheme.colorScheme.outline,
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                        enabled = true,
                        selected = isActive,
                    ),
                    modifier = Modifier.testTag(
                        Strings.formatTestTag(Strings.TestTags.discoverFilterChip, category.name)
                    ),
                )
            }
            if (activeCategory != null && selectedValues.isNotEmpty()) {
                item {
                    FilterChip(
                        selected = false,
                        onClick = onClear,
                        label = {
                            Text(
                                Strings.filterClearAll,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error,
                            )
                        },
                        shape = HomeDecorShape.Chip,
                    )
                }
            }
        }

        // Value chips row (shown when a category is active)
        AnimatedVisibility(
            visible = activeCategory != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            val values = options[activeCategory] ?: emptyList()
            if (values.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(
                        start = HomeDecorSpacing.Base,
                        end = HomeDecorSpacing.Base,
                        top = HomeDecorSpacing.Xs,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                ) {
                    items(values) { value ->
                        val isSelected = value in selectedValues
                        FilterChip(
                            selected = isSelected,
                            onClick = { onValueToggle(value) },
                            label = {
                                Text(
                                    value,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                )
                            },
                            trailingIcon = if (isSelected) {
                                {
                                    Icon(
                                        Icons.Rounded.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                    )
                                }
                            } else null,
                            shape = HomeDecorShape.Chip,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = MaterialTheme.colorScheme.outline,
                                selectedBorderColor = MaterialTheme.colorScheme.primary,
                                enabled = true,
                                selected = isSelected,
                            ),
                        )
                    }
                }
            }
        }
    }
}

private fun FilterCategory.icon(): ImageVector = when (this) {
    FilterCategory.Room -> Icons.Rounded.SingleBed
    FilterCategory.Style -> Icons.Rounded.Palette
    FilterCategory.Color -> Icons.Rounded.Palette
    FilterCategory.Mood -> Icons.Rounded.Star
}

// ─── Section Row ───────────────────────────────────────────────────────────

@Composable
fun DiscoverSectionRow(
    section: DiscoverSectionItem,
    onSeeAll: () -> Unit,
    onPreview: (GalleryCardItem) -> Unit,
    favoriteSources: Set<String>,
    onFavorite: (GalleryCardItem) -> Unit,
    onMoodboard: (GalleryCardItem) -> Unit,
    onUseStyle: (GalleryCardItem) -> Unit,
    isSignedIn: Boolean,
    onRequireAuth: () -> Unit,
) {
    val sectionTitle = Strings.discoverSectionTitle(section.id)
    val isDesktop = rememberIsDesktop()

    Column(
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.discoverSectionRow, section.id)),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                sectionTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.3).sp,
                modifier = Modifier.weight(1f).semantics { heading() },
            )
            TextButton(
                onClick = onSeeAll,
                contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                modifier = Modifier
                    .minimumTouchTarget()
                    .testTag(Strings.formatTestTag(Strings.TestTags.discoverSeeAll, section.id))
                    .semantics { contentDescription = Strings.a11ySeeAll(sectionTitle) },
            ) {
                Text(
                    Strings.seeAll,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.width(HomeDecorSpacing.Xs))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(HomeDecorIconSize.Small),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        if (isDesktop) {
            DiscoverSectionGrid(
                section = section,
                onPreview = onPreview,
                favoriteSources = favoriteSources,
                onFavorite = onFavorite,
                onMoodboard = onMoodboard,
                onUseStyle = onUseStyle,
                isSignedIn = isSignedIn,
                onRequireAuth = onRequireAuth,
            )
        } else {
            DiscoverSectionMobileRow(
                section = section,
                onPreview = onPreview,
                favoriteSources = favoriteSources,
                onFavorite = onFavorite,
                onUseStyle = onUseStyle,
            )
        }
    }
}

@Composable
private fun DiscoverSectionMobileRow(
    section: DiscoverSectionItem,
    onPreview: (GalleryCardItem) -> Unit,
    favoriteSources: Set<String>,
    onFavorite: (GalleryCardItem) -> Unit,
    onUseStyle: ((GalleryCardItem) -> Unit)? = null,
) {
    val sectionTitle = Strings.discoverSectionTitle(section.id)
    val availableWidth = getScreenWidthDp().dp - HomeDecorSpacing.ScreenHorizontal * 2
    val cardWidth = ((availableWidth - HomeDecorSpacing.Sm) / 2.15f).coerceAtMost(160.dp).coerceAtLeast(120.dp)

    Box(modifier = Modifier.fillMaxWidth()) {
        val listState = rememberLazyListState()

        LazyRow(
            state = listState,
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.Base,
                end = HomeDecorSpacing.Xl,
            ),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(section.items, key = { it.id }) { item ->
                GalleryCard(
                    item = item,
                    sectionTitle = sectionTitle,
                    isFavorite = item.id in favoriteSources,
                    isDesktop = false,
                    modifier = Modifier.width(cardWidth),
                    onClick = { onPreview(item) },
                    onFavorite = { onFavorite(item) },
                    onUseStyle = onUseStyle?.let { fn -> { fn(item) } },
                )
            }
        }

        // Fade gradient at right edge to indicate more content
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(32.dp)
                .height(cardWidth * 0.75f)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background,
                        ),
                    ),
                ),
        )
    }
}

// ─── Desktop Section Grid ──────────────────────────────────────────────────

@Composable
fun DiscoverSectionGrid(
    section: DiscoverSectionItem,
    onPreview: (GalleryCardItem) -> Unit,
    favoriteSources: Set<String>,
    onFavorite: (GalleryCardItem) -> Unit,
    onMoodboard: (GalleryCardItem) -> Unit,
    onUseStyle: (GalleryCardItem) -> Unit,
    isSignedIn: Boolean,
    onRequireAuth: () -> Unit,
) {
    val sectionTitle = Strings.discoverSectionTitle(section.id)
    val screenWidthDp = getScreenWidthDp()
    val columns = when {
        screenWidthDp >= 1200 -> 4
        screenWidthDp >= 900 -> 3
        else -> 2
    }
    // Only show filled cells — no empty gray placeholders
    val visibleItems = section.items.take(columns * 2)

    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
        val rows = visibleItems.chunked(columns)
        rows.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                modifier = Modifier.fillMaxWidth(),
            ) {
                rowItems.forEach { item ->
                    GalleryCard(
                        item = item,
                        sectionTitle = sectionTitle,
                        isFavorite = item.id in favoriteSources,
                        isDesktop = true,
                        modifier = Modifier.weight(1f),
                        onClick = { onPreview(item) },
                        onFavorite = { onFavorite(item) },
                        onMoodboard = {
                            if (!isSignedIn) { onRequireAuth() } else onMoodboard(item)
                        },
                        onUseStyle = { onUseStyle(item) },
                    )
                }
            }
        }
    }
}

// ─── Detail Screen ─────────────────────────────────────────────────────────

@Composable
fun DiscoverDetailScreen(
    section: DiscoverSectionItem,
    onBack: () -> Unit,
    onPreview: (GalleryCardItem) -> Unit,
    favoriteSources: Set<String>,
    onFavorite: (GalleryCardItem) -> Unit,
    onMoodboard: (GalleryCardItem) -> Unit,
    onUseStyle: (GalleryCardItem) -> Unit,
    isSignedIn: Boolean,
    onRequireAuth: () -> Unit,
) {
    val sectionTitle = Strings.discoverSectionTitle(section.id)
    val isDesktop = rememberIsDesktop()

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = Strings.back)
            }
            Column(Modifier.weight(1f)) {
                Text(
                    sectionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.3).sp,
                )
            }
            Surface(
                shape = HomeDecorShape.Badge,
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = 2.dp,
            ) {
                Text(
                    Strings.ideasCount(section.items.size),
                    modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        // Responsive grid: adaptive min 280dp
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            contentPadding = PaddingValues(
                start = if (isDesktop) HomeDecorSpacing.ScreenHorizontal else HomeDecorSpacing.Base,
                end = if (isDesktop) HomeDecorSpacing.ScreenHorizontal else HomeDecorSpacing.Base,
                top = HomeDecorSpacing.Xs,
                bottom = HomeDecorSpacing.Xxl,
            ),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            items(section.items, key = { it.id }) { item ->
                GalleryCard(
                    item = item,
                    sectionTitle = sectionTitle,
                    isFavorite = item.id in favoriteSources,
                    isDesktop = isDesktop,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onPreview(item) },
                    onFavorite = { onFavorite(item) },
                    onMoodboard = {
                        if (!isSignedIn) { onRequireAuth() } else onMoodboard(item)
                    },
                    onUseStyle = { onUseStyle(item) },
                )
            }
        }
    }
}

// ─── Preview Dialog (internally scrollable) ───────────────────────────────

@Composable
fun DiscoverPreviewDialog(
    section: DiscoverSectionItem,
    item: GalleryCardItem,
    onDismiss: () -> Unit,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    onMoodboard: () -> Unit,
    onUseStyle: () -> Unit,
) {
    val itemTitle = item.title
    val sectionTitle = Strings.discoverSectionTitle(section.id)

    val metaItems = buildList {
        if (item.styleType.isNotBlank()) add(item.styleType)
        add(sectionTitle)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onUseStyle,
                modifier = Modifier.fillMaxWidth(),
                shape = HomeDecorShape.Button,
                colors = studioPrimaryButtonColors(),
            ) {
                Icon(Icons.Rounded.Palette, null, Modifier.size(HomeDecorSpacing.Base))
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(Strings.createWithStyle, style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedButton(
                    onClick = onFavorite,
                    shape = HomeDecorShape.Button,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isFavorite) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent,
                    ),
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(HomeDecorSpacing.Base),
                        tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        if (isFavorite) Strings.favorited else Strings.favorite,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                OutlinedButton(
                    onClick = onMoodboard,
                    shape = HomeDecorShape.Button,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Rounded.Bookmark, null, Modifier.size(HomeDecorSpacing.Base))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        Strings.addToMoodboard,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(end = 52.dp),
                ) {
                    Text(
                        itemTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.3).sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(HomeDecorSpacing.Xxs))
                    Text(
                        metaItems.joinToString(" \u00b7 "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            CircleShape,
                        ),
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = Strings.close)
                }
            }
        },
        text = {
            // Internally scrollable content
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                NetworkImage(
                    url = item.imageUrl,
                    contentDescription = itemTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f)
                        .clip(HomeDecorShape.ImageLarge),
                )
                if (item.description.isNotBlank()) {
                    Text(
                        item.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        shape = HomeDecorShape.Dialog,
    )
}

// ─── Gallery Card ──────────────────────────────────────────────────────────

@Composable
fun GalleryCard(
    item: GalleryCardItem,
    sectionTitle: String,
    isFavorite: Boolean,
    isDesktop: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onMoodboard: (() -> Unit)? = null,
    onUseStyle: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    // Subtle hover/focus animation
    val cardScale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.97f
            isHovered -> 1.01f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 300f,
        ),
        label = "cardScale",
    )

    val cardDescription = if (item.styleType.isNotBlank()) {
        Strings.a11yDiscoverCard(item.title, sectionTitle, item.styleType)
    } else {
        Strings.a11yInspirationImage(sectionTitle)
    }

    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = 1.dp,
                shape = HomeDecorShape.Card,
                ambientColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f),
                spotColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.10f),
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.40f), HomeDecorShape.Card)
            .testTag(Strings.formatTestTag(Strings.TestTags.discoverSectionCard, item.id))
            .semantics {
                contentDescription = cardDescription
                role = Role.Button
            },
        shape = HomeDecorShape.Card,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isHovered) 3.dp else 2.dp,
            pressedElevation = 2.dp,
        ),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f) // stable aspect ratio — no 0.8f
        ) {
            GalleryImageCard(
                item = item,
                sectionTitle = sectionTitle,
                isDesktop = isDesktop,
                modifier = Modifier.fillMaxSize(),
            )
            // Style type badge — top-start
            if (item.styleType.isNotBlank()) {
                Surface(
                    shape = HomeDecorShape.Chip,
                    color = HomeDecorColors.PrimaryContainer.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(HomeDecorSpacing.Sm),
                ) {
                    Text(
                        text = item.styleType,
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = HomeDecorColors.OnPrimaryContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            // Category label — below style badge
            if (sectionTitle.isNotBlank()) {
                Surface(
                    shape = HomeDecorShape.Chip,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = HomeDecorSpacing.Sm, top = if (item.styleType.isNotBlank()) 36.dp else HomeDecorSpacing.Sm),
                ) {
                    Text(
                        text = sectionTitle,
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            // Favorite badge — top-end
            if (isFavorite) {
                Surface(
                    shape = HomeDecorShape.Full,
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Md)
                        .size(28.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = Strings.favorited,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
            // Action buttons — always visible at bottom
            if (onUseStyle != null || onFavorite != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(HomeDecorSpacing.Sm),
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                ) {
                    if (onUseStyle != null) {
                        Surface(
                            onClick = onUseStyle,
                            shape = HomeDecorShape.Button,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.92f),
                            shadowElevation = 2.dp,
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                            ) {
                                Icon(
                                    Icons.Rounded.Palette,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                                Text(
                                    Strings.useThisStyle,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    }
                    if (onFavorite != null) {
                        Surface(
                            shape = CircleShape,
                            color = if (isFavorite) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)
                            else MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            shadowElevation = 2.dp,
                        ) {
                            IconButton(
                                onClick = onFavorite,
                                modifier = Modifier.size(28.dp),
                            ) {
                                Icon(
                                    if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                    contentDescription = if (isFavorite) Strings.favorited else Strings.discoverSave,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isFavorite) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryImageCard(
    item: GalleryCardItem,
    sectionTitle: String,
    isDesktop: Boolean,
    modifier: Modifier = Modifier,
) {
    val cardDescription = if (item.styleType.isNotBlank()) {
        Strings.a11yDiscoverCard(item.title, sectionTitle, item.styleType)
    } else {
        Strings.a11yInspirationImage(sectionTitle)
    }

    Box(modifier = modifier) {
        // Single image, 4:3 aspect ratio
        NetworkImage(
            url = item.imageUrl,
            contentDescription = cardDescription,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(4f / 3f),
        )

        // Scrim gradient: bottom 40% black 60% to transparent
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(40.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f),
                        ),
                    ),
                ),
        )

        // Tags top-left 8dp margin
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
        ) {
            if (item.styleType.isNotBlank()) {
                Surface(
                    shape = HomeDecorShape.Chip,
                    color = HomeDecorColors.PrimaryContainer.copy(alpha = 0.85f),
                ) {
                    Text(
                        text = item.styleType,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = HomeDecorColors.OnPrimaryContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (item.room.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Surface(
                    shape = HomeDecorShape.Chip,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                ) {
                    Text(
                        text = item.room,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        // Title text at bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ─── Loading, Error & Empty States ────────────────────────────────────────

@Composable
private fun DiscoverLoadingContent() {
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
private fun DiscoverErrorContent(message: String) {
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
                modifier = Modifier.size(HomeDecorIconSize.Xxxl),
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
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun DiscoverNoResultsContent(message: String) {
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
                modifier = Modifier.size(HomeDecorIconSize.Xxxl),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(
                text = Strings.discoverNoResults,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun DiscoverFilteredEmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = HomeDecorSpacing.Xxxxl),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(
                text = Strings.discoverEmptyFilteredTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(
                text = Strings.discoverEmptyFilteredHint,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

// ─── Network Image (expect/actual) ─────────────────────────────────────────

@Composable
expect fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
)
