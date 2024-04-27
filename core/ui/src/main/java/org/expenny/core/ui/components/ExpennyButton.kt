package org.expenny.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyPreviewTheme
import org.expenny.core.ui.base.BooleanPreviewParameterProvider
import org.expenny.core.ui.base.ExpennyPreview

@Composable
fun ExpennyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: (@Composable ExpennyButtonScope.() -> Unit)? = null,
    label: @Composable ExpennyButtonScope.() -> Unit,
) {
    val scope = remember { ExpennyButtonScope() }

    Button(
        modifier = modifier.sizeIn(
            minHeight = 56.dp,
            maxHeight = 56.dp,
            minWidth = 64.dp
        ),
        elevation = null,
        onClick = onClick,
        enabled = isEnabled,
        interactionSource = interactionSource,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(0.12f),
            disabledContentColor = MaterialTheme.colorScheme.primary.copy(0.38f),
        ),
        content = {
            icon?.invoke(scope)
            Spacer(Modifier.width(4.dp))
            label(scope)
        }
    )
}

@Stable
class ExpennyButtonScope {

    @Composable
    fun ButtonIcon(
        modifier: Modifier = Modifier,
        painter: Painter,
    ) {
        Icon(
            modifier = modifier.size(24.dp),
            painter = painter,
            contentDescription = stringResource(R.string.icon_a11y)
        )
    }

    @Composable
    fun ButtonLabel(
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

@ExpennyPreview
@Composable
private fun ButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isEnabled: Boolean
) {
    ExpennyPreviewTheme {
        ExpennyButton(
            onClick = {},
            isEnabled = isEnabled,
            label = {
                ButtonLabel(text = "Preview")
            }
        )
        ExpennyButton(
            onClick = {},
            isEnabled = isEnabled,
            label = {
                ButtonLabel(text = "Preview")
            },
            icon = {
                ButtonIcon(painter = painterResource(R.drawable.ic_info))
            }
        )
    }
}
