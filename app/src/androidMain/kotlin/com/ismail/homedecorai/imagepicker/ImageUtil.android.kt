package com.ismail.homedecorai.imagepicker

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual fun decodeImageToBitmap(
    bytes: ByteArray,
    maxDimension: Int,
): ImageBitmap? {
    return try {
        // Decode bounds only first to check dimensions
        val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
        val w = opts.outWidth
        val h = opts.outHeight
        if (w <= 0 || h <= 0) return null

        // Calculate inSampleSize for downscaling
        var inSampleSize = 1
        if (maxOf(w, h) > maxDimension) {
            while (maxOf(w / inSampleSize, h / inSampleSize) > maxDimension) {
                inSampleSize *= 2
            }
        }

        val decodeOpts = BitmapFactory.Options().apply { this.inSampleSize = inSampleSize }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, decodeOpts)?.asImageBitmap()
    } catch (_: Exception) {
        null
    }
}
