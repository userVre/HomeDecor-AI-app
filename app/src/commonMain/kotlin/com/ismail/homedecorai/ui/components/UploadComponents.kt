package com.ismail.homedecorai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Crop
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.RotateRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.theme.HomeDecorColors
import com.ismail.homedecorai.ui.theme.HomeDecorIconSize
import com.ismail.homedecorai.ui.theme.HomeDecorExtra
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing

// ---------------------------------------------------------------------------
// PhotoUploadStep  –  Complete upload step for all AI design tools
// ---------------------------------------------------------------------------
// Composes: heading, drop zone or photo preview, tips, sample button.
// Handles empty state, uploaded state, validation messages inline.
// ---------------------------------------------------------------------------

/**
 * Complete photo upload step for the design wizard.
 *
 * @param title Step heading (route-specific).
 * @param subtitle Step description (route-specific).
 * @param isPhotoUploaded Whether a photo has been uploaded.
 * @param isUsingExample Whether the current photo is a sample.
 * @param isDragging Whether a file is being dragged over the drop zone.
 * @param validationError Inline validation error message (shown next to the requirement).
 * @param sampleButtonLabel Label for the sample image button.
 * @param workflowSteps List of 3 workflow step labels.
 * @param photoPreview Composable slot for the uploaded photo preview.
 * @param dropZone Composable slot for the upload drop zone.
 * @param onTryExample Callback when "Try example" is clicked.
 * @param onImagePicked Callback when upload is clicked.
 * @param onDragEnter Callback when drag enters.
 * @param onDragLeave Callback when drag leaves.
 * @param onDrop Callback when file is dropped.
 */
