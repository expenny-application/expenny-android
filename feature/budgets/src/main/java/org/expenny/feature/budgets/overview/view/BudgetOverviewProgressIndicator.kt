package org.expenny.feature.budgets.overview.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.expenny.core.common.extensions.toMonetaryString
import org.expenny.core.resources.R
import java.math.BigDecimal

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun BudgetOverviewProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    lowerBound: BigDecimal,
    upperBound: BigDecimal,
    value: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var progressIndicatorWidth by remember { mutableStateOf(IntSize.Zero) }

        BoxWithConstraints(
            modifier = Modifier.onSizeChanged { progressIndicatorWidth = it },
            contentAlignment = Alignment.BottomCenter
        ) {
            ProgressIndicator(progress = progress)

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.remaining_funds_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row(
            modifier = Modifier.then(
                with(LocalDensity.current) {
                    Modifier.width(progressIndicatorWidth.width.toDp() + 44.dp)
                }
            ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = lowerBound.toMonetaryString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = upperBound.toMonetaryString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
) {
    val trackColor = MaterialTheme.colorScheme.surfaceContainer
    val fillColor = MaterialTheme.colorScheme.primary
    val progressValue = when {
        progress < 0f -> 0f
        progress > 1f -> 1f
        else -> progress
    }
    val transitionProgress = remember(progress) {
        Animatable(initialValue = 0f)
    }

    LaunchedEffect(transitionProgress) {
        transitionProgress.animateTo(
            targetValue = progressValue,
            animationSpec = TweenSpec(durationMillis = 700)
        )
    }

    Canvas(
        modifier = modifier
            .size(
                width = 250.dp,
                height = 140.dp
            )
            .clipToBounds()
            .padding(8.dp)
    ) {
        drawArc(
            color = trackColor,
            startAngle = -180f,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round),
            size = Size(size.width, size.height * 2),
        )

        drawArc(
            color = fillColor,
            startAngle = -180f,
            sweepAngle = transitionProgress.value * 180f,
            useCenter = false,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round),
            size = Size(size.width, size.height * 2),
        )
    }
}