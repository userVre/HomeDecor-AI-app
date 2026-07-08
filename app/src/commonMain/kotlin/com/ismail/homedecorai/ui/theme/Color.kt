package com.ismail.homedecorai.ui.theme

import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------------------
// HomeDecor AI  –  Material 3 Expressive Design Foundation
// ---------------------------------------------------------------------------
// Palette philosophy (v2 — intentional contrast & role diversity)
//   Light  → warm ivory surfaces, deep charcoal text, rich teal creative
//            accents, terracotta/clay warmth, muted gold premium highlights.
//            Premium, grounded, trustworthy.
//            Each color role (primary/secondary/tertiary/quaternary) is used
//            intentionally across different UI contexts so no single accent
//            dominates every screen.
//   Dark   → warm charcoal / deep green-gray surfaces, calm muted accents.
//            Never pure black. Premium & calm.
//
// Key principle: Every surface level must be visually distinct.
//   Canvas ≠ SurfaceContainerLow ≠ SurfaceContainer ≠ SurfaceContainerHigh
//   Cards on Canvas must "pop" — not blend into the background.
//
// M3 Expressive additions:
//   - Expanded shape system (larger radii for hero surfaces)
//   - More varied component styling
//   - Motion/emphasis patterns via state layers
// ---------------------------------------------------------------------------

object HomeDecorColors {

    // =========================== LIGHT MODE =================================

    // -- Surfaces (warm ivory family — distinct elevation hierarchy) --
    val Canvas        = Color(0xFFF5F0EA)   // main app background — warm ivory, NOT white
    val Paper         = Color(0xFFFFFFFF)   // cards, sheets, dialogs — pure white pops on Canvas
    val Mist          = Color(0xFFEDE8E1)   // subtle recessed areas — visible tint
    val SurfaceDim    = Color(0xFFD4CEC5)   // dimmed surface overlays
    val SurfaceBright = Color(0xFFFEFDFB)   // brightest surface

    // MD3 surface container scale — each step clearly distinguishable
    val SurfaceContainerLowest  = Color(0xFFFFFFFF)  // white — cards on canvas
    val SurfaceContainerLow     = Color(0xFFF8F5F0)  // slightly warm
    val SurfaceContainer        = Color(0xFFEFEBE5)  // warm middle ground
    val SurfaceContainerHigh    = Color(0xFFE5E0D9)  // distinct from Container
    val SurfaceContainerHighest = Color(0xFFDBD5CC)  // clearly darker

    // -- Text (deep charcoal family — high contrast on ivory) --
    val Ink            = Color(0xFF1A1714)  // primary headings — highest contrast
    val InkSoft        = Color(0xFF4A4540)  // secondary body text — bumped contrast
    val OnSurface      = Color(0xFF1C1B18)  // MD3 primary text
    val OnSurfaceVariant = Color(0xFF3D3833) // MD3 secondary text — strong contrast
    val OnBackground   = Color(0xFF1C1B18)

    // -- Lines & borders --
    val Line           = Color(0xFFD0C9C0)  // dividers, card borders
    val Outline        = Color(0xFF6B6459)  // MD3 outline — stronger
    val OutlineVariant = Color(0xFFBFBAB0)  // MD3 outlineVariant

    // -- Primary: Deep Teal (creative, trustworthy) --
    val Primary            = Color(0xFF1D5C5F)  // deepened teal — richer, more premium
    val OnPrimary          = Color(0xFFFFFFFF)
    val PrimaryContainer   = Color(0xFFC1E4E7)  // light teal tint — more saturated
    val OnPrimaryContainer = Color(0xFF0A1F21)

    // -- Secondary: Sage Green (natural, garden) --
    val Secondary            = Color(0xFF3D6B45)  // richer sage — deeper
    val OnSecondary          = Color(0xFFFFFFFF)
    val SecondaryContainer   = Color(0xFFC8E3CE)  // stronger green wash
    val OnSecondaryContainer = Color(0xFF0D2312)

    // -- Tertiary: Warm Gold (premium, diamond) --
    val Tertiary            = Color(0xFF7A5B10)  // deeper warm gold
    val OnTertiary          = Color(0xFFFFFFFF)
    val TertiaryContainer   = Color(0xFFF5DFA0)  // richer gold wash
    val OnTertiaryContainer = Color(0xFF2C1E00)

