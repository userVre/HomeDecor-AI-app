package com.ismail.homedecorai.ui.upgrade

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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
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
            checkGreen = HomeDecorColors.DarkCheckGreen,
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
                UpgradePlanCardsDesktop(colors = colors, onSelectPlan = { scope.launch { scrollState.animateScrollTo(scrollState.maxValue) } })
            } else {
                UpgradePlanCardsMobile(colors = colors, onSelectPlan = { scope.launch { scrollState.animateScrollTo(scrollState.maxValue) } })
            }

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // ── Benefits Grid ─────────────────────────────────────────────
            UpgradeBenefitsSection(colors = colors, isDesktop = isDesktop)

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // ── Comparison Table ──────────────────────────────────────────
            UpgradeComparisonTable(colors = colors)

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // ── Testimonials ──────────────────────────────────────────────
            UpgradeTestimonials(colors = colors, isDesktop = isDesktop)

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // ── Bottom CTA ────────────────────────────────────────────────
            UpgradeBottomCta(colors = colors, onJoinWaitlist = onOpenPaywall)

            Spacer(Modifier.height(120.dp))
        }

        // ── Sticky CTA Bar ──────────────────────────────────────────────
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
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accent,
                        contentColor = Color.White,
                    ),
                    contentPadding = PaddingValues(),
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.ButtonHeight)
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
                Text(
                    text = Strings.upgradeWaitlistNote,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textMuted,
                    textAlign = TextAlign.Center,
                )
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
                    Strings.upgradeV3TrialBadge + ". " + Strings.upgradeV3Trust,
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
                    Strings.upgradeV3TrialBadge + ". " + Strings.upgradeV3Trust,
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
            isRecommended = false,
            onSelect = onSelectPlan,
            modifier = Modifier.weight(1f),
        )

        // Yearly Plan (Recommended)
        PlanCard(
            colors = colors,
            title = Strings.upgradePlanYearly,
            price = Strings.upgradePlanYearlyPrice,
            period = Strings.upgradePlanYearlyPeriod,
            subtitle = Strings.upgradePlanYearlySave,
            isRecommended = true,
            onSelect = onSelectPlan,
            modifier = Modifier.weight(1f),
        )

        // Family / Team Plan
        PlanCard(
            colors = colors,
            title = Strings.upgradePlanFamily,
            price = Strings.upgradePlanFamilyPrice,
            period = Strings.upgradePlanFamilyPeriod,
            subtitle = Strings.upgradePlanFamilySubtitle,
            isRecommended = false,
            onSelect = onSelectPlan,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun UpgradePlanCardsMobile(
    colors: SharedUpgradeColors,
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
            isRecommended = false,
            onSelect = onSelectPlan,
            modifier = Modifier.fillMaxWidth(),
        )

        PlanCard(
            colors = colors,
            title = Strings.upgradePlanYearly,
            price = Strings.upgradePlanYearlyPrice,
            period = Strings.upgradePlanYearlyPeriod,
            subtitle = Strings.upgradePlanYearlySave,
            isRecommended = true,
            onSelect = onSelectPlan,
            modifier = Modifier.fillMaxWidth(),
        )

        PlanCard(
            colors = colors,
            title = Strings.upgradePlanFamily,
            price = Strings.upgradePlanFamilyPrice,
            period = Strings.upgradePlanFamilyPeriod,
            subtitle = Strings.upgradePlanFamilySubtitle,
            isRecommended = false,
            onSelect = onSelectPlan,
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
    isRecommended: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val borderColor = when {
        isRecommended -> colors.gold
        isHovered -> colors.accent.copy(alpha = 0.5f)
        else -> colors.border
    }
    val borderWidth = if (isRecommended) 2.dp else 1.dp

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isHovered) colors.accentSurface else colors.cardSurface,
        border = BorderStroke(borderWidth, borderColor),
        modifier = modifier,
    ) {
        Box {
            if (isRecommended) {
                Surface(
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                    color = colors.gold,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        Strings.upgradePlanBestValue,
                        modifier = Modifier.padding(vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(HomeDecorSpacing.Base),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                if (isRecommended) {
                    Spacer(Modifier.height(24.dp))
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        price,
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = colors.textPrimary,
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

                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.textPrimary,
                )
            }
        }
    }
}

@Composable
private fun UpgradeBenefitsSection(
    colors: SharedUpgradeColors,
    isDesktop: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        Text(
            Strings.upgradeFeatureCompare,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = colors.textPrimary,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(HomeDecorSpacing.Xs))

        if (isDesktop) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                BenefitCard(
                    colors = colors,
                    icon = Icons.Rounded.AutoAwesome,
                    title = Strings.upgradeBenefitUnlimited,
                    modifier = Modifier.weight(1f),
                )
                BenefitCard(
                    colors = colors,
                    icon = Icons.Rounded.Check,
                    title = Strings.upgradeBenefitExport,
                    modifier = Modifier.weight(1f),
                )
                BenefitCard(
                    colors = colors,
                    icon = Icons.Rounded.Star,
                    title = Strings.upgradeBenefitNoWatermark,
                    modifier = Modifier.weight(1f),
                )
                BenefitCard(
                    colors = colors,
                    icon = Icons.Rounded.AutoAwesome,
                    title = Strings.upgradeBenefitPriority,
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    BenefitCard(
                        colors = colors,
                        icon = Icons.Rounded.AutoAwesome,
                        title = Strings.upgradeBenefitUnlimited,
                        modifier = Modifier.weight(1f),
                    )
                    BenefitCard(
                        colors = colors,
                        icon = Icons.Rounded.Check,
                        title = Strings.upgradeBenefitExport,
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    BenefitCard(
                        colors = colors,
                        icon = Icons.Rounded.Star,
                        title = Strings.upgradeBenefitNoWatermark,
                        modifier = Modifier.weight(1f),
                    )
                    BenefitCard(
                        colors = colors,
                        icon = Icons.Rounded.AutoAwesome,
                        title = Strings.upgradeBenefitPriority,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun BenefitCard(
    colors: SharedUpgradeColors,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Md),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Surface(
                shape = CircleShape,
                color = colors.accent.copy(alpha = 0.12f),
                modifier = Modifier.size(36.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = colors.textPrimary,
            )
        }
    }
}

@Composable
private fun UpgradeComparisonTable(colors: SharedUpgradeColors) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.accent.copy(alpha = 0.06f))
                    .padding(horizontal = 20.dp, vertical = 16.dp),
            ) {
                Text(
                    "",
                    modifier = Modifier.weight(1f),
                )
                Text(
                    Strings.upgradeFreePlan,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.textMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(80.dp),
                )
                Text(
                    Strings.upgradeProPlan,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.accent,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(80.dp),
                )
            }

            HorizontalDivider(color = colors.border)

            // Rows
            ComparisonRow(
                colors = colors,
                feature = Strings.upgradeCompareGenerations,
                free = Strings.upgradeCompareGenerationsFree,
                pro = Strings.upgradeCompareGenerationsPro,
            )
            ComparisonRow(
                colors = colors,
                feature = Strings.upgradeCompareExport,
                free = Strings.upgradeCompareExportFree,
                pro = Strings.upgradeCompareExportPro,
            )
            ComparisonRow(
                colors = colors,
                feature = Strings.upgradeCompareWatermark,
                free = Strings.upgradeCompareWatermarkFree,
                pro = Strings.upgradeCompareWatermarkPro,
                isProGood = true,
            )
            ComparisonRow(
                colors = colors,
                feature = Strings.upgradeCompareSpeed,
                free = Strings.upgradeCompareSpeedFree,
                pro = Strings.upgradeCompareSpeedPro,
            )
            ComparisonRow(
                colors = colors,
                feature = Strings.upgradeCompareStyles,
                free = Strings.upgradeCompareStylesFree,
                pro = Strings.upgradeCompareStylesPro,
            )
            ComparisonRow(
                colors = colors,
                feature = Strings.upgradeCompareSupport,
                free = Strings.upgradeCompareSupportFree,
                pro = Strings.upgradeCompareSupportPro,
            )
        }
    }
}

