package ir.erfansn.rendering_playground.element

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.graphics.toArgb
import ir.erfansn.rendering_playground.entity.PointEntity

class PointElement(private val entity: PointEntity) : Element {

    val position = entity.position

    override val bounds: RectF = RectF(
        entity.position.x - POINT_SIZE,
        entity.position.y - POINT_SIZE,
        entity.position.x + POINT_SIZE,
        entity.position.y + POINT_SIZE,
    )

    override val style: ElementStyle get() =
        ElementStyle(entity.color.toArgb(), Paint.Style.FILL)

    override fun structure(path: Path, matrix: Matrix, zoom: Float) {
        path.addRect(
            entity.position.x - POINT_SIZE,
            entity.position.y - POINT_SIZE,
            entity.position.x + POINT_SIZE,
            entity.position.y + POINT_SIZE,
            Path.Direction.CW
        )
    }

    companion object {
        private const val POINT_SIZE = 5
    }
}
