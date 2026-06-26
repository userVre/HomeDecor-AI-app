package com.ismail.homedecorai

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class StringsTest {

    @Test
    fun toolTitle_containsToolId() {
        val title = Strings.toolTitle("room-vision")
        assertTrue(title.contains("room-vision"), "Tool title should contain the tool ID")
    }

    @Test
    fun a11yBottomNavItem_includesRoleAndState() {
        val result = Strings.a11yBottomNavItem("Tools", isSelected = true)
        assertTrue(result.contains("Tools"), "Should contain item label")
        assertTrue(result.contains("selected") || result.contains("active"), "Should indicate selected state")
    }

    @Test
    fun a11yTopNavItem_includesLabel() {
        val result = Strings.a11yTopNavItem("Tools")
        assertTrue(result.contains("Tools"), "Should contain item label")
    }

    @Test
    fun a11yBoardTab_includesTabNameAndState() {
        val result = Strings.a11yBoardTab("Favorites", isSelected = true)
        assertTrue(result.contains("Favorites"), "Should contain tab name")
        assertTrue(result.contains("selected") || result.contains("active"), "Should indicate selected state")
    }

    @Test
    fun a11yPaywallPlan_includesPlanNameAndPrice() {
        val result = Strings.a11yPaywallPlan("Diamond", "$4.99/mo", isSelected = true)
        assertTrue(result.contains("Diamond"), "Should contain plan name")
        assertTrue(result.contains("$4.99/mo"), "Should contain price")
        assertTrue(result.contains("selected") || result.contains("active"), "Should indicate selected state")
    }

    @Test
    fun a11yUpgradePlan_includesPlanName() {
        val result = Strings.a11yUpgradePlan("Free", isSelected = false)
        assertTrue(result.contains("Free"), "Should contain plan name")
    }

    @Test
    fun a11yProfileRow_includesTitle() {
        val result = Strings.a11yProfileRow("My Projects")
        assertTrue(result.contains("My Projects"), "Should contain row title")
    }

    @Test
    fun a11yWizardOption_includesLabelAndState() {
        val result = Strings.a11yWizardOption("Bedroom", isSelected = true)
        assertTrue(result.contains("Bedroom"), "Should contain option label")
        assertTrue(result.contains("selected") || result.contains("active"), "Should indicate selected state")
    }
}
