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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
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
        Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = ProTextSecondary,
                    modifier = Modifier.size(22.dp),
                )
            }
        } else {
            Spacer(Modifier.size(48.dp))
        }
        ProBadge()
        IconButton(onClick = onClose, modifier = Modifier.size(48.dp)) {
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
            .height(56.dp)
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
                Spacer(Modifier.width(10.dp))
                Text(label, style = MaterialTheme.typography.titleSmall)
            } else if (success) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White,
                )
                Spacer(Modifier.width(8.dp))
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        Box(
            modifier = Modifier
                .minimumTouchTarget()
                .semantics { contentDescription = restoreDescription; role = Role.Button }
                .clickable { onRestore() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
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
            modifier = Modifier.padding(vertical = 12.dp),
        )
        Box(
            modifier = Modifier
                .minimumTouchTarget()
                .semantics { contentDescription = termsDescription; role = Role.Button }
                .clickable { }
                .padding(horizontal = 12.dp, vertical = 8.dp),
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
            modifier = Modifier.padding(vertical = 12.dp),
        )
        Box(
            modifier = Modifier
                .minimumTouchTarget()
                .semantics { contentDescription = privacyDescription; role = Role.Button }
                .clickable { }
                .padding(horizontal = 12.dp, vertical = 8.dp),
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
            // Stylized living room silhouette with premium feel
            androidx.compose.foundation.Canvas(
                Modifier
                    .fillMaxWidth(0.85f)
                    .height(160.dp),
            ) {
                val w = size.width
                val h = size.height

                // Floor line
                drawLine(
                    color = ProAccent.copy(alpha = 0.4f),
                    start = Offset(w * 0.05f, h * 0.78f),
                    end = Offset(w * 0.95f, h * 0.78f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                // Sofa — rounded rectangle body
                drawRoundRect(
                    color = ProAccent.copy(alpha = 0.5f),
                    topLeft = Offset(w * 0.1f, h * 0.5f),
                    size = androidx.compose.ui.geometry.Size(w * 0.55f, h * 0.28f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
                )
                // Sofa back
                drawRoundRect(
                    color = ProAccent.copy(alpha = 0.35f),
                    topLeft = Offset(w * 0.12f, h * 0.38f),
                    size = androidx.compose.ui.geometry.Size(w * 0.51f, h * 0.15f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()),
                )
                // Sofa cushions
                drawRoundRect(
                    color = ProAccentLight.copy(alpha = 0.3f),
                    topLeft = Offset(w * 0.15f, h * 0.53f),
                    size = androidx.compose.ui.geometry.Size(w * 0.22f, h * 0.12f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                )
                drawRoundRect(
                    color = ProAccentLight.copy(alpha = 0.3f),
                    topLeft = Offset(w * 0.4f, h * 0.53f),
                    size = androidx.compose.ui.geometry.Size(w * 0.22f, h * 0.12f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                )

                // Side table
                drawRoundRect(
                    color = ProGold.copy(alpha = 0.4f),
                    topLeft = Offset(w * 0.72f, h * 0.58f),
                    size = androidx.compose.ui.geometry.Size(w * 0.12f, h * 0.2f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()),
                )
                // Lamp on table
                drawLine(
                    color = ProGold.copy(alpha = 0.5f),
                    start = Offset(w * 0.78f, h * 0.58f),
                    end = Offset(w * 0.78f, h * 0.42f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                // Lamp shade
                drawPath(
                    path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.73f, h * 0.42f)
                        lineTo(w * 0.83f, h * 0.42f)
                        lineTo(w * 0.8f, h * 0.34f)
                        lineTo(w * 0.76f, h * 0.34f)
                        close()
                    },
                    color = ProGold.copy(alpha = glowAlpha * 0.5f),
                )

                // Wall art frame
                drawRoundRect(
                    color = ProTextMuted.copy(alpha = 0.25f),
                    topLeft = Offset(w * 0.25f, h * 0.1f),
                    size = androidx.compose.ui.geometry.Size(w * 0.35f, h * 0.22f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()),
                    style = Stroke(width = 1.5.dp.toPx()),
                )
                // Art inner
                drawRoundRect(
                    color = ProAccent.copy(alpha = glowAlpha * 0.2f),
                    topLeft = Offset(w * 0.28f, h * 0.13f),
                    size = androidx.compose.ui.geometry.Size(w * 0.29f, h * 0.16f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
                )

                // Plant
                drawCircle(
                    color = ProCheckGreen.copy(alpha = 0.35f),
                    radius = w * 0.04f,
                    center = Offset(w * 0.88f, h * 0.52f),
                )
                drawCircle(
                    color = ProCheckGreen.copy(alpha = 0.25f),
                    radius = w * 0.03f,
                    center = Offset(w * 0.85f, h * 0.48f),
                )
                drawLine(
                    color = ProCheckGreen.copy(alpha = 0.3f),
                    start = Offset(w * 0.88f, h * 0.56f),
                    end = Offset(w * 0.88f, h * 0.65f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(onBack = null, onClose = onClose)

            Spacer(Modifier.height(12.dp))

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

            Spacer(Modifier.height(20.dp))

            // Hero illustration with sparkle particles
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                ProHeroIllustration(Modifier.fillMaxSize())
                CelebrationParticles(Modifier.fillMaxSize())
            }

            Spacer(Modifier.height(28.dp))

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

            Spacer(Modifier.height(12.dp))

            // Subtitle
            Text(
                stringResource(R.string.pro_intro_sub),
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                color = ProTextSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(8.dp))

            // PRO badge below subtitle
            ProBadge()

            Spacer(Modifier.weight(1f))

            // CTA
            ProCtaButton(
                label = stringResource(R.string.pro_intro_cta),
                processing = false,
                success = false,
                onClick = onContinue,
            )

            Spacer(Modifier.height(12.dp))

            // Benefit preview chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
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
                            Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                stringResource(R.string.pro_google_play_checkout),
                style = MaterialTheme.typography.bodySmall,
                color = ProTextMuted,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(24.dp))

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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(8.dp))

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

            Spacer(Modifier.height(12.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                val benefit = benefits[page]
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
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

                    Spacer(Modifier.height(32.dp))

                    Text(
                        stringResource(benefit.titleRes),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp,
                        ),
                        color = ProTextPrimary,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(12.dp))

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

            Spacer(Modifier.height(20.dp))

            // Bottom buttons
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Skip button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
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

            Spacer(Modifier.height(24.dp))

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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(32.dp))

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

            Spacer(Modifier.height(24.dp))

            Text(
                stringResource(R.string.pro_reminder_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp,
                ),
                color = ProTextPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                stringResource(R.string.pro_reminder_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = ProTextSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(28.dp))

            ReminderOption(
                label = stringResource(R.string.pro_reminder_2days),
                selected = selectedReminder == "2days",
                onClick = { onSelectReminder("2days") },
                contentDescription = reminder2Description,
            )

            Spacer(Modifier.height(12.dp))

            ReminderOption(
                label = stringResource(R.string.pro_reminder_3days),
                selected = selectedReminder == "3days",
                onClick = { onSelectReminder("3days") },
                contentDescription = reminder3Description,
            )

            Spacer(Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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

            Spacer(Modifier.height(24.dp))

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
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProScreenHeader(onBack = onBack, onClose = onClose)

            Spacer(Modifier.height(20.dp))

            Text(
                stringResource(R.string.pro_plan_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = ProTextPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(24.dp))

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

            Spacer(Modifier.height(12.dp))

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

            Spacer(Modifier.height(20.dp))

            // Trial note
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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

            Spacer(Modifier.height(24.dp))

            // CTA
            ProCtaButton(
                label = stringResource(R.string.pro_plan_trial_cta),
                processing = false,
                success = false,
                onClick = onContinue,
            )

            Spacer(Modifier.height(12.dp))

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
                Spacer(Modifier.width(6.dp))
                Text(
                    stringResource(R.string.pro_cancel_anytime),
                    color = ProTextMuted,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(16.dp))

            // Terms
            Text(
                stringResource(R.string.pro_plan_terms),
                style = MaterialTheme.typography.bodySmall,
                color = ProTextMuted.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(16.dp))

            ProBottomLinks(onRestore = onRestore)

            Spacer(Modifier.height(24.dp))

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
                .padding(16.dp),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
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

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    price ?: "\u2014",
                    style = MaterialTheme.typography.displaySmall.copy(letterSpacing = (-1).sp),
                    color = ProTextPrimary,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    priceSuffix,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ProTextMuted,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            Spacer(Modifier.height(6.dp))

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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (processing && !success) {
                CircularProgressIndicator(
                    modifier = Modifier.size(56.dp),
                    color = ProAccent,
                    strokeWidth = 4.dp,
                )
                Spacer(Modifier.height(24.dp))
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

                Spacer(Modifier.height(24.dp))

                Text(
                    stringResource(R.string.pro_cta_welcome),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = ProTextPrimary,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(8.dp))

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
