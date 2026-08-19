package com.ismail.homedecorai.imagepicker

import androidx.compose.runtime.Composable

@Composable
actual fun rememberDragDropHandler(
    onImageDropped: (PickedImageData) -> Unit,
    onDragEnter: () -> Unit,
    onDragLeave: () -> Unit,
) {
    // Android uses native image picker; drag-and-drop is web-only
}
