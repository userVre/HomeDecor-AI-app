package com.ismail.homedecorai.ui.discover

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.model.DiscoverSection
import com.ismail.homedecorai.model.GalleryItem
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*

@Composable
fun DiscoverScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    var selectedCluster by remember { mutableStateOf("Intérieurs") }
    var detailSection by remember { mutableStateOf<DiscoverSection?>(null) }
    var previewTarget by remember { mutableStateOf<DiscoverPreviewTarget?>(null) }
    val clusters = listOf("Intérieurs", "Architecture", "Paysages")
    val sections = HomeDecorCatalog.discoverSections.filter { it.cluster == selectedCluster }
    val favoriteSources = remember(state.workspace.favorites) { state.workspace.favorites.map { it.sourceType }.toSet() }
    fun openPreview(section: DiscoverSection, item: GalleryItem) {
        previewTarget = section.discoverPreviewTarget(item)
    }
    fun toggleFavorite(section: DiscoverSection, item: GalleryItem) {
        val favorite = viewModel.toggleDiscoverFavorite(item, section)
        Toast.makeText(
            context,
            context.getString(if (favorite) R.string.toast_favorite_added else R.string.toast_favorite_removed),
            Toast.LENGTH_SHORT,
        ).show()
    }
    fun addToMoodboard(section: DiscoverSection, item: GalleryItem) {
        viewModel.addDiscoverToMoodboard(item, section)
        Toast.makeText(context, context.getString(R.string.toast_moodboard_added), Toast.LENGTH_SHORT).show()
    }
    fun useStyle(section: DiscoverSection, item: GalleryItem) {
        viewModel.useDiscoverStyle(item, section)
        previewTarget = null
        detailSection = null
    }
    val activeDetail = detailSection
    if (activeDetail != null) {
        DiscoverDetailScreen(
            section = activeDetail,
            onBack = { detailSection = null },
            onPreview = { openPreview(activeDetail, it) },
            favoriteSources = favoriteSources,
            onFavorite = { toggleFavorite(activeDetail, it) },
            onMoodboard = { addToMoodboard(activeDetail, it) },
            onUseStyle = { useStyle(activeDetail, it) },
        )
        previewTarget?.let { target ->
            DiscoverPreviewDialog(
                target = target,
                onDismiss = { previewTarget = null },
                isFavorite = discoverSource(target.item) in favoriteSources,
                onFavorite = { toggleFavorite(target.section, target.item) },
                onMoodboard = { addToMoodboard(target.section, target.item) },
                onUseStyle = { useStyle(target.section, target.item) },
            )
        }
        return
    }
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        ScreenHeaderPills(title = stringResource(R.string.discover_styles_title), trailing = null)
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item { DiscoverClusterTabs(clusters = clusters, selected = selectedCluster, onSelect = { selectedCluster = it }) }
            items(sections, key = { it.id }) { section ->
                DiscoverSectionRow(
                    section = section,
                    onSeeAll = {
                        previewTarget = null
                        detailSection = section
                    },
                    onPreview = { openPreview(section, it) },
                    favoriteSources = favoriteSources,
                    onFavorite = { toggleFavorite(section, it) },
                    onMoodboard = { addToMoodboard(section, it) },
                    onUseStyle = { useStyle(section, it) },
                )
            }
        }
    }
    previewTarget?.let { target ->
        DiscoverPreviewDialog(
            target = target,
            onDismiss = { previewTarget = null },
            isFavorite = discoverSource(target.item) in favoriteSources,
            onFavorite = { toggleFavorite(target.section, target.item) },
            onMoodboard = { addToMoodboard(target.section, target.item) },
            onUseStyle = { useStyle(target.section, target.item) },
        )
    }
}

@Composable
fun ScreenHeaderPills(
    title: String,
    trailing: (@Composable () -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(48.dp)
            .semantics {
                contentDescription = title
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.width(40.dp))
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            letterSpacing = (-0.3).sp,
        )
        Box(Modifier.width(40.dp), contentAlignment = Alignment.CenterEnd) {
            trailing?.invoke()
        }
    }
}

