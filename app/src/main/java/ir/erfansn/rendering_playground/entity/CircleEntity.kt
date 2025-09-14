package ir.erfansn.rendering_playground.entity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

class CircleEntity(
    val radius: Float,
    val center: Offset,
    override val color: Color
) : Entity
