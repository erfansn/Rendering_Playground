package ir.erfansn.rendering_playground.benchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.simpleViewResourceName
import androidx.test.uiautomator.uiAutomator
import androidx.test.uiautomator.waitForStable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CanvasTransformationBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun transform() = benchmarkRule.measureRepeated(
        packageName = "ir.erfansn.rendering_playground",
        metrics = listOf(
            StartupTimingMetric(),
            FrameTimingMetric()
        ),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        startActivityAndWait()

        uiAutomator {
            onElement { simpleViewResourceName() == "canvas" }.apply {
                pinchOpen(0.7f)
                waitForStable()
                repeat(2) { swipe(Direction.RIGHT, 0.7f) }
                waitForStable()
            }
        }
    }
}
