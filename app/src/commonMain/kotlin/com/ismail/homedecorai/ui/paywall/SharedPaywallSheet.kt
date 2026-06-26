package com.ismail.homedecorai.ui.paywall

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.openUrl
import com.ismail.homedecorai.ui.theme.*
import homedecorai.app.generated.resources.Res
import homedecorai.app.generated.resources.assets_media_paywall_carouseljapandibedroom
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource

private const val TOTAL_STEPS = 5

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SharedPaywallSheet(
    state: PaywallState,
    onClose: () -> Unit,
    onPlanSelected: (String) -> Unit,
    onContinue: () -> Unit,
    onRestore: () -> Unit,
    ctaLabel: String = Strings.paywallV3Cta,
    showRestore: Boolean = true,
) {
    val proColors = rememberSheetPalette()
    val modalTapBlocker = remember { MutableInteractionSource() }
    var currentStep by remember { mutableIntStateOf(1) }
    var selectedReminder by remember { mutableIntStateOf(2) }
    var selectedPlan by remember { mutableStateOf("yearly") }

    fun goNext() {
        if (currentStep < TOTAL_STEPS) currentStep++
    }

    fun goCheckout() {
        onPlanSelected(selectedPlan)
        currentStep = TOTAL_STEPS
    }

    fun goBack() {
        if (currentStep > 1) currentStep-- else onClose()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(proColors.overlayBg)
            .clickable(interactionSource = modalTapBlocker, indication = null, onClick = {})
            .testTag(Strings.TestTags.paywallSheet),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(proColors.gradientStart, proColors.gradientMid, proColors.gradientEnd),
                    ),
                )
                .windowInsetsPadding(WindowInsets.statusBars),
        ) {
            Column(
                Modifier.fillMaxSize(),
            ) {
                PaywallTopBar(
                    currentStep = currentStep,
                    onClose = onClose,
                    onBack = ::goBack,
                )

                Column(
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (currentStep) {
                        1 -> PaywallStep1Unlimited(colors = proColors)
                        2 -> PaywallStep2Reminder(
                            colors = proColors,
                            selectedReminder = selectedReminder,
                            onReminderSelected = { selectedReminder = it },
                        )
                        3 -> PaywallStep3Comparison(colors = proColors)
                        4 -> PaywallStep4Plans(
                            colors = proColors,
                            selectedPlan = selectedPlan,
                            onPlanSelected = { selectedPlan = it },
                        )
                        5 -> PaywallStep5Checkout(
                            colors = proColors,
                            selectedPlan = selectedPlan,
                        )
                    }

                    Spacer(Modifier.height(24.dp))
                }

                if (currentStep < 5) {
                    PaywallBottomCta(
                        colors = proColors,
                        label = when (currentStep) {
                            1 -> Strings.pwS1Cta
                            2 -> Strings.pwS2Cta
                            3 -> Strings.pwS3Cta
                            4 -> Strings.pwS4Cta
                            else -> ""
                        },
                        onClick = if (currentStep == 4) ::goCheckout else ::goNext,
                        onRestore = onRestore,
                    )
                } else {
                    PaywallBottomCta(
                        colors = proColors,
                        label = Strings.pwS5Cta,
                        onClick = onContinue,
                        onRestore = onRestore,
                    )
                }
            }
        }
    }
}

@Composable
private fun PaywallTopBar(
    currentStep: Int,
    onClose: () -> Unit,
    onBack: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 4.dp)
            .testTag(Strings.TestTags.paywallTopBar),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = if (currentStep == 1) onClose else onBack,
            modifier = Modifier
                .size(48.dp)
                .testTag(
                    if (currentStep == 1) Strings.TestTags.paywallCloseButton
                    else Strings.TestTags.paywallBackButton
                ),
        ) {
            Icon(
                imageVector = if (currentStep == 1) Icons.Rounded.Close else Icons.Rounded.ArrowBack,
                contentDescription = if (currentStep == 1) Strings.proA11yClose else Strings.paywallA11yBack,
                tint = ProTextSecondary,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(Modifier.weight(1f))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = ProCardSurface,
            border = BorderStroke(1.dp, ProBorder),
            modifier = Modifier
                .height(28.dp)
                .testTag(Strings.TestTags.paywallStepIndicator),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 12.dp),
            ) {
                Text(
                    "$currentStep / $TOTAL_STEPS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                    ),
                    color = ProTextPrimary,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Spacer(Modifier.size(48.dp))
    }
}

