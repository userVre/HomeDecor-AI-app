package com.ismail.homedecorai.ui.paywall

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.pointer.pointerInput
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Offering
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PackageType
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.delay

// ── Theme-aware Pro palette ────────────────────────────────────────────────────

@Composable
private fun rememberProPaywallColors(): ProPaywallColors {
    val dark = isSystemInDarkTheme()
    return remember(dark) {
        if (dark) ProPaywallColors.Dark else ProPaywallColors.Light
    }
}

private class ProPaywallColors(
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
    val badgeBg: Color,
    val badgeText: Color,
    val gold: Color,
    val goldSurface: Color,
    val mint: Color,
    val checkGreen: Color,
    val border: Color,
    val borderSelected: Color,
    val ctaDisabled: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val overlayBg: Color,
    val heroDivider: Color,
) {
    companion object {
        val Light = ProPaywallColors(
            gradientStart = ProDarkStart,
            gradientMid = ProDarkMid,
            gradientEnd = ProDarkEnd,
            accent = ProAccent,
            accentLight = ProAccentLight,
            accentSurface = ProAccentSurface,
            cardSurface = ProCardSurface,
            textPrimary = ProTextPrimary,
            textSecondary = ProTextSecondary,
            textMuted = ProTextMuted,
            badgeBg = ProBadgeBg,
            badgeText = ProBadgeText,
            gold = ProGold,
            goldSurface = HomeDecorColors.GoldSurface,
            mint = ProMint,
            checkGreen = ProCheckGreen,
            border = ProBorder,
            borderSelected = ProBorderSelected,
            ctaDisabled = ProCtaDisabled,
            surface = ProSurface,
            surfaceAlt = ProSurfaceAlt,
            overlayBg = HomeDecorColors.Scrim.copy(alpha = 0.5f),
            heroDivider = ProHeroDivider,
        )
        val Dark = ProPaywallColors(
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
            badgeBg = ProDarkBadgeBg,
            badgeText = ProDarkBadgeText,
            gold = ProDarkGold,
            goldSurface = HomeDecorColors.DarkGoldSurface,
            mint = ProDarkMint,
            checkGreen = HomeDecorColors.DarkCheckGreen,
            border = ProDarkBorder,
            borderSelected = ProDarkBorderSelected,
            ctaDisabled = ProDarkCtaDisabled,
            surface = ProDarkSurface,
            surfaceAlt = ProDarkSurfaceAlt,
            overlayBg = HomeDecorColors.Scrim.copy(alpha = 0.7f),
            heroDivider = ProDarkHeroDivider,
        )
    }
}

// ── Main PaywallSheet ─────────────────────────────────────────────────────────

