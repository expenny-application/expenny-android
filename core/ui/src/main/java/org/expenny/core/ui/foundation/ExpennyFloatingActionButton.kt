package org.expenny.core.ui.foundation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ExpennyFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    text: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.onPrimary

    val hasLabel = text != null
    val minWidth = if (hasLabel) 80.dp else 56.dp
    val minHeight = if (hasLabel) 48.dp else 56.dp
    val iconSize = if (hasLabel) 18.dp else 24.dp

    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        shape = MaterialTheme.shapes.small,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = 3.dp,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                Row(
                    modifier = Modifier
                        .sizeIn(minWidth = minWidth, minHeight = minHeight)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.size(iconSize)) {
                        icon()
                    }
                    if (text != null) {
                        text()
                    }
                }
            }
        }
    }
}

@Composable
fun ExpennyFloatingActionButton(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.onPrimary

    val minWidth by animateDpAsState(if (isExpanded) 80.dp else 56.dp, label = "MinWidthAnimation")
    val minHeight by animateDpAsState(if (isExpanded) 48.dp else 56.dp, label = "MinHeightAnimation")
    val iconSize by animateDpAsState(if (isExpanded) 18.dp else 24.dp, label = "IconSizeAnimation")

    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        shape = MaterialTheme.shapes.small,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = 3.dp,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                Row(
                    modifier = Modifier
                        .sizeIn(minWidth = minWidth, minHeight = minHeight)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
                ) {
                    Box(modifier = Modifier.size(iconSize)) {
                        icon()
                    }
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = extendedFabExpandAnimation,
                        exit = extendedFabCollapseAnimation,
                    ) {
                        Row(Modifier.clearAndSetSemantics {}) {
                            Spacer(Modifier.width(8.dp))
                            text()
                        }
                    }
                }
            }
        }
    }
}

private val fadeDuration = 100
private val shrinkDuration = 200
private val delayDuration = 100
private val easingLinearCubicBezier = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)
private val easingEmphasizedCubicBezier = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

private val extendedFabCollapseAnimation = fadeOut(
    animationSpec = tween(
        durationMillis = fadeDuration,
        easing = easingLinearCubicBezier,
    )
) + shrinkHorizontally(
    animationSpec = tween(
        durationMillis = shrinkDuration,
        easing = easingEmphasizedCubicBezier,
    ),
    shrinkTowards = Alignment.Start,
)

private val extendedFabExpandAnimation = fadeIn(
    animationSpec = tween(
        durationMillis = fadeDuration,
        delayMillis = delayDuration,
        easing = easingLinearCubicBezier,
    ),
) + expandHorizontally(
    animationSpec = tween(
        durationMillis = shrinkDuration,
        easing = easingEmphasizedCubicBezier,
    ),
    expandFrom = Alignment.Start,
)
