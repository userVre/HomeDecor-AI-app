package com.ismail.homedecorai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestTagsTest {

    @Test
    fun bottomNav_hasTestTag() {
        assertNotNull(Strings.TestTags.bottomNav, "bottomNav test tag must not be null")
        assertEquals("bottomNav", Strings.TestTags.bottomNav)
    }

    @Test
    fun topNav_hasTestTag() {
        assertNotNull(Strings.TestTags.topNav, "topNav test tag must not be null")
        assertEquals("topNav", Strings.TestTags.topNav)
    }

    @Test
    fun bottomNavItem_formatsCorrectly() {
        val tag = Strings.TestTags.bottomNavItem.format("tools")
        assertEquals("bottomNavItem_tools", tag)
    }

    @Test
    fun topNavItem_formatsCorrectly() {
        val tag = Strings.TestTags.topNavItem.format("discover")
        assertEquals("topNavItem_discover", tag)
    }

    @Test
    fun toolsScreen_hasTestTag() {
        assertEquals("toolsScreen", Strings.TestTags.toolsScreen)
    }

    @Test
    fun toolCard_formatsCorrectly() {
        val tag = Strings.TestTags.toolCard.format("room-vision")
        assertEquals("toolCard_room-vision", tag)
    }

    @Test
    fun discoverScreen_hasTestTag() {
        assertEquals("discoverScreen", Strings.TestTags.discoverScreen)
    }

    @Test
    fun discoverClusterTab_formatsCorrectly() {
        val tag = Strings.TestTags.discoverClusterTab.format("All")
        assertEquals("discoverClusterTab_All", tag)
    }

    @Test
    fun discoverSectionCard_formatsCorrectly() {
        val tag = Strings.TestTags.discoverSectionCard.format("living-room-001")
        assertEquals("discoverSectionCard_living-room-001", tag)
    }

    @Test
    fun boardScreen_hasTestTag() {
        assertEquals("boardScreen", Strings.TestTags.boardScreen)
    }

    @Test
    fun boardTab_formatsCorrectly() {
        val tag = Strings.TestTags.boardTab.format("Favorites")
        assertEquals("boardTab_Favorites", tag)
    }

    @Test
    fun profileScreen_hasTestTag() {
        assertEquals("profileScreen", Strings.TestTags.profileScreen)
    }

    @Test
    fun profileRow_formatsCorrectly() {
        val tag = Strings.TestTags.profileRow.format("My Projects")
        assertEquals("profileRow_My Projects", tag)
    }

    @Test
    fun wizardScreen_hasTestTag() {
        assertEquals("wizardScreen", Strings.TestTags.wizardScreen)
    }

    @Test
    fun paywallSheet_hasTestTag() {
        assertEquals("paywallSheet", Strings.TestTags.paywallSheet)
    }

    @Test
    fun paywallCtaButton_hasTestTag() {
        assertEquals("paywallCtaButton", Strings.TestTags.paywallCtaButton)
    }

    @Test
    fun upgradeCtaButton_hasTestTag() {
        assertEquals("upgradeCtaButton", Strings.TestTags.upgradeCtaButton)
    }

    @Test
    fun upgradePlanCard_formatsCorrectly() {
        val tag = Strings.TestTags.upgradePlanCard.format("diamond")
        assertEquals("upgradePlanCard_diamond", tag)
    }
}
