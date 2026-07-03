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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.rounded.OpenInNew
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
import androidx.compose.ui.layout.ContentScale
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
import com.ismail.homedecorai.ui.discover.NetworkImage
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
    onItemClick: (BoardItem) -> Unit = {},
    onToggleFavorite: (BoardItem) -> Unit = {},
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

    if (isDesktop) {
        Row(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag(Strings.TestTags.boardScreen),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                BoardHeader(isDesktop = true)

                if (isGuest) {
                    BoardGuestHero(onSignIn = onSignIn, isDesktop = true)
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
                        isDesktop = true,
                        onSignIn = onSignIn,
                        onNavigateToTools = onNavigateToTools,
                        onNavigateToDiscover = onNavigateToDiscover,
                        onItemClick = onItemClick,
                    )
                    BoardTab.Favorites -> FavoritesSection(
                        items = state.favoriteItems,
                        isGuest = isGuest,
                        isDesktop = true,
                        onSignIn = onSignIn,
                        onNavigateToTools = onNavigateToTools,
                        onNavigateToDiscover = onNavigateToDiscover,
                        onItemClick = onItemClick,
                        onToggleFavorite = onToggleFavorite,
                    )
                    BoardTab.Projects -> ProjectsSection(
                        items = state.projectItems,
                        isGuest = isGuest,
                        isDesktop = true,
                        onSignIn = onSignIn,
                        onNavigateToTools = onNavigateToTools,
                        onNavigateToDiscover = onNavigateToDiscover,
                        onItemClick = onItemClick,
                    )
                }
            }

            BoardPreviewSidebar(
                state = state,
                isGuest = isGuest,
                onSignIn = onSignIn,
                onNavigateToTools = onNavigateToTools,
                onItemClick = onItemClick,
            )
        }
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag(Strings.TestTags.boardScreen),
        ) {
            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            BoardHeader(isDesktop = false)

            if (isGuest) {
                BoardGuestHero(onSignIn = onSignIn, isDesktop = false)
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
                    isDesktop = false,
                    onSignIn = onSignIn,
                    onNavigateToTools = onNavigateToTools,
                    onNavigateToDiscover = onNavigateToDiscover,
                    onItemClick = onItemClick,
                )
                BoardTab.Favorites -> FavoritesSection(
                    items = state.favoriteItems,
                    isGuest = isGuest,
                    isDesktop = false,
                    onSignIn = onSignIn,
                    onNavigateToTools = onNavigateToTools,
                    onNavigateToDiscover = onNavigateToDiscover,
                    onItemClick = onItemClick,
                    onToggleFavorite = onToggleFavorite,
                )
                BoardTab.Projects -> ProjectsSection(
                    items = state.projectItems,
                    isGuest = isGuest,
                    isDesktop = false,
                    onSignIn = onSignIn,
                    onNavigateToTools = onNavigateToTools,
                    onNavigateToDiscover = onNavigateToDiscover,
                    onItemClick = onItemClick,
                )
            }

            Spacer(Modifier.height(navBarBottomPadding()))
        }
    }
}

