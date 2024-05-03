package org.expenny.core.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpennyCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable BoxScope.() -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColorFor(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Box(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = MaterialTheme.shapes.medium,
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    border = null,
                    shadowElevation = 0.dp
                )
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    enabled = true,
                    onClick = onClick,
                    onLongClick = onLongClick
                ),
            propagateMinConstraints = true,
            content = content,
        )
    }
}

@Composable
fun ExpennyCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 0.dp,
    ) {
        Box(content = content)
    }
}

private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    shadowElevation: Dp
) = this
    .shadow(shadowElevation, shape, clip = false)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)
