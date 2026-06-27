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
    fun a11yBottomNavItem_includesLabel() {
        val result = Strings.a11yBottomNavItem("Tools")
        assertTrue(result.contains("Tools"), "Should contain item label")
        assertTrue(result.contains("Navigate to"), "Should include navigation prompt")
    }

    @Test
    fun a11yTopNavItem_includesLabel() {
        val result = Strings.a11yTopNavItem("Tools")
        assertTrue(result.contains("Tools"), "Should contain item label")
    }

    @Test
    fun a11yBoardTab_includesTabNameAndState() {
        val result = Strings.a11yBoardTab("Favorites", selected = true)
        assertTrue(result.contains("Favorites"), "Should contain tab name")
        assertTrue(result.contains("selected"), "Should indicate selected state")
    }

    @Test
    fun a11yBoardTab_notSelected() {
        val result = Strings.a11yBoardTab("Favorites", selected = false)
        assertTrue(result.contains("Favorites"), "Should contain tab name")
        assertTrue(!result.contains("selected"), "Should not indicate selected state")
    }

    @Test
    fun a11yPaywallPlan_includesPlanNameAndState() {
        val result = Strings.a11yPaywallPlan("Yearly", selected = true)
        assertTrue(result.contains("Yearly"), "Should contain plan name")
        assertTrue(result.contains("selected"), "Should indicate selected state")
    }

    @Test
    fun a11yUpgradePlan_includesPlanName() {
        val result = Strings.a11yUpgradePlan("Free", recommended = false)
        assertTrue(result.contains("Free"), "Should contain plan name")
    }

    @Test
    fun a11yUpgradePlan_recommended() {
        val result = Strings.a11yUpgradePlan("Yearly", recommended = true)
        assertTrue(result.contains("Yearly"), "Should contain plan name")
        assertTrue(result.contains("recommended"), "Should indicate recommended")
    }

    @Test
    fun a11yProfileRow_includesTitle() {
        val result = Strings.a11yProfileRow("My Projects")
        assertEquals("My Projects", result)
    }

    @Test
    fun a11yWizardOption_includesLabelAndState() {
        val result = Strings.a11yWizardOption("Bedroom", selected = true)
        assertTrue(result.contains("Bedroom"), "Should contain option label")
        assertTrue(result.contains("selected"), "Should indicate selected state")
    }

    @Test
    fun a11yWizardOption_notSelected() {
        val result = Strings.a11yWizardOption("Bedroom", selected = false)
        assertTrue(result.contains("Bedroom"), "Should contain option label")
        assertTrue(!result.contains("selected"), "Should not indicate selected state")
    }

    @Test
    fun a11yDiscoverCluster_includesLabel() {
        val result = Strings.a11yDiscoverCluster("Interior")
        assertEquals("Cluster: Interior", result)
    }

    @Test
    fun a11yDiscoverCard_includesTitleAndCategory() {
        val result = Strings.a11yDiscoverCard("Living Room", "Modern Interior")
        assertEquals("Living Room \u2014 Modern Interior", result)
    }

    @Test
    fun a11yBoardDesign_includesAllParts() {
        val result = Strings.a11yBoardDesign("Bedroom", "Bohemian", "Generated")
        assertEquals("Bedroom \u2014 Bohemian \u2014 Generated", result)
    }

    @Test
    fun a11yPaywallStep_includesStepAndHeading() {
        val result = Strings.a11yPaywallStep(3, "Choose plan")
        assertEquals("Step 3: Choose plan", result)
    }

    @Test
    fun a11yWizardStep_includesStepTotalAndLabel() {
        val result = Strings.a11yWizardStep(2, 4, "Room Type")
        assertEquals("Step 2 of 4: Room Type", result)
    }
}
