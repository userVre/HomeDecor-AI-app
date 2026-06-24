package com.ismail.homedecorai.ui.theme

import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// HomeDecor AI  –  Material 3 Expressive Design Foundation
// ---------------------------------------------------------------------------
// Palette philosophy
//   Light  → warm cream surfaces, deep brown text, teal creative accents,
//            subtle gold / diamond highlights. Premium & trustworthy.
//   Dark   → warm charcoal / deep green-gray surfaces, calm muted accents.
//            Never pure black. Premium & calm.
// ---------------------------------------------------------------------------

object HomeDecorColors {

    // =========================== LIGHT MODE =================================

    // -- Surfaces (warm cream family) --
    val Canvas        = Color(0xFFFAF8F5)   // main app background
    val Paper         = Color(0xFFFFFFFF)   // cards, sheets, dialogs
    val Mist          = Color(0xFFF3F0EB)   // subtle recessed areas
    val SurfaceDim    = Color(0xFFD8D3CC)   // dimmed surface overlays
    val SurfaceBright = Color(0xFFFEFDFB)   // brightest surface
    val SurfaceContainerLowest  = Color(0xFFFFFFFF)
    val SurfaceContainerLow     = Color(0xFFF8F6F3)
    val SurfaceContainer        = Color(0xFFF2EFEB)
    val SurfaceContainerHigh    = Color(0xFFECEAE6)
    val SurfaceContainerHighest = Color(0xFFE6E4E0)

    // -- Text --
    val Ink            = Color(0xFF1A1714)  // primary headings
    val InkSoft        = Color(0xFF5C5549)  // secondary body text
    val OnSurface      = Color(0xFF1C1B18)  // MD3 primary text
    val OnSurfaceVariant = Color(0xFF4C4639) // MD3 secondary text
    val OnBackground   = Color(0xFF1C1B18)

    // -- Lines & borders --
    val Line           = Color(0xFFD8D2CB)  // dividers, card borders
    val Outline        = Color(0xFF7D7667)  // MD3 outline
    val OutlineVariant = Color(0xFFCCC5B8)  // MD3 outlineVariant

    // -- Primary: Teal (creative, trustworthy) --
    val Primary            = Color(0xFF2E6B6E)  // deep teal – strong & creative
    val OnPrimary          = Color(0xFFFFFFFF)
    val PrimaryContainer   = Color(0xFFD0EBEE)  // light teal tint
    val OnPrimaryContainer = Color(0xFF0A1F21)

    // -- Secondary: Sage green (natural, garden) --
    val Secondary            = Color(0xFF4E7B56)  // rich sage
    val OnSecondary          = Color(0xFFFFFFFF)
    val SecondaryContainer   = Color(0xFFD6EDDA)  // soft green wash
    val OnSecondaryContainer = Color(0xFF0D2312)

    // -- Tertiary: Warm gold (premium, diamond) --
    val Tertiary            = Color(0xFF8B6914)  // deep warm gold
    val OnTertiary          = Color(0xFFFFFFFF)
    val TertiaryContainer   = Color(0xFFFCE8A8)  // soft gold wash
    val OnTertiaryContainer = Color(0xFF2C1E00)

    // -- Error --
    val ErrorColor          = Color(0xFFBA1A1A)
    val OnError             = Color(0xFFFFFFFF)
    val ErrorContainerColor = Color(0xFFFFDAD6)
    val OnErrorContainer    = Color(0xFF410002)

    // -- Success --
    val Success           = Color(0xFF3A7D44)
    val SuccessContainer  = Color(0xFFC2F0C9)

    // -- Legacy aliases kept for backward compatibility --
    val SecondaryColor    = Color(0xFF5E6E60)
    val ProContainer      = Color(0xFFE8EDF8)

    // -- Warning --
    val Warning           = Color(0xFFB5860A)
    val WarningContainer  = Color(0xFFFFF3D6)

