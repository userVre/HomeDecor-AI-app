package com.ismail.homedecorai.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*

/**
 * Animated validation alert banner shown when the user taps Continue/Generate without meeting step requirements.
 * Displays a lock icon and dynamic message text.
 */
@Composable
fun ValidationAlertBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message.isNotBlank(),
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Assertive },
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    Icons.Rounded.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

/**
 * Unified wizard error banner that replaces all scattered error displays.
 * Handles generation errors, validation errors, and network errors consistently.
 * Shows retry button for recoverable errors and dismiss for all.
 */
@Composable
fun UnifiedWizardError(
    message: String,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message.isNotBlank(),
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.semantics { liveRegion = LiveRegionMode.Assertive },
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = StudioErrorContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        Icons.Rounded.ErrorOutline,
                        contentDescription = null,
                        tint = StudioRose,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = StudioRose,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (onRetry != null || onDismiss != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        onRetry?.let { retry ->
                            Button(
                                onClick = retry,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = StudioRose,
                                    contentColor = Color.White,
                                ),
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(stringResource(R.string.retry), fontWeight = FontWeight.SemiBold)
                            }
                        }
                        onDismiss?.let { dismiss ->
                            TextButton(onClick = dismiss) {
                                Text(stringResource(R.string.dismiss))
                            }
                        }
                    }
                }
            }
        }
    }
}
