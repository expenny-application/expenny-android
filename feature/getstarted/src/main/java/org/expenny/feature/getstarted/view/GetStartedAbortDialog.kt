package org.expenny.feature.getstarted.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun GetStartedAbortDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.abort_setup_label))
        },
        content = {
            Text(text = stringResource(R.string.get_started_abort_paragraph))
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onConfirm,
                content = {
                    Text(text = stringResource(R.string.abort_button))
                }
            )
        },
        dismissButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.continue_button))
                }
            )
        }
    )
}