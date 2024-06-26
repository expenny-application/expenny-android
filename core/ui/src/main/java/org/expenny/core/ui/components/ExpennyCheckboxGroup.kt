package org.expenny.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.base.BooleanPreviewParameterProvider
import org.expenny.core.ui.base.ExpennyLoremIpsum
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.foundation.ExpennyThemePreview

@Composable
fun ExpennyCheckboxGroup(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    error: String? = null,
    onClick: (Boolean) -> Unit,
    caption: @Composable ExpennyCheckboxGroupScope.() -> Unit
) {
    val scope = remember(isRequired, isEnabled) {
        ExpennyCheckboxGroupScope(isRequired, isEnabled)
    }

    Column(
        modifier = modifier.clickable {
            onClick(!isChecked)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ExpennyCheckbox(
                isChecked = isChecked,
                isEnabled = isEnabled,
                onClick = onClick
            )
            caption(scope)
        }
        error?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Stable
class ExpennyCheckboxGroupScope(
    private val isRequired: Boolean,
    private val isEnabled: Boolean
) {

    @Composable
    fun CheckboxGroupCaption(
        modifier: Modifier = Modifier,
        text: String,
    ) {
        val caption = text.let {
            if (isRequired) {
                buildAnnotatedString {
                    append(it)
                    withStyle(
                        SpanStyle(
                            MaterialTheme.colorScheme.error.copy(if (isEnabled) 1f else 0.38f)
                        )
                    ) {
                        append(" *")
                    }
                }
            } else {
                AnnotatedString(it)
            }
        }

        Text(
            modifier = modifier,
            text = caption,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                alpha = if (isEnabled) 1f else 0.38f
            )
        )
    }
}

@ExpennyPreview
@Composable
private fun ExpennyCheckboxGroupPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isEnabled: Boolean
) {
    ExpennyThemePreview {
        ExpennyCheckboxGroup(
            isChecked = true,
            isEnabled = isEnabled,
            onClick = {},
            caption = {
                CheckboxGroupCaption(text = ExpennyLoremIpsum(10).text)
            }
        )
        ExpennyCheckboxGroup(
            isChecked = false,
            isRequired = true,
            isEnabled = isEnabled,
            onClick = {},
            caption = {
                CheckboxGroupCaption(text = ExpennyLoremIpsum(10).text)
            }
        )
    }
}
