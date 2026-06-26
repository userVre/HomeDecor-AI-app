package com.ismail.homedecorai.imagepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.theme.*

@Composable
fun SharedMoziUploadCard(
    onGallery: () -> Unit,
    onCamera: () -> Unit,
    onExample: () -> Unit,
) {
    Surface(
        shape = HomeDecorShape.ExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = Strings.uploadPhotoArea }
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), HomeDecorShape.ExtraLarge),
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Base),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .clip(HomeDecorShape.Large)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.25f),
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(48.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.PhotoCamera,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    }
                    Text(
                        "Upload a photo of your space",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                val galleryInteraction = remember { MutableInteractionSource() }
                val galleryPressed by galleryInteraction.collectIsPressedAsState()
                val galleryScale = if (galleryPressed) 0.95f else 1f

                OutlinedButton(
                    onClick = onGallery,
                    shape = CircleShape,
                    interactionSource = galleryInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .graphicsLayer { scaleX = galleryScale; scaleY = galleryScale }
                        .semantics { contentDescription = Strings.openGallery },
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        Strings.galleryLabel,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                val cameraInteraction = remember { MutableInteractionSource() }
                val cameraPressed by cameraInteraction.collectIsPressedAsState()
                val cameraScale = if (cameraPressed) 0.95f else 1f

                OutlinedButton(
                    onClick = onCamera,
                    shape = CircleShape,
                    interactionSource = cameraInteraction,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .graphicsLayer { scaleX = cameraScale; scaleY = cameraScale }
                        .semantics { contentDescription = Strings.takePhoto },
                    contentPadding = PaddingValues(horizontal = HomeDecorSpacing.Md),
                ) {
                    Icon(Icons.Rounded.PhotoCamera, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        Strings.cameraLabel,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Surface(
                onClick = onExample,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = Strings.wizardTryExample },
            ) {
                Row(
                    Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(HomeDecorSpacing.Xs))
                    Text(
                        Strings.wizardTryExample,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
fun SharedUploadSheet(
    onCamera: () -> Unit,
    onGallery: () -> Unit,
    onExample: () -> Unit,
) {
    Column(
        Modifier.padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Sm),
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
    ) {
            Text(
                Strings.addPhoto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Surface(
            onClick = onCamera,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            color = Color.Transparent,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.padding(vertical = HomeDecorSpacing.Base),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
            ) {
                Icon(Icons.Rounded.PhotoCamera, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Text(Strings.cameraLabel, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        Surface(
            onClick = onGallery,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            color = Color.Transparent,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.padding(vertical = HomeDecorSpacing.Base),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
            ) {
                Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Text(Strings.photosLabel, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        Surface(
            onClick = onExample,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            color = Color.Transparent,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.padding(vertical = HomeDecorSpacing.Base),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
            ) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Text(Strings.wizardTryAgain, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(HomeDecorSpacing.Lg))
    }
}