    // -- Quaternary: Terracotta / Clay (home, warmth) --
    val Quaternary            = Color(0xFFB85C38)  // rich terracotta — warmth, earthiness
    val OnQuaternary          = Color(0xFFFFFFFF)
    val QuaternaryContainer   = Color(0xFFFDDDD0)  // soft clay wash
    val OnQuaternaryContainer = Color(0xFF3B1508)

    // -- Error --
    val ErrorColor          = Color(0xFFBA1A1A)
    val OnError             = Color(0xFFFFFFFF)
    val ErrorContainerColor = Color(0xFFFFDAD6)
    val OnErrorContainer    = Color(0xFF410002)

    // -- Success --
    val Success           = Color(0xFF2E7D32)  // deeper, more authoritative green
    val SuccessContainer  = Color(0xFFC8E6C9)

    // -- Warning --
    val Warning           = Color(0xFF9A6B00)  // deeper gold warning
    val WarningContainer  = Color(0xFFFFF3D6)

    // -- Semantic / brand tokens (diversified — not all teal) --
    val Accent            = Color(0xFF1D5C5F)   // creative teal accent (primary only)
    val AccentContainer   = Color(0xFFC1E4E7)
    val DiamondAccent     = Color(0xFF2EC4B6)   // teal diamond sparkle
    val ProAccent         = Color(0xFF1D5C5F)   // subscription teal
    val PremiumGold       = Color(0xFFA07E2E)   // premium badge gold — richer
    val GoldDeep          = Color(0xFF7A5B10)

    // -- Contextual accents (reduce teal overuse) --
    val BoardAccent       = Color(0xFF3D6B45)   // sage green for My Board
    val DiscoverAccent    = Color(0xFF4A3FBF)   // indigo for Discover
    val ProfileAccent     = Color(0xFFB85C38)   // terracotta for Profile
    val SettingsAccent    = Color(0xFF6B6459)   // warm neutral for Settings
    val StoreAccent       = Color(0xFF7A5B10)   // gold for Store
    val ToolsAccent       = Color(0xFF1D5C5F)   // teal for Tools (creative hub)

    // -- Scrim & overlays --
    val Scrim              = Color(0xFF000000)   // standard MD3 scrim
    val ScrimLight         = Color(0x1A000000)   // subtle overlay (10% black)
    val ScrimMedium        = Color(0x33000000)   // medium overlay (20% black)
    val ScrimHeavy         = Color(0x66000000)   // heavy overlay for text readability (40% black)
    val OnGradientText     = Color(0xFFFFFFFF)   // text/icon over gradient backgrounds
    val OnGradientTextSubtle = Color(0xE6FFFFFF) // 90% white for secondary text on gradient

    // -- Tool gradient palettes (consolidated from App.kt) --
    val ToolGradientInteriorStart   = Color(0xFF1D5C5F)
    val ToolGradientInteriorEnd     = Color(0xFF0F3A3C)
    val ToolGradientExteriorStart   = Color(0xFF2D4A85)
    val ToolGradientExteriorEnd     = Color(0xFF1A2E5A)
    val ToolGradientGardenStart     = Color(0xFF2A5E42)
    val ToolGradientGardenEnd       = Color(0xFF183828)
    val ToolGradientPaintStart      = Color(0xFFB85C38)
    val ToolGradientPaintEnd        = Color(0xFF7A3020)
    val ToolGradientFloorStart      = Color(0xFF7A5B10)
    val ToolGradientFloorEnd        = Color(0xFF4E3A0A)
    val ToolGradientLayoutStart     = Color(0xFF4A3FBF)
    val ToolGradientLayoutEnd       = Color(0xFF2E2580)
    val ToolGradientReplaceStart    = Color(0xFFA05030)
    val ToolGradientReplaceEnd      = Color(0xFF6A2E18)
    val ToolGradientReferenceStart  = Color(0xFF1A3050)
    val ToolGradientReferenceEnd    = Color(0xFF0C1E35)

    // -- Diamond store pack accents --
    val DiamondStoreTeal   = Color(0xFF2EC4B6)
    val DiamondStorePurple = Color(0xFF8B5CF6)
    val DiamondStoreGreen  = Color(0xFF22C55E)
    val DiamondStoreGold   = Color(0xFFF59E0B)

