package com.ismail.homedecorai.whop

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.util.AttributeKey
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("AccessGuard")

/**
 * Attribute key for storing the authenticated user ID on the call.
 * Use this to retrieve the user ID inside protected route handlers.
 */
val UserIdKey = AttributeKey<String>("UserId")

/**
 * Extracts the user ID from the request.
 *
 * By default reads from the `X-User-Id` header.
 * Adapt this to your auth scheme (JWT claim, session cookie, etc.).
 */
private fun ApplicationCall.extractUserId(): String? {
    return request.header("X-User-Id")
        ?: request.queryParameters["user_id"]
}

/**
 * Checks if the current request has a valid Whop membership.
 * Call this at the start of any protected route handler.
 *
 * @return The user ID if access is granted, or null if denied (response already sent).
 */
suspend fun ApplicationCall.requireWhopAccess(whopService: WhopService): String? {
    val userId = extractUserId()

    if (userId == null) {
        logger.warn("Access denied: no user ID provided")
        respond(
            HttpStatusCode.Unauthorized,
            mapOf(
                "error" to "unauthorized",
                "message" to "User ID is required. Provide X-User-Id header."
            )
        )
        return null
    }

    val hasAccess = whopService.checkUserAccess(userId)
    if (!hasAccess) {
        logger.info("Access denied for user $userId: no active membership")
        respond(
            HttpStatusCode.Forbidden,
            mapOf(
                "error" to "forbidden",
                "message" to "Active membership required",
                "checkout_urls" to mapOf(
                    "essential_monthly" to WhopConfig.CHECKOUT_URL_ESSENTIAL_MONTHLY,
                    "essential_yearly" to WhopConfig.CHECKOUT_URL_ESSENTIAL_YEARLY,
                    "pro_monthly" to WhopConfig.CHECKOUT_URL_PRO_MONTHLY,
                    "pro_yearly" to WhopConfig.CHECKOUT_URL_PRO_YEARLY,
                    "studio_monthly" to WhopConfig.CHECKOUT_URL_STUDIO_MONTHLY,
                    "studio_yearly" to WhopConfig.CHECKOUT_URL_STUDIO_YEARLY,
                    "agency_monthly" to WhopConfig.CHECKOUT_URL_AGENCY_MONTHLY,
                    "agency_yearly" to WhopConfig.CHECKOUT_URL_AGENCY_YEARLY,
                )
            )
        )
        return null
    }

    attributes.put(UserIdKey, userId)
    return userId
}

/**
 * Helper to get the authenticated user ID from inside a protected route.
 */
fun ApplicationCall.requireUserId(): String {
    return attributes.getOrNull(UserIdKey)
        ?: throw IllegalStateException("User ID not set. Ensure requireWhopAccess() was called first.")
}
