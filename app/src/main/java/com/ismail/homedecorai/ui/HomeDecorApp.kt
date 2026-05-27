package com.ismail.homedecorai.ui

import android.content.Intent
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.automirrored.rounded.ViewQuilt
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.content.FileProvider
import com.ismail.homedecorai.BoardItem
import com.ismail.homedecorai.BuildConfig
import com.ismail.homedecorai.DecorTool
import com.ismail.homedecorai.DiamondPack
import com.ismail.homedecorai.DiscoverSection
import com.ismail.homedecorai.GalleryItem
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.MainTab
import com.ismail.homedecorai.R
import com.ismail.homedecorai.WizardStage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

private val StudioInk = Color(0xFF11140F)
private val StudioBlue = Color(0xFF435BFF)
private val StudioGreen = Color(0xFF26763A)
private val StudioMint = Color(0xFF94EFE0)
private val StudioMoss = Color(0xFF5B6B74)
private val StudioRose = Color(0xFFE85D76)
private val StudioCanvas = Color(0xFFFCFCFF)
private val StudioPaper = Color(0xFFFFFFFF)
private val StudioMist = Color(0xFFF0F2FF)
private val StudioLine = Color(0xFFD9DEEA)
private val StudioBlack = Color(0xFF080A07)
private val StudioGold = Color(0xFFD8AE48)
private val StudioSky = Color(0xFF59C9F7)
private val StudioViolet = Color(0xFF7367F0)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeDecorApp(viewModel: HomeDecorViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MaterialExpressiveTheme(
        colorScheme = expressiveLightColorScheme().copy(
            primary = StudioBlue,
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE7EAFF),
            onPrimaryContainer = Color(0xFF16226E),
            secondary = StudioMoss,
            tertiary = StudioRose,
            surface = StudioCanvas,
            surfaceContainer = StudioPaper,
            surfaceContainerHigh = StudioMist,
            background = StudioCanvas,
            onSurface = StudioInk,
            onSurfaceVariant = Color(0xFF606575),
            outlineVariant = StudioLine,
        ),
    ) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AppScaffold(state = state, viewModel = viewModel)
        }
    }
}

@Composable
private fun AppScaffold(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    BackHandler(enabled = state.storeVisible) {
        viewModel.closeDiamondStore()
    }
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (state.selectedTab != MainTab.Create && !state.storeVisible) {
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
                    NavItem(MainTab.Tools, state.selectedTab, Icons.Rounded.Home, "Outils", viewModel::selectTab)
                    NavItem(MainTab.ElitePass, state.selectedTab, Icons.Rounded.Star, "Pass Elite", viewModel::selectTab)
                    NavItem(MainTab.Discover, state.selectedTab, Icons.Rounded.Explore, "Découvrir", viewModel::selectTab)
                    NavItem(MainTab.Profile, state.selectedTab, Icons.Rounded.Person, "Profil", viewModel::selectTab)
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

            if (state.storeVisible) {
                DiamondStoreSheet(
                    state = state,
                    onClose = viewModel::closeDiamondStore,
                    onPack = viewModel::buyDiamondPack,
                )
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
            .clip(RoundedCornerShape(20.dp))
            .clickable { onSelect(tab) }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(30.dp)
                .clip(CircleShape)
                .background(if (selected) StudioMint else Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (selected) StudioInk else Color(0xFF49454F),
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) StudioInk else MaterialTheme.colorScheme.onSurfaceVariant,
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
            onPass = { viewModel.selectTab(MainTab.ElitePass) },
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
            Text("Outils", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Surface(
                onClick = onPass,
                shape = CircleShape,
                color = StudioMint,
                tonalElevation = 1.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Icon(Icons.Rounded.Star, null, Modifier.size(16.dp), tint = StudioInk)
                    Text(if (state.isPro) "Pro" else "Passer à Pro", fontWeight = FontWeight.Bold)
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
    Surface(
        onClick = { onClick?.invoke() },
        shape = CircleShape,
        color = if (state.isPro) MaterialTheme.colorScheme.primaryContainer else StudioPaper,
        tonalElevation = 2.dp,
        modifier = Modifier.border(1.dp, StudioLine, CircleShape),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (compact) 10.dp else 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            Icon(Icons.Rounded.Star, null, Modifier.size(17.dp), tint = StudioBlue)
            Text(if (state.isPro) "PRO" else "${state.diamonds}", fontWeight = FontWeight.Bold)
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
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
    ) {
        Box(Modifier.fillMaxWidth().height(394.dp)) {
            Image(
                painter = painterResource(tool.imageRes),
                contentDescription = tool.title,
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
                    tool.title,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    tool.description,
                    color = Color.White.copy(alpha = 0.84f),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onClick,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioMint, contentColor = StudioInk),
                        modifier = Modifier.height(46.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp),
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(17.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Essayez ceci", fontWeight = FontWeight.Black)
                    }
                    Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.18f)) {
                    Text(
                            tool.serviceType,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                    )
                }
                }
            }
        }
    }
}

private data class StepCopy(
    val title: String,
    val body: String,
    val options: List<String> = emptyList(),
)

private fun photoCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "facade" -> StepCopy("Extérieur", "Redessinez et embellissez votre maison.")
        "garden" -> StepCopy("Jardin", "Transformez votre extérieur en espace prêt à vivre.")
        "floor" -> StepCopy("Sol", "Marquez, recolorez et transformez votre espace sans effort.")
        "paint" -> StepCopy("Peinture", "Marquez, recolorez et transformez votre espace sans effort.")
        "replace" -> StepCopy("Objets", "Choisissez une photo avec l'objet que vous voulez remplacer.")
        "reference" -> StepCopy("Référence", "Importez une pièce puis une référence visuelle.")
        else -> StepCopy("Intérieur", "Utilisez une photo nette, bien éclairée et prise assez large.")
    }
}

