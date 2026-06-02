package com.ismail.homedecorai.ui.utility

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ViewQuilt
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.ismail.homedecorai.DecorTool
import com.ismail.homedecorai.DiamondPack
import com.ismail.homedecorai.DiscoverSection
import com.ismail.homedecorai.FavoriteItem
import com.ismail.homedecorai.GalleryItem
import com.ismail.homedecorai.GeneratedResult
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.MainTab
import com.ismail.homedecorai.MoodboardItem
import com.ismail.homedecorai.R
import com.ismail.homedecorai.SelectedPhoto
import com.ismail.homedecorai.WizardStage
import com.revenuecat.purchases.Package
import java.text.DateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

data class StepCopy(
    @StringRes val titleRes: Int,
    @StringRes val bodyRes: Int,
    val options: List<String> = emptyList(),
)

@StringRes
fun tabLabelRes(tab: MainTab): Int = when (tab) {
    MainTab.Tools -> R.string.nav_tools
    MainTab.Discover -> R.string.nav_discover
    MainTab.Profile -> R.string.nav_profile
    MainTab.Create -> R.string.workflow_interior
}

@StringRes
fun toolTitleRes(tool: DecorTool): Int = when (tool.id) {
    "interior" -> R.string.tool_interior_title
    "facade" -> R.string.tool_facade_title
    "garden" -> R.string.tool_garden_title
    "paint" -> R.string.tool_paint_title
    "floor" -> R.string.tool_floor_title
    "layout" -> R.string.tool_layout_title
    "replace" -> R.string.tool_replace_title
    "reference" -> R.string.tool_reference_title
    else -> R.string.app_name
}

@StringRes
fun toolDescriptionRes(tool: DecorTool): Int = when (tool.id) {
    "interior" -> R.string.tool_interior_description
    "facade" -> R.string.tool_facade_description
    "garden" -> R.string.tool_garden_description
    "paint" -> R.string.tool_paint_description
    "floor" -> R.string.tool_floor_description
    "layout" -> R.string.tool_layout_description
    "replace" -> R.string.tool_replace_description
    "reference" -> R.string.tool_reference_description
    else -> R.string.tool_interior_description
}

@StringRes
fun workflowTitleRes(tool: DecorTool): Int = when (tool.id) {
    "interior" -> R.string.workflow_interior
    "facade" -> R.string.workflow_exterior
    "garden" -> R.string.workflow_garden
    "paint" -> R.string.workflow_paint
    "floor" -> R.string.workflow_floor
    "layout" -> R.string.workflow_layout
    "replace" -> R.string.workflow_replace
    "reference" -> R.string.workflow_reference
    else -> toolTitleRes(tool)
}

@Composable
fun localizedToolTitle(tool: DecorTool): String = stringResource(toolTitleRes(tool))

@Composable
fun localizedToolDescription(tool: DecorTool): String = stringResource(toolDescriptionRes(tool))

@Composable
fun localizedWorkflowTitle(tool: DecorTool): String = stringResource(workflowTitleRes(tool))

@StringRes
fun optionLabelRes(label: String): Int? = when (label) {
    "Suggestion IA" -> R.string.option_ai_suggestion
    "Salon" -> R.string.option_room_living_room
    "Chambre à coucher" -> R.string.option_room_bedroom
    "Cuisine" -> R.string.option_room_kitchen
    "Salle de bain" -> R.string.option_room_bathroom
    "Bureau à domicile" -> R.string.option_room_home_office
    "Salle à manger" -> R.string.option_room_dining_room
    "Chambre d'enfant" -> R.string.option_room_child_room
    "Cinéma maison" -> R.string.option_room_home_cinema
    "Salle de jeux" -> R.string.option_room_game_room
    "Entrée / couloir" -> R.string.option_room_entry_hall
    "Bibliothèque" -> R.string.option_room_library
    "Blanchisserie" -> R.string.option_room_laundry
    "Appartement" -> R.string.option_building_apartment
    "Maison" -> R.string.option_building_house
    "Immeuble de bureaux" -> R.string.option_building_office
    "Résidentiel" -> R.string.option_building_residential
    "Vente au détail" -> R.string.option_building_retail
    "Villa" -> R.string.option_building_villa
    "Jardin" -> R.string.workflow_garden
    "Cour arrière" -> R.string.option_outdoor_backyard
    "Terrasse" -> R.string.option_outdoor_terrace
    "Patio" -> R.string.option_outdoor_patio
    "Cour" -> R.string.option_outdoor_yard
    "Piscine" -> R.string.option_outdoor_pool
    "Jardin avant" -> R.string.option_outdoor_front_garden
    "Noël" -> R.string.option_garden_christmas
    "Moderne" -> R.string.option_style_modern
    "Luxe" -> R.string.option_style_luxury
    "Japandi" -> R.string.option_style_japandi
    "Cyberpunk" -> R.string.option_style_cyberpunk
    "Tropicale" -> R.string.option_style_tropical
    "Minimaliste" -> R.string.option_style_minimalist
    "Marocain" -> R.string.option_style_moroccan
    "Scandinave" -> R.string.option_style_scandinavian
    "Bohème" -> R.string.option_style_bohemian
    "Midcentury" -> R.string.option_style_midcentury
    "Art Deco" -> R.string.option_style_art_deco
    "Côtier" -> R.string.option_style_coastal
    "Rustique" -> R.string.option_style_rustic
    "Vintage" -> R.string.option_style_vintage
    "Méditerranéen" -> R.string.option_style_mediterranean
    "Glam" -> R.string.option_style_glam
    "Campagne française" -> R.string.option_style_french_country
    "Mur" -> R.string.option_mask_wall
    "Sol" -> R.string.option_mask_floor
    "Sofa" -> R.string.option_mask_sofa
    "Table" -> R.string.option_mask_table
    "Cabinet" -> R.string.option_mask_cabinet
    "Éclairage" -> R.string.option_mask_lighting
    "Parquet clair" -> R.string.option_floor_light_wood
    "Parquet foncé" -> R.string.option_floor_dark_wood
    "Marbre" -> R.string.option_floor_marble
    "Chêne" -> R.string.option_material_oak_wood
    "Noyer" -> R.string.option_material_walnut
    "Béton" -> R.string.option_material_concrete
    "Limewash" -> R.string.option_material_limewash
    "Terrazzo" -> R.string.option_material_terrazzo
    "Carrelage blanc" -> R.string.option_material_white_tile
    "Carrelage noir" -> R.string.option_material_black_tile
    "Peinture beige chaude" -> R.string.option_material_warm_beige_paint
    "Peinture sombre élégante" -> R.string.option_material_dark_elegant_paint
    "Béton ciré" -> R.string.option_floor_polished_concrete
    "Carrelage moderne" -> R.string.option_floor_modern_tile
    "Pierre naturelle" -> R.string.option_floor_natural_stone
    "Vinyle premium" -> R.string.option_floor_premium_vinyl
    "Tapis élégant" -> R.string.option_floor_elegant_carpet
    "Circulation ouverte" -> R.string.option_layout_open_flow
    "Plus de rangement" -> R.string.option_layout_more_storage
    "Coin bureau" -> R.string.option_layout_office_corner
    "Espace familial" -> R.string.option_layout_family_space
    "Salon plus spacieux" -> R.string.option_layout_larger_living_room
    "Meilleure lumière" -> R.string.option_layout_better_light
    "Réorganisation complète" -> R.string.option_layout_full_reorg
    "Subtil" -> R.string.option_reference_subtle
    "Équilibré" -> R.string.option_reference_balanced
    "Fidèle" -> R.string.option_reference_faithful
    "Très fidèle" -> R.string.option_reference_very_faithful
    "Palette seulement" -> R.string.option_reference_palette_only
    "Matériaux" -> R.string.option_reference_materials
    "Mobilier" -> R.string.option_reference_furniture
    "Lumière" -> R.string.option_reference_light
    "Ambiance complète" -> R.string.option_reference_full_mood
    "Blanc chaud" -> R.string.option_paint_warm_white
    "Beige" -> R.string.option_paint_beige
    "Gris clair" -> R.string.option_paint_light_gray
    "Vert sauge" -> R.string.option_paint_sage_green
    "Bleu doux" -> R.string.option_paint_soft_blue
    "Terracotta" -> R.string.option_paint_terracotta
    "Noir élégant" -> R.string.option_paint_elegant_black
    "Canapé beige moderne" -> R.string.option_replace_sofa
    "Table basse en bois clair" -> R.string.option_replace_coffee_table
    "Suspension noire minimaliste" -> R.string.option_replace_black_pendant
    "Grande plante en pot" -> R.string.option_replace_potted_plant
    "Fauteuil bouclé crème" -> R.string.option_replace_boucle_armchair
    "Meuble TV noyer épuré" -> R.string.option_replace_tv_unit
    "Remplacer le sofa" -> R.string.option_replace_template_sofa
    "Remplacer la table" -> R.string.option_replace_template_table
    "Remplacer la lampe" -> R.string.option_replace_template_lamp
    "Remplacer le tapis" -> R.string.option_replace_template_rug
    "Remplacer l'art mural" -> R.string.option_replace_template_wall_art
    "Remplacer la plante" -> R.string.option_replace_template_plant
    "Remplacer la chaise" -> R.string.option_replace_template_chair
    "Remplacer le cabinet" -> R.string.option_replace_template_cabinet
    "Mélange organisé" -> R.string.option_palette_curated_mix
    "Gris millénaire" -> R.string.option_palette_millennial_gray
    "Mirage en terre cuite" -> R.string.option_palette_terracotta_mirage
    "Teintes forestières" -> R.string.option_palette_forest_tones
    "Verger de pêchers" -> R.string.option_palette_peach_orchard
    "Fleur fuchsia" -> R.string.option_palette_fuchsia_bloom
    "Gemme d'émeraude" -> R.string.option_palette_emerald_gem
    "Brise pastel" -> R.string.option_palette_pastel_breeze
    "Brume océanique" -> R.string.option_palette_ocean_mist
    "Crépuscule de velours" -> R.string.option_palette_velvet_twilight
    "Rêve d'améthyste" -> R.string.option_palette_amethyst_dream
    "Fuchsia Noir" -> R.string.option_palette_fuchsia_noir
    "Conserver la structure" -> R.string.option_design_preserve
    "Rénover librement" -> R.string.option_design_free
    else -> null
}

