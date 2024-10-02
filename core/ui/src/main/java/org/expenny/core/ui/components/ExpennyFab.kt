package org.expenny.core.ui.components

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.base.BooleanPreviewParameterProvider
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.foundation.ExpennyThemePreview
import org.expenny.core.ui.foundation.primaryContrast


@Composable
fun ExpennyFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    content: @Composable ExpennyFabScope.() -> Unit
) {
    val scope = remember { ExpennyFabScope() }
    val contentColor by rememberUpdatedState(
        MaterialTheme.colorScheme.onPrimary.copy(if (isEnabled) 1f else 0.38f)
    )

    Surface(
        modifier = modifier.semantics {
            role = Role.Button
        },
        enabled = isEnabled,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContrast,
        contentColor = contentColor,
        interactionSource = remember { MutableInteractionSource() },
        shadowElevation = 3.dp,
        tonalElevation = 0.dp,
        onClick = onClick,
    ) {
        Row(
            Modifier
                .defaultMinSize(
                    minWidth = 56.dp,
                    minHeight = 56.dp,
                )
                .padding(PaddingValues(16.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            content = {
                content(scope)
            }
        )
    }
}

@Composable
fun ExpennyFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isExpanded: Boolean = true,
    isEnabled: Boolean = true,
    icon: @Composable ExpennyFabScope.() -> Unit,
    label: @Composable ExpennyFabScope.() -> Unit
) {
    val minWidth by animateDpAsState(
        targetValue = if (isExpanded) 56.dp.times(0.85f) else 56.dp,
        label = "AnimateMinWidth"
    )
    val minHeight by animateDpAsState(
        targetValue = if (isExpanded) 56.dp.times(0.85f) else 56.dp,
        label = "AnimateMinHeight"
    )
    val iconSize by animateDpAsState(
        targetValue = if (isExpanded) 24.dp.times(0.85f) else 24.dp,
        label = "AnimateIconSize"
    )
    val horizontalArrangement by rememberUpdatedState(
        if (isExpanded) Arrangement.Start else Arrangement.Center
    )
    val contentColor by rememberUpdatedState(
        MaterialTheme.colorScheme.onPrimary.copy(if (isEnabled) 1f else 0.38f)
    )

    val scope = remember(iconSize) { ExpennyFabScope(iconSize) }

    Surface(
        modifier = modifier.semantics {
            role = Role.Button
        },
        enabled = isEnabled,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContrast,
        contentColor = contentColor,
        interactionSource = remember { MutableInteractionSource() },
        shadowElevation = 3.dp,
        tonalElevation = 0.dp,
        onClick = onClick,
    ) {
        Row(
            Modifier
                .defaultMinSize(
                    minWidth = minWidth,
                    minHeight = minHeight,
                )
                .padding(PaddingValues(16.dp, 12.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement,
            content = {
                icon(scope)
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandAnimation,
                    exit = collapseAnimation,
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        label(scope)
                    }
                }
            }
        )
    }
}

@Stable
class ExpennyFabScope(private val iconSize: Dp = 24.dp) {

    @Composable
    fun FabIcon(
        modifier: Modifier = Modifier,
        painter: Painter,
    ) {
        Icon(
            modifier = modifier.size(iconSize),
            painter = painter,
            contentDescription = stringResource(R.string.icon_a11y)
        )
    }

    @Composable
    fun FabText(
        modifier: Modifier = Modifier,
        text: String
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

private val easingLinearCubicBezier = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)

private val easingEmphasizedCubicBezier = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

private val expandAnimation = fadeIn(tween(100, 0, easingLinearCubicBezier)) +
        expandHorizontally(tween(200, 0, easingEmphasizedCubicBezier), Alignment.Start)

private val collapseAnimation = fadeOut(tween(100, 0, easingLinearCubicBezier)) +
        shrinkHorizontally(tween(200, 0, easingEmphasizedCubicBezier), Alignment.Start)

@ExpennyPreview
@Composable
private fun ExpennyFabPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isEnabled: Boolean
) {
    ExpennyThemePreview {
        var isExpanded by remember { mutableStateOf(true) }

        ExpennyFab(
            onClick = { isExpanded = !isExpanded },
            isEnabled = isEnabled,
            isExpanded = isExpanded,
            icon = {
                FabIcon(painter = painterResource(R.drawable.ic_image_placeholder))
            },
            label = {
                FabText(text = "Preview")
            }
        )
    }
}