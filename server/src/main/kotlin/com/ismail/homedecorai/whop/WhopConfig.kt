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
    val YEARLY_PLAN_ID: String = dotenv["WHOP_YEARLY_PLAN_ID"] ?: "plan_FCAl4pKNoaC3X"
    val PRO_PLAN_ID: String = dotenv["WHOP_PRO_PLAN_ID"] ?: "plan_cBvc9Qkwr476O"
    val MONTHLY_PLAN_ID: String = dotenv["WHOP_MONTHLY_PLAN_ID"] ?: "plan_MONTHLY_PLAN_ID"

    // ── Checkout URLs ────────────────────────────────────────────────────
    val CHECKOUT_URL_MONTHLY: String = "https://whop.com/checkout/$PRODUCT_ID/"
    val CHECKOUT_URL_YEARLY: String = "https://whop.com/checkout/$YEARLY_PLAN_ID/"
    val CHECKOUT_URL_PRO: String = "https://whop.com/checkout/$PRO_PLAN_ID/"

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
