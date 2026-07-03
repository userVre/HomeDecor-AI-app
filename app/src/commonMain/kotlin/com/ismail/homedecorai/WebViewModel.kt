package com.ismail.homedecorai

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ismail.homedecorai.model.BoardScreenState
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.ui.profile.ProfileScreenState
import com.ismail.homedecorai.ui.settings.SettingsScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class WebViewModel {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var anonymousId: String = ""

    var isSignedIn by mutableStateOf(false)
        private set
    var signedInName by mutableStateOf<String?>(null)
        private set
    var signedInEmail by mutableStateOf<String?>(null)
        private set

    var diamonds by mutableIntStateOf(0)
        private set
    var isPro by mutableStateOf(false)
        private set

    var boardState by mutableStateOf(BoardScreenState(
        generatedItems = emptyList(),
        favoriteItems = emptyList(),
        projectItems = emptyList(),
    ))
        private set

    var isGenerating by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    val profileState: ProfileScreenState
        get() = ProfileScreenState(
            isGuest = !isSignedIn,
            signedInName = signedInName,
            signedInEmail = signedInEmail,
            diamonds = diamonds,
            isPro = isPro,
            favoritesCount = boardState.favoriteItems.size,
            savedDesigns = boardState.favoriteItems,
        )

    val settingsState: SettingsScreenState
        get() = SettingsScreenState(
            versionName = "1.0.0-web",
            settingsBusy = false,
            isSignedIn = isSignedIn,
            signedInName = signedInName,
            signedInEmail = signedInEmail,
            diamonds = diamonds,
        )

    fun initialize() {
        anonymousId = getAnonymousIdFromPlatform()
        scope.launch {
            // 1. Initialize Clerk SDK first so it's ready for auth calls
            val clerkResult = clerkInit()
            if (clerkResult != "ok") {
                // Clerk failed to load — continue as guest
                bootstrapViewer()
                fetchBoard()
                return@launch
            }

            // 2. Check if user has an existing Clerk session (returning user)
            val user = clerkGetUser()
            if (user != null) {
                isSignedIn = true
                signedInName = user.fullName
                signedInEmail = user.email
            }

            // 3. Bootstrap viewer (works for both guest and authenticated users)
            bootstrapViewer()
            fetchBoard()
        }
    }

    private suspend fun bootstrapViewer() {
        try {
            val response = convexMutationAuth(
                "users:getOrCreateCurrentUser",
                mapOf("anonymousId" to anonymousId),
            )
            parseViewer(response)
        } catch (e: Exception) {
            try {
                val response = convexQueryAuth(
                    "users:me",
                    mapOf("anonymousId" to anonymousId),
                )
                parseViewer(response)
            } catch (_: Exception) {
                diamonds = 0
                isPro = false
            }
        }
    }

    private fun parseViewer(json: String) {
        diamonds = extractInt(json, "diamondBalance", 0)
        isPro = extractBool(json, "hasProAccess")
        val plan = extractString(json, "plan", "free")
        if (plan != "free") isPro = true
    }

    private suspend fun fetchBoard() {
        try {
            val response = convexQueryAuth(
                "generations:getUserArchive",
                mapOf("anonymousId" to anonymousId),
            )
            val items = extractArray(response, "result")
            val generated = items.mapNotNull { parseBoardItem(it) }
            val favorites = generated.filter { it.isFavorite }
            boardState = BoardScreenState(
                generatedItems = generated,
                favoriteItems = favorites,
                projectItems = emptyList(),
            )
        } catch (_: Exception) {}
    }

    private fun parseBoardItem(json: String): BoardItem? {
        val id = extractString(json, "_id")
        if (id.isBlank()) return null
        return BoardItem(
            id = id,
            toolTitle = extractString(json, "serviceType"),
            style = extractString(json, "style"),
            roomType = extractString(json, "roomType"),
            imageUrl = extractString(json, "imageUrl"),
            sourceImageUrl = extractString(json, "sourceImageUrl"),
            status = extractString(json, "status"),
            createdAt = extractDouble(json, "createdAt"),
            isFavorite = extractBool(json, "isFavorite"),
        )
    }

    fun onSignIn() {
        scope.launch {
            // Ensure Clerk is initialized before attempting sign-in
            if (clerkInit() != "ok") {
                errorMessage = "Failed to load authentication. Please refresh."
                return@launch
            }
            val result = clerkSignIn()
            if (result == "ok") {
                val user = clerkGetUser()
                if (user != null) {
                    isSignedIn = true
                    signedInName = user.fullName
                    signedInEmail = user.email
                }
                bootstrapViewer()
                fetchBoard()
            }
        }
    }

    fun onSignUp() {
        scope.launch {
            if (clerkInit() != "ok") {
                errorMessage = "Failed to load authentication. Please refresh."
                return@launch
            }
            val result = clerkSignUp()
            if (result == "ok") {
                val user = clerkGetUser()
                if (user != null) {
                    isSignedIn = true
                    signedInName = user.fullName
                    signedInEmail = user.email
                }
                bootstrapViewer()
                fetchBoard()
            }
        }
    }

    fun onDeleteAccount() {
        scope.launch {
            try {
                convexMutationAuth(
                    "users:deleteAccountData",
                    mapOf("anonymousId" to anonymousId),
                )
            } catch (_: Exception) {}
            clerkSignOut()
            isSignedIn = false
            signedInName = null
            signedInEmail = null
            bootstrapViewer()
            fetchBoard()
        }
    }

    fun onSignOut() {
        scope.launch {
            clerkSignOut()
            isSignedIn = false
            signedInName = null
            signedInEmail = null
            bootstrapViewer()
            fetchBoard()
        }
    }

    fun onOpenDiamonds() {
        scope.launch {
            try {
                val response = convexMutationAuth(
                    "diamonds:claimDailyDiamond",
                    mapOf("anonymousId" to anonymousId),
                )
                parseViewer(response)
            } catch (_: Exception) {}
        }
    }

    fun toggleFavorite(itemId: String) {
        scope.launch {
            try {
                convexMutationAuth(
                    "generations:toggleFavorite",
                    mapOf(
                        "anonymousId" to anonymousId,
                        "generationId" to itemId,
                    ),
                )
                fetchBoard()
            } catch (_: Exception) {}
        }
    }

    fun addToMyBoard(imageUrl: String) {
        scope.launch {
            try {
                convexMutationAuth(
                    "generations:saveToBoard",
                    mapOf(
                        "anonymousId" to anonymousId,
                        "imageUrl" to imageUrl,
                    ),
                )
                fetchBoard()
            } catch (_: Exception) {}
        }
    }

    fun dismissError() {
        errorMessage = null
    }

    // ---- Simple JSON extractors ----

    private fun extractString(json: String, key: String, default: String = ""): String {
        val match = Regex("\"$key\"\\s*:\\s*\"([^\"]*?)\"").find(json) ?: return default
        return match.groupValues[1]
    }

    private fun extractInt(json: String, key: String, default: Int = 0): Int {
        val match = Regex("\"$key\"\\s*:\\s*(-?\\d+)").find(json) ?: return default
        return match.groupValues[1].toIntOrNull() ?: default
    }

    private fun extractDouble(json: String, key: String, default: Double = 0.0): Double {
        val match = Regex("\"$key\"\\s*:\\s*(-?\\d+\\.?\\d*)").find(json) ?: return default
        return match.groupValues[1].toDoubleOrNull() ?: default
    }

    private fun extractBool(json: String, key: String, default: Boolean = false): Boolean {
        val match = Regex("\"$key\"\\s*:\\s*(true|false)").find(json) ?: return default
        return match.groupValues[1] == "true"
    }

    private fun extractArray(json: String, key: String): List<String> {
        val searchKey = "\"$key\"\\s*:\\s*\\["
        val match = Regex(searchKey).find(json) ?: return emptyList()
        val startIdx = match.range.last
        var depth = 0
        var inString = false
        var escape = false
        for (i in startIdx until json.length) {
            when {
                escape -> escape = false
                json[i] == '\\' && inString -> escape = true
                json[i] == '"' -> inString = !inString
                !inString && json[i] == '[' -> depth++
                !inString && json[i] == ']' -> {
                    depth--
                    if (depth == 0) {
                        val content = json.substring(startIdx + 1, i).trim()
                        if (content.isBlank()) return emptyList()
                        return splitTopLevelObjects(content)
                    }
                }
            }
        }
        return emptyList()
    }

    private fun splitTopLevelObjects(content: String): List<String> {
        val objects = mutableListOf<String>()
        var depth = 0
        var inString = false
        var escape = false
        var start = -1
        for (i in content.indices) {
            when {
                escape -> escape = false
                content[i] == '\\' && inString -> escape = true
                content[i] == '"' -> inString = !inString
                !inString && content[i] == '{' -> {
                    if (depth == 0) start = i
                    depth++
                }
                !inString && content[i] == '}' -> {
                    depth--
                    if (depth == 0 && start >= 0) {
                        objects.add(content.substring(start, i + 1))
                        start = -1
                    }
                }
            }
        }
        return objects
    }
}
