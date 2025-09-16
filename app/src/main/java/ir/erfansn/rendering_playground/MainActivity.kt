package ir.erfansn.rendering_playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.util.fastForEach
import androidx.core.util.Pools
import ir.erfansn.rendering_playground.ui.theme.RenderingPlaygroundTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RenderingPlaygroundTheme {
                var zoom by remember { mutableFloatStateOf(1f) }
                var offset by remember { mutableStateOf(Offset.Zero) }
                var matrix by remember { mutableStateOf(android.graphics.Matrix()) }
                Canvas(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .pointerInput(Unit) {
                            detectTransformGestures { centroid, pan, gestureZoom, _ ->
                                val oldScale = zoom
                                val newScale = zoom * gestureZoom

                                offset = (offset + centroid / oldScale) -
                                        (centroid / newScale + pan / oldScale)
                                zoom = newScale

                                matrix = android.graphics.Matrix().apply {
                                    postTranslate(-offset.x, -offset.y)
                                    postScale(zoom, zoom, 0f, 0f)
                                }
                            }
                        }
                        .clipToBounds()
                        .semantics {
                            testTagsAsResourceId = true
                            testTag = "canvas"
                        }
                ) {
                    SampleElements.fastForEach {
                        it.render(drawContext.canvas, matrix)
                    }
                }
            }
        }
    }
}

