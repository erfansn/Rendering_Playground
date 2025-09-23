package ir.erfansn.rendering_playground.element

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.util.fastForEach
import androidx.core.graphics.withMatrix
import ir.erfansn.rendering_playground.entity.CircleEntity
import ir.erfansn.rendering_playground.entity.InsertEntity
import ir.erfansn.rendering_playground.entity.PointEntity
import ir.erfansn.rendering_playground.entity.PolylineEntity

class InsertElement(entity: InsertEntity) : Element {

    override val style: ElementStyle
        get() = error("not supported")

    override fun structure(path: Path, matrix: Matrix) {
        error("not supported")
    }

    private val elements = entity.entities.map {
        when (it) {
            is CircleEntity -> CircleElement(it)
            is InsertEntity -> InsertElement(it)
            is PointEntity -> PointElement(it)
            is PolylineEntity -> PolylineElement(it)
        }
    }

    private val innerMatrix = Matrix().apply {
        setTranslate(entity.insersionPoint.x, entity.insersionPoint.y)
        postScale(entity.scale, entity.scale)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun render(canvas: Canvas, path: Path, paint: Paint, matrix: Matrix) {
        canvas.withMatrix(innerMatrix) {
            elements.fastForEach {
                it.render(this, path, paint, innerMatrix)
            }
        }
    }
}
