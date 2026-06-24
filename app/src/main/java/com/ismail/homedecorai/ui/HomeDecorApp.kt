package com.ismail.homedecorai.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ismail.homedecorai.R
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.model.MainTab
import com.ismail.homedecorai.ui.auth.AuthSheet
import com.ismail.homedecorai.ui.board.MyBoardScreen
import com.ismail.homedecorai.ui.designviewer.DesignViewerSheet
import com.ismail.homedecorai.ui.dialogs.FirstLaunchDisclosure
import com.ismail.homedecorai.ui.discover.DiscoverScreen
import com.ismail.homedecorai.ui.paywall.PaywallSheet
import com.ismail.homedecorai.ui.profile.ProfileScreen
import com.ismail.homedecorai.ui.settings.SettingsSheet
import com.ismail.homedecorai.ui.store.DiamondStoreSheet
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.tools.CreateScreen
import com.ismail.homedecorai.ui.tools.ToolsScreen
import com.ismail.homedecorai.ui.upgrade.UpgradeScreen
import com.ismail.homedecorai.ui.utility.openAuth
import com.ismail.homedecorai.ui.utility.openGooglePlayReview
import com.ismail.homedecorai.ui.utility.tabLabelRes

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeDecorApp(
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeDecorTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (!state.isAppReady) {
                BrandingLoadingScreen()
            } else {
                AppScaffold(
                    state = state,
                    viewModel = viewModel,
                    currentLanguageTag = currentLanguageTag,
                    onLanguageSelected = onLanguageSelected,
                )
            }
        }
    }
}

@Composable
private fun BrandingLoadingScreen() {
    var showContent by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        showContent = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (showContent) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "splash_alpha",
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        val loadingDescription = stringResource(R.string.loading_ellipsis)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(80.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            CircularProgressIndicator(
                modifier = Modifier
                    .size(28.dp)
                    .semantics { contentDescription = loadingDescription },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                strokeWidth = 3.dp,
            )
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
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
        state.designViewerVisible ||
        !state.disclosureAccepted
    BackHandler(enabled = state.storeVisible) {
        viewModel.closeDiamondStore()
        viewModel.selectTab(MainTab.Tools)
    }
    BackHandler(enabled = state.paywallVisible) {
        viewModel.closePaywall()
    }
    BackHandler(enabled = state.authVisible) {
        viewModel.closeAuth()
    }
    BackHandler(enabled = state.settingsVisible) {
        viewModel.closeSettings()
    }
    BackHandler(enabled = state.designViewerVisible) {
        viewModel.closeDesignViewer()
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            if (state.selectedTab != MainTab.Create && !modalVisible) {
                HomeDecorNavigationBar(
                    selectedTab = state.selectedTab,
                    onSelectTab = viewModel::selectTab,
                )
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            AnimatedContent(targetState = state.selectedTab, label = "tab") { tab ->
                when (tab) {
                    MainTab.Tools -> ToolsScreen(state = state, viewModel = viewModel)
                    MainTab.Discover -> DiscoverScreen(state = state, viewModel = viewModel)
                    MainTab.Upgrade -> UpgradeScreen(
                        state = state,
                        onOpenPaywall = viewModel::openPaywall,
                    )
                    MainTab.Profile -> ProfileScreen(
                        state = state,
                        viewModel = viewModel,
                    )
                    MainTab.MyBoard -> MyBoardScreen(state = state, viewModel = viewModel)
                    MainTab.Create -> CreateScreen(state = state, viewModel = viewModel)
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
                        onOpenDiamondStore = viewModel::openDiamondStore,
                        currentLanguageTag = currentLanguageTag,
                        onLanguageSelected = onLanguageSelected,
                    )
                }
                state.designViewerVisible -> {
                    DesignViewerSheet(
                        result = state.designViewerResult,
                        onBack = viewModel::closeDesignViewer,
                        onDelete = {
                            state.designViewerResult?.let { viewModel.deleteDesignResult(it) }
                        },
                        onRegenerate = viewModel::closeDesignViewer,
                        onLike = {
                            openGooglePlayReview(context)
                        },
                        onDislike = {},
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
private fun HomeDecorNavigationBar(
    selectedTab: MainTab,
    onSelectTab: (MainTab) -> Unit,
) {
    Column {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
        )
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars),
            tonalElevation = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            NavItem(
                tab = MainTab.Tools,
                selectedTab = selectedTab,
                icon = Icons.Rounded.Home,
                label = stringResource(tabLabelRes(MainTab.Tools)),
                onSelect = onSelectTab,
            )
            NavItem(
                tab = MainTab.Discover,
                selectedTab = selectedTab,
                icon = Icons.Rounded.Explore,
                label = stringResource(tabLabelRes(MainTab.Discover)),
                onSelect = onSelectTab,
            )
            NavItem(
                tab = MainTab.Upgrade,
                selectedTab = selectedTab,
                icon = Icons.Rounded.Stars,
                label = stringResource(tabLabelRes(MainTab.Upgrade)),
                onSelect = onSelectTab,
            )
            NavItem(
                tab = MainTab.MyBoard,
                selectedTab = selectedTab,
                icon = Icons.Rounded.GridView,
                label = stringResource(tabLabelRes(MainTab.MyBoard)),
                onSelect = onSelectTab,
            )
            NavItem(
                tab = MainTab.Profile,
                selectedTab = selectedTab,
                icon = Icons.Rounded.Person,
                label = stringResource(tabLabelRes(MainTab.Profile)),
                onSelect = onSelectTab,
            )
        }
    }
}

@Composable
private fun RowScope.NavItem(
    tab: MainTab,
    selectedTab: MainTab,
    icon: ImageVector,
    label: String,
    onSelect: (MainTab) -> Unit,
) {
    val selected = selectedTab == tab
    NavigationBarItem(
        selected = selected,
        onClick = { onSelect(tab) },
        modifier = Modifier.heightIn(min = HomeDecorSpacing.TouchTarget),
        icon = {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(22.dp),
            )
        },
        label = {
            Text(
                label,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
    )
}
