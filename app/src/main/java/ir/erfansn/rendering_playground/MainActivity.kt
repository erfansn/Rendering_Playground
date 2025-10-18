package ir.erfansn.rendering_playground

import android.graphics.Color.BLACK
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.RectF.intersects
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.util.fastForEach
import androidx.core.graphics.withMatrix
import ir.erfansn.rendering_playground.element.ElementStyle
import ir.erfansn.rendering_playground.element.InsertElement
import ir.erfansn.rendering_playground.element.PointElement
import ir.erfansn.rendering_playground.ui.theme.RenderingPlaygroundTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RenderingPlaygroundTheme {
                val matrix = remember { android.graphics.Matrix() }
                var redrawSignal by remember { mutableIntStateOf(0) }
                val path = remember { android.graphics.Path() }
                val path2 = remember { android.graphics.Path() }
                val paint = remember { android.graphics.Paint() }

                Box(
                    Modifier.fillMaxSize().semantics {
                        testTagsAsResourceId = true
                        testTag = "canvas"
                    }
                ) {
                    AndroidExternalSurface(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                var zoom = 1f
                                val offset = PointF(0f, 0f)
                                detectTransformGestures { centroid, pan, gestureZoom, _ ->
                                    val oldScale = zoom
                                    val newScale = zoom * gestureZoom

                                    offset.x = (offset.x + centroid.x / oldScale) -
                                            (centroid.x / newScale + pan.x / oldScale)
                                    offset.y = (offset.y + centroid.y / oldScale) -
                                            (centroid.y / newScale + pan.y / oldScale)
                                    zoom = newScale

                                    matrix.apply {
                                        setTranslate(-offset.x, -offset.y)
                                        postScale(zoom, zoom, 0f, 0f)
                                    }
                                    redrawSignal++
                                }
                            },
                    ) {
                        onSurface { surface, width, height ->
                            var viewBounds = RectF(
                                0.0f, 0.0f,
                                width.toFloat(), height.toFloat()
                            )

                            surface.onChanged { width, height ->
                                viewBounds = RectF(
                                    0.0f, 0.0f,
                                    width.toFloat(), height.toFloat()
                                )
                            }

                            surface.lockHardwareCanvas().apply {
                                drawColor(BLACK)
                                surface.unlockCanvasAndPost(this)
                            }

                            var lastElementStyle: ElementStyle =
                                SampleElements.first { it !is InsertElement }.style
                            val elementBounds = RectF()
                            snapshotFlow { redrawSignal }.collectLatest {
                                surface.lockHardwareCanvas().apply {
                                    drawColor(BLACK)
                                    withMatrix(matrix) {
                                        SampleElements.fastForEach { element ->
                                            elementBounds.set(element.bounds)
                                            matrix.mapRect(elementBounds)

                                            if (
                                                element is PointElement || ((elementBounds.width() > 4 || elementBounds.height() > 4) &&
                                                        intersects(viewBounds, elementBounds))
                                            ) {
                                                if (element is PointElement || element is InsertElement) {
                                                    element.render(
                                                        this,
                                                        path2,
                                                        paint,
                                                        matrix
                                                    )
                                                } else if (element.style == lastElementStyle) {
                                                    element.structure(path, matrix)
                                                } else {
                                                    val paint = paint.apply {
                                                        this.color =
                                                            lastElementStyle.color
                                                        this.style =
                                                            lastElementStyle.paintingStyle
                                                    }

                                                    if (!quickReject(path)) {
                                                        drawPath(path, paint)
                                                    }

                                                    path.rewind()
                                                    element.structure(path, matrix)
                                                    lastElementStyle = element.style
                                                }
                                            }
                                        }
                                    }
                                    surface.unlockCanvasAndPost(this)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

