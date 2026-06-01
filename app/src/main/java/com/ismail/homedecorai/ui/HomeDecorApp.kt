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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.content.FileProvider
import androidx.core.content.ContextCompat
import com.ismail.homedecorai.BoardItem
import com.ismail.homedecorai.AppLocale
import com.ismail.homedecorai.BuildConfig
import com.ismail.homedecorai.DecorTool
import com.ismail.homedecorai.DiamondPack
import com.ismail.homedecorai.DiscoverSection
import com.ismail.homedecorai.FavoriteItem
import com.ismail.homedecorai.GalleryItem
import com.ismail.homedecorai.GeneratedResult
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.MainTab
import com.ismail.homedecorai.MaskPoint
import com.ismail.homedecorai.MaskStroke
import com.ismail.homedecorai.MoodboardItem
import com.ismail.homedecorai.Project
import com.ismail.homedecorai.R
import com.ismail.homedecorai.SelectedPhoto
import com.ismail.homedecorai.WizardStage
import com.ismail.homedecorai.hasVisibleMaskPaint
import com.ismail.homedecorai.isValidReplacementPrompt
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.DateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

private object HomeDecorColors {
    val Ink = Color(0xFF19140D)
    val InkSoft = Color(0xFF5F5648)
    val Canvas = Color(0xFFF6F1E9)
    val Paper = Color(0xFFFFFCF7)
    val Mist = Color(0xFFEDE5D8)
    val Line = Color(0xFFD9CBB8)
    val Secondary = Color(0xFF6A604D)
    val Accent = Color(0xFF8A5A18)
    val AccentContainer = Color(0xFFFFE7BA)
    val ProContainer = Color(0xFFFFF0D1)
    val PremiumGold = Color(0xFFD2AA5A)
    val GoldDeep = Color(0xFFB88A3A)
    val Dark = Color(0xFF070706)
    val DarkSurface = Color(0xFF11100D)
    val DarkOverlay = Color(0xFF4A3522)
    val DisabledDarkButton = Color(0xFF2B261D)
    val DisabledDarkText = Color(0xFFC8BDAA)
    val Error = Color(0xFFB3261E)
    val ErrorContainer = Color(0xFFFFEDEA)
    val Success = Color(0xFF9BC489)
}

private val StudioInk = HomeDecorColors.Ink
private val StudioAccent = HomeDecorColors.Accent
private val StudioBlue = StudioAccent
private val StudioGreen = StudioAccent
private val StudioMoss = HomeDecorColors.Secondary
private val StudioRose = HomeDecorColors.Error
private val StudioCanvas = HomeDecorColors.Canvas
private val StudioPaper = HomeDecorColors.Paper
private val StudioMist = HomeDecorColors.Mist
private val StudioLine = HomeDecorColors.Line
private val StudioBlack = HomeDecorColors.DarkSurface
private val StudioGold = HomeDecorColors.GoldDeep
private val StudioSky = StudioAccent
private val StudioViolet = StudioAccent
private val StudioPrimaryContainer = HomeDecorColors.AccentContainer
private val StudioProContainer = HomeDecorColors.ProContainer
private val StudioErrorContainer = HomeDecorColors.ErrorContainer
private val PaywallBg = HomeDecorColors.Dark
private val PaywallAccent = HomeDecorColors.GoldDeep
private val PaywallPremiumGold = HomeDecorColors.PremiumGold
private val PaywallCard = Color(0x14FFFAEE)
private val PaywallCardAlt = Color(0x0CFFFAEE)
private val PaywallBorder = Color(0x29EFDDB8)
private val PaywallTextSecondary = Color(0xDDF6EFE0)
private val PaywallTextMuted = Color(0xB8F6EFE0)
private val PaywallDisabledButton = HomeDecorColors.DisabledDarkButton
private val PaywallDisabledText = HomeDecorColors.DisabledDarkText
private val PaywallSuccess = HomeDecorColors.Success

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
private fun localizedOption(label: String): String = optionLabelRes(label)?.let { stringResource(it) } ?: label

@Composable
private fun localizedReplacementPrompt(prompt: String): String =
    prompt.takeIf { it.isNotBlank() }?.let { localizedOption(it) }.orEmpty()

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
private fun discoverSectionSubtitleRes(section: DiscoverSection): Int = when (section.id) {
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

private fun discoverItemById(itemId: String): GalleryItem? =
    HomeDecorCatalog.discoverSections
        .asSequence()
        .flatMap { it.items.asSequence() }
        .firstOrNull { it.id == itemId }

private fun discoverSource(item: GalleryItem): String = "discover:${item.id}"

@Composable
private fun localizedDiscoverCluster(cluster: String): String = stringResource(discoverClusterRes(cluster))

@Composable
private fun localizedDiscoverSection(section: DiscoverSection): String = stringResource(discoverSectionTitleRes(section))

@Composable
private fun localizedDiscoverSectionSubtitle(section: DiscoverSection): String = stringResource(discoverSectionSubtitleRes(section))

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
            onSurfaceVariant = HomeDecorColors.InkSoft,
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
    disabledContentColor = HomeDecorColors.InkSoft,
)

@Composable
private fun studioProButtonColors() = ButtonDefaults.buttonColors(
    containerColor = StudioGold,
    contentColor = StudioInk,
    disabledContainerColor = PaywallDisabledButton,
    disabledContentColor = PaywallDisabledText,
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
                    MainTab.Discover -> DiscoverScreen(state = state, viewModel = viewModel)
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
                        onDailyRewardClaim = viewModel::claimLocalDailyReward,
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
                        onSubscription = viewModel::syncSubscriptionFromRevenueCat,
                        onRetrySync = viewModel::retryPurchaseSync,
                        onFeedback = viewModel::submitSettingsFeedback,
                        onDeleteAccount = viewModel::deleteAccountData,
                        onLogout = viewModel::logOut,
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
    BackHandler(enabled = true) {
        if (state.wizardStage == WizardStage.Photo) {
            viewModel.selectTab(MainTab.Tools)
        } else {
            viewModel.previousStage()
        }
    }
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
                            state = state,
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
                        state = state,
                        eyebrow = stringResource(R.string.step_count_format, 3, wizardTotalSteps(state.selectedTool)),
                        copy = stepThreeCopy(state.selectedTool),
                        selected = state.selectedStyles,
                        onSelect = viewModel::setStyle,
                        onContinue = viewModel::nextStage,
                        visualStyleCards = state.selectedTool.id in listOf("interior", "facade", "garden", "floor"),
                    )
                }
                WizardStage.Refine -> RefineStep(state = state, viewModel = viewModel)
                WizardStage.Processing -> ProcessingStep(
                    state = state,
                    message = state.progressMessage,
                )
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
    validationMessage: String? = null,
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
                Modifier.fillMaxWidth().heightIn(min = 58.dp)
            } else {
                Modifier.fillMaxWidth().height(58.dp)
            }
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!buttonEnabled && !validationMessage.isNullOrBlank()) {
                    ValidationNotice(validationMessage)
                }
                Button(
                    onClick = onButton,
                    enabled = buttonEnabled,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = buttonModifier,
                ) {
                    Icon(buttonIcon, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
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
}

@Composable
private fun ValidationNotice(message: String) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioErrorContainer,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioRose.copy(alpha = 0.24f), RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(Icons.Rounded.Lock, contentDescription = null, modifier = Modifier.size(18.dp), tint = StudioRose)
            Text(
                message,
                modifier = Modifier.weight(1f),
                color = StudioRose,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
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
    val allowExamplePhotos = true
    val isSingleSourceFlow = state.selectedTool.id in setOf("interior", "facade", "garden", "layout", "replace")
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
        validationMessage = if (allowExamplePhotos) {
            stringResource(R.string.validation_add_source_photo)
        } else {
            stringResource(R.string.validation_upload_source_photo)
        },
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
            if (allowExamplePhotos) {
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
                    Text(stringResource(R.string.try_with_example))
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
}

@Composable
private fun ReferenceImagesStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val editorialReference = stringResource(R.string.editorial_reference)
    val roomImageInputActions = rememberImageInputActions { uri ->
        viewModel.setPrimaryPhoto(uri)
    }
    val referenceImageInputActions = rememberImageInputActions { uri ->
        viewModel.setReferencePhoto(uri)
    }
    val roomPhoto = state.selectedPhotos.firstOrNull()
    val hasRoom = roomPhoto != null
    val hasReference = state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null
    val selectedDiscoverReference = state.selectedReferenceDiscoverItemId?.let(::discoverItemById)
    val referenceImageRes = selectedDiscoverReference?.imageRes ?: R.drawable.tool_reference
    val referenceSelectedText = selectedDiscoverReference?.let { localizedGalleryTitle(it) }
        ?: state.selectedReferenceExampleLabel
        ?: stringResource(R.string.reference_added)
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
        validationMessage = missingHint,
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
                selectedText = referenceSelectedText,
                uri = state.selectedReferenceUri,
                imageRes = referenceImageRes,
                contentDescription = stringResource(R.string.reference_image),
                onGallery = referenceImageInputActions.openGallery,
                onCamera = referenceImageInputActions.openCamera,
                onExample = { viewModel.selectReferenceExample(editorialReference) },
            )
            OutlinedButton(
                onClick = viewModel::tryWithExample,
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.try_with_example))
            }
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
private fun SourcePreviewCard(state: HomeDecorUiState) {
    val firstPhoto = state.selectedPhotos.firstOrNull()
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp)),
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(Modifier.size(76.dp).clip(RoundedCornerShape(18.dp)).background(StudioMist)) {
                UriOrResourceImage(
                    uri = firstPhoto?.uri,
                    imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
                    contentDescription = stringResource(R.string.source_photo_preview),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.source_photo_preview), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text(stringResource(R.string.source_photo_preview_body), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioGreen, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun MaskPreviewCard(
    state: HomeDecorUiState,
    title: String,
    body: String,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp), tint = StudioBlue)
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            }
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            MaskPreviewBox(state = state)
        }
    }
}

@Composable
private fun MaskPreviewBox(state: HomeDecorUiState) {
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1.18f)
            .clip(RoundedCornerShape(18.dp))
            .background(StudioMist),
    ) {
        val firstPhoto = state.selectedPhotos.firstOrNull()
        UriOrResourceImage(
            uri = firstPhoto?.uri,
            imageRes = firstPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
            contentDescription = stringResource(R.string.mask_preview_title),
            modifier = Modifier.fillMaxSize(),
        )
        Canvas(Modifier.matchParentSize()) {
            state.maskStrokes.forEach { stroke ->
                val points = stroke.points
                if (points.size >= 2) {
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
            }
        }
    }
}