@Composable
fun PaywallSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onSubscription: (String, String, String, Double?, Double?) -> Unit,
    onRetrySync: () -> Unit,
    onStore: () -> Unit,
) {
    val context = LocalContext.current
    val modalTapBlocker = remember { MutableInteractionSource() }
    var offering by remember { mutableStateOf<Offering?>(null) }
    var offeringsLoading by remember { mutableStateOf(true) }
    var purchasing by remember { mutableStateOf(false) }
    var restoring by remember { mutableStateOf(false) }
    var selectedPlan by remember { mutableStateOf("yearly") }
    var purchaseSuccess by remember { mutableStateOf(false) }
    val proColors = rememberProPaywallColors()

    fun loadOfferings() {
        offeringsLoading = true
        offering = null
        if (!Purchases.isConfigured) {
            offeringsLoading = false
            return
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                offering = offerings.current ?: offerings.all.values.firstOrNull()
                offeringsLoading = false
            }
            override fun onError(error: PurchasesError) {
                offeringsLoading = false
            }
        })
    }

    LaunchedEffect(Unit) { loadOfferings() }

    fun buy(packageToPurchase: Package?, subscriptionType: String, entitlement: String) {
        val activity = context.findActivity()
        if (packageToPurchase == null || activity == null || !Purchases.isConfigured) return
        purchasing = true
        Purchases.sharedInstance.purchase(
            PurchaseParams.Builder(activity, packageToPurchase).build(),
            object : PurchaseCallback {
                override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                    purchasing = false
                    purchaseSuccess = true
                    val active = customerInfo.entitlements.active.values.firstOrNull()
                    onSubscription(
                        "pro",
                        subscriptionType,
                        active?.identifier ?: entitlement,
                        active?.latestPurchaseDate?.time?.toDouble(),
                        active?.expirationDate?.time?.toDouble(),
                    )
                }
                override fun onError(error: PurchasesError, userCancelled: Boolean) {
                    purchasing = false
                }
            },
        )
    }

    val yearlyPackage = offering?.annual
        ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.ANNUAL }
    val weeklyPackage = offering?.weekly
        ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.WEEKLY }
    val purchaseBusy = purchasing || restoring || state.purchaseBusy

    LaunchedEffect(offeringsLoading, yearlyPackage, weeklyPackage) {
        if (!offeringsLoading) {
            selectedPlan = when {
                selectedPlan == "yearly" && yearlyPackage != null -> "yearly"
                selectedPlan == "weekly" && weeklyPackage != null -> "weekly"
                yearlyPackage != null -> "yearly"
                weeklyPackage != null -> "weekly"
                else -> selectedPlan
            }
        }
    }

    val selectedPackage = when (selectedPlan) {
        "yearly" -> yearlyPackage
        "weekly" -> weeklyPackage
        else -> yearlyPackage
    }

    val weeklyPrice = weeklyPackage?.product?.price?.formatted ?: ""

    // Handle purchase success — auto-close after delay
    LaunchedEffect(purchaseSuccess) {
        if (purchaseSuccess) {
            delay(1800)
            onClose()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(proColors.overlayBg)
            .clickable(interactionSource = modalTapBlocker, indication = null, onClick = {}),
    ) {
        // Single-screen paywall — no multi-step flow
        PaywallSingleScreen(
            colors = proColors,
            selectedPlan = selectedPlan,
            weeklyPrice = weeklyPrice,
            yearlyPackage = yearlyPackage,
            weeklyPackage = weeklyPackage,
            offeringsLoading = offeringsLoading,
            purchasing = purchasing,
            purchaseSuccess = purchaseSuccess,
            onPlanSelected = { selectedPlan = it },
            onClose = onClose,
            onContinue = {
                if (!purchaseBusy) {
                    val entitlement = when (selectedPlan) {
                        "yearly" -> "annual_pro"
                        "weekly" -> "weekly_pro"
                        else -> "annual_pro"
                    }
                    buy(selectedPackage, selectedPlan, entitlement)
                }
            },
            onRestore = { onRetrySync() },
        )
    }
}

