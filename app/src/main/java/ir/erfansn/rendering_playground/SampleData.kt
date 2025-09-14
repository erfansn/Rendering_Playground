package ir.erfansn.rendering_playground

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private val SampleColors = listOf(Color.White, Color.Red, Color.Cyan)

private val SamplePolylines = buildList {
    repeat(50_000) {
        val verticesCount = Random.nextInt(2, 4)
        val vertices = List(verticesCount) {
            val randomN: () -> Float = { (Random.nextFloat() * 200_000) - 100_000f }
            Offset(randomN(), randomN())
        }
        add(Polyline(vertices, SampleColors.random()))
    }
}

val GroupedPolylineElements = SamplePolylines.groupBy { it.color }.map { (color, polylines) ->
    PolylineElement(
        Polyline(
            polylines.map { it.vertices }.reduce { acc, vertices -> acc + vertices },
            color
        )
    )
}