// ── Step 1: Unlimited Room Makeovers ──────────────────────────────────────

@Composable
private fun PaywallStep1Unlimited(colors: SheetPalette) {
    Spacer(Modifier.height(8.dp))

    val heading = Strings.pwS1Heading
    val highlight = Strings.pwS1HeadingHighlight
    Text(
        "$heading\n$highlight",
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp,
        ),
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(20.dp))

    // Room transformation hero visual
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp)),
    ) {
        Image(
            painter = painterResource(Res.drawable.assets_media_paywall_carouseljapandibedroom),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        // Gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.45f),
                        ),
                    ),
                ),
        )

        // BEFORE / AFTER labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.55f),
            ) {
                Text(
                    "BEFORE",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colors.accent.copy(alpha = 0.85f),
            ) {
                Text(
                    "AFTER",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Benefits card
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            listOf(
                Strings.pwS1Benefit1,
                Strings.pwS1Benefit2,
                Strings.pwS1Benefit3,
                Strings.pwS1Benefit4,
            ).forEach { benefit ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        benefit,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textPrimary,
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Social proof
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
            listOf(colors.accent, colors.gold, colors.mint, colors.accent).forEach { color ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp),
                ) {}
            }
        }
        Text(
            Strings.pwS1SocialProof,
            style = MaterialTheme.typography.bodySmall,
            color = colors.textSecondary,
            modifier = Modifier.weight(1f),
        )
    }
}

// ── Step 2: Trial Reminder ────────────────────────────────────────────────

@Composable
private fun PaywallStep2Reminder(
    colors: SheetPalette,
    selectedReminder: Int,
    onReminderSelected: (Int) -> Unit,
) {
    Spacer(Modifier.height(8.dp))

    Text(
        Strings.pwS2Heading,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(24.dp))

    // Reminder icons visual
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        listOf(
            Icons.Rounded.Notifications to colors.gold,
            Icons.Rounded.AutoAwesome to colors.accent,
            Icons.Rounded.Lock to colors.mint,
        ).forEach { (icon, tint) ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = tint.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
                }
            }
        }
    }

    Spacer(Modifier.height(24.dp))

    // Option cards
    listOf(
        1 to Strings.pwS2Option1,
        2 to Strings.pwS2Option2,
    ).forEach { (value, label) ->
        val selected = selectedReminder == value
        val bgColor by animateColorAsState(
            targetValue = if (selected) colors.accentSurface else colors.cardSurface,
            animationSpec = tween(200),
            label = "reminder_bg",
        )
        val borderColor by animateColorAsState(
            targetValue = if (selected) colors.accent else colors.border,
            animationSpec = tween(200),
            label = "reminder_border",
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = bgColor,
            border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable { onReminderSelected(value) },
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    Icons.Rounded.Notifications,
                    contentDescription = null,
                    tint = if (selected) colors.accent else colors.textMuted,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    label,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(if (selected) colors.accent else Color.Transparent)
                        .then(
                            if (!selected) Modifier.border(2.dp, colors.textMuted.copy(alpha = 0.4f), CircleShape) else Modifier
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))
    }

    Spacer(Modifier.height(12.dp))

    // Info card
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = colors.accentSurface.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                Icons.Rounded.Lock,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(18.dp),
            )
            Text(
                Strings.pwS2Info,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
            )
        }
    }
}

// ── Step 3: Free vs Premium Comparison ─────────────────────────────────────