// ── Single-Screen Paywall ─────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PaywallSingleScreen(
    colors: ProPaywallColors,
    selectedPlan: String,
    weeklyPrice: String,
    yearlyPackage: Package?,
    weeklyPackage: Package?,
    offeringsLoading: Boolean,
    purchasing: Boolean,
    purchaseSuccess: Boolean,
    onPlanSelected: (String) -> Unit,
    onClose: () -> Unit,
    onContinue: () -> Unit,
    onRestore: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(colors.gradientStart, colors.gradientMid, colors.gradientEnd),
                ),
            )
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Close button
            Row(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.pro_a11y_close),
                        tint = colors.textSecondary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }

            // Headline — immediate value proposition
            Text(
                stringResource(R.string.paywall_v3_headline),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp,
                    letterSpacing = (-0.5).sp,
                ),
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(8.dp))

            // Subtitle
            Text(
                stringResource(R.string.paywall_v3_subtitle),
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(12.dp))

            // Trial info — shown early, not buried
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
                        stringResource(R.string.paywall_v3_trial_note),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = colors.accent,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Before/After hero — strong visual
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                RoomTransformationHero(colors = colors, Modifier.fillMaxSize())
                CelebrationParticles(colors = colors, Modifier.fillMaxSize())
            }

            Spacer(Modifier.height(16.dp))

            // Benefit chips — spacious, readable
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                listOf(
                    stringResource(R.string.paywall_v3_benefit_generations),
                    stringResource(R.string.paywall_v3_benefit_export),
                    stringResource(R.string.paywall_v3_benefit_watermark),
                    stringResource(R.string.paywall_v3_benefit_speed),
                    stringResource(R.string.paywall_v3_benefit_styles),
                    stringResource(R.string.paywall_v3_benefit_history),
                ).forEach { label ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = colors.accent.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.15f)),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(12.dp),
                            )
                            Text(
                                label,
                                style = MaterialTheme.typography.labelMedium,
                                color = colors.textSecondary,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Plan selector — visible early, clean
            PaywallPlanCard(
                colors = colors,
                planLabel = stringResource(R.string.paywall_v3_plan_yearly),
                price = yearlyPackage?.product?.price?.formatted,
                priceSuffix = stringResource(R.string.paywall_v3_plan_per_year),
                detail = stringResource(R.string.paywall_v3_plan_annual_detail),
                selected = selectedPlan == "yearly",
                isRecommended = true,
                offeringsLoading = offeringsLoading,
                onClick = { onPlanSelected("yearly") },
            )

            Spacer(Modifier.height(10.dp))

            PaywallPlanCard(
                colors = colors,
                planLabel = stringResource(R.string.paywall_v3_plan_weekly),
                price = weeklyPrice.ifEmpty { null },
                priceSuffix = stringResource(R.string.paywall_v3_plan_per_week),
                detail = stringResource(R.string.paywall_v3_plan_weekly_detail),
                selected = selectedPlan == "weekly",
                isRecommended = false,
                offeringsLoading = offeringsLoading,
                onClick = { onPlanSelected("weekly") },
            )

            Spacer(Modifier.height(16.dp))

            // Trial note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.checkGreen,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    stringResource(R.string.paywall_v3_trial_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                )
            }

            Spacer(Modifier.height(100.dp)) // space for sticky CTA
        }

        // ── Sticky CTA bar ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, colors.gradientMid.copy(alpha = 0.95f), colors.gradientMid),
                    ),
                )
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PaywallCtaButton(
                colors = colors,
                label = stringResource(R.string.paywall_v3_cta),
                processing = purchasing,
                success = purchaseSuccess,
                enabled = !offeringsLoading && (yearlyPackage != null || weeklyPackage != null),
                onClick = onContinue,
            )
            Text(
                stringResource(R.string.paywall_v3_trust),
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted,
                textAlign = TextAlign.Center,
            )
            PaywallBottomLinks(onRestore = onRestore)
        }
    }
}

// ── Shared Components ──────────────────────────────────────────────────────────

@Composable
private fun PaywallCtaButton(
    colors: ProPaywallColors,
    label: String,
    processing: Boolean,
    success: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
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
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                success -> colors.checkGreen
                !buttonEnabled -> colors.ctaDisabled
                else -> colors.accent
            },
            contentColor = Color.White,
            disabledContainerColor = colors.ctaDisabled,
            disabledContentColor = colors.textSecondary.copy(alpha = 0.6f),
        ),
        contentPadding = PaddingValues(),
        enabled = buttonEnabled,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(HomeDecorSpacing.ButtonHeight)
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
                    color = Color.White,
                    strokeWidth = 2.dp,
                )
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(label, style = MaterialTheme.typography.titleSmall)
            } else if (success) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White,
                )
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(label, style = MaterialTheme.typography.titleSmall)
            } else {
                Text(label, style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Composable
private fun PaywallBottomLinks(onRestore: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs, Alignment.CenterHorizontally),
    ) {
        Box(
            modifier = Modifier
                .minimumTouchTarget()
                .clickable { onRestore() }
                .padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.paywall_v3_restore),
                color = ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        Text(
            "\u00B7",
            color = ProTextMuted.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = HomeDecorSpacing.Md),
        )
        Box(
            modifier = Modifier
                .minimumTouchTarget()
                .clickable { }
                .padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.terms),
                color = ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        Text(
            "\u00B7",
            color = ProTextMuted.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = HomeDecorSpacing.Md),
        )
        Box(
            modifier = Modifier
                .minimumTouchTarget()
                .clickable { }
                .padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.privacy_policy),
                color = ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
    }
}

// ── Plan Card ─────────────────────────────────────────────────────────────────

