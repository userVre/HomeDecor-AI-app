package com.ismail.homedecorai.ui

import android.content.Intent
import android.content.ContentValues
import android.app.Activity
import android.content.ContextWrapper
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Help
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
import androidx.compose.material.icons.rounded.Redo
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material.icons.rounded.Visibility
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
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntSize
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
import com.ismail.homedecorai.MaskPoint
import com.ismail.homedecorai.MaskStroke
import com.ismail.homedecorai.R
import com.ismail.homedecorai.SelectedPhoto
import com.ismail.homedecorai.WizardStage
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Offering
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PackageType
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
                    onFulfill = viewModel::fulfillDiamondPurchase,
                )
            }

            if (state.paywallVisible) {
                PaywallSheet(
                    state = state,
                    onClose = viewModel::closePaywall,
                    onSubscription = viewModel::syncSubscriptionFromRevenueCat,
                    onStore = viewModel::openDiamondStore,
                )
            }

            if (state.authVisible) {
                AuthSheet(
                    onClose = viewModel::closeAuth,
                    onGoogle = viewModel::signInWithGooglePreview,
                )
            }

            if (state.settingsVisible) {
                SettingsSheet(
                    onClose = viewModel::closeSettings,
                    onStore = viewModel::openDiamondStore,
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
                ) {
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
            Icon(Icons.Rounded.Diamond, null, Modifier.size(17.dp), tint = StudioBlue)
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
        "floor" -> StepCopy("Changer le sol", "Importez une photo de votre pièce")
        "paint" -> StepCopy("Peindre les murs", "Importez une photo de votre pièce")
        "layout" -> StepCopy("Agencement intelligent", "Optimisez l’espace et la circulation de votre pièce")
        "replace" -> StepCopy("Remplacer un objet", "Importez une photo contenant l’objet à modifier")
        "reference" -> StepCopy("Style de référence", "Importez la pièce à transformer")
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
            listOf("Suggestion IA") + HomeDecorCatalog.gardenStyles,
        )
        "paint" -> StepCopy(
            "Sélectionnez les surfaces à transformer",
            "Peignez un masque sur les murs ou surfaces à transformer.",
        )
        "floor" -> StepCopy(
            "Marquez le sol",
            "Peignez un masque sur le sol à remplacer.",
        )
        "layout" -> StepCopy(
            "Choisissez votre objectif",
            "Optimisez l'agencement sans perdre la structure de la pièce.",
            HomeDecorCatalog.layoutGoals,
        )
        "replace" -> StepCopy(
            "Marquez l'objet",
            "Masquez l'objet à remplacer et gardez le reste de la photo intact.",
        )
        "reference" -> StepCopy(
            "Ajoutez une référence",
            "Importez une image avec le style à appliquer.",
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
            "Choisissez une couleur",
            "Sélectionnez une couleur ou décrivez l’ambiance souhaitée.",
            HomeDecorCatalog.paintColors,
        )
        "floor" -> StepCopy(
            "Choisissez le matériau",
            "Choisissez un matériau. L'IA l'appliquera en respectant la perspective, la lumière et les meubles.",
            HomeDecorCatalog.floorMaterials,
        )
        "replace" -> StepCopy(
            "Décrivez le remplacement",
            "Choisissez ce qui doit remplacer l'objet masqué.",
            HomeDecorCatalog.replaceSuggestions,
        )
        "reference" -> StepCopy(
            "Intensité du transfert",
            "Choisissez à quel point le style de référence doit guider le rendu.",
            HomeDecorCatalog.referenceStrengths,
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
                    "replace" -> ObjectMaskStep(state = state, viewModel = viewModel)
                    "reference" -> ReferencePhotoStep(state = state, viewModel = viewModel)
                    else -> ChoiceStep(
                    eyebrow = "Étape 2/${wizardTotalSteps(state.selectedTool)}",
                    copy = stepTwoCopy(state.selectedTool),
                    selected = state.selectedRooms,
                    onSelect = viewModel::setRoom,
                    onContinue = viewModel::nextStage,
                    visualStyleCards = state.selectedTool.id == "garden",
                    visualBuildingCards = state.selectedTool.id == "facade",
                    )
                }
                WizardStage.Style -> when (state.selectedTool.id) {
                    "paint", "floor", "replace", "reference" -> SpecializedGenerateStep(state = state, viewModel = viewModel)
                    else -> ChoiceStep(
                        eyebrow = "Étape 3/${wizardTotalSteps(state.selectedTool)}",
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
    val hasMainPhoto = state.selectedPhotos.isNotEmpty()
    val canContinue = hasMainPhoto
    StepScaffold(
        eyebrow = "Étape 1/${wizardTotalSteps(state.selectedTool)}",
        title = if (!hasMainPhoto) copy.title else "Photo ajoutée",
        body = if (!hasMainPhoto) copy.body else null,
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
                        Text(copy.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
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
                        val firstPhoto = state.selectedPhotos.first()
                        UriOrResourceImage(
                            uri = firstPhoto.uri,
                            imageRes = selectedPhotoImageRes(state, firstPhoto),
                            contentDescription = "Photo ajoutée",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                SelectedPhotoStrip(
                    state = state,
                    onAdd = { imageLauncher.launch("image/*") },
                    onRemove = viewModel::removePhoto,
                )
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
            OutlinedButton(
                onClick = { viewModel.selectExamplePhoto(examplesForTool(state.selectedTool).first().label) },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Utiliser un exemple")
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Photos d'exemple", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(examplesForTool(state.selectedTool), key = { "example-${it.label}" }) { photo ->
                        ExamplePhotoCard(
                            photo = photo,
                            selected = state.selectedPhotos.any { it.exampleLabel == photo.label },
                            onClick = { viewModel.selectExamplePhoto(photo.label) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedPhotoStrip(
    state: HomeDecorUiState,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(state.selectedPhotos.size, key = { "selected-photo-$it" }) { index ->
            val slot = state.selectedPhotos[index]
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
                        contentDescription = "Photo ${index + 1}",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Surface(
                    onClick = { onRemove(index) },
                    shape = CircleShape,
                    color = Color(0xFF4A4D57),
                    modifier = Modifier.align(Alignment.TopEnd).size(24.dp),
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "Supprimer la photo", modifier = Modifier.padding(4.dp), tint = Color.White)
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
                        Icon(Icons.Rounded.Add, contentDescription = "Ajouter une photo", tint = StudioInk)
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
    selected: List<String>,
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
    var pendingReferenceCameraUri by remember { mutableStateOf<Uri?>(null) }
    val referenceLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.setReferencePhoto(uri)
    }
    val referenceCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        if (saved) viewModel.setReferencePhoto(pendingReferenceCameraUri)
    }
    val hasReference = state.selectedReferenceUri != null || state.selectedReferenceExampleLabel != null
    StepScaffold(
        eyebrow = "Étape 2/${wizardTotalSteps(state.selectedTool)}",
        title = "Ajoutez une référence",
        body = "Importez une image avec le style à appliquer",
        buttonLabel = "Continuer",
        buttonEnabled = hasReference,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            ReferenceImagePicker(
                selectedUri = state.selectedReferenceUri,
                selectedExample = state.selectedReferenceExampleLabel,
                onImport = { referenceLauncher.launch("image/*") },
                onExample = { viewModel.selectReferenceExample("Référence éditoriale") },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { referenceLauncher.launch("image/*") }, shape = CircleShape, modifier = Modifier.weight(1f).height(48.dp)) {
                    Icon(Icons.Rounded.Add, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Galerie")
                }
                OutlinedButton(
                    onClick = {
                        val uri = createCameraUri(context)
                        pendingReferenceCameraUri = uri
                        referenceCameraLauncher.launch(uri)
                    },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Caméra")
                }
            }
            OutlinedButton(
                onClick = { viewModel.selectReferenceExample("Référence éditoriale") },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Utiliser un exemple")
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
        title = "Marquez le sol",
        disabledLabel = "Marquez le sol pour continuer",
        target = "floor",
        imageDescription = "Sol à transformer",
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
        title = "Sélectionnez les surfaces à transformer",
        disabledLabel = "Marquez le mur pour continuer",
        target = "wall",
        imageDescription = "Mur à transformer",
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
        title = "Marquez l’objet",
        disabledLabel = "Marquez l’objet pour continuer",
        target = "object",
        imageDescription = "Objet à remplacer",
    )
}

@Composable
private fun MaskEditorStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    title: String,
    disabledLabel: String,
    target: String,
    imageDescription: String,
) {
    val hasMask = state.maskStrokes.any { !it.erase && it.points.size > 1 }
    StepScaffold(
        eyebrow = "Étape 2/${wizardTotalSteps(state.selectedTool)}",
        title = title,
        body = null,
        buttonLabel = if (hasMask) "Continuer" else disabledLabel,
        buttonEnabled = hasMask,
        onButton = viewModel::nextStage,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            MaskCanvas(
                state = state,
                imageDescription = imageDescription,
                onStroke = viewModel::addMaskStroke,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ToolToggle("Brush", Icons.Rounded.Brush, !state.eraserSelected) { viewModel.setMaskEraser(false) }
                ToolToggle("Eraser", Icons.Rounded.Delete, state.eraserSelected) { viewModel.setMaskEraser(true) }
                FilledIconButton(onClick = viewModel::undoMaskStroke, enabled = state.maskStrokes.isNotEmpty()) {
                    Icon(Icons.Rounded.Undo, contentDescription = "Annuler")
                }
                FilledIconButton(onClick = viewModel::redoMaskStroke, enabled = state.undoneMaskStrokes.isNotEmpty()) {
                    Icon(Icons.Rounded.Redo, contentDescription = "Rétablir")
                }
                FilledIconButton(onClick = viewModel::clearMask, enabled = state.maskStrokes.isNotEmpty()) {
                    Icon(Icons.Rounded.Close, contentDescription = "Effacer le masque")
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Taille du pinceau", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Slider(value = state.brushSize, onValueChange = viewModel::setBrushSize, valueRange = 8f..72f)
            }
            if (target != "object") {
                OutlinedButton(onClick = { viewModel.markMaskWithAutoDetect(target) }, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(if (target == "floor") "Auto-détecter le sol" else "Auto-détecter le mur")
                }
            }
        }
    }
}

@Composable
private fun ToolToggle(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) StudioBlue else StudioPaper,
        tonalElevation = if (selected) 4.dp else 1.dp,
        modifier = Modifier.height(48.dp).border(1.dp, if (selected) StudioBlue else StudioLine, CircleShape),
    ) {
        Row(Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(18.dp), tint = if (selected) Color.White else StudioInk)
            Text(label, color = if (selected) Color.White else StudioInk, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MaskCanvas(
    state: HomeDecorUiState,
    imageDescription: String,
    onStroke: (MaskStroke) -> Unit,
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var livePoints by remember { mutableStateOf<List<MaskPoint>>(emptyList()) }
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
                    )
                }
            }
            state.maskStrokes.filterNot { it.erase }.forEach(::drawStroke)
            livePoints.takeIf { it.isNotEmpty() }?.let {
                drawStroke(MaskStroke(it, state.brushSize, state.eraserSelected))
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
    if (state.selectedTool.id == "layout") {
        StepScaffold(
            eyebrow = "Étape 3/${wizardTotalSteps(state.selectedTool)}",
            title = "Ajoutez des détails",
            body = "Précisez les contraintes pour optimiser l’espace sans perdre vos priorités.",
            buttonLabel = "Générer",
            buttonEnabled = state.selectedRooms.isNotEmpty(),
            onButton = viewModel::generate,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(value = state.roomType, onValueChange = viewModel::setRoomTypeText, label = { Text("Type de pièce") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp))
                OutlinedTextField(value = state.style, onValueChange = viewModel::setStyleText, label = { Text("Nombre de personnes") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp))
                OutlinedTextField(value = state.palette, onValueChange = viewModel::setPaletteText, label = { Text("Mobilier à garder / mobilier à déplacer") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp))
                OutlinedTextField(
                    value = state.customPrompt,
                    onValueChange = viewModel::setCustomPrompt,
                    label = { Text("Contraintes importantes") },
                    placeholder = { Text("Exemple : garder le canapé, ajouter un bureau, améliorer la circulation") },
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
        eyebrow = "Étape ${wizardStepNumber(state.wizardStage, state.selectedTool)}/${wizardTotalSteps(state.selectedTool)}",
        title = copy.title,
        body = copy.body,
        buttonLabel = "Générer mon design",
        buttonIcon = Icons.Rounded.AutoAwesome,
        buttonEnabled = state.selectedPalettes.isNotEmpty(),
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
                label = { Text("Décrivez votre vision") },
                placeholder = { Text("Optionnel : texture, matière ou ambiance.") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(18.dp),
            )
            Surface(shape = RoundedCornerShape(22.dp), color = StudioBlack) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Brief design", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${state.roomType.ifBlank { "Espace à choisir" }} / ${state.style.ifBlank { "Style à choisir" }}", color = Color.White.copy(alpha = 0.82f))
                    Text(state.designMode, color = Color.White.copy(alpha = 0.82f))
                    Text(state.palette.ifBlank { "Palette à choisir" }, color = Color.White.copy(alpha = 0.72f))
                }
            }
        }
    }
}

@Composable
private fun SpecializedGenerateStep(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val copy = stepThreeCopy(state.selectedTool)
    val selected = state.selectedStyles
    val requiresPrompt = state.selectedTool.id == "replace"
    val canGenerate = when (state.selectedTool.id) {
        "replace" -> state.customPrompt.isNotBlank() || selected.isNotEmpty()
        "reference" -> selected.isNotEmpty()
        else -> selected.isNotEmpty() || state.customPrompt.isNotBlank()
    }
    StepScaffold(
        eyebrow = "Étape 3/${wizardTotalSteps(state.selectedTool)}",
        title = copy.title,
        body = copy.body,
        buttonLabel = "Générer",
        buttonEnabled = canGenerate && (!requiresPrompt || state.customPrompt.isNotBlank() || selected.isNotEmpty()),
        onButton = viewModel::generate,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (!state.generationError.isNullOrBlank()) {
                Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFE8EA)) {
                    Text(
                        state.generationError,
                        modifier = Modifier.padding(14.dp),
                        color = Color(0xFF9D1D2E),
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                copy.options.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { option ->
                            ExpressiveChoiceChip(
                                label = option,
                                selected = option in selected,
                                onClick = {
                                    viewModel.setStyle(option)
                                    if (state.selectedTool.id == "replace" && state.customPrompt.isBlank()) {
                                        viewModel.setCustomPrompt(option)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
            if (state.selectedTool.id == "reference") {
                Text("Options du transfert", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
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
                placeholder = {
                    Text(
                        when (state.selectedTool.id) {
                            "paint" -> "Décrivez la couleur ou l’ambiance souhaitée"
                            "floor" -> "Décrivez le style du sol souhaité"
                            "replace" -> "Exemple : remplacer ce canapé par un canapé beige moderne"
                            else -> "Ajoutez une instruction optionnelle"
                        },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = if (state.selectedTool.id == "replace") 4 else 3,
                shape = RoundedCornerShape(18.dp),
            )
            Surface(shape = RoundedCornerShape(22.dp), color = StudioBlack) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("Brief envoyé au backend", color = Color.White, fontWeight = FontWeight.Black)
                    Text("serviceType: ${state.selectedTool.serviceType}", color = Color.White.copy(alpha = 0.8f))
                    Text(selected.joinToString(" + ").ifBlank { state.customPrompt.ifBlank { "Sélection requise" } }, color = Color.White.copy(alpha = 0.8f))
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
        Text("Préparation de votre transformation...", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
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
        title = "Votre résultat",
        body = "Rendu enregistré dans votre espace de travail.",
        buttonLabel = "Nouvelle création",
        buttonIcon = Icons.Rounded.Check,
        onButton = { viewModel.startTool(state.selectedTool) },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(shape = RoundedCornerShape(28.dp)) {
                NetworkOrResourceImage(
                    imageUrl = result?.imageUrl,
                    imageRes = result?.imageRes ?: R.drawable.sample_after_luxury,
                    contentDescription = "Image générée",
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                )
            }
            Text("Avant / après", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(Modifier.weight(1f), shape = RoundedCornerShape(20.dp)) {
                    NetworkOrResourceImage(
                        imageUrl = result?.sourceImageUrl,
                        imageRes = selectedExampleImageRes(state),
                        contentDescription = "Avant",
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    )
                }
                Card(Modifier.weight(1f), shape = RoundedCornerShape(20.dp)) {
                    NetworkOrResourceImage(
                        imageUrl = result?.imageUrl,
                        imageRes = result?.imageRes ?: R.drawable.sample_after_luxury,
                        contentDescription = "Après",
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    )
                }
            }
            if (state.selectedTool.id == "reference") {
                Text("Référence", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Card(shape = RoundedCornerShape(20.dp)) {
                    UriOrResourceImage(
                        uri = state.selectedReferenceUri,
                        imageRes = R.drawable.tool_reference,
                        contentDescription = "Image de référence",
                        modifier = Modifier.fillMaxWidth().aspectRatio(1.4f),
                    )
                }
            }
            Surface(shape = RoundedCornerShape(22.dp), color = StudioPaper, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth().border(1.dp, StudioLine, RoundedCornerShape(22.dp))) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Métadonnées", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text("Service : ${workflowTitle(state.selectedTool)}")
                    Text("Style : ${(state.style.ifBlank { state.palette }).ifBlank { "Choix IA" }}")
                    Text("Prompt : ${state.customPrompt.ifBlank { "Aucun prompt personnalisé" }}")
                    Text("Statut : ${result?.status ?: "ready"}")
                    Text("Date : ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date((result?.createdAt ?: System.currentTimeMillis().toDouble()).toLong()))}")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            val saved = saveResultToGallery(context, result)
                            Toast.makeText(context, if (saved) "Design enregistré dans la galerie." else "Impossible d'enregistrer ce design.", Toast.LENGTH_LONG).show()
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Rounded.Save, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Enregistrer")
                }
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val shared = shareResult(context, result)
                            if (!shared) {
                                Toast.makeText(context, "Impossible de partager ce design pour le moment.", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Rounded.Share, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Partager")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { scope.launch { saveResultToGallery(context, result) } }, shape = CircleShape, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Download, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Télécharger")
                }
                OutlinedButton(onClick = viewModel::generate, shape = CircleShape, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Réessayer")
                }
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
            FeatureRow(Icons.Rounded.Diamond, "Générations sans friction")
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
            FilledIconButton(onClick = viewModel::openSettings, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Rounded.Settings, contentDescription = "Paramètres")
            }
            Spacer(Modifier.weight(1f))
            if (state.signedInName == null) {
                Button(
                    onClick = viewModel::openAuth,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioBlue, contentColor = Color.White),
                    modifier = Modifier.height(48.dp),
                ) {
                    Text("Se connecter", fontWeight = FontWeight.Black)
                }
            } else {
                Surface(shape = CircleShape, color = StudioPaper, tonalElevation = 2.dp, modifier = Modifier.border(1.dp, StudioLine, CircleShape)) {
                    Row(
                        Modifier.padding(start = 6.dp, end = 14.dp, top = 6.dp, bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Surface(shape = CircleShape, color = StudioMint) {
                            Text(
                                state.signedInName.take(1),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                color = StudioInk,
                                fontWeight = FontWeight.Black,
                            )
                        }
                        Column {
                            Text(state.signedInName, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelLarge)
                            Text(state.signedInEmail.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                        }
                    }
                }
            }
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
                if (!state.isPro) {
                    Button(onClick = viewModel::openPaywall, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(54.dp)) {
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
        Text("Mon portfolio de concepts", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
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
private fun PaywallSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onSubscription: (String, String, String, Double?, Double?) -> Unit,
    onStore: () -> Unit,
) {
    val context = LocalContext.current
    var offering by remember { mutableStateOf<Offering?>(null) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        if (!Purchases.isConfigured) {
            message = "Les abonnements sont momentanément indisponibles."
            return@LaunchedEffect
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                offering = offerings.current ?: offerings.all.values.firstOrNull()
            }

            override fun onError(error: PurchasesError) {
                message = "Impossible de charger les prix pour le moment."
            }
        })
    }
    fun buy(packageToPurchase: Package?, subscriptionType: String, entitlement: String) {
        val activity = context.findActivity()
        if (packageToPurchase == null || activity == null || !Purchases.isConfigured) {
            message = "Ce plan n'est pas disponible pour le moment."
            return
        }
        loading = true
        Purchases.sharedInstance.purchasePackage(activity, packageToPurchase, object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                loading = false
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
                loading = false
                message = if (userCancelled) "Achat annulé." else "L'achat a échoué. Réessayez."
            }
        })
    }
    val monthlyPackage = offering?.monthly ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.MONTHLY }
    val yearlyPackage = offering?.annual ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.ANNUAL }
    Box(Modifier.fillMaxSize().background(StudioCanvas)) {
        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd).windowInsetsPadding(WindowInsets.statusBars).padding(16.dp).size(48.dp),
        ) {
            Icon(Icons.Rounded.Close, contentDescription = "Fermer")
        }
        LazyColumn(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 92.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, tonalElevation = 6.dp) {
                        Icon(Icons.Rounded.Diamond, null, Modifier.padding(20.dp).size(38.dp), tint = StudioInk)
                    }
                    Text("Passez à Pro", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                    Text(
                        "Créez plus vite, exportez proprement et gardez vos concepts premium sans friction.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
            item {
                ElevatedCard(shape = RoundedCornerShape(28.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioBlack)) {
                    Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("PRO", color = StudioMint, fontWeight = FontWeight.Black)
                        Text("Création premium", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                        FeatureRowOnDark(Icons.Rounded.AutoAwesome, "Générations illimitées ou limite mensuelle supérieure")
                        FeatureRowOnDark(Icons.Rounded.Refresh, "Rendus plus rapides")
                        FeatureRowOnDark(Icons.Rounded.Star, "Styles premium")
                        FeatureRowOnDark(Icons.Rounded.Save, "Historique complet")
                        FeatureRowOnDark(Icons.Rounded.Download, "Sans filigrane si applicable")
                        if (message != null || state.purchaseMessage != null) {
                            Text(message ?: state.purchaseMessage.orEmpty(), color = StudioMint, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { buy(monthlyPackage, "weekly", "weekly_pro") },
                                enabled = !loading && monthlyPackage != null,
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = StudioMint, contentColor = StudioInk),
                                modifier = Modifier.weight(1f).height(56.dp),
                            ) {
                                Text(monthlyPackage?.product?.price?.formatted ?: "Mensuel", fontWeight = FontWeight.Black)
                            }
                            Button(
                                onClick = { buy(yearlyPackage, "yearly", "annual_pro") },
                                enabled = !loading && yearlyPackage != null,
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = StudioMint, contentColor = StudioInk),
                                modifier = Modifier.weight(1f).height(56.dp),
                            ) {
                                Text(yearlyPackage?.product?.price?.formatted ?: "Annuel", fontWeight = FontWeight.Black)
                            }
                        }
                        Button(
                            onClick = { buy(yearlyPackage ?: monthlyPackage, if (yearlyPackage != null) "yearly" else "weekly", if (yearlyPackage != null) "annual_pro" else "weekly_pro") },
                            enabled = !loading && (yearlyPackage != null || monthlyPackage != null),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = StudioMint, contentColor = StudioInk),
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                        ) {
                            Text(if (loading) "Chargement..." else if (state.isPro) "Pro activé" else "Continuer", fontWeight = FontWeight.Black)
                        }
                        OutlinedButton(
                            onClick = {
                                if (!Purchases.isConfigured) {
                                    message = "Restauration indisponible pour le moment."
                                } else {
                                    loading = true
                                    Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
                                        override fun onReceived(customerInfo: CustomerInfo) {
                                            loading = false
                                            val active = customerInfo.entitlements.active.values.firstOrNull()
                                            if (active != null) {
                                                onSubscription("pro", if (active.identifier.contains("annual")) "yearly" else "weekly", active.identifier, active.latestPurchaseDate.time.toDouble(), active.expirationDate?.time?.toDouble())
                                            } else {
                                                message = "Aucun achat Pro actif trouvé."
                                            }
                                        }

                                        override fun onError(error: PurchasesError) {
                                            loading = false
                                            message = "La restauration a échoué. Réessayez."
                                        }
                                    })
                                }
                            },
                            shape = CircleShape,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                        ) {
                            Text("Restaurer les achats")
                        }
                    }
                }
            }
            item {
                OutlinedButton(onClick = onStore, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                    Icon(Icons.Rounded.Diamond, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Acheter des diamants seulement")
                }
            }
        }
    }
}

@Composable
private fun FeatureRowOnDark(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f)) {
            Icon(icon, contentDescription = null, tint = StudioMint, modifier = Modifier.padding(8.dp).size(18.dp))
        }
        Text(text, color = Color.White.copy(alpha = 0.86f), fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun AuthSheet(
    onClose: () -> Unit,
    onGoogle: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(Modifier.fillMaxSize().background(StudioCanvas)) {
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd).windowInsetsPadding(WindowInsets.statusBars).padding(16.dp).size(48.dp)) {
            Icon(Icons.Rounded.Close, contentDescription = "Fermer")
        }
        Column(
            Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(shape = CircleShape, color = Color(0xFFE0E5FF), tonalElevation = 5.dp) {
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(18.dp).size(38.dp), tint = StudioInk)
            }
            Spacer(Modifier.height(20.dp))
            Text("Bon retour.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(
                "Connectez-vous pour retrouver vos designs, vos crédits et votre historique.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Spacer(Modifier.height(22.dp))
            ElevatedCard(shape = RoundedCornerShape(18.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioMist)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedButton(onClick = onGoogle, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                        Text("G", color = Color(0xFF4285F4), fontWeight = FontWeight.Black)
                        Spacer(Modifier.width(10.dp))
                        Text("Continuer avec Google")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.weight(1f).height(1.dp).background(StudioLine))
                        Text("ou", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Box(Modifier.weight(1f).height(1.dp).background(StudioLine))
                    }
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        leadingIcon = { Icon(Icons.Rounded.Email, null) },
                        placeholder = { Text("Courriel") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = { Icon(Icons.Rounded.Lock, null) },
                        trailingIcon = { Icon(Icons.Rounded.Visibility, null) },
                        placeholder = { Text("Mot de passe") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    Button(
                        onClick = onGoogle,
                        enabled = email.isNotBlank() && password.isNotBlank(),
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                    ) {
                        Text("Se connecter")
                    }
                    Text(
                        "Vos données sont protégées et ne sont jamais partagées.",
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
    onClose: () -> Unit,
    onStore: () -> Unit,
) {
    val context = LocalContext.current
    Box(Modifier.fillMaxSize().background(StudioCanvas)) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Retour")
                }
                Text("Paramètres", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, modifier = Modifier.weight(1f))
            }
            LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { Text("Paramètres complets arrivent dans la prochaine passe native.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                item { SettingsRow(Icons.Rounded.Language, "Langue", "Français", onClick = { Toast.makeText(context, "Français sélectionné.", Toast.LENGTH_LONG).show() }) }
                item { SettingsRow(Icons.Rounded.RateReview, "Feedback", "Envoyer une suggestion à l'équipe", onClick = { openUrl(context, "mailto:support@homedecor.ai?subject=HomeDecor%20AI%20Feedback") }) }
                item { SettingsRow(Icons.Rounded.Help, "FAQ", "Questions fréquentes sur les designs IA", onClick = { showToast(context, "FAQ : importez une photo, choisissez vos options, puis générez un concept.") }) }
                item { SettingsRow(Icons.Rounded.Star, "Restaurer les achats", "Récupérer vos abonnements et packs", onClick = { showToast(context, "Restauration RevenueCat à relier au compte Play Store.") }) }
                item { SettingsRow(Icons.Rounded.Diamond, "Boutique de diamants", "Acheter des crédits de génération", onClick = onStore) }
                item {
                    SettingsRow(Icons.Rounded.Share, "Partager l'app", "Invitez quelqu'un à essayer HomeDecor AI", onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Essayez HomeDecor AI pour transformer intérieurs, extérieurs, jardins, murs et sols.")
                        }
                        context.startActivity(Intent.createChooser(intent, "Partager HomeDecor AI"))
                    })
                }
                item { SettingsRow(Icons.Rounded.Policy, "Conditions d'utilisation", "Règles d'utilisation et avertissement IA", onClick = { openUrl(context, "${BuildConfig.APP_URL}/terms") }) }
                item { SettingsRow(Icons.Rounded.Policy, "Politique de confidentialité", "Données, images et compte utilisateur", onClick = { openUrl(context, "${BuildConfig.APP_URL}/privacy") }) }
                item { SettingsRow(Icons.Rounded.Delete, "Supprimer le compte", "Demande de suppression de compte", onClick = { showToast(context, "La suppression complète sera reliée à Clerk dans la passe native suivante.") }) }
            }
        }
    }
}

private fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

private fun openUrl(context: android.content.Context, url: String) {
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }.onFailure {
        showToast(context, "Impossible d'ouvrir ce lien pour le moment.")
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
    onPack: (DiamondPack) -> Unit,
    onFulfill: (String, String, String, String?, Double, String, Double) -> Unit,
) {
    val context = LocalContext.current
    var packages by remember { mutableStateOf<List<Package>>(emptyList()) }
    var loadingPack by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        if (!Purchases.isConfigured) {
            message = "Les achats sont momentanément indisponibles."
            return@LaunchedEffect
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                packages = (offerings.current?.availablePackages ?: emptyList()) + offerings.all.values.flatMap { it.availablePackages }
            }

            override fun onError(error: PurchasesError) {
                message = "Impossible de charger les packs pour le moment."
            }
        })
    }
    fun packageFor(pack: DiamondPack): Package? {
        val needles = listOf(pack.id, pack.title.lowercase(), "diamond", "diamonds", "credit")
        return packages.firstOrNull { pkg ->
            val haystack = "${pkg.identifier} ${pkg.product.id} ${pkg.product.title} ${pkg.product.description}".lowercase()
            pack.id in haystack || (needles.any { it in haystack } && pack.title.lowercase().split(" ").any { it in haystack })
        } ?: packages.firstOrNull { pkg -> pack.id in pkg.identifier.lowercase() || pack.id in pkg.product.id.lowercase() }
    }
    fun buy(pack: DiamondPack) {
        val productPackage = packageFor(pack)
        val activity = context.findActivity()
        if (productPackage == null || activity == null || !Purchases.isConfigured) {
            onPack(pack)
            message = "Pack indisponible dans Google Play pour le moment."
            return
        }
        loadingPack = pack.id
        Purchases.sharedInstance.purchasePackage(activity, productPackage, object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                loadingPack = null
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
                message = if (userCancelled) "Achat annulé." else "L'achat a échoué. Réessayez."
            }
        })
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.34f))
            .clickable { onClose() },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            color = StudioCanvas,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().clickable { },
        ) {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            Modifier
                                .width(44.dp)
                                .height(5.dp)
                                .clip(CircleShape)
                                .background(StudioLine)
                                .clickable { onClose() },
                        )
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
                                Icon(Icons.Rounded.Diamond, null, Modifier.padding(12.dp).size(26.dp), tint = StudioBlue)
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
                if (message != null || state.purchaseMessage != null) {
                    item {
                        Text(message ?: state.purchaseMessage.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                items(HomeDecorCatalog.diamondPacks.take(3), key = { it.id }) { pack ->
                    val productPackage = packageFor(pack)
                    DiamondPackRow(
                        pack = pack.copy(price = productPackage?.product?.price?.formatted ?: pack.price),
                        loading = loadingPack == pack.id || state.purchaseBusy,
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
    loading: Boolean = false,
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
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(10.dp).size(22.dp), tint = StudioBlue)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Pack ${pack.title}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                if (pack.description.isNotBlank()) {
                    Text(pack.description, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Button(
                onClick = onClick,
                enabled = !loading,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = StudioBlue, contentColor = Color.White),
                modifier = Modifier.widthIn(min = 106.dp).height(44.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
            ) {
                Text(if (loading) "..." else pack.price, fontWeight = FontWeight.Black)
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
        "garden" -> 3
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
