package com.ismail.homedecorai.ui.paywall

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.findActivity
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

private const val TOTAL_STEPS = 5

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
    var currentStep by remember { mutableIntStateOf(1) }
    var selectedReminder by remember { mutableIntStateOf(2) }

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
    val monthlyPackage = offering?.availablePackages?.firstOrNull { it.packageType == PackageType.MONTHLY }
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

    LaunchedEffect(purchaseSuccess) {
        if (purchaseSuccess) {
            delay(1800)
            onClose()
        }
    }

    fun goNext() {
        if (currentStep < TOTAL_STEPS) currentStep++
    }

    fun goBack() {
        if (currentStep > 1) currentStep-- else onClose()
    }

    BackHandler(enabled = true) {
        goBack()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(PaywallBg)
            .clickable(interactionSource = modalTapBlocker, indication = null, onClick = {}),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            PaywallTopBar(
                currentStep = currentStep,
                onClose = onClose,
                onBack = ::goBack,
            )

            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (currentStep) {
                    1 -> PaywallScreen1Unlimited(onNext = ::goNext)
                    2 -> PaywallScreen2Reminder(
                        selectedReminder = selectedReminder,
                        onReminderSelected = { selectedReminder = it },
                        onNext = ::goNext,
                    )
                    3 -> PaywallScreen3Comparison(onNext = ::goNext)
                    4 -> PaywallScreen4Pricing(
                        selectedPlan = selectedPlan,
                        onPlanSelected = { selectedPlan = it },
                        yearlyPackage = yearlyPackage,
                        weeklyPackage = weeklyPackage,
                        monthlyPackage = monthlyPackage,
                        offeringsLoading = offeringsLoading,
                        onNext = ::goNext,
                    )
                    5 -> PaywallScreen5Checkout(
                        selectedPlan = selectedPlan,
                        selectedPackage = selectedPackage,
                        yearlyPackage = yearlyPackage,
                        offeringsLoading = offeringsLoading,
                        purchasing = purchasing,
                        purchaseSuccess = purchaseSuccess,
                        onPurchase = {
                            if (!purchaseBusy) {
                                val entitlement = when (selectedPlan) {
                                    "yearly" -> "annual_pro"
                                    "weekly" -> "weekly_pro"
                                    else -> "annual_pro"
                                }
                                buy(selectedPackage, selectedPlan, entitlement)
                            }
                        },
                    )
                }
            }

            if (currentStep < 5) {
                PaywallBottomCta(
                    label = when (currentStep) {
                        1 -> stringResource(R.string.pw_s1_cta)
                        2 -> stringResource(R.string.pw_s2_cta)
                        3 -> stringResource(R.string.pw_s3_cta)
                        4 -> stringResource(R.string.pw_s4_cta)
                        else -> ""
                    },
                    enabled = !offeringsLoading && (yearlyPackage != null || weeklyPackage != null),
                    processing = false,
                    success = false,
                    onClick = ::goNext,
                )
            }
        }

        if (currentStep == 5) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, PaywallBg, PaywallBg),
                        ),
                    )
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                PaywallCtaButton(
                    label = stringResource(R.string.pw_s5_cta),
                    processing = purchasing,
                    success = purchaseSuccess,
                    enabled = !offeringsLoading && selectedPackage != null,
                    onClick = {
                        if (!purchaseBusy) {
                            val entitlement = when (selectedPlan) {
                                "yearly" -> "annual_pro"
                                "weekly" -> "weekly_pro"
                                else -> "annual_pro"
                            }
                            buy(selectedPackage, selectedPlan, entitlement)
                        }
                    },
                )
                Text(
                    stringResource(R.string.pw_s4_trust),
                    style = MaterialTheme.typography.bodySmall,
                    color = PaywallTextMuted,
                    textAlign = TextAlign.Center,
                )
                PaywallBottomLinks(onRestore = { onRetrySync() })
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
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = if (currentStep == 1) onClose else onBack,
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                imageVector = if (currentStep == 1) Icons.Rounded.Close else Icons.Rounded.ArrowBack,
                contentDescription = stringResource(
                    if (currentStep == 1) R.string.paywall_a11y_close else R.string.paywall_a11y_back
                ),
                tint = PaywallTextSecondary,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(Modifier.weight(1f))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = PaywallCard,
            border = BorderStroke(1.dp, PaywallBorderStrong),
            modifier = Modifier.height(28.dp),
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
                    color = PaywallTextPrimary,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Spacer(Modifier.size(48.dp))
    }
}