@Composable
fun localizedOption(label: String): String = optionLabelRes(label)?.let { stringResource(it) } ?: label

@Composable
fun localizedReplacementPrompt(prompt: String): String =
    prompt.takeIf { it.isNotBlank() }?.let { localizedOption(it) }.orEmpty()

@StringRes
fun discoverClusterRes(cluster: String): Int = when (cluster) {
    "Architecture" -> R.string.discover_cluster_architecture
    "Paysages" -> R.string.discover_cluster_landscapes
    else -> R.string.discover_cluster_interiors
}

@StringRes
fun discoverSectionTitleRes(section: DiscoverSection): Int = when (section.id) {
    "kitchen" -> R.string.discover_section_kitchen
    "living-room" -> R.string.discover_section_living_room
    "bedroom" -> R.string.discover_section_bedroom
    "bathroom" -> R.string.discover_section_bathroom
    "dining" -> R.string.discover_section_dining
    "home-office" -> R.string.discover_section_home_office
    "library" -> R.string.discover_section_library
    "hall" -> R.string.discover_section_hall
    "gaming" -> R.string.discover_section_gaming
    "laundry" -> R.string.discover_section_laundry
    "villa" -> R.string.discover_section_villa
    "house" -> R.string.option_building_house
    "apartment" -> R.string.discover_section_apartment
    "office-building" -> R.string.option_building_office
    "retail" -> R.string.option_building_retail
    "residential" -> R.string.discover_section_residential
    "wall-scenes" -> R.string.discover_section_wall_scenes
    "floors" -> R.string.discover_section_floors
    "garden" -> R.string.discover_section_garden
    "backyard" -> R.string.option_outdoor_backyard
    "terrace" -> R.string.option_outdoor_terrace
    "patio" -> R.string.option_outdoor_patio
    "yard" -> R.string.option_outdoor_yard
    "pool" -> R.string.option_outdoor_pool
    "front-garden" -> R.string.option_outdoor_front_garden
    "outdoor-spaces" -> R.string.discover_section_outdoor_spaces
    else -> R.string.nav_discover
}

@StringRes
fun discoverSectionSubtitleRes(section: DiscoverSection): Int = when (section.id) {
    "kitchen" -> R.string.discover_subtitle_kitchen
    "living-room" -> R.string.discover_subtitle_living_room
    "bedroom" -> R.string.discover_subtitle_bedroom
    "bathroom" -> R.string.discover_subtitle_bathroom
    "dining" -> R.string.discover_subtitle_dining
    "home-office" -> R.string.discover_subtitle_home_office
    "library" -> R.string.discover_subtitle_library
    "hall" -> R.string.discover_subtitle_hall
    "gaming" -> R.string.discover_subtitle_gaming
    "laundry" -> R.string.discover_subtitle_laundry
    "villa" -> R.string.discover_subtitle_villa
    "house" -> R.string.discover_subtitle_house
    "apartment" -> R.string.discover_subtitle_apartment
    "office-building" -> R.string.discover_subtitle_office_building
    "retail" -> R.string.discover_subtitle_retail
    "residential" -> R.string.discover_subtitle_residential
    "wall-scenes" -> R.string.discover_subtitle_wall_scenes
    "floors" -> R.string.discover_subtitle_floors
    "garden" -> R.string.discover_subtitle_garden
    "backyard" -> R.string.discover_subtitle_backyard
    "terrace" -> R.string.discover_subtitle_terrace
    "patio" -> R.string.discover_subtitle_patio
    "yard" -> R.string.discover_subtitle_yard
    "pool" -> R.string.discover_subtitle_pool
    "front-garden" -> R.string.discover_subtitle_front_garden
    "outdoor-spaces" -> R.string.discover_subtitle_outdoor_spaces
    else -> R.string.discover_hero_body
}

