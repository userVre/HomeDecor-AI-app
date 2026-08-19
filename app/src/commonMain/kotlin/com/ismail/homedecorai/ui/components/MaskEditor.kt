package com.ismail.homedecorai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.Redo
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.theme.HomeDecorIconSize
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing

// ---------------------------------------------------------------------------
// Data model
// ---------------------------------------------------------------------------

data class MaskStroke(
    val points: List<Offset> = emptyList(),
    val brushSize: Float = 24f,
    val erase: Boolean = false,
)

enum class MaskTool { Brush, Eraser }

// ---------------------------------------------------------------------------
// MaskEditorState
// ---------------------------------------------------------------------------

class MaskEditorState(
    initialBrushSize: Float = 24f,
) {
    var strokes = mutableStateListOf<MaskStroke>()
        private set

    var livePoints by mutableStateOf<List<Offset>>(emptyList())

    var selectedTool by mutableStateOf(MaskTool.Brush)
        private set

    var brushSize by mutableFloatStateOf(initialBrushSize)
        private set

    var canvasSize by mutableStateOf(IntSize.Zero)
        private set

    /** Density for converting dp brush size to pixels. */
    var density by mutableFloatStateOf(1f)

    var zoomLevel by mutableFloatStateOf(1f)
        private set

    var panOffset by mutableStateOf(Offset.Zero)

    var cursorPosition by mutableStateOf(Offset.Zero)

    var isCursorInsideCanvas by mutableStateOf(false)

    /** Whether the first-use instruction has been dismissed. */
    var instructionDismissed by mutableStateOf(false)
        private set

    /** Mask opacity (0..1) controlling the translucent mask alpha. */
    var maskOpacity by mutableFloatStateOf(0.5f)
        private set

    private val undoStack = mutableListOf<List<MaskStroke>>()
    private val redoStack = mutableListOf<List<MaskStroke>>()

    val canUndo: Boolean get() = undoStack.isNotEmpty()
    val canRedo: Boolean get() = redoStack.isNotEmpty()

    val hasMask: Boolean get() = strokes.any { !it.erase && it.points.size >= 2 }

    /** True if mask covers >= 1.5% of the image area. */
    val isMaskValid: Boolean get() = coverageRatio >= MIN_COVERAGE_RATIO

    val coverageRatio: Float
        get() {
            if (canvasSize.width <= 0 || canvasSize.height <= 0) return 0f
            val canvasArea = canvasSize.width.toFloat() * canvasSize.height.toFloat()
            if (canvasArea <= 0f) return 0f

            var totalPaintArea = 0f
            var totalEraseArea = 0f

            for (stroke in strokes) {
                val area = estimateStrokeArea(stroke)
                if (stroke.erase) totalEraseArea += area else totalPaintArea += area
            }

            val netArea = (totalPaintArea - totalEraseArea).coerceAtLeast(0f)
            return (netArea / canvasArea).coerceIn(0f, 1f)
        }

    val coveragePercent: String get() = "${(coverageRatio * 100).toInt()}%"

    // -- Public actions --------------------------------------------------------

    fun setCanvasSize(size: IntSize) {
        canvasSize = size
    }

    fun setDensity(d: Float) {
        density = d
    }

    fun selectTool(tool: MaskTool) {
        selectedTool = tool
    }

    fun setBrushSize(size: Float) {
        brushSize = size.coerceIn(MIN_BRUSH_SIZE, MAX_BRUSH_SIZE)
    }

    fun setMaskOpacity(opacity: Float) {
        maskOpacity = opacity.coerceIn(0.1f, 1f)
    }

    /** Dismiss the first-use instruction overlay. */
    fun dismissInstruction() {
        instructionDismissed = true
    }

    fun setZoom(level: Float) {
        zoomLevel = level.coerceIn(MIN_ZOOM, MAX_ZOOM)
    }

    fun zoomIn() {
        setZoom(zoomLevel + ZOOM_STEP)
    }

    fun zoomOut() {
        setZoom(zoomLevel - ZOOM_STEP)
    }

    fun resetZoom() {
        zoomLevel = 1f
        panOffset = Offset.Zero
    }

    fun updatePan(offset: Offset) {
        panOffset = offset
    }

    fun updateCursor(offset: Offset) {
        cursorPosition = offset
    }

    fun setCursorInside(inside: Boolean) {
        isCursorInsideCanvas = inside
    }

    fun startStroke(point: Offset) {
        livePoints = listOf(point)
    }

    fun continueStroke(point: Offset) {
        livePoints = livePoints + point
    }

    fun commitStroke() {
        if (livePoints.isEmpty()) return
        saveUndo()
        val normalized = livePoints.map { normalizePoint(it) }
        val stroke = MaskStroke(
            points = normalized,
            brushSize = brushSize,
            erase = selectedTool == MaskTool.Eraser,
        )
        strokes = mutableStateListOf<MaskStroke>().apply {
            addAll(strokes)
            add(stroke)
        }
        livePoints = emptyList()
        redoStack.clear()
    }

    fun undo() {
        if (undoStack.isEmpty()) return
        redoStack.add(strokes.toList())
        strokes = mutableStateListOf<MaskStroke>().apply {
            addAll(undoStack.removeLast())
        }
    }

    fun redo() {
        if (redoStack.isEmpty()) return
        undoStack.add(strokes.toList())
        strokes = mutableStateListOf<MaskStroke>().apply {
            addAll(redoStack.removeLast())
        }
    }

    fun clear() {
        if (strokes.isEmpty()) return
        saveUndo()
        strokes = mutableStateListOf()
        redoStack.clear()
    }

    fun loadStrokes(external: List<com.ismail.homedecorai.ui.tools.MaskStroke>) {
        strokes = mutableStateListOf<MaskStroke>().apply {
            addAll(external.map { s ->
                MaskStroke(
                    points = s.points.map { Offset(it.first, it.second) },
                    brushSize = s.brushSize,
                    erase = s.erase,
                )
            })
        }
    }

    fun exportStrokes(): List<com.ismail.homedecorai.ui.tools.MaskStroke> {
        return strokes.map { s ->
            com.ismail.homedecorai.ui.tools.MaskStroke(
                points = s.points.map { Pair(it.x, it.y) },
                brushSize = s.brushSize,
                erase = s.erase,
            )
        }
    }

    // -- Internal -------------------------------------------------------------

    private fun saveUndo() {
        undoStack.add(strokes.toList())
        if (undoStack.size > MAX_UNDO_STACK) undoStack.removeFirst()
    }

    private fun normalizePoint(point: Offset): Offset {
        val w = canvasSize.width.coerceAtLeast(1).toFloat()
        val h = canvasSize.height.coerceAtLeast(1).toFloat()
        return Offset((point.x / w).coerceIn(0f, 1f), (point.y / h).coerceIn(0f, 1f))
    }

    private fun estimateStrokeArea(stroke: MaskStroke): Float {
        if (stroke.points.size < 2) return 0f
        val brushPx = stroke.brushSize * density
        var length = 0f
        for (i in 1 until stroke.points.size) {
            val dx = (stroke.points[i].x - stroke.points[i - 1].x) * canvasSize.width
            val dy = (stroke.points[i].y - stroke.points[i - 1].y) * canvasSize.height
            length += kotlin.math.sqrt(dx * dx + dy * dy)
        }
        return length * brushPx
    }

    companion object {
        const val MIN_BRUSH_SIZE = 16f
        const val MAX_BRUSH_SIZE = 72f
        const val DEFAULT_BRUSH_SIZE = 24f
        const val MAX_UNDO_STACK = 50
        const val MIN_COVERAGE_RATIO = 0.015f
        const val MIN_ZOOM = 0.5f
        const val MAX_ZOOM = 3f
        const val ZOOM_STEP = 0.25f
    }
}

