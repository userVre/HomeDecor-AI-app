package com.ismail.homedecorai.ui.upgrade

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.R
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.ui.theme.*


// ── Theme-aware palette ──────────────────────────────────────────────────────

private class UpgradeColors(
    val surface: Color,
    val cardSurface: Color,
    val accent: Color,
    val accentLight: Color,
    val accentSurface: Color,
    val gold: Color,
    val mint: Color,
    val mintSurface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val border: Color,
    val checkGreen: Color,
    val ctaDisabled: Color,
    val heroDivider: Color,
) {
    companion object {
        val Light = UpgradeColors(
            surface = ProSurface,
            cardSurface = ProCardSurface,
            accent = ProAccent,
            accentLight = ProAccentLight,
            accentSurface = ProAccentSurface,
            gold = ProGold,
            mint = ProMint,
            mintSurface = ProMint.copy(alpha = 0.08f),
            textPrimary = ProTextPrimary,
            textSecondary = ProTextSecondary,
            textMuted = ProTextMuted,
            border = ProBorder,
            checkGreen = ProCheckGreen,
            ctaDisabled = ProCtaDisabled,
            heroDivider = ProHeroDivider,
        )
        val Dark = UpgradeColors(
            surface = ProDarkSurface,
            cardSurface = ProDarkCardSurface,
            accent = ProDarkAccent,
            accentLight = ProDarkAccentLight,
            accentSurface = ProDarkAccentSurface,
            gold = ProDarkGold,
            mint = ProDarkMint,
            mintSurface = ProDarkMint.copy(alpha = 0.08f),
            textPrimary = ProDarkTextPrimary,
            textSecondary = ProDarkTextSecondary,
            textMuted = ProDarkTextMuted,
            border = ProDarkBorder,
            checkGreen = HomeDecorColors.DarkCheckGreen,
            ctaDisabled = ProDarkCtaDisabled,
            heroDivider = ProDarkHeroDivider,
        )
    }
}

// ── Main screen ──────────────────────────────────────────────────────────────

@Composable
fun UpgradeScreen(
    state: HomeDecorUiState,
    onOpenPaywall: () -> Unit,
) {
    if (state.isPro) {
        ProActiveScreen()
    } else {
        UpgradeV3Screen(onOpenPaywall = onOpenPaywall)
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

// ── Upgrade V3: Premium, MD3 Expressive, conversion-focused ────────────────

@Composable
private fun UpgradeV3Screen(onOpenPaywall: () -> Unit) {
    val dark = isSystemInDarkTheme()
    val colors = remember(dark) { if (dark) UpgradeColors.Dark else UpgradeColors.Light }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val ctaScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cta_scale",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.surface)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = HomeDecorSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(HomeDecorSpacing.Base))

            // Headline — strong, clear
            Text(
                text = stringResource(R.string.upgrade_v3_headline),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    letterSpacing = (-0.5).sp,
                ),
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(8.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.upgrade_v3_subtitle),
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(12.dp))

            // Trial info — shown early
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = colors.accent.copy(alpha = 0.08f),
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        stringResource(R.string.upgrade_v3_trial_badge) + ". " + stringResource(R.string.upgrade_v3_trust),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = colors.accent,
                    )
                }
            }

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            // Before/After hero — strong visual
            BeforeAfterHero(colors = colors)

            Spacer(Modifier.height(HomeDecorSpacing.Base))

            // Benefit cards — clean, spacious, not cramped
            UpgradeBenefitRow(
                colors = colors,
                label = stringResource(R.string.upgrade_v3_benefit_generations),
                iconTint = colors.accent,
            )
            Spacer(Modifier.height(10.dp))
            UpgradeBenefitRow(
                colors = colors,
                label = stringResource(R.string.upgrade_v3_benefit_export),
                iconTint = colors.accent,
            )
            Spacer(Modifier.height(10.dp))
            UpgradeBenefitRow(
                colors = colors,
                label = stringResource(R.string.upgrade_v3_benefit_no_watermark),
                iconTint = colors.checkGreen,
            )
            Spacer(Modifier.height(10.dp))
            UpgradeBenefitRow(
                colors = colors,
                label = stringResource(R.string.upgrade_v3_benefit_speed),
                iconTint = colors.mint,
            )
            Spacer(Modifier.height(10.dp))
            UpgradeBenefitRow(
                colors = colors,
                label = stringResource(R.string.upgrade_v3_benefit_styles),
                iconTint = colors.gold,
            )
            Spacer(Modifier.height(10.dp))
            UpgradeBenefitRow(
                colors = colors,
                label = stringResource(R.string.upgrade_v3_benefit_history),
                iconTint = colors.accentLight,
            )

            Spacer(Modifier.height(120.dp)) // space for sticky CTA
        }

        // ── Sticky CTA bar ──
        Surface(
            color = colors.surface,
            shadowElevation = 8.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HomeDecorSpacing.Base)
                    .padding(bottom = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onOpenPaywall,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accent,
                        contentColor = Color.White,
                    ),
                    contentPadding = PaddingValues(),
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.ButtonHeight)
                        .scale(ctaScale),
                ) {
                    Text(
                        text = stringResource(R.string.upgrade_v3_cta),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text = stringResource(R.string.upgrade_v3_trust),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textMuted,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.upgrade_v3_secondary),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = colors.textMuted,
                    modifier = Modifier.clickable { },
                )
            }
        }
    }
}

