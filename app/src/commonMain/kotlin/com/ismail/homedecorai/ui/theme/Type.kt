package com.ismail.homedecorai.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// HomeDecor AI Typography  –  MD3 Expressive (v2)
// ---------------------------------------------------------------------------
// Premium, modern, readable.
// Uses system default font family for fast rendering.
// M3 Expressive: stronger weight hierarchy, wider letter-spacing range,
// larger display sizes for hero moments.
// ---------------------------------------------------------------------------

object HomeDecorType {

    // -- Display: hero / splash / onboarding (M3 Expressive — larger, bolder) --
    val DisplayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,
        lineHeight = 50.sp,
        letterSpacing = (-0.8).sp,
    )
    val DisplayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp,
    )
    val DisplaySmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.25).sp,
    )

    // -- Headline: section headers --
    val HeadlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.25).sp,
    )
    val HeadlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    )
    val HeadlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    )

    // -- Title: card headings, list item titles --
    val TitleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    )
    val TitleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp,
    )
    val TitleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.1.sp,
    )

    // -- Body: paragraphs, descriptions --
    val BodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.3.sp,
    )
    val BodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
    )
    val BodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp,
    )

    // -- Label: buttons, chips, badges --
    val LabelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    )
    val LabelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    )
    val LabelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.4.sp,
    )

    // -- Card-specific --
    val CardSubtitle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp,
    )
}

val HomeDecorTypography = Typography(
    displayLarge = HomeDecorType.DisplayLarge,
    displayMedium = HomeDecorType.DisplayMedium,
    displaySmall = HomeDecorType.DisplaySmall,
    headlineLarge = HomeDecorType.HeadlineLarge,
    headlineMedium = HomeDecorType.HeadlineMedium,
    headlineSmall = HomeDecorType.HeadlineSmall,
    titleLarge = HomeDecorType.TitleLarge,
    titleMedium = HomeDecorType.TitleMedium,
    titleSmall = HomeDecorType.TitleSmall,
    bodyLarge = HomeDecorType.BodyLarge,
    bodyMedium = HomeDecorType.BodyMedium,
    bodySmall = HomeDecorType.BodySmall,
    labelLarge = HomeDecorType.LabelLarge,
    labelMedium = HomeDecorType.LabelMedium,
    labelSmall = HomeDecorType.LabelSmall,
)
