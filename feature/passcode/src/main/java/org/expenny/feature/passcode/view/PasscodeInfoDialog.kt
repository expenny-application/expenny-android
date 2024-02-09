package org.expenny.feature.passcode.view

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun PasscodeInfoDialog(
    onDismiss: () -> Unit,
) {
    ExpennyDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null
            )
        },
        title = {
            Text(text = stringResource(R.string.passcode_setup_label))
        },
        content = {
            Text(
                text = stringResource(R.string.passcode_setup_paragraph),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.ok_button))
                }
            )
        }
    )
}