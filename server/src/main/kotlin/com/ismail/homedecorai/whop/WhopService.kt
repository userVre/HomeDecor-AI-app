package com.ismail.homedecorai.whop

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * Service for interacting with the Whop API v5.
 *
 * Uses OkHttp for HTTP calls and Gson for JSON parsing.
 * All sensitive configuration lives in [WhopConfig].
 */
class WhopService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    // ── Public API ───────────────────────────────────────────────────────

    /**
     * Check if a user has an active membership on the configured product.
     *
     * @param userId Whop user ID (e.g. "user_xxxxx")
     * @return `true` if the user has at least one active membership for [WhopConfig.PRODUCT_ID]
     */
    fun checkUserAccess(userId: String): Boolean {
        return try {
            val memberships = fetchMemberships(userId)
            memberships.any { membership ->
                membership.status == MembershipStatus.ACTIVE &&
                    membership.product?.id == WhopConfig.PRODUCT_ID
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Fetch the user's current membership details for the configured product.
     *
     * @param userId Whop user ID
     * @return The active membership, or `null` if none found
     */
    fun getUserMembership(userId: String): Membership? {
        return try {
            val memberships = fetchMemberships(userId)
            memberships.firstOrNull { membership ->
                membership.status == MembershipStatus.ACTIVE &&
                    membership.product?.id == WhopConfig.PRODUCT_ID
            }
        } catch (e: Exception) {
            null
        }
    }

    // ── Internal ─────────────────────────────────────────────────────────

    private fun fetchMemberships(userId: String): List<Membership> {
        val url = "${WhopConfig.BASE_URL}/memberships?user_id=$userId"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${WhopConfig.API_KEY}")
            .addHeader("Accept", "application/json")
            .get()
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw WhopApiException(
                message = "Whop API returned ${response.code}: ${response.body?.string() ?: "no body"}",
                code = response.code
            )
        }

        val body = response.body?.string()
            ?: throw WhopApiException(message = "Empty response body from Whop API", code = 0)

        return try {
            val parsed = gson.fromJson(body, MembershipListResponse::class.java)
            parsed.data ?: emptyList()
        } catch (e: Exception) {
            throw WhopApiException(message = "Failed to parse Whop response: ${e.message}", code = 0)
        }
    }

    /**
     * Verify a webhook signature to ensure the payload came from Whop.
     *
     * @param payload Raw request body bytes
     * @param signature Signature from the `X-Whop-Signature` header
     * @return `true` if the signature is valid
     *
     * Note: Implement HMAC-SHA256 verification using your webhook signing secret
     * from the Whop dashboard when you have one configured.
     */
    fun verifyWebhookSignature(payload: ByteArray, signature: String): Boolean {
        // TODO: Implement HMAC-SHA256 verification with your Whop webhook signing secret
        // For now, accept all webhooks in development
        return true
    }

    // ── Data Classes ─────────────────────────────────────────────────────

    data class MembershipListResponse(
        @SerializedName("data") val data: List<Membership>?
    )

    data class Membership(
        @SerializedName("id") val id: String,
        @SerializedName("status") val status: String,
        @SerializedName("product") val product: Product?,
        @SerializedName("plan") val plan: Plan?,
        @SerializedName("created_at") val createdAt: String?,
        @SerializedName("updated_at") val updatedAt: String?
    ) {
        val isActive: Boolean get() = status == MembershipStatus.ACTIVE
    }

    data class Product(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String?
    )

    data class Plan(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String?,
        @SerializedName("price") val price: Long?
    )

    object MembershipStatus {
        const val ACTIVE = "active"
        const val CANCELED = "canceled"
        const val INACTIVE = "inactive"
    }

    class WhopApiException(
        message: String,
        val code: Int
    ) : Exception(message)
}
