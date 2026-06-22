package com.ismail.homedecorai.validation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.model.WizardStage
import com.ismail.homedecorai.model.hasVisibleMaskPaint
import com.ismail.homedecorai.model.isValidReplacementPrompt

/**
 * Unified validation state for a single wizard step.
 * Every step composable uses this to determine button state and error messages.
 */
data class WizardStepValidation(
    val canProceed: Boolean,
    val validationMessage: String?,
    val isStepValid: Boolean,
    val completedRequiredFields: Int,
    val totalRequiredFields: Int,
)

/**
 * Compute the validation state for the current step based on the tool and wizard stage.
 * This replaces all inline ad-hoc validation logic across every step composable.
 */
@Composable
fun rememberStepValidation(
    state: HomeDecorUiState,
    validationMessage: String? = null,
): WizardStepValidation {
    return remember(state) {
        computeStepValidation(state, validationMessage)
    }
}

private fun computeStepValidation(
    state: HomeDecorUiState,
    overrideMessage: String?,
): WizardStepValidation {
    val toolId = state.selectedTool.id
    val stage = state.wizardStage

    return when (stage) {
        WizardStage.Photo -> validatePhotoStep(state, toolId)
        WizardStage.Space -> validateSpaceStep(state, toolId)
        WizardStage.Style -> validateStyleStep(state, toolId)
        WizardStage.Refine -> validateRefineStep(state, toolId)
        WizardStage.Processing -> WizardStepValidation(
            canProceed = true,
            validationMessage = null,
            isStepValid = true,
            completedRequiredFields = 1,
            totalRequiredFields = 1,
        )
        WizardStage.Result -> WizardStepValidation(
            canProceed = true,
            validationMessage = null,
            isStepValid = true,
            completedRequiredFields = 1,
            totalRequiredFields = 1,
        )
    }.let { base ->
        if (overrideMessage != null) {
            base.copy(validationMessage = overrideMessage)
        } else {
            base
        }
    }
}

private fun validatePhotoStep(state: HomeDecorUiState, toolId: String): WizardStepValidation {
    val hasPhoto = state.selectedPhotos.isNotEmpty() || state.selectedPhotoUri != null
    return WizardStepValidation(
        canProceed = hasPhoto,
        validationMessage = if (!hasPhoto) "Upload a photo from Gallery or Camera to continue." else null,
        isStepValid = hasPhoto,
        completedRequiredFields = if (hasPhoto) 1 else 0,
        totalRequiredFields = 1,
    )
}

private fun validateSpaceStep(state: HomeDecorUiState, toolId: String): WizardStepValidation {
    return when (toolId) {
        "paint", "floor" -> {
            val hasStyle = state.selectedStyles.isNotEmpty()
            WizardStepValidation(
                canProceed = hasStyle,
                validationMessage = if (!hasStyle) "Choose a wall/floor material before generating." else null,
                isStepValid = hasStyle,
                completedRequiredFields = if (hasStyle) 1 else 0,
                totalRequiredFields = 1,
            )
        }
        "replace" -> {
            val hasMask = state.maskStrokes.hasVisibleMaskPaint()
            WizardStepValidation(
                canProceed = hasMask,
                validationMessage = if (!hasMask) "Mark the object you want to replace before generating." else null,
                isStepValid = hasMask,
                completedRequiredFields = if (hasMask) 1 else 0,
                totalRequiredFields = 1,
            )
        }
        "reference" -> {
            val hasRoom = state.selectedPhotos.firstOrNull() != null ||
                state.selectedPhotoUri != null ||
                state.selectedExampleLabel != null
            val hasReference = state.selectedReferenceUri != null ||
                state.selectedReferenceExampleLabel != null
            val hasBoth = hasRoom && hasReference
            WizardStepValidation(
                canProceed = hasBoth,
                validationMessage = if (!hasBoth) "Add both the room image and reference image before generating." else null,
                isStepValid = hasBoth,
                completedRequiredFields = if (hasBoth) 1 else 0,
                totalRequiredFields = 1,
            )
        }
        "layout" -> {
            val hasGoal = state.selectedRooms.isNotEmpty()
            WizardStepValidation(
                canProceed = hasGoal,
                validationMessage = if (!hasGoal) "Select at least one planning goal to unlock Generate." else null,
                isStepValid = hasGoal,
                completedRequiredFields = if (hasGoal) 1 else 0,
                totalRequiredFields = 1,
            )
        }
        else -> {
            val hasSelection = state.selectedRooms.isNotEmpty()
            WizardStepValidation(
                canProceed = hasSelection,
                validationMessage = if (!hasSelection) "Choose at least one option to continue." else null,
                isStepValid = hasSelection,
                completedRequiredFields = if (hasSelection) 1 else 0,
                totalRequiredFields = 1,
            )
        }
    }
}

