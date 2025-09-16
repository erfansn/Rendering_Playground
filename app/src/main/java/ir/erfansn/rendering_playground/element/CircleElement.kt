package ir.erfansn.rendering_playground.element

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import ir.erfansn.rendering_playground.element.Element.Companion.paint
import ir.erfansn.rendering_playground.entity.CircleEntity

class CircleElement(private val circleEntity: CircleEntity) : Element {

    override fun render(canvas: Canvas) {
        canvas.drawCircle(
            paint = paint.apply {
                color = circleEntity.color
            },
            radius = circleEntity.radius,
            center = circleEntity.center
        )
    }
}