@StringRes
fun galleryCategoryRes(category: String): Int = when (category) {
    "Cuisine" -> R.string.category_kitchen
    "Salon" -> R.string.category_living_room
    "Chambre" -> R.string.category_bedroom
    "Salle de bain" -> R.string.category_bathroom
    "Salle à manger" -> R.string.category_dining_room
    "Bureau" -> R.string.category_office
    "Bibliothèque" -> R.string.category_library
    "Entrée" -> R.string.category_entry
    "Loisir" -> R.string.category_leisure
    "Service" -> R.string.category_service
    "Villa" -> R.string.category_villa
    "Maison" -> R.string.option_building_house
    "Appartement" -> R.string.category_apartment
    "Immeuble de bureaux" -> R.string.option_building_office
    "Vente au détail" -> R.string.option_building_retail
    "Résidentiel" -> R.string.category_residential
    "Mur" -> R.string.category_wall
    "Sol" -> R.string.category_floor
    "Jardin" -> R.string.category_garden
    "Cour arrière" -> R.string.option_outdoor_backyard
    "Terrasse" -> R.string.option_outdoor_terrace
    "Patio" -> R.string.option_outdoor_patio
    "Cour" -> R.string.option_outdoor_yard
    "Piscine" -> R.string.option_outdoor_pool
    "Jardin avant" -> R.string.option_outdoor_front_garden
    "Paysage" -> R.string.category_landscape
    else -> R.string.nav_discover
}

@StringRes
fun galleryItemTitleRes(item: GalleryItem): Int? = when (item.id) {
    "bathroom-1" -> R.string.gallery_item_bathroom
    "dining-1" -> R.string.gallery_item_dining_room
    "office-1" -> R.string.gallery_item_home_office
    "office-2" -> R.string.gallery_item_bright_study
    "library-1" -> R.string.gallery_item_library
    "hall-1" -> R.string.gallery_item_hallway
    "gaming-1" -> R.string.gallery_item_game_room
    "laundry-1" -> R.string.gallery_item_laundry
    "residential-1" -> R.string.gallery_item_pool_house
    "residential-2" -> R.string.gallery_item_stone_manor
    "residential-3" -> R.string.gallery_item_facade_7
    "wall-1" -> R.string.gallery_item_soft_ivory
    "wall-2" -> R.string.gallery_item_sage_green
    "wall-3" -> R.string.gallery_item_midnight_blue
    "wall-4" -> R.string.gallery_item_gallery_charcoal
    "wall-5" -> R.string.gallery_item_terracotta
    "wall-6" -> R.string.gallery_item_dusty_rose
    "floor-1" -> R.string.gallery_item_natural_oak
    "floor-2" -> R.string.gallery_item_walnut
    "floor-3" -> R.string.gallery_item_marble
    "floor-4" -> R.string.gallery_item_polished_concrete
    "floor-5" -> R.string.gallery_item_chevron
    "floor-6" -> R.string.gallery_item_terracotta_floor
    "outdoor-1" -> R.string.gallery_item_backyard
    "outdoor-2" -> R.string.gallery_item_terrace
    "outdoor-3" -> R.string.gallery_item_patio
    "outdoor-4" -> R.string.gallery_item_pool
    "outdoor-5" -> R.string.gallery_item_villa_entry
    else -> null
}

fun galleryItemNumber(item: GalleryItem): Int? = item.id.substringAfterLast('-').toIntOrNull()?.takeIf {
    item.id.startsWith("kitchen-") ||
        item.id.startsWith("living-") ||
        item.id.startsWith("bedroom-") ||
        item.id.startsWith("bathroom-") ||
        item.id.startsWith("dining-") ||
        item.id.startsWith("office-") ||
        item.id.startsWith("library-") ||
        item.id.startsWith("hall-") ||
        item.id.startsWith("gaming-") ||
        item.id.startsWith("laundry-") ||
        item.id.startsWith("villa-") ||
        item.id.startsWith("house-") ||
        item.id.startsWith("apartment-") ||
        item.id.startsWith("office-building-") ||
        item.id.startsWith("retail-") ||
        item.id.startsWith("residential-") ||
        item.id.startsWith("wall-") ||
        item.id.startsWith("floor-") ||
        item.id.startsWith("garden-") ||
        item.id.startsWith("backyard-") ||
        item.id.startsWith("terrace-") ||
        item.id.startsWith("patio-") ||
        item.id.startsWith("yard-") ||
        item.id.startsWith("pool-") ||
        item.id.startsWith("front-garden-")
}

fun discoverItemById(itemId: String): GalleryItem? =
    HomeDecorCatalog.discoverSections
        .asSequence()
        .flatMap { it.items.asSequence() }
        .firstOrNull { it.id == itemId }

fun discoverSource(item: GalleryItem): String = "discover:${item.id}"

@Composable
fun localizedDiscoverCluster(cluster: String): String = stringResource(discoverClusterRes(cluster))

@Composable
fun localizedDiscoverSection(section: DiscoverSection): String = stringResource(discoverSectionTitleRes(section))

@Composable
fun localizedDiscoverSectionSubtitle(section: DiscoverSection): String = stringResource(discoverSectionSubtitleRes(section))

@Composable
fun localizedGalleryCategory(category: String): String = stringResource(galleryCategoryRes(category))

@Composable
fun localizedGalleryTitle(item: GalleryItem): String {
    galleryItemTitleRes(item)?.let { return stringResource(it) }
    galleryItemNumber(item)?.let { return stringResource(R.string.gallery_numbered_title, localizedGalleryCategory(item.category), it) }
    return localizedGalleryCategory(item.category)
}

@StringRes
fun diamondPackTitleRes(pack: DiamondPack): Int = when (pack.id) {
    "starter" -> R.string.pack_starter
    "designer" -> R.string.pack_designer
    "architect" -> R.string.pack_architect
    "estate" -> R.string.pack_estate
    else -> R.string.pack_estate
}

@StringRes
fun diamondPackBadgeRes(pack: DiamondPack): Int? = when (pack.id) {
    "designer" -> R.string.pack_badge_popular
    "estate" -> R.string.pack_badge_best_offer
    else -> null
}

