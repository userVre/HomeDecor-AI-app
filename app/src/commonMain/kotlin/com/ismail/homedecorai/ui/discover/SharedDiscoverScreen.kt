package com.ismail.homedecorai.ui.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CardDefaults
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
) {
    if (state.isLoading) {
        DiscoverLoadingContent()
        return
    }

    if (state.error != null) {
        DiscoverErrorContent(message = state.error)
        return
    }

    var selectedCluster by remember { mutableStateOf(state.selectedCluster) }
    var detailSection by remember { mutableStateOf<DiscoverSectionItem?>(null) }
    var previewTarget by remember { mutableStateOf<Pair<DiscoverSectionItem, GalleryCardItem>?>(null) }
    val clusters = listOf("interior", "architecture", "landscape")
    val sections = state.sections.filter { it.cluster == selectedCluster }
    val isDesktop = rememberIsDesktop()

    fun toggleFavorite(section: DiscoverSectionItem, item: GalleryCardItem) {
        onToggleFavorite(section, item)
        showToast(
            if (item.id in state.favoriteSourceIds) Strings.toastFavoriteRemoved
            else Strings.toastFavoriteAdded
        )
    }

    fun addToMoodboard(section: DiscoverSectionItem, item: GalleryCardItem) {
        onAddToMoodboard(section, item)
        showToast(Strings.toastMoodboardAdded)
    }

    fun useStyle(section: DiscoverSectionItem, item: GalleryCardItem) {
        onUseStyle(section, item)
        previewTarget = null
        detailSection = null
    }

    val activeDetail = detailSection
    if (activeDetail != null) {
        DiscoverDetailScreen(
            section = activeDetail,
            onBack = { detailSection = null },
            onPreview = { previewTarget = activeDetail to it },
            favoriteSources = state.favoriteSourceIds,
            onFavorite = { toggleFavorite(activeDetail, it) },
            onMoodboard = { addToMoodboard(activeDetail, it) },
            onUseStyle = { useStyle(activeDetail, it) },
        )
        previewTarget?.let { (targetSection, targetItem) ->
            DiscoverPreviewDialog(
                section = targetSection,
                item = targetItem,
                onDismiss = { previewTarget = null },
                isFavorite = targetItem.id in state.favoriteSourceIds,
                onFavorite = { toggleFavorite(targetSection, targetItem) },
                onMoodboard = { addToMoodboard(targetSection, targetItem) },
                onUseStyle = { useStyle(targetSection, targetItem) },
            )
        }
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
            contentPadding = PaddingValues(
                start = if (isDesktop) HomeDecorSpacing.ScreenHorizontal else 0.dp,
                end = if (isDesktop) HomeDecorSpacing.ScreenHorizontal else 0.dp,
                top = HomeDecorSpacing.Xs,
                bottom = HomeDecorSpacing.NavBarReservation + HomeDecorSpacing.Base,
            ),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
        ) {
            item { DiscoverClusterTabs(clusters = clusters, selected = selectedCluster, onSelect = { selectedCluster = it }) }
            items(sections, key = { it.id }) { section ->
                DiscoverSectionRow(
                    section = section,
                    onSeeAll = { detailSection = section },
                    onPreview = { previewTarget = section to it },
                    favoriteSources = state.favoriteSourceIds,
                    onFavorite = { toggleFavorite(section, it) },
                    onMoodboard = { addToMoodboard(section, it) },
                    onUseStyle = { useStyle(section, it) },
                )
            }
        }
    }

    previewTarget?.let { (targetSection, targetItem) ->
        DiscoverPreviewDialog(
            section = targetSection,
            item = targetItem,
            onDismiss = { previewTarget = null },
            isFavorite = targetItem.id in state.favoriteSourceIds,
            onFavorite = { toggleFavorite(targetSection, targetItem) },
            onMoodboard = { addToMoodboard(targetSection, targetItem) },
            onUseStyle = { useStyle(targetSection, targetItem) },
        )
    }
}