// ── Benefit Row ──────────────────────────────────────────────────────────────

@Composable
private fun UpgradeBenefitRow(
    colors: UpgradeColors,
    label: String,
    iconTint: Color,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = CircleShape,
                color = iconTint.copy(alpha = 0.12f),
                modifier = Modifier.size(32.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                ),
                color = colors.textPrimary,
            )
        }
    }
}

// ── Before/After Hero — Interactive Slider ────────────────────────────────────

@Composable
private fun BeforeAfterHero(colors: UpgradeColors) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow_alpha",
    )

    // Slider position: 0f = fully before, 1f = fully after; start at 0.48f
    val sliderProgress = remember { androidx.compose.animation.core.Animatable(0.48f) }
    var isDragging by remember { mutableStateOf(false) }

    // Animate in on first display
    LaunchedEffect(Unit) {
        sliderProgress.animateTo(
            targetValue = 0.48f,
            animationSpec = tween(durationMillis = 800, easing = EaseInOut),
        )
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.5.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp)),
        ) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clickable { },
            ) {
                val w = size.width
                val h = size.height
                val dividerX = w * sliderProgress.value

                // ── BEFORE scene (left side, clipped to left of divider) ──
                drawContext.canvas.saveLayer(
                    androidx.compose.ui.geometry.Rect(0f, 0f, dividerX, h),
                    androidx.compose.ui.graphics.Paint(),
                )
                // Room outline
                drawRoundRect(
                    color = colors.textMuted.copy(alpha = 0.10f),
                    topLeft = Offset(w * 0.02f, h * 0.08f),
                    size = Size(w * 0.44f, h * 0.74f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
                // Plain sofa
                drawRoundRect(
                    color = colors.textMuted.copy(alpha = 0.18f),
                    topLeft = Offset(w * 0.08f, h * 0.48f),
                    size = Size(w * 0.30f, h * 0.18f),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                // Floor line
                drawLine(
                    color = colors.textMuted.copy(alpha = 0.12f),
                    start = Offset(w * 0.04f, h * 0.66f),
                    end = Offset(w * 0.44f, h * 0.66f),
                    strokeWidth = 1.dp.toPx(),
                )
                drawContext.canvas.restore()

                // ── AFTER scene (right side, clipped to right of divider) ──
                drawContext.canvas.saveLayer(
                    androidx.compose.ui.geometry.Rect(dividerX, 0f, w, h),
                    androidx.compose.ui.graphics.Paint(),
                )
                // Room outline
                drawRoundRect(
                    color = colors.accent.copy(alpha = 0.10f),
                    topLeft = Offset(w * 0.54f, h * 0.08f),
                    size = Size(w * 0.44f, h * 0.74f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
                // Window glow
                drawRoundRect(
                    color = colors.gold.copy(alpha = glowAlpha * 0.18f),
                    topLeft = Offset(w * 0.62f, h * 0.12f),
                    size = Size(w * 0.24f, h * 0.26f),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                )
                // Styled sofa
                drawRoundRect(
                    color = colors.accent.copy(alpha = 0.50f),
                    topLeft = Offset(w * 0.58f, h * 0.48f),
                    size = Size(w * 0.32f, h * 0.18f),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                )
                // Throw pillow
                drawRoundRect(
                    color = colors.gold.copy(alpha = 0.45f),
                    topLeft = Offset(w * 0.60f, h * 0.42f),
                    size = Size(w * 0.08f, h * 0.08f),
                    cornerRadius = CornerRadius(5.dp.toPx()),
                )
                // Coffee table
                drawRoundRect(
                    color = colors.gold.copy(alpha = 0.30f),
                    topLeft = Offset(w * 0.66f, h * 0.62f),
                    size = Size(w * 0.16f, h * 0.04f),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                )
                // Rug
                drawOval(
                    color = colors.accent.copy(alpha = 0.10f),
                    topLeft = Offset(w * 0.58f, h * 0.60f),
                    size = Size(w * 0.32f, h * 0.10f),
                )
                // Floor lamp
                drawLine(
                    color = colors.gold.copy(alpha = 0.40f),
                    start = Offset(w * 0.92f, h * 0.66f),
                    end = Offset(w * 0.92f, h * 0.30f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round,
                )
                drawCircle(
                    color = colors.gold.copy(alpha = glowAlpha * 0.12f),
                    radius = w * 0.04f,
                    center = Offset(w * 0.92f, h * 0.28f),
                )
                drawContext.canvas.restore()

                // ── Divider line ──
                drawLine(
                    color = colors.heroDivider.copy(alpha = if (isDragging) 0.9f else 0.6f),
                    start = Offset(dividerX, h * 0.05f),
                    end = Offset(dividerX, h * 0.88f),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                // ── Sparkle particles along divider ──
                drawCircle(
                    color = colors.gold.copy(alpha = glowAlpha * 0.5f),
                    radius = 7.dp.toPx(),
                    center = Offset(dividerX, h * 0.20f),
                )
                drawCircle(
                    color = colors.mint.copy(alpha = glowAlpha * 0.35f),
                    radius = 4.dp.toPx(),
                    center = Offset(dividerX - 2.dp.toPx(), h * 0.35f),
                )
                drawCircle(
                    color = colors.gold.copy(alpha = glowAlpha * 0.25f),
                    radius = 3.dp.toPx(),
                    center = Offset(dividerX + 2.dp.toPx(), h * 0.55f),
                )
            }

            // ── Draggable handle ──
            val handleX by animateFloatAsState(
                targetValue = sliderProgress.value,
                animationSpec = if (isDragging) tween(0) else spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                ),
                label = "handle_x",
            )

            // Handle circle
            Surface(
                shape = CircleShape,
                color = colors.heroDivider,
                shadowElevation = if (isDragging) 6.dp else 3.dp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = ((handleX - 0.5f) * 280f).dp)
                    .size(32.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            // Labels
            Text(
                stringResource(R.string.upgrade_v3_before).uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                ),
                color = colors.textMuted.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 8.dp),
            )
            Text(
                stringResource(R.string.upgrade_v3_after).uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                ),
                color = colors.accent.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 8.dp),
            )
        }
    }
}
