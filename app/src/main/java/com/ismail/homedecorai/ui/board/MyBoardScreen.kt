package com.ismail.homedecorai.ui.board

import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
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
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.model.MainTab
import com.ismail.homedecorai.FavoriteItem
import com.ismail.homedecorai.GeneratedResult
import com.ismail.homedecorai.Project
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.components.WorkspaceImage
import com.ismail.homedecorai.ui.theme.*

enum class BoardTab { Generated, Favorites, Projects }

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyBoardScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val signedIn = !state.viewer.isGuest || state.signedInName != null
    var selectedTab by remember { mutableStateOf(BoardTab.Generated) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        BoardHeader()

        if (!signedIn) {
            BoardSignInBanner(
                onSignIn = { viewModel.openAuth() },
            )
        }

        if (!state.isPro) {
            CompactProBanner(
                onUpgrade = { viewModel.openPaywall() },
            )
        }

        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        BoardTabRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
        )

        Spacer(Modifier.height(HomeDecorSpacing.Md))

        val navigateToTools: () -> Unit = { viewModel.selectTab(MainTab.Tools) }
        val navigateToDiscover: () -> Unit = { viewModel.selectTab(MainTab.Discover) }

        when (selectedTab) {
            BoardTab.Generated -> GeneratedSection(
                state = state,
                viewModel = viewModel,
                signedIn = signedIn,
                onNavigateToTools = navigateToTools,
                onNavigateToDiscover = navigateToDiscover,
                onSignIn = { viewModel.openAuth() },
            )
            BoardTab.Favorites -> FavoritesBoardSection(
                state = state,
                viewModel = viewModel,
                signedIn = signedIn,
                onNavigateToTools = navigateToTools,
                onNavigateToDiscover = navigateToDiscover,
                onSignIn = { viewModel.openAuth() },
            )
            BoardTab.Projects -> ProjectsSection(
                state = state,
                viewModel = viewModel,
                signedIn = signedIn,
                onNavigateToTools = navigateToTools,
                onNavigateToDiscover = navigateToDiscover,
                onSignIn = { viewModel.openAuth() },
            )
        }
    }
}

@Composable
private fun BoardHeader() {
    Text(
        stringResource(R.string.my_board_title),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Sm),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun BoardSignInBanner(
    onSignIn: () -> Unit,
) {
    Surface(
        shape = HomeDecorShape.CardLarge,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = HomeDecorSpacing.Base,
                vertical = HomeDecorSpacing.Xs,
            ),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = HomeDecorSpacing.Base,
                vertical = HomeDecorSpacing.Md,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Icon(
                Icons.Rounded.Lock,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.board_sign_in_cta),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    stringResource(R.string.board_preview_locked),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            OutlinedButton(
                onClick = onSignIn,
                shape = HomeDecorShape.Button,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.height(HomeDecorSpacing.ButtonHeight),
            ) {
                Text(
                    stringResource(R.string.sign_in),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun CompactProBanner(
    onUpgrade: () -> Unit,
) {
    val upgradeDescription = stringResource(R.string.nav_upgrade_pro)
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
                    contentDescription = upgradeDescription
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
                    stringResource(R.string.nav_upgrade_pro),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    stringResource(R.string.board_upgrade_subtitle),
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
                    BoardTab.Generated -> stringResource(R.string.generated_tab)
                    BoardTab.Favorites -> stringResource(R.string.favorites_tab)
                    BoardTab.Projects -> stringResource(R.string.projects_tab)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) }
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Lg)
                .height(2.dp),
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTab == tab
                val indicatorWidth = 1f / tabs.size
                Box(
                    modifier = Modifier
                        .weight(indicatorWidth)
                        .fillMaxSize()
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else Color.Transparent,
                        ),
                )
            }
        }
    }
}

private val samplePreviewResources = listOf(
    R.drawable.assets_media_styles_styleluxury,
    R.drawable.assets_media_styles_stylejapandi,
    R.drawable.assets_media_styles_styleminimalist,
)

