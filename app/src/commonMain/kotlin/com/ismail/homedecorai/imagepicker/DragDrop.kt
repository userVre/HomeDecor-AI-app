package com.ismail.homedecorai.imagepicker

import androidx.compose.runtime.Composable

@Composable
expect fun rememberDragDropHandler(
    onImageDropped: (PickedImageData) -> Unit,
    onDragEnter: () -> Unit,
    onDragLeave: () -> Unit,
)
