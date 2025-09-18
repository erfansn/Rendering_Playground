package ir.erfansn.rendering_playground.element

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint

interface Element {
    fun render(canvas: Canvas, matrix: android.graphics.Matrix)

    companion object {
        val paint = Paint()
    }
}