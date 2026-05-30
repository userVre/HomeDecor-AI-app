package com.ismail.homedecorai.ui

import android.Manifest
import android.content.Intent
import android.content.ContentValues
import android.app.Activity
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.ViewQuilt
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.content.FileProvider
import androidx.core.content.ContextCompat
import com.ismail.homedecorai.BoardItem
import com.ismail.homedecorai.AppLocale
import com.ismail.homedecorai.BuildConfig
import com.ismail.homedecorai.DecorTool
import com.ismail.homedecorai.DiamondPack
import com.ismail.homedecorai.DiscoverSection
import com.ismail.homedecorai.ElitePassSyncState
import com.ismail.homedecorai.GalleryItem
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.MainTab
import com.ismail.homedecorai.MaskPoint
import com.ismail.homedecorai.MaskStroke
import com.ismail.homedecorai.R
import com.ismail.homedecorai.SelectedPhoto
import com.ismail.homedecorai.WizardStage
import com.ismail.homedecorai.hasVisibleMaskPaint
import com.ismail.homedecorai.isGeneratedResult
import com.ismail.homedecorai.purchaseAttemptMessageRes
import com.ismail.homedecorai.purchaseSyncMessageRes
import com.ismail.homedecorai.rawServiceMessageToKind
import com.ismail.homedecorai.storeMessageRes
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Offering
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PackageType
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

private val StudioInk = Color(0xFF171A18)
private val StudioBlue = Color(0xFF2F5EBA)
private val StudioGreen = StudioBlue
private val StudioMoss = Color(0xFF60645F)
private val StudioRose = Color(0xFFB3261E)
private val StudioCanvas = Color(0xFFF5F4F0)
private val StudioPaper = Color(0xFFFFFFFF)
private val StudioMist = Color(0xFFE9EBE4)
private val StudioLine = Color(0xFFD6D9D1)
private val StudioBlack = Color(0xFF121411)
private val StudioGold = Color(0xFFB78632)
private val StudioSky = StudioBlue
private val StudioViolet = StudioBlue
private val StudioPrimaryContainer = Color(0xFFE8EEF9)
private val StudioProContainer = Color(0xFFFFF3D9)
private val StudioErrorContainer = Color(0xFFFFEDEA)

private fun Modifier.minimumTouchTarget(): Modifier = sizeIn(minWidth = 48.dp, minHeight = 48.dp)

private fun Modifier.disabledSemantics(enabled: Boolean): Modifier =
    if (enabled) this else semantics { disabled() }

private fun studioStateContainer(selected: Boolean): Color = if (selected) StudioPrimaryContainer else StudioPaper
private fun studioStateBorder(selected: Boolean): Color = if (selected) StudioBlue else StudioLine
private fun studioStateElevation(selected: Boolean) = if (selected) 4.dp else 1.dp
private fun studioStateIconContainer(selected: Boolean): Color = if (selected) StudioBlue else StudioMist
@Composable
private fun studioStateIconContent(selected: Boolean): Color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

private data class StepCopy(
    @StringRes val titleRes: Int,
    @StringRes val bodyRes: Int,
    val options: List<String> = emptyList(),
)

@StringRes
private fun tabLabelRes(tab: MainTab): Int = when (tab) {
    MainTab.Tools -> R.string.nav_tools
    MainTab.ElitePass -> R.string.nav_elite_pass
    MainTab.Discover -> R.string.nav_discover
    MainTab.Profile -> R.string.nav_profile
    MainTab.Create -> R.string.workflow_interior
}

@StringRes
private fun toolTitleRes(tool: DecorTool): Int = when (tool.id) {
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
private fun toolDescriptionRes(tool: DecorTool): Int = when (tool.id) {
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
private fun workflowTitleRes(tool: DecorTool): Int = when (tool.id) {
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
private fun localizedToolTitle(tool: DecorTool): String = stringResource(toolTitleRes(tool))

@Composable
private fun localizedToolDescription(tool: DecorTool): String = stringResource(toolDescriptionRes(tool))

@Composable
private fun localizedWorkflowTitle(tool: DecorTool): String = stringResource(workflowTitleRes(tool))

@StringRes
private fun optionLabelRes(label: String): Int? = when (label) {
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
private fun localizedOption(label: String): String = optionLabelRes(label)?.let { stringResource(it) } ?: label

@StringRes
private fun discoverClusterRes(cluster: String): Int = when (cluster) {
    "Architecture" -> R.string.discover_cluster_architecture
    "Paysages" -> R.string.discover_cluster_landscapes
    else -> R.string.discover_cluster_interiors
}

@StringRes
private fun discoverSectionTitleRes(section: DiscoverSection): Int = when (section.id) {
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
    "apartment" -> R.string.discover_section_apartment
    "residential" -> R.string.discover_section_residential
    "wall-scenes" -> R.string.discover_section_wall_scenes
    "floors" -> R.string.discover_section_floors
    "garden" -> R.string.discover_section_garden
    "outdoor-spaces" -> R.string.discover_section_outdoor_spaces
    else -> R.string.nav_discover
}

@StringRes
private fun galleryCategoryRes(category: String): Int = when (category) {
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
    "Appartement" -> R.string.category_apartment
    "Résidentiel" -> R.string.category_residential
    "Mur" -> R.string.category_wall
    "Sol" -> R.string.category_floor
    "Jardin" -> R.string.category_garden
    "Paysage" -> R.string.category_landscape
    else -> R.string.nav_discover
}

@StringRes
private fun galleryItemTitleRes(item: GalleryItem): Int? = when (item.id) {
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

private fun galleryItemNumber(item: GalleryItem): Int? = item.id.substringAfterLast('-').toIntOrNull()?.takeIf {
    item.id.startsWith("kitchen-") ||
        item.id.startsWith("living-") ||
        item.id.startsWith("bedroom-") ||
        item.id.startsWith("villa-") ||
        item.id.startsWith("apartment-") ||
        item.id.startsWith("garden-")
}

@Composable
private fun localizedDiscoverCluster(cluster: String): String = stringResource(discoverClusterRes(cluster))

@Composable
private fun localizedDiscoverSection(section: DiscoverSection): String = stringResource(discoverSectionTitleRes(section))

@Composable
private fun localizedGalleryCategory(category: String): String = stringResource(galleryCategoryRes(category))

@Composable
private fun localizedGalleryTitle(item: GalleryItem): String {
    galleryItemTitleRes(item)?.let { return stringResource(it) }
    galleryItemNumber(item)?.let { return stringResource(R.string.gallery_numbered_title, localizedGalleryCategory(item.category), it) }
    return localizedGalleryCategory(item.category)
}

@StringRes
private fun diamondPackTitleRes(pack: DiamondPack): Int = when (pack.id) {
    "starter" -> R.string.pack_starter
    "designer" -> R.string.pack_designer
    "architect" -> R.string.pack_architect
    "estate" -> R.string.pack_estate
    else -> R.string.pack_estate
}

@StringRes
private fun diamondPackBadgeRes(pack: DiamondPack): Int? = when (pack.id) {
    "designer" -> R.string.pack_badge_popular
    "estate" -> R.string.pack_badge_best_offer
    else -> null
}

@StringRes
private fun diamondPackDescriptionRes(pack: DiamondPack): Int = when (pack.id) {
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

private fun Package.matchesDiamondPack(pack: DiamondPack): Boolean {
    val productId = product.id.trim().lowercase()
    val packageId = identifier.trim().lowercase()
    return productId in DiamondRevenueCatProductIds.orEmpty(pack.id) ||
        packageId in DiamondRevenueCatPackageIds.orEmpty(pack.id)
}

private fun Map<String, Set<String>>.orEmpty(key: String): Set<String> = get(key).orEmpty()

@StringRes
private fun designModeDescriptionRes(description: String): Int = when (description) {
    "Gardez les murs, ouvertures et volumes en place tout en améliorant le style." -> R.string.option_design_preserve_description
    "Autorisez l'IA à proposer une transformation plus ambitieuse et créative." -> R.string.option_design_free_description
    else -> R.string.option_design_preserve_description
}

@StringRes
private fun boardToolTitleRes(title: String): Int? = when (title) {
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeDecorApp(
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MaterialExpressiveTheme(
        colorScheme = expressiveLightColorScheme().copy(
            primary = StudioBlue,
            onPrimary = Color.White,
            primaryContainer = StudioPrimaryContainer,
            onPrimaryContainer = StudioInk,
            secondary = StudioMoss,
            tertiary = StudioGold,
            onTertiary = StudioInk,
            tertiaryContainer = StudioProContainer,
            onTertiaryContainer = StudioInk,
            error = StudioRose,
            errorContainer = StudioErrorContainer,
            surface = StudioCanvas,
            surfaceContainer = StudioPaper,
            surfaceContainerHigh = StudioMist,
            background = StudioCanvas,
            onSurface = StudioInk,
            onSurfaceVariant = Color(0xFF62665F),
            outlineVariant = StudioLine,
        ),
    ) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AppScaffold(
                state = state,
                viewModel = viewModel,
                currentLanguageTag = currentLanguageTag,
                onLanguageSelected = onLanguageSelected,
            )
        }
    }
}

@Composable
private fun studioPrimaryButtonColors() = ButtonDefaults.buttonColors(
    containerColor = StudioBlue,
    contentColor = Color.White,
    disabledContainerColor = StudioMist,
    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
)

@Composable
private fun studioProButtonColors() = ButtonDefaults.buttonColors(
    containerColor = StudioGold,
    contentColor = StudioInk,
    disabledContainerColor = Color.White.copy(alpha = 0.14f),
    disabledContentColor = Color.White.copy(alpha = 0.62f),
)

@Composable
private fun AppScaffold(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val modalVisible = state.storeVisible ||
        state.paywallVisible ||
        state.authVisible ||
        state.settingsVisible ||
        !state.disclosureAccepted
    BackHandler(enabled = state.storeVisible) {
        viewModel.closeDiamondStore()
    }
    BackHandler(enabled = state.paywallVisible) {
        viewModel.closePaywall()
    }
    BackHandler(enabled = state.authVisible) {
        viewModel.closeAuth()
    }
    BackHandler(enabled = state.settingsVisible) {
        viewModel.closeSettings()
    }
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (state.selectedTab != MainTab.Create && !modalVisible) {
            NavigationBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                tonalElevation = 0.dp,
                containerColor = StudioPaper,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NavItem(MainTab.Tools, state.selectedTab, Icons.Rounded.Home, stringResource(tabLabelRes(MainTab.Tools)), viewModel::selectTab)
                    NavItem(MainTab.ElitePass, state.selectedTab, Icons.Rounded.Star, stringResource(tabLabelRes(MainTab.ElitePass)), viewModel::selectTab)
                    NavItem(MainTab.Discover, state.selectedTab, Icons.Rounded.Explore, stringResource(tabLabelRes(MainTab.Discover)), viewModel::selectTab)
                    NavItem(MainTab.Profile, state.selectedTab, Icons.Rounded.Person, stringResource(tabLabelRes(MainTab.Profile)), viewModel::selectTab)
                }
            }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            AnimatedContent(targetState = state.selectedTab, label = "tab") { tab ->
                when (tab) {
                    MainTab.Tools -> ToolsScreen(state = state, viewModel = viewModel)
                    MainTab.Create -> CreateScreen(state = state, viewModel = viewModel)
                    MainTab.Discover -> DiscoverScreen(onTool = viewModel::startTool)
                    MainTab.ElitePass -> ElitePassScreen(state = state, viewModel = viewModel)
                    MainTab.Profile -> ProfileScreen(state = state, viewModel = viewModel)
                }
            }

            when {
                state.storeVisible -> {
                    DiamondStoreSheet(
                        state = state,
                        onClose = viewModel::closeDiamondStore,
                        onFulfill = viewModel::fulfillDiamondPurchase,
                        onRetrySync = viewModel::retryPurchaseSync,
                    )
                }
                state.paywallVisible -> {
                    PaywallSheet(
                        state = state,
                        onClose = viewModel::closePaywall,
                        onSubscription = viewModel::syncSubscriptionFromRevenueCat,
                        onRetrySync = viewModel::retryPurchaseSync,
                        onStore = viewModel::openDiamondStore,
                    )
                }
                state.authVisible -> {
                    AuthSheet(
                        onClose = viewModel::closeAuth,
                        onAuth = {
                            openAuth(context)
                            viewModel.closeAuth()
                        },
                    )
                }
                state.settingsVisible -> {
                    SettingsSheet(
                        state = state,
                        onClose = viewModel::closeSettings,
                        onStore = viewModel::openDiamondStore,
                        onSubscription = viewModel::syncSubscriptionFromRevenueCat,
                        onRetrySync = viewModel::retryPurchaseSync,
                        onFeedback = viewModel::submitSettingsFeedback,
                        onDeleteAccount = viewModel::deleteAccountData,
                        currentLanguageTag = currentLanguageTag,
                        onLanguageSelected = onLanguageSelected,
                    )
                }
            }

            if (!state.disclosureAccepted) {
                FirstLaunchDisclosure(onAccept = viewModel::acceptDisclosure)
            }
        }
    }
}

@Composable
private fun NavItem(
    tab: MainTab,
    selectedTab: MainTab,
    icon: ImageVector,
    label: String,
    onSelect: (MainTab) -> Unit,
) {
    val selected = selectedTab == tab
    Column(
        modifier = Modifier
            .width(70.dp)
            .minimumTouchTarget()
            .clip(RoundedCornerShape(20.dp))
            .semantics { this.selected = selected }
            .clickable(role = Role.Tab) { onSelect(tab) }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(30.dp)
                .clip(CircleShape)
                .background(if (selected) StudioPrimaryContainer else Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (selected) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
        )
    }
}

@Composable
private fun ScreenColumn(
    title: String,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            trailing?.invoke()
        }
        content()
    }
}

@Composable
private fun ToolsScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        ToolsHeader(
            state = state,
            onCredits = viewModel::openDiamondStore,
            onPass = viewModel::openPaywall,
        )
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(HomeDecorCatalog.tools, key = { it.id }) { tool ->
                ToolCard(tool = tool, onClick = { viewModel.startTool(tool) })
            }
        }
    }
}

