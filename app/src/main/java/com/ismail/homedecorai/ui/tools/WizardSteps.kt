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
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PhotoCamera
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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

import com.ismail.homedecorai.BoardItem
import com.ismail.homedecorai.DecorTool

import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.MainTab
import com.ismail.homedecorai.MaskPoint
import com.ismail.homedecorai.MaskStroke
import com.ismail.homedecorai.Project
import com.ismail.homedecorai.R

import com.ismail.homedecorai.WizardStage
import com.ismail.homedecorai.hasVisibleMaskPaint
import com.ismail.homedecorai.isGeneratedResult
import com.ismail.homedecorai.isValidReplacementPrompt
import com.ismail.homedecorai.ui.components.*
import com.ismail.homedecorai.ui.dialogs.*
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*


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
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        DesignStepHeader(
            state = state,
            onBack = viewModel::previousStage,
            onClose = { viewModel.selectTab(MainTab.Tools) },
            onCredits = viewModel::openDiamondStore,
        )
        AnimatedContent(targetState = state.wizardStage, label = "wizard", modifier = Modifier.weight(1f)) { stage ->
            when (stage) {
                WizardStage.Photo -> if (state.selectedTool.id == "reference") {
                    ReferenceImagesStep(state = state, viewModel = viewModel)
                } else {
                    PhotoStep(state = state, viewModel = viewModel)
                }
                WizardStage.Space -> when (state.selectedTool.id) {
                    "floor" -> FloorMaskStep(state = state, viewModel = viewModel)
                    "paint" -> WallSurfaceStep(state = state, viewModel = viewModel)
                    "replace" -> ObjectMaskStep(state = state, viewModel = viewModel)
                    "reference" -> ReferencePhotoStep(state = state, viewModel = viewModel)
                    "layout" -> LayoutPlanningStep(state = state, viewModel = viewModel)
                    else -> {
                        val isGarden = state.selectedTool.id == "garden"
                        ChoiceStep(
                            state = state,
                            eyebrow = stringResource(R.string.step_count_format, 2, wizardTotalSteps(state.selectedTool)),
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
                        eyebrow = stringResource(R.string.step_count_format, 3, wizardTotalSteps(state.selectedTool)),
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
    onCredits: () -> Unit,
) {
    val step = wizardStepNumber(state.wizardStage, state.selectedTool)
    val totalSteps = wizardTotalSteps(state.selectedTool)
    Surface(color = StudioPaper, tonalElevation = 2.dp) {
        Column(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    if (step > 1) {
                        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    } else {
                        CreditPill(state, compact = false, onClick = onCredits)
                    }
                }
                Text(
                    localizedWorkflowTitle(state.selectedTool),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.Center),
                )
                IconButton(onClick = onClose, modifier = Modifier.align(Alignment.CenterEnd).size(48.dp)) {
                    Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close))
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.step_count_format, step, totalSteps),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    repeat(totalSteps) { index ->
                        val active = index < step
                        Box(
                            modifier = Modifier.weight(1f).height(5.dp).clip(CircleShape).background(if (active) StudioBlue else StudioLine)
                        )
                    }
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
    buttonEnabled: Boolean = true,
    validationMessage: String? = null,
    contentBottomPadding: Dp = 18.dp,
    protectBottomInsets: Boolean = false,
    buttonAllowsTwoLines: Boolean = false,
    onButton: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = contentBottomPadding),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                    if (body != null) {
                        Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            item { content() }
        }
        val bottomBarModifier = if (protectBottomInsets) {
            Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        } else {
            Modifier
        }
        Surface(color = StudioPaper, tonalElevation = 3.dp, modifier = bottomBarModifier) {
            val buttonModifier = if (buttonAllowsTwoLines) {
                Modifier.fillMaxWidth().heightIn(min = 58.dp)
            } else {
                Modifier.fillMaxWidth().height(58.dp)
            }
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!buttonEnabled && !validationMessage.isNullOrBlank()) {
                    ValidationNotice(validationMessage)
                }
                Button(
                    onClick = onButton,
                    enabled = buttonEnabled,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = buttonModifier,
                ) {
                    Icon(buttonIcon, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        buttonLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = if (buttonAllowsTwoLines) 2 else 1,
                        overflow = TextOverflow.Ellipsis,
                    )
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
        eyebrow = stringResource(R.string.step_count_format, 1, wizardTotalSteps(state.selectedTool)),
        title = stringResource(R.string.reference_two_images_title),
        body = stringResource(R.string.reference_two_images_body),
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        buttonEnabled = canContinue,
        validationMessage = missingHint,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
            OutlinedButton(
                onClick = viewModel::tryWithExample,
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.try_with_example))
            }
            if (missingHint != null) {
                ReferenceContinueHint(missingHint)
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
    Surface(
        shape = RoundedCornerShape(26.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, studioStateBorder(selected), RoundedCornerShape(26.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = studioStateIconContainer(selected)) {
                    Icon(
                        if (selected) Icons.Rounded.Check else Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.padding(9.dp).size(19.dp),
                        tint = studioStateIconContent(selected),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(
                        if (selected) selectedText else body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(StudioMist),
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
                        Modifier.fillMaxSize().padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Surface(shape = CircleShape, color = StudioPaper) {
                            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.padding(12.dp).size(24.dp), tint = StudioBlue)
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(stringResource(R.string.reference_empty_preview_title), fontWeight = FontWeight.Black)
                        Text(
                            missingHint,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onGallery, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.Add, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(7.dp))
                    Text(stringResource(R.string.gallery))
                }
                OutlinedButton(onClick = onCamera, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(7.dp))
                    Text(stringResource(R.string.camera))
                }
            }
            OutlinedButton(onClick = onExample, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.example))
            }
        }
    }
}

