package ir.erfansn.rendering_playground.element

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PointMode
import ir.erfansn.rendering_playground.entity.PolylineEntity

class PolylineElement(private val polyline: PolylineEntity) : Element {

    @Composable
    override fun Render() {
        Canvas(Modifier.Companion) {
            drawPoints(
                points = polyline.vertices,
                pointMode = PointMode.Companion.Polygon,
                color = polyline.color
            )
        }
    }
}