package com.ismail.homedecorai

import com.ismail.homedecorai.model.BoardTab
import com.ismail.homedecorai.model.DiscoverScreenState
import com.ismail.homedecorai.ui.profile.ProfileScreenState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DiscoverBoardProfileTest {

    // ── Discover ──
    @Test
    fun testDiscoverClusterTabTagFormat() {
        assertEquals("discover_cluster_tab_rooms", Strings.formatTestTag(Strings.TestTags.discoverClusterTab, "rooms"))
    }

    @Test
    fun testDiscoverSeeAllTagFormat() {
        assertEquals("discover_see_all_all", Strings.formatTestTag(Strings.TestTags.discoverSeeAll, "all"))
    }

    @Test
    fun testDiscoverSectionRowTagFormat() {
        assertEquals("discover_section_row_living-room", Strings.formatTestTag(Strings.TestTags.discoverSectionRow, "living-room"))
    }

    @Test
    fun testDiscoverSectionCardTagFormat() {
        assertEquals("discover_section_card_item1", Strings.formatTestTag(Strings.TestTags.discoverSectionCard, "item1"))
    }

    @Test
    fun testDiscoverTestTagsExist() {
        assertNotNull(Strings.TestTags.discoverScreen)
        assertNotNull(Strings.TestTags.discoverClusterTabRow)
        assertNotNull(Strings.TestTags.discoverSectionScroll)
    }

    @Test
    fun testDiscoverA11yLabel() {
        val label = Strings.a11yDiscoverCard("Living Room", "Modern Interior")
        assertEquals("Living Room \u2014 Modern Interior", label)
    }

    @Test
    fun testDiscoverClusterA11yLabel() {
        val label = Strings.a11yDiscoverCluster("Interior")
        assertEquals("Cluster: Interior", label)
    }

    @Test
    fun testDiscoverSeeAllA11yLabel() {
        val label = Strings.a11ySeeAll("Living Room")
        assertEquals("See all Living Room", label)
    }

    // ── Board ──
    @Test
    fun testBoardTestTagsExist() {
        assertNotNull(Strings.TestTags.boardScreen)
        assertNotNull(Strings.TestTags.boardGuestHero)
        assertNotNull(Strings.TestTags.boardTabRow)
        assertNotNull(Strings.TestTags.boardTab)
        assertNotNull(Strings.TestTags.boardSignInButton)
    }

    @Test
    fun testBoardTabTagFormat() {
        assertEquals("board_tab_generated", Strings.formatTestTag(Strings.TestTags.boardTab, "generated"))
    }

    @Test
    fun testBoardGeneratedCardTagFormat() {
        assertEquals("board_generated_card_item1", Strings.formatTestTag(Strings.TestTags.boardGeneratedCard, "item1"))
    }

    @Test
    fun testBoardFavoriteCardTagFormat() {
        assertEquals("board_favorite_card_item1", Strings.formatTestTag(Strings.TestTags.boardFavoriteCard, "item1"))
    }

    @Test
    fun testBoardProjectCardTagFormat() {
        assertEquals("board_project_card_item1", Strings.formatTestTag(Strings.TestTags.boardProjectCard, "item1"))
    }

    @Test
    fun testBoardLockedCardTagFormat() {
        assertEquals("board_locked_card_living-room", Strings.formatTestTag(Strings.TestTags.boardLockedCard, "living-room"))
    }

    @Test
    fun testBoardEmptyTitles() {
        assertEquals("No designs yet", Strings.boardEmptyGenerated)
        assertEquals("No favorites yet", Strings.boardEmptyFavorites)
        assertEquals("No projects yet", Strings.boardEmptyProjects)
    }

    @Test
    fun testBoardEmptyDescriptions() {
        assertEquals("Create your first AI design and it will appear here.", Strings.boardEmptyGeneratedBody)
        assertEquals("Save designs you love and they'll show up here.", Strings.boardEmptyFavoritesBody)
        assertEquals("Create projects to organize your design collections.", Strings.boardEmptyProjectsBody)
    }

    @Test
    fun testBoardA11yLabel() {
        val label = Strings.a11yBoardDesign("Living Room", "Modern", "Generated")
        assertEquals("Living Room \u2014 Modern \u2014 Generated", label)
    }

    @Test
    fun testBoardTabLabels() {
        assertEquals("Generated", Strings.generatedTab)
        assertEquals("Favorites", Strings.favoritesTab)
        assertEquals("Projects", Strings.projectsTab)
    }

    @Test
    fun testBoardSignInCta() {
        assertEquals("Sign in to save your designs.", Strings.boardSignInCta)
    }

    @Test
    fun testBoardTabEntries() {
        assertEquals(3, BoardTab.entries.size)
        assertEquals(BoardTab.Generated, BoardTab.entries[0])
        assertEquals(BoardTab.Favorites, BoardTab.entries[1])
        assertEquals(BoardTab.Projects, BoardTab.entries[2])
    }

    // ── Profile ──
    @Test
    fun testProfileTestTagsExist() {
        assertNotNull(Strings.TestTags.profileScreen)
        assertNotNull(Strings.TestTags.profileHeading)
        assertNotNull(Strings.TestTags.profileStatusCard)
        assertNotNull(Strings.TestTags.profileSettingsButton)
        assertNotNull(Strings.TestTags.profileSignInButton)
    }

    @Test
    fun testProfileStatusCardTagFormat() {
        assertEquals("profile_status_card_Diamonds", Strings.formatTestTag(Strings.TestTags.profileStatusCard, "Diamonds"))
    }

    @Test
    fun testProfileHeading() {
        assertEquals("Profile", Strings.myProfileTitle)
    }

    @Test
    fun testProfileSubtitle() {
        assertEquals("Manage your account and preferences", Strings.profileSubtitle)
    }

    @Test
    fun testProfileSignInStrings() {
        assertEquals("Sign in to save your projects and access your diamonds across devices.", Strings.profileSignInBody)
        assertEquals("Sign In / Register", Strings.profileSignInRegister)
    }

    @Test
    fun testProfileSettingsStrings() {
        assertEquals("Settings", Strings.settings)
    }

    @Test
    fun testProfileA11yLabels() {
        assertEquals("Profile", Strings.a11yProfileHeading)
        assertNotNull(Strings.a11yDiamondBadge)
    }

    @Test
    fun testProfileScreenStateDefaults() {
        val state = ProfileScreenState()
        assertEquals(true, state.isGuest)
    }

    @Test
    fun testProfileStatusLabels() {
        assertEquals("Diamonds", Strings.profileStatusDiamonds)
        assertEquals("Plan", Strings.profileStatusPlan)
        assertEquals("Saved", Strings.profileStatusSaved)
    }
}
