package org.expenny.feature.accountdetails.view

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.ExpennyFloatingActionButton
import org.expenny.core.ui.foundation.ExpennyText

@Composable
internal fun AccountDetailsActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ExpennyFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null
            )
        },
        text = {
            ExpennyText(text = stringResource(R.string.save_button))
        }
    )
}