@Composable
fun ScreenHeaderPills(title: String, trailing: (@Composable () -> Unit)?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Sm)
            .height(48.dp)
            .semantics {
                contentDescription = title
                heading()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.width(HomeDecorSpacing.Xxl))
        Text(title, style = MaterialTheme.typography.headlineMedium, letterSpacing = (-0.3).sp)
        Box(Modifier.width(40.dp), contentAlignment = Alignment.CenterEnd) {
            trailing?.invoke()
        }
    }
}

@Composable
fun DiscoverClusterTabs(clusters: List<String>, selected: String, onSelect: (String) -> Unit) {
    val isDesktop = rememberIsDesktop()
    val containerHorizontalPadding = if (isDesktop) HomeDecorSpacing.Lg else HomeDecorSpacing.Base

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = containerHorizontalPadding)
            .padding(vertical = HomeDecorSpacing.Sm),
        horizontalArrangement = if (isDesktop) Arrangement.Center else Arrangement.Start,
    ) {
        clusters.forEach { cluster ->
            val clusterLabel = Strings.discoverCluster(cluster)
            val isSelected = selected == cluster

            Box(
                modifier = Modifier
                    .padding(horizontal = HomeDecorSpacing.Sm)
                    .clickable { onSelect(cluster) }
                    .testTag(Strings.formatTestTag(Strings.TestTags.discoverClusterTab, cluster))
                    .semantics {
                        this.selected = isSelected
                        contentDescription = Strings.a11yDiscoverCluster(clusterLabel)
                        role = Role.Tab
                    }
                    .padding(bottom = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = clusterLabel,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(24.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else Color.Transparent,
                            ),
                    )
                }
            }
        }
    }
}

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
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
        modifier = Modifier
            .padding(horizontal = if (isDesktop) 0.dp else HomeDecorSpacing.Base)
            .testTag(Strings.formatTestTag(Strings.TestTags.discoverSectionRow, section.id)),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                sectionTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.3).sp,
            )
            Spacer(Modifier.width(HomeDecorSpacing.Xs))
            TextButton(
                onClick = onSeeAll,
                contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Xs, vertical = 0.dp),
                modifier = Modifier
                    .minimumTouchTarget()
                    .testTag(Strings.formatTestTag(Strings.TestTags.discoverSeeAll, section.id))
                    .semantics { contentDescription = Strings.a11ySeeAll(sectionTitle) },
            ) {
                Text(Strings.seeAll, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
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
            LazyRow(
                contentPadding = PaddingValues(start = HomeDecorSpacing.Base, end = HomeDecorSpacing.Base, top = HomeDecorSpacing.Xxs, bottom = HomeDecorSpacing.Xxs),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(section.items, key = { it.id }) { item ->
                    GalleryCard(
                        item = item,
                        sectionTitle = sectionTitle,
                        isFavorite = item.id in favoriteSources,
                        isDesktop = false,
                        modifier = Modifier.width(164.dp),
                        onClick = { onUseStyle(item) },
                        onPreview = { onPreview(item) },
                        onFavorite = { onFavorite(item) },
                        onMoodboard = { onMoodboard(item) },
                    )
                }
            }
        }
    }
}

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
        screenWidthDp >= 1100 -> 4
        screenWidthDp >= 900 -> 3
        else -> 2
    }

    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
        val rows = section.items.chunked(columns)
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
                        onClick = { onUseStyle(item) },
                        onPreview = { onPreview(item) },
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

