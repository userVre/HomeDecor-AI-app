package com.ismail.homedecorai.ui.tools

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.getScreenWidthDp
import com.ismail.homedecorai.imagepicker.PickedImageData
import com.ismail.homedecorai.imagepicker.rememberDragDropHandler
import com.ismail.homedecorai.imagepicker.rememberImagePicker
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.ui.discover.NetworkImage
import com.ismail.homedecorai.ui.theme.*

// ---------------------------------------------------------------------------
// State
// ---------------------------------------------------------------------------

enum class WizardStep { Upload, RoomType, Style, Review }

data class WizardOption(
    val id: String,
    val label: String,
    val icon: @Composable () -> Unit = {},
)

data class WizardState(
    val step: WizardStep = WizardStep.Upload,
    val tool: ToolItem? = null,
    val photo: PickedImageData? = null,
    val isUsingExample: Boolean = false,
    val selectedRoom: String? = null,
    val selectedStyle: String? = null,
    val isGenerating: Boolean = false,
    val generationComplete: Boolean = false,
    val generationError: String? = null,
    val error: String? = null,
    val isDragging: Boolean = false,
    val waitlistEmail: String = "",
    val waitlistSubmitted: Boolean = false,
)

// ---------------------------------------------------------------------------
// Tool-specific options
// ---------------------------------------------------------------------------

private fun roomOptions(toolId: String): List<WizardOption> = when (toolId) {
    "interior" -> listOf(
        WizardOption("living-room", "Living Room"),
        WizardOption("bedroom", "Bedroom"),
        WizardOption("kitchen", "Kitchen"),
        WizardOption("bathroom", "Bathroom"),
        WizardOption("office", "Office"),
        WizardOption("dining", "Dining Room"),
    )
    "facade" -> listOf(
        WizardOption("house", "House"),
        WizardOption("apartment", "Apartment"),
        WizardOption("villa", "Villa"),
        WizardOption("cabin", "Cabin"),
    )
    "garden" -> listOf(
        WizardOption("backyard", "Backyard"),
        WizardOption("front-yard", "Front Yard"),
        WizardOption("patio", "Patio"),
        WizardOption("rooftop", "Rooftop"),
        WizardOption("balcony", "Balcony"),
    )
    "paint" -> listOf(
        WizardOption("living-room", "Living Room"),
        WizardOption("bedroom", "Bedroom"),
        WizardOption("kitchen", "Kitchen"),
        WizardOption("office", "Office"),
    )
    "floor" -> listOf(
        WizardOption("living-room", "Living Room"),
        WizardOption("bedroom", "Bedroom"),
        WizardOption("kitchen", "Kitchen"),
        WizardOption("bathroom", "Bathroom"),
        WizardOption("hallway", "Hallway"),
    )
    "layout" -> listOf(
        WizardOption("studio", "Studio Apartment"),
        WizardOption("one-bedroom", "One Bedroom"),
        WizardOption("open-plan", "Open Plan"),
        WizardOption("office", "Home Office"),
    )
    "replace" -> listOf(
        WizardOption("sofa", "Sofa"),
        WizardOption("table", "Table"),
        WizardOption("chair", "Chair"),
        WizardOption("bed", "Bed"),
        WizardOption("shelf", "Shelf"),
        WizardOption("lamp", "Lamp"),
    )
    "reference" -> listOf(
        WizardOption("living-room", "Living Room"),
        WizardOption("bedroom", "Bedroom"),
        WizardOption("kitchen", "Kitchen"),
        WizardOption("bathroom", "Bathroom"),
    )
    else -> listOf(
        WizardOption("room", "Room"),
        WizardOption("space", "Space"),
    )
}

