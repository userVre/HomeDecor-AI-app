package com.ismail.homedecorai.ui.board

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
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
            .background(MaterialTheme.colorScheme.background),
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
        )

        Spacer(Modifier.height(HomeDecorSpacing.Md))

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
            .padding(horizontal = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Sm),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )
}

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
            ),
    ) {
        if (isDesktop) {
            Row(
                modifier = Modifier.padding(HomeDecorSpacing.Lg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xl),
            ) {
                // Left: Benefits
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

                    BoardGuestBenefit(Strings.boardGuestBenefit1)
                    BoardGuestBenefit(Strings.boardGuestBenefit2)
                    BoardGuestBenefit(Strings.boardGuestBenefit3)

                    Spacer(Modifier.height(HomeDecorSpacing.Xs))

                    OutlinedButton(
                        onClick = onSignIn,
                        shape = HomeDecorShape.Button,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        modifier = Modifier
                            .height(HomeDecorSpacing.ButtonHeight)
                            .width(200.dp),
                    ) {
                        Text(
                            Strings.profileSignInRegister,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }

                // Right: Sample Design Preview
                BoardSampleDesignsPreview()
            }
        } else {
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

                OutlinedButton(
                    onClick = onSignIn,
                    shape = HomeDecorShape.Button,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.ButtonHeight),
                ) {
                    Text(
                        Strings.profileSignInRegister,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun BoardGuestBenefit(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        Surface(
            shape = HomeDecorShape.Badge,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            modifier = Modifier.size(24.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
            SampleDesignCard(
                name = name,
                style = style,
                containerColor = containerColor,
            )
        }
    }
}

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
            SampleDesignCard(
                name = name,
                style = style,
                containerColor = containerColor,
            )
        }
    }
}

@Composable
private fun SampleDesignCard(
    name: String,
    style: String,
    containerColor: Color,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.width(140.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(HomeDecorShape.Large)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                containerColor.copy(alpha = 0.4f),
                                containerColor.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                            ),
                        ),
                    ),
            ) {
                // Decorative room shapes
                Box(
                    modifier = Modifier
                        .padding(HomeDecorSpacing.Md)
                        .size(40.dp, 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 55.dp, top = 20.dp)
                        .size(55.dp, 35.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                )

                // AI badge
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Sm)
                        .size(24.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
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

@Composable
private fun BoardTabRow(
    selectedTab: BoardTab,
    onTabSelected: (BoardTab) -> Unit,
) {
    val tabs = BoardTab.entries
    val selectedIndex = tabs.indexOf(selectedTab).coerceAtLeast(0)

    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Lg),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xl),
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
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) }
                        .semantics {
                            role = Role.Tab
                            selected = isSelected
                            contentDescription = label
                        }
                        .padding(vertical = HomeDecorSpacing.Sm),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = labelColor,
                        letterSpacing = if (isSelected) 0.sp else 0.2.sp,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Lg)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Lg)
                .height(2.dp),
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val totalWidthPx = constraints.maxWidth.toFloat()
                val tabWidthPx = if (tabs.isNotEmpty()) totalWidthPx / tabs.size else totalWidthPx

                val indicatorOffset by animateFloatAsState(
                    targetValue = selectedIndex * tabWidthPx,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                    label = "tabIndicator",
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f / tabs.size)
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = indicatorOffset
                        }
                        .background(MaterialTheme.colorScheme.primary),
                )
            }
        }
    }
}

@Composable
private fun GeneratedSection(
    items: List<BoardItem>,
    isGuest: Boolean,
    onSignIn: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                Strings.generatedImages,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Base))

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
                contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
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
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                Strings.favoritesSection,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Base))

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
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                Strings.savedProjects,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Base))

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

@Composable
private fun LockedPreviewRow(onSignIn: () -> Unit) {
    val sampleColors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer,
    )
    val sampleLabels = listOf(Strings.boardSampleLivingRoom, Strings.boardSampleBedroom, Strings.boardSampleKitchen)

    LazyRow(
        contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        items(3) { index ->
            LockedPreviewCard(
                gradientColor = sampleColors[index % sampleColors.size],
                label = sampleLabels[index % sampleLabels.size],
                onClick = onSignIn,
            )
        }
    }
}

@Composable
private fun LockedPreviewCard(
    gradientColor: Color,
    label: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.width(140.dp),
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
                                gradientColor.copy(alpha = 0.4f),
                                gradientColor.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.3f),
                            ),
                        ),
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .padding(HomeDecorSpacing.Md)
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 68.dp)
                        .size(70.dp, 30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 70.dp, top = 40.dp)
                        .size(50.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)),
                )

                Surface(
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Lock,
                            contentDescription = Strings.boardPreviewLocked,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            Column(Modifier.padding(HomeDecorSpacing.Sm)) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    Strings.boardSignInToUnlock,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

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
            .height(300.dp),
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

@Composable
private fun BoardGeneratedCard(item: BoardItem) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.width(140.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
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
                        .size(40.dp, 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 60.dp, top = 20.dp)
                        .size(60.dp, 40.dp)
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
                modifier = Modifier.padding(HomeDecorSpacing.Sm),
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
                    .height(110.dp)
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
                        .size(50.dp, 45.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 68.dp, top = 25.dp)
                        .size(45.dp, 35.dp)
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
            Column(Modifier.padding(HomeDecorSpacing.Sm)) {
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
                .height(150.dp),
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
                    .height(110.dp)
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
                        .size(55.dp, 45.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 50.dp, top = 55.dp)
                        .size(75.dp, 35.dp)
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
            Column(Modifier.padding(HomeDecorSpacing.Sm)) {
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
