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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyThemePreview
import java.time.LocalTime

@Composable
fun ExpennyTimePicker(
    currentTime: LocalTime?,
    minTime: LocalTime = LocalTime.MIN,
    maxTime: LocalTime = LocalTime.MAX,
    onSelect: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
) {
    var localTime by rememberSaveable { mutableStateOf(currentTime ?: LocalTime.now()) }

    ExpennyDialog(
        modifier = Modifier.wrapContentWidth(),
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.select_time_label))
        },
        body = {
            WheelTimePicker(
                startTime = localTime,
                minTime = minTime,
                maxTime = maxTime,
                size = DpSize(256.dp, 128.dp),
                timeFormat = TimeFormat.HOUR_24,
                textStyle = MaterialTheme.typography.bodyLarge,
                textColor = MaterialTheme.colorScheme.onSurface,
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = null
                ),
                onSnappedTime = {
                    localTime = it
                }
            )
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.select_button),
                onClick = {
                    onSelect(localTime)
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
private fun ExpennyTimePickerPreview() {
    ExpennyThemePreview {
        ExpennyTimePicker(
            currentTime = LocalTime.now(),
            onSelect = {},
            onDismiss = {}
        )
    }
}