@Composable
private fun ReferenceStylePreview(state: HomeDecorUiState) {
    val roomPhoto = state.selectedPhotos.firstOrNull()
    val referenceImageRes = state.selectedReferenceDiscoverItemId
        ?.let(::discoverItemById)
        ?.imageRes
        ?: R.drawable.tool_reference
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.reference_preview_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PreviewTile(
                    title = stringResource(R.string.your_room),
                    uri = roomPhoto?.uri,
                    imageRes = roomPhoto?.let { selectedPhotoImageRes(state, it) } ?: selectedExampleImageRes(state),
                    modifier = Modifier.weight(1f),
                )
                PreviewTile(
                    title = stringResource(R.string.reference_image),
                    uri = state.selectedReferenceUri,
                    imageRes = referenceImageRes,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun PreviewTile(
    title: String,
    uri: Uri?,
    imageRes: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.fillMaxWidth().aspectRatio(1.08f).clip(RoundedCornerShape(16.dp)).background(StudioMist)) {
            UriOrResourceImage(
                uri = uri,
                imageRes = imageRes,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                        color = HomeDecorColors.DarkOverlay,
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
    selectedImageRes: Int,
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
                        imageRes = selectedImageRes,
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
    state: HomeDecorUiState,
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
        validationMessage = stringResource(R.string.validation_choose_option_to_continue),
        onButton = onContinue,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SourcePreviewCard(state = state)
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
}

@Composable
private fun ReferencePhotoStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val editorialReference = stringResource(R.string.editorial_reference)
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
                selectedImageRes = state.selectedReferenceDiscoverItemId?.let(::discoverItemById)?.imageRes ?: R.drawable.tool_reference,
                onImport = referenceImageInputActions.openGallery,
                onExample = { viewModel.selectReferenceExample(editorialReference) },
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
                onClick = { viewModel.selectReferenceExample(editorialReference) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.try_with_example))
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
        validationMessage = disabledLabel,
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
            .aspectRatio(1.18f)
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
        Canvas(
            Modifier
                .matchParentSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen },
        ) {
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 24.dp),
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
        item {
            AdvancedControls(state = state, viewModel = viewModel)
        }
        item {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!canGenerate) {
                    ValidationNotice(
                        if (firstPhoto == null) {
                            stringResource(R.string.validation_add_source_photo)
                        } else {
                            stringResource(R.string.validation_select_layout_goal)
                        },
                    )
                }
                Button(
                    onClick = viewModel::generate,
                    enabled = canGenerate,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier
                        .fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth().heightIn(min = 96.dp),
                minLines = 2,
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
            value = state.mobilierASupprimer,
            onValueChange = viewModel::setMobilierASupprimerText,
            label = { Text(stringResource(R.string.furniture_to_remove)) },
            placeholder = { Text(stringResource(R.string.furniture_to_remove_placeholder)) },
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
            label = { Text(stringResource(R.string.optional_notes)) },
            placeholder = { Text(stringResource(R.string.optional_notes_placeholder)) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp),
            minLines = 3,
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
            validationMessage = stringResource(R.string.validation_select_layout_goal),
            onButton = viewModel::generate,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(value = state.roomType, onValueChange = viewModel::setRoomTypeText, label = { Text(stringResource(R.string.room_type)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.style, onValueChange = viewModel::setStyleText, label = { Text(stringResource(R.string.people_count)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.palette, onValueChange = viewModel::setPaletteText, label = { Text(stringResource(R.string.furniture_to_keep)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
                OutlinedTextField(value = state.mobilierASupprimer, onValueChange = viewModel::setMobilierASupprimerText, label = { Text(stringResource(R.string.furniture_to_remove)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), singleLine = true)
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
                AdvancedControls(state = state, viewModel = viewModel)
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
        validationMessage = stringResource(R.string.validation_choose_palette_to_generate),
        onButton = viewModel::generate,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (!state.generationError.isNullOrBlank()) {
                GenerationErrorNotice(
                    message = state.generationError,
                    onRetry = viewModel::generate,
                )
            }
            SourcePreviewCard(state = state)
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
            AdvancedControls(state = state, viewModel = viewModel)
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

private enum class MaterialPattern {
    Vein,
    Wood,
    Concrete,
    Limewash,
    Terrazzo,
    Tile,
    Paint,
}

private data class MaterialSwatchSpec(
    val base: Color,
    val accent: Color,
    val pattern: MaterialPattern,
)

private fun materialSwatchSpec(label: String): MaterialSwatchSpec = when (label) {
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
private fun MaterialLibrarySection(
    options: List<String>,
    selected: List<String>,
    target: String,
    onSelect: (String) -> Unit,
) {
    val selectedMaterial = selected.firstOrNull()
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(R.string.material_library), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Text(
                stringResource(if (target == "floor") R.string.material_library_floor_scope else R.string.material_library_wall_scope),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
        }
        selectedMaterial?.let {
            SelectedMaterialPreview(
                label = it,
                target = target,
            )
        }
        options.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { option ->
                    MaterialSwatchCard(
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

@Composable
private fun SelectedMaterialPreview(
    label: String,
    target: String,
) {
    val displayLabel = localizedOption(label)
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioPrimaryContainer,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioBlue.copy(alpha = 0.26f), RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MaterialSwatchThumb(label = label, selected = true, modifier = Modifier.size(52.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(displayLabel, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioInk)
                Text(
                    stringResource(if (target == "floor") R.string.material_selected_floor_preview else R.string.material_selected_wall_preview),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioBlue, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun MaterialSwatchCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayLabel = localizedOption(label)
    val shape = RoundedCornerShape(18.dp)
    Surface(
        onClick = onClick,
        shape = shape,
        color = studioStateContainer(selected),
        tonalElevation = studioStateElevation(selected),
        modifier = modifier.height(104.dp).border(if (selected) 2.dp else 1.dp, studioStateBorder(selected), shape),
    ) {
        Column(
            Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MaterialSwatchThumb(label = label, selected = selected, modifier = Modifier.fillMaxWidth().height(48.dp))
            Text(
                displayLabel,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = StudioInk,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun MaterialSwatchThumb(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val spec = materialSwatchSpec(label)
    Box(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.linearGradient(listOf(spec.base, spec.base.copy(alpha = 0.82f), spec.accent.copy(alpha = 0.5f))))
            .border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            when (spec.pattern) {
                MaterialPattern.Vein -> {
                    drawLine(spec.accent.copy(alpha = 0.72f), Offset(size.width * 0.12f, size.height * 0.75f), Offset(size.width * 0.88f, size.height * 0.2f), strokeWidth = 2.2f)
                    drawLine(spec.accent.copy(alpha = 0.42f), Offset(size.width * 0.2f, size.height * 0.24f), Offset(size.width * 0.72f, size.height * 0.64f), strokeWidth = 1.4f)
                }
                MaterialPattern.Wood -> {
                    for (index in 1..5) {
                        val y = size.height * index / 6f
                        drawLine(spec.accent.copy(alpha = 0.46f), Offset(0f, y), Offset(size.width, y + if (index % 2 == 0) 8f else -6f), strokeWidth = 2f)
                    }
                }
                MaterialPattern.Concrete, MaterialPattern.Limewash -> {
                    for (index in 0..8) {
                        val x = size.width * ((index * 23) % 100) / 100f
                        val y = size.height * ((index * 37) % 100) / 100f
                        drawCircle(spec.accent.copy(alpha = if (spec.pattern == MaterialPattern.Concrete) 0.2f else 0.12f), radius = 7f + index, center = Offset(x, y))
                    }
                }
                MaterialPattern.Terrazzo -> {
                    val chips = listOf(
                        Offset(size.width * 0.18f, size.height * 0.3f),
                        Offset(size.width * 0.44f, size.height * 0.62f),
                        Offset(size.width * 0.7f, size.height * 0.28f),
                        Offset(size.width * 0.84f, size.height * 0.74f),
                    )
                    chips.forEachIndexed { index, offset ->
                        drawCircle(listOf(spec.accent, Color(0xFFC47A5A), Color(0xFF2D2A26))[index % 3].copy(alpha = 0.72f), radius = 4f + index, center = offset)
                    }
                }
                MaterialPattern.Tile -> {
                    drawLine(spec.accent.copy(alpha = 0.52f), Offset(size.width / 2f, 0f), Offset(size.width / 2f, size.height), strokeWidth = 1.4f)
                    drawLine(spec.accent.copy(alpha = 0.52f), Offset(0f, size.height / 2f), Offset(size.width, size.height / 2f), strokeWidth = 1.4f)
                }
                MaterialPattern.Paint -> Unit
            }
        }
        if (selected) {
            Surface(shape = CircleShape, color = Color.White) {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.padding(5.dp).size(15.dp), tint = StudioBlue)
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
    val hasReplacementPrompt = replacementPrompt.isValidReplacementPrompt()
    val localizedReplacement = localizedReplacementPrompt(replacementPrompt)
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
    val disabledReason = when {
        requiresMask && !hasRequiredMask -> when (state.selectedTool.id) {
            "floor" -> stringResource(R.string.validation_mark_floor_to_generate)
            "replace" -> stringResource(R.string.validation_mark_object_to_generate)
            else -> stringResource(R.string.validation_mark_wall_to_generate)
        }
        state.selectedTool.id == "reference" && !hasReferenceImages -> stringResource(R.string.reference_missing_error)
        state.selectedTool.id == "reference" && selected.isEmpty() -> stringResource(R.string.validation_choose_transfer_strength)
        state.selectedTool.id == "replace" && !hasReplacementPrompt -> stringResource(R.string.replacement_prompt_required_error)
        state.selectedTool.id == "paint" && selected.isEmpty() && state.customPrompt.isBlank() -> stringResource(R.string.validation_choose_color_or_prompt)
        state.selectedTool.id == "floor" && selected.isEmpty() && state.customPrompt.isBlank() -> stringResource(R.string.validation_choose_material_or_prompt)
        else -> null
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
        validationMessage = disabledReason,
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
            if (state.selectedTool.id == "reference") {
                ReferenceStylePreview(state = state)
            } else {
                SourcePreviewCard(state = state)
            }
            if (state.selectedTool.id in setOf("paint", "floor")) {
                val maskLabel = if (state.selectedTool.id == "floor") {
                    stringResource(R.string.mask_floor_marked)
                } else {
                    stringResource(R.string.mask_wall_marked)
                }
                MaskPreviewCard(
                    state = state,
                    title = stringResource(R.string.mask_preview_title),
                    body = stringResource(R.string.mask_preview_body),
                )
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
            if (state.selectedTool.id == "replace") {
                MaskPreviewCard(
                    state = state,
                    title = stringResource(R.string.mask_preview_title),
                    body = stringResource(R.string.mask_preview_body),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (state.selectedTool.id in setOf("paint", "floor")) {
                    MaterialLibrarySection(
                        options = stepCopy.options,
                        selected = selected,
                        target = if (state.selectedTool.id == "floor") "floor" else "wall",
                        onSelect = viewModel::setStyle,
                    )
                } else {
                if (state.selectedTool.id == "replace") {
                    Text(stringResource(R.string.replacement_suggestions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                }
                    stepCopy.options.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            row.forEach { option ->
                                if (state.selectedTool.id == "replace") {
                                    val optionPrompt = localizedOption(option)
                                    val templatePrompt = HomeDecorCatalog.replacementTemplatePrompts[option].orEmpty()
                                    ReplaceSuggestionChip(
                                        label = option,
                                        selected = option in selected ||
                                            optionPrompt in selected ||
                                            templatePrompt in selected ||
                                            replacementPrompt == option ||
                                            replacementPrompt == optionPrompt ||
                                            replacementPrompt == templatePrompt,
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
                label = when (state.selectedTool.id) {
                    "replace" -> {
                        { Text(stringResource(R.string.replacement_object)) }
                    }
                    "reference" -> {
                        { Text(stringResource(R.string.custom_notes)) }
                    }
                    else -> null
                },
                placeholder = {
                    Text(
                        when (state.selectedTool.id) {
                            "paint" -> stringResource(R.string.prompt_paint)
                            "floor" -> stringResource(R.string.prompt_floor)
                            "replace" -> stringResource(R.string.prompt_replace)
                            "reference" -> stringResource(R.string.custom_notes_placeholder)
                            else -> stringResource(R.string.prompt_optional)
                        },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = if (state.selectedTool.id in setOf("replace", "reference")) 4 else 3,
                shape = RoundedCornerShape(18.dp),
                isError = state.selectedTool.id == "replace" && state.customPrompt.isNotBlank() && !hasReplacementPrompt,
                supportingText = if (state.selectedTool.id == "replace") {
                    {
                        Text(
                            if (state.customPrompt.isBlank() || hasReplacementPrompt) {
                                stringResource(R.string.describe_new_object)
                            } else {
                                stringResource(R.string.replacement_prompt_required_error)
                            },
                        )
                    }
                } else {
                    null
                },
            )
            if (state.selectedTool.id == "replace") {
                ReplacementReadinessSummary(
                    hasMask = hasRequiredMask,
                    hasReplacementPrompt = hasReplacementPrompt,
                    replacementPrompt = localizedReplacement,
                )
            }
            AdvancedControls(state = state, viewModel = viewModel)
        }
    }
}

@Composable
private fun AdvancedControls(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val spec = HomeDecorCatalog.advancedControlSpecs[state.selectedTool.id]
    val protectionOnly = state.selectedTool.id in HomeDecorCatalog.protectRestToolIds
    val recentStyles = remember(state.workspace.recentStyles, state.selectedTool.id) {
        state.workspace.recentStyles
            .asSequence()
            .filter { it.toolId == state.selectedTool.id && it.style.isNotBlank() }
            .sortedByDescending { it.lastUsedAt }
            .distinctBy { it.style.trim().lowercase() }
            .take(6)
            .map { it.style }
            .toList()
    }
    var expanded by remember(state.selectedTool.id) { mutableStateOf(false) }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(20.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(Icons.Rounded.Settings, contentDescription = null, tint = StudioBlue, modifier = Modifier.size(20.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(stringResource(R.string.advanced_controls), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.advanced_controls_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                Icon(
                    if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (expanded) {
                if (spec != null) {
                    AdvancedOptionGroup(
                        title = stringResource(R.string.keep_controls_title),
                        options = spec.keepOptions,
                        selectedOptions = state.keepOptions,
                        onToggle = viewModel::toggleKeepOption,
                    )
                    AdvancedOptionGroup(
                        title = stringResource(R.string.change_controls_title),
                        options = spec.changeOptions,
                        selectedOptions = state.changeOptions,
                        onToggle = viewModel::toggleChangeOption,
                    )
                }
                if (protectionOnly) {
                    CompactFilterChip(
                        label = stringResource(R.string.preserve_rest_of_image),
                        selected = state.preserveRestOfImage,
                        onClick = viewModel::togglePreserveRestOfImage,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                if (!protectionOnly) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.budget_mode), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            HomeDecorCatalog.budgetModes.forEach { mode ->
                                    CompactFilterChip(
                                        label = localizedAdvancedOption(mode),
                                        selected = state.budgetMode == mode,
                                        onClick = { viewModel.setBudgetMode(mode) },
                                        modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.avoid_these), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        HomeDecorCatalog.avoidOptions.chunked(2).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { option ->
                                    CompactFilterChip(
                                        label = localizedAdvancedOption(option),
                                        selected = option in state.avoidOptions,
                                        onClick = { viewModel.toggleAvoidOption(option) },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                                if (row.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                    if (recentStyles.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(stringResource(R.string.recent_styles), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(recentStyles, key = { "recent-style-$it" }) { style ->
                                    CompactFilterChip(
                                        label = localizedOption(style),
                                        selected = style in state.selectedStyles || state.style == style || state.customPrompt == style,
                                        onClick = {
                                            when (state.selectedTool.id) {
                                                "replace" -> viewModel.setCustomPrompt(style)
                                                "layout" -> viewModel.setStyleText(style)
                                                else -> viewModel.setStyle(style)
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = viewModel::tryWithExample,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.try_with_example), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdvancedOptionGroup(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onToggle: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        options.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { option ->
                    CompactFilterChip(
                        label = localizedAdvancedOption(option),
                        selected = option in selectedOptions,
                        onClick = { onToggle(option) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun localizedAdvancedOption(label: String): String = when (label) {
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

@Composable
private fun CompactFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingIcon = if (selected) {
            {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        } else {
            null
        },
        modifier = modifier.heightIn(min = 44.dp),
    )
}

@Composable
private fun ReplacementReadinessSummary(
    hasMask: Boolean,
    hasReplacementPrompt: Boolean,
    replacementPrompt: String,
) {
    val allReady = hasMask && hasReplacementPrompt
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (allReady) StudioPrimaryContainer else StudioMist.copy(alpha = 0.72f),
        modifier = Modifier.fillMaxWidth().border(
            1.dp,
            if (allReady) StudioBlue.copy(alpha = 0.36f) else StudioLine,
            RoundedCornerShape(22.dp),
        ),
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                stringResource(R.string.replacement_confirmation_title),
                color = if (allReady) StudioBlue else StudioInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
            )
            ReplacementSummaryLine(
                checked = hasMask,
                text = if (hasMask) {
                    stringResource(R.string.replacement_confirmation_mask_ready)
                } else {
                    stringResource(R.string.replacement_confirmation_need_mask)
                },
            )
            ReplacementSummaryLine(
                checked = hasReplacementPrompt,
                text = if (hasReplacementPrompt) {
                    stringResource(R.string.replacement_confirmation_object_format, replacementPrompt)
                } else {
                    stringResource(R.string.replacement_confirmation_need_replacement)
                },
            )
        }
    }
}

@Composable
private fun ReplacementSummaryLine(
    checked: Boolean,
    text: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
        Surface(shape = CircleShape, color = if (checked) StudioBlue else StudioPaper, modifier = Modifier.padding(top = 1.dp)) {
            Icon(
                if (checked) Icons.Rounded.Check else Icons.Rounded.Lock,
                contentDescription = null,
                modifier = Modifier.padding(4.dp).size(13.dp),
                tint = if (checked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(text, color = StudioInk.copy(alpha = 0.78f), style = MaterialTheme.typography.bodyMedium)
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
private fun ProcessingStep(
    state: HomeDecorUiState,
    message: String,
) {
    val steps = listOf(
        stringResource(R.string.generation_step_analyzing_room),
        stringResource(R.string.generation_step_applying_style),
        stringResource(R.string.generation_step_preparing_result),
        stringResource(R.string.generation_step_finalizing),
    )
    val applyingMessage = stringResource(R.string.progress_applying_color)
    val finalizingMessage = stringResource(R.string.progress_finalizing_render)
    val reportedStep = when (message) {
        applyingMessage -> 1
        finalizingMessage -> 3
        else -> 0
    }
    var visibleStep by remember(message) { mutableStateOf(reportedStep) }

    LaunchedEffect(message) {
        visibleStep = reportedStep
        if (reportedStep < 1) {
            delay(1600)
            visibleStep = maxOf(visibleStep, 1)
        }
        if (reportedStep < 2) {
            delay(2200)
            visibleStep = maxOf(visibleStep, 2)
        }
    }

    val progress = when (visibleStep.coerceIn(0, 3)) {
        0 -> 0.26f
        1 -> 0.52f
        2 -> 0.78f
        else -> 0.94f
    }
    val heroImage = processingHeroImage(state.selectedTool.id)
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            shape = RoundedCornerShape(30.dp),
            color = StudioPaper,
            tonalElevation = 3.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(178.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(StudioMist),
                ) {
                    Image(
                        painter = painterResource(heroImage),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.58f)))))
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.align(Alignment.TopStart).padding(14.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                        ) {
                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(17.dp), tint = StudioBlue)
                            Text(stringResource(R.string.generation_progress_badge), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = StudioInk)
                        }
                    }
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            stringResource(R.string.generation_progress_title),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                        )
                        Text(
                            stringResource(R.string.generation_progress_body),
                            color = Color.White.copy(alpha = 0.84f),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(9.dp).clip(CircleShape),
                    )
                    steps.forEachIndexed { index, label ->
                        GenerationProgressRow(
                            label = label,
                            index = index,
                            visibleStep = visibleStep,
                        )
                    }
                }
                Surface(shape = RoundedCornerShape(18.dp), color = StudioMist, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        message.ifBlank { stringResource(R.string.processing_transform) },
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun GenerationProgressRow(
    label: String,
    index: Int,
    visibleStep: Int,
) {
    val completed = index < visibleStep
    val active = index == visibleStep
    val container = when {
        completed -> StudioPrimaryContainer
        active -> StudioProContainer
        else -> StudioMist
    }
    val content = when {
        completed -> StudioBlue
        active -> StudioGold
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Surface(shape = CircleShape, color = container) {
            Icon(
                if (completed) Icons.Rounded.Check else Icons.Rounded.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.padding(7.dp).size(16.dp),
                tint = content,
            )
        }
        Text(
            label,
            modifier = Modifier.weight(1f),
            color = if (active || completed) StudioInk else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (active || completed) FontWeight.Black else FontWeight.Medium,
        )
    }
}

private fun processingHeroImage(toolId: String): Int = when (toolId) {
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
    val remove = state.mobilierASupprimer.ifBlank { stringResource(R.string.layout_default_remove) }
    val move = state.mobilierADeplacer.ifBlank { stringResource(R.string.layout_default_move) }
    val people = state.style.ifBlank { stringResource(R.string.layout_default_people) }
    val constraints = state.layoutConstraints.ifBlank { stringResource(R.string.layout_default_constraints) }
    return stringResource(R.string.layout_summary_format, goals, keep, remove, move, people, constraints)
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

@Composable
private fun ReplacementResultSummary(
    replacementPrompt: String,
    resultReady: Boolean,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPrimaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBlue.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                stringResource(R.string.replacement_result_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = StudioBlue,
            )
            Text(
                stringResource(R.string.replacement_result_body),
                style = MaterialTheme.typography.bodyMedium,
                color = StudioInk,
            )
            ReplacementResultRow(
                label = stringResource(R.string.replacement_result_mask_used),
                value = stringResource(R.string.mask_object_marked),
            )
            ReplacementResultRow(
                label = stringResource(R.string.replacement_result_object),
                value = replacementPrompt.ifBlank { stringResource(R.string.no_custom_prompt) },
            )
            ReplacementResultRow(
                label = stringResource(R.string.replacement_result_status),
                value = stringResource(if (resultReady) R.string.ready else R.string.failed),
            )
        }
    }
}

@Composable
private fun ReplacementResultRow(
    label: String,
    value: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
        Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.padding(top = 2.dp).size(18.dp), tint = StudioBlue)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = StudioBlue, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = StudioInk)
        }
    }
}

@Composable
private fun ResultStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val result = state.board.firstOrNull()
    val context = LocalContext.current
    val resources = LocalResources.current
    val scope = rememberCoroutineScope()
    var showOriginal by remember { mutableStateOf(false) }
    var projectPickerVisible by remember { mutableStateOf(false) }
    var createProjectForResultVisible by remember { mutableStateOf(false) }
    val resultReady = result?.let { it.isGeneratedResult() && it.status != "failed" } == true
    val isReplaceResult = state.selectedTool.id == "replace"
    val replacementPrompt = localizedReplacementPrompt(state.customPrompt)
    val savedGenerated = result?.let { current -> state.workspace.generatedResults.firstOrNull { it.id == current.id } }
    val attachedProject = savedGenerated?.projectId?.let { id -> state.workspace.projects.firstOrNull { it.id == id } }
    val isFavorite = result?.let { current -> state.workspace.favorites.any { it.resultId == current.id } } == true
    if (showOriginal) {
        OriginalImageDialog(
            state = state,
            result = result,
            onDismiss = { showOriginal = false },
        )
    }
    if (projectPickerVisible) {
        AddToProjectDialog(
            state = state,
            result = result,
            onDismiss = { projectPickerVisible = false },
            onCreateProject = {
                projectPickerVisible = false
                createProjectForResultVisible = true
            },
            onSelectProject = { project ->
                val saved = viewModel.addResultToProject(result, project.id)
                Toast.makeText(context, resources.getString(if (saved) R.string.toast_added_to_project else R.string.toast_project_save_failed), Toast.LENGTH_LONG).show()
                projectPickerVisible = false
            },
        )
    }
    if (createProjectForResultVisible) {
        ProjectEditorDialog(
            title = stringResource(R.string.project_create_from_result_title),
            confirmLabel = stringResource(R.string.create),
            initialName = result?.roomType?.takeIf { it.isNotBlank() } ?: "",
            initialRoomType = result?.roomType.orEmpty(),
            initialNotes = state.customPrompt,
            initialStyleInfo = listOf(state.style, state.palette, state.designMode).filter { it.isNotBlank() }.joinToString(" - "),
            onDismiss = { createProjectForResultVisible = false },
            onConfirm = { name, roomType, notes, styleInfo ->
                val project = viewModel.createProjectFromResult(name = name, roomType = roomType, notes = notes, styleInfo = styleInfo, result = result)
                Toast.makeText(context, resources.getString(if (project != null) R.string.toast_added_to_project else R.string.toast_project_save_failed), Toast.LENGTH_LONG).show()
                createProjectForResultVisible = false
            },
        )
    }
    StepScaffold(
        eyebrow = stringResource(R.string.result),
        title = stringResource(R.string.your_result),
        body = if (isReplaceResult) stringResource(R.string.result_replace_body) else stringResource(R.string.result_saved_workspace),
        buttonLabel = stringResource(R.string.new_creation),
        buttonIcon = Icons.Rounded.Check,
        onButton = { viewModel.startTool(state.selectedTool) },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (result == null) {
                ResultStateNotice(
                    title = stringResource(R.string.result_empty_title),
                    body = stringResource(R.string.result_empty_body),
                    icon = Icons.Rounded.AutoAwesome,
                )
            } else if (!resultReady) {
                ResultStateNotice(
                    title = stringResource(R.string.result_failed_title),
                    body = result.errorMessage ?: stringResource(R.string.generation_failed_retry),
                    icon = Icons.Rounded.Refresh,
                )
            } else {
                BeforeAfterResultSlider(
                    state = state,
                    result = result,
                )
                if (state.selectedTool.id == "reference") {
                    val referenceImageRes = state.selectedReferenceDiscoverItemId
                        ?.let(::discoverItemById)
                        ?.imageRes
                        ?: R.drawable.tool_reference
                    ResultImageCard(title = stringResource(R.string.reference_image)) {
                        UriOrResourceImage(
                            uri = state.selectedReferenceUri,
                            imageRes = referenceImageRes,
                            contentDescription = stringResource(R.string.reference_image),
                            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        )
                    }
                }
            }
            ResultContentsSummary(
                state = state,
                result = result,
                replacementPrompt = replacementPrompt,
                resultReady = resultReady,
            )
            if (resultReady && state.selectedTool.id == "layout") {
                LayoutResultSummary(state)
            }
            if (resultReady && isReplaceResult) {
                ReplacementResultSummary(
                    replacementPrompt = replacementPrompt,
                    resultReady = true,
                )
            }
            Surface(shape = RoundedCornerShape(22.dp), color = StudioPaper, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp))) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(stringResource(if (isReplaceResult) R.string.replacement_summary else R.string.metadata), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.metadata_service, localizedWorkflowTitle(state.selectedTool)))
                    if (isReplaceResult) {
                        Text(stringResource(R.string.metadata_replacement_object, replacementPrompt.ifBlank { stringResource(R.string.no_custom_prompt) }))
                        Text(stringResource(R.string.metadata_mask, stringResource(R.string.mask_object_marked)))
                    } else {
                        Text(stringResource(R.string.metadata_style, (state.style.ifBlank { state.palette }).takeIf { it.isNotBlank() }?.let { localizedOption(it) } ?: stringResource(R.string.ai_choice)))
                        Text(stringResource(R.string.metadata_prompt, state.customPrompt.ifBlank { stringResource(R.string.no_custom_prompt) }))
                    }
                    Text(stringResource(R.string.metadata_status, stringResource(if (!resultReady) R.string.failed else R.string.ready)))
                    Text(stringResource(R.string.metadata_date, java.text.DateFormat.getDateTimeInstance().format(java.util.Date((result?.createdAt ?: System.currentTimeMillis().toDouble()).toLong()))))
                }
            }
            if (resultReady) {
                ResultProjectWorkspaceActions(
                    attachedProject = attachedProject,
                    isFavorite = isFavorite,
                    onProject = { projectPickerVisible = true },
                    onFavorite = {
                        val favorite = viewModel.toggleFavorite(result)
                        Toast.makeText(context, resources.getString(if (favorite) R.string.toast_favorite_added else R.string.toast_favorite_removed), Toast.LENGTH_LONG).show()
                    },
                    onMoodboard = {
                        val saved = viewModel.addResultToMoodboard(result, attachedProject?.id)
                        Toast.makeText(context, resources.getString(if (saved) R.string.toast_moodboard_added else R.string.toast_project_save_failed), Toast.LENGTH_LONG).show()
                    },
                )
                TryAnotherStyleRow(
                    selectedStyle = state.style.ifBlank { result.style },
                    onStyle = viewModel::tryAnotherStyle,
                )
                OutlinedButton(
                    onClick = viewModel::previousStage,
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.undo_edit_design_choices), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        val saved = viewModel.saveResultToPortfolio(result)
                        Toast.makeText(context, if (saved) resources.getString(R.string.toast_design_saved) else resources.getString(R.string.toast_design_save_failed), Toast.LENGTH_LONG).show()
                    },
                    enabled = resultReady,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Save, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.save), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val shared = shareResult(context, result)
                            if (!shared) {
                                Toast.makeText(context, resources.getString(R.string.toast_share_failed), Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    enabled = resultReady,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Share, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.share), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val saved = saveResultToGallery(context, result)
                            Toast.makeText(
                                context,
                                resources.getString(if (saved) R.string.toast_design_downloaded else R.string.toast_design_save_failed),
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                    },
                    enabled = resultReady,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Download, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.download), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(onClick = viewModel::generate, shape = CircleShape, modifier = Modifier.weight(1f).heightIn(min = 48.dp)) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.regenerate), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            OutlinedButton(
                onClick = { showOriginal = true },
                enabled = result != null,
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) {
                Icon(Icons.Rounded.Visibility, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.view_original))
            }
        }
    }
}

@Composable
private fun BeforeAfterResultSlider(
    state: HomeDecorUiState,
    result: BoardItem,
) {
    var comparePosition by remember(result.id) { mutableStateOf(0.5f) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.before_after_slider), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(28.dp))
                .background(StudioMist),
        ) {
            WorkspaceImage(
                imageUrl = result.imageUrl,
                imageUri = result.imageUri,
                imageRes = result.imageRes,
                contentDescription = stringResource(R.string.after),
                modifier = Modifier.fillMaxSize(),
            )
            OriginalSourceImage(
                state = state,
                result = result,
                contentDescription = stringResource(R.string.before),
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        clipRect(right = size.width * comparePosition) {
                            this@drawWithContent.drawContent()
                        }
                    },
            )
            Canvas(Modifier.matchParentSize()) {
                val handleX = size.width * comparePosition
                drawLine(
                    color = Color.White,
                    start = Offset(handleX, 0f),
                    end = Offset(handleX, size.height),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawCircle(
                    color = Color.White,
                    radius = 18.dp.toPx(),
                    center = Offset(handleX, size.height / 2f),
                )
                drawLine(
                    color = StudioInk,
                    start = Offset(handleX - 7.dp.toPx(), size.height / 2f),
                    end = Offset(handleX + 7.dp.toPx(), size.height / 2f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ComparisonBadge(stringResource(R.string.before))
                ComparisonBadge(stringResource(R.string.after))
            }
        }
        Slider(
            value = comparePosition,
            onValueChange = { comparePosition = it.coerceIn(0.05f, 0.95f) },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            stringResource(R.string.before_after_slider_hint),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun ComparisonBadge(label: String) {
    Surface(
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.54f),
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun TryAnotherStyleRow(
    selectedStyle: String,
    onStyle: (String) -> Unit,
) {
    val styles = remember { listOf("Japandi", "Luxe", "Moderne", "Minimaliste", "Marocain", "Scandinave") }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.try_another_style), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(styles) { style ->
                val selected = selectedStyle.split(" + ").any { it.trim() == style }
                FilterChip(
                    selected = selected,
                    onClick = { onStyle(style) },
                    label = { Text(localizedOption(style), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    leadingIcon = if (selected) {
                        { Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else {
                        null
                    },
                )
            }
        }
    }
}

@Composable
private fun ResultProjectWorkspaceActions(
    attachedProject: Project?,
    isFavorite: Boolean,
    onProject: () -> Unit,
    onFavorite: () -> Unit,
    onMoodboard: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPrimaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBlue.copy(alpha = 0.28f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = StudioPaper) {
                    Icon(Icons.Rounded.Layers, null, Modifier.padding(10.dp).size(20.dp), tint = StudioBlue)
                }
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.result_workspace_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioInk)
                    Text(
                        attachedProject?.let { stringResource(R.string.result_workspace_project, it.name) }
                            ?: stringResource(R.string.result_workspace_body),
                        color = StudioInk.copy(alpha = 0.78f),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onProject,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.save_to_project), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(
                    onClick = onFavorite,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(18.dp), tint = if (isFavorite) StudioGold else Color.Unspecified)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(if (isFavorite) R.string.favorited else R.string.favorite), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            OutlinedButton(
                onClick = onMoodboard,
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) {
                Icon(Icons.AutoMirrored.Rounded.ViewQuilt, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.add_to_moodboard), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun AddToProjectDialog(
    state: HomeDecorUiState,
    result: BoardItem?,
    onDismiss: () -> Unit,
    onCreateProject: () -> Unit,
    onSelectProject: (Project) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.save_to_project), fontWeight = FontWeight.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    stringResource(R.string.save_to_project_body),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                if (state.workspace.projects.isEmpty()) {
                    Surface(shape = RoundedCornerShape(18.dp), color = StudioMist, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            stringResource(R.string.no_projects_yet),
                            modifier = Modifier.padding(14.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.workspace.projects, key = { it.id }) { project ->
                            val alreadyLinked = state.workspace.generatedResults.firstOrNull { it.id == result?.id }?.projectId == project.id
                            Surface(
                                onClick = { onSelectProject(project) },
                                shape = RoundedCornerShape(18.dp),
                                color = if (alreadyLinked) StudioPrimaryContainer else StudioPaper,
                                tonalElevation = 1.dp,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (alreadyLinked) StudioBlue.copy(alpha = 0.36f) else StudioLine),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    WorkspaceImage(
                                        imageUrl = project.coverImageUrl,
                                        imageUri = project.coverImageUri ?: project.originalPhotoUris.firstOrNull(),
                                        contentDescription = project.name,
                                        modifier = Modifier.size(54.dp).clip(RoundedCornerShape(14.dp)),
                                    )
                                    Column(Modifier.weight(1f)) {
                                        Text(project.name, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        Text(
                                            project.roomType.ifBlank { stringResource(R.string.project_room_unspecified) },
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                    if (alreadyLinked) {
                                        Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioBlue, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onCreateProject, shape = CircleShape) {
                Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.new_project))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
private fun ResultContentsSummary(
    state: HomeDecorUiState,
    result: BoardItem?,
    replacementPrompt: String,
    resultReady: Boolean,
) {
    val space = state.roomType.ifBlank { result?.roomType.orEmpty() }
        .takeIf { it.isNotBlank() }
        ?.let { localizedOption(it) }
        ?: stringResource(R.string.ai_choice)
    val finish = (state.style.ifBlank { state.palette.ifBlank { result?.style.orEmpty() } })
        .takeIf { it.isNotBlank() }
        ?.let { localizedOption(it) }
        ?: stringResource(R.string.ai_choice)
    val description = when {
        !resultReady -> stringResource(R.string.result_contains_failed)
        state.selectedTool.id == "layout" -> stringResource(R.string.result_contains_layout, space)
        state.selectedTool.id == "replace" -> stringResource(R.string.result_contains_replace, replacementPrompt.ifBlank { stringResource(R.string.ai_choice) })
        state.selectedTool.id == "reference" -> stringResource(R.string.result_contains_reference, finish)
        state.selectedTool.id == "paint" -> stringResource(R.string.result_contains_paint, finish)
        state.selectedTool.id == "floor" -> stringResource(R.string.result_contains_floor, finish)
        else -> stringResource(R.string.result_contains_default, space, finish)
    }
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = StudioPrimaryContainer,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBlue.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.result_contains_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = StudioBlue)
            Text(description, color = StudioInk, style = MaterialTheme.typography.bodyMedium)
            if (resultReady) {
                Text(stringResource(R.string.result_saved_to_profile_history), color = StudioInk, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ResultStateNotice(
    title: String,
    body: String,
    icon: ImageVector,
) {
    val samples = sampleProjectCards()
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPaper,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                Icon(icon, contentDescription = null, modifier = Modifier.padding(16.dp).size(28.dp), tint = StudioBlue)
            }
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
private fun OriginalImageDialog(
    state: HomeDecorUiState,
    result: BoardItem?,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.original_image)) },
        text = {
            OriginalSourceImage(
                state = state,
                result = result,
                contentDescription = stringResource(R.string.original_image),
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            )
        },
        confirmButton = {
            Button(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.close))
            }
        },
    )
}

@Composable
private fun OriginalSourceImage(
    state: HomeDecorUiState,
    result: BoardItem?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val resultSourceUri = result?.sourceImageUri
        ?.takeIf { it.isNotBlank() }
        ?.let { runCatching { Uri.parse(it) }.getOrNull() }
    val sourceUri = resultSourceUri ?: state.selectedPhotos.firstOrNull()?.uri ?: state.selectedPhotoUri
    if (!result?.sourceImageUrl.isNullOrBlank()) {
        NetworkOrResourceImage(
            imageUrl = result.sourceImageUrl,
            imageRes = selectedExampleImageRes(state),
            contentDescription = contentDescription,
            modifier = modifier,
        )
    } else {
        UriOrResourceImage(
            uri = sourceUri,
            imageRes = selectedExampleImageRes(state),
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}

@Composable
private fun ResultImageCard(
    title: String,
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
        Card(shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
            image()
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
    val imageUri = result?.imageUri
    if (!imageUri.isNullOrBlank()) {
        context.contentResolver.openInputStream(Uri.parse(imageUri))?.use { input -> input.copyTo(output) }
        return
    }
    val imageUrl = result?.imageUrl
    if (!imageUrl.isNullOrBlank()) {
        URL(imageUrl).openStream().use { input -> input.copyTo(output) }
        return
    }
    resultBitmap(context, result).compress(Bitmap.CompressFormat.JPEG, 94, output)
}

private fun resultBitmap(context: android.content.Context, result: BoardItem?): Bitmap {
    result?.imageUri?.takeIf { it.isNotBlank() }?.let { imageUri ->
        context.contentResolver.openInputStream(Uri.parse(imageUri))?.use { input ->
            BitmapFactory.decodeStream(input)?.let { return it }
        }
    }
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
private fun DiscoverScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    var selectedCluster by remember { mutableStateOf("Intérieurs") }
    var detailSection by remember { mutableStateOf<DiscoverSection?>(null) }
    var previewTarget by remember { mutableStateOf<DiscoverPreviewTarget?>(null) }
    val clusters = listOf("Intérieurs", "Architecture", "Paysages")
    val sections = HomeDecorCatalog.discoverSections.filter { it.cluster == selectedCluster }
    val favoriteSources = remember(state.workspace.favorites) { state.workspace.favorites.map { it.sourceType }.toSet() }
    fun openPreview(section: DiscoverSection, item: GalleryItem) {
        previewTarget = section.discoverPreviewTarget(item)
    }
    fun toggleFavorite(section: DiscoverSection, item: GalleryItem) {
        val favorite = viewModel.toggleDiscoverFavorite(item, section)
        Toast.makeText(
            context,
            context.getString(if (favorite) R.string.toast_favorite_added else R.string.toast_favorite_removed),
            Toast.LENGTH_SHORT,
        ).show()
    }
    fun addToMoodboard(section: DiscoverSection, item: GalleryItem) {
        viewModel.addDiscoverToMoodboard(item, section)
        Toast.makeText(context, context.getString(R.string.toast_moodboard_added), Toast.LENGTH_SHORT).show()
    }
    fun useStyle(section: DiscoverSection, item: GalleryItem) {
        viewModel.useDiscoverStyle(item, section)
        previewTarget = null
        detailSection = null
    }
    val activeDetail = detailSection
    if (activeDetail != null) {
        DiscoverDetailScreen(
            section = activeDetail,
            onBack = { detailSection = null },
            onPreview = { openPreview(activeDetail, it) },
            favoriteSources = favoriteSources,
            onFavorite = { toggleFavorite(activeDetail, it) },
            onMoodboard = { addToMoodboard(activeDetail, it) },
            onUseStyle = { useStyle(activeDetail, it) },
        )
        previewTarget?.let { target ->
            DiscoverPreviewDialog(
                target = target,
                onDismiss = { previewTarget = null },
                isFavorite = discoverSource(target.item) in favoriteSources,
                onFavorite = { toggleFavorite(target.section, target.item) },
                onMoodboard = { addToMoodboard(target.section, target.item) },
                onUseStyle = { useStyle(target.section, target.item) },
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
                    favoriteSources = favoriteSources,
                    onFavorite = { toggleFavorite(section, it) },
                    onMoodboard = { addToMoodboard(section, it) },
                    onUseStyle = { useStyle(section, it) },
                )
            }
        }
    }
    previewTarget?.let { target ->
        DiscoverPreviewDialog(
            target = target,
            onDismiss = { previewTarget = null },
            isFavorite = discoverSource(target.item) in favoriteSources,
            onFavorite = { toggleFavorite(target.section, target.item) },
            onMoodboard = { addToMoodboard(target.section, target.item) },
            onUseStyle = { useStyle(target.section, target.item) },
        )
    }
}

private data class DiscoverPreviewTarget(
    val item: GalleryItem,
    val section: DiscoverSection,
    val tool: DecorTool,
)

private data class DiscoverSavedTarget(
    val item: GalleryItem,
    val section: DiscoverSection,
)

private fun DiscoverSection.discoverPreviewTarget(item: GalleryItem): DiscoverPreviewTarget? {
    val belongsToSection = items.any { it.id == item.id }
    val tool = HomeDecorCatalog.tools.firstOrNull { it.id == serviceToolId }
    return if (belongsToSection && tool != null) DiscoverPreviewTarget(item, this, tool) else null
}

private fun discoverTargetForSource(source: String): DiscoverSavedTarget? {
    val itemId = source.removePrefix("discover:")
    if (itemId == source) return null
    HomeDecorCatalog.discoverSections.forEach { section ->
        section.items.firstOrNull { it.id == itemId }?.let { item ->
            return DiscoverSavedTarget(item = item, section = section)
        }
    }
    return null
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
    favoriteSources: Set<String>,
    onFavorite: (GalleryItem) -> Unit,
    onMoodboard: (GalleryItem) -> Unit,
    onUseStyle: (GalleryItem) -> Unit,
) {
    val sectionTitle = localizedDiscoverSection(section)
    val sectionSubtitle = localizedDiscoverSectionSubtitle(section)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f).padding(end = 12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(sectionTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text(
                    sectionSubtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
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
                GalleryCard(
                    item = item,
                    isFavorite = discoverSource(item) in favoriteSources,
                    onClick = { onPreview(item) },
                    onFavorite = { onFavorite(item) },
                    onMoodboard = { onMoodboard(item) },
                    onUseStyle = { onUseStyle(item) },
                )
            }
        }
    }
}

@Composable
private fun DiscoverDetailScreen(
    section: DiscoverSection,
    onBack: () -> Unit,
    onPreview: (GalleryItem) -> Unit,
    favoriteSources: Set<String>,
    onFavorite: (GalleryItem) -> Unit,
    onMoodboard: (GalleryItem) -> Unit,
    onUseStyle: (GalleryItem) -> Unit,
) {
    val sectionTitle = localizedDiscoverSection(section)
    val sectionCluster = localizedDiscoverCluster(section.cluster)
    val sectionSubtitle = localizedDiscoverSectionSubtitle(section)
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
                Text(sectionSubtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(stringResource(R.string.discover_detail_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
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
                GalleryCard(
                    item = item,
                    isFavorite = discoverSource(item) in favoriteSources,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onPreview(item) },
                    onFavorite = { onFavorite(item) },
                    onMoodboard = { onMoodboard(item) },
                    onUseStyle = { onUseStyle(item) },
                )
            }
        }
    }
}

@Composable
private fun DiscoverPreviewDialog(
    target: DiscoverPreviewTarget,
    onDismiss: () -> Unit,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    onMoodboard: () -> Unit,
    onUseStyle: () -> Unit,
) {
    val item = target.item
    val itemTitle = localizedGalleryTitle(item)
    val itemCategory = localizedGalleryCategory(item.category)
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onUseStyle()
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onFavorite, shape = CircleShape, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(17.dp), tint = if (isFavorite) StudioGold else Color.Unspecified)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(if (isFavorite) R.string.favorited else R.string.favorite), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(onClick = onMoodboard, shape = CircleShape, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Save, null, Modifier.size(17.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.add_to_moodboard), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
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
    isFavorite: Boolean,
    modifier: Modifier = Modifier.width(196.dp),
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onMoodboard: () -> Unit,
    onUseStyle: () -> Unit,
) {
    val itemTitle = localizedGalleryTitle(item)
    val itemCategory = localizedGalleryCategory(item.category)
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        modifier = modifier,
    ) {
        Column {
        Box(Modifier.fillMaxWidth().height(226.dp)) {
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
            Row(
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                DiscoverIconAction(
                    icon = Icons.Rounded.Star,
                    label = stringResource(if (isFavorite) R.string.favorited else R.string.favorite),
                    onClick = onFavorite,
                    active = isFavorite,
                )
                DiscoverIconAction(
                    icon = Icons.Rounded.Save,
                    label = stringResource(R.string.add_to_moodboard),
                    onClick = onMoodboard,
                )
            }
        }
        Button(
            onClick = onUseStyle,
            shape = CircleShape,
            colors = studioPrimaryButtonColors(),
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = Modifier.fillMaxWidth().padding(10.dp).height(44.dp),
        ) {
            Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(stringResource(R.string.create_with_style), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        }
    }
}

@Composable
private fun DiscoverIconAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    active: Boolean = false,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.White.copy(alpha = 0.92f),
            contentColor = if (active) StudioGold else StudioBlue,
        ),
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun DailyRewardCard(
    state: HomeDecorUiState,
    onClaim: () -> Boolean,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
) {
    val reward = state.workspace.dailyReward
    val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
    val claimedToday = reward.lastClaimEpochDay == today
    val activeStreak = when (reward.lastClaimEpochDay) {
        today, today - 1 -> reward.currentStreak.coerceAtLeast(if (claimedToday) 1 else 0)
        else -> 0
    }
    val displayDay = if (claimedToday) activeStreak.coerceAtLeast(1) else (activeStreak + 1).coerceAtLeast(1)
    val titleColor = if (dark) Color.White else StudioInk
    val bodyColor = if (dark) PaywallTextSecondary else MaterialTheme.colorScheme.onSurfaceVariant
    val container = if (dark) PaywallCardAlt else StudioPaper
    val borderColor = if (dark) PaywallBorder else StudioLine
    val cardShape = RoundedCornerShape(20.dp)
    ElevatedCard(
        shape = cardShape,
        colors = CardDefaults.elevatedCardColors(containerColor = container),
        modifier = modifier.fillMaxWidth().border(1.dp, borderColor, cardShape),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(shape = CircleShape, color = if (dark) PaywallAccent.copy(alpha = 0.20f) else StudioProContainer) {
                Icon(
                    Icons.Rounded.Diamond,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp).size(19.dp),
                    tint = if (dark) PaywallPremiumGold else StudioGold,
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    stringResource(R.string.daily_reward_title),
                    color = titleColor,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    stringResource(R.string.daily_reward_subtitle),
                    color = bodyColor,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    DailyRewardQuietPill(
                        label = stringResource(R.string.daily_reward_day_format, displayDay),
                        dark = dark,
                    )
                    DailyRewardQuietPill(
                        label = if (activeStreak > 0) {
                            stringResource(R.string.daily_reward_streak_format, activeStreak)
                        } else {
                            stringResource(R.string.daily_reward_soft_start)
                        },
                        dark = dark,
                    )
                }
            }
            Button(
                onClick = { onClaim() },
                enabled = !claimedToday,
                shape = CircleShape,
                colors = if (dark) studioProButtonColors() else studioPrimaryButtonColors(),
                contentPadding = PaddingValues(horizontal = 13.dp),
                modifier = Modifier.height(44.dp),
            ) {
                Icon(if (claimedToday) Icons.Rounded.Check else Icons.Rounded.Add, null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    stringResource(if (claimedToday) R.string.daily_reward_claimed else R.string.daily_reward_claim),
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun DailyRewardQuietPill(
    label: String,
    dark: Boolean,
) {
    Surface(
        shape = CircleShape,
        color = if (dark) Color.White.copy(alpha = 0.10f) else StudioMist.copy(alpha = 0.72f),
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            color = if (dark) Color.White.copy(alpha = 0.78f) else HomeDecorColors.InkSoft,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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

private enum class ProfileWorkspaceTab { Favorites, Moodboard, History, Projects }

@Composable
private fun ProfileScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    val openRealAuth = { openAuth(context) }
    val signedIn = !state.viewer.isGuest || state.signedInName != null
    var createProjectVisible by remember { mutableStateOf(false) }
    var selectedProjectId by remember { mutableStateOf<String?>(null) }
    var selectedProfileTab by remember { mutableStateOf(ProfileWorkspaceTab.Favorites) }
    val selectedProject = state.workspace.projects.firstOrNull { it.id == selectedProjectId }
    fun openDiscoverSource(source: String): Boolean {
        val target = discoverTargetForSource(source) ?: return false
        viewModel.useDiscoverStyle(target.item, target.section)
        return true
    }
    if (createProjectVisible) {
        ProjectEditorDialog(
            title = stringResource(R.string.project_create_title),
            confirmLabel = stringResource(R.string.create),
            initialName = "",
            initialRoomType = "",
            initialNotes = "",
            initialStyleInfo = "",
            onDismiss = { createProjectVisible = false },
            onConfirm = { name, roomType, notes, styleInfo ->
                val project = viewModel.createProject(name = name, roomType = roomType, notes = notes, styleInfo = styleInfo)
                selectedProjectId = project.id
                createProjectVisible = false
            },
        )
    }
    if (selectedProject != null) {
        ProjectDetailDialog(
            project = selectedProject,
            state = state,
            onDismiss = { selectedProjectId = null },
            onUpdate = viewModel::updateProject,
            onCreateDesign = {
                selectedProjectId = null
                viewModel.selectTab(MainTab.Tools)
            },
        )
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item("profile-header") {
                SimpleProfileHeader(
                    state = state,
                    signedIn = signedIn,
                    name = state.signedInName,
                    email = state.signedInEmail,
                    onSignIn = openRealAuth,
                    onSettings = viewModel::openSettings,
                    onStore = viewModel::openDiamondStore,
                    onPaywall = viewModel::openPaywall,
                )
            }
            item("profile-daily-reward") {
                DailyRewardCard(
                    state = state,
                    onClaim = viewModel::claimLocalDailyReward,
                )
            }
            item("profile-tabs") {
                ProfileWorkspaceTabs(
                    selected = selectedProfileTab,
                    state = state,
                    onSelect = { selectedProfileTab = it },
                )
            }
            item("profile-tab-content") {
                when (selectedProfileTab) {
                    ProfileWorkspaceTab.Favorites -> FavoritesSection(
                        state = state,
                        onExplore = { viewModel.selectTab(MainTab.Discover) },
                        onTryExample = { viewModel.selectTab(MainTab.Tools) },
                        onOpen = { favorite ->
                            when {
                                favorite.resultId != null -> viewModel.openHistoryResult(favorite.resultId)
                                favorite.sourceType.startsWith("discover:") -> openDiscoverSource(favorite.sourceType)
                                else -> false
                            }
                        },
                    )
                    ProfileWorkspaceTab.Moodboard -> MoodboardSection(
                        state = state,
                        onExplore = { viewModel.selectTab(MainTab.Discover) },
                        onTryExample = { viewModel.selectTab(MainTab.Tools) },
                        onOpen = { item ->
                            when {
                                item.source.startsWith("generated_result:") -> viewModel.openHistoryResult(item.source.removePrefix("generated_result:"))
                                item.source.startsWith("discover:") -> openDiscoverSource(item.source)
                                else -> false
                            }
                        },
                    )
                    ProfileWorkspaceTab.History -> PortfolioHistorySection(
                        state = state,
                        onCreate = { viewModel.selectTab(MainTab.Tools) },
                        onExplore = { viewModel.selectTab(MainTab.Discover) },
                        onOpen = viewModel::openHistoryResult,
                        onFavorite = viewModel::toggleHistoryFavorite,
                        onSaveToProject = viewModel::saveHistoryResultToProject,
                        onDelete = viewModel::deleteHistoryResult,
                    )
                    ProfileWorkspaceTab.Projects -> ProjectsWorkspaceSection(
                        state = state,
                        onCreateProject = { createProjectVisible = true },
                        onOpenProject = { selectedProjectId = it.id },
                        onCreateDesign = { viewModel.selectTab(MainTab.Tools) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleProfileHeader(
    state: HomeDecorUiState,
    signedIn: Boolean,
    name: String?,
    email: String?,
    onSignIn: () -> Unit,
    onSettings: () -> Unit,
    onStore: () -> Unit,
    onPaywall: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (signedIn) {
                Surface(
                    onClick = onSignIn,
                    shape = RoundedCornerShape(24.dp),
                    color = StudioPaper,
                    tonalElevation = 1.dp,
                    modifier = Modifier.weight(1f).heightIn(min = 58.dp),
                ) {
                    Row(
                        Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        ProfileAvatarPreview(state = state, signedIn = signedIn, modifier = Modifier.size(42.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                name ?: stringResource(R.string.account_connected),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                email ?: stringResource(R.string.profile_signed_in_sync),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            } else {
                Button(
                    onClick = onSignIn,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.weight(1f).height(54.dp),
                ) {
                    ProfileAvatarPreview(state = state, signedIn = signedIn, modifier = Modifier.size(34.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.sign_in), fontWeight = FontWeight.Black, maxLines = 1)
                }
            }
            OutlinedButton(
                onClick = onSettings,
                shape = CircleShape,
                modifier = Modifier.height(54.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
            ) {
                Icon(Icons.Rounded.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.settings), fontWeight = FontWeight.Black, maxLines = 1)
            }
        }
        ProfileStatusStrip(
            state = state,
            onStore = onStore,
            onPaywall = onPaywall,
        )
    }
}

@Composable
private fun ProfileAvatarPreview(
    state: HomeDecorUiState,
    signedIn: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier, contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = painterResource(R.drawable.profile_workspace),
            contentDescription = stringResource(R.string.profile_photo_preview),
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .border(1.dp, if (state.isPro) StudioGold else StudioLine, CircleShape),
            contentScale = ContentScale.Crop,
        )
        Surface(
            shape = CircleShape,
            color = if (signedIn) StudioPrimaryContainer else StudioPaper,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.86f)),
        ) {
            Icon(
                if (signedIn) Icons.Rounded.Check else Icons.Rounded.Person,
                contentDescription = null,
                modifier = Modifier.padding(3.dp).size(10.dp),
                tint = if (signedIn) StudioBlue else StudioInk,
            )
        }
    }
}

@Composable
private fun ProfileStatusStrip(
    state: HomeDecorUiState,
    onStore: () -> Unit,
    onPaywall: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ProfileStatusPill(
            icon = Icons.Rounded.Diamond,
            label = stringResource(R.string.diamonds_count, state.diamonds),
            onClick = onStore,
            modifier = Modifier.weight(1f),
        )
        ProfileStatusPill(
            icon = Icons.Rounded.Star,
            label = if (state.isPro) stringResource(R.string.active_pro) else stringResource(R.string.free_plan),
            onClick = onPaywall,
            modifier = Modifier.weight(1f),
            accent = state.isPro,
        )
    }
}

@Composable
private fun ProfileStatusPill(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Boolean = false,
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (accent) StudioProContainer else StudioPaper,
        tonalElevation = 1.dp,
        modifier = modifier,
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(icon, null, Modifier.size(17.dp), tint = if (accent) StudioGold else StudioBlue)
            Text(
                label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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
private fun ProfileWorkspaceTabs(
    selected: ProfileWorkspaceTab,
    state: HomeDecorUiState,
    onSelect: (ProfileWorkspaceTab) -> Unit,
) {
    val tabs = listOf(
        ProfileWorkspaceTab.Favorites to state.workspace.favorites.size,
        ProfileWorkspaceTab.Moodboard to state.workspace.moodboardItems.size,
        ProfileWorkspaceTab.History to state.workspace.generatedResults.count { it.status != "failed" && (!it.imageUrl.isNullOrBlank() || !it.imageUri.isNullOrBlank()) },
        ProfileWorkspaceTab.Projects to state.workspace.projects.size,
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(tabs, key = { it.first.name }) { (tab, count) ->
            val active = selected == tab
            FilterChip(
                selected = active,
                onClick = { onSelect(tab) },
                label = {
                    Text(
                        stringResource(R.string.profile_tab_count, profileWorkspaceTabLabel(tab), count),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                },
                leadingIcon = {
                    Icon(profileWorkspaceTabIcon(tab), contentDescription = null, modifier = Modifier.size(17.dp))
                },
                shape = CircleShape,
                modifier = Modifier.heightIn(min = 48.dp),
            )
        }
    }
}

@Composable
private fun FavoritesSection(
    state: HomeDecorUiState,
    onExplore: () -> Unit,
    onTryExample: () -> Unit,
    onOpen: (FavoriteItem) -> Boolean,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val favorites = remember(state.workspace.favorites) {
        state.workspace.favorites.sortedByDescending { it.createdAt }
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.Rounded.Star, title = stringResource(R.string.profile_favorites_title))
        Text(
            stringResource(R.string.profile_favorites_body),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (favorites.isEmpty()) {
            CollectionEmptyState(
                icon = Icons.Rounded.Star,
                title = stringResource(R.string.empty_favorites_title),
                body = stringResource(R.string.empty_favorites_body),
                primaryLabel = stringResource(R.string.empty_action_explore_discover),
                onPrimary = onExplore,
                secondaryLabel = stringResource(R.string.try_with_example),
                onSecondary = onTryExample,
                samples = sampleFavoriteCards(),
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                favorites.forEach { favorite ->
                    SavedCollectionCard(
                        title = savedFavoriteTitle(favorite),
                        subtitle = savedFavoriteSubtitle(favorite),
                        imageRes = favorite.imageRes,
                        imageUrl = favorite.imageUrl,
                        imageUri = favorite.imageUri,
                        icon = Icons.Rounded.Star,
                        actionLabel = stringResource(if (favorite.resultId != null) R.string.open else R.string.create_with_style),
                        onAction = {
                            if (!onOpen(favorite)) {
                                Toast.makeText(context, resources.getString(R.string.history_open_failed), Toast.LENGTH_SHORT).show()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodboardSection(
    state: HomeDecorUiState,
    onExplore: () -> Unit,
    onTryExample: () -> Unit,
    onOpen: (MoodboardItem) -> Boolean,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val moodboardItems = remember(state.workspace.moodboardItems) {
        state.workspace.moodboardItems.sortedWith(compareBy<MoodboardItem> { it.sortOrder }.thenByDescending { it.createdAt })
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.AutoMirrored.Rounded.ViewQuilt, title = stringResource(R.string.profile_moodboard_title))
        Text(
            stringResource(R.string.profile_moodboard_body),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (moodboardItems.isEmpty()) {
            CollectionEmptyState(
                icon = Icons.AutoMirrored.Rounded.ViewQuilt,
                title = stringResource(R.string.empty_moodboard_title),
                body = stringResource(R.string.empty_moodboard_body),
                primaryLabel = stringResource(R.string.empty_action_explore_discover),
                onPrimary = onExplore,
                secondaryLabel = stringResource(R.string.try_with_example),
                onSecondary = onTryExample,
                samples = sampleMoodboardCards(),
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                moodboardItems.forEach { item ->
                    SavedCollectionCard(
                        title = savedMoodboardTitle(item),
                        subtitle = savedMoodboardSubtitle(item),
                        imageRes = item.imageRes,
                        imageUrl = item.imageUrl,
                        imageUri = item.imageUri,
                        colorHex = item.colorHex,
                        icon = Icons.AutoMirrored.Rounded.ViewQuilt,
                        actionLabel = stringResource(if (item.source.startsWith("generated_result:")) R.string.open else R.string.create_with_style),
                        onAction = {
                            if (!onOpen(item)) {
                                Toast.makeText(context, resources.getString(R.string.history_open_failed), Toast.LENGTH_SHORT).show()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedCollectionCard(
    title: String,
    subtitle: String,
    imageRes: Int,
    imageUrl: String?,
    imageUri: String?,
    icon: ImageVector,
    actionLabel: String,
    onAction: () -> Unit,
    colorHex: String? = null,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(StudioMist),
            ) {
                SavedCollectionImage(
                    imageRes = imageRes,
                    imageUrl = imageUrl,
                    imageUri = imageUri,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                )
                colorHex?.let { hex ->
                    Surface(
                        shape = CircleShape,
                        color = hex.toComposeColor() ?: StudioPrimaryContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.78f)),
                        modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).size(28.dp),
                    ) {}
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                        Icon(icon, null, Modifier.padding(6.dp).size(15.dp), tint = StudioBlue)
                    }
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(
                    subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                OutlinedButton(
                    onClick = onAction,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.heightIn(min = 44.dp),
                ) {
                    Text(actionLabel, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun SavedCollectionImage(
    imageRes: Int,
    imageUrl: String?,
    imageUri: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    if (imageRes != 0) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    } else {
        WorkspaceImage(
            imageUrl = imageUrl,
            imageUri = imageUri,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}

@Composable
private fun CollectionEmptyState(
    icon: ImageVector,
    title: String,
    body: String,
    primaryLabel: String,
    onPrimary: () -> Unit,
    samples: List<SampleCollectionCard>,
    secondaryLabel: String? = null,
    onSecondary: (() -> Unit)? = null,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                    Icon(icon, null, Modifier.padding(11.dp).size(22.dp), tint = StudioBlue)
                }
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(items = samples, key = { it.title }) { sample ->
                    SampleCollectionPreview(sample)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onPrimary,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Icon(Icons.Rounded.Explore, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(primaryLabel, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                if (secondaryLabel != null && onSecondary != null) {
                    OutlinedButton(
                        onClick = onSecondary,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(secondaryLabel, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

private data class SampleCollectionCard(
    val title: String,
    val subtitle: String,
    val imageRes: Int,
)

@Composable
private fun SampleCollectionPreview(sample: SampleCollectionCard) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioMist,
        modifier = Modifier.width(136.dp),
    ) {
        Column {
            Image(
                painter = painterResource(sample.imageRes),
                contentDescription = sample.title,
                modifier = Modifier.fillMaxWidth().height(112.dp),
                contentScale = ContentScale.Crop,
            )
            Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(sample.title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(sample.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun profileWorkspaceTabLabel(tab: ProfileWorkspaceTab): String = stringResource(
    when (tab) {
        ProfileWorkspaceTab.Favorites -> R.string.profile_tab_favorites
        ProfileWorkspaceTab.Moodboard -> R.string.profile_tab_moodboard
        ProfileWorkspaceTab.History -> R.string.profile_tab_history
        ProfileWorkspaceTab.Projects -> R.string.profile_tab_projects
    },
)

private fun profileWorkspaceTabIcon(tab: ProfileWorkspaceTab): ImageVector = when (tab) {
    ProfileWorkspaceTab.Favorites -> Icons.Rounded.Star
    ProfileWorkspaceTab.Moodboard -> Icons.AutoMirrored.Rounded.ViewQuilt
    ProfileWorkspaceTab.History -> Icons.Rounded.Visibility
    ProfileWorkspaceTab.Projects -> Icons.Rounded.Layers
}

@Composable
private fun savedFavoriteTitle(item: FavoriteItem): String {
    val discoverTarget = discoverTargetForSource(item.sourceType)
    return discoverTarget?.let { target ->
        listOf(localizedDiscoverSection(target.section), localizedGalleryTitle(target.item))
            .filter { it.isNotBlank() }
            .joinToString(" - ")
    } ?: item.title
}

@Composable
private fun savedFavoriteSubtitle(item: FavoriteItem): String {
    val discoverTarget = discoverTargetForSource(item.sourceType)
    return discoverTarget?.let { target ->
        stringResource(R.string.saved_discover_source, localizedDiscoverCluster(target.section.cluster))
    } ?: listOf(item.roomType, item.style)
        .filter { it.isNotBlank() }
        .joinToString(" - ")
        .ifBlank { stringResource(R.string.saved_generated_source) }
}

@Composable
private fun savedMoodboardTitle(item: MoodboardItem): String {
    val discoverTarget = discoverTargetForSource(item.source)
    return discoverTarget?.let { target ->
        listOf(localizedDiscoverSection(target.section), localizedGalleryTitle(target.item))
            .filter { it.isNotBlank() }
            .joinToString(" - ")
    } ?: item.title
}

@Composable
private fun savedMoodboardSubtitle(item: MoodboardItem): String {
    val discoverTarget = discoverTargetForSource(item.source)
    return when {
        discoverTarget != null -> stringResource(R.string.saved_discover_source, localizedDiscoverCluster(discoverTarget.section.cluster))
        item.notes.isNotBlank() -> item.notes
        item.source.startsWith("generated_result:") -> stringResource(R.string.saved_generated_source)
        else -> stringResource(R.string.saved_local_source)
    }
}

@Composable
private fun sampleFavoriteCards(): List<SampleCollectionCard> = listOf(
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
private fun sampleMoodboardCards(): List<SampleCollectionCard> = listOf(
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
private fun sampleProjectCards(): List<SampleCollectionCard> = listOf(
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
private fun sampleHistoryCards(): List<SampleCollectionCard> = listOf(
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

private fun String.toComposeColor(): Color? {
    return runCatching {
        val clean = removePrefix("#")
        Color(clean.toLong(16) or if (clean.length <= 6) 0xFF000000 else 0x00000000)
    }.getOrNull()
}

@Composable
private fun PortfolioHistorySection(
    state: HomeDecorUiState,
    onCreate: () -> Unit,
    onExplore: () -> Unit,
    onOpen: (String) -> Boolean,
    onFavorite: (String) -> Boolean,
    onSaveToProject: (String) -> com.ismail.homedecorai.Project?,
    onDelete: (String) -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val historyItems = remember(state.workspace.generatedResults) {
        state.workspace.generatedResults
            .filter { it.status != "failed" && (!it.imageUrl.isNullOrBlank() || !it.imageUri.isNullOrBlank()) }
            .sortedByDescending { it.createdAt }
    }
    val favoriteResultIds = remember(state.workspace.favorites) {
        state.workspace.favorites.mapNotNull { it.resultId }.toSet()
    }
    val groupedHistory = remember(historyItems) { groupHistoryResults(historyItems) }
    var deleteCandidate by remember { mutableStateOf<GeneratedResult?>(null) }

    deleteCandidate?.let { result ->
        AlertDialog(
            onDismissRequest = { deleteCandidate = null },
            icon = { Icon(Icons.Rounded.Delete, contentDescription = null, tint = StudioRose) },
            title = { Text(stringResource(R.string.delete_history_item_title), fontWeight = FontWeight.Black) },
            text = { Text(stringResource(R.string.delete_history_item_body)) },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(result.id)
                        deleteCandidate = null
                        Toast.makeText(context, resources.getString(R.string.history_deleted), Toast.LENGTH_SHORT).show()
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioRose, contentColor = Color.White),
                ) {
                    Text(stringResource(R.string.delete), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { deleteCandidate = null }, shape = CircleShape) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionTitle(icon = Icons.AutoMirrored.Rounded.ViewQuilt, title = stringResource(R.string.history_timeline))
        Text(
            stringResource(R.string.history_saved_count, historyItems.size),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (historyItems.isEmpty()) {
            EmptyPortfolio(onCreate = onCreate, onExplore = onExplore)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                groupedHistory.forEach { section ->
                    HistoryTimelineGroup(
                        section = section,
                        favoriteResultIds = favoriteResultIds,
                        onOpen = { result ->
                            if (!onOpen(result.id)) {
                                Toast.makeText(context, resources.getString(R.string.history_open_failed), Toast.LENGTH_SHORT).show()
                            }
                        },
                        onFavorite = { result ->
                            val favorite = onFavorite(result.id)
                            Toast.makeText(
                                context,
                                resources.getString(if (favorite) R.string.history_favorited else R.string.history_unfavorited),
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                        onSaveToProject = { result ->
                            val project = onSaveToProject(result.id)
                            Toast.makeText(
                                context,
                                if (project != null) {
                                    resources.getString(R.string.history_saved_to_project, project.name)
                                } else {
                                    resources.getString(R.string.toast_design_save_failed)
                                },
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                        onDelete = { result -> deleteCandidate = result },
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryTimelineGroup(
    section: HistoryTimelineSection,
    favoriteResultIds: Set<String>,
    onOpen: (GeneratedResult) -> Unit,
    onFavorite: (GeneratedResult) -> Unit,
    onSaveToProject: (GeneratedResult) -> Unit,
    onDelete: (GeneratedResult) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 2.dp),
        ) {
            Surface(shape = CircleShape, color = StudioBlue) {
                Box(Modifier.size(9.dp))
            }
            Text(
                historyBucketLabel(section.bucket),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = StudioInk,
            )
            Text(
                section.items.size.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            section.items.forEach { result ->
                HistoryTimelineItem(
                    result = result,
                    favorite = result.id in favoriteResultIds,
                    onOpen = { onOpen(result) },
                    onFavorite = { onFavorite(result) },
                    onSaveToProject = { onSaveToProject(result) },
                    onDelete = { onDelete(result) },
                )
            }
        }
    }
}

@Composable
private fun HistoryTimelineItem(
    result: GeneratedResult,
    favorite: Boolean,
    onOpen: () -> Unit,
    onFavorite: () -> Unit,
    onSaveToProject: () -> Unit,
    onDelete: () -> Unit,
) {
    val toolTitle = boardToolTitleRes(result.toolTitle)?.let { stringResource(it) } ?: result.toolTitle
    val styleLabel = result.style.ifBlank { result.palette }
        .takeIf { it.isNotBlank() }
        ?.let { localizedOption(it) }
    val detail = listOfNotNull(styleLabel, result.budgetLabel.takeIf { it.isNotBlank() })
        .joinToString(" - ")
        .ifBlank { stringResource(R.string.history_style_budget_unavailable) }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(StudioMist),
                ) {
                    HistoryThumbnail(result = result, contentDescription = toolTitle, modifier = Modifier.fillMaxSize())
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(toolTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(detail, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(formatHistoryItemDate(result.createdAt), color = StudioInk.copy(alpha = 0.72f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HistoryQuickAction(Icons.Rounded.Visibility, stringResource(R.string.open), onOpen, Modifier.weight(1f))
                HistoryQuickAction(Icons.Rounded.Star, stringResource(if (favorite) R.string.favorited else R.string.favorite), onFavorite, Modifier.weight(1f), active = favorite)
                HistoryQuickAction(Icons.Rounded.Save, stringResource(R.string.project), onSaveToProject, Modifier.weight(1f))
                HistoryQuickAction(Icons.Rounded.Delete, stringResource(R.string.delete), onDelete, Modifier.weight(1f), danger = true)
            }
        }
    }
}

@Composable
private fun HistoryThumbnail(
    result: GeneratedResult,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val imageUri = result.imageUri?.takeIf { it.isNotBlank() }?.let(Uri::parse)
    if (imageUri != null) {
        UriOrResourceImage(
            uri = imageUri,
            imageRes = R.drawable.sample_after_luxury,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    } else {
        NetworkOrResourceImage(
            imageUrl = result.imageUrl,
            imageRes = R.drawable.sample_after_luxury,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}

@Composable
private fun HistoryQuickAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    danger: Boolean = false,
) {
    val container = when {
        danger -> StudioErrorContainer
        active -> StudioProContainer
        else -> StudioMist
    }
    val content = when {
        danger -> StudioRose
        active -> StudioGold
        else -> StudioInk
    }
    Column(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(shape = CircleShape, color = container) {
            Icon(icon, contentDescription = label, tint = content, modifier = Modifier.padding(6.dp).size(16.dp))
        }
        Spacer(Modifier.height(3.dp))
        Text(
            label,
            color = content,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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
                    title = stringResource(R.string.pro),
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
private fun ProjectsWorkspaceSection(
    state: HomeDecorUiState,
    onCreateProject: () -> Unit,
    onOpenProject: (Project) -> Unit,
    onCreateDesign: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ProfileSectionTitle(icon = Icons.Rounded.Layers, title = stringResource(R.string.room_projects))
            FilledIconButton(
                onClick = onCreateProject,
                modifier = Modifier.size(48.dp),
                colors = androidx.compose.material3.IconButtonDefaults.filledIconButtonColors(
                    containerColor = StudioPrimaryContainer,
                    contentColor = StudioBlue,
                ),
            ) {
                Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.project_create_title))
            }
        }
        Text(
            stringResource(R.string.room_projects_body),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (state.workspace.projects.isEmpty()) {
            EmptyProjects(onCreateProject = onCreateProject, onCreateDesign = onCreateDesign)
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 4.dp),
            ) {
                items(state.workspace.projects, key = { it.id }) { project ->
                    ProjectCard(
                        project = project,
                        state = state,
                        onClick = { onOpenProject(project) },
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyProjects(
    onCreateProject: () -> Unit,
    onCreateDesign: () -> Unit,
) {
    val samples = sampleProjectCards()
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = CircleShape, color = StudioPrimaryContainer) {
                    Icon(Icons.Rounded.Layers, null, Modifier.padding(11.dp).size(22.dp), tint = StudioBlue)
                }
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.empty_projects_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(stringResource(R.string.empty_projects_body), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(items = samples, key = { it.title }) { sample ->
                    SampleCollectionPreview(sample)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onCreateProject,
                    shape = CircleShape,
                    colors = studioPrimaryButtonColors(),
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.empty_action_start_project), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(
                    onClick = onCreateDesign,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                ) {
                    Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.try_with_example), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    state: HomeDecorUiState,
    onClick: () -> Unit,
) {
    val projectResults = state.workspace.generatedResults.filter { it.projectId == project.id }
    val favoriteCount = state.workspace.favorites.count { it.projectId == project.id }
    val originalCount = project.originalPhotoUris.size + project.originalPhotoUrls.size
    val coverUrl = project.coverImageUrl ?: projectResults.firstOrNull()?.imageUrl
    val coverUri = project.coverImageUri ?: projectResults.firstOrNull()?.imageUri ?: project.originalPhotoUris.firstOrNull()
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        modifier = Modifier.width(260.dp),
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(150.dp)) {
                WorkspaceImage(
                    imageUrl = coverUrl,
                    imageUri = coverUri,
                    contentDescription = project.name,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.62f)))))
                Text(
                    project.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.BottomStart).padding(14.dp),
                )
            }
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    project.roomType.ifBlank { stringResource(R.string.project_room_unspecified) },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProjectMetricChip(Icons.Rounded.PhotoCamera, originalCount.toString())
                    ProjectMetricChip(Icons.AutoMirrored.Rounded.ViewQuilt, projectResults.size.toString())
                    ProjectMetricChip(Icons.Rounded.Star, favoriteCount.toString())
                }
                Text(
                    stringResource(R.string.project_created, formatProjectDate(project.createdAt)),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ProjectMetricChip(
    icon: ImageVector,
    label: String,
) {
    Surface(shape = CircleShape, color = StudioMist) {
        Row(
            Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(icon, null, Modifier.size(14.dp), tint = StudioBlue)
            Text(label, style = MaterialTheme.typography.labelSmall, color = StudioInk, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProjectDetailDialog(
    project: Project,
    state: HomeDecorUiState,
    onDismiss: () -> Unit,
    onUpdate: (Project) -> Unit,
    onCreateDesign: () -> Unit,
) {
    val projectResults = state.workspace.generatedResults.filter { it.projectId == project.id }
    val projectFavorites = state.workspace.favorites.filter { it.projectId == project.id }
    var name by remember(project.id) { mutableStateOf(project.name) }
    var roomType by remember(project.id) { mutableStateOf(project.roomType) }
    var notes by remember(project.id) { mutableStateOf(project.notes) }
    var styleInfo by remember(project.id) { mutableStateOf(project.styleInfo) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.project_workspace_title), fontWeight = FontWeight.Black) },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 520.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    ProjectHeaderPreview(project = project, results = projectResults)
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ProjectMetricChip(Icons.Rounded.PhotoCamera, stringResource(R.string.project_originals_count, project.originalPhotoUris.size + project.originalPhotoUrls.size))
                        ProjectMetricChip(Icons.AutoMirrored.Rounded.ViewQuilt, stringResource(R.string.project_results_count, projectResults.size))
                        ProjectMetricChip(Icons.Rounded.Star, stringResource(R.string.project_favorites_count, projectFavorites.size))
                    }
                }
                item {
                    Text(stringResource(R.string.project_created, formatProjectDate(project.createdAt)), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.project_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        singleLine = true,
                    )
                }
                item {
                    OutlinedTextField(
                        value = roomType,
                        onValueChange = { roomType = it },
                        label = { Text(stringResource(R.string.room_type)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        singleLine = true,
                    )
                }
                item {
                    OutlinedTextField(
                        value = styleInfo,
                        onValueChange = { styleInfo = it },
                        label = { Text(stringResource(R.string.project_style_info)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                    )
                }
                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text(stringResource(R.string.project_notes)) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp),
                        shape = RoundedCornerShape(18.dp),
                        minLines = 3,
                    )
                }
                if (projectResults.isNotEmpty()) {
                    item {
                        Text(stringResource(R.string.project_generated_results), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(projectResults, key = { it.id }) { item ->
                                ProjectResultThumb(title = item.toolTitle, imageUrl = item.imageUrl, imageUri = item.imageUri)
                            }
                        }
                    }
                }
                if (projectFavorites.isNotEmpty()) {
                    item {
                        Text(stringResource(R.string.project_favorites), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(projectFavorites, key = { it.id }) { item ->
                                ProjectResultThumb(title = item.title, imageUrl = item.imageUrl, imageUri = item.imageUri, imageRes = item.imageRes)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdate(project.copy(name = name.ifBlank { project.name }, roomType = roomType, notes = notes, styleInfo = styleInfo))
                    onDismiss()
                },
                shape = CircleShape,
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onCreateDesign, shape = CircleShape) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.create_design))
            }
        },
    )
}

@Composable
private fun ProjectHeaderPreview(
    project: Project,
    results: List<com.ismail.homedecorai.GeneratedResult>,
) {
    val coverUrl = project.coverImageUrl ?: results.firstOrNull()?.imageUrl
    val coverUri = project.coverImageUri ?: results.firstOrNull()?.imageUri ?: project.originalPhotoUris.firstOrNull()
    Surface(shape = RoundedCornerShape(24.dp), color = StudioMist, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth().height(180.dp)) {
            WorkspaceImage(
                imageUrl = coverUrl,
                imageUri = coverUri,
                contentDescription = project.name,
                modifier = Modifier.fillMaxSize(),
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.68f)))))
            Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(project.name, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(project.roomType.ifBlank { stringResource(R.string.project_room_unspecified) }, color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun ProjectResultThumb(
    title: String,
    imageUrl: String?,
    imageUri: String?,
    imageRes: Int = R.drawable.profile_workspace,
) {
    Surface(shape = RoundedCornerShape(18.dp), color = StudioPaper, tonalElevation = 1.dp, modifier = Modifier.width(124.dp)) {
        Column {
            WorkspaceImage(
                imageUrl = imageUrl,
                imageUri = imageUri,
                imageRes = imageRes,
                contentDescription = title,
                modifier = Modifier.fillMaxWidth().height(104.dp),
            )
            Text(
                title,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ProjectEditorDialog(
    title: String,
    confirmLabel: String,
    initialName: String,
    initialRoomType: String,
    initialNotes: String,
    initialStyleInfo: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit,
) {
    var name by remember { mutableStateOf(initialName) }
    var roomType by remember { mutableStateOf(initialRoomType) }
    var notes by remember { mutableStateOf(initialNotes) }
    var styleInfo by remember { mutableStateOf(initialStyleInfo) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.project_name)) },
                    placeholder = { Text(stringResource(R.string.project_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = roomType,
                    onValueChange = { roomType = it },
                    label = { Text(stringResource(R.string.room_type)) },
                    placeholder = { Text(stringResource(R.string.project_room_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = styleInfo,
                    onValueChange = { styleInfo = it },
                    label = { Text(stringResource(R.string.project_style_info)) },
                    placeholder = { Text(stringResource(R.string.project_style_info_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(stringResource(R.string.project_notes)) },
                    placeholder = { Text(stringResource(R.string.project_notes_hint)) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp),
                    shape = RoundedCornerShape(18.dp),
                    minLines = 3,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name.trim(), roomType.trim(), notes.trim(), styleInfo.trim()) },
                enabled = name.trim().length >= 2,
                shape = CircleShape,
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
private fun WorkspaceImage(
    imageUrl: String?,
    imageUri: String?,
    imageRes: Int = R.drawable.profile_workspace,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    if (!imageUri.isNullOrBlank()) {
        UriOrResourceImage(
            uri = runCatching { Uri.parse(imageUri) }.getOrNull(),
            imageRes = imageRes,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    } else {
        NetworkOrResourceImage(
            imageUrl = imageUrl,
            imageRes = imageRes,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}

private fun formatProjectDate(createdAt: Long): String =
    java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(java.util.Date(createdAt))

@Composable
private fun EmptyPortfolio(
    onCreate: () -> Unit,
    onExplore: () -> Unit,
) {
    CollectionEmptyState(
        icon = Icons.Rounded.Diamond,
        title = stringResource(R.string.empty_history_title),
        body = stringResource(R.string.empty_history_body),
        primaryLabel = stringResource(R.string.try_with_example),
        onPrimary = onCreate,
        secondaryLabel = stringResource(R.string.empty_action_explore_discover),
        onSecondary = onExplore,
        samples = sampleHistoryCards(),
    )
}

private enum class HistoryBucket { Today, Yesterday, ThisWeek, Older }

private data class HistoryTimelineSection(
    val bucket: HistoryBucket,
    val items: List<GeneratedResult>,
)

private fun groupHistoryResults(results: List<GeneratedResult>): List<HistoryTimelineSection> {
    if (results.isEmpty()) return emptyList()
    val zone = ZoneId.systemDefault()
    val today = LocalDate.now(zone)
    return results
        .groupBy { result ->
            val date = LocalDate.ofInstant(java.time.Instant.ofEpochMilli(result.createdAt), zone)
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
private fun historyBucketLabel(bucket: HistoryBucket): String = stringResource(
    when (bucket) {
        HistoryBucket.Today -> R.string.history_today
        HistoryBucket.Yesterday -> R.string.history_yesterday
        HistoryBucket.ThisWeek -> R.string.history_this_week
        HistoryBucket.Older -> R.string.history_older
    }
)

private fun formatHistoryItemDate(createdAt: Long): String {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(createdAt))
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = if (enabled) Color.Unspecified else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        supportingContent = {
            Text(
                subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            Surface(shape = CircleShape, color = if (enabled) StudioPrimaryContainer else StudioMist) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (enabled) StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(9.dp).size(20.dp),
                )
            }
        },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp),
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = if (enabled) StudioPaper else StudioMist,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .minimumTouchTarget()
            .clip(RoundedCornerShape(18.dp))
            .disabledSemantics(enabled)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    )
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
            when {
                failed -> ImageFailureState(modifier = Modifier.fillMaxSize())
                ready -> WorkspaceImage(
                    imageUrl = item.imageUrl,
                    imageUri = item.imageUri,
                    imageRes = item.imageRes,
                    contentDescription = toolTitle,
                    modifier = Modifier.fillMaxSize(),
                )
                else -> ImageLoadingState(modifier = Modifier.fillMaxSize())
            }
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
    var loading by remember(imageUrl) { mutableStateOf(!imageUrl.isNullOrBlank()) }
    var failed by remember(imageUrl) { mutableStateOf(false) }
    LaunchedEffect(imageUrl) {
        bitmap = null
        failed = false
        loading = !imageUrl.isNullOrBlank()
        if (!imageUrl.isNullOrBlank()) {
            bitmap = withContext(Dispatchers.IO) {
                runCatching {
                    URL(imageUrl).openStream().use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
                }.getOrNull()
            }
            failed = bitmap == null
        }
        loading = false
    }
    when {
        bitmap != null -> {
            Image(
                bitmap = bitmap!!,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop,
            )
        }
        loading -> {
            ImageLoadingState(modifier = modifier)
        }
        failed -> {
            ImageFailureState(modifier = modifier)
        }
        else -> {
            Image(
                painter = painterResource(imageRes),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun ImageLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(StudioMist),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Surface(shape = CircleShape, color = StudioPaper) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.padding(10.dp).size(20.dp), tint = StudioBlue)
            }
            Text(
                stringResource(R.string.image_loading_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = StudioInk,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(0.72f).height(6.dp).clip(CircleShape),
                color = StudioBlue,
                trackColor = StudioLine,
            )
            Text(
                stringResource(R.string.image_loading_body),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ImageFailureState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(StudioErrorContainer),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(28.dp), tint = StudioRose)
            Text(stringResource(R.string.image_unavailable_title), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = StudioRose, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text(stringResource(R.string.image_unavailable_body), style = MaterialTheme.typography.bodySmall, color = StudioInk, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
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
    val resources = LocalResources.current
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
            message = resources.getString(R.string.subscriptions_unavailable)
            return
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                offering = offerings.current ?: offerings.all.values.firstOrNull()
                offeringsLoading = false
                if (offering == null) {
                    message = resources.getString(R.string.no_subscription_available)
                }
            }

            override fun onError(error: PurchasesError) {
                offeringsLoading = false
                message = resources.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.load_prices_failed))
            }
        })
    }

    LaunchedEffect(Unit) {
        loadOfferings()
    }

    fun buy(packageToPurchase: Package?, subscriptionType: String, entitlement: String) {
        val activity = context.findActivity()
        if (packageToPurchase == null || activity == null || !Purchases.isConfigured) {
            message = resources.getString(R.string.plan_unavailable)
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
                    resources.getString(R.string.purchase_cancelled)
                } else {
                    resources.getString(rawServiceMessageToKind(context, error.message).purchaseAttemptMessageRes(R.string.purchase_failed))
                }
            }
        })
    }

    val weeklyPackage = offering?.weekly ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.WEEKLY }
    val monthlyPackage = offering?.monthly ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.MONTHLY }
    val yearlyPackage = offering?.annual ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.ANNUAL }
    val purchaseBusy = purchasing || restoring || state.purchaseBusy
    val pricesUnavailable = !offeringsLoading && weeklyPackage == null && monthlyPackage == null && yearlyPackage == null
    val availablePlans = listOfNotNull(
        yearlyPackage?.let {
            PaywallPlan(
                id = "yearly",
                label = stringResource(R.string.paywall_access_yearly),
                price = it.product.price.formatted,
                detail = stringResource(R.string.paywall_billed_yearly, it.product.price.formatted),
                badge = stringResource(R.string.paywall_best_offer),
                packageToPurchase = it,
                entitlement = "annual_pro",
            )
        },
        weeklyPackage?.let {
            PaywallPlan(
                id = "weekly",
                label = stringResource(R.string.paywall_access_weekly),
                price = it.product.price.formatted,
                detail = stringResource(R.string.paywall_flexible_access),
                badge = null,
                packageToPurchase = it,
                entitlement = "weekly_pro",
            )
        },
        monthlyPackage?.let {
            PaywallPlan(
                id = "monthly",
                label = stringResource(R.string.paywall_access_monthly),
                price = it.product.price.formatted,
                detail = stringResource(R.string.paywall_billed_monthly),
                badge = null,
                packageToPurchase = it,
                entitlement = "monthly_pro",
            )
        },
    )

    LaunchedEffect(offeringsLoading, weeklyPackage, monthlyPackage, yearlyPackage) {
        if (!offeringsLoading) {
            selectedPlan = when {
                selectedPlan == "yearly" && yearlyPackage != null -> "yearly"
                selectedPlan == "monthly" && monthlyPackage != null -> "monthly"
                selectedPlan == "weekly" && weeklyPackage != null -> "weekly"
                yearlyPackage != null -> "yearly"
                weeklyPackage != null -> "weekly"
                monthlyPackage != null -> "monthly"
                else -> selectedPlan
            }
        }
    }

    val selectedPlanModel = availablePlans.firstOrNull { it.id == selectedPlan } ?: availablePlans.firstOrNull()
    val selectedPackage = selectedPlanModel?.packageToPurchase
    val selectedSubscriptionType = selectedPlanModel?.id ?: selectedPlan
    val selectedEntitlement = selectedPlanModel?.entitlement ?: "pro"

    Box(
        Modifier
            .fillMaxSize()
            .background(PaywallBg)
            .clickable(
                interactionSource = modalTapBlocker,
                indication = null,
                onClick = {},
            ),
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(1f)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 10.dp, end = 18.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.50f)),
        ) {
            Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close), tint = Color.White)
        }
        LazyColumn(
            contentPadding = PaddingValues(bottom = 34.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                PaywallHeroCarousel()
            }
            item {
                Column(
                    Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp),
                ) {
                    Text(stringResource(R.string.paywall_eyebrow), color = PaywallPremiumGold, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                    Text(
                        stringResource(R.string.paywall_pro_studio_title),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        lineHeight = 35.sp,
                    )
                    Text(
                        stringResource(R.string.paywall_pro_studio_subtitle),
                        color = PaywallTextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            item {
                Column(
                    Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PaywallOutcomeCard(stringResource(R.string.paywall_outcome_4k_title), stringResource(R.string.paywall_outcome_4k_body))
                    PaywallOutcomeCard(stringResource(R.string.paywall_outcome_watermark_title), stringResource(R.string.paywall_outcome_watermark_body))
                    PaywallOutcomeCard(stringResource(R.string.paywall_outcome_priority_title), stringResource(R.string.paywall_outcome_priority_body))
                }
            }
            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PaywallCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PaywallBorder),
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(stringResource(R.string.paywall_included), color = PaywallTextSecondary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                        FeatureRowOnDark(Icons.Rounded.AutoAwesome, stringResource(R.string.paywall_feature_generations))
                        FeatureRowOnDark(Icons.Rounded.Download, stringResource(R.string.paywall_feature_watermark))
                        FeatureRowOnDark(Icons.Rounded.Refresh, stringResource(R.string.paywall_feature_fast))
                        FeatureRowOnDark(Icons.Rounded.Save, stringResource(R.string.paywall_feature_history))
                    }
                }
            }
            item {
                Surface(
                    shape = CircleShape,
                    color = PaywallAccent.copy(alpha = 0.16f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PaywallAccent.copy(alpha = 0.45f)),
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null, tint = PaywallPremiumGold, modifier = Modifier.size(18.dp))
                        Text(stringResource(R.string.paywall_google_play_checkout), color = PaywallTextSecondary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            if (offeringsLoading) {
                item {
                    Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = PaywallPremiumGold, strokeWidth = 2.dp)
                            Text(stringResource(R.string.loading_prices), color = PaywallTextSecondary, style = MaterialTheme.typography.bodyMedium)
                        }
                        repeat(2) {
                            PaywallPlanSkeleton()
                        }
                    }
                }
            }
            if (!offeringsLoading && !pricesUnavailable) {
                items(availablePlans, key = { it.id }) { plan ->
                    PaywallPlanCard(
                        plan = plan,
                        selected = plan.id == selectedPlanModel?.id,
                        enabled = !purchaseBusy,
                        onClick = { selectedPlan = plan.id },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
            if (pricesUnavailable) {
                item {
                    PaywallPricesFallback(
                        message = message ?: resources.getString(R.string.load_prices_failed),
                        retrying = offeringsLoading,
                        onRetry = { loadOfferings() },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
            if (!pricesUnavailable && (message != null || state.purchaseMessage != null)) {
                item {
                    val displayMessage = message ?: state.purchaseMessage.orEmpty()
                    if (state.pendingPurchaseSync != null && message == null) {
                        PurchaseSyncNotice(
                            message = displayMessage,
                            pending = true,
                            busy = state.purchaseBusy,
                            onRetry = onRetrySync,
                        )
                    } else {
                        Text(displayMessage, color = PaywallPremiumGold, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 20.dp))
                    }
                }
            }
            item {
                Column(
                    Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = { buy(selectedPackage, selectedSubscriptionType, selectedEntitlement) },
                        enabled = !offeringsLoading && !purchaseBusy && selectedPackage != null,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PaywallPremiumGold,
                            contentColor = PaywallBg,
                            disabledContainerColor = PaywallDisabledButton,
                            disabledContentColor = PaywallDisabledText,
                        ),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                    ) {
                        Text(
                            when {
                                purchaseBusy -> stringResource(R.string.processing_ellipsis)
                                offeringsLoading -> stringResource(R.string.loading_ellipsis)
                                state.isPro -> stringResource(R.string.pro_activated)
                                selectedPackage != null -> stringResource(R.string.paywall_start_trial)
                                else -> stringResource(R.string.prices_unavailable)
                            },
                            fontWeight = FontWeight.Black,
                        )
                    }
                    Text(
                        if (pricesUnavailable) stringResource(R.string.paywall_restore_or_retry) else stringResource(R.string.paywall_cta_fine_print),
                        color = PaywallTextMuted,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            item {
                Surface(
                    onClick = onStore,
                    enabled = !purchaseBusy,
                    shape = RoundedCornerShape(16.dp),
                    color = PaywallCardAlt,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PaywallBorder),
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().heightIn(min = 58.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Surface(shape = CircleShape, color = PaywallAccent.copy(alpha = 0.20f)) {
                            Icon(Icons.Rounded.Diamond, null, Modifier.padding(9.dp).size(18.dp), tint = PaywallPremiumGold)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(stringResource(R.string.paywall_diamond_store_title), color = Color.White, fontWeight = FontWeight.Black)
                            Text(stringResource(R.string.paywall_diamond_store_subtitle), color = PaywallTextMuted, style = MaterialTheme.typography.labelMedium)
                        }
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = PaywallTextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = {
                            if (!Purchases.isConfigured) {
                                message = resources.getString(R.string.restore_unavailable)
                            } else {
                                restoring = true
                                message = null
                                Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
                                    override fun onReceived(customerInfo: CustomerInfo) {
                                        restoring = false
                                        val active = customerInfo.entitlements.active.values.firstOrNull()
                                        if (active != null) {
                                            val restoredType = when {
                                                active.identifier.contains("annual", ignoreCase = true) -> "yearly"
                                                active.identifier.contains("year", ignoreCase = true) -> "yearly"
                                                active.identifier.contains("week", ignoreCase = true) -> "weekly"
                                                else -> "monthly"
                                            }
                                            onSubscription("pro", restoredType, active.identifier, active.latestPurchaseDate.time.toDouble(), active.expirationDate?.time?.toDouble())
                                        } else {
                                            message = resources.getString(R.string.no_active_pro_purchase)
                                        }
                                    }

                                    override fun onError(error: PurchasesError) {
                                        restoring = false
                                        message = resources.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.restore_failed))
                                    }
                                })
                            }
                        },
                        enabled = !purchaseBusy,
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PaywallTextMuted),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Transparent),
                    ) {
                        Text(if (restoring) stringResource(R.string.restoring) else stringResource(R.string.restore_purchases))
                    }
                }
            }
        }
    }
}

private data class PaywallPlan(
    val id: String,
    val label: String,
    val price: String,
    val detail: String,
    val badge: String?,
    val packageToPurchase: Package,
    val entitlement: String,
)

private val PaywallHeroImages = listOf(
    R.drawable.assets_media_paywall_carouseljapandibedroom,
    R.drawable.assets_media_paywall_carouselluxurymarble,
    R.drawable.assets_media_paywall_paintintroblackmarblesalon,
)

@Composable
private fun PaywallHeroCarousel() {
    var heroIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(3_000)
            heroIndex = (heroIndex + 1) % PaywallHeroImages.size
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(242.dp)
            .background(PaywallBg),
    ) {
        AnimatedContent(
            targetState = PaywallHeroImages[heroIndex],
            label = "paywallHero",
        ) { imageRes ->
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, PaywallBg), startY = 140f)))
        Row(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            PaywallHeroImages.forEachIndexed { index, _ ->
                Box(
                    Modifier
                        .width(if (index == heroIndex) 18.dp else 7.dp)
                        .height(7.dp)
                        .clip(CircleShape)
                        .background(if (index == heroIndex) PaywallPremiumGold else Color.White.copy(alpha = 0.28f)),
                )
            }
        }
    }
}

@Composable
private fun PaywallOutcomeCard(title: String, body: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = PaywallCardAlt,
        border = androidx.compose.foundation.BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier
                    .width(3.dp)
                    .height(48.dp)
                    .clip(CircleShape)
                    .background(PaywallPremiumGold),
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
                Text(body, color = PaywallTextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun PaywallPlanSkeleton() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PaywallCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth().height(74.dp),
    ) {
        Row(Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = PaywallPremiumGold, strokeWidth = 2.dp)
            Spacer(Modifier.width(12.dp))
            Text(stringResource(R.string.loading_prices), color = PaywallTextSecondary)
        }
    }
}

@Composable
private fun PaywallPlanCard(
    plan: PaywallPlan,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (selected) PaywallPremiumGold.copy(alpha = 0.85f) else PaywallBorder
    val backgroundColor = if (selected) PaywallPremiumGold.copy(alpha = 0.10f) else PaywallCard
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(if (plan.id == "yearly") 18.dp else 16.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(if (selected) 1.3.dp else 1.dp, borderColor),
        modifier = modifier.fillMaxWidth().heightIn(min = if (plan.id == "yearly") 96.dp else 72.dp),
    ) {
        Box(Modifier.fillMaxWidth()) {
            if (plan.badge != null) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PaywallPremiumGold.copy(alpha = 0.20f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PaywallPremiumGold.copy(alpha = 0.45f)),
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 10.dp, end = 14.dp),
                ) {
                    Text(
                        plan.badge,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        color = PaywallPremiumGold,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Surface(shape = CircleShape, color = if (selected) PaywallPremiumGold else Color.White.copy(alpha = 0.12f)) {
                    Icon(
                        if (selected) Icons.Rounded.Check else Icons.Rounded.Star,
                        contentDescription = null,
                        tint = if (selected) PaywallBg else PaywallPremiumGold,
                        modifier = Modifier.padding(8.dp).size(18.dp),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(plan.label, color = if (plan.id == "weekly") PaywallTextMuted else PaywallTextSecondary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                    Text(plan.price, color = Color.White, style = if (plan.id == "yearly") MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(plan.detail, color = if (plan.id == "yearly") PaywallSuccess else PaywallTextMuted, style = MaterialTheme.typography.labelMedium)
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
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)),
        modifier = modifier.fillMaxWidth(),
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
            Icon(icon, contentDescription = null, tint = PaywallPremiumGold, modifier = Modifier.padding(8.dp).size(18.dp))
        }
        Text(text, color = PaywallTextSecondary, fontWeight = FontWeight.SemiBold)
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
                        Text(stringResource(R.string.google_initial), color = StudioBlue, fontWeight = FontWeight.Black)
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
    onSubscription: (String, String, String, Double?, Double?) -> Unit,
    onRetrySync: () -> Unit,
    onFeedback: (String) -> Unit,
    onDeleteAccount: () -> Unit,
    onLogout: () -> Unit,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val modalTapBlocker = remember { MutableInteractionSource() }
    var languagePickerVisible by remember { mutableStateOf(false) }
    var feedbackDialogVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }
    var logoutDialogVisible by remember { mutableStateOf(false) }
    var unavailableMessage by remember { mutableStateOf<String?>(null) }
    var restoring by remember { mutableStateOf(false) }
    var settingsMessage by remember { mutableStateOf<String?>(null) }
    val signedIn = !state.viewer.isGuest || state.signedInName != null
    val actionBusy = restoring || state.settingsBusy || state.purchaseBusy
    fun setLinkFailureMessage(opened: Boolean) {
        if (!opened) {
            settingsMessage = resources.getString(R.string.open_link_failed)
        }
    }
    fun showTemporaryUnavailable(message: String = resources.getString(R.string.settings_temporarily_unavailable_body)) {
        unavailableMessage = message
    }
    fun restorePurchases() {
        if (actionBusy) {
            settingsMessage = resources.getString(R.string.settings_action_in_progress)
            return
        }
        if (!Purchases.isConfigured) {
            showTemporaryUnavailable(resources.getString(R.string.restore_unavailable_body))
            return
        }
        restoring = true
        settingsMessage = null
        Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
            override fun onReceived(customerInfo: CustomerInfo) {
                restoring = false
                val active = customerInfo.entitlements.active.values.firstOrNull()
                if (active != null) {
                    settingsMessage = resources.getString(R.string.pro_syncing)
                    onSubscription(
                        "pro",
                        if (active.identifier.contains("annual")) "yearly" else "monthly",
                        active.identifier,
                        active.latestPurchaseDate.time.toDouble(),
                        active.expirationDate?.time?.toDouble(),
                    )
                } else {
                    settingsMessage = resources.getString(R.string.no_active_pro_purchase)
                }
            }

            override fun onError(error: PurchasesError) {
                restoring = false
                settingsMessage = resources.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.restore_failed))
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
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = StudioPaper,
                        tonalElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(Modifier.padding(vertical = 6.dp)) {
                            SettingsRow(
                                Icons.Rounded.Language,
                                stringResource(R.string.language),
                                AppLocale.labelFor(context, currentLanguageTag),
                                onClick = { languagePickerVisible = true },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.AutoMirrored.Rounded.Help,
                                stringResource(R.string.faq),
                                stringResource(R.string.faq_subtitle),
                                onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/faq"))) },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Policy,
                                stringResource(R.string.terms),
                                stringResource(R.string.terms_subtitle),
                                onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/terms"))) },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Lock,
                                stringResource(R.string.privacy_policy),
                                stringResource(R.string.privacy_subtitle),
                                onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/privacy"))) },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.RateReview,
                                stringResource(R.string.feedback),
                                stringResource(R.string.feedback_subtitle),
                                onClick = {
                                    if (actionBusy) settingsMessage = resources.getString(R.string.settings_action_in_progress) else feedbackDialogVisible = true
                                },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Star,
                                stringResource(R.string.restore_purchases),
                                when {
                                    restoring -> stringResource(R.string.restoring)
                                    !Purchases.isConfigured -> stringResource(R.string.restore_unavailable)
                                    else -> stringResource(R.string.restore_purchases_subtitle)
                                },
                                onClick = { restorePurchases() },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Delete,
                                stringResource(R.string.delete_account),
                                stringResource(R.string.delete_account_subtitle),
                                onClick = {
                                    if (actionBusy) settingsMessage = resources.getString(R.string.settings_action_in_progress) else deleteDialogVisible = true
                                },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Person,
                                if (signedIn) stringResource(R.string.log_out) else stringResource(R.string.sign_in),
                                if (signedIn) {
                                    state.signedInEmail ?: stringResource(R.string.logout_subtitle)
                                } else {
                                    stringResource(R.string.sign_in_subtitle)
                                },
                                onClick = {
                                    if (actionBusy) {
                                        settingsMessage = resources.getString(R.string.settings_action_in_progress)
                                    } else if (signedIn) {
                                        logoutDialogVisible = true
                                    } else {
                                        val opened = openUrlSafely(context, appUrl("/sign-in"))
                                        setLinkFailureMessage(opened)
                                        if (opened) onClose()
                                    }
                                },
                            )
                        }
                    }
                }
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
    if (logoutDialogVisible) {
        LogoutDialog(
            onConfirm = {
                logoutDialogVisible = false
                onLogout()
            },
            onDismiss = { logoutDialogVisible = false },
        )
    }
    unavailableMessage?.let { message ->
        TemporaryUnavailableDialog(
            message = message,
            onDismiss = { unavailableMessage = null },
        )
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 72.dp),
        color = StudioLine.copy(alpha = 0.65f),
    )
}

@Composable
private fun TemporaryUnavailableDialog(
    message: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_temporarily_unavailable_title)) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss, shape = CircleShape) {
                Text(stringResource(R.string.close))
            }
        },
    )
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
private fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.logout_title)) },
        text = { Text(stringResource(R.string.logout_body)) },
        confirmButton = {
            Button(onClick = onConfirm, shape = CircleShape) {
                Text(stringResource(R.string.logout_confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = CircleShape) {
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
            Column(
                Modifier
                    .heightIn(max = 520.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val systemSelected = currentLanguageTag == AppLocale.SYSTEM_LANGUAGE_TAG
                Surface(
                    color = if (systemSelected) StudioPrimaryContainer else Color.Transparent,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.language_system_default), fontWeight = FontWeight.Bold) },
                        trailingContent = {
                            if (systemSelected) {
                                Icon(Icons.Rounded.Check, contentDescription = null, tint = StudioBlue)
                            }
                        },
                        modifier = Modifier
                            .minimumTouchTarget()
                            .semantics { this.selected = systemSelected }
                            .clickable(role = Role.Button) { onLanguageSelected(AppLocale.SYSTEM_LANGUAGE_TAG) },
                    )
                }
                AppLocale.supportedLanguages.forEach { language ->
                    val selected = language.tag == currentLanguageTag
                    Surface(
                        color = if (selected) StudioPrimaryContainer else Color.Transparent,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ListItem(
                            headlineContent = { Text(stringResource(language.labelRes), fontWeight = FontWeight.Bold) },
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
    onDailyRewardClaim: () -> Boolean,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
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
            loadError = resources.getString(R.string.store_purchases_unavailable)
            return@LaunchedEffect
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                packages = ((offerings.current?.availablePackages ?: emptyList()) + offerings.all.values.flatMap { it.availablePackages })
                    .distinctBy { "${it.identifier}:${it.product.id}" }
                storeLoading = false
                if (packages.isEmpty()) {
                    loadError = resources.getString(R.string.store_no_packs)
                }
            }

            override fun onError(error: PurchasesError) {
                storeLoading = false
                loadError = resources.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.store_load_packs_failed))
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
            message = resources.getString(R.string.pack_unavailable_google)
            return
        }
        loadingPack = pack.id
        syncingPack = null
        message = null
        Purchases.sharedInstance.purchase(PurchaseParams.Builder(activity, productPackage).build(), object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                loadingPack = null
                syncingPack = pack.id
                message = resources.getString(R.string.purchase_google_confirmed)
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
                    resources.getString(R.string.purchase_cancelled)
                } else {
                    resources.getString(rawServiceMessageToKind(context, error.message).purchaseAttemptMessageRes(R.string.purchase_failed))
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
            .background(Color.Black.copy(alpha = 0.58f))
            .clickable(
                interactionSource = scrimTapBlocker,
                indication = null,
                onClick = onClose,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = PaywallBg,
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
                                    .background(Color.White.copy(alpha = 0.22f)),
                            )
                        }
                    }
                }
                item {
                    ElevatedCard(shape = RoundedCornerShape(20.dp), colors = CardDefaults.elevatedCardColors(containerColor = PaywallCardAlt)) {
                        Row(
                            Modifier.fillMaxWidth().padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            Surface(shape = CircleShape, color = PaywallAccent.copy(alpha = 0.20f)) {
                                Icon(Icons.Rounded.Diamond, null, Modifier.padding(12.dp).size(26.dp), tint = PaywallPremiumGold)
                            }
                            Column(Modifier.weight(1f)) {
                                Text(stringResource(R.string.current_balance), color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                                Text(stringResource(R.string.diamonds_amount, state.diamonds), color = PaywallTextSecondary)
                            }
                            IconButton(onClick = onClose) {
                                Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close), tint = Color.White)
                            }
                        }
                    }
                }
                item {
                    DailyRewardCard(
                        state = state,
                        onClaim = onDailyRewardClaim,
                        dark = true,
                    )
                }
                item {
                    Text(stringResource(R.string.get_more_credits), color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
                if (storeLoading) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = PaywallPremiumGold, strokeWidth = 2.dp)
                            Text(stringResource(R.string.loading_packs), color = PaywallTextSecondary)
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
                            Text(notice.orEmpty(), color = PaywallTextSecondary)
                        }
                    }
                }
                if (loadError != null) {
                    item {
                        OutlinedButton(
                            onClick = { loadAttempt += 1 },
                            enabled = !storeLoading && loadingPack == null && !state.purchaseBusy,
                            shape = CircleShape,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PaywallTextSecondary),
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
    val titleColor = if (unavailable) PaywallTextMuted else Color.White
    val bodyColor = if (unavailable) PaywallTextMuted else PaywallTextSecondary
    ElevatedCard(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = PaywallCardAlt,
            disabledContainerColor = PaywallCardAlt.copy(alpha = 0.72f),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .border(1.dp, if (unavailable) StudioRose.copy(alpha = 0.36f) else PaywallBorder, RoundedCornerShape(16.dp)),
    ) {
        Row(
            Modifier.fillMaxSize().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(shape = CircleShape, color = if (unavailable) Color.White.copy(alpha = 0.08f) else PaywallAccent.copy(alpha = 0.20f)) {
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(10.dp).size(22.dp), tint = if (unavailable) bodyColor else PaywallPremiumGold)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(R.string.pack_title, packTitle), color = titleColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (packBadge != null) {
                    Surface(shape = RoundedCornerShape(6.dp), color = PaywallPremiumGold.copy(alpha = 0.18f), border = androidx.compose.foundation.BorderStroke(1.dp, PaywallPremiumGold.copy(alpha = 0.40f))) {
                        Text(
                            packBadge,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            color = PaywallPremiumGold,
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = PaywallPremiumGold,
                    contentColor = PaywallBg,
                    disabledContainerColor = PaywallDisabledButton,
                    disabledContentColor = PaywallDisabledText,
                ),
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
        WizardStage.Refine -> if (tool?.id in listOf("reference", "layout")) 2 else if (tool?.id in listOf("garden", "paint", "floor", "replace")) 3 else 4
        WizardStage.Processing -> wizardTotalSteps(tool)
        WizardStage.Result -> wizardTotalSteps(tool)
    }
}

private fun wizardTotalSteps(tool: DecorTool?): Int {
    return when (tool?.id) {
        "garden", "paint", "floor", "replace" -> 3
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