// ---------------------------------------------------------------------------
// MaskEditor — main composable
// ---------------------------------------------------------------------------

@Composable
fun MaskEditor(
    imageContent: @Composable () -> Unit,
    state: MaskEditorState,
    onStrokesChanged: (List<com.ismail.homedecorai.ui.tools.MaskStroke>) -> Unit = {},
    onValidationChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
    imageAspectRatio: Float = 16f / 9f,
) {
    val density = LocalDensity.current

    // Set density on state for dp→px conversion in drawing
    state.setDensity(density.density)

    val maskFill = Color(0xFF0F4C4C).copy(alpha = 0.35f)
    val maskStroke = Color.White

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // -- Combined toolbar: Brush | Eraser | Slider | Undo | Redo | Clear --
        MaskToolbar(
            selectedTool = state.selectedTool,
            onBrushClick = { state.selectTool(MaskTool.Brush) },
            onEraserClick = { state.selectTool(MaskTool.Eraser) },
            brushSize = state.brushSize,
            onBrushSizeChange = { state.setBrushSize(it) },
            canUndo = state.canUndo,
            canRedo = state.canRedo,
            onUndo = {
                state.undo()
                onStrokesChanged(state.exportStrokes())
                onValidationChanged(state.isMaskValid)
            },
            onRedo = {
                state.redo()
                onStrokesChanged(state.exportStrokes())
                onValidationChanged(state.isMaskValid)
            },
            onClear = {
                state.clear()
                onStrokesChanged(state.exportStrokes())
                onValidationChanged(state.isMaskValid)
            },
        )

        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        // -- Instructions (outside image, above canvas) --
        Text(
            text = "Paint over the entire sofa you want to replace. Cover it completely.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base),
        )

        Spacer(Modifier.height(HomeDecorSpacing.Sm))

        // -- Canvas area with zoom --
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(HomeDecorShape.Card)
                .border(
                    2.dp,
                    if (state.hasMask) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant,
                    HomeDecorShape.Card,
                )
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .testTag(Strings.TestTags.wizardMaskCanvas),
        ) {
            // Image + drawing layers with zoom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageAspectRatio)
                    .clip(HomeDecorShape.Card)
                    .graphicsLayer {
                        scaleX = state.zoomLevel
                        scaleY = state.zoomLevel
                        translationX = state.panOffset.x
                        translationY = state.panOffset.y
                    },
            ) {
                imageContent()

                // Drawing layer
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .onSizeChanged { state.setCanvasSize(it) }
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                        .pointerInput(state.selectedTool, state.brushSize) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    state.startStroke(offset)
                                    state.setCursorInside(true)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    state.continueStroke(change.position)
                                    state.updateCursor(change.position)
                                },
                                onDragEnd = {
                                    state.commitStroke()
                                    onStrokesChanged(state.exportStrokes())
                                    onValidationChanged(state.isMaskValid)
                                },
                                onDragCancel = {
                                    state.livePoints = emptyList()
                                },
                            )
                        }
                        .pointerInput(state.selectedTool, state.brushSize) {
                            detectTapGestures { offset ->
                                state.startStroke(offset)
                                state.commitStroke()
                                onStrokesChanged(state.exportStrokes())
                                onValidationChanged(state.isMaskValid)
                            }
                        }
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    event.changes.firstOrNull()?.let { change ->
                                        state.updateCursor(change.position)
                                        state.setCursorInside(true)
                                    }
                                }
                            }
                        }
                        .testTag(Strings.TestTags.wizardMaskDrawing),
                ) {
                    // Draw committed strokes
                    for (stroke in state.strokes) {
                        drawMaskStroke(stroke, maskFill, maskStroke, state.canvasSize, state.density)
                    }

                    // Draw live stroke
                    state.livePoints.takeIf { it.isNotEmpty() }?.let { points ->
                        val liveStroke = MaskStroke(
                            points = points,
                            brushSize = state.brushSize,
                            erase = state.selectedTool == MaskTool.Eraser,
                        )
                        drawMaskStroke(liveStroke, maskFill, maskStroke, state.canvasSize, state.density)
                    }
                }

                // Brush cursor indicator
                if (state.isCursorInsideCanvas && state.zoomLevel <= 1.5f) {
                    BrushCursor(
                        cursorPosition = state.cursorPosition,
                        brushSizePx = state.brushSize,
                        isEraser = state.selectedTool == MaskTool.Eraser,
                        modifier = Modifier.matchParentSize(),
                    )
                }
            }

            // Zoom buttons (bottom-right)
            ZoomControls(
                zoomLevel = state.zoomLevel,
                onZoomIn = { state.zoomIn() },
                onZoomOut = { state.zoomOut() },
                onResetZoom = { state.resetZoom() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(HomeDecorSpacing.Sm),
            )
        }

        Spacer(Modifier.height(HomeDecorSpacing.Xs))

        // -- Validation message --
        AnimatedVisibility(
            visible = state.hasMask && !state.isMaskValid,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Text(
                text = "Paint a bit more",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// BrushCursor — circle overlay showing brush size
// ---------------------------------------------------------------------------

@Composable
private fun BrushCursor(
    cursorPosition: Offset,
    brushSizePx: Float,
    isEraser: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = if (isEraser)
        MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
    else
        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart,
    ) {
        Box(
            modifier = Modifier
                .offset(
                    x = with(LocalDensity.current) { (cursorPosition.x - brushSizePx / 2).toDp() },
                    y = with(LocalDensity.current) { (cursorPosition.y - brushSizePx / 2).toDp() },
                )
                .size(with(LocalDensity.current) { brushSizePx.toDp() })
                .border(1.5.dp, color, CircleShape),
        )
    }
}

// ---------------------------------------------------------------------------
// ZoomControls — bottom-right zoom buttons
// ---------------------------------------------------------------------------

@Composable
private fun ZoomControls(
    zoomLevel: Float,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onResetZoom: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = HomeDecorShape.Chip,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
        shadowElevation = 2.dp,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xxs),
        ) {
            IconButton(
                onClick = onZoomOut,
                enabled = zoomLevel > MaskEditorState.MIN_ZOOM,
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    Icons.Rounded.Remove,
                    contentDescription = "Zoom out",
                    modifier = Modifier.size(HomeDecorIconSize.Small),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            Text(
                text = "${(zoomLevel * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { onResetZoom() }
                    }
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center,
            )

            IconButton(
                onClick = onZoomIn,
                enabled = zoomLevel < MaskEditorState.MAX_ZOOM,
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Zoom in",
                    modifier = Modifier.size(HomeDecorIconSize.Small),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// MaskToolbar — Brush | Eraser | divider | Undo | Redo | divider | Clear
// ---------------------------------------------------------------------------

@Composable
fun MaskToolbar(
    selectedTool: MaskTool,
    onBrushClick: () -> Unit,
    onEraserClick: () -> Unit,
    brushSize: Float,
    onBrushSizeChange: (Float) -> Unit,
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = HomeDecorShape.Chip,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Sm, vertical = HomeDecorSpacing.Xs),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Brush
            MaskToggleButton(
                icon = Icons.Rounded.Brush,
                label = Strings.wizardMaskBrush,
                isSelected = selectedTool == MaskTool.Brush,
                onClick = onBrushClick,
                testTag = Strings.TestTags.wizardMaskBrush,
            )

            Spacer(Modifier.width(HomeDecorSpacing.Xs))

            // Eraser
            MaskToggleButton(
                icon = Icons.Rounded.FormatPaint,
                label = Strings.wizardMaskEraser,
                isSelected = selectedTool == MaskTool.Eraser,
                onClick = onEraserClick,
                testTag = Strings.TestTags.wizardMaskEraser,
            )

            Spacer(Modifier.width(HomeDecorSpacing.Sm))

            // Inline brush size slider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(140.dp),
            ) {
                Slider(
                    value = brushSize,
                    onValueChange = onBrushSizeChange,
                    valueRange = MaskEditorState.MIN_BRUSH_SIZE..MaskEditorState.MAX_BRUSH_SIZE,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(Strings.TestTags.wizardMaskBrushSize),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                    ),
                )
                Text(
                    "${brushSize.toInt()}px",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp).width(36.dp),
                    textAlign = TextAlign.End,
                )
            }

            Spacer(Modifier.width(HomeDecorSpacing.Sm))

            // Divider
            VerticalDivider()

            Spacer(Modifier.width(HomeDecorSpacing.Sm))

            // Undo
            MaskIconButton(
                icon = Icons.Rounded.Undo,
                label = Strings.wizardMaskUndo,
                enabled = canUndo,
                onClick = onUndo,
                testTag = Strings.TestTags.wizardMaskUndo,
            )

            Spacer(Modifier.width(HomeDecorSpacing.Xs))

            // Redo
            MaskIconButton(
                icon = Icons.Rounded.Redo,
                label = Strings.wizardMaskRedo,
                enabled = canRedo,
                onClick = onRedo,
                testTag = Strings.TestTags.wizardMaskRedo,
            )

            Spacer(Modifier.width(HomeDecorSpacing.Sm))

            // Divider
            VerticalDivider()

            Spacer(Modifier.width(HomeDecorSpacing.Sm))

            // Clear
            MaskClearButton(onClick = onClear)
        }
    }
}