@Composable
fun DiscoverHero(
    section: DiscoverSection?,
    onPreview: (GalleryItem) -> Unit,
) {
    val first = section?.items?.firstOrNull() ?: return
    val sectionTitle = localizedDiscoverSection(section)
    val sectionCluster = localizedDiscoverCluster(section.cluster)
    val firstTitle = localizedGalleryTitle(first)
    ElevatedCard(
        onClick = { onPreview(first) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
    ) {
        Box(Modifier.fillMaxWidth().height(240.dp)) {
            Image(
                painter = painterResource(first.imageRes),
                contentDescription = firstTitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.78f)))))
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(shape = RoundedCornerShape(8.dp), color = StudioPrimaryContainer) {
                    Text(
                        sectionCluster,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = StudioInk,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Text(sectionTitle, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.discover_hero_body), color = Color.White.copy(alpha = 0.84f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverClusterTabs(
    clusters: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        clusters.forEach { cluster ->
            val clusterLabel = localizedDiscoverCluster(cluster)
            val isSelected = selected == cluster
            val chipDescription = stringResource(R.string.a11y_discover_cluster_format, clusterLabel)
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(cluster) },
                label = {
                    Text(
                        clusterLabel,
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StudioAccent,
                    selectedLabelColor = Color.White,
                    containerColor = Color.Transparent,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = StudioLine,
                    selectedBorderColor = Color.Transparent,
                    borderWidth = 1.dp,
                ),
                modifier = Modifier.semantics {
                    this.selected = isSelected
                    contentDescription = chipDescription
                },
            )
        }
    }
}

@Composable
fun DiscoverSectionRow(
    section: DiscoverSection,
    onSeeAll: () -> Unit,
    onPreview: (GalleryItem) -> Unit,
    favoriteSources: Set<String>,
    onFavorite: (GalleryItem) -> Unit,
    onMoodboard: (GalleryItem) -> Unit,
    onUseStyle: (GalleryItem) -> Unit,
) {
    val sectionTitle = localizedDiscoverSection(section)
    val sectionSubtitle = localizedDiscoverSectionSubtitle(section)
    val seeAllDescription = stringResource(R.string.a11y_see_all_format, sectionTitle)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    sectionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.2).sp,
                )
                Text(
                    sectionSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            TextButton(
                onClick = onSeeAll,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.semantics {
                    contentDescription = seeAllDescription
                },
            ) {
                Text(
                    stringResource(R.string.see_all),
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioAccent,
                )
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 1.dp),
        ) {
            items(section.items, key = { it.id }) { item ->
                GalleryCard(
                    item = item,
                    isFavorite = discoverSource(item) in favoriteSources,
                    onClick = { onUseStyle(item) },
                )
            }
        }
    }
}

@Composable
fun DiscoverDetailScreen(
    section: DiscoverSection,
    onBack: () -> Unit,
    onPreview: (GalleryItem) -> Unit,
    favoriteSources: Set<String>,
    onFavorite: (GalleryItem) -> Unit,
    onMoodboard: (GalleryItem) -> Unit,
    onUseStyle: (GalleryItem) -> Unit,
) {
    val sectionTitle = localizedDiscoverSection(section)
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        Row(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Column(Modifier.weight(1f)) {
                Text(sectionTitle, style = MaterialTheme.typography.titleLarge)
            }
            Surface(shape = RoundedCornerShape(10.dp), color = StudioPrimaryContainer, tonalElevation = 2.dp) {
                Text(
                    stringResource(R.string.ideas_count, section.items.size),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = StudioBlue,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(section.items, key = { it.id }) { item ->
                GalleryCard(
                    item = item,
                    isFavorite = discoverSource(item) in favoriteSources,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onUseStyle(item) },
                )
            }
        }
    }
}

@Composable
fun DiscoverPreviewDialog(
    target: DiscoverPreviewTarget,
    onDismiss: () -> Unit,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    onMoodboard: () -> Unit,
    onUseStyle: () -> Unit,
) {
    val item = target.item
    val itemTitle = localizedGalleryTitle(item)
    val itemCategory = localizedGalleryCategory(item.category)
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onUseStyle()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = studioPrimaryButtonColors(),
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(stringResource(R.string.create_with_style), style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onFavorite, shape = RoundedCornerShape(14.dp), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(15.dp), tint = if (isFavorite) StudioGold else Color.Unspecified)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        stringResource(if (isFavorite) R.string.favorited else R.string.favorite),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                OutlinedButton(onClick = onMoodboard, shape = RoundedCornerShape(14.dp), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(15.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        stringResource(R.string.add_to_moodboard),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        },
        title = { Text(itemCategory, style = MaterialTheme.typography.titleMedium) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = itemTitle,
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.92f).clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                )
                Text(itemTitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
        },
        shape = RoundedCornerShape(24.dp),
    )
}

@Composable
fun GalleryCard(
    item: GalleryItem,
    isFavorite: Boolean,
    modifier: Modifier = Modifier.width(164.dp),
    onClick: () -> Unit,
) {
    val itemTitle = localizedGalleryTitle(item)
    val itemCategory = localizedGalleryCategory(item.category)
    val galleryDescription = stringResource(R.string.a11y_gallery_card_format, itemTitle, itemCategory)
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp, pressedElevation = 3.dp),
        modifier = modifier.semantics {
            contentDescription = galleryDescription
            role = Role.Button
        },
    ) {
        Column {
            Box(Modifier.fillMaxWidth().aspectRatio(0.82f)) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = itemTitle,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
                    contentScale = ContentScale.Crop,
                )
                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                            )
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                ) {
                    Text(
                        itemTitle,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Text(
                itemCategory,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
