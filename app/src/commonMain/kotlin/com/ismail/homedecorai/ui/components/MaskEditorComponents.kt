package com.ismail.homedecorai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Redo
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.theme.HomeDecorExtra
import com.ismail.homedecorai.ui.theme.HomeDecorIconSize
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing

// ---------------------------------------------------------------------------
// Mask data model
// ---------------------------------------------------------------------------

/**
 * A single stroke in the mask editor.
 *
 * @param points Normalized coordinates (0..1) relative to the canvas size.
 * @param brushSize Stroke width in pixels at the current canvas scale.
 * @param erase If true, this stroke removes mask (eraser mode).
 */
data class MaskStroke(
    val points: List<Offset> = emptyList(),
    val brushSize: Float = 24f,
    val erase: Boolean = false,
)

// ---------------------------------------------------------------------------
// Mask tool
// ---------------------------------------------------------------------------

enum class MaskTool { Brush, Eraser }

// ---------------------------------------------------------------------------
// MaskEditorState — manages strokes, undo/redo, brush settings
// ---------------------------------------------------------------------------

class MaskEditorState(
    initialBrushSize: Float = 24f,
) {
    /** Committed strokes (the persistent mask). */
    var strokes = mutableStateListOf<MaskStroke>()
        private set

    /** Live points being drawn (not yet committed). */
    var livePoints by mutableStateOf<List<Offset>>(emptyList())

    /** Current tool selection. */
    var selectedTool by mutableStateOf(MaskTool.Brush)
        private set

    /** Current brush size in raw pixels. */
    var brushSize by mutableFloatStateOf(initialBrushSize)
        private set

    /** Canvas size in pixels (set via onSizeChanged). */
    var canvasSize by mutableStateOf(IntSize.Zero)
        private set

    /** Whether the first-use instruction has been dismissed. */
    var instructionDismissed by mutableStateOf(false)
        private set

    // Undo / redo stacks — store full stroke snapshots
    private val undoStack = mutableListOf<List<MaskStroke>>()
    private val redoStack = mutableListOf<List<MaskStroke>>()

    /** True if there are strokes to undo. */
    val canUndo: Boolean get() = undoStack.isNotEmpty()

    /** True if there are strokes to redo. */
    val canRedo: Boolean get() = redoStack.isNotEmpty()

    /** True if a meaningful mask area exists. */
    val hasMask: Boolean get() = strokes.any { !it.erase && it.points.size >= 2 }

    /** Total number of non-erased paint strokes. */
    val paintStrokeCount: Int get() = strokes.count { !it.erase }

    /** Mask opacity (0..1) controlling the translucent mask alpha. */
    var maskOpacity by mutableFloatStateOf(0.5f)
        private set

    /** Approximate coverage ratio (0..1) based on stroke area. */
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

    /** Coverage as a percentage string (e.g. "12%"). */
    val coveragePercent: String get() = "${(coverageRatio * 100).toInt()}%"

    // -- Public actions --------------------------------------------------------

    fun setCanvasSize(size: IntSize) {
        canvasSize = size
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

    /** Start a new stroke (called on drag start / tap). */
    fun startStroke(point: Offset) {
        livePoints = listOf(point)
    }

    /** Continue the current stroke (called on drag). */
    fun continueStroke(point: Offset) {
        livePoints = livePoints + point
    }

    /** Commit the current live stroke to the strokes list. */
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

    /** Undo the last committed stroke. */
    fun undo() {
        if (undoStack.isEmpty()) return
        redoStack.add(strokes.toList())
        strokes = mutableStateListOf<MaskStroke>().apply {
            addAll(undoStack.removeLast())
        }
    }

    /** Redo the last undone stroke. */
    fun redo() {
        if (redoStack.isEmpty()) return
        undoStack.add(strokes.toList())
        strokes = mutableStateListOf<MaskStroke>().apply {
            addAll(redoStack.removeLast())
        }
    }

    /** Clear all strokes. */
    fun clear() {
        if (strokes.isEmpty()) return
        saveUndo()
        strokes = mutableStateListOf()
        redoStack.clear()
    }

    /** Dismiss the first-use instruction overlay. */
    fun dismissInstruction() {
        instructionDismissed = true
    }

    /** Load strokes from external state (e.g. WizardState). */
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

    /** Export current strokes as the wizard's MaskStroke format. */
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
        val avgBrush = stroke.brushSize
        var length = 0f
        for (i in 1 until stroke.points.size) {
            val dx = (stroke.points[i].x - stroke.points[i - 1].x) * canvasSize.width
            val dy = (stroke.points[i].y - stroke.points[i - 1].y) * canvasSize.height
            length += kotlin.math.sqrt(dx * dx + dy * dy)
        }
        return length * avgBrush
    }

    companion object {
        const val MIN_BRUSH_SIZE = 8f
        const val MAX_BRUSH_SIZE = 72f
        const val DEFAULT_BRUSH_SIZE = 24f
        const val MAX_UNDO_STACK = 50
        /** Minimum coverage ratio to consider the mask "meaningful". */
        const val MIN_COVERAGE_THRESHOLD = 0.005f
    }
}

