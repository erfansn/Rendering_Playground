package ir.erfansn.rendering_playground.element

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import ir.erfansn.rendering_playground.entity.PolylineEntity
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sqrt

class PolylineElement(private val entity: PolylineEntity) : Element {

    override val bounds: RectF = run {
        val xMax = entity.vertices.maxOf { it.x }
        val yMax = entity.vertices.maxOf { it.y }
        val xMin = entity.vertices.minOf { it.x }
        val yMin = entity.vertices.minOf { it.y }
        RectF(
            xMin,
            yMin,
            xMax,
            yMax
        )
    }

    override val style: ElementStyle =
        ElementStyle(entity.color.toArgb(), Paint.Style.STROKE)

    private val diag = bounds.let { hypot(it.width(), it.height()) }

    private val threshold = 0.01f

    private val simplifiedStructures = buildMap {
        this[0.8f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.8f)
        this[0.4f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.4f)
        this[0.2f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.2f)
        this[0.1f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.1f)
        this[0.05f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.05f)
        this[0.025f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.025f)
        this[0.02f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.02f)
        this[0.0175f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.0175f)
        this[0.01625f] = simplifyVerticesIterative(entity.vertices, diag * threshold / 0.01625f)
    }

    override fun structure(path: Path, matrix: Matrix, zoom: Float) {
        val vertices = if (zoom < 1.0f) simplifiedStructures.minBy { abs(zoom - it.key) }.value else entity.vertices
        val firstVertex = vertices[0]
        path.moveTo(firstVertex.x, firstVertex.y)
        for ((x, y) in vertices.drop(1)) {
            path.lineTo(x, y)
        }
    }

    // Ramer-Douglas-Peucker algorithm
    private fun simplifyVerticesIterative(points: List<Offset>, tolerance: Float): List<Offset> {
        if (points.size <= 2) return points

        val keep = BooleanArray(points.size)
        keep[0] = true
        keep[points.lastIndex] = true

        val stack = mutableListOf<Pair<Int, Int>>()
        stack.add(0 to points.lastIndex)

        while (stack.isNotEmpty()) {
            val (startIdx, endIdx) = stack.removeAt(stack.lastIndex)

            // Find point with maximum perpendicular distance
            var maxDist = 0f
            var maxIdx = -1

            for (i in (startIdx + 1) until endIdx) {
                val dist = perpendicularDistance(points[i], points[startIdx], points[endIdx])
                if (dist > maxDist) {
                    maxDist = dist
                    maxIdx = i
                }
            }

            // If the max distance exceeds tolerance, keep that point and subdivide
            if (maxDist > tolerance) {
                keep[maxIdx] = true
                // Process both segments
                stack.add(startIdx to maxIdx)
                stack.add(maxIdx to endIdx)
            }
        }

        // Build result list from kept points
        val result = mutableListOf<Offset>()
        for (i in points.indices) {
            if (keep[i]) {
                result.add(points[i])
            }
        }

        return result
    }

    private fun perpendicularDistance(point: Offset, lineStart: Offset, lineEnd: Offset): Float {
        val dx = lineEnd.x - lineStart.x
        val dy = lineEnd.y - lineStart.y
        val lengthSquared = dx * dx + dy * dy

        // If line segment is actually a point
        if (lengthSquared == 0f) {
            val px = point.x - lineStart.x
            val py = point.y - lineStart.y
            return sqrt(px * px + py * py)
        }

        // Calculate perpendicular distance
        val numerator = abs(dy * point.x - dx * point.y + lineEnd.x * lineStart.y - lineEnd.y * lineStart.x)
        return numerator / sqrt(lengthSquared)
    }
}
