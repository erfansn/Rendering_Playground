package ir.erfansn.rendering_playground.element

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.graphics.toArgb
import ir.erfansn.rendering_playground.entity.CircleEntity

class CircleElement(private val entity: CircleEntity) : Element {

    override val bounds: RectF = RectF(
        entity.center.x - entity.radius,
        entity.center.y - entity.radius,
        entity.center.x + entity.radius,
        entity.center.y + entity.radius
    )

    override val style: ElementStyle =
        ElementStyle(entity.color.toArgb(), Paint.Style.FILL)

    override fun structure(path: Path, matrix: Matrix, zoom: Float) {
        path.addCircle(
            entity.center.x,
            entity.center.y,
            entity.radius,
            Path.Direction.CW
        )
    }
}
