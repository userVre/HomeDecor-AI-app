package com.ismail.homedecorai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.UUID

enum class MainTab { Tools, Create, Discover, Profile }
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
    for (x in 0 until bitmap.width step 2) {
        for (y in 0 until bitmap.height step 2) {
            if (android.graphics.Color.alpha(bitmap.getPixel(x, y)) > 0) {
                return true
            }
        }
    }
    return false
}

fun String.isValidReplacementPrompt(): Boolean =
    trim().let { prompt -> prompt.length >= 3 && prompt.any { it.isLetterOrDigit() } }

private fun List<MaskStroke>.toMaskBitmap(size: Int): Bitmap {
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
    val selectedTab: MainTab = MainTab.Tools,
    val selectedTool: DecorTool = HomeDecorCatalog.tools.first(),
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
)

object HomeDecorCatalog {
    val tools = listOf(
        DecorTool(
            id = "interior",
            title = "Design d'intérieur",
            description = "Redéfinissez votre espace avec l'IA.",
            imageRes = R.drawable.tool_interior,
            serviceType = "redesign",
        ),
        DecorTool(
            id = "facade",
            title = "Conception extérieure",
            description = "Réinventez l'extérieur de votre maison avec un style de façade de classe mondiale.",
            imageRes = R.drawable.tool_exterior,
            serviceType = "redesign",
        ),
        DecorTool(
            id = "garden",
            title = "Conception de jardin",
            description = "Concevez de superbes jardins et espaces extérieurs sans effort.",
            imageRes = R.drawable.tool_garden,
            serviceType = "redesign",
        ),
        DecorTool(
            id = "paint",
            title = "Peinture intelligente",
            description = "Affinez vos murs avec des palettes de couleurs sur mesure et des textures design.",
            imageRes = R.drawable.tool_paint,
            serviceType = "paint",
        ),
        DecorTool(
            id = "floor",
            title = "Relooking du sol",
            description = "Élevez les fondations de votre pièce avec des matériaux et des finitions haut de gamme.",
            imageRes = R.drawable.tool_floor,
            serviceType = "floor",
        ),
        DecorTool(
            id = "layout",
            title = "Agencement Intelligent",
            description = "Optimisez l'agencement pour un confort maximal.",
            imageRes = R.drawable.tool_layout,
            serviceType = "layout",
        ),
        DecorTool(
            id = "replace",
            title = "Remplacer des objets",
            description = "Masquez un objet dans votre photo et remplacez-le avec des retouches AI précises.",
            imageRes = R.drawable.tool_replace,
            serviceType = "replace",
        ),
        DecorTool(
            id = "reference",
            title = "Transfert de style de référence",
            description = "Importez une référence visuelle et appliquez son style à votre pièce.",
            imageRes = R.drawable.tool_reference,
            serviceType = "reference",
        ),
    )

    val rooms = listOf(
        "Salon",
        "Chambre à coucher",
        "Cuisine",
        "Salle de bain",
        "Bureau à domicile",
        "Salle à manger",
        "Chambre d'enfant",
        "Cinéma maison",
        "Salle de jeux",
        "Entrée / couloir",
        "Bibliothèque",
        "Blanchisserie",
    )

    val buildingTypes = listOf(
        "Appartement",
        "Maison",
        "Immeuble de bureaux",
        "Résidentiel",
        "Vente au détail",
        "Villa",
    )

    val outdoorSpaces = listOf(
        "Cour arrière",
        "Terrasse",
        "Patio",
        "Cour",
        "Piscine",
        "Jardin avant",
    )

    val gardenStyles = listOf(
        "Noël",
        "Moderne",
        "Tropicale",
        "Minimaliste",
        "Méditerranéen",
        "Japandi",
        "Rustique",
        "Zen",
        "Anglais",
        "Paysage",
        "Bohème",
        "Scandinave",
    )

    val maskTargets = listOf(
        "Mur",
        "Sol",
        "Sofa",
        "Table",
        "Cabinet",
        "Éclairage",
    )

    val materialLibrary = listOf(
        "Marbre",
        "Chêne",
        "Noyer",
        "Béton",
        "Limewash",
        "Terrazzo",
        "Carrelage blanc",
        "Carrelage noir",
        "Peinture beige chaude",
        "Peinture sombre élégante",
    )

    val floorMaterials = materialLibrary

    val layoutGoals = listOf(
        "Circulation ouverte",
        "Plus de rangement",
        "Coin bureau",
        "Espace familial",
        "Salon plus spacieux",
        "Meilleure lumière",
        "Réorganisation complète",
    )

    val referenceStrengths = listOf(
        "Subtil",
        "Équilibré",
        "Fidèle",
        "Très fidèle",
    )

    val referenceOptions = listOf(
        "Palette seulement",
        "Matériaux",
        "Mobilier",
        "Lumière",
        "Ambiance complète",
    )

    val budgetModes = listOf(
        "Low budget",
        "Medium budget",
        "Luxury",
    )

    val avoidOptions = listOf(
        "no dark colors",
        "no structural changes",
        "no plants",
        "keep windows",
        "no furniture changes",
    )

    val advancedControlSpecs = mapOf(
        "interior" to AdvancedControlSpec(
            keepOptions = listOf("agencement", "fenêtres", "sol", "mobilier principal"),
            changeOptions = listOf("style", "couleurs", "décor", "éclairage"),
        ),
        "facade" to AdvancedControlSpec(
            keepOptions = listOf("structure", "fenêtres", "toit", "entrée"),
            changeOptions = listOf("façade", "couleurs", "éclairage", "paysage"),
        ),
        "garden" to AdvancedControlSpec(
            keepOptions = listOf("agencement", "arbres", "piscine", "terrasse", "clôture"),
            changeOptions = listOf("plantes", "éclairage", "mobilier", "chemins"),
        ),
        "layout" to AdvancedControlSpec(
            keepOptions = listOf("murs", "fenêtres", "portes", "mobilier important"),
            changeOptions = listOf("organisation", "circulation", "rangement", "zones"),
        ),
        "reference" to AdvancedControlSpec(
            keepOptions = listOf("agencement", "mobilier", "couleurs principales"),
            changeOptions = listOf("style", "ambiance", "matériaux", "décor"),
        ),
    )

    val protectRestToolIds = setOf("replace", "paint", "floor")

    val paintColors = materialLibrary

    val replaceSuggestions = listOf(
        "Remplacer le sofa",
        "Remplacer la table",
        "Remplacer la lampe",
        "Remplacer le tapis",
        "Remplacer l'art mural",
        "Remplacer la plante",
        "Remplacer la chaise",
        "Remplacer le cabinet",
    )

    val replacementTemplatePrompts = mapOf(
        "Remplacer le sofa" to "modern sofa matching the room scale, perspective, and light",
        "Remplacer la table" to "refined table matching the room scale, perspective, and light",
        "Remplacer la lampe" to "elegant lamp matching the room scale, perspective, and light",
        "Remplacer le tapis" to "textured area rug matching the room scale, perspective, and light",
        "Remplacer l'art mural" to "framed wall art matching the room scale, perspective, and light",
        "Remplacer la plante" to "healthy indoor plant matching the room scale, perspective, and light",
        "Remplacer la chaise" to "comfortable accent chair matching the room scale, perspective, and light",
        "Remplacer le cabinet" to "streamlined cabinet matching the room scale, perspective, and light",
    )

    val styles = listOf(
        "Moderne",
        "Luxe",
        "Japandi",
        "Cyberpunk",
        "Tropicale",
        "Minimaliste",
        "Scandinave",
        "Bohème",
        "Midcentury",
        "Art Deco",
        "Côtier",
        "Rustique",
        "Vintage",
        "Méditerranéen",
        "Glam",
        "Campagne française",
    )

    val palettes = listOf(
        "Mélange organisé",
        "Gris millénaire",
        "Mirage en terre cuite",
        "Teintes forestières",
        "Verger de pêchers",
        "Fleur fuchsia",
        "Gemme d'émeraude",
        "Brise pastel",
        "Brume océanique",
        "Crépuscule de velours",
        "Rêve d'améthyste",
        "Fuchsia Noir",
        "Sable doré",
        "Bleu profond",
        "Rose poudré",
        "Vert sauge",
        "Terracotta chaleureux",
        "Noir et blanc",
        "Bleu canard",
        "Mauve doux",
        " Jaune moutarde",
        " Vert forêt",
        " Rouge brique",
        " Bleu ciel",
    )

    val designModes = listOf(
        "Conserver la structure" to "Gardez les murs, ouvertures et volumes en place tout en améliorant le style.",
        "Rénover librement" to "Autorisez l'IA à proposer une transformation plus ambitieuse et créative.",
    )

    val diamondPacks = listOf(
        DiamondPack("starter", "Découverte", 10, "19,80 MAD", description = "Pour tester plusieurs idées sans engagement."),
        DiamondPack("designer", "Designer", 30, "49,65 MAD", "POPULAIRE", "Le meilleur équilibre pour explorer une pièce complète."),
        DiamondPack("architect", "Architecte", 100, "129,25 MAD", description = "Pensé pour les séries de concepts et variantes."),
        DiamondPack("estate", "Studio", 300, "249,00 MAD", "MEILLEURE OFFRE", "Crédits profonds pour gros projets et portfolios."),
    )

    val gallery = tools.mapIndexed { index, tool ->
        GalleryItem(
            id = tool.id,
            title = tool.title,
            category = if (index < 3) "Spaces" else "Tools",
            imageRes = tool.imageRes,
        )
    }

    private fun numberedDiscoverItems(idPrefix: String, category: String, vararg imageRes: Int): List<GalleryItem> =
        imageRes.mapIndexed { index, image ->
            val number = index + 1
            GalleryItem("$idPrefix-$number", "$category $number", category, image)
        }

