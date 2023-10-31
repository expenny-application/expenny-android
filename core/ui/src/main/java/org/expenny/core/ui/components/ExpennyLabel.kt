package org.expenny.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyLabel(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        LabelContent(
            modifier = modifier,
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
private fun LabelContent(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Box(
            modifier = modifier
                .heightIn(min = minHeight)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(containerColor)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = contentSpacing,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides MaterialTheme.typography.labelMedium
                ) {
                    if (leadingIcon != null) {
                        Box(modifier = Modifier.size(iconSize)) {
                            leadingIcon()
                        }
                    }
                    label()
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

private val minHeight = 28.dp
private val contentSpacing = 6.dp
private val iconSize = 16.dp

const val ExpennyLabelColorAlpha = 0.2f
val ExpennyLabelHeight = 24.dp