// ---------------------------------------------------------------------------
// MaskToggleButton — Brush / Eraser toggle (icon + label)
// ---------------------------------------------------------------------------

@Composable
private fun MaskToggleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(),
        label = "toggleScale",
    )

    val bgColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        isHovered -> MaterialTheme.colorScheme.surfaceContainerHigh
        else -> Color.Transparent
    }

    val contentColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        interactionSource = interactionSource,
        modifier = modifier
            .height(40.dp)
            .scale(scale)
            .testTag(testTag)
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = label
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Medium),
                tint = contentColor,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// MaskIconButton — Undo / Redo (icon + label, 40dp)
// ---------------------------------------------------------------------------

@Composable
private fun MaskIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(),
        label = "iconBtnScale",
    )

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        color = when {
            !enabled -> Color.Transparent
            isHovered -> MaterialTheme.colorScheme.surfaceContainerHigh
            else -> Color.Transparent
        },
        interactionSource = interactionSource,
        modifier = modifier
            .height(40.dp)
            .scale(scale)
            .testTag(testTag)
            .semantics { contentDescription = label },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Medium),
                tint = if (enabled)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = if (enabled)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// MaskClearButton — Clear all strokes
// ---------------------------------------------------------------------------

@Composable
private fun MaskClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(),
        label = "clearScale",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isHovered)
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f)
        else
            MaterialTheme.colorScheme.errorContainer,
        interactionSource = interactionSource,
        modifier = modifier
            .height(40.dp)
            .scale(scale)
            .testTag(Strings.TestTags.wizardMaskClear)
            .semantics {
                role = Role.Button
                contentDescription = Strings.wizardMaskClear
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                Icons.Rounded.Delete,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Small),
                tint = MaterialTheme.colorScheme.onErrorContainer,
            )
            Text(
                Strings.wizardMaskClear,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// VerticalDivider
// ---------------------------------------------------------------------------

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(20.dp)
            .background(MaterialTheme.colorScheme.outlineVariant),
    )
}

