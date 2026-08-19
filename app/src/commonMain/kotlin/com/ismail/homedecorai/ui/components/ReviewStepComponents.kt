package com.ismail.homedecorai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.imagepicker.PickedImageData
import com.ismail.homedecorai.ui.discover.NetworkImage
import com.ismail.homedecorai.ui.theme.HomeDecorExtra
import com.ismail.homedecorai.ui.theme.HomeDecorIconSize
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing
import com.ismail.homedecorai.ui.tools.LocalImagePreview
import com.ismail.homedecorai.ui.tools.exampleImageUrlForTool

// ---------------------------------------------------------------------------
// ReviewSummaryItem — one editable row in the summary card
// ---------------------------------------------------------------------------

data class ReviewSummaryItem(
    val label: String,
    val value: String,
    val onEdit: () -> Unit,
    val testTag: String = "",
)

// ---------------------------------------------------------------------------
// InsufficientCreditsBlock — shown when user has 0 credits
// ---------------------------------------------------------------------------

@Composable
private fun InsufficientCreditsBlock(
    onBuyCredits: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Rounded.Diamond,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                Strings.insufficientCreditsTitle,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                Strings.insufficientCreditsWithBuy(1),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
            )
            if (onBuyCredits != null) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    onClick = onBuyCredits,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        Strings.buyCreditsCta(10, "$1.99"),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// generateActionForTool — maps toolId to the generate button label
// ---------------------------------------------------------------------------

fun generateActionForTool(toolId: String?): String = when (toolId) {
    "interior" -> Strings.wizardActionInterior
    "facade", "exterior" -> Strings.wizardActionExterior
    "garden" -> Strings.wizardActionGarden
    "floor" -> Strings.wizardActionFloor
    "paint" -> Strings.wizardActionPaint
    "replace" -> Strings.wizardActionReplace
    "layout" -> Strings.wizardActionLayout
    "reference" -> Strings.wizardActionReference
    else -> Strings.wizardGenerate
}

// ---------------------------------------------------------------------------
// ReviewStep — shared review composable for all wizard tools
// ---------------------------------------------------------------------------

