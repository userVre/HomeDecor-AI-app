package com.ismail.homedecorai.ui.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.components.ResponsiveDialog
import com.ismail.homedecorai.ui.theme.*

@Composable
fun PricingDialog(
    onDismiss: () -> Unit,
    onUpgrade: () -> Unit,
) {
    ResponsiveDialog(
        onDismissRequest = onDismiss,
        title = Strings.pricingDialogTitle,
        subtitle = Strings.pricingCompareTitle,
        footer = {
            Button(
                onClick = onUpgrade,
                shape = HomeDecorShape.Button,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HomeDecorSpacing.ButtonHeight),
            ) {
                Text(
                    Strings.pwS4Cta,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
    ) {
        PricingComparisonTable()
    }
}

@Composable
private fun PricingComparisonTable() {
    Column(
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
    ) {
        PlanCard(
            title = Strings.pricingFreePlanTitle,
            price = "$0",
            period = "",
            features = listOf(
                Strings.pricingFreeLimit,
                Strings.upgradeCompareExportFree,
            ),
            accent = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
        PlanCard(
            title = Strings.pricingProPlanTitle,
            price = "\$3.33",
            period = "/month · billed annually",
            features = listOf(
                Strings.pricingProLimit,
                Strings.upgradeCompareExportPro,
                Strings.pricingRenewalBehavior,
                Strings.pricingCancelPolicy,
            ),
            accent = HomeDecorExtra.premiumGold.copy(alpha = 0.12f),
            isRecommended = true,
            yearlyEquivalent = "$39.99/year · save $55.89 vs monthly",
        )
        PlanCard(
            title = "Monthly",
            price = "\$7.99",
            period = "/month",
            features = listOf(
                Strings.pricingProLimit,
                Strings.upgradeCompareExportPro,
                Strings.pricingRenewalBehavior,
                Strings.pricingCancelPolicy,
            ),
            accent = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    period: String,
    features: List<String>,
    accent: androidx.compose.ui.graphics.Color,
    isRecommended: Boolean = false,
    yearlyEquivalent: String? = null,
) {
    Surface(
        shape = HomeDecorShape.CardLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HomeDecorElevation.Level1,
        border = androidx.compose.foundation.BorderStroke(
            if (isRecommended) 2.dp else 1.dp,
            if (isRecommended) HomeDecorExtra.premiumGold else MaterialTheme.colorScheme.outlineVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "$title plan. $price${if (period.isNotBlank()) " $period" else ""}. Features: ${features.joinToString(", ")}"
                role = Role.Button
            },
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Lg),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.semantics { heading() },
                )
                if (isRecommended) {
                    Surface(
                        shape = HomeDecorShape.Pill,
                        color = HomeDecorExtra.premiumGold.copy(alpha = 0.15f),
                    ) {
                        Text(
                            Strings.pwS4PlanYearlyBadge,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = HomeDecorExtra.premiumGold,
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    price,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (period.isNotBlank()) {
                    Text(
                        period,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 3.dp),
                    )
                }
            }
            if (yearlyEquivalent != null) {
                Text(
                    yearlyEquivalent,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            features.forEach { feature ->
                PricingFeatureRow(feature)
            }
        }
    }
}

@Composable
private fun PricingFeatureRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        Text(
            "\u2713",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
