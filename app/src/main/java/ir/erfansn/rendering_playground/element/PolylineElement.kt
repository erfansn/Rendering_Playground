package ir.erfansn.rendering_playground.element

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.PointMode
import ir.erfansn.rendering_playground.element.Element.Companion.paint
import ir.erfansn.rendering_playground.entity.PolylineEntity

class PolylineElement(private val polyline: PolylineEntity) : Element {

    override fun render(canvas: Canvas, matrix: android.graphics.Matrix) {
        canvas.drawPoints(
            pointMode = PointMode.Polygon,
            points = polyline.vertices.map {
                val inout = floatArrayOf(it.x, it.y)
                matrix.mapPoints(inout)
                Offset(inout[0], inout[1])
            },
            paint = paint.apply {
                color = polyline.color
            }
        )
    }
}