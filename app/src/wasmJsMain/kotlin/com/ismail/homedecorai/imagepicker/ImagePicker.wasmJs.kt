package com.ismail.homedecorai.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@JsFun("() => { if (!window._ipRegistry) { window._ipRegistry = {}; window._ipResults = {}; } }")
private external fun initRegistry()

@JsFun("() => { const el = document.createElement('input'); el.type = 'file'; el.accept = 'image/*'; document.body.appendChild(el); return el; }")
private external fun createFileInput(): JsAny

@JsFun("(el) => { el.click(); }")
private external fun triggerClick(el: JsAny)

@JsFun("(el, id) => { el.onchange = function() { if (this.files && this.files.length > 0) { var file = this.files[0]; var reader = new FileReader(); reader.onload = function() { var arr = new Uint8Array(reader.result); var copy = []; for (var i = 0; i < arr.length; i++) copy.push(arr[i]); window._ipResults[id] = { bytes: copy, mime: file.type, done: 1 }; }; reader.readAsArrayBuffer(file); } else { window._ipResults[id] = { bytes: [], mime: '', done: 1 }; } this.remove(); }; }")
private external fun setupListener(el: JsAny, id: Int)

@JsFun("(id) => { var r = window._ipResults[id]; return r ? r.done : 0; }")
private external fun isReady(id: Int): Int

@JsFun("(id) => { var r = window._ipResults[id]; return r ? r.mime : ''; }")
private external fun getMime(id: Int): String

@JsFun("(id) => { var r = window._ipResults[id]; return r ? r.bytes.length : 0; }")
private external fun getLength(id: Int): Int

@JsFun("(id, i) => { var r = window._ipResults[id]; return r ? r.bytes[i] : 0; }")
private external fun getByte(id: Int, i: Int): Int

@JsFun("(id) => { delete window._ipResults[id]; }")
private external fun cleanupResult(id: Int)

@Composable
actual fun rememberImagePicker(
    onImageSelected: (PickedImageData) -> Unit,
): ImagePickerActions {
    val requestId = remember { mutableIntStateOf(0) }
    var collectedBytes by remember { mutableStateOf<ByteArray?>(null) }
    var collectedMime by remember { mutableStateOf("") }
    var ready by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(ready) {
        if (ready && collectedBytes != null) {
            onImageSelected(PickedImageData(imageBytes = collectedBytes!!, mimeType = collectedMime))
            collectedBytes = null
            collectedMime = ""
            ready = false
        }
    }

    fun openPicker() {
        initRegistry()
        val nextId = requestId.intValue + 1
        requestId.intValue = nextId
        val input = createFileInput()
        setupListener(input, nextId)
        triggerClick(input)

        scope.launch {
            var attempts = 0
            while (attempts < 200) {
                delay(50)
                attempts++
                if (isReady(nextId) == 1) {
                    val len = getLength(nextId)
                    val mime = getMime(nextId)
                    val bytes = ByteArray(len)
                    for (i in 0 until len) {
                        bytes[i] = getByte(nextId, i).toByte()
                    }
                    cleanupResult(nextId)
                    collectedBytes = bytes
                    collectedMime = mime.ifEmpty { "image/jpeg" }
                    ready = true
                    return@launch
                }
            }
        }
    }

    return ImagePickerActions(
        openGallery = { openPicker() },
        openCamera = { openPicker() },
    )
}
