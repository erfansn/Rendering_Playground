package ir.erfansn.rendering_playground.element

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.toArgb
import ir.erfansn.rendering_playground.entity.PolylineEntity

class PolylineElement(private val entity: PolylineEntity) : Element {

    override val style: ElementStyle =
        ElementStyle(entity.color.toArgb(), Paint.Style.STROKE)

    override fun structure(path: Path, matrix: Matrix) {
        val firstVertex = entity.vertices[0]
        path.moveTo(firstVertex.x, firstVertex.y)
        for ((x, y) in entity.vertices.drop(1)) {
            path.lineTo(x, y)
        }
    }

}