package com.ismail.homedecorai.ui.paywall

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.showToast
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.theme.isReducedMotionEnabled
import homedecorai.app.generated.resources.Res
import homedecorai.app.generated.resources.assets_media_paywall_carouseljapandibedroom
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource

private const val TOTAL_STEPS = 2

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SharedPaywallSheet(
    state: PaywallState,
    onClose: () -> Unit,
    onPlanSelected: (String) -> Unit,
    onContinue: () -> Unit,
    onRestore: () -> Unit,
) {
    val proColors = rememberSheetPalette()
    val modalTapBlocker = remember { MutableInteractionSource() }
    var currentStep by remember { mutableIntStateOf(1) }
    var checkoutLoading by remember { mutableStateOf(false) }
    var checkoutError by remember { mutableStateOf<String?>(null) }
    var checkoutSuccess by remember { mutableStateOf(false) }
    val isDesktop = rememberIsDesktop()

    fun goBack() {
        if (currentStep > 1) currentStep-- else onClose()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(proColors.overlayBg)
            .clickable(interactionSource = modalTapBlocker, indication = null, onClick = {})
            .testTag(Strings.TestTags.paywallSheet),
    ) {
        if (isDesktop) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .widthIn(max = 840.dp)
                    .fillMaxHeight(0.94f)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(proColors.gradientStart, proColors.gradientMid, proColors.gradientEnd),
                        ),
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    proColors.accent.copy(alpha = 0.06f),
                                    Color.Transparent,
                                ),
                                radius = 400f,
                            ),
                        ),
                )
                Column(Modifier.fillMaxSize()) {
                    PaywallTopBar(
                        currentStep = currentStep,
                        onClose = onClose,
                        onBack = ::goBack,
                    )

                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 32.dp)
                            .testTag(Strings.formatTestTag(Strings.TestTags.paywallStepContent, currentStep)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        when (currentStep) {
                            1 -> PaywallStep1ValueProp(
                                colors = proColors,
                                selectedPlan = state.selectedPlanId,
                                onPlanSelected = { onPlanSelected(it) },
                            )
                            2 -> PaywallStep5Checkout(
                                colors = proColors,
                                selectedPlan = state.selectedPlanId,
                                purchasing = state.purchasing || checkoutLoading,
                                purchaseSuccess = state.purchaseSuccess || checkoutSuccess,
                                errorMessage = checkoutError,
                            )
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    val ctaLabel = when {
                        checkoutLoading -> "Processing..."
                        checkoutSuccess -> "Subscribed!"
                        currentStep == 2 -> Strings.pwS5Cta
                        else -> Strings.pwS1Cta
                    }

                    PaywallBottomCta(
                        colors = proColors,
                        label = ctaLabel,
                        enabled = !checkoutLoading && !checkoutSuccess,
                        processing = checkoutLoading,
                        success = checkoutSuccess,
                        onClick = {
                            checkoutError = null
                            when (currentStep) {
                                1 -> currentStep = 2
                                2 -> {
                                    checkoutLoading = true
                                    onContinue()
                                }
                            }
                        },
                        onRestore = onRestore,
                    )
                }
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(proColors.gradientStart, proColors.gradientMid, proColors.gradientEnd),
                        ),
                    )
                    .windowInsetsPadding(WindowInsets.statusBars),
            ) {
                Column(Modifier.fillMaxSize()) {
                    PaywallTopBar(
                        currentStep = currentStep,
                        onClose = onClose,
                        onBack = ::goBack,
                    )

                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                            .testTag(Strings.formatTestTag(Strings.TestTags.paywallStepContent, currentStep)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        when (currentStep) {
                            1 -> PaywallStep1ValueProp(
                                colors = proColors,
                                selectedPlan = state.selectedPlanId,
                                onPlanSelected = { onPlanSelected(it) },
                            )
                            2 -> PaywallStep5Checkout(
                                colors = proColors,
                                selectedPlan = state.selectedPlanId,
                                purchasing = state.purchasing || checkoutLoading,
                                purchaseSuccess = state.purchaseSuccess || checkoutSuccess,
                                errorMessage = checkoutError,
                            )
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    val ctaLabel = when {
                        checkoutLoading -> "Processing..."
                        checkoutSuccess -> "Subscribed!"
                        currentStep == 2 -> Strings.pwS5Cta
                        else -> Strings.pwS1Cta
                    }

                    PaywallBottomCta(
                        colors = proColors,
                        label = ctaLabel,
                        enabled = !checkoutLoading && !checkoutSuccess,
                        processing = checkoutLoading,
                        success = checkoutSuccess,
                        onClick = {
                            checkoutError = null
                            when (currentStep) {
                                1 -> currentStep = 2
                                2 -> {
                                    checkoutLoading = true
                                    onContinue()
                                }
                            }
                        },
                        onRestore = onRestore,
                    )
                }
            }
        }
    }
}

