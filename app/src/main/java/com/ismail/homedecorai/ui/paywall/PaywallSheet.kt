package com.ismail.homedecorai.ui.paywall

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AllInclusive
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.HighQuality
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.Role.Companion.RadioButton
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
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

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
    val checkGreen: Color,
    val celebration: Color,
    val border: Color,
    val borderSelected: Color,
    val ctaGradientStart: Color,
    val ctaGradientEnd: Color,
    val ctaDisabled: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val overlayBg: Color,
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
            goldSurface = Color(0xFFFFF8E1),
            checkGreen = ProCheckGreen,
            celebration = ProCelebration,
            border = ProBorder,
            borderSelected = ProBorderSelected,
            ctaGradientStart = ProCtaGradientStart,
            ctaGradientEnd = ProCtaGradientEnd,
            ctaDisabled = ProCtaDisabled,
            surface = ProSurface,
            surfaceAlt = ProSurfaceAlt,
            overlayBg = Color.Black.copy(alpha = 0.5f),
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
            goldSurface = Color(0xFF3D3100),
            checkGreen = Color(0xFF81D98A),
            celebration = ProDarkCelebration,
            border = ProDarkBorder,
            borderSelected = ProDarkBorderSelected,
            ctaGradientStart = ProDarkCtaGradientStart,
            ctaGradientEnd = ProDarkCtaGradientEnd,
            ctaDisabled = ProDarkCtaDisabled,
            surface = ProDarkSurface,
            surfaceAlt = ProDarkSurfaceAlt,
            overlayBg = Color.Black.copy(alpha = 0.7f),
        )
    }
}

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
    var currentStep by remember { mutableIntStateOf(0) }
    var selectedReminder by remember { mutableStateOf<String?>(null) }
    val benefitPagerState = rememberPagerState(pageCount = { 6 })
    val coroutineScope = rememberCoroutineScope()
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

    Box(
        Modifier
            .fillMaxSize()
            .background(proColors.overlayBg)
            .clickable(interactionSource = modalTapBlocker, indication = null, onClick = {}),
    ) {
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                (fadeIn(tween(300)) + slideInHorizontally(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    initialOffsetX = { it / 4 },
                )) togetherWith (fadeOut(tween(200)) + slideOutHorizontally(
                    animationSpec = tween(200, easing = FastOutSlowInEasing),
                    targetOffsetX = { -it / 4 },
                ))
            },
            label = "paywall_step",
        ) { step ->
            when (step) {
                0 -> ProIntroScreen(
                    colors = proColors,
                    onClose = onClose,
                    onContinue = { currentStep = 1 },
                )
                1 -> ProBenefitCarousel(
                    colors = proColors,
                    pagerState = benefitPagerState,
                    onClose = onClose,
                    onContinue = {
                        coroutineScope.launch {
                            benefitPagerState.animateScrollToPage(0)
                        }
                        currentStep = 2
                    },
                    onSkip = { currentStep = 3 },
                    onBack = { currentStep = 0 },
                )
                2 -> ProReminderScreen(
                    colors = proColors,
                    selectedReminder = selectedReminder,
                    onSelectReminder = { selectedReminder = it },
                    onClose = onClose,
                    onContinue = { currentStep = 3 },
                    onBack = { currentStep = 1 },
                )
                3 -> ProPlanScreen(
                    colors = proColors,
                    selectedPlan = selectedPlan,
                    weeklyPrice = weeklyPrice,
                    yearlyPackage = yearlyPackage,
                    weeklyPackage = weeklyPackage,
                    offeringsLoading = offeringsLoading,
                    onPlanSelected = { selectedPlan = it },
                    onClose = onClose,
                    onBack = { currentStep = 2 },
                    onContinue = {
                        if (!purchaseBusy) {
                            currentStep = 4
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
                4 -> ProCheckoutScreen(
                    colors = proColors,
                    processing = purchaseBusy,
                    success = purchaseSuccess,
                    selectedPlan = selectedPlan,
                    selectedPackage = selectedPackage,
                    onClose = onClose,
                )
            }
        }
    }
}

// ── Shared Components ──────────────────────────────────────────────────────────

@Composable
private fun ProBadge(colors: ProPaywallColors = rememberProPaywallColors()) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = colors.badgeBg,
    ) {
        Row(
            Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            Icon(
                Icons.Rounded.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = colors.badgeText,
            )
            Text(
                stringResource(R.string.pro_badge_label),
                color = colors.badgeText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                ),
            )
        }
    }
}