    val discoverSections = listOf(
        DiscoverSection(
            id = "kitchen",
            title = "Cuisine",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "kitchen",
                "Cuisine",
                R.drawable.assets_media_discover_generated_kitchen_kitchen1,
                R.drawable.assets_media_discover_generated_kitchen_kitchen2,
                R.drawable.assets_media_discover_generated_kitchen_kitchen3,
                R.drawable.assets_media_discover_generated_kitchen_kitchen4,
                R.drawable.assets_media_discover_generated_kitchen_kitchen5,
                R.drawable.assets_media_discover_generated_kitchen_kitchen6,
                R.drawable.assets_media_discover_generated_kitchen_kitchen7,
            ),
        ),
        DiscoverSection(
            id = "living-room",
            title = "Salon",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "living",
                "Salon",
                R.drawable.assets_media_discover_generated_livingroom_livingroom1,
                R.drawable.assets_media_discover_generated_livingroom_livingroom2,
                R.drawable.assets_media_discover_generated_livingroom_livingroom3,
                R.drawable.assets_media_discover_generated_livingroom_livingroom4,
                R.drawable.assets_media_discover_generated_livingroom_livingroom5,
                R.drawable.assets_media_discover_generated_livingroom_livingroom6,
                R.drawable.assets_media_discover_generated_livingroom_livingroom7,
            ),
        ),
        DiscoverSection(
            id = "bedroom",
            title = "Chambre",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "bedroom",
                "Chambre",
                R.drawable.assets_media_discover_generated_bedroom_bedroom1,
                R.drawable.assets_media_discover_generated_bedroom_bedroom2,
                R.drawable.assets_media_discover_generated_bedroom_bedroom3,
                R.drawable.assets_media_discover_generated_bedroom_bedroom4,
                R.drawable.assets_media_discover_generated_bedroom_bedroom5,
                R.drawable.assets_media_discover_generated_bedroom_bedroom6,
                R.drawable.assets_media_discover_generated_bedroom_bedroom7,
            ),
        ),
        DiscoverSection(
            id = "bathroom",
            title = "Salle de bain",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "bathroom",
                "Salle de bain",
                R.drawable.assets_media_discover_home_homebathroom,
                R.drawable.assets_media_discover_wallscenes_lavendermistbath,
                R.drawable.assets_media_styles_styleluxury,
            ),
        ),
        DiscoverSection(
            id = "dining",
            title = "Salle à manger",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "dining",
                "Salle à manger",
                R.drawable.assets_media_discover_home_homediningroom,
                R.drawable.assets_media_styles_styleartdeco,
                R.drawable.assets_media_styles_stylemediterranean,
            ),
        ),
        DiscoverSection(
            id = "home-office",
            title = "Bureau à domicile",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "office",
                "Bureau",
                R.drawable.assets_media_discover_home_homehomeoffice,
                R.drawable.assets_media_discover_home_homestudy,
                R.drawable.assets_media_styles_stylemidcentury,
            ),
        ),
        DiscoverSection(
            id = "library",
            title = "Bibliothèque",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "library",
                "Bibliothèque",
                R.drawable.assets_media_discover_home_homelibrary,
                R.drawable.assets_media_styles_stylevintage,
                R.drawable.assets_media_styles_stylerustic,
            ),
        ),
        DiscoverSection(
            id = "hall",
            title = "Entrée / couloir",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "hall",
                "Entrée",
                R.drawable.assets_media_discover_home_homehall,
                R.drawable.assets_media_styles_stylefrenchcountry,
                R.drawable.assets_media_styles_stylecoastal,
            ),
        ),
        DiscoverSection(
            id = "gaming",
            title = "Salle de jeux",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "gaming",
                "Loisir",
                R.drawable.assets_media_discover_home_homegamingroom,
                R.drawable.assets_media_styles_stylecyberpunk,
                R.drawable.assets_media_styles_stylemodern,
            ),
        ),
        DiscoverSection(
            id = "laundry",
            title = "Blanchisserie",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = numberedDiscoverItems(
                "laundry",
                "Service",
                R.drawable.assets_media_discover_home_homelaundry,
                R.drawable.assets_media_styles_stylescandinavian,
                R.drawable.assets_media_styles_styleminimalist,
            ),
        ),
        DiscoverSection(
            id = "villa",
            title = "Villa",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "villa",
                "Villa",
                R.drawable.assets_media_discover_exterior_exteriormodernvilla,
                R.drawable.assets_media_discover_generated_exterior_exterior1,
                R.drawable.assets_media_discover_generated_exterior_exterior2,
            ),
        ),
        DiscoverSection(
            id = "house",
            title = "Maison",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "house",
                "Maison",
                R.drawable.tool_exterior,
                R.drawable.assets_media_discover_generated_exterior_exterior7,
            ),
        ),
        DiscoverSection(
            id = "apartment",
            title = "Appartement",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "apartment",
                "Appartement",
                R.drawable.assets_media_discover_exterior_exteriorapartmentblock,
                R.drawable.assets_media_discover_generated_exterior_exterior3,
            ),
        ),
        DiscoverSection(
            id = "office-building",
            title = "Immeuble de bureaux",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "office-building",
                "Immeuble de bureaux",
                R.drawable.assets_media_discover_exterior_exteriorglassoffice,
                R.drawable.assets_media_discover_generated_exterior_exterior4,
            ),
        ),
        DiscoverSection(
            id = "retail",
            title = "Vente au détail",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "retail",
                "Vente au détail",
                R.drawable.assets_media_discover_exterior_exteriorretailstorefront,
                R.drawable.assets_media_discover_generated_exterior_exterior5,
            ),
        ),
        DiscoverSection(
            id = "residential",
            title = "Résidentiel",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = numberedDiscoverItems(
                "residential",
                "Résidentiel",
                R.drawable.assets_media_discover_exterior_exteriorpoolhouse,
                R.drawable.assets_media_discover_exterior_exteriorstonemanor,
                R.drawable.assets_media_discover_generated_exterior_exterior6,
            ),
        ),
        DiscoverSection(
            id = "wall-scenes",
            title = "Murs",
            cluster = "Intérieurs",
            serviceToolId = "paint",
            items = listOf(
                GalleryItem("wall-1", "Ivoire doux", "Mur", R.drawable.assets_media_discover_wallscenes_softivorykitchen),
                GalleryItem("wall-2", "Vert sauge", "Mur", R.drawable.assets_media_discover_wallscenes_sagegreensuite),
                GalleryItem("wall-3", "Bleu nuit", "Mur", R.drawable.assets_media_discover_wallscenes_midnightnavybedroom),
                GalleryItem("wall-4", "Charbon galerie", "Mur", R.drawable.assets_media_discover_wallscenes_gallerycharcoallounge),
                GalleryItem("wall-5", "Terre cuite", "Mur", R.drawable.assets_media_discover_wallscenes_terracottadining),
                GalleryItem("wall-6", "Rose poudré", "Mur", R.drawable.assets_media_discover_wallscenes_dustyroseretreat),
                GalleryItem("wall-7", "Vert olive", "Mur", R.drawable.assets_media_discover_wallscenes_deepolivestudy),
                GalleryItem("wall-8", "Gris perle", "Mur", R.drawable.assets_media_discover_wallscenes_pearlgraysalon),
            ),
        ),
        DiscoverSection(
            id = "floors",
            title = "Sols",
            cluster = "Intérieurs",
            serviceToolId = "floor",
            items = listOf(
                GalleryItem("floor-1", "Chêne naturel", "Sol", R.drawable.assets_media_discover_floorscenes_naturaloakparquet),
                GalleryItem("floor-2", "Noyer", "Sol", R.drawable.assets_media_discover_floorscenes_heritagewalnutplank),
                GalleryItem("floor-3", "Marbre", "Sol", R.drawable.assets_media_discover_floorscenes_polishedcarraramarble),
                GalleryItem("floor-4", "Béton poli", "Sol", R.drawable.assets_media_discover_floorscenes_industrialgrayconcrete),
                GalleryItem("floor-5", "Chevron", "Sol", R.drawable.assets_media_discover_floorscenes_walnutchevron),
                GalleryItem("floor-6", "Terre cuite", "Sol", R.drawable.assets_media_discover_floorscenes_terracottaateliertile),
                GalleryItem("floor-7", "Carrelage ardoise", "Sol", R.drawable.assets_media_discover_floorscenes_modernslatetile),
                GalleryItem("floor-8", "Tapis ivoire", "Sol", R.drawable.assets_media_discover_floorscenes_plushivorycarpet),
                GalleryItem("floor-9", "Chêne patiné", "Sol", R.drawable.assets_media_discover_floorscenes_weatheredoakstudio),
            ),
        ),
        DiscoverSection(
            id = "garden",
            title = "Jardin",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "garden",
                "Jardin",
                R.drawable.assets_media_discover_garden_gardenfiresidepatio,
                R.drawable.assets_media_discover_generated_garden_garden1,
                R.drawable.assets_media_discover_generated_garden_garden2,
            ),
        ),
        DiscoverSection(
            id = "backyard",
            title = "Cour arrière",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "backyard",
                "Cour arrière",
                R.drawable.assets_media_discover_garden_gardenbackyard,
                R.drawable.assets_media_discover_generated_garden_garden3,
            ),
        ),
        DiscoverSection(
            id = "terrace",
            title = "Terrasse",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "terrace",
                "Terrasse",
                R.drawable.assets_media_discover_garden_gardenterrace,
                R.drawable.assets_media_discover_garden_gardendeck,
            ),
        ),
        DiscoverSection(
            id = "patio",
            title = "Patio",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "patio",
                "Patio",
                R.drawable.assets_media_discover_garden_gardenpatio,
                R.drawable.assets_media_discover_generated_garden_garden4,
            ),
        ),
        DiscoverSection(
            id = "yard",
            title = "Cour",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "yard",
                "Cour",
                R.drawable.assets_media_discover_generated_garden_garden5,
                R.drawable.assets_media_discover_generated_garden_garden6,
                R.drawable.assets_media_discover_generated_garden_garden7,
            ),
        ),
        DiscoverSection(
            id = "pool",
            title = "Piscine",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "pool",
                "Piscine",
                R.drawable.assets_media_discover_garden_gardenswimmingpool,
                R.drawable.assets_media_discover_garden_gardenpoolcourtyard,
            ),
        ),
        DiscoverSection(
            id = "front-garden",
            title = "Jardin avant",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = numberedDiscoverItems(
                "front-garden",
                "Jardin avant",
                R.drawable.assets_media_discover_garden_gardenfrontyard,
                R.drawable.assets_media_discover_garden_gardenvillaentry,
            ),
        ),
    )
}

