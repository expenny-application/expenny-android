package org.expenny.feature.currencydetails.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyDialog

@Composable
internal fun CurrencyDetailsInfoDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    ExpennyDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = {
            DialogIcon(painter = painterResource(R.drawable.ic_info))
        },
        title = {
            DialogTitle(text = stringResource(R.string.currency_rates_label))
        },
        body = {
            DialogBody(text = stringResource(R.string.currency_rates_paragraph))
        },
        rightButton = {
            DialogButton(
                label = stringResource(R.string.ok_button),
                onClick = onDismiss
            )
        }
    )
}