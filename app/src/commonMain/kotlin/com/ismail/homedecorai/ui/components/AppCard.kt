package com.ismail.homedecorai.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.ui.theme.AppTokens
import com.ismail.homedecorai.ui.theme.HomeDecorShape
import com.ismail.homedecorai.ui.theme.HomeDecorType

// ---------------------------------------------------------------------------
// AppCard  –  Standard card composable for consistent depth & typography
// ---------------------------------------------------------------------------
// shape: 20.dp (AppTokens.CardRadius)
// tonalElevation: 2.dp
// shadow: 1.dp
// border: 1dp outlineVariant at 40% opacity
//
// Title: 16sp Medium, maxLines 1
// Subtitle: 13sp Regular, maxLines 2, minHeight 36dp
// ---------------------------------------------------------------------------

private val CardShadowShape = HomeDecorShape.Card

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 1.dp,
                shape = CardShadowShape,
                ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                spotColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.10f),
            )
            .clip(CardShadowShape),
        shape = HomeDecorShape.Card,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.40f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            if (!title.isNullOrBlank()) {
                Text(
                    text = title,
                    style = HomeDecorType.TitleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = HomeDecorType.CardSubtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 36.dp)
                        .padding(top = if (title.isNullOrBlank()) 0.dp else 4.dp),
                )
            }
            content()
        }
    }
}
