package com.ismail.homedecorai.ui.discover

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.ismail.homedecorai.showToast
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*

@Composable
fun SharedDiscoverScreen(
    state: DiscoverScreenState,
    onToggleFavorite: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onAddToMoodboard: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onUseStyle: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onSignIn: () -> Unit = {},
) {
    if (state.isLoading) {
        DiscoverLoadingContent()
        return
    }

    if (state.error != null) {
        DiscoverErrorContent(message = state.error)
        return
    }

    var selectedCluster by rememberSaveable { mutableStateOf(state.selectedCluster) }
    var detailSection by rememberSaveable { mutableStateOf<String?>(null) }
    var previewItem by rememberSaveable { mutableStateOf<String?>(null) }
    val clusters = listOf("interior", "architecture", "landscape")
    val sections = state.sections.filter { it.cluster == selectedCluster }
    val isDesktop = rememberIsDesktop()
    val scrollState = rememberLazyListState()

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
            showToast(Strings.boardSignInCta)
            onSignIn()
            return
        }
        onToggleFavorite(section, item)
    }

    fun addToMoodboard(section: DiscoverSectionItem, item: GalleryCardItem) {
        if (!state.isSignedIn) {
            showToast(Strings.boardSignInCta)
            onSignIn()
            return
        }
        onAddToMoodboard(section, item)
    }

    fun useStyle(section: DiscoverSectionItem, item: GalleryCardItem) {
        onUseStyle(section, item)
        previewItem = null
        detailSection = null
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
            item {
                DiscoverClusterTabs(
                    clusters = clusters,
                    selected = selectedCluster,
                    onSelect = { selectedCluster = it },
                )
            }
            items(sections, key = { it.id }) { section ->
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
                )
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Box(contentAlignment = Alignment.CenterEnd) {
            trailing?.invoke()
        }
    }
}

// ─── MD3 Expressive Segmented Tabs with Sliding Indicator ──────────────────

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
            // Sliding filled indicator
            Box(
                modifier = Modifier
                    .offset(x = animatedOffset)
                    .width(tabWidth)
                    .height(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary),
            )
            // Tab labels
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
                contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Xs, vertical = HomeDecorSpacing.Xxs),
                modifier = Modifier
                    .minimumTouchTarget()
                    .testTag(Strings.formatTestTag(Strings.TestTags.discoverSeeAll, section.id))
                    .semantics { contentDescription = Strings.a11ySeeAll(sectionTitle) },
            ) {
                Text(
                    Strings.seeAll,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
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
            )
        } else {
            DiscoverSectionMobileRow(
                section = section,
                onPreview = onPreview,
                favoriteSources = favoriteSources,
                onFavorite = onFavorite,
                onMoodboard = onMoodboard,
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
    onMoodboard: (GalleryCardItem) -> Unit,
    onUseStyle: (GalleryCardItem) -> Unit,
) {
    val sectionTitle = Strings.discoverSectionTitle(section.id)
    val availableWidth = getScreenWidthDp().dp - HomeDecorSpacing.ScreenHorizontal * 2
    val cardWidth = ((availableWidth - HomeDecorSpacing.Sm) / 2.15f).coerceAtMost(160.dp).coerceAtLeast(120.dp)

    LazyRow(
        contentPadding = PaddingValues(
            start = HomeDecorSpacing.Base,
            end = HomeDecorSpacing.Base,
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
                onUseStyle = { onUseStyle(item) },
                onFavorite = { onFavorite(item) },
                onMoodboard = { onMoodboard(item) },
            )
        }
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
) {
    val sectionTitle = Strings.discoverSectionTitle(section.id)
    val screenWidthDp = getScreenWidthDp()
    val columns = when {
        screenWidthDp >= 1200 -> 4
        screenWidthDp >= 900 -> 3
        else -> 2
    }
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
                        onUseStyle = { onUseStyle(item) },
                        onFavorite = { onFavorite(item) },
                        onMoodboard = { onMoodboard(item) },
                    )
                }
                repeat(columns - rowItems.size) {
                    Spacer(Modifier.weight(1f))
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

        // Responsive grid
        val columns = when {
            isDesktop && getScreenWidthDp() >= 1200 -> GridCells.Fixed(4)
            isDesktop && getScreenWidthDp() >= 900 -> GridCells.Fixed(3)
            isDesktop -> GridCells.Fixed(3)
            else -> GridCells.Fixed(2)
        }
        LazyVerticalGrid(
            columns = columns,
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
                    onUseStyle = { onUseStyle(item) },
                    onFavorite = { onFavorite(item) },
                    onMoodboard = { onMoodboard(item) },
                )
            }
        }
    }
}

