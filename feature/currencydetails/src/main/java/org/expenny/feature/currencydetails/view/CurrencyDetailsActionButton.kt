package org.expenny.feature.currencydetails.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyFab

@Composable
internal fun CurrencyDetailsActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ExpennyFab(
        modifier = modifier,
        onClick = onClick,
        icon = {
            FabIcon(painter = painterResource(R.drawable.ic_check))
        },
        label = {
            FabLabel(text = stringResource(R.string.save_button))
        }
    )
}