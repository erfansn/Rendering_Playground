package ir.erfansn.rendering_playground.element

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.erfansn.rendering_playground.entity.CircleEntity

class CircleElement(private val circleEntity: CircleEntity) : Element {

    @Composable
    override fun Render() {
        Canvas(Modifier) {
            drawCircle(
                color = circleEntity.color,
                radius = circleEntity.radius,
                center = circleEntity.center
            )
        }
    }
}