// ---------------------------------------------------------------------------
// MaskEditor — main composable
// ---------------------------------------------------------------------------

/**
 * Full-featured mask editor for the Replace Furniture step.
 *
 * @param imageContent Composable that renders the source image.
 * @param state The editor state managing strokes, tools, undo/redo.
 * @param onStrokesChanged Called whenever strokes change (for external state sync).
 * @param modifier Modifier applied to the root container.
 * @param imageAspectRatio Width/height ratio for the image (default 16/9).
 * @param maskOpacity Translucent mask alpha (0..1, default 0.5).
 */
@Composable
fun MaskEditor(
    imageContent: @Composable () -> Unit,
    state: MaskEditorState,
    onStrokesChanged: (List<com.ismail.homedecorai.ui.tools.MaskStroke>) -> Unit = {},
    modifier: Modifier = Modifier,
    imageAspectRatio: Float = 16f / 9f,
    maskOpacity: Float = 0.5f,
) {
    val animatedOpacity by animateFloatAsState(
        targetValue = maskOpacity,
        animationSpec = tween(durationMillis = 120, easing = LinearEasing),
        label = "maskOpacity",
    )
    val maskColor = Color((animatedOpacity * 255).toInt().coerceIn(0, 255) * 0x01000000 + 0xFF4444)
    val borderColor = if (state.hasMask)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant

    // Animated tool-state transition (100-150 ms)
    val toolAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 120, easing = LinearEasing),
        label = "toolAlpha",
    )

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ── Canvas area ──────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, borderColor, RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .testTag(Strings.TestTags.wizardMaskCanvas),
        ) {
            // Image layer — preserve aspect ratio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageAspectRatio)
                    .clip(RoundedCornerShape(20.dp)),
            ) {
                imageContent()
            }

            // Drawing layer (on top of image)
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                    .pointerInput(state.selectedTool, state.brushSize) {
                        detectDragGestures(
                            onDragStart = { offset -> state.startStroke(offset) },
                            onDrag = { change, _ ->
                                change.consume()
                                state.continueStroke(change.position)
                            },
                            onDragEnd = {
                                state.commitStroke()
                                onStrokesChanged(state.exportStrokes())
                            },
                            onDragCancel = { state.livePoints = emptyList() },
                        )
                    }
                    .pointerInput(state.selectedTool, state.brushSize) {
                        detectTapGestures { offset ->
                            state.startStroke(offset)
                            state.commitStroke()
                            onStrokesChanged(state.exportStrokes())
                        }
                    }
                    .testTag(Strings.TestTags.wizardMaskDrawing),
            ) {
                // Draw committed strokes
                for (stroke in state.strokes) {
                    drawStroke(stroke, maskColor, state.canvasSize)
                }
                // Draw live stroke
                state.livePoints.takeIf { it.isNotEmpty() }?.let { points ->
                    val liveStroke = MaskStroke(
                        points = points,
                        brushSize = state.brushSize,
                        erase = state.selectedTool == MaskTool.Eraser,
                    )
                    drawStroke(liveStroke, maskColor, state.canvasSize)
                }
            }

            // Instruction overlay (first use)
            MaskInstructionOverlay(
                visible = !state.instructionDismissed && !state.hasMask,
                onDismiss = { state.dismissInstruction() },
                modifier = Modifier.matchParentSize(),
            )
        }

        Spacer(Modifier.height(12.dp))

        // ── Toolbar row ──────────────────────────────────────────────────
        MaskToolbar(
            selectedTool = state.selectedTool,
            onBrushClick = { state.selectTool(MaskTool.Brush) },
            onEraserClick = { state.selectTool(MaskTool.Eraser) },
            canUndo = state.canUndo,
            canRedo = state.canRedo,
            onUndo = { state.undo(); onStrokesChanged(state.exportStrokes()) },
            onRedo = { state.redo(); onStrokesChanged(state.exportStrokes()) },
            onClear = { state.clear(); onStrokesChanged(state.exportStrokes()) },
            modifier = Modifier.graphicsLayer { alpha = toolAlpha },
        )

        Spacer(Modifier.height(8.dp))

        // ── Brush size slider ────────────────────────────────────────────
        BrushSizeControl(
            brushSize = state.brushSize,
            onSizeChange = { state.setBrushSize(it) },
        )

        Spacer(Modifier.height(8.dp))

        // ── Mask opacity slider ──────────────────────────────────────────
        MaskOpacityControl(
            opacity = maskOpacity,
            onOpacityChange = { state.setMaskOpacity(it) },
        )

        Spacer(Modifier.height(8.dp))

        // ── Coverage indicator ───────────────────────────────────────────
        MaskCoverageIndicator(
            hasMask = state.hasMask,
            coveragePercent = state.coveragePercent,
            coverageRatio = state.coverageRatio,
        )
    }
}

