package ir.erfansn.rendering_playground

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import ir.erfansn.rendering_playground.element.CircleElement
import ir.erfansn.rendering_playground.element.PolylineElement
import ir.erfansn.rendering_playground.entity.CircleEntity
import ir.erfansn.rendering_playground.entity.PolylineEntity
import kotlin.random.Random

private val randomN: () -> Float = { (Random.nextFloat() * 200_000) - 100_000f }

private val SampleColors = listOf(Color.White, Color.Red, Color.Cyan)

private val SamplePolylines = buildList {
    repeat(50_000) {
        val shouldCreateAPolyline = Random.nextBoolean()
        if (shouldCreateAPolyline) {
            val verticesCount = Random.nextInt(2, 4)
            val vertices = List(verticesCount) {
                Offset(randomN(), randomN())
            }
            add(PolylineEntity(vertices, SampleColors.random()))
        } else {
            val radius = Random.nextFloat() * 200
            val center = Offset(randomN(), randomN())
            add(CircleEntity(radius, center, SampleColors.random()))
        }
    }
}

val SampleElements = SamplePolylines.map {
    when (it) {
        is CircleEntity -> {
            CircleElement(it)
        }
        is PolylineEntity -> {
            PolylineElement(it)
        }
    }
}
