package org.expenny.feature.records.details.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyDatePicker
import org.expenny.core.ui.components.ExpennyTimePicker
import org.expenny.core.ui.components.ExpennyDialog
import org.expenny.feature.records.details.contract.RecordDetailsAction
import org.expenny.feature.records.details.contract.RecordDetailsState
import java.time.LocalDate
import java.time.LocalTime

@Composable
internal fun RecordDetailsDialog(
    dialog: RecordDetailsState.Dialog?,
    datePickerDate: LocalDate?,
    timePickerDate: LocalTime?,
    onAction: (RecordDetailsAction.Dialog) -> Unit
) {
    when (dialog) {
        is RecordDetailsState.Dialog.DatePickerDialog -> {
            ExpennyDatePicker(
                currentDate = datePickerDate,
                onSelect = { onAction(RecordDetailsAction.Dialog.OnDateChange(it)) },
                onDismiss = { onAction(RecordDetailsAction.Dialog.OnDialogDismiss) }
            )
        }
        is RecordDetailsState.Dialog.TimePickerDialog -> {
            ExpennyTimePicker(
                currentTime = timePickerDate,
                onSelect = { onAction(RecordDetailsAction.Dialog.OnTimeChange(it)) },
                onDismiss = { onAction(RecordDetailsAction.Dialog.OnDialogDismiss) }
            )
        }
        is RecordDetailsState.Dialog.DeleteRecordDialog -> {
            DeleteRecordDialog(
                onConfirm = { onAction(RecordDetailsAction.Dialog.OnDeleteRecordDialogConfirm) },
                onDismiss = { onAction(RecordDetailsAction.Dialog.OnDialogDismiss) }
            )
        }
        is RecordDetailsState.Dialog.ConversionDialog -> {
            ConversionDialog(
                onDismiss = { onAction(RecordDetailsAction.Dialog.OnDialogDismiss) }
            )
        }
        is RecordDetailsState.Dialog.TransferDisclaimerDialog -> {
            TransferDisclaimerDialog(
                onDismiss = { onAction(RecordDetailsAction.Dialog.OnDialogDismiss) }
            )
        }
        is RecordDetailsState.Dialog.ResetTransferDialog -> {
            ResetTransferDialog(
                onConfirm = { onAction(RecordDetailsAction.Dialog.OnResetTransferDialogConfirm) },
                onDismiss = { onAction(RecordDetailsAction.Dialog.OnDialogDismiss) }
            )
        }
        is RecordDetailsState.Dialog.ReceiptSourceDialog -> {
            ReceiptSourceDialog(
                onGalleryClick = { onAction(RecordDetailsAction.Dialog.OnReceiptSourceDialogGallerySelect) },
                onCameraClick = { onAction(RecordDetailsAction.Dialog.OnReceiptSourceDialogCameraSelect) },
                onDismiss = { onAction(RecordDetailsAction.Dialog.OnDialogDismiss) }
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
            DialogIcon(painter = painterResource(R.drawable.ic_info))
        },
        title = {
            DialogTitle(text = stringResource(R.string.amount_conversion_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.amount_conversion_paragraph))
        },
        rightButton = {
            DialogButton(
                onClick = onDismiss,
                label = stringResource(R.string.ok_button)
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
            DialogTitle(text = stringResource(R.string.delete_record_question_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.delete_record_paragraph))
        },
        rightButton = {
            DialogButton(
                onClick = onConfirm,
                label = stringResource(R.string.delete_button)
            )
        },
        leftButton = {
            DialogButton(
                onClick = onDismiss,
                label = stringResource(R.string.cancel_button)
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
            DialogTitle(text = stringResource(R.string.add_receipt_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.image_source_paragraph))
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.gallery_button),
                onClick = onGalleryClick
            )
        },
        leftButton = {
            DialogButton(
                label = stringResource(R.string.camera_button),
                onClick = onCameraClick
            )
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
            DialogTitle(text = stringResource(R.string.change_record_type_question_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.change_record_type_paragraph))
        },
        rightButton = {
            DialogButton(
                onClick = onConfirm,
                label = stringResource(R.string.change_button)
            )
        },
        leftButton = {
            DialogButton(
                onClick = onDismiss,
                label = stringResource(R.string.cancel_button)
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
            DialogIcon(painter = painterResource(R.drawable.ic_info))
        },
        title = {
            DialogTitle(text = stringResource(R.string.transfers_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.transfer_disclaimer_paragraph))
        },
        rightButton = {
            DialogButton(
                onClick = onDismiss,
                label = stringResource(R.string.ok_button),
            )
        }
    )
}