@Composable
private fun ProScreenHeader(
    colors: ProPaywallColors,
    onBack: (() -> Unit)?,
    onClose: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(top = HomeDecorSpacing.Sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.size(HomeDecorSpacing.TouchTarget)) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = colors.textSecondary,
                    modifier = Modifier.size(22.dp),
                )
            }
        } else {
            Spacer(Modifier.size(HomeDecorSpacing.TouchTarget))
        }
        ProBadge(colors)
        IconButton(onClick = onClose, modifier = Modifier.size(HomeDecorSpacing.TouchTarget)) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = stringResource(R.string.pro_a11y_close),
                tint = colors.textSecondary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun ProCtaButton(
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
private fun ProPageIndicator(colors: ProPaywallColors, pageCount: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 24.dp else 8.dp, 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) colors.accent else colors.textMuted.copy(alpha = 0.3f),
                    ),
            )
        }
    }
}

@Composable
private fun ProBottomLinks(onRestore: () -> Unit) {
    val restoreDescription = stringResource(R.string.a11y_restore_link)
    val termsDescription = stringResource(R.string.a11y_terms_link)
    val privacyDescription = stringResource(R.string.a11y_privacy_link)
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs, Alignment.CenterHorizontally),
    ) {
        Box(
            modifier = Modifier
                .minimumTouchTarget()
                .semantics { contentDescription = restoreDescription; role = Role.Button }
                .clickable { onRestore() }
                .padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.pro_restore),
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
                .semantics { contentDescription = termsDescription; role = Role.Button }
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
                .semantics { contentDescription = privacyDescription; role = Role.Button }
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

// ── Room Transformation Hero ──────────────────────────────────────────────────

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
    val arrowBounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "arrow_bounce",
    )

    Box(modifier.clip(RoundedCornerShape(24.dp))) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colors.accent.copy(alpha = 0.08f),
                            colors.gold.copy(alpha = 0.05f),
                            Color.Transparent,
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth(0.92f)
                    .height(180.dp),
            ) {
                val w = size.width
                val h = size.height
                val dividerX = w * 0.48f

                // ── "BEFORE" side (left) ──
                // Room outline
                drawRoundRect(
                    color = colors.textMuted.copy(alpha = 0.12f),
                    topLeft = Offset(w * 0.02f, h * 0.08f),
                    size = Size(w * 0.44f, h * 0.74f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
                // Wall texture
                for (i in 1..3) {
                    val x = w * (0.08f + i * 0.10f)
                    drawLine(
                        color = colors.textMuted.copy(alpha = 0.08f),
                        start = Offset(x, h * 0.12f),
                        end = Offset(x, h * 0.38f),
                        strokeWidth = 1.dp.toPx(),
                    )
                }
                // Plain sofa
                drawRoundRect(
                    color = colors.textMuted.copy(alpha = 0.18f),
                    topLeft = Offset(w * 0.06f, h * 0.48f),
                    size = Size(w * 0.34f, h * 0.20f),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                // Plain floor
                drawLine(
                    color = colors.textMuted.copy(alpha = 0.15f),
                    start = Offset(w * 0.04f, h * 0.68f),
                    end = Offset(w * 0.44f, h * 0.68f),
                    strokeWidth = 1.dp.toPx(),
                )
                // "BEFORE" label background
                drawRoundRect(
                    color = colors.textMuted.copy(alpha = 0.15f),
                    topLeft = Offset(w * 0.10f, h * 0.82f),
                    size = Size(w * 0.28f, h * 0.10f),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                )

                // ── Sparkle divider ──
                val sparkleX = dividerX + arrowBounce
                drawCircle(
                    color = colors.gold.copy(alpha = glowAlpha * 0.4f),
                    radius = 12.dp.toPx(),
                    center = Offset(sparkleX, h * 0.45f),
                )
                drawCircle(
                    color = colors.accent.copy(alpha = glowAlpha * 0.3f),
                    radius = 8.dp.toPx(),
                    center = Offset(sparkleX - 4.dp.toPx(), h * 0.38f),
                )
                drawCircle(
                    color = colors.gold.copy(alpha = glowAlpha * 0.25f),
                    radius = 6.dp.toPx(),
                    center = Offset(sparkleX + 6.dp.toPx(), h * 0.52f),
                )
                // Arrow
                drawLine(
                    color = colors.accent.copy(alpha = glowAlpha * 0.7f),
                    start = Offset(w * 0.43f, h * 0.45f),
                    end = Offset(w * 0.53f, h * 0.45f),
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.50f, h * 0.40f)
                        lineTo(w * 0.55f, h * 0.45f)
                        lineTo(w * 0.50f, h * 0.50f)
                    },
                    color = colors.accent.copy(alpha = glowAlpha * 0.7f),
                )

                // ── "AFTER" side (right) ──
                // Room outline
                drawRoundRect(
                    color = colors.accent.copy(alpha = 0.15f),
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
                    color = colors.accent.copy(alpha = 0.55f),
                    topLeft = Offset(w * 0.58f, h * 0.48f),
                    size = Size(w * 0.34f, h * 0.20f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
                // Sofa backrest
                drawRoundRect(
                    color = colors.accent.copy(alpha = 0.40f),
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
                    color = ProCheckGreen.copy(alpha = 0.5f),
                    start = Offset(w * 0.745f, h * 0.57f),
                    end = Offset(w * 0.745f, h * 0.52f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                // Leaf
                drawCircle(
                    color = ProCheckGreen.copy(alpha = 0.45f),
                    radius = w * 0.018f,
                    center = Offset(w * 0.755f, h * 0.51f),
                )
                // Floor with rug
                drawOval(
                    color = colors.accent.copy(alpha = 0.12f),
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
                // "AFTER" label background
                drawRoundRect(
                    color = colors.accent.copy(alpha = 0.18f),
                    topLeft = Offset(w * 0.64f, h * 0.82f),
                    size = Size(w * 0.24f, h * 0.10f),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                )
            }

            // "BEFORE" and "AFTER" text labels
            Text(
                "BEFORE",
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
                "AFTER",
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

// ── Benefit Illustrations ────────────────────────────────────────────────────

@Composable
private fun UnlimitedIllustration(accentColor: Color) {
    Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawRoundRect(
                color = accentColor.copy(alpha = 0.25f),
                topLeft = Offset(w * 0.1f, h * 0.15f),
                size = Size(w * 0.8f, h * 0.7f),
                cornerRadius = CornerRadius(12.dp.toPx()),
            )
            drawRoundRect(
                color = Color(0xFFA8A29E).copy(alpha = 0.20f),
                topLeft = Offset(w * 0.12f, h * 0.20f),
                size = Size(w * 0.35f, h * 0.55f),
                cornerRadius = CornerRadius(8.dp.toPx()),
            )
            drawRoundRect(
                color = Color(0xFFA8A29E).copy(alpha = 0.30f),
                topLeft = Offset(w * 0.15f, h * 0.50f),
                size = Size(w * 0.28f, h * 0.15f),
                cornerRadius = CornerRadius(6.dp.toPx()),
            )
            drawRoundRect(
                color = accentColor.copy(alpha = 0.35f),
                topLeft = Offset(w * 0.53f, h * 0.20f),
                size = Size(w * 0.35f, h * 0.55f),
                cornerRadius = CornerRadius(8.dp.toPx()),
            )
            drawRoundRect(
                color = accentColor.copy(alpha = 0.55f),
                topLeft = Offset(w * 0.56f, h * 0.50f),
                size = Size(w * 0.28f, h * 0.15f),
                cornerRadius = CornerRadius(6.dp.toPx()),
            )
            drawLine(
                color = accentColor.copy(alpha = 0.60f),
                start = Offset(w * 0.48f, h * 0.48f),
                end = Offset(w * 0.52f, h * 0.48f),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
private fun FourKIllustration(accentColor: Color) {
    Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawRoundRect(
                color = accentColor.copy(alpha = 0.30f),
                topLeft = Offset(w * 0.15f, h * 0.10f),
                size = Size(w * 0.70f, h * 0.65f),
                cornerRadius = CornerRadius(8.dp.toPx()),
                style = Stroke(width = 3.dp.toPx()),
            )
            drawRoundRect(
                color = accentColor.copy(alpha = 0.15f),
                topLeft = Offset(w * 0.20f, h * 0.15f),
                size = Size(w * 0.60f, h * 0.55f),
                cornerRadius = CornerRadius(4.dp.toPx()),
            )
            drawPath(
                path = Path().apply {
                    moveTo(w * 0.20f, h * 0.70f)
                    lineTo(w * 0.38f, h * 0.35f)
                    lineTo(w * 0.50f, h * 0.50f)
                    lineTo(w * 0.62f, h * 0.30f)
                    lineTo(w * 0.80f, h * 0.70f)
                    close()
                },
                color = accentColor.copy(alpha = 0.35f),
            )
            drawRoundRect(
                color = accentColor.copy(alpha = 0.60f),
                topLeft = Offset(w * 0.60f, h * 0.72f),
                size = Size(w * 0.25f, h * 0.16f),
                cornerRadius = CornerRadius(4.dp.toPx()),
            )
        }
        Text(
            "4K",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.offset(y = 38.dp),
        )
    }
}

@Composable
private fun NoWatermarkIllustration(accentColor: Color) {
    Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawRoundRect(
                color = accentColor.copy(alpha = 0.20f),
                topLeft = Offset(w * 0.10f, h * 0.10f),
                size = Size(w * 0.80f, h * 0.70f),
                cornerRadius = CornerRadius(10.dp.toPx()),
            )
            drawLine(
                color = Color(0xFFA8A29E).copy(alpha = 0.25f),
                start = Offset(w * 0.20f, h * 0.55f),
                end = Offset(w * 0.80f, h * 0.35f),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawLine(
                color = Color(0xFFA8A29E).copy(alpha = 0.25f),
                start = Offset(w * 0.20f, h * 0.35f),
                end = Offset(w * 0.80f, h * 0.55f),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawPath(
                path = Path().apply {
                    moveTo(w * 0.35f, h * 0.48f)
                    lineTo(w * 0.45f, h * 0.58f)
                    lineTo(w * 0.65f, h * 0.38f)
                },
                color = accentColor.copy(alpha = 0.70f),
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
            )
        }
    }
}

@Composable
private fun FastIllustration(accentColor: Color) {
    Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawPath(
                path = Path().apply {
                    moveTo(w * 0.55f, h * 0.08f)
                    lineTo(w * 0.30f, h * 0.48f)
                    lineTo(w * 0.48f, h * 0.48f)
                    lineTo(w * 0.42f, h * 0.92f)
                    lineTo(w * 0.72f, h * 0.42f)
                    lineTo(w * 0.54f, h * 0.42f)
                    close()
                },
                color = accentColor.copy(alpha = 0.65f),
            )
            for (i in 0..2) {
                val y = h * (0.25f + i * 0.20f)
                drawLine(
                    color = accentColor.copy(alpha = 0.25f - i * 0.05f),
                    start = Offset(w * 0.05f, y),
                    end = Offset(w * 0.22f, y),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

@Composable
private fun StylesIllustration(accentColor: Color) {
    Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val swatchColors = listOf(
                accentColor.copy(alpha = 0.50f),
                Color(0xFFFFC107).copy(alpha = 0.50f),
                Color(0xFF4CAF50).copy(alpha = 0.50f),
                Color(0xFFFF6D00).copy(alpha = 0.50f),
            )
            swatchColors.forEachIndexed { index, color ->
                val x = w * (0.15f + index * 0.18f)
                drawRoundRect(
                    color = color,
                    topLeft = Offset(x, h * 0.15f),
                    size = Size(w * 0.14f, h * 0.35f),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                )
            }
            for (i in 0..3) {
                val x = w * (0.15f + i * 0.18f)
                drawLine(
                    color = Color.White.copy(alpha = 0.15f),
                    start = Offset(x + w * 0.02f, h * 0.25f),
                    end = Offset(x + w * 0.12f, h * 0.25f),
                    strokeWidth = 1.dp.toPx(),
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.10f),
                    start = Offset(x + w * 0.02f, h * 0.35f),
                    end = Offset(x + w * 0.12f, h * 0.35f),
                    strokeWidth = 1.dp.toPx(),
                )
            }
        }
    }
}

@Composable
private fun HistoryIllustration(accentColor: Color) {
    Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawLine(
                color = accentColor.copy(alpha = 0.35f),
                start = Offset(w * 0.50f, h * 0.10f),
                end = Offset(w * 0.50f, h * 0.90f),
                strokeWidth = 2.dp.toPx(),
            )
            for (i in 0..2) {
                val y = h * (0.20f + i * 0.30f)
                drawCircle(
                    color = accentColor.copy(alpha = 0.50f),
                    radius = 5.dp.toPx(),
                    center = Offset(w * 0.50f, y),
                )
                drawRoundRect(
                    color = accentColor.copy(alpha = 0.20f + i * 0.10f),
                    topLeft = Offset(w * 0.58f, y - h * 0.08f),
                    size = Size(w * 0.30f, h * 0.18f),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                )
                drawRoundRect(
                    color = accentColor.copy(alpha = 0.30f + i * 0.10f),
                    topLeft = Offset(w * 0.62f, y + h * 0.02f),
                    size = Size(w * 0.10f, h * 0.05f),
                    cornerRadius = CornerRadius(2.dp.toPx()),
                )
            }
        }
    }
}

// ── Screen 0: ProIntroScreen ───────────────────────────────────────────────────

@Composable
private fun ProIntroScreen(
    colors: ProPaywallColors,
    onClose: () -> Unit,
    onContinue: () -> Unit,
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
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(colors = colors, onBack = null, onClose = onClose)

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            // Celebration text
            Text(
                stringResource(R.string.pro_intro_celebration),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                ),
                color = colors.gold,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            // Room transformation hero with sparkle particles
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            ) {
                RoomTransformationHero(colors = colors, Modifier.fillMaxSize())
                CelebrationParticles(colors = colors, Modifier.fillMaxSize())
            }

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            // Headline
            Text(
                stringResource(R.string.pro_intro_headline),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp,
                    letterSpacing = (-0.5).sp,
                ),
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            // Subtitle
            Text(
                stringResource(R.string.pro_intro_sub),
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            // PRO badge below subtitle
            ProBadge(colors)

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            // Benefit preview chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                listOf(
                    stringResource(R.string.pro_benefit_unlimited_title),
                    stringResource(R.string.pro_benefit_4k_title),
                    stringResource(R.string.pro_benefit_watermark_title),
                ).forEach { label ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = colors.accent.copy(alpha = 0.10f),
                        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.2f)),
                    ) {
                        Row(
                            Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                        ) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = colors.accentLight,
                                modifier = Modifier.size(12.dp),
                            )
                            Text(
                                label,
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textSecondary,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // CTA
            ProCtaButton(
                colors = colors,
                label = stringResource(R.string.pro_intro_cta),
                processing = false,
                success = false,
                onClick = onContinue,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            Text(
                stringResource(R.string.pro_google_play_checkout),
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        }
    }
}

// ── Screen 1: ProBenefitCarousel ───────────────────────────────────────────────

@Composable
private fun ProBenefitCarousel(
    colors: ProPaywallColors,
    pagerState: androidx.compose.foundation.pager.PagerState,
    onClose: () -> Unit,
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val benefits = listOf(
        ProBenefitItem(Icons.Rounded.AllInclusive, R.string.pro_benefit_unlimited_title, R.string.pro_benefit_unlimited_sub, colors.accent),
        ProBenefitItem(Icons.Rounded.HighQuality, R.string.pro_benefit_4k_title, R.string.pro_benefit_4k_sub, colors.gold),
        ProBenefitItem(Icons.Rounded.WaterDrop, R.string.pro_benefit_watermark_title, R.string.pro_benefit_watermark_sub, colors.checkGreen),
        ProBenefitItem(Icons.Rounded.Speed, R.string.pro_benefit_faster_title, R.string.pro_benefit_faster_sub, colors.celebration),
        ProBenefitItem(Icons.Rounded.Palette, R.string.pro_benefit_styles_title, R.string.pro_benefit_styles_sub, ProGoldDark),
        ProBenefitItem(Icons.Rounded.History, R.string.pro_benefit_history_title, R.string.pro_benefit_history_sub, colors.accentLight),
    )

    val isLastPage = pagerState.currentPage == 5

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
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(colors = colors, onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            // Step progress
            Text(
                stringResource(
                    R.string.pro_a11y_step_progress,
                    pagerState.currentPage + 1,
                    6,
                ),
                style = MaterialTheme.typography.labelMedium,
                color = colors.textMuted,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                val benefit = benefits[page]
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = HomeDecorSpacing.Base),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    // Benefit-specific illustration
                    Box(
                        modifier = Modifier.size(130.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        when (page) {
                            0 -> UnlimitedIllustration(benefit.accentColor)
                            1 -> FourKIllustration(benefit.accentColor)
                            2 -> NoWatermarkIllustration(benefit.accentColor)
                            3 -> FastIllustration(benefit.accentColor)
                            4 -> StylesIllustration(benefit.accentColor)
                            5 -> HistoryIllustration(benefit.accentColor)
                        }
                    }

                    Spacer(Modifier.height(HomeDecorSpacing.Xl))

                    Text(
                        stringResource(benefit.titleRes),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp,
                        ),
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(HomeDecorSpacing.Md))

                    Text(
                        stringResource(benefit.subRes),
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            ProPageIndicator(colors = colors, pageCount = 6, currentPage = pagerState.currentPage)

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            // Bottom buttons
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            ) {
                // Skip button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(HomeDecorSpacing.ButtonHeight)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, colors.textMuted.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .clickable { onSkip() },
                ) {
                    Text(
                        stringResource(R.string.pro_benefit_skip),
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.textSecondary,
                    )
                }

                // Next / Start Trial button
                ProCtaButton(
                    colors = colors,
                    label = if (isLastPage) stringResource(R.string.pro_cta_start_trial)
                    else stringResource(R.string.pro_benefit_next),
                    processing = false,
                    success = false,
                    onClick = {
                        if (isLastPage) onContinue()
                        else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                )
            }

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        }
    }
}

private data class ProBenefitItem(
    val icon: ImageVector,
    val titleRes: Int,
    val subRes: Int,
    val accentColor: Color,
)

// ── Screen 2: ProReminderScreen ────────────────────────────────────────────────

@Composable
private fun ProReminderScreen(
    colors: ProPaywallColors,
    selectedReminder: String?,
    onSelectReminder: (String) -> Unit,
    onClose: () -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit,
) {
    val canProceed = selectedReminder != null
    val reminder2Description = stringResource(R.string.pro_reminder_a11y_2days)
    val reminder3Description = stringResource(R.string.pro_reminder_a11y_3days)

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
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(colors = colors, onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            // Notification bell icon
            Surface(
                shape = CircleShape,
                color = colors.gold.copy(alpha = 0.15f),
                border = BorderStroke(1.5.dp, colors.gold.copy(alpha = 0.3f)),
                modifier = Modifier.size(88.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = colors.gold,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            Text(
                stringResource(R.string.pro_reminder_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp,
                ),
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            Text(
                stringResource(R.string.pro_reminder_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            ReminderOption(
                colors = colors,
                label = stringResource(R.string.pro_reminder_2days),
                selected = selectedReminder == "2days",
                onClick = { onSelectReminder("2days") },
                contentDescription = reminder2Description,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            ReminderOption(
                colors = colors,
                label = stringResource(R.string.pro_reminder_3days),
                selected = selectedReminder == "3days",
                onClick = { onSelectReminder("3days") },
                contentDescription = reminder3Description,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.checkGreen,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    stringResource(R.string.pro_reminder_reassurance),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary,
                )
            }

            Spacer(Modifier.weight(1f))

            ProCtaButton(
                colors = colors,
                label = stringResource(R.string.pro_cta_start_trial),
                processing = false,
                success = false,
                enabled = canProceed,
                onClick = onContinue,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        }
    }
}

@Composable
private fun ReminderOption(
    colors: ProPaywallColors,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.accent else colors.textMuted.copy(alpha = 0.2f),
        animationSpec = tween(200),
        label = "reminderBorder",
    )
    val cd = contentDescription
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) colors.accent.copy(alpha = 0.12f) else colors.cardSurface,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = borderColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                role = RadioButton
                this.selected = selected
                this.contentDescription = cd
            }
            .clickable(onClick = onClick),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (selected) colors.accent else Color.Transparent)
                    .then(
                        if (!selected) {
                            Modifier.border(2.dp, colors.textMuted.copy(alpha = 0.4f), CircleShape)
                        } else Modifier,
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
                label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = if (selected) colors.textPrimary else colors.textSecondary,
            )
        }
    }
}

// ── Screen 3: ProPlanScreen ────────────────────────────────────────────────────

@Composable
private fun ProPlanScreen(
    colors: ProPaywallColors,
    selectedPlan: String,
    weeklyPrice: String,
    yearlyPackage: Package?,
    weeklyPackage: Package?,
    offeringsLoading: Boolean,
    onPlanSelected: (String) -> Unit,
    onClose: () -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onRestore: () -> Unit,
) {
    val recommendedPlan = remember(yearlyPackage, weeklyPackage) {
        val yearly = yearlyPackage?.product?.price?.amountMicros?.let { it / 1_000_000.0 }
        val weekly = weeklyPackage?.product?.price?.amountMicros?.let { it / 1_000_000.0 }
        if (yearly != null && weekly != null) {
            val yearlyPerDay = yearly / 365.0
            val weeklyPerDay = weekly / 7.0
            if (yearlyPerDay < weeklyPerDay) "yearly" else "weekly"
        } else {
            "yearly"
        }
    }

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
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(colors = colors, onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            Text(
                stringResource(R.string.pro_plan_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            // Yearly plan card
            ProPlanCard(
                colors = colors,
                planLabel = stringResource(R.string.pro_plan_annual),
                price = yearlyPackage?.product?.price?.formatted,
                priceSuffix = stringResource(R.string.pro_plan_per_year),
                detail = stringResource(R.string.pro_plan_annual_detail),
                selected = selectedPlan == "yearly",
                isRecommended = recommendedPlan == "yearly",
                offeringsLoading = offeringsLoading,
                onClick = { onPlanSelected("yearly") },
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            // Weekly plan card
            ProPlanCard(
                colors = colors,
                planLabel = stringResource(R.string.pro_plan_weekly),
                price = weeklyPrice.ifEmpty { null },
                priceSuffix = stringResource(R.string.pro_plan_per_week),
                detail = stringResource(R.string.pro_plan_weekly_detail),
                selected = selectedPlan == "weekly",
                isRecommended = recommendedPlan == "weekly",
                offeringsLoading = offeringsLoading,
                onClick = { onPlanSelected("weekly") },
            )

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            // Trial note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.checkGreen,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    stringResource(R.string.pro_trial_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                )
            }

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            // CTA
            ProCtaButton(
                colors = colors,
                label = stringResource(R.string.pro_plan_trial_cta),
                processing = false,
                success = false,
                enabled = !offeringsLoading && (yearlyPackage != null || weeklyPackage != null),
                onClick = onContinue,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            // Cancel note
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = colors.checkGreen,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(
                    stringResource(R.string.pro_cancel_anytime),
                    color = colors.textMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            // Terms
            Text(
                stringResource(R.string.pro_plan_terms),
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            ProBottomLinks(onRestore = onRestore)

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        }
    }
}

@Composable
private fun ProPlanCard(
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
            else -> colors.textMuted.copy(alpha = 0.15f)
        },
        animationSpec = tween(200),
        label = "planBorder",
    )

    val bgBrush = when {
        isRecommended -> Brush.horizontalGradient(
            colors = listOf(colors.accent.copy(alpha = 0.08f), colors.gold.copy(alpha = 0.05f)),
        )
        else -> SolidColor(if (selected) colors.accentSurface else colors.cardSurface)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgBrush)
            .semantics {
                role = RadioButton
                this.selected = selected
            }
            .clickable(onClick = onClick),
    ) {
        Column(
            Modifier
                .padding(if (selected) 2.dp else 1.dp)
                .background(
                    color = if (selected) colors.accentSurface else colors.cardSurface,
                    shape = RoundedCornerShape(14.dp),
                )
                .padding(HomeDecorSpacing.Base),
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
                    Text(
                        planLabel,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = if (selected) colors.accentLight else colors.textSecondary,
                    )
                    if (isRecommended) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.gold.copy(alpha = 0.15f),
                        ) {
                            Text(
                                stringResource(R.string.pro_plan_best_value),
                                modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = 3.dp),
                                color = colors.gold,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            )
                        }
                    }
                }
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
            }

            Spacer(Modifier.height(HomeDecorSpacing.Md))

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

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            Text(
                detail,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted,
            )
        }
    }
}