// ── Screen 1: Unlimited Room Makeovers ──────────────────────────────────────

@Composable
private fun PaywallScreen1Unlimited(onNext: () -> Unit) {
    Spacer(Modifier.height(8.dp))

    val heading = stringResource(R.string.pw_s1_heading)
    val highlight = stringResource(R.string.pw_s1_heading_highlight)
    val parts = heading.split(highlight, limit = 2)
    val annotatedHeading = if (parts.size == 2) {
        buildAnnotatedString {
            append(parts[0])
            withStyle(SpanStyle(color = PaywallAccent)) { append(highlight) }
            append(parts[1])
        }
    } else {
        buildAnnotatedString { append(heading) }
    }
    Text(
        annotatedHeading,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(20.dp))

    // Infinity hero visual placeholder
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PaywallCardAlt,
        border = BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                "\u221E",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Light,
                ),
                color = PaywallAccent.copy(alpha = 0.3f),
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    // Benefits card
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PaywallCard,
        border = BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            listOf(
                stringResource(R.string.pw_s1_benefit_1),
                stringResource(R.string.pw_s1_benefit_2),
                stringResource(R.string.pw_s1_benefit_3),
                stringResource(R.string.pw_s1_benefit_4),
            ).forEach { benefit ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = PaywallAccent,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        benefit,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PaywallTextPrimary,
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Social proof
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
            listOf(PaywallAccent, PaywallPremiumGold, PaywallAccentLight, PaywallAccent).forEach { color ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp),
                ) {}
            }
        }
        Text(
            stringResource(R.string.pw_s1_social_proof),
            style = MaterialTheme.typography.bodySmall,
            color = PaywallTextSecondary,
            modifier = Modifier.weight(1f),
        )
    }

    Spacer(Modifier.height(24.dp))
}

// ── Screen 2: Trial Reminder ────────────────────────────────────────────────

@Composable
private fun PaywallScreen2Reminder(
    selectedReminder: Int,
    onReminderSelected: (Int) -> Unit,
    onNext: () -> Unit,
) {
    Spacer(Modifier.height(8.dp))

    Text(
        stringResource(R.string.pw_s2_heading),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = PaywallTextPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(24.dp))

    // Reminder icons visual
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        listOf(
            Icons.Rounded.Notifications to PaywallPremiumGold,
            Icons.Rounded.Star to PaywallAccent,
            Icons.Rounded.Lock to PaywallAccentLight,
        ).forEach { (icon, tint) ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = tint.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                }
            }
        }
    }

    Spacer(Modifier.height(24.dp))

    // Option cards
    listOf(
        1 to stringResource(R.string.pw_s2_option_1),
        2 to stringResource(R.string.pw_s2_option_2),
    ).forEach { (value, label) ->
        val selected = selectedReminder == value
        val bgColor by animateColorAsState(
            targetValue = if (selected) PaywallAccentSurface else PaywallCard,
            animationSpec = tween(200),
            label = "reminder_bg",
        )
        val borderColor by animateColorAsState(
            targetValue = if (selected) PaywallAccent else PaywallBorder,
            animationSpec = tween(200),
            label = "reminder_border",
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = bgColor,
            border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { onReminderSelected(value) },
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    Icons.Rounded.Notifications,
                    contentDescription = null,
                    tint = if (selected) PaywallAccent else PaywallTextMuted,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    label,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = PaywallTextPrimary,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(if (selected) PaywallAccent else Color.Transparent)
                        .then(
                            if (!selected) Modifier.border(2.dp, PaywallTextMuted.copy(alpha = 0.4f), CircleShape) else Modifier
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
        }

        Spacer(Modifier.height(10.dp))
    }

    Spacer(Modifier.height(12.dp))

    // Info card
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = PaywallAccentSurface.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, PaywallAccent.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                Icons.Rounded.Lock,
                contentDescription = null,
                tint = PaywallAccent,
                modifier = Modifier.size(18.dp),
            )
            Text(
                stringResource(R.string.pw_s2_info),
                style = MaterialTheme.typography.bodySmall,
                color = PaywallTextSecondary,
            )
        }
    }

    Spacer(Modifier.height(24.dp))
}

