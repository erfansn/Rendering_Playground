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

    override val bounds: RectF = RectF(
        entity.position.x - POINT_SIZE,
        entity.position.y - POINT_SIZE,
        entity.position.x + POINT_SIZE,
        entity.position.y + POINT_SIZE,
    )

    override val style: ElementStyle get() =
        ElementStyle(entity.color.toArgb(), Paint.Style.FILL)

    private val mappedPosition = floatArrayOf(entity.position.x, entity.position.y)

    override fun structure(path: Path, matrix: Matrix) {
        mappedPosition[0] = entity.position.x; mappedPosition[1] = entity.position.y
        matrix.mapPoints(mappedPosition)
        path.addRect(
            mappedPosition[0] - POINT_SIZE,
            mappedPosition[1] - POINT_SIZE,
            mappedPosition[0] + POINT_SIZE,
            mappedPosition[1] + POINT_SIZE,
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

    companion object {
        private const val POINT_SIZE = 5
    }
}
