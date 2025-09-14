package ir.erfansn.rendering_playground

import android.graphics.PointF
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Polyline(
    val vertices: List<Offset>,
    val color: Color
)
