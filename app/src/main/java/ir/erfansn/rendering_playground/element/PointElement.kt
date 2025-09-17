package ir.erfansn.rendering_playground.element

import android.graphics.Matrix
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.PointMode
import ir.erfansn.rendering_playground.element.Element.Companion.paint
import ir.erfansn.rendering_playground.entity.PointEntity

class PointElement(private val entity: PointEntity) : Element {

    override fun render(
        canvas: Canvas,
        matrix: Matrix
    ) {
        canvas.drawCircle(
            entity.position.let {
                val inout = floatArrayOf(it.x, it.y)
                matrix.mapPoints(inout)
                Offset(inout[0], inout[1])
            },
            10f,
            paint.apply {
                color = entity.color
            }
        )
    }
}
