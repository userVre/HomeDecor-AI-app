package com.ismail.homedecorai.ui.wizard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.tools.WizardStep
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.isReducedMotionEnabled

// ---------------------------------------------------------------------------
// Responsive constraint
// ---------------------------------------------------------------------------

private val WizardContentMaxWidth = 1080.dp

// ---------------------------------------------------------------------------
// WizardScaffold
// ---------------------------------------------------------------------------

/**
 * Reusable wizard layout scaffold with three fixed regions:
 * - **Header**: back button, tool title, close button
 * - **Content**: scrollable step body with [AnimatedContent] keyed by [WizardStep]
 * - **Footer**: back / next / generate buttons with inline validation hint
 *
 * The content area uses [AnimatedContent] with [key] per step to prevent stale
 * composables leaking across step transitions. On desktop (≥1024 dp), the
 * content is horizontally constrained to [WizardContentMaxWidth] and centered.
 *
 * @param tool the active tool item (used for title and progress bar)
 * @param currentStep the active wizard step
 * @param steps ordered list of steps for this tool's flow
 * @param onBack called when the header back arrow or browser back is pressed
 * @param onClose called when the header close button is pressed
 * @param onNext called when the footer Next button is pressed
 * @param onGenerate called when the footer Generate button is pressed (Review step only)
 * @param canProceed whether the current step's required input is satisfied
 * @param validationError optional inline validation error shown above content
 * @param isGenerating whether AI generation is in progress
 * @param generationComplete whether generation finished successfully
 * @param content the step-body composable; receives a [Modifier] to apply
 */
@Composable
fun WizardScaffold(
    tool: ToolItem,
    currentStep: WizardStep,
    steps: List<WizardStep>,
    onBack: () -> Unit,
    onClose: () -> Unit,
    onNext: () -> Unit,
    onGenerate: () -> Unit = {},
    canProceed: Boolean,
    validationError: String? = null,
    isGenerating: Boolean = false,
    generationComplete: Boolean = false,
    content: @Composable (Modifier) -> Unit,
) {
    val isDesktop = rememberIsDesktop()
    val isLastStep = currentStep == steps.lastOrNull()
    val reducedMotion = isReducedMotionEnabled()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.wizardScreen)
            .onPreviewKeyEvent { event ->
                if (event.key == Key.Escape) {
                    onClose()
                    true
                } else false
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WizardHeader(
            tool = tool,
            onBack = onBack,
            onClose = onClose,
        )

        WizardProgressBar(
            currentStep = currentStep,
            toolId = tool.id,
        )

        ValidationAlertBanner(
            message = validationError ?: "",
            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base),
        )

        // ── Content area ────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .then(
                    if (isDesktop) Modifier.widthIn(max = WizardContentMaxWidth)
                    else Modifier
                )
                .testTag(
                    Strings.formatTestTag(Strings.TestTags.wizardStepContent, currentStep.name)
                ),
            contentAlignment = Alignment.TopCenter,
        ) {
            AnimatedContent(
                targetState = currentStep,
                contentKey = { it },
                transitionSpec = {
                    if (reducedMotion) {
                        fadeIn(animationSpec = tween(0)) togetherWith fadeOut(animationSpec = tween(0))
                    } else {
                        fadeIn(animationSpec = tween(180)) +
                            slideInVertically(animationSpec = tween(180)) { it / 10 } togetherWith
                            fadeOut(animationSpec = tween(150))
                    }
                },
                label = "WizardStepContent",
            ) { step ->
                key(step) {
                    content(Modifier.fillMaxSize())
                }
            }
        }

        WizardFooter(
            currentStep = currentStep,
            steps = steps,
            canProceed = canProceed,
            isGenerating = isGenerating,
            generationComplete = generationComplete,
            onBack = onBack,
            onNext = onNext,
            onGenerate = onGenerate,
        )
    }
}

// ---------------------------------------------------------------------------
// Header
// ---------------------------------------------------------------------------

@Composable
private fun WizardHeader(
    tool: ToolItem,
    onBack: () -> Unit,
    onClose: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier.testTag(Strings.TestTags.wizardHeader),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Sm, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag(Strings.TestTags.wizardBackButton),
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = Strings.a11yWizardBack,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    Strings.toolTitle(tool.id),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.semantics { heading() },
                )
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag(Strings.TestTags.wizardCloseButton),
            ) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = Strings.a11yWizardClose,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Progress Bar
// ---------------------------------------------------------------------------

