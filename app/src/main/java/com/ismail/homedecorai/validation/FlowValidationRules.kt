package com.ismail.homedecorai.validation

import com.ismail.homedecorai.R
import com.ismail.homedecorai.WizardStage

/**
 * Field IDs used across all validation flows
 */
object ValidationFields {
    const val PHOTO = "photo"
    const val ROOM_SELECTION = "room_selection"
    const val STYLE_SELECTION = "style_selection"
    const val PALETTE_SELECTION = "palette_selection"
    const val MASK = "mask"
    const val REPLACEMENT_PROMPT = "replacement_prompt"
    const val REFERENCE_IMAGE = "reference_image"
    const val LAYOUT_GOAL = "layout_goal"
    const val DESIGN_MODE = "design_mode"
    const val CUSTOM_PROMPT = "custom_prompt"
}

/**
 * Validation rules for each creation flow
 */
object FlowValidationRules {

    /**
     * Interior Design flow validation rules
     * Steps: Photo -> Space (Room) -> Style -> Refine -> Generate
     */
    fun interiorDesignRules(): Map<WizardStage, List<ValidationRuleConfig>> = mapOf(
        WizardStage.Photo to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.PHOTO,
                errorMessageRes = R.string.add_photo_to_continue,
            )
        ),
        WizardStage.Space to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.ROOM_SELECTION,
                errorMessageRes = R.string.validation_choose_option_to_continue,
            )
        ),
        WizardStage.Style to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.STYLE_SELECTION,
                errorMessageRes = R.string.validation_choose_option_to_continue,
            )
        ),
        WizardStage.Refine to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.PALETTE_SELECTION,
                errorMessageRes = R.string.validation_choose_option_to_continue,
            )
        ),
    )

    /**
     * Wall Paint flow validation rules
     * Steps: Photo -> Style (Mask + Color)
     */
    fun wallPaintRules(): Map<WizardStage, List<ValidationRuleConfig>> = mapOf(
        WizardStage.Photo to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.PHOTO,
                errorMessageRes = R.string.add_photo_to_continue,
            )
        ),
        WizardStage.Style to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.MASK,
                errorMessageRes = R.string.mark_area_before_generate,
            ),
            ValidationRuleConfig(
                fieldId = ValidationFields.STYLE_SELECTION,
                errorMessageRes = R.string.validation_choose_option_to_continue,
            ),
        ),
    )

    /**
     * Floor Restyle flow validation rules
     * Steps: Photo -> Style (Mask + Material)
     */
    fun floorRestyleRules(): Map<WizardStage, List<ValidationRuleConfig>> = mapOf(
        WizardStage.Photo to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.PHOTO,
                errorMessageRes = R.string.add_photo_to_continue,
            )
        ),
        WizardStage.Style to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.MASK,
                errorMessageRes = R.string.mark_area_before_generate,
            ),
            ValidationRuleConfig(
                fieldId = ValidationFields.STYLE_SELECTION,
                errorMessageRes = R.string.validation_choose_option_to_continue,
            ),
        ),
    )

    /**
     * Space Planning flow validation rules
     * Steps: Photo -> Space (Layout Goals)
     */
    fun spacePlanningRules(): Map<WizardStage, List<ValidationRuleConfig>> = mapOf(
        WizardStage.Photo to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.PHOTO,
                errorMessageRes = R.string.add_photo_to_continue,
            )
        ),
        WizardStage.Space to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.LAYOUT_GOAL,
                errorMessageRes = R.string.layout_goal_required_error,
            )
        ),
    )

    /**
     * Replace Objects flow validation rules
     * Steps: Photo -> Space (Mask) -> Style (Replacement)
     */
    fun replaceObjectsRules(): Map<WizardStage, List<ValidationRuleConfig>> = mapOf(
        WizardStage.Photo to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.PHOTO,
                errorMessageRes = R.string.add_photo_to_continue,
            )
        ),
        WizardStage.Space to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.MASK,
                errorMessageRes = R.string.real_mask_required_error,
            )
        ),
        WizardStage.Style to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.REPLACEMENT_PROMPT,
                errorMessageRes = R.string.replacement_prompt_required_error,
            )
        ),
    )

    /**
     * Get validation rules for a specific tool
     */
    fun rulesForTool(toolId: String): Map<WizardStage, List<ValidationRuleConfig>> = when (toolId) {
        "interior" -> interiorDesignRules()
        "facade" -> interiorDesignRules()
        "garden" -> interiorDesignRules()
        "paint" -> wallPaintRules()
        "floor" -> floorRestyleRules()
        "layout" -> spacePlanningRules()
        "replace" -> replaceObjectsRules()
        "reference" -> referenceStyleRules()
        else -> emptyMap()
    }

    /**
     * Reference Style flow validation rules
     * Steps: Photo -> Space (Reference) -> Style (Transfer)
     */
    fun referenceStyleRules(): Map<WizardStage, List<ValidationRuleConfig>> = mapOf(
        WizardStage.Photo to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.PHOTO,
                errorMessageRes = R.string.add_photo_to_continue,
            )
        ),
        WizardStage.Space to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.REFERENCE_IMAGE,
                errorMessageRes = R.string.reference_missing_error,
            )
        ),
        WizardStage.Style to listOf(
            ValidationRuleConfig(
                fieldId = ValidationFields.STYLE_SELECTION,
                errorMessageRes = R.string.validation_choose_option_to_continue,
            ),
        ),
    )
}
