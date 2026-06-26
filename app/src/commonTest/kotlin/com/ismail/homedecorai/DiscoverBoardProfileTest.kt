package com.ismail.homedecorai

import com.ismail.homedecorai.model.BoardScreenState
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
        assertEquals("discover_cluster_tab_rooms", Strings.TestTags.discoverClusterTab.format("rooms"))
    }

    @Test
    fun testDiscoverSeeAllTagFormat() {
        assertEquals("discover_see_all_all", Strings.TestTags.discoverSeeAll.format("all"))
    }

    @Test
    fun testDiscoverSectionRowTagFormat() {
        assertEquals("discover_section_row_1", Strings.TestTags.discoverSectionRow.format("1"))
    }

    @Test
    fun testDiscoverSectionCardTagFormat() {
        assertEquals("discover_section_card_1_0", Strings.TestTags.discoverSectionCard.format("1", "0"))
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
        assertEquals("Living Room — Modern Interior", label)
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
        assertEquals("board_tab_generated", Strings.TestTags.boardTab.format("generated"))
    }

    @Test
    fun testBoardEmptyTitle() {
        assertEquals("No designs yet", Strings.boardEmptyTitle)
    }

    @Test
    fun testBoardEmptyDescription() {
        assertEquals("Create your first design and it will appear here.", Strings.boardEmptyDesc)
    }

    @Test
    fun testBoardA11yLabel() {
        val label = Strings.a11yBoardDesign("Living Room", "Modern", "Generated")
        assertEquals("Living Room — Modern — Generated", label)
    }

    @Test
    fun testBoardGuestState() {
        val state = BoardScreenState(isGuest = true)
        assertEquals(true, state.isGuest)
    }

    @Test
    fun testBoardCreatedTabLabel() {
        assertEquals("Created", Strings.boardTabCreated)
    }

    @Test
    fun testBoardSavedTabLabel() {
        assertEquals("Saved", Strings.boardTabSaved)
    }

    @Test
    fun testBoardSignInTitle() {
        assertEquals("Sign in to save your designs", Strings.boardSignInTitle)
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
        assertEquals("profile_status_card_pro", Strings.TestTags.profileStatusCard.format("pro"))
    }

    @Test
    fun testProfileHeading() {
        assertEquals("Profile", Strings.profileHeading)
    }

    @Test
    fun testProfileStatusTitle() {
        assertEquals("Subscription Status", Strings.profileStatusTitle)
    }

    @Test
    fun testProfileSignInTitle() {
        assertEquals("Sign in to save your progress", Strings.profileSignInTitle)
    }

    @Test
    fun testProfileSettingsStrings() {
        assertEquals("Settings", Strings.settingsTitle)
        assertNotNull(Strings.settingsNotificationLabel)
        assertNotNull(Strings.settingsPrivacyPolicy)
        assertNotNull(Strings.settingsTermsOfService)
    }

    @Test
    fun testProfileA11yLabels() {
        assertEquals("Profile", Strings.a11yProfileHeading)
        assertNotNull(Strings.a11yDiamondBadge)
    }

    @Test
    fun testProfileScreenStateDefaults() {
        val state = ProfileScreenState(isSignedIn = false, isSignedOut = true)
        assertEquals(false, state.isSignedIn)
        assertEquals(true, state.isSignedOut)
    }
}