private fun styleOptions(toolId: String): List<WizardOption> = when (toolId) {
    "interior", "reference" -> listOf(
        WizardOption("modern", "Modern"),
        WizardOption("minimalist", "Minimalist"),
        WizardOption("scandinavian", "Scandinavian"),
        WizardOption("bohemian", "Bohemian"),
        WizardOption("industrial", "Industrial"),
        WizardOption("japandi", "Japandi"),
        WizardOption("mid-century", "Mid-Century"),
        WizardOption("coastal", "Coastal"),
    )
    "facade" -> listOf(
        WizardOption("modern", "Modern"),
        WizardOption("classic", "Classic"),
        WizardOption("mediterranean", "Mediterranean"),
        WizardOption("tropical", "Tropical"),
    )
    "garden" -> listOf(
        WizardOption("zen", "Zen Garden"),
        WizardOption("english", "English Garden"),
        WizardOption("modern", "Modern Landscape"),
        WizardOption("tropical", "Tropical"),
        WizardOption("minimalist", "Minimalist"),
    )
    "paint" -> listOf(
        WizardOption("warm", "Warm Tones"),
        WizardOption("cool", "Cool Tones"),
        WizardOption("neutral", "Neutral"),
        WizardOption("bold", "Bold Accent"),
        WizardOption("pastel", "Pastel"),
        WizardOption("earth", "Earth Tones"),
    )
    "floor" -> listOf(
        WizardOption("hardwood", "Hardwood"),
        WizardOption("marble", "Marble"),
        WizardOption("tile", "Tile"),
        WizardOption("concrete", "Concrete"),
        WizardOption("carpet", "Carpet"),
    )
    "layout" -> listOf(
        WizardOption("open", "Open Layout"),
        WizardOption("partitioned", "Partitioned"),
        WizardOption("multi-use", "Multi-Use"),
    )
    "replace" -> listOf(
        WizardOption("modern", "Modern"),
        WizardOption("minimalist", "Minimalist"),
        WizardOption("luxury", "Luxury"),
        WizardOption("cozy", "Cozy"),
    )
    else -> listOf(
        WizardOption("modern", "Modern"),
        WizardOption("classic", "Classic"),
        WizardOption("minimalist", "Minimalist"),
    )
}

private fun stepTitle(step: WizardStep, toolId: String?): String = when (step) {
    WizardStep.Upload -> Strings.wizardStepUpload
    WizardStep.RoomType -> when (toolId) {
        "facade", "exterior" -> "Exterior Type"
        "garden" -> "Outdoor Area"
        "paint" -> "Wall Type"
        "floor" -> "Floor Area"
        "replace" -> "Furniture Type"
        "layout" -> "Layout Type"
        "reference" -> "Room Type"
        else -> Strings.wizardStepRoom
    }
    WizardStep.Style -> Strings.wizardStepStyle
    WizardStep.Review -> Strings.wizardStepReview
}

private fun roomStepTitle(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "What type of exterior is this?"
    "garden" -> "What outdoor area are you redesigning?"
    "paint" -> "What room are you painting?"
    "floor" -> "Where should the new flooring go?"
    "replace" -> "What furniture do you want to replace?"
    "layout" -> "What layout are you improving?"
    "reference" -> Strings.wizardRoomTitle
    else -> Strings.wizardRoomTitle
}

private fun roomStepSubtitle(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "Select the building type that best matches the facade"
    "garden" -> "Select the outdoor area that best matches your project"
    "paint" -> "Select the room where you want to change wall colors"
    "floor" -> "Select the room or area that needs new flooring"
    "replace" -> "Select the furniture category to swap in your design"
    "layout" -> "Select the floor plan type that needs a better flow"
    "reference" -> Strings.wizardRoomSubtitle
    else -> Strings.wizardRoomSubtitle
}

private fun roomReviewLabel(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "Exterior"
    "garden" -> "Outdoor Area"
    "paint" -> "Room"
    "floor" -> "Floor Area"
    "replace" -> "Furniture"
    "layout" -> "Layout"
    "reference" -> "Room"
    else -> Strings.wizardReviewRoom
}

private fun roomRequiredMessage(toolId: String?): String = when (toolId) {
    "interior" -> "Select a room type to continue"
    "facade", "exterior" -> "Select an exterior type to continue"
    "garden" -> "Select an outdoor area to continue"
    "paint" -> "Select a wall type to continue"
    "floor" -> "Select a floor area to continue"
    "replace" -> "Select a furniture type to continue"
    "layout" -> "Select a layout type to continue"
    "reference" -> "Select a room type to continue"
    else -> "Select a room type to continue"
}

private fun uploadTitleForTool(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "Upload a photo of your exterior"
    "garden" -> "Upload a photo of your outdoor area"
    "paint" -> "Upload a photo of your wall"
    "floor" -> "Upload a photo of your floor"
    "replace" -> "Upload a photo of your furniture"
    "layout" -> "Upload a photo of your floor plan"
    "reference" -> "Upload a photo of your space"
    else -> Strings.wizardUploadTitle
}

private fun uploadSubtitleForTool(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "Drag and drop an exterior photo, or click to browse"
    "garden" -> "Drag and drop a garden photo, or click to browse"
    "paint" -> "Drag and drop a wall photo, or click to browse"
    "floor" -> "Drag and drop a floor photo, or click to browse"
    "replace" -> "Drag and drop a furniture photo, or click to browse"
    "layout" -> "Drag and drop a floor plan photo, or click to browse"
    else -> Strings.wizardUploadSubtitle
}