@Composable
private fun PaywallTopBar(
    currentStep: Int,
    onClose: () -> Unit,
    onBack: () -> Unit,
) {
    val isDesktop = rememberIsDesktop()
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 4.dp)
            .testTag(Strings.TestTags.paywallTopBar),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (currentStep == 1 && !isDesktop) {
            Spacer(Modifier.size(48.dp))
        } else {
            IconButton(
                onClick = if (currentStep == 1) onClose else onBack,
                modifier = Modifier
                    .size(48.dp)
                    .testTag(Strings.TestTags.paywallBackButton),
            ) {
                Icon(
                    imageVector = if (currentStep == 1) Icons.Rounded.Close else Icons.Rounded.ArrowBack,
                    contentDescription = if (currentStep == 1) Strings.proA11yClose else Strings.paywallA11yBack,
                    tint = ProTextSecondary,
                    modifier = Modifier.size(HomeDecorIconSize.Large),
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = ProCardSurface,
            border = BorderStroke(1.dp, ProBorder),
            modifier = Modifier
                .height(28.dp)
                .testTag(Strings.TestTags.paywallStepIndicator)
                .semantics { contentDescription = "Step $currentStep of $TOTAL_STEPS" },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 12.dp),
            ) {
                Text(
                    "$currentStep / $TOTAL_STEPS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                    ),
                    color = ProTextPrimary,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(48.dp)
                .testTag(Strings.TestTags.paywallCloseButton),
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = Strings.proA11yClose,
                tint = ProTextSecondary,
                modifier = Modifier.size(HomeDecorIconSize.Large),
            )
        }
    }
}

// ── Step 1: Plan Selection ─────────────────────────────────────────────────

