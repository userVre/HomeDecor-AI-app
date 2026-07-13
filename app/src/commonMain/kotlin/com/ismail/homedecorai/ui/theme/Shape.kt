package com.ismail.homedecorai.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// HomeDecor AI Shapes  –  MD3 Expressive (v2)
// ---------------------------------------------------------------------------
// M3 Expressive: larger radii for hero surfaces, more varied component shapes.
// Rounded but controlled. Premium feel through generous corner radii.
// Expressive: shapes communicate hierarchy — larger = more prominent.
// ---------------------------------------------------------------------------

object HomeDecorShape {
    val None = RoundedCornerShape(0.dp)
    val ExtraSmall = RoundedCornerShape(4.dp)
    val Small = RoundedCornerShape(8.dp)
    val Medium = RoundedCornerShape(12.dp)
    val Large = RoundedCornerShape(16.dp)
    val ExtraLarge = RoundedCornerShape(20.dp)
    val ExtraExtraLarge = RoundedCornerShape(28.dp)
    val Full = RoundedCornerShape(9999.dp)

    // Semantic aliases for common surfaces — M3 Expressive sizes
    val Card = RoundedCornerShape(20.dp)          // 16→20: more expressive
    val CardLarge = RoundedCornerShape(24.dp)     // 20→24: hero cards
    val Dialog = RoundedCornerShape(32.dp)        // 28→32: expressive dialogs
    val BottomSheet = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    val Button = RoundedCornerShape(14.dp)        // 12→14: slightly rounder
    val ButtonLarge = RoundedCornerShape(20.dp)   // 16→20: large CTA
    val Chip = RoundedCornerShape(12.dp)          // 8→12: more expressive chips
    val ChipSelected = RoundedCornerShape(14.dp)  // 10→14: selected chips
    val Badge = RoundedCornerShape(10.dp)         // 8→10: badges
    val ImageLarge = RoundedCornerShape(20.dp)    // 16→20: image frames
    val NavBar = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)  // 16→20
    val TopNav = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)

    // M3 Expressive additions — expanded shape vocabulary
    val Pill = RoundedCornerShape(9999.dp)
    val PillMedium = RoundedCornerShape(24.dp)     // 20→24: larger pills
    val HeroCard = RoundedCornerShape(28.dp)       // hero surfaces
    val ModalSheet = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)  // expressive sheets
    val FloatingAction = RoundedCornerShape(20.dp)
    val NavigationBar = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val TopAppBar = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
    val Snackbar = RoundedCornerShape(16.dp)
    val Tooltip = RoundedCornerShape(12.dp)

    // Content panel shape — soft 14dp rounding for modular sections (sidebar, panels, cards)
    val ContentPanel = RoundedCornerShape(14.dp)
}

val HomeDecorShapes = Shapes(
    extraSmall = HomeDecorShape.ExtraSmall,
    small = HomeDecorShape.Small,
    medium = HomeDecorShape.Medium,
    large = HomeDecorShape.Large,
    extraLarge = HomeDecorShape.ExtraLarge,
)
