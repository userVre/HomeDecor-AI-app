package com.ismail.homedecorai.ui.paywall

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Compare
import androidx.compose.material.icons.rounded.HighQuality
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.WaterDrop
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
import androidx.compose.ui.graphics.Color
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

            Spacer(Modifier.height(24.dp))

            HeadlineSection()

            Spacer(Modifier.height(28.dp))

            BenefitsSection()

            Spacer(Modifier.height(28.dp))

            PlanSelectorSection(
                selectedPlan = selectedPlan,
                weeklyPrice = weeklyPrice,
                yearlyPackage = yearlyPackage,
                onPlanSelected = { selectedPlan = it },
            )

            Spacer(Modifier.height(16.dp))

            TrialNote()

            Spacer(Modifier.height(20.dp))

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

            Spacer(Modifier.height(12.dp))

            CancelNote()

            Spacer(Modifier.height(20.dp))

            BottomLinks(onRetrySync = onRetrySync)

            Spacer(Modifier.height(32.dp))
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
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = stringResource(R.string.paywall_a11y_close),
                tint = PaywallTextMuted,
                modifier = Modifier.size(22.dp),
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = PaywallAccentSurface,
            border = BorderStroke(1.dp, PaywallBorder),
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
                    tint = PaywallAccent,
                )
                Text(
                    stringResource(R.string.paywall_upgrade_pro),
                    color = PaywallAccent,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                    ),
                )
            }
        }

        Spacer(Modifier.size(48.dp))
    }
}

@Composable
private fun HeadlineSection() {
    Column(
        Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.paywall_hero_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                letterSpacing = (-0.5).sp,
            ),
            color = PaywallTextPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.paywall_hero_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = PaywallTextSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun BenefitsSection() {
    Column(
        Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        BenefitRow(
            icon = Icons.Rounded.HighQuality,
            title = stringResource(R.string.paywall_feat_4k),
            subtitle = stringResource(R.string.paywall_feat_4k_sub),
        )
        BenefitRow(
            icon = Icons.Rounded.WaterDrop,
            title = stringResource(R.string.paywall_feat_unlimited),
            subtitle = stringResource(R.string.paywall_feat_unlimited_sub),
        )
        BenefitRow(
            icon = Icons.Rounded.Speed,
            title = stringResource(R.string.paywall_feat_priority),
            subtitle = stringResource(R.string.paywall_feat_priority_sub),
        )
    }
}

@Composable
private fun BenefitRow(icon: ImageVector, title: String, subtitle: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = PaywallCard,
        border = BorderStroke(1.dp, PaywallBorder),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = PaywallAccentSurface,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = PaywallAccent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(9.dp),
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    color = PaywallTextPrimary,
                )
                Spacer(Modifier.height(1.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = PaywallTextMuted,
                )
            }
        }
    }
}

@Composable
private fun PlanSelectorSection(
    selectedPlan: String,
    weeklyPrice: String,
    yearlyPackage: Package?,
    onPlanSelected: (String) -> Unit,
) {
    Column(
        Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            stringResource(R.string.paywall_choose_plan),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
            ),
            color = PaywallTextMuted,
        )

        AnnualPlanCard(
            selected = selectedPlan == "yearly",
            onClick = { onPlanSelected("yearly") },
        )

        WeeklyPlanCard(
            price = weeklyPrice,
            selected = selectedPlan == "weekly",
            onClick = { onPlanSelected("weekly") },
        )
    }
}

