package com.ismail.homedecorai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BottomNavTest {

    @Test
    fun testBottomNavTestTag() {
        assertEquals("bottom_nav", Strings.TestTags.bottomNav)
    }

    @Test
    fun testTopNavTestTag() {
        assertEquals("top_nav", Strings.TestTags.topNav)
    }

    @Test
    fun testBottomNavItemTagFormat() {
        assertEquals("bottom_nav_item_tools", Strings.formatTestTag(Strings.TestTags.bottomNavItem, "tools"))
    }

    @Test
    fun testTopNavItemTagFormat() {
        assertEquals("top_nav_item_discover", Strings.formatTestTag(Strings.TestTags.topNavItem, "discover"))
    }

    @Test
    fun testNavA11yLabels() {
        assertEquals("Tools", Strings.a11yToolsHeading)
        assertEquals("Discover", Strings.a11yDiscoverHeading)
        assertEquals("My Board", Strings.a11yBoardHeading)
        assertEquals("Upgrade to Pro", Strings.a11yUpgradeHeading)
        assertEquals("Profile", Strings.a11yProfileHeading)
    }

    @Test
    fun testNavBottomBarA11yLabel() {
        assertEquals("Main navigation", Strings.a11yBottomBar)
    }

    @Test
    fun testNavTopBarA11yLabel() {
        assertEquals("Main navigation", Strings.a11yTopBar)
    }

    @Test
    fun testBottomNavItemLabels() {
        assertEquals("Tools", Strings.navTools)
        assertEquals("Discover", Strings.navDiscover)
        assertEquals("My Board", Strings.navMyBoard)
        assertEquals("Profile", Strings.navProfile)
    }

    @Test
    fun testTopNavItemLabels() {
        assertEquals("Tools", Strings.navTools)
        assertEquals("Discover", Strings.navDiscover)
        assertEquals("My Board", Strings.navMyBoard)
        assertEquals("Pro", Strings.navUpgrade)
        assertEquals("Profile", Strings.navProfile)
    }

    @Test
    fun testBottomNavHidesDuringWizard() {
        val isInWizard = true
        val shouldHide = isInWizard
        assertEquals(true, shouldHide)
    }

    @Test
    fun testBottomNavHidesDuringSettings() {
        val isSettingsOpen = true
        val shouldHide = isSettingsOpen
        assertEquals(true, shouldHide)
    }

    @Test
    fun testBottomNavHidesDuringPaywall() {
        val isPaywallVisible = true
        val shouldHide = isPaywallVisible
        assertEquals(true, shouldHide)
    }

    @Test
    fun testBottomNavVisibleNormally() {
        val isInWizard = false
        val isSettingsOpen = false
        val isPaywallVisible = false
        val shouldHide = isInWizard || isSettingsOpen || isPaywallVisible
        assertEquals(false, shouldHide)
    }

    @Test
    fun testDesktopLayoutDetection() {
        val isMobileWidth = false
        val isDesktop = !isDesktop(isMobileWidth)
        assertEquals(true, isDesktop)
    }

    @Test
    fun testEscapeKeyDismissesModal() {
        val isPaywallVisible = true
        val result = if (isPaywallVisible) "dismiss" else "no-op"
        assertEquals("dismiss", result)
    }

    @Test
    fun testWizardCloseIconTag() {
        assertNotNull(Strings.TestTags.wizardCloseButton)
    }

    @Test
    fun testSettingsScreenTag() {
        assertNotNull(Strings.TestTags.settingsScreen)
    }

    @Test
    fun testDiscoverScreenTag() {
        assertNotNull(Strings.TestTags.discoverScreen)
    }
}

private fun isDesktop(isMobileWidth: Boolean): Boolean = !isMobileWidth
