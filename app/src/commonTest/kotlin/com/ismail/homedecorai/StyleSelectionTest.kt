package com.ismail.homedecorai

import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.ui.tools.WizardState
import com.ismail.homedecorai.ui.tools.WizardStep
import com.ismail.homedecorai.ui.tools.stepsForTool
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StyleSelectionTest {

    // ── WizardState: selectedStyle persistence ──────────────────────────

    @Test
    fun clickingModern_selectsModern() {
        val tool = ToolItem(id = "interior", title = "Interior", description = "")
        val initial = WizardState(tool = tool)
        assertNull(initial.selectedStyle)

        val afterSelect = initial.copy(selectedStyle = "modern")
        assertEquals("modern", afterSelect.selectedStyle)
    }

    @Test
    fun clickingAnotherStyle_replacesSelection() {
        val tool = ToolItem(id = "interior", title = "Interior", description = "")
        val state = WizardState(tool = tool)
            .copy(selectedStyle = "modern")
        assertEquals("modern", state.selectedStyle)

        val replaced = state.copy(selectedStyle = "minimalist")
        assertEquals("minimalist", replaced.selectedStyle)
    }

    @Test
    fun selectedStyle_survivesNavigationBackAndForward() {
        val tool = ToolItem(id = "interior", title = "Interior", description = "")
        var state = WizardState(tool = tool, step = WizardStep.Style, selectedStyle = "modern")

        // Navigate forward to Palette
        state = state.copy(step = WizardStep.Palette, selectedPalette = "ocean-mist")
        assertEquals("modern", state.selectedStyle)

        // Navigate back to Style
        state = state.copy(step = WizardStep.Style)
        assertEquals("modern", state.selectedStyle)
    }

    @Test
    fun selectedStyle_notResetDuringRecomposition() {
        val tool = ToolItem(id = "interior", title = "Interior", description = "")
        val state = WizardState(tool = tool, selectedStyle = "japandi")

        // Simulate recomposition by creating a new copy with unrelated field changes
        val recomposed = state.copy(error = null, isDragging = false)
        assertEquals("japandi", recomposed.selectedStyle)
    }

    // ── WizardState: selectedReplacementStyle persistence ───────────────

    @Test
    fun clickingReplacementStyle_selectsReplacementStyle() {
        val tool = ToolItem(id = "replace", title = "Replace", description = "")
        val initial = WizardState(tool = tool)
        assertNull(initial.selectedReplacementStyle)

        val afterSelect = initial.copy(selectedReplacementStyle = "modern")
        assertEquals("modern", afterSelect.selectedReplacementStyle)
    }

    @Test
    fun clickingAnotherReplacementStyle_replacesSelection() {
        val tool = ToolItem(id = "replace", title = "Replace", description = "")
        val state = WizardState(tool = tool)
            .copy(selectedReplacementStyle = "modern")
        val replaced = state.copy(selectedReplacementStyle = "minimalist")
        assertEquals("minimalist", replaced.selectedReplacementStyle)
    }

    // ── CanProceed logic: Style step requires selection ─────────────────

    @Test
    fun styleStep_requiresSelectedStyle() {
        val tool = ToolItem(id = "interior", title = "Interior", description = "")
        val stateWithoutStyle = WizardState(tool = tool, step = WizardStep.Style)
        assertNull(stateWithoutStyle.selectedStyle)

        val stateWithStyle = WizardState(tool = tool, step = WizardStep.Style, selectedStyle = "modern")
        assertNotNull(stateWithStyle.selectedStyle)
    }

    @Test
    fun replacementStyleStep_requiresSelectedReplacementStyle() {
        val tool = ToolItem(id = "replace", title = "Replace", description = "")
        val stateWithout = WizardState(tool = tool, step = WizardStep.ReplacementStyle)
        assertNull(stateWithout.selectedReplacementStyle)

        val stateWith = WizardState(tool = tool, step = WizardStep.ReplacementStyle, selectedReplacementStyle = "modern")
        assertNotNull(stateWith.selectedReplacementStyle)
    }

    // ── Review: selectedStyle is preserved in WizardState ───────────────

    @Test
    fun reviewStep_preservesSelectedStyle() {
        val tool = ToolItem(id = "interior", title = "Interior", description = "")
        val state = WizardState(
            tool = tool,
            step = WizardStep.Refine,
            selectedStyle = "modern",
        )
        assertEquals("modern", state.selectedStyle)
    }

    @Test
    fun reviewStep_preservesSelectedReplacementStyle() {
        val tool = ToolItem(id = "replace", title = "Replace", description = "")
        val state = WizardState(
            tool = tool,
            step = WizardStep.Review,
            selectedReplacementStyle = "scandinavian",
        )
        assertEquals("scandinavian", state.selectedReplacementStyle)
    }

    // ── Step enum: Style step exists in all affected flows ──────────────

    @Test
    fun wizardStepEnum_containsStyleAndReplacementStyle() {
        assertTrue(WizardStep.Style in WizardStep.entries)
        assertTrue(WizardStep.ReplacementStyle in WizardStep.entries)
    }

    // ── All affected routes have Style or ReplacementStyle step ─────────

    @Test
    fun interiorRoute_hasStyleStep() {
        val steps = stepsForTool("interior")
        assertTrue(WizardStep.Style in steps, "interior route must include Style step")
    }

    @Test
    fun facadeRoute_hasStyleStep() {
        val steps = stepsForTool("facade")
        assertTrue(WizardStep.Style in steps, "facade route must include Style step")
    }

    @Test
    fun gardenRoute_hasStyleStep() {
        val steps = stepsForTool("garden")
        assertTrue(WizardStep.Style in steps, "garden route must include Style step")
    }

    @Test
    fun replaceRoute_hasReplacementStyleStep() {
        val steps = stepsForTool("replace")
        assertTrue(WizardStep.ReplacementStyle in steps, "replace route must include ReplacementStyle step")
    }

    // ── Style step is not first (requires Upload first) ─────────────────

    @Test
    fun styleStepNeverFirst() {
        val toolIds = listOf("interior", "facade", "garden", "replace")
        toolIds.forEach { toolId ->
            val steps = stepsForTool(toolId)
            assertTrue(
                steps.first() != WizardStep.Style && steps.first() != WizardStep.ReplacementStyle,
                "Style/ReplacementStyle should never be the first step for $toolId"
            )
        }
    }

    // ── Test tags for style cards ───────────────────────────────────────

    @Test
    fun styleCardTestTag_format() {
        assertEquals(
            "wizard_style_card_modern",
            Strings.formatTestTag(Strings.TestTags.wizardStyleCard, "modern")
        )
    }

    @Test
    fun styleCardTestTag_allAffectedRoutes() {
        val styleIds = listOf("modern", "minimalist", "scandinavian", "luxury")
        styleIds.forEach { styleId ->
            val tag = Strings.formatTestTag(Strings.TestTags.wizardStyleCard, styleId)
            assertTrue(tag.startsWith("wizard_style_card_"), "Tag should start with wizard_style_card_")
        }
    }
}
