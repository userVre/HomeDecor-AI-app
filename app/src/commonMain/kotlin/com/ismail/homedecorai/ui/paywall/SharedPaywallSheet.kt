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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.openUrl
import com.ismail.homedecorai.ui.rememberIsDesktop
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
) {
    val proColors = rememberSheetPalette()
    val modalTapBlocker = remember { MutableInteractionSource() }
    var currentStep by remember { mutableIntStateOf(1) }
    var selectedReminder by remember { mutableIntStateOf(2) }
    var selectedPlan by remember { mutableStateOf("yearly") }
    var checkoutLoading by remember { mutableStateOf(false) }
    var checkoutError by remember { mutableStateOf<String?>(null) }
    var checkoutSuccess by remember { mutableStateOf(false) }
    val isDesktop = rememberIsDesktop()

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
        if (isDesktop) {
            // Desktop: wide centered panel with gradient background
            Box(
                Modifier
                    .align(Alignment.Center)
                    .widthIn(max = 840.dp)
                    .fillMaxHeight(0.94f)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(proColors.gradientStart, proColors.gradientMid, proColors.gradientEnd),
                        ),
                    ),
            ) {
                // Subtle accent glow at top
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    proColors.accent.copy(alpha = 0.06f),
                                    Color.Transparent,
                                ),
                                radius = 400f,
                            ),
                        ),
                )
                Column(Modifier.fillMaxSize()) {
                    PaywallTopBar(
                        currentStep = currentStep,
                        onClose = onClose,
                        onBack = ::goBack,
                    )

                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = if (currentStep == 4) 32.dp else 40.dp)
                            .testTag(Strings.formatTestTag(Strings.TestTags.paywallStepContent, currentStep)),
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
                                purchasing = state.purchasing || checkoutLoading,
                                purchaseSuccess = state.purchaseSuccess || checkoutSuccess,
                                errorMessage = checkoutError,
                            )
                        }

                        Spacer(Modifier.height(24.dp))
                    }

                    val ctaLabel = when {
                        checkoutLoading -> "Processing..."
                        checkoutSuccess -> "Subscribed!"
                        currentStep == 5 -> Strings.pwS5Cta
                        else -> when (currentStep) {
                            1 -> Strings.pwS1Cta
                            2 -> Strings.pwS2Cta
                            3 -> Strings.pwS3Cta
                            4 -> Strings.pwS4Cta
                            else -> ""
                        }
                    }

                    PaywallBottomCta(
                        colors = proColors,
                        label = ctaLabel,
                        enabled = !checkoutLoading && !checkoutSuccess,
                        processing = checkoutLoading,
                        success = checkoutSuccess,
                        onClick = {
                            checkoutError = null
                            when (currentStep) {
                                1, 2, 3 -> currentStep++
                                4 -> {
                                    onPlanSelected(selectedPlan)
                                    currentStep = 5
                                }
                                5 -> {
                                    checkoutLoading = true
                                    onContinue()
                                }
                            }
                        },
                        onRestore = onRestore,
                    )
                }
            }
        } else {
            // Mobile: full-screen sheet
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
                Column(Modifier.fillMaxSize()) {
                    PaywallTopBar(
                        currentStep = currentStep,
                        onClose = onClose,
                        onBack = ::goBack,
                    )

                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                            .testTag(Strings.formatTestTag(Strings.TestTags.paywallStepContent, currentStep)),
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
                                purchasing = state.purchasing || checkoutLoading,
                                purchaseSuccess = state.purchaseSuccess || checkoutSuccess,
                                errorMessage = checkoutError,
                            )
                        }

                        Spacer(Modifier.height(24.dp))
                    }

                    val ctaLabel = when {
                        checkoutLoading -> "Processing..."
                        checkoutSuccess -> "Subscribed!"
                        currentStep == 5 -> Strings.pwS5Cta
                        else -> when (currentStep) {
                            1 -> Strings.pwS1Cta
                            2 -> Strings.pwS2Cta
                            3 -> Strings.pwS3Cta
                            4 -> Strings.pwS4Cta
                            else -> ""
                        }
                    }

                    PaywallBottomCta(
                        colors = proColors,
                        label = ctaLabel,
                        enabled = !checkoutLoading && !checkoutSuccess,
                        processing = checkoutLoading,
                        success = checkoutSuccess,
                        onClick = {
                            checkoutError = null
                            when (currentStep) {
                                1, 2, 3 -> currentStep++
                                4 -> {
                                    onPlanSelected(selectedPlan)
                                    currentStep = 5
                                }
                                5 -> {
                                    checkoutLoading = true
                                    onContinue()
                                }
                            }
                        },
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
    val isDesktop = rememberIsDesktop()
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 4.dp)
            .testTag(Strings.TestTags.paywallTopBar),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (currentStep == 1 && !isDesktop) {
            Spacer(Modifier.size(48.dp))
        } else {
            IconButton(
                onClick = if (currentStep == 1) onClose else onBack,
                modifier = Modifier
                    .size(48.dp)
                    .testTag(Strings.TestTags.paywallBackButton),
            ) {
                Icon(
                    imageVector = if (currentStep == 1) Icons.Rounded.Close else Icons.Rounded.ArrowBack,
                    contentDescription = if (currentStep == 1) Strings.proA11yClose else Strings.paywallA11yBack,
                    tint = ProTextSecondary,
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = ProCardSurface,
            border = BorderStroke(1.dp, ProBorder),
            modifier = Modifier
                .height(28.dp)
                .testTag(Strings.TestTags.paywallStepIndicator)
                .semantics { contentDescription = "Step $currentStep of $TOTAL_STEPS" },
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

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(48.dp)
                .testTag(Strings.TestTags.paywallCloseButton),
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = Strings.proA11yClose,
                tint = ProTextSecondary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

// ── Step 1: Unlimited Room Makeovers ──────────────────────────────────────

@Composable
private fun PaywallStep1Unlimited(colors: SheetPalette) {
    Spacer(Modifier.height(8.dp))

    val heading = Strings.pwS1Heading
    val highlight = Strings.pwS1HeadingHighlight
    val parts = heading.split(highlight, limit = 2)
    val annotatedHeading = if (parts.size == 2) {
        buildAnnotatedString {
            append(parts[0])
            withStyle(SpanStyle(color = colors.accent)) { append(highlight) }
            append(parts[1])
        }
    } else {
        buildAnnotatedString { append(heading) }
    }
    Text(
        annotatedHeading,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp,
        ),
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
                            HomeDecorExtra.scrimLight,
                            HomeDecorExtra.scrimMedium,
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
                color = HomeDecorExtra.scrimHeavy,
            ) {
                Text(
                    "BEFORE",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = HomeDecorExtra.onGradientText,
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
                    color = HomeDecorExtra.onGradientText,
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
            Icons.Rounded.AutoAwesome to colors.accent,
            Icons.Rounded.Star to colors.gold,
            Icons.Rounded.Check to colors.mint,
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
                .semantics {
                    role = Role.RadioButton
                    this.selected = selected
                    contentDescription = label
                }
                .clickable { onReminderSelected(value) },
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    Icons.Rounded.AutoAwesome,
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
                            tint = HomeDecorExtra.onGradientText,
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
                Icons.Rounded.Shield,
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
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = Strings.a11yComparisonTable
            },
    ) {
        Column {
            // Header row
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                colors.accent.copy(alpha = 0.08f),
                                colors.accent.copy(alpha = 0.04f),
                            ),
                        ),
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    Strings.pwS3ColFeature,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.textSecondary,
                    modifier = Modifier.weight(1.2f),
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = colors.textMuted.copy(alpha = 0.1f),
                    modifier = Modifier.weight(0.8f),
                ) {
                    Text(
                        Strings.pwS3ColFree,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = colors.textMuted,
                        modifier = Modifier.padding(vertical = 3.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = colors.accent,
                    modifier = Modifier.weight(0.8f),
                ) {
                    Text(
                        Strings.pwS3ColPremium,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = HomeDecorExtra.onGradientText,
                        modifier = Modifier.padding(vertical = 3.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            val rows = listOf(
                Triple(Strings.pwS3Row1Feature, Strings.pwS3Row1Free, "\u2713"),
                Triple(Strings.pwS3Row2Feature, Strings.pwS3Row2Free, "\u2713"),
                Triple(Strings.pwS3Row3Feature, Strings.pwS3Row3Free, "\u2713"),
                Triple(Strings.pwS3Row4Feature, Strings.pwS3Row4Free, "\u2713"),
                Triple(Strings.pwS3Row5Feature, Strings.pwS3Row5Free, "\u2713"),
                Triple(Strings.pwS3Row6Feature, Strings.pwS3Row6Free, "\u2713"),
            )

            rows.forEachIndexed { index, (feature, freeVal, proVal) ->
                val isEven = index % 2 == 0
                if (index > 0) {
                    HorizontalDivider(thickness = 0.5.dp, color = colors.border)
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(if (isEven) colors.accent.copy(alpha = 0.02f) else Color.Transparent)
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
                                        tint = HomeDecorExtra.onGradientText,
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
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colors.gold.copy(alpha = 0.12f),
                modifier = Modifier.size(32.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                        tint = colors.gold,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
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
        color = when {
            selected && badge != null -> colors.accentSurface
            selected -> colors.accentSurface.copy(alpha = 0.5f)
            else -> colors.cardSurface
        },
        border = BorderStroke(if (selected) 2.dp else 1.dp, borderColor),
        shadowElevation = if (selected && badge != null) 4.dp else 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(Strings.formatTestTag(Strings.TestTags.paywallPlanCard, title.lowercase()))
            .semantics {
                role = Role.RadioButton
                contentDescription = Strings.a11yPaywallPlan(title, selected)
            }
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
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = HomeDecorExtra.onGradientText,
                                    modifier = Modifier.size(10.dp),
                                )
                                Text(
                                    badge,
                                    color = HomeDecorExtra.onGradientText,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                    ),
                                )
                            }
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
                            tint = HomeDecorExtra.onGradientText,
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
                    color = if (selected && badge != null) colors.accent else colors.textPrimary,
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
                    color = colors.checkGreen.copy(alpha = 0.12f),
                ) {
                    Text(
                        savings,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color = colors.checkGreen,
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
    purchasing: Boolean = false,
    purchaseSuccess: Boolean = false,
    errorMessage: String? = null,
) {
    // Badge
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (purchaseSuccess) colors.checkGreen.copy(alpha = 0.15f) else colors.accentSurface,
        border = BorderStroke(1.dp, (if (purchaseSuccess) colors.checkGreen else colors.accent).copy(alpha = 0.2f)),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                if (purchaseSuccess) Icons.Rounded.Check else Icons.Rounded.Shield,
                contentDescription = null,
                tint = if (purchaseSuccess) colors.checkGreen else colors.accent,
                modifier = Modifier.size(14.dp),
            )
            Text(
                if (purchaseSuccess) "Payment confirmed" else Strings.pwS5Badge,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (purchaseSuccess) colors.checkGreen else colors.accent,
            )
        }
    }

    Spacer(Modifier.height(12.dp))

    Text(
        if (purchaseSuccess) "Welcome to Pro!" else Strings.pwS5Heading,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp,
        ),
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
    )

    Spacer(Modifier.height(16.dp))

    if (errorMessage != null) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFFF3F0),
            border = BorderStroke(1.dp, Color(0xFFE57373).copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("\u26A0", style = MaterialTheme.typography.titleMedium, color = Color(0xFFD32F2F))
                Text(
                    errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD32F2F),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
    }

    // Shield hero visual
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (purchaseSuccess) colors.checkGreen.copy(alpha = 0.08f) else colors.accentSurface,
        border = BorderStroke(1.dp, (if (purchaseSuccess) colors.checkGreen else colors.accent).copy(alpha = 0.15f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (purchasing) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = colors.accent,
                        strokeWidth = 3.dp,
                    )
                    Text(
                        "Processing...",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = (if (purchaseSuccess) colors.checkGreen else colors.accent).copy(alpha = 0.12f),
                        modifier = Modifier.size(56.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                if (purchaseSuccess) Icons.Rounded.Check else Icons.Rounded.Lock,
                                contentDescription = null,
                                tint = if (purchaseSuccess) colors.checkGreen else colors.accent,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }
                    if (purchaseSuccess) {
                        Text(
                            "Payment confirmed",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = colors.checkGreen,
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Subscription summary
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.cardSurface,
        border = BorderStroke(1.dp, colors.border),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            val planTitle = when (selectedPlan) {
                "yearly" -> Strings.pwS4PlanYearlyTitle
                "monthly" -> Strings.pwS4PlanMonthlyTitle
                "family" -> Strings.pwS4PlanFamilyTitle
                else -> Strings.pwS4PlanYearlyTitle
            }
            val planSubtitle = when (selectedPlan) {
                "yearly" -> "Annual subscription"
                "monthly" -> "Monthly subscription"
                "family" -> "Family annual subscription"
                else -> "Annual subscription"
            }
            Text(
                planTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = colors.textPrimary,
            )
            Text(
                planSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted,
            )

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(thickness = 0.5.dp, color = colors.border)
            Spacer(Modifier.height(14.dp))

            val summaryRows = listOf(
                Strings.pwS5TrialPeriod to when (selectedPlan) {
                    "yearly" -> "Yearly billing"
                    "monthly" -> "Monthly billing"
                    "family" -> "Family yearly billing"
                    else -> "Yearly billing"
                },
                Strings.pwS5Then to when (selectedPlan) {
                    "yearly" -> "\$39.99 / year"
                    "monthly" -> "\$7.99 / month"
                    "family" -> "\$59.99 / year"
                    else -> "\$39.99 / year"
                },
                Strings.pwS5RenewalDate to run {
                    val now = kotlinx.datetime.Clock.System.now()
                    val renewal = now.plus(7, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                    val dt = renewal.toLocalDateTime(TimeZone.currentSystemDefault())
                    "${dt.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${dt.dayOfMonth}, ${dt.year}"
                },
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
            ).forEachIndexed { index, benefit ->
                Row(
                    Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Surface(
                        shape = CircleShape,
                        color = colors.checkGreen.copy(alpha = 0.12f),
                        modifier = Modifier.size(22.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = colors.checkGreen,
                                modifier = Modifier.size(14.dp),
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
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colors.accent.copy(alpha = 0.10f),
                modifier = Modifier.size(32.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
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
    enabled: Boolean = true,
    processing: Boolean = false,
    success: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, colors.gradientMid.copy(alpha = 0.95f), colors.gradientMid),
                ),
            )
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 16.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PaywallCtaButton(
            colors = colors,
            label = label,
            onClick = onClick,
            enabled = enabled,
            processing = processing,
            success = success,
        )
        if (!success) {
            Text(
                Strings.pwS5Trust,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textMuted,
                textAlign = TextAlign.Center,
            )
        }
        PaywallBottomLinks(onRestore = onRestore)
    }
}

@Composable
private fun PaywallCtaButton(
    colors: SheetPalette,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    processing: Boolean = false,
    success: Boolean = false,
) {
    val buttonEnabled = enabled && !processing
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && buttonEnabled) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "cta_scale",
    )

    Button(
        onClick = { if (buttonEnabled) onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                success -> colors.checkGreen
                !buttonEnabled -> colors.ctaDisabled
                else -> colors.accent
            },
            contentColor = HomeDecorExtra.onGradientText,
            disabledContainerColor = colors.ctaDisabled,
            disabledContentColor = colors.textMuted,
        ),
        contentPadding = PaddingValues(),
        enabled = buttonEnabled,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag(Strings.TestTags.paywallCtaButton)
            .semantics {
                role = Role.Button
                contentDescription = Strings.a11yPaywallCta
            }
            .scale(scale),
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (processing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = HomeDecorExtra.onGradientText,
                    strokeWidth = 2.dp,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            } else if (success) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = HomeDecorExtra.onGradientText,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            } else {
                Icon(
                    Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
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
    val scrimAccent: Color,
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
            overlayBg = Color.Black.copy(alpha = 0.6f),
            scrimAccent = ProCtaAccent.copy(alpha = 0.08f),
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
            checkGreen = ProCheckGreen,
            border = ProDarkBorder,
            ctaDisabled = ProDarkCtaDisabled,
            surface = ProDarkSurface,
            overlayBg = Color.Black.copy(alpha = 0.75f),
            scrimAccent = ProDarkAccent.copy(alpha = 0.06f),
        )
    }
}
