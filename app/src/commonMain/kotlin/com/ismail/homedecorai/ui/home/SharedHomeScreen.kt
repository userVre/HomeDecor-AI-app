package com.ismail.homedecorai.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.getScreenWidthDp
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.model.ToolsScreenState
import com.ismail.homedecorai.ui.MainLayout
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.tools.CategorySection
import com.ismail.homedecorai.ui.tools.ToolInfoCard
import com.ismail.homedecorai.ui.tools.toolCategories

// ---------------------------------------------------------------------------
// Home Screen  –  Landing page with hero + grouped tool sections
// ---------------------------------------------------------------------------

@Composable
fun SharedHomeScreen(
    state: ToolsScreenState,
    onToolClick: (ToolItem) -> Unit,
    onOpenPaywall: () -> Unit = {},
) {
    if (state.isLoading) {
        HomeLoadingContent()
        return
    }

    if (state.error != null) {
        HomeErrorContent(message = state.error)
        return
    }

    if (state.tools.isEmpty()) {
        HomeEmptyContent()
        return
    }

    val isDesktop = rememberIsDesktop()

    MainLayout {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag(Strings.TestTags.homeScreen),
        ) {
            if (isDesktop) {
                DesktopHomeLayout(state = state, onToolClick = onToolClick, onOpenPaywall = onOpenPaywall)
            } else {
                MobileHomeLayout(state = state, onToolClick = onToolClick, onOpenPaywall = onOpenPaywall)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Desktop Layout  –  Hero left, sections right
// ---------------------------------------------------------------------------

@Composable
private fun DesktopHomeLayout(
    state: ToolsScreenState,
    onToolClick: (ToolItem) -> Unit,
    onOpenPaywall: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        contentPadding = PaddingValues(
            bottom = HomeDecorSpacing.NavBarReservation,
        ),
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
    ) {
        item {
            HomeHeroSection(onToolClick = state.tools.firstOrNull(), onOpenPaywall = onOpenPaywall)
        }
        items(toolCategories.size) { index ->
            val category = toolCategories[index]
            CategorySection(
                category = category,
                tools = state.tools,
                onToolClick = onToolClick,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Mobile Layout  –  Vertical scroll
// ---------------------------------------------------------------------------

@Composable
private fun MobileHomeLayout(
    state: ToolsScreenState,
    onToolClick: (ToolItem) -> Unit,
    onOpenPaywall: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = HomeDecorSpacing.NavBarReservation,
        ),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        item {
            HomeHeroSection(onToolClick = state.tools.firstOrNull(), onOpenPaywall = onOpenPaywall)
        }
        items(toolCategories.size) { index ->
            val category = toolCategories[index]
            CategorySection(
                category = category,
                tools = state.tools,
                onToolClick = onToolClick,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Hero Section  –  "Start with a photo" CTA
// ---------------------------------------------------------------------------

@Composable
private fun HomeHeroSection(
    onToolClick: ToolItem?,
    onOpenPaywall: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = HomeDecorSpacing.Xl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            Strings.heroTitle,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(HomeDecorSpacing.Sm))
        Text(
            Strings.heroSubtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(HomeDecorSpacing.Lg))

        // Primary CTA — "Start with a photo"
        Button(
            onClick = { onToolClick?.let { onOpenPaywall() } },
            shape = HomeDecorShape.Button,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                Icons.Rounded.PhotoCamera,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorSpacing.Base),
            )
            Spacer(Modifier.width(HomeDecorSpacing.Sm))
            Text(
                "Start with a photo",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(Modifier.height(HomeDecorSpacing.Lg))

        // Trust chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TrustChip("Photos private")
            TrustChip("No commitment")
            TrustChip("5 free")
        }
    }
}

// ---------------------------------------------------------------------------
// Loading, Error & Empty States
// ---------------------------------------------------------------------------

@Composable
private fun TrustChip(label: String) {
    Surface(
        shape = HomeDecorShape.Chip,
        color = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HomeLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(
                text = Strings.loadingContent,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HomeErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Xxl),
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(
                text = Strings.errorGeneric,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HomeEmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.Home,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Xxl),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            )
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            Text(
                text = "No tools available",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(
                text = "Check back later for new tools",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
