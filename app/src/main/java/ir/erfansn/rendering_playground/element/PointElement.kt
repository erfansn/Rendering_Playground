package ir.erfansn.rendering_playground.element

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.withMatrix
import ir.erfansn.rendering_playground.entity.PointEntity

class PointElement(private val entity: PointEntity) : Element {

    override val bounds: RectF
        get() = RectF(
            entity.position.x - 5,
            entity.position.y - 5,
            entity.position.x + 5,
            entity.position.y + 5,
        )

    override val style: ElementStyle get() =
        ElementStyle(entity.color.toArgb(), Paint.Style.FILL)

    override fun structure(path: Path, matrix: Matrix) {
        val position = floatArrayOf(entity.position.x, entity.position.y)
        matrix.mapPoints(position)
        path.addRect(
            bounds,
            Path.Direction.CW
        )
    }

    override fun render(
        canvas: Canvas,
        path: Path,
        paint: Paint,
        matrix: Matrix
    ) {
        canvas.restore()
        super.render(canvas, path, paint, matrix)
        canvas.save()
        canvas.concat(matrix)
    }
}