private fun validateStyleStep(state: HomeDecorUiState, toolId: String): WizardStepValidation {
    return when (toolId) {
        "paint" -> {
            val hasStyle = state.selectedStyles.isNotEmpty()
            val canGenerate = hasStyle
            val message = if (!hasStyle) "Choose a wall/floor material before generating." else null
            WizardStepValidation(
                canProceed = canGenerate,
                validationMessage = message,
                isStepValid = canGenerate,
                completedRequiredFields = if (hasStyle) 1 else 0,
                totalRequiredFields = 1,
            )
        }
        "floor" -> {
            val hasStyle = state.selectedStyles.isNotEmpty()
            val canGenerate = hasStyle
            val message = if (!hasStyle) "Choose a wall/floor material before generating." else null
            WizardStepValidation(
                canProceed = canGenerate,
                validationMessage = message,
                isStepValid = canGenerate,
                completedRequiredFields = if (hasStyle) 1 else 0,
                totalRequiredFields = 1,
            )
        }
        "replace" -> {
            val hasMask = state.maskStrokes.hasVisibleMaskPaint()
            val hasPrompt = state.customPrompt.trim().isValidReplacementPrompt()
            val canGenerate = hasMask && hasPrompt
            val message = when {
                !hasMask -> "Mark the object you want to replace before generating."
                !hasPrompt -> "Describe the replacement object before generating."
                else -> null
            }
            WizardStepValidation(
                canProceed = canGenerate,
                validationMessage = message,
                isStepValid = canGenerate,
                completedRequiredFields = listOf(hasMask, hasPrompt).count { it },
                totalRequiredFields = 2,
            )
        }
        "reference" -> {
            val hasStyle = state.selectedStyles.isNotEmpty()
            val hasRoom = state.selectedPhotos.firstOrNull() != null ||
                state.selectedPhotoUri != null ||
                state.selectedExampleLabel != null
            val hasReference = state.selectedReferenceUri != null ||
                state.selectedReferenceExampleLabel != null
            val hasBoth = hasRoom && hasReference
            val canGenerate = hasStyle && hasBoth
            val message = when {
                !hasBoth -> "Add both the room image and reference image before generating."
                !hasStyle -> "Choose a transfer intensity before generating."
                else -> null
            }
            WizardStepValidation(
                canProceed = canGenerate,
                validationMessage = message,
                isStepValid = canGenerate,
                completedRequiredFields = listOf(hasBoth, hasStyle).count { it },
                totalRequiredFields = 2,
            )
        }
        else -> {
            val hasSelection = state.selectedStyles.isNotEmpty()
            WizardStepValidation(
                canProceed = hasSelection,
                validationMessage = if (!hasSelection) "Choose at least one option to continue." else null,
                isStepValid = hasSelection,
                completedRequiredFields = if (hasSelection) 1 else 0,
                totalRequiredFields = 1,
            )
        }
    }
}

private fun validateRefineStep(state: HomeDecorUiState, toolId: String): WizardStepValidation {
    return when (toolId) {
        "layout" -> {
            val hasGoal = state.selectedRooms.isNotEmpty()
            WizardStepValidation(
                canProceed = hasGoal,
                validationMessage = if (!hasGoal) "Select at least one planning goal to unlock Generate." else null,
                isStepValid = hasGoal,
                completedRequiredFields = if (hasGoal) 1 else 0,
                totalRequiredFields = 1,
            )
        }
        else -> {
            val hasPalette = state.selectedPalettes.isNotEmpty()
            WizardStepValidation(
                canProceed = hasPalette,
                validationMessage = if (!hasPalette) "Choose a color harmony to unlock Generate." else null,
                isStepValid = hasPalette,
                completedRequiredFields = if (hasPalette) 1 else 0,
                totalRequiredFields = 1,
            )
        }
    }
}
