package org.expenny.core.ui.foundation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ExpennyFloatingButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    icon: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier.semantics {
            role = Role.Button
        },
        enabled = isEnabled,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        interactionSource = interactionSource,
        shadowElevation = 3.dp,
        onClick = onClick,
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Row(
                Modifier
                    .defaultMinSize(
                        minWidth = 56.dp,
                        minHeight = 56.dp,
                    )
                    .padding(
                        PaddingValues(16.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                content = icon
            )
        }
    }
}

@Composable
fun ExpennyFloatingButton(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = true,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    val minWidth by animateDpAsState(
        targetValue = if (isExpanded) 56.dp.times(expandSizeMultiplier) else 56.dp,
        label = "MinWidthAnimation"
    )
    val minHeight by animateDpAsState(
        targetValue = if (isExpanded) 56.dp.times(expandSizeMultiplier) else 56.dp,
        label = "MinHeightAnimation"
    )
    val iconSize by animateDpAsState(
        targetValue = if (isExpanded) 24.dp.times(expandSizeMultiplier) else 24.dp,
        label = "IconSizeAnimation"
    )

    Surface(
        modifier = modifier.semantics {
            role = Role.Button
        },
        enabled = isEnabled,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        interactionSource = interactionSource,
        shadowElevation = 3.dp,
        onClick = onClick,
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Row(
                Modifier
                    .defaultMinSize(
                        minWidth = minWidth,
                        minHeight = minHeight,
                    )
                    .padding(
                        PaddingValues(16.dp, 12.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center,
                content = {
                    Box(modifier = Modifier.size(iconSize)) {
                        icon()
                    }
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandAnimation,
                        exit = collapseAnimation,
                    ) {
                        Row {
                            Spacer(modifier = Modifier.width(8.dp))
                            label()
                        }
                    }
                }
            )
        }
    }
}

private const val expandSizeMultiplier = 0.85f
private val easingLinearCubicBezier = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)
private val easingEmphasizedCubicBezier = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

private val expandAnimation = fadeIn(tween(100, 0, easingLinearCubicBezier)) +
        expandHorizontally(tween(200, 0, easingEmphasizedCubicBezier), Alignment.Start)

private val collapseAnimation = fadeOut(tween(100, 0, easingLinearCubicBezier)) +
        shrinkHorizontally(tween(200, 0, easingEmphasizedCubicBezier), Alignment.Start)