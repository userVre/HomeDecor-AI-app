package com.ismail.homedecorai

import androidx.compose.ui.graphics.Color
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.model.ToolsScreenState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ToolsScreenTest {

    private fun createToolsState(
        isPro: Boolean = false,
        diamonds: Int = 150,
        tools: List<ToolItem> = listOf(
            ToolItem("interior", "Interior Design", "Redesign any room", Color(0xFF2E6B6E), Color(0xFF1A4A4C), "images/tool_interior.webp"),
            ToolItem("facade", "Exterior Design", "Transform exterior", Color(0xFF3B5998), Color(0xFF1E3A5F), "images/tool_exterior.webp"),
            ToolItem("garden", "Garden Design", "Plan garden", Color(0xFF2D6A4F), Color(0xFF1B4332), "images/tool_garden.webp"),
            ToolItem("paint", "Smart Wall Paint", "Preview colors", Color(0xFFC45B3F), Color(0xFF8B2E1A), "images/tool_paint.webp"),
            ToolItem("floor", "Floor Design", "Explore flooring", Color(0xFF8B6914), Color(0xFF5C4510), "images/tool_floor.webp"),
            ToolItem("layout", "Layout Makeover", "Optimize layout", Color(0xFF5B4FCF), Color(0xFF3A2D8F), "images/tool_layout.webp"),
            ToolItem("replace", "Replace Furniture", "Swap furniture", Color(0xFFB85C38), Color(0xFF7A3520), "images/tool_replace.webp"),
            ToolItem("reference", "Reference Style", "Use reference", Color(0xFF1A3A5C), Color(0xFF0D2240), "images/tool_reference.webp"),
        ),
    ) = ToolsScreenState(isPro = isPro, diamonds = diamonds, tools = tools)

    @Test
    fun testToolsScreenTestTag() {
        assertEquals("tools_screen", Strings.TestTags.toolsScreen)
    }

    @Test
    fun testToolsHeaderTestTag() {
        assertEquals("tools_header", Strings.TestTags.toolsHeader)
    }

    @Test
    fun testToolCardTestTagFormat() {
        assertEquals("tool_card_interior", Strings.formatTestTag(Strings.TestTags.toolCard, "interior"))
    }

    @Test
    fun testToolsScreenStateDefaults() {
        val state = createToolsState()
        assertEquals(false, state.isPro)
        assertEquals(150, state.diamonds)
        assertEquals(8, state.tools.size)
    }

    @Test
    fun testToolItemProperties() {
        val state = createToolsState()
        state.tools.forEach { tool ->
            assertTrue(tool.id.isNotEmpty(), "Tool ${tool.id} should have non-empty id")
            assertTrue(tool.title.isNotEmpty(), "Tool ${tool.id} should have non-empty title")
            assertTrue(tool.description.isNotEmpty(), "Tool ${tool.id} should have non-empty description")
        }
    }

    @Test
    fun testToolTitles() {
        assertEquals("Interior Design", Strings.toolTitle("interior"))
        assertEquals("Exterior Design", Strings.toolTitle("facade"))
        assertEquals("Garden Design", Strings.toolTitle("garden"))
        assertEquals("Smart Wall Paint", Strings.toolTitle("paint"))
        assertEquals("Floor Design", Strings.toolTitle("floor"))
        assertEquals("Layout Makeover", Strings.toolTitle("layout"))
        assertEquals("Replace Furniture", Strings.toolTitle("replace"))
        assertEquals("Reference Style", Strings.toolTitle("reference"))
    }

    @Test
    fun testToolDescriptions() {
        assertTrue(Strings.toolDescription("interior").contains("room"))
        assertTrue(Strings.toolDescription("garden").contains("garden"))
    }

    @Test
    fun testA11yToolCardLabel() {
        assertEquals("Interior Design: Redesign any room", Strings.a11yToolCard("Interior Design", "Redesign any room"))
    }

    @Test
    fun testDiamondStoreLabel() {
        assertEquals("Open diamond store", Strings.a11yOpenDiamondStore)
    }

    @Test
    fun testToolsLoadingState() {
        val state = ToolsScreenState(isLoading = true)
        assertTrue(state.isLoading)
    }

    @Test
    fun testToolsErrorState() {
        val state = ToolsScreenState(error = "Network error")
        assertEquals("Network error", state.error)
    }
}
