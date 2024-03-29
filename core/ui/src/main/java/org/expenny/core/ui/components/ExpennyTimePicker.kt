package org.expenny.core.ui.components

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton
import org.expenny.core.ui.theme.ExpennyTheme
import java.time.LocalTime


@Composable
fun ExpennyTimePicker(
    currentTime: LocalTime?,
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    onSelect: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
) {
    var time: LocalTime by rememberSaveable { mutableStateOf(currentTime ?: LocalTime.now()) }

    ExpennyDialog(
        modifier = Modifier.wrapContentWidth(),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.select_time_label),
                style = MaterialTheme.typography.titleLarge
            )
        },
        content = {
            WheelTimePicker(
                startTime = time,
                minTime = minTime,
                maxTime = maxTime,
                size = DpSize(256.dp, 128.dp),
                timeFormat = TimeFormat.HOUR_24,
                textStyle = MaterialTheme.typography.bodyLarge,
                textColor = MaterialTheme.colorScheme.onSurface,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    border = null
                ),
                onSnappedTime = {
                    time = it
                }
            )
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = {
                    onSelect(time)
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.select_button))
            }
        },
        dismissButton = {
            ExpennyTextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel_button))
            }
        }
    )
}

@Preview
@Composable
private fun ExpennyDatePickerPreview() {
    ExpennyTheme {
        ExpennyTimePicker(
            currentTime = LocalTime.now(),
            onSelect = {},
            onDismiss = {}
        )
    }
}