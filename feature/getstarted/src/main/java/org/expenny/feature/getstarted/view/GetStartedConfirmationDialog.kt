package org.expenny.feature.getstarted.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType

@Composable
internal fun GetStartedConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyAlertDialog(
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        onDismissRequest = onDismiss,
        title = {
            ExpennyText(text = stringResource(R.string.skip_balance_setup_label))
        },
        content = {
            ExpennyText(
                text = stringResource(R.string.get_started_confirmation_paragraph),
                maxLines = Int.MAX_VALUE
            )
        },
        confirmButton = {
            ExpennyButton(
                onClick = onConfirm,
                attributes = ExpennyFlatButtonAttributes(
                    type = ExpennyFlatButtonType.Tertiary,
                    size = ExpennyFlatButtonSize.Medium,
                    label = stringResource(R.string.continue_button)
                )
            )
        },
        dismissButton = {
            ExpennyButton(
                onClick = onDismiss,
                attributes = ExpennyFlatButtonAttributes(
                    type = ExpennyFlatButtonType.Tertiary,
                    size = ExpennyFlatButtonSize.Medium,
                    label = stringResource(R.string.set_balance_button)
                )
            )
        }
    )
}