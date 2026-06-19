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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.model.DecorTool
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.R
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
        )
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
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
) {
    Surface(color = StudioCanvas, tonalElevation = 0.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.nav_tools),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f),
            )
            val creditsDescription = stringResource(R.string.a11y_diamond_credits)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.semantics {
                    contentDescription = creditsDescription
                },
            ) {
                Icon(
                    Icons.Rounded.Diamond,
                    contentDescription = stringResource(R.string.a11y_diamond_credits),
                    modifier = Modifier.size(14.dp),
                    tint = StudioBlue,
                )
                Text(
                    if (state.isPro) stringResource(R.string.pro_upper) else "${state.diamonds}",
                    style = MaterialTheme.typography.titleSmall,
                )
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
    val cardShape = RoundedCornerShape(14.dp)
    val toolCardDescription = stringResource(R.string.a11y_tool_card_format, title, description)

    Surface(
        onClick = onClick,
        shape = cardShape,
        color = StudioPaper,
        modifier = Modifier
            .minimumTouchTarget()
            .clip(cardShape)
            .semantics {
                contentDescription = toolCardDescription
                role = Role.Button
            },
    ) {
        Box(Modifier.fillMaxWidth().height(120.dp)) {
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
                            0f to Color.Black.copy(alpha = 0.0f),
                            0.4f to Color.Black.copy(alpha = 0.15f),
                            0.7f to Color.Black.copy(alpha = 0.50f),
                            1f to Color.Black.copy(alpha = 0.75f),
                        ),
                    ),
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Text(
                    title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    description,
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Surface(
                        onClick = onClick,
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.height(28.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Rounded.AutoAwesome,
                                null,
                                Modifier.size(12.dp),
                                tint = Color.White,
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                stringResource(R.string.try_this),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                    Icon(
                        Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White.copy(alpha = 0.55f),
                    )
                }
            }
        }
    }
}
