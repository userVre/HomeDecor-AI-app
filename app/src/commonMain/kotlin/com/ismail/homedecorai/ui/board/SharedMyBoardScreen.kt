package com.ismail.homedecorai.ui.board

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.model.BoardScreenState
import com.ismail.homedecorai.model.BoardTab
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*

@Composable
fun SharedMyBoardScreen(
    state: BoardScreenState,
    isGuest: Boolean,
    isPro: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onOpenUpgrade: () -> Unit,
) {
    if (state.isLoading) {
        BoardLoadingContent()
        return
    }

    if (state.error != null) {
        BoardErrorContent(message = state.error)
        return
    }

    val isDesktop = rememberIsDesktop()
    var selectedTab by remember { mutableStateOf(BoardTab.Generated) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.boardScreen),
    ) {
        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        BoardHeader(isDesktop = isDesktop)

        if (isGuest) {
            BoardGuestHero(onSignIn = onSignIn, isDesktop = isDesktop)
        }

        if (!isPro && !isGuest) {
            CompactProBanner(onUpgrade = onOpenUpgrade)
        }

        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        BoardTabRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            isGuest = isGuest,
        )

        when (selectedTab) {
            BoardTab.Generated -> GeneratedSection(
                items = state.generatedItems,
                isGuest = isGuest,
                onSignIn = onSignIn,
                onNavigateToTools = onNavigateToTools,
                onNavigateToDiscover = onNavigateToDiscover,
            )
            BoardTab.Favorites -> FavoritesSection(
                items = state.favoriteItems,
                isGuest = isGuest,
                isDesktop = isDesktop,
                onSignIn = onSignIn,
                onNavigateToTools = onNavigateToTools,
                onNavigateToDiscover = onNavigateToDiscover,
            )
            BoardTab.Projects -> ProjectsSection(
                items = state.projectItems,
                isGuest = isGuest,
                isDesktop = isDesktop,
                onSignIn = onSignIn,
                onNavigateToTools = onNavigateToTools,
                onNavigateToDiscover = onNavigateToDiscover,
            )
        }

        Spacer(Modifier.height(navBarBottomPadding()))
    }
}

@Composable
private fun BoardHeader(isDesktop: Boolean) {
    Text(
        Strings.myBoardTitle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Lg,
                vertical = HomeDecorSpacing.Sm,
            )
            .semantics { heading() },
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )
}

// ---------------------------------------------------------------------------
// Guest Hero — Desktop: full-width 2-column with benefit cards + preview grid
// ---------------------------------------------------------------------------