private fun stepTwoCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "facade" -> StepCopy(
            "Choisissez le type de bâtiment",
            "Sélectionnez la forme architecturale que vous souhaitez repenser.",
            HomeDecorCatalog.buildingTypes,
        )
        "garden" -> StepCopy(
            "Choisissez votre style",
            "Choisissez un style signature pour façonner votre vision du design.",
            listOf("Suggestion IA") + HomeDecorCatalog.styles.take(5),
        )
        "paint" -> StepCopy(
            "Sélectionnez les surfaces à transformer",
            "Choisissez une couleur, une surface et décrivez l'ambiance du mur.",
            HomeDecorCatalog.maskTargets,
        )
        "floor" -> StepCopy(
            "Relooking du sol",
            "Brossez le sol à transformer et ajoutez votre vision.",
            listOf("Brossez le sol à transformer", "Annuler", "Effacer", "Aperçu"),
        )
        "layout" -> StepCopy(
            "Choisissez votre objectif",
            "Optimisez l'agencement sans perdre la structure de la pièce.",
            HomeDecorCatalog.layoutGoals,
        )
        "replace" -> StepCopy(
            "Marquez l'objet",
            "Masquez l'objet à remplacer et gardez le reste de la photo intact.",
            HomeDecorCatalog.maskTargets,
        )
        "reference" -> StepCopy(
            "Ajoutez une référence",
            "Choisissez l'intensité du transfert de style.",
            HomeDecorCatalog.referenceStrengths,
        )
        else -> StepCopy(
            "Choisissez votre espace",
            "Sélectionnez le type de pièce pour adapter le rendu à l'espace.",
            HomeDecorCatalog.rooms,
        )
    }
}

private fun stepThreeCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "paint" -> StepCopy(
            "Harmonie des couleurs",
            "Sélectionnez une couleur ou une suggestion IA.",
            HomeDecorCatalog.palettes,
        )
        "floor" -> StepCopy(
            "Choisir le matériau",
            "Choisissez un matériau. L'IA l'appliquera en respectant la perspective, la lumière et les meubles.",
            HomeDecorCatalog.floorMaterials,
        )
        "replace" -> StepCopy(
            "Décrivez le remplacement",
            "Choisissez ce qui doit remplacer l'objet masqué.",
            listOf("Canapé moderne", "Table en bois", "Étagères intégrées", "Luminaire statement", "Plante", "Prompt personnalisé"),
        )
        "reference" -> StepCopy(
            "Direction du transfert",
            "Appliquez le style de référence tout en conservant la structure.",
            HomeDecorCatalog.styles,
        )
        else -> StepCopy(
            "Choisissez votre style",
            "Choisissez l'ambiance que vous voulez appliquer à la pièce.",
            listOf("Suggestion IA") + HomeDecorCatalog.styles,
        )
    }
}

