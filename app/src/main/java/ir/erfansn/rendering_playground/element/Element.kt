package ir.erfansn.rendering_playground.element

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.core.util.Pools

interface Element {
    fun render(canvas: Canvas, matrix: android.graphics.Matrix)

    companion object {
        val paint = Paint()
    }
}