// ── Screen 4: ProCheckoutScreen ────────────────────────────────────────────────

@Composable
private fun ProCheckoutScreen(
    colors: ProPaywallColors,
    processing: Boolean,
    success: Boolean,
    selectedPlan: String,
    selectedPackage: Package?,
    onClose: () -> Unit,
) {
    LaunchedEffect(success) {
        if (success) {
            delay(1800)
            onClose()
        }
    }

    val checkScale by animateFloatAsState(
        targetValue = if (success) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "check_scale",
    )

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
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (processing && !success) {
                CircularProgressIndicator(
                    modifier = Modifier.size(56.dp),
                    color = colors.accent,
                    strokeWidth = 4.dp,
                )
                Spacer(Modifier.height(HomeDecorSpacing.Lg))
                Text(
                    stringResource(R.string.pro_checkout_setting_up),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center,
                )
            } else if (success) {
                // Animated checkmark
                Surface(
                    shape = CircleShape,
                    color = colors.checkGreen.copy(alpha = 0.15f),
                    border = BorderStroke(2.dp, colors.checkGreen.copy(alpha = 0.3f)),
                    modifier = Modifier.size(100.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint = colors.checkGreen,
                            modifier = Modifier
                                .size(48.dp)
                                .scale(checkScale),
                        )
                    }
                }

                Spacer(Modifier.height(HomeDecorSpacing.Lg))

                Text(
                    stringResource(R.string.pro_cta_welcome),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(HomeDecorSpacing.Md))

                // Plan confirmation card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = colors.cardSurface,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        Modifier.padding(HomeDecorSpacing.Base),
                        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                    ) {
                        Text(
                            stringResource(
                                if (selectedPlan == "yearly") R.string.pro_plan_annual
                                else R.string.pro_plan_weekly
                            ),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = colors.textPrimary,
                        )
                        Text(
                            selectedPackage?.product?.price?.formatted ?: "",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                            color = colors.accentLight,
                        )
                        Text(
                            stringResource(
                                if (selectedPlan == "yearly") R.string.pro_plan_annual_detail
                                else R.string.pro_plan_weekly_detail
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textMuted,
                        )
                        HorizontalDivider(color = colors.textMuted.copy(alpha = 0.15f))
                        Text(
                            stringResource(R.string.pro_trial_note),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary,
                        )
                        Text(
                            stringResource(R.string.pro_cancel_anytime),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.checkGreen,
                        )
                    }
                }

                Spacer(Modifier.height(HomeDecorSpacing.Md))

                Text(
                    stringResource(R.string.pro_hero_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
