package com.ismail.homedecorai.ui.paywall

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.R
import com.ismail.homedecorai.purchaseAttemptMessageRes
import com.ismail.homedecorai.rawServiceMessageToKind
import com.ismail.homedecorai.storeMessageRes
import com.ismail.homedecorai.ui.components.*
import com.ismail.homedecorai.ui.profile.PurchaseSyncNotice
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
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import com.revenuecat.purchases.models.StoreTransaction
import kotlinx.coroutines.delay

@Composable
fun PaywallSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onSubscription: (String, String, String, Double?, Double?) -> Unit,
    onRetrySync: () -> Unit,
    onStore: () -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val modalTapBlocker = remember { MutableInteractionSource() }
    var offering by remember { mutableStateOf<Offering?>(null) }
    var offeringsLoading by remember { mutableStateOf(true) }
    var purchasing by remember { mutableStateOf(false) }
    var restoring by remember { mutableStateOf(false) }
    var selectedPlan by remember { mutableStateOf("yearly") }
    var message by remember { mutableStateOf<String?>(null) }

    fun loadOfferings() {
        offeringsLoading = true
        offering = null
        message = null
        if (!Purchases.isConfigured) {
            offeringsLoading = false
            message = resources.getString(R.string.subscriptions_unavailable)
            return
        }
        Purchases.sharedInstance.getOfferings(object : ReceiveOfferingsCallback {
            override fun onReceived(offerings: com.revenuecat.purchases.Offerings) {
                offering = offerings.current ?: offerings.all.values.firstOrNull()
                offeringsLoading = false
                if (offering == null) {
                    message = resources.getString(R.string.no_subscription_available)
                }
            }

            override fun onError(error: PurchasesError) {
                offeringsLoading = false
                message = resources.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.load_prices_failed))
            }
        })
    }

    LaunchedEffect(Unit) {
        loadOfferings()
    }

    fun buy(packageToPurchase: Package?, subscriptionType: String, entitlement: String) {
        val activity = context.findActivity()
        if (packageToPurchase == null || activity == null || !Purchases.isConfigured) {
            message = resources.getString(R.string.plan_unavailable)
            return
        }
        purchasing = true
        message = null
        Purchases.sharedInstance.purchase(PurchaseParams.Builder(activity, packageToPurchase).build(), object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                purchasing = false
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
                message = if (userCancelled) {
                    resources.getString(R.string.purchase_cancelled)
                } else {
                    resources.getString(rawServiceMessageToKind(context, error.message).purchaseAttemptMessageRes(R.string.purchase_failed))
                }
            }
        })
    }

    val weeklyPackage = offering?.weekly ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.WEEKLY }
    val monthlyPackage = offering?.monthly ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.MONTHLY }
    val yearlyPackage = offering?.annual ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.ANNUAL }
    val purchaseBusy = purchasing || restoring || state.purchaseBusy
    val pricesUnavailable = !offeringsLoading && weeklyPackage == null && monthlyPackage == null && yearlyPackage == null
    val availablePlans = listOfNotNull(
        yearlyPackage?.let {
            PaywallPlan(
                id = "yearly",
                label = stringResource(R.string.paywall_access_yearly),
                price = it.product.price.formatted,
                detail = stringResource(R.string.paywall_billed_yearly, it.product.price.formatted),
                badge = stringResource(R.string.paywall_best_offer),
                packageToPurchase = it,
                entitlement = "annual_pro",
            )
        },
        weeklyPackage?.let {
            PaywallPlan(
                id = "weekly",
                label = stringResource(R.string.paywall_access_weekly),
                price = it.product.price.formatted,
                detail = stringResource(R.string.paywall_flexible_access),
                badge = null,
                packageToPurchase = it,
                entitlement = "weekly_pro",
            )
        },
        monthlyPackage?.let {
            PaywallPlan(
                id = "monthly",
                label = stringResource(R.string.paywall_access_monthly),
                price = it.product.price.formatted,
                detail = stringResource(R.string.paywall_billed_monthly),
                badge = null,
                packageToPurchase = it,
                entitlement = "monthly_pro",
            )
        },
    )

    LaunchedEffect(offeringsLoading, weeklyPackage, monthlyPackage, yearlyPackage) {
        if (!offeringsLoading) {
            selectedPlan = when {
                selectedPlan == "yearly" && yearlyPackage != null -> "yearly"
                selectedPlan == "monthly" && monthlyPackage != null -> "monthly"
                selectedPlan == "weekly" && weeklyPackage != null -> "weekly"
                yearlyPackage != null -> "yearly"
                weeklyPackage != null -> "weekly"
                monthlyPackage != null -> "monthly"
                else -> selectedPlan
            }
        }
    }

    val selectedPlanModel = availablePlans.firstOrNull { it.id == selectedPlan } ?: availablePlans.firstOrNull()
    val selectedPackage = selectedPlanModel?.packageToPurchase
    val selectedSubscriptionType = selectedPlanModel?.id ?: selectedPlan
    val selectedEntitlement = selectedPlanModel?.entitlement ?: "pro"

    Box(
        Modifier
            .fillMaxSize()
            .background(PaywallBg)
            .clickable(
                interactionSource = modalTapBlocker,
                indication = null,
                onClick = {},
            ),
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(1f)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 10.dp, end = 18.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.50f)),
        ) {
            Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close), tint = Color.White)
        }
        LazyColumn(
            contentPadding = PaddingValues(bottom = 34.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                PaywallHeroCarousel()
            }
            item {
                Column(
                    Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp),
                ) {
                    Text(stringResource(R.string.paywall_eyebrow), color = PaywallPremiumGold, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                    Text(
                        stringResource(R.string.paywall_pro_studio_title),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        lineHeight = 35.sp,
                    )
                    Text(
                        stringResource(R.string.paywall_pro_studio_subtitle),
                        color = PaywallTextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            item {
                Column(
                    Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PaywallOutcomeCard(stringResource(R.string.paywall_outcome_4k_title), stringResource(R.string.paywall_outcome_4k_body))
                    PaywallOutcomeCard(stringResource(R.string.paywall_outcome_watermark_title), stringResource(R.string.paywall_outcome_watermark_body))
                    PaywallOutcomeCard(stringResource(R.string.paywall_outcome_priority_title), stringResource(R.string.paywall_outcome_priority_body))
                }
            }
            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PaywallCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PaywallBorder),
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(stringResource(R.string.paywall_included), color = PaywallTextSecondary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                        FeatureRowOnDark(Icons.Rounded.AutoAwesome, stringResource(R.string.paywall_feature_generations))
                        FeatureRowOnDark(Icons.Rounded.Download, stringResource(R.string.paywall_feature_watermark))
                        FeatureRowOnDark(Icons.Rounded.Refresh, stringResource(R.string.paywall_feature_fast))
                        FeatureRowOnDark(Icons.Rounded.Save, stringResource(R.string.paywall_feature_history))
                    }
                }
            }
            item {
                Surface(
                    shape = CircleShape,
                    color = PaywallAccent.copy(alpha = 0.16f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PaywallAccent.copy(alpha = 0.45f)),
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null, tint = PaywallPremiumGold, modifier = Modifier.size(18.dp))
                        Text(stringResource(R.string.paywall_google_play_checkout), color = PaywallTextSecondary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            if (offeringsLoading) {
                item {
                    Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = PaywallPremiumGold, strokeWidth = 2.dp)
                            Text(stringResource(R.string.loading_prices), color = PaywallTextSecondary, style = MaterialTheme.typography.bodyMedium)
                        }
                        repeat(2) {
                            PaywallPlanSkeleton()
                        }
                    }
                }
            }
            if (!offeringsLoading && !pricesUnavailable) {
                items(availablePlans, key = { it.id }) { plan ->
                    PaywallPlanCard(
                        plan = plan,
                        selected = plan.id == selectedPlanModel?.id,
                        enabled = !purchaseBusy,
                        onClick = { selectedPlan = plan.id },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
            if (pricesUnavailable) {
                item {
                    PaywallPricesFallback(
                        message = message ?: resources.getString(R.string.load_prices_failed),
                        retrying = offeringsLoading,
                        onRetry = { loadOfferings() },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
            if (!pricesUnavailable && (message != null || state.purchaseMessage != null)) {
                item {
                    val displayMessage = message ?: state.purchaseMessage.orEmpty()
                    if (state.pendingPurchaseSync != null && message == null) {
                        PurchaseSyncNotice(
                            message = displayMessage,
                            pending = true,
                            busy = state.purchaseBusy,
                            onRetry = onRetrySync,
                        )
                    } else {
                        Text(displayMessage, color = PaywallPremiumGold, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 20.dp))
                    }
                }
            }
            item {
                Column(
                    Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = { buy(selectedPackage, selectedSubscriptionType, selectedEntitlement) },
                        enabled = !offeringsLoading && !purchaseBusy && selectedPackage != null,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PaywallPremiumGold,
                            contentColor = PaywallBg,
                            disabledContainerColor = PaywallDisabledButton,
                            disabledContentColor = PaywallDisabledText,
                        ),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                    ) {
                        Text(
                            when {
                                purchaseBusy -> stringResource(R.string.processing_ellipsis)
                                offeringsLoading -> stringResource(R.string.loading_ellipsis)
                                state.isPro -> stringResource(R.string.pro_activated)
                                selectedPackage != null -> stringResource(R.string.paywall_start_trial)
                                else -> stringResource(R.string.prices_unavailable)
                            },
                            fontWeight = FontWeight.Black,
                        )
                    }
                    Text(
                        if (pricesUnavailable) stringResource(R.string.paywall_restore_or_retry) else stringResource(R.string.paywall_cta_fine_print),
                        color = PaywallTextMuted,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            item {
                Surface(
                    onClick = onStore,
                    enabled = !purchaseBusy,
                    shape = RoundedCornerShape(16.dp),
                    color = PaywallCardAlt,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PaywallBorder),
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().heightIn(min = 58.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Surface(shape = CircleShape, color = PaywallAccent.copy(alpha = 0.20f)) {
                            Icon(Icons.Rounded.Diamond, null, Modifier.padding(9.dp).size(18.dp), tint = PaywallPremiumGold)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(stringResource(R.string.paywall_diamond_store_title), color = Color.White, fontWeight = FontWeight.Black)
                            Text(stringResource(R.string.paywall_diamond_store_subtitle), color = PaywallTextMuted, style = MaterialTheme.typography.labelMedium)
                        }
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = PaywallTextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = {
                            if (!Purchases.isConfigured) {
                                message = resources.getString(R.string.restore_unavailable)
                            } else {
                                restoring = true
                                message = null
                                Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
                                    override fun onReceived(customerInfo: CustomerInfo) {
                                        restoring = false
                                        val active = customerInfo.entitlements.active.values.firstOrNull()
                                        if (active != null) {
                                            val restoredType = when {
                                                active.identifier.contains("annual", ignoreCase = true) -> "yearly"
                                                active.identifier.contains("year", ignoreCase = true) -> "yearly"
                                                active.identifier.contains("week", ignoreCase = true) -> "weekly"
                                                else -> "monthly"
                                            }
                                            onSubscription("pro", restoredType, active.identifier, active.latestPurchaseDate.time.toDouble(), active.expirationDate?.time?.toDouble())
                                        } else {
                                            message = resources.getString(R.string.no_active_pro_purchase)
                                        }
                                    }

                                    override fun onError(error: PurchasesError) {
                                        restoring = false
                                        message = resources.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.restore_failed))
                                    }
                                })
                            }
                        },
                        enabled = !purchaseBusy,
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PaywallTextMuted),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Transparent),
                    ) {
                        Text(if (restoring) stringResource(R.string.restoring) else stringResource(R.string.restore_purchases))
                    }
                }
            }
        }
    }
}

