package ir.erfansn.rendering_playground.element

import android.graphics.Matrix
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.util.fastForEach
import ir.erfansn.rendering_playground.entity.CircleEntity
import ir.erfansn.rendering_playground.entity.InsertEntity
import ir.erfansn.rendering_playground.entity.PointEntity
import ir.erfansn.rendering_playground.entity.PolylineEntity

class InsertElement(entity: InsertEntity) : Element {

    private val elements = entity.entities.map {
        when (it) {
            is CircleEntity -> CircleElement(it)
            is InsertEntity -> InsertElement(it)
            is PointEntity -> PointElement(it)
            is PolylineEntity -> PolylineElement(it)
        }
    }

    private val innerMatrix = Matrix().apply {
        setScale(entity.scale, entity.scale)
        postTranslate(entity.insersionPoint.x, entity.insersionPoint.y)
    }

    override fun render(
        canvas: Canvas,
        matrix: Matrix
    ) {
        elements.fastForEach {
            it.render(canvas, Matrix(innerMatrix).also { it.postConcat(Matrix(matrix)) })
        }
    }
}