@Composable
fun ReviewStep(
    summaryItems: List<ReviewSummaryItem>,
    generateAction: String,
    onGenerate: () -> Unit,
    onEditPhoto: () -> Unit,
    toolId: String?,
    photo: PickedImageData?,
    isUsingExample: Boolean,
    customNotes: String,
    onCustomNotesChange: (String) -> Unit,
    isGenerating: Boolean,
    generationComplete: Boolean,
    generationError: String?,
    onNewDesign: () -> Unit,
    onDownload: () -> Unit = {},
    onFavorite: () -> Unit = {},
    isFavorited: Boolean = false,
    onFollowUp: (() -> Unit)? = null,
    generatedImageUrl: String?,
    isGuest: Boolean,
    onShowSignInDialog: () -> Unit,
    isWide: Boolean,
    showAdvancedControls: Boolean = false,
    selectedDesignMode: String? = null,
    onDesignModeSelected: (String) -> Unit = {},
    creditsRemaining: Int? = null,
    onBuyCredits: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var notesInput by remember(customNotes) { mutableStateOf(customNotes) }

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isWide) {
            // ── Wide layout: side-by-side ────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                // Left: Image Preview
                Column(modifier = Modifier.weight(1f)) {
                    ReviewImagePreview(
                        photo = photo,
                        isUsingExample = isUsingExample,
                        toolId = toolId,
                        onEditPhoto = onEditPhoto,
                    )
                }

                // Right: Design Brief Card
                Column(modifier = Modifier.weight(1f)) {
                    ReviewDesignBriefCard(
                        summaryItems = summaryItems,
                        notesInput = notesInput,
                        onNotesChange = { notesInput = it; onCustomNotesChange(it) },
                        creditsRemaining = creditsRemaining,
                    )
                }
            }
        } else {
            // ── Narrow layout: stacked ──────────────────────────────
            Text(
                Strings.wizardReviewTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                Strings.wizardReviewSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))

            ReviewImagePreview(
                photo = photo,
                isUsingExample = isUsingExample,
                toolId = toolId,
                onEditPhoto = onEditPhoto,
            )

            Spacer(Modifier.height(16.dp))

            ReviewDesignBriefCard(
                summaryItems = summaryItems,
                notesInput = notesInput,
                onNotesChange = { notesInput = it; onCustomNotesChange(it) },
                creditsRemaining = creditsRemaining,
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Generation States ────────────────────────────────────────
        when {
            generationComplete -> {
                GenerationSuccess(
                    generatedImageUrl = generatedImageUrl,
                    onNewDesign = onNewDesign,
                    onSaveToBoard = {
                        if (isGuest) onShowSignInDialog()
                        else onNewDesign()
                    },
                    onShare = {
                        generatedImageUrl?.let { url ->
                            com.ismail.homedecorai.browserShareContent(
                                title = "Check out my HomeDecor AI design!",
                                url = url,
                            )
                        }
                    },
                    onDownload = onDownload,
                    onFavorite = onFavorite,
                    isFavorited = isFavorited,
                    creditsRemaining = creditsRemaining,
                    onFollowUp = onFollowUp,
                )
            }
            generationError != null -> {
                GenerationErrorBlock(
                    error = generationError,
                    onRetry = onGenerate,
                )
            }
            isGenerating -> {
                GeneratingStateBlock(toolId = toolId ?: "")
            }
            else -> {
                // ── Single CTA with credit check ──────────────────────
                val hasEnoughCredits = creditsRemaining == null || creditsRemaining > 0
                if (hasEnoughCredits) {
                    GenerateActionButton(
                        onClick = onGenerate,
                        label = Strings.wizardGenerate,
                        modifier = Modifier.testTag(Strings.TestTags.wizardReviewGenerateAction),
                    )
                } else {
                    // Insufficient credits block
                    InsufficientCreditsBlock(onBuyCredits = onBuyCredits)
                }

                Spacer(Modifier.height(12.dp))

                // Trust copy near CTA
                Text(
                    Strings.trustCtaNearGenerate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// ReviewImagePreview — uploaded photo preview card with change photo button
// ---------------------------------------------------------------------------

@Composable
private fun ReviewImagePreview(
    photo: PickedImageData?,
    isUsingExample: Boolean,
    toolId: String?,
    onEditPhoto: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .testTag(Strings.TestTags.wizardReviewImageCard),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isUsingExample) {
                NetworkImage(
                    url = exampleImageUrlForTool(toolId),
                    contentDescription = Strings.wizardExampleRoom,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LocalImagePreview(
                    imageState = photo,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = Strings.wizardPhotoSelected,
                )
            }

            // Change photo button
            val changePhotoInteraction = remember { MutableInteractionSource() }
            val changePhotoPressed by changePhotoInteraction.collectIsPressedAsState()
            val changePhotoScale by animateFloatAsState(
                targetValue = if (changePhotoPressed) 0.95f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessHigh),
                label = "changePhotoScale",
            )
            Surface(
                onClick = onEditPhoto,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                interactionSource = changePhotoInteraction,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .testTag(Strings.TestTags.wizardReviewChangePhoto)
                    .semantics {
                        role = Role.Button
                        contentDescription = Strings.wizardUploadChange
                    }
                    .scale(changePhotoScale),
            ) {
                Text(
                    Strings.wizardUploadChange,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// ReviewDesignBriefCard — dark card with summary, notes, credit info
// ---------------------------------------------------------------------------

@Composable
private fun ReviewDesignBriefCard(
    summaryItems: List<ReviewSummaryItem>,
    notesInput: String,
    onNotesChange: (String) -> Unit,
    creditsRemaining: Int?,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.inverseSurface,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(Strings.TestTags.wizardReviewSummaryCard),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                Strings.wizardReviewDesignBrief,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                Strings.wizardReviewSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(16.dp))

            summaryItems.forEachIndexed { index, item ->
                val rowTag = if (item.testTag.isNotEmpty()) item.testTag
                    else Strings.formatTestTag(Strings.TestTags.wizardReviewSummaryRow, item.label)
                val editTag = Strings.formatTestTag(Strings.TestTags.wizardReviewSummaryEdit, item.label)

                val rowInteraction = remember { MutableInteractionSource() }
                val rowHovered by rowInteraction.collectIsHoveredAsState()
                val rowPressed by rowInteraction.collectIsPressedAsState()
                val rowScale by animateFloatAsState(
                    targetValue = if (rowPressed) 0.98f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                    label = "summaryRowScale",
                )
                Surface(
                    onClick = item.onEdit,
                    shape = RoundedCornerShape(10.dp),
                    color = if (rowHovered) MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.1f) else Color.Transparent,
                    interactionSource = rowInteraction,
                    modifier = Modifier
                        .testTag(rowTag)
                        .semantics {
                            role = Role.Button
                            contentDescription = "${item.label}: ${item.value}. ${Strings.wizardReviewEdit}"
                        }
                        .scale(rowScale),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                item.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                            )
                            Text(
                                item.value,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Text(
                            Strings.wizardReviewEdit,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag(editTag),
                        )
                    }
                }
                if (index < summaryItems.lastIndex) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.1f)),
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Custom Notes
            OutlinedTextField(
                value = notesInput,
                onValueChange = onNotesChange,
                placeholder = {
                    Text(
                        Strings.wizardReviewCustomNotesHint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.4f),
                    )
                },
                label = {
                    Text(
                        Strings.wizardReviewCustomNotes,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(Strings.TestTags.wizardReviewCustomNotes),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.2f),
                    focusedTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
                singleLine = false,
                maxLines = 3,
                minLines = 2,
            )

            Spacer(Modifier.height(12.dp))

            // Time estimate, Credit cost, Remaining balance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Time estimate
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "\u23F1",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        Strings.wizardReviewTimeEstimate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.7f),
                    )
                }

                // Credit cost
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Diamond,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        Strings.wizardReviewCreditCost,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            if (creditsRemaining != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    Strings.wizardReviewCreditsBalance(creditsRemaining),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                    modifier = Modifier.testTag(Strings.TestTags.wizardReviewDiamondBalance),
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// GenerateActionButton — gradient generate button with diamond cost
// ---------------------------------------------------------------------------

@Composable
private fun GenerateActionButton(
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "genScale",
    )

    val gradientBrush = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
        )
    )

    Surface(
        onClick = { if (enabled) onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .semantics {
                role = Role.Button
                contentDescription = label
            }
            .scale(scale),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (enabled) Modifier.background(gradientBrush, RoundedCornerShape(16.dp))
                    else Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(16.dp))
                ),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    label,
                    color = if (enabled) HomeDecorExtra.onGradientText else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
                if (enabled) {
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White.copy(alpha = 0.2f),
                    ) {
                        Text(
                            Strings.wizardReviewDiamondCost,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = HomeDecorExtra.onGradientText,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// GeneratingStateBlock — animated loading state
// ---------------------------------------------------------------------------

@Composable
private fun GeneratingStateBlock(
    toolId: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "AI is re-imagining your space...\nThis usually takes 10-15 seconds.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// GenerationErrorBlock — error with retry
// ---------------------------------------------------------------------------

@Composable
private fun GenerationErrorBlock(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                Strings.wizardErrorTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
            val retryInteraction = remember { MutableInteractionSource() }
            val retryPressed by retryInteraction.collectIsPressedAsState()
            val retryScale by animateFloatAsState(
                targetValue = if (retryPressed) 0.95f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessHigh),
                label = "retryScale",
            )
            Surface(
                onClick = onRetry,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary,
                interactionSource = retryInteraction,
                modifier = Modifier.semantics {
                    role = Role.Button
                    contentDescription = Strings.wizardTryAgain
                }.scale(retryScale),
            ) {
                Text(
                    Strings.wizardTryAgain,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = HomeDecorExtra.onGradientText,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// GenerationSuccess — result display with actions
// ---------------------------------------------------------------------------

@Composable
private fun GenerationSuccess(
    generatedImageUrl: String?,
    onNewDesign: () -> Unit,
    onSaveToBoard: () -> Unit,
    onShare: () -> Unit,
    onDownload: () -> Unit,
    onFavorite: () -> Unit,
    isFavorited: Boolean = false,
    creditsRemaining: Int? = null,
    onFollowUp: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                Strings.wizardResultReady,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                Strings.wizardResultSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            if (creditsRemaining != null) {
                Spacer(Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                ) {
                    Text(
                        Strings.creditsRemaining(creditsRemaining),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            // Primary action row: Save to Board + New Design
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                val newDesignInteraction = remember { MutableInteractionSource() }
                val newDesignPressed by newDesignInteraction.collectIsPressedAsState()
                val newDesignScale by animateFloatAsState(
                    targetValue = if (newDesignPressed) 0.97f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                    label = "newDesignScale",
                )
                Surface(
                    onClick = onNewDesign,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    interactionSource = newDesignInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            role = Role.Button
                            contentDescription = Strings.wizardNewDesign
                        }
                        .scale(newDesignScale),
                ) {
                    Text(
                        Strings.wizardNewDesign,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }
                val saveBoardInteraction = remember { MutableInteractionSource() }
                val saveBoardPressed by saveBoardInteraction.collectIsPressedAsState()
                val saveBoardScale by animateFloatAsState(
                    targetValue = if (saveBoardPressed) 0.97f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                    label = "saveBoardScale",
                )
                Surface(
                    onClick = onSaveToBoard,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    interactionSource = saveBoardInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            role = Role.Button
                            contentDescription = Strings.wizardSaveToBoard
                        }
                        .scale(saveBoardScale),
                ) {
                    Text(
                        Strings.wizardSaveToBoard,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = HomeDecorExtra.onGradientText,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Secondary action row: Download + Favorite + Share
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                val downloadInteraction = remember { MutableInteractionSource() }
                val downloadPressed by downloadInteraction.collectIsPressedAsState()
                val downloadScale by animateFloatAsState(
                    targetValue = if (downloadPressed) 0.97f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                    label = "downloadScale",
                )
                Surface(
                    onClick = onDownload,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    interactionSource = downloadInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            role = Role.Button
                            contentDescription = Strings.wizardDownload
                        }
                        .scale(downloadScale),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            Strings.wizardDownload,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                val favInteraction = remember { MutableInteractionSource() }
                val favPressed by favInteraction.collectIsPressedAsState()
                val favScale by animateFloatAsState(
                    targetValue = if (favPressed) 0.97f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                    label = "favScale",
                )
                Surface(
                    onClick = onFavorite,
                    shape = RoundedCornerShape(12.dp),
                    color = if (isFavorited) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    interactionSource = favInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            role = Role.Button
                            contentDescription = if (isFavorited) Strings.wizardFavorited else Strings.wizardFavorite
                        }
                        .scale(favScale),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            if (isFavorited) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isFavorited) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            if (isFavorited) Strings.wizardFavorited else Strings.wizardFavorite,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isFavorited) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                val shareInteraction = remember { MutableInteractionSource() }
                val sharePressed by shareInteraction.collectIsPressedAsState()
                val shareScale by animateFloatAsState(
                    targetValue = if (sharePressed) 0.97f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                    label = "shareScale",
                )
                Surface(
                    onClick = onShare,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    interactionSource = shareInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            role = Role.Button
                            contentDescription = Strings.wizardShare
                        }
                        .scale(shareScale),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            Strings.wizardShare,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            // Follow-up prompt
            if (onFollowUp != null) {
                Spacer(Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                Strings.wizardFollowUp,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                        val followUpInteraction = remember { MutableInteractionSource() }
                        val followUpPressed by followUpInteraction.collectIsPressedAsState()
                        val followUpScale by animateFloatAsState(
                            targetValue = if (followUpPressed) 0.97f else 1f,
                            animationSpec = spring(stiffness = Spring.StiffnessHigh),
                            label = "followUpScale",
                        )
                        Surface(
                            onClick = onFollowUp,
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            interactionSource = followUpInteraction,
                            modifier = Modifier.scale(followUpScale),
                        ) {
                            Text(
                                Strings.wizardFollowUpCta,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }
    }
}
