package org.expenny.core.ui.foundation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.theme.ExpennyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyChip(
    modifier: Modifier = Modifier,
    tonalElevation: Dp = 1.dp,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        ChipContent(
            modifier = modifier,
            tonalElevation = tonalElevation,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            containerColor = containerColor,
            contentColor = contentColor,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennySelectableChip(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    tonalElevation: Dp = 1.dp,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit
) {
    val chipContainerColor by animateColorAsState(
        targetValue = if (isSelected) selectedContainerColor else containerColor,
        label = "ContainerColor"
    )
    val chipContentColor by animateColorAsState(
        targetValue = if (isSelected) selectedContentColor else contentColor,
        label = "ContentColor"
    )

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        ChipContent(
            modifier = modifier,
            tonalElevation = tonalElevation,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            containerColor = chipContainerColor,
            contentColor = chipContentColor,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipContent(
    modifier: Modifier = Modifier,
    tonalElevation: Dp = 1.dp,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Surface(
            modifier = modifier.height(height),
            shape = MaterialTheme.shapes.small,
            color = containerColor,
            tonalElevation = tonalElevation,
            onClick = onClick,
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(
                        horizontal = if (label == null) 8.dp else 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = contentSpacing,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides MaterialTheme.typography.labelLarge
                ) {
                    if (leadingIcon != null) {
                        Box(modifier = Modifier.size(iconSize)) {
                            leadingIcon()
                        }
                    }
                    if (label != null) {
                        label()
                    }
                    if (trailingIcon != null) {
                        Box(modifier = Modifier.size(iconSize)) {
                            trailingIcon()
                        }
                    }
                }
            }
        }
    }
}

private fun paddingValues(
    hasLabel: Boolean = false,
    hasLeadingIcon: Boolean = false,
    hasTrailingIcon: Boolean = false
): PaddingValues {
    val (startPadding, endPadding) = if (!hasLabel) {
        Pair(8.dp, 8.dp)
    } else {
        Pair(
            if (hasLeadingIcon) 12.dp else 16.dp,
            if (hasTrailingIcon) 12.dp else 16.dp
        )
    }

    return PaddingValues(
        start = startPadding,
        end = endPadding
    )
}

private val height = 32.dp
private val contentSpacing = 8.dp
private val iconSize = 16.dp