@Composable
fun DiscoverDetailScreen(
    section: DiscoverSectionItem,
    onBack: () ->Unit,
    onPreview: (GalleryCardItem) -> Unit,
    favoriteSources: Set<String>,
    onFavorite: (GalleryCardItem) -> Unit,
    onMoodboard: (GalleryCardItem) -> Unit,
    onUseStyle: (GalleryCardItem) -> Unit,
) {
    val sectionTitle = Strings.discoverSectionTitle(section.id)
    val isDesktop = rememberIsDesktop()
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = Strings.back)
            }
            Column(Modifier.weight(1f)) { Text(sectionTitle, style = MaterialTheme.typography.titleLarge) }
            Surface(shape = HomeDecorShape.Badge, color = MaterialTheme.colorScheme.primaryContainer, tonalElevation = 2.dp) {
                Text(
                    Strings.ideasCount(section.items.size),
                    modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Sm),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
        val columns = if (isDesktop) GridCells.Fixed(3) else GridCells.Fixed(2)
        LazyVerticalGrid(
            columns = columns,
            contentPadding = PaddingValues(start = HomeDecorSpacing.ScreenHorizontal, end = HomeDecorSpacing.ScreenHorizontal, top = HomeDecorSpacing.Base, bottom = HomeDecorSpacing.NavBarReservation + HomeDecorSpacing.Base),
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
                    onClick = { onUseStyle(item) },
                    onPreview = { onPreview(item) },
                    onFavorite = { onFavorite(item) },
                    onMoodboard = { onMoodboard(item) },
                )
            }
        }
    }
}

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
    val sectionTitle = Strings.discoverSectionTitle(section.id)
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onUseStyle, modifier = Modifier.fillMaxWidth(), shape = HomeDecorShape.Button, colors = studioPrimaryButtonColors()) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(HomeDecorSpacing.Base))
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(Strings.createWithStyle, style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onFavorite, shape = HomeDecorShape.Button, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(HomeDecorSpacing.Base), tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else Color.Unspecified)
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(if (isFavorite) Strings.favorited else Strings.favorite, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelMedium)
                }
                OutlinedButton(onClick = onMoodboard, shape = HomeDecorShape.Button, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(HomeDecorSpacing.Base))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(Strings.addToMoodboard, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelMedium)
                }
            }
        },
        title = { Text(sectionTitle, style = MaterialTheme.typography.titleMedium) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                GalleryImageCard(
                    item = item,
                    sectionTitle = sectionTitle,
                    isDesktop = false,
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.92f),
                )
                Text(sectionTitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
        },
        shape = HomeDecorShape.Dialog,
    )
}

@Composable
fun GalleryCard(
    item: GalleryCardItem,
    sectionTitle: String,
    isFavorite: Boolean,
    isDesktop: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onPreview: () -> Unit,
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
            defaultElevation = if (isHovered) 4.dp else 1.dp,
            pressedElevation = 3.dp,
        ),
        interactionSource = interactionSource,
        modifier = modifier
            .testTag(Strings.formatTestTag(Strings.TestTags.discoverSectionCard, item.id))
            .semantics {
                contentDescription = Strings.a11yInspirationImage(sectionTitle)
                role = Role.Button
            },
    ) {
        Box(Modifier.fillMaxWidth().aspectRatio(3f / 4f)) {
            GalleryImageCard(
                item = item,
                sectionTitle = sectionTitle,
                isDesktop = isDesktop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(Modifier.fillMaxSize()) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isDesktop && isHovered,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    HoverOverlay(
                        onPreview = onPreview,
                        onUseStyle = onClick,
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
        contentAlignment = Alignment.BottomStart,
    ) {
        if (item.imageUrl.isNotEmpty()) {
            NetworkImage(
                url = item.imageUrl,
                contentDescription = Strings.a11yInspirationImage(sectionTitle),
                modifier = Modifier.fillMaxSize(),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.45f))))
                .padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun HoverOverlay(
    onPreview: () -> Unit,
    onUseStyle: () -> Unit,
    onMoodboard: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            HoverActionButton(
                icon = Icons.Rounded.Visibility,
                label = Strings.discoverPreview,
                onClick = onPreview,
            )
            HoverActionButton(
                icon = Icons.Rounded.AutoAwesome,
                label = Strings.discoverUseStyle,
                onClick = onUseStyle,
            )
            HoverActionButton(
                icon = Icons.Rounded.Bookmark,
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
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
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
            .padding(horizontal = HomeDecorSpacing.Sm, vertical = 6.dp),
    ) {
        Surface(
            shape = HomeDecorShape.Full,
            color = Color.White.copy(alpha = 0.95f),
            modifier = Modifier.size(HomeDecorSpacing.TouchTarget),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

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

@Composable
expect fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
)
