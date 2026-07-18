package com.ismail.homedecorai.ui.board

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.model.BoardScreenState
import com.ismail.homedecorai.model.BoardTab
import com.ismail.homedecorai.ui.components.ResponsiveDialog
import com.ismail.homedecorai.ui.discover.NetworkImage
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.theme.HomeDecorIconSize
import com.ismail.homedecorai.ui.theme.isReducedMotionEnabled

@Composable
fun SharedMyBoardScreen(
    state: BoardScreenState,
    isGuest: Boolean,
    isPro: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit = {},
    onOpenUpgrade: () -> Unit,
    onItemClick: (BoardItem) -> Unit = {},
    onToggleFavorite: (BoardItem) -> Unit = {},
    signedInName: String? = null,
) {
    if (state.isLoading) {
        BoardLoadingContent()
        return
    }

    if (state.error != null) {
        BoardErrorContent(state.error)
        return
    }

    val isDesktop = rememberIsDesktop()
    val reducedMotion = isReducedMotionEnabled()
    var selectedTab by remember { mutableStateOf(BoardTab.Generated) }
    var renameTarget by remember { mutableStateOf<BoardItem?>(null) }
    var deleteTarget by remember { mutableStateOf<BoardItem?>(null) }

    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.boardScreen),
    ) {
        BoardHeader(isDesktop = isDesktop)

        if (isGuest) {
            GuestHero(onSignIn = onSignIn, isDesktop = isDesktop)
            GuestLocalBanner(isDesktop = isDesktop)
        }

        BoardTabRow(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                if (reducedMotion) {
                    fadeIn(tween(0)) togetherWith fadeOut(tween(0))
                } else {
                    fadeIn(animationSpec = tween(180)) togetherWith fadeOut(animationSpec = tween(150))
                }
            },
            label = "boardTabContent",
        ) { tab ->
            when (tab) {
                BoardTab.Generated -> GeneratedTab(
                    items = state.generatedItems,
                    localDesigns = state.localGuestDesigns,
                    isGuest = isGuest,
                    isDesktop = isDesktop,
                    onSignIn = onSignIn,
                    onNavigateToTools = onNavigateToTools,
                    onItemClick = onItemClick,
                    onToggleFavorite = onToggleFavorite,
                    onRename = { renameTarget = it },
                    onDelete = { deleteTarget = it },
                )
                BoardTab.Favorites -> FavoritesTab(
                    items = state.favoriteItems,
                    isGuest = isGuest,
                    isDesktop = isDesktop,
                    onSignIn = onSignIn,
                    onNavigateToTools = onNavigateToTools,
                    onNavigateToDiscover = onNavigateToDiscover,
                    onItemClick = onItemClick,
                    onToggleFavorite = onToggleFavorite,
                    onRename = { renameTarget = it },
                    onDelete = { deleteTarget = it },
                )
                BoardTab.Projects -> ProjectsTab(
                    items = state.projectItems,
                    isGuest = isGuest,
                    isDesktop = isDesktop,
                    onSignIn = onSignIn,
                    onNavigateToTools = onNavigateToTools,
                    onItemClick = onItemClick,
                    onToggleFavorite = onToggleFavorite,
                    onRename = { renameTarget = it },
                    onDelete = { deleteTarget = it },
                )
            }
        }

        Spacer(Modifier.height(navBarBottomPadding()))
    }

    renameTarget?.let { item ->
        RenameDialog(
            currentName = item.toolTitle.ifBlank { item.roomType }.ifBlank { "Design" },
            onConfirm = { renameTarget = null },
            onDismiss = { renameTarget = null },
        )
    }

    deleteTarget?.let { item ->
        DeleteConfirmDialog(
            title = item.toolTitle.ifBlank { item.roomType }.ifBlank { "this design" },
            onConfirm = { deleteTarget = null },
            onDismiss = { deleteTarget = null },
        )
    }
}