// ---------------------------------------------------------------------------
// Main Wizard Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WebWizardScreen(
    tool: ToolItem,
    onBack: () -> Unit,
) {
    var state by remember { mutableStateOf(WizardState(tool = tool)) }
    var previousStep by remember { mutableStateOf(state.step) }
    val screenWidth = getScreenWidthDp()
    val isWide = screenWidth >= 700
    val stepFocusRequester = remember { FocusRequester() }

    // Request focus when wizard step changes for screen reader users
    LaunchedEffect(state.step) {
        try {
            stepFocusRequester.requestFocus()
        } catch (_: Exception) { }
    }

    // Track step direction for animation
    LaunchedEffect(state.step) {
        if (state.step != previousStep) {
            previousStep = state.step
        }
    }

    val picker = rememberImagePicker { picked: PickedImageData ->
        if (picked.isValid) {
            state = state.copy(photo = picked, isUsingExample = false, error = null, isDragging = false)
        }
    }

    rememberDragDropHandler(
        onImageDropped = { picked ->
            if (picked.isValid) {
                state = state.copy(photo = picked, isUsingExample = false, error = null, isDragging = false)
            }
        },
        onDragEnter = { state = state.copy(isDragging = true) },
        onDragLeave = { state = state.copy(isDragging = false) },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.wizardScreen),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WizardHeader(
            tool = tool,
            currentStep = state.step,
            onBack = {
                when (state.step) {
                    WizardStep.Upload -> onBack()
                    WizardStep.RoomType -> state = state.copy(step = WizardStep.Upload)
                    WizardStep.Style -> state = state.copy(step = WizardStep.RoomType)
                    WizardStep.Review -> state = state.copy(step = WizardStep.Style)
                }
            },
            onClose = onBack,
        )

        WizardProgressBar(
            currentStep = state.step,
            toolId = tool.id,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Md),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .focusRequester(stepFocusRequester)
                .testTag(Strings.formatTestTag(Strings.TestTags.wizardStepContent, state.step.name)),
        ) {
            AnimatedContent(
                targetState = state.step,
                label = "wizardStep",
                transitionSpec = {
                    val isForward = targetState.ordinal > initialState.ordinal
                    if (isForward) {
                        slideInHorizontally { it / 3 } + fadeIn() togetherWith
                                slideOutHorizontally { -it / 3 } + fadeOut()
                    } else {
                        slideInHorizontally { -it / 3 } + fadeIn() togetherWith
                                slideOutHorizontally { it / 3 } + fadeOut()
                    }
                },
            ) { step ->
                when (step) {
                    WizardStep.Upload -> UploadStep(
                        state = state,
                        isWide = isWide,
                        onImagePicked = { picker.openGallery() },
                        onTryExample = {
                            state = state.copy(
                                photo = PickedImageData(sourceUri = "example://demo", mimeType = "image/jpeg"),
                                isUsingExample = true,
                                error = null,
                            )
                        },
                        onRemovePhoto = { state = state.copy(photo = null, isUsingExample = false) },
                        onDragEnter = { state = state.copy(isDragging = true) },
                        onDragLeave = { state = state.copy(isDragging = false) },
                        onDrop = { picked ->
                            if (picked.isValid) {
                                state = state.copy(photo = picked, isUsingExample = false, error = null, isDragging = false)
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.RoomType -> RoomTypeStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(selectedRoom = it, error = null) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.Style -> StyleStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(selectedStyle = it, error = null) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.Review -> ReviewStep(
                        state = state,
                        isWide = isWide,
                        onGenerate = {
                            state = state.copy(isGenerating = true, generationError = null)
                        },
                        onJoinWaitlist = { email ->
                            state = state.copy(waitlistEmail = email, waitlistSubmitted = true)
                        },
                        onEditStep = { step ->
                            state = state.copy(step = step)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }

        WizardBottomBar(
            state = state,
            onBack = {
                when (state.step) {
                    WizardStep.Upload -> onBack()
                    WizardStep.RoomType -> state = state.copy(step = WizardStep.Upload)
                    WizardStep.Style -> state = state.copy(step = WizardStep.RoomType)
                    WizardStep.Review -> state = state.copy(step = WizardStep.Style)
                }
            },
            onNext = {
                when (state.step) {
                    WizardStep.Upload -> {
                        if (state.photo == null) {
                            state = state.copy(error = Strings.wizardErrorPhoto)
                        } else {
                            state = state.copy(step = WizardStep.RoomType, error = null)
                        }
                    }
                    WizardStep.RoomType -> {
                        if (state.selectedRoom == null) {
                            state = state.copy(error = roomRequiredMessage(state.tool?.id))
                        } else {
                            state = state.copy(step = WizardStep.Style, error = null)
                        }
                    }
                    WizardStep.Style -> {
                        if (state.selectedStyle == null) {
                            state = state.copy(error = Strings.wizardErrorStyle)
                        } else {
                            state = state.copy(step = WizardStep.Review, error = null)
                        }
                    }
                    WizardStep.Review -> { }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// ---------------------------------------------------------------------------
// Header
// ---------------------------------------------------------------------------

@Composable
private fun WizardHeader(
    tool: ToolItem,
    currentStep: WizardStep,
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
                .padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag(Strings.TestTags.wizardBackButton),
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = Strings.wizardBack,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    Strings.toolTitle(tool.id),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    stepTitle(currentStep, tool.id),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag(Strings.TestTags.wizardCloseButton),
            ) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = Strings.wizardClose,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Progress Bar (Stronger visual indicator)
// ---------------------------------------------------------------------------

@Composable
private fun WizardProgressBar(
    currentStep: WizardStep,
    toolId: String?,
    modifier: Modifier = Modifier,
) {
    val steps = WizardStep.entries
    val currentIndex = steps.indexOf(currentStep)
    val progress = (currentIndex + 1).toFloat() / steps.size

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "progressAnim",
    )

    Column(modifier = modifier
        .testTag(Strings.TestTags.wizardProgressBar)
        .semantics {
        contentDescription = "Step ${currentIndex + 1} of ${steps.size}: ${stepTitle(currentStep, toolId)}"
    }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            steps.forEachIndexed { index, step ->
                val isCompleted = index < currentIndex
                val isActive = index == currentIndex

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = when {
                            isCompleted -> MaterialTheme.colorScheme.primary
                            isActive -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.surfaceContainerHigh
                        },
                        modifier = Modifier.size(24.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isCompleted) {
                                Icon(
                                    Icons.Rounded.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.White,
                                )
                            } else {
                                Text(
                                    "${index + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                    Text(
                        stepTitle(step, toolId),
                        style = MaterialTheme.typography.labelMedium,
                        color = when {
                            isCompleted -> MaterialTheme.colorScheme.primary
                            isActive -> MaterialTheme.colorScheme.onSurface
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            strokeCap = StrokeCap.Round,
        )
    }
}

// ---------------------------------------------------------------------------
// Step 1: Upload (with real drag-and-drop)
// ---------------------------------------------------------------------------

@Composable
private fun UploadStep(
    state: WizardState,
    isWide: Boolean,
    onImagePicked: () -> Unit,
    onTryExample: () -> Unit,
    onRemovePhoto: () -> Unit,
    onDragEnter: () -> Unit,
    onDragLeave: () -> Unit,
    onDrop: (PickedImageData) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 24.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isWide) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        uploadTitleForTool(state.tool?.id),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        uploadSubtitleForTool(state.tool?.id),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(20.dp))
                    if (state.photo != null) {
                        PhotoPreview(
                            photo = state.photo,
                            isUsingExample = state.isUsingExample,
                            toolId = state.tool?.id,
                            isWide = isWide,
                            onRemove = onRemovePhoto,
                            onChange = onImagePicked,
                        )
                    } else {
                        UploadDropZone(
                            isWide = isWide,
                            isDragging = state.isDragging,
                            onClick = onImagePicked,
                            onDragEnter = onDragEnter,
                            onDragLeave = onDragLeave,
                            onDrop = onDrop,
                        )
                        Spacer(Modifier.height(12.dp))
                        TryExampleButton(onTryExample = onTryExample)
                    }
                    AnimatedVisibility(visible = state.error != null) {
                        Text(
                            state.error ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 12.dp),
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        uploadTitleForTool(state.tool?.id),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        uploadSubtitleForTool(state.tool?.id),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    TipRow("Upload a clear photo of your space for best results")
                    TipRow("JPG, PNG, or WebP formats supported")
                    TipRow("Use the 'Try example' option to see how it works")
                }
            }
        } else {
            Text(
                uploadTitleForTool(state.tool?.id),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                uploadSubtitleForTool(state.tool?.id),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(20.dp))
            if (state.photo != null) {
                PhotoPreview(
                    photo = state.photo,
                    isUsingExample = state.isUsingExample,
                    toolId = state.tool?.id,
                    isWide = isWide,
                    onRemove = onRemovePhoto,
                    onChange = onImagePicked,
                )
            } else {
                UploadDropZone(
                    isWide = isWide,
                    isDragging = state.isDragging,
                    onClick = onImagePicked,
                    onDragEnter = onDragEnter,
                    onDragLeave = onDragLeave,
                    onDrop = onDrop,
                )
                Spacer(Modifier.height(12.dp))
                TryExampleButton(onTryExample = onTryExample)
            }
            AnimatedVisibility(visible = state.error != null) {
                Text(
                    state.error ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun UploadDropZone(
    isWide: Boolean,
    isDragging: Boolean,
    onClick: () -> Unit,
    onDragEnter: () -> Unit,
    onDragLeave: () -> Unit,
    onDrop: (PickedImageData) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "dropScale",
    )

    val highlightBorder = isDragging || isHovered

    val borderColor = if (highlightBorder)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant

    val bgColor = if (highlightBorder)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
    else
        MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isWide) 280.dp else 200.dp)
            .testTag(Strings.TestTags.wizardUploadDropZone)
            .semantics {
                role = Role.Button
                contentDescription = Strings.wizardChooseImage
            }
            .scale(scale)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    listOf(borderColor, borderColor.copy(alpha = 0.3f))
                ),
                shape = RoundedCornerShape(20.dp),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(24.dp),
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
                        modifier = Modifier.size(32.dp),
                        tint = if (highlightBorder) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                if (isDragging) Strings.wizardDropHere else Strings.wizardChooseImage,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                Strings.wizardImageFormats,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))

            Surface(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Rounded.Image,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White,
                    )
                    Text(
                        Strings.wizardChooseImage,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun TryExampleButton(onTryExample: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "tryExampleScale",
    )

    Surface(
        onClick = onTryExample,
        shape = RoundedCornerShape(12.dp),
        color = if (isHovered)
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
        else
            Color.Transparent,
        interactionSource = interactionSource,
        modifier = Modifier
            .testTag(Strings.TestTags.wizardTryExample)
            .scale(scale)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp),
            )
            .semantics {
                role = Role.Button
                contentDescription = Strings.wizardTryExample
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.secondary,
            )
            Text(
                Strings.wizardTryExample,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun PhotoPreview(
    photo: PickedImageData,
    isUsingExample: Boolean = false,
    toolId: String? = null,
    isWide: Boolean,
    onRemove: () -> Unit,
    onChange: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(photo.sourceUri) {
        visible = false
        delay(50)
        visible = true
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isWide) 320.dp else 240.dp),
    ) {
        Box {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
            ) {
                if (isUsingExample) {
                    val exampleImageUrl = when (toolId) {
                        "garden" -> "images/assets_media_discover_garden_gardenpatio.webp"
                        "facade", "exterior" -> "images/assets_media_discover_exterior_exteriormodernvilla.webp"
                        "paint" -> "images/assets_media_discover_wallscenes_lavendermistbath.webp"
                        "floor" -> "images/assets_media_discover_floorscenes_naturaloakparquet.webp"
                        else -> "images/tool_interior.webp"
                    }
                    NetworkImage(
                        url = exampleImageUrl,
                        contentDescription = Strings.wizardExampleRoom,
                        modifier = Modifier.fillMaxSize(),
                    )
                    // Example badge overlay
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Rounded.Star,
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
                } else {
                    // User uploaded photo placeholder
                    Box(
                        modifier = Modifier
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    onClick = onChange,
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    modifier = Modifier.semantics {
                        role = Role.Button
                        contentDescription = Strings.wizardUploadChange
                    },
                ) {
                    Text(
                        Strings.wizardUploadChange,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Surface(
                    onClick = onRemove,
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.semantics {
                        contentDescription = Strings.wizardRemove
                    },
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = Strings.wizardRemove,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(18.dp),
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.White,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        Strings.wizardReady,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Step 2: Room Type
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RoomTypeStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = roomOptions(state.tool?.id ?: "")

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            roomStepTitle(state.tool?.id),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            roomStepSubtitle(state.tool?.id),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                val isSelected = state.selectedRoom == option.id
                OptionCard(
                    label = option.label,
                    isSelected = isSelected,
                    onClick = { onSelect(option.id) },
                    modifier = Modifier
                        .then(
                            if (isWide) Modifier.weight(1f).heightIn(min = 100.dp)
                            else Modifier.fillMaxWidth().heightIn(min = 80.dp)
                        ),
                )
            }
        }

        AnimatedVisibility(visible = state.error != null) {
            Text(
                state.error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step 3: Style (with thumbnails)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StyleStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = styleOptions(state.tool?.id ?: "")

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            Strings.wizardStyleTitle,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            Strings.wizardStyleSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                val isSelected = state.selectedStyle == option.id
                StyleOptionCard(
                    label = option.label,
                    styleId = option.id,
                    isSelected = isSelected,
                    onClick = { onSelect(option.id) },
                    modifier = Modifier
                        .then(
                            if (isWide) Modifier.weight(1f).heightIn(min = 120.dp)
                            else Modifier.fillMaxWidth().heightIn(min = 100.dp)
                        ),
                )
            }
        }

        AnimatedVisibility(visible = state.error != null) {
            Text(
                state.error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step 4: Review
// ---------------------------------------------------------------------------

@Composable
private fun ReviewStep(
    state: WizardState,
    isWide: Boolean,
    onGenerate: () -> Unit,
    onJoinWaitlist: (String) -> Unit,
    onEditStep: (WizardStep) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            Strings.wizardReviewTitle,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            Strings.wizardReviewSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))

        // Image Preview Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isWide) 200.dp else 160.dp),
        ) {
            Box {
                if (state.isUsingExample) {
                    val exampleImageUrl = when (state.tool?.id) {
                        "garden" -> "images/assets_media_discover_garden_gardenpatio.webp"
                        "facade", "exterior" -> "images/assets_media_discover_exterior_exteriormodernvilla.webp"
                        "paint" -> "images/assets_media_discover_wallscenes_lavendermistbath.webp"
                        "floor" -> "images/assets_media_discover_floorscenes_naturaloakparquet.webp"
                        else -> "images/tool_interior.webp"
                    }
                    NetworkImage(
                        url = exampleImageUrl,
                        contentDescription = Strings.wizardExampleRoom,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Box(
                        modifier = Modifier
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                Surface(
                    onClick = { onEditStep(WizardStep.Upload) },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .testTag(Strings.TestTags.wizardReviewEditRoom),
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

        Spacer(Modifier.height(16.dp))

        // Selection Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                onClick = { onEditStep(WizardStep.RoomType) },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .testTag(Strings.TestTags.wizardReviewEditRoom),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Rounded.CreditCard,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        roomReviewLabel(state.tool?.id),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        (state.selectedRoom ?: "").replace("-", " ").replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Surface(
                onClick = { onEditStep(WizardStep.Style) },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .testTag(Strings.TestTags.wizardReviewEditStyle),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        Strings.wizardReviewStyle,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        (state.selectedStyle ?: "").replace("-", " ").replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Generation States
        when {
            state.generationComplete -> {
                WaitlistSuccess()
            }
            state.generationError != null -> {
                GenerationError(
                    error = state.generationError!!,
                    onRetry = onGenerate,
                )
            }
            state.isGenerating -> {
                GeneratingState()
                Spacer(Modifier.height(16.dp))
                WaitlistFallback(
                    email = state.waitlistEmail,
                    onJoinWaitlist = onJoinWaitlist,
                )
            }
            else -> {
                GenerateButton(onClick = onGenerate)
                Spacer(Modifier.height(16.dp))
                WaitlistFallback(
                    email = state.waitlistEmail,
                    onJoinWaitlist = onJoinWaitlist,
                )
            }
        }
    }
}

@Composable
private fun GenerateButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
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
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag(Strings.TestTags.wizardGenerateButton)
            .semantics {
                role = Role.Button
                contentDescription = Strings.wizardGenerate
            }
            .scale(scale),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                Strings.wizardGenerate,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun GeneratingState() {
    var shimmer by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            shimmer = 1f; kotlinx.coroutines.delay(750)
            shimmer = 0f; kotlinx.coroutines.delay(750)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 5.dp,
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .graphicsLayer { alpha = shimmer * 0.5f },
                color = MaterialTheme.colorScheme.tertiary,
                strokeWidth = 5.dp,
                strokeCap = StrokeCap.Round,
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            Strings.wizardGenerating,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            Strings.wizardGeneratingBody,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            strokeCap = StrokeCap.Round,
        )
    }
}

@Composable
private fun GenerationError(
    error: String,
    onRetry: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(56.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Error,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color.White,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                Strings.wizardErrorTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
            Surface(
                onClick = onRetry,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    Strings.wizardTryAgain,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun WaitlistFallback(
    email: String,
    onJoinWaitlist: (String) -> Unit,
) {
    var emailInput by remember { mutableStateOf(email) }
    var isValidEmail by remember { mutableStateOf(true) }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.WbSunny,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                Strings.wizardComingSoon,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                Strings.wizardComingSoonBody,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))

            // Benefits list
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BenefitRow(Strings.wizardWaitlistBenefit1)
                BenefitRow(Strings.wizardWaitlistBenefit2)
                BenefitRow(Strings.wizardWaitlistBenefit3)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = emailInput,
                onValueChange = {
                    emailInput = it
                    isValidEmail = true
                },
                label = { Text(Strings.wizardWaitlistEmailLabel) },
                placeholder = { Text(Strings.wizardWaitlistEmailPlaceholder) },
                isError = !isValidEmail,
                supportingText = if (!isValidEmail) {
                    { Text(Strings.wizardWaitlistEmailError) }
                } else null,
                leadingIcon = {
                    Icon(
                        Icons.Rounded.MailOutline,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
            )

            Spacer(Modifier.height(12.dp))

            Surface(
                onClick = {
                    if (emailInput.isNotBlank() && emailInput.contains("@")) {
                        onJoinWaitlist(emailInput)
                    } else {
                        isValidEmail = false
                    }
                },
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth().semantics {
                    role = Role.Button
                    contentDescription = Strings.wizardJoinWaitlist
                },
            ) {
                Text(
                    Strings.wizardJoinWaitlist,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun BenefitRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(20.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun TipRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.tertiaryContainer,
            modifier = Modifier.size(20.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun WaitlistSuccess() {
    var pulse by remember { mutableStateOf(0.8f) }
    LaunchedEffect(Unit) {
        while (true) {
            pulse = 1.1f; kotlinx.coroutines.delay(500)
            pulse = 0.8f; kotlinx.coroutines.delay(500)
        }
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .graphicsLayer { scaleX = pulse; scaleY = pulse },
                        tint = Color.White,
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Text(
                Strings.wizardWaitlistSuccess,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                Strings.wizardWaitlistSuccessBody,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Option Cards
// ---------------------------------------------------------------------------

@Composable
private fun OptionCard(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cardScale",
    )

    val borderColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    val bgColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    else
        MaterialTheme.colorScheme.surface

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        interactionSource = interactionSource,
        modifier = modifier
            .testTag(Strings.formatTestTag(Strings.TestTags.wizardOptionCard, label))
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = Strings.a11yWizardOption(label, isSelected)
            }
            .scale(scale)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleSmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )
            AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn(spring(stiffness = Spring.StiffnessHigh)) + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}

// Style thumbnail colors and gradients
private data class StyleVisual(
    val gradient: List<Color>,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private fun getStyleVisual(styleId: String): StyleVisual = when (styleId) {
    "modern" -> StyleVisual(
        gradient = listOf(Color(0xFFE8EAF6), Color(0xFFC5CAE9)),
        icon = Icons.Rounded.WbSunny,
    )
    "minimalist" -> StyleVisual(
        gradient = listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0)),
        icon = Icons.Rounded.Check,
    )
    "scandinavian" -> StyleVisual(
        gradient = listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7)),
        icon = Icons.Rounded.Star,
    )
    "bohemian" -> StyleVisual(
        gradient = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2)),
        icon = Icons.Rounded.WbSunny,
    )
    "industrial" -> StyleVisual(
        gradient = listOf(Color(0xFFECEFF1), Color(0xFFCFD8DC)),
        icon = Icons.Rounded.CreditCard,
    )
    "japandi" -> StyleVisual(
        gradient = listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)),
        icon = Icons.Rounded.Star,
    )
    "mid-century" -> StyleVisual(
        gradient = listOf(Color(0xFFFBE9E7), Color(0xFFFFCCBC)),
        icon = Icons.Rounded.WbSunny,
    )
    "coastal" -> StyleVisual(
        gradient = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)),
        icon = Icons.Rounded.Star,
    )
    "classic" -> StyleVisual(
        gradient = listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7)),
        icon = Icons.Rounded.Star,
    )
    "mediterranean" -> StyleVisual(
        gradient = listOf(Color(0xFFE3F2FD), Color(0xFF90CAF9)),
        icon = Icons.Rounded.WbSunny,
    )
    "tropical" -> StyleVisual(
        gradient = listOf(Color(0xFFE8F5E9), Color(0xFFA5D6A7)),
        icon = Icons.Rounded.Star,
    )
    "zen" -> StyleVisual(
        gradient = listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9)),
        icon = Icons.Rounded.Star,
    )
    "english" -> StyleVisual(
        gradient = listOf(Color(0xFFE8F5E9), Color(0xFFA5D6A7)),
        icon = Icons.Rounded.Star,
    )
    "warm" -> StyleVisual(
        gradient = listOf(Color(0xFFFFF3E0), Color(0xFFFFCCBC)),
        icon = Icons.Rounded.WbSunny,
    )
    "cool" -> StyleVisual(
        gradient = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)),
        icon = Icons.Rounded.Star,
    )
    "neutral" -> StyleVisual(
        gradient = listOf(Color(0xFFEFEBE9), Color(0xFFD7CCC8)),
        icon = Icons.Rounded.Check,
    )
    "bold" -> StyleVisual(
        gradient = listOf(Color(0xFFFCE4EC), Color(0xFFF8BBD0)),
        icon = Icons.Rounded.Star,
    )
    "pastel" -> StyleVisual(
        gradient = listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7)),
        icon = Icons.Rounded.Star,
    )
    "earth" -> StyleVisual(
        gradient = listOf(Color(0xFFEFEBE9), Color(0xFFBCAAA4)),
        icon = Icons.Rounded.WbSunny,
    )
    "hardwood" -> StyleVisual(
        gradient = listOf(Color(0xFFD7CCC8), Color(0xFFA1887F)),
        icon = Icons.Rounded.CreditCard,
    )
    "marble" -> StyleVisual(
        gradient = listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0)),
        icon = Icons.Rounded.Star,
    )
    "tile" -> StyleVisual(
        gradient = listOf(Color(0xFFECEFF1), Color(0xFFB0BEC5)),
        icon = Icons.Rounded.CreditCard,
    )
    "concrete" -> StyleVisual(
        gradient = listOf(Color(0xFFECEFF1), Color(0xFF90A4AE)),
        icon = Icons.Rounded.CreditCard,
    )
    "carpet" -> StyleVisual(
        gradient = listOf(Color(0xFFE8EAF6), Color(0xFFC5CAE9)),
        icon = Icons.Rounded.Star,
    )
    "open" -> StyleVisual(
        gradient = listOf(Color(0xFFE3F2FD), Color(0xFF90CAF9)),
        icon = Icons.Rounded.WbSunny,
    )
    "partitioned" -> StyleVisual(
        gradient = listOf(Color(0xFFECEFF1), Color(0xFFB0BEC5)),
        icon = Icons.Rounded.CreditCard,
    )
    "multi-use" -> StyleVisual(
        gradient = listOf(Color(0xFFF3E5F5), Color(0xFFCE93D8)),
        icon = Icons.Rounded.Star,
    )
    "luxury" -> StyleVisual(
        gradient = listOf(Color(0xFFF3E5F5), Color(0xFFCE93D8)),
        icon = Icons.Rounded.Star,
    )
    "cozy" -> StyleVisual(
        gradient = listOf(Color(0xFFFFF3E0), Color(0xFFFFCCBC)),
        icon = Icons.Rounded.WbSunny,
    )
    else -> StyleVisual(
        gradient = listOf(Color(0xFFF5F5F5), Color(0xFFE0E0E0)),
        icon = Icons.Rounded.Check,
    )
}

@Composable
private fun StyleOptionCard(
    label: String,
    styleId: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "styleCardScale",
    )

    val borderColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    val styleVisual = remember(styleId) { getStyleVisual(styleId) }

    val bgBrush = if (isSelected) {
        Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
            )
        )
    } else {
        Brush.linearGradient(styleVisual.gradient)
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        interactionSource = interactionSource,
        modifier = modifier
            .testTag(Strings.formatTestTag(Strings.TestTags.wizardStyleCard, styleId))
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = Strings.a11yWizardOption(label, isSelected)
            }
            .scale(scale)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush)
                .padding(12.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) 
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    else 
                        Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(40.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            styleVisual.icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = if (isSelected) 
                                MaterialTheme.colorScheme.primary
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(22.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Bottom Bar
// ---------------------------------------------------------------------------

@Composable
private fun WizardBottomBar(
    state: WizardState,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val canProceed = when (state.step) {
        WizardStep.Upload -> state.photo != null
        WizardStep.RoomType -> state.selectedRoom != null
        WizardStep.Style -> state.selectedStyle != null
        WizardStep.Review -> false
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && canProceed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "nextScale",
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.testTag(Strings.TestTags.wizardBottomBar),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (state.step != WizardStep.Upload) {
                Surface(
                    onClick = onBack,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.testTag(Strings.TestTags.wizardBackStepButton),
                ) {
                    Text(
                        Strings.wizardBack,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                    )
                }
            } else {
                Spacer(Modifier.weight(1f))
            }

            Spacer(Modifier.weight(1f))

            if (state.step != WizardStep.Review) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        onClick = onNext,
                        shape = RoundedCornerShape(12.dp),
                        color = if (canProceed) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainerHigh,
                        interactionSource = interactionSource,
                        modifier = Modifier
                            .testTag(Strings.TestTags.wizardNextStepButton)
                            .scale(scale),
                    ) {
                        Text(
                            Strings.wizardNext,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (canProceed) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    if (!canProceed) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            when (state.step) {
                                WizardStep.Upload -> "Upload a photo or try an example to continue"
                                WizardStep.RoomType -> roomRequiredMessage(state.tool?.id)
                                WizardStep.Style -> "Choose a style to continue"
                                else -> ""
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