    // -- Diamond store pack top surfaces --
    val PackTopTeal        = Color(0xFFD5F0ED)
    val PackTopPurple      = Color(0xFFE8E0FF)
    val PackTopGreen       = Color(0xFFD5F0DC)
    val PackTopGold        = Color(0xFFFFF0D0)

    // -- Paywall specific --
    val GoldSurface        = Color(0xFFFFF5E0)

    // =========================== DARK MODE ==================================
    // Warm charcoal + deep green-gray. Never pure black.

    // -- Surfaces --
    val Dark              = Color(0xFF121412)   // app background — warm charcoal-green
    val DarkSurface       = Color(0xFF1A1D1A)   // cards, sheets
    val DarkOverlay       = Color(0xFF252825)   // elevated surfaces, modals
    val DarkSurfaceDim    = Color(0xFF0E100E)
    val DarkSurfaceBright = Color(0xFF2A2D2A)
    val DarkSurfaceContainerLowest  = Color(0xFF121412)
    val DarkSurfaceContainerLow     = Color(0xFF1A1D1A)
    val DarkSurfaceContainer        = Color(0xFF202320)
    val DarkSurfaceContainerHigh    = Color(0xFF2A2D2A)
    val DarkSurfaceContainerHighest = Color(0xFF343834)

    // -- Dark text --
    val DarkOnSurface          = Color(0xFFE5E6E0)
    val DarkOnSurfaceVariant   = Color(0xFFB8B9B2)
    val DarkOnBackground       = Color(0xFFE5E6E0)

    // -- Dark outlines --
    val DarkOutline        = Color(0xFF7A7C75)
    val DarkOutlineVariant = Color(0xFF3D4038)

    // -- Dark primary: muted teal --
    val DarkPrimary            = Color(0xFF7ECDD0)
    val DarkOnPrimary          = Color(0xFF003738)
    val DarkPrimaryContainer   = Color(0xFF1A4A4C)
    val DarkOnPrimaryContainer = Color(0xFFC0E4E7)

    // -- Dark secondary: muted sage --
    val DarkSecondary            = Color(0xFF9CC8A3)
    val DarkOnSecondary          = Color(0xFF16381C)
    val DarkSecondaryContainer   = Color(0xFF2A4A30)
    val DarkOnSecondaryContainer = Color(0xFFC8E3CE)

    // -- Dark tertiary: muted gold --
    val DarkTertiary            = Color(0xFFDAB660)
    val DarkOnTertiary          = Color(0xFF3A2D00)
    val DarkTertiaryContainer   = Color(0xFF4A3A0A)
    val DarkOnTertiaryContainer = Color(0xFFF5DFA0)

    // -- Dark quaternary: muted terracotta --
    val DarkQuaternary            = Color(0xFFD4896A)
    val DarkOnQuaternary          = Color(0xFF3B1508)
    val DarkQuaternaryContainer   = Color(0xFF4A2518)
    val DarkOnQuaternaryContainer = Color(0xFFFDDDD0)

    // -- Dark error --
    val DarkError          = Color(0xFFF2B8B5)
    val DarkOnError        = Color(0xFF601410)
    val DarkErrorContainer = Color(0xFF8C1D18)
    val DarkOnErrorContainer = Color(0xFFF2B8B5)

    // -- Dark success --
    val DarkSuccess          = Color(0xFF81C995)
    val DarkSuccessContainer = Color(0xFF1A3D1F)

    // -- Dark warning --
    val DarkWarning          = Color(0xFFFFD166)
    val DarkWarningContainer = Color(0xFF3D3100)

    // -- Dark accent tokens (diversified) --
    val DarkAccent        = Color(0xFF7ECDD0)
    val DarkAccentContainer = Color(0xFF1A4A4C)
    val DarkDiamondAccent = Color(0xFF4DD9D0)
    val DarkProAccent     = Color(0xFF6ACFC7)
    val DarkPremiumGold   = Color(0xFFC8A040)
    val DarkGoldDeep      = Color(0xFFA07E2E)

    // -- Dark contextual accents --
    val DarkBoardAccent    = Color(0xFF9CC8A3)
    val DarkDiscoverAccent = Color(0xFF8B83D4)
    val DarkProfileAccent  = Color(0xFFD4896A)
    val DarkSettingsAccent = Color(0xFF9A9A90)
    val DarkStoreAccent    = Color(0xFFDAB660)
    val DarkToolsAccent    = Color(0xFF7ECDD0)

