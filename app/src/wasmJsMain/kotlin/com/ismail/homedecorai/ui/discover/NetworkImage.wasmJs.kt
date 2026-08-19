package com.ismail.homedecorai.ui.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.CdnImages
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image

private var fetchRequestId = 0

@JsFun("""() => {
    if (!window._cdnRegistry) { window._cdnRegistry = {}; window._cdnResults = {}; }
}""")
private external fun initCdnRegistry()

@JsFun("""(url, id) => {
    window._cdnResults[id] = { bytes: [], done: 0, ok: 0 };
    fetch(url).then(function(resp) {
        if (!resp.ok) { window._cdnResults[id].done = 1; return; }
        return resp.arrayBuffer();
    }).then(function(ab) {
        if (!ab) return;
        var arr = new Uint8Array(ab);
        var copy = [];
        for (var i = 0; i < arr.length; i++) copy.push(arr[i]);
        window._cdnResults[id] = { bytes: copy, done: 1, ok: 1 };
    }).catch(function() {
        window._cdnResults[id].done = 1;
    });
}""")
private external fun fetchCdnImage(url: JsAny, id: Int)

@JsFun("(id) => { var r = window._cdnResults[id]; return r ? r.done : 0; }")
private external fun isCdnReady(id: Int): Int

@JsFun("(id) => { var r = window._cdnResults[id]; return r ? r.ok : 0; }")
private external fun isCdnOk(id: Int): Int

@JsFun("(id) => { var r = window._cdnResults[id]; return r ? r.bytes.length : 0; }")
private external fun getCdnLength(id: Int): Int

@JsFun("(id, i) => { var r = window._cdnResults[id]; return r ? r.bytes[i] : 0; }")
private external fun getCdnByte(id: Int, i: Int): Int

@JsFun("(id) => { delete window._cdnResults[id]; }")
private external fun cleanupCdnResult(id: Int)

@JsFun("(s) => { return s; }")
private external fun toJsString(s: String): JsAny

@Composable
actual fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier,
) {
    val cdnUrl = CdnImages.resolve(url)

    if (cdnUrl == null) {
        GradientPlaceholder(contentDescription, modifier)
        return
    }

    var bitmap by remember(cdnUrl) { mutableStateOf<ImageBitmap?>(null) }
    var imageBytes by remember(cdnUrl) { mutableStateOf<ByteArray?>(null) }
    var isDecoding by remember(cdnUrl) { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(cdnUrl) {
        initCdnRegistry()
        val nextId = ++fetchRequestId
        fetchCdnImage(toJsString(cdnUrl), nextId)

        var attempts = 0
        while (attempts < 200) {
            delay(50)
            attempts++
            if (isCdnReady(nextId) == 1) {
                val ok = isCdnOk(nextId)
                if (ok == 1) {
                    val len = getCdnLength(nextId)
                    val bytes = ByteArray(len)
                    for (i in 0 until len) {
                        bytes[i] = getCdnByte(nextId, i).toByte()
                    }
                    cleanupCdnResult(nextId)
                    imageBytes = bytes
                    isDecoding = true
                    bitmap = try {
                        val skiaImage = Image.makeFromEncoded(bytes)
                        skiaImage.toComposeImageBitmap()
                    } catch (_: Exception) {
                        null
                    }
                    isDecoding = false
                } else {
                    cleanupCdnResult(nextId)
                }
                return@LaunchedEffect
            }
        }
    }

    Box(
        modifier = modifier.semantics {
            if (contentDescription != null) {
                this.contentDescription = contentDescription
            }
        },
        contentAlignment = Alignment.Center,
    ) {
        val currentBitmap = bitmap
        if (currentBitmap != null) {
            Image(
                bitmap = currentBitmap,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            GradientPlaceholder(null, Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun GradientPlaceholder(contentDescription: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
                    ),
                ),
            )
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.Rounded.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        )
    }
}
