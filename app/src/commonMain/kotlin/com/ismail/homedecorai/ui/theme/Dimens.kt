package com.ismail.homedecorai.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// HomeDecor AI Dimensions  –  MD3 Expressive
// ---------------------------------------------------------------------------
// Consistent spacing, sizing, layout, state, and motion tokens.
// 4/8dp grid system. All values are reusable design tokens.
// ---------------------------------------------------------------------------

object HomeDecorSpacing {
    // -- Spacing scale (4/8dp grid) --
    val Xxs = 2.dp
    val Xs = 4.dp
    val Sm = 8.dp
    val Md = 12.dp
    val Base = 16.dp
    val Lg = 20.dp
    val Xl = 24.dp
    val Xxl = 32.dp
    val Xxxl = 40.dp
    val Xxxxl = 48.dp
    val Xxxxxl = 64.dp

    // -- Layout spacing --
    val SectionGap = 20.dp     // tight but breathable between sections
    val ListItemGap = 12.dp
    val ChipGap = 8.dp
    val IconGap = 8.dp
    val CardPadding = 16.dp
    val CardInternal = 16.dp
    val ScreenHorizontal = 16.dp   // aligned with native app for tighter feel
    val ScreenVerticalPadding = 20.dp
    val HeroVerticalPadding = 40.dp
    val ContentGap = 16.dp       // gap between content sections

    // -- Component sizing --
    val ButtonHeight = 56.dp
    val ButtonHeightMedium = 48.dp
    val ButtonHeightSmall = 40.dp
    val TouchTarget = 48.dp
    val CtaBarHeight = 64.dp
    val NavBarReservation = 80.dp
    val BottomContentPadding = 16.dp
    val WizardBottomContentPadding = 80.dp

    // -- Desktop layout --
    val DesktopMaxWidth = 1200.dp
    val DesktopTopNavHeight = 60.dp    // slightly taller for premium feel
    val DesktopSideRailWidth = 80.dp
    val DesktopContentHorizontalPadding = 40.dp  // generous desktop spacing
}

// ---------------------------------------------------------------------------
// State layer opacities  –  MD3 Expressive state layers
// ---------------------------------------------------------------------------

object HomeDecorStateLayers {
    /** Hover state layer opacity (web only) */
    const val Hover = 0.06f
    /** Pressed / focused state layer opacity */
    const val Pressed = 0.10f
    /** Dragged state layer opacity */
    const val Dragged = 0.16f
    /** Disabled state layer opacity */
    const val DisabledContainer = 0.12f
    /** Disabled content opacity */
    const val DisabledContent = 0.38f
    /** Disabled outline opacity */
    const val DisabledOutline = 0.38f
    /** Focus ring opacity */
    const val FocusRing = 0.24f
}

// ---------------------------------------------------------------------------
// Motion tokens  –  MD3 Expressive transitions
// ---------------------------------------------------------------------------

object HomeDecorMotion {
    /** Duration for micro-interactions (hover, press, focus) */
    const val DurationShort1 = 50
    const val DurationShort2 = 100
    /** Standard transition duration (150ms) */
    const val DurationMedium1 = 150
    /** Default transition duration (200ms) */
    const val DurationMedium2 = 200
    /** Complex transitions (page, sheet) */
    const val DurationMedium3 = 250
    /** Long transitions (modals, drawers) */
    const val DurationLong1 = 300
    const val DurationLong2 = 400
    /** Very long transitions (hero) */
    const val DurationExtraLong = 500

    /** Easing: standard */
    const val EasingStandard = 0.2f
    /** Easing: decelerate (entering) */
    const val EasingDecelerate = 0.0f
    /** Easing: accelerate (exiting) */
    const val EasingAccelerate = 0.3f
}

// ---------------------------------------------------------------------------
// Elevation tokens  –  MD3 Expressive
// ---------------------------------------------------------------------------

object HomeDecorElevation {
    val Level0 = 0.dp
    val Level1 = 1.dp
    val Level2 = 3.dp
    val Level3 = 6.dp
    val Level4 = 8.dp
    val Level5 = 12.dp

    // Expressive additions
    val Level6 = 16.dp
    val Level7 = 24.dp

    // Semantic aliases
    val CardRest = Level0
    val CardHover = Level2
    val CardPressed = Level3
    val DialogElevation = Level5
    val SheetElevation = Level3
    val NavElevation = Level2
}

// ---------------------------------------------------------------------------
// Breakpoint tokens  –  responsive web
// ---------------------------------------------------------------------------

object HomeDecorBreakpoints {
    /** Mobile: < 768px */
    const val Mobile = 390
    /** Tablet: 768px - 1023px */
    const val Tablet = 768
    /** Desktop: 1024px - 1439px */
    const val Desktop = 1024
    /** Wide: >= 1440px */
    const val Wide = 1440
    /** Ultra-wide: >= 1920px */
    const val UltraWide = 1920
}

fun navBarBottomPadding(additionalContentPadding: Dp = 0.dp): Dp {
    return HomeDecorSpacing.NavBarReservation + additionalContentPadding
}