@Composable
private fun ToolsHeader(
    state: HomeDecorUiState,
    onCredits: () -> Unit,
    onPass: () -> Unit,
) {
    Surface(color = StudioCanvas, tonalElevation = 0.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CreditPill(state, compact = true, onClick = onCredits)
            Text(stringResource(R.string.nav_tools), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Surface(
                onClick = onPass,
                shape = CircleShape,
                color = if (state.isPro) StudioProContainer else StudioPrimaryContainer,
                tonalElevation = 1.dp,
                modifier = Modifier.minimumTouchTarget(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(if (state.isPro) stringResource(R.string.pro) else stringResource(R.string.upgrade_to_pro), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CreditPill(state: HomeDecorUiState) {
    CreditPill(state = state, compact = false)
}

@Composable
private fun CreditPill(
    state: HomeDecorUiState,
    compact: Boolean,
    onClick: (() -> Unit)? = null,
) {
    val balanceLabel = if (state.isPro) stringResource(R.string.pro_upper) else stringResource(R.string.diamonds_count, state.diamonds)
    val pillDescription = if (onClick != null) {
        stringResource(R.string.a11y_open_diamond_store_balance, balanceLabel)
    } else {
        balanceLabel
    }
    Surface(
        onClick = { onClick?.invoke() },
        shape = CircleShape,
        color = if (state.isPro) StudioProContainer else StudioPaper,
        tonalElevation = 2.dp,
        modifier = Modifier
            .minimumTouchTarget()
            .border(1.dp, StudioLine, CircleShape)
            .semantics { contentDescription = pillDescription },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (compact) 10.dp else 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            Icon(Icons.Rounded.Diamond, null, Modifier.size(17.dp), tint = if (state.isPro) StudioGold else StudioBlue)
            Text(if (state.isPro) stringResource(R.string.pro_upper) else "${state.diamonds}", fontWeight = FontWeight.Bold)
        }
    }
}

private fun toolIcon(tool: DecorTool): ImageVector {
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

@Composable
private fun ToolCard(
    tool: DecorTool,
    onClick: () -> Unit,
) {
    val title = localizedToolTitle(tool)
    val description = localizedToolDescription(tool)
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
    ) {
        Box(Modifier.fillMaxWidth().height(394.dp)) {
            Image(
                painter = painterResource(tool.imageRes),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.04f),
                                Color.Black.copy(alpha = 0.08f),
                                Color.Black.copy(alpha = 0.72f),
                            ),
                        ),
                    ),
            )
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.92f),
                tonalElevation = 4.dp,
                modifier = Modifier.align(Alignment.TopStart).padding(18.dp),
            ) {
                Icon(toolIcon(tool), contentDescription = null, tint = StudioBlue, modifier = Modifier.padding(12.dp).size(22.dp))
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    description,
                    color = Color.White.copy(alpha = 0.84f),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onClick,
                        shape = CircleShape,
                        colors = studioPrimaryButtonColors(),
                        modifier = Modifier.height(48.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp),
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(17.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.try_this), fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

private fun photoCopy(tool: DecorTool): StepCopy {
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

private fun stepTwoCopy(tool: DecorTool): StepCopy {
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

private fun stepThreeCopy(tool: DecorTool): StepCopy {
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

private fun stepFourCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "facade", "garden", "paint" -> StepCopy(R.string.step_color_harmony_title, R.string.step_color_harmony_body)
        "floor" -> StepCopy(R.string.step_describe_vision_title, R.string.step_describe_floor_body)
        "replace" -> StepCopy(R.string.step_review_edit_title, R.string.step_review_edit_body)
        "reference" -> StepCopy(R.string.step_review_transfer_title, R.string.step_review_transfer_body)
        "layout" -> StepCopy(R.string.step_design_mode_title, R.string.step_design_mode_body)
        else -> StepCopy(R.string.step_design_mode_title, R.string.step_design_mode_color_body)
    }
}

@Composable
private fun CreateScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        DesignStepHeader(
            state = state,
            onBack = viewModel::previousStage,
            onClose = { viewModel.selectTab(MainTab.Tools) },
            onCredits = viewModel::openDiamondStore,
        )
        AnimatedContent(targetState = state.wizardStage, label = "wizard", modifier = Modifier.weight(1f)) { stage ->
            when (stage) {
                WizardStage.Photo -> if (state.selectedTool.id == "reference") {
                    ReferenceImagesStep(state = state, viewModel = viewModel)
                } else {
                    PhotoStep(state = state, viewModel = viewModel)
                }
                WizardStage.Space -> when (state.selectedTool.id) {
                    "floor" -> FloorMaskStep(state = state, viewModel = viewModel)
                    "paint" -> WallSurfaceStep(state = state, viewModel = viewModel)
                    "replace" -> ObjectMaskStep(state = state, viewModel = viewModel)
                    "reference" -> ReferencePhotoStep(state = state, viewModel = viewModel)
                    "layout" -> LayoutPlanningStep(state = state, viewModel = viewModel)
                    else -> {
                        val isGarden = state.selectedTool.id == "garden"
                        ChoiceStep(
                            eyebrow = stringResource(R.string.step_count_format, 2, wizardTotalSteps(state.selectedTool)),
                            copy = stepTwoCopy(state.selectedTool),
                            selected = if (isGarden) state.selectedStyles else state.selectedRooms,
                            onSelect = if (isGarden) viewModel::setStyle else viewModel::setRoom,
                            onContinue = viewModel::nextStage,
                            visualStyleCards = isGarden,
                            visualBuildingCards = state.selectedTool.id == "facade",
                        )
                    }
                }
                WizardStage.Style -> when (state.selectedTool.id) {
                    "paint", "floor", "replace", "reference" -> SpecializedGenerateStep(state = state, viewModel = viewModel)
                    else -> ChoiceStep(
                        eyebrow = stringResource(R.string.step_count_format, 3, wizardTotalSteps(state.selectedTool)),
                        copy = stepThreeCopy(state.selectedTool),
                        selected = state.selectedStyles,
                        onSelect = viewModel::setStyle,
                        onContinue = viewModel::nextStage,
                        visualStyleCards = state.selectedTool.id in listOf("interior", "facade", "garden", "floor"),
                    )
                }
                WizardStage.Refine -> RefineStep(state = state, viewModel = viewModel)
                WizardStage.Processing -> ProcessingStep(state.progressMessage)
                WizardStage.Result -> ResultStep(state = state, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun DesignStepHeader(
    state: HomeDecorUiState,
    onBack: () -> Unit,
    onClose: () -> Unit,
    onCredits: () -> Unit,
) {
    val step = wizardStepNumber(state.wizardStage, state.selectedTool)
    val totalSteps = wizardTotalSteps(state.selectedTool)
    Surface(color = StudioPaper, tonalElevation = 2.dp) {
        Column(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (step > 1) {
                    IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                } else {
                    CreditPill(state, compact = false, onClick = onCredits)
                }
                Text(
                    localizedWorkflowTitle(state.selectedTool),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 12.dp).weight(1f),
                )
                IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close))
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.step_count_format, step, totalSteps),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    repeat(totalSteps) { index ->
                        val active = index < step
                        Box(
                            modifier = Modifier.weight(1f).height(5.dp).clip(CircleShape).background(if (active) StudioBlue else StudioLine)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepScaffold(
    eyebrow: String,
    title: String,
    body: String? = null,
    buttonLabel: String,
    buttonIcon: ImageVector = Icons.Rounded.AutoAwesome,
    buttonEnabled: Boolean = true,
    contentBottomPadding: Dp = 18.dp,
    protectBottomInsets: Boolean = false,
    buttonAllowsTwoLines: Boolean = false,
    onButton: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = contentBottomPadding),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                    if (body != null) {
                        Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            item { content() }
        }
        val bottomBarModifier = if (protectBottomInsets) {
            Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        } else {
            Modifier
        }
        Surface(color = StudioPaper, tonalElevation = 3.dp, modifier = bottomBarModifier) {
            val buttonModifier = if (buttonAllowsTwoLines) {
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp).heightIn(min = 58.dp)
            } else {
                Modifier.fillMaxWidth().padding(20.dp).height(58.dp)
            }
            Button(
                onClick = onButton,
                enabled = buttonEnabled,
                shape = CircleShape,
                colors = studioPrimaryButtonColors(),
                modifier = buttonModifier,
            ) {
                Text(
                    buttonLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = if (buttonAllowsTwoLines) 2 else 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

private data class ImageInputActions(
    val openGallery: () -> Unit,
    val openCamera: () -> Unit,
)

@Composable
private fun rememberImageInputActions(
    onImageSelected: (Uri) -> Unit,
): ImageInputActions {
    val context = LocalContext.current
    val currentOnImageSelected by rememberUpdatedState(onImageSelected)
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let(currentOnImageSelected)
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        val capturedUri = pendingCameraUri
        pendingCameraUri = null
        if (saved && capturedUri != null) {
            currentOnImageSelected(capturedUri)
        }
    }

    fun launchCameraCapture() {
        val uri = createCameraUri(context)
        pendingCameraUri = uri
        runCatching { cameraLauncher.launch(uri) }
            .onFailure { pendingCameraUri = null }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            launchCameraCapture()
        } else {
            pendingCameraUri = null
        }
    }

    return ImageInputActions(
        openGallery = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        openCamera = {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                launchCameraCapture()
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
    )
}

@Composable
private fun PhotoStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val isLayoutTool = state.selectedTool.id == "layout"
    val isSingleSourceFlow = state.selectedTool.id in setOf("interior", "facade", "garden", "layout")
    val imageInputActions = rememberImageInputActions { uri ->
        if (isSingleSourceFlow) viewModel.setPrimaryPhoto(uri) else viewModel.setPhoto(uri)
    }
    val copy = photoCopy(state.selectedTool)
    val copyTitle = stringResource(copy.titleRes)
    val copyBody = stringResource(copy.bodyRes)
    val hasMainPhoto = state.selectedPhotos.isNotEmpty()
    val canContinue = hasMainPhoto
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, 1, wizardTotalSteps(state.selectedTool)),
        title = if (!hasMainPhoto) copyTitle else stringResource(R.string.photo_added),
        body = if (!hasMainPhoto) copyBody else null,
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        buttonEnabled = canContinue,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (!hasMainPhoto) {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = StudioPaper,
                    modifier = Modifier.fillMaxWidth().height(292.dp).border(1.dp, StudioLine, RoundedCornerShape(26.dp)),
                ) {
                    Column(
                        Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(copyTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Spacer(Modifier.height(12.dp))
                        Text(copyBody, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(18.dp))
                        Button(
                            onClick = imageInputActions.openGallery,
                            shape = CircleShape,
                            colors = studioPrimaryButtonColors(),
                        ) {
                            Text(if (isLayoutTool) stringResource(R.string.import_photo) else stringResource(R.string.import_plus))
                        }
                    }
                }
            } else {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Box(Modifier.fillMaxWidth().aspectRatio(1.18f)) {
                        val firstPhoto = state.selectedPhotos.first()
                        UriOrResourceImage(
                            uri = firstPhoto.uri,
                            imageRes = selectedPhotoImageRes(state, firstPhoto),
                            contentDescription = stringResource(R.string.photo_added),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                if (!isSingleSourceFlow) {
                    SelectedPhotoStrip(
                        state = state,
                        onAdd = imageInputActions.openGallery,
                        onRemove = viewModel::removePhoto,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = imageInputActions.openGallery, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.gallery))
                }
                OutlinedButton(
                    onClick = imageInputActions.openCamera,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.camera))
                }
            }
            OutlinedButton(
                onClick = {
                    val example = examplesForTool(state.selectedTool).first().label
                    if (isSingleSourceFlow) viewModel.selectPrimaryExamplePhoto(example) else viewModel.selectExamplePhoto(example)
                },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.use_example))
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(stringResource(R.string.example_photos), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(examplesForTool(state.selectedTool), key = { "example-${it.label}" }) { photo ->
                        ExamplePhotoCard(
                            photo = photo,
                            selected = state.selectedPhotos.any { it.exampleLabel == photo.label },
                            onClick = {
                                if (isSingleSourceFlow) viewModel.selectPrimaryExamplePhoto(photo.label) else viewModel.selectExamplePhoto(photo.label)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReferenceImagesStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    val roomImageInputActions = rememberImageInputActions { uri ->
        viewModel.setPrimaryPhoto(uri)
    }
    val referenceImageInputActions = rememberImageInputActions { uri ->
        viewModel.setReferencePhoto(uri)
    }
    val roomPhoto = state.selectedPhotos.firstOrNull()
    val hasRoom = roomPhoto != null
    val hasReference = state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null
    val canContinue = hasRoom && hasReference
    val missingHint = when {
        !hasRoom && !hasReference -> stringResource(R.string.reference_missing_both_hint)
        !hasRoom -> stringResource(R.string.reference_missing_room_hint)
        !hasReference -> stringResource(R.string.reference_missing_reference_hint)
        else -> null
    }
    val selectedRoomExample = roomPhoto?.exampleLabel?.let { selectedLabel ->
        examplesForTool(state.selectedTool).firstOrNull { it.label == selectedLabel }
    }
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, 1, wizardTotalSteps(state.selectedTool)),
        title = stringResource(R.string.reference_two_images_title),
        body = stringResource(R.string.reference_two_images_body),
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        buttonEnabled = canContinue,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ReferenceDualImagePicker(
                title = stringResource(R.string.your_room),
                body = stringResource(R.string.your_room_body),
                missingHint = stringResource(R.string.reference_missing_room_hint),
                selected = hasRoom,
                selectedText = selectedRoomExample?.let { stringResource(it.labelRes) } ?: stringResource(R.string.photo_added),
                uri = roomPhoto?.uri,
                imageRes = roomPhoto?.let { selectedPhotoImageRes(state, it) } ?: examplesForTool(state.selectedTool).first().imageRes,
                contentDescription = stringResource(R.string.your_room),
                onGallery = roomImageInputActions.openGallery,
                onCamera = roomImageInputActions.openCamera,
                onExample = { viewModel.selectPrimaryExamplePhoto(examplesForTool(state.selectedTool).first().label) },
            )
            ReferenceDualImagePicker(
                title = stringResource(R.string.reference_image),
                body = stringResource(R.string.reference_image_body),
                missingHint = stringResource(R.string.reference_missing_reference_hint),
                selected = hasReference,
                selectedText = state.selectedReferenceExampleLabel ?: stringResource(R.string.reference_added),
                uri = state.selectedReferenceUri,
                imageRes = R.drawable.tool_reference,
                contentDescription = stringResource(R.string.reference_image),
                onGallery = referenceImageInputActions.openGallery,
                onCamera = referenceImageInputActions.openCamera,
                onExample = { viewModel.selectReferenceExample(context.getString(R.string.editorial_reference)) },
            )
            if (missingHint != null) {
                ReferenceContinueHint(missingHint)
            }
        }
    }
}

@Composable
private fun ReferenceDualImagePicker(
    title: String,
    body: String,
    missingHint: String,
    selected: Boolean,
    selectedText: String,
    uri: Uri?,
    imageRes: Int,
    contentDescription: String,
    onGallery: () -> Unit,
    onCamera: () -> Unit,
    onExample: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(26.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, studioStateBorder(selected), RoundedCornerShape(26.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = studioStateIconContainer(selected)) {
                    Icon(
                        if (selected) Icons.Rounded.Check else Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.padding(9.dp).size(19.dp),
                        tint = studioStateIconContent(selected),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(
                        if (selected) selectedText else body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(StudioMist),
            ) {
                if (selected) {
                    UriOrResourceImage(
                        uri = uri,
                        imageRes = imageRes,
                        contentDescription = contentDescription,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Column(
                        Modifier.fillMaxSize().padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Surface(shape = CircleShape, color = StudioPaper) {
                            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.padding(12.dp).size(24.dp), tint = StudioBlue)
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(stringResource(R.string.reference_empty_preview_title), fontWeight = FontWeight.Black)
                        Text(
                            missingHint,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onGallery, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.Add, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(7.dp))
                    Text(stringResource(R.string.gallery))
                }
                OutlinedButton(onClick = onCamera, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(7.dp))
                    Text(stringResource(R.string.camera))
                }
            }
            OutlinedButton(onClick = onExample, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.example))
            }
        }
    }
}

@Composable
private fun ReferenceContinueHint(message: String) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioErrorContainer,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, StudioRose.copy(alpha = 0.28f), RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(Icons.Rounded.Lock, contentDescription = null, modifier = Modifier.size(18.dp), tint = StudioRose)
            Text(message, color = StudioRose, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SelectedPhotoStrip(
    state: HomeDecorUiState,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
) {
    val removePhotoDescription = stringResource(R.string.remove_photo)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(state.selectedPhotos.size, key = { "selected-photo-$it" }) { index ->
            val slot = state.selectedPhotos[index]
            val removePhotoDescription = stringResource(R.string.remove_photo)
            Box(Modifier.width(72.dp).height(64.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = StudioPaper,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxSize().border(1.dp, if (index == 0) StudioBlue else StudioLine, RoundedCornerShape(16.dp)),
                ) {
                    UriOrResourceImage(
                        uri = slot.uri,
                        imageRes = selectedPhotoImageRes(state, slot),
                        contentDescription = stringResource(R.string.photo_number, index + 1),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .minimumTouchTarget()
                        .semantics {
                            contentDescription = removePhotoDescription
                            role = Role.Button
                        }
                        .clickable(role = Role.Button) { onRemove(index) },
                    contentAlignment = Alignment.TopEnd,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF4A4D57),
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.padding(7.dp), tint = Color.White)
                    }
                }
            }
        }
        if (state.selectedPhotos.size < 3) {
            item("add-photo") {
                Surface(
                    onClick = onAdd,
                    shape = RoundedCornerShape(16.dp),
                    color = StudioPaper,
                    tonalElevation = 1.dp,
                    modifier = Modifier.width(72.dp).height(64.dp).border(1.dp, StudioLine, RoundedCornerShape(16.dp)),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.add_photo), tint = StudioInk)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReferenceImagePicker(
    selectedUri: Uri?,
    selectedExample: String?,
    onImport: () -> Unit,
    onExample: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.padding(8.dp).size(18.dp), tint = StudioBlue)
            }
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.reference_picker_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text(stringResource(R.string.reference_picker_body), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = studioStateContainer(selectedUri != null || selectedExample != null),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().border(1.dp, studioStateBorder(selectedUri != null || selectedExample != null), RoundedCornerShape(22.dp)),
        ) {
            Row(
                Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(Modifier.size(78.dp).clip(RoundedCornerShape(18.dp))) {
                    UriOrResourceImage(
                        uri = selectedUri,
                        imageRes = R.drawable.tool_reference,
                        contentDescription = stringResource(R.string.style_reference),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (selectedUri != null || selectedExample != null) stringResource(R.string.reference_added) else stringResource(R.string.no_reference), fontWeight = FontWeight.Black)
                    Text(
                        if (selectedUri != null || selectedExample != null) stringResource(R.string.reference_picker_selected_body) else stringResource(R.string.reference_picker_empty_body),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Icon(Icons.Rounded.Check, null, tint = if (selectedUri != null || selectedExample != null) StudioBlue else StudioLine)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onImport, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.import_action))
            }
            OutlinedButton(onClick = onExample, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.example))
            }
        }
    }
}

@Composable
private fun UriOrResourceImage(
    uri: Uri?,
    imageRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    var bitmap by remember(uri) { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    val context = LocalContext.current
    LaunchedEffect(uri) {
        bitmap = null
        if (uri != null) {
            bitmap = withContext(Dispatchers.IO) {
                runCatching {
                    val sourceBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                    } else {
                        context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream)
                    }
                    sourceBitmap?.asImageBitmap()
                }.getOrNull()
            }
        }
    }
    if (bitmap != null) {
        Image(bitmap = bitmap!!, contentDescription = contentDescription, modifier = modifier, contentScale = ContentScale.Crop)
    } else {
        Image(painter = painterResource(imageRes), contentDescription = contentDescription, modifier = modifier, contentScale = ContentScale.Crop)
    }
}

private fun createCameraUri(context: android.content.Context): Uri {
    val imageDir = File(context.cacheDir, "camera").apply { mkdirs() }
    val imageFile = File(imageDir, "homedecor-${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
}

@Composable
private fun ExamplePhotoCard(
    photo: ExamplePhoto,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val label = stringResource(photo.labelRes)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = StudioPaper,
        tonalElevation = studioStateElevation(selected),
        modifier = Modifier.width(112.dp).height(104.dp).border(1.dp, studioStateBorder(selected), RoundedCornerShape(18.dp)),
    ) {
        Box {
            Image(
                painter = painterResource(photo.imageRes),
                contentDescription = label,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.58f)))))
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter).padding(7.dp),
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.45f),
            ) {
                Text(
                    label,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (selected) {
                Surface(modifier = Modifier.align(Alignment.TopEnd).padding(6.dp), shape = CircleShape, color = StudioBlue) {
                    Icon(Icons.Rounded.Check, null, Modifier.padding(5.dp).size(14.dp), tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ChoiceStep(
    eyebrow: String,
    copy: StepCopy,
    selected: List<String>,
    onSelect: (String) -> Unit,
    onContinue: () -> Unit,
    visualStyleCards: Boolean = false,
    visualBuildingCards: Boolean = false,
) {
    StepScaffold(
        eyebrow = eyebrow,
        title = stringResource(copy.titleRes),
        body = stringResource(copy.bodyRes),
        buttonLabel = stringResource(R.string.continue_action),
        buttonIcon = Icons.Rounded.Check,
        buttonEnabled = selected.isNotEmpty(),
        onButton = onContinue,
    ) {
        if (visualStyleCards || visualBuildingCards) {
            val gridRows = (copy.options.size + 2) / 3
            LazyVerticalGrid(
                columns = if (visualBuildingCards) GridCells.Fixed(2) else GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth().height(((if (visualBuildingCards) (copy.options.size + 1) / 2 else gridRows) * if (visualBuildingCards) 174 else 176).dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false,
            ) {
                items(copy.options, key = { it }) { option ->
                    StyleChoiceCard(
                        label = option,
                        selected = option in selected,
                        onClick = { onSelect(option) },
                        large = visualBuildingCards,
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                copy.options.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { option ->
                            ExpressiveChoiceChip(
                                label = option,
                                selected = option in selected,
                                onClick = { onSelect(option) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReferencePhotoStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    val referenceImageInputActions = rememberImageInputActions { uri ->
        viewModel.setReferencePhoto(uri)
    }
    val hasReference = state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, 2, wizardTotalSteps(state.selectedTool)),
        title = stringResource(R.string.step_add_reference_title),
        body = stringResource(R.string.step_add_reference_body),
        buttonLabel = stringResource(R.string.continue_action),
        buttonEnabled = hasReference,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            ReferenceImagePicker(
                selectedUri = state.selectedReferenceUri,
                selectedExample = state.selectedReferenceExampleLabel,
                onImport = referenceImageInputActions.openGallery,
                onExample = { viewModel.selectReferenceExample(context.getString(R.string.editorial_reference)) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = referenceImageInputActions.openGallery, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.Add, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.gallery))
                }
                OutlinedButton(
                    onClick = referenceImageInputActions.openCamera,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.camera))
                }
            }
            OutlinedButton(
                onClick = { viewModel.selectReferenceExample(context.getString(R.string.editorial_reference)) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.use_example))
            }
        }
    }
}

@Composable
private fun FloorMaskStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    MaskEditorStep(
        state = state,
        viewModel = viewModel,
        title = stringResource(R.string.mask_floor_title),
        body = stringResource(R.string.mask_floor_body),
        disabledLabel = stringResource(R.string.mask_floor_disabled),
        target = "floor",
        imageDescription = stringResource(R.string.mask_floor_description),
        emptyStateTitle = stringResource(R.string.mask_floor_empty_title),
        emptyStateBody = stringResource(R.string.mask_floor_empty_body),
        polishedControls = true,
        allowAutoDetect = false,
    )
}

@Composable
private fun WallSurfaceStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    MaskEditorStep(
        state = state,
        viewModel = viewModel,
        title = stringResource(R.string.mask_wall_title),
        body = stringResource(R.string.mask_wall_body),
        disabledLabel = stringResource(R.string.mask_wall_disabled),
        target = "wall",
        imageDescription = stringResource(R.string.mask_wall_description),
        emptyStateTitle = stringResource(R.string.mask_wall_empty_title),
        emptyStateBody = stringResource(R.string.mask_wall_empty_body),
        polishedControls = true,
        allowAutoDetect = false,
    )
}

@Composable
private fun ObjectMaskStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    MaskEditorStep(
        state = state,
        viewModel = viewModel,
        title = stringResource(R.string.mask_object_title),
        body = stringResource(R.string.mask_object_body),
        disabledLabel = stringResource(R.string.mask_object_disabled),
        target = "object",
        imageDescription = stringResource(R.string.mask_object_description),
        emptyStateTitle = stringResource(R.string.mask_object_empty_title),
        emptyStateBody = stringResource(R.string.mask_object_empty_body),
        polishedControls = true,
    )
}

@Composable
private fun MaskEditorStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    title: String,
    body: String? = null,
    disabledLabel: String,
    target: String,
    imageDescription: String,
    emptyStateTitle: String? = null,
    emptyStateBody: String? = null,
    polishedControls: Boolean = false,
    allowAutoDetect: Boolean = target != "object",
) {
    val requiresVisibleMask = target in setOf("floor", "wall", "object")
    val isSurfaceMask = target in setOf("floor", "wall")
    val hasMask = remember(state.maskStrokes, target) {
        if (requiresVisibleMask) {
            state.maskStrokes.hasVisibleMaskPaint()
        } else {
            state.maskStrokes.any { !it.erase && it.points.size > 1 }
        }
    }
    val surfaceLabel = if (target == "floor") {
        stringResource(R.string.mask_floor_marked)
    } else {
        stringResource(R.string.mask_wall_marked)
    }
    val surfaceGuidance = if (target == "floor") {
        stringResource(R.string.mask_required_floor)
    } else {
        stringResource(R.string.mask_required_wall)
    }
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, 2, wizardTotalSteps(state.selectedTool)),
        title = title,
        body = body,
        buttonLabel = if (hasMask) stringResource(R.string.continue_action) else disabledLabel,
        buttonEnabled = hasMask,
        contentBottomPadding = if (isSurfaceMask) 32.dp else 18.dp,
        protectBottomInsets = isSurfaceMask,
        buttonAllowsTwoLines = isSurfaceMask,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            MaskCanvas(
                state = state,
                imageDescription = imageDescription,
                emptyStateTitle = emptyStateTitle,
                emptyStateBody = emptyStateBody,
                hasVisibleMask = hasMask,
                readyLabel = if (isSurfaceMask) stringResource(R.string.mask_ready, surfaceLabel) else null,
                onStroke = viewModel::addMaskStroke,
            )
            if (polishedControls && isSurfaceMask) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SurfaceMaskStatus(
                        hasMask = hasMask,
                        readyText = stringResource(R.string.mask_ready, surfaceLabel),
                        requiredText = surfaceGuidance,
                    )
                    BoxWithConstraints(Modifier.fillMaxWidth()) {
                        val compactControls = maxWidth < 360.dp
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                ToolToggle(
                                    label = stringResource(R.string.mask_mark),
                                    contentDescription = stringResource(R.string.a11y_mask_brush_add),
                                    icon = Icons.Rounded.Brush,
                                    selected = !state.eraserSelected,
                                    modifier = Modifier.weight(1f),
                                ) { viewModel.setMaskEraser(false) }
                                ToolToggle(
                                    label = stringResource(R.string.mask_remove),
                                    contentDescription = stringResource(R.string.a11y_mask_eraser_remove),
                                    icon = Icons.Rounded.Delete,
                                    selected = state.eraserSelected,
                                    modifier = Modifier.weight(1f),
                                ) { viewModel.setMaskEraser(true) }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                MaskActionButton(
                                    label = stringResource(R.string.undo),
                                    contentDescription = stringResource(R.string.a11y_undo_mask_stroke),
                                    icon = Icons.AutoMirrored.Rounded.ArrowBack,
                                    enabled = state.maskStrokes.isNotEmpty(),
                                    onClick = viewModel::undoMaskStroke,
                                    modifier = Modifier.weight(1f),
                                    showLabel = !compactControls,
                                )
                                MaskActionButton(
                                    label = stringResource(R.string.redo),
                                    contentDescription = stringResource(R.string.a11y_redo_mask_stroke),
                                    icon = Icons.AutoMirrored.Rounded.ArrowForward,
                                    enabled = state.undoneMaskStrokes.isNotEmpty(),
                                    onClick = viewModel::redoMaskStroke,
                                    modifier = Modifier.weight(1f),
                                    showLabel = !compactControls,
                                )
                                MaskActionButton(
                                    label = stringResource(R.string.clear),
                                    contentDescription = stringResource(R.string.clear_full_mask),
                                    icon = Icons.Rounded.Close,
                                    enabled = state.maskStrokes.isNotEmpty(),
                                    onClick = viewModel::clearMask,
                                    modifier = Modifier.weight(1f),
                                    showLabel = !compactControls,
                                )
                            }
                        }
                    }
                }
            } else if (polishedControls) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ToolToggle(
                            label = stringResource(R.string.mask_brush),
                            contentDescription = stringResource(R.string.a11y_mask_brush_add),
                            icon = Icons.Rounded.Brush,
                            selected = !state.eraserSelected,
                            modifier = Modifier.weight(1f),
                        ) { viewModel.setMaskEraser(false) }
                        ToolToggle(
                            label = stringResource(R.string.mask_eraser),
                            contentDescription = stringResource(R.string.a11y_mask_eraser_remove),
                            icon = Icons.Rounded.Delete,
                            selected = state.eraserSelected,
                            modifier = Modifier.weight(1f),
                        ) { viewModel.setMaskEraser(true) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MaskActionButton(
                            label = stringResource(R.string.undo),
                            contentDescription = stringResource(R.string.a11y_undo_mask_stroke),
                            icon = Icons.AutoMirrored.Rounded.ArrowBack,
                            enabled = state.maskStrokes.isNotEmpty(),
                            onClick = viewModel::undoMaskStroke,
                            modifier = Modifier.weight(1f),
                        )
                        MaskActionButton(
                            label = stringResource(R.string.redo),
                            contentDescription = stringResource(R.string.a11y_redo_mask_stroke),
                            icon = Icons.AutoMirrored.Rounded.ArrowForward,
                            enabled = state.undoneMaskStrokes.isNotEmpty(),
                            onClick = viewModel::redoMaskStroke,
                            modifier = Modifier.weight(1f),
                        )
                        MaskActionButton(
                            label = stringResource(R.string.clear),
                            contentDescription = stringResource(R.string.clear_full_mask),
                            icon = Icons.Rounded.Close,
                            enabled = state.maskStrokes.isNotEmpty(),
                            onClick = viewModel::clearMask,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ToolToggle(stringResource(R.string.mask_brush), Icons.Rounded.Brush, !state.eraserSelected, contentDescription = stringResource(R.string.a11y_mask_brush_add)) { viewModel.setMaskEraser(false) }
                    ToolToggle(stringResource(R.string.mask_eraser), Icons.Rounded.Delete, state.eraserSelected, contentDescription = stringResource(R.string.a11y_mask_eraser_remove)) { viewModel.setMaskEraser(true) }
                    FilledIconButton(onClick = viewModel::undoMaskStroke, enabled = state.maskStrokes.isNotEmpty()) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.undo))
                    }
                    FilledIconButton(onClick = viewModel::redoMaskStroke, enabled = state.undoneMaskStrokes.isNotEmpty()) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = stringResource(R.string.redo))
                    }
                    FilledIconButton(onClick = viewModel::clearMask, enabled = state.maskStrokes.isNotEmpty()) {
                        Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.clear_mask))
                    }
                }
            }
            if (isSurfaceMask) {
                BrushSizeControl(
                    brushSize = state.brushSize,
                    onBrushSizeChange = viewModel::setBrushSize,
                    showRangeLabels = true,
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(stringResource(R.string.brush_size), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "${state.brushSize.toInt()} px",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Slider(value = state.brushSize, onValueChange = viewModel::setBrushSize, valueRange = 8f..72f)
                }
            }
            if (allowAutoDetect) {
                OutlinedButton(onClick = { viewModel.markMaskWithAutoDetect(target) }, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(if (target == "floor") stringResource(R.string.auto_detect_floor) else stringResource(R.string.auto_detect_wall))
                }
            }
        }
    }
}

