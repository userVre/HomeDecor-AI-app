package com.ismail.homedecorai.ui.paywall

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.R
import com.ismail.homedecorai.purchaseAttemptMessageRes
import com.ismail.homedecorai.rawServiceMessageToKind
import com.ismail.homedecorai.storeMessageRes
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

private val PlayfairSerif = FontFamily.Serif
private val DmSans = FontFamily.SansSerif

private val PlayfairHeadline = TextStyle(
    fontFamily = PlayfairSerif,
    fontWeight = FontWeight.Bold,
    fontStyle = FontStyle.Italic,
    fontSize = 24.sp,
    lineHeight = 30.sp,
)

private val DmSansBody = TextStyle(
    fontFamily = DmSans,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
)

private val DmSansBold = TextStyle(
    fontFamily = DmSans,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
)

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
        Purchases.sharedInstance.purchase(PurchaseParams.Builder(activity, packageToPurchase).build(), object : PurchaseCallback {
            override fun onCompleted(storeTransaction: StoreTransaction, customerInfo: CustomerInfo) {
                purchasing = false
                purchaseSuccess = true
                val active = customerInfo.entitlements.active.values.firstOrNull()
                onSubscription("pro", subscriptionType, active?.identifier ?: entitlement, active?.latestPurchaseDate?.time?.toDouble(), active?.expirationDate?.time?.toDouble())
            }
            override fun onError(error: PurchasesError, userCancelled: Boolean) {
                purchasing = false
            }
        })
    }

    val yearlyPackage = offering?.annual ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.ANNUAL }
    val weeklyPackage = offering?.weekly ?: offering?.availablePackages?.firstOrNull { it.packageType == PackageType.WEEKLY }
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

    val weeklyPrice = weeklyPackage?.product?.price?.formatted ?: "44.68 MAD"

    Box(
        Modifier
            .fillMaxSize()
            .background(PaywallBg)
            .clickable(interactionSource = modalTapBlocker, indication = null, onClick = {}),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(48.dp))

            TopBar(onClose = onClose)

            Spacer(Modifier.height(12.dp))

            HeadlineSection()

            Spacer(Modifier.height(12.dp))

            FeaturePillsRow()

            Spacer(Modifier.height(12.dp))

            ComparisonTableSection()

            Spacer(Modifier.height(12.dp))

            SectionLabel(stringResource(R.string.paywall_choose_plan))

            Spacer(Modifier.height(8.dp))

            AnnualPlanCard(
                selected = selectedPlan == "yearly",
                onClick = { selectedPlan = "yearly" },
            )

            Spacer(Modifier.height(8.dp))

            WeeklyPlanCard(
                price = weeklyPrice,
                selected = selectedPlan == "weekly",
                onClick = { selectedPlan = "weekly" },
            )

            Spacer(Modifier.height(10.dp))

            TrialBanner()

            Spacer(Modifier.height(10.dp))

            CtaButton(
                processing = purchaseBusy,
                success = purchaseSuccess,
                onClick = {
                    if (!purchaseBusy) {
                        val entitlement = when (selectedPlan) {
                            "yearly" -> "annual_pro"
                            "weekly" -> "weekly_pro"
                            else -> "monthly_pro"
                        }
                        buy(selectedPackage, selectedPlan, entitlement)
                    }
                },
            )

            Spacer(Modifier.height(8.dp))

            CancelRow()

            Spacer(Modifier.height(6.dp))

            BottomLinks(onRetrySync = onRetrySync)

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun TopBar(onClose: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.08f)),
        ) {
            Icon(Icons.Rounded.Close, contentDescription = null, tint = PaywallTextPrimary, modifier = Modifier.size(16.dp))
        }

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = PaywallAccent,
        ) {
            Row(
                Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.White)
                Text(
                    stringResource(R.string.paywall_upgrade_pro),
                    color = Color.White,
                    style = DmSansBold.copy(fontSize = 10.sp),
                    letterSpacing = 1.5.sp,
                )
            }
        }

        Spacer(Modifier.size(34.dp))
    }
}

@Composable
private fun HeadlineSection() {
    Column(Modifier.padding(horizontal = 22.dp)) {
        Text(
            stringResource(R.string.paywall_hero_title),
            style = PlayfairHeadline,
            color = PaywallTextPrimary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(R.string.paywall_hero_subtitle),
            style = DmSansBody.copy(fontSize = 13.sp, lineHeight = 18.sp),
            color = PaywallTextSecondary,
        )
    }
}