data class PaywallPlan(
    val id: String,
    val label: String,
    val price: String,
    val detail: String,
    val badge: String?,
    val packageToPurchase: Package,
    val entitlement: String,
)

val PaywallHeroImages = listOf(
    R.drawable.assets_media_paywall_carouseljapandibedroom,
    R.drawable.assets_media_paywall_carouselluxurymarble,
    R.drawable.assets_media_paywall_paintintroblackmarblesalon,
)

@Composable
fun PaywallHeroCarousel() {
    var heroIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(3_000)
            heroIndex = (heroIndex + 1) % PaywallHeroImages.size
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(242.dp)
            .background(PaywallBg),
    ) {
        AnimatedContent(
            targetState = PaywallHeroImages[heroIndex],
            label = "paywallHero",
        ) { imageRes ->
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Box(Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, PaywallBg), startY = 140f)))
        Row(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            PaywallHeroImages.forEachIndexed { index, _ ->
                Box(
                    Modifier
                        .width(if (index == heroIndex) 18.dp else 7.dp)
                        .height(7.dp)
                        .clip(CircleShape)
                        .background(if (index == heroIndex) PaywallPremiumGold else Color.White.copy(alpha = 0.28f)),
                )
            }
        }
    }
}

@Composable
fun PaywallOutcomeCard(title: String, body: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = PaywallCardAlt,
        border = androidx.compose.foundation.BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier
                    .width(3.dp)
                    .height(48.dp)
                    .clip(CircleShape)
                    .background(PaywallPremiumGold),
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
                Text(body, color = PaywallTextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun PaywallPlanSkeleton() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PaywallCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, PaywallBorder),
        modifier = Modifier.fillMaxWidth().height(74.dp),
    ) {
        Row(Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = PaywallPremiumGold, strokeWidth = 2.dp)
            Spacer(Modifier.width(12.dp))
            Text(stringResource(R.string.loading_prices), color = PaywallTextSecondary)
        }
    }
}

