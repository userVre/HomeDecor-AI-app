package com.ismail.homedecorai.ui.upgrade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.R
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.ui.theme.*

private data class ProBenefit(
    val titleRes: Int,
    val subtitleRes: Int,
)

private val proBenefits = listOf(
    ProBenefit(R.string.pro_benefit_unlimited_title, R.string.pro_benefit_unlimited_sub),
    ProBenefit(R.string.pro_benefit_4k_title, R.string.pro_benefit_4k_sub),
    ProBenefit(R.string.pro_benefit_watermark_title, R.string.pro_benefit_watermark_sub),
    ProBenefit(R.string.pro_benefit_faster_title, R.string.pro_benefit_faster_sub),
    ProBenefit(R.string.pro_benefit_styles_title, R.string.pro_benefit_styles_sub),
    ProBenefit(R.string.pro_benefit_history_title, R.string.pro_benefit_history_sub),
)

@Composable
fun UpgradeScreen(
    state: HomeDecorUiState,
    onOpenPaywall: () -> Unit,
) {
    if (state.isPro) {
        ProActiveScreen()
    } else {
        ProLandingScreen(onOpenPaywall = onOpenPaywall)
    }
}

@Composable
private fun ProActiveScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.statusBars),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Xl),
        ) {
            Surface(
                shape = CircleShape,
                color = ProCheckGreen.copy(alpha = 0.15f),
                modifier = Modifier.size(72.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = ProCheckGreen,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }
            Text(
                text = stringResource(R.string.pro_activated),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.active_pro_access),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ProLandingScreen(onOpenPaywall: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = PaddingValues(bottom = navBarBottomPadding()),
    ) {
        item { ProHeroHeader() }
        item { ProBenefitsList() }
        item { ProCtaSection(onOpenPaywall = onOpenPaywall) }
    }
}

@Composable
private fun ProHeroHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ProDarkStart, ProDarkMid, ProDarkEnd),
                ),
            )
            .padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Xxl),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ProBadgeBg,
            ) {
                Row(
                    Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = ProBadgeText,
                    )
                    Text(
                        stringResource(R.string.pro_badge_label),
                        color = ProBadgeText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp,
                        ),
                    )
                }
            }
            Spacer(Modifier.height(HomeDecorSpacing.Xs))
            Text(
                text = stringResource(R.string.pro_intro_headline),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ProTextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
            )
            Text(
                text = stringResource(R.string.pro_hero_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = ProTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )
        }
    }
}

@Composable
private fun ProBenefitsList() {
    Column(
        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Lg),
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
    ) {
        proBenefits.forEach { benefit ->
            ProBenefitRow(
                title = stringResource(benefit.titleRes),
                subtitle = stringResource(benefit.subtitleRes),
            )
        }
    }
}

@Composable
private fun ProBenefitRow(title: String, subtitle: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ProCardSurface,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
        ) {
            Surface(
                shape = CircleShape,
                color = ProAccent.copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = ProAccent,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = ProTextPrimary,
                )
                Spacer(Modifier.height(HomeDecorSpacing.Xxs))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = ProTextMuted,
                    lineHeight = 18.sp,
                )
            }
        }
    }
}

@Composable
private fun ProCtaSection(onOpenPaywall: () -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Base),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
    ) {
        Button(
            onClick = onOpenPaywall,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ProAccent,
                contentColor = Color.White,
            ),
            contentPadding = PaddingValues(),
            modifier = Modifier
                .fillMaxWidth()
                .height(HomeDecorSpacing.ButtonHeight),
        ) {
            Text(
                text = stringResource(R.string.pro_cta_start_trial),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = stringResource(R.string.pro_trial_note),
            style = MaterialTheme.typography.bodySmall,
            color = ProTextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
        )
    }
}
