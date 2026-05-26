package com.ismail.homedecorai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
)

data class DiamondPack(
    val id: String,
    val title: String,
    val diamonds: Int,
    val price: String,
    val badge: String? = null,
)

data class HomeDecorUiState(
    val selectedTab: MainTab = MainTab.Tools,
    val selectedTool: DecorTool = HomeDecorCatalog.tools.first(),
    val wizardStage: WizardStage = WizardStage.Photo,
    val selectedPhotoUri: Uri? = null,
    val selectedExampleLabel: String? = null,
    val roomType: String = "Living Room",
    val style: String = "Warm Modern",
    val palette: String = "Natural oak, soft ivory, deep olive",
    val designMode: String = "Preserve Layout",
    val customPrompt: String = "",
    val progressMessage: String = "Preparing your studio...",
    val diamonds: Int = 1,
    val eliteStreakDay: Int = 1,
    val claimedToday: Boolean = false,
    val storeVisible: Boolean = false,
    val viewer: ViewerSummary = ViewerSummary(),
    val board: List<BoardItem> = emptyList(),
    val disclosureAccepted: Boolean = false,
    val isPro: Boolean = false,
    val generationError: String? = null,
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
            serviceType = "redesign",
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

    val maskTargets = listOf(
        "Mur",
        "Sol",
        "Sofa",
        "Table",
        "Cabinet",
        "Éclairage",
    )

    val floorMaterials = listOf(
        "Choix de l'IA",
        "Marbre de Carrare",
        "Chêne naturel",
        "Bois de noyer",
        "Tuile calcaire",
        "Béton poli",
        "Bois chevron",
        "Terrazzo",
    )

    val layoutGoals = listOf(
        "Circulation ouverte",
        "Plus de rangement",
        "Meilleure lumière",
        "Zone de travail",
        "Salon familial",
        "Prêt à louer",
    )

    val referenceStrengths = listOf(
        "Transfert équilibré",
        "Style très fidèle",
        "Palette subtile",
        "Matériaux + lumière",
        "Ambiance mobilier",
        "Bureau à domicile",
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
        DiamondPack("starter", "Pack Découverte", 10, "19,80 MAD"),
        DiamondPack("designer", "Pack Designer", 30, "49,65 MAD", "POPULAIRE"),
        DiamondPack("architect", "Pack Architecte", 100, "129,25 MAD"),
        DiamondPack("estate", "Pack Studio", 250, "249,00 MAD", "MEILLEURE OFFRE"),
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
            ),
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
            id = "garden",
            title = "Jardin",
            cluster = "Paysages",
            serviceToolId = "garden",
            items = listOf(
                GalleryItem("garden-1", "Jardin 1", "Jardin", R.drawable.assets_media_discover_garden_gardenfiresidepatio),
                GalleryItem("garden-2", "Jardin 2", "Jardin", R.drawable.assets_media_discover_generated_garden_garden1),
                GalleryItem("garden-3", "Jardin 3", "Jardin", R.drawable.assets_media_discover_generated_garden_garden2),
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
                roomType = defaultRoomFor(tool),
                style = "Moderne",
                palette = HomeDecorCatalog.palettes.first(),
                designMode = HomeDecorCatalog.designModes.first().first,
                customPrompt = "",
            )
        }
    }

    fun acceptDisclosure() {
        preferences.edit().putBoolean(KEY_DISCLOSURE_ACCEPTED, true).apply()
        _uiState.update { it.copy(disclosureAccepted = true) }
    }

    fun setPhoto(uri: Uri?) {
        _uiState.update { it.copy(selectedPhotoUri = uri, selectedExampleLabel = null) }
    }

    fun selectExamplePhoto(label: String) {
        _uiState.update { it.copy(selectedExampleLabel = label, selectedPhotoUri = null) }
    }

    fun setRoom(room: String) {
        _uiState.update { it.copy(roomType = room) }
    }

    fun setStyle(style: String) {
        _uiState.update { it.copy(style = style) }
    }

    fun setPalette(palette: String) {
        _uiState.update { it.copy(palette = palette) }
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

    fun buyDiamondPack(pack: DiamondPack) {
        _uiState.update { it.copy(diamonds = it.diamonds + pack.diamonds, storeVisible = false) }
    }

    fun nextStage() {
        _uiState.update { state ->
            state.copy(
                wizardStage = when (state.wizardStage) {
                    WizardStage.Photo -> WizardStage.Space
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
                    WizardStage.Style -> WizardStage.Space
                    WizardStage.Refine -> if (state.selectedTool.id == "garden") WizardStage.Space else WizardStage.Style
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
                    progressMessage = "Analyser la géométrie architecturale...",
                    generationError = null,
                )
            }
            runCatching {
                val access = repository.canUserGenerate(anonymousId)
                if (!access.allowed && access.shouldTriggerPaywall) {
                    error(access.message ?: "No Diamonds left. Buy more to continue.")
                }
                val source = readSelectedSource(snapshot)
                _uiState.update { it.copy(progressMessage = "Téléverser la photo vers Convex...") }
                val start = repository.startGeneration(
                    anonymousId = anonymousId,
                    imageBytes = source.bytes,
                    mimeType = source.mimeType,
                    tool = snapshot.selectedTool,
                    roomType = snapshot.roomType,
                    style = snapshot.style,
                    palette = snapshot.palette,
                    designMode = snapshot.designMode,
                    customPrompt = snapshot.customPrompt,
                )
                _uiState.update { it.copy(progressMessage = "Composer le rendu final avec Azure OpenAI...") }
                val ready = repository.waitForGeneration(anonymousId, start.generationId)
                ready.toBoardItem() ?: BoardItem(
                    id = start.generationId,
                    toolTitle = snapshot.selectedTool.title,
                    style = snapshot.style,
                    roomType = snapshot.roomType,
                    imageRes = R.drawable.sample_after_luxury,
                    imageUrl = ready.imageUrl,
                    sourceImageUrl = ready.sourceImageUrl,
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
                            eliteStreakDay = if (isDaySeven) 1 else state.eliteStreakDay + 1,
                            claimedToday = true,
                            isPro = state.isPro || isDaySeven,
                        )
                    }
                }
        }
    }

    fun unlockProPreview() {
        _uiState.update { it.copy(isPro = true, diamonds = it.diamonds + 10) }
    }

    private fun defaultRoomFor(tool: DecorTool): String {
        return when (tool.id) {
            "facade" -> "Villa"
            "garden" -> "Moderne"
            "paint" -> "Mur"
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
        snapshot.selectedPhotoUri?.let { uri ->
            val bytes = appContext.contentResolver.openInputStream(uri)?.use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                    ?: error("Impossible de lire l'image sélectionnée.")
                bitmap.toJpegBytes()
            }
                ?: error("Impossible de lire l'image sélectionnée.")
            return SourceImage(bytes, "image/jpeg")
        }
        val resId = exampleImageResFor(snapshot.selectedTool.id)
        val bytes = appContext.resources.openRawResource(resId).use { stream ->
            val bitmap = BitmapFactory.decodeStream(stream)
                ?: error("Impossible de préparer l'image d'exemple.")
            bitmap.toJpegBytes()
        }
        return SourceImage(bytes, "image/jpeg")
    }

    private fun Bitmap.toJpegBytes(): ByteArray {
        return ByteArrayOutputStream().use { output ->
            compress(Bitmap.CompressFormat.JPEG, 94, output)
            output.toByteArray()
        }
    }

    private fun friendlyGenerationError(error: Throwable): String {
        val message = error.message.orEmpty()
        return when {
            "401" in message && "subscription key" in message ->
                "La génération IA est momentanément indisponible. Vérifiez la configuration Azure OpenAI du backend puis réessayez."
            "No Diamonds" in message || "Diamonds left" in message ->
                "Vous n'avez plus de diamants. Rechargez votre solde pour continuer."
            "converted to a JPG or PNG" in message ->
                "Cette image n'a pas pu être préparée. Essayez une photo JPG ou PNG."
            message.isNotBlank() -> message
            else -> "La génération a échoué. Réessayez dans un instant."
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
