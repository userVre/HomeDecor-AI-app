package com.ismail.homedecorai.ui.upgrade

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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

private class SharedUpgradeColors(
    val surface: Color,
    val cardSurface: Color,
    val accent: Color,
    val accentLight: Color,
    val accentSurface: Color,
    val gold: Color,
    val mint: Color,
    val mintSurface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val border: Color,
    val checkGreen: Color,
    val ctaDisabled: Color,
    val heroDivider: Color,
) {
    companion object {
        val Light = SharedUpgradeColors(
            surface = ProSurface,
            cardSurface = ProCardSurface,
            accent = ProCtaAccent,
            accentLight = ProAccentLight,
            accentSurface = ProAccentSurface,
            gold = ProGold,
            mint = ProMint,
            mintSurface = ProMint.copy(alpha = 0.08f),
            textPrimary = ProTextPrimary,
            textSecondary = ProTextSecondary,
            textMuted = ProTextMuted,
            border = ProBorder,
            checkGreen = ProCheckGreen,
            ctaDisabled = ProCtaDisabled,
            heroDivider = ProHeroDivider,
        )
        val Dark = SharedUpgradeColors(
            surface = ProDarkSurface,
            cardSurface = ProDarkCardSurface,
            accent = ProDarkAccent,
            accentLight = ProDarkAccentLight,
            accentSurface = ProDarkAccentSurface,
            gold = ProDarkGold,
            mint = ProDarkMint,
            mintSurface = ProDarkMint.copy(alpha = 0.08f),
            textPrimary = ProDarkTextPrimary,
            textSecondary = ProDarkTextSecondary,
            textMuted = ProDarkTextMuted,
            border = ProDarkBorder,
            checkGreen = ProCheckGreen,
            ctaDisabled = ProDarkCtaDisabled,
            heroDivider = ProDarkHeroDivider,
        )
    }
}

@Composable
fun SharedUpgradeScreen(
    isPro: Boolean,
    onOpenPaywall: () -> Unit,
) {
    if (isPro) {
        SharedProActiveScreen()
    } else {
        SharedUpgradeV3Screen(onOpenPaywall = onOpenPaywall)
    }
}

