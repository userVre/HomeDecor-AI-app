package com.ismail.homedecorai.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------------------
// HomeDecor AI Shapes  –  MD3 Expressive
// ---------------------------------------------------------------------------
// Rounded but controlled. Larger radii for hero surfaces (cards, sheets),
// tighter radii for small interactive elements (chips, badges).
// Expressive: slightly larger radii than standard MD3 for premium feel.
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

    // Semantic aliases for common surfaces
    val Card = RoundedCornerShape(16.dp)
    val CardLarge = RoundedCornerShape(20.dp)
    val Dialog = RoundedCornerShape(28.dp)
    val BottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    val Button = RoundedCornerShape(12.dp)
    val ButtonLarge = RoundedCornerShape(16.dp)
    val Chip = RoundedCornerShape(8.dp)
    val ChipSelected = RoundedCornerShape(10.dp)
    val Badge = RoundedCornerShape(8.dp)
    val ImageLarge = RoundedCornerShape(16.dp)
    val NavBar = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    val TopNav = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)

    // Expressive additions
    val Pill = RoundedCornerShape(9999.dp)
    val PillMedium = RoundedCornerShape(20.dp)
}

val HomeDecorShapes = Shapes(
    extraSmall = HomeDecorShape.ExtraSmall,
    small = HomeDecorShape.Small,
    medium = HomeDecorShape.Medium,
    large = HomeDecorShape.Large,
    extraLarge = HomeDecorShape.ExtraLarge,
)
