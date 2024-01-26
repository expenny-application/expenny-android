package org.expenny.feature.recorddetails.view

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyAlertDialog
import org.expenny.core.ui.foundation.ExpennyButton
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType

@Composable
internal fun RecordDetailsAmountConversionDialog(
    onDismiss: () -> Unit
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
            ExpennyText(text = stringResource(R.string.amount_conversion_label))
        },
        content = {
            ExpennyText(
                text = stringResource(R.string.amount_conversion_paragraph),
                align = TextAlign.Center,
                maxLines = Int.MAX_VALUE
            )
        },
        confirmButton = {
            ExpennyButton(
                onClick = onDismiss,
                attributes = ExpennyFlatButtonAttributes(
                    type = ExpennyFlatButtonType.Tertiary,
                    size = ExpennyFlatButtonSize.Medium,
                    label = stringResource(R.string.ok_button)
                )
            )
        }
    )
}