package com.ismail.homedecorai

import android.content.Context
import com.ismail.homedecorai.model.DecorTool
import dev.convex.android.ConvexClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import java.net.HttpURLConnection
import java.net.URL

class NativeServices {
    val convex: ConvexClient by lazy {
        ConvexClient(BuildConfig.CONVEX_URL)
    }
}

@Serializable
data class ViewerSummary(
    val plan: String = "free",
    val credits: Int = 1,
    val diamondBalance: Int = credits,
    val streakCount: Int = 0,
    val lastClaimAt: Double? = null,
    val nextDiamondClaimAt: Double = 0.0,
    val canClaimDiamond: Boolean = false,
    val claimStatus: String? = null,
    val status: String? = null,
    val granted: Boolean = false,
    val creditsAdded: Int = 0,
    val proTrialExpiresAt: Double? = null,
    val hasProAccess: Boolean = false,
    val hasPaidAccess: Boolean = false,
    val isGuest: Boolean = true,
    val canGenerateNow: Boolean = credits > 0 || hasProAccess,
)

@Serializable
data class GenerationAccess(
    val allowed: Boolean = false,
    val reason: String? = null,
    val shouldTriggerPaywall: Boolean = false,
    val shouldShowLimitReached: Boolean = false,
    val message: String? = null,
)

@Serializable
data class StorageUploadResponse(
    val storageId: String,
)

@Serializable
data class StartGenerationResponse(
    val generationId: String,
    val creditsRemaining: Double = 0.0,
    val planUsed: String? = null,
    val imageUrl: String? = null,
    val quality: String? = null,
    val renderLabel: String? = null,
)

@Serializable
data class ArchiveGeneration(
    @SerialName("_id")
    val id: String,
    val imageUrl: String? = null,
    val sourceImageUrl: String? = null,
    val style: String? = null,
    val roomType: String? = null,
    val serviceType: String? = null,
    val status: String? = null,
    val errorMessage: String? = null,
    val createdAt: Double = 0.0,
    val watermarkRequired: Boolean = false,
)