    // -- Semantic / brand tokens --
    val Accent            = Color(0xFF2E6B6E)   // creative teal accent
    val AccentContainer   = Color(0xFFD0EBEE)
    val DiamondAccent     = Color(0xFF2EC4B6)   // teal diamond sparkle
    val ProAccent         = Color(0xFF2E6B6E)   // subscription teal
    val PremiumGold       = Color(0xFFB08D3A)   // premium badge gold
    val GoldDeep          = Color(0xFF8E712E)

    // -- Disabled states --
    val DisabledDarkButton  = Color(0xFFF0EDE8)
    val DisabledDarkText    = Color(0xFFB8AFA0)

    // =========================== DARK MODE ==================================
    // Warm charcoal + deep green-gray. Never pure black.

    // -- Surfaces --
    val Dark              = Color(0xFF141614)   // app background – warm charcoal-green
    val DarkSurface       = Color(0xFF1C1F1C)   // cards, sheets
    val DarkOverlay       = Color(0xFF2A2E2A)   // elevated surfaces, modals
    val DarkSurfaceDim    = Color(0xFF101210)
    val DarkSurfaceBright = Color(0xFF2A2E2A)
    val DarkSurfaceContainerLowest  = Color(0xFF141614)
    val DarkSurfaceContainerLow     = Color(0xFF1C1F1C)
    val DarkSurfaceContainer        = Color(0xFF222622)
    val DarkSurfaceContainerHigh    = Color(0xFF2C302C)
    val DarkSurfaceContainerHighest = Color(0xFF363B36)

    // -- Dark text --
    val DarkOnSurface          = Color(0xFFE2E3DE)
    val DarkOnSurfaceVariant   = Color(0xFFBFC1BA)
    val DarkOnBackground       = Color(0xFFE2E3DE)

    // -- Dark outlines --
    val DarkOutline        = Color(0xFF8A8C85)
    val DarkOutlineVariant = Color(0xFF444842)

    // -- Dark primary: muted teal --
    val DarkPrimary            = Color(0xFF8AD4D7)
    val DarkOnPrimary          = Color(0xFF003738)
    val DarkPrimaryContainer   = Color(0xFF1E5254)
    val DarkOnPrimaryContainer = Color(0xFFC8E8EB)

    // -- Dark secondary: muted sage --
    val DarkSecondary            = Color(0xFFA8D1AE)
    val DarkOnSecondary          = Color(0xFF16381C)
    val DarkSecondaryContainer   = Color(0xFF2E5335)
    val DarkOnSecondaryContainer = Color(0xFFD6EDDA)

    // -- Dark tertiary: muted gold --
    val DarkTertiary            = Color(0xFFE0BF6E)
    val DarkOnTertiary          = Color(0xFF3A2D00)
    val DarkTertiaryContainer   = Color(0xFF53430A)
    val DarkOnTertiaryContainer = Color(0xFFFCE8A8)

    // -- Dark error --
    val DarkError          = Color(0xFFF2B8B5)
    val DarkOnError        = Color(0xFF601410)
    val DarkErrorContainer = Color(0xFF8C1D18)
    val DarkOnErrorContainer = Color(0xFFF2B8B5)

    // -- Dark success --
    val DarkSuccess          = Color(0xFF81D98A)
    val DarkSuccessContainer = Color(0xFF1A3D1F)

    // -- Dark warning --
    val DarkWarning          = Color(0xFFFFD166)
    val DarkWarningContainer = Color(0xFF3D3100)

    // -- Dark accent tokens --
    val DarkAccent        = Color(0xFF8AD4D7)
    val DarkAccentContainer = Color(0xFF1E5254)
    val DarkDiamondAccent = Color(0xFF5CE0D4)
    val DarkProAccent     = Color(0xFF9B93E8)
    val DarkPremiumGold   = Color(0xFFD4AD4A)
    val DarkGoldDeep      = Color(0xFFB08D3A)

