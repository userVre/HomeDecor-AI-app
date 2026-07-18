package com.ismail.homedecorai

import com.ismail.homedecorai.ui.wizard.stepsForTool
import com.ismail.homedecorai.ui.tools.WizardStep
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UploadStepTest {

    // ── Route-specific workflow step 2 strings exist ─────────────────────

    @Test
    fun workflowStep2Paint_isDefined() {
        assertTrue(Strings.wizardWorkflowStep2Paint.startsWith("2."))
        assertTrue(Strings.wizardWorkflowStep2Paint.contains("material", ignoreCase = true))
    }

    @Test
    fun workflowStep2Floor_isDefined() {
        assertTrue(Strings.wizardWorkflowStep2Floor.startsWith("2."))
        assertTrue(Strings.wizardWorkflowStep2Floor.contains("material", ignoreCase = true))
    }

    @Test
    fun workflowStep2Replace_isDefined() {
        assertTrue(Strings.wizardWorkflowStep2Replace.startsWith("2."))
        assertTrue(Strings.wizardWorkflowStep2Replace.contains("replace", ignoreCase = true))
    }

    @Test
    fun workflowStep2Layout_isDefined() {
        assertTrue(Strings.wizardWorkflowStep2Layout.startsWith("2."))
        assertTrue(Strings.wizardWorkflowStep2Layout.contains("goal", ignoreCase = true))
    }

    @Test
    fun workflowStep2Reference_isDefined() {
        assertTrue(Strings.wizardWorkflowStep2Reference.startsWith("2."))
        assertTrue(Strings.wizardWorkflowStep2Reference.contains("transfer", ignoreCase = true))
    }

    @Test
    fun workflowStep2Design_isDefined() {
        assertTrue(Strings.wizardWorkflowStep2Design.startsWith("2."))
        assertTrue(Strings.wizardWorkflowStep2Design.contains("style", ignoreCase = true))
    }

    // ── Upload file constraint strings ───────────────────────────────────

    @Test
    fun wizardMaxFileSize_isDefined() {
        assertTrue(Strings.wizardMaxFileSize.isNotBlank())
        assertTrue(Strings.wizardMaxFileSize.contains("MB", ignoreCase = true))
    }

    @Test
    fun wizardAcceptedFormats_isDefined() {
        assertTrue(Strings.wizardAcceptedTypes.isNotBlank())
        assertTrue(Strings.wizardAcceptedTypes.contains("JPG", ignoreCase = true))
        assertTrue(Strings.wizardAcceptedTypes.contains("PNG", ignoreCase = true))
    }

    @Test
    fun wizardDragAndDrop_isDefined() {
        assertTrue(Strings.wizardDragAndDrop.isNotBlank())
    }

    // ── Upload state strings ─────────────────────────────────────────────

    @Test
    fun wizardImageUploaded_isDefined() {
        assertTrue(Strings.wizardImageUploaded.isNotBlank())
    }

    @Test
    fun wizardChangeImage_isDefined() {
        assertTrue(Strings.wizardChangeImage.isNotBlank())
    }

    @Test
    fun wizardRemoveImage_isDefined() {
        assertTrue(Strings.wizardRemoveImage.isNotBlank())
    }

    // ── Upload validation strings ────────────────────────────────────────

    @Test
    fun wizardInvalidFileType_isDefined() {
        assertTrue(Strings.wizardInvalidFileType.isNotBlank())
    }

    @Test
    fun wizardFileTooLarge_isDefined() {
        assertTrue(Strings.wizardFileTooLarge.isNotBlank())
        assertTrue(Strings.wizardFileTooLarge.contains("MB", ignoreCase = true))
    }

    // ── Upload step strings ──────────────────────────────────────────────

    @Test
    fun wizardPhotoSelected_isDefined() {
        assertTrue(Strings.wizardPhotoSelected.isNotBlank())
    }

    @Test
    fun wizardCropImage_isDefined() {
        assertTrue(Strings.wizardCropImage.isNotBlank())
    }

    @Test
    fun wizardRotateImage_isDefined() {
        assertTrue(Strings.wizardRotateImage.isNotBlank())
    }

    @Test
    fun wizardUploadAfterHelper_isDefined() {
        assertTrue(Strings.wizardUploadAfterHelper.isNotBlank())
    }

    // ── Route-specific upload titles ─────────────────────────────────────

    @Test
    fun interiorUploadStep_usesCorrectSteps() {
        val steps = stepsForTool("interior")
        assertTrue(steps.contains(WizardStep.Upload))
        assertTrue(steps.contains(WizardStep.RoomType))
        assertTrue(steps.contains(WizardStep.Style))
        assertTrue(steps.contains(WizardStep.Palette))
        assertTrue(steps.contains(WizardStep.Refine))
    }

    @Test
    fun paintUploadStep_usesCorrectSteps() {
        val steps = stepsForTool("paint")
        assertTrue(steps.contains(WizardStep.Upload))
        assertTrue(steps.contains(WizardStep.Material))
        assertTrue(steps.contains(WizardStep.PaintColor))
        assertTrue(steps.contains(WizardStep.Review))
    }

    @Test
    fun floorUploadStep_usesCorrectSteps() {
        val steps = stepsForTool("floor")
        assertTrue(steps.contains(WizardStep.Upload))
        assertTrue(steps.contains(WizardStep.Material))
        assertTrue(steps.contains(WizardStep.FloorStyle))
        assertTrue(steps.contains(WizardStep.Review))
    }

    @Test
    fun replaceUploadStep_usesCorrectSteps() {
        val steps = stepsForTool("replace")
        assertTrue(steps.contains(WizardStep.Upload))
        assertTrue(steps.contains(WizardStep.FurnitureType))
        assertTrue(steps.contains(WizardStep.Mask))
        assertTrue(steps.contains(WizardStep.ReplacementStyle))
        assertTrue(steps.contains(WizardStep.ReplacementPrompt))
        assertTrue(steps.contains(WizardStep.Review))
    }

    @Test
    fun layoutUploadStep_usesCorrectSteps() {
        val steps = stepsForTool("layout")
        assertTrue(steps.contains(WizardStep.Upload))
        assertTrue(steps.contains(WizardStep.RoomType))
        assertTrue(steps.contains(WizardStep.Goals))
        assertTrue(steps.contains(WizardStep.Review))
    }

    @Test
    fun referenceUploadStep_usesCorrectSteps() {
        val steps = stepsForTool("reference")
        assertTrue(steps.contains(WizardStep.Upload))
        assertTrue(steps.contains(WizardStep.ReferenceImage))
        assertTrue(steps.contains(WizardStep.TransferStrength))
        assertTrue(steps.contains(WizardStep.Review))
    }

    // ── All tools start with Upload step ─────────────────────────────────

    @Test
    fun allTools_startWithUploadStep() {
        val toolIds = listOf("interior", "paint", "floor", "replace", "layout", "reference", "facade", "garden")
        for (toolId in toolIds) {
            val steps = stepsForTool(toolId)
            assertEquals(
                WizardStep.Upload,
                steps.first(),
                "Tool '$toolId' should start with Upload step"
            )
        }
    }

    @Test
    fun allTools_endWithReviewOrRefineStep() {
        val toolIds = listOf("interior", "paint", "floor", "replace", "layout", "reference", "facade", "garden")
        for (toolId in toolIds) {
            val steps = stepsForTool(toolId)
            val lastStep = steps.last()
            assertTrue(
                lastStep == WizardStep.Review || lastStep == WizardStep.Refine,
                "Tool '$toolId' should end with Review or Refine step, got $lastStep"
            )
        }
    }
}
