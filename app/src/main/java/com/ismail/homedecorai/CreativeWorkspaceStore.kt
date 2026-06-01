package com.ismail.homedecorai

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

@Serializable
data class Project(
    val id: String = newWorkspaceId(),
    val name: String,
    val roomType: String = "",
    val coverImageUri: String? = null,
    val coverImageUrl: String? = null,
    val originalPhotoUris: List<String> = emptyList(),
    val originalPhotoUrls: List<String> = emptyList(),
    val notes: String = "",
    val styleInfo: String = "",
    val createdAt: Long = nowMillis(),
    val updatedAt: Long = createdAt,
)

@Serializable
data class GeneratedResult(
    val id: String = newWorkspaceId(),
    val projectId: String? = null,
    val toolId: String,
    val toolTitle: String,
    val roomType: String = "",
    val style: String = "",
    val palette: String = "",
    val prompt: String? = null,
    val budgetLabel: String = "",
    val sourceImageUri: String? = null,
    val sourceImageUrl: String? = null,
    val imageUri: String? = null,
    val imageUrl: String? = null,
    val status: String = "ready",
    val errorMessage: String? = null,
    val createdAt: Long = nowMillis(),
)

@Serializable
data class FavoriteItem(
    val id: String = newWorkspaceId(),
    val projectId: String? = null,
    val resultId: String? = null,
    val title: String,
    val toolId: String = "",
    val roomType: String = "",
    val style: String = "",
    val imageRes: Int = 0,
    val imageUri: String? = null,
    val imageUrl: String? = null,
    val sourceType: String = "generated_result",
    val notes: String = "",
    val createdAt: Long = nowMillis(),
)

@Serializable
data class MoodboardItem(
    val id: String = newWorkspaceId(),
    val projectId: String? = null,
    val title: String,
    val imageRes: Int = 0,
    val imageUri: String? = null,
    val imageUrl: String? = null,
    val colorHex: String? = null,
    val source: String = "manual",
    val notes: String = "",
    val sortOrder: Int = 0,
    val createdAt: Long = nowMillis(),
)

@Serializable
data class RecentStyle(
    val id: String = newWorkspaceId(),
    val toolId: String,
    val style: String,
    val roomType: String = "",
    val palette: String = "",
    val useCount: Int = 1,
    val lastUsedAt: Long = nowMillis(),
)

@Serializable
data class DailyRewardState(
    val currentStreak: Int = 0,
    val totalClaims: Int = 0,
    val totalDiamondsEarned: Int = 0,
    val lastClaimedAt: Long? = null,
    val lastClaimEpochDay: Long? = null,
    val nextClaimAt: Long = 0L,
    val lastRewardAmount: Int = 0,
)

@Serializable
data class PersistedMaskPoint(
    val x: Float,
    val y: Float,
)

@Serializable
data class PersistedMaskStroke(
    val points: List<PersistedMaskPoint> = emptyList(),
    val brushSize: Float,
    val erase: Boolean = false,
)

@Serializable
data class ToolDraft(
    val id: String = newWorkspaceId(),
    val toolId: String,
    val projectId: String? = null,
    val selectedPhotoUris: List<String> = emptyList(),
    val selectedExampleLabels: List<String> = emptyList(),
    val referencePhotoUri: String? = null,
    val selectedReferenceExampleLabel: String? = null,
    val selectedReferenceDiscoverItemId: String? = null,
    val selectedRooms: List<String> = emptyList(),
    val selectedStyles: List<String> = emptyList(),
    val selectedPalettes: List<String> = emptyList(),
    val roomType: String = "",
    val style: String = "",
    val palette: String = "",
    val designMode: String = "",
    val budgetMode: String = "",
    val avoidOptions: List<String> = emptyList(),
    val keepOptions: List<String> = emptyList(),
    val changeOptions: List<String> = emptyList(),
    val preserveRestOfImage: Boolean = false,
    val customPrompt: String = "",
    val layoutConstraints: String = "",
    val mobilierASupprimer: String = "",
    val mobilierADeplacer: String = "",
    val maskStrokes: List<PersistedMaskStroke> = emptyList(),
    val createdAt: Long = nowMillis(),
    val updatedAt: Long = createdAt,
)

