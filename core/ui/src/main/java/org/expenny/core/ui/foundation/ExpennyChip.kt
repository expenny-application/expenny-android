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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyChip(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        ChipContent(
            modifier = modifier,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennySelectableChip(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    val selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
    val selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer

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
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Surface(
            modifier = modifier.height(height),
            shape = RoundedCornerShape(6.0.dp),
            color = containerColor,
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
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium
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

private val height = 32.dp
private val contentSpacing = 8.dp
private val iconSize = 16.dp
