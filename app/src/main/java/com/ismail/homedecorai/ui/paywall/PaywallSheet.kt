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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
            .background(Color.Black.copy(alpha = 0.7f))
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
                    onClose = onClose,
                    onContinue = { currentStep = 1 },
                )
                1 -> ProBenefitCarousel(
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
                    selectedReminder = selectedReminder,
                    onSelectReminder = { selectedReminder = it },
                    onClose = onClose,
                    onContinue = { currentStep = 3 },
                    onBack = { currentStep = 1 },
                )
                3 -> ProPlanScreen(
                    selectedPlan = selectedPlan,
                    weeklyPrice = weeklyPrice,
                    yearlyPackage = yearlyPackage,
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
                    processing = purchaseBusy,
                    success = purchaseSuccess,
                    onClose = onClose,
                )
            }
        }
    }
}

// ── Shared Components ──────────────────────────────────────────────────────────

@Composable
private fun ProBadge() {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ProBadgeBg,
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
                tint = ProBadgeText,
            )
            Text(
                stringResource(R.string.pro_badge_label),
                color = ProBadgeText,
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
                    tint = ProTextSecondary,
                    modifier = Modifier.size(22.dp),
                )
            }
        } else {
            Spacer(Modifier.size(HomeDecorSpacing.TouchTarget))
        }
        ProBadge()
        IconButton(onClick = onClose, modifier = Modifier.size(HomeDecorSpacing.TouchTarget)) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = stringResource(R.string.pro_a11y_close),
                tint = ProTextSecondary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun ProCtaButton(
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
                success -> ProCheckGreen
                !buttonEnabled -> ProCtaDisabled
                else -> ProAccent
            },
            contentColor = Color.White,
            disabledContainerColor = ProCtaDisabled,
            disabledContentColor = ProTextSecondary.copy(alpha = 0.6f),
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
private fun ProPageIndicator(pageCount: Int, currentPage: Int) {
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
                        if (index == currentPage) ProAccent else ProTextMuted.copy(alpha = 0.3f),
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

// ── Living Room Hero Illustration ──────────────────────────────────────────────

@Composable
private fun ProHeroIllustration(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow_alpha",
    )

    Box(modifier.clip(RoundedCornerShape(24.dp))) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ProAccent.copy(alpha = 0.2f),
                            ProGradientEnd.copy(alpha = 0.1f),
                            Color.Transparent,
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            // Detailed interior room scene — cozy living room
            androidx.compose.foundation.Canvas(
                Modifier
                    .fillMaxWidth(0.92f)
                    .height(180.dp),
            ) {
                val w = size.width
                val h = size.height

                // ── Back wall ──
                drawRoundRect(
                    color = ProAccent.copy(alpha = 0.07f),
                    topLeft = Offset.Zero,
                    size = Size(w, h * 0.78f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )

                // Wall wood-panel texture (subtle vertical lines)
                for (i in 1..6) {
                    val x = w * (0.12f + i * 0.12f)
                    drawLine(
                        color = ProTextMuted.copy(alpha = 0.06f),
                        start = Offset(x, h * 0.04f),
                        end = Offset(x, h * 0.34f),
                        strokeWidth = 1.dp.toPx(),
                    )
                }

                // ── Window with warm glow ──
                val winX = w * 0.30f
                val winY = h * 0.06f
                val winW = w * 0.38f
                val winH = h * 0.44f

                // Glow behind window
                drawRoundRect(
                    color = ProGold.copy(alpha = glowAlpha * 0.12f),
                    topLeft = Offset(winX - 10.dp.toPx(), winY - 6.dp.toPx()),
                    size = Size(winW + 20.dp.toPx(), winH + 12.dp.toPx()),
                    cornerRadius = CornerRadius(14.dp.toPx()),
                )
                // Window glass
                drawRoundRect(
                    color = ProGold.copy(alpha = glowAlpha * 0.10f),
                    topLeft = Offset(winX, winY),
                    size = Size(winW, winH),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                )
                // Window cross-bars
                drawLine(
                    color = ProTextMuted.copy(alpha = 0.18f),
                    start = Offset(winX + winW / 2, winY),
                    end = Offset(winX + winW / 2, winY + winH),
                    strokeWidth = 1.5.dp.toPx(),
                )
                drawLine(
                    color = ProTextMuted.copy(alpha = 0.18f),
                    start = Offset(winX, winY + winH * 0.45f),
                    end = Offset(winX + winW, winY + winH * 0.45f),
                    strokeWidth = 1.5.dp.toPx(),
                )
                // Left curtain
                drawRoundRect(
                    color = ProTextMuted.copy(alpha = 0.12f),
                    topLeft = Offset(winX - 14.dp.toPx(), winY - 6.dp.toPx()),
                    size = Size(12.dp.toPx(), winH + 20.dp.toPx()),
                    cornerRadius = CornerRadius(5.dp.toPx()),
                )
                // Right curtain
                drawRoundRect(
                    color = ProTextMuted.copy(alpha = 0.12f),
                    topLeft = Offset(winX + winW + 2.dp.toPx(), winY - 6.dp.toPx()),
                    size = Size(12.dp.toPx(), winH + 20.dp.toPx()),
                    cornerRadius = CornerRadius(5.dp.toPx()),
                )

                // ── Wall art (above sofa) ──
                drawRoundRect(
                    color = ProTextMuted.copy(alpha = 0.20f),
                    topLeft = Offset(w * 0.10f, h * 0.08f),
                    size = Size(w * 0.16f, h * 0.22f),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                    style = Stroke(width = 1.5.dp.toPx()),
                )
                // Art inner landscape
                drawRoundRect(
                    color = ProAccent.copy(alpha = glowAlpha * 0.12f),
                    topLeft = Offset(w * 0.115f, h * 0.10f),
                    size = Size(w * 0.13f, h * 0.18f),
                    cornerRadius = CornerRadius(2.dp.toPx()),
                )
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.115f, h * 0.24f)
                        lineTo(w * 0.16f, h * 0.14f)
                        lineTo(w * 0.21f, h * 0.20f)
                        lineTo(w * 0.245f, h * 0.16f)
                        lineTo(w * 0.245f, h * 0.28f)
                        lineTo(w * 0.115f, h * 0.28f)
                        close()
                    },
                    color = ProAccent.copy(alpha = glowAlpha * 0.10f),
                )

                // ── Floor line ──
                drawLine(
                    color = ProAccent.copy(alpha = 0.30f),
                    start = Offset(w * 0.02f, h * 0.78f),
                    end = Offset(w * 0.98f, h * 0.78f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                // ── Rug (oval, under furniture) ──
                drawOval(
                    color = ProAccent.copy(alpha = 0.08f),
                    topLeft = Offset(w * 0.06f, h * 0.72f),
                    size = Size(w * 0.68f, h * 0.14f),
                )
                drawOval(
                    color = ProGold.copy(alpha = 0.06f),
                    topLeft = Offset(w * 0.10f, h * 0.74f),
                    size = Size(w * 0.60f, h * 0.08f),
                )

                // ── Sofa ──
                // Legs
                drawLine(
                    color = ProTextMuted.copy(alpha = 0.25f),
                    start = Offset(w * 0.16f, h * 0.78f),
                    end = Offset(w * 0.16f, h * 0.82f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = ProTextMuted.copy(alpha = 0.25f),
                    start = Offset(w * 0.60f, h * 0.78f),
                    end = Offset(w * 0.60f, h * 0.82f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                // Sofa body
                drawRoundRect(
                    color = ProAccent.copy(alpha = 0.45f),
                    topLeft = Offset(w * 0.08f, h * 0.50f),
                    size = Size(w * 0.58f, h * 0.28f),
                    cornerRadius = CornerRadius(14.dp.toPx()),
                )
                // Sofa backrest
                drawRoundRect(
                    color = ProAccent.copy(alpha = 0.30f),
                    topLeft = Offset(w * 0.10f, h * 0.38f),
                    size = Size(w * 0.54f, h * 0.14f),
                    cornerRadius = CornerRadius(12.dp.toPx()),
                )
                // Left armrest
                drawRoundRect(
                    color = ProAccent.copy(alpha = 0.38f),
                    topLeft = Offset(w * 0.06f, h * 0.42f),
                    size = Size(w * 0.08f, h * 0.30f),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                // Right armrest
                drawRoundRect(
                    color = ProAccent.copy(alpha = 0.38f),
                    topLeft = Offset(w * 0.60f, h * 0.42f),
                    size = Size(w * 0.08f, h * 0.30f),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                // Left cushion
                drawRoundRect(
                    color = ProAccentLight.copy(alpha = 0.22f),
                    topLeft = Offset(w * 0.14f, h * 0.53f),
                    size = Size(w * 0.22f, h * 0.12f),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                // Right cushion
                drawRoundRect(
                    color = ProAccentLight.copy(alpha = 0.22f),
                    topLeft = Offset(w * 0.40f, h * 0.53f),
                    size = Size(w * 0.22f, h * 0.12f),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                // Throw pillow
                drawRoundRect(
                    color = ProGold.copy(alpha = 0.18f),
                    topLeft = Offset(w * 0.14f, h * 0.41f),
                    size = Size(w * 0.10f, h * 0.10f),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                )

                // ── Coffee table ──
                // Table top
                drawRoundRect(
                    color = ProGold.copy(alpha = 0.22f),
                    topLeft = Offset(w * 0.24f, h * 0.72f),
                    size = Size(w * 0.30f, h * 0.05f),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                )
                // Table legs
                drawLine(
                    color = ProGold.copy(alpha = 0.18f),
                    start = Offset(w * 0.27f, h * 0.77f),
                    end = Offset(w * 0.27f, h * 0.82f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = ProGold.copy(alpha = 0.18f),
                    start = Offset(w * 0.51f, h * 0.77f),
                    end = Offset(w * 0.51f, h * 0.82f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                // Vase on table
                drawRoundRect(
                    color = ProGold.copy(alpha = 0.30f),
                    topLeft = Offset(w * 0.35f, h * 0.64f),
                    size = Size(w * 0.07f, h * 0.08f),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                )
                // Stem
                drawLine(
                    color = ProCheckGreen.copy(alpha = 0.30f),
                    start = Offset(w * 0.385f, h * 0.64f),
                    end = Offset(w * 0.385f, h * 0.58f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                // Leaf
                drawCircle(
                    color = ProCheckGreen.copy(alpha = 0.28f),
                    radius = w * 0.022f,
                    center = Offset(w * 0.395f, h * 0.56f),
                )

                // ── Floor lamp (right side) ──
                // Base
                drawOval(
                    color = ProGold.copy(alpha = 0.25f),
                    topLeft = Offset(w * 0.76f, h * 0.78f),
                    size = Size(w * 0.10f, h * 0.03f),
                )
                // Pole
                drawLine(
                    color = ProGold.copy(alpha = 0.35f),
                    start = Offset(w * 0.81f, h * 0.78f),
                    end = Offset(w * 0.81f, h * 0.28f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                // Shade (trapezoid)
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.75f, h * 0.28f)
                        lineTo(w * 0.87f, h * 0.28f)
                        lineTo(w * 0.84f, h * 0.20f)
                        lineTo(w * 0.78f, h * 0.20f)
                        close()
                    },
                    color = ProGold.copy(alpha = glowAlpha * 0.45f),
                )
                // Lamp glow
                drawCircle(
                    color = ProGold.copy(alpha = glowAlpha * 0.06f),
                    radius = w * 0.09f,
                    center = Offset(w * 0.81f, h * 0.24f),
                )

                // ── Potted plant (far left) ──
                // Pot
                drawRoundRect(
                    color = ProGold.copy(alpha = 0.25f),
                    topLeft = Offset(w * 0.00f, h * 0.64f),
                    size = Size(w * 0.07f, h * 0.14f),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                )
                // Stems + leaves
                drawLine(
                    color = ProCheckGreen.copy(alpha = 0.25f),
                    start = Offset(w * 0.035f, h * 0.64f),
                    end = Offset(w * 0.035f, h * 0.56f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawCircle(
                    color = ProCheckGreen.copy(alpha = 0.32f),
                    radius = w * 0.045f,
                    center = Offset(w * 0.035f, h * 0.52f),
                )
                drawCircle(
                    color = ProCheckGreen.copy(alpha = 0.22f),
                    radius = w * 0.032f,
                    center = Offset(w * 0.01f, h * 0.49f),
                )
                drawCircle(
                    color = ProCheckGreen.copy(alpha = 0.28f),
                    radius = w * 0.028f,
                    center = Offset(w * 0.06f, h * 0.50f),
                )
            }
        }
    }
}

// ── Sparkle Particles ──────────────────────────────────────────────────────────

@Composable
private fun CelebrationParticles(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkles")
    val particles = remember {
        List(5) { index ->
            Triple(
                0.15f + (index * 0.175f),   // x position
                0.3f + (index % 3) * 0.15f,  // y position
                0.8f + (index % 2) * 0.4f,   // size multiplier
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
                tint = ProGold.copy(alpha = alpha * 0.7f),
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

// ── Screen 0: ProIntroScreen ───────────────────────────────────────────────────

@Composable
private fun ProIntroScreen(
    onClose: () -> Unit,
    onContinue: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ProDarkStart, ProDarkMid, ProDarkEnd),
                ),
            )
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = HomeDecorSpacing.Lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(onBack = null, onClose = onClose)

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            // Celebration text
            Text(
                stringResource(R.string.pro_intro_celebration),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                ),
                color = ProGold,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            // Hero illustration with sparkle particles
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(220.dp),
            ) {
                ProHeroIllustration(Modifier.fillMaxSize())
                CelebrationParticles(Modifier.fillMaxSize())
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
                color = ProTextPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            // Subtitle
            Text(
                stringResource(R.string.pro_intro_sub),
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                color = ProTextSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            // PRO badge below subtitle
            ProBadge()

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            // Benefit preview chips (wrapped to prevent clipping)
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
                        color = ProAccent.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, ProAccent.copy(alpha = 0.2f)),
                    ) {
                        Row(
                            Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                        ) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = ProAccentLight,
                                modifier = Modifier.size(12.dp),
                            )
                            Text(
                                label,
                                style = MaterialTheme.typography.labelSmall,
                                color = ProTextSecondary,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // CTA
            ProCtaButton(
                label = stringResource(R.string.pro_intro_cta),
                processing = false,
                success = false,
                onClick = onContinue,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            Text(
                stringResource(R.string.pro_google_play_checkout),
                style = MaterialTheme.typography.bodySmall,
                color = ProTextMuted,
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
    pagerState: androidx.compose.foundation.pager.PagerState,
    onClose: () -> Unit,
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val benefits = listOf(
        ProBenefit(Icons.Rounded.AllInclusive, R.string.pro_benefit_unlimited_title, R.string.pro_benefit_unlimited_sub, ProAccent),
        ProBenefit(Icons.Rounded.HighQuality, R.string.pro_benefit_4k_title, R.string.pro_benefit_4k_sub, ProGold),
        ProBenefit(Icons.Rounded.WaterDrop, R.string.pro_benefit_watermark_title, R.string.pro_benefit_watermark_sub, ProCheckGreen),
        ProBenefit(Icons.Rounded.Speed, R.string.pro_benefit_faster_title, R.string.pro_benefit_faster_sub, ProCelebration),
        ProBenefit(Icons.Rounded.Palette, R.string.pro_benefit_styles_title, R.string.pro_benefit_styles_sub, ProGoldDark),
        ProBenefit(Icons.Rounded.History, R.string.pro_benefit_history_title, R.string.pro_benefit_history_sub, ProAccentLight),
    )

    val isLastPage = pagerState.currentPage == 5

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ProDarkStart, ProDarkMid, ProDarkEnd),
                ),
            )
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = HomeDecorSpacing.Lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            // Step progress
            Text(
                stringResource(
                    R.string.pro_a11y_step_progress,
                    pagerState.currentPage + 1,
                    6,
                ),
                style = MaterialTheme.typography.labelMedium,
                color = ProTextMuted,
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
                    // Large icon container with benefit-specific accent color
                    Surface(
                        shape = CircleShape,
                        color = benefit.accentColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.5.dp, benefit.accentColor.copy(alpha = 0.3f)),
                        modifier = Modifier.size(110.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                benefit.icon,
                                contentDescription = null,
                                tint = benefit.accentColor,
                                modifier = Modifier.size(50.dp),
                            )
                        }
                    }

                    Spacer(Modifier.height(HomeDecorSpacing.Xl))

                    Text(
                        stringResource(benefit.titleRes),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp,
                        ),
                        color = ProTextPrimary,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(HomeDecorSpacing.Md))

                    Text(
                        stringResource(benefit.subRes),
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                        color = ProTextSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            ProPageIndicator(
                pageCount = 6,
                currentPage = pagerState.currentPage,
            )

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
                        .border(1.dp, ProTextMuted.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .clickable { onSkip() },
                ) {
                    Text(
                        stringResource(R.string.pro_benefit_skip),
                        style = MaterialTheme.typography.titleSmall,
                        color = ProTextSecondary,
                    )
                }

                // Next / Start Trial button
                ProCtaButton(
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

private data class ProBenefit(
    val icon: ImageVector,
    val titleRes: Int,
    val subRes: Int,
    val accentColor: Color,
)

// ── Screen 2: ProReminderScreen ────────────────────────────────────────────────

@Composable
private fun ProReminderScreen(
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
                    colors = listOf(ProDarkStart, ProDarkMid, ProDarkEnd),
                ),
            )
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = HomeDecorSpacing.Lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(HomeDecorSpacing.Xl))

            // Notification bell icon
            Surface(
                shape = CircleShape,
                color = ProGold.copy(alpha = 0.15f),
                border = BorderStroke(1.5.dp, ProGold.copy(alpha = 0.3f)),
                modifier = Modifier.size(88.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = ProGold,
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
                color = ProTextPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            Text(
                stringResource(R.string.pro_reminder_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = ProTextSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            ReminderOption(
                label = stringResource(R.string.pro_reminder_2days),
                selected = selectedReminder == "2days",
                onClick = { onSelectReminder("2days") },
                contentDescription = reminder2Description,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            ReminderOption(
                label = stringResource(R.string.pro_reminder_3days),
                selected = selectedReminder == "3days",
                onClick = { onSelectReminder("3days") },
                contentDescription = reminder3Description,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = ProCheckGreen,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    stringResource(R.string.pro_reminder_reassurance),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ProTextSecondary,
                )
            }

            Spacer(Modifier.weight(1f))

            ProCtaButton(
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
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) ProAccent else ProTextMuted.copy(alpha = 0.2f),
        animationSpec = tween(200),
        label = "reminderBorder",
    )
    val cd = contentDescription
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) ProAccent.copy(alpha = 0.12f) else ProCardSurface,
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
                .padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (selected) ProAccent else Color.Transparent)
                    .then(
                        if (!selected) {
                            Modifier.border(2.dp, ProTextMuted.copy(alpha = 0.4f), CircleShape)
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
                color = if (selected) ProTextPrimary else ProTextSecondary,
            )
        }
    }
}

// ── Screen 3: ProPlanScreen ────────────────────────────────────────────────────

@Composable
private fun ProPlanScreen(
    selectedPlan: String,
    weeklyPrice: String,
    yearlyPackage: Package?,
    onPlanSelected: (String) -> Unit,
    onClose: () -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    onRestore: () -> Unit,
) {
    // Determine which plan gets the "MOST POPULAR" badge.
    // Currently yearly, but this can be changed based on best value/savings logic.
    val recommendedPlan = remember {
        // Logic: use the plan with the best per-period value.
        // For now, yearly is always recommended.
        // To make this dynamic, compare yearly vs weekly price-per-day here.
        "yearly"
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ProDarkStart, ProDarkMid, ProDarkEnd),
                ),
            )
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = HomeDecorSpacing.Lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            Text(
                stringResource(R.string.pro_plan_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = ProTextPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            // Yearly plan card
            ProPlanCard(
                planLabel = stringResource(R.string.pro_plan_annual),
                price = yearlyPackage?.product?.price?.formatted,
                priceSuffix = stringResource(R.string.pro_plan_per_year),
                detail = stringResource(R.string.pro_plan_annual_detail),
                selected = selectedPlan == "yearly",
                isRecommended = recommendedPlan == "yearly",
                onClick = { onPlanSelected("yearly") },
            )

            Spacer(Modifier.height(HomeDecorSpacing.Md))

            // Weekly plan card
            ProPlanCard(
                planLabel = stringResource(R.string.pro_plan_weekly),
                price = weeklyPrice.ifEmpty { null },
                priceSuffix = stringResource(R.string.pro_plan_per_week),
                detail = stringResource(R.string.pro_plan_weekly_detail),
                selected = selectedPlan == "weekly",
                isRecommended = recommendedPlan == "weekly",
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
                    tint = ProCheckGreen,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    stringResource(R.string.pro_trial_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = ProTextSecondary,
                )
            }

            Spacer(Modifier.height(HomeDecorSpacing.Lg))

            // CTA
            ProCtaButton(
                label = stringResource(R.string.pro_plan_trial_cta),
                processing = false,
                success = false,
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
                    tint = ProCheckGreen,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                Text(
                    stringResource(R.string.pro_cancel_anytime),
                    color = ProTextMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            // Terms
            Text(
                stringResource(R.string.pro_plan_terms),
                style = MaterialTheme.typography.bodySmall,
                color = ProTextMuted.copy(alpha = 0.6f),
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
    planLabel: String,
    price: String?,
    priceSuffix: String,
    detail: String,
    selected: Boolean,
    isRecommended: Boolean,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            isRecommended && selected -> ProGold
            selected -> ProAccent
            else -> ProTextMuted.copy(alpha = 0.15f)
        },
        animationSpec = tween(200),
        label = "planBorder",
    )

    val bgBrush = when {
        isRecommended -> Brush.horizontalGradient(
            colors = listOf(ProGradientStart.copy(alpha = 0.15f), ProGradientEnd.copy(alpha = 0.1f)),
        )
        else -> SolidColor(if (selected) ProAccentSurface else ProCardSurface)
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
                    color = if (selected) ProAccentSurface else ProCardSurface,
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
                        color = if (selected) ProAccentLight else ProTextSecondary,
                    )
                    if (isRecommended) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ProGold.copy(alpha = 0.2f),
                        ) {
                            Text(
                                stringResource(R.string.pro_plan_best_value),
                                modifier = Modifier.padding(horizontal = HomeDecorSpacing.Sm, vertical = 3.dp),
                                color = ProGold,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(if (selected) ProAccent else Color.Transparent)
                        .then(
                            if (!selected) Modifier.border(2.dp, ProTextMuted.copy(alpha = 0.4f), CircleShape) else Modifier,
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
                Text(
                    price ?: "\u2014",
                    style = MaterialTheme.typography.displaySmall.copy(letterSpacing = (-1).sp),
                    color = ProTextPrimary,
                )
                Spacer(Modifier.width(HomeDecorSpacing.Xs))
                Text(
                    priceSuffix,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ProTextMuted,
                    modifier = Modifier.padding(bottom = HomeDecorSpacing.Xs),
                )
            }

            Spacer(Modifier.height(HomeDecorSpacing.Sm))

            Text(
                detail,
                style = MaterialTheme.typography.bodySmall,
                color = ProTextMuted,
            )
        }
    }
}

// ── Screen 4: ProCheckoutScreen ────────────────────────────────────────────────

@Composable
private fun ProCheckoutScreen(
    processing: Boolean,
    success: Boolean,
    onClose: () -> Unit,
) {
    // Auto-close on success after brief celebration
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
                    colors = listOf(ProDarkStart, ProDarkMid, ProDarkEnd),
                ),
            )
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = HomeDecorSpacing.Lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (processing && !success) {
                CircularProgressIndicator(
                    modifier = Modifier.size(56.dp),
                    color = ProAccent,
                    strokeWidth = 4.dp,
                )
                Spacer(Modifier.height(HomeDecorSpacing.Lg))
                Text(
                    stringResource(R.string.pro_checkout_setting_up),
                    style = MaterialTheme.typography.titleMedium,
                    color = ProTextSecondary,
                    textAlign = TextAlign.Center,
                )
            } else if (success) {
                // Animated checkmark
                Surface(
                    shape = CircleShape,
                    color = ProCheckGreen.copy(alpha = 0.15f),
                    border = BorderStroke(2.dp, ProCheckGreen.copy(alpha = 0.3f)),
                    modifier = Modifier.size(100.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint = ProCheckGreen,
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
                    color = ProTextPrimary,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(HomeDecorSpacing.Sm))

                Text(
                    stringResource(R.string.pro_hero_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ProTextSecondary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