@Composable
private fun PaywallStep1ValueProp(
    colors: SheetPalette,
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
) {
    var isYearly by remember { mutableStateOf(selectedPlan.endsWith("yearly")) }

    // Heading — brings the user to the decision immediately
    Text(
        Strings.pwS1Heading,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp,
        ),
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(4.dp))

    Text(
        Strings.pwS1HeadingHighlight,
        style = MaterialTheme.typography.bodyMedium,
        color = colors.textMuted,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(10.dp))

    // Compact hero — visual proof, not a wall of text
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp)),
    ) {
        Image(
            painter = painterResource(Res.drawable.assets_media_paywall_carouseljapandibedroom),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            HomeDecorExtra.scrimLight,
                            HomeDecorExtra.scrimMedium,
                        ),
                    ),
                ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = HomeDecorExtra.scrimHeavy,
            ) {
                Text(
                    "BEFORE",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    color = HomeDecorExtra.onGradientText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                )
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = colors.accent.copy(alpha = 0.85f),
            ) {
                Text(
                    "AFTER",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    color = HomeDecorExtra.onGradientText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }

    Spacer(Modifier.height(10.dp))

    // ── Monthly / Annual Toggle ───────────────────────────────────────
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
    ) {
        Row {
            listOf(
                "Monthly" to false,
                "Annual" to true,
            ).forEach { (label, yearly) ->
                val selected = isYearly == yearly
                Surface(
                    onClick = { isYearly = yearly },
                    shape = RoundedCornerShape(14.dp),
                    color = if (selected) colors.accent else Color.Transparent,
                    modifier = Modifier.padding(4.dp),
                ) {
                    Text(
                        label,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = if (selected) Color.White else colors.textMuted,
                    )
                }
            }
        }
    }
    if (isYearly) {
        Spacer(Modifier.height(4.dp))
        Text(
            Strings.pricingDiscountPill + " with annual billing",
            style = MaterialTheme.typography.labelSmall,
            color = ProCheckGreen,
            fontWeight = FontWeight.Medium,
        )
    }

    Spacer(Modifier.height(10.dp))

    // ── 4-Tier Plan Cards ─────────────────────────────────────────────
    PaywallPlanCard(
        colors = colors,
        title = Strings.upgradePlanEssential,
        price = if (isYearly) Strings.upgradePlanEssentialYearlyPrice else Strings.upgradePlanEssentialMonthlyPrice,
        pricePer = "/${if (isYearly) "year" else "month"}",
        detail = if (isYearly) "${Strings.upgradePlanEssentialYearlyCredits} credits \u00B7 ${Strings.upgradePlanEssentialYearlyPerCredit} per credit" else "${Strings.upgradePlanEssentialCredits} credits \u00B7 ${Strings.upgradePlanEssentialMonthlyPerCredit} per credit",
        badge = null,
        selected = selectedPlan == if (isYearly) "essential_yearly" else "essential_monthly",
        onClick = { onPlanSelected(if (isYearly) "essential_yearly" else "essential_monthly") },
    )

    Spacer(Modifier.height(6.dp))

    PaywallPlanCard(
        colors = colors,
        title = Strings.upgradePlanPro,
        price = if (isYearly) Strings.upgradePlanProYearlyPrice else Strings.upgradePlanProMonthlyPrice,
        pricePer = "/${if (isYearly) "year" else "month"}",
        detail = if (isYearly) "${Strings.upgradePlanProYearlyCredits} credits \u00B7 ${Strings.upgradePlanProYearlyPerCredit} per credit" else "${Strings.upgradePlanProCredits} credits \u00B7 ${Strings.upgradePlanProMonthlyPerCredit} per credit",
        badge = Strings.upgradePlanPopular,
        isPopular = true,
        selected = selectedPlan == if (isYearly) "pro_yearly" else "pro_monthly",
        onClick = { onPlanSelected(if (isYearly) "pro_yearly" else "pro_monthly") },
    )

    Spacer(Modifier.height(6.dp))

    PaywallPlanCard(
        colors = colors,
        title = Strings.upgradePlanStudio,
        price = if (isYearly) Strings.upgradePlanStudioYearlyPrice else Strings.upgradePlanStudioMonthlyPrice,
        pricePer = "/${if (isYearly) "year" else "month"}",
        detail = if (isYearly) "${Strings.upgradePlanStudioYearlyCredits} credits \u00B7 ${Strings.upgradePlanStudioYearlyPerCredit} per credit" else "${Strings.upgradePlanStudioCredits} credits \u00B7 ${Strings.upgradePlanStudioMonthlyPerCredit} per credit",
        badge = null,
        selected = selectedPlan == if (isYearly) "studio_yearly" else "studio_monthly",
        onClick = { onPlanSelected(if (isYearly) "studio_yearly" else "studio_monthly") },
    )

    Spacer(Modifier.height(6.dp))

    PaywallPlanCard(
        colors = colors,
        title = Strings.upgradePlanAgency,
        price = if (isYearly) Strings.upgradePlanAgencyYearlyPrice else Strings.upgradePlanAgencyMonthlyPrice,
        pricePer = "/${if (isYearly) "year" else "month"}",
        detail = if (isYearly) "${Strings.upgradePlanAgencyYearlyCredits} credits \u00B7 ${Strings.upgradePlanAgencyYearlyPerCredit} per credit" else "${Strings.upgradePlanAgencyCredits} credits \u00B7 ${Strings.upgradePlanAgencyMonthlyPerCredit} per credit",
        badge = null,
        selected = selectedPlan == if (isYearly) "agency_yearly" else "agency_monthly",
        onClick = { onPlanSelected(if (isYearly) "agency_yearly" else "agency_monthly") },
    )

    Spacer(Modifier.height(8.dp))

    // Trust line
    Text(
        Strings.pricingTrustCta,
        style = MaterialTheme.typography.bodySmall,
        color = colors.textMuted,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun PaywallPlanCard(
    colors: SheetPalette,
    title: String,
    price: String,
    pricePer: String,
    detail: String,
    badge: String? = null,
    savings: String? = null,
    isPopular: Boolean = false,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            isPopular && selected -> colors.accent
            isPopular -> colors.accent.copy(alpha = 0.6f)
            selected -> colors.accent
            else -> colors.border
        },
        animationSpec = tween(200),
        label = "plan_border",
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "planCardScale",
    )

    Box {
        // Popular badge floating above the card
        if (isPopular && badge != null) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = colors.gold,
                shadowElevation = 3.dp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = (-10).dp),
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
                        modifier = Modifier.size(10.dp),
                    )
                    Text(
                        badge,
                        color = HomeDecorExtra.onGradientText,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }

        // Non-popular inline badge (e.g. "Save 40%")
        if (!isPopular && badge != null) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = colors.gold,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = HomeDecorExtra.onGradientText,
                        modifier = Modifier.size(10.dp),
                    )
                    Text(
                        badge,
                        color = HomeDecorExtra.onGradientText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                        ),
                    )
                }
            }
        }

        Surface(
            shape = HomeDecorShape.Card,
            color = when {
                isPopular && selected -> colors.accentSurface
                isPopular -> colors.accentSurface.copy(alpha = 0.3f)
                selected -> colors.accentSurface.copy(alpha = 0.5f)
                isHovered -> colors.accentSurface.copy(alpha = 0.15f)
                else -> colors.cardSurface
            },
            border = BorderStroke(if (isPopular) 2.dp else 1.dp, borderColor),
            shadowElevation = if (isPopular) 8.dp else 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(Strings.formatTestTag(Strings.TestTags.paywallPlanCard, title.lowercase()))
                .semantics {
                    role = Role.RadioButton
                    contentDescription = Strings.a11yPaywallPlan(title, selected)
                }
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .scale(cardScale),
        ) {
            Column(Modifier.padding(14.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = colors.textPrimary,
                    )

                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (selected) colors.accent else Color.Transparent)
                            .then(
                                if (!selected) Modifier.border(2.dp, colors.textMuted.copy(alpha = 0.4f), CircleShape) else Modifier
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (selected) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = HomeDecorExtra.onGradientText,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        price,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp,
                        ),
                        color = if (selected && isPopular) colors.accent else colors.textPrimary,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        pricePer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textMuted,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }

                if (detail.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textMuted,
                    )
                }

                if (savings != null) {
                    Spacer(Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colors.checkGreen.copy(alpha = 0.12f),
                    ) {
                        Text(
                            savings,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            color = colors.checkGreen,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }
        }
    }
}

