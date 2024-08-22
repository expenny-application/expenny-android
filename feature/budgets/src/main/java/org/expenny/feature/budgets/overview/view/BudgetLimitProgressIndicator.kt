package org.expenny.feature.budgets.overview.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun BudgetLimitProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color,
    progress: Float,
    value: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            ProgressIndicator(
                color = color,
                progress = progress
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color,
    progress: Float,
) {
    val trackColor = MaterialTheme.colorScheme.surface
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
            .size(44.dp)
            .clipToBounds()
            .padding(4.dp)
    ) {
        drawArc(
            color = trackColor,
            startAngle = 270f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round),
            size = Size(size.width, size.height),
        )

        drawArc(
            color = color,
            startAngle = 270f,
            sweepAngle = transitionProgress.value * 360f,
            useCenter = false,
            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round),
            size = Size(size.width, size.height),
        )
    }
}