@Composable
private fun WizardProgressBar(
    currentStep: WizardStep,
    toolId: String?,
    modifier: Modifier = Modifier,
) {
    val steps = stepsForTool(toolId)
    val currentIndex = steps.indexOf(currentStep)
    val pulseAnim = remember { Animatable(0f) }
    val reducedMotion = isReducedMotionEnabled()

    LaunchedEffect(currentStep) {
        if (reducedMotion) {
            pulseAnim.snapTo(1f)
        } else {
            pulseAnim.snapTo(0f)
            pulseAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600),
            )
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = modifier
            .testTag(Strings.TestTags.wizardProgressBar)
            .semantics {
                contentDescription = "Step ${currentIndex + 1} of ${steps.size}: ${stepTitle(currentStep, toolId)}"
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Step ${currentIndex + 1} of ${steps.size}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                steps.forEachIndexed { index, step ->
                    val isCompleted = index < currentIndex
                    val isActive = index == currentIndex

                    if (index > 0) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (isCompleted || isActive)
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                                        else
                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                    ),
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(if (isActive) 12.dp else 10.dp)
                            .graphicsLayer {
                                if (isActive) {
                                    val scale = 1f + pulseAnim.value * 0.18f
                                    scaleX = scale
                                    scaleY = scale
                                    alpha = 0.85f + pulseAnim.value * 0.15f
                                }
                            }
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> MaterialTheme.colorScheme.primary
                                    isActive -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                }
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isCompleted) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(6.dp),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }

                    if (index < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (isCompleted)
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                                        else
                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                    ),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                stepTitle(currentStep, toolId),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Footer
// ---------------------------------------------------------------------------

