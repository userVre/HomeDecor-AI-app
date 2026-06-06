package com.ismail.homedecorai.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.BoardItem
import com.ismail.homedecorai.GeneratedResult
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.PendingPurchaseSync
import com.ismail.homedecorai.Project
import com.ismail.homedecorai.R
import com.ismail.homedecorai.isGeneratedResult
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.boardToolTitleRes
import com.ismail.homedecorai.ui.utility.choiceIcon
import com.ismail.homedecorai.ui.utility.choiceImageRes
import com.ismail.homedecorai.ui.utility.localizedOption
import com.ismail.homedecorai.ui.utility.paletteColors
import com.ismail.homedecorai.ui.utility.ExamplePhoto
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun FeatureRow(
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
fun FeatureRowOnDark(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f)) {
            Icon(icon, contentDescription = null, tint = PaywallPremiumGold, modifier = Modifier.padding(8.dp).size(18.dp))
        }
        Text(text, color = PaywallTextSecondary, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ComparisonBadge(label: String) {
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
fun CompactFilterChip(
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
fun ValidationNotice(message: String) {
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

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 72.dp),
        color = StudioLine.copy(alpha = 0.65f),
    )
}

@Composable
fun ToolToggle(
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
fun MaskActionButton(
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
fun StyleChoiceCard(
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
fun ExpressiveChoiceChip(
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
fun ReplaceSuggestionChip(
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

@Composable
fun ColorSwatchCard(
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

@Composable
fun ModeCard(
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
fun PaletteChoiceCard(
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
fun DailyRewardCard(
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
fun DailyRewardQuietPill(
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
fun CreditPill(state: HomeDecorUiState) {
    CreditPill(state = state, compact = false)
}

@Composable
fun CreditPill(
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

@Composable
fun BoardCard(item: BoardItem) {
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
fun ReferenceImagePicker(
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
fun ExamplePhotoCard(
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
fun ProjectHeaderPreview(
    project: Project,
    results: List<GeneratedResult>,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            WorkspaceImage(
                imageUrl = project.coverImageUrl,
                imageUri = project.coverImageUri ?: project.originalPhotoUris.firstOrNull(),
                contentDescription = project.name,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)),
            )
            Column(Modifier.weight(1f)) {
                Text(
                    project.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    project.roomType.ifBlank { "No room type" },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (results.isNotEmpty()) {
                    Text(
                        "${results.size} result${if (results.size > 1) "s" else ""}",
                        color = StudioBlue,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectMetricChip(
    icon: ImageVector,
    text: String,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = StudioMist,
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = StudioInk)
            Text(text, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProjectResultThumb(
    title: String,
    imageUrl: String?,
    imageUri: String?,
    imageRes: Int = 0,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
        modifier = Modifier.size(100.dp),
    ) {
        Box {
            WorkspaceImage(
                imageUrl = imageUrl,
                imageUri = imageUri,
                imageRes = imageRes,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
            )
            Surface(
                shape = RoundedCornerShape(bottomStart = 14.dp),
                color = Color.Black.copy(alpha = 0.54f),
                modifier = Modifier.align(Alignment.BottomStart),
            ) {
                Text(
                    title,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun PurchaseSyncNotice(
    message: String,
    pending: Boolean,
    busy: Boolean,
    onRetry: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = StudioErrorContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(Icons.Rounded.Error, contentDescription = null, modifier = Modifier.size(18.dp), tint = StudioRose)
            Text(
                message,
                modifier = Modifier.weight(1f),
                color = StudioRose,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
            if (pending) {
                OutlinedButton(
                    onClick = onRetry,
                    enabled = !busy,
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(38.dp),
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Retry", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