@Composable
fun PaywallPlanCard(
    plan: PaywallPlan,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (selected) PaywallPremiumGold.copy(alpha = 0.85f) else PaywallBorder
    val backgroundColor = if (selected) PaywallPremiumGold.copy(alpha = 0.10f) else PaywallCard
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(if (plan.id == "yearly") 18.dp else 16.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(if (selected) 1.3.dp else 1.dp, borderColor),
        modifier = modifier.fillMaxWidth().heightIn(min = if (plan.id == "yearly") 96.dp else 72.dp),
    ) {
        Box(Modifier.fillMaxWidth()) {
            if (plan.badge != null) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PaywallPremiumGold.copy(alpha = 0.20f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PaywallPremiumGold.copy(alpha = 0.45f)),
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 10.dp, end = 14.dp),
                ) {
                    Text(
                        plan.badge,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        color = PaywallPremiumGold,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                    )
                }
            }
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Surface(shape = CircleShape, color = if (selected) PaywallPremiumGold else Color.White.copy(alpha = 0.12f)) {
                    Icon(
                        if (selected) Icons.Rounded.Check else Icons.Rounded.Star,
                        contentDescription = null,
                        tint = if (selected) PaywallBg else PaywallPremiumGold,
                        modifier = Modifier.padding(8.dp).size(18.dp),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(plan.label, color = if (plan.id == "weekly") PaywallTextMuted else PaywallTextSecondary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                    Text(plan.price, color = Color.White, style = if (plan.id == "yearly") MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(plan.detail, color = if (plan.id == "yearly") PaywallSuccess else PaywallTextMuted, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
fun PaywallPricesFallback(
    message: String,
    retrying: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(shape = CircleShape, color = StudioGold.copy(alpha = 0.18f)) {
                Icon(Icons.Rounded.Refresh, contentDescription = null, tint = StudioGold, modifier = Modifier.padding(9.dp).size(18.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(stringResource(R.string.prices_unavailable), color = Color.White, fontWeight = FontWeight.Black)
                Text(message, color = Color.White.copy(alpha = 0.74f), style = MaterialTheme.typography.bodySmall)
            }
            OutlinedButton(
                onClick = onRetry,
                enabled = !retrying,
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(if (retrying) stringResource(R.string.loading_ellipsis) else stringResource(R.string.retry), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PlanChoiceButton(
    title: String,
    price: String?,
    loading: Boolean,
    enabled: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = when {
        !enabled -> Color.White.copy(alpha = 0.62f)
        selected -> StudioInk
        else -> Color.White
    }
    val accessibilityPrice = when {
        loading -> stringResource(R.string.loading_ellipsis)
        price != null -> price
        else -> stringResource(R.string.unavailable)
    }
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        color = when {
            selected -> StudioGold
            enabled -> Color.White.copy(alpha = 0.12f)
            else -> Color.White.copy(alpha = 0.06f)
        },
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                selected -> StudioGold
                enabled -> Color.White.copy(alpha = 0.18f)
                else -> Color.White.copy(alpha = 0.10f)
            },
        ),
        modifier = modifier
            .minimumTouchTarget()
            .semantics {
                contentDescription = "$title, $accessibilityPrice"
                this.selected = selected
                role = Role.Button
            }
            .disabledSemantics(enabled),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(title, color = contentColor, fontWeight = FontWeight.Black, maxLines = 1)
            Text(
                when {
                    loading -> stringResource(R.string.loading_ellipsis)
                    price != null -> price
                    else -> stringResource(R.string.unavailable)
                },
                color = contentColor.copy(alpha = if (selected) 0.76f else 0.72f),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            )
        }
    }
}

