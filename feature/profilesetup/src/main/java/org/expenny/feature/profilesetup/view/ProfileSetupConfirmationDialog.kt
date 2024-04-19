package org.expenny.feature.profilesetup.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog

@Composable
internal fun ProfileSetupConfirmationDialog(
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
            DialogTitle(text = stringResource(R.string.skip_balance_setup_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.get_started_confirmation_paragraph))
        },
        rightButton = {
            DialogButton(
                onClick = onConfirm,
                label = stringResource(R.string.continue_button)
            )
        },
        leftButton = {
            DialogButton(
                onClick = onDismiss,
                label = stringResource(R.string.set_balance_button)
            )
        }
    )
}