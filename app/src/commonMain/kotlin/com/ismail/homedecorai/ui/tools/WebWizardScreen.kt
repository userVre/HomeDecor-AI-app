package com.ismail.homedecorai.ui.tools

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.Bathtub
import androidx.compose.material.icons.rounded.Bed
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.Cabin
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.Desk
import androidx.compose.material.icons.rounded.DoorFront
import androidx.compose.material.icons.rounded.Fireplace
import androidx.compose.material.icons.rounded.GridOn
import androidx.compose.material.icons.rounded.Hotel
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LocalLaundryService
import androidx.compose.material.icons.rounded.LunchDining
import androidx.compose.material.icons.rounded.Minimize
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.Style
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.TableRestaurant
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.Weekend
import androidx.compose.material.icons.rounded.Yard
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.convexCreateUploadUrl
import com.ismail.homedecorai.convexMutationAuth
import com.ismail.homedecorai.convexQueryAuth
import com.ismail.homedecorai.convexUploadToStorage
import com.ismail.homedecorai.browserDownloadFile
import com.ismail.homedecorai.browserShareContent
import com.ismail.homedecorai.getAnonymousIdFromPlatform
import com.ismail.homedecorai.getScreenWidthDp
import com.ismail.homedecorai.imagepicker.PickedImageData
import com.ismail.homedecorai.imagepicker.rememberDragDropHandler
import com.ismail.homedecorai.imagepicker.rememberImagePicker
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.ui.components.MaskEditor
import com.ismail.homedecorai.ui.components.MaskEditorState
import com.ismail.homedecorai.ui.components.MaskTool
import com.ismail.homedecorai.ui.components.ReviewStep
import com.ismail.homedecorai.ui.components.ReviewSummaryItem
import com.ismail.homedecorai.ui.components.generateActionForTool
import com.ismail.homedecorai.ui.discover.NetworkImage
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.isReducedMotionEnabled
import com.ismail.homedecorai.ui.wizard.SelectionCard
import com.ismail.homedecorai.ui.wizard.SelectionGrid
import com.ismail.homedecorai.ui.wizard.SelectionRequirementMessage
import com.ismail.homedecorai.ui.wizard.ValidationAlertBanner
import org.jetbrains.skia.Image

// ---------------------------------------------------------------------------
// State
// ---------------------------------------------------------------------------

enum class WizardStep { Upload, RoomType, Style, Palette, Refine, Material, Goals, Mask, ReplacementPrompt, TransferStrength, PaintColor, FloorStyle, FurnitureType, ReplacementStyle, ReferenceImage, Review }

data class MaskStroke(
    val points: List<Pair<Float, Float>> = emptyList(),
    val brushSize: Float = 24f,
    val erase: Boolean = false,
)

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
    val selectedMaterial: String? = null,
    val selectedGoals: List<String> = emptyList(),
    val maskStrokes: List<MaskStroke> = emptyList(),
    val replacementPrompt: String = "",
    val selectedTransferStrength: String? = null,
    val selectedTransferOptions: List<String> = emptyList(),
    val isGenerating: Boolean = false,
    val generationComplete: Boolean = false,
    val generationError: String? = null,
    val error: String? = null,
    val isDragging: Boolean = false,
    val selectedPaintColor: String? = null,
    val selectedFloorStyle: String? = null,
    val selectedRugOption: Boolean = false,
    val selectedFurnitureType: String? = null,
    val selectedReplacementStyle: String? = null,
    val referencePhoto: PickedImageData? = null,
    val customNotes: String = "",
    val selectedPalette: String? = null,
    val selectedDesignMode: String? = null,
    val selectedBudgetMode: String? = null,
    val avoidElements: List<String> = emptyList(),
    val keepElements: List<String> = emptyList(),
    val changeElements: List<String> = emptyList(),
    val generatedImageUrl: String? = null,
    val objectSelectedOnImage: Boolean = false,
    val hasDrawnMask: Boolean = false,
)

// ---------------------------------------------------------------------------
// Tool-specific step flow
// ---------------------------------------------------------------------------

internal fun stepsForTool(toolId: String?): List<WizardStep> = when (toolId) {
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

// ---------------------------------------------------------------------------
// Generation helpers: prompt building, upload, start, poll
// ---------------------------------------------------------------------------

private fun buildToolPrompt(state: WizardState): String {
    val toolId = state.tool?.id ?: return ""
    return when (toolId) {
        "layout" -> buildString {
            append("Focused space-planning task, not a normal redesign. Re-arrange existing furniture and propose a practical room layout to gain more space and fluid circulation. Preserve the exact architectural shell, camera angle, walls, windows, doors, fixed cabinetry, flooring, wall finishes, and room structure. Do not change the decor style unless needed for small staging consistency.")
            if (!state.selectedRoom.isNullOrBlank()) append(" Planning goals: ").append(state.selectedRoom!!.replace("-", " ")).append(".")
            if (state.selectedGoals.isNotEmpty()) append(" Goals: ").append(state.selectedGoals.joinToString(", ") { it.replace("-", " ") }).append(".")
            if (state.customNotes.isNotBlank()) append(" Custom notes: ").append(state.customNotes).append(".")
        }
        "reference" -> buildString {
            append("Reference style transfer. Use the first image as source room and the second image as reference style.")
            if (!state.selectedTransferStrength.isNullOrBlank()) append(" Transfer strength: ").append(state.selectedTransferStrength).append(".")
            if (state.selectedTransferOptions.isNotEmpty()) append(" Transfer options: ").append(state.selectedTransferOptions.joinToString(", ")).append(".")
            append(" Preserve the source room structure, camera angle, openings, and proportions.")
            if (state.customNotes.isNotBlank()) append(" Additional notes: ").append(state.customNotes).append(".")
        }
        "paint" -> buildString {
            append("Smart wall paint. Automatically detect all visible walls in the room and apply the selected material to them.")
            if (!state.selectedMaterial.isNullOrBlank()) append(" Apply material: ").append(state.selectedMaterial!!.replace("-", " ")).append(".")
            if (!state.selectedPaintColor.isNullOrBlank()) append(" Paint color: ").append(state.selectedPaintColor!!.replace("-", " ")).append(".")
            append(" Preserve the floor, ceiling, furniture, decor, trim, windows, shadows, camera angle, and every non-wall surface exactly as they are. The AI should intelligently identify wall surfaces, respect room geometry, and apply the material realistically with proper lighting and perspective.")
            if (state.customNotes.isNotBlank()) append(" Additional notes: ").append(state.customNotes).append(".")
        }
        "floor" -> buildString {
            append("Smart floor restyle. Automatically detect the floor surface in the room and apply the selected material to it.")
            if (!state.selectedMaterial.isNullOrBlank()) append(" Apply material: ").append(state.selectedMaterial!!.replace("-", " ")).append(".")
            if (!state.selectedFloorStyle.isNullOrBlank()) append(" Floor style: ").append(state.selectedFloorStyle!!.replace("-", " ")).append(".")
            if (state.selectedRugOption) append(" Optionally add an area rug to complement the flooring.")
            append(" Preserve walls, furniture, decor, baseboards, lighting, contact shadows, camera angle, and every non-floor surface exactly as they are. The AI should intelligently identify the floor area, respect perspective lines, and apply the material realistically with proper lighting and shadows.")
            if (state.customNotes.isNotBlank()) append(" Additional notes: ").append(state.customNotes).append(".")
        }
        "replace" -> buildString {
            append("Replace the selected furniture item with a new piece in the specified style.")
            if (!state.selectedFurnitureType.isNullOrBlank()) append(" Furniture type: ").append(state.selectedFurnitureType!!.replace("-", " ")).append(".")
            if (!state.selectedReplacementStyle.isNullOrBlank()) append(" Replacement style: ").append(state.selectedReplacementStyle!!.replace("-", " ")).append(".")
            if (state.replacementPrompt.isNotBlank()) append(" Description: ").append(state.replacementPrompt).append(".")
            append(" Preserve the rest of the room exactly as it is.")
        }
        else -> buildString {
            append("Redesign this room with a ")
            if (!state.selectedStyle.isNullOrBlank()) append(state.selectedStyle!!.replace("-", " ")).append(" style")
            else append("modern style")
            if (!state.selectedRoom.isNullOrBlank()) append(" for a ").append(state.selectedRoom!!.replace("-", " ")).append(" room")
            append(".")
            if (!state.selectedPalette.isNullOrBlank()) append(" Color palette: ").append(state.selectedPalette!!.replace("-", " ")).append(".")
            if (!state.selectedDesignMode.isNullOrBlank()) append(" Design mode: ").append(state.selectedDesignMode).append(".")
            if (!state.selectedBudgetMode.isNullOrBlank()) append(" Budget: ").append(state.selectedBudgetMode).append(".")
            if (state.avoidElements.isNotEmpty()) append(" Avoid: ").append(state.avoidElements.joinToString(", ")).append(".")
            if (state.keepElements.isNotEmpty()) append(" Keep: ").append(state.keepElements.joinToString(", ")).append(".")
            if (state.changeElements.isNotEmpty()) append(" Change: ").append(state.changeElements.joinToString(", ")).append(".")
            if (state.customNotes.isNotBlank()) append(" Additional notes: ").append(state.customNotes).append(".")
        }
    }
}

private fun serviceTypeForTool(toolId: String?): String = when (toolId) {
    "interior" -> "interior"
    "facade", "exterior" -> "exterior"
    "garden" -> "garden"
    "paint" -> "paint"
    "floor" -> "floor"
    "layout" -> "layout"
    "replace" -> "replace"
    "reference" -> "reference"
    else -> "interior"
}

private fun encodeToBase64(bytes: ByteArray): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    val sb = StringBuilder()
    var i = 0
    while (i < bytes.size) {
        val b0 = bytes[i].toInt() and 0xFF
        val b1 = if (i + 1 < bytes.size) bytes[i + 1].toInt() and 0xFF else 0
        val b2 = if (i + 2 < bytes.size) bytes[i + 2].toInt() and 0xFF else 0
        sb.append(chars[(b0 shr 2) and 0x3F])
        sb.append(chars[((b0 shl 4) or (b1 shr 4)) and 0x3F])
        if (i + 1 < bytes.size) sb.append(chars[((b1 shl 2) or (b2 shr 6)) and 0x3F]) else sb.append('=')
        if (i + 2 < bytes.size) sb.append(chars[b2 and 0x3F]) else sb.append('=')
        i += 3
    }
    return sb.toString()
}

private fun downscaleImageBytes(bytes: ByteArray, maxDimension: Int = 1200): ByteArray {
    val image = org.jetbrains.skia.Image.makeFromEncoded(bytes)
    val w = image.width
    val h = image.height
    val scale = if (maxOf(w, h) <= maxDimension) 1.0f else maxDimension.toFloat() / maxOf(w, h)
    if (scale >= 1.0f) return bytes
    return try {
        val newW = (w * scale).toInt()
        val newH = (h * scale).toInt()
        val info = org.jetbrains.skia.ImageInfo.makeN32Premul(newW, newH)
        val surface = org.jetbrains.skia.Surface.makeRaster(info)
        surface.canvas.drawImageRect(
            image,
            org.jetbrains.skia.Rect.makeWH(w.toFloat(), h.toFloat()),
            org.jetbrains.skia.Rect.makeWH(newW.toFloat(), newH.toFloat()),
        )
        surface.makeImageSnapshot().encodeToData()?.bytes ?: bytes
    } catch (_: Exception) {
        bytes
    }
}

private fun extractJsonString(json: String, key: String): String {
    val match = Regex("\"$key\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").find(json) ?: return ""
    return match.groupValues[1].replace("\\\"", "\"").replace("\\\\", "\\")
}

private fun extractJsonInt(json: String, key: String, default: Int = 0): Int {
    val match = Regex("\"$key\"\\s*:\\s*(-?\\d+)").find(json) ?: return default
    return match.groupValues[1].toIntOrNull() ?: default
}