// ── Screen 3: Free vs Premium Comparison ─────────────────────────────────────

@Composable
private fun PaywallScreen3Comparison(onNext: () -> Unit) {
    Spacer(Modifier.height(8.dp))

    Text(
        stringResource(R.string.pw_s3_heading),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = PaywallTextPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(20.dp))

    // Comparison table card
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PaywallCard,
        border = BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            // Header row
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(PaywallCardAlt)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.pw_s3_col_feature),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = PaywallTextSecondary,
                    modifier = Modifier.weight(1.2f),
                )
                Text(
                    stringResource(R.string.pw_s3_col_free),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = PaywallTextSecondary,
                    modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.Center,
                )
                Text(
                    stringResource(R.string.pw_s3_col_premium),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = PaywallAccent,
                    modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.Center,
                )
            }

            val rows = listOf(
                Triple(stringResource(R.string.pw_s3_row1_feature), stringResource(R.string.pw_s3_row1_free), "\u2713"),
                Triple(stringResource(R.string.pw_s3_row2_feature), "\u2014", "\u2713"),
                Triple(stringResource(R.string.pw_s3_row3_feature), "\u2014", "\u2713"),
                Triple(stringResource(R.string.pw_s3_row4_feature), stringResource(R.string.pw_s3_row4_free), "\u2713"),
                Triple(stringResource(R.string.pw_s3_row5_feature), "\u2014", "\u2713"),
                Triple(stringResource(R.string.pw_s3_row6_feature), "\u2014", "\u2713"),
            )

            rows.forEachIndexed { index, (feature, freeVal, proVal) ->
                if (index > 0) {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = PaywallBorder,
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        feature,
                        style = MaterialTheme.typography.bodySmall,
                        color = PaywallTextPrimary,
                        modifier = Modifier.weight(1.2f),
                    )
                    Text(
                        freeVal,
                        style = MaterialTheme.typography.bodySmall,
                        color = PaywallTextMuted,
                        modifier = Modifier.weight(0.8f),
                        textAlign = TextAlign.Center,
                    )
                    if (proVal == "\u2713") {
                        Box(
                            modifier = Modifier
                                .weight(0.8f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = PaywallAccent,
                                modifier = Modifier.size(22.dp),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp),
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            proVal,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = PaywallAccent,
                            modifier = Modifier.weight(0.8f),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Recommendation card
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = PaywallCard,
        border = BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = null,
                tint = PaywallPremiumGold,
                modifier = Modifier.size(20.dp),
            )
            Text(
                stringResource(R.string.pw_s3_recommendation),
                style = MaterialTheme.typography.bodySmall,
                color = PaywallTextSecondary,
            )
        }
    }

    Spacer(Modifier.height(24.dp))
}

// ── Screen 4: Plan Selection / Pricing ───────────────────────────────────────

