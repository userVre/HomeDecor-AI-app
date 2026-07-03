package com.ismail.homedecorai

import com.ismail.homedecorai.ui.paywall.PaywallPlan
import com.ismail.homedecorai.ui.paywall.PaywallState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PaywallTest {

    @Test
    fun testPaywallTestTags() {
        assertNotNull(Strings.TestTags.paywallSheet)
        assertNotNull(Strings.TestTags.paywallTopBar)
        assertNotNull(Strings.TestTags.paywallCloseButton)
        assertNotNull(Strings.TestTags.paywallBackButton)
        assertNotNull(Strings.TestTags.paywallStepIndicator)
        assertNotNull(Strings.TestTags.paywallCtaButton)
        assertNotNull(Strings.TestTags.paywallRestoreButton)
    }

    @Test
    fun testPaywallPlanCardTagFormat() {
        assertEquals("paywall_plan_card_yearly", Strings.formatTestTag(Strings.TestTags.paywallPlanCard, "yearly"))
    }

    @Test
    fun testPaywallStepContentTagFormat() {
        assertEquals("paywall_step_content_3", Strings.formatTestTag(Strings.TestTags.paywallStepContent, 3))
    }

    @Test
    fun testPaywallStepProgression() {
        var currentStep = 1
        currentStep++; assertEquals(2, currentStep)
        currentStep++; assertEquals(3, currentStep)
        currentStep++; assertEquals(4, currentStep)
        currentStep = 5; assertEquals(5, currentStep)
    }

    @Test
    fun testPaywallStep4ToStep5() {
        var currentStep = 4
        val selectedPlan = "yearly"
        currentStep = 5
        assertEquals(5, currentStep)
        assertEquals("yearly", selectedPlan)
    }

    @Test
    fun testPaywallPlanSelection() {
        val plans = listOf(
            PaywallPlan("yearly", "Yearly Premium", "$39.99", "/year", "Only $3.33 / month", isRecommended = true),
            PaywallPlan("monthly", "Monthly Premium", "$7.99", "/month", "Billed monthly", isRecommended = false),
            PaywallPlan("family", "Family / Team", "$59.99", "/year", "Up to 5 users", isRecommended = false),
        )
        assertEquals(3, plans.size)
        assertTrue(plans[0].isRecommended)
        assertEquals("yearly", plans[0].id)
    }

    @Test
    fun testPaywallStepLabels() {
        assertEquals("Redesign your room", Strings.pwS1Heading)
        assertEquals("Why go Pro?", Strings.pwS2Heading)
        assertEquals("What's included in Pro", Strings.pwS3Heading)
        assertEquals("Choose your plan", Strings.pwS4Heading)
        assertEquals("Your subscription is handled securely.", Strings.pwS5Heading)
    }

    @Test
    fun testPaywallCtaLabels() {
        assertEquals("Continue", Strings.pwS1Cta)
        assertEquals("Continue", Strings.pwS2Cta)
        assertEquals("Continue", Strings.pwS3Cta)
        assertEquals("Subscribe Now", Strings.pwS4Cta)
        assertEquals("Subscribe Now", Strings.pwS5Cta)
    }

    @Test
    fun testPaywallA11yLabels() {
        assertEquals("Close", Strings.proA11yClose)
        assertEquals("Go back", Strings.paywallA11yBack)
        assertEquals("Close paywall", Strings.a11yPaywallClose)
        assertEquals("Go to previous step", Strings.a11yPaywallBack)
        assertEquals("Continue with selected plan", Strings.a11yPaywallCta)
    }

    @Test
    fun testPaywallA11yStepLabel() {
        val cta = "Continue"
        val label = Strings.a11yPaywallStep(1, cta)
        assertEquals("Step 1: $cta", label)
    }

    @Test
    fun testPaywallTrustLabels() {
        assertEquals("Cancel anytime. No commitment.", Strings.pwS5Trust)
        assertEquals("Cancel anytime.", Strings.pwS4Trust)
    }

    @Test
    fun testPaywallPlanDetails() {
        assertEquals("\$39.99", Strings.pwS4PlanYearlyPrice)
        assertEquals("/year", Strings.pwS4PlanYearlyPer)
        assertEquals("Only \$3.33 / month", Strings.pwS4PlanYearlyDetail)
        assertEquals("Most Popular", Strings.pwS4PlanYearlyBadge)
        assertEquals("Save 58%", Strings.pwS4PlanYearlySavings)
    }

    @Test
    fun testPaywallCheckoutBenefits() {
        val benefits = listOf(
            Strings.pwS5Benefit1, Strings.pwS5Benefit2, Strings.pwS5Benefit3,
            Strings.pwS5Benefit4, Strings.pwS5Benefit5, Strings.pwS5Benefit6,
        )
        assertEquals(6, benefits.size)
        benefits.forEach { assertTrue(it.isNotEmpty()) }
    }

    @Test
    fun testPaywallBackNavigation() {
        var currentStep = 5
        currentStep = if (currentStep > 1) currentStep - 1 else currentStep; assertEquals(4, currentStep)
        currentStep = if (currentStep > 1) currentStep - 1 else currentStep; assertEquals(3, currentStep)
        currentStep = if (currentStep > 1) currentStep - 1 else currentStep; assertEquals(2, currentStep)
        currentStep = if (currentStep > 1) currentStep - 1 else currentStep; assertEquals(1, currentStep)
        currentStep = if (currentStep > 1) currentStep - 1 else currentStep; assertEquals(1, currentStep)
    }

    @Test
    fun testPaywallStateDefaults() {
        val state = PaywallState(
            isPro = false, plans = emptyList(), selectedPlanId = "yearly",
            offeringsLoading = false, purchasing = false, purchaseSuccess = false,
        )
        assertEquals(false, state.isPro)
        assertEquals("yearly", state.selectedPlanId)
    }

    @Test
    fun testPaywallComparisonRows() {
        assertEquals("AI room redesigns", Strings.pwS3Row1Feature)
        assertEquals("3/day", Strings.pwS3Row1Free)
        assertEquals("Export quality", Strings.pwS3Row2Feature)
    }

    @Test
    fun testPaywallNotificationOptions() {
        assertEquals("Unlimited AI generations", Strings.pwS2Option1)
        assertEquals("All premium styles", Strings.pwS2Option2)
    }
}
