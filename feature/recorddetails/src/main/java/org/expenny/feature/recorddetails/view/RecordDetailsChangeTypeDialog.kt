package org.expenny.feature.recorddetails.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType

@Composable
internal fun RecordDetailsChangeTypeDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = stringResource(R.string.change_record_type_question_label))
        },
        content = {
            ExpennyText(
                text = stringResource(R.string.change_record_type_paragraph),
                maxLines = Int.MAX_VALUE
            )
        },
        confirmButton = {
            ExpennyButton(
                onClick = onConfirm,
                attributes = ExpennyFlatButtonAttributes(
                    type = ExpennyFlatButtonType.Tertiary,
                    size = ExpennyFlatButtonSize.Medium,
                    label = stringResource(R.string.change_button)
                )
            )
        },
        dismissButton = {
            ExpennyButton(
                onClick = onDismiss,
                attributes = ExpennyFlatButtonAttributes(
                    type = ExpennyFlatButtonType.Tertiary,
                    size = ExpennyFlatButtonSize.Medium,
                    label = stringResource(R.string.cancel_button)
                )
            )
        }
    )
}