package com.ismail.homedecorai.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object HomeDecorShape {
    val None = RoundedCornerShape(0.dp)
    val ExtraSmall = RoundedCornerShape(4.dp)
    val Small = RoundedCornerShape(8.dp)
    val Medium = RoundedCornerShape(12.dp)
    val Large = RoundedCornerShape(16.dp)
    val ExtraLarge = RoundedCornerShape(20.dp)
    val ExtraExtraLarge = RoundedCornerShape(24.dp)
    val ExtraExtraExtraLarge = RoundedCornerShape(28.dp)
    val Full = RoundedCornerShape(9999.dp)

    val Card = RoundedCornerShape(16.dp)
    val CardLarge = RoundedCornerShape(20.dp)
    val Sheet = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val Dialog = RoundedCornerShape(24.dp)
    val Button = RoundedCornerShape(12.dp)
    val Chip = RoundedCornerShape(8.dp)
    val Input = RoundedCornerShape(12.dp)
    val Badge = RoundedCornerShape(8.dp)
    val Image = RoundedCornerShape(12.dp)
    val ImageLarge = RoundedCornerShape(16.dp)
    val FAB = RoundedCornerShape(16.dp)
    val TopAppBar = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
}

val HomeDecorShapes = Shapes(
    extraSmall = HomeDecorShape.ExtraSmall,
    small = HomeDecorShape.Small,
    medium = HomeDecorShape.Medium,
    large = HomeDecorShape.Large,
    extraLarge = HomeDecorShape.ExtraLarge,
)
