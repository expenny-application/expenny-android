package org.expenny.feature.recorddetails.view

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyDatePicker
import org.expenny.core.ui.components.ExpennyTimePicker
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton
import org.expenny.feature.recorddetails.Action
import org.expenny.feature.recorddetails.State
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun RecordDetailsDialog(
    dialog: State.Dialog?,
    datePickerDate: LocalDate?,
    timePickerDate: LocalTime?,
    onAction: (Action.Dialog) -> Unit
) {
    when (dialog) {
        is State.Dialog.DatePickerDialog -> {
            ExpennyDatePicker(
                currentDate = datePickerDate,
                onSelect = { onAction(Action.Dialog.OnDateChange(it)) },
                onDismiss = { onAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.TimePickerDialog -> {
            ExpennyTimePicker(
                currentTime = timePickerDate,
                onSelect = { onAction(Action.Dialog.OnTimeChange(it)) },
                onDismiss = { onAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteRecordDialog -> {
            DeleteRecordDialog(
                onConfirm = { onAction(Action.Dialog.OnDeleteRecordDialogConfirm) },
                onDismiss = { onAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.ConversionDialog -> {
            ConversionDialog(
                onDismiss = { onAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.TransferDisclaimerDialog -> {
            TransferDisclaimerDialog(
                onDismiss = { onAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.ResetTransferDialog -> {
            ResetTransferDialog(
                onConfirm = { onAction(Action.Dialog.OnResetTransferDialogConfirm) },
                onDismiss = { onAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        is State.Dialog.ReceiptSourceDialog -> {
            ReceiptSourceDialog(
                onGalleryClick = { onAction(Action.Dialog.OnReceiptSourceDialogGallerySelect) },
                onCameraClick = { onAction(Action.Dialog.OnReceiptSourceDialogCameraSelect) },
                onDismiss = { onAction(Action.Dialog.OnDialogDismiss) }
            )
        }
        else -> {}
    }
}

@Composable
private fun ConversionDialog(
    onDismiss: () -> Unit
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null
            )
        },
        title = {
            Text(text = stringResource(R.string.amount_conversion_label))
        },
        content = {
            Text(
                text = stringResource(R.string.amount_conversion_paragraph),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.ok_button))
                }
            )
        }
    )
}

@Composable
private fun DeleteRecordDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.delete_record_question_label))
        },
        content = {
            Text(text = stringResource(R.string.delete_record_paragraph))
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onConfirm,
                content = {
                    Text(text = stringResource(R.string.delete_button))
                }
            )
        },
        dismissButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.cancel_button))
                }
            )
        }
    )
}

@Composable
private fun ReceiptSourceDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.add_receipt_label))
        },
        content = {
            Text(text = stringResource(R.string.image_source_paragraph))
        },
        confirmButton = {
            ExpennyTextButton(onClick = onGalleryClick) {
                Text(text = stringResource(R.string.gallery_button))
            }
        },
        dismissButton = {
            ExpennyTextButton(onClick = onCameraClick) {
                Text(text = stringResource(R.string.camera_button))
            }
        }
    )
}

@Composable
private fun ResetTransferDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.change_record_type_question_label))
        },
        content = {
            Text(text = stringResource(R.string.change_record_type_paragraph))
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onConfirm,
                content = {
                    Text(text = stringResource(R.string.change_button))
                }
            )
        },
        dismissButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.cancel_button))
                }
            )
        }
    )
}

@Composable
private fun TransferDisclaimerDialog(
    onDismiss: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null
            )
        },
        title = {
            Text(text = stringResource(R.string.transfers_label))
        },
        content = {
            Text(
                text = stringResource(R.string.transfer_disclaimer_paragraph),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.ok_button))
                }
            )
        }
    )
}