private fun stepFourCopy(tool: DecorTool): StepCopy {
    return when (tool.id) {
        "facade", "garden", "paint" -> StepCopy("Harmonie des couleurs", "Choisissez une palette avant de générer.")
        "floor" -> StepCopy("Décrivez votre vision", "Décrivez le sol souhaité : matière, teinte, finition et ambiance.")
        "replace" -> StepCopy("Vérifiez l'édition", "Confirmez le remplacement avant de générer.")
        "reference" -> StepCopy("Vérifiez le transfert", "Confirmez la référence et le style avant de générer.")
        "layout" -> StepCopy("Mode de design", "Choisissez le niveau de transformation.")
        else -> StepCopy("Mode de design", "Choisissez un mode et une harmonie de couleurs.")
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
                WizardStage.Photo -> PhotoStep(state = state, viewModel = viewModel)
                WizardStage.Space -> when (state.selectedTool.id) {
                    "floor" -> FloorMaskStep(state = state, viewModel = viewModel)
                    "paint" -> WallSurfaceStep(state = state, viewModel = viewModel)
                    else -> ChoiceStep(
                    eyebrow = "Étape 2/${wizardTotalSteps(state.selectedTool)}",
                    copy = stepTwoCopy(state.selectedTool),
                    selected = state.roomType,
                    onSelect = viewModel::setRoom,
                    onContinue = viewModel::nextStage,
                    visualStyleCards = state.selectedTool.id == "garden",
                    visualBuildingCards = state.selectedTool.id == "facade",
                    )
                }
                WizardStage.Style -> ChoiceStep(
                    eyebrow = "Étape 3/${wizardTotalSteps(state.selectedTool)}",
                    copy = stepThreeCopy(state.selectedTool),
                    selected = state.style,
                    onSelect = viewModel::setStyle,
                    onContinue = viewModel::nextStage,
                    visualStyleCards = state.selectedTool.id in listOf("interior", "facade", "garden", "reference", "floor"),
                )
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
                modifier = Modifier.fillMaxWidth().height(44.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (step > 1) {
                    IconButton(onClick = onBack, modifier = Modifier.size(44.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                } else {
                    CreditPill(state, compact = false, onClick = onCredits)
                }
                Text(
                    workflowTitle(state.selectedTool),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 12.dp).weight(1f),
                )
                IconButton(onClick = onClose, modifier = Modifier.size(44.dp)) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close")
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Étape $step/$totalSteps",
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
    onButton: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 18.dp),
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
        Surface(color = StudioPaper, tonalElevation = 3.dp) {
            Button(
                onClick = onButton,
                enabled = buttonEnabled,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = StudioBlue, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(20.dp).height(58.dp),
            ) {
                Text(buttonLabel, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PhotoStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val context = LocalContext.current
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.setPhoto(uri)
    }
    val referenceLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.setReferencePhoto(uri)
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        if (saved) {
            viewModel.setPhoto(pendingCameraUri)
        }
    }
    val copy = photoCopy(state.selectedTool)
    val hasMainPhoto = state.selectedPhotoUri != null || state.selectedExampleLabel != null
    val hasReferencePhoto = state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null
    val canContinue = hasMainPhoto && (state.selectedTool.id != "reference" || hasReferencePhoto)
    StepScaffold(
        eyebrow = "Étape 1/${wizardTotalSteps(state.selectedTool)}",
        title = if (!hasMainPhoto) copy.title else "Photo ajoutée",
        body = null,
        buttonLabel = "Continuer",
        buttonIcon = Icons.Rounded.Check,
        buttonEnabled = canContinue,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (!hasMainPhoto) {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = Color(0xFFFAFBFF),
                    modifier = Modifier.fillMaxWidth().height(292.dp).border(1.dp, Color(0xFFBFC5D6), RoundedCornerShape(26.dp)),
                ) {
                    Column(
                        Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text("Importez une photo de votre pièce", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Spacer(Modifier.height(12.dp))
                        Text(copy.body, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(18.dp))
                        Button(
                            onClick = { imageLauncher.launch("image/*") },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = StudioBlack, contentColor = Color.White),
                        ) {
                            Text("Importer +")
                        }
                    }
                }
            } else {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Box(Modifier.fillMaxWidth().aspectRatio(1.18f)) {
                        UriOrResourceImage(
                            uri = state.selectedPhotoUri,
                            imageRes = selectedExampleImageRes(state),
                            contentDescription = "Photo ajoutée",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { imageLauncher.launch("image/*") }, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Galerie")
                }
                OutlinedButton(
                    onClick = {
                        val uri = createCameraUri(context)
                        pendingCameraUri = uri
                        cameraLauncher.launch(uri)
                    },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Caméra")
                }
            }
            if (state.selectedTool.id == "reference") {
                ReferenceImagePicker(
                    selectedUri = state.selectedReferenceUri,
                    selectedExample = state.selectedReferenceExampleLabel,
                    onImport = { referenceLauncher.launch("image/*") },
                    onExample = { viewModel.selectReferenceExample("Référence éditoriale") },
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Photos d'exemple", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(examplesForTool(state.selectedTool), key = { "example-${it.label}" }) { photo ->
                        ExamplePhotoCard(
                            photo = photo,
                            selected = state.selectedExampleLabel == photo.label,
                            onClick = { viewModel.selectExamplePhoto(photo.label) },
                        )
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
                Text("Image de référence", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text("Ajoutez le style visuel à appliquer à votre pièce.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = if (selectedUri != null || selectedExample != null) MaterialTheme.colorScheme.primaryContainer else StudioPaper,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().border(1.dp, if (selectedUri != null || selectedExample != null) StudioBlue else StudioLine, RoundedCornerShape(22.dp)),
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
                        contentDescription = "Référence de style",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (selectedUri != null || selectedExample != null) "Référence ajoutée" else "Aucune référence", fontWeight = FontWeight.Black)
                    Text(
                        if (selectedUri != null || selectedExample != null) "Le transfert utilisera cette ambiance." else "Importez une image ou utilisez l'exemple.",
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
                Text("Importer")
            }
            OutlinedButton(onClick = onExample, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Exemple")
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
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = StudioPaper,
        tonalElevation = if (selected) 4.dp else 1.dp,
        modifier = Modifier.width(112.dp).height(104.dp).border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(18.dp)),
    ) {
        Box {
            Image(
                painter = painterResource(photo.imageRes),
                contentDescription = photo.label,
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
                    photo.label,
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
    selected: String,
    onSelect: (String) -> Unit,
    onContinue: () -> Unit,
    visualStyleCards: Boolean = false,
    visualBuildingCards: Boolean = false,
) {
    StepScaffold(
        eyebrow = eyebrow,
        title = copy.title,
        body = copy.body,
        buttonLabel = "Continuer",
        buttonIcon = Icons.Rounded.Check,
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
                        selected = selected == option,
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
                                selected = selected == option,
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
private fun FloorMaskStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    StepScaffold(
        eyebrow = "Etape 2/${wizardTotalSteps(state.selectedTool)}",
        title = "Relooking du sol",
        body = null,
        buttonLabel = if (state.roomType == "Sol marque") "Continuer" else "Marquez le sol pour continuer",
        buttonEnabled = state.roomType == "Sol marque",
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.fillMaxWidth().aspectRatio(0.98f).clip(RoundedCornerShape(22.dp))) {
                Image(
                    painter = painterResource(selectedExampleImageRes(state)),
                    contentDescription = "Sol a transformer",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                if (state.roomType == "Sol marque") {
                    Box(
                        Modifier
                            .align(Alignment.Center)
                            .width(64.dp)
                            .height(42.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFF24A5FF).copy(alpha = 0.82f)),
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { viewModel.setRoom("Sol marque") },
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Icon(Icons.Rounded.Brush, null, Modifier.size(18.dp), tint = StudioBlue)
                    Spacer(Modifier.width(8.dp))
                    Text("Brossez le sol à transformer", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                FilledIconButton(onClick = { viewModel.setRoom("Choix de l'IA") }, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.Refresh, null)
                }
                FilledIconButton(onClick = { viewModel.setRoom("Choix de l'IA") }, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Rounded.Close, null)
                }
            }
            LinearProgressIndicator(
                progress = { if (state.roomType == "Sol marque") 0.42f else 0.32f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = Color(0xFF5AC7F5),
                trackColor = StudioLine,
            )
            Text(
                "La détection automatique n'a pas trouvé le sol. Peignez simplement la zone à relooker.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedTextField(
                value = state.customPrompt,
                onValueChange = viewModel::setCustomPrompt,
                label = { Text("Décrivez votre vision") },
                placeholder = { Text("Choix de l'IA") },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth().height(104.dp),
            )
        }
    }
}

@Composable
private fun WallSurfaceStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    StepScaffold(
        eyebrow = "Etape 2/${wizardTotalSteps(state.selectedTool)}",
        title = "Sélectionnez les surfaces à transformer",
        body = null,
        buttonLabel = if (state.roomType == "Mur marqué") "Continuer" else "Marquez le mur pour continuer",
        buttonEnabled = state.roomType == "Mur marqué",
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Image(
                painter = painterResource(selectedExampleImageRes(state)),
                contentDescription = "Mur a transformer",
                modifier = Modifier.fillMaxWidth().aspectRatio(1.08f).clip(RoundedCornerShape(22.dp)),
                contentScale = ContentScale.Crop,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SurfacePanel(
                    title = "Couleur",
                    icon = Icons.Rounded.Brush,
                    selected = true,
                    primary = state.palette.ifBlank { "Suggestion IA" },
                    onPrimary = { viewModel.setPalette("Suggestion IA") },
                    onMagic = { viewModel.setPalette("Suggestion IA") },
                    modifier = Modifier.weight(1f),
                )
                SurfacePanel(
                    title = "Surface",
                    icon = Icons.Rounded.Layers,
                    selected = state.roomType == "Mur marqué",
                    primary = if (state.roomType == "Mur marqué") "Mur" else "Choisir",
                    onPrimary = { viewModel.setRoom("Mur marqué") },
                    onMagic = { viewModel.setRoom("Mur marqué") },
                    modifier = Modifier.weight(1f),
                )
            }
            OutlinedTextField(
                value = state.customPrompt,
                onValueChange = viewModel::setCustomPrompt,
                label = { Text("Décrivez votre vision") },
                placeholder = { Text("Optionnel : texture, matière ou ambiance du mur") },
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth().height(112.dp),
            )
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
        color = if (selected) Color(0xFFF2FAFF) else StudioPaper,
        tonalElevation = if (selected) 4.dp else 1.dp,
        modifier = modifier.height(146.dp).border(1.dp, if (selected) Color(0xFF59C9F7) else StudioLine, RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(shape = CircleShape, color = StudioPaper) {
                    Icon(icon, null, Modifier.padding(8.dp).size(18.dp), tint = StudioInk)
                }
                Text(title, fontWeight = FontWeight.Black, color = if (selected) Color(0xFF4BBFEB) else StudioInk)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onPrimary,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) StudioBlack else StudioPaper,
                        contentColor = if (selected) Color.White else StudioInk,
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier.height(42.dp).weight(1f),
                ) {
                    Text(primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = onMagic, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, null, tint = StudioBlue)
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
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = StudioPaper,
        tonalElevation = if (selected) 5.dp else 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (large) 162.dp else 164.dp)
            .border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(18.dp)),
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
                        contentDescription = label,
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
                    label,
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
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else StudioPaper,
        tonalElevation = if (selected) 4.dp else 1.dp,
        modifier = modifier.height(78.dp).border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(18.dp)),
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(if (selected) StudioBlue else StudioMist),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (selected) Icons.Rounded.Check else choiceIcon(label),
                    null,
                    Modifier.size(if (selected) 17.dp else 19.dp),
                    tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                label,
                modifier = Modifier.weight(1f),
                style = if (label.length > 12) {
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
private fun RefineStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val copy = stepFourCopy(state.selectedTool)
    StepScaffold(
        eyebrow = "Étape ${wizardStepNumber(state.wizardStage, state.selectedTool)}/${wizardTotalSteps(state.selectedTool)}",
        title = copy.title,
        body = copy.body,
        buttonLabel = "Générer mon design",
        buttonIcon = Icons.Rounded.AutoAwesome,
        onButton = viewModel::generate,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            if (!state.generationError.isNullOrBlank()) {
                Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFE8EA)) {
                    Text(
                        state.generationError,
                        modifier = Modifier.padding(14.dp),
                        color = Color(0xFF9D1D2E),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            if (state.selectedTool.id !in listOf("facade", "garden", "paint")) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Mode de design", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HomeDecorCatalog.designModes.forEach { (mode, description) ->
                            ModeCard(
                                title = mode,
                                description = description,
                                selected = state.designMode == mode,
                                onClick = { viewModel.setDesignMode(mode) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Harmonie des couleurs", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PaletteChoiceCard(
                        label = "Suggestion IA",
                        selected = state.palette == "Suggestion IA",
                        onClick = { viewModel.setPalette("Suggestion IA") },
                        modifier = Modifier.width(112.dp),
                    )
                    Text(
                        "Suggestion IA",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (state.palette == "Suggestion IA") StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (state.palette == "Suggestion IA") FontWeight.Bold else FontWeight.Medium,
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth().height(548.dp),
                    horizontalArrangement = Arrangement.spacedBy(34.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    userScrollEnabled = false,
                ) {
                    items(HomeDecorCatalog.palettes, key = { it }) { palette ->
                        PaletteChoiceCard(
                            label = palette,
                            selected = palette == state.palette,
                            onClick = { viewModel.setPalette(palette) },
                        )
                    }
                }
            }
            OutlinedTextField(
                value = state.customPrompt,
                onValueChange = viewModel::setCustomPrompt,
                label = { Text("Décrivez votre vision") },
                placeholder = { Text("Optionnel : texture, matière ou ambiance.") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(18.dp),
            )
            Surface(shape = RoundedCornerShape(22.dp), color = StudioBlack) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Brief design", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${state.roomType} / ${state.style}", color = Color.White.copy(alpha = 0.82f))
                    Text(state.designMode, color = Color.White.copy(alpha = 0.82f))
                    Text(state.palette, color = Color.White.copy(alpha = 0.72f))
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
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else StudioPaper,
        tonalElevation = if (selected) 4.dp else 1.dp,
        modifier = modifier.height(188.dp).border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(22.dp)),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(shape = CircleShape, color = if (selected) StudioBlue else StudioMist) {
                Icon(
                    if (title.contains("Renovation")) Icons.Rounded.AutoAwesome else Icons.Rounded.Brush,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp).size(22.dp),
                    tint = if (selected) Color.White else StudioInk,
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
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = StudioPaper,
        tonalElevation = if (selected) 5.dp else 1.dp,
        modifier = modifier
            .width(96.dp)
            .height(142.dp)
            .border(1.dp, if (selected) StudioBlue else StudioLine, RoundedCornerShape(18.dp)),
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
                label,
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
        Text("L'IA compose votre chef-d'oeuvre architectural...", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        LinearProgressIndicator(Modifier.fillMaxWidth(0.78f).height(8.dp).clip(CircleShape))
    }
}

@Composable
private fun ResultStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val result = state.board.firstOrNull()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    StepScaffold(
        eyebrow = "Result",
        title = "Concept ${workflowTitle(state.selectedTool)}",
        body = "${state.roomType} en style ${state.style}",
        buttonLabel = "Terminé",
        buttonIcon = Icons.Rounded.Check,
        onButton = { viewModel.selectTab(MainTab.Tools) },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(shape = RoundedCornerShape(28.dp)) {
                NetworkOrResourceImage(
                    imageUrl = result?.imageUrl,
                    imageRes = result?.imageRes ?: R.drawable.sample_after_luxury,
                    contentDescription = "Generated design",
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.78f),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilledIconButton(onClick = viewModel::generate) { Icon(Icons.Rounded.Refresh, contentDescription = "Regenerate") }
                FilledIconButton(
                    onClick = {
                        scope.launch {
                            val saved = saveResultToGallery(context, result)
                            Toast.makeText(context, if (saved) "Design enregistré dans la galerie." else "Impossible d'enregistrer ce design.", Toast.LENGTH_LONG).show()
                        }
                    },
                ) { Icon(Icons.Rounded.Download, contentDescription = "Enregistrer") }
                FilledIconButton(
                    onClick = {
                        scope.launch {
                            val shared = shareResult(context, result)
                            if (!shared) {
                                Toast.makeText(context, "Impossible de partager ce design pour le moment.", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                ) { Icon(Icons.Rounded.Share, contentDescription = "Partager") }
            }
        }
    }
}

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
            context.startActivity(Intent.createChooser(intent, "Partager le design"))
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
        Toast.makeText(context, "Impossible d'ouvrir la connexion pour le moment.", Toast.LENGTH_LONG).show()
    }
}

@Composable
private fun DiscoverScreen(onTool: (DecorTool) -> Unit) {
    var selectedCluster by remember { mutableStateOf("Intérieurs") }
    var detailSection by remember { mutableStateOf<DiscoverSection?>(null) }
    var previewItem by remember { mutableStateOf<GalleryItem?>(null) }
    val clusters = listOf("Intérieurs", "Architecture", "Paysages")
    val sections = HomeDecorCatalog.discoverSections.filter { it.cluster == selectedCluster }
    val activeDetail = detailSection
    if (activeDetail != null) {
        DiscoverDetailScreen(
            section = activeDetail,
            onBack = { detailSection = null },
            onPreview = { previewItem = it },
            onTool = onTool,
        )
        previewItem?.let { item ->
            DiscoverPreviewDialog(
                item = item,
                section = activeDetail,
                onDismiss = { previewItem = null },
                onTool = onTool,
            )
        }
        return
    }
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        ScreenHeaderPills(title = "Découvrir", trailing = null)
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            item { DiscoverHero(section = sections.firstOrNull(), onTool = onTool) }
            item { DiscoverClusterTabs(clusters = clusters, selected = selectedCluster, onSelect = { selectedCluster = it }) }
            items(sections, key = { it.id }) { section ->
                DiscoverSectionRow(
                    section = section,
                    onSeeAll = { detailSection = section },
                    onPreview = { previewItem = it },
                )
            }
        }
    }
    previewItem?.let { item ->
        val section = sections.firstOrNull { candidate -> candidate.items.any { it.id == item.id } } ?: sections.first()
        DiscoverPreviewDialog(
            item = item,
            section = section,
            onDismiss = { previewItem = null },
            onTool = onTool,
        )
    }
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
    onTool: (DecorTool) -> Unit,
) {
    val first = section?.items?.firstOrNull() ?: return
    val tool = HomeDecorCatalog.tools.firstOrNull { it.id == section.serviceToolId }
    ElevatedCard(
        onClick = { tool?.let(onTool) },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
    ) {
        Box(Modifier.fillMaxWidth().height(260.dp)) {
            Image(
                painter = painterResource(first.imageRes),
                contentDescription = first.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.78f)))))
            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Surface(shape = CircleShape, color = StudioMint) {
                    Text(
                        section.cluster,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = StudioInk,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                    )
                }
                Text(section.title, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text("Inspiration prête à transformer en création", color = Color.White.copy(alpha = 0.84f), style = MaterialTheme.typography.bodyMedium)
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
            Surface(
                onClick = { onSelect(cluster) },
                shape = CircleShape,
                color = if (active) StudioBlack else StudioPaper,
                tonalElevation = if (active) 4.dp else 1.dp,
                modifier = Modifier.border(1.dp, if (active) StudioBlack else StudioLine, CircleShape),
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
                        tint = if (active) Color.White else StudioBlue,
                    )
                    Text(cluster, color = if (active) Color.White else StudioInk, fontWeight = FontWeight.Bold)
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(section.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            OutlinedButton(
                onClick = onSeeAll,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text("Voir tout")
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
    onTool: (DecorTool) -> Unit,
) {
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        Row(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Retour")
            }
            Column(Modifier.weight(1f)) {
                Text(section.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                Text(section.cluster, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            val tool = HomeDecorCatalog.tools.firstOrNull { it.id == section.serviceToolId }
            Button(
                onClick = { tool?.let(onTool) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = StudioMint, contentColor = StudioInk),
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(17.dp))
                Spacer(Modifier.width(8.dp))
                Text("Créer", fontWeight = FontWeight.Black)
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
    item: GalleryItem,
    section: DiscoverSection,
    onDismiss: () -> Unit,
    onTool: (DecorTool) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    HomeDecorCatalog.tools.firstOrNull { it.id == section.serviceToolId }?.let(onTool)
                    onDismiss()
                },
                shape = CircleShape,
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(17.dp))
                Spacer(Modifier.width(8.dp))
                Text("Créer avec ce style")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = CircleShape) {
                Text("Fermer")
            }
        },
        title = { Text(item.category, fontWeight = FontWeight.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.92f).clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop,
                )
                Text(item.title, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        modifier = Modifier.width(196.dp),
    ) {
        Box(Modifier.fillMaxWidth().height(250.dp)) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f)))))
            Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                Text(item.category, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 2)
                Text(item.title, color = Color.White.copy(alpha = 0.76f), style = MaterialTheme.typography.labelMedium, maxLines = 1)
            }
            Surface(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.9f)) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.padding(8.dp).size(16.dp), tint = StudioBlue)
            }
        }
    }
}