@Composable
private fun PaywallStep3Comparison(colors: SheetPalette) {
    Spacer(Modifier.height(8.dp))

    Text(
        Strings.pwS3Heading,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(20.dp))

    // Comparison table card
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            // Header row
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(colors.accent.copy(alpha = 0.06f))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    Strings.pwS3ColFeature,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.textSecondary,
                    modifier = Modifier.weight(1.2f),
                )
                Text(
                    Strings.pwS3ColFree,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.textSecondary,
                    modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.Center,
                )
                Text(
                    Strings.pwS3ColPremium,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.accent,
                    modifier = Modifier.weight(0.8f),
                    textAlign = TextAlign.Center,
                )
            }

            val rows = listOf(
                Triple(Strings.pwS3Row1Feature, Strings.pwS3Row1Free, "\u2713"),
                Triple(Strings.pwS3Row2Feature, Strings.pwS3Row2Free, "\u2713"),
                Triple(Strings.pwS3Row3Feature, Strings.pwS3Row3Free, "\u2713"),
                Triple(Strings.pwS3Row4Feature, Strings.pwS3Row4Free, "Yes"),
                Triple(Strings.pwS3Row5Feature, Strings.pwS3Row5Free, "\u2713"),
                Triple(Strings.pwS3Row6Feature, Strings.pwS3Row6Free, "\u2713"),
            )

            rows.forEachIndexed { index, (feature, freeVal, proVal) ->
                if (index > 0) {
                    HorizontalDivider(thickness = 0.5.dp, color = colors.border)
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        feature,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1.2f),
                    )
                    Text(
                        freeVal,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textMuted,
                        modifier = Modifier.weight(0.8f),
                        textAlign = TextAlign.Center,
                    )
                    if (proVal == "\u2713") {
                        Box(
                            modifier = Modifier.weight(0.8f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = colors.accent,
                                modifier = Modifier.size(22.dp),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp),
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            proVal,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = colors.accent,
                            modifier = Modifier.weight(0.8f),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Recommendation card
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = null,
                tint = colors.gold,
                modifier = Modifier.size(20.dp),
            )
            Text(
                Strings.pwS3Recommendation,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
            )
        }
    }
}

// ── Step 4: Plan Selection ────────────────────────────────────────────────

@Composable
private fun PaywallStep4Plans(
    colors: SheetPalette,
    selectedPlan: String,
    onPlanSelected: (String) -> Unit,
) {
    Spacer(Modifier.height(8.dp))

    Text(
        Strings.pwS4Heading,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(20.dp))

    // Yearly Plan (Recommended)
    PaywallPlanCard(
        colors = colors,
        title = Strings.pwS4PlanYearlyTitle,
        price = Strings.pwS4PlanYearlyPrice,
        pricePer = Strings.pwS4PlanYearlyPer,
        detail = Strings.pwS4PlanYearlyDetail,
        badge = Strings.pwS4PlanYearlyBadge,
        savings = Strings.pwS4PlanYearlySavings,
        selected = selectedPlan == "yearly",
        onClick = { onPlanSelected("yearly") },
    )

    Spacer(Modifier.height(10.dp))

    // Monthly Plan
    PaywallPlanCard(
        colors = colors,
        title = Strings.pwS4PlanMonthlyTitle,
        price = Strings.pwS4PlanMonthlyPrice,
        pricePer = Strings.pwS4PlanMonthlyPer,
        detail = "",
        badge = null,
        savings = null,
        selected = selectedPlan == "monthly",
        onClick = { onPlanSelected("monthly") },
    )

    Spacer(Modifier.height(10.dp))

    // Family Plan
    PaywallPlanCard(
        colors = colors,
        title = Strings.pwS4PlanFamilyTitle,
        price = Strings.pwS4PlanFamilyPrice,
        pricePer = Strings.pwS4PlanFamilyPer,
        detail = Strings.pwS4PlanFamilyDetail,
        badge = null,
        savings = null,
        selected = selectedPlan == "family",
        onClick = { onPlanSelected("family") },
    )
}

