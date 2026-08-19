package com.ismail.homedecorai.ui.store

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*

// ---------------------------------------------------------------------------
// PricingScreen  –  4-tier pricing with Monthly/Annual toggle
// ---------------------------------------------------------------------------

@Composable
fun PricingScreen(
    onPlanSelected: (String) -> Unit,
    onBack: () -> Unit,
) {
    val isDesktop = rememberIsDesktop()
    var isAnnual by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.Base,
                    end = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.Base,
                    top = HomeDecorSpacing.Xxl,
                    bottom = HomeDecorSpacing.Base,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = Strings.pricingPageTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            Text(
                text = Strings.pricingPageSubtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        // Monthly/Annual toggle
        PricingToggle(
            isAnnual = isAnnual,
            onToggle = { isAnnual = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.Base),
        )

        Spacer(Modifier.height(HomeDecorSpacing.Base))

        // Plan cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.Base),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            // Essential
            PricingPlanCard(
                name = Strings.upgradePlanEssential,
                credits = Strings.upgradePlanEssentialCredits,
                monthlyPrice = Strings.upgradePlanEssentialMonthlyPrice,
                yearlyPrice = Strings.upgradePlanEssentialYearlyPrice,
                monthlyPerCredit = Strings.upgradePlanEssentialMonthlyPerCredit,
                yearlyPerCredit = Strings.upgradePlanEssentialYearlyPerCredit,
                description = Strings.upgradePlanEssentialDesc,
                isPopular = false,
                isAnnual = isAnnual,
                onSelect = {
                    val planId = if (isAnnual) Strings.upgradePlanEssentialYearlyId else Strings.upgradePlanEssentialMonthlyId
                    onPlanSelected(planId)
                },
            )

            // Pro (Popular)
            PricingPlanCard(
                name = Strings.upgradePlanPro,
                credits = Strings.upgradePlanProCredits,
                monthlyPrice = Strings.upgradePlanProMonthlyPrice,
                yearlyPrice = Strings.upgradePlanProYearlyPrice,
                monthlyPerCredit = Strings.upgradePlanProMonthlyPerCredit,
                yearlyPerCredit = Strings.upgradePlanProYearlyPerCredit,
                description = Strings.upgradePlanProDesc,
                isPopular = true,
                isAnnual = isAnnual,
                onSelect = {
                    val planId = if (isAnnual) Strings.upgradePlanProYearlyId else Strings.upgradePlanProMonthlyId
                    onPlanSelected(planId)
                },
            )

            // Studio
            PricingPlanCard(
                name = Strings.upgradePlanStudio,
                credits = Strings.upgradePlanStudioCredits,
                monthlyPrice = Strings.upgradePlanStudioMonthlyPrice,
                yearlyPrice = Strings.upgradePlanStudioYearlyPrice,
                monthlyPerCredit = Strings.upgradePlanStudioMonthlyPerCredit,
                yearlyPerCredit = Strings.upgradePlanStudioYearlyPerCredit,
                description = Strings.upgradePlanStudioDesc,
                isPopular = false,
                isAnnual = isAnnual,
                onSelect = {
                    val planId = if (isAnnual) Strings.upgradePlanStudioYearlyId else Strings.upgradePlanStudioMonthlyId
                    onPlanSelected(planId)
                },
            )

            // Agency
            PricingPlanCard(
                name = Strings.upgradePlanAgency,
                credits = Strings.upgradePlanAgencyCredits,
                monthlyPrice = Strings.upgradePlanAgencyMonthlyPrice,
                yearlyPrice = Strings.upgradePlanAgencyYearlyPrice,
                monthlyPerCredit = Strings.upgradePlanAgencyMonthlyPerCredit,
                yearlyPerCredit = Strings.upgradePlanAgencyYearlyPerCredit,
                description = Strings.upgradePlanAgencyDesc,
                isPopular = false,
                isAnnual = isAnnual,
                onSelect = {
                    val planId = if (isAnnual) Strings.upgradePlanAgencyYearlyId else Strings.upgradePlanAgencyMonthlyId
                    onPlanSelected(planId)
                },
            )
        }

        Spacer(Modifier.height(HomeDecorSpacing.Base))

        // Trust copy near CTA
        Text(
            text = Strings.pricingTrustCta,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.Base),
        )

        Spacer(Modifier.height(HomeDecorSpacing.Xxl))
    }
}

// ---------------------------------------------------------------------------
// Monthly/Annual Toggle
// ---------------------------------------------------------------------------

@Composable
private fun PricingToggle(
    isAnnual: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(3.dp),
        ) {
            // Monthly
            ToggleOption(
                label = Strings.pricingToggleMonthly,
                selected = !isAnnual,
                onClick = { onToggle(false) },
                modifier = Modifier.weight(1f),
            )
            // Annual with discount pill
            ToggleOption(
                label = Strings.pricingToggleYearly,
                selected = isAnnual,
                onClick = { onToggle(true) },
                discountPill = Strings.pricingDiscountPill,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ToggleOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    discountPill: String? = null,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(200),
        label = "toggleBg",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(200),
        label = "toggleText",
    )

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .semantics {
                role = Role.Tab
                this.selected = selected
                contentDescription = "$label ${if (selected) "selected" else ""}"
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                color = textColor,
            )
            if (discountPill != null && selected) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White.copy(alpha = 0.25f),
                ) {
                    Text(
                        text = discountPill,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Plan Card
// ---------------------------------------------------------------------------

@Composable
private fun PricingPlanCard(
    name: String,
    credits: String,
    monthlyPrice: String,
    yearlyPrice: String,
    monthlyPerCredit: String,
    yearlyPerCredit: String,
    description: String,
    isPopular: Boolean,
    isAnnual: Boolean,
    onSelect: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "planCardScale",
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isPopular -> HomeDecorExtra.premiumGold
            isHovered -> MaterialTheme.colorScheme.outline
            else -> MaterialTheme.colorScheme.outlineVariant
        },
        animationSpec = tween(200),
        label = "planBorder",
    )

    val price = if (isAnnual) yearlyPrice else monthlyPrice
    val perCredit = if (isAnnual) yearlyPerCredit else monthlyPerCredit

    Surface(
        shape = HomeDecorShape.CardLarge,
        color = when {
            isPopular -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            isHovered -> MaterialTheme.colorScheme.surfaceContainerLow
            else -> MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = if (isPopular) 2.dp else 1.dp,
            color = borderColor,
        ),
        shadowElevation = if (isPopular) 8.dp else 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .semantics {
                role = Role.RadioButton
                contentDescription = "$name plan. $price. $credits credits."
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onSelect,
            ),
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Lg),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            // Header row: name + popular badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (isPopular) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = HomeDecorExtra.premiumGold,
                        shadowElevation = 4.dp,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(
                                Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp),
                            )
                            Text(
                                text = Strings.upgradePlanPopular,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                        }
                    }
                }
            }

            // Credits
            Text(
                text = "${Strings.creditsPerMonth(credits)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Price
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = price,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isPopular) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = if (isAnnual) "/year" else "/month",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 3.dp),
                )
            }

            // Per-credit price
            Text(
                text = Strings.perCreditPrice(perCredit),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            // Description
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Xs))

            // CTA Button
            Button(
                onClick = onSelect,
                shape = HomeDecorShape.Button,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPopular) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (isPopular) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                ),
                border = if (!isPopular) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HomeDecorSpacing.ButtonHeightSmall),
            ) {
                Text(
                    text = Strings.pwS4Cta,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