@Composable
private fun FeaturePillsRow() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FeaturePill("\uD83D\uDDBC\uFE0F", stringResource(R.string.paywall_feat_4k), stringResource(R.string.paywall_feat_4k_sub), Modifier.weight(1f))
        FeaturePill("\u221E", stringResource(R.string.paywall_feat_unlimited), stringResource(R.string.paywall_feat_unlimited_sub), Modifier.weight(1f))
        FeaturePill("\u26A1", stringResource(R.string.paywall_feat_priority), stringResource(R.string.paywall_feat_priority_sub), Modifier.weight(1f))
        FeaturePill("\uD83D\uDCC2", stringResource(R.string.paywall_feat_portfolio), stringResource(R.string.paywall_feat_portfolio_sub), Modifier.weight(1f))
    }
}

@Composable
private fun FeaturePill(emoji: String, title: String, subtitle: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = PaywallCard,
        border = BorderStroke(1.5.dp, PaywallBorder),
        modifier = modifier,
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Surface(shape = RoundedCornerShape(8.dp), color = PaywallGoldTint, modifier = Modifier.size(32.dp)) {
                Text(emoji, modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center, style = TextStyle(fontSize = 16.sp))
            }
            Column(Modifier.weight(1f, fill = false)) {
                Text(title, style = DmSansBold.copy(fontSize = 11.sp, lineHeight = 13.sp), color = PaywallTextPrimary, maxLines = 1)
                Text(subtitle, style = DmSansBody.copy(fontSize = 10.sp, lineHeight = 12.sp), color = PaywallTextMuted, maxLines = 1)
            }
        }
    }
}

@Composable
private fun ComparisonTableSection() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PaywallCard,
        border = BorderStroke(1.dp, PaywallBorder),
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
    ) {
        Column {
            Row(
                Modifier
                    .background(PaywallCardAlt)
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.paywall_cmp_feature).uppercase(), style = DmSansBold.copy(fontSize = 10.sp, letterSpacing = 1.sp), color = PaywallTextMuted, modifier = Modifier.weight(1f))
                Text(stringResource(R.string.paywall_cmp_free).uppercase(), style = DmSansBold.copy(fontSize = 10.sp), color = PaywallTextMuted, modifier = Modifier.width(65.dp), textAlign = TextAlign.Center)
                Text(stringResource(R.string.paywall_cmp_pro).uppercase(), style = DmSansBold.copy(fontSize = 10.sp), color = PaywallAccent, modifier = Modifier.width(65.dp), textAlign = TextAlign.Center)
            }
            ComparisonRow(stringResource(R.string.paywall_cmp_renders), "3", stringResource(R.string.paywall_cmp_unlimited))
            ComparisonRow(stringResource(R.string.paywall_cmp_export), "HD", stringResource(R.string.paywall_cmp_4k))
            ComparisonRow(stringResource(R.string.paywall_cmp_watermark), stringResource(R.string.paywall_cmp_yes), stringResource(R.string.paywall_cmp_none))
            ComparisonRow(stringResource(R.string.paywall_cmp_ai), stringResource(R.string.paywall_cmp_standard), stringResource(R.string.paywall_cmp_priority_pro))
        }
    }
}

@Composable
private fun ComparisonRow(feature: String, freeValue: String, proValue: String) {
    Row(
        Modifier
            .border(1.dp, Color(0xFFF0E8D8))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(feature, style = DmSansBody.copy(fontSize = 12.sp), color = PaywallTextSecondary, modifier = Modifier.weight(1f))
        Text(freeValue, style = DmSansBody.copy(fontSize = 12.sp), color = PaywallTextMuted, modifier = Modifier.width(65.dp), textAlign = TextAlign.Center)
        Text(proValue, style = DmSansBold.copy(fontSize = 12.sp), color = PaywallGreen, modifier = Modifier.width(65.dp), textAlign = TextAlign.Center)
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = DmSansBold.copy(fontSize = 10.sp, letterSpacing = 2.sp),
        color = PaywallTextMuted,
        modifier = Modifier.padding(horizontal = 22.dp),
    )
}

@Composable
private fun AnnualPlanCard(selected: Boolean, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.22f,
        targetValue = 0.42f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Reverse),
        label = "glow",
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PaywallCard,
        border = BorderStroke(2.dp, PaywallAccent),
        shadowElevation = 20.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .clickable(onClick = onClick),
    ) {
        Box {
            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(PaywallAccent.copy(alpha = glowAlpha), Color.Transparent),
                            center = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                            radius = 600f,
                        ),
                    ),
            )
            Column(Modifier.padding(13.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(R.string.paywall_plan_annual).uppercase(), style = DmSansBold.copy(fontSize = 10.sp, letterSpacing = 1.5.sp), color = PaywallAccent)
                    Surface(shape = RoundedCornerShape(20.dp), color = PaywallAccent) {
                        Text(stringResource(R.string.paywall_best_value), modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), color = Color.White, style = DmSansBold.copy(fontSize = 9.5.sp))
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        "4.78",
                        style = TextStyle(fontFamily = PlayfairSerif, fontWeight = FontWeight.Bold, fontSize = 42.sp, lineHeight = 44.sp),
                        color = PaywallTextPrimary,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("MAD / week", style = DmSansBody.copy(fontSize = 12.sp), color = PaywallTextMuted, modifier = Modifier.padding(bottom = 4.dp))
                }
                Text(stringResource(R.string.paywall_annual_detail), style = DmSansBody.copy(fontSize = 11.sp), color = PaywallTextMuted)
                Spacer(Modifier.height(8.dp))
                Surface(shape = RoundedCornerShape(20.dp), color = PaywallGreenBg, border = BorderStroke(1.dp, PaywallGreenBorder)) {
                    Text(stringResource(R.string.paywall_save_badge), modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), color = PaywallGreen, style = DmSansBold.copy(fontSize = 10.5.sp))
                }
            }
        }
    }
}

