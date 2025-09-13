package ir.erfansn.rendering_playground

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import kotlin.collections.emptyList

class PolylineElement(private val polyline: Polyline) {

    @Composable
    fun Render() {
        Canvas(Modifier) {
            drawPoints(
                points = polyline.vertices,
                pointMode = PointMode.Polygon,
                color = Color.White
            )
        }
    }
}