// ── Step 2: Checkout ──────────────────────────────────────────────────────

@Composable
private fun PaywallStep5Checkout(
    colors: SheetPalette,
    selectedPlan: String,
    purchasing: Boolean = false,
    purchaseSuccess: Boolean = false,
    errorMessage: String? = null,
) {
    // Compact trust badge
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (purchaseSuccess) colors.checkGreen.copy(alpha = 0.15f) else colors.accentSurface,
        border = BorderStroke(1.dp, (if (purchaseSuccess) colors.checkGreen else colors.accent).copy(alpha = 0.2f)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                if (purchaseSuccess) Icons.Rounded.Check else Icons.Rounded.Shield,
                contentDescription = null,
                tint = if (purchaseSuccess) colors.checkGreen else colors.accent,
                modifier = Modifier.size(14.dp),
            )
            Text(
                if (purchaseSuccess) "Payment confirmed" else Strings.pwS5Badge,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (purchaseSuccess) colors.checkGreen else colors.accent,
            )
        }
    }

    Spacer(Modifier.height(8.dp))

    Text(
        if (purchaseSuccess) "Welcome to Pro!" else Strings.pwS5Heading,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
        ),
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(10.dp))

    if (errorMessage != null) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFFF3F0),
            border = BorderStroke(1.dp, Color(0xFFE57373).copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("\u26A0", style = MaterialTheme.typography.titleMedium, color = Color(0xFFD32F2F))
                Text(
                    errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD32F2F),
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }

    // Processing indicator
    if (purchasing) {
        Surface(
            shape = HomeDecorShape.HeroCard,
            color = colors.accentSurface,
            border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.15f)),
            modifier = Modifier.fillMaxWidth().height(64.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colors.accent,
                        strokeWidth = 2.5.dp,
                    )
                    Text(
                        "Processing...",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                    )
                }
            }
        }
    } else {
        // Subscription summary — compact, scannable
        Surface(
            shape = HomeDecorShape.CardLarge,
            color = colors.cardSurface,
            border = BorderStroke(1.dp, colors.border),
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(14.dp)) {
                val planTitle = when (selectedPlan) {
                    "essential_yearly", "essential_monthly" -> Strings.upgradePlanEssential
                    "pro_yearly", "pro_monthly" -> Strings.upgradePlanPro
                    "studio_yearly", "studio_monthly" -> Strings.upgradePlanStudio
                    "agency_yearly", "agency_monthly" -> Strings.upgradePlanAgency
                    else -> Strings.upgradePlanPro
                }
                val planSubtitle = when (selectedPlan) {
                    "essential_yearly", "pro_yearly", "studio_yearly", "agency_yearly" -> "Annual subscription"
                    "essential_monthly", "pro_monthly", "studio_monthly", "agency_monthly" -> "Monthly subscription"
                    else -> "Annual subscription"
                }
                Text(
                    planTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.textPrimary,
                )
                Text(
                    planSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textMuted,
                )

                Spacer(Modifier.height(10.dp))
                HorizontalDivider(thickness = 0.5.dp, color = colors.border)
                Spacer(Modifier.height(10.dp))

                val summaryRows = listOf(
                    Strings.pwS5TrialPeriod to when (selectedPlan) {
                        "essential_yearly", "pro_yearly", "studio_yearly", "agency_yearly" -> "Annual billing"
                        "essential_monthly", "pro_monthly", "studio_monthly", "agency_monthly" -> "Monthly billing"
                        else -> "Annual billing"
                    },
                    Strings.pwS5Then to when (selectedPlan) {
                        "essential_yearly" -> "${Strings.upgradePlanEssentialYearlyPrice} / year"
                        "essential_monthly" -> "${Strings.upgradePlanEssentialMonthlyPrice} / month"
                        "pro_yearly" -> "${Strings.upgradePlanProYearlyPrice} / year"
                        "pro_monthly" -> "${Strings.upgradePlanProMonthlyPrice} / month"
                        "studio_yearly" -> "${Strings.upgradePlanStudioYearlyPrice} / year"
                        "studio_monthly" -> "${Strings.upgradePlanStudioMonthlyPrice} / month"
                        "agency_yearly" -> "${Strings.upgradePlanAgencyYearlyPrice} / year"
                        "agency_monthly" -> "${Strings.upgradePlanAgencyMonthlyPrice} / month"
                        else -> "${Strings.upgradePlanProYearlyPrice} / year"
                    },
                    Strings.pwS5RenewalDate to run {
                        val now = kotlinx.datetime.Clock.System.now()
                        val renewal = now.plus(7, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                        val dt = renewal.toLocalDateTime(TimeZone.currentSystemDefault())
                        "${dt.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${dt.dayOfMonth}, ${dt.year}"
                    },
                    Strings.pwS5Payment to Strings.pwS5PaymentValue,
                )

                summaryRows.forEach { (label, value) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            label,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary,
                        )
                        Text(
                            value,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = colors.textPrimary,
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(6.dp))

    Text(
        Strings.pwS5Legal,
        style = MaterialTheme.typography.bodySmall,
        color = colors.textMuted,
        textAlign = TextAlign.Center,
    )
}

// ── Shared Components ──────────────────────────────────────────────────────

@Composable
private fun PaywallBottomCta(
    colors: SheetPalette,
    label: String,
    onClick: () -> Unit,
    onRestore: () -> Unit,
    enabled: Boolean = true,
    processing: Boolean = false,
    success: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, colors.gradientMid.copy(alpha = 0.95f), colors.gradientMid),
                ),
            )
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp, bottom = 12.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PaywallCtaButton(
            colors = colors,
            label = label,
            onClick = onClick,
            enabled = enabled,
            processing = processing,
            success = success,
        )
        if (!success) {
            Text(
                Strings.pwS5Trust,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted,
                textAlign = TextAlign.Center,
            )
        }
        PaywallBottomLinks(onRestore = onRestore)
    }
}

