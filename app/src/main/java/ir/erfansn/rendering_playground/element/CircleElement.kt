package ir.erfansn.rendering_playground.element

import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import ir.erfansn.rendering_playground.element.Element.Companion.paint
import ir.erfansn.rendering_playground.entity.CircleEntity

class CircleElement(private val circleEntity: CircleEntity) : Element {

    override fun render(canvas: Canvas, matrix: android.graphics.Matrix) {
        canvas.drawCircle(
            paint = paint.apply {
                color = circleEntity.color
            },
            radius = circleEntity.radius.let { matrix.mapRadius(it) },
            center = circleEntity.center.let {
                val inout = floatArrayOf(it.x, it.y)
                matrix.mapPoints(inout)
                Offset(inout[0], inout[1])
            }
        )
    }
}
