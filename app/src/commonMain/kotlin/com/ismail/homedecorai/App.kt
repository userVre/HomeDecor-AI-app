package com.ismail.homedecorai

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.model.BoardScreenState
import com.ismail.homedecorai.model.DiscoverScreenState
import com.ismail.homedecorai.model.DiscoverSectionItem
import com.ismail.homedecorai.model.GalleryCardItem
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.model.ToolsScreenState
import com.ismail.homedecorai.ui.ResponsiveLayout
import com.ismail.homedecorai.ui.board.SharedMyBoardScreen
import com.ismail.homedecorai.ui.discover.SharedDiscoverScreen
import com.ismail.homedecorai.ui.paywall.PaywallPlan
import com.ismail.homedecorai.ui.paywall.PaywallState
import com.ismail.homedecorai.ui.paywall.SharedPaywallSheet
import com.ismail.homedecorai.ui.profile.ProfileScreenState
import com.ismail.homedecorai.ui.profile.SharedProfileScreen
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.settings.SettingsLanguage
import com.ismail.homedecorai.ui.settings.SettingsScreenState
import com.ismail.homedecorai.ui.settings.SharedSettingsScreen
import com.ismail.homedecorai.ui.tools.SharedToolsScreen
import com.ismail.homedecorai.ui.tools.WebWizardScreen
import com.ismail.homedecorai.ui.upgrade.SharedUpgradeScreen
import com.ismail.homedecorai.ui.theme.HomeDecorColors
import com.ismail.homedecorai.ui.theme.HomeDecorTheme

private enum class WebTab(val label: String, val icon: ImageVector, val route: String, val pageTitle: String) {
    Tools("Tools", Icons.Rounded.Widgets, "/tools", "Tools - HomeDecor AI"),
    Discover("Discover", Icons.Rounded.Explore, "/discover", "Discover - HomeDecor AI"),
    Board("My Board", Icons.Rounded.Dashboard, "/board", "My Board - HomeDecor AI"),
    Upgrade("Pro", Icons.Rounded.Stars, "/pro", "Pro - HomeDecor AI"),
    Profile("Profile", Icons.Rounded.Person, "/profile", "Profile - HomeDecor AI"),
}