class HomeDecorViewModel(
    private val repository: HomeDecorRepository,
    context: Context,
    private val workspaceStore: LocalWorkspaceStore = LocalWorkspaceStore(context),
) : ViewModel() {
    private val appContext = context.applicationContext
    private val preferences = context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private var anonymousId = preferences.getString(KEY_ANONYMOUS_ID, null) ?: newAnonymousId()
    private val _uiState = MutableStateFlow(
        HomeDecorUiState(
            progressMessage = text(R.string.progress_preparing_studio),
            elitePassSyncMessage = text(R.string.elite_pass_sync_initial),
            disclosureAccepted = preferences.getBoolean(KEY_DISCLOSURE_ACCEPTED, false),
            workspace = workspaceStore.state.value,
            board = workspaceStore.state.value.generatedResults.toBoardItems(),
        )
    )
    val uiState: StateFlow<HomeDecorUiState> = _uiState.asStateFlow()

    private fun text(@StringRes resId: Int): String = appContext.getString(resId)

    init {
        viewModelScope.launch {
            workspaceStore.state.collect { workspace ->
                _uiState.update { state ->
                    state.copy(
                        workspace = workspace,
                        board = if (state.board.isEmpty()) workspace.generatedResults.toBoardItems() else state.board,
                    )
                }
            }
        }
        viewModelScope.launch {
            runCatching { repository.bootstrapViewer(anonymousId) }
                .recoverCatching { repository.viewerSummary(anonymousId) }
                .onSuccess { viewer ->
                    val archive = repository.archive(anonymousId).mapNotNull { it.toBoardItem() }
                    archive.forEach { workspaceStore.upsertGeneratedResult(it.toGeneratedResult(_uiState.value)) }
                    _uiState.update {
                        it.copy(
                            viewer = viewer,
                            diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                            isPro = viewer.hasProAccess,
                            eliteStreakDay = viewer.nextElitePassDay(),
                            claimedToday = viewer.claimedWithinLocalDay(),
                            eliteLastClaimWasDaySeven = viewer.wasDaySevenEliteClaim(),
                            elitePassSyncState = ElitePassSyncState.Synced,
                            elitePassSyncMessage = text(R.string.elite_pass_synced),
                            board = archive,
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            elitePassSyncState = ElitePassSyncState.LocalOnly,
                            elitePassSyncMessage = text(R.string.elite_pass_local_sync),
                        )
                    }
                }
        }
    }

    fun selectTab(tab: MainTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun startTool(tool: DecorTool) {
        _uiState.update {
            it.copy(
                selectedTab = MainTab.Create,
                selectedTool = tool,
                wizardStage = WizardStage.Photo,
                selectedPhotoUri = null,
                selectedExampleLabel = null,
                selectedPhotos = emptyList(),
                selectedReferenceUri = null,
                selectedReferenceExampleLabel = null,
                selectedReferenceDiscoverItemId = null,
                selectedRooms = emptyList(),
                selectedStyles = emptyList(),
                selectedPalettes = emptyList(),
                roomType = "",
                style = "",
                palette = "",
                designMode = HomeDecorCatalog.designModes.first().first,
                budgetMode = "",
                avoidOptions = emptyList(),
                keepOptions = emptyList(),
                changeOptions = emptyList(),
                preserveRestOfImage = false,
                customPrompt = "",
                layoutConstraints = "",
                mobilierASupprimer = "",
                mobilierADeplacer = "",
                maskStrokes = emptyList(),
                undoneMaskStrokes = emptyList(),
                brushSize = 28f,
                eraserSelected = false,
                generationError = null,
            )
        }
    }

    fun acceptDisclosure() {
        preferences.edit().putBoolean(KEY_DISCLOSURE_ACCEPTED, true).apply()
        _uiState.update { it.copy(disclosureAccepted = true) }
    }

    fun createProject(name: String, roomType: String = "", notes: String = "", styleInfo: String = ""): Project {
        return workspaceStore.createProject(
            name = name,
            roomType = roomType,
            notes = notes,
            styleInfo = styleInfo,
        )
    }

    fun updateProject(project: Project) {
        workspaceStore.upsertProject(project)
    }

    fun deleteProject(projectId: String) {
        workspaceStore.deleteProject(projectId)
    }

    fun toggleFavorite(result: BoardItem?): Boolean {
        if (result == null || !result.isGeneratedResult()) return false
        val generated = result.toGeneratedResult(_uiState.value)
        workspaceStore.upsertGeneratedResult(generated)
        return workspaceStore.toggleFavorite(generated)
    }

    fun addResultToProject(result: BoardItem?, projectId: String): Boolean {
        if (result == null || projectId.isBlank() || !result.isGeneratedResult()) return false
        val generated = result.toGeneratedResult(_uiState.value, projectId)
        workspaceStore.upsertGeneratedResult(generated)
        _uiState.update { state ->
            state.copy(board = state.board.map { if (it.id == result.id) result else it })
        }
        return true
    }

    fun createProjectFromResult(
        name: String,
        roomType: String,
        notes: String,
        styleInfo: String,
        result: BoardItem?,
    ): Project? {
        if (result == null || !result.isGeneratedResult()) return null
        val snapshot = _uiState.value
        val sourceUris = snapshot.selectedPhotos.mapNotNull { it.uri?.toString() }
            .ifEmpty { snapshot.selectedPhotoUri?.toString()?.let(::listOf) ?: emptyList() }
        val inferredStyleInfo = listOf(
            snapshot.style.ifBlank { result.style },
            snapshot.palette,
            snapshot.designMode,
        )
            .filter { it.isNotBlank() }
            .joinToString(" - ")
        val project = workspaceStore.createProject(
            name = name,
            roomType = roomType.ifBlank { snapshot.roomType.ifBlank { result.roomType } },
            coverImageUrl = result.imageUrl,
            originalPhotoUris = sourceUris,
            originalPhotoUrls = listOfNotNull(result.sourceImageUrl),
            notes = notes,
            styleInfo = styleInfo.ifBlank { inferredStyleInfo },
        )
        addResultToProject(result, project.id)
        return project
    }

    fun addResultToMoodboard(result: BoardItem?, projectId: String? = null): Boolean {
        if (result == null || !result.isGeneratedResult()) return false
        val generated = result.toGeneratedResult(_uiState.value, projectId)
        workspaceStore.upsertGeneratedResult(generated)
        workspaceStore.upsertMoodboardItem(
            MoodboardItem(
                id = "generated_result:${generated.id}",
                projectId = projectId ?: generated.projectId,
                title = listOf(generated.roomType, generated.style, generated.toolTitle)
                    .filter { it.isNotBlank() }
                    .joinToString(" - ")
                    .ifBlank { "Moodboard idea" },
                imageUrl = generated.imageUrl,
                source = "generated_result:${generated.id}",
            )
        )
        return true
    }

    fun toggleDiscoverFavorite(item: GalleryItem, section: DiscoverSection): Boolean {
        val source = discoverSource(item)
        var isFavorite = false
        workspaceStore.state.value.favorites.firstOrNull { it.sourceType == source }?.let { existing ->
            workspaceStore.removeFavorite(existing.id)
            return false
        }
        isFavorite = true
        workspaceStore.upsertFavorite(
            FavoriteItem(
                id = source,
                title = discoverSavedTitle(item, section),
                toolId = section.serviceToolId,
                roomType = section.title,
                style = item.title,
                imageRes = item.imageRes,
                sourceType = source,
            ),
        )
        return isFavorite
    }

    fun addDiscoverToMoodboard(item: GalleryItem, section: DiscoverSection): Boolean {
        workspaceStore.upsertMoodboardItem(
            MoodboardItem(
                id = discoverSource(item),
                title = discoverSavedTitle(item, section),
                imageRes = item.imageRes,
                source = discoverSource(item),
                notes = section.title,
            ),
        )
        return true
    }

    fun useDiscoverStyle(item: GalleryItem, section: DiscoverSection) {
        val referenceTool = HomeDecorCatalog.tools.firstOrNull { it.id == "reference" } ?: return
        _uiState.update {
            it.copy(
                selectedTab = MainTab.Create,
                selectedTool = referenceTool,
                wizardStage = WizardStage.Photo,
                selectedPhotoUri = null,
                selectedExampleLabel = null,
                selectedPhotos = emptyList(),
                selectedReferenceUri = null,
                selectedReferenceExampleLabel = item.title,
                selectedReferenceDiscoverItemId = item.id,
                selectedRooms = emptyList(),
                selectedStyles = emptyList(),
                selectedPalettes = emptyList(),
                roomType = section.title,
                style = "",
                palette = "",
                designMode = HomeDecorCatalog.designModes.first().first,
                budgetMode = "",
                avoidOptions = emptyList(),
                keepOptions = emptyList(),
                changeOptions = emptyList(),
                preserveRestOfImage = false,
                customPrompt = "",
                layoutConstraints = "",
                mobilierASupprimer = "",
                mobilierADeplacer = "",
                maskStrokes = emptyList(),
                undoneMaskStrokes = emptyList(),
                brushSize = 28f,
                eraserSelected = false,
                generationError = null,
            )
        }
    }

    fun openHistoryResult(resultId: String): Boolean {
        val generated = workspaceStore.state.value.generatedResults.firstOrNull { it.id == resultId } ?: return false
        val tool = HomeDecorCatalog.tools.firstOrNull { it.id == generated.toolId || it.title == generated.toolTitle }
            ?: _uiState.value.selectedTool
        val boardItem = generated.toBoardItem()
        _uiState.update { state ->
            state.copy(
                selectedTab = MainTab.Create,
                selectedTool = tool,
                wizardStage = WizardStage.Result,
                roomType = generated.roomType,
                style = generated.style,
                palette = generated.palette,
                customPrompt = generated.prompt.orEmpty(),
                board = listOf(boardItem) + state.board.filterNot { it.id == boardItem.id },
                generationError = null,
            )
        }
        return true
    }

    fun toggleHistoryFavorite(resultId: String): Boolean {
        val generated = workspaceStore.state.value.generatedResults.firstOrNull { it.id == resultId } ?: return false
        return workspaceStore.toggleFavorite(generated)
    }

    fun saveHistoryResultToProject(resultId: String): Project? {
        val workspace = workspaceStore.state.value
        val generated = workspace.generatedResults.firstOrNull { it.id == resultId } ?: return null
        val project = generated.projectId?.let { id -> workspace.projects.firstOrNull { it.id == id } }
            ?: workspace.projects.firstOrNull { it.name == DEFAULT_HISTORY_PROJECT_NAME }
            ?: workspaceStore.createProject(
                name = DEFAULT_HISTORY_PROJECT_NAME,
                roomType = generated.roomType,
                coverImageUri = generated.imageUri,
            )
        val savedProject = if (project.coverImageUrl.isNullOrBlank() && !generated.imageUrl.isNullOrBlank()) {
            project.copy(coverImageUrl = generated.imageUrl, coverImageUri = generated.imageUri ?: project.coverImageUri)
                .also(workspaceStore::upsertProject)
        } else {
            project
        }
        val saved = generated.copy(projectId = savedProject.id)
        workspaceStore.upsertGeneratedResult(saved)
        workspaceStore.upsertMoodboardItem(
            MoodboardItem(
                projectId = savedProject.id,
                title = listOf(saved.roomType, saved.style, saved.toolTitle)
                    .filter { it.isNotBlank() }
                    .joinToString(" - ")
                    .ifBlank { "Project save" },
                imageUri = saved.imageUri,
                imageUrl = saved.imageUrl,
                source = "generated_result:${saved.id}",
            )
        )
        _uiState.update { state ->
            state.copy(board = state.board.map { if (it.id == saved.id) saved.toBoardItem() else it })
        }
        return savedProject
    }

    fun deleteHistoryResult(resultId: String) {
        workspaceStore.removeGeneratedResult(resultId)
        _uiState.update { state ->
            state.copy(board = state.board.filterNot { it.id == resultId })
        }
    }

    fun removeFavorite(favoriteId: String) {
        workspaceStore.removeFavorite(favoriteId)
    }

    fun removeMoodboardItem(itemId: String) {
        workspaceStore.removeMoodboardItem(itemId)
    }

    fun saveCurrentToolDraft(projectId: String? = null) {
        workspaceStore.upsertToolDraft(_uiState.value.toToolDraft(projectId))
    }

    fun loadToolDraft(toolId: String = _uiState.value.selectedTool.id, projectId: String? = null): Boolean {
        val draft = workspaceStore.draftFor(toolId, projectId) ?: return false
        val tool = HomeDecorCatalog.tools.firstOrNull { it.id == draft.toolId } ?: _uiState.value.selectedTool
        _uiState.update {
            it.copy(
                selectedTab = MainTab.Create,
                selectedTool = tool,
                wizardStage = WizardStage.Photo,
                selectedPhotos = draft.toSelectedPhotos(),
                selectedPhotoUri = draft.selectedPhotoUris.firstOrNull()?.let(Uri::parse),
                selectedExampleLabel = draft.selectedExampleLabels.firstOrNull(),
                selectedReferenceUri = draft.referencePhotoUri?.let(Uri::parse),
                selectedReferenceExampleLabel = draft.selectedReferenceExampleLabel,
                selectedReferenceDiscoverItemId = draft.selectedReferenceDiscoverItemId,
                selectedRooms = draft.selectedRooms,
                selectedStyles = draft.selectedStyles,
                selectedPalettes = draft.selectedPalettes,
                roomType = draft.roomType,
                style = draft.style,
                palette = draft.palette,
                designMode = draft.designMode.ifBlank { HomeDecorCatalog.designModes.first().first },
                budgetMode = draft.budgetMode,
                avoidOptions = draft.avoidOptions,
                keepOptions = draft.keepOptions,
                changeOptions = draft.changeOptions,
                preserveRestOfImage = draft.preserveRestOfImage,
                customPrompt = draft.customPrompt,
                layoutConstraints = draft.layoutConstraints,
                mobilierASupprimer = draft.mobilierASupprimer,
                mobilierADeplacer = draft.mobilierADeplacer,
                maskStrokes = draft.maskStrokes.toMaskStrokes(),
                undoneMaskStrokes = emptyList(),
                generationError = null,
            )
        }
        return true
    }

    fun clearCurrentToolDraft(projectId: String? = null) {
        workspaceStore.clearToolDraft(_uiState.value.selectedTool.id, projectId)
    }

    fun claimLocalDailyReward(): Boolean {
        val reward = workspaceStore.claimDailyReward() ?: return false
        _uiState.update {
            val nextDiamonds = it.diamonds + reward.lastRewardAmount
            it.copy(
                diamonds = nextDiamonds,
                viewer = it.viewer.copy(
                    credits = nextDiamonds,
                    diamondBalance = nextDiamonds,
                ),
                eliteStreakDay = reward.currentStreak.coerceAtLeast(1),
                claimedToday = true,
                elitePassSyncState = ElitePassSyncState.LocalOnly,
                elitePassSyncMessage = text(R.string.elite_pass_local_sync),
            )
        }
        return true
    }

    fun setPhoto(uri: Uri?) {
        if (uri == null) return
        _uiState.update { state ->
            val next = (state.selectedPhotos + SelectedPhoto(uri = uri)).take(3)
            state.copy(selectedPhotos = next, selectedPhotoUri = next.firstOrNull()?.uri, selectedExampleLabel = next.firstOrNull()?.exampleLabel)
        }
    }

    fun selectExamplePhoto(label: String) {
        _uiState.update { state ->
            val next = (state.selectedPhotos + SelectedPhoto(exampleLabel = label)).take(3)
            state.copy(selectedPhotos = next, selectedPhotoUri = next.firstOrNull()?.uri, selectedExampleLabel = next.firstOrNull()?.exampleLabel)
        }
    }

    fun setPrimaryPhoto(uri: Uri?) {
        if (uri == null) return
        _uiState.update {
            it.copy(
                selectedPhotos = listOf(SelectedPhoto(uri = uri)),
                selectedPhotoUri = uri,
                selectedExampleLabel = null,
            )
        }
    }

    fun selectPrimaryExamplePhoto(label: String) {
        _uiState.update {
            it.copy(
                selectedPhotos = listOf(SelectedPhoto(exampleLabel = label)),
                selectedPhotoUri = null,
                selectedExampleLabel = label,
            )
        }
    }

    fun removePhoto(index: Int) {
        _uiState.update { state ->
            val next = state.selectedPhotos.filterIndexed { slotIndex, _ -> slotIndex != index }
            state.copy(selectedPhotos = next, selectedPhotoUri = next.firstOrNull()?.uri, selectedExampleLabel = next.firstOrNull()?.exampleLabel)
        }
    }

    fun setReferencePhoto(uri: Uri?) {
        if (uri == null) return
        _uiState.update { it.copy(selectedReferenceUri = uri, selectedReferenceExampleLabel = null, selectedReferenceDiscoverItemId = null) }
    }

    fun selectReferenceExample(label: String) {
        _uiState.update { it.copy(selectedReferenceExampleLabel = label, selectedReferenceUri = null, selectedReferenceDiscoverItemId = null) }
    }

    fun setRoom(room: String) {
        _uiState.update { state ->
            if (room in singleRoomSelections) {
                return@update state.copy(selectedRooms = listOf(room), roomType = room)
            }
            val limit = if (state.selectedTool.id == "layout") 7 else 2
            val selected = toggleLimited(state.selectedRooms, room, limit = limit)
            state.copy(selectedRooms = selected, roomType = selected.joinToString(" + "))
        }
    }

    fun setRoomTypeText(room: String) {
        _uiState.update { it.copy(roomType = room) }
    }

    fun setStyleText(style: String) {
        _uiState.update { it.copy(style = style) }
    }

    fun setPaletteText(palette: String) {
        _uiState.update { it.copy(palette = palette) }
    }

    fun setMobilierADeplacerText(text: String) {
        _uiState.update { it.copy(mobilierADeplacer = text) }
    }

    fun setMobilierASupprimerText(text: String) {
        _uiState.update { it.copy(mobilierASupprimer = text) }
    }

    fun setStyle(style: String) {
        _uiState.update { state ->
            if (state.selectedTool.id in setOf("paint", "floor")) {
                return@update state.copy(selectedStyles = listOf(style), style = style)
            }
            val selected = toggleLimited(state.selectedStyles, style, limit = 2)
            state.copy(selectedStyles = selected, style = selected.joinToString(" + "))
        }
    }

    fun tryAnotherStyle(style: String) {
        if (style.isBlank()) return
        _uiState.update {
            it.copy(
                selectedStyles = listOf(style),
                selectedPalettes = emptyList(),
                style = style,
                palette = "",
                generationError = null,
            )
        }
        generate()
    }

    fun setPalette(palette: String) {
        _uiState.update { state ->
            val selected = toggleLimited(state.selectedPalettes, palette, limit = 2)
            state.copy(selectedPalettes = selected, palette = selected.joinToString(" + "))
        }
    }

    fun setDesignMode(mode: String) {
        _uiState.update { it.copy(designMode = mode) }
    }

    fun setBudgetMode(mode: String) {
        _uiState.update { state ->
            state.copy(budgetMode = if (state.budgetMode == mode) "" else mode)
        }
    }

    fun toggleAvoidOption(option: String) {
        _uiState.update { state ->
            val next = if (option in state.avoidOptions) {
                state.avoidOptions - option
            } else {
                state.avoidOptions + option
            }
            state.copy(avoidOptions = next)
        }
    }

    fun toggleKeepOption(option: String) {
        _uiState.update { state ->
            val next = if (option in state.keepOptions) {
                state.keepOptions - option
            } else {
                state.keepOptions + option
            }
            state.copy(keepOptions = next)
        }
    }

    fun toggleChangeOption(option: String) {
        _uiState.update { state ->
            val next = if (option in state.changeOptions) {
                state.changeOptions - option
            } else {
                state.changeOptions + option
            }
            state.copy(changeOptions = next)
        }
    }

    fun togglePreserveRestOfImage() {
        _uiState.update { state ->
            state.copy(preserveRestOfImage = !state.preserveRestOfImage)
        }
    }

    fun tryWithExample() {
        val snapshot = _uiState.value
        val exampleLabel = firstExampleLabelForTool(snapshot.selectedTool.id)
        _uiState.update { state ->
            state.copy(
                selectedPhotos = listOf(SelectedPhoto(exampleLabel = exampleLabel)),
                selectedPhotoUri = null,
                selectedExampleLabel = exampleLabel,
                selectedReferenceUri = if (state.selectedTool.id == "reference") null else state.selectedReferenceUri,
                selectedReferenceExampleLabel = if (state.selectedTool.id == "reference") {
                    text(R.string.editorial_reference)
                } else {
                    state.selectedReferenceExampleLabel
                },
                selectedReferenceDiscoverItemId = if (state.selectedTool.id == "reference") null else state.selectedReferenceDiscoverItemId,
                generationError = null,
            )
        }
    }

    fun setCustomPrompt(prompt: String) {
        _uiState.update { state ->
            if (state.selectedTool.id == "replace") {
                val selectedSuggestion = HomeDecorCatalog.replaceSuggestions.firstOrNull {
                    it == prompt || HomeDecorCatalog.replacementTemplatePrompts[it] == prompt
                }
                state.copy(
                    customPrompt = prompt,
                    selectedStyles = selectedSuggestion?.let(::listOf).orEmpty(),
                    style = selectedSuggestion.orEmpty(),
                )
            } else {
                state.copy(customPrompt = prompt)
            }
        }
    }

    fun selectReplacementSuggestion(suggestion: String) {
        _uiState.update {
            it.copy(
                selectedStyles = listOf(suggestion),
                style = suggestion,
                customPrompt = HomeDecorCatalog.replacementTemplatePrompts[suggestion] ?: suggestion,
            )
        }
    }

    fun setLayoutConstraints(text: String) {
        _uiState.update { it.copy(layoutConstraints = text) }
    }

    fun openDiamondStore() {
        _uiState.update {
            it.copy(
                storeVisible = true,
                paywallVisible = false,
                authVisible = false,
                settingsVisible = false,
                purchaseMessage = null,
            )
        }
    }

    fun closeDiamondStore() {
        _uiState.update { it.copy(storeVisible = false) }
    }

    fun openPaywall() {
        _uiState.update {
            it.copy(
                paywallVisible = true,
                authVisible = false,
                storeVisible = false,
                settingsVisible = false,
                purchaseMessage = null,
            )
        }
    }

    fun closePaywall() {
        _uiState.update { it.copy(paywallVisible = false) }
    }

    fun openAuth() {
        _uiState.update {
            it.copy(
                authVisible = true,
                paywallVisible = false,
                storeVisible = false,
                settingsVisible = false,
            )
        }
    }

    fun closeAuth() {
        _uiState.update { it.copy(authVisible = false) }
    }

    fun openSettings() {
        _uiState.update {
            it.copy(
                settingsVisible = true,
                storeVisible = false,
                paywallVisible = false,
                authVisible = false,
                settingsMessage = null,
            )
        }
    }

    fun closeSettings() {
        _uiState.update { it.copy(settingsVisible = false) }
    }

    fun buyDiamondPack(pack: DiamondPack) {
        _uiState.update {
            it.copy(
                purchaseMessage = text(R.string.select_real_pack_google_play),
                purchaseBusy = false,
            )
        }
    }

    fun setBrushSize(size: Float) {
        _uiState.update { it.copy(brushSize = size.coerceIn(8f, 72f)) }
    }

    fun setMaskEraser(enabled: Boolean) {
        _uiState.update { it.copy(eraserSelected = enabled) }
    }

    fun addMaskStroke(stroke: MaskStroke) {
        if (stroke.points.size < 2) return
        _uiState.update { state ->
            val next = state.maskStrokes + stroke
            state.copy(
                maskStrokes = next,
                undoneMaskStrokes = emptyList(),
                roomType = when (state.selectedTool.id) {
                    "paint" -> text(R.string.mask_wall_marked)
                    "floor" -> text(R.string.mask_floor_marked)
                    "replace" -> text(R.string.mask_object_marked)
                    else -> state.roomType
                },
            )
        }
    }

    fun undoMaskStroke() {
        _uiState.update { state ->
            val last = state.maskStrokes.lastOrNull() ?: return@update state
            val remaining = state.maskStrokes.dropLast(1)
            state.copy(
                maskStrokes = remaining,
                undoneMaskStrokes = listOf(last) + state.undoneMaskStrokes,
                roomType = if (remaining.any { !it.erase }) state.roomType else "",
            )
        }
    }

    fun redoMaskStroke() {
        _uiState.update { state ->
            val nextStroke = state.undoneMaskStrokes.firstOrNull() ?: return@update state
            state.copy(
                maskStrokes = state.maskStrokes + nextStroke,
                undoneMaskStrokes = state.undoneMaskStrokes.drop(1),
            )
        }
    }

    fun clearMask() {
        _uiState.update { it.copy(maskStrokes = emptyList(), undoneMaskStrokes = emptyList(), roomType = "") }
    }

    fun markMaskWithAutoDetect(target: String) {
        val stroke = MaskStroke(
            brushSize = 54f,
            erase = false,
            points = when (target) {
                "floor" -> listOf(MaskPoint(0.18f, 0.70f), MaskPoint(0.42f, 0.63f), MaskPoint(0.78f, 0.70f), MaskPoint(0.88f, 0.92f), MaskPoint(0.12f, 0.92f))
                "object" -> listOf(MaskPoint(0.36f, 0.46f), MaskPoint(0.64f, 0.46f), MaskPoint(0.68f, 0.68f), MaskPoint(0.32f, 0.68f), MaskPoint(0.36f, 0.46f))
                else -> listOf(MaskPoint(0.18f, 0.20f), MaskPoint(0.82f, 0.20f), MaskPoint(0.82f, 0.66f), MaskPoint(0.18f, 0.66f), MaskPoint(0.18f, 0.20f))
            },
        )
        addMaskStroke(stroke)
    }

    fun fulfillDiamondPurchase(
        packId: String,
        transactionId: String,
        productIdentifier: String,
        packageIdentifier: String?,
        amount: Double,
        currencyCode: String,
        purchasedAt: Double,
    ) {
        val pending = PendingPurchaseSync.Diamond(
            packId = packId,
            transactionId = transactionId,
            productIdentifier = productIdentifier,
            packageIdentifier = packageIdentifier,
            amount = amount,
            currencyCode = currencyCode,
            purchasedAt = purchasedAt,
        )
        viewModelScope.launch {
            _uiState.update { it.copy(purchaseBusy = true, purchaseMessage = text(R.string.purchase_validating)) }
            runCatching {
                repository.fulfillDiamondPurchase(
                    anonymousId = anonymousId,
                    packId = packId,
                    transactionId = transactionId,
                    productIdentifier = productIdentifier,
                    packageIdentifier = packageIdentifier,
                    amount = amount,
                    currencyCode = currencyCode,
                    purchasedAt = purchasedAt,
                )
                repository.viewerSummaryStrict(anonymousId)
            }.onSuccess { viewer ->
                _uiState.update {
                    it.copy(
                        viewer = viewer,
                        diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                        purchaseBusy = false,
                        pendingPurchaseSync = null,
                        purchaseMessage = text(R.string.diamond_purchase_confirmed),
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        purchaseBusy = false,
                        pendingPurchaseSync = pending,
                        purchaseMessage = friendlyPurchaseSyncError(error, R.string.diamond_purchase_sync_failed),
                    )
                }
            }
        }
    }

    fun submitSettingsFeedback(message: String) {
        val trimmed = message.trim()
        if (trimmed.length < 3) {
            _uiState.update { it.copy(settingsMessage = text(R.string.feedback_empty_error)) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(settingsBusy = true, settingsMessage = text(R.string.feedback_sending)) }
            runCatching {
                repository.submitFeedback(
                    anonymousId = anonymousId,
                    message = trimmed,
                    generationCount = _uiState.value.board.size,
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        settingsBusy = false,
                        settingsMessage = text(R.string.feedback_sent),
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        settingsBusy = false,
                        settingsMessage = text(R.string.feedback_failed),
                    )
                }
            }
        }
    }

    fun deleteAccountData() {
        viewModelScope.launch {
            val previousAnonymousId = anonymousId
            _uiState.update { it.copy(settingsBusy = true, settingsMessage = text(R.string.deleting_account)) }
            runCatching {
                repository.deleteAccountData(previousAnonymousId)
                anonymousId = newAnonymousId()
                repository.bootstrapViewer(anonymousId)
            }.onSuccess { viewer ->
                _uiState.update {
                    it.copy(
                        viewer = viewer,
                        diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                        isPro = viewer.hasProAccess,
                        board = emptyList(),
                        signedInName = null,
                        signedInEmail = null,
                        settingsVisible = false,
                        paywallVisible = false,
                        storeVisible = false,
                        authVisible = false,
                        settingsBusy = false,
                        settingsMessage = null,
                        purchaseBusy = false,
                        purchaseMessage = text(R.string.delete_account_done),
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        settingsBusy = false,
                        settingsMessage = text(R.string.delete_account_failed),
                    )
                }
            }
        }
    }

    fun logOut() {
        _uiState.update {
            it.copy(
                viewer = it.viewer.copy(isGuest = true),
                signedInName = null,
                signedInEmail = null,
                authVisible = false,
                settingsMessage = text(R.string.logout_done),
            )
        }
    }

    fun syncSubscriptionFromRevenueCat(
        plan: String,
        subscriptionType: String,
        entitlement: String,
        purchasedAt: Double?,
        subscriptionEnd: Double?,
    ) {
        val pending = PendingPurchaseSync.Subscription(
            plan = plan,
            subscriptionType = subscriptionType,
            entitlement = entitlement,
            purchasedAt = purchasedAt,
            subscriptionEnd = subscriptionEnd,
        )
        viewModelScope.launch {
            _uiState.update { it.copy(purchaseBusy = true, purchaseMessage = text(R.string.pro_syncing)) }
            runCatching {
                repository.setViewerPlanFromRevenueCat(
                    anonymousId = anonymousId,
                    plan = plan,
                    subscriptionType = subscriptionType,
                    entitlement = entitlement,
                    purchasedAt = purchasedAt,
                    subscriptionEnd = subscriptionEnd,
                )
                repository.viewerSummaryStrict(anonymousId)
            }.onSuccess { viewer ->
                _uiState.update {
                    it.copy(
                        viewer = viewer,
                        isPro = viewer.hasProAccess,
                        diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                        paywallVisible = false,
                        purchaseBusy = false,
                        pendingPurchaseSync = null,
                        purchaseMessage = text(R.string.pro_activated_success),
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        purchaseBusy = false,
                        pendingPurchaseSync = pending,
                        purchaseMessage = friendlyPurchaseSyncError(error, R.string.pro_sync_failed),
                    )
                }
            }
        }
    }

    fun retryPurchaseSync() {
        when (val pending = _uiState.value.pendingPurchaseSync) {
            is PendingPurchaseSync.Diamond -> fulfillDiamondPurchase(
                packId = pending.packId,
                transactionId = pending.transactionId,
                productIdentifier = pending.productIdentifier,
                packageIdentifier = pending.packageIdentifier,
                amount = pending.amount,
                currencyCode = pending.currencyCode,
                purchasedAt = pending.purchasedAt,
            )
            is PendingPurchaseSync.Subscription -> syncSubscriptionFromRevenueCat(
                plan = pending.plan,
                subscriptionType = pending.subscriptionType,
                entitlement = pending.entitlement,
                purchasedAt = pending.purchasedAt,
                subscriptionEnd = pending.subscriptionEnd,
            )
            null -> Unit
        }
    }

    fun nextStage() {
        _uiState.update { state ->
            state.copy(
                wizardStage = when (state.wizardStage) {
                    WizardStage.Photo -> if (state.selectedTool.id in setOf("reference", "paint", "floor")) WizardStage.Style else WizardStage.Space
                    WizardStage.Space -> if (state.selectedTool.id == "garden") WizardStage.Refine else WizardStage.Style
                    WizardStage.Style -> WizardStage.Refine
                    WizardStage.Refine -> WizardStage.Refine
                    WizardStage.Processing -> WizardStage.Processing
                    WizardStage.Result -> WizardStage.Result
                }
            )
        }
    }

    fun previousStage() {
        _uiState.update { state ->
            state.copy(
                wizardStage = when (state.wizardStage) {
                    WizardStage.Photo -> WizardStage.Photo
                    WizardStage.Space -> WizardStage.Photo
                    WizardStage.Style -> if (state.selectedTool.id in setOf("reference", "paint", "floor")) WizardStage.Photo else WizardStage.Space
                    WizardStage.Refine -> if (state.selectedTool.id in listOf("garden", "layout")) WizardStage.Space else WizardStage.Style
                    WizardStage.Processing -> when (state.selectedTool.id) {
                        "layout" -> WizardStage.Space
                        "reference", "paint", "floor", "replace" -> WizardStage.Style
                        else -> WizardStage.Refine
                    }
                    WizardStage.Result -> when (state.selectedTool.id) {
                        "layout" -> WizardStage.Space
                        "reference", "paint", "floor", "replace" -> WizardStage.Style
                        else -> WizardStage.Refine
                    }
                }
            )
        }
    }

    private fun ViewerSummary.claimedWithinLocalDay(now: Long = System.currentTimeMillis()): Boolean {
        val claimedAt = lastClaimAt?.toLong() ?: return false
        if (claimedAt <= 0 || claimedAt > now) return false
        val claimDay = Calendar.getInstance().apply { timeInMillis = claimedAt }
        val currentDay = Calendar.getInstance().apply { timeInMillis = now }
        return claimDay.get(Calendar.YEAR) == currentDay.get(Calendar.YEAR) &&
            claimDay.get(Calendar.DAY_OF_YEAR) == currentDay.get(Calendar.DAY_OF_YEAR)
    }

    private fun ViewerSummary.wasDaySevenEliteClaim(now: Long = System.currentTimeMillis()): Boolean {
        val trialActive = (proTrialExpiresAt ?: 0.0) > now
        return status == "day7_claimed" || (claimedWithinLocalDay(now) && streakCount == 0 && trialActive)
    }

    private fun ViewerSummary.nextElitePassDay(now: Long = System.currentTimeMillis()): Int {
        if (wasDaySevenEliteClaim(now)) return 7
        return (streakCount + 1).coerceIn(1, 7)
    }

    private fun hasReferenceFlowImages(snapshot: HomeDecorUiState): Boolean {
        val hasRoom = snapshot.selectedPhotos.firstOrNull() != null ||
            snapshot.selectedPhotoUri != null ||
            snapshot.selectedExampleLabel != null
        val hasReference = snapshot.selectedReferenceUri != null ||
            snapshot.selectedReferenceExampleLabel != null
        return hasRoom && hasReference
    }

    fun generate() {
        val snapshot = _uiState.value
        if (snapshot.selectedTool.id == "layout" && snapshot.selectedRooms.isEmpty()) {
            _uiState.update {
                it.copy(
                    wizardStage = WizardStage.Space,
                    generationError = text(R.string.layout_goal_required_error),
                )
            }
            return
        }
        if (snapshot.selectedTool.id == "reference" && !hasReferenceFlowImages(snapshot)) {
            _uiState.update {
                it.copy(
                    wizardStage = WizardStage.Photo,
                    generationError = text(R.string.reference_missing_error),
                )
            }
            return
        }
        if (snapshot.selectedTool.id == "replace" && !snapshot.maskStrokes.hasVisibleMaskPaint()) {
            _uiState.update {
                it.copy(
                    wizardStage = WizardStage.Space,
                    generationError = text(R.string.real_mask_required_error),
                )
            }
            return
        }
        if (snapshot.selectedTool.id == "replace" && !snapshot.customPrompt.isValidReplacementPrompt()) {
            _uiState.update {
                it.copy(
                    wizardStage = WizardStage.Style,
                    generationError = text(R.string.replacement_prompt_required_error),
                )
            }
            return
        }
        if (snapshot.diamonds <= 0 && !snapshot.isPro) {
            _uiState.update {
                it.copy(
                    paywallVisible = true,
                    storeVisible = false,
                    authVisible = false,
                    settingsVisible = false,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    wizardStage = WizardStage.Processing,
                    progressMessage = text(R.string.processing_transform),
                    generationError = null,
                )
            }
            runCatching {
                val access = repository.canUserGenerate(anonymousId)
                if (!access.allowed && access.shouldTriggerPaywall) {
                    throw AppRecoverableException(AppErrorKind.Limit)
                }
                val source = readSelectedSource(snapshot)
                val reference = readSelectedReference(snapshot)
                val mask = readMaskSource(snapshot)
                val generationRoomType = when {
                    snapshot.selectedTool.id == "garden" && snapshot.roomType.isBlank() -> "Jardin"
                    else -> snapshot.roomType
                }
                val generationPrompt = when (snapshot.selectedTool.id) {
                    "layout" -> buildString {
                        append("Focused space-planning task, not a normal redesign. Re-arrange existing furniture and propose a practical room layout to gain more space and fluid circulation. Preserve the exact architectural shell, camera angle, walls, windows, doors, fixed cabinetry, flooring, wall finishes, and room structure. Do not change the decor style unless needed for small staging consistency.")
                        if (snapshot.roomType.isNotBlank()) append(" Planning goals: ").append(snapshot.roomType).append(".")
                        if (snapshot.layoutConstraints.isNotBlank()) append(" Constraints: ").append(snapshot.layoutConstraints).append(".")
                        if (snapshot.customPrompt.isNotBlank()) append(" Custom notes: ").append(snapshot.customPrompt).append(".")
                        if (snapshot.palette.isNotBlank()) append(" Mobilier a garder: ").append(snapshot.palette).append(".")
                        if (snapshot.mobilierASupprimer.isNotBlank()) append(" Mobilier a supprimer: ").append(snapshot.mobilierASupprimer).append(".")
                        if (snapshot.mobilierADeplacer.isNotBlank()) append(" Mobilier a deplacer: ").append(snapshot.mobilierADeplacer).append(".")
                        appendAdvancedInstructions(snapshot)
                    }
                    "reference" -> buildString {
                        append("Reference style transfer. Use the first image as Votre piece and the second image as Image de reference.")
                        if (snapshot.style.isNotBlank()) append(" Transfer strength: ").append(snapshot.style).append(".")
                        if (snapshot.palette.isNotBlank()) append(" Transfer options: ").append(snapshot.palette).append(".")
                        append(" Preserve the source room structure, camera angle, openings, and proportions.")
                        if (snapshot.customPrompt.isNotBlank()) append(" Additional notes: ").append(snapshot.customPrompt).append(".")
                        appendAdvancedInstructions(snapshot)
                    }
                    "paint" -> buildString {
                        append("Wall-only masked material edit. Change only the masked wall area")
                        if (snapshot.style.isNotBlank()) append(" to ").append(snapshot.style)
                        append(". Preserve the floor, ceiling, furniture, decor, trim, windows, shadows, camera angle, and every non-wall surface exactly.")
                        if (snapshot.customPrompt.isNotBlank()) append(" Wall finish notes: ").append(snapshot.customPrompt).append(".")
                        appendAdvancedInstructions(snapshot)
                    }
                    "floor" -> buildString {
                        append("Floor-only masked material edit. Change only the masked floor area")
                        if (snapshot.style.isNotBlank()) append(" to ").append(snapshot.style)
                        append(". Preserve walls, furniture, decor, baseboards, lighting, contact shadows, camera angle, and every non-floor surface exactly.")
                        if (snapshot.customPrompt.isNotBlank()) append(" Floor finish notes: ").append(snapshot.customPrompt).append(".")
                        appendAdvancedInstructions(snapshot)
                    }
                    else -> buildString {
                        append(snapshot.customPrompt)
                        appendAdvancedInstructions(snapshot)
                    }
                }
                _uiState.update { it.copy(progressMessage = text(R.string.progress_analyzing_image)) }
                val start = repository.startGeneration(
                    anonymousId = anonymousId,
                    imageBytes = source.bytes,
                    mimeType = source.mimeType,
                    maskBytes = mask?.bytes,
                    maskMimeType = mask?.mimeType,
                    tool = snapshot.selectedTool,
                    roomType = generationRoomType,
                    style = snapshot.style,
                    palette = snapshot.palette,
                    designMode = snapshot.designMode,
                    customPrompt = generationPrompt,
                    referenceImageBytes = reference?.bytes,
                    referenceMimeType = reference?.mimeType,
                )
                _uiState.update { it.copy(progressMessage = text(R.string.progress_applying_color)) }
                val ready = repository.waitForGeneration(anonymousId, start.generationId)
                _uiState.update { it.copy(progressMessage = text(R.string.progress_finalizing_render)) }
                val budgetLabel = start.renderLabel ?: start.quality.orEmpty()
                ready.toBoardItem()?.copy(budgetLabel = budgetLabel) ?: BoardItem(
                    id = start.generationId,
                    toolTitle = snapshot.selectedTool.title,
                    style = snapshot.style,
                    roomType = generationRoomType,
                    imageRes = R.drawable.sample_after_luxury,
                    imageUrl = ready.imageUrl,
                    sourceImageUrl = ready.sourceImageUrl,
                    prompt = generationPrompt,
                    budgetLabel = budgetLabel,
                    createdAt = System.currentTimeMillis().toDouble(),
                )
            }.onSuccess { result ->
                val viewer = repository.viewerSummary(anonymousId)
                workspaceStore.upsertGeneratedResult(result.toGeneratedResult(snapshot))
                workspaceStore.recordRecentStyle(
                    toolId = snapshot.selectedTool.id,
                    style = snapshot.style,
                    roomType = snapshot.roomType,
                    palette = snapshot.palette,
                )
                _uiState.update {
                    it.copy(
                        wizardStage = WizardStage.Result,
                        diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                        viewer = viewer,
                        board = listOf(result) + it.board.filterNot { boardItem -> boardItem.id == result.id },
                    )
                }
            }.onFailure { error ->
                val message = friendlyGenerationError(error)
                val recoveredViewer = runCatching { repository.viewerSummaryStrict(anonymousId) }.getOrNull()
                val recoveredBoard = runCatching { repository.archive(anonymousId).mapNotNull { item -> item.toBoardItem() } }.getOrNull()
                _uiState.update {
                    it.copy(
                        wizardStage = when (snapshot.selectedTool.id) {
                            "layout" -> WizardStage.Space
                            "reference", "paint", "floor", "replace" -> WizardStage.Style
                            else -> WizardStage.Refine
                        },
                        viewer = recoveredViewer ?: it.viewer,
                        diamonds = recoveredViewer?.let { viewer -> viewer.diamondBalance.coerceAtLeast(viewer.credits) } ?: it.diamonds,
                        board = recoveredBoard ?: it.board,
                        generationError = message,
                        progressMessage = message,
                    )
                }
            }
        }
    }

    fun saveResultToPortfolio(result: BoardItem?): Boolean {
        if (result == null || !result.isGeneratedResult()) return false
        workspaceStore.upsertGeneratedResult(result.toGeneratedResult(_uiState.value))
        _uiState.update { state ->
            state.copy(
                board = listOf(result) + state.board.filterNot { it.id == result.id },
            )
        }
        return true
    }

    fun claimDiamond() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    elitePassSyncState = ElitePassSyncState.Syncing,
                    elitePassSyncMessage = text(R.string.reward_syncing),
                )
            }
            runCatching { repository.claimDailyDiamond(anonymousId) }
                .onSuccess { viewer ->
                    workspaceStore.updateDailyReward(viewer.toDailyRewardState())
                    _uiState.update {
                        it.copy(
                            viewer = viewer,
                            diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                            eliteStreakDay = viewer.nextElitePassDay(),
                            claimedToday = viewer.claimedWithinLocalDay() || viewer.status == "already_claimed",
                            eliteLastClaimWasDaySeven = viewer.wasDaySevenEliteClaim(),
                            elitePassSyncState = ElitePassSyncState.Synced,
                            elitePassSyncMessage = when (viewer.status) {
                                "day7_claimed" -> text(R.string.reward_day7_synced)
                                "claimed" -> text(R.string.reward_synced)
                                "already_claimed" -> text(R.string.reward_already_claimed)
                                "at_cap" -> text(R.string.daily_balance_full)
                                else -> text(R.string.elite_pass_synced)
                            },
                            isPro = viewer.hasProAccess,
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            elitePassSyncState = ElitePassSyncState.Error,
                            elitePassSyncMessage = text(R.string.reward_claim_unconfirmed),
                        )
                    }
                }
        }
    }

    fun unlockProPreview() {
        _uiState.update { it.copy(isPro = true, diamonds = it.diamonds + 10, paywallVisible = false) }
    }

    private fun toggleLimited(current: List<String>, value: String, limit: Int): List<String> {
        if (value.isBlank()) return current
        return if (value in current) {
            current - value
        } else {
            (current + value).takeLast(limit)
        }
    }

    private val singleRoomSelections = setOf(
        "Sol marque",
        "Choix de l'IA",
        "Mur marqué",
        "Surface à marquer",
    )

    private data class SourceImage(
        val bytes: ByteArray,
        val mimeType: String,
    )

    private fun readSelectedSource(snapshot: HomeDecorUiState): SourceImage {
        val firstPhoto = snapshot.selectedPhotos.firstOrNull()
        firstPhoto?.uri?.let { uri -> return readUriSource(uri) }
        firstPhoto?.exampleLabel?.let {
            val resId = exampleImageResFor(snapshot.selectedTool.id, it)
            val bytes = appContext.resources.openRawResource(resId).use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                    ?: error(text(R.string.prepare_example_failed))
                bitmap.toJpegBytes()
            }
            return SourceImage(bytes, "image/jpeg")
        }
        snapshot.selectedPhotoUri?.let { uri -> return readUriSource(uri) }
        val resId = exampleImageResFor(snapshot.selectedTool.id, snapshot.selectedExampleLabel)
        val bytes = appContext.resources.openRawResource(resId).use { stream ->
            val bitmap = BitmapFactory.decodeStream(stream)
                ?: error(text(R.string.prepare_example_failed))
            bitmap.toJpegBytes()
        }
        return SourceImage(bytes, "image/jpeg")
    }

    private fun readSelectedReference(snapshot: HomeDecorUiState): SourceImage? {
        if (snapshot.selectedTool.id != "reference") return null
        snapshot.selectedReferenceUri?.let { uri -> return readUriSource(uri) }
        snapshot.selectedReferenceDiscoverItemId
            ?.let(::discoverImageResForItemId)
            ?.let { imageRes ->
                val bitmap = BitmapFactory.decodeResource(appContext.resources, imageRes)
                    ?: error(text(R.string.prepare_reference_failed))
                return SourceImage(bitmap.toJpegBytes(), "image/jpeg")
            }
        if (snapshot.selectedReferenceExampleLabel != null) {
            val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.tool_reference)
                ?: error(text(R.string.prepare_reference_failed))
            val bytes = bitmap.toJpegBytes()
            return SourceImage(bytes, "image/jpeg")
        }
        return null
    }

    private fun readMaskSource(snapshot: HomeDecorUiState): SourceImage? {
        if (snapshot.selectedTool.id !in setOf("paint", "floor", "replace")) return null
        if (!snapshot.maskStrokes.hasVisibleMaskPaint()) {
            error(text(R.string.real_mask_required_error))
        }
        return SourceImage(snapshot.maskStrokes.toMaskPngBytes(), "image/png")
    }

    private fun readUriSource(uri: Uri): SourceImage {
        val bytes = appContext.contentResolver.openInputStream(uri)?.use { stream ->
            val bitmap = BitmapFactory.decodeStream(stream)
                ?: error(text(R.string.read_selected_image_failed))
            bitmap.toJpegBytes()
        }
            ?: error(text(R.string.read_selected_image_failed))
        return SourceImage(bytes, "image/jpeg")
    }

    private fun Bitmap.toJpegBytes(): ByteArray {
        return ByteArrayOutputStream().use { output ->
            compress(Bitmap.CompressFormat.JPEG, 94, output)
            output.toByteArray()
        }
    }

    private fun List<MaskStroke>.toMaskPngBytes(): ByteArray {
        val bitmap = toMaskBitmap(size = 1024)
        return ByteArrayOutputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.toByteArray()
        }
    }

    private fun friendlyGenerationError(error: Throwable): String {
        val message = error.message.orEmpty()
        val kind = when {
            text(R.string.real_mask_required_error) in message -> AppErrorKind.Mask
            text(R.string.prepare_example_failed) in message ||
                text(R.string.prepare_reference_failed) in message ||
                text(R.string.read_selected_image_failed) in message -> AppErrorKind.ImagePreparation
            else -> error.toAppErrorKind(appContext)
        }
        return text(kind.generationMessageRes())
    }

    private fun friendlyPurchaseSyncError(error: Throwable, @StringRes fallback: Int): String {
        return text(error.toAppErrorKind(appContext).purchaseSyncMessageRes(fallback))
    }

    private fun ArchiveGeneration.toBoardItem(): BoardItem? {
        if (status == "failed") {
            return BoardItem(
                id = id,
                toolTitle = serviceType ?: "HomeDecor AI",
                style = style ?: "",
                roomType = roomType ?: "",
                    imageRes = R.drawable.sample_after_luxury,
                    imageUri = null,
                    imageUrl = null,
                    sourceImageUri = null,
                    sourceImageUrl = sourceImageUrl,
                status = "failed",
                errorMessage = text(R.string.generation_failed_retry),
                prompt = null,
                budgetLabel = "",
                createdAt = createdAt,
            )
        }
        if (imageUrl.isNullOrBlank()) return null
        return BoardItem(
            id = id,
            toolTitle = serviceType ?: "HomeDecor AI",
            style = style ?: "",
            roomType = roomType ?: "",
            imageRes = R.drawable.sample_after_luxury,
            imageUri = null,
            imageUrl = imageUrl,
            sourceImageUri = null,
            sourceImageUrl = sourceImageUrl,
            status = status ?: "ready",
            errorMessage = null,
            prompt = null,
            budgetLabel = "",
            createdAt = createdAt,
        )
    }

    private fun List<GeneratedResult>.toBoardItems(): List<BoardItem> {
        return sortedByDescending { it.createdAt }.map { it.toBoardItem() }
    }

    private fun GeneratedResult.toBoardItem(): BoardItem {
        return BoardItem(
            id = id,
            toolTitle = toolTitle,
            style = style,
            roomType = roomType,
            imageRes = R.drawable.sample_after_luxury,
            imageUri = imageUri,
            imageUrl = imageUrl,
            sourceImageUri = sourceImageUri,
            sourceImageUrl = sourceImageUrl,
            status = status,
            errorMessage = errorMessage,
            prompt = prompt,
            budgetLabel = budgetLabel,
            createdAt = createdAt.toDouble(),
        )
    }

    private fun BoardItem.toGeneratedResult(
        snapshot: HomeDecorUiState,
        projectId: String? = null,
    ): GeneratedResult {
        val existing = workspaceStore.state.value.generatedResults.firstOrNull { it.id == id }
        val sourceImageUri = snapshot.selectedPhotos.firstOrNull()?.uri?.toString()
            ?: snapshot.selectedPhotoUri?.toString()
            ?: existing?.sourceImageUri
        val matchingTool = HomeDecorCatalog.tools.firstOrNull { tool ->
            tool.id == toolTitle || tool.title == toolTitle || tool.title == snapshot.selectedTool.title
        }
        return GeneratedResult(
            id = id,
            projectId = projectId ?: existing?.projectId,
            toolId = matchingTool?.id ?: snapshot.selectedTool.id,
            toolTitle = toolTitle,
            roomType = roomType,
            style = style,
            palette = snapshot.palette,
            prompt = prompt ?: snapshot.customPrompt.ifBlank { null },
            budgetLabel = budgetLabel.ifBlank { existing?.budgetLabel.orEmpty() },
            sourceImageUri = sourceImageUri,
            sourceImageUrl = sourceImageUrl ?: existing?.sourceImageUrl,
            imageUri = imageUri ?: existing?.imageUri,
            imageUrl = imageUrl,
            status = status,
            errorMessage = errorMessage,
            createdAt = if (createdAt > 0.0) createdAt.toLong() else System.currentTimeMillis(),
        )
    }

    private fun HomeDecorUiState.toToolDraft(projectId: String? = null): ToolDraft {
        return ToolDraft(
            toolId = selectedTool.id,
            projectId = projectId,
            selectedPhotoUris = selectedPhotos.mapNotNull { it.uri?.toString() }
                .ifEmpty { selectedPhotoUri?.toString()?.let(::listOf) ?: emptyList() },
            selectedExampleLabels = selectedPhotos.mapNotNull { it.exampleLabel }
                .ifEmpty { selectedExampleLabel?.let(::listOf) ?: emptyList() },
            referencePhotoUri = selectedReferenceUri?.toString(),
            selectedReferenceExampleLabel = selectedReferenceExampleLabel,
            selectedReferenceDiscoverItemId = selectedReferenceDiscoverItemId,
            selectedRooms = selectedRooms,
            selectedStyles = selectedStyles,
            selectedPalettes = selectedPalettes,
            roomType = roomType,
            style = style,
            palette = palette,
            designMode = designMode,
            budgetMode = budgetMode,
            avoidOptions = avoidOptions,
            keepOptions = keepOptions,
            changeOptions = changeOptions,
            preserveRestOfImage = preserveRestOfImage,
            customPrompt = customPrompt,
            layoutConstraints = layoutConstraints,
            mobilierASupprimer = mobilierASupprimer,
            mobilierADeplacer = mobilierADeplacer,
            maskStrokes = maskStrokes.toPersistedMaskStrokes(),
        )
    }

    private fun ToolDraft.toSelectedPhotos(): List<SelectedPhoto> {
        return selectedPhotoUris.map { SelectedPhoto(uri = Uri.parse(it)) } +
            selectedExampleLabels.map { SelectedPhoto(exampleLabel = it) }
    }

    private fun List<MaskStroke>.toPersistedMaskStrokes(): List<PersistedMaskStroke> {
        return map { stroke ->
            PersistedMaskStroke(
                points = stroke.points.map { PersistedMaskPoint(it.x, it.y) },
                brushSize = stroke.brushSize,
                erase = stroke.erase,
            )
        }
    }

    private fun List<PersistedMaskStroke>.toMaskStrokes(): List<MaskStroke> {
        return map { stroke ->
            MaskStroke(
                points = stroke.points.map { MaskPoint(it.x, it.y) },
                brushSize = stroke.brushSize,
                erase = stroke.erase,
            )
        }
    }

    private fun ViewerSummary.toDailyRewardState(): DailyRewardState {
        val claimedAt = lastClaimAt?.toLong()
        val claimDay = claimedAt?.let { millis ->
            Calendar.getInstance().apply { timeInMillis = millis }
        }
        return DailyRewardState(
            currentStreak = streakCount.coerceIn(0, 7),
            lastClaimedAt = claimedAt,
            lastClaimEpochDay = claimDay?.let {
                it.get(Calendar.YEAR).toLong() * 400L + it.get(Calendar.DAY_OF_YEAR)
            },
            nextClaimAt = nextDiamondClaimAt.toLong(),
            lastRewardAmount = creditsAdded,
        )
    }

    private fun exampleImageResFor(toolId: String, exampleLabel: String? = null): Int {
        return when (toolId) {
            "interior" -> when (exampleLabel) {
                "interior-messy-lounge" -> R.drawable.assets_media_examples_interior_interiorbeforemessylounge
                "interior-damaged-room" -> R.drawable.assets_media_examples_interior_interiorbeforedamagedroom
                "interior-outdated-kitchen" -> R.drawable.assets_media_examples_interior_interiorbeforeoutdatedkitchen
                else -> R.drawable.assets_media_examples_interior_interiorbeforeemptyroom
            }
            "facade" -> when (exampleLabel) {
                "facade-scaffold-house" -> R.drawable.assets_media_examples_exterior_exteriorbeforescaffoldhouse
                "facade-weathered-house" -> R.drawable.assets_media_examples_exterior_exteriorbeforeweatheredhouse
                "facade-overgrown-cottage" -> R.drawable.assets_media_examples_exterior_exteriorbeforeovergrowncottage
                else -> R.drawable.assets_media_examples_exterior_exteriorbeforebrickshell
            }
            "garden" -> when (exampleLabel) {
                "garden-muddy-yard" -> R.drawable.assets_media_examples_garden_gardenbeforemuddyyard
                "garden-weedy-yard" -> R.drawable.assets_media_examples_garden_gardenbeforeweedyyard
                "garden-overgrown-corner" -> R.drawable.assets_media_examples_garden_gardenbeforeovergrowncorner
                else -> R.drawable.assets_media_examples_garden_gardenbeforerubbleyard
            }
            "floor" -> R.drawable.assets_media_examples_floor_floorbeforecrackedconcrete
            "paint" -> R.drawable.assets_media_examples_wall_wallbeforerawconcrete
            else -> R.drawable.assets_media_examples_interior_interiorbeforeemptyroom
        }
    }

    private fun firstExampleLabelForTool(toolId: String): String {
        return when (toolId) {
            "facade" -> "facade-scaffold-house"
            "garden" -> "garden-muddy-yard"
            "floor" -> "floor-cracked-concrete"
            "paint" -> "paint-raw-concrete"
            else -> "interior-empty-room"
        }
    }

    private fun StringBuilder.appendAdvancedInstructions(snapshot: HomeDecorUiState) {
        if (snapshot.budgetMode.isNotBlank()) {
            if (isNotEmpty() && !last().isWhitespace()) append(" ")
            append("Budget mode: ").append(snapshot.budgetMode).append(".")
        }
        if (snapshot.avoidOptions.isNotEmpty()) {
            if (isNotEmpty() && !last().isWhitespace()) append(" ")
            append("Avoid these: ").append(snapshot.avoidOptions.joinToString(", ")).append(".")
        }
        if (snapshot.keepOptions.isNotEmpty()) {
            if (isNotEmpty() && !last().isWhitespace()) append(" ")
            append("Advanced keep controls: preserve ").append(snapshot.keepOptions.joinToString(", ")).append(".")
        }
        if (snapshot.changeOptions.isNotEmpty()) {
            if (isNotEmpty() && !last().isWhitespace()) append(" ")
            append("Advanced change controls: change ").append(snapshot.changeOptions.joinToString(", ")).append(".")
        }
        if (snapshot.preserveRestOfImage) {
            if (isNotEmpty() && !last().isWhitespace()) append(" ")
            append("Protection: preserve the rest of the image exactly; only edit the requested target area.")
        }
    }

    private fun discoverImageResForItemId(itemId: String): Int? =
        HomeDecorCatalog.discoverSections
            .asSequence()
            .flatMap { it.items.asSequence() }
            .firstOrNull { it.id == itemId }
            ?.imageRes

    private fun discoverSource(item: GalleryItem): String = "discover:${item.id}"

    private fun discoverSavedTitle(item: GalleryItem, section: DiscoverSection): String =
        listOf(section.title, item.title)
            .filter { it.isNotBlank() }
            .joinToString(" - ")
            .ifBlank { "Discover style" }

    class Factory(
        private val repository: HomeDecorRepository,
        private val context: Context,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeDecorViewModel(repository, context) as T
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "home_decor_preferences"
        const val KEY_DISCLOSURE_ACCEPTED = "disclosure_accepted"
        const val KEY_ANONYMOUS_ID = "anonymous_id"
        const val DEFAULT_HISTORY_PROJECT_NAME = "Saved designs"
    }

    private fun newAnonymousId(): String {
        return UUID.randomUUID().toString().also {
            preferences.edit().putString(KEY_ANONYMOUS_ID, it).apply()
        }
    }
}