    // -- Dark scrim --
    val DarkScrim         = Color(0xFF000000)
    val DarkScrimLight    = Color(0x1A000000)
    val DarkScrimMedium   = Color(0x33000000)
    val DarkScrimHeavy    = Color(0x66000000)
    val DarkOnGradientText     = Color(0xFFFFFFFF)
    val DarkOnGradientTextSubtle = Color(0xE6FFFFFF)

    // -- Dark diamond store pack top surfaces --
    val DarkPackTopTeal   = Color(0xFF0A1E2E)
    val DarkPackTopPurple = Color(0xFF18103A)
    val DarkPackTopGreen  = Color(0xFF0A2518)
    val DarkPackTopGold   = Color(0xFF251A08)

    // -- Dark paywall --
    val DarkGoldSurface   = Color(0xFF352A00)
    val DarkCheckGreen    = Color(0xFF81C995)
}

// ===========================================================================
// Backward-compatible top-level aliases (13+ screen files consume these)
// ===========================================================================

// Core palette — only those actually referenced in code
val StudioAccent         = HomeDecorColors.Accent
val StudioBlack          = HomeDecorColors.DarkSurface
val StudioGold           = HomeDecorColors.GoldDeep
val ProAccent            = HomeDecorColors.ProAccent

// Pro / Upgrade palette — MD3 Expressive (consumed by PaywallSheet.kt, UpgradeScreen.kt)
val ProDarkStart     = Color(0xFFE8F5F3)
val ProDarkMid       = Color(0xFFF2F8F6)
val ProDarkEnd       = Color(0xFFFFFFFF)
val ProCtaAccent     = Color(0xFF1A6B63)
val ProAccentLight   = Color(0xFF3A8F85)
val ProAccentSurface = Color(0xFFE0F5F2)
val ProCardSurface   = Color(0xFFFFFFFF)
val ProTextPrimary   = Color(0xFF1A1714)
val ProTextSecondary = Color(0xFF5C5549)
val ProTextMuted     = Color(0xFFA8A29E)
val ProBadgeBg       = Color(0xFFD6F0ED)
val ProBadgeText     = Color(0xFF1A6B63)
val ProSurface       = Color(0xFFFAFAF7)
val ProSurfaceAlt    = Color(0xFFF3F1EC)
val ProBorder        = Color(0xFFE0DCD6)
val ProBorderSelected = Color(0xFF1A6B63)
val ProCtaDisabled   = Color(0xFFB8B2A8)
val ProGold          = Color(0xFFE8A825)
val ProMint          = Color(0xFF4ECDC4)
val ProCheckGreen    = Color(0xFF3A9D4A)
val ProHeroDivider   = Color(0xFF1A6B63)

// Dark mode Pro palette
val ProDarkGradientStart = Color(0xFF141614)
val ProDarkGradientMid   = Color(0xFF1C1F1C)
val ProDarkGradientEnd   = Color(0xFF222622)
val ProDarkAccent        = Color(0xFF6ACFC7)
val ProDarkAccentLight   = Color(0xFF8AE0D8)
val ProDarkAccentSurface = Color(0xFF1E3A38)
val ProDarkCardSurface   = Color(0xFF1C1F1C)
val ProDarkTextPrimary   = Color(0xFFE2E3DE)
val ProDarkTextSecondary = Color(0xFFBFC1BA)
val ProDarkTextMuted     = Color(0xFF7B806B)
val ProDarkBadgeBg       = Color(0xFF1E3A38)
val ProDarkBadgeText     = Color(0xFF6ACFC7)
val ProDarkSurface       = Color(0xFF222622)
val ProDarkSurfaceAlt    = Color(0xFF2A2E2A)
val ProDarkBorder        = Color(0xFF444842)
val ProDarkBorderSelected = Color(0xFF6ACFC7)
val ProDarkCtaDisabled   = Color(0xFF3D4238)
val ProDarkGold          = Color(0xFFD4AD4A)
val ProDarkMint          = Color(0xFF3AAFA8)
val ProDarkHeroDivider   = Color(0xFF6ACFC7)

// Diamond store accent
val DiamondTeal = HomeDecorColors.DiamondStoreTeal

// Extra alias needed by WizardSteps.kt
val DarkOverlay = HomeDecorColors.DarkOverlay
