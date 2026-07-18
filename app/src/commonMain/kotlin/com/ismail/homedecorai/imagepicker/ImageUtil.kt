package com.ismail.homedecorai.imagepicker

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Decode encoded image bytes into an [ImageBitmap], optionally downscaling
 * to [maxDimension] on the longest edge.
 *
 * On targets where Skia is available (WasmJS, Desktop) this uses
 * `org.jetbrains.skia.Image`. On Android it uses `BitmapFactory`.
 */
expect fun decodeImageToBitmap(
    bytes: ByteArray,
    maxDimension: Int = 1200,
): ImageBitmap?
