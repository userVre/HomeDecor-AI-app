package com.ismail.homedecorai

import com.ismail.homedecorai.model.BoardScreenState
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.model.ProfileScreenState
import com.ismail.homedecorai.model.SettingsScreenState

expect fun getAnonymousIdFromPlatform(): String

expect suspend fun convexQuery(path: String, args: Map<String, Any?>): String

expect suspend fun convexMutation(path: String, args: Map<String, Any?>): String

expect suspend fun convexQueryAuth(path: String, args: Map<String, Any?>): String

expect suspend fun convexMutationAuth(path: String, args: Map<String, Any?>): String

expect suspend fun convexCreateUploadUrl(anonymousId: String): String

expect suspend fun convexUploadToStorage(uploadUrl: String, fileBase64: String, mimeType: String): String

expect fun browserDownloadFile(url: String, filename: String)

expect fun browserShareContent(title: String, url: String)

expect suspend fun clerkInit(): String

expect suspend fun clerkSignIn(): String

expect suspend fun clerkSignUp(): String

expect suspend fun clerkSignOut(): String

expect suspend fun clerkGetUser(): ClerkUserData?

data class ClerkUserData(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
) {
    val fullName: String
        get() = listOf(firstName, lastName).filter { it.isNotBlank() }.joinToString(" ").ifBlank { email }
}
