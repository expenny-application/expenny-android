package org.expenny.core.ui.components

import androidx.compose.foundation.layout.wrapContentWidth
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
import org.expenny.core.ui.foundation.ExpennyThemePreview
import java.time.LocalDate


@Composable
fun ExpennyDatePicker(
    modifier: Modifier = Modifier,
    currentDate: LocalDate?,
    yearsRange: IntRange? = IntRange(1970, LocalDate.now().year),
    minDate: LocalDate = LocalDate.MIN,
    maxDate: LocalDate = LocalDate.MAX,
    onSelect: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    var localDate by rememberSaveable { mutableStateOf(currentDate ?: LocalDate.now()) }

    ExpennyDialog(
        modifier = modifier.wrapContentWidth(),
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.select_date_label))
        },
        body = {
            WheelDatePicker(
                startDate = localDate,
                minDate = minDate,
                maxDate = maxDate,
                yearsRange = yearsRange,
                textColor = MaterialTheme.colorScheme.onSurface,
                textStyle = MaterialTheme.typography.bodyLarge,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = null,
                ),
                onSnappedDate = {
                    localDate = it
                }
            )
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.select_button),
                onClick = {
                    onSelect(localDate)
                    onDismiss()
                }
            )
        },
        leftButton = {
            DialogButton(
                label = stringResource(R.string.cancel_button),
                onClick = onDismiss
            )
        }
    )
}

@Preview
@Composable
private fun ExpennyDatePickerPreview() {
    ExpennyThemePreview {
        ExpennyDatePicker(
            currentDate = LocalDate.now(),
            onSelect = {},
            onDismiss = {}
        )
    }
}