@StringRes
fun diamondPackDescriptionRes(pack: DiamondPack): Int = when (pack.id) {
    "starter" -> R.string.pack_starter_description
    "designer" -> R.string.pack_designer_description
    "architect" -> R.string.pack_architect_description
    "estate" -> R.string.pack_estate_description
    else -> R.string.pack_starter_description
}

private val DiamondRevenueCatProductIds = mapOf(
    "starter" to setOf("starter", "diamond_starter", "diamonds_starter", "diamond_pack_starter", "starter_diamonds", "diamonds_10", "diamond_10", "10_diamonds"),
    "designer" to setOf("designer", "diamond_designer", "diamonds_designer", "diamond_pack_designer", "designer_diamonds", "diamonds_30", "diamond_30", "30_diamonds"),
    "architect" to setOf("architect", "diamond_architect", "diamonds_architect", "diamond_pack_architect", "architect_diamonds", "diamonds_100", "diamond_100", "100_diamonds"),
    "estate" to setOf("estate", "studio", "diamond_estate", "diamonds_estate", "diamond_pack_estate", "estate_diamonds", "diamond_studio", "diamonds_studio", "studio_diamonds", "diamonds_300", "diamond_300", "300_diamonds"),
)

private val DiamondRevenueCatPackageIds = mapOf(
    "starter" to setOf("starter", "diamond_starter", "diamonds_starter"),
    "designer" to setOf("designer", "diamond_designer", "diamonds_designer"),
    "architect" to setOf("architect", "diamond_architect", "diamonds_architect"),
    "estate" to setOf("estate", "studio", "diamond_estate", "diamonds_estate", "diamond_studio", "diamonds_studio"),
)

fun Package.matchesDiamondPack(pack: DiamondPack): Boolean {
    val productId = product.id.trim().lowercase()
    val packageId = identifier.trim().lowercase()
    return productId in DiamondRevenueCatProductIds.orEmpty(pack.id) ||
        packageId in DiamondRevenueCatPackageIds.orEmpty(pack.id)
}

fun Map<String, Set<String>>.orEmpty(key: String): Set<String> = get(key).orEmpty()

@StringRes
fun designModeDescriptionRes(description: String): Int = when (description) {
    "Gardez les murs, ouvertures et volumes en place tout en améliorant le style." -> R.string.option_design_preserve_description
    "Autorisez l'IA à proposer une transformation plus ambitieuse et créative." -> R.string.option_design_free_description
    else -> R.string.option_design_preserve_description
}

@StringRes
fun boardToolTitleRes(title: String): Int? = when (title) {
    "Design d'intérieur", "redesign", "interior" -> R.string.tool_interior_title
    "Conception extérieure", "facade", "exterior" -> R.string.tool_facade_title
    "Conception de jardin", "garden" -> R.string.tool_garden_title
    "Peinture intelligente", "paint" -> R.string.tool_paint_title
    "Relooking du sol", "floor" -> R.string.tool_floor_title
    "Agencement Intelligent", "layout" -> R.string.tool_layout_title
    "Remplacer des objets", "replace" -> R.string.tool_replace_title
    "Transfert de style de référence", "reference" -> R.string.tool_reference_title
    "HomeDecor AI" -> R.string.app_name
    else -> null
}

fun toolIcon(tool: DecorTool): ImageVector {
    return when (tool.id) {
        "interior" -> Icons.Rounded.Home
        "facade" -> Icons.Rounded.Landscape
        "garden" -> Icons.Rounded.Brush
        "paint" -> Icons.Rounded.Brush
        "floor" -> Icons.Rounded.Layers
        "layout" -> Icons.AutoMirrored.Rounded.ViewQuilt
        "replace" -> Icons.Rounded.Refresh
        else -> Icons.Rounded.AutoAwesome
    }
}

fun photoCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "facade" -> StepCopy(R.string.step_photo_facade_title, R.string.step_photo_facade_body)
        "garden" -> StepCopy(R.string.step_photo_garden_title, R.string.step_photo_garden_body)
        "floor" -> StepCopy(R.string.step_photo_floor_title, R.string.step_photo_floor_body)
        "paint" -> StepCopy(R.string.step_photo_paint_title, R.string.step_photo_paint_body)
        "layout" -> StepCopy(R.string.step_photo_layout_title, R.string.step_photo_layout_body)
        "replace" -> StepCopy(R.string.step_photo_replace_title, R.string.step_photo_replace_body)
        "reference" -> StepCopy(R.string.step_photo_reference_title, R.string.step_photo_reference_body)
        else -> StepCopy(R.string.step_photo_interior_title, R.string.step_photo_interior_body)
    }
}

fun stepTwoCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "facade" -> StepCopy(
            R.string.step_building_type_title,
            R.string.step_building_type_body,
            HomeDecorCatalog.buildingTypes,
        )
        "garden" -> StepCopy(
            R.string.step_choose_garden_style_title,
            R.string.step_choose_garden_style_body,
            listOf("Suggestion IA") + HomeDecorCatalog.gardenStyles,
        )
        "paint" -> StepCopy(
            R.string.step_paint_surfaces_title,
            R.string.step_paint_surfaces_body,
        )
        "floor" -> StepCopy(
            R.string.step_mark_floor_title,
            R.string.step_mark_floor_body,
        )
        "layout" -> StepCopy(
            R.string.step_layout_goal_title,
            R.string.step_layout_goal_body,
            HomeDecorCatalog.layoutGoals,
        )
        "replace" -> StepCopy(
            R.string.step_mark_object_title,
            R.string.step_mark_object_body,
        )
        "reference" -> StepCopy(
            R.string.step_add_reference_title,
            R.string.step_add_reference_body,
        )
        else -> StepCopy(
            R.string.step_choose_space_title,
            R.string.step_choose_space_body,
            HomeDecorCatalog.rooms,
        )
    }
}

fun stepThreeCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "paint" -> StepCopy(
            R.string.step_choose_color_title,
            R.string.step_choose_color_body,
            HomeDecorCatalog.paintColors,
        )
        "floor" -> StepCopy(
            R.string.step_choose_material_title,
            R.string.step_choose_material_body,
            HomeDecorCatalog.floorMaterials,
        )
        "replace" -> StepCopy(
            R.string.step_describe_replacement_title,
            R.string.step_describe_replacement_body,
            HomeDecorCatalog.replaceSuggestions,
        )
        "reference" -> StepCopy(
            R.string.step_transfer_strength_title,
            R.string.step_transfer_strength_body,
            HomeDecorCatalog.referenceStrengths,
        )
        else -> StepCopy(
            R.string.step_choose_style_title,
            R.string.step_style_body,
            listOf("Suggestion IA") + HomeDecorCatalog.styles,
        )
    }
}