    // -- Dark disabled --
    val DisabledLightButton = Color(0xFF2C302C)
    val DisabledLightText   = Color(0xFF5C6059)
}

// ===========================================================================
// Backward-compatible top-level aliases (13+ screen files consume these)
// ===========================================================================

// Core palette
val StudioInk            = HomeDecorColors.Ink
val StudioAccent         = HomeDecorColors.Accent
val StudioBlue           = HomeDecorColors.Accent
val StudioGreen          = HomeDecorColors.Success
val StudioMoss           = HomeDecorColors.SecondaryColor
val StudioRose           = HomeDecorColors.ErrorColor
val StudioCanvas         = HomeDecorColors.Canvas
val StudioPaper          = HomeDecorColors.Paper
val StudioMist           = HomeDecorColors.Mist
val StudioLine           = HomeDecorColors.Line
val StudioBlack          = HomeDecorColors.DarkSurface
val StudioGold           = HomeDecorColors.GoldDeep
val StudioPrimaryContainer = HomeDecorColors.AccentContainer
val StudioProContainer   = HomeDecorColors.ProContainer
val StudioErrorContainer = HomeDecorColors.ErrorContainerColor
val StudioSuccessContainer = HomeDecorColors.SuccessContainer
val StudioBrownBtn       = Color(0xFF2E6B6E)
val StudioBrownDark      = Color(0xFF1E4A4C)

// Paywall palette (consumed by PaywallSheet.kt, SharedComponents.kt)
val PaywallBg            = Color(0xFFFAF8F5)
val PaywallCard          = Color(0xFFFFFFFF)
val PaywallCardAlt       = Color(0xFFF5F3EF)
val PaywallBorder        = Color(0xFFE0DCD6)
val PaywallBorderStrong  = Color(0xFFD0CBC3)
val PaywallTextPrimary   = Color(0xFF1C1917)
val PaywallTextSecondary = Color(0xFF57534E)
val PaywallTextMuted     = Color(0xFFA8A29E)
val PaywallAccent        = Color(0xFF2E6B6E)
val PaywallAccentLight   = Color(0xFF4A8E91)
val PaywallAccentSurface = Color(0xFFEFF8F9)
val PaywallSuccess       = Color(0xFF2E7D32)
val PaywallSuccessSurface = Color(0xFFEDF7EE)
val PaywallPremiumGold   = Color(0xFFB08D3A)

// Paywall Reminder palette
val PaywallReminderTop            = Color(0xFF1B2A3A)
val PaywallReminderBottom         = Color(0xFF2A3B5A)
val PaywallReminderCard           = Color(0xFFFFFFFF)
val PaywallReminderCardSelected   = Color(0xFFEDF6FF)
val PaywallReminderBorder         = Color(0xFFD6E4F0)
val PaywallReminderBorderSelected = Color(0xFF2E6B6E)
val PaywallReminderText           = Color(0xFFFFFFFF)
val PaywallReminderTextMuted      = Color(0xFFB8C4D0)
val PaywallReminderCta            = Color(0xFFFFFFFF)
val PaywallReminderCtaDisabled    = Color(0xFF6B8CA0)

