package com.ismail.homedecorai.model

import androidx.compose.ui.graphics.Color
import com.ismail.homedecorai.ui.theme.HomeDecorColors

data class ToolItem(
    val id: String,
    val title: String,
    val description: String,
    val gradientStart: Color = HomeDecorColors.Primary,
    val gradientEnd: Color = HomeDecorColors.OnPrimaryContainer,
    val imageUrl: String = "",
    val accentColor: Color = HomeDecorColors.PrimaryContainer,
)

data class ToolsScreenState(
    val isPro: Boolean = false,
    val diamonds: Int = 0,
    val tools: List<ToolItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class GalleryCardItem(
    val id: String,
    val title: String,
    val category: String,
    val imageUrl: String = "",
)

data class DiscoverSectionItem(
    val id: String,
    val title: String,
    val cluster: String,
    val items: List<GalleryCardItem>,
)

data class DiscoverScreenState(
    val favoriteSourceIds: Set<String> = emptySet(),
    val moodboardSourceIds: Set<String> = emptySet(),
    val sections: List<DiscoverSectionItem> = emptyList(),
    val selectedCluster: String = "interior",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignedIn: Boolean = false,
)

data class BoardItem(
    val id: String,
    val toolTitle: String = "",
    val style: String = "",
    val roomType: String = "",
    val imageUrl: String? = null,
    val imageUri: String? = null,
    val sourceImageUri: String? = null,
    val sourceImageUrl: String? = null,
    val status: String = "completed",
    val errorMessage: String? = null,
    val prompt: String? = null,
    val budgetLabel: String = "",
    val createdAt: Double = 0.0,
    val isFavorite: Boolean = false,
)

data class BoardScreenState(
    val generatedItems: List<BoardItem> = emptyList(),
    val favoriteItems: List<BoardItem> = emptyList(),
    val projectItems: List<BoardItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

enum class BoardTab { Generated, Favorites, Projects }
