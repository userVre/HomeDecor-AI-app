package com.ismail.homedecorai.validation

import androidx.annotation.StringRes
import com.ismail.homedecorai.WizardStage

/**
 * Severity levels for validation messages
 */
enum class ValidationSeverity {
    Info,
    Warning,
    Error
}

/**
 * A single validation message for a specific field or step
 */
data class ValidationMessage(
    val fieldId: String,
    @StringRes val messageRes: Int,
    val args: List<Any> = emptyList(),
    val severity: ValidationSeverity = ValidationSeverity.Error,
)

/**
 * Validation state for a single wizard step
 */
data class StepValidationState(
    val step: WizardStage,
    val isStepValid: Boolean,
    val messages: List<ValidationMessage>,
    val requiredFields: Set<String>,
    val completedFields: Set<String>,
) {
    val missingFields: Set<String> = requiredFields - completedFields
    val primaryMessage: ValidationMessage? = messages.firstOrNull { it.severity == ValidationSeverity.Error }
        ?: messages.firstOrNull { it.severity == ValidationSeverity.Warning }
        ?: messages.firstOrNull()
}

/**
 * Complete validation state for the current wizard flow
 */
data class WizardValidationState(
    val currentStep: WizardStage,
    val stepStates: Map<WizardStage, StepValidationState>,
    val canProceed: Boolean,
    val canGenerate: Boolean,
    val globalErrors: List<ValidationMessage>,
) {
    val currentStepState: StepValidationState?
        get() = stepStates[currentStep]

    val currentStepIsValid: Boolean
        get() = currentStepState?.isStepValid ?: false

    val currentStepPrimaryMessage: ValidationMessage?
        get() = currentStepState?.primaryMessage

    val allStepsValid: Boolean
        get() = stepStates.values.all { it.isStepValid }

    val totalSteps: Int
        get() = stepStates.size

    val completedSteps: Int
        get() = stepStates.values.count { it.isStepValid }
}

/**
 * Result of a validation operation
 */
sealed class ValidationResult {
    data class Valid(
        val warnings: List<ValidationMessage> = emptyList(),
    ) : ValidationResult()

    data class Invalid(
        val errors: List<ValidationMessage>,
        val warnings: List<ValidationMessage> = emptyList(),
        val redirectStep: WizardStage? = null,
    ) : ValidationResult() {
        val primaryError: ValidationMessage?
            get() = errors.firstOrNull()
    }
}

/**
 * Configuration for a validation rule
 */
data class ValidationRuleConfig(
    val fieldId: String,
    @StringRes val errorMessageRes: Int,
    val severity: ValidationSeverity = ValidationSeverity.Error,
    val dependsOn: Set<String> = emptySet(),
)

/**
 * Extension function to convert ValidationResult to a user-friendly message
 */
fun ValidationResult.toUserMessage(): String? = when (this) {
    is ValidationResult.Valid -> null
    is ValidationResult.Invalid -> primaryError?.let { "Validation error" }
}

/**
 * Extension function to check if a ValidationResult is valid
 */
fun ValidationResult.isValid(): Boolean = this is ValidationResult.Valid
