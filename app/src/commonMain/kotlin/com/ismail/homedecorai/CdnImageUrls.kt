package com.ismail.homedecorai

/**
 * Maps internal image resource paths to CDN URLs for async loading.
 *
 * CDN_BASE should point to your R2/S3 bucket with public-read access.
 * Images are stored as webp at the same relative path structure.
 */
object CdnImages {
    const val CDN_BASE = "https://cdn.homedecor.ai"

    /**
     * Resolves an internal image path (e.g. "images/tool_interior.webp")
     * to a full CDN URL. Returns null for empty input.
     */
    fun resolve(internalPath: String): String? {
        if (internalPath.isBlank()) return null
        val cleaned = internalPath.removePrefix("images/").removeSuffix(".webp")
        return "$CDN_BASE/images/$cleaned.webp"
    }

    /**
     * Resolves an image name (without .webp extension) to a full CDN URL.
     */
    fun resolveName(name: String): String? {
        if (name.isBlank()) return null
        return "$CDN_BASE/images/$name.webp"
    }
}
