package com.ismail.homedecorai.imagepicker

data class PickedImageData(
    val sourceUri: String = "",
    val imageBytes: ByteArray? = null,
    val mimeType: String = "image/jpeg",
) {
    val isValid: Boolean get() = sourceUri.isNotEmpty() || imageBytes != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PickedImageData) return false
        return sourceUri == other.sourceUri &&
            imageBytes.contentEquals(other.imageBytes) &&
            mimeType == other.mimeType
    }

    override fun hashCode(): Int {
        var result = sourceUri.hashCode()
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        result = 31 * result + mimeType.hashCode()
        return result
    }
}

data class ImagePickerActions(
    val openGallery: () -> Unit,
    val openCamera: () -> Unit,
)
