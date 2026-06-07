package com.ismail.homedecorai.ui.designviewer

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.ThumbDown
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.BoardItem
import com.ismail.homedecorai.R
import com.ismail.homedecorai.isGeneratedResult
import com.ismail.homedecorai.ui.components.WorkspaceImage
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.saveResultToGallery
import com.ismail.homedecorai.ui.utility.shareResult
import kotlinx.coroutines.launch

@Composable
fun DesignViewerSheet(
    result: BoardItem?,
    onBack: () -> Unit,
    onRegenerate: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onLike: (() -> Unit)? = null,
    onDislike: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var feedbackState by remember { mutableStateOf<String?>(null) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            ),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    onClick = onBack,
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(44.dp),
                ) {
                    Icon(
                        Icons.Rounded.Diamond,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp).size(24.dp),
                        tint = StudioBlue,
                    )
                }
                Text(
                    stringResource(R.string.your_design),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Surface(
                    onClick = onBack,
                    shape = CircleShape,
                    color = Color(0xFFF0EDE8),
                    modifier = Modifier.size(44.dp),
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.close),
                        modifier = Modifier.padding(10.dp).size(24.dp),
                        tint = StudioInk,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (result != null && result.isGeneratedResult() && result.status != "failed") {
                var comparePosition by remember(result.id) { mutableStateOf(0.5f) }
                val hasSourceImage = !result.sourceImageUrl.isNullOrBlank() || !result.sourceImageUri.isNullOrBlank()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(StudioMist),
                ) {
                    WorkspaceImage(
                        imageUrl = result.imageUrl,
                        imageUri = result.imageUri,
                        imageRes = result.imageRes,
                        contentDescription = stringResource(R.string.after),
                        modifier = Modifier.fillMaxSize(),
                    )

                    if (hasSourceImage) {
                        WorkspaceImage(
                            imageUrl = result.sourceImageUrl,
                            imageUri = result.sourceImageUri,
                            contentDescription = stringResource(R.string.before),
                            modifier = Modifier
                                .fillMaxSize()
                                .drawWithContent {
                                    clipRect(right = size.width * comparePosition) {
                                        this@drawWithContent.drawContent()
                                    }
                                },
                        )
                        Canvas(Modifier.matchParentSize()) {
                            val handleX = size.width * comparePosition
                            drawLine(
                                color = Color.White,
                                start = Offset(handleX, 0f),
                                end = Offset(handleX, size.height),
                                strokeWidth = 3.dp.toPx(),
                                cap = StrokeCap.Round,
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 20.dp.toPx(),
                                center = Offset(handleX, size.height / 2f),
                            )
                            val barWidth = 3.dp.toPx()
                            val barHeight = 14.dp.toPx()
                            val barGap = 3.dp.toPx()
                            val centerY = size.height / 2f
                            drawLine(
                                color = StudioInk,
                                start = Offset(handleX - barGap - barWidth / 2, centerY - barHeight / 2),
                                end = Offset(handleX - barGap - barWidth / 2, centerY + barHeight / 2),
                                strokeWidth = barWidth,
                                cap = StrokeCap.Round,
                            )
                            drawLine(
                                color = StudioInk,
                                start = Offset(handleX + barGap + barWidth / 2, centerY - barHeight / 2),
                                end = Offset(handleX + barGap + barWidth / 2, centerY + barHeight / 2),
                                strokeWidth = barWidth,
                                cap = StrokeCap.Round,
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .size(40.dp),
                    ) {
                        Icon(
                            Icons.Rounded.Diamond,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp).size(24.dp),
                            tint = StudioBlue,
                        )
                    }

                    Surface(
                        onClick = { onDelete?.invoke() },
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.35f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(40.dp),
                    ) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = stringResource(R.string.delete_image),
                            modifier = Modifier.padding(8.dp).size(24.dp),
                            tint = Color.White,
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.35f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .size(40.dp),
                    ) {
                        Icon(
                            Icons.Rounded.Lock,
                            contentDescription = stringResource(R.string.lock),
                            modifier = Modifier.padding(10.dp).size(20.dp),
                            tint = Color.White,
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = StudioPaper,
                        tonalElevation = 1.dp,
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                stringResource(R.string.room_label),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                result.roomType.ifBlank { stringResource(R.string.room_type) },
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = StudioPaper,
                        tonalElevation = 1.dp,
                        modifier = Modifier.weight(1f),
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                stringResource(R.string.style_label),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                result.style.ifBlank { stringResource(R.string.style_to_choose) },
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(
                        onClick = { onRegenerate?.invoke() },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudioBlue),
                        modifier = Modifier.weight(1f).height(52.dp),
                    ) {
                        Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.regenerate), fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                val saved = saveResultToGallery(context, result)
                                Toast.makeText(context, if (saved) context.getString(R.string.toast_design_downloaded) else context.getString(R.string.toast_design_save_failed), Toast.LENGTH_LONG).show()
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudioBlue),
                        modifier = Modifier.weight(1f).height(52.dp),
                    ) {
                        Icon(Icons.Rounded.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.save), fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                val shared = shareResult(context, result)
                                if (!shared) Toast.makeText(context, context.getString(R.string.toast_share_failed), Toast.LENGTH_LONG).show()
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudioBlue),
                        modifier = Modifier.weight(1f).height(52.dp),
                    ) {
                        Icon(Icons.Rounded.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.share), fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                Spacer(Modifier.height(16.dp))

                HorizontalDivider(color = StudioLine)

                Spacer(Modifier.height(12.dp))

                Text(
                    stringResource(R.string.rate_this_result),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                ) {
                    OutlinedButton(
                        onClick = {
                            feedbackState = "liked"
                            onLike?.invoke()
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (feedbackState == "liked") StudioPrimaryContainer else Color.Transparent,
                        ),
                        modifier = Modifier.height(44.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Icon(
                            Icons.Rounded.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (feedbackState == "liked") StudioBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.like), fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = {
                            feedbackState = "disliked"
                            onDislike?.invoke()
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (feedbackState == "disliked") StudioErrorContainer else Color.Transparent,
                        ),
                        modifier = Modifier.height(44.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Icon(
                            Icons.Rounded.ThumbDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (feedbackState == "disliked") StudioRose else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.dislike), fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(Modifier.height(16.dp))
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(shape = CircleShape, color = StudioMist, modifier = Modifier.size(72.dp)) {
                            Icon(Icons.Rounded.Diamond, null, Modifier.padding(18.dp).size(36.dp), tint = StudioLine)
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            stringResource(R.string.no_designs_yet),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.no_designs_body),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