@Composable
fun App() {
    HomeDecorTheme(darkTheme = false, dynamicColor = false) {
        val initialTab = remember {
            val path = getCurrentPathname().removePrefix("/")
            when {
                path.startsWith("create/") -> WebTab.Tools
                path.startsWith("tools") -> WebTab.Tools
                path.startsWith("discover") -> WebTab.Discover
                path.startsWith("board") -> WebTab.Board
                path.startsWith("profile") -> WebTab.Profile
                path.startsWith("pro") -> WebTab.Upgrade
                else -> WebTab.Tools
            }
        }
        var selectedTab by remember { mutableStateOf(initialTab) }
        var paywallVisible by remember { mutableStateOf(false) }
        var settingsVisible by remember { mutableStateOf(false) }
        var activeWizard by remember {
            val path = getCurrentPathname()
            val match = Regex("/create/(\\w+)").find(path)
            mutableStateOf(match?.groupValues?.get(1)?.let { toolId ->
                ToolItem(toolId, "", "", Color(0xFF2E6B6E), Color(0xFF1A4A4C), "images/tool_${toolId}.webp")
            })
        }

        var hasPushedInitial by remember { mutableStateOf(false) }
        LaunchedEffect(selectedTab, activeWizard) {
            val title = if (activeWizard != null) {
                "${Strings.toolTitle(activeWizard!!.id)} - HomeDecor AI"
            } else {
                selectedTab.pageTitle
            }
            setpageTitle(title)
            val path = if (activeWizard != null) {
                "/create/${activeWizard!!.id}"
            } else {
                selectedTab.route
            }
            if (!hasPushedInitial) {
                hasPushedInitial = true
                // Use replaceState on first composition so the initial URL keeps
                // its correct path but gets a history entry for back/forward.
                replaceHistoryState(path, title)
            } else {
                pushHistoryState(path, title)
            }
            announceToScreenReader(title)
        }

        val toolsState = remember {
            ToolsScreenState(
                isPro = false,
                diamonds = 150,
                tools = listOf(
                    ToolItem("interior", "Interior Design", "Redesign any room with AI-powered interior concepts",
                        gradientStart = Color(0xFF2E6B6E), gradientEnd = Color(0xFF1A4A4C),
                        imageUrl = "images/tool_interior.webp"),
                    ToolItem("facade", "Exterior Design", "Transform your home's exterior with modern facade styles",
                        gradientStart = Color(0xFF3B5998), gradientEnd = Color(0xFF1E3A5F),
                        imageUrl = "images/tool_exterior.webp"),
                    ToolItem("garden", "Garden Design", "Plan and visualize your dream garden landscape",
                        gradientStart = Color(0xFF2D6A4F), gradientEnd = Color(0xFF1B4332),
                        imageUrl = "images/tool_garden.webp"),
                    ToolItem("paint", "Smart Wall Paint", "Preview smart paint colors on your walls instantly",
                        gradientStart = Color(0xFFC45B3F), gradientEnd = Color(0xFF8B2E1A),
                        imageUrl = "images/tool_paint.webp"),
                    ToolItem("floor", "Floor Design", "Explore premium flooring from hardwood to marble tile",
                        gradientStart = Color(0xFF8B6914), gradientEnd = Color(0xFF5C4510),
                        imageUrl = "images/tool_floor.webp"),
                    ToolItem("layout", "Layout Makeover", "Optimize room layout for better flow and functionality",
                        gradientStart = Color(0xFF5B4FCF), gradientEnd = Color(0xFF3A2D8F),
                        imageUrl = "images/tool_layout.webp"),
                    ToolItem("replace", "Replace Furniture", "Swap furniture and decor with AI-generated alternatives",
                        gradientStart = Color(0xFFB85C38), gradientEnd = Color(0xFF7A3520),
                        imageUrl = "images/tool_replace.webp"),
                    ToolItem("reference", "Reference Style", "Use any reference image to guide your design direction",
                        gradientStart = Color(0xFF1A3A5C), gradientEnd = Color(0xFF0D2240),
                        imageUrl = "images/tool_reference.webp"),
                ),
            )
        }

        val discoverState = remember {
            DiscoverScreenState(
                favoriteSourceIds = emptySet(),
                selectedCluster = "interior",
                sections = listOf(
                    DiscoverSectionItem("kitchen", "Kitchen", "interior", listOf(
                        GalleryCardItem("kitchen-1", "Kitchen Inspiration", "interior", "images/assets_media_discover_generated_kitchen_kitchen1.webp"),
                        GalleryCardItem("kitchen-2", "Kitchen Design", "interior", "images/assets_media_discover_generated_kitchen_kitchen2.webp"),
                        GalleryCardItem("kitchen-3", "Kitchen Style", "interior", "images/assets_media_discover_generated_kitchen_kitchen3.webp"),
                        GalleryCardItem("kitchen-4", "Modern Kitchen", "interior", "images/assets_media_discover_generated_kitchen_kitchen4.webp"),
                        GalleryCardItem("kitchen-5", "Kitchen Layout", "interior", "images/assets_media_discover_generated_kitchen_kitchen5.webp"),
                        GalleryCardItem("kitchen-6", "Kitchen Ideas", "interior", "images/assets_media_discover_generated_kitchen_kitchen6.webp"),
                        GalleryCardItem("kitchen-7", "Kitchen Makeover", "interior", "images/assets_media_discover_generated_kitchen_kitchen7.webp"),
                    )),
                    DiscoverSectionItem("living-room", "Living Room", "interior", listOf(
                        GalleryCardItem("living-1", "Living Room Inspiration", "interior", "images/assets_media_discover_generated_livingroom_livingroom1.webp"),
                        GalleryCardItem("living-2", "Living Room Design", "interior", "images/assets_media_discover_generated_livingroom_livingroom2.webp"),
                        GalleryCardItem("living-3", "Living Room Style", "interior", "images/assets_media_discover_generated_livingroom_livingroom3.webp"),
                        GalleryCardItem("living-4", "Modern Living", "interior", "images/assets_media_discover_generated_livingroom_livingroom4.webp"),
                        GalleryCardItem("living-5", "Living Room Ideas", "interior", "images/assets_media_discover_generated_livingroom_livingroom5.webp"),
                        GalleryCardItem("living-6", "Cozy Living", "interior", "images/assets_media_discover_generated_livingroom_livingroom6.webp"),
                        GalleryCardItem("living-7", "Living Makeover", "interior", "images/assets_media_discover_generated_livingroom_livingroom7.webp"),
                    )),
                    DiscoverSectionItem("bedroom", "Bedroom", "interior", listOf(
                        GalleryCardItem("bedroom-1", "Bedroom Inspiration", "interior", "images/assets_media_discover_generated_bedroom_bedroom1.webp"),
                        GalleryCardItem("bedroom-2", "Bedroom Design", "interior", "images/assets_media_discover_generated_bedroom_bedroom2.webp"),
                        GalleryCardItem("bedroom-3", "Bedroom Style", "interior", "images/assets_media_discover_generated_bedroom_bedroom3.webp"),
                        GalleryCardItem("bedroom-4", "Modern Bedroom", "interior", "images/assets_media_discover_generated_bedroom_bedroom4.webp"),
                        GalleryCardItem("bedroom-5", "Bedroom Ideas", "interior", "images/assets_media_discover_generated_bedroom_bedroom5.webp"),
                        GalleryCardItem("bedroom-6", "Cozy Bedroom", "interior", "images/assets_media_discover_generated_bedroom_bedroom6.webp"),
                        GalleryCardItem("bedroom-7", "Bedroom Makeover", "interior", "images/assets_media_discover_generated_bedroom_bedroom7.webp"),
                    )),
                    DiscoverSectionItem("bathroom", "Bathroom", "interior", listOf(
                        GalleryCardItem("bathroom-1", "Bathroom Inspiration", "interior", "images/assets_media_discover_home_homebathroom.webp"),
                        GalleryCardItem("bathroom-2", "Bathroom Design", "interior", "images/assets_media_discover_wallscenes_lavendermistbath.webp"),
                        GalleryCardItem("bathroom-3", "Luxury Bathroom", "interior", "images/assets_media_styles_styleluxury.webp"),
                    )),
                    DiscoverSectionItem("office", "Office", "interior", listOf(
                        GalleryCardItem("office-1", "Home Office", "interior", "images/assets_media_discover_home_homehomeoffice.webp"),
                        GalleryCardItem("office-2", "Study Space", "interior", "images/assets_media_discover_home_homestudy.webp"),
                        GalleryCardItem("office-3", "Office Style", "interior", "images/assets_media_styles_stylemidcentury.webp"),
                    )),
                    DiscoverSectionItem("dining", "Dining Room", "interior", listOf(
                        GalleryCardItem("dining-1", "Dining Inspiration", "interior", "images/assets_media_discover_home_homediningroom.webp"),
                        GalleryCardItem("dining-2", "Dining Design", "interior", "images/assets_media_styles_styleartdeco.webp"),
                        GalleryCardItem("dining-3", "Dining Style", "interior", "images/assets_media_styles_stylemediterranean.webp"),
                    )),

                    DiscoverSectionItem("villa", "Villa", "architecture", listOf(
                        GalleryCardItem("villa-1", "Villa Inspiration", "architecture", "images/assets_media_discover_exterior_exteriormodernvilla.webp"),
                        GalleryCardItem("villa-2", "Villa Design", "architecture", "images/assets_media_discover_generated_exterior_exterior1.webp"),
                        GalleryCardItem("villa-3", "Villa Style", "architecture", "images/assets_media_discover_generated_exterior_exterior2.webp"),
                    )),
                    DiscoverSectionItem("house", "House", "architecture", listOf(
                        GalleryCardItem("house-1", "House Inspiration", "architecture", "images/tool_exterior.webp"),
                        GalleryCardItem("house-2", "House Design", "architecture", "images/assets_media_discover_generated_exterior_exterior7.webp"),
                    )),
                    DiscoverSectionItem("apartment", "Apartment", "architecture", listOf(
                        GalleryCardItem("apartment-1", "Apartment Inspiration", "architecture", "images/assets_media_discover_exterior_exteriorapartmentblock.webp"),
                        GalleryCardItem("apartment-2", "Apartment Design", "architecture", "images/assets_media_discover_generated_exterior_exterior3.webp"),
                    )),
                    DiscoverSectionItem("modern-house", "Modern House", "architecture", listOf(
                        GalleryCardItem("modern-house-1", "Modern House Inspiration", "architecture", "images/assets_media_discover_exterior_exteriorglassoffice.webp"),
                        GalleryCardItem("modern-house-2", "Modern House Design", "architecture", "images/assets_media_discover_generated_exterior_exterior4.webp"),
                    )),
                    DiscoverSectionItem("cabin", "Cabin", "architecture", listOf(
                        GalleryCardItem("cabin-1", "Cabin Inspiration", "architecture", "images/assets_media_discover_exterior_exteriorpoolhouse.webp"),
                        GalleryCardItem("cabin-2", "Cabin Design", "architecture", "images/assets_media_discover_exterior_exteriorstonemanor.webp"),
                        GalleryCardItem("cabin-3", "Cabin Style", "architecture", "images/assets_media_discover_generated_exterior_exterior6.webp"),
                    )),

                    DiscoverSectionItem("garden", "Garden", "landscape", listOf(
                        GalleryCardItem("garden-1", "Garden Inspiration", "landscape", "images/assets_media_discover_garden_gardenfiresidepatio.webp"),
                        GalleryCardItem("garden-2", "Garden Design", "landscape", "images/assets_media_discover_generated_garden_garden1.webp"),
                        GalleryCardItem("garden-3", "Garden Style", "landscape", "images/assets_media_discover_generated_garden_garden2.webp"),
                    )),
                    DiscoverSectionItem("patio", "Patio", "landscape", listOf(
                        GalleryCardItem("patio-1", "Patio Inspiration", "landscape", "images/assets_media_discover_garden_gardenpatio.webp"),
                        GalleryCardItem("patio-2", "Patio Design", "landscape", "images/assets_media_discover_generated_garden_garden4.webp"),
                    )),
                    DiscoverSectionItem("pool", "Pool Area", "landscape", listOf(
                        GalleryCardItem("pool-1", "Pool Inspiration", "landscape", "images/assets_media_discover_garden_gardenswimmingpool.webp"),
                        GalleryCardItem("pool-2", "Pool Design", "landscape", "images/assets_media_discover_garden_gardenpoolcourtyard.webp"),
                    )),
                    DiscoverSectionItem("rooftop", "Rooftop", "landscape", listOf(
                        GalleryCardItem("rooftop-1", "Rooftop Inspiration", "landscape", "images/assets_media_discover_garden_gardenterrace.webp"),
                        GalleryCardItem("rooftop-2", "Rooftop Design", "landscape", "images/assets_media_discover_garden_gardendeck.webp"),
                    )),
                    DiscoverSectionItem("balcony", "Balcony", "landscape", listOf(
                        GalleryCardItem("balcony-1", "Balcony Inspiration", "landscape", "images/assets_media_discover_garden_gardenfrontyard.webp"),
                        GalleryCardItem("balcony-2", "Balcony Design", "landscape", "images/assets_media_discover_garden_gardenvillaentry.webp"),
                    )),
                ),
            )
        }

        val profileState = remember {
            ProfileScreenState(
                isGuest = true,
                signedInName = null,
                signedInEmail = null,
                diamonds = 150,
                isPro = false,
                favoritesCount = 0,
            )
        }

        val boardState = remember {
            BoardScreenState(
                generatedItems = emptyList(),
                favoriteItems = emptyList(),
                projectItems = emptyList(),
            )
        }

        val settingsState = remember {
            SettingsScreenState(
                versionName = "1.0.0-web",
                settingsBusy = false,
                isSignedIn = !profileState.isGuest,
                signedInName = profileState.signedInName,
                signedInEmail = profileState.signedInEmail,
                diamonds = profileState.diamonds,
            )
        }

        DisposableEffect(Unit) {
            val unsubscribe = subscribeToNavigationChanges { path ->
                val cleanPath = path.removePrefix("/")
                when {
                    cleanPath.startsWith("create/") -> {
                        val toolId = cleanPath.removePrefix("create/")
                        val tool = toolsState.tools.find { it.id == toolId }
                        if (tool != null) { activeWizard = tool } else { selectedTab = WebTab.Tools; activeWizard = null }
                    }
                    cleanPath.startsWith("tools") -> { selectedTab = WebTab.Tools; activeWizard = null }
                    cleanPath.startsWith("discover") -> { selectedTab = WebTab.Discover; activeWizard = null }
                    cleanPath.startsWith("board") -> { selectedTab = WebTab.Board; activeWizard = null }
                    cleanPath.startsWith("profile") -> { selectedTab = WebTab.Profile; activeWizard = null }
                    cleanPath.startsWith("pro") -> { selectedTab = WebTab.Upgrade; activeWizard = null }
                }
            }
            onDispose { unsubscribe() }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val isDesktop = rememberIsDesktop()

            if (isDesktop) {
                DesktopAppLayout(
                    selectedTab = selectedTab,
                    onSelectTab = { selectedTab = it },
                    paywallVisible = paywallVisible,
                    onPaywallDismiss = { paywallVisible = false },
                    onOpenPaywall = { paywallVisible = true },
                    settingsVisible = settingsVisible,
                    onSettingsDismiss = { settingsVisible = false },
                    onSettingsOpen = { settingsVisible = true },
                    activeWizard = activeWizard,
                    onWizardBack = { activeWizard = null },
                    onToolClick = { tool -> activeWizard = tool },
                    toolsState = toolsState,
                    discoverState = discoverState,
                    profileState = profileState,
                    boardState = boardState,
                    settingsState = settingsState,
                )
            } else {
                ResponsiveLayout {
                    Scaffold(
                        containerColor = Color.Transparent,
                        bottomBar = {
                            if (!settingsVisible && activeWizard == null && !paywallVisible) {
                                WebBottomBar(
                                    selectedTab = selectedTab,
                                    onSelectTab = { selectedTab = it },
                                )
                            }
                        },
                    ) { padding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                        ) {
                            if (activeWizard != null) {
                                Box(Modifier.testTag(Strings.TestTags.wizardScreen)) {
                                    WebWizardScreen(
                                        tool = activeWizard!!,
                                        onBack = { activeWizard = null },
                                    )
                                }
                            } else {
                                AnimatedContent(
                                    targetState = selectedTab,
                                    label = "tab",
                                    transitionSpec = {
                                        fadeIn() + slideInVertically { it / 20 } togetherWith fadeOut() + slideOutVertically { it / 20 }
                                    },
                                ) { tab ->
                                    when (tab) {
                                        WebTab.Tools -> Box(Modifier.testTag(Strings.TestTags.toolsScreen)) {
                                            SharedToolsScreen(
                                                state = toolsState,
                                                onToolClick = { tool -> activeWizard = tool },
                                            )
                                        }
                                        WebTab.Discover -> Box(Modifier.testTag(Strings.TestTags.discoverScreen)) {
                                            SharedDiscoverScreen(
                                                state = discoverState,
                                                onToggleFavorite = { _, _ -> },
                                                onAddToMoodboard = { _, _ -> },
                                                onUseStyle = { _, _ -> },
                                            )
                                        }
                                        WebTab.Board -> Box(Modifier.testTag(Strings.TestTags.boardScreen)) {
                                            SharedMyBoardScreen(
                                                state = boardState,
                                                isGuest = profileState.isGuest,
                                                isPro = toolsState.isPro,
                                                onSignIn = { },
                                                onNavigateToTools = { selectedTab = WebTab.Tools },
                                                onNavigateToDiscover = { selectedTab = WebTab.Discover },
                                                onOpenUpgrade = { paywallVisible = true },
                                            )
                                        }
                                        WebTab.Upgrade -> Box(Modifier.testTag(Strings.TestTags.upgradeScreen)) {
                                            SharedUpgradeScreen(
                                                isPro = false,
                                                onOpenPaywall = { paywallVisible = true },
                                            )
                                        }
                                        WebTab.Profile -> Box(Modifier.testTag(Strings.TestTags.profileScreen)) {
                                            SharedProfileScreen(
                                                state = profileState,
                                                onSettings = { settingsVisible = true },
                                                onSignIn = { },
                                                onOpenDiamonds = { },
                                                onOpenPaywall = { paywallVisible = true },
                                                onOpenBoard = { selectedTab = WebTab.Board },
                                            )
                                        }
                                    }
                                }
                            }

                            if (settingsVisible) {
                                Box(
                                    modifier = Modifier.onPreviewKeyEvent { event ->
                                        if (event.key == Key.Escape) {
                                            settingsVisible = false
                                            true
                                        } else false
                                    }
                                ) {
                                    SharedSettingsScreen(
                                    state = settingsState,
                                    currentLanguageTag = "en",
                                    supportedLanguages = listOf(
                                        SettingsLanguage("en", "English"),
                                    ),
                                    onLanguageSelected = { },
                                    onRateUs = { openUrl("https://homedecor-ai.com/rate") },
                                    onContactSupport = { openUrl("https://homedecor-ai.com/support") },
                                    onDeleteInformation = { },
                                    onSubmitFeedback = { },
                                    onConfirmDelete = { },
                                    onEditProfile = { },
                                    onOpenDiamonds = { },
                                    onOpenPaywall = { paywallVisible = true },
                                    onFaq = { openUrl("https://homedecor-ai.com/faq") },
                                    onShareApp = { openUrl("https://homedecor-ai.com") },
                                    onTerms = { openUrl("https://homedecor-ai.com/terms") },
                                    onPrivacy = { openUrl("https://homedecor-ai.com/privacy") },
                                    onLogout = { },
                                    onClose = { settingsVisible = false },
                                )
                                }
                            }

                            if (paywallVisible) {
                                var selectedPlan by remember { mutableStateOf("yearly") }
                                val paywallState = PaywallState(
                                    isPro = false,
                                    plans = listOf(
                                        PaywallPlan("yearly", Strings.paywallV3PlanYearly, "$39.99", Strings.paywallV3PlanPerYear, Strings.paywallV3PlanAnnualDetail, isRecommended = true),
                                        PaywallPlan("monthly", Strings.paywallV3PlanMonthly, "$7.99", Strings.paywallV3PlanPerMonth, Strings.paywallV3PlanMonthlyDetail, isRecommended = false),
                                        PaywallPlan("family", Strings.paywallV3PlanFamily, "$59.99", Strings.paywallV3PlanPerYear, Strings.paywallV3PlanFamilyDetail, isRecommended = false),
                                    ),
                                    selectedPlanId = selectedPlan,
                                    offeringsLoading = false,
                                    purchasing = false,
                                    purchaseSuccess = false,
                                )
                                Box(
                                    modifier = Modifier
                                        .testTag(Strings.TestTags.paywallSheet)
                                        .onPreviewKeyEvent { event ->
                                            if (event.key == Key.Escape) {
                                                paywallVisible = false
                                                true
                                            } else false
                                        }
                                ) {
                                    SharedPaywallSheet(
                                        state = paywallState.copy(selectedPlanId = selectedPlan),
                                        onClose = { paywallVisible = false },
                                        onPlanSelected = { selectedPlan = it },
                                        onContinue = { openUrl("https://homedecor-ai.com/waitlist") },
                                        onRestore = { openUrl("https://homedecor-ai.com/support") },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WebBottomBar(
    selectedTab: WebTab,
    onSelectTab: (WebTab) -> Unit,
) {
    NavigationBar(
        containerColor = HomeDecorColors.Paper,
        contentColor = HomeDecorColors.Ink,
        modifier = Modifier.testTag(Strings.TestTags.bottomNav),
    ) {
        WebTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectTab(tab) },
                modifier = Modifier.testTag(
                    Strings.formatTestTag(Strings.TestTags.bottomNavItem, tab.name)
                ),
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        modifier = Modifier.size(22.dp),
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = HomeDecorColors.Accent,
                    selectedTextColor = HomeDecorColors.Accent,
                    unselectedIconColor = HomeDecorColors.InkSoft,
                    unselectedTextColor = HomeDecorColors.InkSoft,
                    indicatorColor = HomeDecorColors.Accent.copy(alpha = 0.12f),
                ),
            )
        }
    }
}

@Composable
private fun DesktopAppLayout(
    selectedTab: WebTab,
    onSelectTab: (WebTab) -> Unit,
    paywallVisible: Boolean,
    onPaywallDismiss: () -> Unit,
    onOpenPaywall: () -> Unit,
    settingsVisible: Boolean,
    onSettingsDismiss: () -> Unit,
    onSettingsOpen: () -> Unit,
    activeWizard: ToolItem?,
    onWizardBack: () -> Unit,
    onToolClick: (ToolItem) -> Unit,
    toolsState: ToolsScreenState,
    discoverState: DiscoverScreenState,
    profileState: ProfileScreenState,
    boardState: BoardScreenState,
    settingsState: SettingsScreenState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        DesktopTopNav(
            selectedTab = selectedTab,
            onSelectTab = onSelectTab,
            diamonds = toolsState.diamonds,
            isPro = toolsState.isPro,
            onCredits = { },
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier.widthIn(max = 1200.dp),
            ) {
                if (activeWizard != null) {
                    Box(Modifier.testTag(Strings.TestTags.wizardScreen)) {
                        WebWizardScreen(
                            tool = activeWizard,
                            onBack = onWizardBack,
                        )
                    }
                } else {
                    AnimatedContent(
                        targetState = selectedTab,
                        label = "tab",
                        transitionSpec = {
                            fadeIn() + slideInVertically { it / 20 } togetherWith fadeOut() + slideOutVertically { it / 20 }
                        },
                    ) { tab ->
                        when (tab) {
                            WebTab.Tools -> Box(Modifier.testTag(Strings.TestTags.toolsScreen)) {
                                SharedToolsScreen(
                                    state = toolsState,
                                    onToolClick = { tool -> onToolClick(tool) },
                                )
                            }
                            WebTab.Discover -> Box(Modifier.testTag(Strings.TestTags.discoverScreen)) {
                                SharedDiscoverScreen(
                                    state = discoverState,
                                    onToggleFavorite = { _, _ -> },
                                    onAddToMoodboard = { _, _ -> },
                                    onUseStyle = { _, _ -> },
                                )
                            }
                            WebTab.Board -> Box(Modifier.testTag(Strings.TestTags.boardScreen)) {
                                SharedMyBoardScreen(
                                    state = boardState,
                                    isGuest = profileState.isGuest,
                                    isPro = toolsState.isPro,
                                    onSignIn = { },
                                    onNavigateToTools = { onSelectTab(WebTab.Tools) },
                                    onNavigateToDiscover = { onSelectTab(WebTab.Discover) },
                                    onOpenUpgrade = onOpenPaywall,
                                )
                            }
                            WebTab.Upgrade -> Box(Modifier.testTag(Strings.TestTags.upgradeScreen)) {
                                SharedUpgradeScreen(
                                    isPro = false,
                                    onOpenPaywall = onOpenPaywall,
                                )
                            }
                            WebTab.Profile -> Box(Modifier.testTag(Strings.TestTags.profileScreen)) {
                                SharedProfileScreen(
                                    state = profileState,
                                    onSettings = { onSettingsOpen() },
                                    onSignIn = { },
                                    onOpenDiamonds = { },
                                    onOpenPaywall = onOpenPaywall,
                                    onOpenBoard = { onSelectTab(WebTab.Board) },
                                )
                            }
                        }
                    }
                }
            }

            if (settingsVisible) {
                Box(
                    modifier = Modifier.onPreviewKeyEvent { event ->
                        if (event.key == Key.Escape) {
                            onSettingsDismiss()
                            true
                        } else false
                    }
                ) {
                    SharedSettingsScreen(
                        state = settingsState,
                        currentLanguageTag = "en",
                        supportedLanguages = listOf(
                            SettingsLanguage("en", "English"),
                        ),
                        onLanguageSelected = { },
                        onRateUs = { openUrl("https://homedecor-ai.com/rate") },
                        onContactSupport = { openUrl("https://homedecor-ai.com/support") },
                        onDeleteInformation = { },
                        onSubmitFeedback = { },
                        onConfirmDelete = { },
                        onEditProfile = { },
                        onOpenDiamonds = { },
                        onOpenPaywall = { },
                        onFaq = { openUrl("https://homedecor-ai.com/faq") },
                        onShareApp = { openUrl("https://homedecor-ai.com") },
                        onTerms = { openUrl("https://homedecor-ai.com/terms") },
                        onPrivacy = { openUrl("https://homedecor-ai.com/privacy") },
                        onLogout = { },
                        onClose = onSettingsDismiss,
                    )
                }
            }

            if (paywallVisible) {
                var selectedPlan by remember { mutableStateOf("yearly") }
                val paywallState = PaywallState(
                    isPro = false,
                    plans = listOf(
                        PaywallPlan("yearly", Strings.paywallV3PlanYearly, "$39.99", Strings.paywallV3PlanPerYear, Strings.paywallV3PlanAnnualDetail, isRecommended = true),
                        PaywallPlan("monthly", Strings.paywallV3PlanMonthly, "$7.99", Strings.paywallV3PlanPerMonth, Strings.paywallV3PlanMonthlyDetail, isRecommended = false),
                        PaywallPlan("family", Strings.paywallV3PlanFamily, "$59.99", Strings.paywallV3PlanPerYear, Strings.paywallV3PlanFamilyDetail, isRecommended = false),
                    ),
                    selectedPlanId = selectedPlan,
                    offeringsLoading = false,
                    purchasing = false,
                    purchaseSuccess = false,
                )
                Box(
                    modifier = Modifier
                        .testTag(Strings.TestTags.paywallSheet)
                        .onPreviewKeyEvent { event ->
                            if (event.key == Key.Escape) {
                                onPaywallDismiss()
                                true
                            } else false
                        }
                ) {
                    SharedPaywallSheet(
                        state = paywallState.copy(selectedPlanId = selectedPlan),
                        onClose = onPaywallDismiss,
                        onPlanSelected = { selectedPlan = it },
                        onContinue = { openUrl("https://homedecor-ai.com/waitlist") },
                        onRestore = { openUrl("https://homedecor-ai.com/support") },
                    )
                }
            }
        }
    }
}

@Composable
private fun DesktopTopNav(
    selectedTab: WebTab,
    onSelectTab: (WebTab) -> Unit,
    diamonds: Int,
    isPro: Boolean,
    onCredits: () -> Unit = {},
) {
    Surface(
        color = HomeDecorColors.Paper,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp)
                .testTag(Strings.TestTags.topNav),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "HomeDecor AI",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = HomeDecorColors.Accent,
                modifier = Modifier.semantics { heading() },
            )

            Spacer(Modifier.width(32.dp))

            WebTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                DesktopTopNavItem(
                    label = tab.label,
                    icon = tab.icon,
                    isSelected = isSelected,
                    onClick = { onSelectTab(tab) },
                    testTag = Strings.formatTestTag(Strings.TestTags.topNavItem, tab.name),
                )
            }

            Spacer(Modifier.weight(1f))

            Surface(
                onClick = onCredits,
                shape = RoundedCornerShape(20.dp),
                color = HomeDecorColors.SurfaceContainerHigh,
                modifier = Modifier.semantics {
                    contentDescription = Strings.a11yOpenDiamondStore
                    role = Role.Button
                },
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Stars,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = HomeDecorColors.DiamondAccent,
                    )
                    Text(
                        if (isPro) "PRO" else "$diamonds",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = HomeDecorColors.Ink,
                    )
                }
            }
        }
    }
}

@Composable
private fun DesktopTopNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String = "",
) {
    val contentColor = if (isSelected) HomeDecorColors.Accent else HomeDecorColors.InkSoft

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) HomeDecorColors.Accent.copy(alpha = 0.08f) else Color.Transparent,
        modifier = Modifier
            .testTag(testTag)
            .semantics {
                role = Role.Tab
                selected = isSelected
                contentDescription = label
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(18.dp),
                tint = contentColor,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = contentColor,
            )
        }
    }
}
