package ir.erfansn.rendering_playground

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.ObjectList
import androidx.collection.mutableObjectListOf
import androidx.compose.foundation.AndroidEmbeddedExternalSurface
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.util.fastForEach
import androidx.core.graphics.withMatrix
import androidx.core.os.ExecutorCompat
import ir.erfansn.rendering_playground.element.PointElement
import ir.erfansn.rendering_playground.ui.theme.RenderingPlaygroundTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.math.sin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RenderingPlaygroundTheme {
                val matrix = remember { android.graphics.Matrix() }
                var redrawSignal by remember { mutableIntStateOf(0) }
                val path = remember { android.graphics.Path() }
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
                                drawColor(Color.Black.toArgb())
                                surface.unlockCanvasAndPost(this)
                            }

                            val elementBounds = RectF()
                            snapshotFlow { redrawSignal }.collectLatest {
                                surface.lockHardwareCanvas().apply {
                                    drawColor(Color.Black.toArgb())
                                    withMatrix(matrix) {
                                        SampleElements.fastForEach { element ->
                                            elementBounds.set(element.bounds)
                                            matrix.mapRect(elementBounds)
                                            if (element is PointElement || ((elementBounds.width() > 4 || elementBounds.height() > 4) && RectF.intersects(viewBounds, elementBounds))) {
                                                element.render(this, path, paint, matrix)
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