@Composable
private fun SharedProActiveScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.statusBars),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Xl),
        ) {
            Surface(
                shape = CircleShape,
                color = ProCheckGreen.copy(alpha = 0.15f),
                modifier = Modifier.size(72.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = ProCheckGreen,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }
            Text(
                text = Strings.proActivated,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = Strings.activeProAccess,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SharedUpgradeV3Screen(onOpenPaywall: () -> Unit) {
    val dark = LocalDarkTheme.current
    val colors = remember(dark) { if (dark) SharedUpgradeColors.Dark else SharedUpgradeColors.Light }
    val isDesktop = rememberIsDesktop()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val ctaScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cta_scale",
    )
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var selectedPlan by remember { mutableStateOf("yearly") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.surface)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.Lg))

            // ── Hero Section ──────────────────────────────────────────────
            if (isDesktop) {
                UpgradeDesktopHero(colors = colors)
            } else {
                UpgradeMobileHero(colors = colors)
            }

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // ── Before/After Hero ─────────────────────────────────────────
            SharedUpgradeBeforeAfterHero(colors = colors)

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // ── Plan Cards ────────────────────────────────────────────────
            if (isDesktop) {
                UpgradePlanCardsDesktop(
                    colors = colors,
                    selectedPlan = selectedPlan,
                    onPlanSelected = { selectedPlan = it },
                    onSelectPlan = { scope.launch { scrollState.animateScrollTo(scrollState.maxValue) } },
                )
            } else {
                UpgradePlanCardsMobile(
                    colors = colors,
                    selectedPlan = selectedPlan,
                    onPlanSelected = { selectedPlan = it },
                    onSelectPlan = { scope.launch { scrollState.animateScrollTo(scrollState.maxValue) } },
                )
            }

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // ── Comparison Table ──────────────────────────────────────────
            UpgradeComparisonTable(colors = colors)

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // ── Bottom CTA ────────────────────────────────────────────────
            UpgradeBottomCta(colors = colors, onGetPro = onOpenPaywall)

            Spacer(Modifier.height(120.dp))
        }

        // ── Sticky CTA Bar ──────────────────────────────────────────────
        Column {
            // Gradient scrim at top of sticky bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, colors.surface),
                        ),
                    ),
            )
            Surface(
                color = colors.surface,
                shadowElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Base)
                        .padding(bottom = 16.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = onOpenPaywall,
                        shape = HomeDecorShape.ButtonLarge,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.accent,
                            contentColor = HomeDecorExtra.onGradientText,
                        ),
                        contentPadding = PaddingValues(),
                        interactionSource = interactionSource,
                        modifier = Modifier
                            .widthIn(max = 480.dp)
                            .fillMaxWidth()
                            .height(HomeDecorSpacing.ButtonHeight)
                            .testTag(Strings.TestTags.upgradeCtaButton)
                            .scale(ctaScale),
                    ) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(HomeDecorSpacing.Sm))
                        Text(
                            text = Strings.upgradeV3Cta,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpgradeDesktopHero(colors: SharedUpgradeColors) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        modifier = Modifier.widthIn(max = 680.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = colors.accent.copy(alpha = 0.08f),
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    Strings.upgradeV3Trust,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = colors.accent,
                )
            }
        }

        Text(
            text = Strings.upgradeV3Headline,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 44.sp,
                letterSpacing = (-1).sp,
            ),
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Text(
            text = Strings.upgradeV3Subtitle,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun UpgradeMobileHero(colors: SharedUpgradeColors) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        Surface(
            shape = HomeDecorShape.PillMedium,
            color = colors.accent.copy(alpha = 0.08f),
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    Strings.upgradeV3Trust,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = colors.accent,
                )
            }
        }

        Text(
            text = Strings.upgradeV3Headline,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp,
                letterSpacing = (-0.5).sp,
            ),
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Text(
            text = Strings.upgradeV3Subtitle,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun UpgradePlanCardsDesktop(
    colors: SharedUpgradeColors,
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
    onSelectPlan: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
    ) {
        // Monthly Plan
        PlanCard(
            colors = colors,
            title = Strings.upgradePlanMonthly,
            price = Strings.upgradePlanMonthlyPrice,
            period = Strings.upgradePlanMonthlyPeriod,
            subtitle = null,
            description = Strings.upgradePlanMonthlyDesc,
            isRecommended = false,
            isSelected = selectedPlan == "monthly",
            onSelect = { onPlanSelected("monthly"); onSelectPlan() },
            modifier = Modifier.weight(1f),
        )

        // Yearly Plan (Recommended)
        PlanCard(
            colors = colors,
            title = Strings.upgradePlanYearly,
            price = Strings.upgradePlanYearlyPrice,
            period = Strings.upgradePlanYearlyPeriod,
            subtitle = Strings.upgradePlanYearlySave,
            description = Strings.upgradePlanYearlyDesc,
            isRecommended = true,
            isSelected = selectedPlan == "yearly",
            onSelect = { onPlanSelected("yearly"); onSelectPlan() },
            modifier = Modifier.weight(1f),
        )

        // Family / Team Plan
        PlanCard(
            colors = colors,
            title = Strings.upgradePlanFamily,
            price = Strings.upgradePlanFamilyPrice,
            period = Strings.upgradePlanFamilyPeriod,
            subtitle = Strings.upgradePlanFamilySeats,
            description = Strings.upgradePlanFamilyDesc,
            isRecommended = false,
            isSelected = selectedPlan == "family",
            onSelect = { onPlanSelected("family"); onSelectPlan() },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun UpgradePlanCardsMobile(
    colors: SharedUpgradeColors,
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
    onSelectPlan: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        PlanCard(
            colors = colors,
            title = Strings.upgradePlanMonthly,
            price = Strings.upgradePlanMonthlyPrice,
            period = Strings.upgradePlanMonthlyPeriod,
            subtitle = null,
            description = Strings.upgradePlanMonthlyDesc,
            isRecommended = false,
            isSelected = selectedPlan == "monthly",
            onSelect = { onPlanSelected("monthly"); onSelectPlan() },
            modifier = Modifier.fillMaxWidth(),
        )

        PlanCard(
            colors = colors,
            title = Strings.upgradePlanYearly,
            price = Strings.upgradePlanYearlyPrice,
            period = Strings.upgradePlanYearlyPeriod,
            subtitle = Strings.upgradePlanYearlySave,
            description = Strings.upgradePlanYearlyDesc,
            isRecommended = true,
            isSelected = selectedPlan == "yearly",
            onSelect = { onPlanSelected("yearly"); onSelectPlan() },
            modifier = Modifier.fillMaxWidth(),
        )

        PlanCard(
            colors = colors,
            title = Strings.upgradePlanFamily,
            price = Strings.upgradePlanFamilyPrice,
            period = Strings.upgradePlanFamilyPeriod,
            subtitle = Strings.upgradePlanFamilySeats,
            description = Strings.upgradePlanFamilyDesc,
            isRecommended = false,
            isSelected = selectedPlan == "family",
            onSelect = { onPlanSelected("family"); onSelectPlan() },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PlanCard(
    colors: SharedUpgradeColors,
    title: String,
    price: String,
    period: String,
    subtitle: String?,
    description: String?,
    isRecommended: Boolean,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected && isRecommended -> colors.gold
            isSelected -> colors.accent
            isRecommended -> colors.gold.copy(alpha = 0.6f)
            isHovered -> colors.accent.copy(alpha = 0.5f)
            else -> colors.border
        },
        animationSpec = tween(200),
        label = "plan_border",
    )
    val borderWidth = when {
        isSelected -> 2.5.dp
        isRecommended -> 2.dp
        else -> 1.dp
    }

    Box(modifier = modifier) {
        if (isRecommended) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = colors.gold,
                shadowElevation = 3.dp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-10).dp),
            ) {
                Row(
                    Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = HomeDecorExtra.onGradientText,
                        modifier = Modifier.size(12.dp),
                    )
                    Text(
                        Strings.upgradePlanBestValueSave,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = HomeDecorExtra.onGradientText,
                    )
                }
            }
        }

        Surface(
            shape = HomeDecorShape.CardLarge,
            color = when {
                isSelected && isRecommended -> colors.accentSurface
                isSelected -> colors.accentSurface.copy(alpha = 0.7f)
                isRecommended -> colors.accentSurface.copy(alpha = 0.3f)
                isHovered -> colors.accentSurface.copy(alpha = 0.2f)
                else -> colors.cardSurface
            },
            border = BorderStroke(borderWidth, borderColor),
            shadowElevation = if (isSelected || isRecommended) 6.dp else 0.dp,
            modifier = Modifier
                .then(if (isRecommended) Modifier.padding(top = 10.dp) else Modifier)
                .testTag(Strings.formatTestTag(Strings.TestTags.upgradePlanCard, title.lowercase()))
                .semantics {
                    role = Role.RadioButton
                    this.selected = isSelected
                    contentDescription = Strings.a11yUpgradePlanCard(title, price, period, isRecommended)
                }
                .clickable { onSelect() },
        ) {
            Column(
                modifier = Modifier.padding(HomeDecorSpacing.Base),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = colors.textPrimary,
                    )
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) colors.accent else Color.Transparent)
                            .then(
                                if (!isSelected) Modifier.border(2.dp, colors.textMuted.copy(alpha = 0.4f), CircleShape) else Modifier
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isSelected) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = HomeDecorExtra.onGradientText,
                                modifier = Modifier.size(14.dp),
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
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isSelected) colors.accent else colors.textPrimary,
                    )
                    Text(
                        period,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textMuted,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                }

                if (subtitle != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colors.checkGreen.copy(alpha = 0.12f),
                    ) {
                        Text(
                            subtitle,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = colors.checkGreen,
                        )
                    }
                }

                if (description != null) {
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                    )
                }
            }
        }
    }
}

