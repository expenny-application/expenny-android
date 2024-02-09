package org.expenny.feature.currencydetails.view

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyDialog
import org.expenny.core.ui.foundation.ExpennyTextButton

@Composable
internal fun CurrencyDetailsInfoDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    ExpennyDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = null
            )
        },
        title = {
            Text(text = stringResource(R.string.currency_rates_label))
        },
        content = {
            Text(
                text = stringResource(R.string.currency_rates_paragraph),
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