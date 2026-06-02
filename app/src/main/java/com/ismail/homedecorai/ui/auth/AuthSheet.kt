package com.ismail.homedecorai.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.theme.*

@Composable
fun AuthSheet(
    onClose: () -> Unit,
    onAuth: () -> Unit,
) {
    val modalTapBlocker = remember { MutableInteractionSource() }
    Box(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .clickable(
                interactionSource = modalTapBlocker,
                indication = null,
                onClick = {},
            ),
    ) {
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd).windowInsetsPadding(WindowInsets.statusBars).padding(16.dp).size(48.dp)) {
            Icon(Icons.Rounded.Close, contentDescription = stringResource(R.string.close))
        }
        Column(
            Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(shape = CircleShape, color = StudioPrimaryContainer, tonalElevation = 5.dp) {
                Icon(Icons.Rounded.Diamond, null, Modifier.padding(18.dp).size(38.dp), tint = StudioInk)
            }
            Spacer(Modifier.height(20.dp))
            Text(stringResource(R.string.auth_welcome), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(
                stringResource(R.string.auth_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(22.dp))
            ElevatedCard(shape = RoundedCornerShape(18.dp), colors = CardDefaults.elevatedCardColors(containerColor = StudioMist)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedButton(onClick = onAuth, shape = CircleShape, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                        Text(stringResource(R.string.google_initial), color = StudioBlue, fontWeight = FontWeight.Black)
                        Spacer(Modifier.width(10.dp))
                        Text(stringResource(R.string.continue_with_google))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.weight(1f).height(1.dp).background(StudioLine))
                        Text(stringResource(R.string.or), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Box(Modifier.weight(1f).height(1.dp).background(StudioLine))
                    }
                    Button(
                        onClick = onAuth,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                    ) {
                        Text(stringResource(R.string.sign_in))
                    }
                    Text(
                        stringResource(R.string.auth_real_flow_note),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