@Composable
private fun ComparisonRow(
    colors: SharedUpgradeColors,
    feature: String,
    free: String,
    pro: String,
    isProGood: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            feature,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = colors.textPrimary,
            modifier = Modifier.weight(1f),
        )
        Text(
            free,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(80.dp),
        )
        Text(
            pro,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (isProGood) colors.checkGreen else colors.accent,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(80.dp),
        )
    }

    HorizontalDivider(color = colors.border.copy(alpha = 0.5f))
}

@Composable
private fun UpgradeTestimonials(
    colors: SharedUpgradeColors,
    isDesktop: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
    ) {
        if (isDesktop) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
            ) {
                TestimonialCard(
                    colors = colors,
                    quote = Strings.upgradeTestimonial1,
                    author = Strings.upgradeTestimonial1Author,
                    modifier = Modifier.weight(1f),
                )
                TestimonialCard(
                    colors = colors,
                    quote = Strings.upgradeTestimonial2,
                    author = Strings.upgradeTestimonial2Author,
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
            TestimonialCard(
                colors = colors,
                quote = Strings.upgradeTestimonial1,
                author = Strings.upgradeTestimonial1Author,
                modifier = Modifier.fillMaxWidth(),
            )
            TestimonialCard(
                colors = colors,
                quote = Strings.upgradeTestimonial2,
                author = Strings.upgradeTestimonial2Author,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun TestimonialCard(
    colors: SharedUpgradeColors,
    quote: String,
    author: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Base),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            // Stars
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                repeat(5) {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                        tint = colors.gold,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
            Text(
                "\"$quote\"",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = colors.textSecondary,
            )
            Text(
                author,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = colors.textMuted,
            )
        }
    }
}

@Composable
private fun UpgradeBottomCta(
    colors: SharedUpgradeColors,
    onJoinWaitlist: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colors.accent.copy(alpha = 0.06f),
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Icon(
                Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(32.dp),
            )
            Text(
                Strings.upgradeV3Headline,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                Strings.upgradeWaitlistNote,
                style = MaterialTheme.typography.bodyMedium,
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
                        tint = Color.White,
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