private data class FeatureRow(val name: String, val freeValue: String, val proValue: String)

private val featureRows = listOf(
    FeatureRow(Strings.upgradeFeatureGenerations, freeValue = "1/day", proValue = "Unlimited"),
    FeatureRow(Strings.upgradeFeatureAiTools, freeValue = "Yes", proValue = "Yes"),
    FeatureRow(Strings.upgradeFeatureExport, freeValue = "No", proValue = "Yes"),
    FeatureRow(Strings.upgradeFeatureNoWatermark, freeValue = "No", proValue = "Yes"),
    FeatureRow(Strings.upgradeFeatureQueue, freeValue = "No", proValue = "Yes"),
)

@Composable
private fun UpgradeComparisonTable(colors: SharedUpgradeColors) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Text(
                Strings.upgradeFeatureTableTitle,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
            )

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(color = colors.border, thickness = 1.dp)

            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                colors.accent.copy(alpha = 0.08f),
                                colors.accent.copy(alpha = 0.04f),
                            ),
                        ),
                    )
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    Strings.upgradeFeatureTableHeaderFeature,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.textSecondary,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    Strings.upgradeFeatureTableHeaderFree,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.textMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(80.dp),
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    Strings.upgradeFeatureTableHeaderPro,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.accent,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(80.dp),
                )
            }

            HorizontalDivider(color = colors.border, thickness = 1.dp)

            // Feature Rows
            val freeRowBg = Color(0xFFFAF8F5)
            val whiteRowBg = Color(0xFFFFFFFF)
            val proColumnAccent = colors.accent.copy(alpha = 0.08f)

            featureRows.forEachIndexed { index, feature ->
                val rowBg = if (index % 2 == 0) freeRowBg else whiteRowBg
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBg)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        feature.name,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f),
                    )

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .background(freeRowBg, RoundedCornerShape(6.dp))
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            feature.freeValue,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = if (feature.freeValue == "Yes") colors.checkGreen else colors.textMuted,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .background(proColumnAccent, RoundedCornerShape(6.dp))
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            feature.proValue,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = colors.accent,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                if (index < featureRows.lastIndex) {
                    HorizontalDivider(color = colors.border.copy(alpha = 0.5f), thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
private fun UpgradeBottomCta(
    colors: SharedUpgradeColors,
    onGetPro: () -> Unit,
) {
        Surface(
            shape = HomeDecorShape.PillMedium,
        color = colors.accent.copy(alpha = 0.06f),
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.15f)),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colors.accent.copy(alpha = 0.10f),
                modifier = Modifier.size(56.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
            Text(
                Strings.upgradeV3Subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SharedUpgradeBeforeAfterHero(colors: SharedUpgradeColors) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow_alpha",
    )

    var sliderPosition by remember { mutableFloatStateOf(0.5f) }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.5.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        BoxWithConstraints(
            Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp)),
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .testTag(Strings.TestTags.upgradeBeforeAfter)
                    .semantics {
                        contentDescription = "${Strings.upgradeV3Before} and ${Strings.upgradeV3After} comparison"
                    }
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            change.consume()
                            sliderPosition = (change.position.x / size.width).coerceIn(0.1f, 0.9f)
                        }
                    },
            ) {
                val w = size.width
                val h = size.height
                val dividerX = w * sliderPosition

                drawContext.canvas.saveLayer(
                    androidx.compose.ui.geometry.Rect(0f, 0f, dividerX, h),
                    androidx.compose.ui.graphics.Paint(),
                )
                drawRoundRect(
                    color = colors.textMuted.copy(alpha = 0.10f),
                    topLeft = Offset(w * 0.02f, h * 0.08f),
                    size = Size(w * 0.44f, h * 0.74f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
                drawRoundRect(
                    color = colors.textMuted.copy(alpha = 0.18f),
                    topLeft = Offset(w * 0.08f, h * 0.48f),
                    size = Size(w * 0.30f, h * 0.18f),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                drawLine(
                    color = colors.textMuted.copy(alpha = 0.12f),
                    start = Offset(w * 0.04f, h * 0.66f),
                    end = Offset(w * 0.44f, h * 0.66f),
                    strokeWidth = 1.dp.toPx(),
                )
                drawContext.canvas.restore()

                drawContext.canvas.saveLayer(
                    androidx.compose.ui.geometry.Rect(dividerX, 0f, w, h),
                    androidx.compose.ui.graphics.Paint(),
                )
                drawRoundRect(
                    color = colors.accent.copy(alpha = 0.10f),
                    topLeft = Offset(w * 0.54f, h * 0.08f),
                    size = Size(w * 0.44f, h * 0.74f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
                drawRoundRect(
                    color = colors.gold.copy(alpha = glowAlpha * 0.18f),
                    topLeft = Offset(w * 0.62f, h * 0.12f),
                    size = Size(w * 0.24f, h * 0.26f),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                )
                drawRoundRect(
                    color = colors.accent.copy(alpha = 0.50f),
                    topLeft = Offset(w * 0.58f, h * 0.48f),
                    size = Size(w * 0.32f, h * 0.18f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
                drawRoundRect(
                    color = colors.gold.copy(alpha = 0.45f),
                    topLeft = Offset(w * 0.60f, h * 0.42f),
                    size = Size(w * 0.08f, h * 0.08f),
                    cornerRadius = CornerRadius(5.dp.toPx()),
                )
                drawRoundRect(
                    color = colors.gold.copy(alpha = 0.30f),
                    topLeft = Offset(w * 0.66f, h * 0.62f),
                    size = Size(w * 0.16f, h * 0.04f),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                )
                drawOval(
                    color = colors.accent.copy(alpha = 0.10f),
                    topLeft = Offset(w * 0.58f, h * 0.60f),
                    size = Size(w * 0.32f, h * 0.10f),
                )
                drawLine(
                    color = colors.gold.copy(alpha = 0.40f),
                    start = Offset(w * 0.92f, h * 0.66f),
                    end = Offset(w * 0.92f, h * 0.30f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawCircle(
                    color = colors.gold.copy(alpha = glowAlpha * 0.12f),
                    radius = w * 0.04f,
                    center = Offset(w * 0.92f, h * 0.28f),
                )
                drawContext.canvas.restore()

                drawLine(
                    color = colors.heroDivider.copy(alpha = 0.6f),
                    start = Offset(dividerX, h * 0.05f),
                    end = Offset(dividerX, h * 0.88f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                drawCircle(
                    color = colors.gold.copy(alpha = glowAlpha * 0.5f),
                    radius = 7.dp.toPx(),
                    center = Offset(dividerX, h * 0.20f),
                )
                drawCircle(
                    color = colors.mint.copy(alpha = glowAlpha * 0.35f),
                    radius = 4.dp.toPx(),
                    center = Offset(dividerX - 2.dp.toPx(), h * 0.35f),
                )
                drawCircle(
                    color = colors.gold.copy(alpha = glowAlpha * 0.25f),
                    radius = 3.dp.toPx(),
                    center = Offset(dividerX + 2.dp.toPx(), h * 0.55f),
                )
            }

            Surface(
                shape = CircleShape,
                color = colors.heroDivider,
                shadowElevation = 3.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = ((sliderPosition - 0.5f) * maxWidth.value).dp)
                    .size(32.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = HomeDecorExtra.onGradientText,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Text(
                Strings.upgradeV3Before.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                ),
                color = colors.textMuted.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 8.dp),
            )
            Text(
                Strings.upgradeV3After.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                ),
                color = colors.accent.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 8.dp),
            )
        }
    }
}
