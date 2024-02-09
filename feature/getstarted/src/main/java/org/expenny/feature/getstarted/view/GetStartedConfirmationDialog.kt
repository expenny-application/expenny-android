package org.expenny.feature.getstarted.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun GetStartedConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.skip_balance_setup_label))
        },
        content = {
            Text(text = stringResource(R.string.get_started_confirmation_paragraph))
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onConfirm,
                content = {
                    Text(text = stringResource(R.string.continue_button))
                }
            )
        },
        dismissButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.set_balance_button))
                }
            )
        }
    )
}