class HomeDecorRepository(
    private val services: NativeServices,
    context: Context,
) {
    private val appContext = context.applicationContext
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    suspend fun bootstrapViewer(anonymousId: String): ViewerSummary = networkCall {
        val raw = services.convex.mutation<JsonElement>(
            "users:getOrCreateCurrentUser",
            mapOf("anonymousId" to anonymousId),
        )
        json.decodeFromJsonElement<ViewerSummary>(raw)
    }

    suspend fun viewerSummary(anonymousId: String): ViewerSummary {
        return runCatching { viewerSummaryStrict(anonymousId) }.getOrElse { ViewerSummary() }
    }

    suspend fun viewerSummaryStrict(anonymousId: String): ViewerSummary = networkCall {
        services.convex
            .subscribe<ViewerSummary>("users:me", mapOf("anonymousId" to anonymousId))
            .first()
            .getOrThrow()
    }

    suspend fun canUserGenerate(anonymousId: String): GenerationAccess {
        return runCatching {
            networkCall {
                services.convex
                    .subscribe<GenerationAccess>("users:canUserGenerate", mapOf("anonymousId" to anonymousId))
                    .first()
                    .getOrThrow()
            }
        }.getOrElse { error ->
            when (error.toAppErrorKind(appContext)) {
                AppErrorKind.Offline, AppErrorKind.Timeout -> throw error
                else -> GenerationAccess(
                    allowed = false,
                    shouldTriggerPaywall = true,
                    message = null,
                )
            }
        }
    }

    suspend fun startGeneration(
        anonymousId: String,
        imageBytes: ByteArray,
        mimeType: String,
        maskBytes: ByteArray? = null,
        maskMimeType: String? = null,
        tool: DecorTool,
        roomType: String,
        style: String,
        palette: String,
        designMode: String,
        customPrompt: String,
        referenceImageBytes: ByteArray? = null,
        referenceMimeType: String? = null,
    ): StartGenerationResponse = networkCall(UPLOAD_TIMEOUT_MS) {
        val uploadUrl = services.convex.mutation<String>(
            "generations:createSourceUploadUrl",
            mapOf("anonymousId" to anonymousId),
        )
        val storageId = uploadToStorage(uploadUrl, imageBytes, mimeType)
        val maskStorageId = if (maskBytes != null) {
            val maskUploadUrl = services.convex.mutation<String>(
                "generations:createSourceUploadUrl",
                mapOf("anonymousId" to anonymousId),
            )
            uploadToStorage(maskUploadUrl, maskBytes, maskMimeType ?: "image/png")
        } else {
            null
        }
        val referenceStorageIds = if (referenceImageBytes != null) {
            val referenceUploadUrl = services.convex.mutation<String>(
                "generations:createSourceUploadUrl",
                mapOf("anonymousId" to anonymousId),
            )
            listOf(uploadToStorage(referenceUploadUrl, referenceImageBytes, referenceMimeType ?: "image/jpeg"))
        } else {
            emptyList()
        }
        val args = mutableMapOf<String, Any>(
            "anonymousId" to anonymousId,
            "imageStorageId" to storageId,
            "serviceType" to serviceTypeForBackend(tool),
            "selection" to palette.ifBlank { style },
            "styleSelections" to listOf(style),
            "roomType" to roomType,
            "displayStyle" to style,
            "aspectRatio" to "1:1",
            "modeId" to designMode,
            "paletteId" to palette,
        )
        if (maskStorageId != null) {
            args["maskStorageId"] = maskStorageId
            args["targetSurface"] = when (tool.id) {
                "paint" -> "wall"
                "floor" -> "floor"
                "replace" -> "object"
                else -> roomType
            }
        }
        if (tool.id == "paint" && style.isNotBlank()) {
            args["targetColor"] = style
            args["targetColorCategory"] = style
        }
        if (customPrompt.isNotBlank()) {
            args["customPrompt"] = customPrompt
        }
        if (referenceStorageIds.isNotEmpty()) {
            args["referenceImageStorageIds"] = referenceStorageIds
            args["displayStyle"] = "Reference style transfer"
        }
        val raw = services.convex.mutation<JsonElement>("generations:startGeneration", args)
        json.decodeFromJsonElement<StartGenerationResponse>(raw)
    }

    suspend fun setViewerPlanFromRevenueCat(
        anonymousId: String,
        plan: String,
        subscriptionType: String,
        entitlement: String,
        purchasedAt: Double?,
        subscriptionEnd: Double?,
    ): JsonElement = networkCall {
        services.convex.mutation<JsonElement>(
            "users:setViewerPlanFromRevenueCat",
            buildMap<String, Any> {
                put("anonymousId", anonymousId)
                put("plan", plan)
                put("subscriptionType", subscriptionType)
                put("subscriptionEntitlement", entitlement)
                purchasedAt?.let { put("purchasedAt", it) }
                subscriptionEnd?.let { put("subscriptionEnd", it) }
            },
        )
    }

    suspend fun fulfillDiamondPurchase(
        anonymousId: String,
        packId: String,
        transactionId: String,
        productIdentifier: String,
        packageIdentifier: String?,
        amount: Double,
        currencyCode: String,
        purchasedAt: Double,
    ): JsonElement = networkCall {
        services.convex.mutation<JsonElement>(
            "users:fulfillDiamondPurchase",
            buildMap<String, Any> {
                put("anonymousId", anonymousId)
                put("packId", packId)
                put("transactionId", transactionId)
                put("productIdentifier", productIdentifier)
                packageIdentifier?.let { put("packageIdentifier", it) }
                put("amount", amount)
                put("currencyCode", currencyCode)
                put("purchasedAt", purchasedAt)
            },
        )
    }

    suspend fun submitFeedback(
        anonymousId: String,
        message: String,
        generationCount: Int,
    ): JsonElement = networkCall {
        services.convex.mutation<JsonElement>(
            "feedback:submit",
            mapOf(
                "anonymousId" to anonymousId,
                "message" to message,
                "generationCount" to generationCount,
            ),
        )
    }

    suspend fun deleteAccountData(anonymousId: String): JsonElement = networkCall {
        services.convex.mutation<JsonElement>(
            "users:deleteAccountData",
            mapOf("anonymousId" to anonymousId),
        )
    }

    suspend fun waitForGeneration(
        anonymousId: String,
        generationId: String,
        maxAttempts: Int = 45,
    ): ArchiveGeneration = withContext(Dispatchers.IO) {
        repeat(maxAttempts) {
            if (!appContext.hasUsableNetwork()) {
                throw AppRecoverableException(AppErrorKind.Offline)
            }
            val item = archiveStrict(anonymousId).firstOrNull { it.id == generationId }
            if (item?.status == "ready" && !item.imageUrl.isNullOrBlank()) {
                return@withContext item
            }
            if (item?.status == "failed") {
                throw AppRecoverableException(AppErrorKind.Generation)
            }
            delay(2_000)
        }
        throw AppRecoverableException(AppErrorKind.Timeout)
    }

    suspend fun archive(anonymousId: String): List<ArchiveGeneration> {
        return runCatching { archiveStrict(anonymousId) }.getOrElse { emptyList() }
    }

    private suspend fun archiveStrict(anonymousId: String): List<ArchiveGeneration> = networkCall {
        services.convex
            .subscribe<List<ArchiveGeneration>>("generations:getUserArchive", mapOf("anonymousId" to anonymousId))
            .first()
            .getOrThrow()
    }

    suspend fun claimDailyDiamond(anonymousId: String): ViewerSummary = networkCall {
        val raw = services.convex.mutation<JsonElement>("diamonds:claimDailyDiamond", mapOf("anonymousId" to anonymousId))
        json.decodeFromJsonElement<ViewerSummary>(raw)
    }

    private suspend fun <T> networkCall(
        timeoutMs: Long = NETWORK_TIMEOUT_MS,
        block: suspend () -> T,
    ): T = withContext(Dispatchers.IO) {
        if (!appContext.hasUsableNetwork()) {
            throw AppRecoverableException(AppErrorKind.Offline)
        }
        try {
            withTimeout(timeoutMs) {
                block()
            }
        } catch (error: TimeoutCancellationException) {
            throw AppRecoverableException(AppErrorKind.Timeout, error)
        }
    }

    private fun uploadToStorage(uploadUrl: String, imageBytes: ByteArray, mimeType: String): String {
        val connection = (URL(uploadUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            connectTimeout = HTTP_TIMEOUT_MS
            readTimeout = HTTP_TIMEOUT_MS
            setRequestProperty("Content-Type", mimeType)
            setRequestProperty("Content-Length", imageBytes.size.toString())
        }
        connection.outputStream.use { it.write(imageBytes) }
        val responseCode = connection.responseCode
        val response = if (responseCode in 200..299) {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            val errorBody = connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            throw AppRecoverableException(
                AppErrorKind.Generation,
                java.io.IOException("Upload failed ($responseCode): $errorBody"),
            )
        }
        return json.decodeFromString(StorageUploadResponse.serializer(), response).storageId
    }

    private fun serviceTypeForBackend(tool: DecorTool): String {
        return when (tool.id) {
            "interior" -> "interior"
            "facade" -> "exterior"
            "garden" -> "garden"
            "paint" -> "paint"
            "floor" -> "floor"
            "layout" -> "layout"
            "replace" -> "replace"
            "reference" -> "reference"
            else -> tool.serviceType
        }
    }

    private companion object {
        const val NETWORK_TIMEOUT_MS = 30_000L
        const val UPLOAD_TIMEOUT_MS = 60_000L
        const val HTTP_TIMEOUT_MS = 30_000
    }
}
