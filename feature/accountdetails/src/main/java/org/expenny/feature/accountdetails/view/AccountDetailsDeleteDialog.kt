package org.expenny.feature.accountdetails.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun AccountDetailsDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.delete_account_question_label))
        },
        content = {
            Text(text = stringResource(R.string.delete_associated_data_paragraph))
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