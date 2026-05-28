package com.ismail.homedecorai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.UUID

enum class MainTab { Tools, Create, Discover, ElitePass, Profile }
enum class WizardStage { Photo, Space, Style, Refine, Processing, Result }

data class DecorTool(
    val id: String,
    val title: String,
    val description: String,
    val imageRes: Int,
    val serviceType: String,
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
    val imageUrl: String? = null,
    val sourceImageUrl: String? = null,
    val status: String = "ready",
    val errorMessage: String? = null,
    val prompt: String? = null,
    val createdAt: Double = 0.0,
)

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

data class DiamondPack(
    val id: String,
    val title: String,
    val diamonds: Int,
    val price: String,
    val badge: String? = null,
    val description: String = "",
)

data class HomeDecorUiState(
    val selectedTab: MainTab = MainTab.Tools,
    val selectedTool: DecorTool = HomeDecorCatalog.tools.first(),
    val wizardStage: WizardStage = WizardStage.Photo,
    val selectedPhotoUri: Uri? = null,
    val selectedExampleLabel: String? = null,
    val selectedPhotos: List<SelectedPhoto> = emptyList(),
    val selectedReferenceUri: Uri? = null,
    val selectedReferenceExampleLabel: String? = null,
    val selectedRooms: List<String> = emptyList(),
    val selectedStyles: List<String> = emptyList(),
    val selectedPalettes: List<String> = emptyList(),
    val roomType: String = "",
    val style: String = "",
    val palette: String = "",
    val designMode: String = "Preserve Layout",
    val customPrompt: String = "",
    val progressMessage: String = "Preparing your studio...",
    val diamonds: Int = 1,
    val eliteStreakDay: Int = 1,
    val claimedToday: Boolean = false,
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
    )

    val maskTargets = listOf(
        "Mur",
        "Sol",
        "Sofa",
        "Table",
        "Cabinet",
        "Éclairage",
    )

    val floorMaterials = listOf(
        "Parquet clair",
        "Parquet foncé",
        "Marbre",
        "Béton ciré",
        "Carrelage moderne",
        "Pierre naturelle",
        "Vinyle premium",
        "Tapis élégant",
    )

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

    val paintColors = listOf(
        "Blanc chaud",
        "Beige",
        "Gris clair",
        "Vert sauge",
        "Bleu doux",
        "Terracotta",
        "Noir élégant",
    )

    val replaceSuggestions = listOf(
        "Canapé moderne",
        "Table en bois",
        "Lampe design",
        "Plante intérieure",
        "Fauteuil minimaliste",
        "Meuble TV élégant",
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
    )

    val designModes = listOf(
        "Conserver la structure" to "Gardez les murs, ouvertures et volumes en place tout en améliorant le style.",
        "Rénover librement" to "Autorisez l'IA à proposer une transformation plus ambitieuse et créative.",
    )

    val diamondPacks = listOf(
        DiamondPack("starter", "Découverte", 10, "19,80 MAD", description = "Pour tester plusieurs idées sans engagement."),
        DiamondPack("designer", "Designer", 30, "49,65 MAD", "POPULAIRE", "Le meilleur équilibre pour explorer une pièce complète."),
        DiamondPack("architect", "Architecte", 100, "129,25 MAD", description = "Pensé pour les séries de concepts et variantes."),
        DiamondPack("estate", "Studio", 250, "249,00 MAD", "MEILLEURE OFFRE", "Crédits profonds pour gros projets et portfolios."),
    )

    val gallery = tools.mapIndexed { index, tool ->
        GalleryItem(
            id = tool.id,
            title = tool.title,
            category = if (index < 3) "Spaces" else "Tools",
            imageRes = tool.imageRes,
        )
    }

    val discoverSections = listOf(
        DiscoverSection(
            id = "kitchen",
            title = "Cuisine",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(
                GalleryItem("kitchen-1", "Cuisine 1", "Cuisine", R.drawable.assets_media_discover_generated_kitchen_kitchen1),
                GalleryItem("kitchen-2", "Cuisine 2", "Cuisine", R.drawable.assets_media_discover_generated_kitchen_kitchen2),
                GalleryItem("kitchen-3", "Cuisine 3", "Cuisine", R.drawable.assets_media_discover_generated_kitchen_kitchen3),
                GalleryItem("kitchen-4", "Cuisine 4", "Cuisine", R.drawable.assets_media_discover_generated_kitchen_kitchen4),
                GalleryItem("kitchen-5", "Cuisine 5", "Cuisine", R.drawable.assets_media_discover_generated_kitchen_kitchen5),
                GalleryItem("kitchen-6", "Cuisine 6", "Cuisine", R.drawable.assets_media_discover_generated_kitchen_kitchen6),
                GalleryItem("kitchen-7", "Cuisine 7", "Cuisine", R.drawable.assets_media_discover_generated_kitchen_kitchen7),
            ),
        ),
        DiscoverSection(
            id = "living-room",
            title = "Salon",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(
                GalleryItem("living-1", "Salon 1", "Salon", R.drawable.assets_media_discover_generated_livingroom_livingroom1),
                GalleryItem("living-2", "Salon 2", "Salon", R.drawable.assets_media_discover_generated_livingroom_livingroom2),
                GalleryItem("living-3", "Salon 3", "Salon", R.drawable.assets_media_discover_generated_livingroom_livingroom3),
                GalleryItem("living-4", "Salon 4", "Salon", R.drawable.assets_media_discover_generated_livingroom_livingroom4),
                GalleryItem("living-5", "Salon 5", "Salon", R.drawable.assets_media_discover_generated_livingroom_livingroom5),
                GalleryItem("living-6", "Salon 6", "Salon", R.drawable.assets_media_discover_generated_livingroom_livingroom6),
                GalleryItem("living-7", "Salon 7", "Salon", R.drawable.assets_media_discover_generated_livingroom_livingroom7),
            ),
        ),
        DiscoverSection(
            id = "bedroom",
            title = "Chambre",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(
                GalleryItem("bedroom-1", "Chambre 1", "Chambre", R.drawable.assets_media_discover_generated_bedroom_bedroom1),
                GalleryItem("bedroom-2", "Chambre 2", "Chambre", R.drawable.assets_media_discover_generated_bedroom_bedroom2),
                GalleryItem("bedroom-3", "Chambre 3", "Chambre", R.drawable.assets_media_discover_generated_bedroom_bedroom3),
                GalleryItem("bedroom-4", "Chambre 4", "Chambre", R.drawable.assets_media_discover_generated_bedroom_bedroom4),
                GalleryItem("bedroom-5", "Chambre 5", "Chambre", R.drawable.assets_media_discover_generated_bedroom_bedroom5),
                GalleryItem("bedroom-6", "Chambre 6", "Chambre", R.drawable.assets_media_discover_generated_bedroom_bedroom6),
                GalleryItem("bedroom-7", "Chambre 7", "Chambre", R.drawable.assets_media_discover_generated_bedroom_bedroom7),
            ),
        ),
        DiscoverSection(
            id = "bathroom",
            title = "Salle de bain",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(GalleryItem("bathroom-1", "Salle de bain", "Salle de bain", R.drawable.assets_media_discover_home_homebathroom)),
        ),
        DiscoverSection(
            id = "dining",
            title = "Salle à manger",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(GalleryItem("dining-1", "Salle à manger", "Salle à manger", R.drawable.assets_media_discover_home_homediningroom)),
        ),
        DiscoverSection(
            id = "home-office",
            title = "Bureau à domicile",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(
                GalleryItem("office-1", "Bureau à domicile", "Bureau", R.drawable.assets_media_discover_home_homehomeoffice),
                GalleryItem("office-2", "Étude lumineuse", "Bureau", R.drawable.assets_media_discover_home_homestudy),
            ),
        ),
        DiscoverSection(
            id = "library",
            title = "Bibliothèque",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(GalleryItem("library-1", "Bibliothèque", "Bibliothèque", R.drawable.assets_media_discover_home_homelibrary)),
        ),
        DiscoverSection(
            id = "hall",
            title = "Entrée / couloir",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(GalleryItem("hall-1", "Couloir", "Entrée", R.drawable.assets_media_discover_home_homehall)),
        ),
        DiscoverSection(
            id = "gaming",
            title = "Salle de jeux",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(GalleryItem("gaming-1", "Salle de jeux", "Loisir", R.drawable.assets_media_discover_home_homegamingroom)),
        ),
        DiscoverSection(
            id = "laundry",
            title = "Blanchisserie",
            cluster = "Intérieurs",
            serviceToolId = "interior",
            items = listOf(GalleryItem("laundry-1", "Blanchisserie", "Service", R.drawable.assets_media_discover_home_homelaundry)),
        ),
        DiscoverSection(
            id = "villa",
            title = "Villa",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = listOf(
                GalleryItem("villa-1", "Villa 1", "Villa", R.drawable.assets_media_discover_exterior_exteriormodernvilla),
                GalleryItem("villa-2", "Villa 2", "Villa", R.drawable.assets_media_discover_generated_exterior_exterior1),
                GalleryItem("villa-3", "Villa 3", "Villa", R.drawable.assets_media_discover_generated_exterior_exterior2),
                GalleryItem("villa-4", "Villa 4", "Villa", R.drawable.assets_media_discover_generated_exterior_exterior3),
                GalleryItem("villa-5", "Villa 5", "Villa", R.drawable.assets_media_discover_generated_exterior_exterior4),
                GalleryItem("villa-6", "Villa 6", "Villa", R.drawable.assets_media_discover_generated_exterior_exterior5),
                GalleryItem("villa-7", "Villa 7", "Villa", R.drawable.assets_media_discover_generated_exterior_exterior6),
            ),
        ),
        DiscoverSection(
            id = "apartment",
            title = "Appartement",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = listOf(
                GalleryItem("apartment-1", "Appartement 1", "Appartement", R.drawable.assets_media_discover_exterior_exteriorapartmentblock),
                GalleryItem("apartment-2", "Appartement 2", "Appartement", R.drawable.assets_media_discover_exterior_exteriorglassoffice),
                GalleryItem("apartment-3", "Appartement 3", "Appartement", R.drawable.assets_media_discover_exterior_exteriorretailstorefront),
            ),
        ),
        DiscoverSection(
            id = "residential",
            title = "Résidentiel",
            cluster = "Architecture",
            serviceToolId = "facade",
            items = listOf(
                GalleryItem("residential-1", "Maison avec piscine", "Résidentiel", R.drawable.assets_media_discover_exterior_exteriorpoolhouse),
                GalleryItem("residential-2", "Manoir en pierre", "Résidentiel", R.drawable.assets_media_discover_exterior_exteriorstonemanor),
                GalleryItem("residential-3", "Façade 7", "Résidentiel", R.drawable.assets_media_discover_generated_exterior_exterior7),
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
            ),
        ),
        DiscoverSection(
            id = "garden",
            title = "Jardin",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = listOf(
                GalleryItem("garden-1", "Jardin 1", "Jardin", R.drawable.assets_media_discover_garden_gardenfiresidepatio),
                GalleryItem("garden-2", "Jardin 2", "Jardin", R.drawable.assets_media_discover_generated_garden_garden1),
                GalleryItem("garden-3", "Jardin 3", "Jardin", R.drawable.assets_media_discover_generated_garden_garden2),
                GalleryItem("garden-4", "Jardin 4", "Jardin", R.drawable.assets_media_discover_generated_garden_garden3),
                GalleryItem("garden-5", "Jardin 5", "Jardin", R.drawable.assets_media_discover_generated_garden_garden4),
                GalleryItem("garden-6", "Jardin 6", "Jardin", R.drawable.assets_media_discover_generated_garden_garden5),
                GalleryItem("garden-7", "Jardin 7", "Jardin", R.drawable.assets_media_discover_generated_garden_garden6),
            ),
        ),
        DiscoverSection(
            id = "outdoor-spaces",
            title = "Espaces extérieurs",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = listOf(
                GalleryItem("outdoor-1", "Cour arrière", "Paysage", R.drawable.assets_media_discover_garden_gardenbackyard),
                GalleryItem("outdoor-2", "Terrasse", "Paysage", R.drawable.assets_media_discover_garden_gardenterrace),
                GalleryItem("outdoor-3", "Patio", "Paysage", R.drawable.assets_media_discover_garden_gardenpatio),
                GalleryItem("outdoor-4", "Piscine", "Paysage", R.drawable.assets_media_discover_garden_gardenswimmingpool),
                GalleryItem("outdoor-5", "Entrée villa", "Paysage", R.drawable.assets_media_discover_garden_gardenvillaentry),
            ),
        ),
    )
}

