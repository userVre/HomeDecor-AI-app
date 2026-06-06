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
import com.ismail.homedecorai.ui.components.PurchaseSyncNotice
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
            .background(Color.White)
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
                .background(HomeDecorColors.Ink.copy(alpha = 0.08f)),
        ) {
            Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close), tint = HomeDecorColors.Ink)
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
                    Text(stringResource(R.string.paywall_eyebrow), color = HomeDecorColors.Accent, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                    Text(
                        stringResource(R.string.paywall_pro_studio_title),
                        color = HomeDecorColors.Ink,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        lineHeight = 35.sp,
                    )
                    Text(
                        stringResource(R.string.paywall_pro_studio_subtitle),
                        color = HomeDecorColors.InkSoft,
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
                    color = HomeDecorColors.Canvas,
                    border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Line),
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(stringResource(R.string.paywall_included), color = HomeDecorColors.InkSoft, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                        FeatureRow(Icons.Rounded.AutoAwesome, stringResource(R.string.paywall_feature_generations))
                        FeatureRow(Icons.Rounded.Download, stringResource(R.string.paywall_feature_watermark))
                        FeatureRow(Icons.Rounded.Refresh, stringResource(R.string.paywall_feature_fast))
                        FeatureRow(Icons.Rounded.Save, stringResource(R.string.paywall_feature_history))
                    }
                }
            }
            item {
                Surface(
                    shape = CircleShape,
                    color = HomeDecorColors.AccentContainer.copy(alpha = 0.4f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Accent.copy(alpha = 0.25f)),
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null, tint = HomeDecorColors.Accent, modifier = Modifier.size(18.dp))
                        Text(stringResource(R.string.paywall_google_play_checkout), color = HomeDecorColors.InkSoft, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
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
                        Text(displayMessage, color = HomeDecorColors.Accent, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 20.dp))
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
                            containerColor = HomeDecorColors.Accent,
                            contentColor = Color.White,
                            disabledContainerColor = HomeDecorColors.DisabledDarkButton,
                            disabledContentColor = HomeDecorColors.DisabledDarkText,
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
                        color = HomeDecorColors.InkSoft,
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
                    color = HomeDecorColors.Canvas,
                    border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Line),
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().heightIn(min = 58.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Surface(shape = CircleShape, color = HomeDecorColors.AccentContainer) {
                            Icon(Icons.Rounded.Diamond, null, Modifier.padding(9.dp).size(18.dp), tint = HomeDecorColors.Accent)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(stringResource(R.string.paywall_diamond_store_title), color = HomeDecorColors.Ink, fontWeight = FontWeight.Black)
                            Text(stringResource(R.string.paywall_diamond_store_subtitle), color = HomeDecorColors.InkSoft, style = MaterialTheme.typography.labelMedium)
                        }
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = HomeDecorColors.InkSoft, modifier = Modifier.size(18.dp))
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
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HomeDecorColors.InkSoft),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Line),
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
        color = HomeDecorColors.Canvas,
        border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Line),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier
                    .width(3.dp)
                    .height(48.dp)
                    .clip(CircleShape)
                    .background(HomeDecorColors.Accent),
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.weight(1f)) {
                Text(title, color = HomeDecorColors.Ink, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
                Text(body, color = HomeDecorColors.InkSoft, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun PaywallPlanSkeleton() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = HomeDecorColors.Canvas,
        border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Line),
        modifier = Modifier.fillMaxWidth().height(74.dp),
    ) {
        Row(Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = HomeDecorColors.Accent, strokeWidth = 2.dp)
            Spacer(Modifier.width(12.dp))
            Text(stringResource(R.string.loading_prices), color = HomeDecorColors.InkSoft)
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
    val borderColor = if (selected) HomeDecorColors.Accent else HomeDecorColors.Line
    val backgroundColor = if (selected) HomeDecorColors.AccentContainer.copy(alpha = 0.3f) else HomeDecorColors.Canvas
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
                    color = HomeDecorColors.AccentContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Accent.copy(alpha = 0.45f)),
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 10.dp, end = 14.dp),
                ) {
                    Text(
                        plan.badge,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        color = HomeDecorColors.Accent,
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
                Surface(shape = CircleShape, color = if (selected) HomeDecorColors.Accent else HomeDecorColors.Mist) {
                    Icon(
                        if (selected) Icons.Rounded.Check else Icons.Rounded.Star,
                        contentDescription = null,
                        tint = if (selected) Color.White else HomeDecorColors.Accent,
                        modifier = Modifier.padding(8.dp).size(18.dp),
                    )
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(plan.label, color = if (plan.id == "weekly") HomeDecorColors.InkSoft else HomeDecorColors.Ink, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
                    Text(plan.price, color = HomeDecorColors.Ink, style = if (plan.id == "yearly") MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    Text(plan.detail, color = if (plan.id == "yearly") HomeDecorColors.Accent else HomeDecorColors.InkSoft, style = MaterialTheme.typography.labelMedium)
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
        color = HomeDecorColors.ErrorContainerColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.ErrorColor.copy(alpha = 0.24f)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(shape = CircleShape, color = HomeDecorColors.ErrorColor.copy(alpha = 0.18f)) {
                Icon(Icons.Rounded.Refresh, contentDescription = null, tint = HomeDecorColors.ErrorColor, modifier = Modifier.padding(9.dp).size(18.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(stringResource(R.string.prices_unavailable), color = HomeDecorColors.Ink, fontWeight = FontWeight.Black)
                Text(message, color = HomeDecorColors.InkSoft, style = MaterialTheme.typography.bodySmall)
            }
            OutlinedButton(
                onClick = onRetry,
                enabled = !retrying,
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HomeDecorColors.Ink),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, HomeDecorColors.Line),
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