@Composable
private fun PaywallPlanCard(
    colors: ProPaywallColors,
    planLabel: String,
    price: String?,
    priceSuffix: String,
    detail: String,
    selected: Boolean,
    isRecommended: Boolean,
    offeringsLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            isRecommended && selected -> colors.gold
            selected -> colors.accent
            else -> colors.border
        },
        animationSpec = tween(200),
        label = "planBorder",
    )

    val bgBrush = when {
        isRecommended -> Brush.horizontalGradient(
            colors = listOf(colors.accent.copy(alpha = 0.06f), colors.mint.copy(alpha = 0.04f)),
        )
        else -> Brush.linearGradient(
            colors = listOf(
                if (selected) colors.accentSurface else colors.cardSurface,
                if (selected) colors.accentSurface else colors.cardSurface,
            ),
        )
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgBrush)
            .then(
                Modifier.border(
                    width = if (selected) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp),
                )
            )
            .semantics {
                role = Role.RadioButton
                this.selected = selected
            }
            .clickable(onClick = onClick),
    ) {
        Column(
            Modifier.padding(HomeDecorSpacing.Base),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Radio indicator
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (selected) colors.accent else Color.Transparent)
                            .then(
                                if (!selected) Modifier.border(2.dp, colors.textMuted.copy(alpha = 0.4f), CircleShape) else Modifier,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (selected) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    }

                    Text(
                        planLabel,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = if (selected) colors.accentLight else colors.textSecondary,
                    )
                }

                if (isRecommended) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors.gold.copy(alpha = 0.12f),
                    ) {
                        Text(
                            stringResource(R.string.paywall_v3_plan_best_value),
                            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = 3.dp),
                            color = colors.gold,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                if (price == null && offeringsLoading) {
                    Box(
                        Modifier
                            .width(80.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.textMuted.copy(alpha = 0.1f)),
                    )
                } else {
                    Text(
                        price ?: "\u2014",
                        style = MaterialTheme.typography.displaySmall.copy(letterSpacing = (-1).sp),
                        color = colors.textPrimary,
                    )
                }
                Spacer(Modifier.width(HomeDecorSpacing.Xs))
                Text(
                    priceSuffix,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textMuted,
                    modifier = Modifier.padding(bottom = HomeDecorSpacing.Xs),
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                detail,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted,
            )
        }
    }
}

// ── Room Transformation Hero — Interactive Before/After Slider ───────────────

