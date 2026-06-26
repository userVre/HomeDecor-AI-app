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
import org.w3c.dom.events.Event

@JsFun("""(el, onDragEnter, onDragLeave, onDrop) => {
    el.addEventListener('dragenter', function(e) {
        e.preventDefault();
        e.stopPropagation();
        onDragEnter();
    });
    el.addEventListener('dragover', function(e) {
        e.preventDefault();
        e.stopPropagation();
    });
    el.addEventListener('dragleave', function(e) {
        e.preventDefault();
        e.stopPropagation();
        onDragLeave();
    });
    el.addEventListener('drop', function(e) {
        e.preventDefault();
        e.stopPropagation();
        if (e.dataTransfer && e.dataTransfer.files && e.dataTransfer.files.length > 0) {
            var file = e.dataTransfer.files[0];
            if (file.type.startsWith('image/')) {
                var reader = new FileReader();
                reader.onload = function() {
                    var arr = new Uint8Array(reader.result);
                    var copy = [];
                    for (var i = 0; i < arr.length; i++) copy.push(arr[i]);
                    window._dropResult = { bytes: copy, mime: file.type, done: 1 };
                };
                reader.readAsArrayBuffer(file);
            }
        }
    });
}""")
private external fun setupDragDropHandlers(
    el: JsAny,
    onDragEnter: () -> Unit,
    onDragLeave: () -> Unit,
    onDrop: () -> Unit,
)

@JsFun("() => { return window._dropResult ? window._dropResult.done : 0; }")
private external fun isDropReady(): Int

@JsFun("() => { return window._dropResult ? window._dropResult.mime : ''; }")
private external fun getDropMime(): String

@JsFun("() => { return window._dropResult ? window._dropResult.bytes.length : 0; }")
private external fun getDropLength(): Int

@JsFun("(i) => { return window._dropResult ? window._dropResult.bytes[i] : 0; }")
private external fun getDropByte(i: Int): Int

@JsFun("() => { delete window._dropResult; }")
private external fun cleanupDropResult()

@Composable
actual fun rememberDragDropHandler(
    onImageDropped: (PickedImageData) -> Unit,
    onDragEnter: () -> Unit,
    onDragLeave: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var dropReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val body = document.body
        if (body != null) {
            setupDragDropHandlers(
                body.unsafeCast<JsAny>(),
                onDragEnter,
                onDragLeave,
                { dropReady = true },
            )
        }
    }

    LaunchedEffect(dropReady) {
        if (dropReady) {
            delay(100)
            if (isDropReady() == 1) {
                val len = getDropLength()
                val mime = getDropMime()
                val bytes = ByteArray(len)
                for (i in 0 until len) {
                    bytes[i] = getDropByte(i).toByte()
                }
                cleanupDropResult()
                onImageDropped(PickedImageData(imageBytes = bytes, mimeType = mime.ifEmpty { "image/jpeg" }))
            }
            dropReady = false
        }
    }
}