@Composable
private fun PaywallPlanCard(
    colors: SheetPalette,
    title: String,
    price: String,
    pricePer: String,
    detail: String,
    badge: String?,
    savings: String?,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            badge != null && selected -> colors.gold
            selected -> colors.accent
            else -> colors.border
        },
        animationSpec = tween(200),
        label = "plan_border",
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) colors.accentSurface else colors.cardSurface,
        border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(Strings.formatTestTag(Strings.TestTags.paywallPlanCard, title.lowercase()))
            .clickable(onClick = onClick),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (badge != null) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.gold,
                        ) {
                            Text(
                                badge,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                ),
                            )
                        }
                    }
                    Text(
                        title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = colors.textPrimary,
                    )
                }

                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(if (selected) colors.accent else Color.Transparent)
                        .then(
                            if (!selected) Modifier.border(2.dp, colors.textMuted.copy(alpha = 0.4f), CircleShape) else Modifier
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    price,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                    ),
                    color = colors.textPrimary,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    pricePer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textMuted,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }

            if (detail.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textMuted,
                )
            }

            if (savings != null) {
                Spacer(Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colors.accentSurface,
                ) {
                    Text(
                        savings,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color = colors.accent,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
    }
}

// ── Step 5: Checkout ──────────────────────────────────────────────────────

@Composable
private fun PaywallStep5Checkout(
    colors: SheetPalette,
    selectedPlan: String,
) {
    // Badge
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.accentSurface,
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.2f)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                Icons.Rounded.Shield,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(14.dp),
            )
            Text(
                Strings.pwS5Badge,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colors.accent,
            )
        }
    }

    Spacer(Modifier.height(12.dp))

    Text(
        Strings.pwS5Heading,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(16.dp))

    // Shield hero visual
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = colors.accentSurface,
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.15f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                Icons.Rounded.Lock,
                contentDescription = null,
                tint = colors.accent.copy(alpha = 0.4f),
                modifier = Modifier.size(48.dp),
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    // Subscription summary
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                Strings.pwS5PlanTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = colors.textPrimary,
            )
            Text(
                Strings.pwS5PlanSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted,
            )

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(thickness = 0.5.dp, color = colors.border)
            Spacer(Modifier.height(14.dp))

            val summaryRows = listOf(
                Strings.pwS5TrialPeriod to Strings.pwS5TrialValue,
                Strings.pwS5Then to when (selectedPlan) {
                    "yearly" -> "\$39.99 / year"
                    "monthly" -> "\$7.99 / month"
                    "family" -> "\$59.99 / year"
                    else -> "\$39.99 / year"
                },
                Strings.pwS5RenewalDate to Clock.System.now().plus(7, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toLocalDateTime(TimeZone.currentSystemDefault()).let { "${it.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${it.dayOfMonth}, ${it.year}" },
                Strings.pwS5Payment to Strings.pwS5PaymentValue,
            )

            summaryRows.forEach { (label, value) ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                    )
                    Text(
                        value,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = colors.textPrimary,
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    // Included benefits
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                Strings.pwS5BenefitsTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colors.textPrimary,
            )

            Spacer(Modifier.height(10.dp))

            listOf(
                Strings.pwS5Benefit1,
                Strings.pwS5Benefit2,
                Strings.pwS5Benefit3,
                Strings.pwS5Benefit4,
                Strings.pwS5Benefit5,
                Strings.pwS5Benefit6,
            ).forEach { benefit ->
                Row(
                    Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = colors.accent,
                        modifier = Modifier.size(18.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp),
                            )
                        }
                    }
                    Text(
                        benefit,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textPrimary,
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    // Trust row
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                Icons.Rounded.Lock,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(18.dp),
            )
            Text(
                Strings.pwS5Trust,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
            )
        }
    }

    Spacer(Modifier.height(10.dp))

    Text(
        Strings.pwS5Legal,
        style = MaterialTheme.typography.bodySmall,
        color = colors.textMuted,
        textAlign = TextAlign.Center,
    )
}

// ── Shared Components ──────────────────────────────────────────────────────

