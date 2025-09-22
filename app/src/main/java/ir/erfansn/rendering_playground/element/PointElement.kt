package ir.erfansn.rendering_playground.element

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.withMatrix
import ir.erfansn.rendering_playground.entity.PointEntity

class PointElement(private val entity: PointEntity) : Element {

    override val style: ElementStyle =
        ElementStyle(entity.color.toArgb(), Paint.Style.FILL)

    override fun structure(path: Path, matrix: Matrix) {
        val position = floatArrayOf(entity.position.x, entity.position.y)
        matrix.mapPoints(position)
        path.addCircle(
            position[0],
            position[1],
            10f,
            Path.Direction.CW
        )
    }

    override fun render(
        canvas: Canvas,
        path: Path,
        paint: Paint,
        matrix: Matrix
    ) {
        val path = path.apply {
            rewind()
            structure(this, matrix)
        }
        val paint = paint.apply {
            this.color = this@PointElement.style.color
            this.style = this@PointElement.style.paintingStyle
        }

        canvas.drawPath(path, paint)
    }
}
