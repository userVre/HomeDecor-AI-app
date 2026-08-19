package com.ismail.homedecorai.whop

import io.github.cdimascio.dotenv.dotenv

/**
 * Whop payment processor configuration.
 *
 * Values are loaded from the .env file in the project root.
 * Replace YOUR_WHOP_API_KEY with your real key in .env before deployment.
 */
object WhopConfig {

    private val dotenv = dotenv {
        ignoreIfMissing = true
    }

    // ── API ──────────────────────────────────────────────────────────────
    val API_KEY: String = dotenv["WHOP_API_KEY"] ?: error("WHOP_API_KEY is not set in .env")
    const val BASE_URL: String = "https://api.whop.com/api/v5"

    // ── Product & Plan IDs ───────────────────────────────────────────────
    val PRODUCT_ID: String = dotenv["WHOP_PRODUCT_ID"] ?: "prod_D4BOQ5mBcS8EX"

    // Essential
    val ESSENTIAL_MONTHLY_PLAN_ID: String = dotenv["WHOP_ESSENTIAL_MONTHLY_PLAN_ID"] ?: "plan_01R6HzQwtM2sN"
    val ESSENTIAL_YEARLY_PLAN_ID: String = dotenv["WHOP_ESSENTIAL_YEARLY_PLAN_ID"] ?: "plan_rJLH5wJVSrZzq"

    // Pro
    val PRO_MONTHLY_PLAN_ID: String = dotenv["WHOP_PRO_MONTHLY_PLAN_ID"] ?: "plan_gFcWfrHIqYPuY"
    val PRO_YEARLY_PLAN_ID: String = dotenv["WHOP_PRO_YEARLY_PLAN_ID"] ?: "plan_bOQkU9fDeBx45"

    // Studio
    val STUDIO_MONTHLY_PLAN_ID: String = dotenv["WHOP_STUDIO_MONTHLY_PLAN_ID"] ?: "plan_mcNP3brC8NK4w"
    val STUDIO_YEARLY_PLAN_ID: String = dotenv["WHOP_STUDIO_YEARLY_PLAN_ID"] ?: "plan_lrUcmLZUyyNOW"

    // Agency
    val AGENCY_MONTHLY_PLAN_ID: String = dotenv["WHOP_AGENCY_MONTHLY_PLAN_ID"] ?: "plan_LlmFu3YvuXbHx"
    val AGENCY_YEARLY_PLAN_ID: String = dotenv["WHOP_AGENCY_YEARLY_PLAN_ID"] ?: "plan_dMjSL0Dy9T2nn"

    // ── Checkout URLs ────────────────────────────────────────────────────
    val CHECKOUT_URL_ESSENTIAL_MONTHLY: String = "https://whop.com/checkout/$ESSENTIAL_MONTHLY_PLAN_ID/"
    val CHECKOUT_URL_ESSENTIAL_YEARLY: String = "https://whop.com/checkout/$ESSENTIAL_YEARLY_PLAN_ID/"
    val CHECKOUT_URL_PRO_MONTHLY: String = "https://whop.com/checkout/$PRO_MONTHLY_PLAN_ID/"
    val CHECKOUT_URL_PRO_YEARLY: String = "https://whop.com/checkout/$PRO_YEARLY_PLAN_ID/"
    val CHECKOUT_URL_STUDIO_MONTHLY: String = "https://whop.com/checkout/$STUDIO_MONTHLY_PLAN_ID/"
    val CHECKOUT_URL_STUDIO_YEARLY: String = "https://whop.com/checkout/$STUDIO_YEARLY_PLAN_ID/"
    val CHECKOUT_URL_AGENCY_MONTHLY: String = "https://whop.com/checkout/$AGENCY_MONTHLY_PLAN_ID/"
    val CHECKOUT_URL_AGENCY_YEARLY: String = "https://whop.com/checkout/$AGENCY_YEARLY_PLAN_ID/"

    // Legacy aliases
    val CHECKOUT_URL_MONTHLY: String = CHECKOUT_URL_PRO_MONTHLY
    val CHECKOUT_URL_YEARLY: String = CHECKOUT_URL_PRO_YEARLY
    val CHECKOUT_URL_PRO: String = CHECKOUT_URL_PRO_MONTHLY

    // ── Webhook ──────────────────────────────────────────────────────────
    const val WEBHOOK_PATH: String = "/webhooks/whop"
    val WEBHOOK_URL: String = (dotenv["YOUR_WEBSITE_URL"] ?: "YOUR_WEBSITE_URL") + WEBHOOK_PATH

    // ── Webhook event types ──────────────────────────────────────────────
    object WebhookEvents {
        const val PAYMENT_SUCCEEDED = "payment.succeeded"
        const val MEMBERSHIP_WENT_VALID = "membership.went_valid"
        const val MEMBERSHIP_WENT_INVALID = "membership.went_invalid"
    }
}
