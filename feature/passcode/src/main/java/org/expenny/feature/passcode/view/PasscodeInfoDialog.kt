package org.expenny.feature.passcode.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyDialog

@Composable
internal fun PasscodeInfoDialog(
    onDismiss: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        icon = {
            DialogIcon(painter = painterResource(R.drawable.ic_info))
        },
        title = {
            DialogTitle(text = stringResource(R.string.passcode_setup_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.passcode_setup_paragraph))
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.ok_button),
                onClick = onDismiss
            )
        }
    )
}