@Serializable
data class CreativeWorkspaceState(
    val projects: List<Project> = emptyList(),
    val generatedResults: List<GeneratedResult> = emptyList(),
    val favorites: List<FavoriteItem> = emptyList(),
    val moodboardItems: List<MoodboardItem> = emptyList(),
    val recentStyles: List<RecentStyle> = emptyList(),
    val dailyReward: DailyRewardState = DailyRewardState(),
    val drafts: List<ToolDraft> = emptyList(),
)

class LocalWorkspaceStore(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }
    private val _state = MutableStateFlow(readState())
    val state: StateFlow<CreativeWorkspaceState> = _state.asStateFlow()

    fun createProject(
        name: String,
        roomType: String = "",
        coverImageUri: String? = null,
        coverImageUrl: String? = null,
        originalPhotoUris: List<String> = emptyList(),
        originalPhotoUrls: List<String> = emptyList(),
        notes: String = "",
        styleInfo: String = "",
    ): Project {
        val project = Project(
            name = name.ifBlank { "Untitled project" },
            roomType = roomType,
            coverImageUri = coverImageUri,
            coverImageUrl = coverImageUrl,
            originalPhotoUris = originalPhotoUris.distinct().take(MAX_PROJECT_ORIGINALS),
            originalPhotoUrls = originalPhotoUrls.distinct().take(MAX_PROJECT_ORIGINALS),
            notes = notes,
            styleInfo = styleInfo,
        )
        update { workspace -> workspace.copy(projects = (listOf(project) + workspace.projects).take(MAX_PROJECTS)) }
        return project
    }

    fun upsertProject(project: Project) {
        update { workspace ->
            workspace.copy(
                projects = workspace.projects
                    .upsert(project.copy(updatedAt = nowMillis())) { it.id == project.id }
                    .sortedByDescending { it.updatedAt }
                    .take(MAX_PROJECTS),
            )
        }
    }

    fun deleteProject(projectId: String) {
        update { workspace ->
            workspace.copy(
                projects = workspace.projects.filterNot { it.id == projectId },
                generatedResults = workspace.generatedResults.map {
                    if (it.projectId == projectId) it.copy(projectId = null) else it
                },
                favorites = workspace.favorites.map {
                    if (it.projectId == projectId) it.copy(projectId = null) else it
                },
                moodboardItems = workspace.moodboardItems.filterNot { it.projectId == projectId },
                drafts = workspace.drafts.map {
                    if (it.projectId == projectId) it.copy(projectId = null, updatedAt = nowMillis()) else it
                },
            )
        }
    }

    fun upsertGeneratedResult(result: GeneratedResult) {
        update { workspace ->
            val nextProjects = if (result.projectId == null) {
                workspace.projects
            } else {
                workspace.projects.map { project ->
                    if (project.id == result.projectId) {
                        project.withGeneratedResult(result)
                    } else {
                        project
                    }
                }
            }
            workspace.copy(
                projects = nextProjects.sortedByDescending { it.updatedAt }.take(MAX_PROJECTS),
                generatedResults = workspace.generatedResults
                    .upsert(result) { it.id == result.id }
                    .sortedByDescending { it.createdAt }
                    .take(MAX_GENERATED_RESULTS),
            )
        }
    }

    fun removeGeneratedResult(resultId: String) {
        update { workspace ->
            workspace.copy(
                generatedResults = workspace.generatedResults.filterNot { it.id == resultId },
                favorites = workspace.favorites.filterNot { it.resultId == resultId },
                moodboardItems = workspace.moodboardItems.filterNot { it.source == "generated_result:$resultId" },
            )
        }
    }

    fun toggleFavorite(result: GeneratedResult): Boolean {
        var isFavorite = false
        update { workspace ->
            val existing = workspace.favorites.firstOrNull { it.resultId == result.id }
            if (existing != null) {
                isFavorite = false
                workspace.copy(favorites = workspace.favorites.filterNot { it.id == existing.id })
            } else {
                isFavorite = true
                val favorite = FavoriteItem(
                    projectId = result.projectId,
                    resultId = result.id,
                    title = favoriteTitle(result),
                    toolId = result.toolId,
                    roomType = result.roomType,
                    style = result.style,
                    imageUri = result.imageUri,
                    imageUrl = result.imageUrl,
                )
                workspace.copy(favorites = (listOf(favorite) + workspace.favorites).take(MAX_FAVORITES))
            }
        }
        return isFavorite
    }

    fun upsertFavorite(favorite: FavoriteItem) {
        update { workspace ->
            workspace.copy(
                favorites = workspace.favorites
                    .upsert(favorite) { it.id == favorite.id }
                    .sortedByDescending { it.createdAt }
                    .take(MAX_FAVORITES),
            )
        }
    }

    fun removeFavorite(favoriteId: String) {
        update { workspace ->
            workspace.copy(favorites = workspace.favorites.filterNot { it.id == favoriteId })
        }
    }

    fun upsertMoodboardItem(item: MoodboardItem) {
        update { workspace ->
            workspace.copy(
                moodboardItems = workspace.moodboardItems
                    .upsert(item) { it.id == item.id }
                    .sortedWith(compareBy<MoodboardItem> { it.sortOrder }.thenByDescending { it.createdAt })
                    .take(MAX_MOODBOARD_ITEMS),
            )
        }
    }

    fun removeMoodboardItem(itemId: String) {
        update { workspace ->
            workspace.copy(moodboardItems = workspace.moodboardItems.filterNot { it.id == itemId })
        }
    }

    fun recordRecentStyle(toolId: String, style: String, roomType: String = "", palette: String = "") {
        if (style.isBlank() && palette.isBlank()) return
        update { workspace ->
            val existing = workspace.recentStyles.firstOrNull {
                it.toolId == toolId && it.style == style && it.roomType == roomType && it.palette == palette
            }
            val recent = existing?.copy(
                useCount = existing.useCount + 1,
                lastUsedAt = nowMillis(),
            ) ?: RecentStyle(
                toolId = toolId,
                style = style,
                roomType = roomType,
                palette = palette,
            )
            workspace.copy(
                recentStyles = workspace.recentStyles
                    .upsert(recent) { it.id == recent.id }
                    .sortedByDescending { it.lastUsedAt }
                    .take(MAX_RECENT_STYLES),
            )
        }
    }

    fun claimDailyReward(now: Long = nowMillis()): DailyRewardState? {
        var claimed: DailyRewardState? = null
        update { workspace ->
            val today = localEpochDay(now)
            val previousDay = workspace.dailyReward.lastClaimEpochDay
            if (previousDay == today) return@update workspace

            val nextStreak = if (previousDay == today - 1) {
                workspace.dailyReward.currentStreak + 1
            } else {
                1
            }
            val rewardAmount = 1
            val nextReward = workspace.dailyReward.copy(
                currentStreak = nextStreak,
                totalClaims = workspace.dailyReward.totalClaims + 1,
                totalDiamondsEarned = workspace.dailyReward.totalDiamondsEarned + rewardAmount,
                lastClaimedAt = now,
                lastClaimEpochDay = today,
                nextClaimAt = startOfNextLocalDayMillis(now),
                lastRewardAmount = rewardAmount,
            )
            claimed = nextReward
            workspace.copy(dailyReward = nextReward)
        }
        return claimed
    }

    fun updateDailyReward(dailyReward: DailyRewardState) {
        update { workspace -> workspace.copy(dailyReward = dailyReward) }
    }

    fun upsertToolDraft(draft: ToolDraft) {
        update { workspace ->
            val saved = draft.copy(updatedAt = nowMillis())
            workspace.copy(
                drafts = workspace.drafts
                    .upsert(saved) { it.toolId == saved.toolId && it.projectId == saved.projectId }
                    .sortedByDescending { it.updatedAt }
                    .take(MAX_DRAFTS),
            )
        }
    }

    fun draftFor(toolId: String, projectId: String? = null): ToolDraft? {
        return state.value.drafts.firstOrNull { it.toolId == toolId && it.projectId == projectId }
            ?: state.value.drafts.firstOrNull { it.toolId == toolId && it.projectId == null }
    }

    fun clearToolDraft(toolId: String, projectId: String? = null) {
        update { workspace ->
            workspace.copy(
                drafts = workspace.drafts.filterNot { it.toolId == toolId && it.projectId == projectId },
            )
        }
    }

    fun clearAll() {
        update { CreativeWorkspaceState() }
    }

    private fun readState(): CreativeWorkspaceState {
        val raw = preferences.getString(KEY_STATE, null) ?: return CreativeWorkspaceState()
        return runCatching { json.decodeFromString(CreativeWorkspaceState.serializer(), raw) }
            .getOrElse { CreativeWorkspaceState() }
    }

    private fun update(transform: (CreativeWorkspaceState) -> CreativeWorkspaceState) {
        val next = transform(_state.value)
        _state.value = next
        preferences.edit().putString(KEY_STATE, json.encodeToString(CreativeWorkspaceState.serializer(), next)).apply()
    }

    private fun favoriteTitle(result: GeneratedResult): String {
        return listOf(result.roomType, result.style, result.toolTitle)
            .filter { it.isNotBlank() }
            .joinToString(" - ")
            .ifBlank { "Saved idea" }
    }

    private fun Project.withGeneratedResult(result: GeneratedResult): Project {
        val nextOriginalUris = (originalPhotoUris + listOfNotNull(result.sourceImageUri))
            .distinct()
            .take(MAX_PROJECT_ORIGINALS)
        val nextOriginalUrls = (originalPhotoUrls + listOfNotNull(result.sourceImageUrl))
            .distinct()
            .take(MAX_PROJECT_ORIGINALS)
        return copy(
            roomType = roomType.ifBlank { result.roomType },
            coverImageUri = coverImageUri ?: result.imageUri ?: result.sourceImageUri,
            coverImageUrl = coverImageUrl ?: result.imageUrl ?: result.sourceImageUrl,
            originalPhotoUris = nextOriginalUris,
            originalPhotoUrls = nextOriginalUrls,
            styleInfo = styleInfo.ifBlank {
                listOf(result.style, result.palette)
                    .filter { it.isNotBlank() }
                    .joinToString(" - ")
            },
            updatedAt = nowMillis(),
        )
    }

    private companion object {
        const val PREFERENCES_NAME = "creative_workspace_store"
        const val KEY_STATE = "creative_workspace_state"
        const val MAX_PROJECTS = 50
        const val MAX_PROJECT_ORIGINALS = 24
        const val MAX_GENERATED_RESULTS = 200
        const val MAX_FAVORITES = 200
        const val MAX_MOODBOARD_ITEMS = 300
        const val MAX_RECENT_STYLES = 30
        const val MAX_DRAFTS = 20
    }
}

private fun <T> List<T>.upsert(item: T, sameItem: (T) -> Boolean): List<T> {
    return listOf(item) + filterNot(sameItem)
}

private fun nowMillis(): Long = System.currentTimeMillis()

private fun newWorkspaceId(): String = UUID.randomUUID().toString()

private fun localEpochDay(now: Long): Long {
    return LocalDate.ofInstant(java.time.Instant.ofEpochMilli(now), ZoneId.systemDefault()).toEpochDay()
}

private fun startOfNextLocalDayMillis(now: Long): Long {
    val zone = ZoneId.systemDefault()
    return LocalDate.ofInstant(java.time.Instant.ofEpochMilli(now), zone)
        .plusDays(1)
        .atStartOfDay(zone)
        .toInstant()
        .toEpochMilli()
}
