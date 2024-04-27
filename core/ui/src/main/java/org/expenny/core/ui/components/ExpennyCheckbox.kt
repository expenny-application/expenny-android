package org.expenny.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyCheckbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    isEnabled: Boolean = true,
    onClick: (Boolean) -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Checkbox(
            modifier = modifier,
            checked = isChecked,
            enabled = isEnabled,
            onCheckedChange = {
                onClick(it)
            }
        )
    }
}

@Composable
fun ExpennyCheckboxInput(
    modifier: Modifier = Modifier,
    error: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    isChecked: Boolean,
    onClick: (Boolean) -> Unit,
    caption: @Composable ExpennyCheckboxInputScope.() -> Unit
) {
    val scope = remember(isRequired) { ExpennyCheckboxInputScope(isRequired) }

    Column(
        modifier = modifier.clickable {
            onClick(!isChecked)
        }
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExpennyCheckbox(
                isChecked = isChecked,
                isEnabled = isEnabled,
                onClick = onClick
            )
            caption(scope)
        }
        error?.let {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Stable
class ExpennyCheckboxInputScope(private val isRequired: Boolean) {

    @Composable
    fun Caption(
        modifier: Modifier = Modifier,
        text: String,
    ) {
        val caption = text.let {
            if (isRequired) {
                buildAnnotatedString {
                    append(it)
                    withStyle(SpanStyle(MaterialTheme.colorScheme.error)) {
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}