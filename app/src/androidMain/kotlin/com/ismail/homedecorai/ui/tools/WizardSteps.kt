package com.ismail.homedecorai.ui.tools

import android.Manifest
import android.content.pm.PackageManager

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ViewQuilt
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ThumbDown
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.model.DecorTool

import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.model.MainTab
import com.ismail.homedecorai.model.MaskPoint
import com.ismail.homedecorai.model.MaskStroke
import com.ismail.homedecorai.Project
import com.ismail.homedecorai.R

import com.ismail.homedecorai.model.WizardStage
import com.ismail.homedecorai.model.hasVisibleMaskPaint
import com.ismail.homedecorai.model.isGeneratedResult
import com.ismail.homedecorai.model.isValidReplacementPrompt
import com.ismail.homedecorai.ui.components.*
import com.ismail.homedecorai.ui.dialogs.*
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*
import com.ismail.homedecorai.ui.components.ValidationAlertBanner
import com.ismail.homedecorai.validation.rememberStepValidation


private data class ImageInputActions(
    val openGallery: () -> Unit,
    val openCamera: () -> Unit,
)

@Composable
private fun rememberImageInputActions(
    onImageSelected: (Uri) -> Unit,
): ImageInputActions {
    val context = LocalContext.current
    val currentOnImageSelected by rememberUpdatedState(onImageSelected)
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let(currentOnImageSelected)
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        val capturedUri = pendingCameraUri
        pendingCameraUri = null
        if (saved && capturedUri != null) {
            currentOnImageSelected(capturedUri)
        }
    }

    fun launchCameraCapture() {
        val uri = createCameraUri(context)
        pendingCameraUri = uri
        runCatching { cameraLauncher.launch(uri) }
            .onFailure { pendingCameraUri = null }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            launchCameraCapture()
        } else {
            pendingCameraUri = null
        }
    }

    return ImageInputActions(
        openGallery = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        openCamera = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCameraCapture()
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
    )
}

