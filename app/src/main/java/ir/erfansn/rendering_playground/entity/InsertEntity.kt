package ir.erfansn.rendering_playground.entity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class InsertEntity(
    val entities: List<Entity>,
    val scale: Float,
    val insersionPoint: Offset
) : Entity {

    override val color: Color
        get() = TODO("Not yet implemented")
}
