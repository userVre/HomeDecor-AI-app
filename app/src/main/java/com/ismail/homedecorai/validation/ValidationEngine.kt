package com.ismail.homedecorai.validation

import android.content.Context
import com.ismail.homedecorai.R
import com.ismail.homedecorai.WizardStage
import com.ismail.homedecorai.hasVisibleMaskPaint
import com.ismail.homedecorai.isValidReplacementPrompt

/**
 * Interface for validating wizard step fields
 */
interface StepValidator {
    /**
     * Validate a specific field value
     * @param fieldId The field identifier
     * @param value The current value of the field
     * @param context Android context for string resources
     * @return ValidationResult indicating success or failure with errors
     */
    fun validateField(fieldId: String, value: Any?, context: Context): ValidationResult

    /**
     * Validate all fields for a step
     * @param step The wizard step to validate
     * @param state Current wizard state as a map of field IDs to values
     * @param context Android context for string resources
     * @return StepValidationState with validation results
     */
    fun validateStep(
        step: WizardStage,
        state: Map<String, Any?>,
        context: Context,
    ): StepValidationState
}

/**
 * Default implementation of StepValidator
 */
class DefaultStepValidator : StepValidator {

    override fun validateField(fieldId: String, value: Any?, context: Context): ValidationResult {
        return when (fieldId) {
            ValidationFields.PHOTO -> validatePhoto(value)
            ValidationFields.ROOM_SELECTION -> validateSelection(value, R.string.validation_choose_option_to_continue)
            ValidationFields.STYLE_SELECTION -> validateSelection(value, R.string.validation_choose_option_to_continue)
            ValidationFields.PALETTE_SELECTION -> validateSelection(value, R.string.validation_choose_option_to_continue)
            ValidationFields.MASK -> validateMask(value)
            ValidationFields.REPLACEMENT_PROMPT -> validateReplacementPrompt(value)
            ValidationFields.REFERENCE_IMAGE -> validateReferenceImage(value)
            ValidationFields.LAYOUT_GOAL -> validateSelection(value, R.string.layout_goal_required_error)
            ValidationFields.DESIGN_MODE -> validateSelection(value, R.string.validation_choose_option_to_continue)
            ValidationFields.CUSTOM_PROMPT -> ValidationResult.Valid()
            else -> ValidationResult.Valid()
        }
    }

    override fun validateStep(
        step: WizardStage,
        state: Map<String, Any?>,
        context: Context,
    ): StepValidationState {
        val rules = getRulesForStep(step)
        val messages = mutableListOf<ValidationMessage>()
        val completedFields = mutableSetOf<String>()
        val requiredFields = rules.map { it.fieldId }.toSet()

        for (rule in rules) {
            val value = state[rule.fieldId]
            val result = validateField(rule.fieldId, value, context)

            when (result) {
                is ValidationResult.Valid -> {
                    completedFields.add(rule.fieldId)
                    messages.addAll(result.warnings.map { warning ->
                        ValidationMessage(
                            fieldId = rule.fieldId,
                            messageRes = warning.messageRes,
                            args = warning.args,
                            severity = warning.severity,
                        )
                    })
                }
                is ValidationResult.Invalid -> {
                    messages.addAll(result.errors.map { error ->
                        ValidationMessage(
                            fieldId = rule.fieldId,
                            messageRes = error.messageRes,
                            args = error.args,
                            severity = error.severity,
                        )
                    })
                    messages.addAll(result.warnings.map { warning ->
                        ValidationMessage(
                            fieldId = rule.fieldId,
                            messageRes = warning.messageRes,
                            args = warning.args,
                            severity = warning.severity,
                        )
                    })
                }
            }
        }

        val isStepValid = rules.all { rule ->
            val value = state[rule.fieldId]
            val result = validateField(rule.fieldId, value, context)
            result is ValidationResult.Valid
        }

        return StepValidationState(
            step = step,
            isStepValid = isStepValid,
            messages = messages,
            requiredFields = requiredFields,
            completedFields = completedFields,
        )
    }

    private fun getRulesForStep(step: WizardStage): List<ValidationRuleConfig> {
        return when (step) {
            WizardStage.Photo -> listOf(
                ValidationRuleConfig(ValidationFields.PHOTO, R.string.add_photo_to_continue)
            )
            WizardStage.Space -> listOf(
                ValidationRuleConfig(ValidationFields.ROOM_SELECTION, R.string.validation_choose_option_to_continue)
            )
            WizardStage.Style -> listOf(
                ValidationRuleConfig(ValidationFields.STYLE_SELECTION, R.string.validation_choose_option_to_continue)
            )
            WizardStage.Refine -> listOf(
                ValidationRuleConfig(ValidationFields.PALETTE_SELECTION, R.string.validation_choose_option_to_continue)
            )
            WizardStage.Processing -> emptyList()
            WizardStage.Result -> emptyList()
        }
    }

    private fun validatePhoto(value: Any?): ValidationResult {
        val hasPhoto = when (value) {
            is Boolean -> value
            is String -> value.isNotBlank()
            is Uri? -> value != null
            else -> false
        }
        return if (hasPhoto) {
            ValidationResult.Valid()
        } else {
            ValidationResult.Invalid(
                errors = listOf(
                    ValidationMessage(
                        fieldId = ValidationFields.PHOTO,
                        messageRes = R.string.add_photo_to_continue,
                    )
                )
            )
        }
    }

