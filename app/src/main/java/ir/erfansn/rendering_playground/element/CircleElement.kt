package ir.erfansn.rendering_playground.element

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.toArgb
import ir.erfansn.rendering_playground.entity.CircleEntity

class CircleElement(private val entity: CircleEntity) : Element {

    override val style: ElementStyle =
        ElementStyle(entity.color.toArgb(), Paint.Style.FILL)

    override fun structure(path: Path, matrix: Matrix) {
        path.addCircle(
            entity.center.x,
            entity.center.y,
            entity.radius,
            Path.Direction.CW
        )
    }

}
