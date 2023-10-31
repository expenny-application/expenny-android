package org.expenny.core.ui.foundation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp


@Composable
fun ExpennyCheckBoxGroup(
    modifier: Modifier = Modifier,
    label: String,
    error: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    isChecked: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val labelText = label.let { text ->
        if (isRequired) {
            buildAnnotatedString {
                append(text)
                withStyle(SpanStyle(MaterialTheme.colorScheme.error)) {
                    append(" *")
                }
            }
        } else {
            AnnotatedString(text)
        }
    }

    Column(
        modifier = modifier.clickable {
            onClick(!isChecked)
        }
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExpennyCheckBox(
                isChecked = isChecked,
                isEnabled = isEnabled,
                onClick = onClick
            )
            ExpennyText(
                text = labelText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = Int.MAX_VALUE,
            )
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