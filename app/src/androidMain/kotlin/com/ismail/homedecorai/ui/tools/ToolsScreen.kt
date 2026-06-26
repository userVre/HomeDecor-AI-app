package com.ismail.homedecorai.ui.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.model.ToolItem
import com.ismail.homedecorai.model.ToolsScreenState

private val toolGradients = mapOf(
    "interior" to (Color(0xFF3A7CA5) to Color(0xFF1E3A5F)),
    "facade" to (Color(0xFF5B8C7A) to Color(0xFF2D5A4A)),
    "garden" to (Color(0xFF4A8B5C) to Color(0xFF2D5E3A)),
    "paint" to (Color(0xFF8B6DB5) to Color(0xFF5A3D7A)),
    "floor" to (Color(0xFFB8860B) to Color(0xFF7A5500)),
    "layout" to (Color(0xFFC46A3C) to Color(0xFF8B3A1A)),
    "replace" to (Color(0xFF5A8CA8) to Color(0xFF2E5A72)),
    "reference" to (Color(0xFF9B7B5A) to Color(0xFF6B4F35)),
)

@Composable
fun ToolsScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    val requestedToolIds = setOf("interior", "facade", "garden", "paint", "floor", "layout", "replace", "reference")
    val tools = HomeDecorCatalog.tools
        .filter { it.id in requestedToolIds }
        .map { tool ->
            val (start, end) = toolGradients[tool.id] ?: (Color(0xFF2E6B6E) to Color(0xFF1A4A4C))
            ToolItem(
                id = tool.id,
                title = tool.title,
                description = tool.description,
                gradientStart = start,
                gradientEnd = end,
            )
        }

    SharedToolsScreen(
        state = ToolsScreenState(isPro = state.isPro, diamonds = state.diamonds, tools = tools),
        onCredits = viewModel::openDiamondStore,
        onToolClick = { tool -> viewModel.startTool(HomeDecorCatalog.tools.first { it.id == tool.id }) },
    )
}
