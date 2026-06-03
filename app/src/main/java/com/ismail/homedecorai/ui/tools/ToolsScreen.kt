package com.ismail.homedecorai.ui.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.DecorTool
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.components.*
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*

@Composable
fun ToolsScreen(
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
fun ToolsHeader(
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
fun ToolCard(
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
