package org.expenny.feature.recorddetails.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyButtonSize
import org.expenny.core.ui.foundation.ExpennyButtonStyle
import org.expenny.core.ui.foundation.ExpennyText

@Composable
internal fun RecordDetailsDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = stringResource(R.string.delete_record_question_label))
        },
        content = {
            ExpennyText(
                text = stringResource(R.string.delete_record_paragraph),
                maxLines = Int.MAX_VALUE
            )
        },
        confirmButton = {
            ExpennyButton(
                style = ExpennyButtonStyle.Text,
                size = ExpennyButtonSize.Small,
                onClick = onConfirm,
                label = {
                    ExpennyText(text = stringResource(R.string.delete_button))
                }
            )
        },
        dismissButton = {
            ExpennyButton(
                style = ExpennyButtonStyle.Text,
                size = ExpennyButtonSize.Small,
                onClick = onDismiss,
                label = {
                    ExpennyText(text = stringResource(R.string.cancel_button))
                }
            )
        }
    )
}