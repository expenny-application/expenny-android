package org.expenny.feature.passcode.view

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyButtonSize
import org.expenny.core.ui.foundation.ExpennyButtonStyle
import org.expenny.core.ui.foundation.ExpennyText

@Composable
internal fun PasscodeInfoDialog(
    onDismiss: () -> Unit,
) {
    ExpennyAlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null
            )
        },
        title = {
            ExpennyText(text = stringResource(R.string.passcode_setup_label))
        },
        content = {
            ExpennyText(
                text = stringResource(R.string.passcode_setup_paragraph),
                align = TextAlign.Center,
                maxLines = Int.MAX_VALUE
            )
        },
        confirmButton = {
            ExpennyButton(
                style = ExpennyButtonStyle.Text,
                size = ExpennyButtonSize.Small,
                onClick = onDismiss,
                label = {
                    ExpennyText(text = stringResource(R.string.ok_button))
                }
            )
        }
    )
}