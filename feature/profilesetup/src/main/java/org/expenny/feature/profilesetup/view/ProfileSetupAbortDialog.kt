package org.expenny.feature.profilesetup.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog

@Composable
internal fun ProfileSetupAbortDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        title = {
            DialogTitle(text = stringResource(R.string.abort_setup_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.get_started_abort_paragraph))
        },
        rightButton = {
            DialogButton(
                onClick = onConfirm,
                label = stringResource(R.string.abort_button)
            )
        },
        leftButton = {
            DialogButton(
                onClick = onDismiss,
                label = stringResource(R.string.continue_button)
            )
        }
    )
}