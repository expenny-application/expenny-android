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
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyTheme
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ExpennyDateTimePicker(
    modifier: Modifier = Modifier,
    currentDateTime: LocalDateTime?,
    yearsRange: IntRange? = IntRange(1970, LocalDate.now().year),
    minDateTime: LocalDateTime = LocalDateTime.MIN,
    maxDateTime: LocalDateTime = LocalDateTime.MAX,
    onSelect: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit,
) {
    var localDateTime by rememberSaveable {
        mutableStateOf(currentDateTime ?: LocalDateTime.now())
    }

    ExpennyDialog(
        modifier = modifier.wrapContentWidth(),
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.select_datetime_label))
        },
        body = {
            WheelDateTimePicker(
                startDateTime = localDateTime,
                minDateTime = minDateTime,
                maxDateTime = maxDateTime,
                yearsRange = yearsRange,
                textColor = MaterialTheme.colorScheme.onSurface,
                textStyle = MaterialTheme.typography.bodyLarge,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = null,
                ),
                onSnappedDateTime = {
                    localDateTime = it
                }
            )
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.select_button),
                onClick = {
                    onSelect(localDateTime)
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
private fun ExpennyDateTimePickerPreview() {
    ExpennyTheme {
        ExpennyDateTimePicker(
            currentDateTime = LocalDateTime.now(),
            onSelect = {},
            onDismiss = {}
        )
    }
}