@Composable
private fun PaywallCtaButton(
    colors: SheetPalette,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    processing: Boolean = false,
    success: Boolean = false,
) {
    val buttonEnabled = enabled && !processing
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && buttonEnabled) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cta_scale",
    )

    Button(
        onClick = { if (buttonEnabled) onClick() },
        shape = HomeDecorShape.ButtonLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                success -> colors.checkGreen
                !buttonEnabled -> colors.ctaDisabled
                else -> colors.accent
            },
            contentColor = HomeDecorExtra.onGradientText,
            disabledContainerColor = colors.ctaDisabled,
            disabledContentColor = colors.textMuted,
        ),
        contentPadding = PaddingValues(),
        enabled = buttonEnabled,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag(Strings.TestTags.paywallCtaButton)
            .semantics {
                role = Role.Button
                contentDescription = Strings.a11yPaywallCta
            }
            .scale(scale),
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (processing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = HomeDecorExtra.onGradientText,
                    strokeWidth = 2.dp,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            } else if (success) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(HomeDecorIconSize.Large),
                    tint = HomeDecorExtra.onGradientText,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            } else {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(HomeDecorIconSize.Medium),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun PaywallBottomLinks(onRestore: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        val restoreInteraction = remember { MutableInteractionSource() }
        val restoreHovered by restoreInteraction.collectIsHoveredAsState()
        Box(
            modifier = Modifier
                .clickable(interactionSource = restoreInteraction, indication = null) { onRestore() }
                .testTag(Strings.TestTags.paywallRestoreButton)
                .semantics {
                    role = Role.Button
                    contentDescription = Strings.pwS5Restore
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                Strings.pwS5Restore,
                color = if (restoreHovered) ProTextPrimary else ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        Text(
            "\u00B7",
            color = ProTextMuted.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Box(
            modifier = Modifier
                .clickable { showToast(Strings.toastComingSoon) }
                .semantics {
                    role = Role.Button
                    contentDescription = Strings.terms
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                Strings.terms,
                color = ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        Text(
            "\u00B7",
            color = ProTextMuted.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Box(
            modifier = Modifier
                .clickable { showToast(Strings.toastComingSoon) }
                .semantics {
                    role = Role.Button
                    contentDescription = Strings.privacyPolicy
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                Strings.privacyPolicy,
                color = ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
    }
}

// ── Theme-aware palette ───────────────────────────────────────────────────

@Composable
private fun rememberSheetPalette(): SheetPalette {
    val dark = LocalDarkTheme.current
    return remember(dark) {
        if (dark) SheetPalette.Dark else SheetPalette.Light
    }
}

private class SheetPalette(
    val gradientStart: Color,
    val gradientMid: Color,
    val gradientEnd: Color,
    val accent: Color,
    val accentLight: Color,
    val accentSurface: Color,
    val cardSurface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val gold: Color,
    val mint: Color,
    val checkGreen: Color,
    val border: Color,
    val ctaDisabled: Color,
    val surface: Color,
    val overlayBg: Color,
    val scrimAccent: Color,
) {
    companion object {
        val Light = SheetPalette(
            gradientStart = ProDarkStart,
            gradientMid = ProDarkMid,
            gradientEnd = ProDarkEnd,
            accent = ProCtaAccent,
            accentLight = ProAccentLight,
            accentSurface = ProAccentSurface,
            cardSurface = ProCardSurface,
            textPrimary = ProTextPrimary,
            textSecondary = ProTextSecondary,
            textMuted = ProTextMuted,
            gold = ProGold,
            mint = ProMint,
            checkGreen = ProCheckGreen,
            border = ProBorder,
            ctaDisabled = ProCtaDisabled,
            surface = ProSurface,
            overlayBg = Color.Black.copy(alpha = 0.6f),
            scrimAccent = ProCtaAccent.copy(alpha = 0.08f),
        )
        val Dark = SheetPalette(
            gradientStart = ProDarkGradientStart,
            gradientMid = ProDarkGradientMid,
            gradientEnd = ProDarkGradientEnd,
            accent = ProDarkAccent,
            accentLight = ProDarkAccentLight,
            accentSurface = ProDarkAccentSurface,
            cardSurface = ProDarkCardSurface,
            textPrimary = ProDarkTextPrimary,
            textSecondary = ProDarkTextSecondary,
            textMuted = ProDarkTextMuted,
            gold = ProDarkGold,
            mint = ProDarkMint,
            checkGreen = ProCheckGreen,
            border = ProDarkBorder,
            ctaDisabled = ProDarkCtaDisabled,
            surface = ProDarkSurface,
            overlayBg = Color.Black.copy(alpha = 0.75f),
            scrimAccent = ProDarkAccent.copy(alpha = 0.06f),
        )
    }
}
