package com.ismail.homedecorai.ui.discover

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
    var selectedCluster by remember { mutableStateOf("interior") }
    var detailSection by remember { mutableStateOf<DiscoverSection?>(null) }
    var previewTarget by remember { mutableStateOf<DiscoverPreviewTarget?>(null) }
    val clusters = listOf("interior", "architecture", "landscape")
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
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ScreenHeaderPills(title = stringResource(R.string.discover_styles_title), trailing = null)
        LazyColumn(
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.ScreenHorizontal,
                end = HomeDecorSpacing.ScreenHorizontal,
                top = HomeDecorSpacing.Xs,
                bottom = navBarBottomPadding(24.dp),
            ),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
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
            .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Sm)
            .height(48.dp)
            .semantics {
                contentDescription = title
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.width(HomeDecorSpacing.Xxl))
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
        shape = HomeDecorShape.CardLarge,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Box(Modifier.fillMaxWidth().height(240.dp)) {
            Image(
                painter = painterResource(first.imageRes),
                contentDescription = firstTitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, HomeDecorExtra.scrim.copy(alpha = 0.88f)))))
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(HomeDecorSpacing.Base),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                Surface(shape = HomeDecorShape.Chip, color = MaterialTheme.colorScheme.primaryContainer) {
                    Text(
                        sectionCluster,
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Text(sectionTitle, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.discover_hero_body), color = Color.White.copy(alpha = 0.84f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun DiscoverClusterTabs(
    clusters: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    val selectedIndex = clusters.indexOf(selected).coerceAtLeast(0)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Xs, vertical = HomeDecorSpacing.Xxs),
    ) {
        val tabWidth = maxWidth / clusters.size
        val animatedOffset by animateDpAsState(
            targetValue = tabWidth * selectedIndex,
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
            label = "tabOffset",
        )
        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .width(tabWidth)
                .height(2.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(1.dp))
                .align(Alignment.BottomCenter),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            clusters.forEachIndexed { index, cluster ->
                val clusterLabel = localizedDiscoverCluster(cluster)
                val isSelected = selected == cluster
                val chipDescription = stringResource(R.string.a11y_discover_cluster_format, clusterLabel)
                Text(
                    text = clusterLabel,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelect(cluster) }
                        .padding(vertical = HomeDecorSpacing.Xs)
                        .semantics {
                            this.selected = isSelected
                            contentDescription = chipDescription
                        },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
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
    val seeAllDescription = stringResource(R.string.a11y_see_all_format, sectionTitle)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val availableWidth = screenWidth - HomeDecorSpacing.ScreenHorizontal * 2
    val cardWidth = ((availableWidth - HomeDecorSpacing.Sm) / 2.15f).coerceAtMost(160.dp).coerceAtLeast(120.dp)

    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                sectionTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.3).sp,
                modifier = Modifier.weight(1f),
            )
            TextButton(
                onClick = onSeeAll,
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                modifier = Modifier
                    .minimumTouchTarget()
                    .semantics {
                        contentDescription = seeAllDescription
                    },
            ) {
                Text(
                    stringResource(R.string.see_all),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            items(section.items, key = { it.id }) { item ->
                GalleryCard(
                    item = item,
                    isFavorite = discoverSource(item) in favoriteSources,
                    modifier = Modifier.width(cardWidth),
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
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Column(Modifier.weight(1f)) {
                Text(sectionTitle, style = MaterialTheme.typography.titleLarge)
            }
            Surface(shape = HomeDecorShape.Badge, color = MaterialTheme.colorScheme.primaryContainer, tonalElevation = 2.dp) {
                Text(
                    stringResource(R.string.ideas_count, section.items.size),
                    modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Sm),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.ScreenHorizontal,
                end = HomeDecorSpacing.ScreenHorizontal,
                top = HomeDecorSpacing.Base,
                bottom = navBarBottomPadding(24.dp),
            ),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
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
                shape = HomeDecorShape.Button,
                colors = studioPrimaryButtonColors(),
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(HomeDecorSpacing.Base))
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(stringResource(R.string.create_with_style), style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onFavorite, shape = HomeDecorShape.Button, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(HomeDecorSpacing.Base), tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else Color.Unspecified)
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        stringResource(if (isFavorite) R.string.favorited else R.string.favorite),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                OutlinedButton(onClick = onMoodboard, shape = HomeDecorShape.Button, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(HomeDecorSpacing.Base))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        stringResource(R.string.add_to_moodboard),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        },
        title = { Text(itemTitle, style = MaterialTheme.typography.titleMedium) },
        text = {
    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = itemTitle,
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.92f).clip(HomeDecorShape.ImageLarge),
                    contentScale = ContentScale.Crop,
                )
                Text(itemTitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
        },
        shape = HomeDecorShape.Dialog,
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
        shape = HomeDecorShape.Card,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp, pressedElevation = 3.dp),
        modifier = modifier.semantics {
            contentDescription = galleryDescription
            role = Role.Button
        },
    ) {
        Box(Modifier.fillMaxWidth().aspectRatio(0.8f)) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = itemTitle,
                modifier = Modifier.fillMaxSize().clip(HomeDecorShape.Card),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
