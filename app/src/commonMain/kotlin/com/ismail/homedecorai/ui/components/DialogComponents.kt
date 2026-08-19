package com.ismail.homedecorai.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.HomeDecorElevation
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorSpacing
import com.ismail.homedecorai.ui.theme.isReducedMotionEnabled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val DIALOG_ANIM_ENTER_MS = 180
private const val DIALOG_ANIM_EXIT_MS = 150

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResponsiveDialog(
    onDismissRequest: () -> Unit,
    title: String,
    subtitle: String? = null,
    footer: @Composable (() -> Unit)? = null,
    maxWidth: Dp = 480.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val isDesktop = rememberIsDesktop()

    if (isDesktop) {
        DesktopDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            subtitle = subtitle,
            footer = footer,
            maxWidth = maxWidth,
            content = content,
        )
    } else {
        MobileDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            subtitle = subtitle,
            footer = footer,
            maxWidth = maxWidth,
            content = content,
        )
    }
}

@Composable
private fun DesktopDialog(
    onDismissRequest: () -> Unit,
    title: String,
    subtitle: String?,
    footer: @Composable (() -> Unit)?,
    maxWidth: Dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val reducedMotion = isReducedMotionEnabled()
    val enterAnim = if (reducedMotion) {
        EnterTransition.None
    } else {
        fadeIn(animationSpec = tween(DIALOG_ANIM_ENTER_MS)) +
            scaleIn(initialScale = 0.95f, animationSpec = tween(DIALOG_ANIM_ENTER_MS, easing = LinearEasing))
    }
    val exitAnim = if (reducedMotion) {
        ExitTransition.None
    } else {
        fadeOut(animationSpec = tween(DIALOG_ANIM_EXIT_MS)) +
            scaleOut(targetScale = 0.95f, animationSpec = tween(DIALOG_ANIM_EXIT_MS))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
            .onPreviewKeyEvent { event ->
                if (event.key == Key.Escape) {
                    onDismissRequest()
                    true
                } else false
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onDismissRequest,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = true,
            enter = enterAnim,
            exit = exitAnim,
        ) {
            Surface(
                shape = HomeDecorShape.Dialog,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = HomeDecorElevation.DialogElevation,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .widthIn(max = maxWidth, min = 320.dp)
                    .fillMaxWidth()
                    .clickable(enabled = false) { },
            ) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                            .heightIn(max = 560.dp),
                    ) {
                        DialogHeader(
                            title = title,
                            subtitle = subtitle,
                            onClose = onDismissRequest,
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Column(
                            modifier = Modifier.padding(
                                horizontal = HomeDecorSpacing.Xl,
                                vertical = HomeDecorSpacing.Lg,
                            ),
                            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
                        ) {
                            content()
                        }
                    }

                    if (footer != null) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        DialogFooter(footer = footer)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MobileDialog(
    onDismissRequest: () -> Unit,
    title: String,
    subtitle: String?,
    footer: @Composable (() -> Unit)?,
    maxWidth: Dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scope = CoroutineScope(kotlinx.coroutines.Dispatchers.Default)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = HomeDecorShape.ModalSheet,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = HomeDecorSpacing.Xl),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .widthIn(max = maxWidth),
            ) {
                DialogHeader(
                    title = title,
                    subtitle = subtitle,
                    onClose = onDismissRequest,
                )
                Column(
                    modifier = Modifier.padding(
                        horizontal = HomeDecorSpacing.Xl,
                        vertical = HomeDecorSpacing.Base,
                    ),
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
                ) {
                    content()
                }
            }

            if (footer != null) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                DialogFooter(footer = footer)
            }
        }
    }
}

@Composable
private fun DialogHeader(
    title: String,
    subtitle: String?,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = HomeDecorSpacing.Xl, end = HomeDecorSpacing.Sm, top = HomeDecorSpacing.Sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() },
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = HomeDecorSpacing.Xs),
                )
            }
        }
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(HomeDecorSpacing.TouchTarget)
                .semantics { contentDescription = Strings.a11yDialogClose },
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DialogFooter(footer: @Composable () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = HomeDecorShape.Dialog,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = HomeDecorSpacing.Xl,
                    vertical = HomeDecorSpacing.Base,
                ),
        ) {
            footer()
        }
    }
}
