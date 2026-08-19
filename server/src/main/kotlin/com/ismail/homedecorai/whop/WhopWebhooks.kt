package com.ismail.homedecorai.whop

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("WhopWebhooks")

/**
 * Registers the Whop webhook endpoint at [WhopConfig.WEBHOOK_PATH].
 *
 * Handles:
 * - payment.succeeded       → grant access
 * - membership.went_valid   → keep access active
 * - membership.went_invalid → revoke access
 *
 * Replace the grant/revoke stubs with your actual database calls.
 */
fun Route.whopWebhooks(userAccessStore: UserAccessStore) {

    post(WhopConfig.WEBHOOK_PATH) {
        val rawBody = call.receiveText()

        // Parse the webhook payload
        val payload = try {
            Gson().fromJson(rawBody, WebhookPayload::class.java)
        } catch (e: Exception) {
            logger.error("Failed to parse webhook payload: ${e.message}")
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid JSON"))
            return@post
        }

        val eventType = payload.eventType
        logger.info("Received Whop webhook: event=$eventType, id=${payload.id}")

        // Optional: verify signature if you have a signing secret configured
        // val signature = call.request.headers["X-Whop-Signature"]
        // if (!whopService.verifyWebhookSignature(rawBody.toByteArray(), signature ?: "")) {
        //     call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid signature"))
        //     return@post
        // }

        try {
            when (eventType) {
                WhopConfig.WebhookEvents.PAYMENT_SUCCEEDED -> {
                    handlePaymentSucceeded(payload, userAccessStore)
                }
                WhopConfig.WebhookEvents.MEMBERSHIP_WENT_VALID -> {
                    handleMembershipWentValid(payload, userAccessStore)
                }
                WhopConfig.WebhookEvents.MEMBERSHIP_WENT_INVALID -> {
                    handleMembershipWentInvalid(payload, userAccessStore)
                }
                else -> {
                    logger.warn("Unhandled Whop event type: $eventType")
                }
            }

            call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
        } catch (e: Exception) {
            logger.error("Error processing webhook ${payload.id}: ${e.message}", e)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal error"))
        }
    }
}

// ── Event Handlers ──────────────────────────────────────────────────────

private suspend fun handlePaymentSucceeded(
    payload: WebhookPayload,
    store: UserAccessStore
) {
    val data = payload.data
    val userId = data?.userId ?: data?.membership?.userId
    if (userId == null) {
        logger.warn("payment.succeeded: no userId in payload ${payload.id}")
        return
    }

    logger.info("Granting access for user $userId after successful payment")
    store.grantAccess(
        userId = userId,
        membershipId = data?.membership?.id,
        planId = data?.membership?.plan?.id
    )
}

private suspend fun handleMembershipWentValid(
    payload: WebhookPayload,
    store: UserAccessStore
) {
    val data = payload.data
    val userId = data?.userId ?: data?.membership?.userId
    if (userId == null) {
        logger.warn("membership.went_valid: no userId in payload ${payload.id}")
        return
    }

    logger.info("Keeping access active for user $userId (membership went valid)")
    store.grantAccess(
        userId = userId,
        membershipId = data?.membership?.id,
        planId = data?.membership?.plan?.id
    )
}

private suspend fun handleMembershipWentInvalid(
    payload: WebhookPayload,
    store: UserAccessStore
) {
    val data = payload.data
    val userId = data?.userId ?: data?.membership?.userId
    if (userId == null) {
        logger.warn("membership.went_invalid: no userId in payload ${payload.id}")
        return
    }

    logger.info("Revoking access for user $userId (membership went invalid)")
    store.revokeAccess(userId = userId)
}

// ── Data Classes ────────────────────────────────────────────────────────

data class WebhookPayload(
    @SerializedName("id") val id: String,
    @SerializedName("event_type") val eventType: String,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("data") val data: WebhookData?
)

data class WebhookData(
    @SerializedName("user_id") val userId: String?,
    @SerializedName("membership") val membership: WebhookMembership?
)

data class WebhookMembership(
    @SerializedName("id") val id: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("plan") val plan: WebhookPlan?
)

data class WebhookPlan(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)

// ── User Access Store Interface ─────────────────────────────────────────

/**
 * Interface for persisting user access state.
 *
 * Implement this with your database (Convex, Postgres, Redis, etc.).
 */
interface UserAccessStore {
    /** Grant or renew access for a user. */
    suspend fun grantAccess(userId: String, membershipId: String?, planId: String?)

    /** Revoke access for a user. */
    suspend fun revokeAccess(userId: String)

    /** Check if a user currently has active access. */
    suspend fun hasAccess(userId: String): Boolean
}

/**
 * In-memory implementation for development / testing.
 * Replace with a real database-backed implementation.
 */
class InMemoryUserAccessStore : UserAccessStore {

    private val accessMap = mutableMapOf<String, UserAccess>()

    override suspend fun grantAccess(userId: String, membershipId: String?, planId: String?) {
        accessMap[userId] = UserAccess(
            userId = userId,
            hasAccess = true,
            membershipId = membershipId,
            planId = planId
        )
    }

    override suspend fun revokeAccess(userId: String) {
        accessMap.remove(userId)
    }

    override suspend fun hasAccess(userId: String): Boolean {
        return accessMap.containsKey(userId) && accessMap[userId]?.hasAccess == true
    }
}

data class UserAccess(
    val userId: String,
    val hasAccess: Boolean,
    val membershipId: String?,
    val planId: String?
)