@Composable
private fun GeneratedSection(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    signedIn: Boolean,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onSignIn: () -> Unit,
) {
    val generatedItems = remember(state.workspace.generatedResults) {
        state.workspace.generatedResults
            .filter { it.status != "failed" && (!it.imageUrl.isNullOrBlank() || !it.imageUri.isNullOrBlank()) }
            .sortedByDescending { it.createdAt }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.generated_images),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Base))

        if (generatedItems.isEmpty()) {
            if (signedIn) {
                BoardEmptyState(
                    icon = Icons.Rounded.Diamond,
                    title = stringResource(R.string.board_empty_generated),
                    body = stringResource(R.string.board_empty_generated_body),
                    primaryLabel = stringResource(R.string.start_a_design),
                    onPrimaryClick = onNavigateToTools,
                    secondaryLabel = stringResource(R.string.empty_action_explore_discover),
                    onSecondaryClick = onNavigateToDiscover,
                )
            } else {
                LockedPreviewRow(onSignIn = onSignIn)
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                items(generatedItems, key = { it.id }) { result ->
                    val boardItem = result.toBoardItem()
                    GeneratedCard(
                        result = boardItem,
                        onClick = {
                            viewModel.openDesignViewer(boardItem)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun LockedPreviewRow(
    onSignIn: () -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        items(samplePreviewResources) { resId ->
            LockedPreviewCard(imageRes = resId, onClick = onSignIn)
        }
    }
}

@Composable
private fun LockedPreviewCard(
    imageRes: Int,
    onClick: () -> Unit,
) {
    val grayscaleMatrix = remember { ColorMatrix().apply { setToSaturation(0f) } }
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.colorMatrix(grayscaleMatrix),
                    modifier = Modifier.fillMaxSize(),
                )
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Lock,
                            contentDescription = stringResource(R.string.board_preview_locked),
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Text(
                stringResource(R.string.board_preview_locked),
                modifier = Modifier.padding(HomeDecorSpacing.Sm),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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
            .fillMaxHeight(),
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
private fun GeneratedCard(
    result: BoardItem,
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            ) {
                WorkspaceImage(
                    imageUrl = result.imageUrl,
                    imageUri = result.imageUri,
                    imageRes = result.imageRes,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Text(
                result.roomType.ifBlank { result.style },
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
private fun FavoritesBoardSection(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    signedIn: Boolean,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onSignIn: () -> Unit,
) {
    val context = LocalContext.current
    val favorites = remember(state.workspace.favorites) {
        state.workspace.favorites.sortedByDescending { it.createdAt }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.favorites_section),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Base))

        if (favorites.isEmpty()) {
            if (signedIn) {
                BoardEmptyState(
                    icon = Icons.Rounded.Star,
                    title = stringResource(R.string.board_empty_favorites),
                    body = stringResource(R.string.board_empty_favorites_body),
                    primaryLabel = stringResource(R.string.start_a_design),
                    onPrimaryClick = onNavigateToTools,
                    secondaryLabel = stringResource(R.string.empty_action_explore_discover),
                    onSecondaryClick = onNavigateToDiscover,
                )
            } else {
                LockedPreviewRow(onSignIn = onSignIn)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = HomeDecorSpacing.Base, end = HomeDecorSpacing.Base, bottom = navBarBottomPadding()),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                items(favorites, key = { it.id }) { favorite ->
                    val boardItem = favorite.toBoardItem()
                    FavoriteBoardCard(
                        favorite = favorite,
                        onClick = {
                            viewModel.openDesignViewer(boardItem)
                        },
                        onDelete = {
                            viewModel.removeFavorite(favorite.id)
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_favorite_removed),
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                    )
                }
                item {
                    AddFavoriteCard()
                }
            }
        }
    }
}

@Composable
private fun FavoriteBoardCard(
    favorite: FavoriteItem,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        onClick = onClick,
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            ) {
                WorkspaceImage(
                    imageUrl = favorite.imageUrl,
                    imageUri = favorite.imageUri,
                    imageRes = favorite.imageRes,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
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
                    favorite.title.ifBlank { stringResource(R.string.favorite) },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (favorite.style.isNotBlank() || favorite.roomType.isNotBlank()) {
                    Text(
                        favorite.style.ifBlank { favorite.roomType },
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
private fun AddFavoriteCard() {
    val addFavoriteDescription = stringResource(R.string.add_favorite)
    Surface(
        shape = HomeDecorShape.Card,
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .minimumTouchTarget()
            .semantics {
                contentDescription = addFavoriteDescription
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
                contentDescription = stringResource(R.string.add_favorite),
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            Text(
                stringResource(R.string.add_favorite),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

private fun GeneratedResult.toBoardItem(): BoardItem = BoardItem(
    id = id,
    toolTitle = toolTitle,
    style = style,
    roomType = roomType,
    imageRes = R.drawable.sample_after_luxury,
    imageUri = imageUri,
    imageUrl = imageUrl,
    sourceImageUri = sourceImageUri,
    sourceImageUrl = sourceImageUrl,
    status = status,
    errorMessage = errorMessage,
    prompt = prompt,
    budgetLabel = budgetLabel,
    createdAt = createdAt.toDouble(),
)

private fun FavoriteItem.toBoardItem(): BoardItem = BoardItem(
    id = id,
    toolTitle = toolId,
    style = style,
    roomType = roomType,
    imageRes = imageRes,
    imageUri = imageUri,
    imageUrl = imageUrl,
    sourceImageUri = null,
    sourceImageUrl = null,
    status = "completed",
    errorMessage = null,
    prompt = notes,
    budgetLabel = "",
    createdAt = createdAt.toDouble(),
)

@Composable
private fun ProjectsSection(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    signedIn: Boolean,
    onNavigateToTools: () -> Unit,
    onNavigateToDiscover: () -> Unit,
    onSignIn: () -> Unit,
) {
    val projects = remember(state.workspace.projects) {
        state.workspace.projects.sortedByDescending { it.updatedAt }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.saved_projects),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(HomeDecorSpacing.Base))

        if (projects.isEmpty()) {
            if (signedIn) {
                BoardEmptyState(
                    icon = Icons.Rounded.AutoAwesome,
                    title = stringResource(R.string.no_projects_yet),
                    body = stringResource(R.string.no_projects_body),
                    primaryLabel = stringResource(R.string.start_a_design),
                    onPrimaryClick = onNavigateToTools,
                    secondaryLabel = stringResource(R.string.empty_action_explore_discover),
                    onSecondaryClick = onNavigateToDiscover,
                )
            } else {
                LockedPreviewRow(onSignIn = onSignIn)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = HomeDecorSpacing.Base, end = HomeDecorSpacing.Base, bottom = navBarBottomPadding()),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                items(projects, key = { it.id }) { project ->
                    ProjectCard(project = project)
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(
    project: Project,
) {
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            ) {
                if (project.coverImageUri != null || project.coverImageUrl != null) {
                    WorkspaceImage(
                        imageUrl = project.coverImageUrl,
                        imageUri = project.coverImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.padding(HomeDecorSpacing.Lg).size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    )
                }
            }
            Column(Modifier.padding(HomeDecorSpacing.Sm)) {
                Text(
                    project.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (project.roomType.isNotBlank()) {
                    Text(
                        project.roomType,
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
