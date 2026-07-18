package com.ismail.homedecorai.ui.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.model.DiamondPackage
import com.ismail.homedecorai.model.DiamondStoreState
import com.ismail.homedecorai.model.TransactionStatus
import com.ismail.homedecorai.ui.components.ResponsiveDialog
import com.ismail.homedecorai.ui.theme.*

@Composable
fun SharedDiamondStoreSheet(
    state: DiamondStoreState,
    onClose: () -> Unit,
    onPurchase: (DiamondPackage) -> Unit,
    onClaimDaily: () -> Unit,
    onRestorePurchases: () -> Unit,
) {
    ResponsiveDialog(
        onDismissRequest = onClose,
        title = Strings.diamondStoreTitle,
        subtitle = Strings.diamondStoreSubtitle,
        maxWidth = 480.dp,
    ) {
        DiamondStoreContent(
            state = state,
            onPurchase = onPurchase,
            onClaimDaily = onClaimDaily,
            onRestorePurchases = onRestorePurchases,
        )
    }
}

@Composable
private fun DiamondStoreContent(
    state: DiamondStoreState,
    onPurchase: (DiamondPackage) -> Unit,
    onClaimDaily: () -> Unit,
    onRestorePurchases: () -> Unit,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(HomeDecorSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = CircleShape,
                color = HomeDecorExtra.diamondAccent.copy(alpha = 0.2f),
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Diamond,
                        contentDescription = null,
                        modifier = Modifier.size(HomeDecorIconSize.Large),
                        tint = HomeDecorExtra.diamondAccent,
                    )
                }
            }
            Column {
                Text(
                    Strings.myDiamondsBody(state.currentDiamonds),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    Strings.diamondYourBalance,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }

    if (state.transactionStatus == TransactionStatus.Processing) {
        Surface(
            shape = HomeDecorShape.Card,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Row(
                modifier = Modifier.padding(HomeDecorSpacing.Base),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "Processing purchase...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    if (state.transactionStatus == TransactionStatus.Success) {
        Surface(
            shape = HomeDecorShape.Card,
            color = Color(0xFF2E7D32).copy(alpha = 0.1f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Row(
                modifier = Modifier.padding(HomeDecorSpacing.Base),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF2E7D32).copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Diamond,
                            contentDescription = null,
                            modifier = Modifier.size(HomeDecorIconSize.Medium),
                            tint = Color(0xFF2E7D32),
                        )
                    }
                }
                Text(
                    "Purchase successful! Diamonds added to your balance.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }

    if (state.transactionStatus == TransactionStatus.Failed) {
        Surface(
            shape = HomeDecorShape.Card,
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Row(
                modifier = Modifier.padding(HomeDecorSpacing.Base),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Error,
                            contentDescription = null,
                            modifier = Modifier.size(HomeDecorIconSize.Medium),
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Purchase failed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        "Please try again or restore purchases.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }

    if (state.canClaimDaily) {
        val dailyInteraction = remember { MutableInteractionSource() }
        val dailyHovered by dailyInteraction.collectIsHoveredAsState()
        val dailyPressed by dailyInteraction.collectIsPressedAsState()
        val dailyScale by animateFloatAsState(
            targetValue = if (dailyPressed) 0.98f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessHigh),
            label = "dailyScale",
        )
        Surface(
            shape = HomeDecorShape.Card,
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (dailyHovered) 0.55f else 0.4f),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    role = Role.Button
                    contentDescription = Strings.diamondStoreDailyBonus
                }
                .clickable(interactionSource = dailyInteraction, indication = null) { onClaimDaily() }
                .scale(dailyScale),
        ) {
            Row(
                modifier = Modifier.padding(HomeDecorSpacing.Base),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    modifier = Modifier.size(44.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier.size(HomeDecorIconSize.Large),
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        Strings.diamondStoreDailyBonus,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        Strings.diamondStoreDailyBonusBody,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Surface(
                    shape = HomeDecorShape.Pill,
                    color = MaterialTheme.colorScheme.secondary,
                ) {
                    Text(
                        "+${state.dailyBonusAmount}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp),
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    Strings.diamondStoreLoading,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    } else if (state.packages.isNotEmpty()) {
        state.packages.forEach { pkg ->
            DiamondPackageCard(
                pkg = pkg,
                isPurchasing = state.purchaseInProgress == pkg.id,
                onClick = { onPurchase(pkg) },
            )
        }
    } else {
        DefaultDiamondPackages { pkg -> onPurchase(pkg) }
    }

    OutlinedButton(
        onClick = onRestorePurchases,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Icon(
            Icons.Rounded.Refresh,
            contentDescription = null,
            modifier = Modifier.size(HomeDecorIconSize.Small),
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            "Restore Purchases",
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun DiamondPackageCard(
    pkg: DiamondPackage,
    isPurchasing: Boolean,
    onClick: () -> Unit,
) {
    val cardInteraction = remember { MutableInteractionSource() }
    val cardHovered by cardInteraction.collectIsHoveredAsState()
    val cardPressed by cardInteraction.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (cardPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "diamondCardScale",
    )
    Surface(
        shape = HomeDecorShape.Card,
        color = if (cardHovered)
            MaterialTheme.colorScheme.surfaceContainerLow
        else
            MaterialTheme.colorScheme.surface,
        tonalElevation = HomeDecorElevation.Level1,
        border = BorderStroke(
            1.dp,
            if (cardHovered)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            else
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                role = Role.Button
                contentDescription = "Purchase ${pkg.name} diamond package"
            }
            .clickable(interactionSource = cardInteraction, indication = null, enabled = !isPurchasing) { onClick() }
            .scale(cardScale),
    ) {
        Row(
            modifier = Modifier.padding(HomeDecorSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = HomeDecorShape.Medium,
                color = Color.Transparent,
                modifier = Modifier.size(48.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(pkg.gradientStart, pkg.gradientEnd),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Rounded.Diamond,
                        contentDescription = null,
                        modifier = Modifier.size(HomeDecorIconSize.Large),
                        tint = Color.White,
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        pkg.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    if (pkg.badge != null) {
                        Surface(
                            shape = HomeDecorShape.Pill,
                            color = MaterialTheme.colorScheme.primary,
                        ) {
                            Text(
                                pkg.badge,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        "${pkg.diamonds} generations",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "\u00B7",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "${pkg.pricePerDiamond}/${Strings.diamondCostPer.lowercase()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            if (isPurchasing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            } else {
                Text(
                    pkg.price,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun DefaultDiamondPackages(onPurchase: (DiamondPackage) -> Unit) {
    val packages = listOf(
        DiamondPackage(
            id = "starter",
            name = "Starter",
            diamonds = 10,
            price = "$1.99",
            pricePerDiamond = "$0.20",
            gradientStart = Color(0xFF4DD9E0),
            gradientEnd = Color(0xFF0097A7),
        ),
        DiamondPackage(
            id = "creator",
            name = "Creator",
            diamonds = 30,
            price = "$4.99",
            pricePerDiamond = "$0.17",
            badge = "POPULAR",
            gradientStart = Color(0xFF9B6EFF),
            gradientEnd = Color(0xFF6A1B9A),
        ),
        DiamondPackage(
            id = "pro",
            name = "Pro",
            diamonds = 75,
            price = "$9.99",
            pricePerDiamond = "$0.13",
            badge = "BEST VALUE",
            gradientStart = Color(0xFF34D399),
            gradientEnd = Color(0xFF047857),
        ),
        DiamondPackage(
            id = "studio",
            name = "Studio",
            diamonds = 180,
            price = "$19.99",
            pricePerDiamond = "$0.11",
            gradientStart = Color(0xFFFFD166),
            gradientEnd = Color(0xFFB08D3A),
        ),
    )

    packages.forEach { pkg ->
        DiamondPackageCard(
            pkg = pkg,
            isPurchasing = false,
            onClick = { onPurchase(pkg) },
        )
    }
}