@Composable
private fun ElitePassScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val completedDays = completedEliteDays(state)
    ScreenColumn(title = "Pass Elite", subtitle = null, trailing = { CreditPill(state, compact = true, onClick = viewModel::openDiamondStore) }) {
        LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            item {
                ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioBlack)) {
                    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("PASS QUOTIDIEN", color = Color(0xFFD6B65D), fontWeight = FontWeight.Bold)
                        Text("Pass Elite", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                        Text(
                            "Récupérez 1 diamant gratuit chaque jour. Le 7e jour, débloquez 24h Pro et 3 diamants pour continuer à créer.",
                            color = Color.White.copy(alpha = 0.78f),
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            repeat(7) { index ->
                                val active = index + 1 <= completedDays
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Surface(shape = CircleShape, color = if (active) Color.White else Color.White.copy(alpha = 0.18f)) {
                                        Text(
                                            "${index + 1}",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                            color = if (active) StudioBlack else Color.White,
                                            fontWeight = FontWeight.Black,
                                        )
                                    }
                                    Text(if (index == 6) "Pro" else "+1", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                        Button(
                            onClick = viewModel::claimDiamond,
                            enabled = !state.claimedToday && (state.diamonds < 3 || state.eliteStreakDay >= 7),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = StudioBlack),
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                        ) {
                            Icon(Icons.Rounded.Star, null)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                when {
                                    state.claimedToday -> "Réclamé aujourd'hui"
                                    state.diamonds >= 3 && state.eliteStreakDay < 7 -> "Plafond quotidien atteint"
                                    state.eliteStreakDay >= 7 -> "Réclamer J7 Pro"
                                    else -> "Réclamer"
                                },
                                fontWeight = FontWeight.Black,
                            )
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
                    Text("Ouvrir la boutique de diamants")
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
    val completedDays = completedEliteDays(state)
    val currentDay = (completedDays + 1).coerceIn(1, 7)
    ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF0F1300))) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Votre série Elite", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Surface(shape = CircleShape, color = StudioMint) {
                    Text(
                        "$completedDays/7",
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        color = StudioInk,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
            LinearProgressIndicator(
                progress = { (completedDays / 7f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                color = StudioBlue,
                trackColor = Color.White.copy(alpha = 0.86f),
            )
            repeat(7) { index ->
                val day = index + 1
                val claimed = day <= completedDays
                val current = day == currentDay && !state.claimedToday
                EliteDayRow(
                    day = day,
                    claimed = claimed,
                    current = current,
                    onClaim = onClaim,
                )
            }
            Text(
                if (state.claimedToday) "Revenez demain pour avancer vers le bonus Pro." else "Réclamez aujourd'hui pour avancer vers le bonus Pro.",
                color = Color.White.copy(alpha = 0.78f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun completedEliteDays(state: HomeDecorUiState): Int {
    return when {
        state.viewer.streakCount > 0 -> state.viewer.streakCount.coerceIn(0, 7)
        else -> (state.eliteStreakDay - 1).coerceIn(0, 7)
    }
}

@Composable
private fun EliteDayRow(
    day: Int,
    claimed: Boolean,
    current: Boolean,
    onClaim: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = when {
            current -> Color(0xFF3A3214)
            claimed -> Color.White.copy(alpha = 0.10f)
            day == 7 -> Color(0xFF302A12)
            else -> Color(0xFF1E1418)
        },
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = if (current) 0.18f else 0.10f)),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(shape = CircleShape, color = if (claimed || current) Color(0xFFD8AE48) else Color.White.copy(alpha = 0.10f)) {
                Icon(
                    if (claimed) Icons.Rounded.Check else if (day == 7) Icons.Rounded.Star else Icons.AutoMirrored.Rounded.ViewQuilt,
                    null,
                    Modifier.padding(9.dp).size(18.dp),
                    tint = if (claimed || current) Color.White else Color.White.copy(alpha = 0.48f),
                )
            }
            Column(Modifier.weight(1f)) {
                Text(if (day == 7) "J7 Pro" else "J$day", color = if (day == 7) Color(0xFFD8AE48) else Color.White, fontWeight = FontWeight.Black)
                Text(
                    when {
                        claimed -> "Réclamé"
                        current -> "Disponible aujourd'hui"
                        else -> "Dans ${day - 1} jours"
                    },
                    color = if (current) Color(0xFFD8AE48) else Color.White.copy(alpha = 0.68f),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            if (current) {
                Button(
                    onClick = onClaim,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8AE48), contentColor = StudioInk),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    modifier = Modifier.height(38.dp),
                ) {
                    Text(if (day == 7) "+3 + Pro" else "+1", fontWeight = FontWeight.Black)
                }
            } else if (day == 7) {
                Text("+3 + Pro", color = Color(0xFFD8AE48), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PaywallCard(onUnlock: () -> Unit) {
    ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Débloquez des designs illimités", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            FeatureRow(Icons.Rounded.AutoAwesome, "Traitement IA prioritaire")
            FeatureRow(Icons.Rounded.Download, "Exports propres en haute qualité")
            FeatureRow(Icons.Rounded.Star, "Générations sans friction")
            Button(onClick = onUnlock, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                Text("Passer à Pro")
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
    Column(Modifier.fillMaxSize().background(StudioCanvas)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = { openAuth(context) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = StudioBlue, contentColor = Color.White),
                modifier = Modifier.height(48.dp),
            ) {
                Text("Se connecter", fontWeight = FontWeight.Black)
            }
            FilledIconButton(onClick = { Toast.makeText(context, "Les paramètres complets arrivent dans la prochaine passe native.", Toast.LENGTH_LONG).show() }, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Rounded.Settings, contentDescription = "Paramètres")
            }
            Spacer(Modifier.weight(1f))
            CreditPill(state, compact = true, onClick = viewModel::openDiamondStore)
        }
        LazyColumn(contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            item { PortfolioHeader() }
            if (state.board.isEmpty()) {
                item { EmptyPortfolio(onCreate = { viewModel.selectTab(MainTab.Tools) }) }
            } else {
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.height(((state.board.size + 1) / 2 * 226).dp),
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
            item {
                ElevatedCard(shape = RoundedCornerShape(28.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
                    Box(Modifier.fillMaxWidth().height(190.dp)) {
                        Image(
                            painter = painterResource(R.drawable.profile_workspace),
                            contentDescription = "Workspace",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        Box(Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.42f)))
                        Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                            Text("Paramètres", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                            Text("Langue, juridique, feedback et achats", color = Color.White.copy(alpha = 0.82f))
                        }
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SettingsRow(Icons.Rounded.Person, "Se connecter avec Clerk", "Retrouver vos designs, crédits et achats", onClick = { openAuth(context) })
                    SettingsRow(Icons.Rounded.Settings, "Langue", "Français", onClick = {
                        Toast.makeText(context, "Français activé. Le sélecteur complet sera relié aux fichiers de traduction.", Toast.LENGTH_LONG).show()
                    })
                    SettingsRow(Icons.Rounded.Star, "Restaurer les achats", "RevenueCat Android SDK", onClick = viewModel::openDiamondStore)
                    SettingsRow(Icons.AutoMirrored.Rounded.ViewQuilt, "Boutique de diamants", "Acheter des crédits de génération", onClick = viewModel::openDiamondStore)
                    SettingsRow(Icons.Rounded.Share, "Partager l'app", "Invitez quelqu'un à essayer HomeDecor AI", onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Essayez HomeDecor AI pour transformer intérieurs, extérieurs, jardins, murs et sols.")
                        }
                        context.startActivity(Intent.createChooser(intent, "Partager HomeDecor AI"))
                    })
                }
            }
            item {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://homedecor.ai"))
                        context.startActivity(intent)
                    },
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Text("Confidentialité et conditions")
                }
            }
            item {
                if (!state.isPro) {
                    Button(onClick = { viewModel.selectTab(MainTab.ElitePass) }, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                        Text("Passer à Pro")
                    }
                }
            }
        }
    }
}

@Composable
private fun PortfolioHeader() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = CircleShape, color = StudioMint) {
            Icon(Icons.AutoMirrored.Rounded.ViewQuilt, null, Modifier.padding(8.dp).size(20.dp), tint = StudioInk)
        }
        Text("Mon portfolio de conception", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun EmptyPortfolio(onCreate: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(540.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Surface(shape = CircleShape, color = Color(0xFFE0E5FF)) {
                    Icon(Icons.Rounded.AutoAwesome, null, Modifier.padding(30.dp).size(42.dp), tint = StudioBlue)
                }
                Surface(shape = CircleShape, color = StudioMint, modifier = Modifier.align(Alignment.BottomEnd)) {
                    Icon(Icons.Rounded.Check, null, Modifier.padding(6.dp).size(14.dp), tint = StudioInk)
                }
            }
            Text("Votre portfolio est vide", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text(
                "Générez votre premier design pour commencer votre collection.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(0.74f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Button(
                onClick = onCreate,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = StudioBlue, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth(0.92f).height(54.dp),
            ) {
                Text("Créer mon premier design", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    ElevatedCard(onClick = onClick, shape = RoundedCornerShape(20.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
        ListItem(
            headlineContent = { Text(title, fontWeight = FontWeight.Bold) },
            supportingContent = { Text(subtitle) },
            leadingContent = {
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                    Icon(icon, contentDescription = null, tint = StudioGreen, modifier = Modifier.padding(9.dp).size(20.dp))
                }
            },
        )
    }
}

@Composable
private fun BoardCard(item: com.ismail.homedecorai.BoardItem) {
    ElevatedCard(shape = RoundedCornerShape(20.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
        Box(Modifier.fillMaxWidth().height(208.dp)) {
            NetworkOrResourceImage(
                imageUrl = item.imageUrl,
                imageRes = item.imageRes,
                contentDescription = item.toolTitle,
                modifier = Modifier.fillMaxSize(),
            )
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f)))))
            Column(Modifier.align(Alignment.BottomStart).padding(12.dp)) {
                Text(item.toolTitle, color = Color.White, fontWeight = FontWeight.Black, maxLines = 1)
                Text("${item.roomType} / ${item.style}", color = Color.White.copy(alpha = 0.76f), style = MaterialTheme.typography.labelMedium, maxLines = 1)
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
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
            Icon(icon, contentDescription = null, tint = StudioGreen, modifier = Modifier.padding(8.dp).size(18.dp))
        }
        Text(text, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun DiamondStoreSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onPack: (DiamondPack) -> Unit,
) {
    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.34f)), contentAlignment = Alignment.BottomCenter) {
        Surface(
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = StudioCanvas,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(Modifier.width(44.dp).height(5.dp).clip(CircleShape).background(StudioLine))
                    }
                }
                item {
                    ElevatedCard(shape = RoundedCornerShape(26.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper)) {
                        Row(
                            Modifier.fillMaxWidth().padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                                Icon(Icons.Rounded.Star, null, Modifier.padding(12.dp).size(26.dp), tint = StudioGreen)
                            }
                            Column(Modifier.weight(1f)) {
                                Text("Solde actuel", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                                Text("${state.diamonds} diamants", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = onClose) {
                                Icon(Icons.Rounded.Close, contentDescription = "Close")
                            }
                        }
                    }
                }
                item {
                    Text("Obtenir plus de crédits", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
                items(HomeDecorCatalog.diamondPacks.take(3), key = { it.id }) { pack ->
                    DiamondPackRow(pack = pack, onClick = { onPack(pack) })
                }
            }
        }
    }
}

@Composable
private fun DiamondPackRow(
    pack: DiamondPack,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = StudioPaper),
        modifier = Modifier.fillMaxWidth().height(108.dp),
    ) {
        Row(
            Modifier.fillMaxSize().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                Icon(Icons.Rounded.Star, null, Modifier.padding(10.dp).size(22.dp), tint = StudioBlue)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(pack.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (pack.badge != null) {
                    Surface(shape = CircleShape, color = Color(0xFFFFE2E6)) {
                        Text(
                            pack.badge,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            color = Color(0xFFE83E54),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Text("${pack.diamonds} diamants", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Button(
                onClick = onClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = StudioBlue, contentColor = Color.White),
                modifier = Modifier.widthIn(min = 106.dp).height(44.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
            ) {
                Text(pack.price, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun FirstLaunchDisclosure(onAccept: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Concepts générés par l'IA") },
        text = {
            Text("HomeDecor AI génère des visuels d'inspiration. Vérifiez toujours les mesures, les modifications structurelles, les permis, les prix et la disponibilité des produits avant de construire ou d'acheter.")
        },
        confirmButton = {
            Button(onClick = onAccept, shape = CircleShape) {
                Text("J'ai compris")
            }
        },
    )
}

private fun wizardStepNumber(stage: WizardStage, tool: DecorTool? = null): Int {
    return when (stage) {
        WizardStage.Photo -> 1
        WizardStage.Space -> 2
        WizardStage.Style -> 3
        WizardStage.Refine -> if (tool?.id in listOf("garden", "paint")) 3 else 4
        WizardStage.Processing -> wizardTotalSteps(tool)
        WizardStage.Result -> wizardTotalSteps(tool)
    }
}

private fun wizardTotalSteps(tool: DecorTool?): Int {
    return when (tool?.id) {
        "garden", "paint" -> 3
        else -> 4
    }
}

private fun workflowTitle(tool: DecorTool): String {
    return when (tool.id) {
        "interior" -> "Intérieur"
        "facade" -> "Extérieur"
        "garden" -> "Jardin"
        "paint" -> "Peinture"
        "floor" -> "Relooking du sol"
        "layout" -> "Agencement"
        "replace" -> "Objets"
        "reference" -> "Référence"
        else -> tool.title
    }
}

private data class ExamplePhoto(
    val label: String,
    val imageRes: Int,
)

private fun examplesForTool(tool: DecorTool): List<ExamplePhoto> {
    return when (tool.id) {
        "facade" -> listOf(
            ExamplePhoto("Maison d'échafaudage", R.drawable.assets_media_examples_exterior_exteriorbeforescaffoldhouse),
            ExamplePhoto("Maison patinée", R.drawable.assets_media_examples_exterior_exteriorbeforeweatheredhouse),
            ExamplePhoto("Coquille de briques", R.drawable.assets_media_examples_exterior_exteriorbeforebrickshell),
            ExamplePhoto("Cottage envahi", R.drawable.assets_media_examples_exterior_exteriorbeforeovergrowncottage),
        )
        "garden" -> listOf(
            ExamplePhoto("Cour boueuse", R.drawable.assets_media_examples_garden_gardenbeforemuddyyard),
            ExamplePhoto("Cour de mauvaises herbes", R.drawable.assets_media_examples_garden_gardenbeforeweedyyard),
            ExamplePhoto("Parc à décombres", R.drawable.assets_media_examples_garden_gardenbeforerubbleyard),
            ExamplePhoto("Coin envahi", R.drawable.assets_media_examples_garden_gardenbeforeovergrowncorner),
        )
        "floor" -> listOf(
            ExamplePhoto("Béton fissuré", R.drawable.assets_media_examples_floor_floorbeforecrackedconcrete),
            ExamplePhoto("Planches endommagées", R.drawable.assets_media_examples_floor_floorbeforedamagedplanks),
            ExamplePhoto("Tuile cassée", R.drawable.assets_media_examples_floor_floorbeforebrokentile),
            ExamplePhoto("Sous-plancher", R.drawable.assets_media_examples_floor_floorbeforerenovationsubfloor),
        )
        "paint" -> listOf(
            ExamplePhoto("Béton brut", R.drawable.assets_media_examples_wall_wallbeforerawconcrete),
            ExamplePhoto("Plâtre écaillé", R.drawable.assets_media_examples_wall_wallbeforepeelingplaster),
            ExamplePhoto("Porte blanc", R.drawable.assets_media_examples_wall_wallbeforewornwhite),
            ExamplePhoto("Brique exposée", R.drawable.assets_media_examples_wall_wallbeforeexposedbrick),
        )
        else -> listOf(
            ExamplePhoto("Salle vide", R.drawable.assets_media_examples_interior_interiorbeforeemptyroom),
            ExamplePhoto("Salon en désordre", R.drawable.assets_media_examples_interior_interiorbeforemessylounge),
            ExamplePhoto("Chambre usée", R.drawable.assets_media_examples_interior_interiorbeforedamagedroom),
            ExamplePhoto("Cuisine usée", R.drawable.assets_media_examples_interior_interiorbeforeoutdatedkitchen),
        )
    }
}

private fun selectedExampleImageRes(state: HomeDecorUiState): Int {
    val selected = examplesForTool(state.selectedTool).firstOrNull { it.label == state.selectedExampleLabel }
    return selected?.imageRes ?: examplesForTool(state.selectedTool).first().imageRes
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
