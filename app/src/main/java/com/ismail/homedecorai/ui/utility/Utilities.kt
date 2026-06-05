package com.ismail.homedecorai.ui.utility

import android.app.Activity
import android.content.ContentValues
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.ismail.homedecorai.BoardItem
import com.ismail.homedecorai.BuildConfig
import com.ismail.homedecorai.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

fun createCameraUri(context: android.content.Context): Uri {
    val imageDir = File(context.cacheDir, "camera").apply { mkdirs() }
    val imageFile = File(imageDir, "homedecor-${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
}

@Suppress("DEPRECATION")
suspend fun saveResultToGallery(context: android.content.Context, result: BoardItem?): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val displayName = "homedecor-ai-${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/HomeDecor AI")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: return@withContext false
            context.contentResolver.openOutputStream(uri)?.use { output ->
                writeResultImage(context, result, output)
            } ?: return@withContext false
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, values, null, null)
        } else {
            val bitmap = resultBitmap(context, result)
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, displayName, "HomeDecor AI") ?: return@withContext false
        }
        true
    }.getOrDefault(false)
}

suspend fun shareResult(context: android.content.Context, result: BoardItem?): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val shareDir = File(context.cacheDir, "share").apply { mkdirs() }
        val file = File(shareDir, "homedecor-ai-${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { output -> writeResultImage(context, result, output) }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        withContext(Dispatchers.Main) {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_design_chooser)))
        }
        true
    }.getOrDefault(false)
}

fun writeResultImage(context: android.content.Context, result: BoardItem?, output: java.io.OutputStream) {
    val imageUri = result?.imageUri
    if (!imageUri.isNullOrBlank()) {
        context.contentResolver.openInputStream(Uri.parse(imageUri))?.use { input -> input.copyTo(output) }
        return
    }
    val imageUrl = result?.imageUrl
    if (!imageUrl.isNullOrBlank()) {
        URL(imageUrl).openStream().use { input -> input.copyTo(output) }
        return
    }
    resultBitmap(context, result).compress(Bitmap.CompressFormat.JPEG, 94, output)
}

fun resultBitmap(context: android.content.Context, result: BoardItem?): Bitmap {
    result?.imageUri?.takeIf { it.isNotBlank() }?.let { imageUri ->
        context.contentResolver.openInputStream(Uri.parse(imageUri))?.use { input ->
            BitmapFactory.decodeStream(input)?.let { return it }
        }
    }
    val imageRes = result?.imageRes ?: R.drawable.sample_after_luxury
    return BitmapFactory.decodeResource(context.resources, imageRes)
}

fun openAuth(context: android.content.Context) {
    val authUrl = BuildConfig.APP_URL.trimEnd('/') + "/sign-in"
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)))
    }.onFailure {
        Toast.makeText(context, context.getString(R.string.auth_open_failed), Toast.LENGTH_LONG).show()
    }
}

fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun appUrl(path: String): String {
    val base = BuildConfig.APP_URL.trim().trimEnd('/').ifBlank { "https://homedecor.ai" }
    return if (path.isBlank()) base else base + path
}

fun openUrlSafely(context: android.content.Context, url: String): Boolean {
    return runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }.isSuccess
}

fun shareTextSafely(context: android.content.Context, text: String): Boolean {
    return runCatching {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_app_chooser)))
    }.isSuccess
}

fun openUrl(context: android.content.Context, url: String) {
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }.onFailure {
        showToast(context, context.getString(R.string.open_link_failed))
    }
}

fun openGooglePlayReview(context: android.content.Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
    runCatching {
        context.startActivity(intent)
    }.onFailure {
        openUrlSafely(context, "https://play.google.com/store/apps/details?id=${context.packageName}")
    }
}

tailrec fun android.content.Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
