package org.expenny.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
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
import org.expenny.core.ui.base.BooleanPreviewParameterProvider
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.foundation.ExpennyThemePreview
import org.expenny.core.ui.foundation.primaryContrast

@Composable
fun ExpennyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    leadingContent: (@Composable ExpennyButtonScope.() -> Unit)? = null,
    content: @Composable ExpennyButtonScope.() -> Unit,
) {
    val scope = remember { ExpennyButtonScope() }

    Button(
        modifier = modifier.sizeIn(
            minHeight = 56.dp,
            maxHeight = 56.dp
        ),
        elevation = null,
        onClick = onClick,
        enabled = isEnabled,
        interactionSource = remember { MutableInteractionSource() },
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContrast,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContrast.copy(0.12f),
            disabledContentColor = MaterialTheme.colorScheme.primaryContrast.copy(0.38f),
        ),
        contentPadding = PaddingValues(
            start = if (leadingContent == null) 24.dp else 16.dp,
            end = 24.dp,
            top = 8.dp,
            bottom = 8.dp
        ),
        content = {
            if (leadingContent != null) {
                leadingContent(scope)
                Spacer(Modifier.width(8.dp))
            }
            content(scope)
        }
    )
}

@Stable
class ExpennyButtonScope {

    @Composable
    fun ButtonText(
        modifier: Modifier = Modifier,
        text: String
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }

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
}

@ExpennyPreview
@Composable
private fun ExpennyButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isEnabled: Boolean
) {
    ExpennyThemePreview {
        ExpennyButton(
            onClick = {},
            isEnabled = isEnabled,
            content = {
                ButtonText(text = "Preview")
            }
        )
        ExpennyButton(
            onClick = {},
            isEnabled = isEnabled,
            content = {
                ButtonText(text = "Preview")
            },
            leadingContent = {
                ButtonIcon(painter = painterResource(R.drawable.ic_image_placeholder))
            }
        )
    }
}
