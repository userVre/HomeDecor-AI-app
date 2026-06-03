package com.ismail.homedecorai.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.content.ContextCompat
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.MainTab
import com.ismail.homedecorai.ui.auth.AuthSheet
import com.ismail.homedecorai.ui.dialogs.FirstLaunchDisclosure
import com.ismail.homedecorai.ui.discover.DiscoverScreen
import com.ismail.homedecorai.ui.paywall.PaywallSheet
import com.ismail.homedecorai.ui.profile.ProfileScreen
import com.ismail.homedecorai.ui.settings.SettingsSheet
import com.ismail.homedecorai.ui.store.DiamondStoreSheet
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.tools.CreateScreen
import com.ismail.homedecorai.ui.tools.ToolsScreen
import com.ismail.homedecorai.ui.utility.createCameraUri
import com.ismail.homedecorai.ui.utility.openAuth
import com.ismail.homedecorai.ui.utility.tabLabelRes

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeDecorApp(
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MaterialExpressiveTheme(
        colorScheme = expressiveLightColorScheme().copy(
            primary = StudioBlue,
            onPrimary = Color.White,
            primaryContainer = StudioPrimaryContainer,
            onPrimaryContainer = StudioInk,
            secondary = StudioMoss,
            tertiary = StudioGold,
            onTertiary = StudioInk,
            tertiaryContainer = StudioProContainer,
            onTertiaryContainer = StudioInk,
            error = StudioRose,
            errorContainer = StudioErrorContainer,
            surface = StudioCanvas,
            surfaceContainer = StudioPaper,
            surfaceContainerHigh = StudioMist,
            background = StudioCanvas,
            onSurface = StudioInk,
            onSurfaceVariant = HomeDecorColors.InkSoft,
            outlineVariant = StudioLine,
        ),
    ) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AppScaffold(
                state = state,
                viewModel = viewModel,
                currentLanguageTag = currentLanguageTag,
                onLanguageSelected = onLanguageSelected,
            )
        }
    }
}

@Composable
private fun AppScaffold(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val modalVisible = state.storeVisible ||
        state.paywallVisible ||
        state.authVisible ||
        state.settingsVisible ||
        !state.disclosureAccepted
    BackHandler(enabled = state.storeVisible) {
        viewModel.closeDiamondStore()
        viewModel.selectTab(MainTab.Tools)
    }
    BackHandler(enabled = state.paywallVisible) {
        viewModel.closePaywall()
        viewModel.selectTab(MainTab.Tools)
    }
    BackHandler(enabled = state.authVisible) {
        viewModel.closeAuth()
    }
    BackHandler(enabled = state.settingsVisible) {
        viewModel.closeSettings()
    }
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (state.selectedTab != MainTab.Create && !modalVisible) {
            NavigationBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                tonalElevation = 0.dp,
                containerColor = StudioPaper,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NavItem(MainTab.Tools, state.selectedTab, Icons.Rounded.Home, stringResource(tabLabelRes(MainTab.Tools)), viewModel::selectTab)
                    NavItem(MainTab.Discover, state.selectedTab, Icons.Rounded.Explore, stringResource(tabLabelRes(MainTab.Discover)), viewModel::selectTab)
                    NavItem(MainTab.Profile, state.selectedTab, Icons.Rounded.Person, stringResource(tabLabelRes(MainTab.Profile)), viewModel::selectTab)
                }
            }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            AnimatedContent(targetState = state.selectedTab, label = "tab") { tab ->
                when (tab) {
                    MainTab.Tools -> ToolsScreen(state = state, viewModel = viewModel)
                    MainTab.Create -> CreateScreen(state = state, viewModel = viewModel)
                    MainTab.Discover -> DiscoverScreen(state = state, viewModel = viewModel)
                    MainTab.Profile -> ProfileScreen(state = state, viewModel = viewModel)
                }
            }

            when {
                state.storeVisible -> {
                    DiamondStoreSheet(
                        state = state,
                        onClose = {
                            viewModel.closeDiamondStore()
                            viewModel.selectTab(MainTab.Tools)
                        },
                        onFulfill = viewModel::fulfillDiamondPurchase,
                        onRetrySync = viewModel::retryPurchaseSync,
                        onDailyRewardClaim = viewModel::claimLocalDailyReward,
                    )
                }
                state.paywallVisible -> {
                    PaywallSheet(
                        state = state,
                        onClose = {
                            viewModel.closePaywall()
                            viewModel.selectTab(MainTab.Tools)
                        },
                        onSubscription = viewModel::syncSubscriptionFromRevenueCat,
                        onRetrySync = viewModel::retryPurchaseSync,
                        onStore = viewModel::openDiamondStore,
                    )
                }
                state.authVisible -> {
                    AuthSheet(
                        onClose = viewModel::closeAuth,
                        onAuth = {
                            openAuth(context)
                            viewModel.closeAuth()
                        },
                    )
                }
                state.settingsVisible -> {
                    SettingsSheet(
                        state = state,
                        onClose = viewModel::closeSettings,
                        onSubscription = viewModel::syncSubscriptionFromRevenueCat,
                        onRetrySync = viewModel::retryPurchaseSync,
                        onFeedback = viewModel::submitSettingsFeedback,
                        onDeleteAccount = viewModel::deleteAccountData,
                        onLogout = viewModel::logOut,
                        currentLanguageTag = currentLanguageTag,
                        onLanguageSelected = onLanguageSelected,
                    )
                }
            }

            if (!state.disclosureAccepted) {
                FirstLaunchDisclosure(onAccept = viewModel::acceptDisclosure)
            }
        }
    }
}

@Composable
private fun NavItem(
    tab: MainTab,
    selectedTab: MainTab,
    icon: ImageVector,
    label: String,
    onSelect: (MainTab) -> Unit,
) {
    val selected = selectedTab == tab
    Column(
        modifier = Modifier
            .width(70.dp)
            .minimumTouchTarget()
            .clip(RoundedCornerShape(20.dp))
            .semantics { this.selected = selected }
            .clickable(role = Role.Tab) { onSelect(tab) }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(30.dp)
                .clip(CircleShape)
                .background(if (selected) StudioPrimaryContainer else Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (selected) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
        )
    }
}

@Composable
private fun ScreenColumn(
    title: String,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            trailing?.invoke()
        }
        content()
    }
}

private data class ImageInputActions(
    val openGallery: () -> Unit,
    val openCamera: () -> Unit,
)

@Composable
private fun rememberImageInputActions(
    onImageSelected: (Uri) -> Unit,
): ImageInputActions {
    val context = LocalContext.current
    val currentOnImageSelected by rememberUpdatedState(onImageSelected)
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let(currentOnImageSelected)
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        val capturedUri = pendingCameraUri
        pendingCameraUri = null
        if (saved && capturedUri != null) {
            currentOnImageSelected(capturedUri)
        }
    }

    fun launchCameraCapture() {
        val uri = createCameraUri(context)
        pendingCameraUri = uri
        runCatching { cameraLauncher.launch(uri) }
            .onFailure { pendingCameraUri = null }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            launchCameraCapture()
        } else {
            pendingCameraUri = null
        }
    }

    return ImageInputActions(
        openGallery = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        openCamera = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCameraCapture()
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
    )
}
