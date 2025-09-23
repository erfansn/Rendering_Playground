package ir.erfansn.rendering_playground.benchmark

import androidx.benchmark.macro.CompilationMode
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

    frameCount               min     1.0,   median     1.0,   max     1.0
    timeToInitialDisplayMs   min 1,467.9,   median 1,814.9,   max 2,193.5
    frameDurationCpuMs       P50    528.1,   P90  1,011.3,   P95  1,019.1,   P99  1,025.4
    frameOverrunMs           P50    531.1,   P90  1,027.9,   P95  1,034.5,   P99  1,039.7
    Traces: Iteration 0 1 2 3 4

    frameCount               min     1.0,   median     2.0,   max     5.0
    timeToInitialDisplayMs   min   947.3,   median   982.6,   max 1,308.8
    frameDurationCpuMs       P50    373.7,   P90    751.9,   P95    774.0,   P99    781.8
    frameOverrunMs           P50    405.4,   P90    742.0,   P95    763.6,   P99    777.3

    frameCount               min   1.0,   median   1.0,   max   1.0
    timeToInitialDisplayMs   min 427.7,   median 574.0,   max 725.8
    frameDurationCpuMs       P50  164.2,   P90  222.8,   P95  237.8,   P99  249.9
    frameOverrunMs           P50  301.4,   P90  328.7,   P95  330.5,   P99  332.0

    frameCount               min   1.0,   median   1.0,   max   1.0
    timeToInitialDisplayMs   min 582.7,   median 601.4,   max 797.9
    frameDurationCpuMs       P50  122.5,   P90  144.4,   P95  146.1,   P99  147.4
    frameOverrunMs           P50  124.7,   P90  184.5,   P95  191.5,   P99  197.1
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