@Composable
private fun BoardHeader(isDesktop: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(
            horizontal = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.ScreenHorizontal,
            vertical = HomeDecorSpacing.Lg,
        ).semantics { heading() },
    ) {
        Text(
            Strings.myBoardTitle,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(HomeDecorSpacing.Xs))
        Text(
            "Your saved designs, favorites, and projects",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun GuestHero(onSignIn: () -> Unit, isDesktop: Boolean) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth().padding(
            horizontal = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.Base,
            vertical = HomeDecorSpacing.Sm,
        ).then(if (isDesktop) Modifier.widthIn(max = 640.dp) else Modifier)
            .testTag(Strings.TestTags.boardGuestHero),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Sm,
                vertical = if (isDesktop) HomeDecorSpacing.Lg else HomeDecorSpacing.Sm,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Text(
                Strings.boardSignInSaveSync,
                style = if (isDesktop) MaterialTheme.typography.titleLarge
                else MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                softWrap = true,
            )
            Text(
                Strings.boardGuestSubtitle,
                style = if (isDesktop) MaterialTheme.typography.bodyMedium
                else MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2,
                softWrap = true,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    if (isDesktop) HomeDecorSpacing.Md else HomeDecorSpacing.Sm,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                GuestBenefitChip(
                    icon = Icons.Rounded.Save,
                    label = Strings.boardGuestBenefitSaved,
                    modifier = Modifier.weight(1f),
                )
                GuestBenefitChip(
                    icon = Icons.Rounded.FolderOpen,
                    label = Strings.boardGuestBenefitProjects,
                    modifier = Modifier.weight(1f),
                )
                GuestBenefitChip(
                    icon = Icons.Rounded.Add,
                    label = Strings.boardGuestBenefitFavorites,
                    modifier = Modifier.weight(1f),
                )
            }
            Button(
                onClick = onSignIn,
                shape = HomeDecorShape.Button,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.fillMaxWidth().height(HomeDecorSpacing.ButtonHeight)
                    .testTag(Strings.TestTags.boardSignInButton),
            ) {
                Text(
                    Strings.boardGuestCta,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun GuestLocalBanner(isDesktop: Boolean) {
    Surface(
        shape = HomeDecorShape.Medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth().padding(
            horizontal = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.Base,
        ).testTag(Strings.TestTags.boardLocalBanner),
    ) {
        Row(
            modifier = Modifier.padding(HomeDecorSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Icon(
                Icons.Rounded.Info,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Small),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    Strings.boardGuestLocalNote,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    Strings.boardGuestSyncNote,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
private fun GuestBenefitChip(icon: ImageVector, label: String, modifier: Modifier = Modifier) {
    Surface(
        shape = HomeDecorShape.Chip,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Small), tint = MaterialTheme.colorScheme.primary)
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Tab Row
// ---------------------------------------------------------------------------

@Composable
private fun BoardTabRow(selectedTab: BoardTab, onTabSelected: (BoardTab) -> Unit) {
    val reducedMotion = isReducedMotionEnabled()
    val animDuration = if (reducedMotion) 0 else 180

    Column(Modifier.fillMaxWidth().testTag(Strings.TestTags.boardTabRow)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
        ) {
            BoardTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab
                val labelColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    animationSpec = tween(animDuration),
                    label = "tabLabel",
                )
                val label = when (tab) {
                    BoardTab.Generated -> Strings.generatedTab
                    BoardTab.Favorites -> Strings.favoritesTab
                    BoardTab.Projects -> Strings.projectsTab
                }
                val tabIcon = when (tab) {
                    BoardTab.Generated -> Icons.Rounded.AutoAwesome
                    BoardTab.Favorites -> Icons.Rounded.FavoriteBorder
                    BoardTab.Projects -> Icons.Rounded.FolderOpen
                }
                val tabDescription = when (tab) {
                    BoardTab.Generated -> Strings.a11yTabGenerated
                    BoardTab.Favorites -> Strings.a11yTabFavorites
                    BoardTab.Projects -> Strings.a11yTabProjects
                }
                Surface(
                    onClick = { onTabSelected(tab) },
                    shape = HomeDecorShape.Pill,
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent,
                    modifier = Modifier.weight(1f)
                        .testTag(Strings.formatTestTag(Strings.TestTags.boardTab, label))
                        .semantics { role = Role.Tab; selected = isSelected
                            contentDescription = tabDescription },
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            tabIcon,
                            contentDescription = null,
                            modifier = Modifier.size(HomeDecorIconSize.Small),
                            tint = labelColor,
                        )
                        Spacer(Modifier.width(HomeDecorSpacing.Xs))
                        Text(
                            label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = labelColor,
                            letterSpacing = if (isSelected) 0.sp else 0.2.sp,
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Base).height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        )
    }
}

// ---------------------------------------------------------------------------
// Tab Content
// ---------------------------------------------------------------------------

@Composable
private fun GeneratedTab(
    items: List<BoardItem>,
    localDesigns: List<BoardItem>,
    isGuest: Boolean,
    isDesktop: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onItemClick: (BoardItem) -> Unit,
    onToggleFavorite: (BoardItem) -> Unit,
    onRename: (BoardItem) -> Unit,
    onDelete: (BoardItem) -> Unit,
) {
    val displayItems = if (isGuest) localDesigns else items

    if (displayItems.isEmpty()) {
        TabEmptyState(
            icon = Icons.Rounded.AutoAwesome,
            title = Strings.boardEmptyGenerated,
            body = Strings.boardEmptyGeneratedBody,
            isGuest = isGuest,
            guestMessage = Strings.boardEmptyGuestGenerated,
            actionLabel = "Open Tools",
            onAction = onNavigateToTools,
            testTag = "generated",
        )
    } else {
        Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
            if (isGuest && localDesigns.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Base),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(Strings.boardLocalDesigns, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text("${localDesigns.size}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(HomeDecorSpacing.Sm))
            } else if (!isGuest) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Base),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(Strings.generatedImages, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text("${items.size}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(HomeDecorSpacing.Sm))
            }

            BoardGrid(
                items = displayItems,
                tab = BoardTab.Generated,
                isGuest = isGuest,
                isDesktop = isDesktop,
                onItemClick = onItemClick,
                onToggleFavorite = onToggleFavorite,
                onRename = onRename,
                onDelete = onDelete,
            )
        }
    }
}

@Composable
private fun FavoritesTab(
    items: List<BoardItem>,
    isGuest: Boolean,
    isDesktop: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onItemClick: (BoardItem) -> Unit,
    onToggleFavorite: (BoardItem) -> Unit,
    onRename: (BoardItem) -> Unit,
    onDelete: (BoardItem) -> Unit,
) {
    if (items.isEmpty()) {
        TabEmptyState(
            icon = Icons.Rounded.FavoriteBorder,
            title = Strings.boardEmptyFavorites,
            body = if (isGuest) Strings.boardEmptyGuestFavorites else Strings.boardEmptyFavoritesBody,
            isGuest = isGuest,
            guestMessage = Strings.boardEmptyGuestFavorites,
            actionLabel = "Explore Discover",
            onAction = onNavigateToDiscover,
            testTag = "favorites",
        )
    } else {
        Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Base),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(Strings.favoritesSection, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("${items.size}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            BoardGrid(
                items = items,
                tab = BoardTab.Favorites,
                isGuest = isGuest,
                isDesktop = isDesktop,
                onItemClick = onItemClick,
                onToggleFavorite = onToggleFavorite,
                onRename = onRename,
                onDelete = onDelete,
            )
        }
    }
}

@Composable
private fun ProjectsTab(
    items: List<BoardItem>,
    isGuest: Boolean,
    isDesktop: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onItemClick: (BoardItem) -> Unit,
    onToggleFavorite: (BoardItem) -> Unit,
    onRename: (BoardItem) -> Unit,
    onDelete: (BoardItem) -> Unit,
) {
    if (items.isEmpty()) {
        TabEmptyState(
            icon = Icons.Rounded.FolderOpen,
            title = Strings.boardEmptyProjects,
            body = if (isGuest) Strings.boardEmptyGuestProjects else Strings.boardEmptyProjectsBody,
            isGuest = isGuest,
            guestMessage = Strings.boardEmptyGuestProjects,
            actionLabel = "Open Tools",
            onAction = onNavigateToTools,
            testTag = "projects",
        )
    } else {
        Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Base),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(Strings.savedProjects, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("${items.size}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            BoardGrid(
                items = items,
                tab = BoardTab.Projects,
                isGuest = isGuest,
                isDesktop = isDesktop,
                onItemClick = onItemClick,
                onToggleFavorite = onToggleFavorite,
                onRename = onRename,
                onDelete = onDelete,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Responsive Grid
// ---------------------------------------------------------------------------

@Composable
private fun BoardGrid(
    items: List<BoardItem>,
    tab: BoardTab,
    isGuest: Boolean,
    isDesktop: Boolean,
    onItemClick: (BoardItem) -> Unit,
    onToggleFavorite: (BoardItem) -> Unit,
    onRename: (BoardItem) -> Unit,
    onDelete: (BoardItem) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = if (isDesktop) 280.dp else 240.dp),
        contentPadding = PaddingValues(
            start = HomeDecorSpacing.Base,
            end = HomeDecorSpacing.Base,
            bottom = HomeDecorSpacing.Sm,
        ),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardGrid, tab.name.lowercase())),
    ) {
        items(items, key = { it.id }) { item ->
            BoardCard(
                item = item,
                tab = tab,
                isGuest = isGuest,
                onClick = { onItemClick(item) },
                onToggleFavorite = { onToggleFavorite(item) },
                onRename = { onRename(item) },
                onDelete = { onDelete(item) },
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Board Card — unified card with context menu actions
// ---------------------------------------------------------------------------

@Composable
private fun BoardCard(
    item: BoardItem,
    tab: BoardTab,
    isGuest: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val testTag = when (tab) {
        BoardTab.Generated -> Strings.TestTags.boardGeneratedCard
        BoardTab.Favorites -> Strings.TestTags.boardFavoriteCard
        BoardTab.Projects -> Strings.TestTags.boardProjectCard
    }
    val subtitle = when {
        item.style.isNotBlank() -> item.style
        item.roomType.isNotBlank() -> item.roomType
        else -> "Design"
    }

    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.testTag(Strings.formatTestTag(testTag, item.id))
            .semantics { contentDescription = Strings.a11yBoardCard(item.toolTitle.ifBlank { subtitle }, subtitle) },
    ) {
        Column {
            // Image area
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp).clip(HomeDecorShape.Large),
            ) {
                val imageUrl = item.imageUrl
                if (!imageUrl.isNullOrBlank()) {
                    NetworkImage(url = imageUrl, contentDescription = subtitle, modifier = Modifier.fillMaxSize())
                } else {
                    // Deliberate fallback swatch — never a blank gray card
                    Box(
                        modifier = Modifier.fillMaxSize().background(
                            Brush.verticalGradient(colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.25f),
                            )),
                        ),
                    ) {
                        Icon(
                            Icons.Rounded.AutoAwesome, contentDescription = null,
                            modifier = Modifier.align(Alignment.Center).size(HomeDecorIconSize.Xl),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        )
                    }
                }
                if (item.status == "processing") {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                if (item.isFavorite && tab != BoardTab.Favorites) {
                    Surface(
                        shape = HomeDecorShape.Badge,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        modifier = Modifier.align(Alignment.TopStart).padding(HomeDecorSpacing.Sm).size(26.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Favorite, contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Small), tint = MaterialTheme.colorScheme.tertiary)
                        }
                    }
                }
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(HomeDecorSpacing.Sm)) {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(26.dp).testTag(Strings.TestTags.boardCardMenu)
                            .semantics { contentDescription = Strings.a11yMoreActions },
                    ) {
                        Icon(
                            Icons.Rounded.MoreVert, contentDescription = null,
                            modifier = Modifier.size(HomeDecorIconSize.Small),
                            tint = if (item.imageUrl.isNullOrBlank()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Open", style = MaterialTheme.typography.bodyMedium) },
                            onClick = { menuExpanded = false; onClick() },
                            leadingIcon = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Small)) },
                            modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardCardMenuItem, "open")),
                        )
                        if (tab == BoardTab.Favorites) {
                            DropdownMenuItem(
                                text = { Text("Remove favorite", style = MaterialTheme.typography.bodyMedium) },
                                onClick = { menuExpanded = false; onToggleFavorite() },
                                leadingIcon = { Icon(Icons.Rounded.Favorite, contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Small)) },
                                modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardCardMenuItem, "unfavorite")),
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Favorite", style = MaterialTheme.typography.bodyMedium) },
                                onClick = { menuExpanded = false; onToggleFavorite() },
                                leadingIcon = {
                                    Icon(
                                        if (item.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                        contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Small),
                                    )
                                },
                                modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardCardMenuItem, "favorite")),
                            )
                        }
                        if (isGuest) {
                            DropdownMenuItem(
                                text = { Text(Strings.boardSave, style = MaterialTheme.typography.bodyMedium) },
                                onClick = { menuExpanded = false },
                                leadingIcon = { Icon(Icons.Rounded.Save, contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Small)) },
                                modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardCardMenuItem, "save")),
                            )
                        }
                        DropdownMenuItem(
                            text = { Text(Strings.boardRename, style = MaterialTheme.typography.bodyMedium) },
                            onClick = { menuExpanded = false; onRename() },
                            leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Small)) },
                            modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardCardMenuItem, "rename")),
                        )
                        DropdownMenuItem(
                            text = { Text(Strings.boardDelete, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error) },
                            onClick = { menuExpanded = false; onDelete() },
                            leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Small), tint = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardCardMenuItem, "delete")),
                        )
                    }
                }
            }

            // Text area
            Column(Modifier.padding(HomeDecorSpacing.Md)) {
                Text(
                    item.toolTitle.ifBlank { item.roomType }.ifBlank { "Design" },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis,
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Empty State
// ---------------------------------------------------------------------------

@Composable
private fun TabEmptyState(
    icon: ImageVector,
    title: String,
    body: String,
    isGuest: Boolean,
    guestMessage: String,
    actionLabel: String,
    onAction: () -> Unit,
    testTag: String,
) {
    Box(
        modifier = Modifier.fillMaxWidth().fillMaxSize().padding(vertical = HomeDecorSpacing.Xxl, horizontal = HomeDecorSpacing.Base),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .testTag(Strings.formatTestTag(Strings.TestTags.boardEmptyState, testTag)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Surface(
                shape = HomeDecorShape.CardLarge,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                modifier = Modifier.size(80.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(HomeDecorIconSize.Xxl), tint = MaterialTheme.colorScheme.primary)
                }
            }
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                body,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = HomeDecorSpacing.Lg),
            )
            if (isGuest && actionLabel.isNotEmpty()) {
                Spacer(Modifier.height(HomeDecorSpacing.Sm))
                Button(
                    onClick = onAction,
                    shape = HomeDecorShape.Button,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier.fillMaxWidth().height(HomeDecorSpacing.ButtonHeight),
                ) {
                    Text(actionLabel, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Dialogs
// ---------------------------------------------------------------------------

@Composable
private fun RenameDialog(
    currentName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(currentName) }
    ResponsiveDialog(
        onDismissRequest = onDismiss,
        title = Strings.boardRenameTitle,
        maxWidth = 400.dp,
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) { Text(Strings.boardCancel) }
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                TextButton(
                    onClick = { onConfirm(name) },
                    modifier = Modifier.testTag(Strings.TestTags.boardRenameConfirm),
                ) { Text(Strings.boardConfirm) }
            }
        },
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(Strings.boardRenameLabel) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag(Strings.TestTags.boardRenameInput),
        )
    }
}

@Composable
private fun DeleteConfirmDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ResponsiveDialog(
        onDismissRequest = onDismiss,
        title = Strings.boardDeleteConfirm,
        maxWidth = 400.dp,
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) { Text(Strings.boardCancel) }
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                TextButton(
                    onClick = onConfirm,
                    modifier = Modifier.testTag(Strings.TestTags.boardDeleteConfirm),
                ) {
                    Text(Strings.boardDelete, color = MaterialTheme.colorScheme.error)
                }
            }
        },
    ) {
        Text(
            Strings.boardDeleteConfirmBody,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------
// Loading & Error
// ---------------------------------------------------------------------------

@Composable
private fun BoardLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(Strings.loadingContent, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun BoardErrorContent(message: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(Strings.errorGeneric, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
