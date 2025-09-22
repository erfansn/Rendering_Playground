package ir.erfansn.rendering_playground

import android.graphics.PointF
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.ObjectList
import androidx.collection.mutableObjectListOf
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.util.fastForEach
import ir.erfansn.rendering_playground.ui.theme.RenderingPlaygroundTheme

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
                Canvas(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
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
                        }
                        .clipToBounds()
                        .semantics {
                            testTagsAsResourceId = true
                            testTag = "canvas"
                        }
                ) {
                    redrawSignal

                    SampleElements.fastForEach { element ->
                        element.render(drawContext.canvas.nativeCanvas, path, paint, matrix)
                    }
                }
            }
        }
    }
}