@Composable
private fun RoomTransformationHero(
    colors: ProPaywallColors,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "room_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow_alpha",
    )

    // Slider position: 0f = fully before, 1f = fully after; start at 0.48f
    val sliderProgress = remember { androidx.compose.animation.core.Animatable(0.48f) }
    var isDragging by remember { mutableStateOf(false) }

    // Animate in on first display
    LaunchedEffect(Unit) {
        sliderProgress.animateTo(
            targetValue = 0.48f,
            animationSpec = tween(durationMillis = 800, easing = EaseInOut),
        )
    }

    Box(
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.accent.copy(alpha = 0.06f),
                        colors.gold.copy(alpha = 0.03f),
                        Color.Transparent,
                    ),
                ),
            ),
    ) {
        Canvas(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                        onHorizontalDrag = { _, dragAmount ->
                            kotlinx.coroutines.runBlocking {
                                val newValue = sliderProgress.value + dragAmount / size.width
                                sliderProgress.snapTo(newValue.coerceIn(0.1f, 0.9f))
                            }
                        },
                    )
                },
        ) {
            val w = size.width
            val h = size.height
            val dividerX = w * sliderProgress.value

            // ── BEFORE scene (left side, clipped to left of divider) ──
            drawContext.canvas.saveLayer(
                androidx.compose.ui.geometry.Rect(0f, 0f, dividerX, h),
                androidx.compose.ui.graphics.Paint(),
            )
            // Room outline
            drawRoundRect(
                color = colors.textMuted.copy(alpha = 0.12f),
                topLeft = Offset(w * 0.02f, h * 0.08f),
                size = Size(w * 0.44f, h * 0.74f),
                cornerRadius = CornerRadius(10.dp.toPx()),
            )
            // Plain sofa
            drawRoundRect(
                color = colors.textMuted.copy(alpha = 0.18f),
                topLeft = Offset(w * 0.06f, h * 0.48f),
                size = Size(w * 0.34f, h * 0.20f),
                cornerRadius = CornerRadius(8.dp.toPx()),
            )
            // Floor
            drawLine(
                color = colors.textMuted.copy(alpha = 0.15f),
                start = Offset(w * 0.04f, h * 0.68f),
                end = Offset(w * 0.44f, h * 0.68f),
                strokeWidth = 1.dp.toPx(),
            )
            drawContext.canvas.restore()

            // ── AFTER scene (right side, clipped to right of divider) ──
            drawContext.canvas.saveLayer(
                androidx.compose.ui.geometry.Rect(dividerX, 0f, w, h),
                androidx.compose.ui.graphics.Paint(),
            )
            // Room outline
            drawRoundRect(
                color = colors.accent.copy(alpha = 0.12f),
                topLeft = Offset(w * 0.54f, h * 0.08f),
                size = Size(w * 0.44f, h * 0.74f),
                cornerRadius = CornerRadius(10.dp.toPx()),
            )
            // Window with warm glow
            drawRoundRect(
                color = colors.gold.copy(alpha = glowAlpha * 0.2f),
                topLeft = Offset(w * 0.62f, h * 0.12f),
                size = Size(w * 0.26f, h * 0.28f),
                cornerRadius = CornerRadius(4.dp.toPx()),
            )
            // Window cross-bars
            drawLine(
                color = colors.accent.copy(alpha = 0.25f),
                start = Offset(w * 0.75f, h * 0.12f),
                end = Offset(w * 0.75f, h * 0.40f),
                strokeWidth = 1.dp.toPx(),
            )
            drawLine(
                color = colors.accent.copy(alpha = 0.25f),
                start = Offset(w * 0.62f, h * 0.26f),
                end = Offset(w * 0.88f, h * 0.26f),
                strokeWidth = 1.dp.toPx(),
            )
            // Styled sofa
            drawRoundRect(
                color = colors.accent.copy(alpha = 0.50f),
                topLeft = Offset(w * 0.58f, h * 0.48f),
                size = Size(w * 0.34f, h * 0.20f),
                cornerRadius = CornerRadius(10.dp.toPx()),
            )
            // Sofa backrest
            drawRoundRect(
                color = colors.accent.copy(alpha = 0.35f),
                topLeft = Offset(w * 0.60f, h * 0.40f),
                size = Size(w * 0.30f, h * 0.10f),
                cornerRadius = CornerRadius(8.dp.toPx()),
            )
            // Throw pillow
            drawRoundRect(
                color = colors.gold.copy(alpha = 0.45f),
                topLeft = Offset(w * 0.60f, h * 0.42f),
                size = Size(w * 0.08f, h * 0.08f),
                cornerRadius = CornerRadius(5.dp.toPx()),
            )
            // Coffee table
            drawRoundRect(
                color = colors.gold.copy(alpha = 0.35f),
                topLeft = Offset(w * 0.66f, h * 0.64f),
                size = Size(w * 0.18f, h * 0.04f),
                cornerRadius = CornerRadius(3.dp.toPx()),
            )
            // Vase on table
            drawRoundRect(
                color = colors.gold.copy(alpha = 0.45f),
                topLeft = Offset(w * 0.72f, h * 0.57f),
                size = Size(w * 0.05f, h * 0.07f),
                cornerRadius = CornerRadius(2.dp.toPx()),
            )
            // Plant stem
            drawLine(
                color = colors.checkGreen.copy(alpha = 0.5f),
                start = Offset(w * 0.745f, h * 0.57f),
                end = Offset(w * 0.745f, h * 0.52f),
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round,
            )
            // Leaf
            drawCircle(
                color = colors.checkGreen.copy(alpha = 0.45f),
                radius = w * 0.018f,
                center = Offset(w * 0.755f, h * 0.51f),
            )
            // Floor with rug
            drawOval(
                color = colors.accent.copy(alpha = 0.10f),
                topLeft = Offset(w * 0.58f, h * 0.62f),
                size = Size(w * 0.34f, h * 0.10f),
            )
            drawLine(
                color = colors.accent.copy(alpha = 0.25f),
                start = Offset(w * 0.56f, h * 0.68f),
                end = Offset(w * 0.96f, h * 0.68f),
                strokeWidth = 1.dp.toPx(),
            )
            // Floor lamp
            drawLine(
                color = colors.gold.copy(alpha = 0.45f),
                start = Offset(w * 0.90f, h * 0.68f),
                end = Offset(w * 0.90f, h * 0.30f),
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawCircle(
                color = colors.gold.copy(alpha = glowAlpha * 0.15f),
                radius = w * 0.05f,
                center = Offset(w * 0.90f, h * 0.28f),
            )
            drawContext.canvas.restore()

            // ── Divider line ──
            drawLine(
                color = colors.heroDivider.copy(alpha = if (isDragging) 0.9f else 0.6f),
                start = Offset(dividerX, h * 0.05f),
                end = Offset(dividerX, h * 0.88f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round,
            )

            // ── Sparkle particles along divider ──
            drawCircle(
                color = colors.gold.copy(alpha = glowAlpha * 0.5f),
                radius = 8.dp.toPx(),
                center = Offset(dividerX, h * 0.20f),
            )
            drawCircle(
                color = colors.mint.copy(alpha = glowAlpha * 0.35f),
                radius = 5.dp.toPx(),
                center = Offset(dividerX - 3.dp.toPx(), h * 0.35f),
            )
            drawCircle(
                color = colors.gold.copy(alpha = glowAlpha * 0.25f),
                radius = 4.dp.toPx(),
                center = Offset(dividerX + 2.dp.toPx(), h * 0.55f),
            )
        }

        // ── Draggable handle ──
        val handleX by animateFloatAsState(
            targetValue = sliderProgress.value,
            animationSpec = if (isDragging) tween(0) else spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
            label = "handle_x",
        )

        Box(
            modifier = Modifier
                .offset(x = 0.dp) // placeholder, positioned via Canvas alignment
                .align(Alignment.CenterStart)
                .fillMaxSize(),
        ) {
            // Handle circle
            Surface(
                shape = CircleShape,
                color = colors.heroDivider,
                shadowElevation = if (isDragging) 6.dp else 3.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (handleX - 0.5f) * 320.dp) // approximate centering
                    .size(36.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }

        // ── Labels ──
        Text(
            stringResource(R.string.paywall_v3_before).uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
            ),
            color = colors.textMuted.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 12.dp),
        )
        Text(
            stringResource(R.string.paywall_v3_after).uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
            ),
            color = colors.accent.copy(alpha = 0.8f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 12.dp),
        )
    }
}

            // "BEFORE" and "AFTER" text labels
            Text(
                stringResource(R.string.paywall_v3_before).uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                ),
                color = colors.textMuted.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 28.dp, bottom = 22.dp),
            )
            Text(
                stringResource(R.string.paywall_v3_after).uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                ),
                color = colors.accent.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 28.dp, bottom = 22.dp),
            )
        }
    }
}

// ── Sparkle Particles ──────────────────────────────────────────────────────────

@Composable
private fun CelebrationParticles(colors: ProPaywallColors, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkles")
    val particles = remember {
        List(5) { index ->
            Triple(
                0.15f + (index * 0.175f),
                0.3f + (index % 3) * 0.15f,
                0.8f + (index % 2) * 0.4f,
            )
        }
    }

    Box(modifier.fillMaxSize()) {
        particles.forEachIndexed { index, (startX, startY, sizeMul) ->
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1800 + index * 400,
                        easing = EaseInOut,
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "sparkle_alpha_$index",
            )
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -20f - index * 8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 2200 + index * 300,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "sparkle_y_$index",
            )

            Icon(
                Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = colors.gold.copy(alpha = alpha * 0.7f),
                modifier = Modifier
                    .offset(
                        x = (sizeMul * 2).dp,
                        y = offsetY.dp,
                    )
                    .align(Alignment.TopStart)
                    .padding(
                        start = (startX * 320).dp,
                        top = (startY * 200).dp,
                    )
                    .size((6 + index * 2).dp * sizeMul),
            )
        }
    }
}