// ---------------------------------------------------------------------------
// MaskToolbar — tool selection + undo/redo + clear
// ---------------------------------------------------------------------------

@Composable
fun MaskToolbar(
    selectedTool: MaskTool,
    onBrushClick: () -> Unit,
    onEraserClick: () -> Unit,
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Brush
        MaskToolButton(
            icon = Icons.Rounded.Brush,
            label = Strings.wizardMaskBrush,
            isSelected = selectedTool == MaskTool.Brush,
            onClick = onBrushClick,
            testTag = Strings.TestTags.wizardMaskBrush,
        )

        Spacer(Modifier.width(8.dp))

        // Eraser
        MaskToolButton(
            icon = Icons.Rounded.Create,
            label = Strings.wizardMaskEraser,
            isSelected = selectedTool == MaskTool.Eraser,
            onClick = onEraserClick,
            testTag = Strings.TestTags.wizardMaskEraser,
        )

        Spacer(Modifier.width(16.dp))

        // Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp)
                .background(MaterialTheme.colorScheme.outlineVariant),
        )

        Spacer(Modifier.width(16.dp))

        // Undo
        UndoRedoButton(
            icon = Icons.Rounded.Undo,
            label = Strings.wizardMaskUndo,
            enabled = canUndo,
            onClick = onUndo,
            testTag = Strings.TestTags.wizardMaskUndo,
        )

        Spacer(Modifier.width(8.dp))

        // Redo
        UndoRedoButton(
            icon = Icons.Rounded.Redo,
            label = Strings.wizardMaskRedo,
            enabled = canRedo,
            onClick = onRedo,
            testTag = Strings.TestTags.wizardMaskRedo,
        )

        Spacer(Modifier.width(16.dp))

        // Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp)
                .background(MaterialTheme.colorScheme.outlineVariant),
        )

        Spacer(Modifier.width(16.dp))

        // Clear
        ClearMaskButton(onClick = onClear)
    }
}

// ---------------------------------------------------------------------------
// MaskToolButton — brush / eraser toggle
// ---------------------------------------------------------------------------

@Composable
fun MaskToolButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    val bgColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceContainerLow

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "maskToolButtonScale",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = when {
            isSelected -> MaterialTheme.colorScheme.primary
            isHovered -> MaterialTheme.colorScheme.surfaceContainerHigh
            else -> MaterialTheme.colorScheme.surfaceContainerLow
        },
        interactionSource = interactionSource,
        modifier = modifier
            .scale(scale)
            .testTag(testTag)
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = label
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
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
// UndoRedoButton
// ---------------------------------------------------------------------------

@Composable
fun UndoRedoButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "",
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .testTag(testTag)
            .semantics {
                contentDescription = label
            },
        colors = IconButtonDefaults.iconButtonColors(
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        ),
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
    }
}

// ---------------------------------------------------------------------------
// ClearMaskButton
// ---------------------------------------------------------------------------

@Composable
fun ClearMaskButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "clearMaskButtonScale",
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isHovered)
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f)
        else
            MaterialTheme.colorScheme.errorContainer,
        interactionSource = interactionSource,
        modifier = modifier
            .scale(scale)
            .testTag(Strings.TestTags.wizardMaskClear)
            .semantics {
                role = Role.Button
                contentDescription = Strings.wizardMaskClear
            },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
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
// BrushSizeControl
// ---------------------------------------------------------------------------

/**
 * Horizontal slider for selecting brush size.
 *
 * Shows the current brush size as a circular preview on the left,
 * a labeled slider in the middle, and the numeric value on the right.
 */
