package ir.erfansn.rendering_playground

import android.annotation.SuppressLint
import android.graphics.Color.BLACK
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
import androidx.compose.runtime.mutableFloatStateOf
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
import ir.erfansn.rendering_playground.element.PolylineElement
import ir.erfansn.rendering_playground.ui.theme.RenderingPlaygroundTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RenderingPlaygroundTheme {
                val matrix = remember { android.graphics.Matrix() }
                var redrawSignal by remember { mutableIntStateOf(0) }

                Box(
                    Modifier.fillMaxSize().semantics {
                        testTagsAsResourceId = true
                        testTag = "canvas"
                    }
                ) {
                    var zoom by remember { mutableFloatStateOf(1.0f) }
                    AndroidExternalSurface(
                        modifier = Modifier
                            .pointerInput(Unit) {
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
                            surface.lockHardwareCanvas().apply {
                                drawColor(BLACK)
                                surface.unlockCanvasAndPost(this)
                            }

                            val elementRenderer = ElementRenderer()
                            snapshotFlow { redrawSignal }.collectLatest {
                                surface.lockHardwareCanvas().apply {
                                    drawColor(BLACK)
                                    elementRenderer.render(matrix, this, zoom)
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
