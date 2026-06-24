package com.ismail.homedecorai.ui.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
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
import com.ismail.homedecorai.model.DecorTool
import com.ismail.homedecorai.HomeDecorCatalog
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*

private val requestedToolIds = setOf(
    "interior", "facade", "garden", "paint", "floor", "layout",
    "replace", "reference"
)

private val ToolCardShape = RoundedCornerShape(20.dp)
private val CtaPillShape = RoundedCornerShape(14.dp)

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
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.ScreenHorizontal,
                end = HomeDecorSpacing.ScreenHorizontal,
                top = HomeDecorSpacing.Lg,
                bottom = navBarBottomPadding(additionalContentPadding = 140.dp),
            ),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.ListItemGap),
        ) {
            items(
                HomeDecorCatalog.tools.filter { it.id in requestedToolIds },
                key = { it.id },
            ) { tool ->
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
                .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.nav_tools),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f),
            )
            val creditsDescription = stringResource(R.string.a11y_open_diamond_store)
            Surface(
                onClick = onCredits,
                shape = RoundedCornerShape(14.dp),
                color = StudioPaper,
                tonalElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, DiamondTeal.copy(alpha = 0.2f)),
                modifier = Modifier
                    .semantics {
                        contentDescription = creditsDescription
                        role = Role.Button
                    },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                    modifier = Modifier
                        .height(HomeDecorSpacing.TouchTarget)
                        .padding(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(
                        Icons.Rounded.Diamond,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = DiamondTeal,
                    )
                    Text(
                        if (state.isPro) stringResource(R.string.pro_upper) else "${state.diamonds}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
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
    val toolCardDescription = stringResource(R.string.a11y_tool_card_format, title, description)

    Surface(
        onClick = onClick,
        shape = ToolCardShape,
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(ToolCardShape)
            .semantics {
                contentDescription = toolCardDescription
                role = Role.Button
            },
    ) {
        Box {
            Image(
                painter = painterResource(tool.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color.Transparent,
                            0.40f to Color.Transparent,
                            0.60f to Color.Black.copy(alpha = 0.08f),
                            0.75f to Color.Black.copy(alpha = 0.18f),
                            0.88f to Color.Black.copy(alpha = 0.35f),
                            1.0f to Color.Black.copy(alpha = 0.50f),
                        ),
                    ),
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Base),
            ) {
                Text(
                    title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(HomeDecorSpacing.Xs))
                Text(
                    description,
                    color = Color.White.copy(alpha = 0.90f),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(HomeDecorSpacing.Md))
                Surface(
                    shape = CtaPillShape,
                    color = Color.White,
                    modifier = Modifier.widthIn(min = 120.dp),
                ) {
                    Row(
                        Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Sm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(HomeDecorSpacing.Base),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(
                            stringResource(R.string.try_this),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}
