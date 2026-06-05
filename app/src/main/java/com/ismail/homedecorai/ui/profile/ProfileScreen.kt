package com.ismail.homedecorai.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ViewQuilt
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.FavoriteItem
import com.ismail.homedecorai.GeneratedResult
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.MainTab
import com.ismail.homedecorai.MoodboardItem
import com.ismail.homedecorai.Project
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.components.*
import com.ismail.homedecorai.ui.dialogs.*
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*

@Composable
fun ProfileScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    val openRealAuth = { openAuth(context) }
    val signedIn = !state.viewer.isGuest || state.signedInName != null
    var createProjectVisible by remember { mutableStateOf(false) }
    var selectedProjectId by remember { mutableStateOf<String?>(null) }
    var selectedProfileTab by remember { mutableStateOf(ProfileWorkspaceTab.Favorites) }
    val selectedProject = state.workspace.projects.firstOrNull { it.id == selectedProjectId }
    fun openDiscoverSource(source: String): Boolean {
        val target = discoverTargetForSource(source) ?: return false
        viewModel.useDiscoverStyle(target.item, target.section)
        return true
    }
    if (createProjectVisible) {
        ProjectEditorDialog(
            title = stringResource(R.string.project_create_title),
            confirmLabel = stringResource(R.string.create),
            initialName = "",
            initialRoomType = "",
            initialNotes = "",
            initialStyleInfo = "",
            onDismiss = { createProjectVisible = false },
            onConfirm = { name, roomType, notes, styleInfo ->
                val project = viewModel.createProject(name = name, roomType = roomType, notes = notes, styleInfo = styleInfo)
                selectedProjectId = project.id
                createProjectVisible = false
            },
        )
    }
    if (selectedProject != null) {
        ProjectDetailDialog(
            project = selectedProject,
            state = state,
            onDismiss = { selectedProjectId = null },
            onUpdate = viewModel::updateProject,
            onCreateDesign = {
                selectedProjectId = null
                viewModel.selectTab(MainTab.Tools)
            },
        )
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item("profile-header") {
                SimpleProfileHeader(
                    state = state,
                    signedIn = signedIn,
                    name = state.signedInName,
                    email = state.signedInEmail,
                    onSignIn = openRealAuth,
                    onSettings = viewModel::openSettings,
                    onStore = viewModel::openDiamondStore,
                    onPaywall = viewModel::openPaywall,
                )
            }
            item("profile-daily-reward") {
                DailyRewardCard(
                    state = state,
                    onClaim = viewModel::claimLocalDailyReward,
                )
            }
            item("profile-tabs") {
                ProfileWorkspaceTabs(
                    selected = selectedProfileTab,
                    state = state,
                    onSelect = { selectedProfileTab = it },
                )
            }
            item("profile-tab-content") {
                when (selectedProfileTab) {
                    ProfileWorkspaceTab.Favorites -> FavoritesSection(
                        state = state,
                        onExplore = { viewModel.selectTab(MainTab.Discover) },
                        onTryExample = { viewModel.selectTab(MainTab.Tools) },
                        onOpen = { favorite ->
                            when {
                                favorite.resultId != null -> viewModel.openHistoryResult(favorite.resultId)
                                favorite.sourceType.startsWith("discover:") -> openDiscoverSource(favorite.sourceType)
                                else -> false
                            }
                        },
                        onDelete = { favorite ->
                            viewModel.removeFavorite(favorite.id)
                            Toast.makeText(context, context.getString(R.string.toast_favorite_removed), Toast.LENGTH_SHORT).show()
                        },
                    )
                    ProfileWorkspaceTab.Moodboard -> MoodboardSection(
                        state = state,
                        onExplore = { viewModel.selectTab(MainTab.Discover) },
                        onTryExample = { viewModel.selectTab(MainTab.Tools) },
                        onOpen = { item ->
                            when {
                                item.source.startsWith("generated_result:") -> viewModel.openHistoryResult(item.source.removePrefix("generated_result:"))
                                item.source.startsWith("discover:") -> openDiscoverSource(item.source)
                                else -> false
                            }
                        },
                        onDelete = { item ->
                            viewModel.removeMoodboardItem(item.id)
                            Toast.makeText(context, context.getString(R.string.toast_moodboard_removed), Toast.LENGTH_SHORT).show()
                        },
                    )
                    ProfileWorkspaceTab.History -> PortfolioHistorySection(
                        state = state,
                        onCreate = { viewModel.selectTab(MainTab.Tools) },
                        onExplore = { viewModel.selectTab(MainTab.Discover) },
                        onOpen = { resultId ->
                            val result = state.workspace.generatedResults.firstOrNull { it.id == resultId }
                            if (result != null) {
                                viewModel.openDesignViewer(
                                    com.ismail.homedecorai.BoardItem(
                                        id = result.id,
                                        toolTitle = result.toolTitle,
                                        imageUrl = result.imageUrl,
                                        imageUri = result.imageUri,
                                        roomType = result.roomType,
                                        style = result.style,
                                        imageRes = 0,
                                        status = result.status,
                                    )
                                )
                                true
                            } else {
                                false
                            }
                        },
                        onFavorite = viewModel::toggleHistoryFavorite,
                        onSaveToProject = viewModel::saveHistoryResultToProject,
                        onDelete = viewModel::deleteHistoryResult,
                    )
                    ProfileWorkspaceTab.Projects -> ProjectsWorkspaceSection(
                        state = state,
                        onCreateProject = { createProjectVisible = true },
                        onOpenProject = { selectedProjectId = it.id },
                        onCreateDesign = { viewModel.selectTab(MainTab.Tools) },
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleProfileHeader(
    state: HomeDecorUiState,
    signedIn: Boolean,
    name: String?,
    email: String?,
    onSignIn: () -> Unit,
    onSettings: () -> Unit,
    onStore: () -> Unit,
    onPaywall: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedButton(
                onClick = onSettings,
                shape = CircleShape,
                modifier = Modifier.height(44.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
            ) {
                Icon(Icons.Rounded.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
            }
            if (!signedIn) {
                Button(
                    onClick = onSignIn,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.height(44.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp),
                ) {
                    Icon(Icons.Rounded.Person, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.sign_in), fontWeight = FontWeight.Bold, maxLines = 1)
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ProfileAvatarPreview(state = state, signedIn = signedIn, modifier = Modifier.size(36.dp))
                    Column {
                        Text(
                            name ?: stringResource(R.string.account_connected),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
        ProfileStatusStrip(
            state = state,
            onStore = onStore,
            onPaywall = onPaywall,
        )
    }
}

@Composable
fun ProfileAvatarPreview(
    state: HomeDecorUiState,
    signedIn: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier, contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = painterResource(R.drawable.profile_workspace),
            contentDescription = stringResource(R.string.profile_photo_preview),
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .border(1.dp, if (state.isPro) StudioGold else StudioLine, CircleShape),
            contentScale = ContentScale.Crop,
        )
        Surface(
            shape = CircleShape,
            color = if (signedIn) StudioPrimaryContainer else StudioPaper,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.86f)),
        ) {
            Icon(
                if (signedIn) Icons.Rounded.Check else Icons.Rounded.Person,
                contentDescription = null,
                modifier = Modifier.padding(3.dp).size(10.dp),
                tint = if (signedIn) StudioBlue else StudioInk,
            )
        }
    }
}

@Composable
fun ProfileStatusStrip(
    state: HomeDecorUiState,
    onStore: () -> Unit,
    onPaywall: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ProfileStatusPill(
            icon = Icons.Rounded.Diamond,
            label = stringResource(R.string.diamonds_count, state.diamonds),
            onClick = onStore,
            modifier = Modifier.weight(1f),
        )
        ProfileStatusPill(
            icon = Icons.Rounded.Star,
            label = if (state.isPro) stringResource(R.string.active_pro) else stringResource(R.string.free_plan),
            onClick = onPaywall,
            modifier = Modifier.weight(1f),
            accent = state.isPro,
        )
    }
}

@Composable
fun ProfileStatusPill(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Boolean = false,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (accent) StudioProContainer else StudioPaper,
        tonalElevation = 1.dp,
        modifier = modifier,
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(icon, null, Modifier.size(17.dp), tint = if (accent) StudioGold else StudioBlue)
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ProfileSignInStateSection(
    signedIn: Boolean,
    email: String?,
    onSignIn: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.Rounded.Lock, title = stringResource(R.string.connection_state))
        ElevatedCard(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(shape = CircleShape, color = if (signedIn) StudioPrimaryContainer else StudioMist) {
                    Icon(if (signedIn) Icons.Rounded.Check else Icons.Rounded.Lock, null, Modifier.padding(10.dp).size(20.dp), tint = if (signedIn) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(Modifier.weight(1f)) {
                    Text(if (signedIn) stringResource(R.string.account_connected) else stringResource(R.string.signed_out), fontWeight = FontWeight.Black)
                    Text(
                        email ?: if (signedIn) stringResource(R.string.profile_signed_in_sync) else stringResource(R.string.local_session_with_device),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (!signedIn) {
                    OutlinedButton(onClick = onSignIn, shape = CircleShape) {
                        Text(stringResource(R.string.connection))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileWorkspaceTabs(
    selected: ProfileWorkspaceTab,
    state: HomeDecorUiState,
    onSelect: (ProfileWorkspaceTab) -> Unit,
) {
    val tabs = listOf(
        ProfileWorkspaceTab.Favorites to state.workspace.favorites.size,
        ProfileWorkspaceTab.Moodboard to state.workspace.moodboardItems.size,
        ProfileWorkspaceTab.History to state.workspace.generatedResults.count { it.status != "failed" && (!it.imageUrl.isNullOrBlank() || !it.imageUri.isNullOrBlank()) },
        ProfileWorkspaceTab.Projects to state.workspace.projects.size,
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(tabs, key = { it.first.name }) { (tab, count) ->
            val active = selected == tab
            FilterChip(
                selected = active,
                onClick = { onSelect(tab) },
                label = {
                    Text(
                        stringResource(R.string.profile_tab_count, profileWorkspaceTabLabel(tab), count),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                },
                leadingIcon = {
                    Icon(profileWorkspaceTabIcon(tab), contentDescription = null, modifier = Modifier.size(17.dp))
                },
                shape = CircleShape,
                modifier = Modifier.heightIn(min = 48.dp),
            )
        }
    }
}

@Composable
fun FavoritesSection(
    state: HomeDecorUiState,
    onExplore: () -> Unit,
    onTryExample: () -> Unit,
    onOpen: (FavoriteItem) -> Boolean,
    onDelete: (FavoriteItem) -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val favorites = remember(state.workspace.favorites) {
        state.workspace.favorites.sortedByDescending { it.createdAt }
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.Rounded.Star, title = stringResource(R.string.profile_favorites_title))
        Text(
            stringResource(R.string.profile_favorites_body),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (favorites.isEmpty()) {
            CollectionEmptyState(
                icon = Icons.Rounded.Star,
                title = stringResource(R.string.empty_favorites_title),
                body = stringResource(R.string.empty_favorites_body),
                primaryLabel = stringResource(R.string.empty_action_explore_discover),
                onPrimary = onExplore,
                secondaryLabel = stringResource(R.string.try_with_example),
                onSecondary = onTryExample,
                samples = sampleFavoriteCards(),
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                favorites.forEach { favorite ->
                    SavedCollectionCard(
                        title = savedFavoriteTitle(favorite),
                        subtitle = savedFavoriteSubtitle(favorite),
                        imageRes = favorite.imageRes,
                        imageUrl = favorite.imageUrl,
                        imageUri = favorite.imageUri,
                        icon = Icons.Rounded.Star,
                        actionLabel = stringResource(if (favorite.resultId != null) R.string.open else R.string.create_with_style),
                        onAction = {
                            if (!onOpen(favorite)) {
                                Toast.makeText(context, resources.getString(R.string.history_open_failed), Toast.LENGTH_SHORT).show()
                            }
                        },
                        onDelete = { onDelete(favorite) },
                    )
                }
            }
        }
    }
}

@Composable
fun MoodboardSection(
    state: HomeDecorUiState,
    onExplore: () -> Unit,
    onTryExample: () -> Unit,
    onOpen: (MoodboardItem) -> Boolean,
    onDelete: (MoodboardItem) -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val moodboardItems = remember(state.workspace.moodboardItems) {
        state.workspace.moodboardItems.sortedWith(compareBy<MoodboardItem> { it.sortOrder }.thenByDescending { it.createdAt })
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.AutoMirrored.Rounded.ViewQuilt, title = stringResource(R.string.profile_moodboard_title))
        Text(
            stringResource(R.string.profile_moodboard_body),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (moodboardItems.isEmpty()) {
            CollectionEmptyState(
                icon = Icons.AutoMirrored.Rounded.ViewQuilt,
                title = stringResource(R.string.empty_moodboard_title),
                body = stringResource(R.string.empty_moodboard_body),
                primaryLabel = stringResource(R.string.empty_action_explore_discover),
                onPrimary = onExplore,
                secondaryLabel = stringResource(R.string.try_with_example),
                onSecondary = onTryExample,
                samples = sampleMoodboardCards(),
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                moodboardItems.forEach { item ->
                    SavedCollectionCard(
                        title = savedMoodboardTitle(item),
                        subtitle = savedMoodboardSubtitle(item),
                        imageRes = item.imageRes,
                        imageUrl = item.imageUrl,
                        imageUri = item.imageUri,
                        colorHex = item.colorHex,
                        icon = Icons.AutoMirrored.Rounded.ViewQuilt,
                        actionLabel = stringResource(if (item.source.startsWith("generated_result:")) R.string.open else R.string.create_with_style),
                        onAction = {
                            if (!onOpen(item)) {
                                Toast.makeText(context, resources.getString(R.string.history_open_failed), Toast.LENGTH_SHORT).show()
                            }
                        },
                        onDelete = { onDelete(item) },
                    )
                }
            }
        }
    }
}

@Composable
fun SavedCollectionCard(
    title: String,
    subtitle: String,
    imageRes: Int,
    imageUrl: String?,
    imageUri: String?,
    icon: ImageVector,
    actionLabel: String,
    onAction: () -> Unit,
    onDelete: (() -> Unit)? = null,
    colorHex: String? = null,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(StudioMist),
            ) {
                SavedCollectionImage(
                    imageRes = imageRes,
                    imageUrl = imageUrl,
                    imageUri = imageUri,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                )
                colorHex?.let { hex ->
                    Surface(
                        shape = CircleShape,
                        color = hex.toComposeColor() ?: StudioPrimaryContainer,
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.78f)),
                        modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).size(28.dp),
                    ) {}
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                        Icon(icon, null, Modifier.padding(6.dp).size(15.dp), tint = StudioBlue)
                    }
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(
                    subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onAction,
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        modifier = Modifier.heightIn(min = 44.dp).weight(1f),
                    ) {
                        Text(actionLabel, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    if (onDelete != null) {
                        OutlinedButton(
                            onClick = onDelete,
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioRose),
                            modifier = Modifier.heightIn(min = 44.dp),
                        ) {
                            Icon(Icons.Rounded.Delete, contentDescription = stringResource(R.string.delete), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionEmptyState(
    icon: ImageVector,
    title: String,
    body: String,
    primaryLabel: String,
    onPrimary: () -> Unit,
    samples: List<SampleCollectionCard>,
    secondaryLabel: String? = null,
    onSecondary: (() -> Unit)? = null,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                    Icon(icon, null, Modifier.padding(11.dp).size(22.dp), tint = StudioBlue)
                }
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(items = samples, key = { it.title }) { sample ->
                    SampleCollectionPreview(sample)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onPrimary,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Icon(Icons.Rounded.Explore, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(primaryLabel, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                if (secondaryLabel != null && onSecondary != null) {
                    OutlinedButton(
                        onClick = onSecondary,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(secondaryLabel, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
fun SampleCollectionPreview(sample: SampleCollectionCard) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioMist,
        modifier = Modifier.width(136.dp),
    ) {
        Column {
            Image(
                painter = painterResource(sample.imageRes),
                contentDescription = sample.title,
                modifier = Modifier.fillMaxWidth().height(112.dp),
                contentScale = ContentScale.Crop,
            )
            Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(sample.title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(sample.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun PortfolioHistorySection(
    state: HomeDecorUiState,
    onCreate: () -> Unit,
    onExplore: () -> Unit,
    onOpen: (String) -> Boolean,
    onFavorite: (String) -> Boolean,
    onSaveToProject: (String) -> com.ismail.homedecorai.Project?,
    onDelete: (String) -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val historyItems = remember(state.workspace.generatedResults) {
        state.workspace.generatedResults
            .filter { it.status != "failed" && (!it.imageUrl.isNullOrBlank() || !it.imageUri.isNullOrBlank()) }
            .sortedByDescending { it.createdAt }
    }
    val favoriteResultIds = remember(state.workspace.favorites) {
        state.workspace.favorites.mapNotNull { it.resultId }.toSet()
    }
    var deleteCandidate by remember { mutableStateOf<GeneratedResult?>(null) }

    deleteCandidate?.let { result ->
        AlertDialog(
            onDismissRequest = { deleteCandidate = null },
            icon = { Icon(Icons.Rounded.Delete, contentDescription = null, tint = StudioRose) },
            title = { Text(stringResource(R.string.delete_history_item_title), fontWeight = FontWeight.Black) },
            text = { Text(stringResource(R.string.delete_history_item_body)) },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(result.id)
                        deleteCandidate = null
                        Toast.makeText(context, resources.getString(R.string.history_deleted), Toast.LENGTH_SHORT).show()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioRose, contentColor = Color.White),
                ) {
                    Text(stringResource(R.string.delete), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { deleteCandidate = null }, shape = CircleShape) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.AutoMirrored.Rounded.ViewQuilt, title = stringResource(R.string.your_design))
        if (historyItems.isEmpty()) {
            CollectionEmptyState(
                icon = Icons.Rounded.Diamond,
                title = stringResource(R.string.empty_history_title),
                body = stringResource(R.string.empty_history_body),
                primaryLabel = stringResource(R.string.try_with_example),
                onPrimary = onCreate,
                secondaryLabel = stringResource(R.string.empty_action_explore_discover),
                onSecondary = onExplore,
                samples = sampleHistoryCards(),
            )
        } else {
            val columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2)
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = columns,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.heightIn(max = 800.dp),
            ) {
                items(
                    count = historyItems.size,
                    key = { historyItems[it].id },
                ) { index ->
                    val result = historyItems[index]
                    val toolTitle = boardToolTitleRes(result.toolTitle)?.let { stringResource(it) } ?: result.toolTitle
                    val styleLabel = result.style.ifBlank { result.palette }
                        .takeIf { it.isNotBlank() }
                        ?.let { localizedOption(it) }
                    val detail = listOfNotNull(styleLabel, result.budgetLabel.takeIf { it.isNotBlank() })
                        .joinToString(" - ")
                        .ifBlank { null }
                    DesignGridCard(
                        result = result,
                        title = toolTitle,
                        subtitle = detail,
                        favorite = result.id in favoriteResultIds,
                        onClick = {
                            if (!onOpen(result.id)) {
                                Toast.makeText(context, resources.getString(R.string.history_open_failed), Toast.LENGTH_SHORT).show()
                            }
                        },
                        onFavorite = {
                            onFavorite(result.id)
                        },
                        onDelete = {
                            deleteCandidate = result
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun DesignGridCard(
    result: GeneratedResult,
    title: String,
    subtitle: String?,
    favorite: Boolean,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(StudioMist),
            ) {
                HistoryThumbnail(result = result, contentDescription = title, modifier = Modifier.fillMaxSize())
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Surface(
                        onClick = onFavorite,
                        shape = CircleShape,
                        color = if (favorite) StudioGold else Color.Black.copy(alpha = 0.35f),
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier.padding(7.dp).size(18.dp),
                            tint = if (favorite) Color.White else Color.White.copy(alpha = 0.85f),
                        )
                    }
                }
                Surface(
                    onClick = onDelete,
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.35f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .size(32.dp),
                ) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = null,
                        modifier = Modifier.padding(7.dp).size(18.dp),
                        tint = Color.White.copy(alpha = 0.85f),
                    )
                }
            }
            Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (subtitle != null) {
                    Text(
                        subtitle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryTimelineGroup(
    section: HistoryTimelineSection,
    favoriteResultIds: Set<String>,
    onOpen: (GeneratedResult) -> Unit,
    onFavorite: (GeneratedResult) -> Unit,
    onSaveToProject: (GeneratedResult) -> Unit,
    onDelete: (GeneratedResult) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 2.dp),
        ) {
            Surface(shape = CircleShape, color = StudioBlue) {
                Box(Modifier.size(9.dp))
            }
            Text(
                historyBucketLabel(section.bucket),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = StudioInk,
            )
            Text(
                section.items.size.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            section.items.forEach { result ->
                HistoryTimelineItem(
                    result = result,
                    favorite = result.id in favoriteResultIds,
                    onOpen = { onOpen(result) },
                    onFavorite = { onFavorite(result) },
                    onSaveToProject = { onSaveToProject(result) },
                    onDelete = { onDelete(result) },
                )
            }
        }
    }
}

@Composable
fun HistoryTimelineItem(
    result: GeneratedResult,
    favorite: Boolean,
    onOpen: () -> Unit,
    onFavorite: () -> Unit,
    onSaveToProject: () -> Unit,
    onDelete: () -> Unit,
) {
    val toolTitle = boardToolTitleRes(result.toolTitle)?.let { stringResource(it) } ?: result.toolTitle
    val styleLabel = result.style.ifBlank { result.palette }
        .takeIf { it.isNotBlank() }
        ?.let { localizedOption(it) }
    val detail = listOfNotNull(styleLabel, result.budgetLabel.takeIf { it.isNotBlank() })
        .joinToString(" - ")
        .ifBlank { stringResource(R.string.history_style_budget_unavailable) }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(StudioMist),
                ) {
                    HistoryThumbnail(result = result, contentDescription = toolTitle, modifier = Modifier.fillMaxSize())
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(toolTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(detail, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(formatHistoryItemDate(result.createdAt), color = StudioInk.copy(alpha = 0.72f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HistoryQuickAction(Icons.Rounded.Visibility, stringResource(R.string.open), onOpen, Modifier.weight(1f))
                HistoryQuickAction(Icons.Rounded.Star, stringResource(if (favorite) R.string.favorited else R.string.favorite), onFavorite, Modifier.weight(1f), active = favorite)
                HistoryQuickAction(Icons.Rounded.Save, stringResource(R.string.project), onSaveToProject, Modifier.weight(1f))
                HistoryQuickAction(Icons.Rounded.Delete, stringResource(R.string.delete), onDelete, Modifier.weight(1f), danger = true)
            }
        }
    }
}

@Composable
fun HistoryThumbnail(
    result: GeneratedResult,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val imageUri = result.imageUri?.takeIf { it.isNotBlank() }?.let(Uri::parse)
    if (imageUri != null) {
        UriOrResourceImage(
            uri = imageUri,
            imageRes = R.drawable.sample_after_luxury,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    } else {
        NetworkOrResourceImage(
            imageUrl = result.imageUrl,
            imageRes = R.drawable.sample_after_luxury,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}

@Composable
fun HistoryQuickAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    danger: Boolean = false,
) {
    val container = when {
        danger -> StudioErrorContainer
        active -> StudioProContainer
        else -> StudioMist
    }
    val content = when {
        danger -> StudioRose
        active -> StudioGold
        else -> StudioInk
    }
    Column(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(shape = CircleShape, color = container) {
            Icon(icon, contentDescription = label, tint = content, modifier = Modifier.padding(6.dp).size(16.dp))
        }
        Spacer(Modifier.height(3.dp))
        Text(
            label,
            color = content,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun PurchasesSection(
    state: HomeDecorUiState,
    onStore: () -> Unit,
    onPaywall: () -> Unit,
    onRetrySync: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.Rounded.Diamond, title = stringResource(R.string.purchases))
        ElevatedCard(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        ) {
            Column(Modifier.padding(6.dp)) {
                ProfileActionRow(
                    icon = Icons.Rounded.Diamond,
                    title = stringResource(R.string.diamonds),
                    subtitle = stringResource(R.string.credits_available, state.diamonds),
                    action = stringResource(R.string.store),
                    onClick = onStore,
                )
                ProfileActionRow(
                    icon = Icons.Rounded.Star,
                    title = stringResource(R.string.pro),
                    subtitle = if (state.isPro) stringResource(R.string.active_pro_access) else stringResource(R.string.clean_exports_and_generations),
                    action = if (state.isPro) stringResource(R.string.manage) else stringResource(R.string.view),
                    onClick = onPaywall,
                )
                state.purchaseMessage?.takeIf { it.isNotBlank() }?.let { message ->
                    PurchaseSyncNotice(
                        message = message,
                        pending = state.pendingPurchaseSync != null,
                        busy = state.purchaseBusy,
                        onRetry = onRetrySync,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun PurchaseSyncNotice(
    message: String,
    pending: Boolean,
    busy: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (pending) StudioErrorContainer else StudioMist,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                if (pending) Icons.Rounded.Refresh else Icons.Rounded.Check,
                contentDescription = null,
                tint = if (pending) StudioRose else StudioBlue,
                modifier = Modifier.size(18.dp),
            )
            Text(
                message,
                modifier = Modifier.weight(1f),
                color = if (pending) StudioRose else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (pending) FontWeight.SemiBold else FontWeight.Normal,
            )
            if (pending) {
                OutlinedButton(
                    onClick = onRetry,
                    enabled = !busy,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(48.dp),
                ) {
                    Text(if (busy) stringResource(R.string.syncing_short) else stringResource(R.string.retry), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileSettingsSection(
    onSettings: () -> Unit,
    onShare: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.Rounded.Settings, title = stringResource(R.string.settings))
        ElevatedCard(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        ) {
            Column(Modifier.padding(6.dp)) {
                ProfileActionRow(
                    icon = Icons.Rounded.Settings,
                    title = stringResource(R.string.preferences),
                    subtitle = stringResource(R.string.preferences_subtitle),
                    action = stringResource(R.string.open),
                    onClick = onSettings,
                )
                ProfileActionRow(
                    icon = Icons.Rounded.Share,
                    title = stringResource(R.string.share_app),
                    subtitle = stringResource(R.string.share_app_subtitle),
                    action = stringResource(R.string.share),
                    onClick = onShare,
                )
            }
        }
    }
}

@Composable
fun ProfileSectionTitle(
    icon: ImageVector,
    title: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Surface(shape = CircleShape, color = StudioPrimaryContainer) {
            Icon(icon, null, Modifier.padding(8.dp).size(18.dp), tint = StudioBlue)
        }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
    }
}

@Composable
fun ProfileActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    action: String,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(subtitle, maxLines = 2, overflow = TextOverflow.Ellipsis) },
        leadingContent = {
            Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                Icon(icon, contentDescription = null, tint = StudioBlue, modifier = Modifier.padding(9.dp).size(20.dp))
            }
        },
        trailingContent = {
            OutlinedButton(
                onClick = onClick,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp),
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(action, fontWeight = FontWeight.Bold, maxLines = 1)
            }
        },
    )
}

@Composable
fun ProjectsWorkspaceSection(
    state: HomeDecorUiState,
    onCreateProject: () -> Unit,
    onOpenProject: (Project) -> Unit,
    onCreateDesign: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ProfileSectionTitle(icon = Icons.Rounded.Layers, title = stringResource(R.string.room_projects))
            FilledIconButton(
                onClick = onCreateProject,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = StudioPrimaryContainer,
                    contentColor = StudioBlue,
                ),
            ) {
                Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.project_create_title))
            }
        }
        Text(
            stringResource(R.string.room_projects_body),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (state.workspace.projects.isEmpty()) {
            EmptyProjects(onCreateProject = onCreateProject, onCreateDesign = onCreateDesign)
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 4.dp),
            ) {
                items(state.workspace.projects, key = { it.id }) { project ->
                    ProjectCard(
                        project = project,
                        state = state,
                        onClick = { onOpenProject(project) },
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyProjects(
    onCreateProject: () -> Unit,
    onCreateDesign: () -> Unit,
) {
    val samples = sampleProjectCards()
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                    Icon(Icons.Rounded.Layers, null, Modifier.padding(11.dp).size(22.dp), tint = StudioBlue)
                }
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.empty_projects_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.empty_projects_body), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(items = samples, key = { it.title }) { sample ->
                    SampleCollectionPreview(sample)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onCreateProject,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.empty_action_start_project), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(
                    onClick = onCreateDesign,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.try_with_example), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    state: HomeDecorUiState,
    onClick: () -> Unit,
) {
    val projectResults = state.workspace.generatedResults.filter { it.projectId == project.id }
    val favoriteCount = state.workspace.favorites.count { it.projectId == project.id }
    val originalCount = project.originalPhotoUris.size + project.originalPhotoUrls.size
    val coverUrl = project.coverImageUrl ?: projectResults.firstOrNull()?.imageUrl
    val coverUri = project.coverImageUri ?: projectResults.firstOrNull()?.imageUri ?: project.originalPhotoUris.firstOrNull()
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        modifier = Modifier.width(260.dp),
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(150.dp)) {
                WorkspaceImage(
                    imageUrl = coverUrl,
                    imageUri = coverUri,
                    contentDescription = project.name,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.62f)))))
                Text(
                    project.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.BottomStart).padding(14.dp),
                )
            }
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    project.roomType.ifBlank { stringResource(R.string.project_room_unspecified) },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProjectMetricChip(Icons.Rounded.PhotoCamera, originalCount.toString())
                    ProjectMetricChip(Icons.AutoMirrored.Rounded.ViewQuilt, projectResults.size.toString())
                    ProjectMetricChip(Icons.Rounded.Star, favoriteCount.toString())
                }
                Text(
                    stringResource(R.string.project_created, formatProjectDate(project.createdAt)),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun ProjectMetricChip(
    icon: ImageVector,
    label: String,
) {
    Surface(shape = CircleShape, color = StudioMist) {
        Row(
            Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(icon, null, Modifier.size(14.dp), tint = StudioBlue)
            Text(label, style = MaterialTheme.typography.labelSmall, color = StudioInk, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProjectHeaderPreview(
    project: Project,
    results: List<com.ismail.homedecorai.GeneratedResult>,
) {
    val coverUrl = project.coverImageUrl ?: results.firstOrNull()?.imageUrl
    val coverUri = project.coverImageUri ?: results.firstOrNull()?.imageUri ?: project.originalPhotoUris.firstOrNull()
    Surface(shape = RoundedCornerShape(24.dp), color = StudioMist, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().height(180.dp)) {
            WorkspaceImage(
                imageUrl = coverUrl,
                imageUri = coverUri,
                contentDescription = project.name,
                modifier = Modifier.fillMaxSize(),
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.68f)))))
            Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(project.name, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(project.roomType.ifBlank { stringResource(R.string.project_room_unspecified) }, color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun ProjectResultThumb(
    title: String,
    imageUrl: String?,
    imageUri: String?,
    imageRes: Int = R.drawable.profile_workspace,
) {
    Surface(shape = RoundedCornerShape(18.dp), color = StudioPaper, tonalElevation = 1.dp, modifier = Modifier.width(124.dp)) {
        Column {
            WorkspaceImage(
                imageUrl = imageUrl,
                imageUri = imageUri,
                imageRes = imageRes,
                contentDescription = title,
                modifier = Modifier.fillMaxWidth().height(104.dp),
            )
            Text(
                title,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun EmptyPortfolio(
    onCreate: () -> Unit,
    onExplore: () -> Unit,
) {
    CollectionEmptyState(
        icon = Icons.Rounded.Diamond,
        title = stringResource(R.string.empty_history_title),
        body = stringResource(R.string.empty_history_body),
        primaryLabel = stringResource(R.string.try_with_example),
        onPrimary = onCreate,
        secondaryLabel = stringResource(R.string.empty_action_explore_discover),
        onSecondary = onExplore,
        samples = sampleHistoryCards(),
    )
}
