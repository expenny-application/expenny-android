package org.expenny.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.commandiron.wheel_picker_compose.WheelDatePicker
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyAlertDialogButton
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyButtonSize
import org.expenny.core.ui.foundation.ExpennyButtonStyle
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.theme.ExpennyTheme
import java.time.LocalDate


@Composable
fun ExpennyDatePicker(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.select_date_label),
    currentDate: LocalDate?,
    yearsRange: IntRange? = IntRange(1970, LocalDate.now().year),
    minDate: LocalDate = LocalDate.MIN,
    maxDate: LocalDate = LocalDate.MAX,
    onSelect: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    var date by rememberSaveable { mutableStateOf(currentDate ?: LocalDate.now()) }

    ExpennyAlertDialog(
        modifier = modifier.wrapContentWidth(),
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        content = {
            WheelDatePicker(
                startDate = date,
                minDate = minDate,
                maxDate = maxDate,
                yearsRange = yearsRange,
                textColor = MaterialTheme.colorScheme.onSurface,
                textStyle = MaterialTheme.typography.bodyLarge,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    border = null,
                ),
                onSnappedDate = {
                    date = it
                }
            )
        },
        confirmButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.select_button),
                onClick = {
                    onSelect(date)
                    onDismiss()
                }
            )
        },
        dismissButton = {
            ExpennyAlertDialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss,
            )
        }
    )
}

@Preview
@Composable
private fun ExpennyDatePickerPreview() {
    ExpennyTheme {
        ExpennyDatePicker(
            currentDate = LocalDate.now(),
            onSelect = {},
            onDismiss = {}
        )
    }
}