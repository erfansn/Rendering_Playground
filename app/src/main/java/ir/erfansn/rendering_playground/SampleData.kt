package ir.erfansn.rendering_playground

import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import kotlin.random.Random

private val SamplePolylines = buildList {
    repeat(50_000) {
        val verticesCount = Random.nextInt(2, 4)
        val vertices = List(verticesCount) {
            val randomN: () -> Float = { (Random.nextFloat() * 200_000) - 100_000f }
            Offset(randomN(), randomN())
        }
        add(Polyline(vertices))
    }
}

val SamplePolylineElements = SamplePolylines.map { PolylineElement(it) }
