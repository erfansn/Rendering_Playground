package ir.erfansn.rendering_playground

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import ir.erfansn.rendering_playground.element.CircleElement
import ir.erfansn.rendering_playground.element.InsertElement
import ir.erfansn.rendering_playground.element.PointElement
import ir.erfansn.rendering_playground.element.PolylineElement
import ir.erfansn.rendering_playground.entity.CircleEntity
import ir.erfansn.rendering_playground.entity.Entity
import ir.erfansn.rendering_playground.entity.InsertEntity
import ir.erfansn.rendering_playground.entity.PointEntity
import ir.erfansn.rendering_playground.entity.PolylineEntity
import kotlin.random.Random

private val randomN: () -> Float = { (Random.nextFloat() * Random.nextInt(0, 200_000)) - Random.nextInt(0, 100_000) }

private val SampleColors = listOf(Color.White, Color.Red, Color.Cyan)

private val SamplePolylines = buildList {
    repeat(50_000) {
        when (Random.nextInt(4)) {
            0 -> {
                val verticesCount = Random.nextInt(2, 4)
                val vertices = List(verticesCount) {
                    Offset(randomN(), randomN())
                }
                add(PolylineEntity(vertices, SampleColors.random()))
            }
            1 -> {
                val radius = Random.nextFloat() * 200
                val center = Offset(randomN(), randomN())
                add(CircleEntity(radius, center, SampleColors.random()))
            }
            2 -> {
                val position = Offset(randomN(), randomN())
                add(PointEntity(position, SampleColors.random()))
            }
            3 -> {
                val entities = mutableListOf<Entity>()
                val radius = Random.nextFloat() * 200
                val center = Offset(randomN(), randomN())
                entities += CircleEntity(radius, center, Color.White)

                val verticesCount = Random.nextInt(2, 4)
                val vertices = List(verticesCount) {
                    Offset(randomN(), randomN())
                }
                entities += PolylineEntity(vertices, Color.White)

                val position = Offset(randomN(), randomN())
                entities += PointEntity(position, Color.White)

                val finalEntities = listOf(InsertEntity(entities, 1/2f, Offset(100_000f, 100_000f)))

                add(InsertEntity(finalEntities, 2f, Offset(100_000f, 100_000f)))

            }
        }
    }
    add(PointEntity(Offset.Zero, Color.Yellow))
}

val SampleElements = SamplePolylines.map {
    when (it) {
        is CircleEntity -> {
            CircleElement(it)
        }
        is PolylineEntity -> {
            PolylineElement(it)
        }
        is PointEntity -> {
            PointElement(it)
        }
        is InsertEntity -> {
            InsertElement(it)
        }
    }
}
