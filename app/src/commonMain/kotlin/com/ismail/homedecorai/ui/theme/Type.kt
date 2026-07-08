package com.ismail.homedecorai.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,        // 40→44: more expressive hero
        lineHeight = 50.sp,
        letterSpacing = (-0.8).sp,  // tighter for display
    )
    val DisplayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,        // 32→36: expressive
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp,
    )
    val DisplaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,  // SemiBold→Bold: stronger hierarchy
        fontSize = 30.sp,        // 28→30: expressive
        lineHeight = 36.sp,
        letterSpacing = (-0.25).sp,
    )

    // -- Headline: section headers --
    val HeadlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,    // SemiBold→Bold: stronger emphasis
        fontSize = 26.sp,              // 24→26: expressive
        lineHeight = 32.sp,
        letterSpacing = (-0.25).sp,
    )
    val HeadlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,              // 20→22: expressive
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    )
    val HeadlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,  // Medium→SemiBold: stronger
        fontSize = 19.sp,               // 18→19
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    )

    // -- Title: card headings, list item titles --
    val TitleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,  // Medium→SemiBold: expressive
        fontSize = 19.sp,               // 18→19
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    )
    val TitleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp,
    )
    val TitleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.1.sp,
    )

    // -- Body: paragraphs, descriptions --
    val BodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.3.sp,
    )
    val BodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
    )
    val BodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp,
    )

    // -- Label: buttons, chips, badges --
    val LabelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,  // Medium→SemiBold: expressive buttons
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    )
    val LabelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    )
    val LabelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.4.sp,
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