    private fun validateSelection(value: Any?, errorRes: Int): ValidationResult {
        val isSelected = when (value) {
            is List<*> -> value.isNotEmpty()
            is String -> value.isNotBlank()
            is Set<*> -> value.isNotEmpty()
            else -> false
        }
        return if (isSelected) {
            ValidationResult.Valid()
        } else {
            ValidationResult.Invalid(
                errors = listOf(
                    ValidationMessage(
                        fieldId = ValidationFields.ROOM_SELECTION,
                        messageRes = errorRes,
                    )
                )
            )
        }
    }

    private fun validateMask(value: Any?): ValidationResult {
        val hasMask = when (value) {
            is List<*> -> {
                @Suppress("UNCHECKED_CAST")
                (value as? List<com.ismail.homedecorai.MaskStroke>)?.hasVisibleMaskPaint() ?: false
            }
            else -> false
        }
        return if (hasMask) {
            ValidationResult.Valid()
        } else {
            ValidationResult.Invalid(
                errors = listOf(
                    ValidationMessage(
                        fieldId = ValidationFields.MASK,
                        messageRes = R.string.mark_area_before_generate,
                    )
                )
            )
        }
    }

    private fun validateReplacementPrompt(value: Any?): ValidationResult {
        val prompt = value as? String ?: ""
        val isValid = prompt.isValidReplacementPrompt()
        return if (isValid) {
            ValidationResult.Valid()
        } else {
            ValidationResult.Invalid(
                errors = listOf(
                    ValidationMessage(
                        fieldId = ValidationFields.REPLACEMENT_PROMPT,
                        messageRes = R.string.replacement_prompt_required_error,
                    )
                )
            )
        }
    }

    private fun validateReferenceImage(value: Any?): ValidationResult {
        val hasReference = when (value) {
            is Pair<*, *> -> {
                val room = value.first
                val reference = value.second
                val hasRoom = room != null
                val hasRef = reference != null
                hasRoom && hasRef
            }
            is Boolean -> value
            else -> false
        }
        return if (hasReference) {
            ValidationResult.Valid()
        } else {
            ValidationResult.Invalid(
                errors = listOf(
                    ValidationMessage(
                        fieldId = ValidationFields.REFERENCE_IMAGE,
                        messageRes = R.string.reference_missing_error,
                    )
                )
            )
        }
    }
}

/**
 * Wizard validation engine that coordinates validation across all steps
 */
class WizardValidationEngine(
    private val validator: StepValidator = DefaultStepValidator(),
) {
    /**
     * Validate all steps for a given tool
     * @param toolId The tool identifier
     * @param currentStep The current wizard step
     * @param state Current wizard state
     * @param context Android context for string resources
     * @return WizardValidationState with complete validation results
     */
    fun validateWizard(
        toolId: String,
        currentStep: WizardStage,
        state: Map<String, Any?>,
        context: Context,
    ): WizardValidationState {
        val rules = FlowValidationRules.rulesForTool(toolId)
        val stepStates = mutableMapOf<WizardStage, StepValidationState>()

        for ((step, stepRules) in rules) {
            val stepState = validator.validateStep(step, state, context)
            stepStates[step] = stepState
        }

        val currentStepState = stepStates[currentStep]
        val canProceed = currentStepState?.isStepValid ?: false
        val canGenerate = canProceed && stepStates.values.all { it.isStepValid }
        val globalErrors = currentStepState?.messages
            ?.filter { it.severity == ValidationSeverity.Error }
            ?: emptyList()

        return WizardValidationState(
            currentStep = currentStep,
            stepStates = stepStates,
            canProceed = canProceed,
            canGenerate = canGenerate,
            globalErrors = globalErrors,
        )
    }

    /**
     * Validate a single step and return validation state
     * @param toolId The tool identifier
     * @param step The wizard step to validate
     * @param state Current wizard state
     * @param context Android context for string resources
     * @return StepValidationState for the specified step
     */
    fun validateSingleStep(
        toolId: String,
        step: WizardStage,
        state: Map<String, Any?>,
        context: Context,
    ): StepValidationState {
        return validator.validateStep(step, state, context)
    }
}

/**
 * Extension function to create a state map from HomeDecorUiState fields
 */
fun createStateMap(
    hasPhoto: Boolean = false,
    selectedRooms: List<String> = emptyList(),
    selectedStyles: List<String> = emptyList(),
    selectedPalettes: List<String> = emptyList(),
    maskStrokes: List<com.ismail.homedecorai.MaskStroke> = emptyList(),
    customPrompt: String = "",
    hasReferenceImages: Boolean = false,
    layoutGoalSelected: Boolean = false,
): Map<String, Any?> = mapOf(
    ValidationFields.PHOTO to hasPhoto,
    ValidationFields.ROOM_SELECTION to selectedRooms,
    ValidationFields.STYLE_SELECTION to selectedStyles,
    ValidationFields.PALETTE_SELECTION to selectedPalettes,
    ValidationFields.MASK to maskStrokes,
    ValidationFields.CUSTOM_PROMPT to customPrompt,
    ValidationFields.REFERENCE_IMAGE to hasReferenceImages,
    ValidationFields.LAYOUT_GOAL to layoutGoalSelected,
    ValidationFields.DESIGN_MODE to true,
)
