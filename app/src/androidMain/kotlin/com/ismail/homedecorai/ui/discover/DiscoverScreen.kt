package com.ismail.homedecorai.ui.discover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.model.DiscoverSectionItem
import com.ismail.homedecorai.model.DiscoverScreenState
import com.ismail.homedecorai.model.GalleryCardItem

@Composable
fun DiscoverScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val favoriteSourceIds = remember(state.workspace.favorites) {
        state.workspace.favorites.map { it.sourceType }.toSet()
    }
    val sections = HomeDecorCatalog.discoverSections.map { section ->
        DiscoverSectionItem(
            id = section.id,
            title = section.title,
            cluster = section.cluster,
            items = section.items.map { item ->
                GalleryCardItem(
                    id = item.id,
                    title = item.title,
                    category = item.category,
                    styleType = item.styleType,
                    description = item.description,
                    room = item.room,
                    color = item.color,
                    mood = item.mood,
                )
            },
        )
    }

    SharedDiscoverScreen(
        state = DiscoverScreenState(favoriteSourceIds = favoriteSourceIds, sections = sections),
        onToggleFavorite = { section, item ->
            val origSection = HomeDecorCatalog.discoverSections.first { it.id == section.id }
            val origItem = origSection.items.first { it.id == item.id }
            viewModel.toggleDiscoverFavorite(origItem, origSection)
        },
        onAddToMoodboard = { section, item ->
            val origSection = HomeDecorCatalog.discoverSections.first { it.id == section.id }
            val origItem = origSection.items.first { it.id == item.id }
            viewModel.addDiscoverToMoodboard(origItem, origSection)
        },
        onUseStyle = { section, item ->
            val origSection = HomeDecorCatalog.discoverSections.first { it.id == section.id }
            val origItem = origSection.items.first { it.id == item.id }
            viewModel.useDiscoverStyle(origItem, origSection)
        },
    )
}
