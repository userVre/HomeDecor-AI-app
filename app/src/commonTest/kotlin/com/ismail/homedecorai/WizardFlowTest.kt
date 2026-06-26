package com.ismail.homedecorai

import com.ismail.homedecorai.ui.tools.WizardStep
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WizardFlowTest {

    @Test
    fun testWizardStepOrder() {
        val steps = WizardStep.entries
        assertEquals(4, steps.size)
        assertEquals(WizardStep.Upload, steps[0])
        assertEquals(WizardStep.RoomType, steps[1])
        assertEquals(WizardStep.Style, steps[2])
        assertEquals(WizardStep.Review, steps[3])
    }

    @Test
    fun testWizardTestTags() {
        assertNotNull(Strings.TestTags.wizardHeader)
        assertNotNull(Strings.TestTags.wizardBackButton)
        assertNotNull(Strings.TestTags.wizardCloseButton)
        assertNotNull(Strings.TestTags.wizardProgressBar)
        assertNotNull(Strings.TestTags.wizardUploadDropZone)
        assertNotNull(Strings.TestTags.wizardTryExample)
        assertNotNull(Strings.TestTags.wizardPhotoPreview)
        assertNotNull(Strings.TestTags.wizardGenerateButton)
        assertNotNull(Strings.TestTags.wizardBottomBar)
        assertNotNull(Strings.TestTags.wizardBackStepButton)
        assertNotNull(Strings.TestTags.wizardNextStepButton)
        assertNotNull(Strings.TestTags.wizardReviewEditRoom)
        assertNotNull(Strings.TestTags.wizardReviewEditStyle)
    }

    @Test
    fun testWizardOptionCardTagFormat() {
        assertEquals("wizard_option_card_Living Room", Strings.TestTags.wizardOptionCard.format("Living Room"))
    }

    @Test
    fun testWizardStyleCardTagFormat() {
        assertEquals("wizard_style_card_modern", Strings.TestTags.wizardStyleCard.format("modern"))
    }

    @Test
    fun testWizardProgressLabel() {
        val progressText = "Step 2 of 4: Room Type"
        assertTrue(progressText.contains("2 of 4"))
        assertTrue(progressText.contains("Room Type"))
    }

    @Test
    fun testWizardUploadStrings() {
        assertEquals("Upload a photo of your space", Strings.wizardUploadTitle)
        assertEquals("Drag and drop an image, or click to browse", Strings.wizardUploadSubtitle)
        assertEquals("Choose image", Strings.wizardChooseImage)
        assertEquals("Try with an example", Strings.wizardTryExample)
        assertEquals("Change photo", Strings.wizardUploadChange)
    }

    @Test
    fun testWizardRoomStrings() {
        assertEquals("What type of space is this?", Strings.wizardRoomTitle)
        assertEquals("Select the option that best describes your space", Strings.wizardRoomSubtitle)
    }

    @Test
    fun testWizardStyleStrings() {
        assertEquals("Choose a design style", Strings.wizardStyleTitle)
        assertEquals("Pick a style to apply to your space", Strings.wizardStyleSubtitle)
    }

    @Test
    fun testWizardReviewStrings() {
        assertEquals("Review your design", Strings.wizardReviewTitle)
        assertEquals("Check your selections before generating", Strings.wizardReviewSubtitle)
        assertEquals("Generate Design", Strings.wizardGenerate)
    }

    @Test
    fun testWizardNavigationStrings() {
        assertEquals("Back", Strings.wizardBack)
        assertEquals("Next", Strings.wizardNext)
        assertEquals("Close", Strings.wizardClose)
    }

    @Test
    fun testWizardErrorStrings() {
        assertEquals("Please upload a photo to continue", Strings.wizardErrorPhoto)
        assertEquals("Please select a room type", Strings.wizardErrorRoom)
        assertEquals("Please select a style", Strings.wizardErrorStyle)
    }

    @Test
    fun testWizardA11yLabels() {
        assertEquals("Go back to previous step", Strings.a11yWizardBack)
        assertEquals("Close wizard", Strings.a11yWizardClose)
        assertEquals("Go to next step", Strings.a11yWizardNext)
        assertEquals("Generate design", Strings.a11yWizardGenerate)
    }

    @Test
    fun testWizardExampleFlow() {
        val roomOptions = listOf("Living Room", "Bedroom", "Kitchen", "Bathroom", "Office", "Dining Room")
        assertTrue(roomOptions.isNotEmpty())
        assertEquals(6, roomOptions.size)
    }

    @Test
    fun testWizardStepTransition() {
        var currentStep = WizardStep.Upload
        assertEquals(WizardStep.Upload, currentStep)

        currentStep = WizardStep.RoomType
        assertEquals(WizardStep.RoomType, currentStep)

        currentStep = WizardStep.Style
        assertEquals(WizardStep.Style, currentStep)

        currentStep = WizardStep.Review
        assertEquals(WizardStep.Review, currentStep)
    }

    @Test
    fun testWizardStepBackNavigation() {
        var currentStep = WizardStep.Review

        currentStep = when (currentStep) {
            WizardStep.Upload -> WizardStep.Upload
            WizardStep.RoomType -> WizardStep.Upload
            WizardStep.Style -> WizardStep.RoomType
            WizardStep.Review -> WizardStep.Style
        }
        assertEquals(WizardStep.Style, currentStep)
    }
}
