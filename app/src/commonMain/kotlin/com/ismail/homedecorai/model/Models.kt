package com.ismail.homedecorai.model

import androidx.compose.ui.graphics.Color
import com.ismail.homedecorai.ui.theme.HomeDecorColors

data class ToolItem(
    val id: String,
    val title: String,
    val description: String,
    val gradientStart: Color = HomeDecorColors.Primary,
    val gradientEnd: Color = HomeDecorColors.OnPrimaryContainer,
    val imageUrl: String = "",
    val accentColor: Color = HomeDecorColors.PrimaryContainer,
)

data class ToolsScreenState(
    val isPro: Boolean = false,
    val diamonds: Int = 0,
    val tools: List<ToolItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class GalleryCardItem(
    val id: String,
    val title: String,
    val category: String,
    val styleType: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val room: String = "",
    val color: String = "",
    val mood: String = "",
)

data class DiscoverSectionItem(
    val id: String,
    val title: String,
    val cluster: String,
    val items: List<GalleryCardItem>,
)

data class DiscoverScreenState(
    val favoriteSourceIds: Set<String> = emptySet(),
    val moodboardSourceIds: Set<String> = emptySet(),
    val sections: List<DiscoverSectionItem> = emptyList(),
    val selectedCluster: String = "interior",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignedIn: Boolean = false,
    val noResultsMessage: String? = null,
)

data class BoardItem(
    val id: String,
    val toolTitle: String = "",
    val style: String = "",
    val roomType: String = "",
    val imageUrl: String? = null,
    val imageUri: String? = null,
    val sourceImageUri: String? = null,
    val sourceImageUrl: String? = null,
    val status: String = "completed",
    val errorMessage: String? = null,
    val prompt: String? = null,
    val budgetLabel: String = "",
    val createdAt: Double = 0.0,
    val isFavorite: Boolean = false,
)

data class BoardScreenState(
    val generatedItems: List<BoardItem> = emptyList(),
    val favoriteItems: List<BoardItem> = emptyList(),
    val projectItems: List<BoardItem> = emptyList(),
    val localGuestDesigns: List<BoardItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

enum class BoardTab { Generated, Favorites, Projects }

data class ProfileScreenState(
    val isGuest: Boolean = true,
    val signedInName: String? = null,
    val signedInEmail: String? = null,
    val diamonds: Int = 0,
    val isPro: Boolean = false,
    val favoritesCount: Int = 0,
    val savedDesigns: List<BoardItem> = emptyList(),
)

data class SettingsLanguage(
    val tag: String,
    val label: String,
)

data class SettingsScreenState(
    val versionName: String = "1.0.0",
    val settingsBusy: Boolean = false,
    val isSignedIn: Boolean = false,
    val signedInName: String? = null,
    val signedInEmail: String? = null,
    val diamonds: Int = 0,
)

data class DiamondPackage(
    val id: String,
    val name: String,
    val diamonds: Int,
    val price: String,
    val pricePerDiamond: String,
    val badge: String? = null,
    val gradientStart: Color,
    val gradientEnd: Color,
)

data class DiamondStoreState(
    val currentDiamonds: Int = 0,
    val packages: List<DiamondPackage> = emptyList(),
    val isLoading: Boolean = false,
    val purchaseInProgress: String? = null,
    val purchaseSuccess: Boolean = false,
    val purchaseError: String? = null,
    val transactionStatus: TransactionStatus = TransactionStatus.Idle,
    val canClaimDaily: Boolean = true,
    val dailyBonusAmount: Int = 1,
    val lastClaimAt: Long = 0,
    val nextClaimAt: Long = 0,
    val streakCount: Int = 0,
)

enum class TransactionStatus {
    Idle, Processing, Success, Failed, Restored
}

sealed class SettingsDialog {
    data object None : SettingsDialog()
    data object Language : SettingsDialog()
    data object Feedback : SettingsDialog()
    data object DeleteAccount : SettingsDialog()
    data object Logout : SettingsDialog()
}

enum class AuthMode { SignIn, SignUp }

// ---------------------------------------------------------------------------
// SampleImage  –  Typed model for route-specific sample images
// ---------------------------------------------------------------------------

enum class SampleImageCategory {
    Interior,
    Exterior,
    Garden,
    Floor,
    WallPaint,
    Layout,
    ReplaceFurniture,
    Reference,
}

data class SampleImage(
    val id: String,
    val route: String,
    val category: SampleImageCategory,
    val title: String,
    val description: String,
    val aspectRatio: Float,
    val resourceUrl: String,
) {
    init {
        require(id.isNotBlank()) { "SampleImage id must not be blank" }
        require(route.isNotBlank()) { "SampleImage route must not be blank" }
        require(title.isNotBlank()) { "SampleImage title must not be blank" }
        require(resourceUrl.isNotBlank()) { "SampleImage resourceUrl must not be blank" }
        require(aspectRatio.isFinite() && aspectRatio > 0f) {
            "SampleImage aspectRatio must be finite and positive, got $aspectRatio"
        }
    }

    val isValid: Boolean
        get() = id.isNotBlank() &&
                route.isNotBlank() &&
                resourceUrl.isNotBlank() &&
                aspectRatio.isFinite() &&
                aspectRatio > 0f
}

/** Allowed categories per tool route. */
val routeAllowedCategories: Map<String, Set<SampleImageCategory>> = mapOf(
    "interior" to setOf(SampleImageCategory.Interior),
    "facade" to setOf(SampleImageCategory.Exterior),
    "exterior" to setOf(SampleImageCategory.Exterior),
    "garden" to setOf(SampleImageCategory.Garden),
    "paint" to setOf(SampleImageCategory.WallPaint),
    "floor" to setOf(SampleImageCategory.Floor),
    "layout" to setOf(SampleImageCategory.Layout),
    "replace" to setOf(SampleImageCategory.ReplaceFurniture),
    "reference" to setOf(SampleImageCategory.Reference),
)

/** Fallback image URL per route with valid nonzero aspect ratios. */
val routeFallbackImageUrls: Map<String, Pair<String, Float>> = mapOf(
    "interior" to ("images/assets_media_styles_example_modernwarm.webp" to 16f / 9f),
    "facade" to ("images/assets_media_exterior_modernhouse.webp" to 16f / 9f),
    "exterior" to ("images/assets_media_exterior_modernhouse.webp" to 16f / 9f),
    "garden" to ("images/assets_media_garden_landscapedpath.webp" to 16f / 9f),
    "paint" to ("images/assets_media_materials_whiteplaster.webp" to 16f / 9f),
    "floor" to ("images/assets_media_floor_naturaloakroom.webp" to 16f / 9f),
    "layout" to ("images/assets_media_floor_floorplanannotated.webp" to 16f / 9f),
    "replace" to ("images/assets_media_styles_example_minimalistsoft.webp" to 16f / 9f),
    "reference" to ("images/assets_media_styles_example_scandinavianbright.webp" to 16f / 9f),
)

/**
 * Canonical sample image registry. Every entry is validated at class-load time:
 *  - aspectRatio must be finite and > 0
 *  - resourceUrl must reference an existing resource pattern
 *  - category must match the allowed set for its route
 */
object SampleImages {

    val all: List<SampleImage> = listOf(
        SampleImage(
            id = "interior-livingroom1",
            route = "interior",
            category = SampleImageCategory.Interior,
            title = "Modern living room",
            description = "Bright open-plan living room with contemporary furniture",
            aspectRatio = 16f / 9f,
            resourceUrl = "images/assets_media_styles_example_modernwarm.webp",
        ),
        SampleImage(
            id = "facade-modernvilla",
            route = "facade",
            category = SampleImageCategory.Exterior,
            title = "Modern villa exterior",
            description = "Sleek villa facade with clean architectural lines",
            aspectRatio = 16f / 9f,
            resourceUrl = "images/assets_media_exterior_modernhouse.webp",
        ),
        SampleImage(
            id = "garden-patio",
            route = "garden",
            category = SampleImageCategory.Garden,
            title = "Backyard patio garden",
            description = "Outdoor patio with landscaped greenery and seating",
            aspectRatio = 16f / 9f,
            resourceUrl = "images/assets_media_garden_landscapedpath.webp",
        ),
        SampleImage(
            id = "paint-sagegreensuite",
            route = "paint",
            category = SampleImageCategory.WallPaint,
            title = "Sage green bedroom walls",
            description = "Bedroom with freshly painted sage green accent walls",
            aspectRatio = 16f / 9f,
            resourceUrl = "images/assets_media_materials_whiteplaster.webp",
        ),
        SampleImage(
            id = "floor-naturaloakparquet",
            route = "floor",
            category = SampleImageCategory.Floor,
            title = "Natural oak parquet flooring",
            description = "Warm natural oak parquet floor in a sunlit room",
            aspectRatio = 16f / 9f,
            resourceUrl = "images/assets_media_floor_naturaloakroom.webp",
        ),
        SampleImage(
            id = "layout-livingroom1",
            route = "layout",
            category = SampleImageCategory.Layout,
            title = "Open-plan living room layout",
            description = "Spacious room with visible floor plan arrangement",
            aspectRatio = 16f / 9f,
            resourceUrl = "images/assets_media_floor_floorplanannotated.webp",
        ),
        SampleImage(
            id = "replace-livingroom2",
            route = "replace",
            category = SampleImageCategory.ReplaceFurniture,
            title = "Living room with furniture",
            description = "Furnished living room ready for furniture replacement",
            aspectRatio = 16f / 9f,
            resourceUrl = "images/assets_media_styles_example_minimalistsoft.webp",
        ),
        SampleImage(
            id = "reference-homelivingroom",
            route = "reference",
            category = SampleImageCategory.Reference,
            title = "Contemporary living room",
            description = "Style-specific interior for design reference transfer",
            aspectRatio = 16f / 9f,
            resourceUrl = "images/assets_media_styles_example_scandinavianbright.webp",
        ),
    ).also { images ->
        images.forEach { image ->
            require(image.isValid) { "Invalid SampleImage: ${image.id}" }
            val allowed = routeAllowedCategories[image.route]
            requireNotNull(allowed) { "Unknown route '${image.route}' in SampleImage ${image.id}" }
            require(image.category in allowed) {
                "SampleImage ${image.id} has category ${image.category} but route '${image.route}' " +
                        "only allows $allowed"
            }
        }
    }

    /** Default aspect ratio used when no image-specific ratio is available. */
    const val DEFAULT_ASPECT_RATIO: Float = 16f / 9f

    /** Aspect ratio for tool card icons (4:3). */
    const val TOOL_CARD_ASPECT_RATIO: Float = 4f / 3f

    /** Aspect ratio for wizard preview / upload (16:9). */
    const val WIZARD_PREVIEW_ASPECT_RATIO: Float = 16f / 9f

    /** Aspect ratio for gallery cards (4:3). */
    const val GALLERY_ASPECT_RATIO: Float = 4f / 3f

    /**
     * Validate that an aspect ratio is finite, positive, and within a sane range.
     * Returns the ratio unchanged if valid, or [fallback] if not.
     */
    fun validateAspectRatio(ratio: Float, fallback: Float = DEFAULT_ASPECT_RATIO): Float {
        return if (ratio.isFinite() && ratio > 0f && ratio < 10f) ratio else fallback
    }

    /**
     * Resolve the fallback image URL for a given route.
     * Always returns a valid URL with a finite, nonzero aspect ratio.
     */
    fun fallbackForRoute(route: String): Pair<String, Float> {
        return routeFallbackImageUrls[route]
            ?: (DEFAULT_FALLBACK_URL to DEFAULT_ASPECT_RATIO)
    }

    private const val DEFAULT_FALLBACK_URL =
        "images/assets_media_styles_example_modernwarm.webp"
}