@Composable
private fun WizardFooter(
    currentStep: WizardStep,
    steps: List<WizardStep>,
    canProceed: Boolean,
    isGenerating: Boolean = false,
    generationComplete: Boolean = false,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onGenerate: () -> Unit,
) {
    val isLastStep = currentStep == steps.lastOrNull()
    val isFirstStep = currentStep == steps.firstOrNull()
    val showGenerate = isLastStep && currentStep == WizardStep.Review

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && canProceed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "footerNextScale",
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.testTag(Strings.TestTags.wizardBottomBar),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HomeDecorSpacing.Base, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Back button (hidden on first step)
                if (!isFirstStep) {
                    Surface(
                        onClick = onBack,
                        shape = HomeDecorShape.Medium,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier
                            .height(44.dp)
                            .testTag(Strings.TestTags.wizardBackStepButton),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(HomeDecorIconSize.Small),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                Strings.wizardBack,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // Next button (shown when not last step)
                if (!isLastStep) {
                    Surface(
                        onClick = onNext,
                        shape = HomeDecorShape.Pill,
                        color = if (canProceed) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainerHigh,
                        enabled = canProceed,
                        interactionSource = interactionSource,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag(Strings.TestTags.wizardNextStepButton)
                            .scale(scale),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(HomeDecorIconSize.Small),
                                tint = if (canProceed) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                Strings.wizardNext,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (canProceed) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                } else {
                    Spacer(Modifier.weight(1f))
                }

                // Generate button (shown on Review step only)
                if (showGenerate) {
                    val generateEnabled = !isGenerating && !generationComplete
                    val generateInteractionSource = remember { MutableInteractionSource() }
                    val generateIsPressed by generateInteractionSource.collectIsPressedAsState()
                    val generateScale by animateFloatAsState(
                        targetValue = if (generateIsPressed && generateEnabled) 0.97f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                        label = "footerGenScale",
                    )

                    Surface(
                        onClick = { if (generateEnabled) onGenerate() },
                        shape = HomeDecorShape.Pill,
                        color = Color(0xFF1D5C5E),
                        enabled = generateEnabled,
                        interactionSource = generateInteractionSource,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag(Strings.TestTags.wizardGenerateButton)
                            .scale(generateScale),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(HomeDecorIconSize.Small),
                                tint = Color.White,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Generate Redesign \u00B7 10 \uD83D\uDC8E",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            // Inline validation hint when a required field is missing
            AnimatedVisibility(
                visible = !canProceed && !isLastStep,
            ) {
                val hint = validationHintForStep(currentStep)
                Text(
                    hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = HomeDecorSpacing.Base)
                        .padding(bottom = 6.dp)
                        .testTag(Strings.TestTags.wizardFooterHint),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Validation Alert Banner
// ---------------------------------------------------------------------------

@Composable
internal fun ValidationAlertBanner(
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
                    modifier = Modifier.size(HomeDecorIconSize.Medium),
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

// ---------------------------------------------------------------------------
// Helpers (shared across header / progress / footer)
// ---------------------------------------------------------------------------

/**
 * Returns a human-readable title for the given [WizardStep], parameterised
 * by the active tool id (e.g. "paint" → "Wall Material").
 */
fun stepTitle(step: WizardStep, toolId: String?): String = when (step) {
    WizardStep.Upload -> "Upload Photo"
    WizardStep.RoomType -> when (toolId) {
        "facade", "exterior" -> "Exterior Type"
        "garden" -> "Garden Style"
        "layout" -> "Room Type"
        else -> "Room Type"
    }
    WizardStep.Style -> when (toolId) {
        "facade", "exterior" -> "Exterior Style"
        "garden" -> "Garden Style"
        else -> "Design Style"
    }
    WizardStep.Palette -> "Color Palette"
    WizardStep.Refine -> "Review"
    WizardStep.Material -> when (toolId) {
        "paint" -> "Wall Material"
        "floor" -> "Floor Material"
        else -> "Material"
    }
    WizardStep.Goals -> "Layout Goals"
    WizardStep.Mask -> "Select Object"
    WizardStep.ReplacementPrompt -> "Replacement"
    WizardStep.TransferStrength -> "Transfer Strength"
    WizardStep.PaintColor -> "Paint Color"
    WizardStep.FloorStyle -> "Floor Style"
    WizardStep.FurnitureType -> "Furniture Type"
    WizardStep.ReplacementStyle -> "Replacement Style"
    WizardStep.ReferenceImage -> "Reference Image"
    WizardStep.Review -> "Review"
}

/**
 * Returns the inline validation hint for the given step.
 */
private fun validationHintForStep(step: WizardStep): String = when (step) {
    WizardStep.Upload -> Strings.wizardHintUpload
    WizardStep.RoomType -> Strings.wizardHintRoomType
    WizardStep.Style -> Strings.wizardHintStyle
    WizardStep.Palette -> Strings.wizardHintPalette
    WizardStep.Material -> Strings.wizardHintMaterial
    WizardStep.Goals -> Strings.wizardHintGoals
    WizardStep.Mask -> Strings.wizardHintMask
    WizardStep.ReplacementPrompt -> Strings.wizardHintReplacementPrompt
    WizardStep.TransferStrength -> Strings.wizardHintTransferStrength
    WizardStep.PaintColor -> Strings.wizardHintPaintColor
    WizardStep.FloorStyle -> Strings.wizardHintFloorStyle
    WizardStep.FurnitureType -> Strings.wizardHintFurnitureType
    WizardStep.ReplacementStyle -> Strings.wizardHintReplacementStyle
    WizardStep.ReferenceImage -> Strings.wizardHintReferenceImage
    WizardStep.Refine -> ""
    WizardStep.Review -> ""
}

/**
 * Returns the ordered list of [WizardStep] entries for the given tool id.
 */
fun stepsForTool(toolId: String?): List<WizardStep> = when (toolId) {
    "interior" -> listOf(WizardStep.Upload, WizardStep.RoomType, WizardStep.Style, WizardStep.Palette, WizardStep.Refine, WizardStep.Review)
    "facade", "exterior" -> listOf(WizardStep.Upload, WizardStep.RoomType, WizardStep.Style, WizardStep.Palette, WizardStep.Refine, WizardStep.Review)
    "garden" -> listOf(WizardStep.Upload, WizardStep.RoomType, WizardStep.Style, WizardStep.Palette, WizardStep.Refine, WizardStep.Review)
    "paint" -> listOf(WizardStep.Upload, WizardStep.Material, WizardStep.PaintColor, WizardStep.Review)
    "floor" -> listOf(WizardStep.Upload, WizardStep.Material, WizardStep.FloorStyle, WizardStep.Review)
    "layout" -> listOf(WizardStep.Upload, WizardStep.RoomType, WizardStep.Goals, WizardStep.Review)
    "replace" -> listOf(WizardStep.Upload, WizardStep.FurnitureType, WizardStep.Mask, WizardStep.ReplacementStyle, WizardStep.ReplacementPrompt, WizardStep.Review)
    "reference" -> listOf(WizardStep.Upload, WizardStep.ReferenceImage, WizardStep.TransferStrength, WizardStep.Review)
    else -> listOf(WizardStep.Upload, WizardStep.RoomType, WizardStep.Style, WizardStep.Palette, WizardStep.Refine, WizardStep.Review)
}
