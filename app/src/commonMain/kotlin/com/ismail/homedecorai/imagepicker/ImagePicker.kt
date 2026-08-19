package com.ismail.homedecorai.imagepicker

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePicker(
    onImageSelected: (PickedImageData) -> Unit,
): ImagePickerActions
