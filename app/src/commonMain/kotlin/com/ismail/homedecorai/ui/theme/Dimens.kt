package com.ismail.homedecorai.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// AppTokens  –  Single source of truth for layout constants & brand tokens
// ---------------------------------------------------------------------------

object AppTokens {
    val MaxContentWidth = 1280.dp
    val ContentPadding = 24.dp
    val CardRadius = 20.dp
    val ChipRadius = 16.dp
    val SectionGap = 48.dp
    val ItemGap = 16.dp

    // Brand colors (canonical references)
    val Primary = Color(0xFF0F4C4C)
    val Surface = Color(0xFFFBF8F5)
}

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
    val SectionGap = 28.dp     // generous breathing room between sections
    val ListItemGap = 14.dp
    val ChipGap = 8.dp
    val IconGap = 8.dp
    val CardPadding = 20.dp
    val CardInternal = 16.dp
    val ScreenHorizontal = 20.dp   // slightly more horizontal padding
    val ScreenVerticalPadding = 24.dp
    val HeroVerticalPadding = 40.dp
    val ContentGap = 20.dp       // gap between content sections

    // -- Component sizing --
    val ButtonHeight = 56.dp
    val ButtonHeightMedium = 48.dp
    val ButtonHeightSmall = 40.dp
    val TouchTarget = 48.dp
    val MinTouchTarget = 44.dp   // WCAG minimum hit area
    val CtaBarHeight = 64.dp
    val NavBarReservation = 80.dp
    val BottomContentPadding = 16.dp
    val WizardBottomContentPadding = 80.dp

    // -- Desktop layout --
    val DesktopMaxWidth = 1280.dp
    val DesktopTopNavHeight = 60.dp    // slightly taller for premium feel
    val DesktopSideRailWidth = 80.dp
    val DesktopContentHorizontalPadding = 40.dp  // generous desktop spacing
}

// ---------------------------------------------------------------------------
// Icon size scale  –  Consistent sizing across the app
// ---------------------------------------------------------------------------

object HomeDecorIconSize {
    /** Inline icons in chips, badges, small UI elements */
    val Small = 16.dp
    /** Standard icons in buttons, list items, card actions */
    val Medium = 20.dp
    /** Standalone icons, navigation items, section headers */
    val Large = 24.dp
    /** Empty states, hero icons, loading indicators */
    val Xl = 32.dp
    /** Large empty state icons */
    val Xxl = 40.dp
    /** Extra-large decorative icons */
    val Xxxl = 56.dp
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

    // Surface panel elevation (soft, natural shadows for content panels)
    val SurfacePanel = Level2      // 3.dp — default content panel
    val SurfacePanelElevated = Level3  // 6.dp — prominent panels (settings dialog, sidebar)
}

fun navBarBottomPadding(additionalContentPadding: Dp = 0.dp): Dp {
    return HomeDecorSpacing.NavBarReservation + additionalContentPadding
}