// ─── Preview Dialog ────────────────────────────────────────────────────────

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

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onUseStyle()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = HomeDecorShape.Button,
                colors = studioPrimaryButtonColors(),
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(HomeDecorSpacing.Base))
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
                        Icons.Rounded.Star, null, Modifier.size(HomeDecorSpacing.Base),
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
                    Icon(Icons.Rounded.Save, null, Modifier.size(HomeDecorSpacing.Base))
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        itemTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.3).sp,
                    )
                    Text(
                        sectionTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = Strings.close)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                NetworkImage(
                    url = item.imageUrl,
                    contentDescription = itemTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.92f)
                        .clip(HomeDecorShape.ImageLarge),
                )
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
    onUseStyle: () -> Unit,
    onFavorite: () -> Unit,
    onMoodboard: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    ElevatedCard(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isHovered) 3.dp else 1.dp,
            pressedElevation = 2.dp,
        ),
        interactionSource = interactionSource,
        modifier = modifier
            .testTag(Strings.formatTestTag(Strings.TestTags.discoverSectionCard, item.id))
            .semantics {
                contentDescription = Strings.a11yInspirationImage(sectionTitle)
                role = Role.Button
            },
    ) {
        Box(Modifier.fillMaxWidth().aspectRatio(0.8f)) {
            GalleryImageCard(
                item = item,
                sectionTitle = sectionTitle,
                isDesktop = isDesktop,
                modifier = Modifier.fillMaxSize(),
            )
            // Favorite indicator badge (always visible when favorited)
            if (isFavorite) {
                Surface(
                    shape = HomeDecorShape.Full,
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.92f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Sm)
                        .size(28.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = Strings.favorited,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
            // Desktop hover overlay
            Box(Modifier.fillMaxSize()) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isDesktop && isHovered,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    HoverOverlay(
                        onFavorite = onFavorite,
                        onUseStyle = onUseStyle,
                        onMoodboard = onMoodboard,
                    )
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
    Box(
        modifier = modifier
            .clip(HomeDecorShape.Card)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        if (item.imageUrl.isNotEmpty()) {
            NetworkImage(
                url = item.imageUrl,
                contentDescription = Strings.a11yInspirationImage(sectionTitle),
                modifier = Modifier.fillMaxSize(),
            )
        }
        // Bottom label with gradient scrim
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            HomeDecorExtra.scrimMedium,
                            HomeDecorExtra.scrimHeavy,
                        )
                    )
                )
                .padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = HomeDecorExtra.onGradientText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ─── Hover Overlay (Desktop) ───────────────────────────────────────────────

@Composable
fun HoverOverlay(
    onFavorite: () -> Unit,
    onUseStyle: () -> Unit,
    onMoodboard: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        HomeDecorExtra.scrimHeavy.copy(alpha = 0.5f),
                        HomeDecorExtra.scrimHeavy,
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            HoverActionButton(
                icon = Icons.Rounded.Star,
                label = Strings.favorite,
                onClick = onFavorite,
            )
            HoverActionButton(
                icon = Icons.Rounded.AutoAwesome,
                label = Strings.discoverUseStyle,
                onClick = onUseStyle,
            )
            HoverActionButton(
                icon = Icons.Rounded.Save,
                label = Strings.discoverSave,
                onClick = onMoodboard,
            )
        }
    }
}

@Composable
fun HoverActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xxs),
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .semantics {
                role = Role.Button
                contentDescription = label
            }
            .padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
    ) {
        Surface(
            shape = HomeDecorShape.Full,
            color = HomeDecorExtra.onGradientText.copy(alpha = 0.92f),
            modifier = Modifier.size(40.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = HomeDecorExtra.onGradientText,
            textAlign = TextAlign.Center,
        )
    }
}

// ─── Loading & Error States ────────────────────────────────────────────────

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

// ─── Network Image (expect/actual) ─────────────────────────────────────────

@Composable
expect fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
)
