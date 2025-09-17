package ir.erfansn.rendering_playground.entity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class PointEntity(
    val position: Offset,
    override val color: Color
) : Entity