@Composable
private fun SurfaceMaskStatus(
    hasMask: Boolean,
    readyText: String,
    requiredText: String,
) {
    val icon = if (hasMask) Icons.Rounded.Check else Icons.Rounded.Brush
    val color = if (hasMask) StudioPrimaryContainer else StudioMist.copy(alpha = 0.72f)
    val contentColor = if (hasMask) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color,
        modifier = Modifier.fillMaxWidth().border(1.dp, if (hasMask) StudioBlue.copy(alpha = 0.32f) else StudioLine, RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = contentColor)
            Text(
                if (hasMask) readyText else requiredText,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ToolToggle(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String = label,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) StudioBlue else StudioPaper,
        tonalElevation = studioStateElevation(selected),
        modifier = modifier
            .height(48.dp)
            .border(1.dp, studioStateBorder(selected), CircleShape)
            .semantics {
                this.contentDescription = contentDescription
                this.selected = selected
                role = Role.Button
            },
    ) {
        Row(Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = if (selected) Color.White else StudioInk)
            Text(label, color = if (selected) Color.White else StudioInk, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun MaskActionButton(
    label: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = label,
    showLabel: Boolean = true,
) {
    val contentColor = if (enabled) StudioInk else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.52f)
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(18.dp),
        color = if (enabled) StudioPaper else StudioMist.copy(alpha = 0.72f),
        tonalElevation = if (enabled) 1.dp else 0.dp,
        modifier = modifier
            .height(50.dp)
            .border(1.dp, if (enabled) StudioLine else StudioLine.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Button
            }
            .disabledSemantics(enabled),
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(17.dp), tint = contentColor)
            if (showLabel) {
                Spacer(Modifier.width(6.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun BrushSizeControl(
    brushSize: Float,
    onBrushSizeChange: (Float) -> Unit,
    showRangeLabels: Boolean,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(18.dp)),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.brush_size), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                    Text(
                        "${brushSize.toInt()} px",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = StudioBlue,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .size((10f + (brushSize / 72f) * 24f).dp)
                            .clip(CircleShape)
                            .background(StudioBlue.copy(alpha = 0.7f)),
                    )
                }
                Slider(
                    value = brushSize,
                    onValueChange = onBrushSizeChange,
                    valueRange = 8f..72f,
                    modifier = Modifier.weight(1f),
                )
            }
            if (showRangeLabels) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(stringResource(R.string.precise), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(stringResource(R.string.wide), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun MaskCanvas(
    state: HomeDecorUiState,
    imageDescription: String,
    emptyStateTitle: String? = null,
    emptyStateBody: String? = null,
    hasVisibleMask: Boolean? = null,
    readyLabel: String? = null,
    onStroke: (MaskStroke) -> Unit,
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var livePoints by remember { mutableStateOf<List<MaskPoint>>(emptyList()) }
    val hasMask = hasVisibleMask ?: state.maskStrokes.any { !it.erase && it.points.size > 1 }
    fun Offset.toMaskPoint(): MaskPoint {
        val width = canvasSize.width.coerceAtLeast(1).toFloat()
        val height = canvasSize.height.coerceAtLeast(1).toFloat()
        return MaskPoint((x / width).coerceIn(0f, 1f), (y / height).coerceIn(0f, 1f))
    }
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(22.dp))
            .background(StudioLine)
            .border(2.dp, if (hasMask) StudioBlue else StudioLine, RoundedCornerShape(22.dp))
            .onSizeChanged { canvasSize = it }
            .pointerInput(state.brushSize, state.eraserSelected) {
                detectDragGestures(
                    onDragStart = { offset -> livePoints = listOf(offset.toMaskPoint()) },
                    onDrag = { change, _ -> livePoints = livePoints + change.position.toMaskPoint() },
                    onDragEnd = {
                        onStroke(MaskStroke(livePoints, state.brushSize, state.eraserSelected))
                        livePoints = emptyList()
                    },
                    onDragCancel = { livePoints = emptyList() },
                )
            },
    ) {
        val firstPhoto = state.selectedPhotos.firstOrNull()
        UriOrResourceImage(
            uri = firstPhoto?.uri,
            imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
            contentDescription = imageDescription,
            modifier = Modifier.fillMaxSize(),
        )
        Canvas(Modifier.matchParentSize()) {
            fun drawStroke(stroke: MaskStroke) {
                val points = stroke.points
                if (points.size < 2) return
                points.zipWithNext().forEach { (start, end) ->
                    drawLine(
                        color = if (stroke.erase) Color.Transparent else StudioSky.copy(alpha = 0.62f),
                        start = Offset(start.x * size.width, start.y * size.height),
                        end = Offset(end.x * size.width, end.y * size.height),
                        strokeWidth = stroke.brushSize,
                        cap = StrokeCap.Round,
                        blendMode = if (stroke.erase) BlendMode.Clear else BlendMode.SrcOver,
                    )
                }
            }
            state.maskStrokes.forEach(::drawStroke)
            livePoints.takeIf { it.isNotEmpty() }?.let {
                drawStroke(MaskStroke(it, state.brushSize, state.eraserSelected))
            }
        }
        if (hasMask && readyLabel != null) {
            Surface(
                shape = CircleShape,
                color = StudioBlue,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Text(readyLabel, color = Color.White, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
        if (!hasMask && emptyStateTitle != null) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = StudioBlack.copy(alpha = 0.78f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(14.dp)
                    .fillMaxWidth(),
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(emptyStateTitle, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    if (!emptyStateBody.isNullOrBlank()) {
                        Text(emptyStateBody, color = Color.White.copy(alpha = 0.82f), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun SurfacePanel(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    primary: String,
    onPrimary: () -> Unit,
    onMagic: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier.height(146.dp).border(1.dp, studioStateBorder(selected), RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(shape = CircleShape, color = StudioPaper) {
                    Icon(icon, null, Modifier.padding(8.dp).size(18.dp), tint = StudioInk)
                }
                Text(title, fontWeight = FontWeight.Black, color = if (selected) StudioBlue else StudioInk)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onPrimary,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) StudioBlue else StudioPaper,
                        contentColor = if (selected) Color.White else StudioInk,
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier.height(48.dp).weight(1f),
                ) {
                    Text(primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = onMagic, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = stringResource(R.string.option_ai_suggestion), tint = StudioBlue)
                }
            }
        }
    }
}

@Composable
private fun StyleChoiceCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    large: Boolean = false,
) {
    val displayLabel = localizedOption(label)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = Modifier
            .fillMaxWidth()
            .height(if (large) 162.dp else 164.dp)
            .border(1.dp, studioStateBorder(selected), RoundedCornerShape(18.dp)),
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(if (large) 100.dp else 108.dp), contentAlignment = Alignment.Center) {
                if (label == "Suggestion IA") {
                Surface(shape = RoundedCornerShape(22.dp), color = StudioMist, tonalElevation = 1.dp) {
                        Icon(Icons.Rounded.AutoAwesome, null, Modifier.padding(18.dp).size(34.dp), tint = StudioBlue)
                    }
                } else {
                    Image(
                        painter = painterResource(choiceImageRes(label)),
                        contentDescription = displayLabel,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
                if (selected) {
                    Surface(modifier = Modifier.align(Alignment.TopEnd).padding(7.dp), shape = CircleShape, color = Color.White) {
                        Icon(Icons.Rounded.Check, null, Modifier.padding(5.dp).size(15.dp), tint = StudioBlue)
                    }
                }
            }
            Box(Modifier.fillMaxWidth().height(if (large) 62.dp else 56.dp), contentAlignment = Alignment.Center) {
                Text(
                    displayLabel,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ExpressiveChoiceChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier.height(78.dp).border(1.dp, studioStateBorder(selected), RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(studioStateIconContainer(selected)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (selected) Icons.Rounded.Check else choiceIcon(label),
                    null,
                    Modifier.size(if (selected) 17.dp else 19.dp),
                    tint = studioStateIconContent(selected),
                )
            }
            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                style = if (displayLabel.length > 12) {
                    MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp, lineHeight = 18.sp)
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ReplaceSuggestionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    val shape = RoundedCornerShape(18.dp)
    val containerColor = if (selected) StudioBlue else StudioPaper
    val contentColor = if (selected) Color.White else StudioInk
    Surface(
        onClick = onClick,
        shape = shape,
        color = containerColor,
        tonalElevation = if (selected) 6.dp else 1.dp,
        modifier = modifier
            .height(78.dp)
            .border(if (selected) 2.dp else 1.dp, if (selected) StudioBlue else StudioLine, shape),
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (selected) Color.White.copy(alpha = 0.2f) else StudioPrimaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (selected) Icons.Rounded.Check else choiceIcon(label),
                    contentDescription = null,
                    modifier = Modifier.size(if (selected) 18.dp else 19.dp),
                    tint = if (selected) Color.White else StudioBlue,
                )
            }
            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                color = contentColor,
                style = if (displayLabel.length > 12) {
                    MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp, lineHeight = 18.sp)
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun choiceIcon(label: String): ImageVector {
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

@Composable
private fun GenerationErrorNotice(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioErrorContainer,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(Icons.Rounded.Refresh, contentDescription = null, tint = StudioRose, modifier = Modifier.size(18.dp))
            Text(
                message,
                modifier = Modifier.weight(1f),
                color = StudioRose,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedButton(
                onClick = onRetry,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier.height(48.dp),
            ) {
                Text(stringResource(R.string.retry), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LayoutPlanningStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val firstPhoto = state.selectedPhotos.firstOrNull()
    val hasPlanningGoal = state.selectedRooms.isNotEmpty()
    val canGenerate = firstPhoto != null && hasPlanningGoal
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.layout_plan_title), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.layout_plan_body), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (!state.generationError.isNullOrBlank()) {
                item {
                    GenerationErrorNotice(
                        message = state.generationError,
                        onRetry = viewModel::generate,
                    )
                }
            }
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = StudioPaper,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(24.dp)),
                ) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(Modifier.size(86.dp).clip(RoundedCornerShape(18.dp))) {
                            UriOrResourceImage(
                                uri = firstPhoto?.uri,
                                imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
                                contentDescription = stringResource(R.string.room_photo),
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(stringResource(R.string.room_photo), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                            Text(stringResource(R.string.room_photo_layout_body), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioGreen, modifier = Modifier.size(22.dp))
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(stringResource(R.string.planning_goals), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    HomeDecorCatalog.layoutGoals.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { option ->
                                LayoutGoalChip(
                                    label = option,
                                    selected = option in state.selectedRooms,
                                    onClick = { viewModel.setRoom(option) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
            item {
                LayoutConstraintFields(state = state, viewModel = viewModel)
            }
        }
        Surface(
            color = StudioPaper,
            tonalElevation = 3.dp,
            modifier = Modifier.imePadding().windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            Button(
                onClick = viewModel::generate,
                enabled = canGenerate,
                shape = CircleShape,
                colors = studioPrimaryButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(58.dp),
            ) {
                Icon(Icons.AutoMirrored.Rounded.ViewQuilt, contentDescription = null, modifier = Modifier.size(19.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (hasPlanningGoal) stringResource(R.string.layout_generate) else stringResource(R.string.layout_select_goal_to_generate),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun LayoutGoalChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    val container = if (selected) StudioBlue else StudioPaper
    val content = if (selected) Color.White else StudioInk
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = container,
        tonalElevation = if (selected) 4.dp else 1.dp,
        modifier = modifier.height(82.dp).border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (selected) Color.White.copy(alpha = 0.18f) else StudioMist),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (selected) Icons.Rounded.Check else choiceIcon(label),
                    contentDescription = null,
                    modifier = Modifier.size(if (selected) 18.dp else 19.dp),
                    tint = if (selected) Color.White else StudioBlue,
                )
            }
            Text(
                displayLabel,
                modifier = Modifier.weight(1f),
                color = content,
                style = if (displayLabel.length > 12) {
                    MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp, lineHeight = 18.sp)
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun LayoutConstraintFields(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.constraints), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            OutlinedTextField(
                value = state.layoutConstraints,
                onValueChange = viewModel::setLayoutConstraints,
                placeholder = { Text(stringResource(R.string.constraints_placeholder)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 132.dp),
                minLines = 4,
                shape = RoundedCornerShape(18.dp),
            )
        }
        OutlinedTextField(
            value = state.palette,
            onValueChange = viewModel::setPaletteText,
            label = { Text(stringResource(R.string.furniture_to_keep)) },
            placeholder = { Text(stringResource(R.string.furniture_to_keep_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.mobilierADeplacer,
            onValueChange = viewModel::setMobilierADeplacerText,
            label = { Text(stringResource(R.string.furniture_to_move)) },
            placeholder = { Text(stringResource(R.string.furniture_to_move_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.style,
            onValueChange = viewModel::setStyleText,
            label = { Text(stringResource(R.string.people_count)) },
            placeholder = { Text(stringResource(R.string.people_count_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
            shape = RoundedCornerShape(18.dp),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.customPrompt,
            onValueChange = viewModel::setCustomPrompt,
            label = { Text(stringResource(R.string.custom_notes)) },
            placeholder = { Text(stringResource(R.string.custom_notes_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 144.dp),
            minLines = 4,
            shape = RoundedCornerShape(18.dp),
        )
    }
}

@Composable
private fun RefineStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    if (state.selectedTool.id == "layout") {
        StepScaffold(
            eyebrow = stringResource(R.string.step_count_format, 3, wizardTotalSteps(state.selectedTool)),
            title = stringResource(R.string.add_details_title),
            body = stringResource(R.string.add_details_body),
            buttonLabel = stringResource(R.string.generate),
            buttonEnabled = state.selectedRooms.isNotEmpty(),
            onButton = viewModel::generate,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(value = state.roomType, onValueChange = viewModel::setRoomTypeText, label = { Text(stringResource(R.string.room_type)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.style, onValueChange = viewModel::setStyleText, label = { Text(stringResource(R.string.people_count)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.palette, onValueChange = viewModel::setPaletteText, label = { Text(stringResource(R.string.furniture_to_keep)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.mobilierADeplacer, onValueChange = viewModel::setMobilierADeplacerText, label = { Text(stringResource(R.string.furniture_to_move)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(
                    value = state.customPrompt,
                    onValueChange = viewModel::setCustomPrompt,
                    label = { Text(stringResource(R.string.important_constraints)) },
                    placeholder = { Text(stringResource(R.string.important_constraints_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(18.dp),
                )
            }
        }
        return
    }
    val copy = stepFourCopy(state.selectedTool)
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, wizardStepNumber(state.wizardStage, state.selectedTool), wizardTotalSteps(state.selectedTool)),
        title = stringResource(copy.titleRes),
        body = stringResource(copy.bodyRes),
        buttonLabel = stringResource(R.string.generate_my_design),
        buttonIcon = Icons.Rounded.AutoAwesome,
        buttonEnabled = state.selectedPalettes.isNotEmpty(),
        onButton = viewModel::generate,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (!state.generationError.isNullOrBlank()) {
                GenerationErrorNotice(
                    message = state.generationError,
                    onRetry = viewModel::generate,
                )
            }
            if (state.selectedTool.id !in listOf("facade", "garden", "paint")) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.step_design_mode_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HomeDecorCatalog.designModes.forEach { (mode, description) ->
                            ModeCard(
                                title = localizedOption(mode),
                                description = stringResource(designModeDescriptionRes(description)),
                                selected = state.designMode == mode,
                                onClick = { viewModel.setDesignMode(mode) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(stringResource(R.string.step_color_harmony_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth().height(548.dp),
                    horizontalArrangement = Arrangement.spacedBy(34.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    userScrollEnabled = false,
                ) {
                    items(listOf("Suggestion IA") + HomeDecorCatalog.palettes, key = { it }) { palette ->
                        PaletteChoiceCard(
                            label = palette,
                            selected = palette in state.selectedPalettes,
                            onClick = { viewModel.setPalette(palette) },
                        )
                    }
                }
            }
            OutlinedTextField(
                value = state.customPrompt,
                onValueChange = viewModel::setCustomPrompt,
                label = { Text(stringResource(R.string.describe_vision_label)) },
                placeholder = { Text(stringResource(R.string.describe_vision_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(18.dp),
            )
            val briefSpace = if (state.selectedTool.id == "garden") {
                stringResource(R.string.workflow_garden)
            } else {
                state.roomType.takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.space_to_choose)
            }
            val briefStyle = state.style.takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.style_to_choose)
            Surface(shape = RoundedCornerShape(22.dp), color = StudioBlack) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.design_brief), color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        stringResource(
                            R.string.design_pair_format,
                            briefSpace,
                            briefStyle,
                        ),
                        color = Color.White.copy(alpha = 0.82f),
                    )
                    Text(localizedOption(state.designMode), color = Color.White.copy(alpha = 0.82f))
                    Text(state.palette.takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.palette_to_choose), color = Color.White.copy(alpha = 0.72f))
                }
            }
        }
    }
}

@Composable
private fun ColorSwatchCard(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier.height(72.dp).border(1.dp, studioStateBorder(selected), RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(1.dp, StudioLine, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (selected) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (color == Color(0xFFFAF9F6) || color == Color(0xFFE2E2E2)) Color.Black else Color.White,
                    )
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(displayLabel, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun SpecializedGenerateStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val stepCopy = stepThreeCopy(state.selectedTool)
    val stepTitle = stringResource(stepCopy.titleRes)
    val stepBody = stringResource(stepCopy.bodyRes)
    val selected = state.selectedStyles
    val requiresMask = state.selectedTool.id in setOf("paint", "floor", "replace")
    val replacementPrompt = state.customPrompt.trim()
    val hasReplacementPrompt = replacementPrompt.isNotBlank()
    val hasRequiredMask = remember(state.maskStrokes, state.selectedTool.id) {
        if (state.selectedTool.id in setOf("paint", "floor", "replace")) {
            state.maskStrokes.hasVisibleMaskPaint()
        } else {
            state.maskStrokes.any { !it.erase && it.points.size > 1 }
        }
    }
    val hasReferenceImages = state.selectedTool.id != "reference" ||
        (state.selectedPhotos.firstOrNull() != null &&
            (state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null))
    val isPaintOrFloor = state.selectedTool.id in setOf("paint", "floor")
    val canGenerate = when (state.selectedTool.id) {
        "replace" -> hasRequiredMask && hasReplacementPrompt
        "reference" -> selected.isNotEmpty() && hasReferenceImages
        else -> (selected.isNotEmpty() || state.customPrompt.isNotBlank()) && (!requiresMask || hasRequiredMask)
    }
    StepScaffold(
        eyebrow = stringResource(R.string.step_count_format, wizardStepNumber(state.wizardStage, state.selectedTool), wizardTotalSteps(state.selectedTool)),
        title = stepTitle,
        body = stepBody,
        buttonLabel = if (requiresMask && !hasRequiredMask) {
            when (state.selectedTool.id) {
                "floor" -> stringResource(R.string.paint_mask_before_generate_floor)
                "replace" -> stringResource(R.string.paint_mask_before_generate_replace)
                else -> stringResource(R.string.paint_mask_before_generate_wall)
            }
        } else if (state.selectedTool.id == "reference" && !hasReferenceImages) {
            stringResource(R.string.add_both_images)
        } else if (state.selectedTool.id == "replace" && !hasReplacementPrompt) {
            stringResource(R.string.choose_replacement_before_generate)
        } else {
            stringResource(R.string.generate)
        },
        buttonEnabled = canGenerate,
        contentBottomPadding = if (isPaintOrFloor) 32.dp else 18.dp,
        protectBottomInsets = isPaintOrFloor,
        buttonAllowsTwoLines = isPaintOrFloor,
        onButton = viewModel::generate,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (!state.generationError.isNullOrBlank()) {
                GenerationErrorNotice(
                    message = state.generationError,
                    onRetry = viewModel::generate,
                )
            }
            if (state.selectedTool.id in setOf("paint", "floor")) {
                val maskLabel = if (state.selectedTool.id == "floor") {
                    stringResource(R.string.mask_floor_marked)
                } else {
                    stringResource(R.string.mask_wall_marked)
                }
                SurfaceMaskStatus(
                    hasMask = hasRequiredMask,
                    readyText = stringResource(R.string.mask_ready, maskLabel),
                    requiredText = if (state.selectedTool.id == "floor") {
                        stringResource(R.string.mask_required_floor)
                    } else {
                        stringResource(R.string.mask_required_wall)
                    },
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (state.selectedTool.id == "paint") {
                    val swatchColors = mapOf(
                        "Blanc chaud" to Color(0xFFFAF9F6),
                        "Beige" to Color(0xFFE8D8C8),
                        "Gris clair" to Color(0xFFE2E2E2),
                        "Vert sauge" to Color(0xFF8FA382),
                        "Bleu doux" to Color(0xFF8CA1C4),
                        "Terracotta" to Color(0xFFD36135),
                        "Noir élégant" to Color(0xFF1F2421)
                    )
                    stepCopy.options.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { option ->
                                ColorSwatchCard(
                                    label = option,
                                    color = swatchColors[option] ?: Color.LightGray,
                                    selected = option in selected,
                                    onClick = { viewModel.setStyle(option) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                }
            } else {
                if (state.selectedTool.id == "replace") {
                    Text(stringResource(R.string.replacement_suggestions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                }
                    stepCopy.options.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { option ->
                                if (state.selectedTool.id == "replace") {
                                    ReplaceSuggestionChip(
                                        label = option,
                                        selected = option in selected || replacementPrompt == option,
                                        onClick = {
                                            viewModel.selectReplacementSuggestion(option)
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                } else {
                                    ExpressiveChoiceChip(
                                        label = option,
                                        selected = option in selected,
                                        onClick = {
                                            viewModel.setStyle(option)
                                        },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
            if (state.selectedTool.id == "reference") {
                Text(stringResource(R.string.transfer_options), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                HomeDecorCatalog.referenceOptions.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { option ->
                            ExpressiveChoiceChip(
                                label = option,
                                selected = option in state.selectedPalettes,
                                onClick = { viewModel.setPalette(option) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
            OutlinedTextField(
                value = state.customPrompt,
                onValueChange = viewModel::setCustomPrompt,
                label = if (state.selectedTool.id == "replace") {
                    { Text(stringResource(R.string.replacement_object)) }
                } else {
                    null
                },
                placeholder = {
                    Text(
                        when (state.selectedTool.id) {
                            "paint" -> stringResource(R.string.prompt_paint)
                            "floor" -> stringResource(R.string.prompt_floor)
                            "replace" -> stringResource(R.string.prompt_replace)
                            else -> stringResource(R.string.prompt_optional)
                        },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = if (state.selectedTool.id == "replace") 4 else 3,
                shape = RoundedCornerShape(18.dp),
            )
            if (state.selectedTool.id == "replace") {
                Surface(shape = RoundedCornerShape(22.dp), color = StudioPrimaryContainer) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            stringResource(R.string.replacement_confirmation_title),
                            color = StudioBlue,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                        )
                        Text(
                            if (hasRequiredMask) {
                                stringResource(R.string.replacement_confirmation_mask_ready)
                            } else {
                                stringResource(R.string.replacement_confirmation_need_mask)
                            },
                            color = StudioInk.copy(alpha = 0.78f),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            if (hasReplacementPrompt) {
                                stringResource(R.string.replacement_confirmation_object_format, replacementPrompt)
                            } else {
                                stringResource(R.string.replacement_confirmation_need_replacement)
                            },
                            color = StudioInk.copy(alpha = 0.78f),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier.height(188.dp).border(1.dp, studioStateBorder(selected), RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(shape = CircleShape, color = studioStateIconContainer(selected)) {
                Icon(
                    if (title.contains("Renovation")) Icons.Rounded.AutoAwesome else Icons.Rounded.Brush,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp).size(22.dp),
                    tint = studioStateIconContent(selected),
                )
            }
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 2)
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PaletteChoiceCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier
            .width(96.dp)
            .height(142.dp)
            .border(1.dp, studioStateBorder(selected), RoundedCornerShape(18.dp)),
    ) {
        Column {
            if (label == "Suggestion IA") {
                Box(Modifier.fillMaxWidth().height(82.dp).background(StudioMist), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(30.dp), tint = StudioBlue)
                }
            } else {
                Row(Modifier.fillMaxWidth().height(82.dp).clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))) {
                    paletteColors(label).forEach { color ->
                        Box(Modifier.weight(1f).fillMaxSize().background(color))
                    }
                }
            }
            Text(
                displayLabel,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ProcessingStep(message: String) {
    Column(
        Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.padding(22.dp).size(46.dp), tint = StudioGreen)
        }
        Spacer(Modifier.height(22.dp))
        Text(stringResource(R.string.processing_transform), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        LinearProgressIndicator(Modifier.fillMaxWidth(0.78f).height(8.dp).clip(CircleShape))
    }
}

@Composable
private fun LayoutResultSummary(state: HomeDecorUiState) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPrimaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBlue),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.layout_changes), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioBlue)
            Text(
                layoutChangeSummary(state),
                style = MaterialTheme.typography.bodyLarge,
                color = StudioInk,
            )
            Spacer(Modifier.height(2.dp))
            Text(stringResource(R.string.layout_suggestions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioBlue)
            layoutSuggestions(state).forEach { suggestion ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Rounded.Check, null, Modifier.padding(top = 2.dp).size(18.dp), tint = StudioBlue)
                    Text(suggestion, style = MaterialTheme.typography.bodyMedium, color = StudioInk)
                }
            }
        }
    }
}

@Composable
private fun layoutChangeSummary(state: HomeDecorUiState): String {
    val goals = state.roomType.ifBlank { stringResource(R.string.layout_default_goals) }
    val keep = state.palette.ifBlank { stringResource(R.string.layout_default_keep) }
    val move = state.mobilierADeplacer.ifBlank { stringResource(R.string.layout_default_move) }
    val people = state.style.ifBlank { stringResource(R.string.layout_default_people) }
    val constraints = state.layoutConstraints.ifBlank { stringResource(R.string.layout_default_constraints) }
    return stringResource(R.string.layout_summary_format, goals, keep, move, people, constraints)
}

@Composable
private fun layoutSuggestions(state: HomeDecorUiState): List<String> {
    val suggestions = mutableListOf<String>()
    if (state.layoutConstraints.isNotBlank()) {
        suggestions += stringResource(R.string.layout_suggestion_constraints)
    }
    if (state.palette.isNotBlank()) {
        suggestions += stringResource(R.string.layout_suggestion_keep)
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

@Composable
private fun ResultStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val result = state.board.firstOrNull()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val resultReady = result?.isGeneratedResult() == true
    StepScaffold(
        eyebrow = stringResource(R.string.result),
        title = stringResource(R.string.your_result),
        body = stringResource(R.string.result_saved_workspace),
        buttonLabel = stringResource(R.string.new_creation),
        buttonIcon = Icons.Rounded.Check,
        onButton = { viewModel.startTool(state.selectedTool) },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(R.string.generated_image), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Card(shape = RoundedCornerShape(28.dp)) {
                NetworkOrResourceImage(
                    imageUrl = result?.imageUrl,
                    imageRes = result?.imageRes ?: R.drawable.sample_after_luxury,
                    contentDescription = stringResource(R.string.generated_image),
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                )
            }
            if (state.selectedTool.id == "layout") {
                LayoutResultSummary(state)
            }
            Text(stringResource(R.string.before_after), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(Modifier.weight(1f), shape = RoundedCornerShape(20.dp)) {
                    NetworkOrResourceImage(
                        imageUrl = result?.sourceImageUrl,
                        imageRes = selectedExampleImageRes(state),
                        contentDescription = stringResource(R.string.before),
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    )
                }
                Card(Modifier.weight(1f), shape = RoundedCornerShape(20.dp)) {
                    NetworkOrResourceImage(
                        imageUrl = result?.imageUrl,
                        imageRes = result?.imageRes ?: R.drawable.sample_after_luxury,
                        contentDescription = stringResource(R.string.after),
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    )
                }
            }
            if (state.selectedTool.id == "reference") {
                Text(stringResource(R.string.reference), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Card(shape = RoundedCornerShape(20.dp)) {
                    UriOrResourceImage(
                        uri = state.selectedReferenceUri,
                        imageRes = R.drawable.tool_reference,
                        contentDescription = stringResource(R.string.reference_image),
                        modifier = Modifier.fillMaxWidth().aspectRatio(1.4f),
                    )
                }
            }
            Surface(shape = RoundedCornerShape(22.dp), color = StudioPaper, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp))) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(stringResource(R.string.metadata), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.metadata_service, localizedWorkflowTitle(state.selectedTool)))
                    Text(stringResource(R.string.metadata_style, (state.style.ifBlank { state.palette }).takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.ai_choice)))
                    Text(stringResource(R.string.metadata_prompt, state.customPrompt.ifBlank { stringResource(R.string.no_custom_prompt) }))
                    Text(stringResource(R.string.metadata_status, stringResource(if (result?.status == "failed" || !resultReady) R.string.failed else R.string.ready)))
                    Text(stringResource(R.string.metadata_date, java.text.DateFormat.getDateTimeInstance().format(java.util.Date((result?.createdAt ?: System.currentTimeMillis().toDouble()).toLong()))))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        val saved = viewModel.saveResultToPortfolio(result)
                        Toast.makeText(context, if (saved) context.getString(R.string.toast_design_saved) else context.getString(R.string.toast_design_save_failed), Toast.LENGTH_LONG).show()
                    },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Rounded.Save, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.save))
                }
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val shared = shareResult(context, result)
                            if (!shared) {
                                Toast.makeText(context, context.getString(R.string.toast_share_failed), Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Rounded.Share, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.share))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val saved = saveResultToGallery(context, result)
                            if (!saved) {
                                Toast.makeText(context, context.getString(R.string.toast_design_save_failed), Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Rounded.Download, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.download))
                }
                OutlinedButton(onClick = viewModel::generate, shape = CircleShape, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.retry))
                }
            }
            OutlinedButton(
                onClick = { viewModel.startTool(state.selectedTool) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.new_creation))
            }
        }
    }
}

@Suppress("DEPRECATION")
private suspend fun saveResultToGallery(context: android.content.Context, result: BoardItem?): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val displayName = "homedecor-ai-${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/HomeDecor AI")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: return@withContext false
            context.contentResolver.openOutputStream(uri)?.use { output ->
                writeResultImage(context, result, output)
            } ?: return@withContext false
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, values, null, null)
        } else {
            val bitmap = resultBitmap(context, result)
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, displayName, "HomeDecor AI") ?: return@withContext false
        }
        true
    }.getOrDefault(false)
}

private suspend fun shareResult(context: android.content.Context, result: BoardItem?): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val shareDir = File(context.cacheDir, "share").apply { mkdirs() }
        val file = File(shareDir, "homedecor-ai-${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { output -> writeResultImage(context, result, output) }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        withContext(Dispatchers.Main) {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_design_chooser)))
        }
        true
    }.getOrDefault(false)
}

private fun writeResultImage(context: android.content.Context, result: BoardItem?, output: java.io.OutputStream) {
    val imageUrl = result?.imageUrl
    if (!imageUrl.isNullOrBlank()) {
        URL(imageUrl).openStream().use { input -> input.copyTo(output) }
        return
    }
    resultBitmap(context, result).compress(Bitmap.CompressFormat.JPEG, 94, output)
}

private fun resultBitmap(context: android.content.Context, result: BoardItem?): Bitmap {
    val imageRes = result?.imageRes ?: R.drawable.sample_after_luxury
    return BitmapFactory.decodeResource(context.resources, imageRes)
}

private fun openAuth(context: android.content.Context) {
    val authUrl = BuildConfig.APP_URL.trimEnd('/') + "/sign-in"
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)))
    }.onFailure {
        Toast.makeText(context, context.getString(R.string.auth_open_failed), Toast.LENGTH_LONG).show()
    }
}

@Composable
private fun DiscoverScreen(onTool: (DecorTool) -> Unit) {
    var selectedCluster by remember { mutableStateOf("Intérieurs") }
    var detailSection by remember { mutableStateOf<DiscoverSection?>(null) }
    var previewTarget by remember { mutableStateOf<DiscoverPreviewTarget?>(null) }
    val clusters = listOf("Intérieurs", "Architecture", "Paysages")
    val sections = HomeDecorCatalog.discoverSections.filter { it.cluster == selectedCluster }
    fun openPreview(section: DiscoverSection, item: GalleryItem) {
        previewTarget = section.discoverPreviewTarget(item)
    }
    val activeDetail = detailSection
    if (activeDetail != null) {
        DiscoverDetailScreen(
            section = activeDetail,
            onBack = { detailSection = null },
            onPreview = { openPreview(activeDetail, it) },
        )
        previewTarget?.let { target ->
            DiscoverPreviewDialog(
                target = target,
                onDismiss = { previewTarget = null },
                onTool = onTool,
            )
        }
        return
    }
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        ScreenHeaderPills(title = stringResource(R.string.nav_discover), trailing = null)
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            item { DiscoverClusterTabs(clusters = clusters, selected = selectedCluster, onSelect = { selectedCluster = it }) }
            items(sections, key = { it.id }) { section ->
                DiscoverSectionRow(
                    section = section,
                    onSeeAll = {
                        previewTarget = null
                        detailSection = section
                    },
                    onPreview = { openPreview(section, it) },
                )
            }
        }
    }
    previewTarget?.let { target ->
        DiscoverPreviewDialog(
            target = target,
            onDismiss = { previewTarget = null },
            onTool = onTool,
        )
    }
}

private data class DiscoverPreviewTarget(
    val item: GalleryItem,
    val section: DiscoverSection,
    val tool: DecorTool,
)

private fun DiscoverSection.discoverPreviewTarget(item: GalleryItem): DiscoverPreviewTarget? {
    val belongsToSection = items.any { it.id == item.id }
    val tool = HomeDecorCatalog.tools.firstOrNull { it.id == serviceToolId }
    return if (belongsToSection && tool != null) DiscoverPreviewTarget(item, this, tool) else null
}

@Composable
private fun ScreenHeaderPills(
    title: String,
    trailing: (@Composable () -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.width(54.dp))
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        Box(Modifier.width(54.dp), contentAlignment = Alignment.CenterEnd) {
            trailing?.invoke()
        }
    }
}

@Composable
private fun DiscoverHero(
    section: DiscoverSection?,
    onPreview: (GalleryItem) -> Unit,
) {
    val first = section?.items?.firstOrNull() ?: return
    val sectionTitle = localizedDiscoverSection(section)
    val sectionCluster = localizedDiscoverCluster(section.cluster)
    val firstTitle = localizedGalleryTitle(first)
    ElevatedCard(
        onClick = { onPreview(first) },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
    ) {
        Box(Modifier.fillMaxWidth().height(260.dp)) {
            Image(
                painter = painterResource(first.imageRes),
                contentDescription = firstTitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.78f)))))
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                    Text(
                        sectionCluster,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = StudioInk,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                    )
                }
                Text(sectionTitle, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text(stringResource(R.string.discover_hero_body), color = Color.White.copy(alpha = 0.84f), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun DiscoverClusterTabs(
    clusters: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(clusters) { cluster ->
            val active = selected == cluster
            val clusterLabel = localizedDiscoverCluster(cluster)
            Surface(
                onClick = { onSelect(cluster) },
                shape = CircleShape,
                color = if (active) StudioPrimaryContainer else StudioPaper,
                tonalElevation = if (active) 4.dp else 1.dp,
                modifier = Modifier.border(1.dp, if (active) StudioBlue else StudioLine, CircleShape),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        when (cluster) {
                            "Architecture" -> Icons.Rounded.Landscape
                            "Paysages" -> Icons.Rounded.Brush
                            else -> Icons.Rounded.Home
                        },
                        null,
                        Modifier.size(17.dp),
                        tint = StudioBlue,
                    )
                    Text(clusterLabel, color = if (active) StudioBlue else StudioInk, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun DiscoverSectionRow(
    section: DiscoverSection,
    onSeeAll: () -> Unit,
    onPreview: (GalleryItem) -> Unit,
) {
    val sectionTitle = localizedDiscoverSection(section)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(sectionTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            OutlinedButton(
                onClick = onSeeAll,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text(stringResource(R.string.see_all))
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(section.items, key = { it.id }) { item ->
                GalleryCard(item = item, onClick = { onPreview(item) })
            }
        }
    }
}

@Composable
private fun DiscoverDetailScreen(
    section: DiscoverSection,
    onBack: () -> Unit,
    onPreview: (GalleryItem) -> Unit,
) {
    val sectionTitle = localizedDiscoverSection(section)
    val sectionCluster = localizedDiscoverCluster(section.cluster)
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        Row(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Column(Modifier.weight(1f)) {
                Text(sectionTitle, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                Text(stringResource(R.string.discover_detail_subtitle, sectionCluster.lowercase()), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(shape = CircleShape, color = StudioPrimaryContainer, tonalElevation = 2.dp) {
                Text(
                    stringResource(R.string.ideas_count, section.items.size),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    color = StudioBlue,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(section.items, key = { it.id }) { item ->
                GalleryCard(item = item, onClick = { onPreview(item) })
            }
        }
    }
}

@Composable
private fun DiscoverPreviewDialog(
    target: DiscoverPreviewTarget,
    onDismiss: () -> Unit,
    onTool: (DecorTool) -> Unit,
) {
    val item = target.item
    val itemTitle = localizedGalleryTitle(item)
    val itemCategory = localizedGalleryCategory(item.category)
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onTool(target.tool)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                colors = studioPrimaryButtonColors(),
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(17.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.create_with_style))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.close))
            }
        },
        title = { Text(itemCategory, fontWeight = FontWeight.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = itemTitle,
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.92f).clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop,
                )
                Text(itemTitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        shape = RoundedCornerShape(30.dp),
    )
}

@Composable
private fun GalleryCard(
    item: GalleryItem,
    onClick: () -> Unit,
) {
    val itemTitle = localizedGalleryTitle(item)
    val itemCategory = localizedGalleryCategory(item.category)
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        modifier = Modifier.width(196.dp),
    ) {
        Box(Modifier.fillMaxWidth().height(250.dp)) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = itemTitle,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f)))))
            Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                Text(itemCategory, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 2)
                Text(itemTitle, color = Color.White.copy(alpha = 0.76f), style = MaterialTheme.typography.labelMedium, maxLines = 1)
            }
            Surface(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.9f)) {
                Icon(Icons.Rounded.Visibility, contentDescription = stringResource(R.string.preview), Modifier.padding(8.dp).size(16.dp), tint = StudioBlue)
            }
        }
    }
}

private data class ElitePassPresentation(
    val claimedDays: Int,
    val currentDay: Int,
    val claimedToday: Boolean,
    val canClaimToday: Boolean,
    val canRetrySync: Boolean,
    val daySevenAvailable: Boolean,
    val atCap: Boolean,
    val lockedByTime: Boolean,
    val currentDayVisible: Boolean,
    val syncing: Boolean,
    val progress: Float,
    val todayStatus: String,
    val claimButtonLabel: String,
    val footer: String,
)

@Composable
private fun elitePassPresentation(state: HomeDecorUiState): ElitePassPresentation {
    val now = System.currentTimeMillis()
    val claimStatus = state.viewer.status ?: state.viewer.claimStatus
    val daySevenClaimed = state.eliteLastClaimWasDaySeven
    val confirmedDays = if (daySevenClaimed) 7 else state.viewer.streakCount.coerceIn(0, 7)
    val syncing = state.elitePassSyncState == ElitePassSyncState.Syncing || state.elitePassSyncState == ElitePassSyncState.Loading
    val claimedToday = state.claimedToday && state.elitePassSyncState != ElitePassSyncState.Error
    val currentDay = when {
        daySevenClaimed -> 7
        claimedToday -> confirmedDays.coerceIn(1, 7)
        else -> (confirmedDays + 1).coerceIn(1, 7)
    }
    val canClaimToday = state.viewer.canClaimDiamond &&
        !claimedToday &&
        state.elitePassSyncState != ElitePassSyncState.LocalOnly &&
        state.elitePassSyncState != ElitePassSyncState.Error
    val daySevenAvailable = canClaimToday && currentDay == 7
    val lockedByTime = !claimedToday && !canClaimToday && state.viewer.nextDiamondClaimAt > now
    val atCap = !claimedToday &&
        !canClaimToday &&
        !daySevenAvailable &&
        (claimStatus == "at_cap" || claimStatus == "already_at_cap" || (!lockedByTime && state.viewer.diamondBalance >= 3))
    val canRetrySync = state.elitePassSyncState == ElitePassSyncState.Error && !syncing
    val currentDayVisible = !claimedToday && (canClaimToday || atCap || lockedByTime)
    val todayStatus = when {
        syncing -> stringResource(R.string.elite_syncing)
        claimedToday -> stringResource(R.string.claimed)
        daySevenAvailable || canClaimToday -> stringResource(R.string.available)
        atCap -> stringResource(R.string.daily_balance_full)
        state.elitePassSyncState == ElitePassSyncState.Error -> stringResource(R.string.elite_resync)
        state.elitePassSyncState == ElitePassSyncState.LocalOnly -> stringResource(R.string.elite_local)
        lockedByTime -> stringResource(R.string.next_window)
        else -> stringResource(R.string.not_ready)
    }
    val claimButtonLabel = when {
        syncing -> stringResource(R.string.elite_syncing)
        claimedToday -> stringResource(R.string.claim_today)
        atCap -> stringResource(R.string.daily_balance_full)
        canRetrySync -> stringResource(R.string.retry_sync_reward)
        daySevenAvailable -> stringResource(R.string.claim_d7_pro)
        canClaimToday -> stringResource(R.string.claim_diamond)
        lockedByTime -> stringResource(R.string.next_reward_pending)
        else -> stringResource(R.string.sync_required)
    }
    val footer = when {
        syncing -> stringResource(R.string.elite_footer_syncing)
        claimedToday && daySevenClaimed -> stringResource(R.string.elite_footer_day7_confirmed)
        claimedToday -> stringResource(R.string.elite_footer_reward_confirmed)
        daySevenAvailable -> stringResource(R.string.elite_footer_day7_available)
        canClaimToday -> stringResource(R.string.elite_footer_diamond_available)
        state.elitePassSyncState == ElitePassSyncState.Error -> state.elitePassSyncMessage
        state.elitePassSyncState == ElitePassSyncState.LocalOnly -> state.elitePassSyncMessage
        atCap -> stringResource(R.string.elite_footer_balance_full)
        else -> stringResource(R.string.elite_footer_next_window)
    }
    return ElitePassPresentation(
        claimedDays = confirmedDays,
        currentDay = currentDay,
        claimedToday = claimedToday,
        canClaimToday = canClaimToday,
        canRetrySync = canRetrySync,
        daySevenAvailable = daySevenAvailable,
        atCap = atCap,
        lockedByTime = lockedByTime,
        currentDayVisible = currentDayVisible,
        syncing = syncing,
        progress = (confirmedDays / 7f).coerceIn(0f, 1f),
        todayStatus = todayStatus,
        claimButtonLabel = claimButtonLabel,
        footer = footer,
    )
}

@Composable
private fun EliteSyncPill(state: HomeDecorUiState) {
    val (label, color, contentColor) = when (state.elitePassSyncState) {
        ElitePassSyncState.Loading -> Triple(stringResource(R.string.elite_sync), Color.White.copy(alpha = 0.14f), Color.White)
        ElitePassSyncState.Syncing -> Triple(stringResource(R.string.elite_sync), StudioGold, StudioInk)
        ElitePassSyncState.Synced -> Triple(stringResource(R.string.elite_sync_ok), Color.White.copy(alpha = 0.16f), Color.White)
        ElitePassSyncState.LocalOnly -> Triple(stringResource(R.string.elite_local), Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.82f))
        ElitePassSyncState.Error -> Triple(stringResource(R.string.elite_resync), StudioErrorContainer, StudioRose)
    }
    Surface(shape = CircleShape, color = color) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (state.elitePassSyncState == ElitePassSyncState.Syncing || state.elitePassSyncState == ElitePassSyncState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(12.dp), color = contentColor, strokeWidth = 2.dp)
            }
            Text(label, color = contentColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EliteStatusStrip(pass: ElitePassPresentation) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        EliteMetricPill(
            label = stringResource(R.string.today),
            value = pass.todayStatus,
            highlighted = pass.canClaimToday,
            modifier = Modifier.weight(1f),
        )
        EliteMetricPill(
            label = stringResource(R.string.streak),
            value = "${pass.claimedDays}/7",
            highlighted = false,
            modifier = Modifier.weight(1f),
        )
        EliteMetricPill(
            label = stringResource(R.string.day_7_pro),
            value = when {
                pass.claimedToday && pass.currentDay == 7 -> stringResource(R.string.claimed)
                pass.daySevenAvailable -> stringResource(R.string.available)
                else -> stringResource(R.string.locked)
            },
            highlighted = pass.daySevenAvailable,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun EliteMetricPill(
    label: String,
    value: String,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (highlighted) StudioGold.copy(alpha = 0.24f) else Color.White.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (highlighted) StudioGold.copy(alpha = 0.62f) else Color.White.copy(alpha = 0.08f)),
        modifier = modifier,
    ) {
        Column(Modifier.padding(horizontal = 10.dp, vertical = 9.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, color = Color.White.copy(alpha = 0.62f), style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(value, color = if (highlighted) StudioGold else Color.White, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ElitePassScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val pass = elitePassPresentation(state)
    ScreenColumn(title = stringResource(R.string.nav_elite_pass), subtitle = null, trailing = { CreditPill(state, compact = true, onClick = viewModel::openDiamondStore) }) {
        LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            item {
                ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF151713))) {
                    Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.weight(1f)) {
                                Text(stringResource(R.string.daily_pass), color = StudioGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                                Text(stringResource(R.string.nav_elite_pass), color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                            }
                            EliteSyncPill(state)
                        }
                        Text(
                            stringResource(R.string.elite_intro),
                            color = Color.White.copy(alpha = 0.78f),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            repeat(7) { index ->
                                val day = index + 1
                                val claimed = day <= pass.claimedDays
                                val today = day == pass.currentDay && pass.canClaimToday
                                val isProDay = day == 7
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = CircleShape,
                                        color = when {
                                            claimed -> Color.White
                                            today -> StudioGold
                                            isProDay -> Color.White.copy(alpha = 0.18f)
                                            else -> Color.White.copy(alpha = 0.10f)
                                        },
                                    ) {
                                        Text(
                                            "$day",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                            color = if (claimed || today) StudioBlack else Color.White.copy(alpha = 0.76f),
                                            fontWeight = FontWeight.Black,
                                        )
                                    }
                                    Text(if (isProDay) stringResource(R.string.pro) else stringResource(R.string.one_diamond_reward), color = if (isProDay) StudioGold else Color.White.copy(alpha = 0.68f), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                        EliteStatusStrip(pass)
                        Button(
                            onClick = viewModel::claimDiamond,
                            enabled = (pass.canClaimToday || pass.canRetrySync) && !pass.syncing,
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (pass.daySevenAvailable) StudioGold else Color.White,
                                contentColor = StudioBlack,
                                disabledContainerColor = Color.White.copy(alpha = 0.16f),
                                disabledContentColor = Color.White.copy(alpha = 0.62f),
                            ),
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                        ) {
                            if (pass.syncing) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = StudioInk, strokeWidth = 2.dp)
                            } else {
                                Icon(
                                    when {
                                        pass.canRetrySync -> Icons.Rounded.Refresh
                                        pass.daySevenAvailable -> Icons.Rounded.Star
                                        else -> Icons.Rounded.Diamond
                                    },
                                    null,
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(pass.claimButtonLabel, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
            item { EliteTimelineCard(state = state, onClaim = viewModel::claimDiamond) }
            item {
                OutlinedButton(
                    onClick = viewModel::openDiamondStore,
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                ) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.open_diamond_store))
                }
            }
            item { PaywallCard(onUnlock = viewModel::unlockProPreview) }
        }
    }
}

@Composable
private fun EliteTimelineCard(
    state: HomeDecorUiState,
    onClaim: () -> Unit,
) {
    val pass = elitePassPresentation(state)
    ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF111410))) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.your_elite_streak), color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.future_days_locked), color = Color.White.copy(alpha = 0.62f), style = MaterialTheme.typography.labelMedium)
                }
                Surface(shape = CircleShape, color = StudioProContainer) {
                    Text(
                        "${pass.claimedDays}/7",
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        color = StudioInk,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
            LinearProgressIndicator(
                progress = { pass.progress },
                modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                color = StudioGold,
                trackColor = Color.White.copy(alpha = 0.18f),
            )
            repeat(7) { index ->
                val day = index + 1
                val claimed = day <= pass.claimedDays
                val current = day == pass.currentDay && pass.currentDayVisible
                val claimable = day == pass.currentDay && pass.canClaimToday
                EliteDayRow(
                    day = day,
                    claimed = claimed,
                    current = current,
                    claimable = claimable,
                    syncing = pass.syncing,
                    statusText = eliteDayStatusText(day, pass),
                    onClaim = onClaim,
                )
            }
            Text(
                pass.footer,
                color = Color.White.copy(alpha = 0.78f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun eliteDayStatusText(day: Int, pass: ElitePassPresentation): String {
    val daysLeft = day - pass.claimedDays
    return when {
        day <= pass.claimedDays -> stringResource(R.string.claimed)
        day == pass.currentDay && pass.canClaimToday -> stringResource(R.string.available_today)
        day == pass.currentDay && pass.atCap -> stringResource(R.string.daily_balance_full)
        day == pass.currentDay && pass.lockedByTime -> stringResource(R.string.next_reward_pending)
        day == 7 -> stringResource(R.string.pro_bonus_locked)
        daysLeft <= 1 -> stringResource(R.string.next_reward_pending)
        else -> stringResource(R.string.locked_day_format, day)
    }
}

@Composable
private fun EliteDayRow(
    day: Int,
    claimed: Boolean,
    current: Boolean,
    claimable: Boolean,
    syncing: Boolean,
    statusText: String,
    onClaim: () -> Unit,
) {
    val locked = !claimed && !claimable
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = when {
            current -> Color(0xFF332B12)
            claimed -> Color.White.copy(alpha = 0.10f)
            day == 7 -> Color(0xFF211C12)
            else -> Color.White.copy(alpha = 0.055f)
        },
        border = androidx.compose.foundation.BorderStroke(1.dp, if (current) StudioGold.copy(alpha = 0.58f) else Color.White.copy(alpha = 0.10f)),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(shape = CircleShape, color = if (claimed || current) StudioGold else Color.White.copy(alpha = 0.10f)) {
                Icon(
                    when {
                        claimed -> Icons.Rounded.Check
                        locked -> Icons.Rounded.Lock
                        day == 7 -> Icons.Rounded.Star
                        else -> Icons.AutoMirrored.Rounded.ViewQuilt
                    },
                    null,
                    Modifier.padding(9.dp).size(18.dp),
                    tint = if (claimed || current) Color.White else Color.White.copy(alpha = 0.48f),
                )
            }
            Column(Modifier.weight(1f)) {
                Text(if (day == 7) stringResource(R.string.day_7_pro) else stringResource(R.string.day_format, day), color = if (day == 7) StudioGold else Color.White, fontWeight = FontWeight.Black)
                Text(
                    statusText,
                    color = if (current) StudioGold else Color.White.copy(alpha = 0.68f),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            if (claimable) {
                Button(
                    onClick = onClaim,
                    enabled = !syncing,
                    shape = CircleShape,
                    colors = studioProButtonColors(),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier.height(48.dp),
                ) {
                    Text(if (syncing) stringResource(R.string.ellipsis) else if (day == 7) stringResource(R.string.day7_bonus) else stringResource(R.string.one_diamond_reward), fontWeight = FontWeight.Black)
                }
            } else if (day == 7) {
                Text(stringResource(R.string.day7_bonus), color = StudioGold, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PaywallCard(onUnlock: () -> Unit) {
    ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(stringResource(R.string.unlock_unlimited_designs), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            FeatureRow(Icons.Rounded.AutoAwesome, stringResource(R.string.feature_priority_ai))
            FeatureRow(Icons.Rounded.Download, stringResource(R.string.feature_clean_exports))
            FeatureRow(Icons.Rounded.Diamond, stringResource(R.string.feature_frictionless_generations))
            Button(onClick = onUnlock, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                Text(stringResource(R.string.upgrade_to_pro))
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    val openRealAuth = { openAuth(context) }
    val signedIn = !state.viewer.isGuest || state.signedInName != null
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        ScreenHeaderPills(
            title = stringResource(R.string.nav_profile),
            trailing = {
                FilledIconButton(onClick = viewModel::openSettings, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.Settings, contentDescription = stringResource(R.string.settings))
                }
            },
        )
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
                item {
                    AccountHeaderSection(
                        state = state,
                        signedIn = signedIn,
                        onSignIn = openRealAuth,
                    onStore = viewModel::openDiamondStore,
                    onPaywall = viewModel::openPaywall,
                )
            }
            item {
                ProfileSignInStateSection(
                    signedIn = signedIn,
                    email = state.signedInEmail,
                    onSignIn = openRealAuth,
                )
            }
            item {
                PortfolioHistorySection(
                    state = state,
                    onCreate = { viewModel.selectTab(MainTab.Tools) },
                )
            }
            item {
                PurchasesSection(
                    state = state,
                    onStore = viewModel::openDiamondStore,
                    onPaywall = viewModel::openPaywall,
                    onRetrySync = viewModel::retryPurchaseSync,
                )
            }
            item {
                ProfileSettingsSection(
                    onSettings = viewModel::openSettings,
                    onShare = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_text))
                        }
                        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_app_chooser)))
                    },
                )
            }
        }
    }
}

@Composable
private fun AccountHeaderSection(
    state: HomeDecorUiState,
    signedIn: Boolean,
    onSignIn: () -> Unit,
    onStore: () -> Unit,
    onPaywall: () -> Unit,
) {
    ElevatedCard(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Surface(shape = CircleShape, color = if (state.isPro) StudioProContainer else StudioPrimaryContainer) {
                    Icon(Icons.Rounded.Person, null, Modifier.padding(16.dp).size(28.dp), tint = if (state.isPro) StudioGold else StudioBlue)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        when {
                            state.signedInName != null -> state.signedInName
                            signedIn -> stringResource(R.string.account_connected)
                            else -> stringResource(R.string.signed_out)
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                    )
                    Text(
                        when {
                            state.signedInEmail != null -> state.signedInEmail
                            signedIn -> stringResource(R.string.profile_signed_in_sync)
                            else -> stringResource(R.string.profile_sign_in_sync)
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                ProfileInfoPill(
                    icon = Icons.Rounded.Diamond,
                    label = stringResource(R.string.diamonds_count, state.diamonds),
                    modifier = Modifier.weight(1f),
                )
                ProfileInfoPill(
                    icon = Icons.Rounded.Star,
                    label = if (state.isPro) stringResource(R.string.active_pro) else stringResource(R.string.free_plan),
                    modifier = Modifier.weight(1f),
                    accent = state.isPro,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                if (!signedIn) {
                    Button(
                        onClick = onSignIn,
                        shape = CircleShape,
                        colors = studioPrimaryButtonColors(),
                        modifier = Modifier.weight(1f).height(50.dp),
                    ) {
                        Text(stringResource(R.string.sign_in), fontWeight = FontWeight.Black)
                    }
                } else if (!state.isPro) {
                    Button(
                        onClick = onPaywall,
                        shape = CircleShape,
                        colors = studioPrimaryButtonColors(),
                        modifier = Modifier.weight(1f).height(50.dp),
                    ) {
                        Text(stringResource(R.string.upgrade_to_pro), fontWeight = FontWeight.Black)
                    }
                }
                OutlinedButton(
                    onClick = onStore,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(50.dp),
                ) {
                    Icon(Icons.Rounded.Diamond, null, Modifier.size(17.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.diamonds), fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoPill(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    accent: Boolean = false,
) {
    Surface(
        shape = CircleShape,
        color = if (accent) StudioProContainer else StudioPrimaryContainer,
        modifier = modifier,
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(icon, null, Modifier.size(17.dp), tint = if (accent) StudioGold else StudioBlue)
            Text(label, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun ProfileSignInStateSection(
    signedIn: Boolean,
    email: String?,
    onSignIn: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.Rounded.Lock, title = stringResource(R.string.connection_state))
        ElevatedCard(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(shape = CircleShape, color = if (signedIn) StudioPrimaryContainer else StudioMist) {
                    Icon(if (signedIn) Icons.Rounded.Check else Icons.Rounded.Lock, null, Modifier.padding(10.dp).size(20.dp), tint = if (signedIn) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(Modifier.weight(1f)) {
                    Text(if (signedIn) stringResource(R.string.account_connected) else stringResource(R.string.signed_out), fontWeight = FontWeight.Black)
                    Text(
                        email ?: if (signedIn) stringResource(R.string.profile_signed_in_sync) else stringResource(R.string.local_session_with_device),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (!signedIn) {
                    OutlinedButton(onClick = onSignIn, shape = CircleShape) {
                        Text(stringResource(R.string.connection))
                    }
                }
            }
        }
    }
}

@Composable
private fun PortfolioHistorySection(
    state: HomeDecorUiState,
    onCreate: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.AutoMirrored.Rounded.ViewQuilt, title = stringResource(R.string.portfolio_history))
        Text(
            stringResource(R.string.portfolio_saved_count, state.board.size),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (state.board.isEmpty()) {
            EmptyPortfolio(onCreate = onCreate)
        } else {
            val rows = ((state.board.size + 1) / 2).coerceAtLeast(1)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height((rows * 226).dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false,
            ) {
                items(state.board, key = { it.id }) { item ->
                    BoardCard(item = item)
                }
            }
        }
    }
}

@Composable
private fun PurchasesSection(
    state: HomeDecorUiState,
    onStore: () -> Unit,
    onPaywall: () -> Unit,
    onRetrySync: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.Rounded.Diamond, title = stringResource(R.string.purchases))
        ElevatedCard(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        ) {
            Column(Modifier.padding(6.dp)) {
                ProfileActionRow(
                    icon = Icons.Rounded.Diamond,
                    title = stringResource(R.string.diamonds),
                    subtitle = stringResource(R.string.credits_available, state.diamonds),
                    action = stringResource(R.string.store),
                    onClick = onStore,
                )
                ProfileActionRow(
                    icon = Icons.Rounded.Star,
                    title = stringResource(R.string.nav_elite_pass),
                    subtitle = if (state.isPro) stringResource(R.string.active_pro_access) else stringResource(R.string.clean_exports_and_generations),
                    action = if (state.isPro) stringResource(R.string.manage) else stringResource(R.string.view),
                    onClick = onPaywall,
                )
                state.purchaseMessage?.takeIf { it.isNotBlank() }?.let { message ->
                    PurchaseSyncNotice(
                        message = message,
                        pending = state.pendingPurchaseSync != null,
                        busy = state.purchaseBusy,
                        onRetry = onRetrySync,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PurchaseSyncNotice(
    message: String,
    pending: Boolean,
    busy: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (pending) StudioErrorContainer else StudioMist,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                if (pending) Icons.Rounded.Refresh else Icons.Rounded.Check,
                contentDescription = null,
                tint = if (pending) StudioRose else StudioBlue,
                modifier = Modifier.size(18.dp),
            )
            Text(
                message,
                modifier = Modifier.weight(1f),
                color = if (pending) StudioRose else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (pending) FontWeight.SemiBold else FontWeight.Normal,
            )
            if (pending) {
                OutlinedButton(
                    onClick = onRetry,
                    enabled = !busy,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(48.dp),
                ) {
                    Text(if (busy) stringResource(R.string.syncing_short) else stringResource(R.string.retry), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ProfileSettingsSection(
    onSettings: () -> Unit,
    onShare: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.Rounded.Settings, title = stringResource(R.string.settings))
        ElevatedCard(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        ) {
            Column(Modifier.padding(6.dp)) {
                ProfileActionRow(
                    icon = Icons.Rounded.Settings,
                    title = stringResource(R.string.preferences),
                    subtitle = stringResource(R.string.preferences_subtitle),
                    action = stringResource(R.string.open),
                    onClick = onSettings,
                )
                ProfileActionRow(
                    icon = Icons.Rounded.Share,
                    title = stringResource(R.string.share_app),
                    subtitle = stringResource(R.string.share_app_subtitle),
                    action = stringResource(R.string.share),
                    onClick = onShare,
                )
            }
        }
    }
}

@Composable
private fun ProfileSectionTitle(
    icon: ImageVector,
    title: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Surface(shape = CircleShape, color = StudioPrimaryContainer) {
            Icon(icon, null, Modifier.padding(8.dp).size(18.dp), tint = StudioBlue)
        }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun ProfileActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    action: String,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(subtitle, maxLines = 2, overflow = TextOverflow.Ellipsis) },
        leadingContent = {
            Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                Icon(icon, contentDescription = null, tint = StudioBlue, modifier = Modifier.padding(9.dp).size(20.dp))
            }
        },
        trailingContent = {
            OutlinedButton(
                onClick = onClick,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp),
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(action, fontWeight = FontWeight.Bold, maxLines = 1)
            }
        },
    )
}

@Composable
private fun EmptyPortfolio(onCreate: () -> Unit) {
    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.padding(24.dp).size(36.dp), tint = StudioBlue)
            }
            Text(stringResource(R.string.empty_portfolio_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text(
                stringResource(R.string.empty_portfolio_body),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(0.86f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Button(
                onClick = onCreate,
                shape = CircleShape,
                colors = studioPrimaryButtonColors(),
                modifier = Modifier.fillMaxWidth().height(54.dp),
            ) {
                Text(stringResource(R.string.create_design), fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (enabled) StudioPaper else StudioMist,
        ),
    ) {
        ListItem(
            headlineContent = { Text(title, fontWeight = FontWeight.Bold, color = if (enabled) Color.Unspecified else MaterialTheme.colorScheme.onSurfaceVariant) },
            supportingContent = { Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            leadingContent = {
        Surface(shape = CircleShape, color = StudioPrimaryContainer) {
            Icon(icon, contentDescription = null, tint = if (enabled) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(9.dp).size(20.dp))
                }
            },
        )
    }
}

@Composable
private fun BoardCard(item: com.ismail.homedecorai.BoardItem) {
    val toolTitle = boardToolTitleRes(item.toolTitle)?.let { stringResource(it) } ?: item.toolTitle
    val ready = item.isGeneratedResult()
    val failed = item.status == "failed"
    val statusText = stringResource(
        when {
            failed -> R.string.failed
            ready -> R.string.ready
            else -> R.string.processing_ellipsis
        },
    )
    ElevatedCard(shape = RoundedCornerShape(20.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
        Box(Modifier.fillMaxWidth().height(208.dp)) {
            NetworkOrResourceImage(
                imageUrl = item.imageUrl,
                imageRes = item.imageRes,
                contentDescription = toolTitle,
                modifier = Modifier.fillMaxSize(),
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f)))))
            Surface(
                shape = CircleShape,
                color = if (ready) StudioPrimaryContainer else if (failed) StudioErrorContainer else StudioMist,
                modifier = Modifier.align(Alignment.TopStart).padding(10.dp),
            ) {
                Text(
                    statusText,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    color = if (failed) StudioRose else StudioInk,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
            }
            Column(Modifier.align(Alignment.BottomStart).padding(12.dp)) {
                Text(toolTitle, color = Color.White, fontWeight = FontWeight.Black, maxLines = 1)
                Text(stringResource(R.string.design_pair_format, localizedOption(item.roomType), localizedOption(item.style)), color = Color.White.copy(alpha = 0.76f), style = MaterialTheme.typography.labelMedium, maxLines = 1)
            }
        }
    }
}

@Composable
private fun NetworkOrResourceImage(
    imageUrl: String?,
    imageRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    var bitmap by remember(imageUrl) { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    LaunchedEffect(imageUrl) {
        bitmap = null
        if (!imageUrl.isNullOrBlank()) {
            bitmap = withContext(Dispatchers.IO) {
                runCatching {
                    URL(imageUrl).openStream().use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
                }.getOrNull()
            }
        }
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap!!,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    } else {
        Image(
            painter = painterResource(imageRes),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun FeatureRow(
    icon: ImageVector,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = CircleShape, color = StudioPrimaryContainer) {
            Icon(icon, contentDescription = null, tint = StudioBlue, modifier = Modifier.padding(8.dp).size(18.dp))
        }
        Text(text, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PaywallSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onSubscription: (String, String, String, Double?, Double?) -> Unit,
    onRetrySync: () -> Unit,
    onStore: () -> Unit,
) {
    val context = LocalContext.current
    val modalTapBlocker = remember { MutableInteractionSource() }
    var offering by remember { mutableStateOf<Offering?>(null) }
    var offeringsLoading by remember { mutableStateOf(true) }
    var purchasing by remember { mutableStateOf(false) }
    var restoring by remember { mutableStateOf(false) }
    var selectedPlan by remember { mutableStateOf("yearly") }
    var message by remember { mutableStateOf<String?>(null) }
    fun loadOfferings() {
        offeringsLoading = true
        offering = null
        message = null
        if (!Purchases.isConfigured) {
            offeringsLoading = false
            message = context.getString(R.string.subscriptions_unavailable)
            return
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                offering = offerings.current ?: offerings.all.values.firstOrNull()
                offeringsLoading = false
                if (offering == null) {
                    message = context.getString(R.string.no_subscription_available)
                }
            }

            override fun onError(error: PurchasesError) {
                offeringsLoading = false
                message = context.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.load_prices_failed))
            }
        })
    }
    LaunchedEffect(Unit) {
        loadOfferings()
    }
    fun buy(packageToPurchase: Package?, subscriptionType: String, entitlement: String) {
        val activity = context.findActivity()
        if (packageToPurchase == null || activity == null || !Purchases.isConfigured) {
            message = context.getString(R.string.plan_unavailable)
            return
        }
        purchasing = true
        message = null
        Purchases.sharedInstance.purchase(PurchaseParams.Builder(activity, packageToPurchase).build(), object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                purchasing = false
                val active = customerInfo.entitlements.active.values.firstOrNull()
                onSubscription(
                    "pro",
                    subscriptionType,
                    active?.identifier ?: entitlement,
                    active?.latestPurchaseDate?.time?.toDouble(),
                    active?.expirationDate?.time?.toDouble(),
                )
            }

            override fun onError(error: PurchasesError, userCancelled: Boolean) {
                purchasing = false
                message = if (userCancelled) {
                    context.getString(R.string.purchase_cancelled)
                } else {
                    context.getString(rawServiceMessageToKind(context, error.message).purchaseAttemptMessageRes(R.string.purchase_failed))
                }
            }
        })
    }
    val monthlyPackage = offering?.monthly ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.MONTHLY }
    val yearlyPackage = offering?.annual ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.ANNUAL }
    val purchaseBusy = purchasing || restoring || state.purchaseBusy
    val pricesUnavailable = !offeringsLoading && monthlyPackage == null && yearlyPackage == null
    LaunchedEffect(offeringsLoading, monthlyPackage, yearlyPackage) {
        if (!offeringsLoading) {
            selectedPlan = when {
                selectedPlan == "yearly" && yearlyPackage != null -> "yearly"
                selectedPlan == "monthly" && monthlyPackage != null -> "monthly"
                yearlyPackage != null -> "yearly"
                monthlyPackage != null -> "monthly"
                else -> selectedPlan
            }
        }
    }
    val selectedPackage = if (selectedPlan == "yearly") yearlyPackage else monthlyPackage
    val selectedSubscriptionType = if (selectedPlan == "yearly") "yearly" else "monthly"
    val selectedEntitlement = if (selectedPlan == "yearly") "annual_pro" else "monthly_pro"
    Box(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .clickable(
                interactionSource = modalTapBlocker,
                indication = null,
                onClick = {},
            ),
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd).windowInsetsPadding(WindowInsets.statusBars).padding(16.dp).size(48.dp),
        ) {
            Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close))
        }
        LazyColumn(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 92.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(shape = CircleShape, color = StudioProContainer, tonalElevation = 6.dp) {
                        Icon(Icons.Rounded.Diamond, null, Modifier.padding(20.dp).size(38.dp), tint = StudioInk)
                    }
                    Text(stringResource(R.string.paywall_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    Text(
                        stringResource(R.string.paywall_subtitle),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
            item {
                ElevatedCard(shape = RoundedCornerShape(28.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioBlack)) {
                    Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(stringResource(R.string.pro_upper), color = StudioGold, fontWeight = FontWeight.Black)
                        Text(stringResource(R.string.premium_creation), color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                        FeatureRowOnDark(Icons.Rounded.AutoAwesome, stringResource(R.string.paywall_feature_generations))
                        FeatureRowOnDark(Icons.Rounded.Refresh, stringResource(R.string.paywall_feature_fast))
                        FeatureRowOnDark(Icons.Rounded.Star, stringResource(R.string.paywall_feature_styles))
                        FeatureRowOnDark(Icons.Rounded.Save, stringResource(R.string.paywall_feature_history))
                        FeatureRowOnDark(Icons.Rounded.Download, stringResource(R.string.paywall_feature_watermark))
                        if (offeringsLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = StudioGold, strokeWidth = 2.dp)
                                Text(stringResource(R.string.loading_prices), color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        if (!pricesUnavailable && (message != null || state.purchaseMessage != null)) {
                            val displayMessage = message ?: state.purchaseMessage.orEmpty()
                            if (state.pendingPurchaseSync != null && message == null) {
                                PurchaseSyncNotice(
                                    message = displayMessage,
                                    pending = true,
                                    busy = state.purchaseBusy,
                                    onRetry = onRetrySync,
                                )
                            } else {
                                Text(displayMessage, color = StudioGold, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        if (pricesUnavailable) {
                            PaywallPricesFallback(
                                message = message ?: context.getString(R.string.load_prices_failed),
                                retrying = offeringsLoading,
                                onRetry = { loadOfferings() },
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            PlanChoiceButton(
                                title = stringResource(R.string.monthly),
                                price = monthlyPackage?.product?.price?.formatted,
                                loading = offeringsLoading,
                                enabled = !offeringsLoading && !purchaseBusy && monthlyPackage != null,
                                selected = selectedPlan == "monthly" && monthlyPackage != null,
                                onClick = { selectedPlan = "monthly" },
                                modifier = Modifier.weight(1f).height(66.dp),
                            )
                            PlanChoiceButton(
                                title = stringResource(R.string.yearly),
                                price = yearlyPackage?.product?.price?.formatted,
                                loading = offeringsLoading,
                                enabled = !offeringsLoading && !purchaseBusy && yearlyPackage != null,
                                selected = selectedPlan == "yearly" && yearlyPackage != null,
                                onClick = { selectedPlan = "yearly" },
                                modifier = Modifier.weight(1f).height(66.dp),
                            )
                        }
                        Button(
                            onClick = { buy(selectedPackage, selectedSubscriptionType, selectedEntitlement) },
                            enabled = !offeringsLoading && !purchaseBusy && selectedPackage != null,
                            shape = CircleShape,
                            colors = studioProButtonColors(),
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                        ) {
                            Text(
                                when {
                                    purchaseBusy -> stringResource(R.string.processing_ellipsis)
                                    offeringsLoading -> stringResource(R.string.loading_ellipsis)
                                    state.isPro -> stringResource(R.string.pro_activated)
                                    selectedPlan == "yearly" && selectedPackage != null -> stringResource(R.string.continue_yearly)
                                    selectedPlan == "monthly" && selectedPackage != null -> stringResource(R.string.continue_monthly)
                                    else -> stringResource(R.string.prices_unavailable)
                                },
                                fontWeight = FontWeight.Black,
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                if (!Purchases.isConfigured) {
                                    message = context.getString(R.string.restore_unavailable)
                                } else {
                                    restoring = true
                                    message = null
                                    Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
                                        override fun onReceived(customerInfo: CustomerInfo) {
                                            restoring = false
                                            val active = customerInfo.entitlements.active.values.firstOrNull()
                                            if (active != null) {
                                                onSubscription("pro", if (active.identifier.contains("annual")) "yearly" else "monthly", active.identifier, active.latestPurchaseDate.time.toDouble(), active.expirationDate?.time?.toDouble())
                                            } else {
                                                message = context.getString(R.string.no_active_pro_purchase)
                                            }
                                        }

                                        override fun onError(error: PurchasesError) {
                                            restoring = false
                                            message = context.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.restore_failed))
                                        }
                                    })
                                }
                            },
                            enabled = !purchaseBusy,
                            shape = CircleShape,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                        ) {
                            Text(if (restoring) stringResource(R.string.restoring) else stringResource(R.string.restore_purchases))
                        }
                    }
                }
            }
            item {
                OutlinedButton(
                    onClick = onStore,
                    enabled = !purchaseBusy,
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                ) {
                    Icon(Icons.Rounded.Diamond, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.buy_diamonds_only))
                }
            }
        }
    }
}

@Composable
private fun PaywallPricesFallback(
    message: String,
    retrying: Boolean,
    onRetry: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(shape = CircleShape, color = StudioGold.copy(alpha = 0.18f)) {
                Icon(Icons.Rounded.Refresh, contentDescription = null, tint = StudioGold, modifier = Modifier.padding(9.dp).size(18.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(stringResource(R.string.prices_unavailable), color = Color.White, fontWeight = FontWeight.Black)
                Text(message, color = Color.White.copy(alpha = 0.74f), style = MaterialTheme.typography.bodySmall)
            }
            OutlinedButton(
                onClick = onRetry,
                enabled = !retrying,
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(if (retrying) stringResource(R.string.loading_ellipsis) else stringResource(R.string.retry), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PlanChoiceButton(
    title: String,
    price: String?,
    loading: Boolean,
    enabled: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = when {
        !enabled -> Color.White.copy(alpha = 0.62f)
        selected -> StudioInk
        else -> Color.White
    }
    val accessibilityPrice = when {
        loading -> stringResource(R.string.loading_ellipsis)
        price != null -> price
        else -> stringResource(R.string.unavailable)
    }
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        color = when {
            selected -> StudioGold
            enabled -> Color.White.copy(alpha = 0.12f)
            else -> Color.White.copy(alpha = 0.06f)
        },
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                selected -> StudioGold
                enabled -> Color.White.copy(alpha = 0.18f)
                else -> Color.White.copy(alpha = 0.10f)
            },
        ),
        modifier = modifier
            .minimumTouchTarget()
            .semantics {
                contentDescription = "$title, $accessibilityPrice"
                this.selected = selected
                role = Role.Button
            }
            .disabledSemantics(enabled),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(title, color = contentColor, fontWeight = FontWeight.Black, maxLines = 1)
            Text(
                when {
                    loading -> stringResource(R.string.loading_ellipsis)
                    price != null -> price
                    else -> stringResource(R.string.unavailable)
                },
                color = contentColor.copy(alpha = if (selected) 0.76f else 0.72f),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun FeatureRowOnDark(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f)) {
            Icon(icon, contentDescription = null, tint = StudioGold, modifier = Modifier.padding(8.dp).size(18.dp))
        }
        Text(text, color = Color.White.copy(alpha = 0.86f), fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AuthSheet(
    onClose: () -> Unit,
    onAuth: () -> Unit,
) {
    val modalTapBlocker = remember { MutableInteractionSource() }
    Box(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .clickable(
                interactionSource = modalTapBlocker,
                indication = null,
                onClick = {},
            ),
    ) {
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd).windowInsetsPadding(WindowInsets.statusBars).padding(16.dp).size(48.dp)) {
            Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close))
        }
        Column(
            Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(shape = CircleShape, color = StudioPrimaryContainer, tonalElevation = 5.dp) {
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(18.dp).size(38.dp), tint = StudioInk)
            }
            Spacer(Modifier.height(20.dp))
            Text(stringResource(R.string.auth_welcome), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(
                stringResource(R.string.auth_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Spacer(Modifier.height(22.dp))
            ElevatedCard(shape = RoundedCornerShape(18.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioMist)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedButton(onClick = onAuth, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                        Text(stringResource(R.string.google_initial), color = Color(0xFF4285F4), fontWeight = FontWeight.Black)
                        Spacer(Modifier.width(10.dp))
                        Text(stringResource(R.string.continue_with_google))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.weight(1f).height(1.dp).background(StudioLine))
                        Text(stringResource(R.string.or), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Box(Modifier.weight(1f).height(1.dp).background(StudioLine))
                    }
                    Button(
                        onClick = onAuth,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                    ) {
                        Text(stringResource(R.string.sign_in))
                    }
                    Text(
                        stringResource(R.string.auth_real_flow_note),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onStore: () -> Unit,
    onSubscription: (String, String, String, Double?, Double?) -> Unit,
    onRetrySync: () -> Unit,
    onFeedback: (String) -> Unit,
    onDeleteAccount: () -> Unit,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val modalTapBlocker = remember { MutableInteractionSource() }
    var languagePickerVisible by remember { mutableStateOf(false) }
    var feedbackDialogVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }
    var restoring by remember { mutableStateOf(false) }
    var settingsMessage by remember { mutableStateOf<String?>(null) }
    val restoreEnabled = Purchases.isConfigured && !restoring && !state.settingsBusy && !state.purchaseBusy
    fun setLinkFailureMessage(opened: Boolean) {
        if (!opened) {
            settingsMessage = context.getString(R.string.open_link_failed)
        }
    }
    fun restorePurchases() {
        if (!Purchases.isConfigured) {
            settingsMessage = context.getString(R.string.restore_unavailable)
            return
        }
        restoring = true
        settingsMessage = null
        Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
            override fun onReceived(customerInfo: CustomerInfo) {
                restoring = false
                val active = customerInfo.entitlements.active.values.firstOrNull()
                if (active != null) {
                    settingsMessage = context.getString(R.string.pro_syncing)
                    onSubscription(
                        "pro",
                        if (active.identifier.contains("annual")) "yearly" else "monthly",
                        active.identifier,
                        active.latestPurchaseDate.time.toDouble(),
                        active.expirationDate?.time?.toDouble(),
                    )
                } else {
                    settingsMessage = context.getString(R.string.no_active_pro_purchase)
                }
            }

            override fun onError(error: PurchasesError) {
                restoring = false
                settingsMessage = context.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.restore_failed))
            }
        })
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .clickable(
                interactionSource = modalTapBlocker,
                indication = null,
                onClick = {},
            ),
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
                }
                Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, modifier = Modifier.weight(1f))
            }
            LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val actionMessage = (settingsMessage ?: state.settingsMessage ?: state.purchaseMessage)?.takeIf { it.isNotBlank() }
                if (actionMessage != null) {
                    item {
                        if (state.pendingPurchaseSync != null && settingsMessage == null && state.settingsMessage == null) {
                            PurchaseSyncNotice(
                                message = actionMessage,
                                pending = true,
                                busy = state.purchaseBusy,
                                onRetry = onRetrySync,
                            )
                        } else {
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = StudioMist,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    actionMessage,
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }
                item {
                    SettingsRow(
                        Icons.Rounded.Language,
                        stringResource(R.string.language),
                        AppLocale.labelFor(currentLanguageTag),
                        onClick = { languagePickerVisible = true },
                    )
                }
                item { SettingsRow(Icons.Rounded.RateReview, stringResource(R.string.feedback), stringResource(R.string.feedback_subtitle), enabled = !state.settingsBusy, onClick = { feedbackDialogVisible = true }) }
                item { SettingsRow(Icons.AutoMirrored.Rounded.Help, stringResource(R.string.faq), stringResource(R.string.faq_subtitle), onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/faq"))) }) }
                item {
                    SettingsRow(
                        Icons.Rounded.Star,
                        stringResource(R.string.restore_purchases),
                        when {
                            restoring -> stringResource(R.string.restoring)
                            !Purchases.isConfigured -> stringResource(R.string.restore_unavailable)
                            else -> stringResource(R.string.restore_purchases_subtitle)
                        },
                        enabled = restoreEnabled,
                        onClick = { restorePurchases() },
                    )
                }
                item { SettingsRow(Icons.Rounded.Diamond, stringResource(R.string.diamond_store), stringResource(R.string.diamond_store_subtitle), enabled = !state.settingsBusy, onClick = onStore) }
                item {
                    SettingsRow(Icons.Rounded.Share, stringResource(R.string.share_app), stringResource(R.string.share_app_subtitle), onClick = {
                        val shareText = context.getString(R.string.share_app_share_text, appUrl(""))
                        setLinkFailureMessage(shareTextSafely(context, shareText))
                    })
                }
                item { SettingsRow(Icons.Rounded.Policy, stringResource(R.string.terms), stringResource(R.string.terms_subtitle), onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/terms"))) }) }
                item { SettingsRow(Icons.Rounded.Policy, stringResource(R.string.privacy_policy), stringResource(R.string.privacy_subtitle), onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/privacy"))) }) }
                item { SettingsRow(Icons.Rounded.Delete, stringResource(R.string.delete_account), stringResource(R.string.delete_account_subtitle), enabled = !state.settingsBusy, onClick = { deleteDialogVisible = true }) }
            }
        }
    }
    if (languagePickerVisible) {
        LanguagePickerDialog(
            currentLanguageTag = currentLanguageTag,
            onLanguageSelected = { languageTag ->
                onLanguageSelected(languageTag)
                val localizedContext = AppLocale.wrap(context, languageTag)
                Toast.makeText(
                    localizedContext,
                    localizedContext.getString(R.string.toast_language_selected),
                    Toast.LENGTH_LONG,
                ).show()
                languagePickerVisible = false
            },
            onDismiss = { languagePickerVisible = false },
        )
    }
    if (feedbackDialogVisible) {
        FeedbackDialog(
            busy = state.settingsBusy,
            onSubmit = { message ->
                onFeedback(message)
                feedbackDialogVisible = false
            },
            onDismiss = { feedbackDialogVisible = false },
        )
    }
    if (deleteDialogVisible) {
        DeleteAccountDialog(
            busy = state.settingsBusy,
            onConfirm = {
                deleteDialogVisible = false
                onDeleteAccount()
            },
            onDismiss = { deleteDialogVisible = false },
        )
    }
}

@Composable
private fun FeedbackDialog(
    busy: Boolean,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var message by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { if (!busy) onDismiss() },
        title = { Text(stringResource(R.string.feedback_dialog_title)) },
        text = {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text(stringResource(R.string.feedback_hint)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 128.dp),
                enabled = !busy,
                minLines = 4,
            )
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(message) },
                enabled = !busy && message.trim().length >= 3,
                shape = CircleShape,
            ) {
                Text(if (busy) stringResource(R.string.feedback_sending) else stringResource(R.string.feedback_send))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, enabled = !busy, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
private fun DeleteAccountDialog(
    busy: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { if (!busy) onDismiss() },
        title = { Text(stringResource(R.string.delete_account_title)) },
        text = { Text(stringResource(R.string.delete_account_body)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !busy,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = StudioRose),
            ) {
                Text(if (busy) stringResource(R.string.deleting_account) else stringResource(R.string.delete_account_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, enabled = !busy, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
private fun LanguagePickerDialog(
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.language_picker_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppLocale.supportedLanguages.forEach { language ->
                    val selected = language.tag == currentLanguageTag
                    Surface(
                        color = if (selected) StudioPrimaryContainer else Color.Transparent,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ListItem(
                            headlineContent = { Text(language.label, fontWeight = FontWeight.Bold) },
                            trailingContent = {
                                if (selected) {
                                    Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioBlue)
                                }
                            },
                            modifier = Modifier
                                .minimumTouchTarget()
                                .semantics { this.selected = selected }
                                .clickable(role = Role.Button) { onLanguageSelected(language.tag) },
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

private fun appUrl(path: String): String {
    val base = BuildConfig.APP_URL.trim().trimEnd('/').ifBlank { "https://homedecor.ai" }
    return if (path.isBlank()) base else base + path
}

private fun openUrlSafely(context: android.content.Context, url: String): Boolean {
    return runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }.isSuccess
}

private fun shareTextSafely(context: android.content.Context, text: String): Boolean {
    return runCatching {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_app_chooser)))
    }.isSuccess
}

private fun openUrl(context: android.content.Context, url: String) {
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }.onFailure {
        showToast(context, context.getString(R.string.open_link_failed))
    }
}

private tailrec fun android.content.Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
private fun DiamondStoreSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onFulfill: (String, String, String, String?, Double, String, Double) -> Unit,
    onRetrySync: () -> Unit,
) {
    val context = LocalContext.current
    val scrimTapBlocker = remember { MutableInteractionSource() }
    val sheetTapBlocker = remember { MutableInteractionSource() }
    var packages by remember { mutableStateOf<List<Package>>(emptyList()) }
    var storeLoading by remember { mutableStateOf(true) }
    var loadAttempt by remember { mutableStateOf(0) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var loadingPack by remember { mutableStateOf<String?>(null) }
    var syncingPack by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(state.purchaseBusy) {
        if (!state.purchaseBusy) syncingPack = null
    }
    LaunchedEffect(loadAttempt) {
        storeLoading = true
        loadError = null
        message = null
        packages = emptyList()
        if (!Purchases.isConfigured) {
            storeLoading = false
            loadError = context.getString(R.string.store_purchases_unavailable)
            return@LaunchedEffect
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                packages = ((offerings.current?.availablePackages ?: emptyList()) + offerings.all.values.flatMap { it.availablePackages })
                    .distinctBy { "${it.identifier}:${it.product.id}" }
                storeLoading = false
                if (packages.isEmpty()) {
                    loadError = context.getString(R.string.store_no_packs)
                }
            }

            override fun onError(error: PurchasesError) {
                storeLoading = false
                loadError = context.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.store_load_packs_failed))
            }
        })
    }
    val mappedPackages = remember(packages) {
        HomeDecorCatalog.diamondPacks.associate { pack ->
            val matches = packages.filter { it.matchesDiamondPack(pack) }
            pack.id to matches.singleOrNull()
        }
    }
    val mappedProductCounts = mappedPackages.values.filterNotNull().groupingBy { it.product.id }.eachCount()
    fun packageFor(pack: DiamondPack): Package? {
        val productPackage = mappedPackages[pack.id] ?: return null
        return productPackage.takeIf { mappedProductCounts[it.product.id] == 1 }
    }
    fun buy(pack: DiamondPack) {
        val productPackage = packageFor(pack)
        val activity = context.findActivity()
        if (storeLoading || loadingPack != null || state.purchaseBusy) return
        if (productPackage == null || activity == null || !Purchases.isConfigured) {
            message = context.getString(R.string.pack_unavailable_google)
            return
        }
        loadingPack = pack.id
        syncingPack = null
        message = null
        Purchases.sharedInstance.purchase(PurchaseParams.Builder(activity, productPackage).build(), object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                loadingPack = null
                syncingPack = pack.id
                message = context.getString(R.string.purchase_google_confirmed)
                onFulfill(
                    pack.id,
                    storeTransaction.orderId ?: storeTransaction.purchaseToken,
                    productPackage.product.id,
                    productPackage.identifier,
                    productPackage.product.price.amountMicros / 1_000_000.0,
                    productPackage.product.price.currencyCode,
                    storeTransaction.purchaseTime.toDouble(),
                )
            }

            override fun onError(error: PurchasesError, userCancelled: Boolean) {
                loadingPack = null
                syncingPack = null
                message = if (userCancelled) {
                    context.getString(R.string.purchase_cancelled)
                } else {
                    context.getString(rawServiceMessageToKind(context, error.message).purchaseAttemptMessageRes(R.string.purchase_failed))
                }
            }
        })
    }
    val hasPartialMapping = !storeLoading && loadError == null && packages.isNotEmpty() && HomeDecorCatalog.diamondPacks.any { packageFor(it) == null }
    val notice = when {
        loadError != null -> loadError
        message != null -> message
        hasPartialMapping -> stringResource(R.string.store_some_packs_unavailable)
        else -> state.purchaseMessage
    }
    val closeDescription = stringResource(R.string.close)
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.34f))
            .clickable(
                interactionSource = scrimTapBlocker,
                indication = null,
                onClick = onClose,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = StudioCanvas,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = sheetTapBlocker,
                    indication = null,
                    onClick = {},
                ),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    val closeDescription = stringResource(R.string.close)
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            Modifier
                                .minimumTouchTarget()
                                .semantics {
                                    contentDescription = closeDescription
                                    role = Role.Button
                                }
                                .clickable(role = Role.Button) { onClose() },
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                Modifier
                                    .width(44.dp)
                                    .height(5.dp)
                                    .clip(CircleShape)
                                    .background(StudioLine),
                            )
                        }
                    }
                }
                item {
                    ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
                        Row(
                            Modifier.fillMaxWidth().padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                                Icon(Icons.Rounded.Diamond, null, Modifier.padding(12.dp).size(26.dp), tint = StudioBlue)
                            }
                            Column(Modifier.weight(1f)) {
                                Text(stringResource(R.string.current_balance), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                                Text(stringResource(R.string.diamonds_amount, state.diamonds), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = onClose) {
                                Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close))
                            }
                        }
                    }
                }
                item {
                    Text(stringResource(R.string.get_more_credits), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
                if (storeLoading) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = StudioBlue, strokeWidth = 2.dp)
                            Text(stringResource(R.string.loading_packs), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                if (notice != null) {
                    item {
                        if (state.pendingPurchaseSync != null && loadError == null && message == null) {
                            PurchaseSyncNotice(
                                message = notice.orEmpty(),
                                pending = true,
                                busy = state.purchaseBusy,
                                onRetry = onRetrySync,
                            )
                        } else {
                            Text(notice.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                if (loadError != null) {
                    item {
                        OutlinedButton(
                            onClick = { loadAttempt += 1 },
                            enabled = !storeLoading && loadingPack == null && !state.purchaseBusy,
                            shape = CircleShape,
                        ) {
                            Icon(Icons.Rounded.Refresh, null, Modifier.size(17.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                items(HomeDecorCatalog.diamondPacks, key = { it.id }) { pack ->
                    val productPackage = packageFor(pack)
                    val purchaseBlocked = loadingPack != null || state.purchaseBusy
                    DiamondPackRow(
                        pack = pack.copy(price = productPackage?.product?.price?.formatted ?: stringResource(R.string.unavailable)),
                        unavailable = !storeLoading && productPackage == null,
                        loading = storeLoading || loadingPack == pack.id || syncingPack == pack.id,
                        purchaseBlocked = purchaseBlocked,
                        onClick = { buy(pack) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DiamondPackRow(
    pack: DiamondPack,
    unavailable: Boolean = false,
    loading: Boolean = false,
    purchaseBlocked: Boolean = false,
    onClick: () -> Unit,
) {
    val packTitle = stringResource(diamondPackTitleRes(pack))
    val packBadge = diamondPackBadgeRes(pack)?.let { stringResource(it) }
    val packDescription = stringResource(diamondPackDescriptionRes(pack))
    val enabled = !loading && !unavailable && !purchaseBlocked
    val titleColor = if (unavailable) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f) else StudioInk
    val bodyColor = if (unavailable) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f) else MaterialTheme.colorScheme.onSurfaceVariant
    ElevatedCard(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = StudioPaper,
            disabledContainerColor = if (unavailable) StudioMist.copy(alpha = 0.78f) else StudioPaper,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .border(1.dp, if (unavailable) StudioRose.copy(alpha = 0.28f) else StudioLine.copy(alpha = 0.5f), RoundedCornerShape(22.dp)),
    ) {
        Row(
            Modifier.fillMaxSize().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(shape = CircleShape, color = if (unavailable) StudioMist else StudioPrimaryContainer) {
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(10.dp).size(22.dp), tint = if (unavailable) bodyColor else StudioBlue)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.pack_title, packTitle), color = titleColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (packBadge != null) {
                    Surface(shape = CircleShape, color = StudioErrorContainer) {
                        Text(
                            packBadge,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            color = StudioRose,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Text(stringResource(R.string.diamonds_amount, pack.diamonds), color = bodyColor)
                if (unavailable) {
                    Text(stringResource(R.string.pack_not_available_in_store), color = StudioRose, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                } else if (packDescription.isNotBlank()) {
                    Text(packDescription, color = bodyColor, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Button(
                onClick = onClick,
                enabled = enabled,
                shape = CircleShape,
                colors = studioPrimaryButtonColors(),
                modifier = Modifier.widthIn(min = 106.dp).height(48.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
            ) {
                Text(
                    when {
                        loading -> stringResource(R.string.ellipsis)
                        unavailable -> stringResource(R.string.unavailable)
                        else -> pack.price
                    },
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun FirstLaunchDisclosure(onAccept: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.disclosure_title)) },
        text = {
            Text(stringResource(R.string.disclosure_body))
        },
        confirmButton = {
            Button(onClick = onAccept, shape = CircleShape) {
                Text(stringResource(R.string.understood))
            }
        },
    )
}

private fun wizardStepNumber(stage: WizardStage, tool: DecorTool? = null): Int {
    return when (stage) {
        WizardStage.Photo -> 1
        WizardStage.Space -> 2
        WizardStage.Style -> if (tool?.id == "reference") 2 else 3
        WizardStage.Refine -> if (tool?.id in listOf("reference", "layout")) 2 else if (tool?.id in listOf("garden", "paint")) 3 else 4
        WizardStage.Processing -> wizardTotalSteps(tool)
        WizardStage.Result -> wizardTotalSteps(tool)
    }
}

private fun wizardTotalSteps(tool: DecorTool?): Int {
    return when (tool?.id) {
        "garden" -> 3
        "reference" -> 2
        "layout" -> 2
        else -> 4
    }
}

private data class ExamplePhoto(
    val label: String,
    @StringRes val labelRes: Int,
    val imageRes: Int,
)

private fun examplesForTool(tool: DecorTool): List<ExamplePhoto> {
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

private fun selectedExampleImageRes(state: HomeDecorUiState): Int {
    val selectedLabel = state.selectedPhotos.firstOrNull()?.exampleLabel ?: state.selectedExampleLabel
    val selected = examplesForTool(state.selectedTool).firstOrNull { it.label == selectedLabel }
    return selected?.imageRes ?: examplesForTool(state.selectedTool).first().imageRes
}

private fun selectedPhotoImageRes(state: HomeDecorUiState, slot: SelectedPhoto): Int {
    val selected = examplesForTool(state.selectedTool).firstOrNull { it.label == slot.exampleLabel }
    return selected?.imageRes ?: selectedExampleImageRes(state)
}

private fun choiceImageRes(label: String): Int {
    return when (label) {
        "Luxe" -> R.drawable.assets_media_styles_styleluxury
        "Japandi" -> R.drawable.assets_media_styles_stylejapandi
        "Cyberpunk" -> R.drawable.assets_media_styles_stylecyberpunk
        "Tropicale" -> R.drawable.assets_media_styles_styletropical
        "Minimaliste" -> R.drawable.assets_media_styles_styleminimalist
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

private fun paletteColors(label: String): List<Color> {
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