private fun extractJsonArray(json: String): List<String> {
    val match = Regex("\\[.*?\\]").find(json) ?: return emptyList()
    val arrayStr = match.value
    val items = mutableListOf<String>()
    var depth = 0
    var start = -1
    var inString = false
    var escaped = false
    for (i in arrayStr.indices) {
        val c = arrayStr[i]
        when {
            escaped -> escaped = false
            c == '\\' && inString -> escaped = true
            c == '"' -> inString = !inString
            !inString && c == '{' && depth == 0 -> { start = i; depth = 1 }
            !inString && c == '{' -> depth++
            !inString && c == '}' -> {
                depth--
                if (depth == 0 && start >= 0) {
                    items.add(arrayStr.substring(start, i + 1))
                    start = -1
                }
            }
        }
    }
    return items
}

private fun stepTitle(step: WizardStep, toolId: String?): String = when (step) {
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

// ---------------------------------------------------------------------------
// Tool-specific options
// ---------------------------------------------------------------------------

private fun roomOptions(toolId: String): List<WizardOption> = when (toolId) {
    "interior" -> listOf(
        WizardOption("living-room", "Living Room"),
        WizardOption("bedroom", "Bedroom"),
        WizardOption("kitchen", "Kitchen"),
        WizardOption("bathroom", "Bathroom"),
        WizardOption("dining", "Dining Room"),
        WizardOption("office", "Home Office"),
        WizardOption("child-room", "Child's Room"),
        WizardOption("entry-hall", "Entryway"),
        WizardOption("home-cinema", "Home Cinema"),
        WizardOption("game-room", "Game Room"),
        WizardOption("library", "Library"),
        WizardOption("laundry", "Laundry Room"),
    )
    "facade", "exterior" -> listOf(
        WizardOption("single-family", "Single-Family Home"),
        WizardOption("apartment", "Apartment Building"),
        WizardOption("villa", "Villa"),
        WizardOption("office-building", "Office Building"),
        WizardOption("retail", "Retail Storefront"),
        WizardOption("hospitality", "Hospitality"),
    )
    "garden" -> listOf(
        WizardOption("modern-courtyard", "Modern Courtyard"),
        WizardOption("mediterranean", "Mediterranean"),
        WizardOption("japanese", "Japanese-Inspired"),
        WizardOption("natural-meadow", "Natural Meadow"),
        WizardOption("tropical", "Tropical Retreat"),
        WizardOption("english", "English Garden"),
        WizardOption("edible", "Edible Garden"),
        WizardOption("low-maintenance", "Low-Maintenance Garden"),
    )
    "layout" -> layoutRoomTypeOptions()
    else -> listOf(
        WizardOption("room", "Room"),
        WizardOption("space", "Space"),
    )
}

// Room type icon mapping — each room gets a distinct semantic icon
private fun roomTypeIcon(roomId: String): @Composable () -> Unit = {
    val icon = when (roomId) {
        "living-room" -> Icons.Rounded.Chair
        "bedroom" -> Icons.Rounded.WbSunny
        "kitchen" -> Icons.Rounded.Restaurant
        "bathroom" -> Icons.Rounded.Bathtub
        "office" -> Icons.Rounded.Desk
        "dining" -> Icons.Rounded.LunchDining
        "child-room" -> Icons.Rounded.ChildCare
        "home-cinema" -> Icons.Rounded.Movie
        "game-room" -> Icons.Rounded.SportsEsports
        "entry-hall" -> Icons.Rounded.DoorFront
        "library" -> Icons.Rounded.Book
        "laundry" -> Icons.Rounded.LocalLaundryService
        // Facade / exterior building types
        "single-family" -> Icons.Rounded.Cabin
        "apartment" -> Icons.Rounded.Apartment
        "villa" -> Icons.Rounded.Cabin
        "office-building" -> Icons.Rounded.Business
        "retail" -> Icons.Rounded.Store
        "hospitality" -> Icons.Rounded.Hotel
        // Garden styles
        "modern-courtyard" -> Icons.Rounded.Yard
        "mediterranean" -> Icons.Rounded.WbSunny
        "japanese" -> Icons.Rounded.Spa
        "natural-meadow" -> Icons.Rounded.Park
        "tropical" -> Icons.Rounded.Park
        "english" -> Icons.Rounded.Yard
        "edible" -> Icons.Rounded.Restaurant
        "low-maintenance" -> Icons.Rounded.Minimize
        else -> Icons.Rounded.Image
    }
    Icon(
        icon,
        contentDescription = null,
        modifier = Modifier.size(16.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

// Furniture type icon mapping
private fun furnitureTypeIcon(furnitureId: String): @Composable () -> Unit = {
    val icon = when (furnitureId) {
        "sofa" -> Icons.Rounded.Weekend
        "chair" -> Icons.Rounded.Chair
        "table" -> Icons.Rounded.TableRestaurant
        "bed" -> Icons.Rounded.Bed
        "cabinet" -> Icons.Rounded.Book
        "shelf" -> Icons.Rounded.Book
        "desk" -> Icons.Rounded.Desk
        "lamp" -> Icons.Rounded.Lightbulb
        "rug" -> Icons.Rounded.GridOn
        "plant" -> Icons.Rounded.Yard
        "art" -> Icons.Rounded.Palette
        "curtain" -> Icons.Rounded.Style
        else -> Icons.Rounded.Image
    }
    Icon(
        icon,
        contentDescription = null,
        modifier = Modifier.size(16.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

private fun styleOptions(toolId: String): List<WizardOption> = when (toolId) {
    "interior", "facade", "reference" -> listOf(
        WizardOption("ai-suggestion", "AI Suggestion"),
        WizardOption("modern", "Modern"),
        WizardOption("luxury", "Luxury"),
        WizardOption("japandi", "Japandi"),
        WizardOption("cyberpunk", "Cyberpunk"),
        WizardOption("tropical", "Tropical"),
        WizardOption("minimalist", "Minimalist"),
        WizardOption("scandinavian", "Scandinavian"),
        WizardOption("bohemian", "Bohemian"),
        WizardOption("mid-century", "Mid-Century"),
        WizardOption("art-deco", "Art Deco"),
        WizardOption("coastal", "Coastal"),
        WizardOption("rustic", "Rustic"),
        WizardOption("vintage", "Vintage"),
        WizardOption("mediterranean", "Mediterranean"),
        WizardOption("glam", "Glam"),
        WizardOption("french-country", "French Country"),
    )
    "garden" -> listOf(
        WizardOption("ai-suggestion", "AI Suggestion"),
        WizardOption("modern", "Modern"),
        WizardOption("tropical", "Tropical"),
        WizardOption("minimalist", "Minimalist"),
        WizardOption("mediterranean", "Mediterranean"),
        WizardOption("japandi", "Japandi"),
        WizardOption("rustic", "Rustic"),
        WizardOption("zen", "Zen"),
        WizardOption("english", "English Garden"),
        WizardOption("landscape", "Landscape"),
        WizardOption("bohemian", "Bohemian"),
        WizardOption("scandinavian", "Scandinavian"),
    )
    else -> listOf(
        WizardOption("ai-suggestion", "AI Suggestion"),
        WizardOption("modern", "Modern"),
        WizardOption("minimalist", "Minimalist"),
        WizardOption("scandinavian", "Scandinavian"),
    )
}

private fun materialOptions(toolId: String): List<WizardOption> = listOf(
    WizardOption("carrara-marble", "Carrara Marble"),
    WizardOption("oak-wood", "Oak Wood"),
    WizardOption("walnut", "Walnut"),
    WizardOption("concrete", "Concrete"),
    WizardOption("limewash", "Limewash"),
    WizardOption("terrazzo", "Terrazzo"),
    WizardOption("white-tile", "White Tile"),
    WizardOption("black-tile", "Black Tile"),
    WizardOption("warm-beige", "Warm Beige"),
    WizardOption("dark-elegant", "Dark Elegant"),
)

private fun layoutGoalOptions(): List<WizardOption> = listOf(
    WizardOption("open-flow", "Open Flow"),
    WizardOption("more-storage", "More Storage"),
    WizardOption("office-corner", "Office Corner"),
    WizardOption("family-space", "Family Space"),
    WizardOption("larger-living", "Larger Living Room"),
    WizardOption("better-light", "Better Light"),
    WizardOption("full-reorg", "Full Reorganization"),
    WizardOption("cozy-reading", "Cozy Reading Nook"),
    WizardOption("pet-friendly", "Pet-Friendly Space"),
    WizardOption("meditation", "Meditation Zone"),
    WizardOption("multi-use", "Multi-Use Zone"),
    WizardOption("work-from-home", "Work From Home"),
    WizardOption("kids-play", "Kids Play Area"),
)

private fun replacementSuggestionOptions(): List<WizardOption> = listOf(
    WizardOption("replace-sofa", "Replace Sofa"),
    WizardOption("replace-table", "Replace Table"),
    WizardOption("replace-lamp", "Replace Lamp"),
    WizardOption("replace-rug", "Replace Rug"),
    WizardOption("replace-wall-art", "Replace Wall Art"),
    WizardOption("replace-plant", "Replace Plant"),
    WizardOption("replace-chair", "Replace Chair"),
    WizardOption("replace-cabinet", "Replace Cabinet"),
)

private fun transferStrengthOptions(): List<WizardOption> = listOf(
    WizardOption("subtle", "Subtle"),
    WizardOption("balanced", "Balanced"),
    WizardOption("faithful", "Faithful"),
    WizardOption("very-faithful", "Very Faithful"),
)

private fun transferOptionItems(): List<WizardOption> = listOf(
    WizardOption("palette-only", "Palette Only"),
    WizardOption("materials", "Materials"),
    WizardOption("furniture", "Furniture"),
    WizardOption("light", "Light"),
    WizardOption("full-mood", "Full Mood"),
)

private fun paintColorOptions(): List<WizardOption> = listOf(
    WizardOption("pure-white", "Pure White"),
    WizardOption("warm-beige", "Warm Beige"),
    WizardOption("cool-gray", "Cool Gray"),
    WizardOption("soft-sage", "Soft Sage"),
    WizardOption("ocean-blue", "Ocean Blue"),
    WizardOption("navy", "Navy"),
    WizardOption("blush-pink", "Blush Pink"),
    WizardOption("dusty-mauve", "Dusty Mauve"),
    WizardOption("charcoal", "Charcoal"),
    WizardOption("forest-green", "Forest Green"),
    WizardOption("mustard-yellow", "Mustard Yellow"),
    WizardOption("terracotta", "Terracotta"),
    WizardOption("lavender", "Lavender"),
    WizardOption("sky-blue", "Sky Blue"),
    WizardOption("peach", "Peach"),
    WizardOption("off-white", "Off White"),
)

private fun floorStyleOptions(): List<WizardOption> = listOf(
    WizardOption("hardwood", "Hardwood"),
    WizardOption("marble", "Marble"),
    WizardOption("concrete", "Concrete"),
    WizardOption("tile", "Tile"),
    WizardOption("carpet", "Carpet"),
    WizardOption("laminate", "Laminate"),
    WizardOption("vinyl", "Vinyl"),
    WizardOption("bamboo", "Bamboo"),
    WizardOption("stone", "Stone"),
    WizardOption("terracotta", "Terracotta"),
    WizardOption("parquet", "Parquet"),
)

private fun furnitureTypeOptions(): List<WizardOption> = listOf(
    WizardOption("sofa", "Sofa"),
    WizardOption("chair", "Chair"),
    WizardOption("table", "Table"),
    WizardOption("bed", "Bed"),
    WizardOption("cabinet", "Cabinet"),
    WizardOption("shelf", "Shelf"),
    WizardOption("desk", "Desk"),
    WizardOption("lamp", "Lamp"),
    WizardOption("rug", "Rug"),
    WizardOption("plant", "Plant"),
    WizardOption("art", "Wall Art"),
    WizardOption("curtain", "Curtain"),
)

private fun replacementStyleOptions(): List<WizardOption> = listOf(
    WizardOption("modern", "Modern"),
    WizardOption("classic", "Classic"),
    WizardOption("minimalist", "Minimalist"),
    WizardOption("boho-eclectic", "Boho Eclectic"),
    WizardOption("natural", "Natural"),
    WizardOption("industrial", "Industrial"),
    WizardOption("mid-century", "Mid-Century"),
    WizardOption("scandinavian", "Scandinavian"),
    WizardOption("rustic", "Rustic"),
    WizardOption("luxury", "Luxury"),
)

private fun layoutRoomTypeOptions(): List<WizardOption> = listOf(
    WizardOption("living-room", "Living Room"),
    WizardOption("bedroom", "Bedroom"),
    WizardOption("kitchen", "Kitchen"),
    WizardOption("bathroom", "Bathroom"),
    WizardOption("office", "Home Office"),
    WizardOption("dining", "Dining Room"),
    WizardOption("child-room", "Child's Room"),
    WizardOption("entry-hall", "Entry / Hallway"),
)

private fun paletteOptions(): List<WizardOption> = listOf(
    WizardOption("curated-blend", "Curated Blend"),
    WizardOption("millennial-gray", "Millennial Gray"),
    WizardOption("terracotta-mirage", "Terracotta Mirage"),
    WizardOption("forest-tones", "Forest Tones"),
    WizardOption("peach-orchard", "Peach Orchard"),
    WizardOption("fuchsia-bloom", "Fuchsia Bloom"),
    WizardOption("emerald-gem", "Emerald Gem"),
    WizardOption("pastel-breeze", "Pastel Breeze"),
    WizardOption("ocean-mist", "Ocean Mist"),
    WizardOption("velvet-twilight", "Velvet Twilight"),
    WizardOption("amethyst-dream", "Amethyst Dream"),
    WizardOption("black-fuchsia", "Black Fuchsia"),
    WizardOption("golden-sand", "Golden Sand"),
    WizardOption("deep-blue", "Deep Blue"),
    WizardOption("powder-rose", "Powder Rose"),
    WizardOption("sage-green", "Sage Green"),
    WizardOption("warm-terracotta", "Warm Terracotta"),
    WizardOption("black-white", "Black & White"),
    WizardOption("teal-blue", "Teal Blue"),
    WizardOption("soft-mauve", "Soft Mauve"),
    WizardOption("mustard-yellow", "Mustard Yellow"),
    WizardOption("forest-green", "Forest Green"),
    WizardOption("brick-red", "Brick Red"),
    WizardOption("sky-blue", "Sky Blue"),
)

private fun paletteHexColors(paletteId: String): List<Color> = when (paletteId) {
    "curated-blend" -> listOf(Color(0xFFE8E0D8), Color(0xFFD4C5B5), Color(0xFFA89888), Color(0xFF7C6C5C))
    "millennial-gray" -> listOf(Color(0xFFD5D5D5), Color(0xFFB0B0B0), Color(0xFF8A8A8A), Color(0xFF6B6B6B))
    "terracotta-mirage" -> listOf(Color(0xFFE8A87C), Color(0xFFD4845A), Color(0xFFC06038), Color(0xFF8B4513))
    "forest-tones" -> listOf(Color(0xFF4CAF50), Color(0xFF388E3C), Color(0xFF2E7D32), Color(0xFF1B5E20))
    "peach-orchard" -> listOf(Color(0xFFFFCBA4), Color(0xFFFFB088), Color(0xFFFF9466), Color(0xFFFF7844))
    "fuchsia-bloom" -> listOf(Color(0xFFFF69B4), Color(0xFFDB3A8A), Color(0xFFB82E6E), Color(0xFF8B1A50))
    "emerald-gem" -> listOf(Color(0xFF50C878), Color(0xFF3DAF62), Color(0xFF2A964C), Color(0xFF1A7A38))
    "pastel-breeze" -> listOf(Color(0xFFF8E8FF), Color(0xFFE8D0F0), Color(0xFFD0B8E0), Color(0xFFB8A0D0))
    "ocean-mist" -> listOf(Color(0xFFB0E0E6), Color(0xFF87CEEB), Color(0xFF6CB4D9), Color(0xFF4A9ABF))
    "velvet-twilight" -> listOf(Color(0xFF6A0DAD), Color(0xFF5B0FA0), Color(0xFF4C1090), Color(0xFF3D1580))
    "amethyst-dream" -> listOf(Color(0xFF9B59B6), Color(0xFF8E44AD), Color(0xFF7D3C98), Color(0xFF6C3483))
    "black-fuchsia" -> listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460), Color(0xFFE94560))
    "golden-sand" -> listOf(Color(0xFFF4D03F), Color(0xFFE6B800), Color(0xFFCC9F00), Color(0xFFB38600))
    "deep-blue" -> listOf(Color(0xFF1A237E), Color(0xFF283593), Color(0xFF303F9F), Color(0xFF3949AB))
    "powder-rose" -> listOf(Color(0xFFF8BBD0), Color(0xFFF48FB1), Color(0xFFF06292), Color(0xFFEC407A))
    "sage-green" -> listOf(Color(0xFFBCAAA4), Color(0xFFA1887F), Color(0xFF8D6E63), Color(0xFF795548))
    "warm-terracotta" -> listOf(Color(0xFFD7CCC8), Color(0xFFBCAAA4), Color(0xFFA1887F), Color(0xFF8D6E63))
    "black-white" -> listOf(Color(0xFF212121), Color(0xFF484848), Color(0xFF909090), Color(0xFFE0E0E0))
    "teal-blue" -> listOf(Color(0xFF008080), Color(0xFF007070), Color(0xFF006060), Color(0xFF005050))
    "soft-mauve" -> listOf(Color(0xFFD8BFD8), Color(0xFFDDA0DD), Color(0xFFBA55D3), Color(0xFF9932CC))
    "mustard-yellow" -> listOf(Color(0xFFFFD54F), Color(0xFFFFCA28), Color(0xFFFFC107), Color(0xFFFFB300))
    "forest-green" -> listOf(Color(0xFF2E7D32), Color(0xFF388E3C), Color(0xFF43A047), Color(0xFF4CAF50))
    "brick-red" -> listOf(Color(0xFFB71C1C), Color(0xFFC62828), Color(0xFFD32F2F), Color(0xFFE53935))
    "sky-blue" -> listOf(Color(0xFF81D4FA), Color(0xFF4FC3F7), Color(0xFF29B6F6), Color(0xFF03A9F4))
    else -> listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD), Color(0xFF9E9E9E), Color(0xFF757575))
}

private fun designModeOptions(): List<WizardOption> = listOf(
    WizardOption("keep-structure", "Keep Structure"),
    WizardOption("free-renovate", "Free Renovate"),
)

private fun designModeDescription(modeId: String): String = when (modeId) {
    "keep-structure" -> "Keep walls, openings and volumes in place while improving the style."
    "free-renovate" -> "Allow the AI to propose a more ambitious and creative transformation."
    else -> ""
}

private fun roomStepTitle(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "What type of exterior is this?"
    "garden" -> "Choose your garden style"
    "layout" -> "What room is this?"
    else -> "Choose a room type"
}

private fun roomStepSubtitle(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "Select the building type that best matches"
    "garden" -> "Pick the style that inspires your outdoor space"
    "layout" -> "Select the room type for your floor plan"
    else -> "Select the room you want to redesign"
}

private fun roomReviewLabel(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "Exterior"
    "garden" -> "Garden Style"
    "layout" -> "Room Type"
    else -> Strings.wizardReviewRoom
}

private fun roomRequiredMessage(toolId: String?): String = when (toolId) {
    "interior" -> "Select a room type to continue"
    "facade", "exterior" -> "Select an exterior type to continue"
    "garden" -> "Select a garden style to continue"
    "layout" -> "Select a room type to continue"
    else -> "Select an option to continue"
}

private fun uploadTitleForTool(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "Upload a photo of your exterior"
    "garden" -> "Upload a photo of your outdoor area"
    "paint" -> "Upload a photo of your wall"
    "floor" -> "Upload a photo of your floor"
    "replace" -> "Upload a photo of your furniture"
    "layout" -> "Upload a photo of your floor plan"
    "reference" -> "Upload a photo of your space"
    else -> "Upload a photo of your room"
}

private fun uploadSubtitleForTool(toolId: String?): String = when (toolId) {
    "facade", "exterior" -> "Upload an exterior photo and we'll redesign it with AI in seconds"
    "garden" -> "Upload an outdoor photo and we'll redesign it with AI in seconds"
    "paint" -> "Upload a wall photo and we'll apply your chosen material with AI"
    "floor" -> "Upload a floor photo and we'll apply your chosen material with AI"
    "replace" -> "Upload a furniture photo and we'll swap it with a new piece"
    "layout" -> "Upload a floor plan and we'll optimize the layout with AI"
    "reference" -> "Upload your space and we'll match it to a reference style"
    else -> "Upload a room photo and we'll redesign it with AI in seconds"
}

private fun exampleLabelForTool(toolId: String?): String = when (toolId) {
    "paint" -> "Example wall photo"
    "floor" -> "Example floor photo"
    "layout" -> "Example room layout"
    "replace" -> "Example furniture"
    "reference" -> "Example space"
    else -> Strings.wizardExampleRoom
}

internal fun exampleImageUrlForTool(toolId: String?): String = when (toolId) {
    "interior" -> "images/assets_media_styles_example_modernwarm.webp"
    "facade", "exterior" -> "images/assets_media_exterior_modernhouse.webp"
    "garden" -> "images/assets_media_garden_landscapedpath.webp"
    "paint" -> "images/assets_media_materials_whiteplaster.webp"
    "floor" -> "images/assets_media_floor_naturaloakroom.webp"
    "layout" -> "images/assets_media_floor_floorplanannotated.webp"
    "replace" -> "images/assets_media_styles_example_minimalistsoft.webp"
    "reference" -> "images/assets_media_styles_example_scandinavianbright.webp"
    else -> EXAMPLE_FALLBACK_IMAGE_URL
}

/** Fallback image URL used when tool ID is unrecognized. Always a real photo, never a tool icon. */
internal const val EXAMPLE_FALLBACK_IMAGE_URL =
    "images/assets_media_styles_example_modernwarm.webp"

/** Aspect ratio for the fallback image (16:9). Must be finite and > 0. */
internal const val EXAMPLE_FALLBACK_ASPECT_RATIO: Float = 16f / 9f

// ---------------------------------------------------------------------------
// Building type image URL mapping (for facade/garden visual cards)
// ---------------------------------------------------------------------------

private fun buildingTypeImageUrl(typeId: String): String = when (typeId) {
    "single-family" -> "images/assets_media_exterior_modernhouse.webp"
    "apartment" -> "images/assets_media_discover_exterior_exteriorapartmentblock.webp"
    "villa" -> "images/assets_media_discover_exterior_exteriormodernvilla.webp"
    "office-building" -> "images/assets_media_discover_exterior_exteriorglassoffice.webp"
    "retail" -> "images/assets_media_discover_exterior_exteriorretailstorefront.webp"
    "hospitality" -> "images/assets_media_discover_exterior_exteriorpoolhouse.webp"
    "modern-courtyard" -> "images/assets_media_garden_landscapedpath.webp"
    "mediterranean" -> "images/assets_media_discover_garden_gardenterrace.webp"
    "japanese" -> "images/assets_media_discover_garden_gardenpoolcourtyard.webp"
    "natural-meadow" -> "images/assets_media_discover_garden_gardenfrontyard.webp"
    "tropical" -> "images/assets_media_discover_garden_gardenswimmingpool.webp"
    "english" -> "images/assets_media_discover_garden_gardenbackyard.webp"
    "edible" -> "images/assets_media_discover_garden_gardendeck.webp"
    "low-maintenance" -> "images/assets_media_discover_garden_gardenfiresidepatio.webp"
    else -> ""
}

// ---------------------------------------------------------------------------
// Style image URL mapping (matches native app drawable resources)
// ---------------------------------------------------------------------------

private fun styleImageUrl(styleId: String): String = when (styleId) {
    "modern" -> "images/assets_media_styles_example_modernwarm.webp"
    "luxury" -> "images/assets_media_styles_example_luxuryclassic.webp"
    "japandi" -> "images/assets_media_styles_example_modernorganic.webp"
    "cyberpunk" -> "images/assets_media_styles_stylecyberpunk.webp"
    "tropical" -> "images/assets_media_styles_styletropical.webp"
    "minimalist" -> "images/assets_media_styles_example_minimalistsoft.webp"
    "scandinavian" -> "images/assets_media_styles_example_scandinavianbright.webp"
    "bohemian" -> "images/assets_media_styles_example_bohemian.webp"
    "mid-century" -> "images/assets_media_styles_stylemidcentury.webp"
    "art-deco" -> "images/assets_media_styles_styleartdeco.webp"
    "coastal" -> "images/assets_media_styles_stylecoastal.webp"
    "rustic" -> "images/assets_media_styles_example_rusticfireplace.webp"
    "vintage" -> "images/assets_media_styles_stylevintage.webp"
    "mediterranean" -> "images/assets_media_styles_stylemediterranean.webp"
    "glam" -> "images/assets_media_styles_example_luxurycontemporary.webp"
    "french-country" -> "images/assets_media_styles_stylefrenchcountry.webp"
    "ai-suggestion" -> ""
    "landscape" -> "images/assets_media_garden_landscapedpath.webp"
    "zen" -> "images/assets_media_styles_example_modernorganic.webp"
    "english" -> "images/assets_media_styles_stylefrenchcountry.webp"
    "christmas" -> "images/assets_media_styles_stylefrenchcountry.webp"
    "classic" -> "images/assets_media_styles_example_luxuryclassic.webp"
    "boho-eclectic" -> "images/assets_media_styles_example_bohemian.webp"
    "natural" -> "images/assets_media_styles_example_warmneutral.webp"
    "industrial" -> "images/assets_media_styles_example_industrial.webp"
    "midcentury" -> "images/assets_media_styles_stylemidcentury.webp"
    else -> ""
}

// ---------------------------------------------------------------------------
// Material image URL mapping (texture previews for wizard material step)
// ---------------------------------------------------------------------------

private fun materialImageUrl(materialId: String): String = when (materialId) {
    "carrara-marble" -> "images/assets_media_materials_carraramarble.webp"
    "oak-wood" -> "images/assets_media_materials_oakwood.webp"
    "walnut" -> "images/assets_media_materials_darkparquet.webp"
    "concrete" -> "images/assets_media_materials_concrete.webp"
    "limewash" -> "images/assets_media_materials_whiteplaster.webp"
    "terrazzo" -> "images/assets_media_materials_terracottatile.webp"
    "white-tile" -> "images/assets_media_materials_whiteplaster.webp"
    "black-tile" -> "images/assets_media_materials_darkstone.webp"
    "warm-beige" -> "images/assets_media_materials_warmoak.webp"
    "dark-elegant" -> "images/assets_media_materials_darkstone.webp"
    else -> ""
}

// ---------------------------------------------------------------------------
// Floor style image URL mapping (floor design previews for wizard floor step)
// ---------------------------------------------------------------------------

private fun floorStyleImageUrl(floorId: String): String = when (floorId) {
    "hardwood" -> "images/assets_media_floor_naturaloakroom.webp"
    "marble" -> "images/assets_media_discover_floorscenes_polishedcarraramarble.webp"
    "concrete" -> "images/assets_media_materials_concrete.webp"
    "tile" -> "images/assets_media_materials_terracottatile.webp"
    "carpet" -> "images/assets_media_materials_carpet.webp"
    "laminate" -> "images/assets_media_floor_oakhallway.webp"
    "vinyl" -> "images/assets_media_floor_rusticliving.webp"
    "bamboo" -> "images/assets_media_materials_bamboo.webp"
    "stone" -> "images/assets_media_materials_darkstone.webp"
    "terracotta" -> "images/assets_media_materials_terracottatile.webp"
    "parquet" -> "images/assets_media_materials_darkparquet.webp"
    else -> ""
}

// ---------------------------------------------------------------------------
// Main Wizard Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WebWizardScreen(
    tool: ToolItem,
    onBack: () -> Unit,
    isGuest: Boolean = false,
    onSignIn: () -> Unit = {},
) {
    var state by remember(tool.id) { mutableStateOf(WizardState(tool = tool)) }
    var previousStep by remember { mutableStateOf(state.step) }
    var showBackDialog by remember { mutableStateOf(false) }
    var showCloseDialog by remember { mutableStateOf(false) }
    var showSignInDialog by remember { mutableStateOf(false) }
    var isDecodingImage by remember { mutableStateOf(false) }
    val maskEditorState = remember { MaskEditorState() }
    val scope = rememberCoroutineScope()
    val screenWidth = getScreenWidthDp()
    val isWide = screenWidth >= 700

    // ── Cleanup on wizard exit / disposal ─────────────────────────────
    // Cancels background jobs, releases image bytes, and resets wizard
    // state so the previous session's resources are available for GC.
    DisposableEffect(Unit) {
        onDispose {
            scope.coroutineContext[Job]?.children?.forEach { it.cancel() }
            state = WizardState(tool = null)
        }
    }



    LaunchedEffect(state.step) {
        if (state.step != previousStep) {
            previousStep = state.step
        }
    }

    LaunchedEffect(state.step) {
        if (state.step == WizardStep.Mask) {
            state = state.copy(objectSelectedOnImage = false)
        }
    }

    // Generation flow: triggered when isGenerating becomes true
    LaunchedEffect(state.isGenerating) {
        if (!state.isGenerating) return@LaunchedEffect
        val sourceBytes = state.photo?.imageBytes
        if (sourceBytes == null) {
            state = state.copy(isGenerating = false, generationError = "No image selected")
            return@LaunchedEffect
        }
        try {
            val anonymousId = getAnonymousIdFromPlatform()
            val toolId = state.tool?.id ?: "interior"
            val photo = state.photo ?: return@LaunchedEffect

            // Step 1: Create upload URL and upload source image
            val uploadUrlJson = convexCreateUploadUrl(anonymousId)
            val uploadUrl = extractJsonString(uploadUrlJson, "url")
            if (uploadUrl.isBlank()) {
                state = state.copy(isGenerating = false, generationError = "Failed to create upload URL")
                return@LaunchedEffect
            }
            val sourceBase64 = encodeToBase64(photo.imageBytes!!)
            val storageJson = convexUploadToStorage(uploadUrl, sourceBase64, photo.mimeType)
            val storageId = extractJsonString(storageJson, "storageId")
            if (storageId.isBlank()) {
                state = state.copy(isGenerating = false, generationError = "Failed to upload image")
                return@LaunchedEffect
            }

            // Step 3: Upload reference image if present (for reference)
            var referenceStorageIds: List<String> = emptyList()
            val refPhoto = state.referencePhoto
            if (toolId == "reference" && refPhoto?.imageBytes != null) {
                val refUploadUrlJson = convexCreateUploadUrl(anonymousId)
                val refUploadUrl = extractJsonString(refUploadUrlJson, "url")
                if (refUploadUrl.isNotBlank()) {
                    val refBase64 = encodeToBase64(refPhoto.imageBytes!!)
                    val refStorageJson = convexUploadToStorage(refUploadUrl, refBase64, refPhoto.mimeType)
                    val refStorageId = extractJsonString(refStorageJson, "storageId")
                    if (refStorageId.isNotBlank()) {
                        referenceStorageIds = listOf(refStorageId)
                    }
                }
            }

            // Step 4: Build prompt and call startGeneration
            val prompt = buildToolPrompt(state)
            val style = when (toolId) {
                "paint" -> state.selectedPaintColor
                "floor" -> state.selectedFloorStyle
                "reference" -> state.selectedTransferStrength
                else -> state.selectedStyle
            } ?: ""
            val roomType = state.selectedRoom ?: ""
            val palette = when (toolId) {
                "paint" -> state.selectedMaterial
                "floor" -> state.selectedMaterial
                "replace" -> state.selectedReplacementStyle
                else -> ""
            } ?: ""

            val args = mutableMapOf<String, Any?>(
                "anonymousId" to anonymousId,
                "imageStorageId" to storageId,
                "serviceType" to serviceTypeForTool(toolId),
                "selection" to palette.ifBlank { style },
                "styleSelections" to listOf(style),
                "roomType" to roomType,
                "displayStyle" to style.ifBlank { "AI Suggestion" },
                "aspectRatio" to "1:1",
                "modeId" to "default",
                "paletteId" to palette,
            )
            if (toolId == "paint" && style.isNotBlank()) {
                args["targetColor"] = style
                args["targetColorCategory"] = style
            }
            if (prompt.isNotBlank()) {
                args["customPrompt"] = prompt
            }
            if (referenceStorageIds.isNotEmpty()) {
                args["referenceImageStorageIds"] = referenceStorageIds
                args["displayStyle"] = "Reference style transfer"
            }

            val startJson = convexMutationAuth("generations:startGeneration", args)
            val generationId = extractJsonString(startJson, "generationId")
            if (generationId.isBlank()) {
                val error = extractJsonString(startJson, "message").ifBlank { "Failed to start generation" }
                state = state.copy(isGenerating = false, generationError = error)
                return@LaunchedEffect
            }

            // Step 5: Poll for result
            var imageUrl: String? = null
            repeat(45) {
                kotlinx.coroutines.delay(2000)
                val archiveJson = convexQueryAuth("generations:getUserArchive", mapOf("anonymousId" to anonymousId))
                val items = extractJsonArray(archiveJson)
                for (item in items) {
                    val itemId = extractJsonString(item, "_id")
                    val status = extractJsonString(item, "status")
                    val url = extractJsonString(item, "imageUrl")
                    if (itemId == generationId && status == "ready" && url.isNotBlank()) {
                        imageUrl = url
                        return@repeat
                    }
                    if (itemId == generationId && status == "failed") {
                        val errMsg = extractJsonString(item, "errorMessage").ifBlank { "Generation failed" }
                        state = state.copy(isGenerating = false, generationError = errMsg)
                        return@LaunchedEffect
                    }
                }
            }

            if (imageUrl != null) {
                state = state.copy(
                    isGenerating = false,
                    generationComplete = true,
                    generatedImageUrl = imageUrl,
                )
            } else {
                state = state.copy(isGenerating = false, generationError = "Generation timed out")
            }
        } catch (e: Exception) {
            state = state.copy(isGenerating = false, generationError = e.message ?: "Unknown error")
        }
    }

    val picker = rememberImagePicker { picked: PickedImageData ->
        if (picked.isValid) {
            scope.launch {
                isDecodingImage = true
                val downscaledBytes = withContext(Dispatchers.Default) {
                    picked.imageBytes?.let { downscaleImageBytes(it) } ?: picked.imageBytes
                }
                state = state.copy(photo = picked.copy(imageBytes = downscaledBytes), isUsingExample = false, error = null, isDragging = false)
                isDecodingImage = false
            }
        }
    }

    rememberDragDropHandler(
        onImageDropped = { picked ->
            if (picked.isValid) {
                scope.launch {
                    isDecodingImage = true
                    val downscaledBytes = withContext(Dispatchers.Default) {
                        picked.imageBytes?.let { downscaleImageBytes(it) } ?: picked.imageBytes
                    }
                    state = state.copy(photo = picked.copy(imageBytes = downscaledBytes), isUsingExample = false, error = null, isDragging = false)
                    isDecodingImage = false
                }
            }
        },
        onDragEnter = { state = state.copy(isDragging = true) },
        onDragLeave = { state = state.copy(isDragging = false) },
    )

    fun hasUserData(): Boolean {
        return state.photo != null || state.selectedRoom != null || state.selectedStyle != null ||
                state.selectedMaterial != null || state.selectedGoals.isNotEmpty()
    }

    fun canAdvanceFromStep(s: WizardState, mask: MaskEditorState?): Boolean = when (s.step) {
        WizardStep.Upload -> s.photo != null
        WizardStep.RoomType -> s.selectedRoom != null
        WizardStep.Style -> s.selectedStyle != null
        WizardStep.Palette -> s.selectedPalette != null
        WizardStep.Refine -> true
        WizardStep.Material -> s.selectedMaterial != null
        WizardStep.Goals -> s.selectedGoals.isNotEmpty()
        WizardStep.Mask -> mask?.hasMask == true
        WizardStep.ReplacementPrompt -> s.replacementPrompt.length >= 3
        WizardStep.TransferStrength -> s.selectedTransferStrength != null
        WizardStep.PaintColor -> s.selectedPaintColor != null
        WizardStep.FloorStyle -> s.selectedFloorStyle != null
        WizardStep.FurnitureType -> s.selectedFurnitureType != null
        WizardStep.ReplacementStyle -> s.selectedReplacementStyle != null
        WizardStep.ReferenceImage -> s.referencePhoto != null
        WizardStep.Review -> true
    }

    fun navigateBack() {
        val steps = stepsForTool(state.tool?.id)
        val currentIndex = steps.indexOf(state.step)
        if (currentIndex > 0) {
            state = state.copy(step = steps[currentIndex - 1], error = null)
        } else {
            onBack()
        }
    }

    if (showBackDialog) {
        AlertDialog(
            onDismissRequest = { showBackDialog = false },
            title = { Text("Discard progress?") },
            text = { Text("You have unsaved changes. Are you sure you want to go back?") },
            confirmButton = {
                TextButton(onClick = {
                    showBackDialog = false
                    navigateBack()
                }) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackDialog = false }) {
                    Text("Stay")
                }
            },
        )
    }

    if (showCloseDialog) {
        AlertDialog(
            onDismissRequest = { showCloseDialog = false },
            title = { Text("Discard progress?") },
            text = { Text("You have unsaved changes. Are you sure you want to close?") },
            confirmButton = {
                TextButton(onClick = {
                    showCloseDialog = false
                    onBack()
                }) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCloseDialog = false }) {
                    Text("Stay")
                }
            },
        )
    }

    // Handle browser back button — BackHandler is not available in WasmJS.
    // Browser back is handled via the Platform-level popstate listener in App.kt.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.wizardScreen)
            .onPreviewKeyEvent { event ->
                when (event.key) {
                    Key.Escape -> {
                        if (hasUserData()) {
                            showCloseDialog = true
                        } else {
                            onBack()
                        }
                        true
                    }
                    Key.DirectionLeft -> {
                        val steps = stepsForTool(state.tool?.id)
                        val idx = steps.indexOf(state.step)
                        if (idx > 0) {
                            state = state.copy(step = steps[idx - 1], error = null)
                        }
                        true
                    }
                    Key.DirectionRight -> {
                        val steps = stepsForTool(state.tool?.id)
                        val idx = steps.indexOf(state.step)
                        if (idx < steps.size - 1 && canAdvanceFromStep(state, maskEditorState)) {
                            state = state.copy(step = steps[idx + 1], error = null)
                        }
                        true
                    }
                    else -> false
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WizardHeader(
            tool = tool,
            currentStep = state.step,
            onBack = {
                val steps = stepsForTool(state.tool?.id)
                val currentIndex = steps.indexOf(state.step)
                if (currentIndex > 0 && hasUserData()) {
                    showBackDialog = true
                } else if (currentIndex > 0) {
                    state = state.copy(step = steps[currentIndex - 1], error = null)
                } else {
                    onBack()
                }
            },
            onClose = {
                if (hasUserData()) {
                    showCloseDialog = true
                } else {
                    onBack()
                }
            },
        )

        WizardProgressBar(
            currentStep = state.step,
            toolId = tool.id,
        )

        ValidationAlertBanner(
            message = state.error ?: "",
            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds()
                .testTag(Strings.formatTestTag(Strings.TestTags.wizardStepContent, state.step.name)),
        ) {
            key(state.step) {
            when (state.step) {
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
                    WizardStep.Palette -> PaletteStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(selectedPalette = it, error = null) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.Refine -> {
                        val summaryItems = remember(state) {
                            buildReviewSummary(
                                state = state,
                                onEdit = { step -> state = state.copy(step = step) },
                                stepsForTool = ::stepsForTool,
                            )
                        }
                        ReviewStep(
                            summaryItems = summaryItems,
                            generateAction = generateActionForTool(state.tool?.id),
                            onGenerate = {
                                state = state.copy(isGenerating = true, generationError = null)
                            },
                            onEditPhoto = {
                                state = state.copy(step = WizardStep.Upload)
                            },
                            toolId = state.tool?.id,
                            photo = state.photo,
                            isUsingExample = state.isUsingExample,
                            customNotes = state.customNotes,
                            onCustomNotesChange = { state = state.copy(customNotes = it) },
                            isGenerating = state.isGenerating,
                            generationComplete = state.generationComplete,
                            generationError = state.generationError,
                            onNewDesign = {
                                state = state.copy(
                                    generationComplete = false,
                                    generatedImageUrl = null,
                                    generationError = null,
                                )
                            },
                            generatedImageUrl = state.generatedImageUrl,
                            isGuest = isGuest,
                            onShowSignInDialog = { showSignInDialog = true },
                            isWide = isWide,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    WizardStep.Material -> MaterialStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(selectedMaterial = it, error = null) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.Goals -> GoalsStep(
                        state = state,
                        isWide = isWide,
                        onToggle = { goalId ->
                            val current = state.selectedGoals.toMutableList()
                            if (current.contains(goalId)) current.remove(goalId) else current.add(goalId)
                            state = state.copy(selectedGoals = current, error = null)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.Mask -> MaskStep(
                        state = state,
                        isWide = isWide,
                        maskEditorState = maskEditorState,
                        onMaskReady = {
                            state = state.copy(maskStrokes = maskEditorState.exportStrokes())
                        },
                        onStateChange = { state = it },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.ReplacementPrompt -> ReplacementPromptStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(replacementPrompt = it, error = null) },
                        onTextChange = { state = state.copy(replacementPrompt = it) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.TransferStrength -> TransferStrengthStep(
                        state = state,
                        isWide = isWide,
                        onStrengthSelect = { state = state.copy(selectedTransferStrength = it, error = null) },
                        onOptionToggle = { optionId ->
                            val current = state.selectedTransferOptions.toMutableList()
                            if (current.contains(optionId)) current.remove(optionId) else current.add(optionId)
                            state = state.copy(selectedTransferOptions = current, error = null)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.PaintColor -> PaintColorStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(selectedPaintColor = it, error = null) },
                        onNotesChange = { state = state.copy(customNotes = it) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.FloorStyle -> FloorStyleStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(selectedFloorStyle = it, error = null) },
                        onRugToggle = { state = state.copy(selectedRugOption = it) },
                        onNotesChange = { state = state.copy(customNotes = it) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.FurnitureType -> FurnitureTypeStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(selectedFurnitureType = it, error = null) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.ReplacementStyle -> ReplacementStyleStep(
                        state = state,
                        isWide = isWide,
                        onSelect = { state = state.copy(selectedReplacementStyle = it, error = null) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.ReferenceImage -> ReferenceImageStep(
                        state = state,
                        isWide = isWide,
                        onImagePicked = { picker.openGallery() },
                        onTryExample = {
                            state = state.copy(
                                referencePhoto = PickedImageData(sourceUri = "example://demo", mimeType = "image/jpeg"),
                                isUsingExample = true,
                                error = null,
                            )
                        },
                        onRemovePhoto = { state = state.copy(referencePhoto = null) },
                        modifier = Modifier.fillMaxSize(),
                    )
                    WizardStep.Review -> {
                        val summaryItems = remember(state) {
                            buildReviewSummary(
                                state = state,
                                onEdit = { step -> state = state.copy(step = step) },
                                stepsForTool = ::stepsForTool,
                            )
                        }
                        ReviewStep(
                            summaryItems = summaryItems,
                            generateAction = generateActionForTool(state.tool?.id),
                            onGenerate = {
                                state = state.copy(isGenerating = true, generationError = null)
                            },
                            onEditPhoto = {
                                state = state.copy(step = WizardStep.Upload)
                            },
                            toolId = state.tool?.id,
                            photo = state.photo,
                            isUsingExample = state.isUsingExample,
                            customNotes = state.customNotes,
                            onCustomNotesChange = { state = state.copy(customNotes = it) },
                            isGenerating = state.isGenerating,
                            generationComplete = state.generationComplete,
                            generationError = state.generationError,
                            onNewDesign = {
                                state = state.copy(
                                    generationComplete = false,
                                    generatedImageUrl = null,
                                    generationError = null,
                                )
                            },
                            generatedImageUrl = state.generatedImageUrl,
                            isGuest = isGuest,
                            onShowSignInDialog = { showSignInDialog = true },
                            isWide = isWide,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
            }
            } // key(state.step)

            if (isDecodingImage) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 8.dp,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 3.dp,
                            )
                            Text(
                                "Processing image…",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }

        WizardBottomBar(
            state = state,
            onBack = {
                val steps = stepsForTool(state.tool?.id)
                val currentIndex = steps.indexOf(state.step)
                if (currentIndex > 0) {
                    state = state.copy(step = steps[currentIndex - 1], error = null)
                } else {
                    onBack()
                }
            },
            onNext = {
                val steps = stepsForTool(state.tool?.id)
                val currentIndex = steps.indexOf(state.step)
                if (currentIndex < steps.size - 1) {
                    state = state.copy(step = steps[currentIndex + 1], error = null)
                }
            },
            onGenerate = {
                state = state.copy(isGenerating = true, generationError = null)
            },
            maskEditorState = maskEditorState,
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
    val steps = stepsForTool(tool.id)
    val currentIndex = steps.indexOf(currentStep)
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
                    contentDescription = Strings.wizardBack,
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
                    contentDescription = Strings.wizardClose,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Progress Bar (animated MD3 Expressive stepper — matches native WizardStepIndicator)
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
            // Step count label (above the dots)
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

                    // Left connector line
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

                    // Dot with pulse animation
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

                    // Right connector line
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

            // Active step label (below the dots)
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
// Step 1: Upload
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
        // Title
        Text(
            uploadTitleForTool(state.tool?.id),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            uploadSubtitleForTool(state.tool?.id),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))

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
            // Workflow explainer — "How it works" with 3 steps
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.6f),
                modifier = if (isWide) Modifier.fillMaxWidth(0.55f) else Modifier.fillMaxWidth(),
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
                    WizardTrustRow(
                        icon = Icons.Rounded.CameraAlt,
                        text = Strings.wizardWorkflowStep1,
                    )
                    WizardTrustRow(
                        icon = Icons.Rounded.Tune,
                        text = Strings.wizardWorkflowStep2,
                    )
                    WizardTrustRow(
                        icon = Icons.Rounded.AutoAwesome,
                        text = Strings.wizardWorkflowStep3,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))

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

            // Post-upload helper — explains what happens next
            Spacer(Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                modifier = if (isWide) Modifier.fillMaxWidth(0.55f) else Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        Strings.wizardUploadAfterHelper,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Photo tips
            Spacer(Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = if (isWide) Modifier.fillMaxWidth(0.5f) else Modifier.fillMaxWidth(),
            ) {
                UploadTipRow(Strings.wizardUploadPhotoTip1)
                UploadTipRow(Strings.wizardUploadPhotoTip2)
                UploadTipRow(Strings.wizardUploadPhotoTip3)
            }
            Spacer(Modifier.height(12.dp))
            // Privacy note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = if (isWide) Modifier.fillMaxWidth(0.5f) else Modifier.fillMaxWidth(),
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
        }

        // Inline validation
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
            .height(if (isWide) 240.dp else 180.dp)
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = if (highlightBorder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(56.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = if (highlightBorder) HomeDecorExtra.onGradientText else MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
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
            Spacer(Modifier.height(2.dp))
            Text(
                "or drag and drop",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
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
        shape = RoundedCornerShape(14.dp),
        color = if (isHovered)
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f),
        interactionSource = interactionSource,
        modifier = Modifier
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
                modifier = Modifier.size(20.dp),
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
                    Strings.wizardExampleCtaSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Upload photo tip row
// ---------------------------------------------------------------------------

@Composable
private fun UploadTipRow(text: String) {
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------
// Standardized local image preview helper (Web WasmJS-safe)
// ---------------------------------------------------------------------------

@Composable
internal fun LocalImagePreview(
    imageState: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isDecoding by remember { mutableStateOf(false) }

    LaunchedEffect(imageState) {
        when (imageState) {
            is PickedImageData -> {
                isDecoding = true
                imageBitmap = withContext(Dispatchers.Default) {
                    imageState.imageBytes?.let { bytes ->
                        try {
                            val downscaled = downscaleImageBytes(bytes)
                            Image.makeFromEncoded(downscaled).toComposeImageBitmap()
                        } catch (_: Exception) { null }
                    }
                }
                isDecoding = false
            }
            is ImageBitmap -> {
                imageBitmap = imageState
                isDecoding = false
            }
            else -> {
                imageBitmap = null
                isDecoding = false
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (isDecoding) {
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
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp,
                )
            }
        } else if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
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
            .aspectRatio(16f / 9f)
            .heightIn(max = 450.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
            ) {
                if (isUsingExample) {
                    NetworkImage(
                        url = exampleImageUrlForTool(toolId),
                        contentDescription = Strings.wizardExampleRoom,
                        modifier = Modifier.fillMaxSize(),
                    )
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
                                Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = HomeDecorExtra.onGradientText,
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                Strings.wizardExampleRoom,
                                style = MaterialTheme.typography.labelSmall,
                                color = HomeDecorExtra.onGradientText,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                } else {
                    LocalImagePreview(
                        imageState = photo,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = Strings.wizardPhotoSelected,
                    )
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
                        tint = HomeDecorExtra.onGradientText,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        Strings.wizardReady,
                        style = MaterialTheme.typography.labelSmall,
                        color = HomeDecorExtra.onGradientText,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Step 2: Room Type / Exterior Type / Outdoor Area
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
    val toolId = state.tool?.id ?: ""
    val useVisualCards = toolId in listOf("facade", "exterior", "garden")

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
        Spacer(Modifier.height(4.dp))
        Text(
            roomStepSubtitle(state.tool?.id),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))

        if (useVisualCards) {
            // Visual cards with images for facade/garden (2-column grid)
            val columns = if (isWide) 3 else 2
            val chunked = options.chunked(columns)

            chunked.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    row.forEach { option ->
                        val isSelected = state.selectedRoom == option.id
                        StyleImageCard(
                            label = option.label,
                            styleId = option.id,
                            isSelected = isSelected,
                            onClick = { onSelect(option.id) },
                            modifier = Modifier
                                .weight(1f)
                                .height(if (isWide) 120.dp else 100.dp),
                        )
                    }
                    repeat(columns - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        } else {
            // ExpressiveChoiceChip grid for interior rooms
            val columns = if (isWide) 3 else 2
            val chunked = options.chunked(columns)

            chunked.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    row.forEach { option ->
                        val isSelected = state.selectedRoom == option.id
                        OptionCard(
                            label = option.label,
                            isSelected = isSelected,
                            onClick = { onSelect(option.id) },
                            icon = roomTypeIcon(option.id),
                            modifier = Modifier.weight(1f),
                        )
                    }
                    repeat(columns - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(6.dp))
            }
        }

        // Inline validation
        AnimatedVisibility(visible = state.error != null) {
            Text(
                state.error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Style descriptions — short descriptors for each design style
// ---------------------------------------------------------------------------

private val styleDescriptions = mapOf(
    "ai-suggestion" to "Let AI pick the best look",
    "modern" to "Clean lines, open spaces",
    "minimalist" to "Less is more",
    "japandi" to "Japanese-Scandinavian blend",
    "scandinavian" to "Light, warm & functional",
    "luxury" to "Opulent & refined",
    "bohemian" to "Eclectic & free-spirited",
    "mid-century" to "Retro charm, timeless appeal",
    "art-deco" to "Bold geometry, rich textures",
    "coastal" to "Breezy seaside palette",
    "rustic" to "Natural warmth & character",
    "vintage" to "Classic nostalgia",
    "mediterranean" to "Sun-washed elegance",
    "glam" to "Luxe drama & sparkle",
    "french-country" to "Provincial charm & elegance",
    "cyberpunk" to "Neon-lit futurism",
    "tropical" to "Lush island vibes",
    "zen" to "Calm, balanced serenity",
    "english" to "Traditional garden elegance",
    "landscape" to "Natural outdoor beauty",
)

// ---------------------------------------------------------------------------
// Step: Style (with real images)
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
    val toolId = state.tool?.id ?: ""
    val title = when (toolId) {
        "facade", "exterior" -> "Choose an exterior style"
        "garden" -> "Choose a garden style"
        else -> "Choose a design style"
    }
    val subtitle = when (toolId) {
        "facade", "exterior" -> "Pick a style to transform your building facade"
        "garden" -> "Select a style for your outdoor space"
        else -> "Pick a style to apply to your space"
    }

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))

        val columns = if (isWide) 3 else 2
        val cardHeight = if (isWide) 130.dp else 110.dp
        val chunked = options.chunked(columns)

        chunked.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                row.forEach { option ->
                    val isSelected = state.selectedStyle == option.id
                    StyleImageCard(
                        label = option.label,
                        styleId = option.id,
                        isSelected = isSelected,
                        description = styleDescriptions[option.id],
                        onClick = { onSelect(option.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(cardHeight),
                    )
                }
                repeat(columns - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        AnimatedVisibility(visible = state.error != null) {
            Text(
                state.error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun StyleImageCard(
    label: String,
    styleId: String,
    isSelected: Boolean,
    description: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val focusRequester = remember { FocusRequester() }
    val reducedMotion = isReducedMotionEnabled()
    val animDuration = if (reducedMotion) 0 else 175

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "styleImageScale",
    )

    val targetBorderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isHovered -> MaterialTheme.colorScheme.outlineVariant
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }
    val animatedBorderColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(durationMillis = animDuration),
        label = "styleBorderColor",
    )

    val targetBorderWidth = if (isSelected) 2.dp else 1.dp
    val animatedBorderWidth by animateDpAsState(
        targetValue = targetBorderWidth,
        animationSpec = tween(durationMillis = animDuration),
        label = "styleBorderWidth",
    )

    val targetBgColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
    else
        Color.Transparent
    val animatedBgColor by animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = tween(durationMillis = animDuration),
        label = "styleBgColor",
    )

    val imageUrl = remember(styleId) { styleImageUrl(styleId) }

    Box(
        modifier = modifier
            .testTag(Strings.formatTestTag(Strings.TestTags.wizardStyleCard, styleId))
            .focusRequester(focusRequester)
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = Strings.a11yWizardOption(label, isSelected)
            }
            .scale(scale)
            .border(
                width = animatedBorderWidth,
                color = animatedBorderColor,
                shape = HomeDecorShape.Card,
            )
            .clip(HomeDecorShape.Card)
            .background(animatedBgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (imageUrl.isNotEmpty()) {
                NetworkImage(
                    url = imageUrl,
                    contentDescription = label,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, HomeDecorExtra.scrimHeavy)
                        )
                    ),
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    color = HomeDecorExtra.onGradientText,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (description != null) {
                    Text(
                        description,
                        style = MaterialTheme.typography.labelSmall,
                        color = HomeDecorExtra.onGradientTextSubtle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn(spring(stiffness = Spring.StiffnessHigh)) + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
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
                            tint = HomeDecorExtra.onGradientText,
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Step: Palette (interior, facade, garden)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PaletteStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = paletteOptions()

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            "Choose a color palette",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Select a palette to guide the color scheme of your design",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))

        val columns = if (isWide) 3 else 2
        val chunked = options.chunked(columns)

        chunked.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                row.forEach { option ->
                    val isSelected = state.selectedPalette == option.id
                    PaletteColorCard(
                        label = option.label,
                        paletteId = option.id,
                        isSelected = isSelected,
                        onClick = { onSelect(option.id) },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 100.dp),
                    )
                }
                repeat(columns - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        AnimatedVisibility(visible = state.error != null) {
            Text(
                state.error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun PaletteColorCard(
    label: String,
    paletteId: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "paletteScale",
    )

    val borderColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else if (isHovered)
        MaterialTheme.colorScheme.outline
    else
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    val colors = paletteHexColors(paletteId)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        interactionSource = interactionSource,
        modifier = modifier
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = "$label palette"
            }
            .scale(scale)
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            // Color swatch strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp)),
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(color),
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (isSelected) {
                Spacer(Modifier.height(4.dp))
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Step: Material (paint / floor)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MaterialStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = materialOptions(state.tool?.id ?: "")

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        val title = when (state.tool?.id) {
            "paint" -> "Choose a wall material"
            "floor" -> "Choose a floor material"
            else -> "Choose a material"
        }
        val subtitle = when (state.tool?.id) {
            "paint" -> "Select the material or finish for your walls"
            "floor" -> "Select the material or finish for your floors"
            else -> "Select a material or finish"
        }
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))

        if (state.selectedMaterial != null) {
            Text(
                "1 of ${options.size} materials selected",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        } else {
            SelectionRequirementMessage(text = "Select a material to continue")
        }

        Spacer(Modifier.height(16.dp))

        SelectionGrid(
            columns = if (isWide) 3 else 2,
            horizontalSpacing = 10,
            verticalSpacing = 10,
            items = options.map { option ->
                {
                    val imageUrl = remember(option.id) { materialImageUrl(option.id) }
                    SelectionCard(
                        label = option.label,
                        isSelected = state.selectedMaterial == option.id,
                        onClick = { onSelect(option.id) },
                        modifier = Modifier.heightIn(min = 80.dp),
                        thumbnail = if (imageUrl.isNotEmpty()) {
                            {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                                ) {
                                    NetworkImage(
                                        url = imageUrl,
                                        contentDescription = option.label,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                            }
                        } else null,
                    )
                }
            },
        )

        AnimatedVisibility(visible = state.error != null) {
            Text(
                state.error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step: Goals (layout)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GoalsStep(
    state: WizardState,
    isWide: Boolean,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = layoutGoalOptions()

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            "What do you want to improve?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Select one or more layout goals for your space",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))

        val columns = if (isWide) 3 else 2
        val chunked = options.chunked(columns)

        chunked.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                row.forEach { option ->
                    val isSelected = state.selectedGoals.contains(option.id)
                    OptionCard(
                        label = option.label,
                        isSelected = isSelected,
                        onClick = { onToggle(option.id) },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 90.dp),
                    )
                }
                repeat(columns - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        if (state.selectedGoals.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                "${state.selectedGoals.size} goal${if (state.selectedGoals.size > 1) "s" else ""} selected",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        AnimatedVisibility(visible = state.error != null) {
            Text(
                state.error ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step: Mask (replace)
// ---------------------------------------------------------------------------

@Composable
private fun MaskStep(
    state: WizardState,
    isWide: Boolean,
    maskEditorState: MaskEditorState,
    onMaskReady: () -> Unit,
    onStateChange: (WizardState) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            Strings.wizardMaskTitle,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            Strings.wizardMaskSubtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))

        MaskEditor(
            imageContent = {
                if (state.isUsingExample) {
                    NetworkImage(
                        url = exampleImageUrlForTool(state.tool?.id),
                        contentDescription = Strings.wizardPhotoSelected,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    LocalImagePreview(
                        imageState = state.photo,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = Strings.wizardPhotoSelected,
                    )
                }
            },
            state = maskEditorState,
            onStrokesChanged = { strokes ->
                val hasPaint = strokes.any { !it.erase && it.points.size >= 2 }
                onStateChange(
                    state.copy(
                        maskStrokes = strokes,
                        objectSelectedOnImage = hasPaint,
                        hasDrawnMask = hasPaint,
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            imageAspectRatio = if (isWide) 16f / 10f else 4f / 3f,
        )

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

// ---------------------------------------------------------------------------
// Step: Replacement Prompt (replace)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReplacementPromptStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = replacementSuggestionOptions()
    var replacementInput by remember { mutableStateOf(state.replacementPrompt) }

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            "What should replace it?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Choose a suggestion or describe what you want in the selected spot",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { option ->
                val isSelected = state.replacementPrompt == option.label
                Surface(
                    onClick = {
                        onSelect(option.label)
                        replacementInput = option.label
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp),
                    ),
                ) {
                    Text(
                        option.label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                               else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = replacementInput,
            onValueChange = {
                replacementInput = it
                onTextChange(it)
            },
            label = { Text("Or describe what you want") },
            placeholder = { Text("e.g. A modern white bookshelf") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
        )

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
// Step: Transfer Strength (reference)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TransferStrengthStep(
    state: WizardState,
    isWide: Boolean,
    onStrengthSelect: (String) -> Unit,
    onOptionToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strengthOptions = transferStrengthOptions()
    val transferOptions = transferOptionItems()

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            "How closely should the style transfer?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Choose the strength of the reference style applied to your space",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            strengthOptions.forEach { option ->
                val isSelected = state.selectedTransferStrength == option.id
                OptionCard(
                    label = option.label,
                    isSelected = isSelected,
                    onClick = { onStrengthSelect(option.id) },
                    modifier = Modifier
                        .then(
                            if (isWide) Modifier.weight(1f).heightIn(min = 80.dp)
                            else Modifier.fillMaxWidth().heightIn(min = 60.dp)
                        ),
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            "What to transfer?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Select which elements to transfer from the reference",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            transferOptions.forEach { option ->
                val isSelected = state.selectedTransferOptions.contains(option.id)
                Surface(
                    onClick = { onOptionToggle(option.id) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp),
                    ),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (isSelected) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(Modifier.width(6.dp))
                        }
                        Text(
                            option.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                   else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        )
                    }
                }
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
// Step: Paint Color (paint)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PaintColorStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = paintColorOptions()

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            "Choose a paint color",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Select a color palette for your walls",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))

        if (state.selectedPaintColor != null) {
            Text(
                "1 of ${options.size} colors selected",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        } else {
            SelectionRequirementMessage(text = "Select a color to continue")
        }

        Spacer(Modifier.height(16.dp))

        SelectionGrid(
            columns = if (isWide) 4 else 3,
            horizontalSpacing = 8,
            verticalSpacing = 8,
            items = options.map { option ->
                {
                    SelectionCard(
                        label = option.label,
                        isSelected = state.selectedPaintColor == option.id,
                        onClick = { onSelect(option.id) },
                        modifier = Modifier.heightIn(min = 72.dp),
                    )
                }
            },
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.customNotes,
            onValueChange = onNotesChange,
            label = { Text("Custom notes (optional)") },
            placeholder = { Text("e.g. Keep the ceiling white, accent wall in navy") },
            singleLine = false,
            minLines = 2,
            maxLines = 4,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
        )

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
// Step: Floor Style (floor)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FloorStyleStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    onRugToggle: (Boolean) -> Unit,
    onNotesChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = floorStyleOptions()

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            "Choose a floor style",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Select the flooring style for your space",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))

        if (state.selectedFloorStyle != null) {
            Text(
                "1 of ${options.size} styles selected",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        } else {
            SelectionRequirementMessage(text = "Select a style to continue")
        }

        Spacer(Modifier.height(16.dp))

        SelectionGrid(
            columns = if (isWide) 4 else 3,
            horizontalSpacing = 8,
            verticalSpacing = 8,
            items = options.map { option ->
                {
                    val imageUrl = remember(option.id) { floorStyleImageUrl(option.id) }
                    SelectionCard(
                        label = option.label,
                        isSelected = state.selectedFloorStyle == option.id,
                        onClick = { onSelect(option.id) },
                        modifier = Modifier.heightIn(min = 72.dp),
                        thumbnail = if (imageUrl.isNotEmpty()) {
                            {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                                ) {
                                    NetworkImage(
                                        url = imageUrl,
                                        contentDescription = option.label,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                            }
                        } else null,
                    )
                }
            },
        )

        Spacer(Modifier.height(16.dp))

        // Optional rug toggle
        Surface(
            onClick = { onRugToggle(!state.selectedRugOption) },
            shape = HomeDecorShape.Medium,
            color = if (state.selectedRugOption)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
            else
                MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (state.selectedRugOption) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.size(32.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (state.selectedRugOption) Icons.Rounded.Check
                            else Icons.Rounded.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (state.selectedRugOption) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Add an area rug",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (state.selectedRugOption) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (state.selectedRugOption) FontWeight.SemiBold else FontWeight.Medium,
                    )
                    Text(
                        "Optional: complement your flooring with a rug",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.customNotes,
            onValueChange = onNotesChange,
            label = { Text("Custom notes (optional)") },
            placeholder = { Text("e.g. Herringbone pattern, matte finish") },
            singleLine = false,
            minLines = 2,
            maxLines = 4,
            modifier = Modifier.fillMaxWidth(),
            shape = HomeDecorShape.Medium,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
        )

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

// ---------------------------------------------------------------------------
// Step: Furniture Type (replace)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FurnitureTypeStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = furnitureTypeOptions()

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            "What furniture do you want to replace?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Select the type of furniture you want to swap out",
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
                val isSelected = state.selectedFurnitureType == option.id
                OptionCard(
                    label = option.label,
                    isSelected = isSelected,
                    onClick = { onSelect(option.id) },
                    icon = furnitureTypeIcon(option.id),
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
// Step: Replacement Style (replace)
// ---------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReplacementStyleStep(
    state: WizardState,
    isWide: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = replacementStyleOptions()
    val columns = if (isWide) 3 else 2
    val cardHeight = if (isWide) 140.dp else 120.dp
    val chunked = options.chunked(columns)

    Column(
        modifier = modifier
            .fillMaxWidth(if (isWide) 0.92f else 1f)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = if (isWide) 40.dp else HomeDecorSpacing.Base)
            .padding(vertical = HomeDecorSpacing.Lg),
    ) {
        Text(
            "What style should the replacement be?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Choose a design style for the new furniture",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))

        chunked.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                row.forEach { option ->
                    val isSelected = state.selectedReplacementStyle == option.id
                    val imageUrl = remember(option.id) { styleImageUrl(option.id) }
                    StyleImageCard(
                        label = option.label,
                        styleId = option.id,
                        isSelected = isSelected,
                        onClick = { onSelect(option.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(cardHeight),
                    )
                }
                repeat(columns - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(12.dp))
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

// ---------------------------------------------------------------------------
// Step: Reference Image (reference)
// ---------------------------------------------------------------------------

@Composable
private fun ReferenceImageStep(
    state: WizardState,
    isWide: Boolean,
    onImagePicked: () -> Unit,
    onTryExample: () -> Unit,
    onRemovePhoto: () -> Unit,
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
            "Add a reference image",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Upload or select a reference image to guide the style transfer",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))

        if (state.referencePhoto != null) {
            PhotoPreview(
                photo = state.referencePhoto,
                isUsingExample = state.isUsingExample,
                toolId = "reference",
                isWide = isWide,
                onRemove = onRemovePhoto,
                onChange = onImagePicked,
            )
        } else {
            UploadDropZone(
                isWide = isWide,
                isDragging = state.isDragging,
                onClick = onImagePicked,
                onDragEnter = { },
                onDragLeave = { },
                onDrop = { },
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

@Composable
private fun CheckIconOverlay(
    isSelected: Boolean,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(spring(stiffness = Spring.StiffnessHigh)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = tint,
            )
        }
    }
}

@Composable
private fun OptionCard(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "cardScale",
    )

    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isHovered -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    val bgColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
    else
        MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f)

    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Badge,
        color = bgColor,
        interactionSource = interactionSource,
        modifier = modifier
            .height(56.dp)
            .testTag(Strings.formatTestTag(Strings.TestTags.wizardOptionCard, label))
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = Strings.a11yWizardOption(label, isSelected)
            }
            .scale(scale)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = HomeDecorShape.Badge,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.size(32.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (icon != null) {
                        icon()
                    } else {
                        Text(
                            label.take(1).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) HomeDecorExtra.onGradientText
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Spacer(Modifier.width(10.dp))

            Text(
                label,
                style = MaterialTheme.typography.titleSmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
                    modifier = Modifier.size(20.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = HomeDecorExtra.onGradientText,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloorMaterialCard(
    label: String,
    materialId: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "floorCardScale",
    )

    val materialColor = when (materialId) {
        "hardwood" -> Color(0xFF8B5A2B)
        "marble" -> Color(0xFFF0EDE8)
        "concrete" -> Color(0xFF9E9E9E)
        "tile" -> Color(0xFFB0BEC5)
        "carpet" -> Color(0xFF795548)
        "laminate" -> Color(0xFFD7CCC8)
        "vinyl" -> Color(0xFFA1887F)
        "bamboo" -> Color(0xFF8D6E63)
        "stone" -> Color(0xFF607D8B)
        "terracotta" -> Color(0xFFBF360C)
        "parquet" -> Color(0xFF6D4C41)
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isHovered -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = Color.Transparent,
        interactionSource = interactionSource,
        modifier = modifier
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = Strings.a11yWizardOption(label, isSelected)
            }
            .scale(scale)
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = borderColor,
                shape = HomeDecorShape.Card,
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(HomeDecorShape.Card),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                materialColor.copy(alpha = 0.3f),
                                materialColor.copy(alpha = 0.7f),
                            )
                        )
                    ),
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (materialId == "marble" || materialId == "laminate")
                        MaterialTheme.colorScheme.onSurface
                    else
                        HomeDecorExtra.onGradientText,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(22.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = HomeDecorExtra.onGradientText,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MaterialSwatchCard(
    label: String,
    materialId: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "swatchScale",
    )

    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isHovered -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    val (baseColor, patternType) = when (materialId) {
        "carrara-marble" -> Pair(Color(0xFFF5F0EB), "marble")
        "oak-wood" -> Pair(Color(0xFFC49A6C), "wood")
        "walnut" -> Pair(Color(0xFF5C4033), "wood")
        "concrete" -> Pair(Color(0xFF9E9E9E), "concrete")
        "limewash" -> Pair(Color(0xFFF5F0E8), "limewash")
        "terrazzo" -> Pair(Color(0xFFE8E0D0), "terrazzo")
        "white-tile" -> Pair(Color(0xFFF0F0F0), "tile")
        "black-tile" -> Pair(Color(0xFF2A2A2A), "tile")
        "warm-beige" -> Pair(Color(0xFFD4C0A8), "flat")
        "dark-elegant" -> Pair(Color(0xFF3A3A3A), "flat")
        else -> Pair(MaterialTheme.colorScheme.primaryContainer, "flat")
    }

    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Card,
        color = Color.Transparent,
        interactionSource = interactionSource,
        modifier = modifier
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = Strings.a11yWizardOption(label, isSelected)
            }
            .scale(scale)
            .border(
                width = if (isSelected) 2.5.dp else 1.dp,
                color = borderColor,
                shape = HomeDecorShape.Card,
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(HomeDecorShape.Card),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = baseColor)
                when (patternType) {
                    "marble" -> {
                        val veinDark = Color(0xFFB0A89C).copy(alpha = 0.7f)
                        val veinLight = Color(0xFFD5CFC8).copy(alpha = 0.5f)
                        drawLine(veinDark, start = Offset(0f, size.height * 0.15f), end = Offset(size.width * 0.6f, size.height), strokeWidth = 4f)
                        drawLine(veinDark, start = Offset(size.width * 0.25f, 0f), end = Offset(size.width * 0.85f, size.height * 0.85f), strokeWidth = 3f)
                        drawLine(veinLight, start = Offset(size.width * 0.5f, size.height * 0.05f), end = Offset(size.width, size.height * 0.65f), strokeWidth = 2.5f)
                        drawLine(veinDark, start = Offset(size.width * 0.1f, size.height * 0.55f), end = Offset(size.width * 0.7f, size.height * 0.3f), strokeWidth = 2f)
                        drawLine(veinLight, start = Offset(size.width * 0.4f, size.height * 0.7f), end = Offset(size.width * 0.95f, size.height * 0.15f), strokeWidth = 1.5f)
                    }
                    "wood" -> {
                        val grainDark = baseColor.copy(alpha = 0.6f)
                        val grainLight = baseColor.copy(alpha = 0.35f)
                        for (i in 0..8) {
                            val y = size.height * (0.08f + i * 0.1f)
                            val sw = if (i % 3 == 0) 2.5f else 1.5f
                            drawLine(grainDark, start = Offset(0f, y), end = Offset(size.width, y + 3f), strokeWidth = sw)
                        }
                        drawCircle(grainDark, radius = 6f, center = Offset(size.width * 0.7f, size.height * 0.4f))
                        drawCircle(grainLight, radius = 3f, center = Offset(size.width * 0.7f, size.height * 0.4f))
                    }
                    "concrete" -> {
                        val speckDark = baseColor.copy(alpha = 0.7f)
                        val speckLight = Color(0xFFB0B0B0).copy(alpha = 0.4f)
                        for (i in 0..30) {
                            val cx = size.width * (0.03f + (i * 0.31f % 1f) * 0.94f)
                            val cy = size.height * (0.05f + (i * 0.47f % 1f) * 0.9f)
                            val r = 1.5f + (i % 3).toFloat()
                            drawCircle(if (i % 2 == 0) speckDark else speckLight, radius = r, center = Offset(cx, cy))
                        }
                        val crackColor = baseColor.copy(alpha = 0.55f)
                        drawLine(crackColor, start = Offset(size.width * 0.2f, 0f), end = Offset(size.width * 0.35f, size.height * 0.4f), strokeWidth = 1f)
                        drawLine(crackColor, start = Offset(size.width * 0.6f, size.height * 0.6f), end = Offset(size.width * 0.8f, size.height), strokeWidth = 1f)
                    }
                    "limewash" -> {
                        val mottle1 = baseColor.copy(alpha = 0.45f)
                        val mottle2 = Color(0xFFE8DFD2).copy(alpha = 0.35f)
                        for (i in 0..20) {
                            val cx = size.width * (0.05f + (i * 0.37f % 1f) * 0.9f)
                            val cy = size.height * (0.05f + (i * 0.53f % 1f) * 0.9f)
                            val r = 4f + (i % 4).toFloat() * 3f
                            drawCircle(if (i % 3 == 0) mottle1 else mottle2, radius = r, center = Offset(cx, cy))
                        }
                    }
                    "terrazzo" -> {
                        val chipColors = listOf(
                            Color(0xFFC49A6C).copy(alpha = 0.7f),
                            Color(0xFF8B7355).copy(alpha = 0.6f),
                            Color(0xFFA0522D).copy(alpha = 0.65f),
                            Color(0xFF6B8E6B).copy(alpha = 0.55f),
                            Color(0xFF9E8B7E).copy(alpha = 0.5f),
                        )
                        for (i in 0..18) {
                            val cx = size.width * (0.06f + (i * 0.29f % 1f) * 0.88f)
                            val cy = size.height * (0.08f + (i * 0.43f % 1f) * 0.84f)
                            val r = 3f + (i % 5).toFloat() * 2f
                            drawCircle(chipColors[i % chipColors.size], radius = r, center = Offset(cx, cy))
                        }
                    }
                    "tile" -> {
                        val gridColor = baseColor.copy(alpha = 0.55f)
                        val cols = 3
                        val rows = 4
                        val cellW = size.width / cols
                        val cellH = size.height / rows
                        for (c in 1 until cols) {
                            drawLine(gridColor, start = Offset(c * cellW, 0f), end = Offset(c * cellW, size.height), strokeWidth = 2f)
                        }
                        for (r in 1 until rows) {
                            drawLine(gridColor, start = Offset(0f, r * cellH), end = Offset(size.width, r * cellH), strokeWidth = 2f)
                        }
                        val shadeColor = baseColor.copy(alpha = 0.15f)
                        for (c in 0 until cols) {
                            for (r in 0 until rows) {
                                drawRect(shadeColor, topLeft = Offset(c * cellW + 2f, r * cellH + 2f), size = Size(cellW - 4f, cellH - 4f))
                            }
                        }
                    }
                    else -> {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.08f),
                                Color.Black.copy(alpha = 0.12f),
                            )
                        )
                        drawRect(gradient, size = size)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                        ),
                    ),
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (materialId in listOf("carrara-marble", "limewash", "white-tile", "warm-beige"))
                        MaterialTheme.colorScheme.onSurface
                    else
                        Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(22.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaintColorSwatch(
    label: String,
    colorId: String,
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

    val swatchColor = when (colorId) {
        "pure-white" -> Color(0xFFFAFAFA)
        "warm-beige" -> Color(0xFFD7CCC8)
        "cool-gray" -> Color(0xFF90A4AE)
        "soft-sage" -> Color(0xFFA5D6A7)
        "ocean-blue" -> Color(0xFF4FC3F7)
        "navy" -> Color(0xFF1A237E)
        "blush-pink" -> Color(0xFFF8BBD0)
        "dusty-mauve" -> Color(0xFFCE93D8)
        "charcoal" -> Color(0xFF424242)
        "forest-green" -> Color(0xFF2E7D32)
        "mustard-yellow" -> Color(0xFFFFD54F)
        "terracotta" -> Color(0xFFBF360C)
        "lavender" -> Color(0xFFB39DDB)
        "sky-blue" -> Color(0xFF81D4FA)
        "peach" -> Color(0xFFFFAB91)
        "off-white" -> Color(0xFFF5F5F5)
        else -> Color(0xFFE0E0E0)
    }

    Surface(
        onClick = onClick,
        modifier = modifier
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                contentDescription = "$label paint color"
            }
            .scale(scale)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp),
            ),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else
            MaterialTheme.colorScheme.surface,
        interactionSource = interactionSource,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = CircleShape,
                color = swatchColor,
                modifier = Modifier.size(48.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                ),
            ) {
                CheckIconOverlay(isSelected = isSelected, tint = if (colorId == "pure-white" || colorId == "off-white" || colorId == "mustard-yellow" || colorId == "warm-beige") MaterialTheme.colorScheme.onSurface else HomeDecorExtra.onGradientText)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun WizardTrustRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
    onGenerate: () -> Unit = {},
    maskEditorState: MaskEditorState? = null,
    modifier: Modifier = Modifier,
) {
    val steps = stepsForTool(state.tool?.id)
    val isLastStep = state.step == steps.lastOrNull()
    val showGenerate = state.step == WizardStep.Review
    val canProceed = when (state.step) {
        WizardStep.Upload -> state.photo != null
        WizardStep.RoomType -> state.selectedRoom != null
        WizardStep.Style -> state.selectedStyle != null
        WizardStep.Palette -> state.selectedPalette != null
        WizardStep.Refine -> true
        WizardStep.Material -> state.selectedMaterial != null
        WizardStep.Goals -> state.selectedGoals.isNotEmpty()
        WizardStep.Mask -> maskEditorState?.hasMask == true
        WizardStep.ReplacementPrompt -> state.replacementPrompt.length >= 3
        WizardStep.TransferStrength -> state.selectedTransferStrength != null
        WizardStep.PaintColor -> state.selectedPaintColor != null
        WizardStep.FloorStyle -> state.selectedFloorStyle != null
        WizardStep.FurnitureType -> state.selectedFurnitureType != null
        WizardStep.ReplacementStyle -> state.selectedReplacementStyle != null
        WizardStep.ReferenceImage -> state.referencePhoto != null
        WizardStep.Review -> true
    }
    val generateEnabled = !state.isGenerating && !state.generationComplete
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && canProceed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "nextScale",
    )
    val validationHint = when {
        state.step == WizardStep.Review -> ""
        !canProceed -> validationHintForBottomBar(state.step)
        else -> ""
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.testTag(Strings.TestTags.wizardBottomBar),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = HomeDecorSpacing.Base, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.step != WizardStep.Upload) {
                    Surface(
                        onClick = onBack,
                        shape = HomeDecorShape.Medium,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier.height(44.dp).testTag(Strings.TestTags.wizardBackStepButton),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
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
                if (!showGenerate) {
                    Surface(
                        onClick = onNext,
                        shape = HomeDecorShape.Pill,
                        color = if (canProceed) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainerHigh,
                        enabled = canProceed,
                        interactionSource = interactionSource,
                        modifier = Modifier.height(48.dp).testTag(Strings.TestTags.wizardNextStepButton).scale(scale),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (canProceed) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                Strings.wizardNext,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (canProceed) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                } else {
                    Surface(
                        onClick = { if (generateEnabled) onGenerate() },
                        shape = HomeDecorShape.Pill,
                        color = Color(0xFF1D5C5E),
                        enabled = generateEnabled,
                        modifier = Modifier.height(48.dp).testTag(Strings.TestTags.wizardGenerateButton).scale(scale),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White,
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                when {
                                    state.isGenerating -> Strings.wizardGenerating
                                    state.generationComplete -> Strings.wizardResultReady
                                    else -> "Generate Design \u00B7 1 \uD83D\uDC8E"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = validationHint.isNotEmpty()) {
                Text(
                    validationHint,
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

private fun validationHintForBottomBar(step: WizardStep): String = when (step) {
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

private fun buildReviewSummary(
    state: WizardState,
    onEdit: (WizardStep) -> Unit,
    stepsForTool: (String?) -> List<WizardStep>,
): List<ReviewSummaryItem> {
    val toolId = state.tool?.id
    val steps = stepsForTool(toolId)
    val items = mutableListOf<ReviewSummaryItem>()
    for (step in steps) {
        if (step == WizardStep.Upload || step == WizardStep.Review) continue
        if (step == WizardStep.Mask && !state.hasDrawnMask) continue
        val label = stepReviewLabel(step, toolId)
        val value = stepReviewValue(step, state, toolId) ?: continue
        items.add(ReviewSummaryItem(label, value, { onEdit(step) }, "review_${step.name}"))
    }
    return items
}

private fun labelForMaterialId(id: String): String =
    materialOptions("").find { it.id == id }?.label ?: id.replace("-", " ")

private fun labelForPaintColorId(id: String): String =
    paintColorOptions().find { it.id == id }?.label ?: id.replace("-", " ")

private fun labelForFloorStyleId(id: String): String =
    floorStyleOptions().find { it.id == id }?.label ?: id.replace("-", " ")

private fun labelForStyleId(id: String, toolId: String? = null): String =
    styleOptions(toolId ?: "").find { it.id == id }?.label
        ?: id.replace("-", " ").replaceFirstChar { it.uppercase() }

private fun labelForReplacementStyleId(id: String): String =
    replacementStyleOptions().find { it.id == id }?.label
        ?: id.replace("-", " ").replaceFirstChar { it.uppercase() }

private fun labelForRowStyleId(id: String): String =
    id.replace("-", " ").replaceFirstChar { it.uppercase() }

private fun stepReviewLabel(step: WizardStep, toolId: String?): String = when (step) {
    WizardStep.RoomType -> roomReviewLabel(toolId)
    WizardStep.Style -> when (toolId) {
        "facade", "exterior" -> "Exterior Style"
        "garden" -> "Garden Style"
        else -> "Design Style"
    }
    WizardStep.Palette -> "Color Palette"
    WizardStep.Refine -> "Design Vibe"
    WizardStep.Material -> when (toolId) {
        "paint" -> "Wall Material"
        "floor" -> "Floor Material"
        else -> "Material"
    }
    WizardStep.Goals -> "Layout Goals"
    WizardStep.Mask -> "Mask Area"
    WizardStep.ReplacementPrompt -> "Description"
    WizardStep.TransferStrength -> "Style Strength"
    WizardStep.PaintColor -> "Paint Color"
    WizardStep.FloorStyle -> "Floor Style"
    WizardStep.FurnitureType -> "Furniture Type"
    WizardStep.ReplacementStyle -> "Replacement Style"
    WizardStep.ReferenceImage -> "Reference"
    WizardStep.Upload, WizardStep.Review -> ""
}

private fun stepReviewValue(step: WizardStep, state: WizardState, toolId: String?): String? = when (step) {
    WizardStep.RoomType -> state.selectedRoom?.let { labelForRowStyleId(it) }
    WizardStep.Style -> state.selectedStyle?.let { labelForStyleId(it, toolId) }
    WizardStep.Palette -> state.selectedPalette?.let { labelForRowStyleId(it) }
    WizardStep.Refine -> state.selectedDesignMode ?: "Standard"
    WizardStep.Material -> state.selectedMaterial?.let { labelForMaterialId(it) }
    WizardStep.Goals -> state.selectedGoals.joinToString(", ") { labelForRowStyleId(it) }
    WizardStep.Mask -> "Mask drawn"
    WizardStep.ReplacementPrompt -> state.replacementPrompt.take(50).let { "$it\u2026" }
    WizardStep.TransferStrength -> state.selectedTransferStrength?.let { labelForRowStyleId(it) }
    WizardStep.PaintColor -> state.selectedPaintColor?.let { labelForPaintColorId(it) }
    WizardStep.FloorStyle -> state.selectedFloorStyle?.let { style ->
        val label = labelForFloorStyleId(style)
        if (state.selectedRugOption) "$label (+ area rug)" else label
    }
    WizardStep.FurnitureType -> state.selectedFurnitureType?.let { labelForRowStyleId(it) }
    WizardStep.ReplacementStyle -> state.selectedReplacementStyle?.let { labelForReplacementStyleId(it) }
    WizardStep.ReferenceImage -> if (state.referencePhoto != null) "Photo selected" else "Using example"
    WizardStep.Upload, WizardStep.Review -> null
}