@Composable
fun CreateScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    BackHandler(enabled = true) {
        if (state.wizardStage == WizardStage.Photo) {
            viewModel.selectTab(MainTab.Tools)
        } else {
            viewModel.previousStage()
        }
    }
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        DesignStepHeader(
            state = state,
            onBack = viewModel::previousStage,
            onClose = { viewModel.selectTab(MainTab.Tools) },
        )
        AnimatedContent(
            targetState = state.wizardStage,
            label = "wizard",
            modifier = Modifier.weight(1f),
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                } else {
                    slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                        slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                }
            },
        ) { stage ->
            when (stage) {
                WizardStage.Photo -> if (state.selectedTool.id == "reference") {
                    ReferenceImagesStep(state = state, viewModel = viewModel)
                } else {
                    PhotoStep(state = state, viewModel = viewModel)
                }
                WizardStage.Space -> when (state.selectedTool.id) {
                    "floor" -> SpecializedGenerateStep(state = state, viewModel = viewModel)
                    "paint" -> SpecializedGenerateStep(state = state, viewModel = viewModel)
                    "replace" -> ObjectMaskStep(state = state, viewModel = viewModel)
                    "reference" -> ReferencePhotoStep(state = state, viewModel = viewModel)
                    "layout" -> LayoutPlanningStep(state = state, viewModel = viewModel)
                    else -> {
                        val isGarden = state.selectedTool.id == "garden"
                        ChoiceStep(
                            state = state,
                            eyebrow = "",
                            copy = stepTwoCopy(state.selectedTool),
                            selected = if (isGarden) state.selectedStyles else state.selectedRooms,
                            onSelect = if (isGarden) viewModel::setStyle else viewModel::setRoom,
                            onContinue = viewModel::nextStage,
                            visualStyleCards = isGarden,
                            visualBuildingCards = state.selectedTool.id == "facade",
                        )
                    }
                }
                WizardStage.Style -> when (state.selectedTool.id) {
                    "paint", "floor", "replace", "reference" -> SpecializedGenerateStep(state = state, viewModel = viewModel)
                    else -> ChoiceStep(
                        state = state,
                        eyebrow = "",
                        copy = stepThreeCopy(state.selectedTool),
                        selected = state.selectedStyles,
                        onSelect = viewModel::setStyle,
                        onContinue = viewModel::nextStage,
                        visualStyleCards = state.selectedTool.id in listOf("interior", "facade", "garden", "floor"),
                    )
                }
                WizardStage.Refine -> RefineStep(state = state, viewModel = viewModel)
                WizardStage.Processing -> ProcessingStep(
                    state = state,
                    message = state.progressMessage,
                )
                WizardStage.Result -> ResultStep(state = state, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DesignStepHeader(
    state: HomeDecorUiState,
    onBack: () -> Unit,
    onClose: () -> Unit,
) {
    val step = wizardStepNumber(state.wizardStage, state.selectedTool)
    val totalSteps = wizardTotalSteps(state.selectedTool)
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(
                    start = HomeDecorSpacing.Base,
                    end = HomeDecorSpacing.Base,
                    top = HomeDecorSpacing.Xs,
                    bottom = HomeDecorSpacing.Sm,
                ),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    localizedWorkflowTitle(state.selectedTool),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 48.dp),
                )
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close))
                    }
                }
                if (step > 1) {
                    Box(modifier = Modifier.align(Alignment.CenterStart)) {
                        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    }
                }
            }
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            WizardStepIndicator(
                currentStep = step,
                totalSteps = totalSteps,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun WizardStepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    val pulseAnim = remember { Animatable(0f) }
    LaunchedEffect(currentStep) {
        pulseAnim.snapTo(0f)
        pulseAnim.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200),
                repeatMode = RepeatMode.Reverse,
            ),
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        repeat(totalSteps) { index ->
            val isCompleted = index < currentStep - 1
            val isActive = index == currentStep - 1

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (index > 0) {
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

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (index < totalSteps - 1) {
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
}

@Composable
fun StepScaffold(
    eyebrow: String,
    title: String,
    body: String? = null,
    buttonLabel: String,
    buttonIcon: ImageVector = Icons.Rounded.AutoAwesome,
    canProceed: Boolean = true,
    validationMessage: String? = null,
    onValidationFailed: (() -> Unit)? = null,
    contentBottomPadding: Dp = HomeDecorSpacing.Lg,
    protectBottomInsets: Boolean = false,
    buttonAllowsTwoLines: Boolean = false,
    showBottomButton: Boolean = true,
    onButton: () -> Unit,
    content: @Composable () -> Unit,
) {
    var showValidationBanner by remember { mutableStateOf(false) }

    LaunchedEffect(canProceed) {
        if (canProceed) showValidationBanner = false
    }

    LaunchedEffect(validationMessage) {
        if (validationMessage.isNullOrBlank()) showValidationBanner = false
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.Base,
                end = HomeDecorSpacing.Base,
                top = HomeDecorSpacing.Sm,
                bottom = contentBottomPadding + HomeDecorSpacing.CtaBarHeight,
            ),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.SectionGap),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    if (body != null) {
                        Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            item { content() }
        }
        if (showBottomButton) {
            Surface(
                color = MaterialTheme.colorScheme.background,
            ) {
                val buttonModifier = if (buttonAllowsTwoLines) {
                    Modifier.fillMaxWidth().heightIn(min = 56.dp)
                } else {
                    Modifier.fillMaxWidth().height(56.dp)
                }
                Column(
                    Modifier.fillMaxWidth().padding(PaddingValues(start = HomeDecorSpacing.Base, top = HomeDecorSpacing.Sm, end = HomeDecorSpacing.Base, bottom = HomeDecorSpacing.Md)),
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    if (showValidationBanner && !validationMessage.isNullOrBlank()) {
                        ValidationAlertBanner(message = validationMessage)
                    }
                    Button(
                        onClick = {
                            if (showValidationBanner) {
                                showValidationBanner = false
                                onButton()
                            } else if (!validationMessage.isNullOrBlank()) {
                                showValidationBanner = true
                                onValidationFailed?.invoke()
                            } else {
                                onButton()
                            }
                        },
                        enabled = canProceed,
                        shape = CircleShape,
                        colors = studioPrimaryButtonColors(),
                        modifier = buttonModifier.disabledSemantics(canProceed),
                    ) {
                        Icon(buttonIcon, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(
                            buttonLabel,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = if (buttonAllowsTwoLines) 2 else 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReferenceImagesStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val editorialReference = stringResource(R.string.editorial_reference)
    val roomImageInputActions = rememberImageInputActions { uri ->
        viewModel.setPrimaryPhoto(uri)
    }
    val referenceImageInputActions = rememberImageInputActions { uri ->
        viewModel.setReferencePhoto(uri)
    }
    val roomPhoto = state.selectedPhotos.firstOrNull()
    val hasRoom = roomPhoto != null
    val hasReference = state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null
    val selectedDiscoverReference = state.selectedReferenceDiscoverItemId?.let(::discoverItemById)
    val referenceImageRes = selectedDiscoverReference?.imageRes ?: R.drawable.tool_reference
    val referenceSelectedText = selectedDiscoverReference?.let { localizedGalleryTitle(it) }
        ?: state.selectedReferenceExampleLabel
        ?: stringResource(R.string.reference_added)
    val canContinue = hasRoom && hasReference
    val missingHint = when {
        !hasRoom && !hasReference -> stringResource(R.string.reference_missing_both_hint)
        !hasRoom -> stringResource(R.string.reference_missing_room_hint)
        !hasReference -> stringResource(R.string.reference_missing_reference_hint)
        else -> null
    }
    val selectedRoomExample = roomPhoto?.exampleLabel?.let { selectedLabel ->
        examplesForTool(state.selectedTool).firstOrNull { it.label == selectedLabel }
    }
    StepScaffold(
        eyebrow = "",
        title = stringResource(R.string.reference_two_images_title),
        body = stringResource(R.string.reference_two_images_body),
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        canProceed = canContinue,
        validationMessage = missingHint,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
            ReferenceDualImagePicker(
                title = stringResource(R.string.your_room),
                body = stringResource(R.string.your_room_body),
                missingHint = stringResource(R.string.reference_missing_room_hint),
                selected = hasRoom,
                selectedText = selectedRoomExample?.let { stringResource(it.labelRes) } ?: stringResource(R.string.photo_added),
                uri = roomPhoto?.uri,
                imageRes = roomPhoto?.let { selectedPhotoImageRes(state, it) } ?: examplesForTool(state.selectedTool).first().imageRes,
                contentDescription = stringResource(R.string.your_room),
                onGallery = roomImageInputActions.openGallery,
                onCamera = roomImageInputActions.openCamera,
                onExample = { viewModel.selectPrimaryExamplePhoto(examplesForTool(state.selectedTool).first().label) },
            )
            ReferenceDualImagePicker(
                title = stringResource(R.string.reference_image),
                body = stringResource(R.string.reference_image_body),
                missingHint = stringResource(R.string.reference_missing_reference_hint),
                selected = hasReference,
                selectedText = referenceSelectedText,
                uri = state.selectedReferenceUri,
                imageRes = referenceImageRes,
                contentDescription = stringResource(R.string.reference_image),
                onGallery = referenceImageInputActions.openGallery,
                onCamera = referenceImageInputActions.openCamera,
                onExample = { viewModel.selectReferenceExample(editorialReference) },
            )
            if (!hasRoom || !hasReference) {
                OutlinedButton(
                    onClick = viewModel::tryWithExample,
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(stringResource(R.string.try_with_example))
                }
            }
        }
    }
}

@Composable
fun ReferenceDualImagePicker(
    title: String,
    body: String,
    missingHint: String,
    selected: Boolean,
    selectedText: String,
    uri: Uri?,
    imageRes: Int,
    contentDescription: String,
    onGallery: () -> Unit,
    onCamera: () -> Unit,
    onExample: () -> Unit,
) {
    val dashedBorderStroke = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!selected) {
                    Modifier.border(dashedBorderStroke, RoundedCornerShape(24.dp))
                } else {
                    Modifier.border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.36f), RoundedCornerShape(24.dp))
                }
            ),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                Surface(shape = CircleShape, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow) {
                    Icon(
                        if (selected) Icons.Rounded.Check else Icons.Rounded.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.padding(9.dp).size(19.dp),
                        tint = if (selected) Color.White else MaterialTheme.colorScheme.primary,
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        if (selected) selectedText else stringResource(R.string.upload_style_reference_helper),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (selected) Color.Transparent else MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                if (selected) {
                    UriOrResourceImage(
                        uri = uri,
                        imageRes = imageRes,
                        contentDescription = contentDescription,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Column(
                        Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            Icons.Rounded.PhotoLibrary,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                        )
                        Spacer(Modifier.height(HomeDecorSpacing.Sm))
                        Text(
                            missingHint,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            if (!selected) {
                Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                    OutlinedButton(
                        onClick = onGallery,
                        shape = CircleShape,
                        modifier = Modifier.weight(1f).height(48.dp),
                        contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                    ) {
                        Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, Modifier.size(18.dp))
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(stringResource(R.string.gallery), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    OutlinedButton(
                        onClick = onCamera,
                        shape = CircleShape,
                        modifier = Modifier.weight(1f).height(48.dp),
                        contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                    ) {
                        Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(stringResource(R.string.camera), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                OutlinedButton(onClick = onExample, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(stringResource(R.string.example))
                }
            }
        }
    }
}

@Composable
fun ReferenceContinueHint(message: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.28f), RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Icon(Icons.Rounded.Lock, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error)
            Text(message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SourcePreviewCard(state: HomeDecorUiState) {
    val firstPhoto = state.selectedPhotos.firstOrNull()
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)),
    ) {
        Row(
            Modifier.padding(HomeDecorSpacing.Md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Box(Modifier.size(76.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)) {
                UriOrResourceImage(
                    uri = firstPhoto?.uri,
                    imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
                    contentDescription = stringResource(R.string.source_photo_preview),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs)) {
                Text(stringResource(R.string.source_photo_preview), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.source_photo_preview_body), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Rounded.Check, contentDescription = null, tint = HomeDecorExtra.success, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun MaskPreviewCard(
    state: HomeDecorUiState,
    title: String,
    body: String,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            MaskPreviewBox(state = state)
        }
    }
}

@Composable
fun MaskPreviewBox(state: HomeDecorUiState) {
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1.18f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        val firstPhoto = state.selectedPhotos.firstOrNull()
        UriOrResourceImage(
            uri = firstPhoto?.uri,
            imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
            contentDescription = stringResource(R.string.mask_preview_title),
            modifier = Modifier.fillMaxSize(),
        )
        Canvas(Modifier.matchParentSize()) {
            state.maskStrokes.forEach { stroke ->
                val points = stroke.points
                if (points.size >= 2) {
                    points.zipWithNext().forEach { (start, end) ->
                        drawLine(
                            color = if (stroke.erase) Color.Transparent else StudioAccent.copy(alpha = 0.62f),
                            start = Offset(start.x * size.width, start.y * size.height),
                            end = Offset(end.x * size.width, end.y * size.height),
                            strokeWidth = stroke.brushSize,
                            cap = StrokeCap.Round,
                            blendMode = if (stroke.erase) BlendMode.Clear else BlendMode.SrcOver,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReferenceStylePreview(state: HomeDecorUiState) {
    val roomPhoto = state.selectedPhotos.firstOrNull()
    val referenceImageRes = state.selectedReferenceDiscoverItemId
        ?.let(::discoverItemById)
        ?.imageRes
        ?: R.drawable.tool_reference
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
            Text(stringResource(R.string.reference_preview_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                PreviewTile(
                    title = stringResource(R.string.your_room),
                    uri = roomPhoto?.uri,
                    imageRes = roomPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
                    modifier = Modifier.weight(1f),
                )
                PreviewTile(
                    title = stringResource(R.string.reference_image),
                    uri = state.selectedReferenceUri,
                    imageRes = referenceImageRes,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun SelectedPhotoStrip(
    state: HomeDecorUiState,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
) {
    val removePhotoDescription = stringResource(R.string.remove_photo)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
        items(state.selectedPhotos.size, key = { "selected-photo-$it" }) { index ->
            val slot = state.selectedPhotos[index]
            val removePhotoDescription = stringResource(R.string.remove_photo)
            Box(Modifier.width(72.dp).height(64.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxSize().border(1.dp, if (index == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
                ) {
                    UriOrResourceImage(
                        uri = slot.uri,
                        imageRes = selectedPhotoImageRes(state, slot),
                        contentDescription = stringResource(R.string.photo_number, index + 1),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .minimumTouchTarget()
                        .semantics {
                            contentDescription = removePhotoDescription
                            role = Role.Button
                        }
                        .clickable(role = Role.Button) { onRemove(index) },
                    contentAlignment = Alignment.TopEnd,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = HomeDecorColors.DarkOverlay,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.padding(7.dp), tint = Color.White)
                    }
                }
            }
        }
        if (state.selectedPhotos.size < 3) {
            item("add-photo") {
                Surface(
                    onClick = onAdd,
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
                    modifier = Modifier.width(72.dp).height(64.dp).border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.add_photo), tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
fun ChoiceStep(
    state: HomeDecorUiState,
    eyebrow: String,
    copy: StepCopy,
    selected: List<String>,
    onSelect: (String) -> Unit,
    onContinue: () -> Unit,
    visualStyleCards: Boolean = false,
    visualBuildingCards: Boolean = false,
) {
    val stepValidation = rememberStepValidation(state)
    val hasSelection = selected.isNotEmpty()
    val canProceed = hasSelection
    val validationMessage = if (hasSelection) null else stepValidation.validationMessage
    StepScaffold(
        eyebrow = eyebrow,
        title = stringResource(copy.titleRes),
        body = stringResource(copy.bodyRes),
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        canProceed = canProceed,
        validationMessage = validationMessage,
        onButton = onContinue,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
        if (visualStyleCards || visualBuildingCards) {
            val columns = if (visualBuildingCards) GridCells.Fixed(2) else GridCells.Fixed(3)
            val rows = if (visualBuildingCards) (copy.options.size + 1) / 2 else (copy.options.size + 2) / 3
            val gridHeight = rows * 140 + (rows - 1) * 8 + 32
            LazyVerticalGrid(
                columns = columns,
                modifier = Modifier.fillMaxWidth().height(gridHeight.dp),
                contentPadding = PaddingValues(bottom = HomeDecorSpacing.BottomContentPadding + HomeDecorSpacing.Lg),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                userScrollEnabled = false,
            ) {
                items(copy.options, key = { it }) { option ->
                    StyleChoiceCard(
                        label = option,
                        selected = option in selected,
                        onClick = { onSelect(option) },
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                copy.options.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                        row.forEach { option ->
                            ExpressiveChoiceChip(
                                label = option,
                                selected = option in selected,
                                onClick = { onSelect(option) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
        }
    }
    }
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val imageInputActions = rememberImageInputActions { uri ->
        viewModel.setPhoto(uri)
    }
    var showUploadSheet by remember { mutableStateOf(false) }
    val copy = photoCopy(state.selectedTool)
    val copyTitle = if (state.roomType.isNotBlank()) {
        stringResource(R.string.upload_photo_of, localizedOption(state.roomType))
    } else {
        stringResource(copy.titleRes)
    }
    val copyBody = stringResource(copy.bodyRes)
    val stepValidation = rememberStepValidation(state)
    val hasMainPhoto = state.selectedPhotos.isNotEmpty()
    StepScaffold(
        eyebrow = "",
        title = if (!hasMainPhoto) copyTitle else stringResource(R.string.photo_added),
        body = if (!hasMainPhoto) copyBody else null,
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        canProceed = hasMainPhoto,
        validationMessage = stepValidation.validationMessage,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
            if (!hasMainPhoto) {
                MoziUploadCard(
                    onGallery = { imageInputActions.openGallery() },
                    onCamera = { imageInputActions.openCamera() },
                    onExample = {
                        val example = examplesForTool(state.selectedTool).first().label
                        viewModel.selectExamplePhoto(example)
                    },
                )
            } else {
                PhotoPreviewCard(
                    state = state,
                    onReplace = { showUploadSheet = true },
                    onRemove = { viewModel.removePhoto(0) },
                )
                SelectedPhotoStrip(
                    state = state,
                    onAdd = { showUploadSheet = true },
                    onRemove = viewModel::removePhoto,
                )
            }
        }
    }

    if (showUploadSheet) {
        ModalBottomSheet(
            onDismissRequest = { showUploadSheet = false },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                Modifier.padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Sm),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
            ) {
                Text(
                    stringResource(R.string.add_photo),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Surface(
                    onClick = {
                        showUploadSheet = false
                        imageInputActions.openCamera()
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        Modifier.padding(vertical = HomeDecorSpacing.Base),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
                    ) {
                        Icon(Icons.Rounded.PhotoCamera, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                        Text(stringResource(R.string.camera), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Surface(
                    onClick = {
                        showUploadSheet = false
                        imageInputActions.openGallery()
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        Modifier.padding(vertical = HomeDecorSpacing.Base),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
                    ) {
                        Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                        Text(stringResource(R.string.photos), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Surface(
                    onClick = {
                        showUploadSheet = false
                        val example = examplesForTool(state.selectedTool).first().label
                        viewModel.selectExamplePhoto(example)
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        Modifier.padding(vertical = HomeDecorSpacing.Base),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                        Text(stringResource(R.string.try_with_example), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(HomeDecorSpacing.Lg))
            }
        }
    }
}

@Composable
private fun PhotoPreviewCard(
    state: HomeDecorUiState,
    onReplace: () -> Unit,
    onRemove: () -> Unit,
) {
    val replaceDescription = stringResource(R.string.a11y_replace_image)
    val removeDescription = stringResource(R.string.a11y_remove_selected_image)
    val previewDescription = stringResource(R.string.a11y_selected_image_preview)
    val checkScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh,
        ),
        label = "checkScale",
    )
    Surface(
        shape = HomeDecorShape.ExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = previewDescription }
            .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.36f), HomeDecorShape.ExtraLarge),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.33f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            ) {
                val firstPhoto = state.selectedPhotos.first()
                UriOrResourceImage(
                    uri = firstPhoto.uri,
                    imageRes = selectedPhotoImageRes(state, firstPhoto),
                    contentDescription = stringResource(R.string.photo_added),
                    modifier = Modifier.fillMaxSize(),
                )
                Surface(
                    shape = CircleShape,
                    color = HomeDecorExtra.success,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .graphicsLayer {
                            scaleX = checkScale
                            scaleY = checkScale
                        },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                OutlinedButton(
                    onClick = onReplace,
                    shape = CircleShape,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .semantics { contentDescription = replaceDescription },
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, Modifier.size(16.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(stringResource(R.string.upload_photo_replace), maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelLarge)
                }
                OutlinedButton(
                    onClick = onRemove,
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                    modifier = Modifier
                        .height(44.dp)
                        .semantics { contentDescription = removeDescription },
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = null, Modifier.size(16.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(stringResource(R.string.upload_photo_remove), maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun MoziUploadCard(
    onGallery: () -> Unit,
    onCamera: () -> Unit,
    onExample: () -> Unit,
) {
    val uploadAreaDescription = stringResource(R.string.a11y_upload_photo_area)
    val galleryDescription = stringResource(R.string.a11y_open_gallery)
    val cameraDescription = stringResource(R.string.a11y_take_photo)
    val exampleDescription = stringResource(R.string.a11y_try_example)
    Surface(
        shape = HomeDecorShape.ExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = uploadAreaDescription }
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), HomeDecorShape.ExtraLarge),
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Base),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .clip(HomeDecorShape.Large)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.25f),
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(48.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.PhotoCamera,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    }
                    Text(
                        stringResource(R.string.upload_photo_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                OutlinedButton(
                    onClick = onGallery,
                    shape = CircleShape,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .semantics { contentDescription = galleryDescription },
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        stringResource(R.string.gallery),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                OutlinedButton(
                    onClick = onCamera,
                    shape = CircleShape,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .semantics { contentDescription = cameraDescription },
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        stringResource(R.string.camera),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            OutlinedButton(
                onClick = onExample,
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .semantics { contentDescription = exampleDescription },
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(
                    stringResource(R.string.try_with_example),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
fun MaterialLibrarySection(
    options: List<String>,
    selected: List<String>,
    onSelect: (String) -> Unit,
) {
    val selectedMaterial = selected.firstOrNull()
    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
        selectedMaterial?.let {
            SelectedMaterialPreview(
                label = it,
            )
        }
        options.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                row.forEach { option ->
                    MaterialSwatchCard(
                        label = option,
                        selected = option in selected,
                        onClick = { onSelect(option) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SelectedMaterialPreview(
    label: String,
) {
    val displayLabel = localizedOption(label)
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.26f), RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.padding(HomeDecorSpacing.Md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            MaterialSwatchThumb(label = label, selected = true, modifier = Modifier.size(52.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(displayLabel, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            }
            Icon(Icons.Rounded.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun MaterialSwatchCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    val shape = RoundedCornerShape(16.dp)
    Surface(
        onClick = onClick,
        shape = shape,
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier
            .height(104.dp)
            .semantics {
                this.selected = selected
                contentDescription = displayLabel
            }
            .border(if (selected) 2.dp else 1.dp, studioStateBorder(selected), shape),
    ) {
        Column(
            Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            MaterialSwatchThumb(label = label, selected = selected, modifier = Modifier.fillMaxWidth().height(48.dp))
            Text(
                displayLabel,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun MaterialSwatchThumb(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val spec = materialSwatchSpec(label)
    Box(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(spec.base, spec.base.copy(alpha = 0.82f), spec.accent.copy(alpha = 0.5f))))
            .border(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            when (spec.pattern) {
                MaterialPattern.Vein -> {
                    drawLine(spec.accent.copy(alpha = 0.72f), Offset(size.width * 0.12f, size.height * 0.75f), Offset(size.width * 0.88f, size.height * 0.2f), strokeWidth = 2.2f)
                    drawLine(spec.accent.copy(alpha = 0.42f), Offset(size.width * 0.2f, size.height * 0.24f), Offset(size.width * 0.72f, size.height * 0.64f), strokeWidth = 1.4f)
                }
                MaterialPattern.Wood -> {
                    for (index in 1..5) {
                        val y = size.height * index / 6f
                        drawLine(spec.accent.copy(alpha = 0.46f), Offset(0f, y), Offset(size.width, y + if (index % 2 == 0) 8f else -6f), strokeWidth = 2f)
                    }
                }
                MaterialPattern.Concrete, MaterialPattern.Limewash -> {
                    for (index in 0..8) {
                        val x = size.width * ((index * 23) % 100) / 100f
                        val y = size.height * ((index * 37) % 100) / 100f
                        drawCircle(spec.accent.copy(alpha = if (spec.pattern == MaterialPattern.Concrete) 0.2f else 0.12f), radius = 7f + index, center = Offset(x, y))
                    }
                }
                MaterialPattern.Terrazzo -> {
                    val chips = listOf(
                        Offset(size.width * 0.18f, size.height * 0.3f),
                        Offset(size.width * 0.44f, size.height * 0.62f),
                        Offset(size.width * 0.7f, size.height * 0.28f),
                        Offset(size.width * 0.84f, size.height * 0.74f),
                    )
                    chips.forEachIndexed { index, offset ->
                        drawCircle(listOf(spec.accent, Color(0xFFC47A5A), Color(0xFF2D2A26))[index % 3].copy(alpha = 0.72f), radius = 4f + index, center = offset)
                    }
                }
                MaterialPattern.Tile -> {
                    drawLine(spec.accent.copy(alpha = 0.52f), Offset(size.width / 2f, 0f), Offset(size.width / 2f, size.height), strokeWidth = 1.4f)
                    drawLine(spec.accent.copy(alpha = 0.52f), Offset(0f, size.height / 2f), Offset(size.width, size.height / 2f), strokeWidth = 1.4f)
                }
                MaterialPattern.Paint -> Unit
            }
        }
        if (selected) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface) {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.padding(5.dp).size(15.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun SpecializedGenerateStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val stepCopy = stepThreeCopy(state.selectedTool)
    val stepTitle = stringResource(stepCopy.titleRes)
    val stepBody = stringResource(stepCopy.bodyRes)
    val selected = state.selectedStyles
    val replacementPrompt = state.customPrompt.trim()
    val hasReplacementPrompt = replacementPrompt.isValidReplacementPrompt()
    
    // Unified validation replaces all inline canGenerate/disabledReason logic
    val stepValidation = rememberStepValidation(state)
    val isPaintOrFloor = state.selectedTool.id in setOf("paint", "floor")
    val isPro = state.isPro
    val diamondCost = if (isPro) stringResource(R.string.pro_upper) else "1"
    
    StepScaffold(
        eyebrow = "",
        title = stepTitle,
        body = stepBody,
        buttonLabel = if (isPro) stringResource(R.string.generate) else stringResource(R.string.generate_with_diamond, diamondCost),
        canProceed = stepValidation.canProceed,
        validationMessage = stepValidation.validationMessage,
        contentBottomPadding = if (isPaintOrFloor) HomeDecorSpacing.Xl else HomeDecorSpacing.Base,
        protectBottomInsets = isPaintOrFloor,
        buttonAllowsTwoLines = isPaintOrFloor,
        onButton = viewModel::generate,
    ) {
        if (state.selectedTool.id == "replace") {
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
                UnifiedWizardError(
                    message = state.generationError.orEmpty(),
                    onRetry = viewModel::generate,
                    onDismiss = viewModel::clearGenerationError,
                )
                OutlinedTextField(
                    value = state.customPrompt,
                    onValueChange = viewModel::setCustomPrompt,
                    label = { Text(stringResource(R.string.replacement_object)) },
                    placeholder = {
                        Text(stringResource(R.string.prompt_replace))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(16.dp),
                    isError = state.customPrompt.isNotBlank() && !hasReplacementPrompt,
                    supportingText = {
                        Text(
                            if (state.customPrompt.isBlank() || hasReplacementPrompt) {
                                stringResource(R.string.describe_new_object)
                            } else {
                                stringResource(R.string.replacement_prompt_required_error)
                            },
                        )
                    },
                )
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                    Text(stringResource(R.string.replacement_suggestions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    stepCopy.options.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                            row.forEach { option ->
                                val optionPrompt = localizedOption(option)
                                val templatePrompt = HomeDecorCatalog.replacementTemplatePrompts[option].orEmpty()
                                ReplaceSuggestionChip(
                                    label = option,
                                    selected = option in selected ||
                                        optionPrompt in selected ||
                                        templatePrompt in selected ||
                                        replacementPrompt == option ||
                                        replacementPrompt == optionPrompt ||
                                        replacementPrompt == templatePrompt,
                                    onClick = {
                                        viewModel.selectReplacementSuggestion(option)
                                    },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
                AdvancedControls(state = state, viewModel = viewModel)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
                UnifiedWizardError(
                    message = state.generationError.orEmpty(),
                    onRetry = viewModel::generate,
                    onDismiss = viewModel::clearGenerationError,
                )
                if (state.selectedTool.id != "reference") {
                    SourcePreviewCard(state = state)
                }
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                    if (state.selectedTool.id in setOf("paint", "floor")) {
                        MaterialLibrarySection(
                            options = stepCopy.options,
                            selected = selected,
                            onSelect = viewModel::setStyle,
                        )
                    } else if (state.selectedTool.id == "reference") {
                        stepCopy.options.chunked(2).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                                row.forEach { option ->
                                    IntensityChip(
                                        label = option,
                                        selected = option in selected,
                                        onClick = {
                                            viewModel.setStyle(option)
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                                if (row.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    } else {
                        stepCopy.options.chunked(2).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                                row.forEach { option ->
                                    ExpressiveChoiceChip(
                                        label = option,
                                        selected = option in selected,
                                        onClick = {
                                            viewModel.setStyle(option)
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                                if (row.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
                if (state.selectedTool.id == "reference") {
                    Text(stringResource(R.string.transfer_options), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    HomeDecorCatalog.referenceOptions.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                            row.forEach { option ->
                                IntensityChip(
                                    label = option,
                                    selected = option in state.selectedPalettes,
                                    onClick = { viewModel.setPalette(option) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
                if (state.selectedTool.id in setOf("paint", "floor")) {
                    OutlinedTextField(
                        value = state.customPrompt,
                        onValueChange = viewModel::setCustomPrompt,
                        label = { Text(stringResource(R.string.custom_notes)) },
                        placeholder = {
                            Text(
                                if (state.selectedTool.id == "paint") stringResource(R.string.custom_notes_paint_placeholder)
                                else stringResource(R.string.custom_notes_floor_placeholder),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 1,
                        maxLines = 4,
                        shape = RoundedCornerShape(16.dp),
                    )
                } else {
                    OutlinedTextField(
                        value = state.customPrompt,
                        onValueChange = viewModel::setCustomPrompt,
                        label = {
                            Text(stringResource(R.string.custom_notes))
                        },
                        placeholder = {
                            Text(stringResource(R.string.custom_notes_placeholder))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = RoundedCornerShape(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun AdvancedControls(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val spec = HomeDecorCatalog.advancedControlSpecs[state.selectedTool.id]
    val protectionOnly = state.selectedTool.id in HomeDecorCatalog.protectRestToolIds
    val recentStyles = remember(state.workspace.recentStyles, state.selectedTool.id) {
        state.workspace.recentStyles
            .asSequence()
            .filter { it.toolId == state.selectedTool.id && it.style.isNotBlank() }
            .sortedByDescending { it.lastUsedAt }
            .distinctBy { it.style.trim().lowercase() }
            .take(6)
            .map { it.style }
            .toList()
    }
    var expanded by remember(state.selectedTool.id) { mutableStateOf(false) }
    val advancedControlsDescription = stringResource(R.string.a11y_advance_controls)
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = advancedControlsDescription
                        role = Role.Button
                    }
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            ) {
                Icon(Icons.Rounded.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(stringResource(R.string.advanced_controls), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(stringResource(R.string.advanced_controls_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                Icon(
                    if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (expanded) {
                if (spec != null) {
                    AdvancedOptionGroup(
                        title = stringResource(R.string.keep_controls_title),
                        options = spec.keepOptions,
                        selectedOptions = state.keepOptions,
                        onToggle = viewModel::toggleKeepOption,
                    )
                    AdvancedOptionGroup(
                        title = stringResource(R.string.change_controls_title),
                        options = spec.changeOptions,
                        selectedOptions = state.changeOptions,
                        onToggle = viewModel::toggleChangeOption,
                    )
                }
                if (protectionOnly) {
                    CompactFilterChip(
                        label = stringResource(R.string.preserve_rest_of_image),
                        selected = state.preserveRestOfImage,
                        onClick = viewModel::togglePreserveRestOfImage,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                if (!protectionOnly) {
                    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                        Text(stringResource(R.string.budget_mode), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                            HomeDecorCatalog.budgetModes.forEach { mode ->
                                    CompactFilterChip(
                                        label = localizedAdvancedOption(mode),
                                        selected = state.budgetMode == mode,
                                        onClick = { viewModel.setBudgetMode(mode) },
                                        modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                        Text(stringResource(R.string.avoid_these), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        HomeDecorCatalog.avoidOptions.chunked(2).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                                row.forEach { option ->
                                    CompactFilterChip(
                                        label = localizedAdvancedOption(option),
                                        selected = option in state.avoidOptions,
                                        onClick = { viewModel.toggleAvoidOption(option) },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                                if (row.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                    if (recentStyles.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                            Text(stringResource(R.string.recent_styles), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                                items(recentStyles, key = { "recent-style-$it" }) { style ->
                                    CompactFilterChip(
                                        label = localizedOption(style),
                                        selected = style in state.selectedStyles || state.style == style || state.customPrompt == style,
                                        onClick = {
                                            when (state.selectedTool.id) {
                                                "replace" -> viewModel.setCustomPrompt(style)
                                                "layout" -> viewModel.setStyleText(style)
                                                else -> viewModel.setStyle(style)
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = viewModel::tryWithExample,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(stringResource(R.string.try_with_example), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun AdvancedOptionGroup(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onToggle: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        options.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                row.forEach { option ->
                    CompactFilterChip(
                        label = localizedAdvancedOption(option),
                        selected = option in selectedOptions,
                        onClick = { onToggle(option) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ReferencePhotoStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val editorialReference = stringResource(R.string.editorial_reference)
    val referenceImageInputActions = rememberImageInputActions { uri ->
        viewModel.setReferencePhoto(uri)
    }
    val hasReference = state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null
    StepScaffold(
        eyebrow = "",
        title = stringResource(R.string.step_add_reference_title),
        body = stringResource(R.string.step_add_reference_body),
        buttonLabel = stringResource(R.string.continue_action),
        canProceed = hasReference,
        validationMessage = if (hasReference) null else stringResource(R.string.reference_missing_error),
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
            ReferenceImagePicker(
                selectedUri = state.selectedReferenceUri,
                selectedExample = state.selectedReferenceExampleLabel,
                selectedImageRes = state.selectedReferenceDiscoverItemId?.let(::discoverItemById)?.imageRes ?: R.drawable.tool_reference,
                onImport = referenceImageInputActions.openGallery,
                onExample = { viewModel.selectReferenceExample(editorialReference) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                OutlinedButton(
                    onClick = referenceImageInputActions.openGallery,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp),
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(stringResource(R.string.gallery), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(
                    onClick = referenceImageInputActions.openCamera,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp),
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(stringResource(R.string.camera), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            OutlinedButton(
                onClick = { viewModel.selectReferenceExample(editorialReference) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(stringResource(R.string.try_with_example))
            }
        }
    }
}

@Composable
fun FloorMaskStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    MaskEditorStep(
        state = state,
        viewModel = viewModel,
        title = stringResource(R.string.mask_floor_title),
        body = stringResource(R.string.mask_floor_body),
        disabledLabel = stringResource(R.string.mask_floor_disabled),
        target = "floor",
        imageDescription = stringResource(R.string.mask_floor_description),
        emptyStateTitle = stringResource(R.string.mask_floor_empty_title),
        emptyStateBody = stringResource(R.string.mask_floor_empty_body),
        polishedControls = true,
        allowAutoDetect = false,
    )
}

@Composable
fun WallSurfaceStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    MaskEditorStep(
        state = state,
        viewModel = viewModel,
        title = stringResource(R.string.mask_wall_title),
        body = stringResource(R.string.mask_wall_body),
        disabledLabel = stringResource(R.string.mask_wall_disabled),
        target = "wall",
        imageDescription = stringResource(R.string.mask_wall_description),
        emptyStateTitle = stringResource(R.string.mask_wall_empty_title),
        emptyStateBody = stringResource(R.string.mask_wall_empty_body),
        polishedControls = true,
        allowAutoDetect = false,
    )
}

@Composable
fun ObjectMaskStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    MaskEditorStep(
        state = state,
        viewModel = viewModel,
        title = stringResource(R.string.mask_object_title),
        body = stringResource(R.string.mask_object_body),
        disabledLabel = stringResource(R.string.mask_object_disabled),
        target = "object",
        imageDescription = stringResource(R.string.mask_object_description),
        emptyStateTitle = null,
        emptyStateBody = null,
        polishedControls = true,
        forceEnableButton = true,
        maskColor = Color(0x80008080),
        showNextButton = true,
        onNext = viewModel::nextStage,
    )
}

@Composable
fun MaskEditorStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    title: String,
    body: String? = null,
    disabledLabel: String,
    target: String,
    imageDescription: String,
    emptyStateTitle: String? = null,
    emptyStateBody: String? = null,
    polishedControls: Boolean = false,
    allowAutoDetect: Boolean = target != "object",
    forceEnableButton: Boolean = false,
    maskColor: Color = StudioAccent,
    showNextButton: Boolean = false,
    onNext: (() -> Unit)? = null,
) {
    val requiresVisibleMask = target in setOf("floor", "wall", "object")
    val isSurfaceMask = target in setOf("floor", "wall")
    val isObjectMask = target == "object"
    val hasMask = remember(state.maskStrokes, target) {
        if (requiresVisibleMask) {
            state.maskStrokes.hasVisibleMaskPaint()
        } else {
            state.maskStrokes.any { !it.erase && it.points.size > 1 }
        }
    }
    val surfaceLabel = if (target == "floor") {
        stringResource(R.string.mask_floor_marked)
    } else {
        stringResource(R.string.mask_wall_marked)
    }
    val surfaceGuidance = if (target == "floor") {
        stringResource(R.string.mask_required_floor)
    } else {
        stringResource(R.string.mask_required_wall)
    }
    val objectStepValidation = if (isObjectMask) rememberStepValidation(state) else null
    val buttonCanProceed = if (forceEnableButton) true else (objectStepValidation?.canProceed ?: hasMask)
    StepScaffold(
        eyebrow = "",
        title = title,
        body = body,
        buttonLabel = stringResource(R.string.continue_action),
        canProceed = buttonCanProceed,
        validationMessage = objectStepValidation?.validationMessage ?: disabledLabel,
        contentBottomPadding = if (isSurfaceMask) 32.dp else 16.dp,
        protectBottomInsets = isSurfaceMask,
        buttonAllowsTwoLines = isSurfaceMask,
        showBottomButton = !(showNextButton && hasMask),
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
            if (isObjectMask && !hasMask && emptyStateTitle != null) {
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs)) {
                    Text(
                        emptyStateTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    if (!emptyStateBody.isNullOrBlank()) {
                        Text(
                            emptyStateBody,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            MaskCanvas(
                state = state,
                imageDescription = imageDescription,
                emptyStateTitle = null,
                emptyStateBody = null,
                hasVisibleMask = hasMask,
                readyLabel = if (isSurfaceMask) stringResource(R.string.mask_ready, surfaceLabel) else null,
                onStroke = viewModel::addMaskStroke,
                maskColor = maskColor,
                showFloatingNext = showNextButton && hasMask,
                onNext = onNext,
            )
            if (polishedControls && isSurfaceMask) {
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                    SurfaceMaskStatus(
                        hasMask = hasMask,
                        readyText = stringResource(R.string.mask_ready, surfaceLabel),
                        requiredText = surfaceGuidance,
                    )
                    BoxWithConstraints(Modifier.fillMaxWidth()) {
                        val compactControls = maxWidth < 360.dp
                        Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                                ToolToggle(
                                    label = stringResource(R.string.mask_mark),
                                    contentDescription = stringResource(R.string.a11y_mask_brush_add),
                                    icon = Icons.Rounded.Brush,
                                    selected = !state.eraserSelected,
                                    modifier = Modifier.weight(1f),
                                ) { viewModel.setMaskEraser(false) }
                                ToolToggle(
                                    label = stringResource(R.string.mask_remove),
                                    contentDescription = stringResource(R.string.a11y_mask_eraser_remove),
                                    icon = Icons.Rounded.Delete,
                                    selected = state.eraserSelected,
                                    modifier = Modifier.weight(1f),
                                ) { viewModel.setMaskEraser(true) }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                                MaskActionButton(
                                    label = stringResource(R.string.undo),
                                    contentDescription = stringResource(R.string.a11y_undo_mask_stroke),
                                    icon = Icons.AutoMirrored.Rounded.ArrowBack,
                                    enabled = state.maskStrokes.isNotEmpty(),
                                    onClick = viewModel::undoMaskStroke,
                                    modifier = Modifier.weight(1f),
                                    showLabel = !compactControls,
                                )
                                MaskActionButton(
                                    label = stringResource(R.string.redo),
                                    contentDescription = stringResource(R.string.a11y_redo_mask_stroke),
                                    icon = Icons.AutoMirrored.Rounded.ArrowForward,
                                    enabled = state.undoneMaskStrokes.isNotEmpty(),
                                    onClick = viewModel::redoMaskStroke,
                                    modifier = Modifier.weight(1f),
                                    showLabel = !compactControls,
                                )
                                MaskActionButton(
                                    label = stringResource(R.string.clear),
                                    contentDescription = stringResource(R.string.clear_full_mask),
                                    icon = Icons.Rounded.Close,
                                    enabled = state.maskStrokes.isNotEmpty(),
                                    onClick = viewModel::clearMask,
                                    modifier = Modifier.weight(1f),
                                    showLabel = !compactControls,
                                )
                            }
                        }
                    }
                }
            } else if (polishedControls) {
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                        ToolToggle(
                            label = stringResource(R.string.mask_brush),
                            contentDescription = stringResource(R.string.a11y_mask_brush_add),
                            icon = Icons.Rounded.Brush,
                            selected = !state.eraserSelected,
                            modifier = Modifier.weight(1f),
                        ) { viewModel.setMaskEraser(false) }
                        ToolToggle(
                            label = stringResource(R.string.mask_eraser),
                            contentDescription = stringResource(R.string.a11y_mask_eraser_remove),
                            icon = Icons.Rounded.Delete,
                            selected = state.eraserSelected,
                            modifier = Modifier.weight(1f),
                        ) { viewModel.setMaskEraser(true) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                        MaskActionButton(
                            label = stringResource(R.string.undo),
                            contentDescription = stringResource(R.string.a11y_undo_mask_stroke),
                            icon = Icons.AutoMirrored.Rounded.ArrowBack,
                            enabled = state.maskStrokes.isNotEmpty(),
                            onClick = viewModel::undoMaskStroke,
                            modifier = Modifier.weight(1f),
                        )
                        MaskActionButton(
                            label = stringResource(R.string.redo),
                            contentDescription = stringResource(R.string.a11y_redo_mask_stroke),
                            icon = Icons.AutoMirrored.Rounded.ArrowForward,
                            enabled = state.undoneMaskStrokes.isNotEmpty(),
                            onClick = viewModel::redoMaskStroke,
                            modifier = Modifier.weight(1f),
                        )
                        MaskActionButton(
                            label = stringResource(R.string.clear),
                            contentDescription = stringResource(R.string.clear_full_mask),
                            icon = Icons.Rounded.Close,
                            enabled = state.maskStrokes.isNotEmpty(),
                            onClick = viewModel::clearMask,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                    ToolToggle(stringResource(R.string.mask_brush), Icons.Rounded.Brush, !state.eraserSelected, contentDescription = stringResource(R.string.a11y_mask_brush_add)) { viewModel.setMaskEraser(false) }
                    ToolToggle(stringResource(R.string.mask_eraser), Icons.Rounded.Delete, state.eraserSelected, contentDescription = stringResource(R.string.a11y_mask_eraser_remove)) { viewModel.setMaskEraser(true) }
                    FilledIconButton(onClick = viewModel::undoMaskStroke, enabled = state.maskStrokes.isNotEmpty()) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.undo))
                    }
                    FilledIconButton(onClick = viewModel::redoMaskStroke, enabled = state.undoneMaskStrokes.isNotEmpty()) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = stringResource(R.string.redo))
                    }
                    FilledIconButton(onClick = viewModel::clearMask, enabled = state.maskStrokes.isNotEmpty()) {
                        Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.clear_mask))
                    }
                }
            }
            if (isSurfaceMask) {
                BrushSizeControl(
                    brushSize = state.brushSize,
                    onBrushSizeChange = viewModel::setBrushSize,
                    showRangeLabels = true,
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(stringResource(R.string.brush_size), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${state.brushSize.toInt()} px",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Slider(value = state.brushSize, onValueChange = viewModel::setBrushSize, valueRange = 8f..72f)
                }
            }
            if (allowAutoDetect) {
                OutlinedButton(onClick = { viewModel.markMaskWithAutoDetect(target) }, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(if (target == "floor") stringResource(R.string.auto_detect_floor) else stringResource(R.string.auto_detect_wall))
                }
            }
        }
    }
}

@Composable
fun SurfaceMaskStatus(
    hasMask: Boolean,
    readyText: String,
    requiredText: String,
) {
    val icon = if (hasMask) Icons.Rounded.Check else Icons.Rounded.Brush
    val color = if (hasMask) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.72f)
    val contentColor = if (hasMask) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color,
        modifier = Modifier.fillMaxWidth().border(1.dp, if (hasMask) MaterialTheme.colorScheme.primary.copy(alpha = 0.32f) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = contentColor)
            Text(
                if (hasMask) readyText else requiredText,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun BrushSizeControl(
    brushSize: Float,
    onBrushSizeChange: (Float) -> Unit,
    showRangeLabels: Boolean,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.brush_size), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                    Text(
                        "${brushSize.toInt()} px",
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .size((10f + (brushSize / 72f) * 24f).dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)),
                    )
                }
                Slider(
                    value = brushSize,
                    onValueChange = onBrushSizeChange,
                    valueRange = 8f..72f,
                    modifier = Modifier.weight(1f),
                )
            }
            if (showRangeLabels) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(stringResource(R.string.precise), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(stringResource(R.string.wide), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun MaskCanvas(
    state: HomeDecorUiState,
    imageDescription: String,
    emptyStateTitle: String? = null,
    emptyStateBody: String? = null,
    hasVisibleMask: Boolean? = null,
    readyLabel: String? = null,
    onStroke: (MaskStroke) -> Unit,
    maskColor: Color = StudioAccent,
    showFloatingNext: Boolean = false,
    onNext: (() -> Unit)? = null,
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var livePoints by remember { mutableStateOf<List<MaskPoint>>(emptyList()) }
    val hasMask = hasVisibleMask ?: state.maskStrokes.any { !it.erase && it.points.size > 1 }
    fun Offset.toMaskPoint(): MaskPoint {
        val width = canvasSize.width.coerceAtLeast(1).toFloat()
        val height = canvasSize.height.coerceAtLeast(1).toFloat()
        return MaskPoint((x / width).coerceIn(0f, 1f), (y / height).coerceIn(0f, 1f))
    }
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1.18f)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.outlineVariant)
            .border(2.dp, if (hasMask) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
            .onSizeChanged { canvasSize = it }
            .pointerInput(state.brushSize, state.eraserSelected) {
                detectDragGestures(
                    onDragStart = { offset -> livePoints = listOf(offset.toMaskPoint()) },
                    onDrag = { change, _ -> livePoints = livePoints + change.position.toMaskPoint() },
                    onDragEnd = {
                        onStroke(MaskStroke(livePoints, state.brushSize, state.eraserSelected))
                        livePoints = emptyList()
                    },
                    onDragCancel = { livePoints = emptyList() },
                )
            }
            .pointerInput(state.brushSize, state.eraserSelected) {
                detectTapGestures { offset ->
                    val point = offset.toMaskPoint()
                    livePoints = listOf(point)
                    onStroke(MaskStroke(livePoints, state.brushSize, state.eraserSelected))
                    livePoints = emptyList()
                }
            },
    ) {
        val firstPhoto = state.selectedPhotos.firstOrNull()
        UriOrResourceImage(
            uri = firstPhoto?.uri,
            imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
            contentDescription = imageDescription,
            modifier = Modifier.fillMaxSize(),
        )
        Canvas(
            Modifier
                .matchParentSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen },
        ) {
            fun drawStroke(stroke: MaskStroke) {
                val points = stroke.points
                if (points.size < 2) return
                points.zipWithNext().forEach { (start, end) ->
                    drawLine(
                        color = if (stroke.erase) Color.Transparent else maskColor.copy(alpha = 0.62f),
                        start = Offset(start.x * size.width, start.y * size.height),
                        end = Offset(end.x * size.width, end.y * size.height),
                        strokeWidth = stroke.brushSize,
                        cap = StrokeCap.Round,
                        blendMode = if (stroke.erase) BlendMode.Clear else BlendMode.SrcOver,
                    )
                }
            }
            state.maskStrokes.forEach(::drawStroke)
            livePoints.takeIf { it.isNotEmpty() }?.let {
                drawStroke(MaskStroke(it, state.brushSize, state.eraserSelected))
            }
        }
        if (hasMask && readyLabel != null) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
            ) {
                Row(
                    Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Text(readyLabel, color = Color.White, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
        if (showFloatingNext && onNext != null) {
            Button(
                onClick = onNext,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .height(48.dp),
            ) {
                Text(
                    stringResource(R.string.continue_action),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.width(HomeDecorSpacing.Xs))
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun SurfacePanel(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    primary: String,
    onPrimary: () -> Unit,
    onMagic: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier.height(146.dp).border(1.dp, studioStateBorder(selected), RoundedCornerShape(24.dp)),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface) {
                    Icon(icon, contentDescription = null, Modifier.padding(8.dp).size(18.dp), tint = MaterialTheme.colorScheme.onSurface)
                }
                Text(title, fontWeight = FontWeight.SemiBold, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onPrimary,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                    ),
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Base),
                    modifier = Modifier.height(48.dp).weight(1f),
                ) {
                    Text(primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = onMagic, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = stringResource(R.string.option_ai_suggestion), tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun LayoutPlanningStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val stepValidation = rememberStepValidation(state)
    val canGenerate = stepValidation.canProceed
    var showValidationBanner by remember { mutableStateOf(false) }
    val isPro = state.isPro
    val diamondCost = if (isPro) stringResource(R.string.pro_upper) else "1"

    LaunchedEffect(canGenerate) {
        if (canGenerate) showValidationBanner = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(start = HomeDecorSpacing.Xl, end = HomeDecorSpacing.Xl, top = HomeDecorSpacing.Sm, bottom = HomeDecorSpacing.WizardBottomContentPadding + HomeDecorSpacing.Lg),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        if (!state.generationError.isNullOrBlank()) {
            item {
                UnifiedWizardError(
                    message = state.generationError,
                    onRetry = viewModel::generate,
                    onDismiss = viewModel::clearGenerationError,
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                Text(stringResource(R.string.planning_goals), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                HomeDecorCatalog.layoutGoals.chunked(3).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                        row.forEach { option ->
                            LayoutGoalChip(
                                label = option,
                                selected = option in state.selectedRooms,
                                onClick = { viewModel.setRoom(option) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                OutlinedTextField(
                    value = state.layoutConstraints,
                    onValueChange = viewModel::setLayoutConstraints,
                    label = { Text(stringResource(R.string.layout_optional_constraints)) },
                    placeholder = { Text(stringResource(R.string.layout_optional_constraints_placeholder)) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                    minLines = 3,
                    shape = RoundedCornerShape(16.dp),
                )
            }
        }
        item {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                if (showValidationBanner && !stepValidation.validationMessage.isNullOrBlank()) {
                    ValidationAlertBanner(message = stepValidation.validationMessage)
                }
                Button(
                    onClick = {
                        showValidationBanner = false
                        viewModel.generate()
                    },
                    enabled = canGenerate,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .disabledSemantics(canGenerate),
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ViewQuilt, contentDescription = null, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(
                        if (isPro) stringResource(R.string.layout_generate) else stringResource(R.string.layout_generate_with_diamond, diamondCost),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
fun LayoutGoalChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val borderWidth = if (selected) 2.dp else 1.dp
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (selected) 0.96f else 1f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow,
        ),
        label = "goalChipScale",
    )
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .height(64.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .semantics {
                this.selected = selected
                contentDescription = displayLabel
            }
            .border(borderWidth, borderColor, RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    choiceIcon(label),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                style = if (displayLabel.length > 12) {
                    MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp, lineHeight = 17.sp)
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun RefineStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val stepValidation = rememberStepValidation(state)
    val isPro = state.isPro
    val diamondCost = if (isPro) stringResource(R.string.pro_upper) else "1"
    
    if (state.selectedTool.id == "layout") {
        StepScaffold(
            eyebrow = "",
            title = stringResource(R.string.add_details_title),
            body = stringResource(R.string.add_details_body),
            buttonLabel = if (isPro) stringResource(R.string.generate) else stringResource(R.string.generate_with_diamond, diamondCost),
            canProceed = stepValidation.canProceed,
            validationMessage = stepValidation.validationMessage,
            onButton = viewModel::generate,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
                OutlinedTextField(value = state.roomType, onValueChange = viewModel::setRoomTypeText, label = { Text(stringResource(R.string.room_type)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
                OutlinedTextField(
                    value = state.customPrompt,
                    onValueChange = viewModel::setCustomPrompt,
                    label = { Text(stringResource(R.string.layout_optional_constraints)) },
                    placeholder = { Text(stringResource(R.string.layout_optional_constraints_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(16.dp),
                )
                AdvancedControls(state = state, viewModel = viewModel)
            }
        }
        return
    }
    val copy = stepFourCopy(state.selectedTool)
    StepScaffold(
        eyebrow = "",
        title = stringResource(copy.titleRes),
        body = stringResource(copy.bodyRes),
        buttonLabel = if (isPro) stringResource(R.string.generate_my_design) else stringResource(R.string.generate_with_diamond, diamondCost),
        buttonIcon = Icons.Rounded.AutoAwesome,
        canProceed = stepValidation.canProceed,
        validationMessage = stepValidation.validationMessage,
        onButton = viewModel::generate,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            // Unified error display replaces inline GenerationErrorNotice
            UnifiedWizardError(
                message = state.generationError.orEmpty(),
                onRetry = viewModel::generate,
                onDismiss = viewModel::clearGenerationError,
            )
            if (state.selectedTool.id !in listOf("facade", "garden", "paint")) {
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(stringResource(R.string.step_design_mode_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(stringResource(R.string.choose_a_mode), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                        HomeDecorCatalog.designModes.forEach { (mode, description) ->
                            ModeCard(
                                title = localizedOption(mode),
                                description = stringResource(designModeDescriptionRes(description)),
                                selected = state.designMode == mode,
                                onClick = { viewModel.setDesignMode(mode) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(stringResource(R.string.step_color_harmony_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(stringResource(R.string.choose_color), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth().height(548.dp),
                    contentPadding = PaddingValues(bottom = HomeDecorSpacing.BottomContentPadding),
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
                    userScrollEnabled = false,
                ) {
                    items(listOf("Suggestion IA") + HomeDecorCatalog.palettes, key = { it }) { palette ->
                        PaletteChoiceCard(
                            label = palette,
                            selected = palette in state.selectedPalettes,
                            onClick = { viewModel.setPalette(palette) },
                        )
                    }
                }
            }
            OutlinedTextField(
                value = state.customPrompt,
                onValueChange = viewModel::setCustomPrompt,
                label = { Text(stringResource(R.string.describe_vision_label)) },
                placeholder = { Text(stringResource(R.string.describe_vision_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(16.dp),
            )
            AdvancedControls(state = state, viewModel = viewModel)
            val briefSpace = if (state.selectedTool.id == "garden") {
                stringResource(R.string.workflow_garden)
            } else {
                state.roomType.takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.space_to_choose)
            }
            val briefStyle = state.style.takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.style_to_choose)
            Surface(shape = RoundedCornerShape(24.dp), color = StudioBlack) {
                Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                    Text(stringResource(R.string.design_brief), color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        stringResource(
                            R.string.design_pair_format,
                            briefSpace,
                            briefStyle,
                        ),
                        color = Color.White.copy(alpha = 0.82f),
                    )
                    Text(localizedOption(state.designMode), color = Color.White.copy(alpha = 0.82f))
                    Text(state.palette.takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.palette_to_choose), color = Color.White.copy(alpha = 0.72f))
                }
            }
        }
    }
}

@Composable
fun CompactFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingIcon = if (selected) {
            {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        } else {
            null
        },
        modifier = modifier.heightIn(min = 48.dp),
    )
}

@Composable
fun ReplacementReadinessSummary(
    hasMask: Boolean,
    hasReplacementPrompt: Boolean,
    replacementPrompt: String,
) {
    val allReady = hasMask && hasReplacementPrompt
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = if (allReady) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.72f),
        modifier = Modifier.fillMaxWidth().border(
            1.dp,
            if (allReady) MaterialTheme.colorScheme.primary.copy(alpha = 0.36f) else MaterialTheme.colorScheme.outlineVariant,
            RoundedCornerShape(24.dp),
        ),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
            ReplacementSummaryLine(
                checked = hasMask,
                text = if (hasMask) {
                    stringResource(R.string.replacement_confirmation_mask_ready)
                } else {
                    stringResource(R.string.replacement_confirmation_need_mask)
                },
            )
            ReplacementSummaryLine(
                checked = hasReplacementPrompt,
                text = if (hasReplacementPrompt) {
                    stringResource(R.string.replacement_confirmation_object_format, replacementPrompt)
                } else {
                    stringResource(R.string.replacement_confirmation_need_replacement)
                },
            )
        }
    }
}

@Composable
fun ReplacementSummaryLine(
    checked: Boolean,
    text: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm), verticalAlignment = Alignment.Top) {
        Surface(shape = CircleShape, color = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface, modifier = Modifier.padding(top = 1.dp)) {
            Icon(
                if (checked) Icons.Rounded.Check else Icons.Rounded.Lock,
                contentDescription = null,
                modifier = Modifier.padding(4.dp).size(13.dp),
                tint = if (checked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(text, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun PaletteChoiceCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxWidth()
            .height(142.dp)
            .semantics {
                this.selected = selected
                contentDescription = displayLabel
            }
            .border(
                if (selected) 2.dp else 1.dp,
                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(16.dp),
            ),
    ) {
        Box {
            Column {
                if (label == "Suggestion IA") {
                    Box(Modifier.fillMaxWidth().height(82.dp).background(MaterialTheme.colorScheme.surfaceContainerLow), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(30.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    Row(Modifier.fillMaxWidth().height(82.dp).clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))) {
                        paletteColors(label).forEach { color ->
                            Box(Modifier.weight(1f).fillMaxSize().background(color))
                        }
                    }
                }
                Text(
                    displayLabel,
                    modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Sm),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (selected) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp).size(12.dp),
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
fun ProcessingStep(
    state: HomeDecorUiState,
    message: String,
) {
    val steps = listOf(
        stringResource(R.string.generation_step_analyzing_room),
        stringResource(R.string.generation_step_applying_style),
        stringResource(R.string.generation_step_preparing_result),
        stringResource(R.string.generation_step_finalizing),
    )
    val applyingMessage = stringResource(R.string.progress_applying_color)
    val finalizingMessage = stringResource(R.string.progress_finalizing_render)
    val reportedStep = when (message) {
        applyingMessage -> 1
        finalizingMessage -> 3
        else -> 0
    }
    var visibleStep by remember(message) { mutableStateOf(reportedStep) }

    LaunchedEffect(message) {
        visibleStep = reportedStep
        if (reportedStep < 1) {
            delay(1600)
            visibleStep = maxOf(visibleStep, 1)
        }
        if (reportedStep < 2) {
            delay(2200)
            visibleStep = maxOf(visibleStep, 2)
        }
    }

    val progress = when (visibleStep.coerceIn(0, 3)) {
        0 -> 0.15f
        1 -> 0.40f
        2 -> 0.70f
        else -> 0.92f
    }
    val heroImage = processingHeroImage(state.selectedTool.id)
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                ) {
                    Image(
                        painter = painterResource(heroImage),
                        contentDescription = stringResource(R.string.a11y_processing_hero),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, HomeDecorExtra.scrim.copy(alpha = 0.55f)))))
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.align(Alignment.TopStart).padding(16.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                        ) {
                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Text(stringResource(R.string.generation_progress_badge), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                    ) {
                        Text(
                            stringResource(R.string.generation_progress_title),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            stringResource(R.string.generation_progress_body),
                            color = Color.White.copy(alpha = 0.84f),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        steps.forEachIndexed { index, label ->
                            GenerationStepDot(
                                label = label,
                                index = index,
                                visibleStep = visibleStep,
                            )
                        }
                    }
                }
                Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
                    ) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            message.ifBlank { stringResource(R.string.processing_transform) },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenerationStepDot(
    label: String,
    index: Int,
    visibleStep: Int,
) {
    val completed = index < visibleStep
    val active = index == visibleStep
    val dotColor = when {
        completed -> MaterialTheme.colorScheme.primary
        active -> StudioGold
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val textColor = when {
        completed -> MaterialTheme.colorScheme.primary
        active -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        modifier = Modifier.width(72.dp),
    ) {
        Surface(
            shape = CircleShape,
            color = dotColor,
            modifier = Modifier.size(10.dp),
        ) {
            if (completed) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.padding(2.dp).size(6.dp),
                    tint = Color.White,
                )
            }
        }
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = if (active || completed) FontWeight.Bold else FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun LayoutResultSummary(state: HomeDecorUiState) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
            Text(stringResource(R.string.layout_changes), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            Text(
                layoutChangeSummary(state),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xxs))
            Text(stringResource(R.string.layout_suggestions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            layoutSuggestions(state).forEach { suggestion ->
                Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Rounded.Check, null, Modifier.padding(top = 2.dp).size(18.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(suggestion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
private fun layoutChangeSummary(state: HomeDecorUiState): String {
    val goals = state.roomType.ifBlank { stringResource(R.string.layout_default_goals) }
    val keep = state.palette.ifBlank { stringResource(R.string.layout_default_keep) }
    val remove = state.mobilierASupprimer.ifBlank { stringResource(R.string.layout_default_remove) }
    val move = state.mobilierADeplacer.ifBlank { stringResource(R.string.layout_default_move) }
    val people = state.style.ifBlank { stringResource(R.string.layout_default_people) }
    val constraints = state.layoutConstraints.ifBlank { stringResource(R.string.layout_default_constraints) }
    return stringResource(R.string.layout_summary_format, goals, keep, remove, move, people, constraints)
}

@Composable
private fun layoutSuggestions(state: HomeDecorUiState): List<String> {
    val suggestions = mutableListOf<String>()
    if (state.layoutConstraints.isNotBlank()) {
        suggestions += stringResource(R.string.layout_suggestion_constraints)
    }
    if (state.palette.isNotBlank()) {
        suggestions += stringResource(R.string.layout_suggestion_keep)
    }
    if (state.mobilierASupprimer.isNotBlank()) {
        suggestions += stringResource(R.string.layout_suggestion_remove)
    }
    if (state.mobilierADeplacer.isNotBlank()) {
        suggestions += stringResource(R.string.layout_suggestion_move)
    }
    if (state.style.isNotBlank()) {
        suggestions += stringResource(R.string.layout_suggestion_people)
    }
    suggestions += stringResource(R.string.layout_suggestion_openings)
    suggestions += stringResource(R.string.layout_suggestion_vertical_storage)
    return suggestions.take(4)
}

@Composable
fun ReplacementResultSummary(
    replacementPrompt: String,
    resultReady: Boolean,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
            Text(
                stringResource(R.string.replacement_result_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                stringResource(R.string.replacement_result_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            ReplacementResultRow(
                label = stringResource(R.string.replacement_result_mask_used),
                value = stringResource(R.string.mask_object_marked),
            )
            ReplacementResultRow(
                label = stringResource(R.string.replacement_result_object),
                value = replacementPrompt.ifBlank { stringResource(R.string.no_custom_prompt) },
            )
            ReplacementResultRow(
                label = stringResource(R.string.replacement_result_status),
                value = stringResource(if (resultReady) R.string.ready else R.string.failed),
            )
        }
    }
}

@Composable
fun ReplacementResultRow(
    label: String,
    value: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md), verticalAlignment = Alignment.Top) {
        Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.padding(top = 2.dp).size(18.dp), tint = MaterialTheme.colorScheme.primary)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun ResultStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val result = state.board.firstOrNull()
    val context = LocalContext.current
    val resources = LocalResources.current
    val scope = rememberCoroutineScope()
    var showOriginal by remember { mutableStateOf(false) }
    var projectPickerVisible by remember { mutableStateOf(false) }
    var createProjectForResultVisible by remember { mutableStateOf(false) }
    val resultReady = result?.let { it.isGeneratedResult() && it.status != "failed" } == true
    val isReplaceResult = state.selectedTool.id == "replace"
    val replacementPrompt = localizedReplacementPrompt(state.customPrompt)
    val savedGenerated = result?.let { current -> state.workspace.generatedResults.firstOrNull { it.id == current.id } }
    val attachedProject = savedGenerated?.projectId?.let { id -> state.workspace.projects.firstOrNull { it.id == id } }
    val isFavorite = result?.let { current -> state.workspace.favorites.any { it.resultId == current.id } } == true
    if (showOriginal) {
        OriginalImageDialog(
            state = state,
            result = result,
            onDismiss = { showOriginal = false },
        )
    }
    if (projectPickerVisible) {
        AddToProjectDialog(
            state = state,
            result = result,
            onDismiss = { projectPickerVisible = false },
            onCreateProject = {
                projectPickerVisible = false
                createProjectForResultVisible = true
            },
            onSelectProject = { project ->
                val saved = viewModel.addResultToProject(result, project.id)
                Toast.makeText(context, resources.getString(if (saved) R.string.toast_added_to_project else R.string.toast_project_save_failed), Toast.LENGTH_LONG).show()
                projectPickerVisible = false
            },
        )
    }
    if (createProjectForResultVisible) {
        ProjectEditorDialog(
            title = stringResource(R.string.project_create_from_result_title),
            confirmLabel = stringResource(R.string.create),
            initialName = result?.roomType?.takeIf { it.isNotBlank() } ?: "",
            initialRoomType = result?.roomType.orEmpty(),
            initialNotes = state.customPrompt,
            initialStyleInfo = listOf(state.style, state.palette, state.designMode).filter { it.isNotBlank() }.joinToString(" - "),
            onDismiss = { createProjectForResultVisible = false },
            onConfirm = { name, roomType, notes, styleInfo ->
                val project = viewModel.createProjectFromResult(name = name, roomType = roomType, notes = notes, styleInfo = styleInfo, result = result)
                Toast.makeText(context, resources.getString(if (project != null) R.string.toast_added_to_project else R.string.toast_project_save_failed), Toast.LENGTH_LONG).show()
                createProjectForResultVisible = false
            },
        )
    }
    StepScaffold(
        eyebrow = stringResource(R.string.result),
        title = stringResource(R.string.your_result),
        body = if (isReplaceResult) stringResource(R.string.result_replace_body) else stringResource(R.string.result_saved_workspace),
        buttonLabel = stringResource(R.string.your_design),
        buttonIcon = Icons.Rounded.Visibility,
        onButton = { viewModel.selectTab(MainTab.MyBoard) },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base)) {
            if (result == null) {
                ResultStateNotice(
                    title = stringResource(R.string.result_empty_title),
                    body = stringResource(R.string.result_empty_body),
                    icon = Icons.Rounded.AutoAwesome,
                )
            } else if (!resultReady) {
                ResultStateNotice(
                    title = stringResource(R.string.result_failed_title),
                    body = result.errorMessage ?: stringResource(R.string.generation_failed_retry),
                    icon = Icons.Rounded.Refresh,
                )
            } else {
                BeforeAfterResultSlider(
                    state = state,
                    result = result,
                )
                if (state.selectedTool.id == "reference") {
                    val referenceImageRes = state.selectedReferenceDiscoverItemId
                        ?.let(::discoverItemById)
                        ?.imageRes
                        ?: R.drawable.tool_reference
                    ResultImageCard(title = stringResource(R.string.reference_image)) {
                        UriOrResourceImage(
                            uri = state.selectedReferenceUri,
                            imageRes = referenceImageRes,
                            contentDescription = stringResource(R.string.reference_image),
                            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        )
                    }
                }
            }
            ResultContentsSummary(
                state = state,
                result = result,
                replacementPrompt = replacementPrompt,
                resultReady = resultReady,
            )
            if (resultReady && state.selectedTool.id == "layout") {
                LayoutResultSummary(state)
            }
            if (resultReady && isReplaceResult) {
                ReplacementResultSummary(
                    replacementPrompt = replacementPrompt,
                    resultReady = true,
                )
            }
            Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))) {
                Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                    Text(stringResource(if (isReplaceResult) R.string.replacement_summary else R.string.metadata), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(stringResource(R.string.metadata_service, localizedWorkflowTitle(state.selectedTool)))
                    if (isReplaceResult) {
                        Text(stringResource(R.string.metadata_replacement_object, replacementPrompt.ifBlank { stringResource(R.string.no_custom_prompt) }))
                        Text(stringResource(R.string.metadata_mask, stringResource(R.string.mask_object_marked)))
                    } else {
                        Text(stringResource(R.string.metadata_style, (state.style.ifBlank { state.palette }).takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.ai_choice)))
                        Text(stringResource(R.string.metadata_prompt, state.customPrompt.ifBlank { stringResource(R.string.no_custom_prompt) }))
                    }
                    Text(stringResource(R.string.metadata_status, stringResource(if (!resultReady) R.string.failed else R.string.ready)))
                    Text(stringResource(R.string.metadata_date, java.text.DateFormat.getDateTimeInstance().format(java.util.Date((result?.createdAt ?: System.currentTimeMillis().toDouble()).toLong()))))
                }
            }
            if (resultReady) {
                Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                    Button(
                        onClick = {
                            val saved = viewModel.saveResultToPortfolio(result)
                            Toast.makeText(context, if (saved) resources.getString(R.string.toast_design_saved) else resources.getString(R.string.toast_design_save_failed), Toast.LENGTH_LONG).show()
                        },
                        enabled = resultReady,
                        shape = CircleShape,
                        modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                    ) {
                        Icon(Icons.Rounded.Save, contentDescription = null, Modifier.size(18.dp))
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(stringResource(R.string.save), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                val saved = saveResultToGallery(context, result)
                                Toast.makeText(context, resources.getString(if (saved) R.string.toast_design_downloaded else R.string.toast_design_save_failed), Toast.LENGTH_LONG).show()
                            }
                        },
                        enabled = resultReady,
                        shape = CircleShape,
                        modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                    ) {
                        Icon(Icons.Rounded.Download, contentDescription = null, Modifier.size(18.dp))
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(stringResource(R.string.download), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                val shared = shareResult(context, result)
                                if (!shared) {
                                    Toast.makeText(context, resources.getString(R.string.toast_share_failed), Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        enabled = resultReady,
                        shape = CircleShape,
                        modifier = Modifier.weight(1f).heightIn(min = 52.dp),
                    ) {
                        Icon(Icons.Rounded.Share, contentDescription = null, Modifier.size(18.dp))
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(stringResource(R.string.share), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
                    }
                }
                OutlinedButton(
                    onClick = viewModel::generate,
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(stringResource(R.string.regenerate), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
                }
                var feedbackState by remember { mutableStateOf<String?>(null) }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    Text(
                        stringResource(R.string.rate_this_result),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                        OutlinedButton(
                            onClick = {
                                feedbackState = "liked"
                                openGooglePlayReview(context)
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (feedbackState == "liked") MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            ),
                            modifier = Modifier.height(44.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Icon(
                                Icons.Rounded.ThumbUp,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (feedbackState == "liked") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.width(HomeDecorSpacing.Sm))
                            Text(stringResource(R.string.like), fontWeight = FontWeight.SemiBold)
                        }
                        OutlinedButton(
                            onClick = { feedbackState = "disliked" },
                            shape = CircleShape,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (feedbackState == "disliked") MaterialTheme.colorScheme.errorContainer else Color.Transparent,
                            ),
                            modifier = Modifier.height(44.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Icon(
                                Icons.Rounded.ThumbDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (feedbackState == "disliked") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.width(HomeDecorSpacing.Sm))
                            Text(stringResource(R.string.dislike), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BeforeAfterResultSlider(
    state: HomeDecorUiState,
    result: BoardItem,
) {
    var comparePosition by remember(result.id) { mutableStateOf(0.5f) }
    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md), modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.before_after_slider), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
        ) {
            WorkspaceImage(
                imageUrl = result.imageUrl,
                imageUri = result.imageUri,
                imageRes = result.imageRes,
                contentDescription = stringResource(R.string.after),
                modifier = Modifier.fillMaxSize(),
            )
            OriginalSourceImage(
                state = state,
                result = result,
                contentDescription = stringResource(R.string.before),
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        clipRect(right = size.width * comparePosition) {
                            this@drawWithContent.drawContent()
                        }
                    },
            )
            val onSurfaceColor = MaterialTheme.colorScheme.onSurface
            Canvas(Modifier.matchParentSize()) {
                val handleX = size.width * comparePosition
                drawLine(
                    color = Color.White,
                    start = Offset(handleX, 0f),
                    end = Offset(handleX, size.height),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawCircle(
                    color = Color.White,
                    radius = 18.dp.toPx(),
                    center = Offset(handleX, size.height / 2f),
                )
                drawLine(
                    color = onSurfaceColor,
                    start = Offset(handleX - 7.dp.toPx(), size.height / 2f),
                    end = Offset(handleX + 7.dp.toPx(), size.height / 2f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ComparisonBadge(stringResource(R.string.before))
                ComparisonBadge(stringResource(R.string.after))
            }
        }
        Slider(
            value = comparePosition,
            onValueChange = { comparePosition = it.coerceIn(0.05f, 0.95f) },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            stringResource(R.string.before_after_slider_hint),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun ComparisonBadge(label: String) {
    Surface(
        shape = CircleShape,
        color = HomeDecorExtra.scrim.copy(alpha = 0.54f),
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun TryAnotherStyleRow(
    selectedStyle: String,
    onStyle: (String) -> Unit,
) {
    val styles = remember { listOf("Japandi", "Luxe", "Moderne", "Minimaliste", "Marocain", "Scandinave") }
    Column(verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md), modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.try_another_style), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
            items(styles) { style ->
                val selected = selectedStyle.split(" + ").any { it.trim() == style }
                FilterChip(
                    selected = selected,
                    onClick = { onStyle(style) },
                    label = { Text(localizedOption(style), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    leadingIcon = if (selected) {
                        { Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else {
                        null
                    },
                )
            }
        }
    }
}

@Composable
fun ResultProjectWorkspaceActions(
    attachedProject: Project?,
    isFavorite: Boolean,
    onProject: () -> Unit,
    onFavorite: () -> Unit,
    onMoodboard: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface) {
                    Icon(Icons.Rounded.Layers, null, Modifier.padding(8.dp).size(20.dp), tint = MaterialTheme.colorScheme.primary)
                }
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.result_workspace_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        attachedProject?.let { stringResource(R.string.result_workspace_project, it.name) }
                            ?: stringResource(R.string.result_workspace_body),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md)) {
                Button(
                    onClick = onProject,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(stringResource(R.string.save_to_project), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(
                    onClick = onFavorite,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(18.dp), tint = if (isFavorite) StudioGold else Color.Unspecified)
                    Spacer(Modifier.width(HomeDecorSpacing.Sm))
                    Text(stringResource(if (isFavorite) R.string.favorited else R.string.favorite), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            OutlinedButton(
                onClick = onMoodboard,
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) {
                Icon(Icons.AutoMirrored.Rounded.ViewQuilt, null, Modifier.size(18.dp))
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(stringResource(R.string.add_to_moodboard), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun ResultContentsSummary(
    state: HomeDecorUiState,
    result: BoardItem?,
    replacementPrompt: String,
    resultReady: Boolean,
) {
    val space = state.roomType.ifBlank { result?.roomType.orEmpty() }
        .takeIf { it.isNotBlank() }
        ?.let { localizedOption(it) }
        ?: stringResource(R.string.ai_choice)
    val finish = (state.style.ifBlank { state.palette.ifBlank { result?.style.orEmpty() } })
        .takeIf { it.isNotBlank() }
        ?.let { localizedOption(it) }
        ?: stringResource(R.string.ai_choice)
    val description = when {
        !resultReady -> stringResource(R.string.result_contains_failed)
        state.selectedTool.id == "layout" -> stringResource(R.string.result_contains_layout, space)
        state.selectedTool.id == "replace" -> stringResource(R.string.result_contains_replace, replacementPrompt.ifBlank { stringResource(R.string.ai_choice) })
        state.selectedTool.id == "reference" -> stringResource(R.string.result_contains_reference, finish)
        state.selectedTool.id == "paint" -> stringResource(R.string.result_contains_paint, finish)
        state.selectedTool.id == "floor" -> stringResource(R.string.result_contains_floor, finish)
        else -> stringResource(R.string.result_contains_default, space, finish)
    }
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(HomeDecorSpacing.Base), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
            Text(stringResource(R.string.result_contains_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            Text(description, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
            if (resultReady) {
                Text(stringResource(R.string.result_saved_to_profile_history), color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ResultStateNotice(
    title: String,
    body: String,
    icon: ImageVector,
) {
    val samples = sampleProjectCards()
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(HomeDecorSpacing.Base).size(28.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun ResultImageCard(
    title: String,
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
            image()
        }
    }
}

private fun processingHeroImage(toolId: String): Int = when (toolId) {
    "facade" -> R.drawable.assets_media_discover_generated_exterior_exterior4
    "garden" -> R.drawable.assets_media_discover_generated_garden_garden5
    "paint" -> R.drawable.assets_media_discover_wallscenes_sagegreensuite
    "floor" -> R.drawable.assets_media_discover_floorscenes_naturaloakparquet
    "layout" -> R.drawable.assets_media_discover_home_homelivingroom
    "replace" -> R.drawable.assets_media_discover_generated_livingroom_livingroom3
    "reference" -> R.drawable.assets_media_styles_stylejapandi
    else -> R.drawable.assets_media_discover_generated_livingroom_livingroom4
}

@Composable
private fun OriginalSourceImage(
    state: HomeDecorUiState,
    result: BoardItem?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    WorkspaceImage(
        imageUrl = result?.sourceImageUrl,
        imageUri = result?.sourceImageUri,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}