@Composable
fun PhotoUploadStep(
    title: String,
    subtitle: String,
    isPhotoUploaded: Boolean,
    isUsingExample: Boolean,
    isDragging: Boolean,
    validationError: String? = null,
    sampleButtonLabel: String = Strings.wizardTryExample,
    workflowSteps: List<String> = listOf(
        Strings.wizardWorkflowStep1,
        Strings.wizardWorkflowStep2,
        Strings.wizardWorkflowStep3,
    ),
    photoPreview: @Composable () -> Unit = {},
    dropZone: @Composable () -> Unit = {},
    onTryExample: () -> Unit,
    onImagePicked: () -> Unit,
    onDragEnter: () -> Unit = {},
    onDragLeave: () -> Unit = {},
    onDrop: @Composable ((com.ismail.homedecorai.imagepicker.PickedImageData) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ── Heading ──────────────────────────────────────────────────────
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))

        // ── Content: uploaded state or empty state ────────────────────────
        if (isPhotoUploaded) {
            photoPreview()
        } else {
            // Workflow explainer
            UploadWorkflowExplainer(steps = workflowSteps)
            Spacer(Modifier.height(14.dp))

            // Sample button (secondary action)
            SampleImageButton(
                label = sampleButtonLabel,
                onClick = onTryExample,
            )

            // "or" divider
            UploadOrDivider()

            // Primary upload target
            UploadDropZone(
                isDragging = isDragging,
                onClick = onImagePicked,
                onDragEnter = onDragEnter,
                onDragLeave = onDragLeave,
                onDrop = onDrop,
            )

            // Helper text
            Spacer(Modifier.height(8.dp))
            UploadHelperText()

            // Photo tips
            Spacer(Modifier.height(16.dp))
            UploadTips()

            // Privacy note
            Spacer(Modifier.height(12.dp))
            UploadPrivacyNote()
        }

        // ── Inline validation (next to the requirement, not at bottom) ────
        AnimatedVisibility(visible = validationError != null) {
            Text(
                validationError ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// UploadDropZone  –  Primary drag-and-drop upload target
// ---------------------------------------------------------------------------

@Composable
fun UploadDropZone(
    isDragging: Boolean,
    onClick: () -> Unit,
    onDragEnter: () -> Unit = {},
    onDragLeave: () -> Unit = {},
    onDrop: @Composable ((com.ismail.homedecorai.imagepicker.PickedImageData) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = androidx.compose.animation.core.Spring.StiffnessHigh),
        label = "dropScale",
    )

    val highlightBorder = isDragging || isHovered

    val borderColor = if (highlightBorder)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

    val bgColor = if (highlightBorder)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
    else
        MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .testTag(Strings.TestTags.wizardUploadDropZone)
            .semantics {
                role = Role.Button
                contentDescription = Strings.wizardChooseImage
            }
            .scale(scale)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = if (highlightBorder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(64.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = if (highlightBorder) HomeDecorExtra.onGradientText else MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                if (isDragging) Strings.wizardDropHere else Strings.wizardChooseImage,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${Strings.wizardAcceptedTypes} ${Strings.wizardDragAndDrop}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "or Take a photo on mobile",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// UploadedPhotoPreview  –  Stable constrained preview with actions
// ---------------------------------------------------------------------------

@Composable
fun UploadedPhotoPreview(
    isUsingExample: Boolean,
    toolId: String?,
    previewContent: @Composable () -> Unit,
    onChange: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .heightIn(max = 420.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            previewContent()

            PhotoActions(
                onChange = onChange,
                onRemove = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(HomeDecorSpacing.Sm),
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(HomeDecorSpacing.Sm),
                shape = HomeDecorShape.Badge,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.White,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        Strings.wizardImageUploaded,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            if (isUsingExample) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(HomeDecorSpacing.Sm),
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White,
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            Strings.wizardExampleRoom,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// PhotoActions  –  Change / Remove / Crop / Rotate affordances
// ---------------------------------------------------------------------------

@Composable
fun PhotoActions(
    onChange: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    onCrop: (() -> Unit)? = null,
    onRotate: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (onCrop != null) {
            PhotoActionButton(
                icon = Icons.Rounded.Crop,
                contentDescription = Strings.wizardCropImage,
                onClick = onCrop,
            )
        }
        if (onRotate != null) {
            PhotoActionButton(
                icon = Icons.Rounded.RotateRight,
                contentDescription = Strings.wizardRotateImage,
                onClick = onRotate,
            )
        }
        // Change image button
        Surface(
            onClick = onChange,
            shape = HomeDecorShape.Badge,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            modifier = Modifier.semantics {
                role = Role.Button
                contentDescription = Strings.wizardChangeImage
            },
        ) {
            Text(
                Strings.wizardChangeImage,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
            )
        }
        // Remove image button (accessible label)
        Surface(
            onClick = onRemove,
            shape = HomeDecorShape.Badge,
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.semantics {
                contentDescription = Strings.wizardRemoveImage
            },
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = Strings.wizardRemoveImage,
                modifier = Modifier
                    .padding(6.dp)
                    .size(HomeDecorIconSize.Small),
                tint = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
    }
}

@Composable
private fun PhotoActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Badge,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        modifier = Modifier.semantics {
            role = Role.Button
            this.contentDescription = contentDescription
        },
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .size(HomeDecorIconSize.Small),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// ---------------------------------------------------------------------------
// UploadTips  –  Photo quality tips
// ---------------------------------------------------------------------------

@Composable
fun UploadTips(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        UploadTipRow(Strings.wizardUploadPhotoTip1)
        UploadTipRow(Strings.wizardUploadPhotoTip2)
        UploadTipRow(Strings.wizardUploadPhotoTip3)
        UploadTipRow(Strings.wizardUploadPhotoTip4)
    }
}

@Composable
fun UploadTipRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(18.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(10.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// ---------------------------------------------------------------------------
// SampleImageButton  –  Secondary "Try example" action
// ---------------------------------------------------------------------------

@Composable
fun SampleImageButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = androidx.compose.animation.core.Spring.StiffnessHigh),
        label = "sampleBtnScale",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (isHovered)
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f),
        interactionSource = interactionSource,
        modifier = modifier
            .testTag(Strings.TestTags.wizardTryExample)
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                shape = RoundedCornerShape(14.dp),
            )
            .semantics {
                role = Role.Button
                contentDescription = Strings.wizardTryExample
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                Icons.Rounded.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Medium),
                tint = MaterialTheme.colorScheme.secondary,
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    Strings.wizardExampleCtaLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// UploadWorkflowExplainer  –  3-step "How it works" card
// ---------------------------------------------------------------------------

@Composable
fun UploadWorkflowExplainer(
    steps: List<String>,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.6f),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                Strings.wizardHowItWorks,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            steps.forEach { step ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        androidx.compose.material.icons.Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        step,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// UploadHelperText  –  Helper text below drop zone
// ---------------------------------------------------------------------------

@Composable
fun UploadHelperText(modifier: Modifier = Modifier) {
    Text(
        "For best results, show entire room, good lighting, avoid blur",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth(),
    )
}

// ---------------------------------------------------------------------------
// UploadOrDivider  –  "or" divider between sample and upload
// ---------------------------------------------------------------------------

@Composable
fun UploadOrDivider(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                ),
        )
        Text(
            "or",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 12.dp),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                ),
        )
    }
}

// ---------------------------------------------------------------------------
// UploadPrivacyNote  –  Privacy assurance row
// ---------------------------------------------------------------------------

@Composable
fun UploadPrivacyNote(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Icon(
            Icons.Rounded.Check,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        )
        Text(
            Strings.wizardPrivacyNote,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
    }
}

// ---------------------------------------------------------------------------
// UploadLoadingPreview  –  Loading state for image decoding
// ---------------------------------------------------------------------------

@Composable
fun UploadLoadingPreview(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                    )
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp,
        )
    }
}

// ---------------------------------------------------------------------------
// UploadImageFallback  –  Empty state inside preview area
// ---------------------------------------------------------------------------

@Composable
fun UploadImageFallback(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                    )
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Rounded.Image,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                Strings.wizardPhotoSelected,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