// ---------------------------------------------------------------------------
// Canvas drawMaskStroke — #0F4C4C at 35% fill + white 2dp stroke
// ---------------------------------------------------------------------------

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMaskStroke(
    stroke: MaskStroke,
    fillColor: Color,
    strokeColor: Color,
    canvasSize: IntSize,
    density: Float,
) {
    val points = stroke.points
    if (points.size < 2) return

    val w = size.width
    val h = size.height
    val brushPx = stroke.brushSize * density

    points.zipWithNext().forEach { (start, end) ->
        if (stroke.erase) {
            drawLine(
                color = Color.Transparent,
                start = Offset(start.x * w, start.y * h),
                end = Offset(end.x * w, end.y * h),
                strokeWidth = brushPx,
                cap = StrokeCap.Round,
                blendMode = BlendMode.Clear,
            )
        } else {
            val startPx = Offset(start.x * w, start.y * h)
            val endPx = Offset(end.x * w, end.y * h)

            // White outline stroke — 2dp
            val outlinePx = 2f * density
            drawLine(
                color = strokeColor,
                start = startPx,
                end = endPx,
                strokeWidth = brushPx + outlinePx,
                cap = StrokeCap.Round,
            )

            // Inner fill — Primary at 40% opacity
            drawLine(
                color = fillColor,
                start = startPx,
                end = endPx,
                strokeWidth = brushPx,
                cap = StrokeCap.Round,
            )
        }
    }
}
