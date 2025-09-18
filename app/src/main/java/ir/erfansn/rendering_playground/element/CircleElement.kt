package ir.erfansn.rendering_playground.element

import android.graphics.RectF
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.nativeCanvas
import ir.erfansn.rendering_playground.element.Element.Companion.paint
import ir.erfansn.rendering_playground.entity.CircleEntity

class CircleElement(private val circleEntity: CircleEntity) : Element {

    override fun render(canvas: Canvas, matrix: android.graphics.Matrix) {
        canvas.nativeCanvas.save()
        canvas.nativeCanvas.concat(matrix)
        canvas.drawCircle(
            paint = paint.apply {
                color = circleEntity.color
            },
            radius = circleEntity.radius,
            center = circleEntity.center
        )
        canvas.nativeCanvas.restore()
    }
}
