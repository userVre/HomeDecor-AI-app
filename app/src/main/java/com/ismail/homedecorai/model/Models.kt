package com.ismail.homedecorai.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import com.ismail.homedecorai.CreativeWorkspaceState
import com.ismail.homedecorai.ViewerSummary

enum class MainTab { Tools, Discover, Upgrade, MyBoard, Profile, Create }
enum class WizardStage { Photo, Space, Style, Refine, Processing, Result }
enum class ElitePassSyncState { Loading, Synced, Syncing, LocalOnly, Error }

data class DecorTool(
    val id: String,
    val title: String,
    val description: String,
    val imageRes: Int,
    val serviceType: String,
)

data class AdvancedControlSpec(
    val keepOptions: List<String>,
    val changeOptions: List<String>,
)

data class GalleryItem(
    val id: String,
    val title: String,
    val category: String,
    val imageRes: Int,
)

data class DiscoverSection(
    val id: String,
    val title: String,
    val cluster: String,
    val serviceToolId: String,
    val items: List<GalleryItem>,
)

data class BoardItem(
    val id: String,
    val toolTitle: String,
    val style: String,
    val roomType: String,
    val imageRes: Int,
    val imageUri: String? = null,
    val imageUrl: String? = null,
    val sourceImageUri: String? = null,
    val sourceImageUrl: String? = null,
    val status: String = "ready",
    val errorMessage: String? = null,
    val prompt: String? = null,
    val budgetLabel: String = "",
    val createdAt: Double = 0.0,
)

fun BoardItem.isGeneratedResult(): Boolean =
    status == "ready" && (imageUrl?.isNotBlank() == true || imageRes != 0)

data class SelectedPhoto(
    val uri: Uri? = null,
    val exampleLabel: String? = null,
)

data class MaskPoint(val x: Float, val y: Float)

data class MaskStroke(
    val points: List<MaskPoint>,
    val brushSize: Float,
    val erase: Boolean = false,
)

fun List<MaskStroke>.hasVisibleMaskPaint(): Boolean {
    val bitmap = toMaskBitmap(size = 128)
    var found = false
    for (x in 0 until bitmap.width step 2) {
        for (y in 0 until bitmap.height step 2) {
            if (Color.alpha(bitmap.getPixel(x, y)) > 0) {
                found = true
                break
            }
        }
        if (found) break
    }
    if (bitmap.isRecycled.not()) bitmap.recycle()
    return found
}

fun String.isValidReplacementPrompt(): Boolean =
    trim().let { prompt -> prompt.length >= 3 && prompt.any { it.isLetterOrDigit() } }

internal fun List<MaskStroke>.toMaskBitmap(size: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    val strokeScale = size / 1024f
    forEach { stroke ->
        if (stroke.points.size < 2) return@forEach
        paint.strokeWidth = stroke.brushSize.coerceIn(8f, 96f) * strokeScale
        paint.color = if (stroke.erase) Color.TRANSPARENT else Color.WHITE
        paint.xfermode = if (stroke.erase) android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR) else null
        val path = Path().apply {
            val first = stroke.points.first()
            moveTo(first.x.coerceIn(0f, 1f) * size, first.y.coerceIn(0f, 1f) * size)
            stroke.points.drop(1).forEach { point ->
                lineTo(point.x.coerceIn(0f, 1f) * size, point.y.coerceIn(0f, 1f) * size)
            }
        }
        canvas.drawPath(path, paint)
    }
    return bitmap
}

data class DiamondPack(
    val id: String,
    val title: String,
    val diamonds: Int,
    val price: String,
    val badge: String? = null,
    val description: String = "",
)

sealed class PendingPurchaseSync {
    data class Diamond(
        val packId: String,
        val transactionId: String,
        val productIdentifier: String,
        val packageIdentifier: String?,
        val amount: Double,
        val currencyCode: String,
        val purchasedAt: Double,
    ) : PendingPurchaseSync()

    data class Subscription(
        val plan: String,
        val subscriptionType: String,
        val entitlement: String,
        val purchasedAt: Double?,
        val subscriptionEnd: Double?,
    ) : PendingPurchaseSync()
}

data class HomeDecorUiState(
    val isAppReady: Boolean = false,
    val selectedTab: MainTab = MainTab.Tools,
    val selectedTool: DecorTool = DecorTool("", "", "", 0, ""),
    val wizardStage: WizardStage = WizardStage.Photo,
    val selectedPhotoUri: Uri? = null,
    val selectedExampleLabel: String? = null,
    val selectedPhotos: List<SelectedPhoto> = emptyList(),
    val selectedReferenceUri: Uri? = null,
    val selectedReferenceExampleLabel: String? = null,
    val selectedReferenceDiscoverItemId: String? = null,
    val selectedRooms: List<String> = emptyList(),
    val selectedStyles: List<String> = emptyList(),
    val selectedPalettes: List<String> = emptyList(),
    val roomType: String = "",
    val style: String = "",
    val palette: String = "",
    val designMode: String = "Preserve Layout",
    val budgetMode: String = "",
    val avoidOptions: List<String> = emptyList(),
    val keepOptions: List<String> = emptyList(),
    val changeOptions: List<String> = emptyList(),
    val preserveRestOfImage: Boolean = false,
    val customPrompt: String = "",
    val layoutConstraints: String = "",
    val mobilierASupprimer: String = "",
    val mobilierADeplacer: String = "",
    val progressMessage: String = "",
    val diamonds: Int = 1,
    val eliteStreakDay: Int = 1,
    val claimedToday: Boolean = false,
    val elitePassSyncState: ElitePassSyncState = ElitePassSyncState.Loading,
    val elitePassSyncMessage: String = "",
    val eliteLastClaimWasDaySeven: Boolean = false,
    val storeVisible: Boolean = false,
    val paywallVisible: Boolean = false,
    val authVisible: Boolean = false,
    val settingsVisible: Boolean = false,
    val signedInName: String? = null,
    val signedInEmail: String? = null,
    val viewer: ViewerSummary = ViewerSummary(),
    val board: List<BoardItem> = emptyList(),
    val disclosureAccepted: Boolean = false,
    val isPro: Boolean = false,
    val generationError: String? = null,
    val maskStrokes: List<MaskStroke> = emptyList(),
    val undoneMaskStrokes: List<MaskStroke> = emptyList(),
    val brushSize: Float = 28f,
    val eraserSelected: Boolean = false,
    val purchaseMessage: String? = null,
    val purchaseBusy: Boolean = false,
    val pendingPurchaseSync: PendingPurchaseSync? = null,
    val settingsMessage: String? = null,
    val settingsBusy: Boolean = false,
    val workspace: CreativeWorkspaceState = CreativeWorkspaceState(),
    val designViewerVisible: Boolean = false,
    val designViewerResult: BoardItem? = null,
)
