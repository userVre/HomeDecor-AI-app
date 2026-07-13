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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.model.BoardScreenState
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.ui.theme.HomeDecorElevation
import com.ismail.homedecorai.ui.theme.HomeDecorExtra
import com.ismail.homedecorai.ui.theme.HomeDecorColors
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing
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
import com.ismail.homedecorai.ui.auth.SharedAuthScreen
import com.ismail.homedecorai.ui.tools.SharedToolsScreen
import com.ismail.homedecorai.ui.tools.WebWizardScreen
import com.ismail.homedecorai.ui.upgrade.SharedUpgradeScreen
import com.ismail.homedecorai.ui.store.SharedDiamondStoreSheet
import com.ismail.homedecorai.ui.store.DiamondStoreState
import com.ismail.homedecorai.ui.store.DiamondPackage
import com.ismail.homedecorai.ui.theme.HomeDecorTheme
import kotlinx.coroutines.launch

private enum class WebTab(val label: String, val icon: ImageVector, val route: String, val pageTitle: String) {
    Tools("Tools", Icons.Rounded.Widgets, "/tools", "Tools - HomeDecor AI"),
    Discover("Discover", Icons.Rounded.Explore, "/discover", "Discover - HomeDecor AI"),
    Board("My Board", Icons.Rounded.Dashboard, "/board", "My Board - HomeDecor AI"),
    Upgrade("Pro", Icons.Rounded.Stars, "/pro", "Pro - HomeDecor AI"),
    Profile("Profile", Icons.Rounded.Person, "/profile", "Profile - HomeDecor AI"),
}

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(getPersistedDarkTheme()) }
    LaunchedEffect(isDarkTheme) { persistDarkTheme(isDarkTheme) }
    HomeDecorTheme(darkTheme = isDarkTheme, dynamicColor = false) {
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
        var paywallSelectedPlan by remember { mutableStateOf("yearly") }
        var settingsVisible by remember { mutableStateOf(false) }
        var diamondStoreVisible by remember { mutableStateOf(false) }

        // Announce modal state changes to screen readers
        LaunchedEffect(paywallVisible) {
            if (paywallVisible) announceToScreenReader(Strings.a11yPaywallHeading)
        }
        LaunchedEffect(settingsVisible) {
            if (settingsVisible) announceToScreenReader(Strings.a11ySettingsHeading)
        }
        LaunchedEffect(diamondStoreVisible) {
            if (diamondStoreVisible) announceToScreenReader(Strings.a11yDiamondStoreTitle)
        }

        var activeWizard by remember {
            val path = getCurrentPathname()
            val match = Regex("/create/(\\w+)").find(path)
            mutableStateOf(match?.groupValues?.get(1)?.let { toolId ->
                ToolItem(toolId, "", "", HomeDecorColors.ToolGradientInteriorStart, HomeDecorColors.ToolGradientInteriorEnd, "images/tool_${toolId}.webp",
                    accentColor = HomeDecorColors.PrimaryContainer)
            })
        }

        var hasPushedInitial by remember { mutableStateOf(false) }
        LaunchedEffect(selectedTab, activeWizard) {
            val title = if (activeWizard != null) {
                "${Strings.toolTitle(activeWizard!!.id)} - HomeDecor AI"
            } else {
                selectedTab.pageTitle
            }
            setPageTitle(title)
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
                // Only push history if the path actually changed to prevent
                // duplicate entries when tab state changes from back/forward.
                val currentPath = getCurrentPathname()
                if (path != currentPath) {
                    pushHistoryState(path, title)
                }
            }
            announceToScreenReader(title)
        }

        val toolsState = remember {
            ToolsScreenState(
                isPro = false,
                diamonds = 150,
                tools = listOf(
                    ToolItem("interior", "Interior Design", "Redesign any room with AI-powered interior concepts",
                        gradientStart = HomeDecorColors.ToolGradientInteriorStart, gradientEnd = HomeDecorColors.ToolGradientInteriorEnd,
                        imageUrl = "images/tool_interior.webp",
                        accentColor = HomeDecorColors.PrimaryContainer),
                    ToolItem("facade", "Exterior Design", "Transform your home's exterior with modern facade styles",
                        gradientStart = HomeDecorColors.ToolGradientExteriorStart, gradientEnd = HomeDecorColors.ToolGradientExteriorEnd,
                        imageUrl = "images/tool_exterior.webp",
                        accentColor = Color(0xFFB8CCE8)),
                    ToolItem("garden", "Garden Design", "Plan and visualize your dream garden landscape",
                        gradientStart = HomeDecorColors.ToolGradientGardenStart, gradientEnd = HomeDecorColors.ToolGradientGardenEnd,
                        imageUrl = "images/tool_garden.webp",
                        accentColor = HomeDecorColors.SecondaryContainer),
                    ToolItem("paint", "Smart Wall Paint", "Preview smart paint colors on your walls instantly",
                        gradientStart = HomeDecorColors.ToolGradientPaintStart, gradientEnd = HomeDecorColors.ToolGradientPaintEnd,
                        imageUrl = "images/tool_paint.webp",
                        accentColor = HomeDecorColors.QuaternaryContainer),
                    ToolItem("floor", "Floor Design", "Explore premium flooring from hardwood to marble tile",
                        gradientStart = HomeDecorColors.ToolGradientFloorStart, gradientEnd = HomeDecorColors.ToolGradientFloorEnd,
                        imageUrl = "images/tool_floor.webp",
                        accentColor = HomeDecorColors.TertiaryContainer),
                    ToolItem("layout", "Layout Makeover", "Optimize room layout for better flow and functionality",
                        gradientStart = HomeDecorColors.ToolGradientLayoutStart, gradientEnd = HomeDecorColors.ToolGradientLayoutEnd,
                        imageUrl = "images/tool_layout.webp",
                        accentColor = Color(0xFFD0C4F8)),
                    ToolItem("replace", "Replace Furniture", "Swap furniture and decor with AI-generated alternatives",
                        gradientStart = HomeDecorColors.ToolGradientReplaceStart, gradientEnd = HomeDecorColors.ToolGradientReplaceEnd,
                        imageUrl = "images/tool_replace.webp",
                        accentColor = Color(0xFFF5D0C0)),
                    ToolItem("reference", "Reference Style", "Use any reference image to guide your design direction",
                        gradientStart = HomeDecorColors.ToolGradientReferenceStart, gradientEnd = HomeDecorColors.ToolGradientReferenceEnd,
                        imageUrl = "images/tool_reference.webp",
                        accentColor = Color(0xFFB0C8E0)),
                ),
            )
        }

        var webFavoriteIds by remember { mutableStateOf(emptySet<String>()) }
        var webMoodboardIds by remember { mutableStateOf(emptySet<String>()) }
        var webIsSignedIn by remember { mutableStateOf(false) }
        var webSignedInName by remember { mutableStateOf<String?>(null) }
        var webSignedInEmail by remember { mutableStateOf<String?>(null) }
        var authVisible by remember { mutableStateOf(false) }
        var authLoading by remember { mutableStateOf(false) }
        var authError by remember { mutableStateOf<String?>(null) }

        fun doSignIn(email: String, password: String) {
            authLoading = true
            authError = null
            kotlinx.coroutines.MainScope().launch {
                val initResult = clerkInit()
                if (initResult != "ok") {
                    authLoading = false
                    authError = Strings.authErrorNetwork
                    return@launch
                }
                val signInResult = clerkSignIn()
                if (signInResult.startsWith("error")) {
                    authLoading = false
                    authError = if (signInResult.contains("invalid") || signInResult.contains("credential")) {
                        Strings.authErrorInvalidCredentials
                    } else {
                        Strings.authErrorGeneric
                    }
                    return@launch
                }
                val user = clerkGetUser()
                if (user != null) {
                    webIsSignedIn = true
                    webSignedInName = user.fullName
                    webSignedInEmail = user.email
                    authVisible = false
                    authLoading = false
                } else {
                    authLoading = false
                    authError = Strings.authErrorGeneric
                }
            }
        }

        fun doSignUp(email: String, password: String) {
            authLoading = true
            authError = null
            kotlinx.coroutines.MainScope().launch {
                val initResult = clerkInit()
                if (initResult != "ok") {
                    authLoading = false
                    authError = Strings.authErrorNetwork
                    return@launch
                }
                val signUpResult = clerkSignUp()
                if (signUpResult.startsWith("error")) {
                    authLoading = false
                    authError = Strings.authErrorGeneric
                    return@launch
                }
                val user = clerkGetUser()
                if (user != null) {
                    webIsSignedIn = true
                    webSignedInName = user.fullName
                    webSignedInEmail = user.email
                    authVisible = false
                    authLoading = false
                } else {
                    authLoading = false
                    authError = Strings.authErrorGeneric
                }
            }
        }

        fun doGoogleSignIn() {
            authLoading = true
            authError = null
            kotlinx.coroutines.MainScope().launch {
                val initResult = clerkInit()
                if (initResult != "ok") {
                    authLoading = false
                    authError = Strings.authErrorNetwork
                    return@launch
                }
                val signInResult = clerkSignIn()
                if (signInResult.startsWith("error")) {
                    authLoading = false
                    authError = Strings.authErrorGeneric
                    return@launch
                }
                val user = clerkGetUser()
                if (user != null) {
                    webIsSignedIn = true
                    webSignedInName = user.fullName
                    webSignedInEmail = user.email
                    authVisible = false
                    authLoading = false
                } else {
                    authLoading = false
                    authError = Strings.authErrorGeneric
                }
            }
        }

        fun doLogout() {
            kotlinx.coroutines.MainScope().launch {
                clerkSignOut()
                webIsSignedIn = false
                webSignedInName = null
                webSignedInEmail = null
                settingsVisible = false
            }
        }

        fun openAuth() {
            authError = null
            authLoading = false
            authVisible = true
        }

        val discoverState = remember(webFavoriteIds, webMoodboardIds, webIsSignedIn) {
            DiscoverScreenState(
                favoriteSourceIds = webFavoriteIds,
                moodboardSourceIds = webMoodboardIds,
                isSignedIn = webIsSignedIn,
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

        val profileState = remember(webIsSignedIn, webSignedInName, webSignedInEmail) {
            ProfileScreenState(
                isGuest = !webIsSignedIn,
                signedInName = webSignedInName,
                signedInEmail = webSignedInEmail,
                diamonds = 150,
                isPro = false,
                favoritesCount = webFavoriteIds.size,
            )
        }

        var boardState by remember {
            mutableStateOf(BoardScreenState(
                generatedItems = emptyList(),
                favoriteItems = emptyList(),
                projectItems = emptyList(),
            ))
        }

        LaunchedEffect(webIsSignedIn) {
            try {
                val anonId = getAnonymousIdFromPlatform()
                val response = convexQueryAuth(
                    "generations:getUserArchive",
                    mapOf("anonymousId" to anonId),
                )
                val items = extractTopLevelArray(response, "result")
                val generated = items.mapNotNull { parseBoardItem(it) }
                val favorites = generated.filter { it.isFavorite }
                boardState = BoardScreenState(
                    generatedItems = generated,
                    favoriteItems = favorites,
                    projectItems = emptyList(),
                )
            } catch (_: Exception) {}
        }

        val settingsState = remember(webIsSignedIn, webSignedInName, webSignedInEmail) {
            SettingsScreenState(
                versionName = "1.0.0-web",
                settingsBusy = false,
                isSignedIn = webIsSignedIn,
                signedInName = webSignedInName,
                signedInEmail = webSignedInEmail,
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
                    paywallSelectedPlan = paywallSelectedPlan,
                    onPaywallPlanSelected = { paywallSelectedPlan = it },
                    onPaywallDismiss = { paywallVisible = false },
                    onOpenPaywall = { paywallVisible = true },
                    settingsVisible = settingsVisible,
                    onSettingsDismiss = { settingsVisible = false },
                    onSettingsOpen = { settingsVisible = true },
                    onOpenDiamondStore = { diamondStoreVisible = true },
                    activeWizard = activeWizard,
                    onWizardBack = { activeWizard = null },
                    onToolClick = { tool -> activeWizard = tool },
                    toolsState = toolsState,
                    discoverState = discoverState,
                    profileState = profileState,
                    boardState = boardState,
                    settingsState = settingsState,
                    onToggleFavorite = { _, item ->
                        webFavoriteIds = if (item.id in webFavoriteIds) {
                            webFavoriteIds - item.id
                        } else {
                            webFavoriteIds + item.id
                        }
                        showToast(
                            if (item.id in webFavoriteIds) Strings.toastFavoriteRemoved
                            else Strings.toastFavoriteAdded
                        )
                    },
                    onAddToMoodboard = { _, item ->
                        webMoodboardIds = webMoodboardIds + item.id
                        showToast(Strings.toastMoodboardAdded)
                    },
                    onUseStyle = { _, item ->
                        val refTool = toolsState.tools.find { it.id == "reference" }
                        if (refTool != null) {
                            activeWizard = refTool
                        } else {
                            selectedTab = WebTab.Tools
                        }
                    },
                    onSignIn = { openAuth() },
                    onLogout = { doLogout() },
                    authVisible = authVisible,
                    onAuthDismiss = { authVisible = false },
                    onAuthSignIn = { email, password -> doSignIn(email, password) },
                    onAuthSignUp = { email, password -> doSignUp(email, password) },
                    onAuthGoogleSignIn = { doGoogleSignIn() },
                    authIsLoading = authLoading,
                    authError = authError,
                    onAuthDismissError = { authError = null },
                    onBoardItemClick = { item ->
                        val url = item.imageUrl
                        if (!url.isNullOrBlank()) {
                            openUrl(url)
                        }
                    },
                    onBoardToggleFavorite = { item ->
                        webFavoriteIds = if (item.id in webFavoriteIds) {
                            webFavoriteIds - item.id
                        } else {
                            webFavoriteIds + item.id
                        }
                        showToast(
                            if (item.id in webFavoriteIds) Strings.toastFavoriteRemoved
                            else Strings.toastFavoriteAdded
                        )
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = !isDarkTheme },
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
                                                onToggleFavorite = { _, item ->
                                                    webFavoriteIds = if (item.id in webFavoriteIds) {
                                                        webFavoriteIds - item.id
                                                    } else {
                                                        webFavoriteIds + item.id
                                                    }
                                                    showToast(
                                                        if (item.id in webFavoriteIds) Strings.toastFavoriteRemoved
                                                        else Strings.toastFavoriteAdded
                                                    )
                                                },
                                                onAddToMoodboard = { _, item ->
                                                    webMoodboardIds = webMoodboardIds + item.id
                                                    showToast(Strings.toastMoodboardAdded)
                                                },
                                                onUseStyle = { _, _ ->
                                                    val refTool = toolsState.tools.find { it.id == "reference" }
                                                    if (refTool != null) {
                                                        activeWizard = refTool
                                                    } else {
                                                        selectedTab = WebTab.Tools
                                                    }
                                                },
                                                onSignIn = { openAuth() },
                                            )
                                        }
                                        WebTab.Board -> Box(Modifier.testTag(Strings.TestTags.boardScreen)) {
                                            SharedMyBoardScreen(
                                                state = boardState,
                                                isGuest = profileState.isGuest,
                                                isPro = toolsState.isPro,
                                                onSignIn = { openAuth() },
                                                onNavigateToTools = { selectedTab = WebTab.Tools },
                                                onNavigateToDiscover = { selectedTab = WebTab.Discover },
                                                onOpenUpgrade = { paywallVisible = true },
                                                onItemClick = { item ->
                                                    val url = item.imageUrl
                                                    if (!url.isNullOrBlank()) {
                                                        openUrl(url)
                                                    }
                                                },
                                                onToggleFavorite = { item ->
                                                    webFavoriteIds = if (item.id in webFavoriteIds) {
                                                        webFavoriteIds - item.id
                                                    } else {
                                                        webFavoriteIds + item.id
                                                    }
                                                    showToast(
                                                        if (item.id in webFavoriteIds) Strings.toastFavoriteRemoved
                                                        else Strings.toastFavoriteAdded
                                                    )
                                                },
                                                signedInName = profileState.signedInName,
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
                                                onSignIn = { openAuth() },
                                    onOpenDiamonds = { paywallVisible = true },
                                                onOpenPaywall = { paywallVisible = true },
                                                onOpenBoard = { selectedTab = WebTab.Board },
                                    )
                                }
                            }

                            if (authVisible) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
                                        .onPreviewKeyEvent { event ->
                                            if (event.key == Key.Escape) {
                                                authVisible = false
                                                true
                                            } else false
                                        },
                                    contentAlignment = Alignment.TopCenter,
                                ) {
                                    SharedAuthScreen(
                                        onSignIn = { email, password -> doSignIn(email, password) },
                                        onSignUp = { email, password -> doSignUp(email, password) },
                                        onGoogleSignIn = { doGoogleSignIn() },
                                        onForgotPassword = {
                                            showToast(Strings.toastComingSoon)
                                        },
                                        onClose = { authVisible = false },
                                        isLoading = authLoading,
                                        errorMessage = authError,
                                        onDismissError = { authError = null },
                                    )
                                }
                            }
                                }
                            }

                            if (settingsVisible) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                        ) { settingsVisible = false }
                                        .onPreviewKeyEvent { event ->
                                            if (event.key == Key.Escape) {
                                                settingsVisible = false
                                                true
                                            } else false
                                        },
                                    contentAlignment = Alignment.TopCenter,
                                ) {
                                    Surface(
                                        modifier = Modifier.fillMaxSize()
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() },
                                            ) { /* absorb clicks */ },
                                        color = MaterialTheme.colorScheme.surface,
                                    ) {
                                        SharedSettingsScreen(
                                            state = settingsState,
                                            currentLanguageTag = "en",
                                            supportedLanguages = listOf(
                                                SettingsLanguage("en", "English"),
                                            ),
                                            onLanguageSelected = { showToast("Language: English") },
                                            onRateUs = { showToast(Strings.toastRateUs) },
                                            onContactSupport = { openUrl("mailto:support@homedecorai.com?subject=Support%20Request") },
                            onDeleteInformation = { openUrl("mailto:support@homedecorai.com?subject=Delete%20Data") },
                            onSubmitFeedback = { openUrl("mailto:support@homedecorai.com?subject=Feedback") },
                            onConfirmDelete = { openUrl("mailto:support@homedecorai.com?subject=Delete%20Account") },
                                            onEditProfile = { showToast(Strings.toastComingSoon) },
                                    onOpenDiamonds = { paywallVisible = true },
                                                onOpenPaywall = { paywallVisible = true },
                                            onFaq = { showToast(Strings.toastComingSoon) },
                                            onTerms = { showToast(Strings.toastComingSoon) },
                                            onPrivacy = { showToast(Strings.toastComingSoon) },
                                            onManageBilling = {
                                                if (Strings.PAYMENTS_ENABLED) {
                                                    openUrl(Strings.CUSTOMER_PORTAL_URL)
                                                } else {
                                                    openUrl(Strings.BILLING_SUPPORT_URL)
                                                }
                                            },
                                            onLogout = { doLogout() },
                                            onClose = { settingsVisible = false },
                                            isDarkTheme = isDarkTheme,
                                            onThemeToggle = { isDarkTheme = !isDarkTheme },
                                        )
                                    }
                                }
                            }

                            if (paywallVisible) {
                                val paywallState = remember(paywallSelectedPlan) {
                                    PaywallState(
                                        isPro = false,
                                        plans = listOf(
                                            PaywallPlan("yearly", Strings.paywallV3PlanYearly, "$39.99", Strings.paywallV3PlanPerYear, Strings.paywallV3PlanAnnualDetail, isRecommended = true),
                                            PaywallPlan("monthly", Strings.paywallV3PlanMonthly, "$7.99", Strings.paywallV3PlanPerMonth, Strings.paywallV3PlanMonthlyDetail, isRecommended = false),
                                            PaywallPlan("family", Strings.paywallV3PlanFamily, "$59.99", Strings.paywallV3PlanPerYear, Strings.paywallV3PlanFamilyDetail, isRecommended = false),
                                        ),
                                        selectedPlanId = paywallSelectedPlan,
                                        offeringsLoading = false,
                                        purchasing = false,
                                        purchaseSuccess = false,
                                    )
                                }
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
                                        state = paywallState,
                                        onClose = { paywallVisible = false },
                                        onPlanSelected = { paywallSelectedPlan = it },
                                        onContinue = {
                                            if (Strings.PAYMENTS_ENABLED) {
                                                openUrl(Strings.checkoutUrlForPlan(paywallSelectedPlan))
                                            } else {
                                                openUrl(Strings.BILLING_SUPPORT_URL)
                                            }
                                        },
                                        onRestore = {
                                            if (Strings.PAYMENTS_ENABLED) {
                                                openUrl(Strings.CUSTOMER_PORTAL_URL)
                                            } else {
                                                openUrl(Strings.BILLING_SUPPORT_URL)
                                            }
                                        },
                                    )
                                }
                            }

                            if (diamondStoreVisible) {
                                val diamondStoreState = remember {
                                    DiamondStoreState(
                                        currentDiamonds = toolsState.diamonds,
                                        packages = listOf(
                                            DiamondPackage("starter", "Starter", 50, "$1.99", "$0.04", gradientStart = Color(0xFF4DD9E0), gradientEnd = Color(0xFF0097A7)),
                                            DiamondPackage("designer", "Creator", 150, "$4.99", "$0.03", badge = "POPULAR", gradientStart = Color(0xFF9B6EFF), gradientEnd = Color(0xFF6A1B9A)),
                                            DiamondPackage("architect", "Pro", 400, "$9.99", "$0.02", badge = "BEST VALUE", gradientStart = Color(0xFF34D399), gradientEnd = Color(0xFF047857)),
                                            DiamondPackage("estate", "Ultimate", 1000, "$19.99", "$0.02", gradientStart = Color(0xFFFFD166), gradientEnd = Color(0xFFB08D3A)),
                                        ),
                                    )
                                }
                                SharedDiamondStoreSheet(
                                    state = diamondStoreState,
                                    onClose = { diamondStoreVisible = false },
                                    onPurchase = { pkg ->
                                        openUrl(Strings.checkoutUrlForPlan(pkg.id))
                                    },
                                    onClaimDaily = { showToast(Strings.toastComingSoon) },
                                )
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
        containerColor = HomeDecorExtra.surfaceCard,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.testTag(Strings.TestTags.bottomNav),
    ) {
        WebTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            NavigationBarItem(
                selected = selected,
                onClick = { if (!selected) onSelectTab(tab) },
                modifier = Modifier.testTag(
                    Strings.formatTestTag(Strings.TestTags.bottomNavItem, tab.name)
                ),
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        modifier = Modifier.size(HomeDecorSpacing.Lg),
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
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
    paywallSelectedPlan: String,
    onPaywallPlanSelected: (String) -> Unit,
    onPaywallDismiss: () -> Unit,
    onOpenPaywall: () -> Unit,
    settingsVisible: Boolean,
    onSettingsDismiss: () -> Unit,
    onSettingsOpen: () -> Unit,
    onOpenDiamondStore: () -> Unit,
    activeWizard: ToolItem?,
    onWizardBack: () -> Unit,
    onToolClick: (ToolItem) -> Unit,
    toolsState: ToolsScreenState,
    discoverState: DiscoverScreenState,
    profileState: ProfileScreenState,
    boardState: BoardScreenState,
    settingsState: SettingsScreenState,
    onToggleFavorite: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onAddToMoodboard: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onUseStyle: (DiscoverSectionItem, GalleryCardItem) -> Unit,
    onSignIn: () -> Unit,
    onLogout: () -> Unit,
    authVisible: Boolean = false,
    onAuthDismiss: () -> Unit = {},
    onAuthSignIn: (String, String) -> Unit = { _, _ -> },
    onAuthSignUp: (String, String) -> Unit = { _, _ -> },
    onAuthGoogleSignIn: () -> Unit = {},
    authIsLoading: Boolean = false,
    authError: String? = null,
    onAuthDismissError: () -> Unit = {},
    onBoardItemClick: (BoardItem) -> Unit = {},
    onBoardToggleFavorite: (BoardItem) -> Unit = {},
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {},
) {
    var showDiamondTooltip by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        DesktopTopNav(
            showDiamondTooltip = showDiamondTooltip,
            onToggleTooltip = { showDiamondTooltip = !showDiamondTooltip },
            onDismissTooltip = { showDiamondTooltip = false },
            selectedTab = selectedTab,
            onSelectTab = onSelectTab,
            diamonds = toolsState.diamonds,
            isPro = toolsState.isPro,
            onOpenDiamondStore = onOpenDiamondStore,
        )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 1200.dp)
                        .fillMaxHeight()
                        .padding(horizontal = HomeDecorSpacing.Xl, vertical = HomeDecorSpacing.Sm),
                    shape = HomeDecorShape.ContentPanel,
                    color = HomeDecorExtra.surfaceCard,
                    shadowElevation = HomeDecorElevation.SurfacePanel,
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
                                        onToggleFavorite = { section, item -> onToggleFavorite(section, item) },
                                        onAddToMoodboard = { section, item -> onAddToMoodboard(section, item) },
                                        onUseStyle = { section, item -> onUseStyle(section, item) },
                                        onSignIn = { onSignIn() },
                                    )
                                }
                                WebTab.Board -> Box(Modifier.testTag(Strings.TestTags.boardScreen)) {
                                    SharedMyBoardScreen(
                                        state = boardState,
                                        isGuest = profileState.isGuest,
                                        isPro = toolsState.isPro,
                                        onSignIn = { onSignIn() },
                                        onNavigateToTools = { onSelectTab(WebTab.Tools) },
                                        onNavigateToDiscover = { onSelectTab(WebTab.Discover) },
                                        onOpenUpgrade = onOpenPaywall,
                                        onItemClick = onBoardItemClick,
                                        onToggleFavorite = onBoardToggleFavorite,
                                        signedInName = profileState.signedInName,
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
                                        onSignIn = { onSignIn() },
                                        onOpenDiamonds = { onOpenPaywall() },
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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) { onSettingsDismiss() }
                        .onPreviewKeyEvent { event ->
                            if (event.key == Key.Escape) {
                                onSettingsDismiss()
                                true
                            } else false
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Surface(
                        modifier = Modifier
                            .then(
                                Modifier.widthIn(max = 800.dp)
                                    .fillMaxHeight(0.92f)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ) { /* absorb clicks */ },
                        shape = RoundedCornerShape(24.dp),
                        color = HomeDecorExtra.surfaceCard,
                        shadowElevation = 8.dp,
                    ) {
                        SharedSettingsScreen(
                            state = settingsState,
                            currentLanguageTag = "en",
                            supportedLanguages = listOf(
                                SettingsLanguage("en", "English"),
                            ),
                            onLanguageSelected = { showToast("Language: English") },
                            onRateUs = { showToast(Strings.toastRateUs) },
                            onContactSupport = { openUrl("mailto:support@homedecorai.com?subject=Support%20Request") },
                            onDeleteInformation = { openUrl("mailto:support@homedecorai.com?subject=Delete%20Data") },
                            onSubmitFeedback = { openUrl("mailto:support@homedecorai.com?subject=Feedback") },
                            onConfirmDelete = { openUrl("mailto:support@homedecorai.com?subject=Delete%20Account") },
                            onEditProfile = { showToast(Strings.toastComingSoon) },
                            onOpenDiamonds = { onOpenPaywall() },
                            onOpenPaywall = { onOpenPaywall() },
                            onFaq = { showToast(Strings.toastComingSoon) },
                            onTerms = { showToast(Strings.toastComingSoon) },
                            onPrivacy = { showToast(Strings.toastComingSoon) },
                            onManageBilling = {
                                if (Strings.PAYMENTS_ENABLED) {
                                    openUrl(Strings.CUSTOMER_PORTAL_URL)
                                } else {
                                    openUrl(Strings.BILLING_SUPPORT_URL)
                                }
                            },
                            onLogout = { onLogout() },
                            onClose = onSettingsDismiss,
                            isDarkTheme = isDarkTheme,
                            onThemeToggle = onThemeToggle,
                        )
                    }
                }
            }

            if (paywallVisible) {
                val paywallState = remember(paywallSelectedPlan) {
                    PaywallState(
                        isPro = false,
                        plans = listOf(
                            PaywallPlan("yearly", Strings.paywallV3PlanYearly, "$39.99", Strings.paywallV3PlanPerYear, Strings.paywallV3PlanAnnualDetail, isRecommended = true),
                            PaywallPlan("monthly", Strings.paywallV3PlanMonthly, "$7.99", Strings.paywallV3PlanPerMonth, Strings.paywallV3PlanMonthlyDetail, isRecommended = false),
                            PaywallPlan("family", Strings.paywallV3PlanFamily, "$59.99", Strings.paywallV3PlanPerYear, Strings.paywallV3PlanFamilyDetail, isRecommended = false),
                        ),
                        selectedPlanId = paywallSelectedPlan,
                        offeringsLoading = false,
                        purchasing = false,
                        purchaseSuccess = false,
                    )
                }
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
                        state = paywallState,
                        onClose = onPaywallDismiss,
                        onPlanSelected = { onPaywallPlanSelected(it) },
                        onContinue = {
                            if (Strings.PAYMENTS_ENABLED) {
                                openUrl(Strings.checkoutUrlForPlan(paywallSelectedPlan))
                            } else {
                                openUrl(Strings.BILLING_SUPPORT_URL)
                            }
                        },
                        onRestore = {
                            if (Strings.PAYMENTS_ENABLED) {
                                openUrl(Strings.CUSTOMER_PORTAL_URL)
                            } else {
                                openUrl(Strings.BILLING_SUPPORT_URL)
                            }
                        },
                    )
                }
            }

            if (diamondStoreVisible) {
                val diamondStoreState = remember {
                    DiamondStoreState(
                        currentDiamonds = toolsState.diamonds,
                        packages = listOf(
                            DiamondPackage("starter", "Starter", 50, "$1.99", "$0.04", gradientStart = Color(0xFF4DD9E0), gradientEnd = Color(0xFF0097A7)),
                            DiamondPackage("designer", "Creator", 150, "$4.99", "$0.03", badge = "POPULAR", gradientStart = Color(0xFF9B6EFF), gradientEnd = Color(0xFF6A1B9A)),
                            DiamondPackage("architect", "Pro", 400, "$9.99", "$0.02", badge = "BEST VALUE", gradientStart = Color(0xFF34D399), gradientEnd = Color(0xFF047857)),
                            DiamondPackage("estate", "Ultimate", 1000, "$19.99", "$0.02", gradientStart = Color(0xFFFFD166), gradientEnd = Color(0xFFB08D3A)),
                        ),
                    )
                }
                SharedDiamondStoreSheet(
                    state = diamondStoreState,
                    onClose = { diamondStoreVisible = false },
                    onPurchase = { pkg ->
                        openUrl(Strings.checkoutUrlForPlan(pkg.id))
                    },
                    onClaimDaily = { showToast(Strings.toastComingSoon) },
                )
            }

            if (authVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
                        .onPreviewKeyEvent { event ->
                            if (event.key == Key.Escape) {
                                onAuthDismiss()
                                true
                            } else false
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    SharedAuthScreen(
                        onSignIn = { email, password -> onAuthSignIn(email, password) },
                        onSignUp = { email, password -> onAuthSignUp(email, password) },
                        onGoogleSignIn = { onAuthGoogleSignIn() },
                        onForgotPassword = {
                            showToast(Strings.toastComingSoon)
                        },
                        onClose = { onAuthDismiss() },
                        isLoading = authIsLoading,
                        errorMessage = authError,
                        onDismissError = { onAuthDismissError() },
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
    showDiamondTooltip: Boolean = false,
    onToggleTooltip: () -> Unit = {},
    onDismissTooltip: () -> Unit = {},
    onOpenDiamondStore: () -> Unit = {},
) {
    Surface(
        color = HomeDecorExtra.surfaceCard,
        shadowElevation = HomeDecorElevation.NavElevation,
        shape = HomeDecorShape.TopNav,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(HomeDecorSpacing.DesktopTopNavHeight)
                .padding(horizontal = HomeDecorSpacing.Xl)
                .testTag(Strings.TestTags.topNav),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "HomeDecor AI",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { heading() },
            )

            Spacer(Modifier.width(HomeDecorSpacing.Xxl))

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

            Box {
                Surface(
                    onClick = {
                        onOpenDiamondStore()
                    },
                    shape = HomeDecorShape.Pill,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.semantics {
                        contentDescription = Strings.a11yOpenDiamondStore
                        role = Role.Button
                    },
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Stars,
                            contentDescription = null,
                            modifier = Modifier.size(HomeDecorSpacing.Base),
                            tint = HomeDecorExtra.diamondAccent,
                        )
                        Text(
                            if (isPro) "PRO" else "$diamonds",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                if (showDiamondTooltip) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.inverseSurface,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 48.dp)
                            .widthIn(max = 280.dp),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "You have 150 free credits! Each generation costs 10 \uD83D\uDC8E",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                            )
                            Spacer(Modifier.height(8.dp))
                            Surface(
                                onClick = onDismissTooltip,
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.inversePrimary,
                            ) {
                                Text(
                                    text = "Got it",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                )
                            }
                        }
                    }
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
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        onClick = { if (!isSelected) onClick() },
        shape = HomeDecorShape.Medium,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else Color.Transparent,
        modifier = Modifier
            .size(48.dp)
            .testTag(testTag)
            .semantics {
                role = Role.Tab
                selected = isSelected
                contentDescription = label
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = HomeDecorSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(HomeDecorSpacing.Lg),
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

// ---------------------------------------------------------------------------
// Board JSON helpers (mirrors WebViewModel extraction logic)
// ---------------------------------------------------------------------------

private fun extractTopLevelArray(json: String, key: String): List<String> {
    val searchKey = "\"$key\"\\s*:\\s*\\["
    val match = Regex(searchKey).find(json) ?: return emptyList()
    val startIdx = match.range.last
    var depth = 0
    var inString = false
    var escape = false
    for (i in startIdx until json.length) {
        when {
            escape -> escape = false
            json[i] == '\\' && inString -> escape = true
            json[i] == '"' -> inString = !inString
            !inString && json[i] == '[' -> depth++
            !inString && json[i] == ']' -> {
                depth--
                if (depth == 0) {
                    val content = json.substring(startIdx + 1, i).trim()
                    if (content.isBlank()) return emptyList()
                    return splitTopLevelObjects(content)
                }
            }
        }
    }
    return emptyList()
}

private fun splitTopLevelObjects(content: String): List<String> {
    val objects = mutableListOf<String>()
    var depth = 0
    var inString = false
    var escape = false
    var start = -1
    for (i in content.indices) {
        when {
            escape -> escape = false
            content[i] == '\\' && inString -> escape = true
            content[i] == '"' -> inString = !inString
            !inString && content[i] == '{' -> {
                if (depth == 0) start = i
                depth++
            }
            !inString && content[i] == '}' -> {
                depth--
                if (depth == 0 && start >= 0) {
                    objects.add(content.substring(start, i + 1))
                    start = -1
                }
            }
        }
    }
    return objects
}

private fun extractBoardString(json: String, key: String, default: String = ""): String {
    val match = Regex("\"$key\"\\s*:\\s*\"([^\"]*?)\"").find(json) ?: return default
    return match.groupValues[1]
}

private fun extractBoardDouble(json: String, key: String, default: Double = 0.0): Double {
    val match = Regex("\"$key\"\\s*:\\s*(-?\\d+\\.?\\d*)").find(json) ?: return default
    return match.groupValues[1].toDoubleOrNull() ?: default
}

private fun extractBoardBool(json: String, key: String, default: Boolean = false): Boolean {
    val match = Regex("\"$key\"\\s*:\\s*(true|false)").find(json) ?: return default
    return match.groupValues[1] == "true"
}

private fun parseBoardItem(json: String): BoardItem? {
    val id = extractBoardString(json, "_id")
    if (id.isBlank()) return null
    return BoardItem(
        id = id,
        toolTitle = extractBoardString(json, "serviceType"),
        style = extractBoardString(json, "style"),
        roomType = extractBoardString(json, "roomType"),
        imageUrl = extractBoardString(json, "imageUrl"),
        sourceImageUrl = extractBoardString(json, "sourceImageUrl"),
        status = extractBoardString(json, "status"),
        createdAt = extractBoardDouble(json, "createdAt"),
        isFavorite = extractBoardBool(json, "isFavorite"),
    )
}
