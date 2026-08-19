package com.ismail.homedecorai

import com.ismail.homedecorai.ui.components.MaskEditorState
import com.ismail.homedecorai.ui.components.MaskTool
import com.ismail.homedecorai.ui.tools.MaskStroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MaskEditorTest {

    // ── MaskStroke data class ─────────────────────────────────────────────

    @Test
    fun maskStroke_defaultFields() {
        val stroke = MaskStroke()
        assertTrue(stroke.points.isEmpty())
        assertEquals(24f, stroke.brushSize)
        assertFalse(stroke.erase)
    }

    @Test
    fun maskStroke_customFields() {
        val points = listOf(Pair(0.1f, 0.2f), Pair(0.3f, 0.4f))
        val stroke = MaskStroke(points = points, brushSize = 40f, erase = true)
        assertEquals(2, stroke.points.size)
        assertEquals(40f, stroke.brushSize)
        assertTrue(stroke.erase)
    }

    @Test
    fun maskStroke_wizardFormat_withBrushSizeAndErase() {
        val wizardStroke = MaskStroke(
            points = listOf(Pair(0.5f, 0.5f)),
            brushSize = 32f,
            erase = true,
        )
        assertEquals(32f, wizardStroke.brushSize)
        assertTrue(wizardStroke.erase)
    }

    // ── MaskTool enum ─────────────────────────────────────────────────────

    @Test
    fun maskTool_hasBrushAndEraser() {
        val tools = MaskTool.entries
        assertEquals(2, tools.size)
        assertTrue(tools.contains(MaskTool.Brush))
        assertTrue(tools.contains(MaskTool.Eraser))
    }

    // ── MaskEditorState — initial state ───────────────────────────────────

    @Test
    fun editorState_initialValues() {
        val state = MaskEditorState()
        assertTrue(state.strokes.isEmpty())
        assertTrue(state.livePoints.isEmpty())
        assertEquals(MaskTool.Brush, state.selectedTool)
        assertEquals(MaskEditorState.DEFAULT_BRUSH_SIZE, state.brushSize)
        assertEquals(IntSize.Zero, state.canvasSize)
        assertFalse(state.instructionDismissed)
        assertFalse(state.canUndo)
        assertFalse(state.canRedo)
        assertFalse(state.hasMask)
        assertEquals(0, state.paintStrokeCount)
        assertEquals(0f, state.coverageRatio)
    }

    // ── MaskEditorState — brush size ──────────────────────────────────────

    @Test
    fun editorState_setBrushSize_clampsToMin() {
        val state = MaskEditorState()
        state.setBrushSize(2f)
        assertEquals(MaskEditorState.MIN_BRUSH_SIZE, state.brushSize)
    }

    @Test
    fun editorState_setBrushSize_clampsToMax() {
        val state = MaskEditorState()
        state.setBrushSize(200f)
        assertEquals(MaskEditorState.MAX_BRUSH_SIZE, state.brushSize)
    }

    @Test
    fun editorState_setBrushSize_validValue() {
        val state = MaskEditorState()
        state.setBrushSize(40f)
        assertEquals(40f, state.brushSize)
    }

    // ── MaskEditorState — tool selection ───────────────────────────────────

    @Test
    fun editorState_selectTool() {
        val state = MaskEditorState()
        assertEquals(MaskTool.Brush, state.selectedTool)
        state.selectTool(MaskTool.Eraser)
        assertEquals(MaskTool.Eraser, state.selectedTool)
        state.selectTool(MaskTool.Brush)
        assertEquals(MaskTool.Brush, state.selectedTool)
    }

    // ── MaskEditorState — stroke lifecycle ─────────────────────────────────

    @Test
    fun editorState_startStroke_populatesLivePoints() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 20f))
        assertEquals(1, state.livePoints.size)
        assertEquals(Offset(10f, 20f), state.livePoints[0])
    }

    @Test
    fun editorState_continueStroke_appendsPoints() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.continueStroke(Offset(30f, 30f))
        assertEquals(3, state.livePoints.size)
    }

    @Test
    fun editorState_commitStroke_movesToStrokes() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()
        assertEquals(1, state.strokes.size)
        assertTrue(state.livePoints.isEmpty())
        assertTrue(state.hasMask)
    }

    @Test
    fun editorState_commitStroke_emptyLivePoints_noOp() {
        val state = MaskEditorState()
        state.commitStroke()
        assertTrue(state.strokes.isEmpty())
    }

    @Test
    fun editorState_commitStroke_normalizesPoints() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 100))
        state.startStroke(Offset(100f, 50f))
        state.commitStroke()
        assertEquals(1, state.strokes.size)
        val normalized = state.strokes[0].points
        assertEquals(1, normalized.size)
        assertEquals(0.5f, normalized[0].x, 0.01f)
        assertEquals(0.5f, normalized[0].y, 0.01f)
    }

    @Test
    fun editorState_commitStroke_recordsBrushSize() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.setBrushSize(40f)
        state.startStroke(Offset(10f, 10f))
        state.commitStroke()
        assertEquals(40f, state.strokes[0].brushSize)
    }

    @Test
    fun editorState_commitStroke_recordsEraseFlag() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.selectTool(MaskTool.Eraser)
        state.startStroke(Offset(10f, 10f))
        state.commitStroke()
        assertTrue(state.strokes[0].erase)
    }

    // ── MaskEditorState — undo / redo ─────────────────────────────────────

    @Test
    fun editorState_undo_restoresPreviousState() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 10f))
        state.commitStroke()
        assertEquals(1, state.strokes.size)
        assertTrue(state.canUndo)
        state.undo()
        assertTrue(state.strokes.isEmpty())
        assertTrue(state.canRedo)
    }

    @Test
    fun editorState_redo_restoresUndoneState() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 10f))
        state.commitStroke()
        state.undo()
        assertTrue(state.strokes.isEmpty())
        state.redo()
        assertEquals(1, state.strokes.size)
    }

    @Test
    fun editorState_undo_onEmptyStack_noOp() {
        val state = MaskEditorState()
        state.undo()
        assertTrue(state.strokes.isEmpty())
        assertFalse(state.canUndo)
    }

    @Test
    fun editorState_redo_onEmptyStack_noOp() {
        val state = MaskEditorState()
        state.redo()
        assertTrue(state.strokes.isEmpty())
        assertFalse(state.canRedo)
    }

    @Test
    fun editorState_newStroke_clearsRedoStack() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 10f))
        state.commitStroke()
        state.undo()
        assertTrue(state.canRedo)
        state.startStroke(Offset(20f, 20f))
        state.commitStroke()
        assertFalse(state.canRedo)
    }

    @Test
    fun editorState_undoRedo_overflowStack() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        val totalStrokes = MaskEditorState.MAX_UNDO_STACK + 5
        repeat(totalStrokes) { i ->
            state.startStroke(Offset(i.toFloat(), i.toFloat()))
            state.commitStroke()
        }
        // All strokes are kept; undo history is capped at MAX_UNDO_STACK
        assertEquals(totalStrokes, state.strokes.size)
        // Can undo up to MAX_UNDO_STACK times
        repeat(MaskEditorState.MAX_UNDO_STACK) { state.undo() }
        assertEquals(totalStrokes - MaskEditorState.MAX_UNDO_STACK, state.strokes.size)
        // Cannot undo further
        state.undo()
        assertEquals(totalStrokes - MaskEditorState.MAX_UNDO_STACK, state.strokes.size)
    }

    // ── MaskEditorState — clear ───────────────────────────────────────────

    @Test
    fun editorState_clear_removesAllStrokes() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 10f))
        state.commitStroke()
        state.startStroke(Offset(20f, 20f))
        state.commitStroke()
        assertEquals(2, state.strokes.size)
        state.clear()
        assertTrue(state.strokes.isEmpty())
        assertFalse(state.hasMask)
    }

    @Test
    fun editorState_clear_onEmpty_noOp() {
        val state = MaskEditorState()
        state.clear()
        assertTrue(state.strokes.isEmpty())
    }

    // ── MaskEditorState — hasMask ─────────────────────────────────────────

    @Test
    fun editorState_hasMask_requiresMinPoints() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        // Single point - not enough
        state.startStroke(Offset(10f, 10f))
        state.commitStroke()
        assertFalse(state.hasMask)
    }

    @Test
    fun editorState_hasMask_twoPointsSufficient() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()
        assertTrue(state.hasMask)
    }

    @Test
    fun editorState_hasMask_eraserStrokeDoesNotCount() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.selectTool(MaskTool.Eraser)
        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()
        assertFalse(state.hasMask)
    }

    // ── MaskEditorState — paintStrokeCount ────────────────────────────────

    @Test
    fun editorState_paintStrokeCount_excludesErasers() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()
        state.selectTool(MaskTool.Eraser)
        state.startStroke(Offset(30f, 30f))
        state.continueStroke(Offset(40f, 40f))
        state.commitStroke()
        assertEquals(1, state.paintStrokeCount)
    }

    // ── MaskEditorState — coverageRatio ───────────────────────────────────

    @Test
    fun editorState_coverageRatio_zeroWhenNoCanvas() {
        val state = MaskEditorState()
        assertEquals(0f, state.coverageRatio)
    }

    @Test
    fun editorState_coverageRatio_zeroWhenNoStrokes() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        assertEquals(0f, state.coverageRatio)
    }

    @Test
    fun editorState_coverageRatio_positiveWithStrokes() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.setBrushSize(50f)
        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(90f, 10f))
        state.commitStroke()
        assertTrue(state.coverageRatio > 0f)
    }

    @Test
    fun editorState_coveragePercent_format() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        assertEquals("0%", state.coveragePercent)
    }

    // ── MaskEditorState — exportStrokes ───────────────────────────────────

    @Test
    fun editorState_exportStrokes_convertsToWizardFormat() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.setBrushSize(36f)
        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()
        val exported = state.exportStrokes()
        assertEquals(1, exported.size)
        assertEquals(2, exported[0].points.size)
        assertEquals(36f, exported[0].brushSize)
        assertFalse(exported[0].erase)
        // Verify normalized
        assertEquals(0.1f, exported[0].points[0].first, 0.01f)
        assertEquals(0.1f, exported[0].points[0].second, 0.01f)
    }

    @Test
    fun editorState_exportStrokes_eraserStrokePreserved() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(100, 100))
        state.selectTool(MaskTool.Eraser)
        state.startStroke(Offset(10f, 10f))
        state.commitStroke()
        val exported = state.exportStrokes()
        assertTrue(exported[0].erase)
    }

    // ── MaskEditorState — loadStrokes ─────────────────────────────────────

    @Test
    fun editorState_loadStrokes_convertsFromWizardFormat() {
        val state = MaskEditorState()
        val wizardStrokes = listOf(
            MaskStroke(
                points = listOf(Pair(0.1f, 0.2f), Pair(0.3f, 0.4f)),
                brushSize = 40f,
                erase = true,
            )
        )
        state.loadStrokes(wizardStrokes)
        assertEquals(1, state.strokes.size)
        assertEquals(2, state.strokes[0].points.size)
        assertEquals(40f, state.strokes[0].brushSize)
        assertTrue(state.strokes[0].erase)
    }

    // ── MaskEditorState — instruction ─────────────────────────────────────

    @Test
    fun editorState_dismissInstruction() {
        val state = MaskEditorState()
        assertFalse(state.instructionDismissed)
        state.dismissInstruction()
        assertTrue(state.instructionDismissed)
    }

    // ── String constants ──────────────────────────────────────────────────

    @Test
    fun maskStrings_allDefined() {
        assertTrue(Strings.wizardMaskTitle.isNotBlank())
        assertTrue(Strings.wizardMaskSubtitle.isNotBlank())
        assertTrue(Strings.wizardMaskInstruction.isNotBlank())
        assertTrue(Strings.wizardMaskInstructionHint.isNotBlank())
        assertTrue(Strings.wizardMaskInstructionDismiss.isNotBlank())
        assertTrue(Strings.wizardMaskBrush.isNotBlank())
        assertTrue(Strings.wizardMaskEraser.isNotBlank())
        assertTrue(Strings.wizardMaskUndo.isNotBlank())
        assertTrue(Strings.wizardMaskRedo.isNotBlank())
        assertTrue(Strings.wizardMaskClear.isNotBlank())
        assertTrue(Strings.wizardMaskCoverageLabel.isNotBlank())
        assertTrue(Strings.wizardMaskCoverageEmpty.isNotBlank())
        assertTrue(Strings.wizardMaskReadyHint.isNotBlank())
        assertTrue(Strings.wizardMaskEmptyHint.isNotBlank())
    }

    @Test
    fun maskTestTags_allDefined() {
        assertTrue(Strings.TestTags.wizardMaskCanvas.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskDrawing.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskBrush.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskEraser.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskUndo.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskRedo.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskClear.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskBrushSize.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskCoverage.isNotBlank())
        assertTrue(Strings.TestTags.wizardMaskOpacity.isNotBlank())
    }

    // ── Regression: full drawing flow ──────────────────────────────────────

    @Test
    fun drawing_fullDragCommitStroke() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(400, 300))
        state.startStroke(Offset(40f, 30f))
        state.continueStroke(Offset(80f, 60f))
        state.continueStroke(Offset(120f, 90f))
        state.commitStroke()

        assertEquals(1, state.strokes.size)
        assertEquals(3, state.strokes[0].points.size)
        assertTrue(state.hasMask)
        assertTrue(state.canUndo)
        assertFalse(state.canRedo)
    }

    @Test
    fun drawing_tapCommitSinglePointStroke() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))
        state.startStroke(Offset(100f, 100f))
        state.commitStroke()

        assertEquals(1, state.strokes.size)
        assertEquals(1, state.strokes[0].points.size)
        assertFalse(state.hasMask)
    }

    @Test
    fun drawing_multipleStrokesAccumulate() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()

        state.startStroke(Offset(30f, 30f))
        state.continueStroke(Offset(40f, 40f))
        state.commitStroke()

        assertEquals(2, state.strokes.size)
        assertTrue(state.hasMask)
    }

    // ── Regression: erasing flow ───────────────────────────────────────────

    @Test
    fun erasing_afterPaintRemovesMask() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()
        assertTrue(state.hasMask)

        state.selectTool(MaskTool.Eraser)
        assertEquals(MaskTool.Eraser, state.selectedTool)

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()

        assertEquals(2, state.strokes.size)
        assertTrue(state.strokes[1].erase)
    }

    @Test
    fun erasing_switchBackToBrush() {
        val state = MaskEditorState()
        state.selectTool(MaskTool.Eraser)
        assertEquals(MaskTool.Eraser, state.selectedTool)
        state.selectTool(MaskTool.Brush)
        assertEquals(MaskTool.Brush, state.selectedTool)
    }

    @Test
    fun erasing_eraserStrokeRecordedWithCurrentBrushSize() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))
        state.setBrushSize(50f)
        state.selectTool(MaskTool.Eraser)

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()

        assertEquals(50f, state.strokes[0].brushSize)
        assertTrue(state.strokes[0].erase)
    }

    // ── Regression: undo flow ──────────────────────────────────────────────

    @Test
    fun undo_singleStrokeRestores() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()
        assertTrue(state.hasMask)

        state.undo()
        assertFalse(state.hasMask)
        assertTrue(state.strokes.isEmpty())
        assertTrue(state.canRedo)
    }

    @Test
    fun undo_multipleStrokesPartialRestore() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()

        state.startStroke(Offset(30f, 30f))
        state.continueStroke(Offset(40f, 40f))
        state.commitStroke()
        assertEquals(2, state.strokes.size)

        state.undo()
        assertEquals(1, state.strokes.size)
    }

    @Test
    fun undo_doesNotCorruptRemainingStrokes() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()

        state.startStroke(Offset(30f, 30f))
        state.continueStroke(Offset(40f, 40f))
        state.commitStroke()

        state.undo()
        assertEquals(1, state.strokes.size)
        assertEquals(2, state.strokes[0].points.size)
    }

    // ── Regression: redo flow ──────────────────────────────────────────────

    @Test
    fun redo_restoresUndoneStroke() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()

        state.undo()
        assertFalse(state.hasMask)

        state.redo()
        assertTrue(state.hasMask)
        assertEquals(1, state.strokes.size)
    }

    @Test
    fun redo_afterMultipleUndos() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()

        state.startStroke(Offset(30f, 30f))
        state.continueStroke(Offset(40f, 40f))
        state.commitStroke()

        state.undo()
        state.undo()
        assertEquals(0, state.strokes.size)

        state.redo()
        assertEquals(1, state.strokes.size)

        state.redo()
        assertEquals(2, state.strokes.size)
    }

    @Test
    fun redo_newStrokeClearsRedo() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()

        state.undo()
        assertTrue(state.canRedo)

        state.startStroke(Offset(50f, 50f))
        state.continueStroke(Offset(60f, 60f))
        state.commitStroke()
        assertFalse(state.canRedo)
    }

    // ── Regression: clear flow ─────────────────────────────────────────────

    @Test
    fun clear_removesAllStrokesAndDisablesMask() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()

        state.startStroke(Offset(60f, 60f))
        state.continueStroke(Offset(90f, 90f))
        state.commitStroke()
        assertEquals(2, state.strokes.size)

        state.clear()
        assertTrue(state.strokes.isEmpty())
        assertFalse(state.hasMask)
        assertFalse(state.canRedo)
    }

    @Test
    fun clear_undoableAfterClear() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()

        state.clear()
        assertTrue(state.strokes.isEmpty())

        state.undo()
        assertEquals(1, state.strokes.size)
        assertTrue(state.hasMask)
    }

    @Test
    fun clear_emptyStateNoOp() {
        val state = MaskEditorState()
        state.clear()
        assertTrue(state.strokes.isEmpty())
        assertFalse(state.canUndo)
    }

    // ── Regression: navigation (tool switching) ────────────────────────────

    @Test
    fun navigation_brushToEraserAndBackPreservesStrokes() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(50f, 50f))
        state.commitStroke()
        assertTrue(state.hasMask)

        state.selectTool(MaskTool.Eraser)
        assertEquals(MaskTool.Eraser, state.selectedTool)

        state.selectTool(MaskTool.Brush)
        assertEquals(MaskTool.Brush, state.selectedTool)
        assertTrue(state.hasMask)
        assertEquals(1, state.strokes.size)
    }

    @Test
    fun navigation_toolSwitchDoesNotAffectUndoRedo() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(200, 200))

        state.startStroke(Offset(10f, 10f))
        state.continueStroke(Offset(20f, 20f))
        state.commitStroke()

        state.selectTool(MaskTool.Eraser)
        state.selectTool(MaskTool.Brush)

        assertTrue(state.canUndo)
        assertFalse(state.canRedo)

        state.undo()
        assertTrue(state.canRedo)

        state.redo()
        assertTrue(state.hasMask)
    }

    // ── Regression: mask coordinate preservation on scale ──────────────────

    @Test
    fun coordinatePreservation_normalizedPointsSurviveRescale() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(400, 300))
        state.startStroke(Offset(200f, 150f))
        state.continueStroke(Offset(400f, 300f))
        state.commitStroke()

        val exported = state.exportStrokes()
        assertEquals(0.5f, exported[0].points[0].first, 0.01f)
        assertEquals(0.5f, exported[0].points[0].second, 0.01f)
        assertEquals(1.0f, exported[0].points[1].first, 0.01f)
        assertEquals(1.0f, exported[0].points[1].second, 0.01f)
    }

    @Test
    fun coordinatePreservation_differentAspectRatioNormalizesCorrectly() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(800, 200))

        state.startStroke(Offset(400f, 100f))
        state.commitStroke()

        val exported = state.exportStrokes()
        assertEquals(0.5f, exported[0].points[0].first, 0.01f)
        assertEquals(0.5f, exported[0].points[0].second, 0.01f)
    }

    // ── Regression: maskOpacity ────────────────────────────────────────────

    @Test
    fun maskOpacity_defaultIsHalf() {
        val state = MaskEditorState()
        assertEquals(0.5f, state.maskOpacity)
    }

    @Test
    fun maskOpacity_setClampsToMin() {
        val state = MaskEditorState()
        state.setMaskOpacity(0.01f)
        assertEquals(0.1f, state.maskOpacity)
    }

    @Test
    fun maskOpacity_setClampsToMax() {
        val state = MaskEditorState()
        state.setMaskOpacity(5f)
        assertEquals(1f, state.maskOpacity)
    }

    @Test
    fun maskOpacity_setValidValue() {
        val state = MaskEditorState()
        state.setMaskOpacity(0.75f)
        assertEquals(0.75f, state.maskOpacity)
    }

    // ── Regression: export/load round-trip ──────────────────────────────────

    @Test
    fun exportLoad_roundTripPreservesData() {
        val state = MaskEditorState()
        state.setCanvasSize(IntSize(300, 200))
        state.setBrushSize(32f)

        state.startStroke(Offset(50f, 50f))
        state.continueStroke(Offset(100f, 100f))
        state.commitStroke()

        val exported = state.exportStrokes()
        val newState = MaskEditorState()
        newState.loadStrokes(exported)

        assertEquals(1, newState.strokes.size)
        assertEquals(32f, newState.strokes[0].brushSize)
        assertEquals(2, newState.strokes[0].points.size)
    }
}