@Composable
fun BrushSizeControl(
    brushSize: Float,
    onSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedSize by animateFloatAsState(
        targetValue = brushSize,
        animationSpec = spring(),
        label = "brushSize",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Brush size preview circle
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                modifier = Modifier.size((animatedSize / MaskEditorState.MAX_BRUSH_SIZE * 28f).dp.coerceAtLeast(6.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), CircleShape),
                )
            }
        }

        // Slider
        Slider(
            value = brushSize,
            onValueChange = onSizeChange,
            valueRange = MaskEditorState.MIN_BRUSH_SIZE..MaskEditorState.MAX_BRUSH_SIZE,
            modifier = Modifier
                .weight(1f)
                .testTag(Strings.TestTags.wizardMaskBrushSize),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
            ),
        )

        // Numeric label
        Text(
            "${brushSize.toInt()}px",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.End,
        )
    }
}

// ---------------------------------------------------------------------------
// MaskOpacityControl
// ---------------------------------------------------------------------------

/**
 * Horizontal slider for controlling mask opacity.
 */
@Composable
fun MaskOpacityControl(
    opacity: Float,
    onOpacityChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Opacity preview dot
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF4444).copy(alpha = opacity * 0.6f),
                modifier = Modifier.size(20.dp),
                content = {},
            )
        }

        // Slider
        Slider(
            value = opacity,
            onValueChange = onOpacityChange,
            valueRange = 0.1f..1f,
            modifier = Modifier
                .weight(1f)
                .testTag(Strings.TestTags.wizardMaskOpacity),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
            ),
        )

        // Numeric label
        Text(
            "${(opacity * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.End,
        )
    }
}

// ---------------------------------------------------------------------------
// MaskCoverageIndicator
// ---------------------------------------------------------------------------

/**
 * Shows current mask coverage with a label and percentage.
 *
 * - Before any mask: "Paint over the object you want to replace."
 * - After mask: "Mask the object completely — 12%"
 */
@Composable
fun MaskCoverageIndicator(
    hasMask: Boolean,
    coveragePercent: String,
    coverageRatio: Float,
    modifier: Modifier = Modifier,
) {
    val indicatorColor = when {
        !hasMask -> MaterialTheme.colorScheme.onSurface
        coverageRatio >= 0.05f -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.tertiary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Coverage dot
        Surface(
            shape = CircleShape,
            color = indicatorColor.copy(alpha = 0.2f),
            modifier = Modifier.size(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(indicatorColor, CircleShape),
            )
        }

        // Label
        Text(
            text = if (hasMask) {
                "${Strings.wizardMaskCoverageLabel} \u2014 $coveragePercent"
            } else {
                Strings.wizardMaskCoverageEmpty
            },
            style = MaterialTheme.typography.bodySmall,
            color = indicatorColor,
        )
    }
}

// ---------------------------------------------------------------------------
// MaskInstructionOverlay — first-use overlay
// ---------------------------------------------------------------------------

/**
 * Semi-transparent overlay shown on first use with a brush icon and
 * instruction text. Disappears after the user starts drawing.
 */
@Composable
fun MaskInstructionOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300, easing = LinearEasing)),
        exit = fadeOut(tween(200, easing = LinearEasing)),
        modifier = modifier,
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.55f),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss() }
                },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Brush icon
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        modifier = Modifier.size(56.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Create,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }

                    // Instruction text
                    Text(
                        Strings.wizardMaskInstruction,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )

                    // Hint
                    Text(
                        Strings.wizardMaskInstructionHint,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(4.dp))

                    // Dismiss label
                    Text(
                        Strings.wizardMaskInstructionDismiss,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.5f),
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Canvas drawStroke extension
// ---------------------------------------------------------------------------

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStroke(
    stroke: MaskStroke,
    maskColor: Color,
    canvasSize: IntSize,
) {
    val points = stroke.points
    if (points.size < 2) return

    val w = size.width
    val h = size.height

    points.zipWithNext().forEach { (start, end) ->
        if (stroke.erase) {
            // Eraser: use BlendMode.Clear to remove painted areas
            drawLine(
                color = Color.Transparent,
                start = Offset(start.x * w, start.y * h),
                end = Offset(end.x * w, end.y * h),
                strokeWidth = stroke.brushSize,
                cap = StrokeCap.Round,
                blendMode = BlendMode.Clear,
            )
        } else {
            // Paint: translucent red mask with visible edges
            val startPx = Offset(start.x * w, start.y * h)
            val endPx = Offset(end.x * w, end.y * h)

            // Outer edge (slightly wider, less opaque)
            drawLine(
                color = maskColor.copy(alpha = 0.3f),
                start = startPx,
                end = endPx,
                strokeWidth = stroke.brushSize + 4f,
                cap = StrokeCap.Round,
            )

            // Inner fill
            drawLine(
                color = maskColor.copy(alpha = 0.62f),
                start = startPx,
                end = endPx,
                strokeWidth = stroke.brushSize,
                cap = StrokeCap.Round,
            )
        }
    }
}