@Composable
private fun WeeklyPlanCard(price: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) PaywallGoldTint else PaywallCard,
        border = BorderStroke(1.5.dp, if (selected) PaywallAccent else PaywallBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.paywall_plan_weekly).uppercase(), style = DmSansBold.copy(fontSize = 10.sp, letterSpacing = 1.5.sp), color = PaywallTextSecondary)
                Spacer(Modifier.height(2.dp))
                Text(stringResource(R.string.paywall_weekly_detail), style = DmSansBody.copy(fontSize = 11.sp), color = PaywallTextMuted)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(price, style = TextStyle(fontFamily = PlayfairSerif, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 26.sp), color = PaywallTextPrimary)
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.paywall_weekly_currency), style = DmSansBody.copy(fontSize = 11.sp), color = PaywallTextMuted)
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(2.dp, if (selected) PaywallAccent else PaywallBorder, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(PaywallAccent))
                    }
                }
            }
        }
    }
}

@Composable
private fun TrialBanner() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = PaywallGreenBg,
        border = BorderStroke(1.dp, PaywallGreenBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("\uD83C\uDF81", style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.paywall_trial_note), style = DmSansBody.copy(fontSize = 13.sp, lineHeight = 17.sp, fontWeight = FontWeight.Medium), color = Color(0xFF1B5E20))
        }
    }
}

@Composable
private fun CtaButton(processing: Boolean, success: Boolean, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing)),
        label = "shimmerX",
    )
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.97f else 1f, label = "scale")

    val buttonColor = when {
        success -> PaywallGreen
        else -> Color.Transparent
    }
    val textColor = when {
        success -> Color.White
        else -> PaywallTextPrimary
    }

    Button(
        onClick = { if (!processing) onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = textColor,
            disabledContainerColor = buttonColor,
        ),
        contentPadding = PaddingValues(),
        enabled = !processing,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .height(52.dp)
            .scale(scale)
            .graphicsLayer { shadowElevation = 28f },
    ) {
        Box(Modifier.fillMaxSize()) {
            if (!success) {
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.linearGradient(listOf(PaywallGoldLight, PaywallAccent, StudioBrownDark)),
                        ),
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.32f), Color.Transparent),
                                start = Offset(1000f * shimmerX, 0f),
                                end = Offset(1000f * shimmerX + 400f, 0f),
                            ),
                        ),
                )
            }
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (processing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = textColor, strokeWidth = 2.5.dp)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.paywall_cta_processing), style = DmSansBold.copy(fontSize = 15.sp), color = textColor)
                } else if (success) {
                    Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.paywall_cta_success), style = DmSansBold.copy(fontSize = 15.sp), color = Color.White)
                } else {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = textColor)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.paywall_cta_start), style = DmSansBold.copy(fontSize = 15.sp), color = textColor)
                }
            }
        }
    }
}

@Composable
private fun CancelRow() {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("\u2713 ", color = PaywallGreen, style = DmSansBold.copy(fontSize = 12.sp))
        Text(stringResource(R.string.paywall_cancel_anytime).removePrefix("\u2713 "), color = PaywallTextMuted, style = DmSansBody.copy(fontSize = 12.sp))
    }
}

@Composable
private fun BottomLinks(onRetrySync: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
    ) {
        Text(stringResource(R.string.paywall_restore), color = PaywallTextMuted, style = DmSansBody.copy(fontSize = 11.sp, textDecoration = TextDecoration.Underline), modifier = Modifier.clickable {
            onRetrySync()
        })
        Text("\u00B7", color = PaywallTextMuted, style = DmSansBody.copy(fontSize = 11.sp))
        Text(stringResource(R.string.terms), color = PaywallTextMuted, style = DmSansBody.copy(fontSize = 11.sp, textDecoration = TextDecoration.Underline))
        Text("\u00B7", color = PaywallTextMuted, style = DmSansBody.copy(fontSize = 11.sp))
        Text(stringResource(R.string.privacy_policy), color = PaywallTextMuted, style = DmSansBody.copy(fontSize = 11.sp, textDecoration = TextDecoration.Underline))
    }
}
