package ir.erfansn.rendering_playground.element

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.nativeCanvas
import ir.erfansn.rendering_playground.element.Element.Companion.paint
import ir.erfansn.rendering_playground.entity.PolylineEntity

class PolylineElement(private val polyline: PolylineEntity) : Element {

    override fun render(canvas: Canvas, matrix: android.graphics.Matrix) {
        canvas.nativeCanvas.save()
        canvas.nativeCanvas.concat(matrix)
        canvas.drawPoints(
            pointMode = PointMode.Polygon,
            points = polyline.vertices,
            paint = paint.apply {
                color = polyline.color
            }
        )
        canvas.nativeCanvas.restore()
    }
}