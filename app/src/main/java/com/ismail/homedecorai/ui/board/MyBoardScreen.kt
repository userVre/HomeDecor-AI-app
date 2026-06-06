package com.ismail.homedecorai.ui.board

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.BoardItem
import com.ismail.homedecorai.FavoriteItem
import com.ismail.homedecorai.GeneratedResult
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.R
import com.ismail.homedecorai.isGeneratedResult
import com.ismail.homedecorai.ui.components.WorkspaceImage
import com.ismail.homedecorai.ui.designviewer.DesignViewerSheet
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.openAuth

enum class BoardTab { Generated, Favorites }

@Composable
fun MyBoardScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    val signedIn = !state.viewer.isGuest || state.signedInName != null
    var selectedTab by remember { mutableStateOf(BoardTab.Generated) }

    Column(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text(
                stringResource(R.string.my_board_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
            )
        }

        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BoardTabChip(
                label = stringResource(R.string.generated_tab),
                icon = Icons.Rounded.Diamond,
                selected = selectedTab == BoardTab.Generated,
                onClick = { selectedTab = BoardTab.Generated },
            )
            BoardTabChip(
                label = stringResource(R.string.favorites_tab),
                icon = Icons.Rounded.Star,
                selected = selectedTab == BoardTab.Favorites,
                onClick = { selectedTab = BoardTab.Favorites },
            )
        }

        Spacer(Modifier.height(12.dp))

        if (!signedIn) {
            BoardLockedState(
                onSignIn = { openAuth(context) },
            )
        } else {
            when (selectedTab) {
                BoardTab.Generated -> GeneratedSection(
                    state = state,
                    viewModel = viewModel,
                )
                BoardTab.Favorites -> FavoritesBoardSection(
                    state = state,
                    viewModel = viewModel,
                )
            }
        }
    }
}

@Composable
private fun BoardTabChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(label, fontWeight = FontWeight.SemiBold, maxLines = 1)
        },
        leadingIcon = {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = StudioBrownBtn,
            selectedLabelColor = Color.White,
            selectedLeadingIconColor = Color.White,
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = StudioLine,
            selectedBorderColor = StudioBrownBtn,
            enabled = true,
            selected = selected,
        ),
    )
}

@Composable
private fun BoardLockedState(
    onSignIn: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.5f.dp, StudioLine),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        Column(
            Modifier.padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = StudioMist,
                modifier = Modifier.size(72.dp),
            ) {
                Icon(
                    Icons.Rounded.Lock,
                    contentDescription = null,
                    modifier = Modifier.padding(18.dp).size(36.dp),
                    tint = StudioLine,
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.sign_in_to_view_board),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.sign_in_to_view_board_body),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onSignIn,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudioBrownBtn),
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.sign_in), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun GeneratedSection(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    val generatedItems = remember(state.workspace.generatedResults) {
        state.workspace.generatedResults
            .filter { it.status != "failed" && (!it.imageUrl.isNullOrBlank() || !it.imageUri.isNullOrBlank()) }
            .sortedByDescending { it.createdAt }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.generated_images),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                stringResource(R.string.see_all),
                color = StudioBrownBtn,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { },
            )
        }
        Spacer(Modifier.height(10.dp))

        if (generatedItems.isEmpty()) {
            EmptyBoardState()
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
private fun GeneratedCard(
    result: BoardItem,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
        modifier = Modifier.width(140.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(StudioMist),
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
                modifier = Modifier.padding(10.dp),
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
) {
    val context = LocalContext.current
    val favorites = remember(state.workspace.favorites) {
        state.workspace.favorites.sortedByDescending { it.createdAt }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.favorites_section),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                stringResource(R.string.see_all),
                color = StudioBrownBtn,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { },
            )
        }
        Spacer(Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.heightIn(max = 600.dp),
        ) {
            items(favorites, key = { it.id }) { favorite ->
                FavoriteBoardCard(
                    favorite = favorite,
                    onClick = {
                        if (favorite.resultId != null) {
                            viewModel.openHistoryResult(favorite.resultId)
                        }
                    },
                    onDelete = {
                        viewModel.removeFavorite(favorite.id)
                        Toast.makeText(context, context.getString(R.string.toast_favorite_removed), Toast.LENGTH_SHORT).show()
                    },
                )
            }
            item {
                AddFavoriteCard()
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
        shape = RoundedCornerShape(16.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(StudioMist),
            ) {
                WorkspaceImage(
                    imageUrl = favorite.imageUrl,
                    imageUri = favorite.imageUri,
                    imageRes = favorite.imageRes,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(26.dp),
                ) {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                        modifier = Modifier.padding(5.dp).size(16.dp),
                        tint = StudioGold,
                    )
                }
            }
            Column(Modifier.padding(10.dp)) {
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
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(1.5f.dp, StudioLine),
        modifier = Modifier.clickable { },
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
                tint = StudioLine,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                stringResource(R.string.add_favorite),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun EmptyBoardState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            shape = CircleShape,
            color = StudioMist,
            modifier = Modifier.size(64.dp),
        ) {
            Icon(
                Icons.Rounded.Diamond,
                contentDescription = null,
                modifier = Modifier.padding(16.dp).size(32.dp),
                tint = StudioLine,
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            stringResource(R.string.no_designs_yet),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            stringResource(R.string.no_designs_body),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
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
