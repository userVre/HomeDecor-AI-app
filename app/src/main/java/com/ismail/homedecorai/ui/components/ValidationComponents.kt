package com.ismail.homedecorai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.validation.ValidationMessage
import com.ismail.homedecorai.validation.ValidationSeverity
import com.ismail.homedecorai.validation.WizardValidationState

/**
 * Validation error banner that appears at the top of a step
 * Shows a single error message with an optional icon
 */
@Composable
fun ValidationErrorBanner(
    message: String,
    icon: ImageVector = Icons.Rounded.Warning,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message.isNotBlank(),
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Assertive },
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

/**
 * Step progress indicator showing validation state for each step
 */
@Composable
fun StepProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    completedSteps: Set<Int>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (step in 1..totalSteps) {
            val isCompleted = step in completedSteps
            val isCurrent = step == currentStep
            
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> StudioBlue
                            isCurrent -> StudioBlue.copy(alpha = 0.6f)
                            else -> StudioMist
                        }
                    ),
            )
            
            if (step < totalSteps) {
                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                        .height(2.dp)
                        .background(
                            if (step < currentStep) StudioBlue else StudioMist
                        ),
                )
            }
        }
    }
}

/**
 * Validation summary showing multiple errors/warnings
 */
@Composable
fun ValidationSummary(
    messages: List<ValidationMessage>,
    modifier: Modifier = Modifier,
) {
    if (messages.isEmpty()) return
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        messages.forEach { message ->
            ValidationItem(message = message)
        }
    }
}

/**
 * Single validation item (error or warning)
 */
@Composable
fun ValidationItem(
    message: ValidationMessage,
    modifier: Modifier = Modifier,
) {
    val (icon, containerColor, contentColor) = when (message.severity) {
        ValidationSeverity.Error -> Triple(
            Icons.Rounded.Error,
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.error,
        )
        ValidationSeverity.Warning -> Triple(
            Icons.Rounded.Warning,
            Color(0xFFFFF3E0),
            Color(0xFFE65100),
        )
        ValidationSeverity.Info -> Triple(
            Icons.Rounded.Info,
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = containerColor,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = stringResource(message.messageRes, *message.args.toTypedArray()),
                style = MaterialTheme.typography.bodySmall,
                color = contentColor,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

/**
 * Compact validation notice for inline use
 */
@Composable
fun InlineValidationNotice(
    message: String,
    isError: Boolean = true,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message.isNotBlank(),
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Row(
            modifier = modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                if (isError) Icons.Rounded.Error else Icons.Rounded.Info,
                contentDescription = null,
                tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

/**
 * Enhanced generation error notice with retry button
 */
@Composable
fun EnhancedGenerationErrorNotice(
    message: String,
    onRetry: () -> Unit,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = StudioErrorContainer,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    Icons.Rounded.ErrorOutline,
                    contentDescription = null,
                    tint = StudioRose,
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = StudioRose,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StudioRose,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.retry), fontWeight = FontWeight.Bold)
                }
                
                onDismiss?.let { dismiss ->
                    TextButton(onClick = dismiss) {
                        Text(stringResource(R.string.dismiss))
                    }
                }
            }
        }
    }
}

/**
 * Step validation status indicator
 */
@Composable
fun StepValidationStatus(
    isValid: Boolean,
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            if (isValid) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isValid) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isValid) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isValid) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

/**
 * Validation state summary for the current wizard flow
 */
@Composable
fun WizardValidationSummary(
    validationState: WizardValidationState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Progress indicator
        StepProgressIndicator(
            currentStep = validationState.currentStep.ordinal + 1,
            totalSteps = validationState.totalSteps,
            completedSteps = validationState.stepStates.values
                .filter { it.isStepValid }
                .map { it.step.ordinal + 1 }
                .toSet(),
        )
        
        // Current step validation messages
        validationState.currentStepState?.messages?.let { messages ->
            if (messages.isNotEmpty()) {
                ValidationSummary(messages = messages)
            }
        }
        
        // Global errors
        if (validationState.globalErrors.isNotEmpty()) {
            ValidationSummary(messages = validationState.globalErrors)
        }
    }
}

/**
 * Prominent red validation alert banner shown at the bottom of the screen
 * when the user taps Continue/Generate without meeting step requirements.
 * Displays a lock icon and dynamic message text.
 */
@Composable
fun ValidationAlertBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message.isNotBlank(),
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Assertive },
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    Icons.Rounded.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

/**
 * Unified wizard error banner that replaces all scattered error displays.
 * Handles generation errors, validation errors, and network errors consistently.
 * Shows retry button for recoverable errors and dismiss for all.
 */
@Composable
fun UnifiedWizardError(
    message: String,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message.isNotBlank(),
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Assertive },
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = StudioErrorContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        Icons.Rounded.ErrorOutline,
                        contentDescription = null,
                        tint = StudioRose,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = StudioRose,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (onRetry != null || onDismiss != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        onRetry?.let { retry ->
                            Button(
                                onClick = retry,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StudioRose,
                                    contentColor = Color.White,
                                ),
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(stringResource(R.string.retry), fontWeight = FontWeight.Bold)
                            }
                        }
                        onDismiss?.let { dismiss ->
                            TextButton(onClick = dismiss) {
                                Text(stringResource(R.string.dismiss))
                            }
                        }
                    }
                }
            }
        }
    }
}
