package org.expenny.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.expenny.core.common.extensions.sumByDecimal
import org.expenny.core.ui.data.ui.ChartEntryUi

@Composable
fun ExpennyDonutChart(
    modifier: Modifier,
    entries: ImmutableList<ChartEntryUi>,
    progress: Float,
    selectedEntryIndex: Int,
    onSliceSelect: (index: Int) -> Unit,
    onSelectionRelease: () -> Unit,
) {
    val chartEntries by rememberUpdatedState(entries)
    val selectedSliceIndex by rememberUpdatedState(selectedEntryIndex)
    val slicesSweepAngles by rememberUpdatedState(entries.toSlicesSweepAngles(progress))
    val sweepAngleProgress by rememberUpdatedState(slicesSweepAngles.toSweepAngleProgress())

    val selectedSliceStroke = with(LocalDensity.current) { Stroke(24.dp.toPx()) }
    val sliceStroke = with(LocalDensity.current) { Stroke(16.dp.toPx()) }
    val unknownEntryColor = MaterialTheme.colorScheme.onSurfaceVariant
    val backgroundColor = MaterialTheme.colorScheme.surface

    BoxWithConstraints(
        modifier = modifier.aspectRatio(1f)
    ) {
        val sideSize = Integer.min(constraints.maxWidth, constraints.maxHeight)

        Canvas(
            modifier = Modifier
                .width(sideSize.dp)
                .height(sideSize.dp)
                .pointerInput(true) {
                    detectTapGestures {
                        val clickedAngle = convertTouchEventPointToAngle(
                            sideSize.toFloat(),
                            sideSize.toFloat(),
                            it.x,
                            it.y
                        )
                        sweepAngleProgress.forEachIndexed { index, item ->
                            if (clickedAngle <= item) {
                                if (selectedSliceIndex != index) {
                                    onSliceSelect(index)
                                } else {
                                    onSelectionRelease()
                                }
                                return@detectTapGestures
                            }
                        }
                    }
                }
        ) {
            drawIntoCanvas {
                var startAngle = -90f
                val innerArcRadius = (size.minDimension - sliceStroke.width) / 2
                val arcSize = Size(innerArcRadius * 2, innerArcRadius * 2)
                val topLeft = Offset(
                    (size / 2.0f).width - innerArcRadius,
                    (size / 2.0f).height - innerArcRadius
                )

                drawArc(
                    style = sliceStroke,
                    size = arcSize,
                    color = backgroundColor,
                    startAngle = startAngle,
                    sweepAngle = 360.0f,
                    topLeft = topLeft,
                    useCenter = false
                )

                slicesSweepAngles.forEachIndexed { index, arcAngle ->
                    drawArc(
                        style = if (index == selectedSliceIndex) selectedSliceStroke else sliceStroke,
                        size = arcSize,
                        color = chartEntries[index].color ?: unknownEntryColor,
                        startAngle = startAngle + 4f / 2,
                        sweepAngle = arcAngle - 4f,
                        topLeft = topLeft,
                        useCenter = false
                    )

                    startAngle += arcAngle
                }
            }
        }
    }
}

private fun List<Float>.toSweepAngleProgress(): List<Float> {
    return this.runningReduce { sum, angle -> sum + angle }
}

private fun List<ChartEntryUi>.toSlicesSweepAngles(progress: Float): List<Float> {
    val allEntriesTotalValue = this.sumByDecimal { it.value }
    return this.map { 360 * (it.value.toFloat() * progress) / allEntriesTotalValue.toFloat() }
}

private fun convertTouchEventPointToAngle(
    width: Float,
    height: Float,
    xPos: Float,
    yPos: Float
): Double {
    val x = xPos - (width * 0.5f)
    val y = yPos - (height * 0.5f)

    var angle = Math.toDegrees(kotlin.math.atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
    angle = if (angle < 0) angle + 360 else angle
    return angle
}
