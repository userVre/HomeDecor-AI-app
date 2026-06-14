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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.DiscoverSection
import com.ismail.homedecorai.GalleryItem
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorUiState
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
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
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
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.width(54.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        Box(Modifier.width(54.dp), contentAlignment = Alignment.CenterEnd) {
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
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
    ) {
        Box(Modifier.fillMaxWidth().height(260.dp)) {
            Image(
                painter = painterResource(first.imageRes),
                contentDescription = firstTitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.78f)))))
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                    Text(
                        sectionCluster,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = StudioInk,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                    )
                }
                Text(sectionTitle, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text(stringResource(R.string.discover_hero_body), color = Color.White.copy(alpha = 0.84f), style = MaterialTheme.typography.bodyMedium)
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
    val selectedIndex = clusters.indexOf(selected).coerceAtLeast(0)
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        contentColor = StudioBlue,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                color = StudioBlue,
            )
        },
        divider = {},
    ) {
        clusters.forEachIndexed { index, cluster ->
            val clusterLabel = localizedDiscoverCluster(cluster)
            Tab(
                selected = selected == cluster,
                onClick = { onSelect(cluster) },
                text = {
                    Text(
                        clusterLabel,
                        fontWeight = if (selected == cluster) FontWeight.Bold else FontWeight.Medium,
                        color = if (selected == cluster) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(sectionTitle, modifier = Modifier.weight(1f).padding(end = 12.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            OutlinedButton(
                onClick = onSeeAll,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text(stringResource(R.string.see_all))
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Column(Modifier.weight(1f)) {
                Text(sectionTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            }
            Surface(shape = CircleShape, color = StudioPrimaryContainer, tonalElevation = 2.dp) {
                Text(
                    stringResource(R.string.ideas_count, section.items.size),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    color = StudioBlue,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
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
                shape = CircleShape,
                colors = studioPrimaryButtonColors(),
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(17.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.create_with_style))
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onFavorite, shape = CircleShape, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(17.dp), tint = if (isFavorite) StudioGold else Color.Unspecified)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(if (isFavorite) R.string.favorited else R.string.favorite), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(onClick = onMoodboard, shape = CircleShape, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(17.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.add_to_moodboard), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        },
        title = { Text(itemCategory, fontWeight = FontWeight.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = itemTitle,
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.92f).clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop,
                )
                Text(itemTitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        shape = RoundedCornerShape(30.dp),
    )
}

@Composable
fun GalleryCard(
    item: GalleryItem,
    isFavorite: Boolean,
    modifier: Modifier = Modifier.width(196.dp),
    onClick: () -> Unit,
) {
    val itemTitle = localizedGalleryTitle(item)
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        modifier = modifier,
    ) {
        Box(Modifier.fillMaxWidth().height(226.dp)) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = itemTitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
