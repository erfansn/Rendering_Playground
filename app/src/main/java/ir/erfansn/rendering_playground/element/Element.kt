package ir.erfansn.rendering_playground.element

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.graphics.PathMeasure
import androidx.core.graphics.transform
import androidx.core.graphics.withMatrix

interface Element {
    val style: ElementStyle
    fun structure(path: Path, matrix: Matrix)
    @SuppressLint("NewApi")
    fun render(canvas: Canvas, path: Path, paint: Paint, matrix: Matrix) {
        val path = path.apply {
            rewind()
            structure(this, matrix)
        }
        val paint = paint.apply {
            this.color = this@Element.style.color
            this.style = this@Element.style.paintingStyle
        }

        canvas.withMatrix(matrix) {
            if (!canvas.quickReject(path)) {
                canvas.drawPath(path, paint)
            }
        }
    }
}

data class ElementStyle(val color: Int, val paintingStyle: Paint.Style)