@Composable
private fun PaywallScreen4Pricing(
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
    yearlyPackage: Package?,
    weeklyPackage: Package?,
    monthlyPackage: Package?,
    offeringsLoading: Boolean,
    onNext: () -> Unit,
) {
    Spacer(Modifier.height(8.dp))

    Text(
        stringResource(R.string.pw_s4_heading),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = PaywallTextPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(20.dp))

    // Plan cards in a Row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Yearly
        PricingPlanCard(
            title = stringResource(R.string.pw_s4_plan_yearly_title),
            price = yearlyPackage?.product?.price?.formatted ?: "\u2014",
            detail = stringResource(R.string.pw_s4_plan_yearly_detail),
            badge = stringResource(R.string.pw_s4_plan_yearly_badge),
            savings = stringResource(R.string.pw_s4_plan_yearly_savings),
            selected = selectedPlan == "yearly",
            offeringsLoading = offeringsLoading,
            modifier = Modifier.weight(1f),
            onClick = { onPlanSelected("yearly") },
        )

        // Monthly
        PricingPlanCard(
            title = stringResource(R.string.pw_s4_plan_monthly_title),
            price = monthlyPackage?.product?.price?.formatted ?: "\u2014",
            detail = "",
            badge = null,
            savings = null,
            selected = selectedPlan == "monthly",
            offeringsLoading = offeringsLoading,
            modifier = Modifier.weight(1f),
            onClick = { onPlanSelected("monthly") },
        )

        // Family
        PricingPlanCard(
            title = stringResource(R.string.pw_s4_plan_family_title),
            price = stringResource(R.string.pw_s4_plan_family_price),
            detail = stringResource(R.string.pw_s4_plan_family_detail),
            badge = null,
            savings = null,
            selected = selectedPlan == "family",
            offeringsLoading = offeringsLoading,
            modifier = Modifier.weight(1f),
            onClick = { onPlanSelected("family") },
        )
    }

    Spacer(Modifier.height(24.dp))
}

@Composable
private fun PricingPlanCard(
    title: String,
    price: String,
    detail: String,
    badge: String?,
    savings: String?,
    selected: Boolean,
    offeringsLoading: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) PaywallAccent else PaywallBorder,
        animationSpec = tween(200),
        label = "plan_border",
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) PaywallAccentSurface else PaywallCard,
        border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor),
        modifier = modifier
            .height(220.dp)
            .clickable(onClick = onClick),
    ) {
        Column(
            Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                if (badge != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = PaywallPremiumGold,
                    ) {
                        Text(
                            badge,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                            ),
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }

                Text(
                    title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 18.sp,
                    ),
                    color = PaywallTextPrimary,
                )
            }

            Column {
                if (offeringsLoading) {
                    Box(
                        Modifier
                            .width(60.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(PaywallTextMuted.copy(alpha = 0.1f)),
                    )
                } else {
                    Text(
                        price,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp,
                        ),
                        color = PaywallTextPrimary,
                    )
                }

                if (detail.isNotEmpty()) {
                    Text(
                        detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = PaywallTextMuted,
                    )
                }

                if (savings != null) {
                    Spacer(Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PaywallAccentSurface,
                    ) {
                        Text(
                            savings,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            color = PaywallAccent,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (selected) PaywallAccent else Color.Transparent)
                    .then(
                        if (!selected) Modifier.border(2.dp, PaywallTextMuted.copy(alpha = 0.4f), CircleShape) else Modifier
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
    }
}

// ── Screen 5: Secure Google Play Checkout ────────────────────────────────────

@Composable
private fun PaywallScreen5Checkout(
    selectedPlan: String,
    selectedPackage: Package?,
    yearlyPackage: Package?,
    offeringsLoading: Boolean,
    purchasing: Boolean,
    purchaseSuccess: Boolean,
    onPurchase: () -> Unit,
) {
    // Badge
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PaywallAccentSurface,
        border = BorderStroke(1.dp, PaywallAccent.copy(alpha = 0.2f)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = null,
                tint = PaywallAccent,
                modifier = Modifier.size(14.dp),
            )
            Text(
                stringResource(R.string.pw_s5_badge),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = PaywallAccent,
            )
        }
    }

    Spacer(Modifier.height(12.dp))

    Text(
        stringResource(R.string.pw_s5_heading),
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = PaywallTextPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(16.dp))

    // Shield hero visual
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PaywallAccentSurface,
        border = BorderStroke(1.dp, PaywallAccent.copy(alpha = 0.15f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                Icons.Rounded.Lock,
                contentDescription = null,
                tint = PaywallAccent.copy(alpha = 0.4f),
                modifier = Modifier.size(48.dp),
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    // Subscription summary
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PaywallCard,
        border = BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                when (selectedPlan) {
                    "yearly" -> stringResource(R.string.pw_s4_plan_yearly_title).replace("\n", " ")
                    "monthly" -> stringResource(R.string.pw_s4_plan_monthly_title).replace("\n", " ")
                    "family" -> stringResource(R.string.pw_s4_plan_family_title).replace("\n", " ")
                    else -> stringResource(R.string.pw_s5_plan_title)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = PaywallTextPrimary,
            )
            Text(
                stringResource(R.string.pw_s5_plan_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = PaywallTextMuted,
            )

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(thickness = 0.5.dp, color = PaywallBorder)
            Spacer(Modifier.height(14.dp))

            val summaryRows = listOf(
                stringResource(R.string.pw_s5_trial_period) to stringResource(R.string.pw_s5_trial_value),
                stringResource(R.string.pw_s5_then) to (selectedPackage?.product?.price?.formatted ?: "\u2014"),
                stringResource(R.string.pw_s5_renewal_date) to "May 24, 2025",
                stringResource(R.string.pw_s5_payment) to stringResource(R.string.pw_s5_payment_value),
            )

            summaryRows.forEach { (label, value) ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.bodySmall,
                        color = PaywallTextSecondary,
                    )
                    Text(
                        value,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = PaywallTextPrimary,
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    // Included benefits
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PaywallCard,
        border = BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.pw_s5_benefits_title),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = PaywallTextPrimary,
            )

            Spacer(Modifier.height(10.dp))

            listOf(
                stringResource(R.string.pw_s5_benefit_1),
                stringResource(R.string.pw_s5_benefit_2),
                stringResource(R.string.pw_s5_benefit_3),
                stringResource(R.string.pw_s5_benefit_4),
                stringResource(R.string.pw_s5_benefit_5),
                stringResource(R.string.pw_s5_benefit_6),
            ).forEach { benefit ->
                Row(
                    Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PaywallAccent,
                        modifier = Modifier.size(18.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp),
                            )
                        }
                    }
                    Text(
                        benefit,
                        style = MaterialTheme.typography.bodySmall,
                        color = PaywallTextPrimary,
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    // Google Play trust row
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = PaywallCard,
        border = BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                Icons.Rounded.Lock,
                contentDescription = null,
                tint = PaywallAccent,
                modifier = Modifier.size(18.dp),
            )
            Text(
                stringResource(R.string.pw_s5_trust_row),
                style = MaterialTheme.typography.bodySmall,
                color = PaywallTextSecondary,
            )
        }
    }

    Spacer(Modifier.height(10.dp))

    Text(
        stringResource(R.string.pw_s5_legal),
        style = MaterialTheme.typography.bodySmall,
        color = PaywallTextMuted,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(100.dp))
}

