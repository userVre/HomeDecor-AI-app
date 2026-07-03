package com.ismail.homedecorai.ui.store

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*

data class DiamondPackage(
    val id: String,
    val name: String,
    val diamonds: Int,
    val price: String,
    val pricePerDiamond: String,
    val badge: String? = null,
    val gradientStart: Color,
    val gradientEnd: Color,
)

data class DiamondStoreState(
    val currentDiamonds: Int = 0,
    val packages: List<DiamondPackage> = emptyList(),
    val isLoading: Boolean = false,
    val purchaseInProgress: String? = null,
    val purchaseSuccess: Boolean = false,
    val canClaimDaily: Boolean = true,
    val dailyBonusAmount: Int = 5,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedDiamondStoreSheet(
    state: DiamondStoreState,
    onClose: () -> Unit,
    onPurchase: (DiamondPackage) -> Unit,
    onClaimDaily: () -> Unit,
) {
    val isDesktop = rememberIsDesktop()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    if (isDesktop) {
        DiamondStoreDialogContent(
            state = state,
            onClose = onClose,
            onPurchase = onPurchase,
            onClaimDaily = onClaimDaily,
        )
    } else {
        ModalBottomSheet(
            onDismissRequest = onClose,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            DiamondStoreContent(
                state = state,
                onClose = onClose,
                onPurchase = onPurchase,
                onClaimDaily = onClaimDaily,
            )
        }
    }
}

@Composable
private fun DiamondStoreDialogContent(
    state: DiamondStoreState,
    onClose: () -> Unit,
    onPurchase: (DiamondPackage) -> Unit,
    onClaimDaily: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f),
        onClick = onClose,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = HomeDecorShape.ExtraExtraLarge,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = HomeDecorElevation.Level3,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .width(420.dp)
                    .clickable(enabled = false) { },
            ) {
                DiamondStoreContent(
                    state = state,
                    onClose = onClose,
                    onPurchase = onPurchase,
                    onClaimDaily = onClaimDaily,
                )
            }
        }
    }
}

@Composable
private fun DiamondStoreContent(
    state: DiamondStoreState,
    onClose: () -> Unit,
    onPurchase: (DiamondPackage) -> Unit,
    onClaimDaily: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = HomeDecorSpacing.Xl),
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Xl, vertical = HomeDecorSpacing.Md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    Strings.diamondStoreTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    Strings.diamondStoreSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onClose) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = Strings.close,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Current balance
        Surface(
            shape = HomeDecorShape.Card,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            modifier = Modifier
                .fillMaxWidth()
                    .padding(horizontal = HomeDecorSpacing.Xl),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
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
                            modifier = Modifier.size(24.dp),
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
                        "Your current balance",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Daily bonus
        if (state.canClaimDaily) {
            Surface(
                shape = HomeDecorShape.Card,
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clickable(onClick = onClaimDaily),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
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
                                modifier = Modifier.size(24.dp),
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            Spacer(Modifier.height(16.dp))
        }

        // Package list
        if (state.packages.isNotEmpty()) {
            Text(
                "Available packages",
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Xl),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.packages) { pkg ->
                    DiamondPackageCard(
                        pkg = pkg,
                        isPurchasing = state.purchaseInProgress == pkg.id,
                        onClick = { onPurchase(pkg) },
                    )
                }
            }
        } else if (state.isLoading) {
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
        } else {
            // Show default packages
            DefaultDiamondPackages { pkg -> onPurchase(pkg) }
        }
    }
}

@Composable
private fun DiamondPackageCard(
    pkg: DiamondPackage,
    isPurchasing: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .semantics { role = Role.Button }
            .clickable(enabled = !isPurchasing, onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Diamond icon with gradient
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
                        modifier = Modifier.size(24.dp),
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
                Text(
                    "${pkg.diamonds} diamonds \u00B7 ${pkg.pricePerDiamond}/diamond",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
            diamonds = 50,
            price = "$1.99",
            pricePerDiamond = "$0.04",
            gradientStart = Color(0xFF4DD9E0),
            gradientEnd = Color(0xFF0097A7),
        ),
        DiamondPackage(
            id = "designer",
            name = "Creator",
            diamonds = 150,
            price = "$4.99",
            pricePerDiamond = "$0.03",
            badge = "POPULAR",
            gradientStart = Color(0xFF9B6EFF),
            gradientEnd = Color(0xFF6A1B9A),
        ),
        DiamondPackage(
            id = "architect",
            name = "Pro",
            diamonds = 400,
            price = "$9.99",
            pricePerDiamond = "$0.02",
            badge = "BEST VALUE",
            gradientStart = Color(0xFF34D399),
            gradientEnd = Color(0xFF047857),
        ),
        DiamondPackage(
            id = "estate",
            name = "Ultimate",
            diamonds = 1000,
            price = "$19.99",
            pricePerDiamond = "$0.02",
            gradientStart = Color(0xFFFFD166),
            gradientEnd = Color(0xFFB08D3A),
        ),
    )

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(packages) { pkg ->
            DiamondPackageCard(
                pkg = pkg,
                isPurchasing = false,
                onClick = { onPurchase(pkg) },
            )
        }
    }
}