@Composable
private fun PaywallBottomCta(
    colors: SheetPalette,
    label: String,
    onClick: () -> Unit,
    onRestore: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, colors.gradientMid, colors.gradientMid),
                ),
            )
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PaywallCtaButton(
            colors = colors,
            label = label,
            onClick = onClick,
        )
        Text(
            Strings.pwS5Trust,
            style = MaterialTheme.typography.bodySmall,
            color = colors.textMuted,
            textAlign = TextAlign.Center,
        )
        PaywallBottomLinks(onRestore = onRestore)
    }
}

@Composable
private fun PaywallCtaButton(
    colors: SheetPalette,
    label: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cta_scale",
    )

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.accent,
            contentColor = Color.White,
        ),
        contentPadding = PaddingValues(),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag(Strings.TestTags.paywallCtaButton)
            .scale(scale),
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                Icons.Rounded.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
private fun PaywallBottomLinks(onRestore: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        Box(
            modifier = Modifier
                .clickable { onRestore() }
                .testTag(Strings.TestTags.paywallRestoreButton)
                .semantics {
                    role = Role.Button
                    contentDescription = Strings.pwS5Restore
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                Strings.pwS5Restore,
                color = ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        Text(
            "\u00B7",
            color = ProTextMuted.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Box(
            modifier = Modifier
                .clickable { openUrl("https://homedecor-ai.com/terms") }
                .semantics {
                    role = Role.Button
                    contentDescription = Strings.terms
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                Strings.terms,
                color = ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        Text(
            "\u00B7",
            color = ProTextMuted.copy(alpha = 0.4f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Box(
            modifier = Modifier
                .clickable { openUrl("https://homedecor-ai.com/privacy") }
                .semantics {
                    role = Role.Button
                    contentDescription = Strings.privacyPolicy
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                Strings.privacyPolicy,
                color = ProTextMuted,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
    }
}

// ── Theme-aware palette ───────────────────────────────────────────────────

@Composable
private fun rememberSheetPalette(): SheetPalette {
    val dark = LocalDarkTheme.current
    return remember(dark) {
        if (dark) SheetPalette.Dark else SheetPalette.Light
    }
}

private class SheetPalette(
    val gradientStart: Color,
    val gradientMid: Color,
    val gradientEnd: Color,
    val accent: Color,
    val accentLight: Color,
    val accentSurface: Color,
    val cardSurface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val gold: Color,
    val mint: Color,
    val checkGreen: Color,
    val border: Color,
    val ctaDisabled: Color,
    val surface: Color,
    val overlayBg: Color,
) {
    companion object {
        val Light = SheetPalette(
            gradientStart = ProDarkStart,
            gradientMid = ProDarkMid,
            gradientEnd = ProDarkEnd,
            accent = ProCtaAccent,
            accentLight = ProAccentLight,
            accentSurface = ProAccentSurface,
            cardSurface = ProCardSurface,
            textPrimary = ProTextPrimary,
            textSecondary = ProTextSecondary,
            textMuted = ProTextMuted,
            gold = ProGold,
            mint = ProMint,
            checkGreen = ProCheckGreen,
            border = ProBorder,
            ctaDisabled = ProCtaDisabled,
            surface = ProSurface,
            overlayBg = HomeDecorColors.Scrim.copy(alpha = 0.5f),
        )
        val Dark = SheetPalette(
            gradientStart = ProDarkGradientStart,
            gradientMid = ProDarkGradientMid,
            gradientEnd = ProDarkGradientEnd,
            accent = ProDarkAccent,
            accentLight = ProDarkAccentLight,
            accentSurface = ProDarkAccentSurface,
            cardSurface = ProDarkCardSurface,
            textPrimary = ProDarkTextPrimary,
            textSecondary = ProDarkTextSecondary,
            textMuted = ProDarkTextMuted,
            gold = ProDarkGold,
            mint = ProDarkMint,
            checkGreen = HomeDecorColors.DarkCheckGreen,
            border = ProDarkBorder,
            ctaDisabled = ProDarkCtaDisabled,
            surface = ProDarkSurface,
            overlayBg = HomeDecorColors.Scrim.copy(alpha = 0.7f),
        )
    }
}