@Composable
private fun BoardHeader(isDesktop: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.ScreenHorizontal,
                vertical = HomeDecorSpacing.Lg,
            )
            .semantics { heading() },
    ) {
        Text(
            Strings.myBoardTitle,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(HomeDecorSpacing.Xs))
        Text(
            "Your saved designs and favorites",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------
// Guest Hero
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
            Row(
                modifier = Modifier.padding(HomeDecorSpacing.Lg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xl),
            ) {
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

                BoardSampleDesignsPreview(isDesktop = isDesktop)
            }
        } else {
            Column(
                modifier = Modifier.padding(HomeDecorSpacing.Sm),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                BoardSampleDesignsRow()

                Text(
                    Strings.boardGuestHeadline,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Text(
                    Strings.boardGuestSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

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
                        label = Strings.boardGuestBenefitCrossDevice,
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
private fun sampleDesigns() = listOf(
    Quadruple(Strings.boardSampleLivingRoom, "Scandinavian", MaterialTheme.colorScheme.primaryContainer, "images/assets_media_discover_generated_livingroom_livingroom1.webp"),
    Quadruple(Strings.boardSampleBedroom, "Bohemian", MaterialTheme.colorScheme.secondaryContainer, "images/assets_media_discover_generated_bedroom_bedroom1.webp"),
    Quadruple(Strings.boardSampleKitchen, "Minimalist", MaterialTheme.colorScheme.tertiaryContainer, "images/assets_media_discover_generated_kitchen_kitchen1.webp"),
    Quadruple(Strings.boardSampleBathroom, "Modern", MaterialTheme.colorScheme.primaryContainer, "images/assets_media_discover_home_homebathroom.webp"),
    Quadruple(Strings.boardSampleOffice, "Industrial", MaterialTheme.colorScheme.secondaryContainer, "images/assets_media_discover_home_homehomeoffice.webp"),
)

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
private fun BoardSampleDesignsPreview(isDesktop: Boolean) {
    val designs = sampleDesigns()
    Column(
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        designs.forEach { (name, style, containerColor, imageUrl) ->
            BoardDesignPreviewCard(
                name = name,
                style = style,
                containerColor = containerColor,
                imageUrl = imageUrl,
                isDesktop = isDesktop,
            )
        }
    }
}

@Composable
private fun BoardSampleDesignsRow() {
    val designs = sampleDesigns()
    LazyRow(
        contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        items(designs) { (name, style, containerColor, imageUrl) ->
            BoardDesignPreviewCard(
                name = name,
                style = style,
                containerColor = containerColor,
                imageUrl = imageUrl,
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
    imageUrl: String = "",
    isDesktop: Boolean = false,
) {
    val cardWidth = if (isDesktop) 200.dp else 170.dp
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = modifier.width(cardWidth),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(HomeDecorShape.Large),
            ) {
                if (imageUrl.isNotEmpty()) {
                    NetworkImage(
                        url = imageUrl,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        containerColor.copy(alpha = 0.6f),
                                        containerColor.copy(alpha = 0.3f),
                                        MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.4f),
                                    ),
                                ),
                            ),
                    )
                }

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
                    tint = HomeDecorExtra.onGradientText,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    Strings.upgradeV3Headline,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = HomeDecorExtra.onGradientText,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    Strings.upgradeV3TrialBadge,
                    style = MaterialTheme.typography.labelSmall,
                    color = HomeDecorExtra.onGradientTextSubtle,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Tab Row
// ---------------------------------------------------------------------------

@Composable
private fun BoardTabRow(
    selectedTab: BoardTab,
    onTabSelected: (BoardTab) -> Unit,
    isGuest: Boolean,
) {
    val tabs = BoardTab.entries

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
    isDesktop: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onItemClick: (BoardItem) -> Unit,
) {
    Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
        if (!isGuest) {
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
                if (items.isNotEmpty()) {
                    Text(
                        "${items.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
        }

        if (items.isEmpty()) {
            if (isGuest) {
                LockedPreviewRow(onSignIn = onSignIn, isDesktop = isDesktop)
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
                    BoardGeneratedCard(item = item, onClick = { onItemClick(item) })
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
    onItemClick: (BoardItem) -> Unit,
    onToggleFavorite: (BoardItem) -> Unit,
) {
    Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
        if (!isGuest) {
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
                if (items.isNotEmpty()) {
                    Text(
                        "${items.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
        }

        if (items.isEmpty()) {
            if (isGuest) {
                LockedPreviewRow(onSignIn = onSignIn, isDesktop = isDesktop)
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
                    BoardFavoriteCard(
                        item = item,
                        onClick = { onItemClick(item) },
                        onToggleFavorite = { onToggleFavorite(item) },
                    )
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
    onItemClick: (BoardItem) -> Unit,
) {
    Column(Modifier.padding(top = HomeDecorSpacing.Sm)) {
        if (!isGuest) {
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
                if (items.isNotEmpty()) {
                    Text(
                        "${items.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
        }

        if (items.isEmpty()) {
            if (isGuest) {
                LockedPreviewRow(onSignIn = onSignIn, isDesktop = isDesktop)
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
                    BoardProjectCard(item = item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Locked Preview
// ---------------------------------------------------------------------------

@Composable
private fun LockedPreviewRow(onSignIn: () -> Unit, isDesktop: Boolean = false) {
    val previewData = listOf(
        Quadruple(Strings.boardSampleLivingRoom, "Scandinavian", MaterialTheme.colorScheme.primaryContainer, "images/assets_media_discover_generated_livingroom_livingroom1.webp"),
        Quadruple(Strings.boardSampleBedroom, "Bohemian", MaterialTheme.colorScheme.secondaryContainer, "images/assets_media_discover_generated_bedroom_bedroom1.webp"),
        Quadruple(Strings.boardSampleKitchen, "Minimalist", MaterialTheme.colorScheme.tertiaryContainer, "images/assets_media_discover_generated_kitchen_kitchen1.webp"),
        Quadruple(Strings.boardSampleBathroom, "Modern", MaterialTheme.colorScheme.primaryContainer, "images/assets_media_discover_home_homebathroom.webp"),
        Quadruple(Strings.boardSampleOffice, "Industrial", MaterialTheme.colorScheme.secondaryContainer, "images/assets_media_discover_home_homehomeoffice.webp"),
    )

    val cardWidth = if (isDesktop) 220.dp else 170.dp

    LazyRow(
        contentPadding = PaddingValues(horizontal = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Base),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        items(previewData.size) { index ->
            LockedPreviewCard(
                name = previewData[index].first,
                style = previewData[index].second,
                gradientColor = previewData[index].third,
                imageUrl = previewData[index].fourth,
                onClick = onSignIn,
                modifier = Modifier.width(cardWidth),
            )
        }
    }
}

@Composable
private fun LockedPreviewCard(
    name: String,
    style: String,
    gradientColor: Color,
    imageUrl: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardLockedCard, name)),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(HomeDecorShape.Large),
            ) {
                if (imageUrl.isNotEmpty()) {
                    NetworkImage(
                        url = imageUrl,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        gradientColor.copy(alpha = 0.5f),
                                        gradientColor.copy(alpha = 0.2f),
                                        MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                                    ),
                                ),
                            ),
                    )
                }

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
                            Icons.Rounded.FavoriteBorder,
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
// Empty State
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
            .fillMaxHeight()
            .padding(vertical = HomeDecorSpacing.Xxl),
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
                modifier = Modifier.size(72.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
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
// Content cards — Generated, Favorites, Projects
// ---------------------------------------------------------------------------

@Composable
private fun BoardGeneratedCard(item: BoardItem, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .width(160.dp)
            .testTag(Strings.formatTestTag(Strings.TestTags.boardGeneratedCard, item.id))
            .semantics {
                contentDescription = Strings.a11yBoardCard(
                    item.roomType.ifBlank { item.style }.ifBlank { item.toolTitle },
                    "Generated"
                )
            },
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(HomeDecorShape.Large),
            ) {
                val imageUrl = item.imageUrl
                if (!imageUrl.isNullOrBlank()) {
                    NetworkImage(
                        url = imageUrl,
                        contentDescription = item.roomType.ifBlank { item.style }.ifBlank { item.toolTitle },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
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
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    )
                }
                if (item.status == "processing") {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                // Open indicator on hover
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
                            Icons.Rounded.OpenInNew,
                            contentDescription = "Open design",
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            Column(Modifier.padding(HomeDecorSpacing.Md)) {
                Text(
                    item.roomType.ifBlank { item.style }.ifBlank { item.toolTitle },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (item.style.isNotBlank()) {
                    Text(
                        item.style,
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
private fun BoardFavoriteCard(item: BoardItem, onClick: () -> Unit, onToggleFavorite: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .testTag(Strings.formatTestTag(Strings.TestTags.boardFavoriteCard, item.id))
            .semantics {
                contentDescription = Strings.a11yBoardCard(
                    item.roomType.ifBlank { item.style }.ifBlank { item.toolTitle },
                    "Favorite"
                )
            },
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(HomeDecorShape.Large),
            ) {
                val imageUrl = item.imageUrl
                if (!imageUrl.isNullOrBlank()) {
                    NetworkImage(
                        url = imageUrl,
                        contentDescription = item.roomType.ifBlank { item.style }.ifBlank { item.toolTitle },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
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
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    )
                }
                Surface(
                    onClick = {
                        onToggleFavorite()
                    },
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Sm)
                        .size(26.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = "Remove from favorites",
                            modifier = Modifier.padding(5.dp).size(16.dp),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }
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
private fun BoardProjectCard(item: BoardItem, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.testTag(Strings.formatTestTag(Strings.TestTags.boardProjectCard, item.id)),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(HomeDecorShape.Large),
            ) {
                val imageUrl = item.imageUrl
                if (!imageUrl.isNullOrBlank()) {
                    NetworkImage(
                        url = imageUrl,
                        contentDescription = item.toolTitle.ifBlank { item.roomType },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
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
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    )
                }
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
                            Icons.Rounded.FolderOpen,
                            contentDescription = "Open project",
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
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
// Preview Sidebar (desktop) — real stats + recent designs
// ---------------------------------------------------------------------------

@Composable
private fun BoardPreviewSidebar(
    state: BoardScreenState,
    isGuest: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onItemClick: (BoardItem) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight(),
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Base),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
        ) {
            Text(
                "Quick Access",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                SidebarStatCard(
                    icon = Icons.Rounded.Diamond,
                    value = state.generatedItems.size.toString(),
                    label = "Generated",
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    iconTint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                SidebarStatCard(
                    icon = Icons.Rounded.Star,
                    value = state.favoriteItems.size.toString(),
                    label = "Favorites",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                    iconTint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f),
                )
            }

            // Recent designs for signed-in users
            if (!isGuest && state.generatedItems.isNotEmpty()) {
                Text(
                    "Recent Designs",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    state.generatedItems.take(4).forEach { item ->
                        SidebarDesignCard(
                            name = item.roomType.ifBlank { item.style }.ifBlank { item.toolTitle },
                            style = item.style.ifBlank { "AI Design" },
                            imageUrl = item.imageUrl ?: "",
                            status = item.status,
                            onClick = { onItemClick(item) },
                        )
                    }
                }
            }

            // Guest: sample designs
            if (isGuest) {
                Text(
                    "What you can create",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    val sidebarSamples = listOf(
                        Triple("Living Room", "Scandinavian", "images/assets_media_discover_generated_livingroom_livingroom1.webp"),
                        Triple("Bedroom", "Bohemian", "images/assets_media_discover_generated_bedroom_bedroom1.webp"),
                        Triple("Kitchen", "Minimalist", "images/assets_media_discover_generated_kitchen_kitchen1.webp"),
                    )
                    sidebarSamples.forEach { (name, style, img) ->
                        SidebarDesignCard(
                            name = name,
                            style = style,
                            imageUrl = img,
                            status = "ready",
                            onClick = {},
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
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.ButtonHeightMedium),
                ) {
                    Text(
                        Strings.boardGuestCta,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

            // New Design button for signed-in users
            if (!isGuest) {
                Spacer(Modifier.height(HomeDecorSpacing.Xs))
                OutlinedButton(
                    onClick = onNavigateToTools,
                    shape = HomeDecorShape.Button,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        "New Design",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun SidebarStatCard(
    icon: ImageVector,
    value: String,
    label: String,
    containerColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = containerColor,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Md),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = iconTint,
            )
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SidebarDesignCard(
    name: String,
    style: String,
    imageUrl: String,
    status: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(HomeDecorSpacing.Md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Surface(
                shape = HomeDecorShape.Small,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                modifier = Modifier.size(44.dp),
            ) {
                if (imageUrl.isNotEmpty()) {
                    NetworkImage(
                        url = imageUrl,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    style = MaterialTheme.typography.labelLarge,
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

            if (status == "processing") {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            } else {
                Icon(
                    Icons.Rounded.OpenInNew,
                    contentDescription = "Open",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                )
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
