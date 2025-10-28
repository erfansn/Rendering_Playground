package ir.erfansn.rendering_playground

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.RectF.intersects
import android.os.Build
import androidx.compose.ui.util.fastForEach
import androidx.core.graphics.withMatrix
import androidx.core.graphics.withTranslation
import ir.erfansn.rendering_playground.element.ElementStyle
import ir.erfansn.rendering_playground.element.InsertElement
import ir.erfansn.rendering_playground.element.PointElement

class ElementRenderer {

    private var lastElementStyle: ElementStyle =
        SampleElements.first { it !is InsertElement }.style

    private val viewBounds = RectF()
    private val elementBounds = RectF()

    private val path = android.graphics.Path()
    private val path2 = android.graphics.Path()
    private val paint = android.graphics.Paint()

    fun render(matrix: Matrix, canvas: Canvas, zoom: Float) {
        if (viewBounds.width().toInt() != canvas.width || viewBounds.height().toInt() != canvas.height) {
            viewBounds.set(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat())
        }
        canvas.withMatrix(matrix) {
            SampleElements.fastForEach { element ->
                elementBounds.set(element.bounds)
                matrix.mapRect(elementBounds)

                if (
                    element is PointElement || ((elementBounds.width() > 4 || elementBounds.height() > 4) &&
                            intersects(viewBounds, elementBounds))
                ) {
                    if (element is PointElement || element is InsertElement) {
                        if (element is PointElement) {
                            canvas.restore()
                            val position = floatArrayOf(element.position.x, element.position.y)
                            matrix.mapPoints(position)
                            canvas.withTranslation(position[0] - element.position.x, position[1] - element.position.y) {
                                element.render(
                                    this,
                                    path2,
                                    paint,
                                    matrix,
                                    zoom
                                )
                            }
                            canvas.save()
                            canvas.concat(matrix)
                        } else {
                            element.render(
                                this,
                                path2,
                                paint,
                                matrix,
                                zoom
                            )
                        }
                    } else if (element.style == lastElementStyle) {
                        element.structure(path, matrix, zoom)
                    } else {
                        val paint = paint.apply {
                            this.color =
                                lastElementStyle.color
                            this.style =
                                lastElementStyle.paintingStyle
                        }

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || !quickReject(path)) {
                            drawPath(path, paint)
                        }

                        path.rewind()
                        element.structure(path, matrix, zoom)
                        lastElementStyle = element.style
                    }
                }
            }
        }
    }
}