fun stepFourCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "facade", "garden", "paint" -> StepCopy(R.string.step_color_harmony_title, R.string.step_color_harmony_body)
        "floor" -> StepCopy(R.string.step_describe_vision_title, R.string.step_describe_floor_body)
        "replace" -> StepCopy(R.string.step_review_edit_title, R.string.step_review_edit_body)
        "reference" -> StepCopy(R.string.step_review_transfer_title, R.string.step_review_transfer_body)
        "layout" -> StepCopy(R.string.step_design_mode_title, R.string.step_design_mode_body)
        else -> StepCopy(R.string.step_design_mode_title, R.string.step_design_mode_color_body)
    }
}

fun choiceIcon(label: String): ImageVector {
    return when {
        label.contains("Cuisine", ignoreCase = true) -> Icons.Rounded.Brush
        label.contains("Salle", ignoreCase = true) -> Icons.Rounded.Layers
        label.contains("Bureau", ignoreCase = true) -> Icons.AutoMirrored.Rounded.ViewQuilt
        label.contains("Chambre", ignoreCase = true) -> Icons.Rounded.Home
        label.contains("Appartement", ignoreCase = true) -> Icons.Rounded.Home
        label.contains("Villa", ignoreCase = true) -> Icons.Rounded.Landscape
        label.contains("bureau", ignoreCase = true) -> Icons.AutoMirrored.Rounded.ViewQuilt
        label.contains("Vente", ignoreCase = true) -> Icons.Rounded.Star
        label.contains("Mur", ignoreCase = true) -> Icons.Rounded.Brush
        label.contains("Sol", ignoreCase = true) -> Icons.Rounded.Layers
        label.contains("Transfert", ignoreCase = true) -> Icons.Rounded.AutoAwesome
        label.contains("Circulation", ignoreCase = true) -> Icons.Rounded.Explore
        label.contains("Lumi", ignoreCase = true) -> Icons.Rounded.Star
        else -> Icons.Rounded.Home
    }
}

enum class MaterialPattern {
    Vein,
    Wood,
    Concrete,
    Limewash,
    Terrazzo,
    Tile,
    Paint,
}

data class MaterialSwatchSpec(
    val base: Color,
    val accent: Color,
    val pattern: MaterialPattern,
)

fun materialSwatchSpec(label: String): MaterialSwatchSpec = when (label) {
    "Marbre" -> MaterialSwatchSpec(Color(0xFFF7F4EE), Color(0xFF9A948B), MaterialPattern.Vein)
    "Chêne" -> MaterialSwatchSpec(Color(0xFFD7AD6F), Color(0xFF8E6335), MaterialPattern.Wood)
    "Noyer" -> MaterialSwatchSpec(Color(0xFF6E4529), Color(0xFF2D1B12), MaterialPattern.Wood)
    "Béton" -> MaterialSwatchSpec(Color(0xFFAAA79F), Color(0xFF6F716E), MaterialPattern.Concrete)
    "Limewash" -> MaterialSwatchSpec(Color(0xFFE7E0D4), Color(0xFFC8BFAF), MaterialPattern.Limewash)
    "Terrazzo" -> MaterialSwatchSpec(Color(0xFFECE3D3), Color(0xFF5C8374), MaterialPattern.Terrazzo)
    "Carrelage blanc" -> MaterialSwatchSpec(Color(0xFFF8F8F5), Color(0xFFC9C9C1), MaterialPattern.Tile)
    "Carrelage noir" -> MaterialSwatchSpec(Color(0xFF171717), Color(0xFF686868), MaterialPattern.Tile)
    "Peinture beige chaude" -> MaterialSwatchSpec(Color(0xFFE4D0B8), Color(0xFFC5A987), MaterialPattern.Paint)
    "Peinture sombre élégante" -> MaterialSwatchSpec(Color(0xFF232625), Color(0xFF606663), MaterialPattern.Paint)
    else -> MaterialSwatchSpec(Color(0xFFE4D8C9), Color(0xFF9A8B78), MaterialPattern.Paint)
}

@Composable
fun localizedAdvancedOption(label: String): String = when (label) {
    "agencement" -> stringResource(R.string.option_advanced_layout)
    "fenêtres" -> stringResource(R.string.option_advanced_windows)
    "sol" -> stringResource(R.string.option_advanced_floor)
    "mobilier principal" -> stringResource(R.string.option_advanced_main_furniture)
    "style" -> stringResource(R.string.option_advanced_style)
    "couleurs" -> stringResource(R.string.option_advanced_colors)
    "décor" -> stringResource(R.string.option_advanced_decor)
    "éclairage" -> stringResource(R.string.option_advanced_lighting)
    "structure" -> stringResource(R.string.option_advanced_structure)
    "toit" -> stringResource(R.string.option_advanced_roof)
    "entrée" -> stringResource(R.string.option_advanced_entry)
    "façade" -> stringResource(R.string.option_advanced_facade)
    "paysage" -> stringResource(R.string.option_advanced_landscape)
    "arbres" -> stringResource(R.string.option_advanced_trees)
    "piscine" -> stringResource(R.string.option_advanced_pool)
    "terrasse" -> stringResource(R.string.option_advanced_terrace)
    "clôture" -> stringResource(R.string.option_advanced_fence)
    "plantes" -> stringResource(R.string.option_advanced_plants)
    "mobilier" -> stringResource(R.string.option_advanced_furniture)
    "chemins" -> stringResource(R.string.option_advanced_paths)
    "murs" -> stringResource(R.string.option_advanced_walls)
    "portes" -> stringResource(R.string.option_advanced_doors)
    "mobilier important" -> stringResource(R.string.option_advanced_priority_furniture)
    "organisation" -> stringResource(R.string.option_advanced_organization)
    "circulation" -> stringResource(R.string.option_advanced_circulation)
    "rangement" -> stringResource(R.string.option_advanced_storage)
    "zones" -> stringResource(R.string.option_advanced_zones)
    "couleurs principales" -> stringResource(R.string.option_advanced_main_colors)
    "ambiance" -> stringResource(R.string.option_advanced_mood)
    "matériaux" -> stringResource(R.string.option_advanced_materials)
    "Low budget" -> stringResource(R.string.option_budget_low)
    "Medium budget" -> stringResource(R.string.option_budget_medium)
    "Luxury" -> stringResource(R.string.option_budget_luxury)
    "no dark colors" -> stringResource(R.string.option_avoid_dark_colors)
    "no structural changes" -> stringResource(R.string.option_avoid_structural_changes)
    "no plants" -> stringResource(R.string.option_avoid_plants)
    "keep windows" -> stringResource(R.string.option_avoid_keep_windows)
    "no furniture changes" -> stringResource(R.string.option_avoid_furniture_changes)
    else -> localizedOption(label)
}

