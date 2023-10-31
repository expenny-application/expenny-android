package org.expenny.feature.accountdetails.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyButtonStyle
import org.expenny.core.ui.foundation.ExpennyText

@Composable
internal fun AccountDetailsDiscardDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = stringResource(R.string.discard_changes_label))
        },
        content = {
            ExpennyText(
                text = stringResource(R.string.discard_changes_paragraph),
                maxLines = Int.MAX_VALUE
            )
        },
        confirmButton = {
            ExpennyButton(
                style = ExpennyButtonStyle.Text,
                onClick = onConfirm,
                label = {
                    ExpennyText(text = stringResource(R.string.discard_button))
                }
            )
        },
        dismissButton = {
            ExpennyButton(
                style = ExpennyButtonStyle.Text,
                onClick = onDismiss,
                label = {
                    ExpennyText(text = stringResource(R.string.continue_button))
                }
            )
        }
    )
}