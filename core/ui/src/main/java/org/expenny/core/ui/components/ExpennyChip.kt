package org.expenny.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyChip(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    label: @Composable ExpennyChipScope.() -> Unit,
    leadingIcon: @Composable (ExpennyChipScope.() -> Unit)? = null,
    trailingIcon: @Composable (ExpennyChipScope.() -> Unit)? = null,
) {
    val scope = remember { ExpennyChipScope() }
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
    val horizontalPadding by rememberUpdatedState(
        if (leadingIcon == null && trailingIcon == null) 8.dp else 12.dp
    )

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Surface(
            modifier = modifier.height(32.dp),
            shape = MaterialTheme.shapes.small,
            color = chipContainerColor,
            onClick = onClick,
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = horizontalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                CompositionLocalProvider(LocalContentColor provides chipContentColor) {
                    leadingIcon?.invoke(scope)
                    label(scope)
                    trailingIcon?.invoke(scope)
                }
            }
        }
    }
}

@Composable
fun ExpennyChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable ExpennyChipScope.() -> Unit,
    leadingIcon: @Composable (ExpennyChipScope.() -> Unit)? = null,
    trailingIcon: @Composable (ExpennyChipScope.() -> Unit)? = null,
) {
    ExpennyChip(
        modifier = modifier,
        isSelected = false,
        onClick = onClick,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

@Composable
fun ExpennyChip(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    count: Int,
    onClick: () -> Unit,
    label: @Composable ExpennyChipScope.() -> Unit,
    trailingIcon: @Composable (ExpennyChipScope.() -> Unit)? = null,
) {
    val leadingIcon: @Composable (ExpennyChipScope.() -> Unit)? =
        count.takeIf { it > 0 }?.let {
            {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(text = count.toString())
                }
            }
        }

    ExpennyChip(
        modifier = modifier,
        isSelected = isSelected,
        onClick = onClick,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

@Stable
class ExpennyChipScope {

    @Composable
    fun ChipLabel(
        modifier: Modifier = Modifier,
        text: String
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    @Composable
    fun ChipIcon(
        modifier: Modifier = Modifier,
        painter: Painter,
    ) {
        Icon(
            modifier = modifier.size(16.dp),
            painter = painter,
            contentDescription = stringResource(R.string.icon_a11y)
        )
    }
}
