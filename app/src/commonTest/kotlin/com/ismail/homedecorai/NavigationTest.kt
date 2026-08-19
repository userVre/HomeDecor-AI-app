package com.ismail.homedecorai

import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.model.ToolsScreenState
import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NavigationTest {

    @Test
    fun testDirectRouteLoading_tools() {
        val path = "/tools"
        val cleanPath = path.removePrefix("/")
        val tab = when {
            cleanPath.startsWith("tools") -> "Tools"
            cleanPath.startsWith("discover") -> "Discover"
            cleanPath.startsWith("board") -> "Board"
            cleanPath.startsWith("profile") -> "Profile"
            cleanPath.startsWith("pro") -> "Upgrade"
            else -> "Tools"
        }
        assertEquals("Tools", tab)
    }

    @Test
    fun testDirectRouteLoading_discover() {
        val path = "/discover"
        val cleanPath = path.removePrefix("/")
        val tab = when {
            cleanPath.startsWith("tools") -> "Tools"
            cleanPath.startsWith("discover") -> "Discover"
            cleanPath.startsWith("board") -> "Board"
            cleanPath.startsWith("profile") -> "Profile"
            cleanPath.startsWith("pro") -> "Upgrade"
            else -> "Tools"
        }
        assertEquals("Discover", tab)
    }

    @Test
    fun testDirectRouteLoading_board() {
        val path = "/board"
        val cleanPath = path.removePrefix("/")
        val tab = when {
            cleanPath.startsWith("tools") -> "Tools"
            cleanPath.startsWith("discover") -> "Discover"
            cleanPath.startsWith("board") -> "Board"
            cleanPath.startsWith("profile") -> "Profile"
            cleanPath.startsWith("pro") -> "Upgrade"
            else -> "Tools"
        }
        assertEquals("Board", tab)
    }

    @Test
    fun testDirectRouteLoading_profile() {
        val path = "/profile"
        val cleanPath = path.removePrefix("/")
        val tab = when {
            cleanPath.startsWith("tools") -> "Tools"
            cleanPath.startsWith("discover") -> "Discover"
            cleanPath.startsWith("board") -> "Board"
            cleanPath.startsWith("profile") -> "Profile"
            cleanPath.startsWith("pro") -> "Upgrade"
            else -> "Tools"
        }
        assertEquals("Profile", tab)
    }

    @Test
    fun testDirectRouteLoading_pro() {
        val path = "/pro"
        val cleanPath = path.removePrefix("/")
        val tab = when {
            cleanPath.startsWith("tools") -> "Tools"
            cleanPath.startsWith("discover") -> "Discover"
            cleanPath.startsWith("board") -> "Board"
            cleanPath.startsWith("profile") -> "Profile"
            cleanPath.startsWith("pro") -> "Upgrade"
            else -> "Tools"
        }
        assertEquals("Upgrade", tab)
    }

    @Test
    fun testWizardRoute_parsing() {
        val path = "/create/interior"
        val match = Regex("/create/(\\w+)").find(path)
        val toolId = match?.groupValues?.get(1)
        assertEquals("interior", toolId)
    }

    @Test
    fun testWizardRoute_unknown_fallsBackToTools() {
        val path = "/create/unknown-tool"
        val match = Regex("/create/(\\w+)").find(path)
        val toolId = match?.groupValues?.get(1)
        assertNotNull(toolId)
        val toolsState = ToolsScreenState(
            tools = listOf(
                ToolItem("interior", "Interior Design", "Desc", Color.Red, Color.Blue, ""),
            )
        )
        val tool = toolsState.tools.find { it.id == toolId }
        assertEquals(null, tool)
    }

    @Test
    fun testTabEnumHasAllRoutes() {
        val routes = listOf("/tools", "/discover", "/board", "/pro", "/profile")
        routes.forEach { route ->
            val cleanPath = route.removePrefix("/")
            val matched = when {
                cleanPath.startsWith("tools") -> true
                cleanPath.startsWith("discover") -> true
                cleanPath.startsWith("board") -> true
                cleanPath.startsWith("profile") -> true
                cleanPath.startsWith("pro") -> true
                else -> false
            }
            assertTrue(matched, "Route $route should match a tab")
        }
    }

    @Test
    fun testScreenHeadingLabels() {
        assertEquals("Tools", Strings.a11yToolsHeading)
        assertEquals("Discover", Strings.a11yDiscoverHeading)
        assertEquals("My Board", Strings.a11yBoardHeading)
        assertEquals("Profile", Strings.a11yProfileHeading)
        assertEquals("Upgrade to Pro", Strings.a11yUpgradeHeading)
    }

    @Test
    fun testTestTagsConsistency() {
        assertNotNull(Strings.TestTags.bottomNav)
        assertNotNull(Strings.TestTags.topNav)
        assertNotNull(Strings.TestTags.toolsScreen)
        assertNotNull(Strings.TestTags.discoverScreen)
        assertNotNull(Strings.TestTags.boardScreen)
        assertNotNull(Strings.TestTags.profileScreen)
        assertNotNull(Strings.TestTags.upgradeScreen)
        assertNotNull(Strings.TestTags.wizardScreen)
        assertNotNull(Strings.TestTags.paywallSheet)
    }

    @Test
    fun testFormatTestTag() {
        val tag = Strings.formatTestTag(Strings.TestTags.toolCard, "interior")
        assertEquals("tool_card_interior", tag)
    }
}
