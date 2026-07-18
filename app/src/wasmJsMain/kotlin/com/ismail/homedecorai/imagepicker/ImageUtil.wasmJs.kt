package com.ismail.homedecorai.imagepicker

import androidx.compose.ui.graphics.ImageBitmap

/**
 * WasmJS actual: on this target, image bytes from the file picker are
 * already decoded by the browser. We return null to signal the caller
 * to use the raw sourceUri-based loading path instead.
 *
 * If Skia's toComposeImageBitmap becomes available in future Skiko
 * versions, this can be upgraded to do proper downscaling.
 */
actual fun decodeImageToBitmap(
    bytes: ByteArray,
    maxDimension: Int,
): ImageBitmap? {
    // Browser handles image decoding natively; return null so
    // LocalImagePreview falls back to sourceUri-based loading.
    return null
}
