package com.ismail.homedecorai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestTagsTest {

    @Test
    fun bottomNav_hasTestTag() {
        assertNotNull(Strings.TestTags.bottomNav)
        assertEquals("bottom_nav", Strings.TestTags.bottomNav)
    }

    @Test
    fun topNav_hasTestTag() {
        assertNotNull(Strings.TestTags.topNav)
        assertEquals("top_nav", Strings.TestTags.topNav)
    }

    @Test
    fun bottomNavItem_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.bottomNavItem, "tools")
        assertEquals("bottom_nav_item_tools", tag)
    }

    @Test
    fun topNavItem_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.topNavItem, "discover")
        assertEquals("top_nav_item_discover", tag)
    }

    @Test
    fun toolsScreen_hasTestTag() {
        assertEquals("tools_screen", Strings.TestTags.toolsScreen)
    }

    @Test
    fun toolCard_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.toolCard, "interior")
        assertEquals("tool_card_interior", tag)
    }

    @Test
    fun discoverScreen_hasTestTag() {
        assertEquals("discover_screen", Strings.TestTags.discoverScreen)
    }

    @Test
    fun discoverClusterTab_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.discoverClusterTab, "interior")
        assertEquals("discover_cluster_tab_interior", tag)
    }

    @Test
    fun discoverSectionCard_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.discoverSectionCard, "living-room-001")
        assertEquals("discover_section_card_living-room-001", tag)
    }

    @Test
    fun boardScreen_hasTestTag() {
        assertEquals("board_screen", Strings.TestTags.boardScreen)
    }

    @Test
    fun boardTab_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.boardTab, "Favorites")
        assertEquals("board_tab_Favorites", tag)
    }

    @Test
    fun profileScreen_hasTestTag() {
        assertEquals("profile_screen", Strings.TestTags.profileScreen)
    }

    @Test
    fun profileRow_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.profileRow, "My Projects")
        assertEquals("profile_row_My Projects", tag)
    }

    @Test
    fun wizardScreen_hasTestTag() {
        assertEquals("wizard_screen", Strings.TestTags.wizardScreen)
    }

    @Test
    fun paywallSheet_hasTestTag() {
        assertEquals("paywall_sheet", Strings.TestTags.paywallSheet)
    }

    @Test
    fun paywallCtaButton_hasTestTag() {
        assertEquals("paywall_cta_button", Strings.TestTags.paywallCtaButton)
    }

    @Test
    fun upgradeCtaButton_hasTestTag() {
        assertEquals("upgrade_cta_button", Strings.TestTags.upgradeCtaButton)
    }

    @Test
    fun upgradePlanCard_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.upgradePlanCard, "yearly")
        assertEquals("upgrade_plan_card_yearly", tag)
    }

    @Test
    fun settingsScreen_hasTestTag() {
        assertEquals("settings_screen", Strings.TestTags.settingsScreen)
    }

    @Test
    fun discoverClusterTabRow_hasTestTag() {
        assertEquals("discover_cluster_tab_row", Strings.TestTags.discoverClusterTabRow)
    }

    @Test
    fun discoverSectionScroll_hasTestTag() {
        assertEquals("discover_section_scroll", Strings.TestTags.discoverSectionScroll)
    }

    @Test
    fun boardLockedCard_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.boardLockedCard, "living-room")
        assertEquals("board_locked_card_living-room", tag)
    }

    @Test
    fun paywallStepContent_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.paywallStepContent, 3)
        assertEquals("paywall_step_content_3", tag)
    }

    @Test
    fun wizardStepContent_formatsCorrectly() {
        val tag = Strings.formatTestTag(Strings.TestTags.wizardStepContent, "Upload")
        assertEquals("wizard_step_content_Upload", tag)
    }
}