// Pro / Upgrade palette — warm teal, gold, cream (consumed by PaywallSheet.kt, UpgradeScreen.kt)
// Light mode
val ProDarkStart     = Color(0xFFE0F2F1)   // light teal tint
val ProDarkMid       = Color(0xFFF0F7F4)   // warm cream-green
val ProDarkEnd       = Color(0xFFFFFFFF)   // white
val ProAccent        = Color(0xFF2E6B6E)   // deep teal
val ProAccentLight   = Color(0xFF4A8E91)   // medium teal
val ProAccentSurface = Color(0xFFE0F7F8)   // light teal surface
val ProCardSurface   = Color(0xFFFFFFFF)   // white cards
val ProTextPrimary   = Color(0xFF1A1714)   // dark brown
val ProTextSecondary = Color(0xFF5C5549)   // warm gray
val ProTextMuted     = Color(0xFFA8A29E)   // light warm gray
val ProGradientStart = Color(0xFF2E6B6E)   // teal
val ProGradientEnd   = Color(0xFF4A8E91)   // lighter teal
val ProBadgeBg       = Color(0xFFD0EBEE)   // light teal badge
val ProBadgeText     = Color(0xFF2E6B6E)   // teal badge text
val ProSurface       = Color(0xFFF8F6F3)   // warm off-white
val ProSurfaceAlt    = Color(0xFFEFEBE6)   // warmer off-white
val ProBorder        = Color(0xFFE0DCD6)   // warm border
val ProBorderSelected = Color(0xFF2E6B6E)  // teal selected border
val ProCtaGradientStart = Color(0xFF2E6B6E) // teal CTA
val ProCtaGradientEnd   = Color(0xFF3A8B8E) // lighter teal CTA
val ProCtaDisabled   = Color(0xFFB8B2A8)   // warm gray disabled

// Duolingo-inspired Pro palette
val ProGold              = Color(0xFFFFC107)
val ProGoldDark          = Color(0xFFFFA000)
val ProCelebration       = Color(0xFF2E6B6E)  // teal celebration
val ProCardHighlight     = Color(0xFFE8F5F5)  // light teal highlight
val ProCheckGreen        = Color(0xFF4CAF50)
val ProPlanRecommended   = Color(0xFFFF6D00)
val ProPlanRecommendedBg = Color(0xFFFFF3E0)
val ProSkeleton          = Color(0xFFE0DCD6)  // warm skeleton

// Dark mode Pro palette
val ProDarkGradientStart = Color(0xFF141614)  // warm charcoal
val ProDarkGradientMid   = Color(0xFF1C1F1C)  // dark surface
val ProDarkGradientEnd   = Color(0xFF222622)  // elevated surface
val ProDarkAccent        = Color(0xFF8AD4D7)  // muted teal
val ProDarkAccentLight   = Color(0xFFA0E0E3)  // lighter muted teal
val ProDarkAccentSurface = Color(0xFF1E3A3C)  // dark teal surface
val ProDarkCardSurface   = Color(0xFF1C1F1C)  // dark card
val ProDarkTextPrimary   = Color(0xFFE2E3DE)  // warm white
val ProDarkTextSecondary = Color(0xFFBFC1BA)  // warm gray
val ProDarkTextMuted     = Color(0xFF7B806B)  // muted
val ProDarkBadgeBg       = Color(0xFF1E3A3C)  // dark teal badge
val ProDarkBadgeText     = Color(0xFF8AD4D7)  // muted teal badge
val ProDarkSurface       = Color(0xFF222622)  // elevated
val ProDarkSurfaceAlt    = Color(0xFF2A2E2A)  // more elevated
val ProDarkBorder        = Color(0xFF444842)  // dark border
val ProDarkBorderSelected = Color(0xFF8AD4D7) // muted teal selected
val ProDarkCtaGradientStart = Color(0xFF8AD4D7) // muted teal CTA
val ProDarkCtaGradientEnd   = Color(0xFF6BBFC3) // darker teal CTA
val ProDarkCtaDisabled   = Color(0xFF3D4238)  // dark warm gray
val ProDarkGold          = Color(0xFFD4AD4A)  // dark gold
val ProDarkCelebration   = Color(0xFF8AD4D7)  // muted teal celebration
val ProDarkCardHighlight = Color(0xFF1E3A3C)  // dark teal highlight
val ProDarkSkeleton      = Color(0xFF363B36)  // dark skeleton

// Diamond store accent
val DiamondTeal = Color(0xFF4DD9E0)

// Extra alias needed by WizardSteps.kt
val DarkOverlay = HomeDecorColors.DarkOverlay