@Composable
fun ReferenceContinueHint(message: String) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioErrorContainer,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, StudioRose.copy(alpha = 0.28f), RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(Icons.Rounded.Lock, contentDescription = null, modifier = Modifier.size(18.dp), tint = StudioRose)
            Text(message, color = StudioRose, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SourcePreviewCard(state: HomeDecorUiState) {
    val firstPhoto = state.selectedPhotos.firstOrNull()
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp)),
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(Modifier.size(76.dp).clip(RoundedCornerShape(18.dp)).background(StudioMist)) {
                UriOrResourceImage(
                    uri = firstPhoto?.uri,
                    imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
                    contentDescription = stringResource(R.string.source_photo_preview),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.source_photo_preview), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text(stringResource(R.string.source_photo_preview_body), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioGreen, modifier = Modifier.size(20.dp))
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
        shape = RoundedCornerShape(22.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp), tint = StudioBlue)
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
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
            .clip(RoundedCornerShape(18.dp))
            .background(StudioMist),
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
                            color = if (stroke.erase) Color.Transparent else StudioSky.copy(alpha = 0.62f),
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
        shape = RoundedCornerShape(22.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.reference_preview_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(state.selectedPhotos.size, key = { "selected-photo-$it" }) { index ->
            val slot = state.selectedPhotos[index]
            val removePhotoDescription = stringResource(R.string.remove_photo)
            Box(Modifier.width(72.dp).height(64.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = StudioPaper,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxSize().border(1.dp, if (index == 0) StudioBlue else StudioLine, RoundedCornerShape(16.dp)),
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
                    color = StudioPaper,
                    tonalElevation = 1.dp,
                    modifier = Modifier.width(72.dp).height(64.dp).border(1.dp, StudioLine, RoundedCornerShape(16.dp)),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.add_photo), tint = StudioInk)
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
    StepScaffold(
        eyebrow = eyebrow,
        title = stringResource(copy.titleRes),
        body = stringResource(copy.bodyRes),
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        buttonEnabled = selected.isNotEmpty(),
        validationMessage = stringResource(R.string.validation_choose_option_to_continue),
        onButton = onContinue,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (visualStyleCards || visualBuildingCards) {
            val gridRows = (copy.options.size + 2) / 3
            LazyVerticalGrid(
                columns = if (visualBuildingCards) GridCells.Fixed(2) else GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth().height(((if (visualBuildingCards) (copy.options.size + 1) / 2 else gridRows) * if (visualBuildingCards) 174 else 176).dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false,
            ) {
                items(copy.options, key = { it }) { option ->
                    StyleChoiceCard(
                        label = option,
                        selected = option in selected,
                        onClick = { onSelect(option) },
                        large = visualBuildingCards,
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                copy.options.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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

@Composable
fun PhotoStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val isLayoutTool = state.selectedTool.id == "layout"
    val allowExamplePhotos = true
    val imageInputActions = rememberImageInputActions { uri ->
        viewModel.setPhoto(uri)
    }
    val copy = photoCopy(state.selectedTool)
    val copyTitle = stringResource(copy.titleRes)
    val copyBody = stringResource(copy.bodyRes)
    val hasMainPhoto = state.selectedPhotos.isNotEmpty()
    val canContinue = hasMainPhoto
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, 1, wizardTotalSteps(state.selectedTool)),
        title = if (!hasMainPhoto) copyTitle else stringResource(R.string.photo_added),
        body = if (!hasMainPhoto) copyBody else null,
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        buttonEnabled = canContinue,
        validationMessage = if (allowExamplePhotos) {
            stringResource(R.string.validation_add_source_photo)
        } else {
            stringResource(R.string.validation_upload_source_photo)
        },
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (!hasMainPhoto) {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = StudioPaper,
                    modifier = Modifier.fillMaxWidth().height(292.dp).border(1.dp, StudioLine, RoundedCornerShape(26.dp)),
                ) {
                    Column(
                        Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(copyTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Spacer(Modifier.height(12.dp))
                        Text(copyBody, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(18.dp))
                        Button(
                            onClick = imageInputActions.openGallery,
                            shape = CircleShape,
                            colors = studioPrimaryButtonColors(),
                        ) {
                            Text(if (isLayoutTool) stringResource(R.string.import_photo) else stringResource(R.string.import_plus))
                        }
                    }
                }
            } else {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Box(Modifier.fillMaxWidth().aspectRatio(1.18f)) {
                        val firstPhoto = state.selectedPhotos.first()
                        UriOrResourceImage(
                            uri = firstPhoto.uri,
                            imageRes = selectedPhotoImageRes(state, firstPhoto),
                            contentDescription = stringResource(R.string.photo_added),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                SelectedPhotoStrip(
                    state = state,
                    onAdd = imageInputActions.openGallery,
                    onRemove = viewModel::removePhoto,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = imageInputActions.openGallery, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.gallery))
                }
                OutlinedButton(
                    onClick = imageInputActions.openCamera,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.camera))
                }
            }
            if (allowExamplePhotos) {
                OutlinedButton(
                    onClick = {
                        val example = examplesForTool(state.selectedTool).first().label
                        viewModel.selectExamplePhoto(example)
                    },
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                ) {
                    Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.try_with_example))
                }
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(stringResource(R.string.example_photos), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(examplesForTool(state.selectedTool), key = { "example-${it.label}" }) { photo ->
                            ExamplePhotoCard(
                                photo = photo,
                                selected = state.selectedPhotos.any { it.exampleLabel == photo.label },
                                onClick = {
                                    viewModel.selectExamplePhoto(photo.label)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MaterialLibrarySection(
    options: List<String>,
    selected: List<String>,
    target: String,
    onSelect: (String) -> Unit,
) {
    val selectedMaterial = selected.firstOrNull()
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(R.string.material_library), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Text(
                stringResource(if (target == "floor") R.string.material_library_floor_scope else R.string.material_library_wall_scope),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
        }
        selectedMaterial?.let {
            SelectedMaterialPreview(
                label = it,
                target = target,
            )
        }
        options.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
    target: String,
) {
    val displayLabel = localizedOption(label)
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioPrimaryContainer,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioBlue.copy(alpha = 0.26f), RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MaterialSwatchThumb(label = label, selected = true, modifier = Modifier.size(52.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(displayLabel, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioInk)
                Text(
                    stringResource(if (target == "floor") R.string.material_selected_floor_preview else R.string.material_selected_wall_preview),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioBlue, modifier = Modifier.size(20.dp))
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
    val shape = RoundedCornerShape(18.dp)
    Surface(
        onClick = onClick,
        shape = shape,
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier.height(104.dp).border(if (selected) 2.dp else 1.dp, studioStateBorder(selected), shape),
    ) {
        Column(
            Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MaterialSwatchThumb(label = label, selected = selected, modifier = Modifier.fillMaxWidth().height(48.dp))
            Text(
                displayLabel,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = StudioInk,
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
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.linearGradient(listOf(spec.base, spec.base.copy(alpha = 0.82f), spec.accent.copy(alpha = 0.5f))))
            .border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(14.dp)),
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
            Surface(shape = CircleShape, color = Color.White) {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.padding(5.dp).size(15.dp), tint = StudioBlue)
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
    val requiresMask = state.selectedTool.id in setOf("paint", "floor", "replace")
    val replacementPrompt = state.customPrompt.trim()
    val hasReplacementPrompt = replacementPrompt.isValidReplacementPrompt()
    val localizedReplacement = localizedReplacementPrompt(replacementPrompt)
    val hasRequiredMask = remember(state.maskStrokes, state.selectedTool.id) {
        if (state.selectedTool.id in setOf("paint", "floor", "replace")) {
            state.maskStrokes.hasVisibleMaskPaint()
        } else {
            state.maskStrokes.any { !it.erase && it.points.size > 1 }
        }
    }
    val hasReferenceImages = state.selectedTool.id != "reference" ||
        (state.selectedPhotos.firstOrNull() != null &&
            (state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null))
    val isPaintOrFloor = state.selectedTool.id in setOf("paint", "floor")
    val canGenerate = when (state.selectedTool.id) {
        "replace" -> hasRequiredMask && hasReplacementPrompt
        "reference" -> selected.isNotEmpty() && hasReferenceImages
        else -> (selected.isNotEmpty() || state.customPrompt.isNotBlank()) && (!requiresMask || hasRequiredMask)
    }
    val disabledReason = when {
        requiresMask && !hasRequiredMask -> when (state.selectedTool.id) {
            "floor" -> stringResource(R.string.validation_mark_floor_to_generate)
            "replace" -> stringResource(R.string.validation_mark_object_to_generate)
            else -> stringResource(R.string.validation_mark_wall_to_generate)
        }
        state.selectedTool.id == "reference" && !hasReferenceImages -> stringResource(R.string.reference_missing_error)
        state.selectedTool.id == "reference" && selected.isEmpty() -> stringResource(R.string.validation_choose_transfer_strength)
        state.selectedTool.id == "replace" && !hasReplacementPrompt -> stringResource(R.string.replacement_prompt_required_error)
        state.selectedTool.id == "paint" && selected.isEmpty() && state.customPrompt.isBlank() -> stringResource(R.string.validation_choose_color_or_prompt)
        state.selectedTool.id == "floor" && selected.isEmpty() && state.customPrompt.isBlank() -> stringResource(R.string.validation_choose_material_or_prompt)
        else -> null
    }
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, wizardStepNumber(state.wizardStage, state.selectedTool), wizardTotalSteps(state.selectedTool)),
        title = stepTitle,
        body = stepBody,
        buttonLabel = if (requiresMask && !hasRequiredMask) {
            when (state.selectedTool.id) {
                "floor" -> stringResource(R.string.paint_mask_before_generate_floor)
                "replace" -> stringResource(R.string.paint_mask_before_generate_replace)
                else -> stringResource(R.string.paint_mask_before_generate_wall)
            }
        } else if (state.selectedTool.id == "reference" && !hasReferenceImages) {
            stringResource(R.string.add_both_images)
        } else if (state.selectedTool.id == "replace" && !hasReplacementPrompt) {
            stringResource(R.string.choose_replacement_before_generate)
        } else {
            stringResource(R.string.generate)
        },
        buttonEnabled = canGenerate,
        validationMessage = disabledReason,
        contentBottomPadding = if (isPaintOrFloor) 32.dp else 18.dp,
        protectBottomInsets = isPaintOrFloor,
        buttonAllowsTwoLines = isPaintOrFloor,
        onButton = viewModel::generate,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (!state.generationError.isNullOrBlank()) {
                GenerationErrorNotice(
                    message = state.generationError,
                    onRetry = viewModel::generate,
                )
            }
            if (state.selectedTool.id == "reference") {
                ReferenceStylePreview(state = state)
            } else {
                SourcePreviewCard(state = state)
            }
            if (state.selectedTool.id in setOf("paint", "floor")) {
                val maskLabel = if (state.selectedTool.id == "floor") {
                    stringResource(R.string.mask_floor_marked)
                } else {
                    stringResource(R.string.mask_wall_marked)
                }
                MaskPreviewCard(
                    state = state,
                    title = stringResource(R.string.mask_preview_title),
                    body = stringResource(R.string.mask_preview_body),
                )
                SurfaceMaskStatus(
                    hasMask = hasRequiredMask,
                    readyText = stringResource(R.string.mask_ready, maskLabel),
                    requiredText = if (state.selectedTool.id == "floor") {
                        stringResource(R.string.mask_required_floor)
                    } else {
                        stringResource(R.string.mask_required_wall)
                    },
                )
            }
            if (state.selectedTool.id == "replace") {
                MaskPreviewCard(
                    state = state,
                    title = stringResource(R.string.mask_preview_title),
                    body = stringResource(R.string.mask_preview_body),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (state.selectedTool.id in setOf("paint", "floor")) {
                    MaterialLibrarySection(
                        options = stepCopy.options,
                        selected = selected,
                        target = if (state.selectedTool.id == "floor") "floor" else "wall",
                        onSelect = viewModel::setStyle,
                    )
                } else {
                if (state.selectedTool.id == "replace") {
                    Text(stringResource(R.string.replacement_suggestions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                }
                    stepCopy.options.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { option ->
                                if (state.selectedTool.id == "replace") {
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
                                } else {
                                    ExpressiveChoiceChip(
                                        label = option,
                                        selected = option in selected,
                                        onClick = {
                                            viewModel.setStyle(option)
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
            if (state.selectedTool.id == "reference") {
                Text(stringResource(R.string.transfer_options), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                HomeDecorCatalog.referenceOptions.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { option ->
                            ExpressiveChoiceChip(
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
            OutlinedTextField(
                value = state.customPrompt,
                onValueChange = viewModel::setCustomPrompt,
                label = when (state.selectedTool.id) {
                    "replace" -> {
                        { Text(stringResource(R.string.replacement_object)) }
                    }
                    "reference" -> {
                        { Text(stringResource(R.string.custom_notes)) }
                    }
                    else -> null
                },
                placeholder = {
                    Text(
                        when (state.selectedTool.id) {
                            "paint" -> stringResource(R.string.prompt_paint)
                            "floor" -> stringResource(R.string.prompt_floor)
                            "replace" -> stringResource(R.string.prompt_replace)
                            "reference" -> stringResource(R.string.custom_notes_placeholder)
                            else -> stringResource(R.string.prompt_optional)
                        },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = if (state.selectedTool.id in setOf("replace", "reference")) 4 else 3,
                shape = RoundedCornerShape(18.dp),
                isError = state.selectedTool.id == "replace" && state.customPrompt.isNotBlank() && !hasReplacementPrompt,
                supportingText = if (state.selectedTool.id == "replace") {
                    {
                        Text(
                            if (state.customPrompt.isBlank() || hasReplacementPrompt) {
                                stringResource(R.string.describe_new_object)
                            } else {
                                stringResource(R.string.replacement_prompt_required_error)
                            },
                        )
                    }
                } else {
                    null
                },
            )
            if (state.selectedTool.id == "replace") {
                ReplacementReadinessSummary(
                    hasMask = hasRequiredMask,
                    hasReplacementPrompt = hasReplacementPrompt,
                    replacementPrompt = localizedReplacement,
                )
            }
            AdvancedControls(state = state, viewModel = viewModel)
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
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(20.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(Icons.Rounded.Settings, contentDescription = null, tint = StudioBlue, modifier = Modifier.size(20.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(stringResource(R.string.advanced_controls), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
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
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.budget_mode), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.avoid_these), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        HomeDecorCatalog.avoidOptions.chunked(2).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(stringResource(R.string.recent_styles), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.try_with_example), fontWeight = FontWeight.Bold)
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        options.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        eyebrow = stringResource(R.string.step_count_format, 2, wizardTotalSteps(state.selectedTool)),
        title = stringResource(R.string.step_add_reference_title),
        body = stringResource(R.string.step_add_reference_body),
        buttonLabel = stringResource(R.string.continue_action),
        buttonEnabled = hasReference,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            ReferenceImagePicker(
                selectedUri = state.selectedReferenceUri,
                selectedExample = state.selectedReferenceExampleLabel,
                selectedImageRes = state.selectedReferenceDiscoverItemId?.let(::discoverItemById)?.imageRes ?: R.drawable.tool_reference,
                onImport = referenceImageInputActions.openGallery,
                onExample = { viewModel.selectReferenceExample(editorialReference) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = referenceImageInputActions.openGallery, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.Add, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.gallery))
                }
                OutlinedButton(
                    onClick = referenceImageInputActions.openCamera,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.camera))
                }
            }
            OutlinedButton(
                onClick = { viewModel.selectReferenceExample(editorialReference) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
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
        emptyStateTitle = stringResource(R.string.mask_object_empty_title),
        emptyStateBody = stringResource(R.string.mask_object_empty_body),
        polishedControls = true,
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
) {
    val requiresVisibleMask = target in setOf("floor", "wall", "object")
    val isSurfaceMask = target in setOf("floor", "wall")
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
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, 2, wizardTotalSteps(state.selectedTool)),
        title = title,
        body = body,
        buttonLabel = if (hasMask) stringResource(R.string.continue_action) else disabledLabel,
        buttonEnabled = hasMask,
        validationMessage = disabledLabel,
        contentBottomPadding = if (isSurfaceMask) 32.dp else 18.dp,
        protectBottomInsets = isSurfaceMask,
        buttonAllowsTwoLines = isSurfaceMask,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            MaskCanvas(
                state = state,
                imageDescription = imageDescription,
                emptyStateTitle = emptyStateTitle,
                emptyStateBody = emptyStateBody,
                hasVisibleMask = hasMask,
                readyLabel = if (isSurfaceMask) stringResource(R.string.mask_ready, surfaceLabel) else null,
                onStroke = viewModel::addMaskStroke,
            )
            if (polishedControls && isSurfaceMask) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SurfaceMaskStatus(
                        hasMask = hasMask,
                        readyText = stringResource(R.string.mask_ready, surfaceLabel),
                        requiredText = surfaceGuidance,
                    )
                    BoxWithConstraints(Modifier.fillMaxWidth()) {
                        val compactControls = maxWidth < 360.dp
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(stringResource(R.string.brush_size), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "${state.brushSize.toInt()} px",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Slider(value = state.brushSize, onValueChange = viewModel::setBrushSize, valueRange = 8f..72f)
                }
            }
            if (allowAutoDetect) {
                OutlinedButton(onClick = { viewModel.markMaskWithAutoDetect(target) }, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
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
    val color = if (hasMask) StudioPrimaryContainer else StudioMist.copy(alpha = 0.72f)
    val contentColor = if (hasMask) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color,
        modifier = Modifier.fillMaxWidth().border(1.dp, if (hasMask) StudioBlue.copy(alpha = 0.32f) else StudioLine, RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
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
        shape = RoundedCornerShape(18.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(18.dp)),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.brush_size), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                    Text(
                        "${brushSize.toInt()} px",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = StudioBlue,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .size((10f + (brushSize / 72f) * 24f).dp)
                            .clip(CircleShape)
                            .background(StudioBlue.copy(alpha = 0.7f)),
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
            .clip(RoundedCornerShape(22.dp))
            .background(StudioLine)
            .border(2.dp, if (hasMask) StudioBlue else StudioLine, RoundedCornerShape(22.dp))
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
                        color = if (stroke.erase) Color.Transparent else StudioSky.copy(alpha = 0.62f),
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
                color = StudioBlue,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Text(readyLabel, color = Color.White, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
        if (!hasMask && emptyStateTitle != null) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = StudioBlack.copy(alpha = 0.78f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(14.dp)
                    .fillMaxWidth(),
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(emptyStateTitle, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    if (!emptyStateBody.isNullOrBlank()) {
                        Text(emptyStateBody, color = Color.White.copy(alpha = 0.82f), style = MaterialTheme.typography.bodyMedium)
                    }
                }
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
        shape = RoundedCornerShape(22.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier.height(146.dp).border(1.dp, studioStateBorder(selected), RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(shape = CircleShape, color = StudioPaper) {
                    Icon(icon, null, Modifier.padding(8.dp).size(18.dp), tint = StudioInk)
                }
                Text(title, fontWeight = FontWeight.Black, color = if (selected) StudioBlue else StudioInk)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onPrimary,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) StudioBlue else StudioPaper,
                        contentColor = if (selected) Color.White else StudioInk,
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier.height(48.dp).weight(1f),
                ) {
                    Text(primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = onMagic, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = stringResource(R.string.option_ai_suggestion), tint = StudioBlue)
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
    val firstPhoto = state.selectedPhotos.firstOrNull()
    val hasPlanningGoal = state.selectedRooms.isNotEmpty()
    val canGenerate = firstPhoto != null && hasPlanningGoal
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.layout_plan_title), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                Text(stringResource(R.string.layout_plan_body), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (!state.generationError.isNullOrBlank()) {
            item {
                GenerationErrorNotice(
                    message = state.generationError,
                    onRetry = viewModel::generate,
                )
            }
        }
        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = StudioPaper,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(24.dp)),
            ) {
                Row(
                    Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(Modifier.size(86.dp).clip(RoundedCornerShape(18.dp))) {
                        UriOrResourceImage(
                            uri = firstPhoto?.uri,
                            imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
                            contentDescription = stringResource(R.string.room_photo),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(stringResource(R.string.room_photo), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                        Text(stringResource(R.string.room_photo_layout_body), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioGreen, modifier = Modifier.size(22.dp))
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(stringResource(R.string.planning_goals), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                HomeDecorCatalog.layoutGoals.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { option ->
                            LayoutGoalChip(
                                label = option,
                                selected = option in state.selectedRooms,
                                onClick = { viewModel.setRoom(option) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
        item {
            LayoutConstraintFields(state = state, viewModel = viewModel)
        }
        item {
            AdvancedControls(state = state, viewModel = viewModel)
        }
        item {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!canGenerate) {
                    ValidationNotice(
                        if (firstPhoto == null) {
                            stringResource(R.string.validation_add_source_photo)
                        } else {
                            stringResource(R.string.validation_select_layout_goal)
                        },
                    )
                }
                Button(
                    onClick = viewModel::generate,
                    enabled = canGenerate,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ViewQuilt, contentDescription = null, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (hasPlanningGoal) stringResource(R.string.layout_generate) else stringResource(R.string.layout_select_goal_to_generate),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
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
    val container = if (selected) StudioBlue else StudioPaper
    val content = if (selected) Color.White else StudioInk
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = container,
        tonalElevation = if (selected) 4.dp else 1.dp,
        modifier = modifier.height(82.dp).border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (selected) Color.White.copy(alpha = 0.18f) else StudioMist),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (selected) Icons.Rounded.Check else choiceIcon(label),
                    contentDescription = null,
                    modifier = Modifier.size(if (selected) 18.dp else 19.dp),
                    tint = if (selected) Color.White else StudioBlue,
                )
            }
            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                color = content,
                style = if (displayLabel.length > 12) {
                    MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp, lineHeight = 18.sp)
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun LayoutConstraintFields(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.constraints), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            OutlinedTextField(
                value = state.layoutConstraints,
                onValueChange = viewModel::setLayoutConstraints,
                placeholder = { Text(stringResource(R.string.constraints_placeholder)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 96.dp),
                minLines = 2,
                shape = RoundedCornerShape(18.dp),
            )
        }
        OutlinedTextField(
            value = state.palette,
            onValueChange = viewModel::setPaletteText,
            label = { Text(stringResource(R.string.furniture_to_keep)) },
            placeholder = { Text(stringResource(R.string.furniture_to_keep_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.mobilierASupprimer,
            onValueChange = viewModel::setMobilierASupprimerText,
            label = { Text(stringResource(R.string.furniture_to_remove)) },
            placeholder = { Text(stringResource(R.string.furniture_to_remove_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.mobilierADeplacer,
            onValueChange = viewModel::setMobilierADeplacerText,
            label = { Text(stringResource(R.string.furniture_to_move)) },
            placeholder = { Text(stringResource(R.string.furniture_to_move_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.style,
            onValueChange = viewModel::setStyleText,
            label = { Text(stringResource(R.string.people_count)) },
            placeholder = { Text(stringResource(R.string.people_count_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.customPrompt,
            onValueChange = viewModel::setCustomPrompt,
            label = { Text(stringResource(R.string.optional_notes)) },
            placeholder = { Text(stringResource(R.string.optional_notes_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp),
            minLines = 3,
            shape = RoundedCornerShape(18.dp),
        )
    }
}

@Composable
fun RefineStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    if (state.selectedTool.id == "layout") {
        StepScaffold(
            eyebrow = stringResource(R.string.step_count_format, 3, wizardTotalSteps(state.selectedTool)),
            title = stringResource(R.string.add_details_title),
            body = stringResource(R.string.add_details_body),
            buttonLabel = stringResource(R.string.generate),
            buttonEnabled = state.selectedRooms.isNotEmpty(),
            validationMessage = stringResource(R.string.validation_select_layout_goal),
            onButton = viewModel::generate,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(value = state.roomType, onValueChange = viewModel::setRoomTypeText, label = { Text(stringResource(R.string.room_type)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.style, onValueChange = viewModel::setStyleText, label = { Text(stringResource(R.string.people_count)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.palette, onValueChange = viewModel::setPaletteText, label = { Text(stringResource(R.string.furniture_to_keep)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.mobilierASupprimer, onValueChange = viewModel::setMobilierASupprimerText, label = { Text(stringResource(R.string.furniture_to_remove)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.mobilierADeplacer, onValueChange = viewModel::setMobilierADeplacerText, label = { Text(stringResource(R.string.furniture_to_move)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(
                    value = state.customPrompt,
                    onValueChange = viewModel::setCustomPrompt,
                    label = { Text(stringResource(R.string.important_constraints)) },
                    placeholder = { Text(stringResource(R.string.important_constraints_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(18.dp),
                )
                AdvancedControls(state = state, viewModel = viewModel)
            }
        }
        return
    }
    val copy = stepFourCopy(state.selectedTool)
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, wizardStepNumber(state.wizardStage, state.selectedTool), wizardTotalSteps(state.selectedTool)),
        title = stringResource(copy.titleRes),
        body = stringResource(copy.bodyRes),
        buttonLabel = stringResource(R.string.generate_my_design),
        buttonIcon = Icons.Rounded.AutoAwesome,
        buttonEnabled = state.selectedPalettes.isNotEmpty(),
        validationMessage = stringResource(R.string.validation_choose_palette_to_generate),
        onButton = viewModel::generate,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (!state.generationError.isNullOrBlank()) {
                GenerationErrorNotice(
                    message = state.generationError,
                    onRetry = viewModel::generate,
                )
            }
            if (state.selectedTool.id !in listOf("facade", "garden", "paint")) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.step_design_mode_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(stringResource(R.string.step_color_harmony_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth().height(548.dp),
                    horizontalArrangement = Arrangement.spacedBy(34.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
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
                shape = RoundedCornerShape(18.dp),
            )
            AdvancedControls(state = state, viewModel = viewModel)
            val briefSpace = if (state.selectedTool.id == "garden") {
                stringResource(R.string.workflow_garden)
            } else {
                state.roomType.takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.space_to_choose)
            }
            val briefStyle = state.style.takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.style_to_choose)
            Surface(shape = RoundedCornerShape(22.dp), color = StudioBlack) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.design_brief), color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
        modifier = modifier.heightIn(min = 44.dp),
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
        shape = RoundedCornerShape(22.dp),
        color = if (allReady) StudioPrimaryContainer else StudioMist.copy(alpha = 0.72f),
        modifier = Modifier.fillMaxWidth().border(
            1.dp,
            if (allReady) StudioBlue.copy(alpha = 0.36f) else StudioLine,
            RoundedCornerShape(22.dp),
        ),
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
        Surface(shape = CircleShape, color = if (checked) StudioBlue else StudioPaper, modifier = Modifier.padding(top = 1.dp)) {
            Icon(
                if (checked) Icons.Rounded.Check else Icons.Rounded.Lock,
                contentDescription = null,
                modifier = Modifier.padding(4.dp).size(13.dp),
                tint = if (checked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(text, color = StudioInk.copy(alpha = 0.78f), style = MaterialTheme.typography.bodyMedium)
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
        shape = RoundedCornerShape(18.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier
            .width(96.dp)
            .height(142.dp)
            .border(1.dp, studioStateBorder(selected), RoundedCornerShape(18.dp)),
    ) {
        Column {
            if (label == "Suggestion IA") {
                Box(Modifier.fillMaxWidth().height(82.dp).background(StudioMist), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(30.dp), tint = StudioBlue)
                }
            } else {
                Row(Modifier.fillMaxWidth().height(82.dp).clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))) {
                    paletteColors(label).forEach { color ->
                        Box(Modifier.weight(1f).fillMaxSize().background(color))
                    }
                }
            }
            Text(
                displayLabel,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
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
        0 -> 0.26f
        1 -> 0.52f
        2 -> 0.78f
        else -> 0.94f
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
            shape = RoundedCornerShape(30.dp),
            color = StudioPaper,
            tonalElevation = 3.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(178.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(StudioMist),
                ) {
                    Image(
                        painter = painterResource(heroImage),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.58f)))))
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.align(Alignment.TopStart).padding(14.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                        ) {
                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(17.dp), tint = StudioBlue)
                            Text(stringResource(R.string.generation_progress_badge), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = StudioInk)
                        }
                    }
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            stringResource(R.string.generation_progress_title),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                        )
                        Text(
                            stringResource(R.string.generation_progress_body),
                            color = Color.White.copy(alpha = 0.84f),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(9.dp).clip(CircleShape),
                    )
                    steps.forEachIndexed { index, label ->
                        GenerationProgressRow(
                            label = label,
                            index = index,
                            visibleStep = visibleStep,
                        )
                    }
                }
                Surface(shape = RoundedCornerShape(18.dp), color = StudioMist, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        message.ifBlank { stringResource(R.string.processing_transform) },
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
fun GenerationProgressRow(
    label: String,
    index: Int,
    visibleStep: Int,
) {
    val completed = index < visibleStep
    val active = index == visibleStep
    val container = when {
        completed -> StudioPrimaryContainer
        active -> StudioProContainer
        else -> StudioMist
    }
    val content = when {
        completed -> StudioBlue
        active -> StudioGold
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Surface(shape = CircleShape, color = container) {
            Icon(
                if (completed) Icons.Rounded.Check else Icons.Rounded.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.padding(7.dp).size(16.dp),
                tint = content,
            )
        }
        Text(
            label,
            modifier = Modifier.weight(1f),
            color = if (active || completed) StudioInk else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (active || completed) FontWeight.Black else FontWeight.Medium,
        )
    }
}

@Composable
fun LayoutResultSummary(state: HomeDecorUiState) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPrimaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBlue),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.layout_changes), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioBlue)
            Text(
                layoutChangeSummary(state),
                style = MaterialTheme.typography.bodyLarge,
                color = StudioInk,
            )
            Spacer(Modifier.height(2.dp))
            Text(stringResource(R.string.layout_suggestions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioBlue)
            layoutSuggestions(state).forEach { suggestion ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Rounded.Check, null, Modifier.padding(top = 2.dp).size(18.dp), tint = StudioBlue)
                    Text(suggestion, style = MaterialTheme.typography.bodyMedium, color = StudioInk)
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
        color = StudioPrimaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBlue.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                stringResource(R.string.replacement_result_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = StudioBlue,
            )
            Text(
                stringResource(R.string.replacement_result_body),
                style = MaterialTheme.typography.bodyMedium,
                color = StudioInk,
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
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
        Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.padding(top = 2.dp).size(18.dp), tint = StudioBlue)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = StudioBlue, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = StudioInk)
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
        buttonLabel = stringResource(R.string.new_creation),
        buttonIcon = Icons.Rounded.Check,
        onButton = { viewModel.startTool(state.selectedTool) },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
            Surface(shape = RoundedCornerShape(22.dp), color = StudioPaper, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp))) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(stringResource(if (isReplaceResult) R.string.replacement_summary else R.string.metadata), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
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
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.save), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold)
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
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.download), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold)
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
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.share), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold)
                    }
                }
                OutlinedButton(
                    onClick = viewModel::generate,
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.regenerate), maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Bold)
                }
                var feedbackState by remember { mutableStateOf<String?>(null) }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        stringResource(R.string.rate_this_result),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = {
                                feedbackState = "liked"
                                openGooglePlayReview(context)
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (feedbackState == "liked") StudioPrimaryContainer else Color.Transparent,
                            ),
                            modifier = Modifier.height(44.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Icon(
                                Icons.Rounded.ThumbUp,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (feedbackState == "liked") StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.like), fontWeight = FontWeight.SemiBold)
                        }
                        OutlinedButton(
                            onClick = { feedbackState = "disliked" },
                            shape = CircleShape,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (feedbackState == "disliked") StudioErrorContainer else Color.Transparent,
                            ),
                            modifier = Modifier.height(44.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Icon(
                                Icons.Rounded.ThumbDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (feedbackState == "disliked") StudioRose else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.width(6.dp))
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
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.before_after_slider), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(28.dp))
                .background(StudioMist),
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
                    color = StudioInk,
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
        color = Color.Black.copy(alpha = 0.54f),
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun TryAnotherStyleRow(
    selectedStyle: String,
    onStyle: (String) -> Unit,
) {
    val styles = remember { listOf("Japandi", "Luxe", "Moderne", "Minimaliste", "Marocain", "Scandinave") }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.try_another_style), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        color = StudioPrimaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBlue.copy(alpha = 0.28f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = StudioPaper) {
                    Icon(Icons.Rounded.Layers, null, Modifier.padding(10.dp).size(20.dp), tint = StudioBlue)
                }
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.result_workspace_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioInk)
                    Text(
                        attachedProject?.let { stringResource(R.string.result_workspace_project, it.name) }
                            ?: stringResource(R.string.result_workspace_body),
                        color = StudioInk.copy(alpha = 0.78f),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onProject,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.save_to_project), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(
                    onClick = onFavorite,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(18.dp), tint = if (isFavorite) StudioGold else Color.Unspecified)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(if (isFavorite) R.string.favorited else R.string.favorite), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            OutlinedButton(
                onClick = onMoodboard,
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) {
                Icon(Icons.AutoMirrored.Rounded.ViewQuilt, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
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
        shape = RoundedCornerShape(22.dp),
        color = StudioPrimaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBlue.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.result_contains_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioBlue)
            Text(description, color = StudioInk, style = MaterialTheme.typography.bodyMedium)
            if (resultReady) {
                Text(stringResource(R.string.result_saved_to_profile_history), color = StudioInk, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
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
        color = StudioPaper,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(16.dp).size(28.dp), tint = StudioBlue)
            }
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
        Card(shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
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