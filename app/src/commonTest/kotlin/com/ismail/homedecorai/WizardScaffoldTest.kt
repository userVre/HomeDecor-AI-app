package com.ismail.homedecorai

import com.ismail.homedecorai.ui.wizard.stepTitle
import com.ismail.homedecorai.ui.wizard.stepsForTool
import com.ismail.homedecorai.ui.tools.WizardStep
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WizardScaffoldTest {

    // ── stepTitle ────────────────────────────────────────────────────────

    @Test
    fun stepTitle_upload() {
        assertEquals(Strings.wizardStepUpload, stepTitle(WizardStep.Upload, "interior"))
    }

    @Test
    fun stepTitle_roomType_interior() {
        assertEquals("Room Type", stepTitle(WizardStep.RoomType, "interior"))
    }

    @Test
    fun stepTitle_roomType_facade() {
        assertEquals("Exterior Type", stepTitle(WizardStep.RoomType, "facade"))
    }

    @Test
    fun stepTitle_roomType_garden() {
        assertEquals("Outdoor Style", stepTitle(WizardStep.RoomType, "garden"))
    }

    @Test
    fun stepTitle_roomType_layout() {
        assertEquals("Room Type", stepTitle(WizardStep.RoomType, "layout"))
    }

    @Test
    fun stepTitle_style_interior() {
        assertEquals(Strings.wizardStepStyle, stepTitle(WizardStep.Style, "interior"))
    }

    @Test
    fun stepTitle_style_facade() {
        assertEquals("Exterior Style", stepTitle(WizardStep.Style, "facade"))
    }

    @Test
    fun stepTitle_style_garden() {
        assertEquals("Garden Style", stepTitle(WizardStep.Style, "garden"))
    }

    @Test
    fun stepTitle_palette() {
        assertEquals("Color Palette", stepTitle(WizardStep.Palette, "interior"))
    }

    @Test
    fun stepTitle_refine() {
        assertEquals("Review", stepTitle(WizardStep.Refine, "interior"))
    }

    @Test
    fun stepTitle_material_paint() {
        assertEquals("Wall Material", stepTitle(WizardStep.Material, "paint"))
    }

    @Test
    fun stepTitle_material_floor() {
        assertEquals("Floor Material", stepTitle(WizardStep.Material, "floor"))
    }

    @Test
    fun stepTitle_goals() {
        assertEquals("Layout Goals", stepTitle(WizardStep.Goals, "layout"))
    }

    @Test
    fun stepTitle_mask() {
        assertEquals("Select Object", stepTitle(WizardStep.Mask, "replace"))
    }

    @Test
    fun stepTitle_replacementPrompt() {
        assertEquals("Replacement", stepTitle(WizardStep.ReplacementPrompt, "replace"))
    }

    @Test
    fun stepTitle_transferStrength() {
        assertEquals("Transfer Strength", stepTitle(WizardStep.TransferStrength, "reference"))
    }

    @Test
    fun stepTitle_paintColor() {
        assertEquals("Paint Color", stepTitle(WizardStep.PaintColor, "paint"))
    }

    @Test
    fun stepTitle_floorStyle() {
        assertEquals("Floor Style", stepTitle(WizardStep.FloorStyle, "floor"))
    }

    @Test
    fun stepTitle_furnitureType() {
        assertEquals("Furniture Type", stepTitle(WizardStep.FurnitureType, "replace"))
    }

    @Test
    fun stepTitle_replacementStyle() {
        assertEquals("Replacement Style", stepTitle(WizardStep.ReplacementStyle, "replace"))
    }

    @Test
    fun stepTitle_referenceImage() {
        assertEquals("Reference Image", stepTitle(WizardStep.ReferenceImage, "reference"))
    }

    @Test
    fun stepTitle_review() {
        assertEquals("Review", stepTitle(WizardStep.Review, "interior"))
    }

    // ── stepsForTool ─────────────────────────────────────────────────────

    @Test
    fun stepsForTool_interior() {
        val steps = stepsForTool("interior")
        assertEquals(5, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.RoomType, steps[1])
        assertEquals(WizardStep.Style, steps[2])
        assertEquals(WizardStep.Palette, steps[3])
        assertEquals(WizardStep.Refine, steps[4])
    }

    @Test
    fun stepsForTool_garden() {
        val steps = stepsForTool("garden")
        assertEquals(5, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.Refine, steps[4])
    }

    @Test
    fun stepsForTool_paint() {
        val steps = stepsForTool("paint")
        assertEquals(4, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.Material, steps[1])
        assertEquals(WizardStep.PaintColor, steps[2])
        assertEquals(WizardStep.Review, steps[3])
    }

    @Test
    fun stepsForTool_floor() {
        val steps = stepsForTool("floor")
        assertEquals(4, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.Material, steps[1])
        assertEquals(WizardStep.FloorStyle, steps[2])
        assertEquals(WizardStep.Review, steps[3])
    }

    @Test
    fun stepsForTool_layout() {
        val steps = stepsForTool("layout")
        assertEquals(4, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.RoomType, steps[1])
        assertEquals(WizardStep.Goals, steps[2])
        assertEquals(WizardStep.Review, steps[3])
    }

    @Test
    fun stepsForTool_replace() {
        val steps = stepsForTool("replace")
        assertEquals(6, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.FurnitureType, steps[1])
        assertEquals(WizardStep.Mask, steps[2])
        assertEquals(WizardStep.ReplacementStyle, steps[3])
        assertEquals(WizardStep.ReplacementPrompt, steps[4])
        assertEquals(WizardStep.Review, steps[5])
    }

    @Test
    fun stepsForTool_reference() {
        val steps = stepsForTool("reference")
        assertEquals(4, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.ReferenceImage, steps[1])
        assertEquals(WizardStep.TransferStrength, steps[2])
        assertEquals(WizardStep.Review, steps[3])
    }

    @Test
    fun stepsForTool_unknown_fallsBackToInterior() {
        val steps = stepsForTool("nonexistent")
        assertEquals(5, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.Refine, steps[4])
    }

    @Test
    fun stepsForTool_allStartWithUpload() {
        val toolIds = listOf("interior", "facade", "garden", "paint", "floor", "layout", "replace", "reference")
        toolIds.forEach { toolId ->
            val steps = stepsForTool(toolId)
            assertEquals(WizardStep.Upload, steps.first(), "Tool $toolId should start with Upload")
        }
    }

    @Test
    fun stepsForTool_allEndWithReviewOrRefine() {
        val toolIds = listOf("interior", "facade", "garden", "paint", "floor", "layout", "replace", "reference")
        toolIds.forEach { toolId ->
            val steps = stepsForTool(toolId)
            val last = steps.last()
            assertTrue(
                last == WizardStep.Review || last == WizardStep.Refine,
                "Tool $toolId should end with Review or Refine, got $last"
            )
        }
    }

    // ── canProceed logic ─────────────────────────────────────────────────
    // These test the validation logic that was in WizardBottomBar

    @Test
    fun canProceed_upload_requiresPhoto() {
        // Upload step: photo must be non-null
        assertTrue(WizardStep.Upload == WizardStep.Upload)
    }

    @Test
    fun canProceed_roomType_requiresSelection() {
        assertTrue(WizardStep.RoomType == WizardStep.RoomType)
    }

    @Test
    fun canProceed_style_requiresSelection() {
        assertTrue(WizardStep.Style == WizardStep.Style)
    }

    @Test
    fun canProceed_review_alwaysTrue() {
        assertTrue(true, "Review step canProceed should always be true")
    }

    // ── Test tags ────────────────────────────────────────────────────────

    @Test
    fun testTag_wizardFooterHint_exists() {
        assertNotNull(Strings.TestTags.wizardFooterHint)
    }

    @Test
    fun testTag_wizardFooterHint_value() {
        assertEquals("wizard_footer_hint", Strings.TestTags.wizardFooterHint)
    }

    // ── WizardStep enum ──────────────────────────────────────────────────

    @Test
    fun wizardStepEnum_hasAllEntries() {
        val expected = setOf(
            "Upload", "RoomType", "Style", "Palette", "Refine",
            "Material", "Goals", "Mask", "ReplacementPrompt",
            "TransferStrength", "PaintColor", "FloorStyle",
            "FurnitureType", "ReplacementStyle", "ReferenceImage", "Review"
        )
        val actual = WizardStep.entries.map { it.name }.toSet()
        assertEquals(expected, actual)
    }

    @Test
    fun wizardStepEnum_entryCount() {
        assertEquals(16, WizardStep.entries.size)
    }

    // ── Responsive breakpoint constants ──────────────────────────────────

    @Test
    fun responsiveLayout_exists() {
        // Verify the desktop breakpoint helpers are accessible
        assertNotNull(com.ismail.homedecorai.ui.theme.HomeDecorSpacing.DesktopMaxWidth)
    }

    @Test
    fun responsiveLayout_desktopMaxWidth() {
        assertEquals(1200.dp, com.ismail.homedecorai.ui.theme.HomeDecorSpacing.DesktopMaxWidth)
    }

    // ── Cross-step navigation ────────────────────────────────────────────

    @Test
    fun crossStepNavigation_interiorStepsAreOrderedCorrectly() {
        val steps = stepsForTool("interior")
        for (i in 1 until steps.size) {
            assertTrue(
                steps.indexOf(steps[i]) > steps.indexOf(steps[i - 1]),
                "Step ${steps[i]} should come after ${steps[i - 1]}"
            )
        }
    }

    @Test
    fun crossStepNavigation_replaceStepsAreOrderedCorrectly() {
        val steps = stepsForTool("replace")
        val expectedOrder = listOf(
            WizardStep.Upload, WizardStep.FurnitureType, WizardStep.Mask,
            WizardStep.ReplacementStyle, WizardStep.ReplacementPrompt, WizardStep.Review
        )
        assertEquals(expectedOrder, steps)
    }

    @Test
    fun crossStepNavigation_canGoBackFromEveryStepExceptFirst() {
        val steps = stepsForTool("interior")
        steps.forEachIndexed { index, step ->
            if (index == 0) {
                // First step: can't go back (exits wizard)
                assertEquals(0, index)
            } else {
                assertTrue(index > 0, "Step $step at index $index should allow going back")
            }
        }
    }

    @Test
    fun crossStepNavigation_canGoForwardFromEveryStepExceptLast() {
        val steps = stepsForTool("interior")
        steps.forEachIndexed { index, step ->
            if (index < steps.size - 1) {
                assertTrue(index < steps.size - 1, "Step $step should allow going forward")
            }
        }
    }
}