class HomeDecorViewModel(
    private val repository: HomeDecorRepository,
    context: Context,
) : ViewModel() {
    private val appContext = context.applicationContext
    private val preferences = context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val anonymousId = preferences.getString(KEY_ANONYMOUS_ID, null) ?: UUID.randomUUID().toString().also {
        preferences.edit().putString(KEY_ANONYMOUS_ID, it).apply()
    }
    private val _uiState = MutableStateFlow(
        HomeDecorUiState(disclosureAccepted = preferences.getBoolean(KEY_DISCLOSURE_ACCEPTED, false))
    )
    val uiState: StateFlow<HomeDecorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val viewer = runCatching { repository.bootstrapViewer(anonymousId) }
                .getOrElse { repository.viewerSummary(anonymousId) }
            val archive = repository.archive(anonymousId).mapNotNull { it.toBoardItem() }
            _uiState.update {
                it.copy(
                    viewer = viewer,
                    diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                    isPro = viewer.hasProAccess,
                    eliteStreakDay = (viewer.streakCount + 1).coerceIn(1, 7),
                    board = archive,
                )
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
                selectedRooms = emptyList(),
                selectedStyles = emptyList(),
                selectedPalettes = emptyList(),
                roomType = "",
                style = "",
                palette = "",
                designMode = HomeDecorCatalog.designModes.first().first,
                customPrompt = "",
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

    fun removePhoto(index: Int) {
        _uiState.update { state ->
            val next = state.selectedPhotos.filterIndexed { slotIndex, _ -> slotIndex != index }
            state.copy(selectedPhotos = next, selectedPhotoUri = next.firstOrNull()?.uri, selectedExampleLabel = next.firstOrNull()?.exampleLabel)
        }
    }

    fun setReferencePhoto(uri: Uri?) {
        _uiState.update { it.copy(selectedReferenceUri = uri, selectedReferenceExampleLabel = null) }
    }

    fun selectReferenceExample(label: String) {
        _uiState.update { it.copy(selectedReferenceExampleLabel = label, selectedReferenceUri = null) }
    }

    fun setRoom(room: String) {
        _uiState.update { state ->
            if (room in singleRoomSelections) {
                return@update state.copy(selectedRooms = listOf(room), roomType = room)
            }
            val selected = toggleLimited(state.selectedRooms, room, limit = 2)
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

    fun setStyle(style: String) {
        _uiState.update { state ->
            val selected = toggleLimited(state.selectedStyles, style, limit = 2)
            state.copy(selectedStyles = selected, style = selected.joinToString(" + "))
        }
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

    fun setCustomPrompt(prompt: String) {
        _uiState.update { it.copy(customPrompt = prompt) }
    }

    fun openDiamondStore() {
        _uiState.update { it.copy(storeVisible = true) }
    }

    fun closeDiamondStore() {
        _uiState.update { it.copy(storeVisible = false) }
    }

    fun openPaywall() {
        _uiState.update { it.copy(paywallVisible = true) }
    }

    fun closePaywall() {
        _uiState.update { it.copy(paywallVisible = false) }
    }

    fun openAuth() {
        _uiState.update { it.copy(authVisible = true) }
    }

    fun closeAuth() {
        _uiState.update { it.copy(authVisible = false) }
    }

    fun signInWithGooglePreview() {
        _uiState.update {
            it.copy(
                authVisible = false,
                signedInName = "Compte Google",
                signedInEmail = "homedecor.user@gmail.com",
            )
        }
    }

    fun openSettings() {
        _uiState.update { it.copy(settingsVisible = true) }
    }

    fun closeSettings() {
        _uiState.update { it.copy(settingsVisible = false) }
    }

    fun buyDiamondPack(pack: DiamondPack) {
        _uiState.update {
            it.copy(
                purchaseMessage = "Sélectionnez un pack réel dans Google Play pour ajouter des diamants.",
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
                    "paint" -> "Mur marqué"
                    "floor" -> "Sol marqué"
                    "replace" -> "Objet marqué"
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
        viewModelScope.launch {
            _uiState.update { it.copy(purchaseBusy = true, purchaseMessage = "Validation de l'achat...") }
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
                repository.viewerSummary(anonymousId)
            }.onSuccess { viewer ->
                _uiState.update {
                    it.copy(
                        viewer = viewer,
                        diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                        purchaseBusy = false,
                        storeVisible = false,
                        purchaseMessage = "Achat confirmé. Vos diamants sont disponibles.",
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        purchaseBusy = false,
                        purchaseMessage = "L'achat a réussi, mais la synchronisation a échoué. Utilisez Restaurer les achats.",
                    )
                }
            }
        }
    }

    fun syncSubscriptionFromRevenueCat(
        plan: String,
        subscriptionType: String,
        entitlement: String,
        purchasedAt: Double?,
        subscriptionEnd: Double?,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(purchaseBusy = true, purchaseMessage = "Synchronisation Pro...") }
            runCatching {
                repository.setViewerPlanFromRevenueCat(
                    anonymousId = anonymousId,
                    plan = plan,
                    subscriptionType = subscriptionType,
                    entitlement = entitlement,
                    purchasedAt = purchasedAt,
                    subscriptionEnd = subscriptionEnd,
                )
                repository.viewerSummary(anonymousId)
            }.onSuccess { viewer ->
                _uiState.update {
                    it.copy(
                        viewer = viewer,
                        isPro = viewer.hasProAccess,
                        diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                        paywallVisible = false,
                        purchaseBusy = false,
                        purchaseMessage = "Pro activé avec succès.",
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        purchaseBusy = false,
                        purchaseMessage = "Impossible de synchroniser Pro pour le moment.",
                    )
                }
            }
        }
    }

    fun nextStage() {
        _uiState.update { state ->
            state.copy(
                wizardStage = when (state.wizardStage) {
                    WizardStage.Photo -> WizardStage.Space
                    WizardStage.Space -> if (state.selectedTool.id in listOf("garden", "layout")) WizardStage.Refine else WizardStage.Style
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
                    WizardStage.Style -> WizardStage.Space
                    WizardStage.Refine -> if (state.selectedTool.id in listOf("garden", "layout")) WizardStage.Space else WizardStage.Style
                    WizardStage.Processing -> WizardStage.Refine
                    WizardStage.Result -> WizardStage.Refine
                }
            )
        }
    }

    fun generate() {
        val snapshot = _uiState.value
        if (snapshot.diamonds <= 0 && !snapshot.isPro) {
            _uiState.update { it.copy(selectedTab = MainTab.ElitePass) }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    wizardStage = WizardStage.Processing,
                    progressMessage = "Préparation de votre transformation...",
                    generationError = null,
                )
            }
            runCatching {
                val access = repository.canUserGenerate(anonymousId)
                if (!access.allowed && access.shouldTriggerPaywall) {
                    error(access.message ?: "No Diamonds left. Buy more to continue.")
                }
                val source = readSelectedSource(snapshot)
                val reference = readSelectedReference(snapshot)
                val mask = readMaskSource(snapshot)
                _uiState.update { it.copy(progressMessage = "Analyse de l’image...") }
                val start = repository.startGeneration(
                    anonymousId = anonymousId,
                    imageBytes = source.bytes,
                    mimeType = source.mimeType,
                    maskBytes = mask?.bytes,
                    maskMimeType = mask?.mimeType,
                    tool = snapshot.selectedTool,
                    roomType = snapshot.roomType,
                    style = snapshot.style,
                    palette = snapshot.palette,
                    designMode = snapshot.designMode,
                    customPrompt = snapshot.customPrompt,
                    referenceImageBytes = reference?.bytes,
                    referenceMimeType = reference?.mimeType,
                )
                _uiState.update { it.copy(progressMessage = "Application de la nouvelle couleur...") }
                val ready = repository.waitForGeneration(anonymousId, start.generationId)
                _uiState.update { it.copy(progressMessage = "Finalisation du rendu...") }
                ready.toBoardItem() ?: BoardItem(
                    id = start.generationId,
                    toolTitle = snapshot.selectedTool.title,
                    style = snapshot.style,
                    roomType = snapshot.roomType,
                    imageRes = R.drawable.sample_after_luxury,
                    imageUrl = ready.imageUrl,
                    sourceImageUrl = ready.sourceImageUrl,
                    prompt = snapshot.customPrompt,
                    createdAt = System.currentTimeMillis().toDouble(),
                )
            }.onSuccess { result ->
                val viewer = repository.viewerSummary(anonymousId)
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
                _uiState.update {
                    it.copy(
                        wizardStage = WizardStage.Refine,
                        generationError = message,
                        progressMessage = message,
                    )
                }
            }
        }
    }

    fun claimDiamond() {
        viewModelScope.launch {
            runCatching { repository.claimDailyDiamond(anonymousId) }
                .onSuccess { viewer ->
                    _uiState.update {
                        it.copy(
                            viewer = viewer,
                            diamonds = viewer.diamondBalance.coerceAtLeast(viewer.credits),
                            eliteStreakDay = (viewer.streakCount + 1).coerceIn(1, 7),
                            claimedToday = !viewer.canClaimDiamond,
                            isPro = viewer.hasProAccess,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        val isDaySeven = state.eliteStreakDay >= 7
                        val reward = if (isDaySeven) 3 else 1
                        state.copy(
                            diamonds = if (isDaySeven) state.diamonds + reward else (state.diamonds + reward).coerceAtMost(3),
                            eliteStreakDay = if (isDaySeven) 7 else state.eliteStreakDay + 1,
                            claimedToday = true,
                            isPro = state.isPro || isDaySeven,
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

    private fun defaultRoomFor(tool: DecorTool): String {
        return when (tool.id) {
            "facade" -> "Villa"
            "garden" -> "Moderne"
            "paint" -> "Surface à marquer"
            "floor" -> "Choix de l'IA"
            "replace" -> "Mur"
            "reference" -> "Transfert équilibré"
            "layout" -> "Circulation ouverte"
            else -> "Salon"
        }
    }

    private data class SourceImage(
        val bytes: ByteArray,
        val mimeType: String,
    )

    private fun readSelectedSource(snapshot: HomeDecorUiState): SourceImage {
        val firstPhoto = snapshot.selectedPhotos.firstOrNull()
        firstPhoto?.uri?.let { uri -> return readUriSource(uri) }
        firstPhoto?.exampleLabel?.let {
            val resId = exampleImageResFor(snapshot.selectedTool.id)
            val bytes = appContext.resources.openRawResource(resId).use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                    ?: error("Impossible de préparer l'image d'exemple.")
                bitmap.toJpegBytes()
            }
            return SourceImage(bytes, "image/jpeg")
        }
        snapshot.selectedPhotoUri?.let { uri -> return readUriSource(uri) }
        val resId = exampleImageResFor(snapshot.selectedTool.id)
        val bytes = appContext.resources.openRawResource(resId).use { stream ->
            val bitmap = BitmapFactory.decodeStream(stream)
                ?: error("Impossible de préparer l'image d'exemple.")
            bitmap.toJpegBytes()
        }
        return SourceImage(bytes, "image/jpeg")
    }

    private fun readSelectedReference(snapshot: HomeDecorUiState): SourceImage? {
        if (snapshot.selectedTool.id != "reference") return null
        snapshot.selectedReferenceUri?.let { uri -> return readUriSource(uri) }
        if (snapshot.selectedReferenceExampleLabel != null) {
            val bytes = appContext.resources.openRawResource(R.drawable.tool_reference).use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                    ?: error("Impossible de préparer l'image de référence.")
                bitmap.toJpegBytes()
            }
            return SourceImage(bytes, "image/jpeg")
        }
        return null
    }

    private fun readMaskSource(snapshot: HomeDecorUiState): SourceImage? {
        if (snapshot.selectedTool.id !in setOf("paint", "floor", "replace")) return null
        if (snapshot.maskStrokes.none { !it.erase && it.points.size > 1 }) {
            error("A real mask is required before generating.")
        }
        return SourceImage(snapshot.maskStrokes.toMaskPngBytes(), "image/png")
    }

    private fun readUriSource(uri: Uri): SourceImage {
        val bytes = appContext.contentResolver.openInputStream(uri)?.use { stream ->
            val bitmap = BitmapFactory.decodeStream(stream)
                ?: error("Impossible de lire l'image sélectionnée.")
            bitmap.toJpegBytes()
        }
            ?: error("Impossible de lire l'image sélectionnée.")
        return SourceImage(bytes, "image/jpeg")
    }

    private fun Bitmap.toJpegBytes(): ByteArray {
        return ByteArrayOutputStream().use { output ->
            compress(Bitmap.CompressFormat.JPEG, 94, output)
            output.toByteArray()
        }
    }

    private fun List<MaskStroke>.toMaskPngBytes(): ByteArray {
        val width = 1024
        val height = 1024
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        forEach { stroke ->
            if (stroke.points.size < 2) return@forEach
            paint.strokeWidth = stroke.brushSize.coerceIn(8f, 96f)
            paint.color = if (stroke.erase) Color.TRANSPARENT else Color.WHITE
            paint.xfermode = if (stroke.erase) android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR) else null
            val path = Path().apply {
                val first = stroke.points.first()
                moveTo(first.x.coerceIn(0f, 1f) * width, first.y.coerceIn(0f, 1f) * height)
                stroke.points.drop(1).forEach { point ->
                    lineTo(point.x.coerceIn(0f, 1f) * width, point.y.coerceIn(0f, 1f) * height)
                }
            }
            canvas.drawPath(path, paint)
        }
        return ByteArrayOutputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.toByteArray()
        }
    }

    private fun friendlyGenerationError(error: Throwable): String {
        val message = error.message.orEmpty()
        return when {
            "A real mask" in message ->
                "Marquez la zone à transformer avant de générer."
            "401" in message || "403" in message || "Access denied" in message || "subscription" in message || "Azure" in message || "OpenAI" in message ->
                "La génération IA est momentanément indisponible. Réessayez dans un instant ou vérifiez la configuration du service."
            "No Diamonds" in message || "Diamonds left" in message ->
                "Vous n'avez plus de diamants. Rechargez votre solde pour continuer."
            "converted to a JPG or PNG" in message ->
                "Cette image n'a pas pu être préparée. Essayez une photo JPG ou PNG."
            "still processing" in message ->
                "Votre design prend plus de temps que prévu. Retrouvez-le dans le portfolio dans quelques instants."
            "Generation failed" in message || "Convex" in message || "upload" in message || "API" in message ->
                "La génération a échoué. Vérifiez votre connexion ou réessayez."
            message.isNotBlank() && message.any { it in 'à'..'ÿ' } -> message
            message.isNotBlank() -> "La génération a échoué. Vérifiez votre connexion ou réessayez."
            else -> "La génération a échoué. Vérifiez votre connexion ou réessayez."
        }
    }

    private fun ArchiveGeneration.toBoardItem(): BoardItem? {
        if (status == "failed") {
            return BoardItem(
                id = id,
                toolTitle = serviceType ?: "HomeDecor AI",
                style = style ?: "",
                roomType = roomType ?: "",
                imageRes = R.drawable.sample_after_luxury,
                imageUrl = imageUrl,
                sourceImageUrl = sourceImageUrl,
            status = "failed",
            errorMessage = errorMessage,
            prompt = null,
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
            imageUrl = imageUrl,
            sourceImageUrl = sourceImageUrl,
            status = status ?: "ready",
            errorMessage = errorMessage,
            prompt = null,
            createdAt = createdAt,
        )
    }

    private fun exampleImageResFor(toolId: String): Int {
        return when (toolId) {
            "facade" -> R.drawable.assets_media_examples_exterior_exteriorbeforebrickshell
            "garden" -> R.drawable.assets_media_examples_garden_gardenbeforerubbleyard
            "floor" -> R.drawable.assets_media_examples_floor_floorbeforecrackedconcrete
            "paint" -> R.drawable.assets_media_examples_wall_wallbeforerawconcrete
            else -> R.drawable.assets_media_examples_interior_interiorbeforeemptyroom
        }
    }

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
    }
}
