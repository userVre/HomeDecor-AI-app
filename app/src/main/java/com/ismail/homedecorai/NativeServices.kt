package com.ismail.homedecorai

import dev.convex.android.ConvexClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
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
    val hasProAccess: Boolean = false,
    val hasPaidAccess: Boolean = false,
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
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    suspend fun bootstrapViewer(anonymousId: String): ViewerSummary = withContext(Dispatchers.IO) {
        val raw = services.convex.mutation<JsonElement>(
            "users:getOrCreateCurrentUser",
            mapOf("anonymousId" to anonymousId),
        )
        json.decodeFromJsonElement<ViewerSummary>(raw)
    }

    suspend fun viewerSummary(anonymousId: String): ViewerSummary = withContext(Dispatchers.IO) {
        runCatching {
            services.convex
                .subscribe<ViewerSummary>("users:me", mapOf("anonymousId" to anonymousId))
                .first()
                .getOrThrow()
        }.getOrElse {
            ViewerSummary()
        }
    }

    suspend fun canUserGenerate(anonymousId: String): GenerationAccess = withContext(Dispatchers.IO) {
        runCatching {
            services.convex
                .subscribe<GenerationAccess>("users:canUserGenerate", mapOf("anonymousId" to anonymousId))
                .first()
                .getOrThrow()
        }.getOrElse {
            GenerationAccess(
                allowed = false,
                shouldTriggerPaywall = true,
                message = it.message ?: "Generation access could not be verified.",
            )
        }
    }

    suspend fun startGeneration(
        anonymousId: String,
        imageBytes: ByteArray,
        mimeType: String,
        tool: DecorTool,
        roomType: String,
        style: String,
        palette: String,
        designMode: String,
        customPrompt: String,
    ): StartGenerationResponse = withContext(Dispatchers.IO) {
        val uploadUrl = services.convex.mutation<String>(
            "generations:createSourceUploadUrl",
            mapOf("anonymousId" to anonymousId),
        )
        val storageId = uploadToStorage(uploadUrl, imageBytes, mimeType)
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
        if (customPrompt.isNotBlank()) {
            args["customPrompt"] = customPrompt
        }
        val raw = services.convex.mutation<JsonElement>("generations:startGeneration", args)
        json.decodeFromJsonElement<StartGenerationResponse>(raw)
    }

    suspend fun waitForGeneration(
        anonymousId: String,
        generationId: String,
        maxAttempts: Int = 45,
    ): ArchiveGeneration = withContext(Dispatchers.IO) {
        repeat(maxAttempts) {
            val item = archive(anonymousId).firstOrNull { it.id == generationId }
            if (item?.status == "ready" && !item.imageUrl.isNullOrBlank()) {
                return@withContext item
            }
            if (item?.status == "failed") {
                error(item.errorMessage ?: "Generation failed.")
            }
            delay(2_000)
        }
        error("Generation is still processing. Check the portfolio again in a moment.")
    }

    suspend fun archive(anonymousId: String): List<ArchiveGeneration> = withContext(Dispatchers.IO) {
        runCatching {
            services.convex
                .subscribe<List<ArchiveGeneration>>("generations:getUserArchive", mapOf("anonymousId" to anonymousId))
                .first()
                .getOrThrow()
        }.getOrElse {
            emptyList()
        }
    }

    suspend fun claimDailyDiamond(anonymousId: String): ViewerSummary = withContext(Dispatchers.IO) {
        val raw = services.convex.mutation<JsonElement>("diamonds:claimDailyDiamond", mapOf("anonymousId" to anonymousId))
        json.decodeFromJsonElement<ViewerSummary>(raw)
    }

    private fun uploadToStorage(uploadUrl: String, imageBytes: ByteArray, mimeType: String): String {
        val connection = (URL(uploadUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", mimeType)
            setRequestProperty("Content-Length", imageBytes.size.toString())
        }
        connection.outputStream.use { it.write(imageBytes) }
        val responseCode = connection.responseCode
        val response = if (responseCode in 200..299) {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            val errorBody = connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            error("Convex storage upload failed ($responseCode): $errorBody")
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
}
