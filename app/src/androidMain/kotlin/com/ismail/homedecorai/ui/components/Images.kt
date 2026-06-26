package com.ismail.homedecorai.ui.components

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun PreviewTile(
    title: String,
    uri: Uri?,
    imageRes: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.fillMaxWidth().aspectRatio(1.08f).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow)) {
            UriOrResourceImage(
                uri = uri,
                imageRes = imageRes,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun UriOrResourceImage(
    uri: Uri?,
    imageRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    var bitmap by remember(uri) { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    val context = LocalContext.current
    LaunchedEffect(uri) {
        bitmap = null
        if (uri != null) {
            bitmap = withContext(Dispatchers.IO) {
                runCatching {
                    val sourceBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                    } else {
                        context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream)
                    }
                    sourceBitmap?.asImageBitmap()
                }.getOrNull()
            }
        }
    }
    val currentBitmap = bitmap
    if (currentBitmap != null) {
        Image(bitmap = currentBitmap, contentDescription = contentDescription, modifier = modifier, contentScale = ContentScale.Crop)
    } else {
        Image(painter = painterResource(imageRes), contentDescription = contentDescription, modifier = modifier, contentScale = ContentScale.Crop)
    }
}

@Composable
fun WorkspaceImage(
    imageUrl: String?,
    imageUri: String?,
    imageRes: Int = R.drawable.profile_workspace,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    if (!imageUri.isNullOrBlank()) {
        UriOrResourceImage(
            uri = runCatching { Uri.parse(imageUri) }.getOrNull(),
            imageRes = imageRes,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    } else {
        NetworkOrResourceImage(
            imageUrl = imageUrl,
            imageRes = imageRes,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}

@Composable
fun NetworkOrResourceImage(
    imageUrl: String?,
    imageRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    var bitmap by remember(imageUrl) { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    var loading by remember(imageUrl) { mutableStateOf(!imageUrl.isNullOrBlank()) }
    var failed by remember(imageUrl) { mutableStateOf(false) }
    LaunchedEffect(imageUrl) {
        bitmap = null
        failed = false
        loading = !imageUrl.isNullOrBlank()
        if (!imageUrl.isNullOrBlank()) {
            bitmap = withContext(Dispatchers.IO) {
                runCatching {
                    URL(imageUrl).openStream().use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
                }.getOrNull()
            }
            failed = bitmap == null
        }
        loading = false
    }
    val currentBitmap = bitmap
    when {
        currentBitmap != null -> {
            Image(
                bitmap = currentBitmap,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop,
            )
        }
        loading -> {
            ImageLoadingState(modifier = modifier)
        }
        failed -> {
            ImageFailureState(modifier = modifier)
        }
        else -> {
            Image(
                painter = painterResource(imageRes),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun ImageLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerLow),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.padding(10.dp).size(20.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Text(
                stringResource(R.string.image_loading_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(0.72f).height(6.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant,
            )
            Text(
                stringResource(R.string.image_loading_body),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ImageFailureState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.error)
            Text(stringResource(R.string.image_unavailable_title), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
            Text(stringResource(R.string.image_unavailable_body), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
        }
    }
}