@Composable
private fun AnnualPlanCard(selected: Boolean, onClick: () -> Unit) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) PaywallAccent else PaywallBorder,
        animationSpec = tween(200),
        label = "annualBorder",
    )
    val annualPlanDescription = stringResource(R.string.paywall_a11y_annual_plan)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) PaywallAccentSurface else PaywallCard,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = borderColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                role = RadioButton
                this.selected = selected
                contentDescription = annualPlanDescription
            }
            .clickable(onClick = onClick),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.paywall_plan_annual),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                    ),
                    color = if (selected) PaywallAccent else PaywallTextSecondary,
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PaywallSuccessSurface,
                ) {
                    Text(
                        stringResource(R.string.paywall_best_value),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = PaywallSuccess,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "4.78",
                    style = MaterialTheme.typography.displaySmall.copy(
                        letterSpacing = (-1).sp,
                    ),
                    color = PaywallTextPrimary,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    stringResource(R.string.paywall_per_week),
                    style = MaterialTheme.typography.bodyMedium,
                    color = PaywallTextMuted,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                stringResource(R.string.paywall_annual_detail),
                style = MaterialTheme.typography.bodySmall,
                color = PaywallTextMuted,
            )

            Spacer(Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = PaywallSuccessSurface,
            ) {
                Text(
                    stringResource(R.string.paywall_save_badge),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = PaywallSuccess,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
    }
}

@Composable
private fun WeeklyPlanCard(price: String, selected: Boolean, onClick: () -> Unit) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) PaywallAccent else PaywallBorder,
        animationSpec = tween(200),
        label = "weeklyBorder",
    )
    val weeklyPlanDescription = stringResource(R.string.paywall_a11y_weekly_plan)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) PaywallAccentSurface else PaywallCard,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = borderColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                role = RadioButton
                this.selected = selected
                contentDescription = weeklyPlanDescription
            }
            .clickable(onClick = onClick),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.paywall_plan_weekly),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                    ),
                    color = if (selected) PaywallAccent else PaywallTextSecondary,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    stringResource(R.string.paywall_weekly_detail),
                    style = MaterialTheme.typography.bodySmall,
                    color = PaywallTextMuted,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    price,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = PaywallTextPrimary,
                )

                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (selected) PaywallAccent else Color.Transparent)
                        .then(
                            if (!selected) {
                                Modifier.border(2.dp, PaywallBorderStrong, RoundedCornerShape(11.dp))
                            } else Modifier
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
}

@Composable
private fun TrialNote() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            Icons.Rounded.Compare,
            contentDescription = null,
            tint = PaywallAccent,
            modifier = Modifier.size(18.dp),
        )
        Text(
            stringResource(R.string.paywall_trial_note),
            style = MaterialTheme.typography.bodyMedium,
            color = PaywallTextSecondary,
        )
    }
}

@Composable
private fun CtaButton(processing: Boolean, success: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = { if (!processing) onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                success -> PaywallSuccess
                else -> PaywallAccent
            },
            contentColor = Color.White,
            disabledContainerColor = when {
                success -> PaywallSuccess
                else -> PaywallAccent.copy(alpha = 0.6f)
            },
            disabledContentColor = Color.White,
        ),
        contentPadding = PaddingValues(),
        enabled = !processing,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(52.dp),
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
                Text(
                    stringResource(R.string.paywall_cta_processing),
                    style = MaterialTheme.typography.titleSmall,
                )
            } else if (success) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    stringResource(R.string.paywall_cta_success),
                    style = MaterialTheme.typography.titleSmall,
                )
            } else {
                Text(
                    stringResource(R.string.paywall_cta_start),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

@Composable
private fun CancelNote() {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Rounded.Check,
            contentDescription = null,
            tint = PaywallSuccess,
            modifier = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            stringResource(R.string.paywall_cancel_anytime),
            color = PaywallTextMuted,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun BottomLinks(onRetrySync: () -> Unit) {
    val restoreDescription = stringResource(R.string.a11y_restore_link)
    val termsDescription = stringResource(R.string.a11y_terms_link)
    val privacyDescription = stringResource(R.string.a11y_privacy_link)
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
    ) {
        Text(
            stringResource(R.string.paywall_restore),
            color = PaywallTextMuted,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier
                .semantics {
                    contentDescription = restoreDescription
                    role = Role.Button
                }
                .clickable { onRetrySync() },
        )
        Text(
            "\u00B7",
            color = PaywallBorderStrong,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            stringResource(R.string.terms),
            color = PaywallTextMuted,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier
                .semantics {
                    contentDescription = termsDescription
                    role = Role.Button
                }
                .clickable { },
        )
        Text(
            "\u00B7",
            color = PaywallBorderStrong,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            stringResource(R.string.privacy_policy),
            color = PaywallTextMuted,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier
                .semantics {
                    contentDescription = privacyDescription
                    role = Role.Button
                }
                .clickable { },
        )
    }
}