fun processingHeroImage(toolId: String): Int = when (toolId) {
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
fun layoutChangeSummary(state: HomeDecorUiState): String {
    val goals = state.roomType.ifBlank { stringResource(R.string.layout_default_goals) }
    val keep = state.palette.ifBlank { stringResource(R.string.layout_default_keep) }
    val remove = state.mobilierASupprimer.ifBlank { stringResource(R.string.layout_default_remove) }
    val move = state.mobilierADeplacer.ifBlank { stringResource(R.string.layout_default_move) }
    val people = state.style.ifBlank { stringResource(R.string.layout_default_people) }
    val constraints = state.layoutConstraints.ifBlank { stringResource(R.string.layout_default_constraints) }
    return stringResource(R.string.layout_summary_format, goals, keep, remove, move, people, constraints)
}

@Composable
fun layoutSuggestions(state: HomeDecorUiState): List<String> {
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

data class DiscoverPreviewTarget(
    val item: GalleryItem,
    val section: DiscoverSection,
    val tool: DecorTool,
)

data class DiscoverSavedTarget(
    val item: GalleryItem,
    val section: DiscoverSection,
)

fun DiscoverSection.discoverPreviewTarget(item: GalleryItem): DiscoverPreviewTarget? {
    val belongsToSection = items.any { it.id == item.id }
    val tool = HomeDecorCatalog.tools.firstOrNull { it.id == serviceToolId }
    return if (belongsToSection && tool != null) DiscoverPreviewTarget(item, this, tool) else null
}

fun discoverTargetForSource(source: String): DiscoverSavedTarget? {
    val itemId = source.removePrefix("discover:")
    if (itemId == source) return null
    HomeDecorCatalog.discoverSections.forEach { section ->
        section.items.firstOrNull { it.id == itemId }?.let { item ->
            return DiscoverSavedTarget(item = item, section = section)
        }
    }
    return null
}

enum class ProfileWorkspaceTab { Favorites, Moodboard, History, Projects }

@Composable
fun profileWorkspaceTabLabel(tab: ProfileWorkspaceTab): String = stringResource(
    when (tab) {
        ProfileWorkspaceTab.Favorites -> R.string.profile_tab_favorites
        ProfileWorkspaceTab.Moodboard -> R.string.profile_tab_moodboard
        ProfileWorkspaceTab.History -> R.string.profile_tab_history
        ProfileWorkspaceTab.Projects -> R.string.profile_tab_projects
    },
)

fun profileWorkspaceTabIcon(tab: ProfileWorkspaceTab): ImageVector = when (tab) {
    ProfileWorkspaceTab.Favorites -> Icons.Rounded.Star
    ProfileWorkspaceTab.Moodboard -> Icons.AutoMirrored.Rounded.ViewQuilt
    ProfileWorkspaceTab.History -> Icons.Rounded.Visibility
    ProfileWorkspaceTab.Projects -> Icons.Rounded.Layers
}

@Composable
fun savedFavoriteTitle(item: FavoriteItem): String {
    val discoverTarget = discoverTargetForSource(item.sourceType)
    return discoverTarget?.let { target ->
        listOf(localizedDiscoverSection(target.section), localizedGalleryTitle(target.item))
            .filter { it.isNotBlank() }
            .joinToString(" - ")
    } ?: item.title
}

@Composable
fun savedFavoriteSubtitle(item: FavoriteItem): String {
    val discoverTarget = discoverTargetForSource(item.sourceType)
    return discoverTarget?.let { target ->
        stringResource(R.string.saved_discover_source, localizedDiscoverCluster(target.section.cluster))
    } ?: listOf(item.roomType, item.style)
        .filter { it.isNotBlank() }
        .joinToString(" - ")
        .ifBlank { stringResource(R.string.saved_generated_source) }
}

@Composable
fun savedMoodboardTitle(item: MoodboardItem): String {
    val discoverTarget = discoverTargetForSource(item.source)
    return discoverTarget?.let { target ->
        listOf(localizedDiscoverSection(target.section), localizedGalleryTitle(target.item))
            .filter { it.isNotBlank() }
            .joinToString(" - ")
    } ?: item.title
}

@Composable
fun savedMoodboardSubtitle(item: MoodboardItem): String {
    val discoverTarget = discoverTargetForSource(item.source)
    return when {
        discoverTarget != null -> stringResource(R.string.saved_discover_source, localizedDiscoverCluster(discoverTarget.section.cluster))
        item.notes.isNotBlank() -> item.notes
        item.source.startsWith("generated_result:") -> stringResource(R.string.saved_generated_source)
        else -> stringResource(R.string.saved_local_source)
    }
}

@Composable
fun sampleFavoriteCards(): List<SampleCollectionCard> = listOf(
    SampleCollectionCard(
        title = stringResource(R.string.sample_favorite_modern_title),
        subtitle = stringResource(R.string.sample_favorite_modern_subtitle),
        imageRes = R.drawable.assets_media_discover_generated_livingroom_livingroom4,
    ),
    SampleCollectionCard(
        title = stringResource(R.string.sample_favorite_garden_title),
        subtitle = stringResource(R.string.sample_favorite_garden_subtitle),
        imageRes = R.drawable.assets_media_discover_garden_gardenpatio,
    ),
    SampleCollectionCard(
        title = stringResource(R.string.sample_favorite_floor_title),
        subtitle = stringResource(R.string.sample_favorite_floor_subtitle),
        imageRes = R.drawable.assets_media_discover_floorscenes_naturaloakparquet,
    ),
)

@Composable
fun sampleMoodboardCards(): List<SampleCollectionCard> = listOf(
    SampleCollectionCard(
        title = stringResource(R.string.sample_moodboard_japandi_title),
        subtitle = stringResource(R.string.sample_moodboard_japandi_subtitle),
        imageRes = R.drawable.assets_media_styles_stylejapandi,
    ),
    SampleCollectionCard(
        title = stringResource(R.string.sample_moodboard_palette_title),
        subtitle = stringResource(R.string.sample_moodboard_palette_subtitle),
        imageRes = R.drawable.assets_media_discover_wallscenes_sagegreensuite,
    ),
    SampleCollectionCard(
        title = stringResource(R.string.sample_moodboard_exterior_title),
        subtitle = stringResource(R.string.sample_moodboard_exterior_subtitle),
        imageRes = R.drawable.assets_media_discover_exterior_exteriormodernvilla,
    ),
)

@Composable
fun sampleProjectCards(): List<SampleCollectionCard> = listOf(
    SampleCollectionCard(
        title = stringResource(R.string.sample_project_bedroom_title),
        subtitle = stringResource(R.string.sample_project_bedroom_subtitle),
        imageRes = R.drawable.assets_media_discover_generated_bedroom_bedroom4,
    ),
    SampleCollectionCard(
        title = stringResource(R.string.sample_project_kitchen_title),
        subtitle = stringResource(R.string.sample_project_kitchen_subtitle),
        imageRes = R.drawable.assets_media_discover_generated_kitchen_kitchen3,
    ),
    SampleCollectionCard(
        title = stringResource(R.string.sample_project_garden_title),
        subtitle = stringResource(R.string.sample_project_garden_subtitle),
        imageRes = R.drawable.assets_media_discover_generated_garden_garden5,
    ),
)

@Composable
fun sampleHistoryCards(): List<SampleCollectionCard> = listOf(
    SampleCollectionCard(
        title = stringResource(R.string.sample_history_before_after_title),
        subtitle = stringResource(R.string.sample_history_before_after_subtitle),
        imageRes = R.drawable.sample_after_luxury,
    ),
    SampleCollectionCard(
        title = stringResource(R.string.sample_history_layout_title),
        subtitle = stringResource(R.string.sample_history_layout_subtitle),
        imageRes = R.drawable.assets_media_discover_home_homehomeoffice,
    ),
    SampleCollectionCard(
        title = stringResource(R.string.sample_history_reference_title),
        subtitle = stringResource(R.string.sample_history_reference_subtitle),
        imageRes = R.drawable.assets_media_styles_stylemodern,
    ),
)

fun String.toComposeColor(): Color? {
    return runCatching {
        val clean = removePrefix("#")
        Color(clean.toLong(16) or if (clean.length <= 6) 0xFF000000 else 0x00000000)
    }.getOrNull()
}

fun formatProjectDate(createdAt: Long): String =
    java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(java.util.Date(createdAt))

enum class HistoryBucket { Today, Yesterday, ThisWeek, Older }

data class HistoryTimelineSection(
    val bucket: HistoryBucket,
    val items: List<GeneratedResult>,
)

fun groupHistoryResults(results: List<GeneratedResult>): List<HistoryTimelineSection> {
    if (results.isEmpty()) return emptyList()
    val zone = ZoneId.systemDefault()
    val today = LocalDate.now(zone)
    return results
        .groupBy { result ->
            val date = LocalDate.ofInstant(Instant.ofEpochMilli(result.createdAt), zone)
            when {
                date == today -> HistoryBucket.Today
                date == today.minusDays(1) -> HistoryBucket.Yesterday
                !date.isBefore(today.minusDays(6)) -> HistoryBucket.ThisWeek
                else -> HistoryBucket.Older
            }
        }
        .let { grouped ->
            listOf(HistoryBucket.Today, HistoryBucket.Yesterday, HistoryBucket.ThisWeek, HistoryBucket.Older)
                .mapNotNull { bucket ->
                    grouped[bucket]?.takeIf { it.isNotEmpty() }?.let { HistoryTimelineSection(bucket, it) }
                }
        }
}

@Composable
fun historyBucketLabel(bucket: HistoryBucket): String = stringResource(
    when (bucket) {
        HistoryBucket.Today -> R.string.history_today
        HistoryBucket.Yesterday -> R.string.history_yesterday
        HistoryBucket.ThisWeek -> R.string.history_this_week
        HistoryBucket.Older -> R.string.history_older
    }
)

fun formatHistoryItemDate(createdAt: Long): String {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(createdAt))
}

fun wizardStepNumber(stage: WizardStage, tool: DecorTool? = null): Int {
    return when (stage) {
        WizardStage.Photo -> 1
        WizardStage.Space -> 2
        WizardStage.Style -> if (tool?.id == "reference") 2 else 3
        WizardStage.Refine -> if (tool?.id in listOf("reference", "layout")) 2 else if (tool?.id in listOf("garden", "paint", "floor", "replace")) 3 else 4
        WizardStage.Processing -> wizardTotalSteps(tool)
        WizardStage.Result -> wizardTotalSteps(tool)
    }
}

fun wizardTotalSteps(tool: DecorTool?): Int {
    return when (tool?.id) {
        "garden", "paint", "floor", "replace" -> 3
        "reference" -> 2
        "layout" -> 2
        else -> 4
    }
}

data class ExamplePhoto(
    val label: String,
    @StringRes val labelRes: Int,
    val imageRes: Int,
)

fun examplesForTool(tool: DecorTool): List<ExamplePhoto> {
    return when (tool.id) {
        "facade" -> listOf(
            ExamplePhoto("facade-scaffold-house", R.string.example_facade_scaffold_house, R.drawable.assets_media_examples_exterior_exteriorbeforescaffoldhouse),
            ExamplePhoto("facade-weathered-house", R.string.example_facade_weathered_house, R.drawable.assets_media_examples_exterior_exteriorbeforeweatheredhouse),
            ExamplePhoto("facade-brick-shell", R.string.example_facade_brick_shell, R.drawable.assets_media_examples_exterior_exteriorbeforebrickshell),
            ExamplePhoto("facade-overgrown-cottage", R.string.example_facade_overgrown_cottage, R.drawable.assets_media_examples_exterior_exteriorbeforeovergrowncottage),
        )
        "garden" -> listOf(
            ExamplePhoto("garden-muddy-yard", R.string.example_garden_muddy_yard, R.drawable.assets_media_examples_garden_gardenbeforemuddyyard),
            ExamplePhoto("garden-weedy-yard", R.string.example_garden_weedy_yard, R.drawable.assets_media_examples_garden_gardenbeforeweedyyard),
            ExamplePhoto("garden-rubble-yard", R.string.example_garden_rubble_yard, R.drawable.assets_media_examples_garden_gardenbeforerubbleyard),
            ExamplePhoto("garden-overgrown-corner", R.string.example_garden_overgrown_corner, R.drawable.assets_media_examples_garden_gardenbeforeovergrowncorner),
        )
        "floor" -> listOf(
            ExamplePhoto("floor-cracked-concrete", R.string.example_floor_cracked_concrete, R.drawable.assets_media_examples_floor_floorbeforecrackedconcrete),
            ExamplePhoto("floor-damaged-planks", R.string.example_floor_damaged_planks, R.drawable.assets_media_examples_floor_floorbeforedamagedplanks),
            ExamplePhoto("floor-broken-tile", R.string.example_floor_broken_tile, R.drawable.assets_media_examples_floor_floorbeforebrokentile),
            ExamplePhoto("floor-subfloor", R.string.example_floor_subfloor, R.drawable.assets_media_examples_floor_floorbeforerenovationsubfloor),
        )
        "paint" -> listOf(
            ExamplePhoto("paint-raw-concrete", R.string.example_paint_raw_concrete, R.drawable.assets_media_examples_wall_wallbeforerawconcrete),
            ExamplePhoto("paint-peeling-plaster", R.string.example_paint_peeling_plaster, R.drawable.assets_media_examples_wall_wallbeforepeelingplaster),
            ExamplePhoto("paint-worn-white", R.string.example_paint_worn_white, R.drawable.assets_media_examples_wall_wallbeforewornwhite),
            ExamplePhoto("paint-exposed-brick", R.string.example_paint_exposed_brick, R.drawable.assets_media_examples_wall_wallbeforeexposedbrick),
        )
        else -> listOf(
            ExamplePhoto("interior-empty-room", R.string.example_interior_empty_room, R.drawable.assets_media_examples_interior_interiorbeforeemptyroom),
            ExamplePhoto("interior-messy-lounge", R.string.example_interior_messy_lounge, R.drawable.assets_media_examples_interior_interiorbeforemessylounge),
            ExamplePhoto("interior-damaged-room", R.string.example_interior_damaged_room, R.drawable.assets_media_examples_interior_interiorbeforedamagedroom),
            ExamplePhoto("interior-outdated-kitchen", R.string.example_interior_outdated_kitchen, R.drawable.assets_media_examples_interior_interiorbeforeoutdatedkitchen),
        )
    }
}

fun selectedExampleImageRes(state: HomeDecorUiState): Int {
    val selectedLabel = state.selectedPhotos.firstOrNull()?.exampleLabel ?: state.selectedExampleLabel
    val selected = examplesForTool(state.selectedTool).firstOrNull { it.label == selectedLabel }
    return selected?.imageRes ?: examplesForTool(state.selectedTool).first().imageRes
}

fun selectedPhotoImageRes(state: HomeDecorUiState, slot: SelectedPhoto): Int {
    val selected = examplesForTool(state.selectedTool).firstOrNull { it.label == slot.exampleLabel }
    return selected?.imageRes ?: selectedExampleImageRes(state)
}

fun choiceImageRes(label: String): Int {
    return when (label) {
        "Luxe" -> R.drawable.assets_media_styles_styleluxury
        "Japandi" -> R.drawable.assets_media_styles_stylejapandi
        "Cyberpunk" -> R.drawable.assets_media_styles_stylecyberpunk
        "Tropicale" -> R.drawable.assets_media_styles_styletropical
        "Minimaliste" -> R.drawable.assets_media_styles_styleminimalist
        "Marocain" -> R.drawable.assets_media_styles_stylemediterranean
        "Scandinave" -> R.drawable.assets_media_styles_stylescandinavian
        "Bohème" -> R.drawable.assets_media_styles_stylebohemian
        "Midcentury" -> R.drawable.assets_media_styles_stylemidcentury
        "Art Deco" -> R.drawable.assets_media_styles_styleartdeco
        "Côtier" -> R.drawable.assets_media_styles_stylecoastal
        "Rustique" -> R.drawable.assets_media_styles_stylerustic
        "Vintage" -> R.drawable.assets_media_styles_stylevintage
        "Méditerranéen" -> R.drawable.assets_media_styles_stylemediterranean
        "Glam" -> R.drawable.assets_media_styles_styleglam
        "Campagne française" -> R.drawable.assets_media_styles_stylefrenchcountry
        "Appartement" -> R.drawable.assets_media_discover_exterior_exteriorapartmentblock
        "Maison" -> R.drawable.assets_media_discover_exterior_exteriormodernvilla
        "Immeuble de bureaux" -> R.drawable.assets_media_discover_exterior_exteriorglassoffice
        "Résidentiel" -> R.drawable.assets_media_discover_exterior_exteriorpoolhouse
        "Vente au détail" -> R.drawable.assets_media_discover_exterior_exteriorretailstorefront
        "Villa" -> R.drawable.assets_media_discover_exterior_exteriormodernvilla
        "Marbre de Carrare" -> R.drawable.assets_media_discover_floorscenes_polishedcarraramarble
        "Chêne naturel" -> R.drawable.assets_media_discover_floorscenes_naturaloakparquet
        "Bois de noyer" -> R.drawable.assets_media_discover_floorscenes_heritagewalnutplank
        "Tuile calcaire" -> R.drawable.assets_media_discover_floorscenes_modernslatetile
        "Béton poli" -> R.drawable.assets_media_discover_floorscenes_industrialgrayconcrete
        "Bois chevron" -> R.drawable.assets_media_discover_floorscenes_walnutchevron
        "Terrazzo" -> R.drawable.assets_media_discover_floorscenes_terracottaateliertile
        else -> R.drawable.assets_media_styles_stylemodern
    }
}

fun paletteColors(label: String): List<Color> {
    return when (label) {
        "Gris millénaire" -> listOf(Color(0xFFF2F1EE), Color(0xFFC7C5C2), Color(0xFF908D89), Color(0xFF5A5652))
        "Mirage en terre cuite" -> listOf(Color(0xFFFFE9D0), Color(0xFFFFC17A), Color(0xFFFF9B45), Color(0xFFF05A0A))
        "Teintes forestières" -> listOf(Color(0xFFE7F7C8), Color(0xFFB8CA9A), Color(0xFF759269), Color(0xFF315A3B))
        "Verger de pêchers" -> listOf(Color(0xFFFFF1E8), Color(0xFFFAD3C2), Color(0xFFF5AE95), Color(0xFFE98570))
        "Fleur fuchsia" -> listOf(Color(0xFFFFE8F4), Color(0xFFF6B6DB), Color(0xFFE66CB2), Color(0xFFD61D78))
        "Gemme d'émeraude" -> listOf(Color(0xFFDFF4E7), Color(0xFF9FD2B4), Color(0xFF5C9B78), Color(0xFF155C42))
        "Brise pastel" -> listOf(Color(0xFFE5F2FF), Color(0xFFFFF8D5), Color(0xFFE8F1EA), Color(0xFFD9C7FA))
        "Brume océanique" -> listOf(Color(0xFF24415F), Color(0xFF629EC1), Color(0xFFC8DFE9), Color(0xFFF2F2EC))
        "Crépuscule de velours" -> listOf(Color(0xFF765365), Color(0xFFA38393), Color(0xFFD5C2C9), Color(0xFFEFE4E0))
        "Rêve d'améthyste" -> listOf(Color(0xFFE9D9FF), Color(0xFFC69BFA), Color(0xFF9A5BEA), Color(0xFF7527C8))
        "Fuchsia Noir" -> listOf(Color(0xFF160D2D), Color(0xFF7C3CE0), Color(0xFFE342D7), Color(0xFFFFA9C5))
        else -> listOf(Color(0xFFFFE7B8), Color(0xFFFF5D7A), Color(0xFF617EFF), Color(0xFF1A2348))
    }
}

data class SampleCollectionCard(
    val title: String,
    val subtitle: String,
    val imageRes: Int,
)
