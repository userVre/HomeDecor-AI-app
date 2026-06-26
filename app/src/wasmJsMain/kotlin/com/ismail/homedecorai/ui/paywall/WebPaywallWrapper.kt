package com.ismail.homedecorai.ui.paywall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.openUrl
import com.ismail.homedecorai.ui.upgrade.SharedUpgradeScreen

@Composable
fun WebPaywallWrapper(
    isPro: Boolean,
    onClose: () -> Unit,
) {
    val state = remember {
        PaywallState(
            isPro = isPro,
            plans = listOf(
                PaywallPlan(
                    id = "yearly",
                    label = Strings.paywallV3PlanYearly,
                    price = "$39.99",
                    priceSuffix = Strings.paywallV3PlanPerYear,
                    detail = Strings.paywallV3PlanAnnualDetail,
                    isRecommended = true,
                ),
                PaywallPlan(
                    id = "monthly",
                    label = Strings.paywallV3PlanMonthly,
                    price = "$7.99",
                    priceSuffix = Strings.paywallV3PlanPerMonth,
                    detail = Strings.paywallV3PlanMonthlyDetail,
                    isRecommended = false,
                ),
                PaywallPlan(
                    id = "family",
                    label = Strings.paywallV3PlanFamily,
                    price = "$59.99",
                    priceSuffix = Strings.paywallV3PlanPerYear,
                    detail = Strings.paywallV3PlanFamilyDetail,
                    isRecommended = false,
                ),
            ),
            selectedPlanId = "yearly",
            offeringsLoading = false,
            purchasing = false,
            purchaseSuccess = false,
        )
    }

    var selectedPlan by remember { mutableStateOf("yearly") }

    SharedPaywallSheet(
        state = state.copy(selectedPlanId = selectedPlan),
        onClose = onClose,
        onPlanSelected = { selectedPlan = it },
        onContinue = {
            openUrl("https://homedecor-ai.com/waitlist")
        },
        onRestore = {
            openUrl("https://homedecor-ai.com/support")
        },
        ctaLabel = Strings.paywallV3JoinWaitlist,
        showRestore = false,
    )
}

@Composable
fun WebUpgradeWrapper(
    isPro: Boolean,
    onOpenPaywall: () -> Unit,
) {
    SharedUpgradeScreen(
        isPro = isPro,
        onOpenPaywall = onOpenPaywall,
    )
}
