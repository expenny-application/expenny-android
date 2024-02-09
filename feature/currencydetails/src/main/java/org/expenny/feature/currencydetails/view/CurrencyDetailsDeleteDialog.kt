package org.expenny.feature.currencydetails.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun CurrencyDetailsDeleteDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ExpennyDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.delete_currency_question_label))
        },
        content = {
            Text(text = stringResource(R.string.delete_associated_data_paragraph))
        },
        confirmButton = {
            ExpennyTextButton(
                onClick = onConfirm,
                content = {
                    Text(text = stringResource(R.string.delete_button))
                }
            )
        },
        dismissButton = {
            ExpennyTextButton(
                onClick = onDismiss,
                content = {
                    Text(text = stringResource(R.string.cancel_button))
                }
            )
        }
    )
}