// ── Shared Components ──────────────────────────────────────────────────────────

@Composable
private fun PaywallBottomCta(
    label: String,
    enabled: Boolean,
    processing: Boolean,
    success: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, PaywallBg, PaywallBg),
                ),
            )
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PaywallCtaButton(
            label = label,
            processing = processing,
            success = success,
            enabled = enabled,
            onClick = onClick,
        )
    }
}

@Composable
private fun PaywallCtaButton(
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
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                success -> PaywallSuccess
                !buttonEnabled -> PaywallTextMuted.copy(alpha = 0.3f)
                else -> PaywallAccent
            },
            contentColor = Color.White,
            disabledContainerColor = PaywallTextMuted.copy(alpha = 0.3f),
            disabledContentColor = PaywallTextMuted,
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
                Spacer(Modifier.width(8.dp))
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
private fun PaywallBottomLinks(onRestore: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        Box(
            modifier = Modifier
                .clickable { onRestore() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.paywall_restore),
                color = PaywallTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        Text(
            "\u00B7",
            color = PaywallTextMuted.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Box(
            modifier = Modifier
                .clickable { }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.terms),
                color = PaywallTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        Text(
            "\u00B7",
            color = PaywallTextMuted.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Box(
            modifier = Modifier
                .clickable { }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(R.string.privacy_policy),
                color = PaywallTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
    }
}
