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

    /*
    frameCount               min     0.0,   median     1.0,   max     3.0
    timeToInitialDisplayMs   min 3,323.2,   median 3,926.5,   max 4,548.7
    frameDurationCpuMs       P50    847.4,   P90  2,043.6,   P95  2,850.1,   P99  3,495.4
    frameOverrunMs           P50    831.1,   P90  2,049.5,   P95  2,872.7,   P99  3,531.3

    frameCount               min     1.0,   median     2.0,   max     3.0
    timeToInitialDisplayMs   min 2,451.3,   median 2,591.7,   max 3,604.0
    frameDurationCpuMs       P50    214.2,   P90  1,839.1,   P95  2,222.3,   P99  2,528.9
    frameOverrunMs           P50    237.4,   P90  1,825.4,   P95  2,209.5,   P99  2,516.8

    frameCount               min     3.0,   median     3.0,   max     4.0
    timeToInitialDisplayMs   min 1,541.4,   median 1,621.9,   max 3,409.8
    frameDurationCpuMs       P50    130.0,   P90  1,054.2,   P95  1,126.4,   P99  1,251.2
    frameOverrunMs           P50    854.9,   P90  1,073.4,   P95  1,112.8,   P99  1,235.3

    frameCount               min     1.0,   median     4.0,   max     6.0
    timeToInitialDisplayMs   min 1,083.8,   median 1,129.4,   max 1,541.9
    frameDurationCpuMs       P50    235.5,   P90    750.7,   P95    835.4,   P99    980.1
    frameOverrunMs           P50    219.3,   P90    903.9,   P95  1,069.1,   P99  1,290.7
     */
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
