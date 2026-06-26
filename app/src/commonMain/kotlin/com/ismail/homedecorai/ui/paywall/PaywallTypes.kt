package com.ismail.homedecorai.ui.paywall

data class PaywallPlan(
    val id: String,
    val label: String,
    val price: String?,
    val priceSuffix: String,
    val detail: String,
    val isRecommended: Boolean = false,
)

data class PaywallState(
    val isPro: Boolean,
    val plans: List<PaywallPlan>,
    val selectedPlanId: String,
    val offeringsLoading: Boolean,
    val purchasing: Boolean,
    val purchaseSuccess: Boolean,
)