@Composable
private fun BoardGuestHero(onSignIn: () -> Unit, isDesktop: Boolean) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Base,
                vertical = HomeDecorSpacing.Sm,
            )
            .testTag(Strings.TestTags.boardGuestHero),
    ) {
        if (isDesktop) {
            // Desktop: 2-column balanced layout
            Row(
                modifier = Modifier.padding(HomeDecorSpacing.Lg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xl),
            ) {
                // Left: headline + benefit grid
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
                ) {
                    Text(
                        Strings.boardGuestHeadline,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        Strings.boardGuestSubtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.height(HomeDecorSpacing.Xs))

                    // 2x2 benefit grid
                    Column(
                        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            BoardBenefitCard(
                                icon = Icons.Rounded.PushPin,
                                title = Strings.boardGuestBenefitSaved,
                                body = Strings.boardGuestBenefitSavedBody,
                                modifier = Modifier.weight(1f),
                            )
                            BoardBenefitCard(
                                icon = Icons.Rounded.Star,
                                title = Strings.boardGuestBenefitFavorites,
                                body = Strings.boardGuestBenefitFavoritesBody,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            BoardBenefitCard(
                                icon = Icons.Rounded.FolderOpen,
                                title = Strings.boardGuestBenefitProjects,
                                body = Strings.boardGuestBenefitProjectsBody,
                                modifier = Modifier.weight(1f),
                            )
                            BoardBenefitCard(
                                icon = Icons.Rounded.PhoneAndroid,
                                title = Strings.boardGuestBenefitCrossDevice,
                                body = Strings.boardGuestBenefitCrossDeviceBody,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    Spacer(Modifier.height(HomeDecorSpacing.Xs))

                    Button(
                        onClick = onSignIn,
                        shape = HomeDecorShape.Button,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        modifier = Modifier
                            .height(HomeDecorSpacing.ButtonHeight)
                            .width(220.dp)
                            .testTag(Strings.TestTags.boardSignInButton),
                    ) {
                        Text(
                            Strings.boardGuestCta,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }

                // Right: realistic design preview grid
                BoardSampleDesignsPreview()
            }
        } else {
            // Mobile: stacked layout with preview row at top
            Column(
                modifier = Modifier.padding(HomeDecorSpacing.Base),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            ) {
                // Sample designs row
                BoardSampleDesignsRow()

                Text(
                    Strings.boardGuestHeadline,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Text(
                    Strings.boardGuestSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                // Compact benefit chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    BoardBenefitChip(
                        icon = Icons.Rounded.PushPin,
                        label = Strings.boardGuestBenefitSaved,
                        modifier = Modifier.weight(1f),
                    )
                    BoardBenefitChip(
                        icon = Icons.Rounded.FolderOpen,
                        label = Strings.boardGuestBenefitProjects,
                        modifier = Modifier.weight(1f),
                    )
                    BoardBenefitChip(
                        icon = Icons.Rounded.PhoneAndroid,
                        label = "Cross-device",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.ButtonHeight),
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
}

// ---------------------------------------------------------------------------
// Benefit components
// ---------------------------------------------------------------------------

@Composable
private fun BoardBenefitCard(
    icon: ImageVector,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(HomeDecorSpacing.Md),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            verticalAlignment = Alignment.Top,
        ) {
            Surface(
                shape = HomeDecorShape.Badge,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.size(36.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    body,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun BoardBenefitChip(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
) {
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
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
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
// Design preview components
// ---------------------------------------------------------------------------

@Composable
private fun BoardSampleDesignsPreview() {
    val sampleDesigns = listOf(
        Triple(Strings.boardSampleLivingRoom, "Scandinavian", MaterialTheme.colorScheme.primaryContainer),
        Triple(Strings.boardSampleBedroom, "Bohemian", MaterialTheme.colorScheme.secondaryContainer),
        Triple(Strings.boardSampleKitchen, "Minimalist", MaterialTheme.colorScheme.tertiaryContainer),
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        sampleDesigns.forEach { (name, style, containerColor) ->
            BoardDesignPreviewCard(
                name = name,
                style = style,
                containerColor = containerColor,
            )
        }
    }
}

@Composable
private fun BoardSampleDesignsRow() {
    val sampleDesigns = listOf(
        Triple(Strings.boardSampleLivingRoom, "Scandinavian", MaterialTheme.colorScheme.primaryContainer),
        Triple(Strings.boardSampleBedroom, "Bohemian", MaterialTheme.colorScheme.secondaryContainer),
        Triple(Strings.boardSampleKitchen, "Minimalist", MaterialTheme.colorScheme.tertiaryContainer),
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        items(sampleDesigns) { (name, style, containerColor) ->
            BoardDesignPreviewCard(
                name = name,
                style = style,
                containerColor = containerColor,
            )
        }
    }
}

@Composable
private fun BoardDesignPreviewCard(
    name: String,
    style: String,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = modifier.width(170.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(HomeDecorShape.Large)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                containerColor.copy(alpha = 0.5f),
                                containerColor.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                            ),
                        ),
                    ),
            ) {
                // Room silhouette: sofa shape
                Box(
                    modifier = Modifier
                        .padding(start = 14.dp, top = 50.dp)
                        .size(60.dp, 28.dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)),
                )
                // Room silhouette: table
                Box(
                    modifier = Modifier
                        .padding(start = 80.dp, top = 55.dp)
                        .size(40.dp, 22.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                // Room silhouette: plant
                Box(
                    modifier = Modifier
                        .padding(start = 130.dp, top = 30.dp)
                        .size(20.dp, 40.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                )
                // Room silhouette: rug
                Box(
                    modifier = Modifier
                        .padding(start = 14.dp, top = 80.dp)
                        .size(130.dp, 12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)),
                )

                // AI sparkle badge
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Sm)
                        .size(26.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            Column(Modifier.padding(HomeDecorSpacing.Sm)) {
                Text(
                    name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    style,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Pro Banner
// ---------------------------------------------------------------------------

@Composable
private fun CompactProBanner(onUpgrade: () -> Unit) {
    Surface(
        shape = HomeDecorShape.Card,
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = HomeDecorSpacing.Base,
                vertical = HomeDecorSpacing.Xxs,
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        ),
                    ),
                    shape = HomeDecorShape.Card,
                )
                .minimumTouchTarget()
                .semantics {
                    contentDescription = Strings.upgradeV3Headline
                    role = Role.Button
                }
                .clickable(onClick = onUpgrade),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = HomeDecorSpacing.CardInternal,
                        vertical = HomeDecorSpacing.Sm,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    Strings.upgradeV3Headline,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    Strings.upgradeV3TrialBadge,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Compact Tab Row — tightly connected to content
// ---------------------------------------------------------------------------

@Composable
private fun BoardTabRow(
    selectedTab: BoardTab,
    onTabSelected: (BoardTab) -> Unit,
    isGuest: Boolean,
) {
    val tabs = BoardTab.entries
    val selectedIndex = tabs.indexOf(selectedTab).coerceAtLeast(0)

    Column(
        Modifier
            .fillMaxWidth()
            .testTag(Strings.TestTags.boardTabRow),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
        ) {
            tabs.forEach { tab ->
                val isSelected = selectedTab == tab
                val labelColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "tabLabel",
                )
                val label = when (tab) {
                    BoardTab.Generated -> Strings.generatedTab
                    BoardTab.Favorites -> Strings.favoritesTab
                    BoardTab.Projects -> Strings.projectsTab
                }
                Surface(
                    onClick = { onTabSelected(tab) },
                    shape = HomeDecorShape.Pill,
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(Strings.formatTestTag(Strings.TestTags.boardTab, label))
                        .semantics {
                            role = Role.Tab
                            selected = isSelected
                            contentDescription = Strings.a11yBoardTab(label, isSelected)
                        },
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
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

        // Subtle divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        )
    }
}

// ---------------------------------------------------------------------------
// Tab Sections
// ---------------------------------------------------------------------------

@Composable
private fun GeneratedSection(
    items: List<BoardItem>,
    isGuest: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
) {
    Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                Strings.generatedImages,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        if (items.isEmpty()) {
            if (isGuest) {
                LockedPreviewRow(onSignIn = onSignIn)
            } else {
                BoardEmptyState(
                    icon = Icons.Rounded.Diamond,
                    title = Strings.boardEmptyGenerated,
                    body = Strings.boardEmptyGeneratedBody,
                    primaryLabel = Strings.startADesign,
                    onPrimaryClick = onNavigateToTools,
                    secondaryLabel = Strings.exploreDiscover,
                    onSecondaryClick = onNavigateToDiscover,
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Lg),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                items(items, key = { it.id }) { item ->
                    BoardGeneratedCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun FavoritesSection(
    items: List<BoardItem>,
    isGuest: Boolean,
    isDesktop: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
) {
    Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                Strings.favoritesSection,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        if (items.isEmpty()) {
            if (isGuest) {
                LockedPreviewRow(onSignIn = onSignIn)
            } else {
                BoardEmptyState(
                    icon = Icons.Rounded.Star,
                    title = Strings.boardEmptyFavorites,
                    body = Strings.boardEmptyFavoritesBody,
                    primaryLabel = Strings.startADesign,
                    onPrimaryClick = onNavigateToTools,
                    secondaryLabel = Strings.exploreDiscover,
                    onSecondaryClick = onNavigateToDiscover,
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isDesktop) 3 else 2),
                contentPadding = PaddingValues(
                    start = HomeDecorSpacing.Base,
                    end = HomeDecorSpacing.Base,
                    bottom = navBarBottomPadding(),
                ),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                items(items, key = { it.id }) { item ->
                    BoardFavoriteCard(item = item)
                }
                item {
                    AddFavoriteCard(onClick = onNavigateToTools)
                }
            }
        }
    }
}

@Composable
private fun ProjectsSection(
    items: List<BoardItem>,
    isGuest: Boolean,
    isDesktop: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
) {
    Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                Strings.savedProjects,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        if (items.isEmpty()) {
            if (isGuest) {
                LockedPreviewRow(onSignIn = onSignIn)
            } else {
                BoardEmptyState(
                    icon = Icons.Rounded.AutoAwesome,
                    title = Strings.boardEmptyProjects,
                    body = Strings.boardEmptyProjectsBody,
                    primaryLabel = Strings.startADesign,
                    onPrimaryClick = onNavigateToTools,
                    secondaryLabel = Strings.exploreDiscover,
                    onSecondaryClick = onNavigateToDiscover,
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isDesktop) 3 else 2),
                contentPadding = PaddingValues(
                    start = HomeDecorSpacing.Base,
                    end = HomeDecorSpacing.Base,
                    bottom = navBarBottomPadding(),
                ),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                items(items, key = { it.id }) { item ->
                    BoardProjectCard(item = item)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Locked Preview — meaningful previews instead of empty placeholders
// ---------------------------------------------------------------------------

@Composable
private fun LockedPreviewRow(onSignIn: () -> Unit) {
    val previewData = listOf(
        Triple(Strings.boardSampleLivingRoom, "Scandinavian", MaterialTheme.colorScheme.primaryContainer),
        Triple(Strings.boardSampleBedroom, "Bohemian", MaterialTheme.colorScheme.secondaryContainer),
        Triple(Strings.boardSampleKitchen, "Minimalist", MaterialTheme.colorScheme.tertiaryContainer),
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        items(previewData.size) { index ->
            LockedPreviewCard(
                name = previewData[index].first,
                style = previewData[index].second,
                gradientColor = previewData[index].third,
                onClick = onSignIn,
            )
        }
    }
}

@Composable
private fun LockedPreviewCard(
    name: String,
    style: String,
    gradientColor: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.width(170.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(HomeDecorShape.Large)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                gradientColor.copy(alpha = 0.5f),
                                gradientColor.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                            ),
                        ),
                    ),
            ) {
                // Room silhouette shapes — realistic furniture outlines
                // Sofa
                Box(
                    modifier = Modifier
                        .padding(start = 14.dp, top = 55.dp)
                        .size(65.dp, 30.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)),
                )
                // Table
                Box(
                    modifier = Modifier
                        .padding(start = 85.dp, top = 60.dp)
                        .size(45.dp, 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                // Lamp
                Box(
                    modifier = Modifier
                        .padding(start = 140.dp, top = 30.dp)
                        .size(22.dp, 45.dp)
                        .clip(RoundedCornerShape(topStart = 11.dp, topEnd = 11.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                )
                // Rug
                Box(
                    modifier = Modifier
                        .padding(start = 14.dp, top = 90.dp)
                        .size(140.dp, 14.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)),
                )

                // Bottom gradient for text readability
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                ),
                            ),
                        ),
                )

                // "Sign in to unlock" label
                Surface(
                    shape = HomeDecorShape.Pill,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = HomeDecorSpacing.Sm),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Xs),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                    ) {
                        Icon(
                            Icons.Rounded.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                        Text(
                            Strings.boardSignInToUnlock,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
            Column(Modifier.padding(HomeDecorSpacing.Md)) {
                Text(
                    name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    style,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Empty State (for signed-in users with no items)
// ---------------------------------------------------------------------------

@Composable
private fun BoardEmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    body: String,
    primaryLabel: String,
    onPrimaryClick: () -> Unit,
    secondaryLabel: String,
    onSecondaryClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Surface(
                shape = HomeDecorShape.CardLarge,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                modifier = Modifier.size(64.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                body,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            OutlinedButton(
                onClick = onPrimaryClick,
                shape = HomeDecorShape.Button,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(HomeDecorSpacing.Xs))
                Text(
                    primaryLabel,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            TextButton(
                onClick = onSecondaryClick,
                shape = HomeDecorShape.Button,
            ) {
                Icon(
                    Icons.Rounded.Explore,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(HomeDecorSpacing.Xs))
                Text(
                    secondaryLabel,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Content cards (Generated, Favorites, Projects)
// ---------------------------------------------------------------------------

@Composable
private fun BoardGeneratedCard(item: BoardItem) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.width(160.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(HomeDecorShape.Large)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f),
                                    MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.4f),
                                ),
                            ),
                        ),
                )
                Box(
                    modifier = Modifier
                        .padding(HomeDecorSpacing.Md)
                        .size(44.dp, 55.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 64.dp, top = 22.dp)
                        .size(65.dp, 45.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)),
                )
                if (item.status == "processing") {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Text(
                item.roomType.ifBlank { item.style }.ifBlank { item.toolTitle },
                modifier = Modifier.padding(HomeDecorSpacing.Md),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun BoardFavoriteCard(item: BoardItem) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(HomeDecorShape.Large)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f),
                                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.12f),
                                    MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.4f),
                                ),
                            ),
                        ),
                )
                Box(
                    modifier = Modifier
                        .padding(HomeDecorSpacing.Md)
                        .size(55.dp, 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 74.dp, top = 28.dp)
                        .size(50.dp, 40.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)),
                )
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Sm)
                        .size(26.dp),
                ) {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                        modifier = Modifier.padding(5.dp).size(16.dp),
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
            Column(Modifier.padding(HomeDecorSpacing.Md)) {
                Text(
                    item.toolTitle.ifBlank { item.style },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (item.style.isNotBlank() || item.roomType.isNotBlank()) {
                    Text(
                        item.style.ifBlank { item.roomType },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun AddFavoriteCard(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .minimumTouchTarget()
            .semantics {
                contentDescription = Strings.favorite
                role = Role.Button
            },
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(155.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            Text(
                Strings.favorite,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun BoardProjectCard(item: BoardItem) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(HomeDecorShape.Large)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.25f),
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f),
                                    MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.4f),
                                ),
                            ),
                        ),
                )
                Box(
                    modifier = Modifier
                        .padding(HomeDecorSpacing.Md)
                        .size(60.dp, 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 55.dp, top = 60.dp)
                        .size(80.dp, 40.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)),
                )
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(HomeDecorSpacing.Lg)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                )
            }
            Column(Modifier.padding(HomeDecorSpacing.Md)) {
                Text(
                    item.toolTitle.ifBlank { item.roomType },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (item.roomType.isNotBlank()) {
                    Text(
                        item.roomType,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Loading & Error
// ---------------------------------------------------------------------------

@Composable
private fun BoardLoadingContent() {
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
private fun BoardErrorContent(message: String) {
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
