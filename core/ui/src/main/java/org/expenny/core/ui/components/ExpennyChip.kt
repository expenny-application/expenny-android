package org.expenny.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.base.BooleanPreviewParameterProvider
import org.expenny.core.ui.base.ExpennyLoremIpsum
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.extensions.noRippleClickable
import org.expenny.core.ui.foundation.ExpennyThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isSelected: Boolean,
    isEnabled: Boolean = true,
    count: Int? = null,
    content: @Composable ExpennyChipScope.() -> Unit,
    leadingContent: @Composable (ExpennyChipScope.() -> Unit)? = null,
    trailingContent: @Composable (ExpennyChipScope.() -> Unit)? = null,
) {
    val scope = remember(count) { ExpennyChipScope(count) }
    val containerColor by animateContainerColorAsState(isSelected)
    val contentColor by animateContentColorAsState(isSelected, isEnabled)
    val horizontalPadding by rememberUpdatedState(
        if (leadingContent == null && trailingContent == null) 8.dp else 12.dp
    )

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Surface(
            modifier = modifier.height(32.dp),
            border = null,
            selected = isSelected,
            enabled = isEnabled,
            shape = MaterialTheme.shapes.small,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            color = containerColor,
            contentColor = contentColor,
            onClick = onClick
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
                leadingContent?.invoke(scope)
                content(scope)
                trailingContent?.invoke(scope)
            }
        }
    }
}

@Composable
fun ExpennyChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    content: @Composable ExpennyChipScope.() -> Unit,
    leadingIcon: @Composable (ExpennyChipScope.() -> Unit)? = null,
    trailingIcon: @Composable (ExpennyChipScope.() -> Unit)? = null,
) {
    ExpennyChip(
        modifier = modifier,
        isSelected = false,
        isEnabled = isEnabled,
        onClick = onClick,
        content = content,
        leadingContent = leadingIcon,
        trailingContent = trailingIcon
    )
}

@Stable
class ExpennyChipScope(private val labelCount: Int?) {

    @Composable
    fun ChipText(
        modifier: Modifier = Modifier,
        text: String
    ) {
        Text(
            modifier = modifier,
            text = if (labelCount != null && labelCount > 0) "$labelCount • $text" else text,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    @Composable
    fun ChipIcon(
        modifier: Modifier = Modifier,
        painter: Painter,
        onClick: (() -> Unit)? = null
    ) {
        Icon(
            modifier = modifier
                .size(16.dp)
                .then(
                    if (onClick != null) Modifier.noRippleClickable { onClick() }
                    else Modifier
                ),
            painter = painter,
            contentDescription = stringResource(R.string.icon_a11y)
        )
    }
}

@Composable
private fun animateContainerColorAsState(isSelected: Boolean): State<Color> {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer

    return animateColorAsState(
        targetValue = containerColor,
        label = "AnimateContainerColor"
    )
}


@Composable
private fun animateContentColorAsState(isSelected: Boolean, isEnabled: Boolean): State<Color> {
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant

    return animateColorAsState(
        targetValue = contentColor.copy(if (isEnabled) 1f else 0.38f),
        label = "AnimateContentColor"
    )
}

@ExpennyPreview
@Composable
private fun ExpennyChipPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isEnabled: Boolean
) {
    ExpennyThemePreview {
        ExpennyChip(
            isSelected = false,
            isEnabled = isEnabled,
            onClick = {},
            content = {
                ChipText(text = ExpennyLoremIpsum(2).text)
            },
            leadingContent = {
                ChipIcon(painter = painterResource(R.drawable.ic_image_placeholder))
            },
            trailingContent = {
                ChipIcon(painter = painterResource(R.drawable.ic_close))
            }
        )
        ExpennyChip(
            isSelected = true,
            isEnabled = isEnabled,
            onClick = {},
            content = {
                ChipText(text = ExpennyLoremIpsum(2).text)
            },
            leadingContent = {
                ChipIcon(painter = painterResource(R.drawable.ic_image_placeholder))
            },
            trailingContent = {
                ChipIcon(painter = painterResource(R.drawable.ic_close))
            }
        )
    }
}
